package src;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class Portfolio {
    private final List<Stock> stocks;

    // Constructor
    public Portfolio() {
        stocks = new ArrayList<>();
    }

    // Methods
    public void addStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Cannot add a null stock.");
        }
        for (Stock s : this.stocks) {
            if (stock.getTicker().equals(s.getTicker())) {
                throw new IllegalArgumentException(stock.getTicker() + " already exists in the portfolio.");
            }
        }
        stocks.add(stock);
    }

    public void updateStockPrice(String ticker, double newPrice) {
        validateTicker(ticker);

        if (!Double.isFinite(newPrice) || newPrice < 0)
            throw new IllegalArgumentException("New price cannot be negative.");

        for (Stock s : stocks) {
            if (s.getTicker().equals(ticker)) {
                s.setCurrentPrice(newPrice);
                return;
            }

        }
        // If execution reaches here, no matching ticker was found.
        throw new NoSuchElementException("No matching ticker found in the portfolio");

    }

    // Get-type methods
    public double getTotalAmountInvested() {
        if (isPortfolioEmpty())
            return 0.0;

        double total = 0.0;

        for (Stock stock : stocks) {
            total += stock.getPurchasePrice() * stock.getQuantity();
        }

        return total;
    }

    public double getCurrentMarketValue() {
        if (isPortfolioEmpty())
            return 0.0;

        double total = 0.0;

        for (Stock stock : stocks) {
            total += stock.getCurrentPrice() * stock.getQuantity();
        }

        return total;
    }

    public double getProfitLoss() {
        if (isPortfolioEmpty())
            return 0.0;
        return getCurrentMarketValue() - getTotalAmountInvested();
    }

    public Stock getMostProfitableStockPosition() {
        double mostProfitableValue = Double.NEGATIVE_INFINITY;
        int index = 0;

        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getProfitLoss() > mostProfitableValue) {
                mostProfitableValue = stocks.get(i).getProfitLoss();
                index = i;
            }
        }

        return stocks.isEmpty() ? null : stocks.get(index);
    }

    public double getPortfolioPercentReturn() {
        if (getTotalAmountInvested() == 0) {
            throw new IllegalStateException("Cannot calculate return on an empty investment.");
        }

        return getProfitLoss() / getTotalAmountInvested() * 100;
    }

    // Display methods
    public void displayPortfolio() {
        if (isPortfolioEmpty()){
            System.out.println("Portforlio is empty.");
            return;
        }

        for (Stock stock : stocks) {
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

    // Helper methods
    public void validateTicker(String ticker) {
        if (ticker == null || ticker.isEmpty() || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be null, empty or blank.");
        } else if (ticker.length() != 4) {
            throw new IllegalArgumentException("Ticker must be of length 4.");
        }
        // If all good then no exceptions thrown.
    }

    public boolean containsTicker(String tickerToSearch) {
        validateTicker(tickerToSearch);
        for (Stock s : stocks) {
            if (s.getTicker().equals(tickerToSearch)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPortfolioEmpty() {
        if (this.stocks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
