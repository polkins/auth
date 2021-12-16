package ru.nonsense.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.nonsense.auth.domain.entity.client.Client;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTUtil {
    private static final String SECRET_KEY = "mysecretkey";

    public String createJWT(String user, List<Client> clientList) {
        SignatureAlgorithm signatureAlgoritm = SignatureAlgorithm.HS256;

        Map<String, Object> customClaims = Map.of(
                "user", user,
                // add authorites
                "clients", clientList.stream().map(Client::getId).collect(Collectors.toList())
        );

        JwtBuilder builder = Jwts.builder()
                .setExpiration(Date.from(ZonedDateTime.now().plusHours(1).toInstant()))
                .setId("1")
                .setSubject("TestJWT")
                .setIssuer("?")
                .setClaims(customClaims);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgoritm.getJcaName());


        return builder.signWith(signatureAlgoritm, signingKey).compact();
    }

    public String extractJWTFromAuthorizationHeader(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer")) {
            log.info("Authorization Header not found");
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
