package com.elson.wallet.infra.adapter.out.persistence.mapper;

import com.elson.wallet.domain.model.User;
import com.elson.wallet.infra.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para converter entre o modelo de domínio User e a entidade de persistência UserEntity.
 */
@Component
public class UserMapper {

    /**
     * Converte um objeto de domínio User para uma UserEntity.
     * Note que a relação com a WalletEntity não é mapeada aqui para evitar ciclos.
     * A ligação entre as entidades será feita na camada de Adapter.
     *
     * @param user O objeto de domínio.
     * @return A entidade de persistência.
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    /**
     * Converte uma UserEntity para um objeto de domínio User.
     * A relação com a Wallet também é ignorada aqui para evitar o carregamento
     * desnecessário e possíveis loops.
     *
     * @param entity A entidade de persistência.
     * @return O objeto de domínio.
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                // Importante: NUNCA expor a senha hasheada de volta para o domínio/API.
                // Passamos null ou um valor vazio.
                null
        );
    }
}