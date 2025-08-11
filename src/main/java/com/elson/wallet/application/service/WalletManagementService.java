package com.elson.wallet.application.service;

import com.elson.wallet.application.port.in.GetBalanceUseCase;
import com.elson.wallet.application.port.in.WalletOperationUseCase;
import com.elson.wallet.application.port.out.TransactionRepositoryPort;
import com.elson.wallet.application.port.out.WalletRepositoryPort;
import com.elson.wallet.domain.exception.InsufficientFundsException;
import com.elson.wallet.domain.exception.InvalidAmountException;
import com.elson.wallet.domain.exception.TransferToSameWalletException;
import com.elson.wallet.domain.exception.WalletNotFoundException;
import com.elson.wallet.domain.model.Transaction;
import com.elson.wallet.domain.model.TransactionType;
import com.elson.wallet.domain.model.Wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// @Service
// @Transactional 

public class WalletManagementService implements GetBalanceUseCase, WalletOperationUseCase {

    private final WalletRepositoryPort walletRepositoryPort;
    private final TransactionRepositoryPort transactionRepositoryPort;

    // Injecao de dependencia via construtor
    public WalletManagementService(WalletRepositoryPort walletRepositoryPort, TransactionRepositoryPort transactionRepositoryPort) {
        this.walletRepositoryPort = walletRepositoryPort;
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    public BigDecimal getBalance(UUID walletId) {
        findWalletOrThrow(walletId); 
        return transactionRepositoryPort.getSumAmountByWalletId(walletId);
    }

    @Override
    public BigDecimal getHistoricalBalance(UUID walletId, Instant timestamp) {
        findWalletOrThrow(walletId); 
        return transactionRepositoryPort.getSumAmountByWalletIdAndTimestamp(walletId, timestamp);
    }

    @Override
    public void deposit(UUID walletId, BigDecimal amount) {
        validateAmount(amount);
        findWalletOrThrow(walletId);

        Transaction depositTransaction = new Transaction(
                UUID.randomUUID(),
                walletId,
                TransactionType.DEPOSIT,
                amount,
                Instant.now(),
                null 
        );
        transactionRepositoryPort.save(depositTransaction);
    }

    @Override
    public void withdraw(UUID walletId, BigDecimal amount) {
        validateAmount(amount);
        findWalletOrThrow(walletId);
        
        BigDecimal currentBalance = getBalance(walletId);
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal. Current balance: " + currentBalance);
        }

        Transaction withdrawalTransaction = new Transaction(
                UUID.randomUUID(),
                walletId,
                TransactionType.WITHDRAWAL,
                amount,
                Instant.now(),
                null
        );
        transactionRepositoryPort.save(withdrawalTransaction);
    }

    @Override
    public void transfer(TransferCommand command) {
        validateAmount(command.getAmount());
        
        if (command.getSourceWalletId().equals(command.getDestinationWalletId())) {
            throw new TransferToSameWalletException("Source and destination wallets cannot be the same.");
        }

        // Para garantir que as duas carteiras existem antes de iniciar uma transacao
        findWalletOrThrow(command.getSourceWalletId());
        findWalletOrThrow(command.getDestinationWalletId());
        
        BigDecimal sourceBalance = getBalance(command.getSourceWalletId());
        if (sourceBalance.compareTo(command.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for transfer. Current balance: " + sourceBalance);
        }

        UUID correlationId = UUID.randomUUID();
        Instant transactionTime = Instant.now();

        Transaction transferOut = new Transaction(
                UUID.randomUUID(),
                command.getSourceWalletId(),
                TransactionType.TRANSFER_OUT,
                command.getAmount(),
                transactionTime,
                correlationId
        );

        Transaction transferIn = new Transaction(
                UUID.randomUUID(),
                command.getDestinationWalletId(),
                TransactionType.TRANSFER_IN,
                command.getAmount(),
                transactionTime,
                correlationId
        );

        // Para justificar o uso do @transactional, ou ambas as transacoes sao salvas ou nenhuma é
        transactionRepositoryPort.save(transferOut);
        transactionRepositoryPort.save(transferIn);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Transaction amount must be positive.");
        }
    }

    private Wallet findWalletOrThrow(UUID walletId) {
        return walletRepositoryPort.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + walletId));
    }
}