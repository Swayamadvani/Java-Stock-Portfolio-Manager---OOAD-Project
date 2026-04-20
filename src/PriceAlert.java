import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PriceAlert {
    private static int alertCounter = 1;

    private String alertId;
    private Stock stock;
    private double targetPrice;
    private AlertType alertType;
    private boolean triggered;
    private LocalDateTime createdAt;
    private LocalDateTime triggeredAt;

    public PriceAlert(Stock stock, double targetPrice, AlertType alertType) {
        this.alertId   = "ALT" + String.format("%03d", alertCounter++);
        this.stock     = stock;
        this.targetPrice = targetPrice;
        this.alertType = alertType;
        this.triggered = false;
        this.createdAt = LocalDateTime.now();
    }

    // Returns true if alert just fired
    public boolean checkAndTrigger() {
        if (triggered) return false;
        boolean fired = alertType == AlertType.ABOVE
                ? stock.getCurrentPrice() >= targetPrice
                : stock.getCurrentPrice() <= targetPrice;
        if (fired) {
            triggered = true;
            triggeredAt = LocalDateTime.now();
        }
        return fired;
    }

    public String getAlertId()      { return alertId; }
    public Stock getStock()         { return stock; }
    public double getTargetPrice()  { return targetPrice; }
    public AlertType getAlertType() { return alertType; }
    public boolean isTriggered()    { return triggered; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String status = triggered
                ? "[TRIGGERED @ " + triggeredAt.format(fmt) + "]"
                : "[ACTIVE]";
        return String.format("%-8s | %-8s | %-10s | Target: Rs.%-9.2f | CMP: Rs.%-9.2f | %s",
                alertId, stock.getSymbol(), alertType,
                targetPrice, stock.getCurrentPrice(), status);
    }
}
