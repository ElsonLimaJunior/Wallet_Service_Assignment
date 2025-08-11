package com.elson.wallet.application.port.in;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface GetBalanceUseCase {
    BigDecimal getBalance(UUID walletId);
    BigDecimal getHistoricalBalance(UUID walletId, Instant timestamp);
}