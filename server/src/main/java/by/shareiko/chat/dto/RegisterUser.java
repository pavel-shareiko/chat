package by.shareiko.chat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Data
public class RegisterUser {
    @NotEmpty(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z\\d]{5,}$", message = "Username must be at least 5 characters and must not contain spaces")
    private String username;

    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^\\S{8,}$", message = "Username must be at least 5 characters and must not contain spaces")
    private String password;

    @NotEmpty(message = "Email is required")
    private String firstName;

    @NotEmpty(message = "Email is required")
    private String lastName;
}
