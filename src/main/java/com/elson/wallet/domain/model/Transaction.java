package com.elson.wallet.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Transaction {

    private final UUID id;
    private final UUID walletId;
    private final TransactionType type;
    private final BigDecimal amount; // Always use BigDecimal for monetary values
    private final Instant timestamp;
    private final UUID correlationId; // Used to link TRANSFER_OUT and TRANSFER_IN

    public Transaction(UUID id, UUID walletId, TransactionType type, BigDecimal amount, Instant timestamp, UUID correlationId) {
        // Here we would add validation logic (e.g., amount must be positive)
        // For now, we keep it simple.
        this.id = id;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
    }

    // Getters only to enforce immutability
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