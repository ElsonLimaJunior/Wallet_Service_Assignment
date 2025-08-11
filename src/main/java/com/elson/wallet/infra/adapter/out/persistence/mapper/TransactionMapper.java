package com.elson.wallet.infrastructure.adapter.out.persistence.mapper;

import com.elson.wallet.domain.model.Transaction;
import com.elson.wallet.infrastructure.adapter.out.persistence.entity.TransactionEntity;
// Importaríamos também o TransactionType e TransactionTypeJpa

// Esta classe seria um @Component do Spring para ser injetada
public class TransactionMapper {
    
    public TransactionEntity toEntity(Transaction domain) {
        // Lógica de conversão de Transaction -> TransactionEntity
        // ...
        return new TransactionEntity(...);
    }
    
    public Transaction toDomain(TransactionEntity entity) {
        // Lógica de conversão de TransactionEntity -> Transaction
        // ...
        return new Transaction(...);
    }
}