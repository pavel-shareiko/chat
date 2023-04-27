package by.shareiko.chat.dto;

import by.shareiko.chat.domain.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link by.shareiko.chat.domain.User} entity
 */
@Data
public class UserWithAuthorities implements Serializable {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Set<Role> roles;
}