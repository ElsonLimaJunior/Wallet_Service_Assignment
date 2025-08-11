package com.elson.wallet.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidade JPA que representa um usuário do sistema.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Armazenará a senha com hash

    // Relação Um-para-Um. O lado inverso da relação em WalletEntity.
    // 'mappedBy="user"' indica que a configuração da chave estrangeira está na outra entidade.
    // A cascata ALL aqui é muito útil: ao salvar um novo usuário, sua carteira associada
    // também será salva automaticamente.
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private WalletEntity wallet;

    /**
     * Construtor padrão para o JPA.
     */
    public UserEntity() {
    }

    /**
     * Construtor completo para facilitar a criação.
     */
    public UserEntity(UUID id, String fullName, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WalletEntity getWallet() {
        return wallet;
    }

    // Método utilitário para gerenciar a relação bidirecional
    public void setWallet(WalletEntity wallet) {
        if (wallet == null) {
            if (this.wallet != null) {
                this.wallet.setUser(null);
            }
        } else {
            wallet.setUser(this);
        }
        this.wallet = wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}