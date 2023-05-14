package by.shareiko.chat.controller;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.AuthenticationResponse;
import by.shareiko.chat.dto.user.LoginUser;
import by.shareiko.chat.dto.user.RegisterUser;
import by.shareiko.chat.dto.user.UserWithAuthorities;
import by.shareiko.chat.security.jwt.JwtTokenProvider;
import by.shareiko.chat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginUser loginUser) {
        log.debug("REST request to login user: {}", loginUser.getUsername());
        User user = userService.login(loginUser);

        String token = jwtTokenProvider.createToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUser registerUser) {
        log.info("REST request to register user: {}", registerUser);
        User user = userService.register(registerUser);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public UserWithAuthorities getCurrentUser() {
        log.debug("REST request to get current user");
        return userService.getCurrentUserWithAuthorities();
    }
}
