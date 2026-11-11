package com.moniraksam.stockwatch.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @Test
    void constructorShouldRejectPurchaseNaNPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", Double.NaN, 200.0, 3));
        assertEquals("Purchase price must be higher than zero.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectZeroPurchasePrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 0, 200.0, 3));
        assertEquals("Purchase price must be higher than zero.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectNegativePurchasePrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", -1, 200.0, 3));
        assertEquals("Purchase price must be higher than zero.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectPositiveInfinityPurchasePrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", Double.POSITIVE_INFINITY, 200.0, 3));
        assertEquals("Purchase price must be higher than zero.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectNegativeInfinityPurchasePrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", Double.NEGATIVE_INFINITY, 200.0, 3));
        assertEquals("Purchase price must be higher than zero.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectCurrentNaNPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, Double.NaN, 3));
        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectNegativeCurrentPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, -1, 3));
        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldAcceptZeroCurrentPrice() {
        Stock stock = new Stock("TEST", 100.0, 0, 3);
        assertEquals(0, stock.getCurrentPrice());
    }

    @Test
    void constructorShouldRejectPositiveInfinityCurrentPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, Double.POSITIVE_INFINITY, 3));
        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectNegativeInfinityCurrentPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, Double.NEGATIVE_INFINITY, 3));
        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectZeroQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, 200.0, 0));
        assertEquals("Quantity must be greater than zero.", exception.getMessage());

    }

    @Test
    void constructorShouldRejectNegativeQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", 100.0, 200.0, -1));
        assertEquals("Quantity must be greater than zero.", exception.getMessage());

    }

    // Method Tests
    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock("TEST", 100.0, 200.0, 3);
    }

    @Test
    void testCalculateProfitLossPositive() {
        assertEquals(300, stock.getProfitLoss());
    }

    @Test
    void testCalcualteProfitLossNegative() {
        stock.setCurrentPrice(20);
        assertEquals(-240, stock.getProfitLoss());
    }

}