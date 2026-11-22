package com.moniraksam.stockwatch.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Represents a holding of a particular stock in a portfolio
// How many shares are held, what did they cost, and what are they worth?
public class Position {
    private final Stock stock;
    private int quantity;
    private BigDecimal averagePurchasePrice;

    // Constructor
    public Position(Stock stock, int quantity, BigDecimal averagePurChasePrice) {
        validateStock(stock);
        validateQuantity(quantity);
        validateAveragePurchasePrice(averagePurChasePrice);

        this.stock = stock;
        this.quantity = quantity;
        this.averagePurchasePrice = averagePurChasePrice;
    }

    // Methods
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

    // Does not affect average purchase price
    public void sell(int quantity) {
        validateQuantity(quantity);
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Cannot sell more shares than currently owned.");
        }
        this.quantity -= quantity;
    }

    // Getters
    public String getTicker() {
        return stock.getTicker();
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePurchasePrice() {
        return averagePurchasePrice;
    }

    public BigDecimal getMarketValue() {
        return stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTotalCostBasis() {
        return averagePurchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

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