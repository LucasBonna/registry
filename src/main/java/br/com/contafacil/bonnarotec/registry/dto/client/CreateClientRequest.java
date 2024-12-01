package br.com.contafacil.bonnarotec.registry.dto.client;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.client.ClientRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClientRequest(
        @NotBlank(message = "Name is required")
        String name,
        
        @NotBlank(message = "CNPJ is required")
        String cnpj,
        
        @NotNull(message = "Role is required")
        ClientRole role
) {}
