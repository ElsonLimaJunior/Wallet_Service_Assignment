package com.yourcompany.wallet.application.service;

import com.yourcompany.wallet.application.port.out.TransactionRepositoryPort;
import com.yourcompany.wallet.application.port.out.WalletRepositoryPort;
import com.yourcompany.wallet.domain.exception.InsufficientFundsException;
import com.yourcompany.wallet.domain.model.Transaction;
import com.yourcompany.wallet.domain.model.TransactionType;
import com.yourcompany.wallet.domain.model.Wallet;
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

    // Cria um mock (simulação) das dependências. Elas não farão operações reais.
    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @Mock
    private TransactionRepositoryPort transactionRepositoryPort;

    // Injeta os mocks criados acima na instância do nosso serviço.
    @InjectMocks
    private WalletManagementService walletManagementService;

    @Test
    void deposit_shouldSaveDepositTransaction_whenWalletExists() {
        // Arrange (Preparação)
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        Wallet fakeWallet = new Wallet(walletId, UUID.randomUUID());

        // Quando o repositório for chamado para buscar a carteira, retornamos nossa carteira falsa.
        when(walletRepositoryPort.findById(walletId)).thenReturn(Optional.of(fakeWallet));

        // Act (Ação)
        walletManagementService.deposit(walletId, amount);

        // Assert (Verificação)
        // ArgumentCaptor nos permite "capturar" o argumento que foi passado para um método mockado.
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        // Verificamos se o método 'save' do repositório de transações foi chamado exatamente 1 vez.
        verify(transactionRepositoryPort, times(1)).save(transactionCaptor.capture());

        // Verificamos as propriedades da transação que foi salva.
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(walletId, savedTransaction.getWalletId());
        assertEquals(TransactionType.DEPOSIT, savedTransaction.getType());
        assertEquals(0, amount.compareTo(savedTransaction.getAmount())); // Compara BigDecimals
    }

    @Test
    void withdraw_shouldThrowInsufficientFundsException_whenBalanceIsTooLow() {
        // Arrange
        UUID walletId = UUID.randomUUID();
        BigDecimal amountToWithdraw = new BigDecimal("200.00");
        Wallet fakeWallet = new Wallet(walletId, UUID.randomUUID());

        when(walletRepositoryPort.findByIdWithLock(walletId)).thenReturn(Optional.of(fakeWallet));
        // Simulamos que o saldo atual da carteira é de apenas 100.
        when(transactionRepositoryPort.getSumAmountByWalletId(walletId)).thenReturn(new BigDecimal("100.00"));

        // Act & Assert
        // Verificamos se a exceção correta é lançada ao tentar executar a operação.
        assertThrows(InsufficientFundsException.class, () -> {
            walletManagementService.withdraw(walletId, amountToWithdraw);
        });

        // Verificamos que, como a operação falhou, nenhuma transação foi salva.
        verify(transactionRepositoryPort, never()).save(any(Transaction.class));
    }
}