import java.io.*;
import java.util.*;

public class FileHandler {

    private static final String TRADE_FILE = "portfolio_trades.csv";
    private static final String HOLDINGS_FILE = "portfolio_holdings.csv";

    // ─── Save trade history to CSV ───
    public static void saveTradeHistory(Investor investor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TRADE_FILE))) {
            pw.println("TradeID,Symbol,Company,Quantity,Price,Type,TotalValue,Timestamp");
            for (Trade t : investor.getPortfolio().getTradeHistory()) {
                pw.printf("%s,%s,%s,%d,%.2f,%s,%.2f,%s%n",
                        t.getTradeId(),
                        t.getStock().getSymbol(),
                        t.getStock().getCompanyName(),
                        t.getQuantity(),
                        t.getPriceAtTrade(),
                        t.getType(),
                        t.getTotalValue(),
                        t.getTimestamp());
            }
            System.out.println("  SUCCESS: Trade history saved to '" + TRADE_FILE + "'");
        } catch (IOException e) {
            System.out.println("  ERROR: Could not save trade history. " + e.getMessage());
        }
    }

    // ─── Save current holdings to CSV ───
    public static void saveHoldings(Investor investor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(HOLDINGS_FILE))) {
            pw.println("Symbol,Company,Quantity,AvgBuyPrice,CurrentPrice,TotalInvested,CurrentValue,ProfitLoss,PnLPercent");
            for (PortfolioEntry entry : investor.getPortfolio().getHoldings().values()) {
                pw.printf("%s,%s,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
                        entry.getStock().getSymbol(),
                        entry.getStock().getCompanyName(),
                        entry.getQuantity(),
                        entry.getAverageBuyPrice(),
                        entry.getStock().getCurrentPrice(),
                        entry.getTotalInvested(),
                        entry.getCurrentValue(),
                        entry.getProfitLoss(),
                        entry.getProfitLossPercent());
            }
            System.out.println("  SUCCESS: Holdings saved to '" + HOLDINGS_FILE + "'");
        } catch (IOException e) {
            System.out.println("  ERROR: Could not save holdings. " + e.getMessage());
        }
    }

    // ─── Save both files ───
    public static void saveAll(Investor investor) {
        System.out.println("\n  Saving portfolio data...");
        saveTradeHistory(investor);
        saveHoldings(investor);
        System.out.printf("  Portfolio value: Rs.%.2f | Trades: %d | Holdings: %d%n",
                investor.getPortfolio().getTotalPortfolioValue(),
                investor.getPortfolio().getTradeHistory().size(),
                investor.getPortfolio().getHoldings().size());
    }

    // ─── Print CSV contents to console ───
    public static void viewSavedFile(String filename) {
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("  [!] File not found: " + filename + ". Please save first.");
            return;
        }
        System.out.println("\n  Contents of " + filename + ":");
        System.out.println("  " + "-".repeat(80));
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("  " + line);
            }
        } catch (IOException e) {
            System.out.println("  ERROR: " + e.getMessage());
        }
    }
}
