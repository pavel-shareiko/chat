package by.shareiko.chat.audit;

import by.shareiko.chat.domain.User;
import by.shareiko.chat.security.SecurityUtils;
import by.shareiko.chat.security.user.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditorAware implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        Optional<UserPrincipal> currentUser = SecurityUtils.getCurrentUser();
        if (currentUser.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(currentUser.get().getDomainUser());
    }
}
