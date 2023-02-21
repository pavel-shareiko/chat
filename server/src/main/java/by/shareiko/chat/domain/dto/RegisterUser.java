package by.shareiko.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterUser {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
