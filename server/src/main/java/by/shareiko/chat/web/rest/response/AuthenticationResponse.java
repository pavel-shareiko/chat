package by.shareiko.chat.web.rest.response;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String username;
    private final String token;

    public AuthenticationResponse(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
