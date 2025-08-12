package com.elson.wallet.infra.adapter.out.persistence.repository;

import com.elson.wallet.infra.adapter.out.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {

    /**
     * Calculates the current balance of a wallet by summing its transactions.
     * Deposits and transfers-in are positive, withdrawals and transfers-out are negative.
     * COALESCE is used to return 0 if there are no transactions, instead of null.
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN t.type IN ('DEPOSIT', 'TRANSFER_IN') THEN t.amount ELSE -t.amount END), 0.00) " +
           "FROM TransactionEntity t WHERE t.walletId = :walletId")
    BigDecimal calculateBalance(@Param("walletId") UUID walletId);

    /**
     * Calculates the historical balance of a wallet at a specific point in time.
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN t.type IN ('DEPOSIT', 'TRANSFER_IN') THEN t.amount ELSE -t.amount END), 0.00) " +
           "FROM TransactionEntity t WHERE t.walletId = :walletId AND t.timestamp <= :timestamp")
    BigDecimal calculateBalanceAtTime(@Param("walletId") UUID walletId, @Param("timestamp") Instant timestamp);
}