package com.elson.wallet.application.service;

import com.elson.wallet.application.port.out.TransactionRepositoryPort;
import com.elson.wallet.application.port.out.WalletRepositoryPort;
import com.elson.wallet.domain.exception.InsufficientFundsException;
import com.elson.wallet.domain.model.Transaction;
import com.elson.wallet.domain.model.TransactionType;
import com.elson.wallet.domain.model.Wallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Habilita a integração do Mockito com o JUnit 5
@ExtendWith(MockitoExtension.class)
class WalletManagementServiceTest {

    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @Mock
    private TransactionRepositoryPort transactionRepositoryPort;

    @InjectMocks
    private WalletManagementService walletManagementService;

    @Test
    void deposit_shouldSaveDepositTransaction_whenWalletExists() {
        // Arrange (Preparação)
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        Wallet fakeWallet = new Wallet(walletId, UUID.randomUUID());

        when(walletRepositoryPort.findById(walletId)).thenReturn(Optional.of(fakeWallet));

        walletManagementService.deposit(walletId, amount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepositoryPort, times(1)).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(walletId, savedTransaction.getWalletId());
        assertEquals(TransactionType.DEPOSIT, savedTransaction.getType());
        assertEquals(0, amount.compareTo(savedTransaction.getAmount())); 
    }

    @Test
    void withdraw_shouldThrowInsufficientFundsException_whenBalanceIsTooLow() {

        UUID walletId = UUID.randomUUID();
        BigDecimal amountToWithdraw = new BigDecimal("200.00");
        Wallet fakeWallet = new Wallet(walletId, UUID.randomUUID());

        when(walletRepositoryPort.findByIdWithLock(walletId)).thenReturn(Optional.of(fakeWallet));

        when(transactionRepositoryPort.getSumAmountByWalletId(walletId)).thenReturn(new BigDecimal("100.00"));

        assertThrows(InsufficientFundsException.class, () -> {
            walletManagementService.withdraw(walletId, amountToWithdraw);
        });

        verify(transactionRepositoryPort, never()).save(any(Transaction.class));
    }
}