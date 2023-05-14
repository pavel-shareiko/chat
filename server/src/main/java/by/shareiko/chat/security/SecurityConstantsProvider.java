package by.shareiko.chat.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Getter
public class SecurityConstantsProvider {
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration-time}")
    private long tokenValidityInMilliseconds;

    @PostConstruct
    public void init() {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }
}
