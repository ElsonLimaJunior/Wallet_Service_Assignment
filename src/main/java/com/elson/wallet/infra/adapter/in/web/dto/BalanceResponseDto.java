package com.elson.wallet.infra.adapter.in.web.dto;

import java.math.BigDecimal;

public final class BalanceResponseDto {

    private final BigDecimal balance;
    private final String currency; // Boa prática incluir a moeda

    public BalanceResponseDto(BigDecimal balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}