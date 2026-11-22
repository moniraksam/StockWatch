package com.moniraksam.stockwatch.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    private Stock stock;
    private Position position;

    @BeforeEach
    void setUp() {
        stock = new Stock("AAPL", new BigDecimal("180.00"), "Apple Inc.");
        position = new Position(stock, 10, new BigDecimal("150.00"));
    }

    // Constructor tests

    @Test
    void shouldConstructPositionCorrectly() {
        assertEquals("AAPL", position.getTicker());
        assertEquals(10, position.getQuantity());
        assertBigDecimalEquals("150.00", position.getAveragePurchasePrice());
    }

    @Test
    void constructorShouldRejectNullStock() {
        assertThrows(IllegalArgumentException.class,
                () -> new Position(null, 10, new BigDecimal("150.00")));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -10 })
    void constructorShouldRejectInvalidQuantity(int quantity) {
        assertThrows(IllegalArgumentException.class,
                () -> new Position(stock, quantity, new BigDecimal("150.00")));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "0", "-1", "-100.50" })
    void constructorShouldRejectInvalidAveragePurchasePrice(String price) {
        BigDecimal value = price == null ? null : new BigDecimal(price);

        assertThrows(IllegalArgumentException.class,
                () -> new Position(stock, 10, value));
    }

    // BUY tests

    @Test
    void shouldIncreaseQuantityAndRecalculateAverageAfterBuy() {
        position.buy(5, new BigDecimal("180.00"));

        assertEquals(15, position.getQuantity());
        assertBigDecimalEquals("160.00", position.getAveragePurchasePrice());
    }

    @ParameterizedTest
    @CsvSource({
            "10, 150.00, 20, 150.00",
            "10, 200.00, 20, 175.00",
            "5, 100.00, 15, 133.33333333",
            "20, 175.00, 30, 166.66666667"
    })
    void shouldCalculateWeightedAverageCorrectly(int boughtQuantity, String boughtPrice,
            int expectedQuantity, String expectedAverage) {

        position.buy(boughtQuantity, new BigDecimal(boughtPrice));

        assertEquals(expectedQuantity, position.getQuantity());
        assertBigDecimalEquals(expectedAverage, position.getAveragePurchasePrice());
    }

    @Test
    void shouldRecalculateAverageAfterMultipleBuys() {
        position.buy(5, new BigDecimal("180.00"));
        position.buy(5, new BigDecimal("200.00"));

        assertEquals(20, position.getQuantity());
        assertBigDecimalEquals("170.00", position.getAveragePurchasePrice());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -10 })
    void buyShouldRejectInvalidQuantity(int quantity) {
        assertThrows(IllegalArgumentException.class,
                () -> position.buy(quantity, new BigDecimal("180.00")));

        assertEquals(10, position.getQuantity());
        assertBigDecimalEquals("150.00", position.getAveragePurchasePrice());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "0", "-1", "-100.50" })
    void buyShouldRejectInvalidPrice(String price) {
        BigDecimal value = price == null ? null : new BigDecimal(price);

        assertThrows(IllegalArgumentException.class,
                () -> position.buy(5, value));

        assertEquals(10, position.getQuantity());
        assertBigDecimalEquals("150.00", position.getAveragePurchasePrice());
    }

    @Test
    void buyShouldRejectIntegerOverflowWithoutChangingPosition() {
        Position largePosition = new Position(stock, Integer.MAX_VALUE,
                new BigDecimal("150.00"));

        assertThrows(ArithmeticException.class,
                () -> largePosition.buy(1, new BigDecimal("180.00")));

        assertEquals(Integer.MAX_VALUE, largePosition.getQuantity());
        assertBigDecimalEquals("150.00", largePosition.getAveragePurchasePrice());
    }

    // SELL tests

    @Test
    void shouldDecreaseQuantityAfterSell() {
        position.sell(5);

        assertEquals(5, position.getQuantity());
    }

    @Test
    void sellShouldNotChangeAveragePurchasePrice() {
        position.sell(5);

        assertBigDecimalEquals("150.00", position.getAveragePurchasePrice());
    }

    @Test
    void shouldAllowSellingAllShares() {
        position.sell(10);

        assertEquals(0, position.getQuantity());
        assertBigDecimalEquals("150.00", position.getAveragePurchasePrice());
        assertBigDecimalEquals("0", position.getMarketValue());
        assertBigDecimalEquals("0", position.getTotalCostBasis());
    }

    @Test
    void shouldRejectSellingMoreThanOwned() {
        assertThrows(IllegalArgumentException.class,
                () -> position.sell(11));

        assertEquals(10, position.getQuantity());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -10 })
    void sellShouldRejectInvalidQuantity(int quantity) {
        assertThrows(IllegalArgumentException.class,
                () -> position.sell(quantity));

        assertEquals(10, position.getQuantity());
    }

    // Financial calculation tests

    @Test
    void shouldCalculateMarketValue() {
        assertBigDecimalEquals("1800.00", position.getMarketValue());
    }

    @Test
    void shouldCalculateTotalCostBasis() {
        assertBigDecimalEquals("1500.00", position.getTotalCostBasis());
    }

    @Test
    void shouldCalculateUnrealizedProfit() {
        assertBigDecimalEquals("300.00", position.getUnrealizedProfitLoss());
    }

    @Test
    void shouldCalculateUnrealizedLoss() {
        stock.setCurrentPrice(new BigDecimal("120.00"));

        assertBigDecimalEquals("-300.00", position.getUnrealizedProfitLoss());
    }

    @Test
    void shouldCalculateZeroProfitLoss() {
        stock.setCurrentPrice(new BigDecimal("150.00"));

        assertBigDecimalEquals("0", position.getUnrealizedProfitLoss());
    }

    @Test
    void shouldUpdateMarketValueWhenStockPriceChanges() {
        stock.setCurrentPrice(new BigDecimal("200.00"));

        assertBigDecimalEquals("2000.00", position.getMarketValue());
        assertBigDecimalEquals("500.00", position.getUnrealizedProfitLoss());
    }

    @Test
    void shouldUpdateCostBasisAndMarketValueAfterBuying() {
        position.buy(5, new BigDecimal("180.00"));

        assertBigDecimalEquals("2400.00", position.getTotalCostBasis());
        assertBigDecimalEquals("2700.00", position.getMarketValue());
        assertBigDecimalEquals("300.00", position.getUnrealizedProfitLoss());
    }

    // Helper to compare BigDecimal numerically, ignoring scale

    private static void assertBigDecimalEquals(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
                "Expected " + expected + " but got " + actual);
    }
}
