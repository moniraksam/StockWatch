package src;
public class Stock {
    private String ticker;
    private double purchasePrice;
    private double currentPrice;
    private int quantity;

    // Constructor
    public Stock(String ticker, double purchasePrice, double currentPrice,
            int quantity) {
        this.ticker = ticker;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
    }

    // Methods
    public double getProfitLoss() {
        return (currentPrice - purchasePrice) * quantity;
    }

    // Generic getters
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

    // Overrides
    @Override
    public String toString() {
        return "****\n" + this.ticker.toUpperCase() + "\n****\nPurchase Price : " + this.purchasePrice
                + "\nCurrent Price : " + this.currentPrice + "\nQuantity : " + this.quantity + "\nProfit Loss : "
                + getProfitLoss() + "\n";
    }
}