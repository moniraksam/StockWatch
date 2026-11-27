package com.moniraksam.stockwatch.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a stock holding within a portfolio, tracking its
 * quantity, weighted average purchase price and performance.
 */
public class Position {
    private final Stock stock;
    private int quantity;
    private BigDecimal averagePurchasePrice;

    /**
     * Creates a position with an initial quantity and average purchase price.
     *
     * @param stock                the associated stock
     * @param quantity             the initial number of shares
     * @param averagePurchasePrice the initial average price per share
     * @throws IllegalArgumentException if the stock is null, the quantity
     *                                  is non-positive or the price is invalid
     */
    public Position(Stock stock, int quantity, BigDecimal averagePurChasePrice) {
        validateStock(stock);
        validateQuantity(quantity);
        validateAveragePurchasePrice(averagePurChasePrice);

        this.stock = stock;
        this.quantity = quantity;
        this.averagePurchasePrice = averagePurChasePrice;
    }

    /**
     * Purchases additional shares and recalculates the weighted
     * average purchase price, rounded to 8 decimal places and using HALF_UP.
     *
     * @param quantity      the number of shares to purchase
     * @param pricePerShare the purchase price per share
     * @throws IllegalArgumentException if either parameter is invalid
     * @throws ArithmeticException      if the resulting quantity overflows
     */
    public void buy(int quantity, BigDecimal pricePerShare) {
        validateQuantity(quantity);
        validatePricePerShare(pricePerShare);

        int newQuantity = Math.addExact(this.quantity, quantity); // Defensive method to avoid int overflow

        BigDecimal oldTotalPrice = averagePurchasePrice.multiply(BigDecimal.valueOf(this.quantity));

        BigDecimal newTotalPrice = oldTotalPrice.add(pricePerShare.multiply(BigDecimal.valueOf(quantity)));

        BigDecimal newAveragePrice = newTotalPrice.divide(BigDecimal.valueOf(newQuantity),
                8, RoundingMode.HALF_UP);

        // Only modify the object after successful calculations
        this.quantity = newQuantity;
        this.averagePurchasePrice = newAveragePrice;
    }

    /**
     * Sells shares without modifying the average purchase price.
     *
     * @param quantity the number of shares to sell
     * @throws IllegalArgumentException if quantity is non-positive
     *                                  or exceeds available shares
     */
    public void sell(int quantity) {
        validateQuantity(quantity);
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Cannot sell more shares than currently owned.");
        }
        this.quantity -= quantity;
    }

    // Getters
    public Stock getStock() {
        return stock;
    }

    public String getTicker() {
        return stock.getTicker();
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePurchasePrice() {
        return averagePurchasePrice;
    }

    /**
     * @return the current share price multiplied by the quantity
     */
    public BigDecimal getMarketValue() {
        return stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * @return the average purchase prices multiplied by quantity
     */
    public BigDecimal getTotalCostBasis() {
        return averagePurchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Calculates the unrealized profit or loss of this position.
     *
     * @return the difference between the current market value
     *         and the total cost basis
     */
    public BigDecimal getUnrealizedProfitLoss() {
        return getMarketValue().subtract(getTotalCostBasis());
    }

    // Validators
    private static void validateStock(Stock stock) {
        if (stock == null)
            throw new IllegalArgumentException("Stock cannot be null.");
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

    private static void validatePricePerShare(BigDecimal pricePerShare) {
        if (pricePerShare == null ||
                pricePerShare.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Average purchase price cannot be negative or zero.");
    }

}