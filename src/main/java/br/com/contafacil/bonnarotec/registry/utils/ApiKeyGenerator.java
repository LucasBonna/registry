package br.com.contafacil.bonnarotec.registry.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class ApiKeyGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int API_KEY_LENGTH = 20;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateApiKey() {
        StringBuilder sb = new StringBuilder(API_KEY_LENGTH);
        for (int i = 0; i < API_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
