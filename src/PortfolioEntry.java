public class PortfolioEntry {
    private Stock stock;
    private int quantity;
    private double averageBuyPrice;
    private double totalInvested;

    public PortfolioEntry(Stock stock, int quantity, double buyPrice) {
        this.stock = stock;
        this.quantity = quantity;
        this.averageBuyPrice = buyPrice;
        this.totalInvested = quantity * buyPrice;
    }

    // Called when buying more of the same stock
    public void addShares(int qty, double price) {
        totalInvested += qty * price;
        quantity += qty;
        averageBuyPrice = totalInvested / quantity;
    }

    // Called when selling shares
    // Returns false if not enough shares
    public boolean removeShares(int qty) {
        if (qty > quantity) return false;
        double costPerShare = averageBuyPrice;
        totalInvested -= qty * costPerShare;
        quantity -= qty;
        return true;
    }

    public Stock getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public double getAverageBuyPrice() { return averageBuyPrice; }
    public double getTotalInvested() { return totalInvested; }

    public double getCurrentValue() {
        return quantity * stock.getCurrentPrice();
    }

    public double getProfitLoss() {
        return getCurrentValue() - totalInvested;
    }

    public double getProfitLossPercent() {
        if (totalInvested == 0) return 0;
        return (getProfitLoss() / totalInvested) * 100;
    }

    public boolean isEmpty() {
        return quantity == 0;
    }

    @Override
    public String toString() {
        String pl = getProfitLoss() >= 0 ? "+" : "";
        return String.format("%-6s | %-28s | Qty: %-5d | Avg: Rs.%-9.2f | CMP: Rs.%-9.2f | Invested: Rs.%-12.2f | P&L: %sRs.%.2f (%s%.2f%%)",
                stock.getSymbol(), stock.getCompanyName(), quantity,
                averageBuyPrice, stock.getCurrentPrice(),
                totalInvested, pl, getProfitLoss(), pl, getProfitLossPercent());
    }
}
