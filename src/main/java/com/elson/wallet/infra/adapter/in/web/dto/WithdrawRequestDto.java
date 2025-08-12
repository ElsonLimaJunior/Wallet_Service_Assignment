package com.elson.wallet.infra.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Data Transfer Object for a withdrawal request.
 * Contains the amount to be withdrawn from the wallet.
 */
public final class WithdrawRequestDto {

    // A anotação @NotNull garante que o campo não seja nulo.
    @NotNull(message = "Amount cannot be null")
    // A anotação @Positive garante que o valor seja maior que zero.
    @Positive(message = "Amount must be positive")
    private final BigDecimal amount;

    /**
     * Constructor for WithdrawRequestDto.
     * @param amount The amount to withdraw.
     */
    public WithdrawRequestDto(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Getter for the amount.
     * @return The amount to withdraw.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawRequestDto that = (WithdrawRequestDto) o;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "WithdrawRequestDto{" +
                "amount=" + amount +
                '}';
    }
}