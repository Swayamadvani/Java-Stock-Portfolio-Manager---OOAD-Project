public class Stock {
    private String symbol;
    private String companyName;
    private double currentPrice;
    private double previousPrice;
    private String sector;

    public Stock(String symbol, String companyName, double currentPrice, String sector) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.previousPrice = currentPrice;
        this.sector = sector;
    }

    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPreviousPrice() { return previousPrice; }
    public String getSector() { return sector; }

    public void updatePrice(double newPrice) {
        this.previousPrice = this.currentPrice;
        this.currentPrice = newPrice;
    }

    public double getPriceChange() {
        return currentPrice - previousPrice;
    }

    public double getPriceChangePercent() {
        return ((currentPrice - previousPrice) / previousPrice) * 100;
    }

    @Override
    public String toString() {
        String direction = getPriceChange() >= 0 ? "+" : "";
        return String.format("%-6s | %-28s | Rs.%9.2f | %s%.2f (%.2f%%) | %s",
                symbol, companyName, currentPrice,
                direction, getPriceChange(),
                getPriceChangePercent(), sector);
    }
}
