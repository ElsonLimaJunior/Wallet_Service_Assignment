package com.elson.wallet.infra.adapter.in.web.controller;

import com.elson.wallet.application.port.in.GetBalanceUseCase;
import com.elson.wallet.application.port.in.WalletOperationUseCase;
import com.elson.wallet.application.port.out.WalletRepositoryPort;
import com.elson.wallet.domain.model.Wallet;
import com.elson.wallet.infra.adapter.in.web.dto.BalanceResponseDto;
import com.elson.wallet.infra.adapter.in.web.dto.DepositRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final GetBalanceUseCase getBalanceUseCase;
    private final WalletOperationUseCase walletOperationUseCase;
    private final WalletRepositoryPort walletRepositoryPort; // Para buscar a carteira do usuário logado

    public WalletController(GetBalanceUseCase getBalanceUseCase, 
                            WalletOperationUseCase walletOperationUseCase, 
                            WalletRepositoryPort walletRepositoryPort) {
        this.getBalanceUseCase = getBalanceUseCase;
        this.walletOperationUseCase = walletOperationUseCase;
        this.walletRepositoryPort = walletRepositoryPort;
    }

    @GetMapping("/me/balance")
    public ResponseEntity<BalanceResponseDto> getCurrentBalance(Authentication authentication) {
        // Extrai o ID do usuário do token JWT processado pelo Spring Security
        UUID userId = UUID.fromString(authentication.getName());
        Wallet wallet = findWalletByUserIdOrThrow(userId);
        
        BigDecimal balance = getBalanceUseCase.getBalance(wallet.getId());
        return ResponseEntity.ok(new BalanceResponseDto(balance, "BRL"));
    }

    @GetMapping("/me/balance/historical")
    public ResponseEntity<BalanceResponseDto> getHistoricalBalance(
            @RequestParam("at_time") Instant atTime,
            Authentication authentication) {
        
        UUID userId = UUID.fromString(authentication.getName());
        Wallet wallet = findWalletByUserIdOrThrow(userId);

        BigDecimal balance = getBalanceUseCase.getHistoricalBalance(wallet.getId(), atTime);
        return ResponseEntity.ok(new BalanceResponseDto(balance, "BRL"));
    }

    @PostMapping("/me/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody DepositRequestDto request, 
                                        Authentication authentication) {
        
        UUID userId = UUID.fromString(authentication.getName());
        Wallet wallet = findWalletByUserIdOrThrow(userId);

        walletOperationUseCase.deposit(wallet.getId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
    
    // Endpoints de withdraw e transfer seguiriam o mesmo padrão...

    private Wallet findWalletByUserIdOrThrow(UUID userId) {
        return walletRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet for user not found. Inconsistency error."));
        // Esta exceção não deveria ocorrer em um sistema consistente
    }
}