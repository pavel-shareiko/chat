package by.shareiko.chat.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;


@Getter
@Data
public class RegisterUser {
    @NotEmpty(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z\\d]{5,}$", message = "Username must be at least 5 characters and must not contain spaces")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^\\S{8,}$", message = "Username must be at least 5 characters and must not contain spaces")
    @Size(min = 8)
    private String password;

    @NotEmpty(message = "First name is required")
    @Size(min = 2, max = 35, message = "First name must be between 2 and 35 characters")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(min = 2, max = 35, message = "Last name must be between 2 and 35 characters")
    private String lastName;
}
