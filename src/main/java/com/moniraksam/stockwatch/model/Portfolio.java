package com.moniraksam.stockwatch.model;

import java.util.NoSuchElementException;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Locale;

// Holds positions and calculates portfolio-wide performance
public class Portfolio {
    private final Map<String, Position> positions;

    // Constructor
    public Portfolio() {
        positions = new LinkedHashMap<>();
    }

    // Methods
    public void buyStock(Stock stock, int quantity, BigDecimal pricePerShare) {
        validateStock(stock);
        validateQuantity(quantity);
        validateAveragePurchasePrice(pricePerShare);

        String ticker = stock.getTicker();

        if (positions.containsKey(ticker)) {
            positions.get(ticker).buy(quantity, pricePerShare);
        } else {
            positions.put(ticker, new Position(stock, quantity, pricePerShare));
        }

    }

    public void sellStock(String ticker, int quantity) {
        validateTicker(ticker);
        ticker = ticker.toUpperCase(Locale.ROOT);
        validateQuantity(quantity);

        if (positions.containsKey(ticker)) {
            positions.get(ticker).sell(quantity);
        } else {
            throw new NoSuchElementException("Cannot sell a stock not in the portfolio.");
        }

        if (positions.get(ticker).getQuantity() == 0) {
            positions.remove(ticker);
        }

    }

    public void updateStockPrice(String ticker, BigDecimal newPrice) {
        validateTicker(ticker);
        ticker = ticker.toUpperCase(Locale.ROOT);
        validateNewPrice(newPrice);

        Position position = positions.get(ticker);

        if (position == null) {
            throw new NoSuchElementException(
                    "No matching ticker found in the portfolio.");
        }

        position.getStock().setCurrentPrice(newPrice);
    }

    // Get-type methods

    public BigDecimal getCurrentMarketValue() {
        BigDecimal total = BigDecimal.ZERO;

        for (Position position : positions.values()) {
            total = total.add(position.getMarketValue());
        }

        return total;
    }

    public BigDecimal getTotalCostBasis() {
        BigDecimal total = BigDecimal.ZERO;

        for (Position position : positions.values()) {
            total = total.add(position.getTotalCostBasis());
        }

        return total;

    }

    public BigDecimal getProfitLoss() {
        return getCurrentMarketValue().subtract(getTotalCostBasis());
    }

    public Position getMostProfitableStockPosition() {
        Position mostProfitable = null;

        for (Position position : positions.values()) {
            if (mostProfitable == null ||
                    position.getUnrealizedProfitLoss()
                            .compareTo(mostProfitable.getUnrealizedProfitLoss()) > 0) {
                mostProfitable = position;
            }
        }

        return mostProfitable;
    }

    public BigDecimal getPortfolioPercentReturn() {
        BigDecimal invested = getTotalCostBasis();

        if (invested.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException(
                    "Cannot calculate return on an empty investment.");
        }

        BigDecimal div = getProfitLoss().divide(invested, 8, RoundingMode.HALF_UP);
        return div.multiply(BigDecimal.valueOf(100));
    }

    // Helper methods
    private static void validateTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be null, empty or blank.");
        }

    }

    private static void validateStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0.");
    }

    private static void validateAveragePurchasePrice(BigDecimal averagePurchasePrice) {
        if (averagePurchasePrice == null ||
                averagePurchasePrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Average purchase price cannot be negative or zero.");
    }

    private static void validateNewPrice(BigDecimal newPrice) {
        if (newPrice == null ||
                newPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("New price cannot be negative.");
    }

    public boolean containsTicker(String tickerToSearch) {
        validateTicker(tickerToSearch);
        return positions.containsKey(tickerToSearch.toUpperCase(Locale.ROOT));
    }

    public boolean isPortfolioEmpty() {
        return positions.isEmpty();
    }

}