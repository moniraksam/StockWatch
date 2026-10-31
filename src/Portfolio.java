package src;

import java.util.ArrayList;

public class Portfolio {
    private ArrayList<Stock> stocks;

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
                throw new IllegalArgumentException(stock.getTicker() + " already exists in the portforlio.");
            }
        }

        try {
            stocks.add(stock);
        } catch (Exception e) {
            System.out.println("Something went wrong adding " + stock.getTicker() + ". Error :  " + e.getMessage());
        }

        System.out.println(stock.getTicker() + " has been added to the portforlio.");
    }

    public void updateStockPrice(String ticker, double newPrice) {
        tickerVerification(ticker);

        if (tickerInPortforlio(ticker) == false) {
            throw new IllegalArgumentException("Ticker must be in the portforlio.");
        }

        for (Stock s : stocks) {
            if (s.getTicker().equals(ticker)) {
                s.setCurrentPrice(newPrice);
                System.out.println("New price set to " + s.getTicker() + ".");
            }
        }

    }

    // Get-type methods

    public double getTotalAmountInvested() {
        double total = 0.0;

        for (Stock stock : stocks) {
            total += stock.getPurchasePrice() * stock.getQuantity();
        }

        return total;
    }

    public double getCurrentMarketValue() {
        double total = 0.0;

        for (Stock stock : stocks) {
            total += stock.getCurrentPrice() * stock.getQuantity();
        }

        return total;
    }

    public double getProfitLoss() {
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
        return getProfitLoss() / getTotalAmountInvested() * 100;
    }

    // Display methods

    public void displayPortfolio() {
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
    public void tickerVerification(String ticker) {
        if (ticker == null || ticker.isEmpty() || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be null, empty or blank.");
        } else if (ticker.length() != 4) {
            throw new IllegalArgumentException("Ticker must be of length 4.");
        }
        // If all good then no exceptions thrown.
    }

    public boolean tickerInPortforlio(String tickerToSearch) {
        tickerVerification(tickerToSearch);
        for (Stock s : stocks) {
            if (s.getTicker().equals(tickerToSearch)) {
                return true;
            }
        }
        return false;
    }
}
