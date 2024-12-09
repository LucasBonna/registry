package br.com.contafacil.bonnarotec.registry.service.interfaces;

import br.com.contafacil.bonnarotec.registry.dto.user.CreateUserRequest;
import br.com.contafacil.bonnarotec.registry.dto.user.UpdateUserRequest;
import br.com.contafacil.bonnarotec.registry.dto.user.UserResponse;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserDTO;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest request, UserDTO user);
    UserResponse update(UUID id, UpdateUserRequest request, UserDTO user);
    UserResponse findById(UUID id);
    UserEntity findByApiKey(String apiKey);
    UserResponse findByUsername(String username);
    Page<UserResponse> findAll(Pageable pageable, UUID clientId);
    void deleteById(UUID id);
    boolean validatePassword(String username, String rawPassword);
    UserEntity authenticate(String username, String password);
}
