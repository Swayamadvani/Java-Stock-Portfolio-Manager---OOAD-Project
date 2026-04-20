import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Trade {
    private static int tradeCounter = 1;

    private String tradeId;
    private Stock stock;
    private int quantity;
    private double priceAtTrade;
    private TradeType type;
    private LocalDateTime timestamp;
    private double totalValue;

    public Trade(Stock stock, int quantity, double priceAtTrade, TradeType type) {
        this.tradeId = "TRD" + String.format("%04d", tradeCounter++);
        this.stock = stock;
        this.quantity = quantity;
        this.priceAtTrade = priceAtTrade;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.totalValue = quantity * priceAtTrade;
    }

    public String getTradeId() { return tradeId; }
    public Stock getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public double getPriceAtTrade() { return priceAtTrade; }
    public TradeType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getTotalValue() { return totalValue; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("%-10s | %-4s | %-6s | Qty: %-5d | Price: Rs.%-9.2f | Total: Rs.%-12.2f | %s",
                tradeId, type, stock.getSymbol(), quantity, priceAtTrade, totalValue,
                timestamp.format(fmt));
    }
}
