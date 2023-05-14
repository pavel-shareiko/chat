package by.shareiko.chat.security.jwt;

import by.shareiko.chat.domain.Role;
import by.shareiko.chat.domain.User;
import by.shareiko.chat.security.SecurityConstantsProvider;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtTokenProvider {
    private final SecurityConstantsProvider securityConstants;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(SecurityConstantsProvider securityConstants, UserDetailsService userDetailsService) {
        this.securityConstants = securityConstants;
        this.userDetailsService = userDetailsService;
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername().toLowerCase(Locale.ENGLISH));
        claims.put("roles", user.getRoles().stream().map(Role::getName).toArray(String[]::new));

        Date now = new Date();
        Date validity = new Date(now.getTime() + securityConstants.getTokenValidityInMilliseconds());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, securityConstants.getSecret())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .setSigningKey(securityConstants.getSecret())
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(securityConstants.getSecret()).parseClaimsJws(token).getBody().getSubject();
    }
}
