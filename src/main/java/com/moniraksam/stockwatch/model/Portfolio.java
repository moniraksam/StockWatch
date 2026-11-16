package com.moniraksam.stockwatch.model;

import java.util.NoSuchElementException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Locale;

// Manages positions and calculates portfolio-wide performance
public class Portfolio {
    private final Map<String, Stock> stocks;

    // Constructor
    public Portfolio() {
        stocks = new LinkedHashMap<>();
    }

    // Methods
    public void addStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Cannot add a null stock.");
        }

        String ticker = stock.getTicker();

        if (stocks.containsKey(ticker)) {
            throw new IllegalArgumentException(
                    ticker + " already exists in the portfolio.");
        }

        stocks.put(ticker, stock);
    }

    public void updateStockPrice(String ticker, double newPrice) {
        validateTicker(ticker);

        if (!Double.isFinite(newPrice) || newPrice < 0) {
            throw new IllegalArgumentException("New price cannot be negative.");
        }

        Stock stock = stocks.get(ticker.toUpperCase(Locale.ROOT));

        if (stock == null) {
            throw new NoSuchElementException(
                    "No matching ticker found in the portfolio");
        }

        stock.setCurrentPrice(newPrice);
    }

    // Get-type methods
    public double getTotalAmountInvested() {
        double total = 0.0;

        for (Stock stock : stocks.values()) {
            total += stock.getPurchasePrice() * stock.getQuantity();
        }

        return total;
    }

    public double getCurrentMarketValue() {
        double total = 0.0;

        for (Stock stock : stocks.values()) {
            total += stock.getCurrentPrice() * stock.getQuantity();
        }

        return total;
    }

    public double getProfitLoss() {
        return getCurrentMarketValue() - getTotalAmountInvested();
    }

    public Stock getMostProfitableStockPosition() {
        Stock mostProfitable = null;

        for (Stock stock : stocks.values()) {
            if (mostProfitable == null ||
                    stock.getProfitLoss() > mostProfitable.getProfitLoss()) {
                mostProfitable = stock;
            }
        }

        return mostProfitable;
    }

    public double getPortfolioPercentReturn() {
        double invested = getTotalAmountInvested();

        if (invested == 0.0) {
            throw new IllegalStateException(
                    "Cannot calculate return on an empty investment.");
        }

        return getProfitLoss() / invested * 100;
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

    public boolean containsTicker(String tickerToSearch) {
        validateTicker(tickerToSearch);
        return stocks.containsKey(tickerToSearch.toUpperCase(Locale.ROOT));
    }

    public boolean isPortfolioEmpty() {
        return stocks.isEmpty();
    }

    // Will need to remove when starting spring boot application
    public void displayPortfolio() {
        if (isPortfolioEmpty()) {
            System.out.println("Portfolio is empty.");
            return;
        }

        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }

        System.out.println("Total invested: "
                + getTotalAmountInvested());

        System.out.println("Current value: "
                + getCurrentMarketValue());

        System.out.println("Total profit: "
                + getProfitLoss() + "\n");

        System.out.println("Porforlio Return in % : "
                + getPortfolioPercentReturn());
        System.out.println();
    }

}