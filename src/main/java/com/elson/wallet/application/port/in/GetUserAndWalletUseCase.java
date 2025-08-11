package com.elson.wallet.application.port.in;

import com.elson.wallet.domain.model.User;
import com.elson.wallet.domain.model.Wallet;
import java.util.UUID;

public interface GetUserAndWalletUseCase {
    User findUserById(UUID userId);
    Wallet findWalletByUserId(UUID userId);
}