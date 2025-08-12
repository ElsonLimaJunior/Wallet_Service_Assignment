package com.elson.wallet.infra.adapter.out.persistence.repository;

import com.elson.wallet.infra.adapter.out.persistence.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {

    /**
     * Finds a wallet by its ID and applies a pessimistic write lock on the database row.
     * This prevents other transactions from reading or writing to this row until the current
     * transaction is complete, thus avoiding race conditions.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.id = :walletId")
    Optional<WalletEntity> findByIdWithLock(UUID walletId);

    Optional<WalletEntity> findByUserId(UUID userId);
}