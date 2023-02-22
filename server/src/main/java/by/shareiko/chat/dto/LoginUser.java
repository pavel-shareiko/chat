package by.shareiko.chat.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
public class LoginUser {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
