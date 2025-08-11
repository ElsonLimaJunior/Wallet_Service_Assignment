package com.elson.wallet.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object for a fund transfer request.
 * Contains the destination wallet ID and the amount to transfer.
 */
public final class TransferRequestDto {

    @NotNull(message = "Destination wallet ID cannot be null")
    private final UUID toWalletId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private final BigDecimal amount;

    /**
     * Constructor for TransferRequestDto.
     * @param toWalletId The ID of the wallet to transfer funds to.
     * @param amount The amount to transfer.
     */
    public TransferRequestDto(UUID toWalletId, BigDecimal amount) {
        this.toWalletId = toWalletId;
        this.amount = amount;
    }

    /**
     * Getter for the destination wallet ID.
     * @return The UUID of the destination wallet.
     */
    public UUID getToWalletId() {
        return toWalletId;
    }

    /**
     * Getter for the amount to transfer.
     * @return The amount to transfer.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferRequestDto that = (TransferRequestDto) o;
        return Objects.equals(toWalletId, that.toWalletId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toWalletId, amount);
    }

    @Override
    public String toString() {
        return "TransferRequestDto{" +
                "toWalletId=" + toWalletId +
                ", amount=" + amount +
                '}';
    }
}