package com.elson.wallet.infra.adapter.out.persistence.adapter;

import com.elson.wallet.application.port.out.UserRepositoryPort;
import com.elson.wallet.domain.model.User;
import com.elson.wallet.infra.adapter.out.persistence.entity.UserEntity;
import com.elson.wallet.infra.adapter.out.persistence.entity.WalletEntity;
import com.elson.wallet.infra.adapter.out.persistence.mapper.UserMapper;
import com.elson.wallet.infra.adapter.out.persistence.mapper.WalletMapper;
import com.elson.wallet.infra.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper, WalletMapper walletMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
        this.walletMapper = walletMapper;
    }

    @Override
    @Transactional // Garante que a criação do usuário e da carteira seja atômica.
    public User save(User user) {
        // 1. Mapeia o usuário do domínio para a entidade.
        UserEntity userEntity = userMapper.toEntity(user);
        
        // 2. Mapeia a carteira do domínio para a entidade, passando a userEntity para a ligação.
        WalletEntity walletEntity = walletMapper.toEntity(user.getWallet(), userEntity);

        // 3. Liga as duas entidades na direção inversa para manter o grafo de objetos consistente.
        userEntity.setWallet(walletEntity);

        // 4. Salva a entidade UserEntity. O CascadeType.ALL cuidará de salvar a WalletEntity junto.
        UserEntity savedUserEntity = userJpaRepository.save(userEntity);
        
        // 5. Converte a entidade salva de volta para o domínio para retornar ao serviço.
        return userMapper.toDomain(savedUserEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }
}