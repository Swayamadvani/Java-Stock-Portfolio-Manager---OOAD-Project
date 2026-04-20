import java.util.*;

public class AlertManager {
    private List<PriceAlert> alerts;

    public AlertManager() {
        this.alerts = new ArrayList<>();
    }

    public PriceAlert addAlert(Stock stock, double targetPrice, AlertType type) {
        PriceAlert alert = new PriceAlert(stock, targetPrice, type);
        alerts.add(alert);
        return alert;
    }

    public boolean removeAlert(String alertId) {
        return alerts.removeIf(a -> a.getAlertId().equalsIgnoreCase(alertId));
    }

    // Check all alerts and return list of newly triggered ones
    public List<PriceAlert> checkAlerts() {
        List<PriceAlert> fired = new ArrayList<>();
        for (PriceAlert alert : alerts) {
            if (alert.checkAndTrigger()) fired.add(alert);
        }
        return fired;
    }

    public List<PriceAlert> getActiveAlerts() {
        List<PriceAlert> active = new ArrayList<>();
        for (PriceAlert a : alerts) if (!a.isTriggered()) active.add(a);
        return active;
    }

    public List<PriceAlert> getAllAlerts() { return alerts; }
    public boolean isEmpty() { return alerts.isEmpty(); }
    public int activeCount() { return (int) alerts.stream().filter(a -> !a.isTriggered()).count(); }
}
