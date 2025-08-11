package com.elson.wallet.infrastructure.adapter.in.web;

import com.elson.wallet.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratamento para exceções de validação de DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extrai os erros de validação e formata uma resposta clara
        Map<String, String> errors = ...;
        return new ResponseEntity<>(createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", errors), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Object> handleWalletNotFound(WalletNotFoundException ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InsufficientFundsException.class, InvalidAmountException.class, TransferToSameWalletException.class})
    public ResponseEntity<Object> handleBusinessRuleExceptions(RuntimeException ex) {
        // Erros de regras de negócio são melhor representados por 422 Unprocessable Entity
        return new ResponseEntity<>(createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    // Um handler genérico para qualquer outra exceção inesperada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return new ResponseEntity<>(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorResponse(HttpStatus status, String message, Object details) {
        // Cria um corpo de erro padronizado
        return Map.of(
            "timestamp", Instant.now().toString(),
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", message,
            "details", details
        );
    }
    private Map<String, Object> createErrorResponse(HttpStatus status, String message) {
        return createErrorResponse(status, message, null);
    }
}