package br.com.contafacil.bonnarotec.registry.dto.user;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String apiKey,
        UserRole role,
        UUID clientId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse fromEntity(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getApiKey(),
                entity.getRole(),
                entity.getClient().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
