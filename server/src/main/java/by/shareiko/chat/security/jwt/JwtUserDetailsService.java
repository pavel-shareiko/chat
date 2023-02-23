package by.shareiko.chat.security.jwt;

import by.shareiko.chat.domain.Role;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.security.UserPrincipal;
import by.shareiko.chat.security.exceptions.UserDeactivatedException;
import by.shareiko.chat.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

@Service
@Log4j2
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    private UserPrincipal createSpringSecurityUser(User user) {
        String lowercaseName = user.getUsername().toLowerCase(Locale.ENGLISH);
        if (!user.isActive()) {
            throw new UserDeactivatedException(lowercaseName);
        }

        return new UserPrincipal(user);
    }
}
