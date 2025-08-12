package com.elson.wallet.infra.adapter.out.persistence.adapter;

import com.elson.wallet.application.port.out.WalletRepositoryPort;
import com.elson.wallet.domain.model.Wallet;
import com.elson.wallet.infra.adapter.out.persistence.entity.WalletEntity;
import com.elson.wallet.infra.adapter.out.persistence.mapper.WalletMapper;
import com.elson.wallet.infra.adapter.out.persistence.repository.WalletJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter que implementa a porta de repositório da carteira, usando Spring Data JPA.
 * Atua como uma ponte entre a camada de aplicação e a camada de persistência.
 */
@Repository // Anotação que indica que esta é uma implementação de repositório.
public class WalletRepositoryAdapter implements WalletRepositoryPort {

    private final WalletJpaRepository walletJpaRepository;
    private final WalletMapper walletMapper;

    public WalletRepositoryAdapter(WalletJpaRepository walletJpaRepository, WalletMapper walletMapper) {
        this.walletJpaRepository = walletJpaRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public Optional<Wallet> findById(UUID walletId) {
        // Busca a entidade JPA...
        Optional<WalletEntity> entityOptional = walletJpaRepository.findById(walletId);
        // ...e a mapeia para o modelo de domínio antes de retornar.
        return entityOptional.map(walletMapper::toDomain);
    }

    @Override
    public Optional<Wallet> findByUserId(UUID userId) {
        Optional<WalletEntity> entityOptional = walletJpaRepository.findByUserId(userId);
        return entityOptional.map(walletMapper::toDomain);
    }

    public Optional<Wallet> findByIdWithLock(UUID walletId) {
        // Chama o método com lock pessimista definido no JpaRepository.
        Optional<WalletEntity> entityOptional = walletJpaRepository.findByIdWithLock(walletId);
        return entityOptional.map(walletMapper::toDomain);
    }
}