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
        if (stock != null) {
            stocks.add(stock);
        }
    }

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
}
