package com.elson.wallet.application.port.out;

import com.elson.wallet.domain.model.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    BigDecimal getSumAmountByWalletId(UUID walletId);
    BigDecimal getSumAmountByWalletIdAndTimestamp(UUID walletId, Instant timestamp);
}