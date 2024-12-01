package br.com.contafacil.bonnarotec.registry.service.implementation;

import br.com.contafacil.bonnarotec.registry.dto.user.CreateUserRequest;
import br.com.contafacil.bonnarotec.registry.dto.user.UpdateUserRequest;
import br.com.contafacil.bonnarotec.registry.dto.user.UserResponse;
import br.com.contafacil.bonnarotec.registry.exception.DuplicateUsernameException;
import br.com.contafacil.bonnarotec.registry.exception.UnauthorizedException;
import br.com.contafacil.bonnarotec.registry.repository.UserRepository;
import br.com.contafacil.bonnarotec.registry.service.interfaces.ClientService;
import br.com.contafacil.bonnarotec.registry.service.interfaces.UserService;
import br.com.contafacil.bonnarotec.registry.utils.ApiKeyGenerator;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.client.ClientEntity;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserDTO;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final ApiKeyGenerator apiKeyGenerator;

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest request, UserDTO currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Only admin users can perform this operation");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new DuplicateUsernameException("Username '" + request.username() + "' is already taken");
        }

        ClientEntity client = clientService.findById(request.clientId());
        
        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setApiKey(apiKeyGenerator.generateApiKey());
        user.setRole(request.role());
        user.setClient(client);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request, UserDTO currentUser) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (!currentUser.getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only modify your own user data");
        }
        
        user.setUsername(request.username());
        user.setRole(request.role());
        
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        
        return UserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public UserResponse findById(UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserEntity findByApiKey(String apiKey) {
        return userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new EntityNotFoundException("User not found with API key: " + apiKey));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Override
    public Page<UserResponse> findAll(Pageable pageable, UUID clientId) {
        if (clientId == null) {
            return userRepository.findAll(pageable)
                .map(UserResponse::fromEntity);
        }
        
        return userRepository.findAllByClientId(clientId, pageable)
            .map(UserResponse::fromEntity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean validatePassword(String username, String rawPassword) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
                
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
