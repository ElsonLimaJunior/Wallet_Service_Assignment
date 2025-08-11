package com.elson.wallet.infrastructure.adapter.out.persistence.adapter;

import com.elson.wallet.application.port.out.TransactionRepositoryPort;
import com.elson.wallet.domain.model.Transaction;
import com.elson.wallet.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import com.elson.wallet.infrastructure.adapter.out.persistence.mapper.TransactionMapper;
import com.elson.wallet.infrastructure.adapter.out.persistence.repository.TransactionJpaRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// Esta classe será um @Component do Spring
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionJpaRepository jpaRepository;
    private final TransactionMapper mapper;

    public TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository, TransactionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        TransactionEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public BigDecimal getSumAmountByWalletId(UUID walletId) {
        return jpaRepository.calculateBalance(walletId);
    }

    @Override
    public BigDecimal getSumAmountByWalletIdAndTimestamp(UUID walletId, Instant timestamp) {
        return jpaRepository.calculateBalanceAtTime(walletId, timestamp);
    }
}