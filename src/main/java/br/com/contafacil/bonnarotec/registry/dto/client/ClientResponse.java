package br.com.contafacil.bonnarotec.registry.dto.client;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.client.ClientEntity;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.client.ClientRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        String cnpj,
        ClientRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ClientResponse fromEntity(ClientEntity entity) {
        return new ClientResponse(
                entity.getId(),
                entity.getName(),
                entity.getCnpj(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
