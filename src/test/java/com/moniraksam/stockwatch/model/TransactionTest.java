package com.moniraksam.stockwatch.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

class TransactionTest {
    private Transaction transaction;
    LocalDateTime date = LocalDateTime.of(2026, 11, 18, 14, 30); // To test dates

    @BeforeEach
    void setUp() {
        transaction = new Transaction(date, Transaction.TransactionType.BUY, "TEST",
                new BigDecimal("100.0"), 3);
    }

    @ParameterizedTest
    @EnumSource(Transaction.TransactionType.class)
    void shouldConstructCorrectly(Transaction.TransactionType type) {
        transaction = new Transaction(date, type, "TEST",
                new BigDecimal("100.0"), 3);
        assertEquals(date, transaction.getDateTime());
        assertEquals(type, transaction.getTransactionType());
        assertEquals("TEST", transaction.getTicker());
        assertEquals(new BigDecimal("100.0"), transaction.getPricePerShare());
        assertEquals(3, transaction.getQuantity());
    }

    @Test
    void constructorShouldRejectNullDateTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(null, Transaction.TransactionType.BUY, "TEST",
                        new BigDecimal("100.0"), 3));
        assertEquals("DateTime cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { 1899, 700, 2101, 3000 })
    void constructorShouldRejectInvalidDates(int year) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(LocalDateTime.of(year, 11, 18, 14, 30), Transaction.TransactionType.BUY, "TEST",
                        new BigDecimal("100.0"), 3));
        assertEquals("Year must be between 1900 and 2100.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectNullTransactionType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(date, null, "TEST", new BigDecimal("100.0"), 3));
        assertEquals("Transaction type cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "\t" })
    void constructorShouldRejectInvalidTicker(String ticker) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(date, Transaction.TransactionType.BUY, ticker,
                        new BigDecimal("100.0"), 3));
        assertEquals("Ticker cannot be null, empty or blank.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "aapl", "AaPl", "AAPL" })
    void constructorShouldNormalizeTicker(String ticker) {
        transaction = new Transaction(date, Transaction.TransactionType.BUY, ticker,
                new BigDecimal("100.0"), 3);
        assertEquals(ticker.toUpperCase(Locale.ROOT), transaction.getTicker());

    }

    @Test
    void constructorShouldRejectNullPricePerShare() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(date, Transaction.TransactionType.BUY, "TEST", null, 3));
        assertEquals("Price per share cannot be negative or zero.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "0.0", "-1.0", "-100.0" })
    void constructorShouldRejectZeroOrNegativePricePerShare(String price) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(date, Transaction.TransactionType.SELL, "TEST", new BigDecimal(price), 3));
        assertEquals("Price per share cannot be negative or zero.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0 })
    void constructorShouldRejectNegativeOrZeroQuantity(int quantity) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(date, Transaction.TransactionType.BUY, "TEST", new BigDecimal("100.0"),
                        quantity));
        assertEquals("Quantity must be greater than zero.", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "BUY, 100.00, 3, 300.00",
            "SELL, 150.00, 2, 300.00",
            "BUY, 0.01, 1, 0.01",
            "SELL, 25.50, 4, 102.00",
            "BUY, 99.99, 10, 999.90",
            "SELL, 123.456, 3, 370.368"
    })
    void shouldCalculateTransactionTotal(
            Transaction.TransactionType type,
            String price,
            int quantity,
            String expectedTotal) {

        Transaction transaction = new Transaction(
                date,
                type,
                "TEST",
                new BigDecimal(price),
                quantity);

        BigDecimal actualTotal = transaction.getTransactionTotal();

        assertEquals(0,
                new BigDecimal(expectedTotal).compareTo(actualTotal));
    }

}
