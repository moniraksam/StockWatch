package com.moniraksam.stockwatch.model;

import java.math.BigDecimal;
import java.util.Locale;

// Represents a financial asset and holds its current market price
public class Stock {
    private final String ticker;
    private BigDecimal currentPrice;
    private final String companyName;

    // Constructor
    public Stock(String ticker, BigDecimal currentPrice, String companyName) {
        validateTicker(ticker);
        validateCurrentPrice(currentPrice);
        validateCompanyName(companyName);

        this.ticker = ticker.toUpperCase(Locale.ROOT);
        this.currentPrice = currentPrice;
        this.companyName = companyName;
    }

    public String getTicker() {
        return this.ticker;
    }

    public BigDecimal getCurrentPrice() {
        return this.currentPrice;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCurrentPrice(BigDecimal newPrice) {
        validateCurrentPrice(newPrice);
        this.currentPrice = newPrice;
    }

    // Helper methods
    private static void validateTicker(String ticker) {
        if (ticker == null || ticker.isBlank())
            throw new IllegalArgumentException("Ticker cannot be null, empty or blank.");

    }

    private static void validateCompanyName(String companyName) {
        if (companyName == null || companyName.isBlank())
            throw new IllegalArgumentException("Company name cannot be null, empty or blank");
    }

    private static void validateCurrentPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Current price cannot be negative.");

    }

    // Overrides
    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "Stock: %s%n" +
                        "Company name: %s%n" +
                        "Current price: %.2f",
                ticker,
                companyName,
                currentPrice);
    }

}