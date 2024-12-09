package br.com.contafacil.bonnarotec.registry.utils;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(UserEntity user) {
        try {
            System.out.println("entrou no generateToken");
            user.setPassword(null);

            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

            Map<String, Object> userClaims = new HashMap<>();
            userClaims.put("id", user.getId());
            userClaims.put("username", user.getUsername());
            userClaims.put("apiKey", user.getApiKey());
            userClaims.put("role", user.getRole());

            if (user.getClient() != null) {
                Map<String, Object> clientMap = new HashMap<>();
                clientMap.put("id", user.getClient().getId());
                clientMap.put("name", user.getClient().getName());
                clientMap.put("role", user.getClient().getRole());

                userClaims.put("client", clientMap);
            } else {
                userClaims.put("client", null);
            }

            userClaims.put("createdAt", user.getCreatedAt().format(formatter));
            userClaims.put("updatedAt", user.getUpdatedAt().format(formatter));
            if (user.getDeletedAt() != null) {
                userClaims.put("deletedAt", user.getDeletedAt().format(formatter));
            }

            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            return Jwts.builder()
                    .subject(user.getUsername())
                    .claim("user", userClaims)
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plus(5, ChronoUnit.HOURS)))
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            throw new RuntimeException("Erro ao processar o usuário para o JWT", e);
        }
    }
}
