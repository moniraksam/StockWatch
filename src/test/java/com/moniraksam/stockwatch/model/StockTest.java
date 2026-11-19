
package com.moniraksam.stockwatch.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock("TEST", new BigDecimal("200.00"), "TESTCOMPANY");
    }

    // Constructor tests

    @Test
    void shouldConstructCorrectly() {
        assertEquals("TEST", stock.getTicker());
        assertEquals("TESTCOMPANY", stock.getCompanyName());
        assertEquals(0, new BigDecimal("200.00").compareTo(stock.getCurrentPrice()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "\t" })
    void constructorShouldRejectInvalidTicker(String ticker) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Stock(ticker, new BigDecimal("200.00"), "TESTCOMPANY"));

        assertEquals("Ticker cannot be null, empty or blank.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "aapl", "AaPl", "AAPL" })
    void constructorShouldNormalizeTicker(String ticker) {
        Stock stock = new Stock(ticker, new BigDecimal("100.00"), "Apple Inc.");

        assertEquals("AAPL", stock.getTicker());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "\t" })
    void constructorShouldRejectInvalidCompanyName(String companyName) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Stock("TEST", new BigDecimal("200.00"), companyName));

        assertEquals("Company name cannot be null, empty or blank",
                exception.getMessage());
    }

    @Test
    void constructorShouldPreserveCompanyName() {
        Stock stock = new Stock("AAPL", new BigDecimal("200.00"), "Apple Inc.");

        assertEquals("Apple Inc.", stock.getCompanyName());
    }

    @ParameterizedTest
    @ValueSource(strings = { "-1", "-0.01", "-100.50" })
    void constructorShouldRejectNegativeCurrentPrice(String price) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Stock("TEST", new BigDecimal(price), "TESTCOMPANY"));

        assertEquals("Current price cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldRejectNullCurrentPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stock("TEST", null, "TESTCOMPANY"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "0.00", "0.01", "100.50", "999999.99" })
    void constructorShouldAcceptNonNegativeCurrentPrice(String price) {
        Stock stock = new Stock("TEST", new BigDecimal(price), "TESTCOMPANY");

        assertEquals(0, new BigDecimal(price).compareTo(stock.getCurrentPrice()));
    }

    // Setter tests

    @ParameterizedTest
    @ValueSource(strings = { "0", "0.01", "100.50", "999999.99" })
    void setCurrentPriceShouldAcceptValidPrice(String price) {
        stock.setCurrentPrice(new BigDecimal(price));

        assertEquals(0, new BigDecimal(price).compareTo(stock.getCurrentPrice()));
    }

    @ParameterizedTest
    @ValueSource(strings = { "-1", "-0.01", "-100.50" })
    void setCurrentPriceShouldRejectNegativePrice(String price) {
        assertThrows(IllegalArgumentException.class,
                () -> stock.setCurrentPrice(new BigDecimal(price)));
    }

    @Test
    void setCurrentPriceShouldRejectNull() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.setCurrentPrice(null));
    }

    @Test
    void invalidPriceShouldNotChangeExistingPrice() {
        BigDecimal originalPrice = stock.getCurrentPrice();

        assertThrows(IllegalArgumentException.class,
                () -> stock.setCurrentPrice(new BigDecimal("-10.00")));

        assertEquals(0, originalPrice.compareTo(stock.getCurrentPrice()));
    }

    @Test
    void nullPriceShouldNotChangeExistingPrice() {
        BigDecimal originalPrice = stock.getCurrentPrice();

        assertThrows(IllegalArgumentException.class,
                () -> stock.setCurrentPrice(null));

        assertEquals(0, originalPrice.compareTo(stock.getCurrentPrice()));
    }

    // toString tests

    @ParameterizedTest
    @CsvSource({
            "200.00, 200.00",
            "20.50, 20.50",
            "0.00, 0.00",
            "100.126, 100.13"
    })
    void toStringShouldFormatStockDetails(
            String currentPrice, String expectedPrice) {

        Stock stock = new Stock("aapl", new BigDecimal(currentPrice), "Apple Inc.");

        String expected = String.join(System.lineSeparator(),
                "Stock: AAPL",
                "Company name: Apple Inc.",
                "Current price: " + expectedPrice);

        assertEquals(expected, stock.toString());
    }
}
