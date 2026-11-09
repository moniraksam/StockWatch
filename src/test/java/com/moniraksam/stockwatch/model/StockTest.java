package com.moniraksam.stockwatch.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void constructorShouldRejectNullTicker() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Stock(null, 100.0, 200.0, 3);
        });
    }

    @Test
    void constructorShouldRejectEmptyTicker() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Stock("", 100.0, 200.0, 3);
        });
    }

    @Test
    void constructorShouldRejectBlankTicker() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Stock(" ", 100.0, 200.0, 3);
        });
    }
}