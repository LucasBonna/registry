package br.com.contafacil.bonnarotec.registry.service.implementation;

import br.com.contafacil.bonnarotec.registry.dto.client.CreateClientRequest;
import br.com.contafacil.bonnarotec.registry.dto.client.UpdateClientRequest;
import br.com.contafacil.bonnarotec.registry.dto.client.ClientResponse;
import br.com.contafacil.bonnarotec.registry.repository.ClientRepository;
import br.com.contafacil.bonnarotec.registry.service.interfaces.ClientService;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.client.ClientEntity;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientResponse create(CreateClientRequest request) {
        ClientEntity client = new ClientEntity();
        client.setName(request.name());
        client.setCnpj(request.cnpj());
        client.setRole(request.role());
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        
        return ClientResponse.fromEntity(clientRepository.save(client));
    }

    @Override
    @Transactional
    public ClientResponse update(UUID id, UpdateClientRequest request) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        
        client.setName(request.name());
        client.setCnpj(request.cnpj());
        client.setRole(request.role());
        
        return ClientResponse.fromEntity(clientRepository.save(client));
    }

    @Override
    public ClientEntity findById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
    }

    @Override
    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientResponse::fromEntity)
                .toList();
    }

    @Override
    public ClientResponse findByUser(UserEntity user) {
        return clientRepository.findByUsersContaining(user)
                .map(ClientResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Client not found for user: " + user.getUsername()));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}
