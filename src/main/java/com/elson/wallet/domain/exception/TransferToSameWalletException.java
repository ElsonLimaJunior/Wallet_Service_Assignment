package com.elson.wallet.domain.exception;

public class TransferToSameWalletException extends RuntimeException {

    public TransferToSameWalletException(String message) {
        super(message);
    }
}
