package com.elson.wallet.infra.adapter.out.persistence.entity;

import com.elson.wallet.domain.model.TransactionType; // Podemos referenciar o enum do domínio diretamente
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade JPA que representa uma transação financeira no banco de dados.
 * Esta classe é o modelo de dados da camada de persistência.
 */
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private UUID id;

    // Em vez de apenas o ID, estabelecemos uma relação Muitos-para-Um.
    // Muitas transações pertencem a uma carteira.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é crucial para performance, evita carregar a carteira desnecessariamente.
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletEntity wallet;

    // Armazena o enum como uma String ("DEPOSIT", "WITHDRAWAL") no banco.
    // É mais seguro e legível do que o padrão (ORDINAL), que armazena um número.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    // Define a precisão e a escala para valores monetários, garantindo consistência no banco.
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false)
    private Instant timestamp;

    // Pode ser nulo, pois só é usado para transações de transferência.
    @Column(nullable = true)
    private UUID correlationId;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public TransactionEntity() {
    }

    /**
     * Construtor completo para facilitar a criação de instâncias.
     */
    public TransactionEntity(UUID id, WalletEntity wallet, TransactionType type, BigDecimal amount, Instant timestamp, UUID correlationId) {
        this.id = id;
        this.wallet = wallet;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WalletEntity getWallet() {
        return wallet;
    }

    public void setWallet(WalletEntity wallet) {
        this.wallet = wallet;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * Para entidades JPA, é uma boa prática basear o equals e hashCode apenas no ID (chave primária),
     * pois ele identifica unicamente uma linha no banco de dados.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        // Se o ID for nulo, os objetos não podem ser considerados iguais.
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Usa a classe para o hashcode inicial para evitar colisões entre diferentes tipos de entidade com o mesmo ID.
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", walletId=" + (wallet != null ? wallet.getId() : "null") + // Evita carregar o objeto 'wallet' inteiro
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", correlationId=" + correlationId +
                '}';
    }
}