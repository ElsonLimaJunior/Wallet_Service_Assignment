package com.elson.wallet.infrastructure.adapter.out.persistence.repository;

import com.elson.wallet.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositório Spring Data JPA para a entidade UserEntity.
 * Fornece métodos CRUD (Create, Read, Update, Delete) e a capacidade
 * de criar queries customizadas.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Encontra um usuário pelo seu endereço de email.
     * O Spring Data JPA implementará este método automaticamente com base em seu nome.
     * É crucial para a funcionalidade de login.
     *
     * @param email O email do usuário a ser buscado.
     * @return um Optional contendo o UserEntity se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<UserEntity> findByEmail(String email);

}