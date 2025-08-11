package com.elson.wallet.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletOperationUseCase {
    void deposit(UUID walletId, BigDecimal amount);
    void withdraw(UUID walletId, BigDecimal amount);
    void transfer(TransferCommand command);

    // Usamos um objeto "Command" para agrupar os parâmetros de uma transferência
    // Isso torna o código mais limpo e fácil de estender
    final class TransferCommand {
        private final UUID sourceWalletId;
        private final UUID destinationWalletId;
        private final BigDecimal amount;

        public TransferCommand(UUID sourceWalletId, UUID destinationWalletId, BigDecimal amount) {
            this.sourceWalletId = sourceWalletId;
            this.destinationWalletId = destinationWalletId;
            this.amount = amount;
        }

        public UUID getSourceWalletId() { return sourceWalletId; }
        public UUID getDestinationWalletId() { return destinationWalletId; }
        public BigDecimal getAmount() { return amount; }
    }
}