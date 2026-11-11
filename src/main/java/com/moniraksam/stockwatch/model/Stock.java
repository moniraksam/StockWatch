package com.moniraksam.stockwatch.model;

public class Stock {
    private final String ticker;
    private final double purchasePrice;
    private double currentPrice;
    private int quantity;

    // Constructor
    public Stock(String ticker, double purchasePrice, double currentPrice,
            int quantity) {
        validateTicker(ticker);
        validatePurchasePrice(purchasePrice);
        validateCurrentPrice(currentPrice);
        validateQuantity(quantity);
        this.ticker = ticker;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
    }

    public double getProfitLoss() {
        return (currentPrice - purchasePrice) * quantity;
    }

    // Getters
    public String getTicker() {
        return this.ticker;
    }

    public double getPurchasePrice() {
        return this.purchasePrice;
    }

    public double getCurrentPrice() {
        return this.currentPrice;
    }

    public int getQuantity() {
        return this.quantity;
    }

    // Setters
    public void setCurrentPrice(double newCurrentPrice) {
        validateCurrentPrice(newCurrentPrice);
        this.currentPrice = newCurrentPrice;
    }

    public void setQuantity(int newQuantity) {
        validateQuantity(newQuantity);
        this.quantity = newQuantity;
    }

    // Helper methods
    private static void validateTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be null, empty or blank.");
        } else if (ticker.length() != 4) {
            throw new IllegalArgumentException("Ticker must be of length 4.");
        }
        // If all good then no exceptions thrown.
    }

    private static void validatePurchasePrice(double price) {
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("Purchase price must be higher than zero.");
        }
    }

    private static void validateCurrentPrice(double price) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Current price cannot be negative.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
    }

    // Overrides
    @Override
    public String toString() {
        return String.format(
                java.util.Locale.ROOT,
                "Stock: %s%n" +
                        "Purchase price: %.2f%n" +
                        "Current price: %.2f%n" +
                        "Quantity: %d%n" +
                        "Profit/Loss: %+.2f",
                ticker.toUpperCase(),
                purchasePrice,
                currentPrice,
                quantity,
                getProfitLoss());
    }

}