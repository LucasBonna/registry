package br.com.contafacil.bonnarotec.registry.repository;

import br.com.contafacil.shared.bonnarotec.toolslib.domain.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByApiKey(String apiKey);
    Page<UserEntity> findAllByClientId(UUID clientId, Pageable pageable);
}
