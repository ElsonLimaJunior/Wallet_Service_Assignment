package com.elson.wallet.application.port.out;

import com.elson.wallet.domain.model.Wallet;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {
    Optional<Wallet> findById(UUID walletId);
    Optional<Wallet> findByUserId(UUID userId);
}