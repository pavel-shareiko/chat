package by.shareiko.chat.dto.user;

import by.shareiko.chat.domain.Role;
import by.shareiko.chat.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal extends org.springframework.security.core.userdetails.User implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public User getDomainUser() {
        return this.user;
    }
}
