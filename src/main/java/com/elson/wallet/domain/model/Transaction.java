package com.elson.wallet.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Transaction {

    private final UUID id;
    private final UUID walletId;
    private final TransactionType type;
    private final BigDecimal amount; 
    private final Instant timestamp;
    private final UUID correlationId; 

    public Transaction(UUID id, UUID walletId, TransactionType type, BigDecimal amount, Instant timestamp, UUID correlationId) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive and not null");
        }
        this.id = id;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    
    public UUID getCorrelationId() {
        return correlationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", walletId=" + walletId +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", correlationId=" + correlationId +
                '}';
    }
}