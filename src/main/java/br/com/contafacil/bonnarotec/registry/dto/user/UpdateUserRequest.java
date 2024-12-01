package br.com.contafacil.bonnarotec.registry.dto.user;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank(message = "Username is required")
        String username,
        
        String password,
        
        @NotNull(message = "Role is required")
        UserRole role
) {}
