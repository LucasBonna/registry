package br.com.contafacil.bonnarotec.registry.dto.user;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        String username,
        
        @NotBlank(message = "Password is required")
        String password,
        
        @NotNull(message = "Role is required")
        UserRole role,
        
        @NotNull(message = "Client ID is required")
        UUID clientId
) {}
