package com.moniraksam.stockwatch.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.math.BigDecimal;

// Records an individual purchase or sale
public class Transaction {
    private final LocalDateTime dateTime;
    private final TransactionType type;
    private final String ticker;
    private final BigDecimal pricePerShare;
    private final int quantity;

    // Enum to determine transaction type
    public enum TransactionType {
        BUY,
        SELL
    }

    // Constructor
    public Transaction(LocalDateTime dateTime, TransactionType type,
            String ticker, BigDecimal pricePerShare, int quantity) {
        validateLocalDateTime(dateTime);
        validateType(type);
        validateTicker(ticker);
        validatePricePerShare(pricePerShare);
        validateQuantity(quantity);

        this.dateTime = dateTime;
        this.type = type;
        this.ticker = ticker.toUpperCase(Locale.ROOT);
        this.pricePerShare = pricePerShare;
        this.quantity = quantity;

    }

    // Getters
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public TransactionType getTransactionType() {
        return this.type;
    }

    public String getTicker() {
        return this.ticker;
    }

    public BigDecimal getPricePerShare() {
        return this.pricePerShare;
    }

    public int getQuantity() {
        return this.quantity;
    }

    // Methods
    public BigDecimal getTransactionTotal() {
        return pricePerShare.multiply(BigDecimal.valueOf(quantity));
    }

    // Validators
    private static void validateLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime cannot be null.");
        }

        if (dateTime.getYear() < 1900 || dateTime.getYear() > 2100) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100.");
        }
    }

    private static void validateType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }
    }

    private static void validateTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be null, empty or blank.");
        }

    }

    private static void validatePricePerShare(BigDecimal pricePerShare) {
        if (pricePerShare == null || pricePerShare.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per share cannot be negative or zero.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }

}
