import java.io.*;
import java.util.*;

// Handles save/load of investor data to a flat text file
public class DataManager {

    private static final String SAVE_FILE = "portfolio_save.txt";

    // ─── SAVE ───
    public static void save(Investor investor, Watchlist watchlist, AlertManager alertManager) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {

            // --- Investor block ---
            pw.println("[INVESTOR]");
            pw.println(investor.getName());
            pw.println(investor.getEmail());
            pw.println(investor.getPhone());
            pw.println(investor.getPortfolio().getCashBalance());
            pw.println(investor.getPortfolio().getTotalDeposited());

            // --- Holdings block ---
            pw.println("[HOLDINGS]");
            for (PortfolioEntry e : investor.getPortfolio().getHoldings().values()) {
                // symbol,qty,avgBuyPrice,totalInvested
                pw.printf("%s,%d,%.2f,%.2f%n",
                        e.getStock().getSymbol(),
                        e.getQuantity(),
                        e.getAverageBuyPrice(),
                        e.getTotalInvested());
            }

            // --- Trade history block ---
            pw.println("[TRADES]");
            for (Trade t : investor.getPortfolio().getTradeHistory()) {
                // tradeId,type,symbol,qty,price,timestamp
                pw.printf("%s,%s,%s,%d,%.2f,%s%n",
                        t.getTradeId(), t.getType(), t.getStock().getSymbol(),
                        t.getQuantity(), t.getPriceAtTrade(),
                        t.getTimestamp().toString());
            }

            // --- Watchlist block ---
            pw.println("[WATCHLIST]");
            for (Stock s : watchlist.getWatchedStocks().values()) {
                pw.println(s.getSymbol());
            }

            // --- Alerts block ---
            pw.println("[ALERTS]");
            for (PriceAlert a : alertManager.getAllAlerts()) {
                // symbol,targetPrice,type,triggered
                pw.printf("%s,%.2f,%s,%b%n",
                        a.getStock().getSymbol(),
                        a.getTargetPrice(),
                        a.getAlertType(),
                        a.isTriggered());
            }

            pw.println("[END]");
            System.out.println("  Data saved to " + SAVE_FILE);

        } catch (IOException e) {
            System.out.println("  [ERROR] Could not save data: " + e.getMessage());
        }
    }

    // ─── LOAD ───
    public static Object[] load(StockMarket market) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String section = "";

            Investor investor = null;
            Watchlist watchlist = new Watchlist("My Watchlist");
            AlertManager alertManager = new AlertManager();
            List<String[]> holdingData = new ArrayList<>();
            List<String[]> tradeData   = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("[")) {
                    section = line;
                    continue;
                }

                switch (section) {
                    case "[INVESTOR]": {
                        String name  = line;
                        String email = br.readLine().trim();
                        String phone = br.readLine().trim();
                        double cash  = Double.parseDouble(br.readLine().trim());
                        double deposited = Double.parseDouble(br.readLine().trim());
                        investor = new Investor(name, email, phone, 0);
                        investor.getPortfolio().deposit(cash); // restore cash
                        break;
                    }
                    case "[HOLDINGS]":
                        holdingData.add(line.split(","));
                        break;
                    case "[TRADES]":
                        tradeData.add(line.split(","));
                        break;
                    case "[WATCHLIST]": {
                        Stock s = market.getStock(line);
                        if (s != null) watchlist.addStock(s);
                        break;
                    }
                    case "[ALERTS]": {
                        String[] parts = line.split(",");
                        Stock s = market.getStock(parts[0]);
                        if (s != null && parts.length >= 4) {
                            double tp  = Double.parseDouble(parts[1]);
                            AlertType at = AlertType.valueOf(parts[2]);
                            alertManager.addAlert(s, tp, at);
                        }
                        break;
                    }
                }
            }

            // Restore holdings
            if (investor != null) {
                for (String[] h : holdingData) {
                    Stock s = market.getStock(h[0]);
                    if (s != null) {
                        int qty         = Integer.parseInt(h[1]);
                        double avgPrice = Double.parseDouble(h[2]);
                        investor.getPortfolio().getHoldings().put(
                                s.getSymbol(), new PortfolioEntry(s, qty, avgPrice));
                    }
                }
            }

            System.out.println("  Data loaded from " + SAVE_FILE);
            return new Object[]{investor, watchlist, alertManager};

        } catch (Exception e) {
            System.out.println("  [ERROR] Could not load data: " + e.getMessage());
            return null;
        }
    }

    public static boolean saveExists() {
        return new File(SAVE_FILE).exists();
    }
}
