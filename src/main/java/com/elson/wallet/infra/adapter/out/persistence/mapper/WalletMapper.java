package com.elson.wallet.infra.adapter.out.persistence.mapper;

import com.elson.wallet.domain.model.Wallet;
import com.elson.wallet.infra.adapter.out.persistence.entity.UserEntity;
import com.elson.wallet.infra.adapter.out.persistence.entity.WalletEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre o modelo de domínio Wallet e a entidade de persistência WalletEntity.
 */
@Component
public class WalletMapper {

    /**
     * Converte um objeto de domínio Wallet para uma WalletEntity.
     * A entidade UserEntity precisa ser passada separadamente, pois o domínio Wallet
     * contém apenas o userId, não o objeto User completo.
     *
     * @param wallet O objeto de domínio Wallet.
     * @param userEntity A UserEntity à qual esta carteira pertence.
     * @return A entidade de persistência WalletEntity.
     */
    public WalletEntity toEntity(Wallet wallet, UserEntity userEntity) {
        if (wallet == null) {
            return null;
        }
        return new WalletEntity(
                wallet.getId(),
                userEntity
        );
    }

    /**
     * Converte uma WalletEntity para um objeto de domínio Wallet.
     *
     * @param entity A entidade de persistência.
     * @return O objeto de domínio.
     */
    public Wallet toDomain(WalletEntity entity) {
        if (entity == null) {
            return null;
        }
        // O domínio Wallet precisa do userId, que obtemos da entidade User aninhada.
        return new Wallet(
                entity.getId(),
                entity.getUser().getId()
        );
    }
}