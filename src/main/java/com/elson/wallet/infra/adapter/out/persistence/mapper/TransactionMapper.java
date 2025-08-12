package com.elson.wallet.infra.adapter.out.persistence.mapper;

import com.elson.wallet.domain.model.Transaction;
import com.elson.wallet.infra.adapter.out.persistence.entity.TransactionEntity;
import com.elson.wallet.infra.adapter.out.persistence.entity.WalletEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre o modelo de domínio Transaction e a entidade de persistência TransactionEntity.
 * Este mapper é "burro" por design: ele apenas converte campos, não busca dados.
 */
@Component
public class TransactionMapper {

    /**
     * Converte um objeto de domínio Transaction em uma TransactionEntity de persistência.
     * A entidade da carteira (WalletEntity) deve ser fornecida externamente porque o mapper
     * não tem a responsabilidade (nem o acesso) de buscá-la no banco de dados.
     *
     * @param transaction  O objeto de domínio da transação.
     * @param walletEntity A entidade da carteira à qual esta transação pertence.
     * @return A entidade de transação pronta para ser salva.
     */
    public TransactionEntity toEntity(Transaction transaction, WalletEntity walletEntity) {
        if (transaction == null) {
            return null;
        }
        return new TransactionEntity(
                transaction.getId(),
                walletEntity, // A entidade completa é associada aqui
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getCorrelationId()
        );
    }

    /**
     * Converte uma TransactionEntity da persistência para um objeto de domínio Transaction.
     * Esta conversão é direta, pois a entidade já contém todas as informações necessárias.
     *
     * @param entity A entidade de persistência.
     * @return O objeto de domínio.
     */
    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        // Para o domínio, pegamos apenas o ID da carteira que está dentro da WalletEntity
        return new Transaction(
                entity.getId(),
                entity.getWallet().getId(),
                entity.getType(),
                entity.getAmount(),
                entity.getTimestamp(),
                entity.getCorrelationId()
        );
    }
}