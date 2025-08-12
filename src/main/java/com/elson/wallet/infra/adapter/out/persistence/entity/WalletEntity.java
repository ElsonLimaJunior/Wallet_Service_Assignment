package com.elson.wallet.infra.adapter.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade JPA que representa a carteira de um usuário.
 */
@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    private UUID id;

    // Relação Um-para-Um: Uma carteira pertence a um único usuário.
    // O 'JoinColumn' define a chave estrangeira na tabela 'wallets'.
    // 'unique=true' garante a restrição 1-para-1 no nível do banco de dados.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    // Relação Um-para-Muitos: Uma carteira pode ter muitas transações.
    // 'mappedBy="wallet"' indica que a entidade 'TransactionEntity' é a dona da relação.
    // 'cascade=CascadeType.ALL' significa que as operações (salvar, deletar) na carteira
    // serão propagadas para as transações associadas.
    // 'orphanRemoval=true' garante que uma transação removida da lista seja deletada do banco.
    @OneToMany(
            mappedBy = "wallet",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TransactionEntity> transactions = new ArrayList<>();

    /**
     * Construtor padrão para o JPA.
     */
    public WalletEntity() {
    }

    /**
     * Construtor para facilitar a criação.
     */
    public WalletEntity(UUID id, UserEntity user) {
        this.id = id;
        this.user = user;
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    // Métodos utilitários para gerenciar a relação bidirecional com transações
    public void addTransaction(TransactionEntity transaction) {
        transactions.add(transaction);
        transaction.setWallet(this);
    }

    public void removeTransaction(TransactionEntity transaction) {
        transactions.remove(transaction);
        transaction.setWallet(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletEntity that = (WalletEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "WalletEntity{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : "null") +
                '}';
    }
}