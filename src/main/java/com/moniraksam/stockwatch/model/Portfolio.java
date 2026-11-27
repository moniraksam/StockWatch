package com.moniraksam.stockwatch.model;

import java.util.NoSuchElementException;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Manages stock positions within a portfolio and calculates
 * its overall value and financial performance.
 */
public class Portfolio {
    private final Map<String, Position> positions;

    /**
     * Creates an empty portfolio.
     */
    public Portfolio() {
        positions = new LinkedHashMap<>();
    }

    // Methods

    /**
     * Purchases shares by creating a new position or updating an existing one.
     *
     * @param stock         the stock to purchase
     * @param quantity      the number of shares to purchase
     * @param pricePerShare the purchase price per share
     * @throws IllegalArgumentException if any parameter is invalid
     * @throws ArithmeticException      if the resulting quantity overflows
     */
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

    /**
     * Sells shares from an existing position and removes it
     * if no shares remain.
     *
     * @param ticker   the stock ticker (case-insensitive)
     * @param quantity the number of shares to sell
     * @throws IllegalArgumentException if the ticker or quantity is invalid,
     *                                  or the quantity exceeds available shares
     * @throws NoSuchElementException   if the position does not exist
     */
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

    /**
     * Updates the current market price of a stock held in the portfolio.
     *
     * @param ticker   the stock ticker (case-insensitive)
     * @param newPrice the new market price per share
     * @throws IllegalArgumentException if the ticker or price is invalid
     * @throws NoSuchElementException   if the position does not exist
     */
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

    /**
     * Calculates the current market value of the portfolio.
     *
     * @return the sum of all position market values
     */
    public BigDecimal getCurrentMarketValue() {
        BigDecimal total = BigDecimal.ZERO;

        for (Position position : positions.values()) {
            total = total.add(position.getMarketValue());
        }

        return total;
    }

    /**
     * Calculates the total cost basis of the portfolio.
     *
     * @return the sum of all position cost bases
     */
    public BigDecimal getTotalCostBasis() {
        BigDecimal total = BigDecimal.ZERO;

        for (Position position : positions.values()) {
            total = total.add(position.getTotalCostBasis());
        }

        return total;

    }

    /**
     * Calculates the total unrealized profit or loss of the portfolio.
     *
     * @return the current market value minus the total cost basis
     */
    public BigDecimal getProfitLoss() {
        return getCurrentMarketValue().subtract(getTotalCostBasis());
    }

    /**
     * Finds the position with the highest unrealized profit or loss.
     * In case of a tie, returns the first position in insertion order.
     *
     * @return the best-performing position by absolute profit or loss,
     *         or null if the portfolio is empty
     */
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

    /**
     * Calculates the unrealized percentage return of the portfolio.
     * The ratio is rounded to 8 decimal places using HALF_UP.
     *
     * @return the profit or loss divided by the cost basis, multiplied by 100
     * @throws IllegalStateException if the total cost basis is zero
     */
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

    /**
     * Checks whether the portfolio contains a position for the given ticker.
     *
     * @param tickerToSearch the stock ticker (case-insensitive)
     * @return true if the position exists, false otherwise
     * @throws IllegalArgumentException if the ticker is invalid
     */
    public boolean containsTicker(String tickerToSearch) {
        validateTicker(tickerToSearch);
        return positions.containsKey(tickerToSearch.toUpperCase(Locale.ROOT));
    }

    public boolean isPortfolioEmpty() {
        return positions.isEmpty();
    }

}