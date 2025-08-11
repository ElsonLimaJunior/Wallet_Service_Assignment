package com.elson.wallet.domain.model;

/**
 * Defines the types of financial operations that can be recorded in the wallet ledger.
 */
public enum TransactionType {
    /**
     * Represents a user depositing funds into their wallet. This increases the balance.
     */
    DEPOSIT,
    
    /**
     * Represents a user withdrawing funds from their wallet. This decreases the balance.
     */
    WITHDRAWAL,

    /**
     * Represents a transfer of funds to another wallet. This is the outgoing part of a transfer
     * and decreases the balance of the source wallet.
     */
    TRANSFER_OUT,

    /**
     * Represents receiving funds from another wallet. This is the incoming part of a transfer
     * and increases the balance of the destination wallet.
     */
    TRANSFER_IN
}