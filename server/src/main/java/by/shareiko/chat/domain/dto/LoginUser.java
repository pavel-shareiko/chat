package by.shareiko.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginUser {
    private String username;
    private String password;
}
