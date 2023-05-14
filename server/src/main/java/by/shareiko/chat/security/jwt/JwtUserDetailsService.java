package by.shareiko.chat.security.jwt;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.dto.user.UserPrincipal;
import by.shareiko.chat.repository.UserRepository;
import by.shareiko.chat.security.exceptions.UserDeactivatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
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
