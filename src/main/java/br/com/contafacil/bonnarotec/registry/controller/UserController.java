package br.com.contafacil.bonnarotec.registry.controller;

import br.com.contafacil.bonnarotec.registry.dto.user.CreateUserRequest;
import br.com.contafacil.bonnarotec.registry.dto.user.UpdateUserRequest;
import br.com.contafacil.bonnarotec.registry.dto.user.UserResponse;
import br.com.contafacil.bonnarotec.registry.service.interfaces.UserService;
import br.com.contafacil.bonnarotec.registry.utils.JwtUtil;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserDTO;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;
import br.com.contafacil.bonnarotec.registry.dto.auth.AuthRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Rotas de gerenciamento de Users")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Cria um novo usuario",
            description = "Realiza o cadastro de um novo usuario"
    )
    @PostMapping
    public ResponseEntity<UserResponse> create(
        @Valid @RequestBody CreateUserRequest request,
        @Parameter(hidden = true)
        @RequestHeader(value = "X-User") String userJSON
        ) {
        try {
            UserDTO user = objectMapper.readValue(userJSON, UserDTO.class);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.create(request, user));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(
            summary = "Atualiza um usuario",
            description = "Realiza a atualização de um usuario"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateUserRequest request,
        @Parameter(hidden = true)
        @RequestHeader(value = "X-User") String userJSON
        ) {
        try {
            UserDTO user = objectMapper.readValue(userJSON, UserDTO.class);
            return ResponseEntity.ok(userService.update(id, request, user));
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(
            summary = "Consulta um usuario",
            description = "Realiza a consulta de um usuario"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(
        @PathVariable UUID id,
        @Parameter(hidden = true)
        @RequestHeader(value = "X-Client") String clientJSON
        ) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            summary = "Consulta um usuario pelo API Key",
            description = "Realiza a consulta de um usuario pelo API Key"
    )
    @GetMapping("/api-key/{apiKey}")
    public ResponseEntity<UserEntity> findByApiKey(@PathVariable String apiKey) {
        return ResponseEntity.ok(userService.findByApiKey(apiKey));
    }

    @Operation(
            summary = "Consulta um usuario pelo username",
            description = "Realiza a consulta de um usuario pelo username"
    )
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @Operation(
            summary = "Consulta todos os usuarios",
            description = "Realiza a consulta de todos os usuarios"
    )
    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(
            @Parameter(hidden = true)
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(required = false) UUID clientId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(userService.findAll(pageRequest, clientId));
    }

    @Operation(
            summary = "Deleta um usuario",
            description = "Realiza a deleção de um usuario"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticate user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthRequestDTO request) {
        System.out.println("request: " + request);
        try {
            UserEntity user = userService.authenticate(request.getUsername(), request.getPassword());
            String token = jwtUtil.generateToken(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
