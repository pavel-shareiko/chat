package by.shareiko.chat.web.rest;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.security.exceptions.JwtAuthenticationException;
import by.shareiko.chat.security.exceptions.UserDeactivatedException;
import by.shareiko.chat.web.rest.response.AuthenticationResponse;
import by.shareiko.chat.dto.LoginUser;
import by.shareiko.chat.dto.RegisterUser;
import by.shareiko.chat.security.jwt.JwtTokenProvider;
import by.shareiko.chat.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginUser loginUser) {
        log.debug("REST request to login user: {}", loginUser.getUsername());
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        if (!user.get().isActive()) {
            throw new UserDeactivatedException("User with username " + username + " is inactive");
        }

        String token = jwtTokenProvider.createToken(user.get());
        AuthenticationResponse response = new AuthenticationResponse(username, token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUser registerUser) {
        log.info("REST request to register user: {}", registerUser);

        if (!userService.isUsernameUnique(registerUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        User user = userService.register(registerUser);
        return ResponseEntity.ok(user);
    }
}
