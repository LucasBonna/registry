package br.com.contafacil.bonnarotec.registry.service.interfaces;

import br.com.contafacil.bonnarotec.registry.dto.client.CreateClientRequest;
import br.com.contafacil.bonnarotec.registry.dto.client.UpdateClientRequest;
import br.com.contafacil.bonnarotec.registry.dto.client.ClientResponse;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.client.ClientEntity;
import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    ClientResponse create(CreateClientRequest request);
    ClientResponse update(UUID id, UpdateClientRequest request);
    ClientEntity findById(UUID id);
    List<ClientResponse> findAll();
    ClientResponse findByUser(UserEntity user);
    void deleteById(UUID id);
}
