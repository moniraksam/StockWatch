import src.Portfolio;
import src.Stock;

public class Main {
    public static void main(String[] args) {

        Portfolio portfolio = new Portfolio();

        Stock s = new Stock("AAPL", 180, 195, 3);
        Stock s1 = new Stock("MSFT", 400, 380, 2);
        Stock s2 = new Stock("NVDA", 100, 125, 5);

        portfolio.addStock(s);
        portfolio.addStock(s1);
        portfolio.addStock(s2);

        portfolio.displayPortfolio();
        System.out.println("Getting Most Profitable Stock :\n" + portfolio.getMostProfitableStockPosition());
    }
}
