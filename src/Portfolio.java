import java.util.*;

public class Portfolio {
    private String portfolioId;
    private Map<String, PortfolioEntry> holdings;
    private List<Trade> tradeHistory;
    private double cashBalance;
    private double totalDeposited;

    public Portfolio(String portfolioId, double initialBalance) {
        this.portfolioId = portfolioId;
        this.cashBalance = initialBalance;
        this.totalDeposited = initialBalance;
        this.holdings = new LinkedHashMap<>();
        this.tradeHistory = new ArrayList<>();
    }

    // ───── Buy Stock ─────
    public String buyStock(Stock stock, int quantity) {
        double cost = stock.getCurrentPrice() * quantity;

        if (cost > cashBalance) {
            return "FAILED: Insufficient balance. Required: Rs." + String.format("%.2f", cost)
                    + " | Available: Rs." + String.format("%.2f", cashBalance);
        }
        if (quantity <= 0) {
            return "FAILED: Quantity must be greater than zero.";
        }

        cashBalance -= cost;

        if (holdings.containsKey(stock.getSymbol())) {
            holdings.get(stock.getSymbol()).addShares(quantity, stock.getCurrentPrice());
        } else {
            holdings.put(stock.getSymbol(), new PortfolioEntry(stock, quantity, stock.getCurrentPrice()));
        }

        Trade trade = new Trade(stock, quantity, stock.getCurrentPrice(), TradeType.BUY);
        tradeHistory.add(trade);

        return "SUCCESS: Bought " + quantity + " shares of " + stock.getSymbol()
                + " @ Rs." + String.format("%.2f", stock.getCurrentPrice())
                + " | Total: Rs." + String.format("%.2f", cost)
                + " | Remaining Balance: Rs." + String.format("%.2f", cashBalance)
                + "\n  Trade ID: " + trade.getTradeId();
    }

    // ───── Sell Stock ─────
    public String sellStock(Stock stock, int quantity) {
        if (!holdings.containsKey(stock.getSymbol())) {
            return "FAILED: You do not own any shares of " + stock.getSymbol();
        }
        if (quantity <= 0) {
            return "FAILED: Quantity must be greater than zero.";
        }

        PortfolioEntry entry = holdings.get(stock.getSymbol());
        if (quantity > entry.getQuantity()) {
            return "FAILED: You only have " + entry.getQuantity() + " shares of " + stock.getSymbol();
        }

        double saleProceeds = stock.getCurrentPrice() * quantity;
        double profitLoss = (stock.getCurrentPrice() - entry.getAverageBuyPrice()) * quantity;

        entry.removeShares(quantity);
        if (entry.isEmpty()) {
            holdings.remove(stock.getSymbol());
        }

        cashBalance += saleProceeds;

        Trade trade = new Trade(stock, quantity, stock.getCurrentPrice(), TradeType.SELL);
        tradeHistory.add(trade);

        String plStr = profitLoss >= 0 ? "+Rs." + String.format("%.2f", profitLoss) + " PROFIT"
                                        : "-Rs." + String.format("%.2f", Math.abs(profitLoss)) + " LOSS";

        return "SUCCESS: Sold " + quantity + " shares of " + stock.getSymbol()
                + " @ Rs." + String.format("%.2f", stock.getCurrentPrice())
                + " | Proceeds: Rs." + String.format("%.2f", saleProceeds)
                + " | P&L: " + plStr
                + " | New Balance: Rs." + String.format("%.2f", cashBalance)
                + "\n  Trade ID: " + trade.getTradeId();
    }

    // ───── Deposit Cash ─────
    public void deposit(double amount) {
        cashBalance += amount;
        totalDeposited += amount;
    }

    // ───── Computed Values ─────
    public double getTotalPortfolioValue() {
        double stockValue = holdings.values().stream()
                .mapToDouble(PortfolioEntry::getCurrentValue).sum();
        return cashBalance + stockValue;
    }

    public double getTotalInvestedInStocks() {
        return holdings.values().stream()
                .mapToDouble(PortfolioEntry::getTotalInvested).sum();
    }

    public double getTotalCurrentStockValue() {
        return holdings.values().stream()
                .mapToDouble(PortfolioEntry::getCurrentValue).sum();
    }

    public double getOverallProfitLoss() {
        return holdings.values().stream()
                .mapToDouble(PortfolioEntry::getProfitLoss).sum();
    }

    // ───── Getters ─────
    public String getPortfolioId() { return portfolioId; }
    public double getCashBalance() { return cashBalance; }
    public double getTotalDeposited() { return totalDeposited; }
    public Map<String, PortfolioEntry> getHoldings() { return holdings; }
    public List<Trade> getTradeHistory() { return tradeHistory; }
    public boolean hasHolding(String symbol) { return holdings.containsKey(symbol); }
    public PortfolioEntry getHolding(String symbol) { return holdings.get(symbol); }
}
