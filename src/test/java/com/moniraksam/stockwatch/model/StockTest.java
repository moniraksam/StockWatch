package com.moniraksam.stockwatch.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

class StockTest {

    // Constructor test
    @Test
    void shouldConstructCorrectly() {
        Stock stock = new Stock("TEST", 100.0, 200.0, 3);
        assertEquals("TEST", stock.getTicker());
        assertEquals(100.0, stock.getPurchasePrice());
        assertEquals(200.0, stock.getCurrentPrice());
        assertEquals(3, stock.getQuantity());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   " })
    void constructorShouldRejectInvalidTicker(String ticker) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock(ticker, 100.0, 200.0, 3));
        assertEquals("Ticker cannot be null, empty or blank.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, -1.0
    })
    void constructorShouldRejectInvalidPurchasePrice(double price) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", price, 200.0, 3));
        assertEquals("Purchase price must be higher than zero.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, -1.0 })
    void constructorShouldRejectInvalidCurrentPrice(double price) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, price, 3));
        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldAcceptZeroCurrentPrice() {
        Stock stock = new Stock("TEST", 100.0, 0, 3);
        assertEquals(0, stock.getCurrentPrice());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void constructorShouldRejectInvalidQuantity(int quantity) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, 200.0, quantity));
        assertEquals("Quantity must be greater than zero.", exception.getMessage());
    }

    // Method Tests
    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock("TEST", 100.0, 200.0, 3);
    }

    @ParameterizedTest
    @CsvSource({
            "200.0, 300.0",
            "20.0, -240.0",
            "100.0, 0.0",
            "0.0, -300.0"
    })
    void shouldCalculateProfitLoss(double currentPrice, double expectedProfitLoss) {
        stock.setCurrentPrice(currentPrice);
        assertEquals(expectedProfitLoss, stock.getProfitLoss());
    }

    @ParameterizedTest
    @ValueSource(doubles = { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
            -1.0 })
    void setCurrentPriceShouldRejectInvalidPrice(double price) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> stock.setCurrentPrice(price));
        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void setCurrentPriceShouldAcceptZero() {
        stock.setCurrentPrice(0.0);
        assertEquals(0.0, stock.getCurrentPrice());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0 })
    void setQuantityShouldRejectInvalidAmounts(int quantity) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> stock.setQuantity(quantity));
        assertEquals("Quantity must be greater than zero.", exception.getMessage());
    }

    @ParameterizedTest 
    @CsvSource({
            "1", "1000", "9999"
    })
    void setQuantityShouldAcceptPositives(int quantity) {
        stock.setQuantity(quantity);
        assertEquals(quantity, stock.getQuantity());
    }


}