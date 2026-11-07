package src;

public class Stock {
    private String ticker;
    private double purchasePrice;
    private double currentPrice;
    private int quantity;

    // Constructor
    public Stock(String ticker, double purchasePrice, double currentPrice,
            int quantity) {
        validateTicker(ticker);

        if (!Double.isFinite(purchasePrice) || purchasePrice <= 0) {
            throw new IllegalArgumentException("Purchase price cannot be inferior to 0.");
        }

        if (!Double.isFinite(currentPrice) || purchasePrice < 0) {
            throw new IllegalArgumentException("Current price cannot be negative.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

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

    // Generic Setters
    public void setTicker(String newTicker) {
        validateTicker(newTicker);
        this.ticker = newTicker;
    }

    public void setCurrentPrice(double newCurrentPrice) {
        if (!Double.isFinite(newCurrentPrice)) {
            throw new IllegalArgumentException("New price cannot be inferior to 0.");
        }
        this.currentPrice = newCurrentPrice;
    }

    public void setQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("New quantity cannot be inferior to 0.");
        }
        this.quantity = newQuantity;
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

    // Overrides
    @Override
    public String toString() {
        return "****\n" + this.ticker.toUpperCase() + "\n****\nPurchase Price : " + this.purchasePrice
                + "\nCurrent Price : " + this.currentPrice + "\nQuantity : " + this.quantity + "\nProfit Loss : "
                + getProfitLoss() + "\n";
    }

}