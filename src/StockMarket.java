import java.util.*;

// Singleton Design Pattern
public class StockMarket {

    private static StockMarket instance;
    private Map<String, Stock> availableStocks;

    private StockMarket() {
        availableStocks = new LinkedHashMap<>();
        initializeStocks();
    }

    public static StockMarket getInstance() {
        if (instance == null) {
            instance = new StockMarket();
        }
        return instance;
    }

    // Preloaded Indian stock market data (NSE-style)
    private void initializeStocks() {
        addStock(new Stock("RELIANCE", "Reliance Industries Ltd",   2850.75, "Energy"));
        addStock(new Stock("TCS",      "Tata Consultancy Services", 3920.50, "IT"));
        addStock(new Stock("HDFCBANK", "HDFC Bank Ltd",             1680.30, "Banking"));
        addStock(new Stock("INFY",     "Infosys Ltd",               1520.90, "IT"));
        addStock(new Stock("ICICIBANK","ICICI Bank Ltd",             1100.45, "Banking"));
        addStock(new Stock("HINDUNILVR","Hindustan Unilever Ltd",   2430.60, "FMCG"));
        addStock(new Stock("ITC",      "ITC Ltd",                    490.25, "FMCG"));
        addStock(new Stock("SBIN",     "State Bank of India",         810.75, "Banking"));
        addStock(new Stock("BHARTIARTL","Bharti Airtel Ltd",         1350.20, "Telecom"));
        addStock(new Stock("WIPRO",    "Wipro Ltd",                   560.40, "IT"));
        addStock(new Stock("TATAMOTORS","Tata Motors Ltd",            980.15, "Auto"));
        addStock(new Stock("MARUTI",   "Maruti Suzuki India Ltd",   12500.80, "Auto"));
        addStock(new Stock("SUNPHARMA","Sun Pharmaceutical Ind",    1780.55, "Pharma"));
        addStock(new Stock("ONGC",     "Oil & Natural Gas Corp",      310.90, "Energy"));
        addStock(new Stock("AXISBANK", "Axis Bank Ltd",              1200.35, "Banking"));
    }

    private void addStock(Stock stock) {
        availableStocks.put(stock.getSymbol(), stock);
    }

    public Stock getStock(String symbol) {
        return availableStocks.get(symbol.toUpperCase());
    }

    public boolean stockExists(String symbol) {
        return availableStocks.containsKey(symbol.toUpperCase());
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(availableStocks.values());
    }

    // Refresh all prices
    public void refreshAllPrices() {
        MarketPrice.refreshMarketPrices(getAllStocks());
    }

    // Get stocks filtered by sector
    public List<Stock> getStocksBySector(String sector) {
        List<Stock> result = new ArrayList<>();
        for (Stock s : availableStocks.values()) {
            if (s.getSector().equalsIgnoreCase(sector)) result.add(s);
        }
        return result;
    }

    // Get all unique sectors
    public Set<String> getSectors() {
        Set<String> sectors = new LinkedHashSet<>();
        for (Stock s : availableStocks.values()) sectors.add(s.getSector());
        return sectors;
    }
}
