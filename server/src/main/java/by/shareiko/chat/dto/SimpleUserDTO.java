package by.shareiko.chat.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link by.shareiko.chat.domain.User} entity
 */
@Data
public class SimpleUserDTO implements Serializable {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final boolean isActive;
}