import java.util.*;

public class Watchlist {
    private String name;
    private Map<String, Stock> watchedStocks;

    public Watchlist(String name) {
        this.name = name;
        this.watchedStocks = new LinkedHashMap<>();
    }

    public boolean addStock(Stock stock) {
        if (watchedStocks.containsKey(stock.getSymbol())) return false;
        watchedStocks.put(stock.getSymbol(), stock);
        return true;
    }

    public boolean removeStock(String symbol) {
        if (!watchedStocks.containsKey(symbol)) return false;
        watchedStocks.remove(symbol);
        return true;
    }

    public boolean contains(String symbol) { return watchedStocks.containsKey(symbol); }
    public boolean isEmpty() { return watchedStocks.isEmpty(); }
    public String getName() { return name; }
    public Map<String, Stock> getWatchedStocks() { return watchedStocks; }
    public int size() { return watchedStocks.size(); }
}
