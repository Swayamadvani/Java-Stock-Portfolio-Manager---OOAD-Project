import java.util.*;

public class MarketPrice {
    private static final Random random = new Random();

    // Simulates realistic stock price movement (up to +/-5%)
    public static double simulatePriceChange(double currentPrice) {
        double changePercent = (random.nextDouble() * 10) - 5; // -5% to +5%
        double newPrice = currentPrice * (1 + changePercent / 100);
        return Math.max(1.0, Math.round(newPrice * 100.0) / 100.0); // min Rs.1
    }

    // Update all stocks in the market
    public static void refreshMarketPrices(List<Stock> stocks) {
        for (Stock stock : stocks) {
            double newPrice = simulatePriceChange(stock.getCurrentPrice());
            stock.updatePrice(newPrice);
        }
    }

    // Update a single stock
    public static void refreshSingleStock(Stock stock) {
        double newPrice = simulatePriceChange(stock.getCurrentPrice());
        stock.updatePrice(newPrice);
    }
}
