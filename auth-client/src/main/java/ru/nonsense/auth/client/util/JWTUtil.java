package ru.nonsense.auth.client.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;

@Component
public class JWTUtil {
    private static final String SECRET_KEY = "mysecretkey";

    public String extractJWTFromAuthorizationHeader(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer")) {
            return null;
        }

        return authorization.substring(7);
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt) // здесь проверяем что токен валиден
                .getBody();
    }
}
