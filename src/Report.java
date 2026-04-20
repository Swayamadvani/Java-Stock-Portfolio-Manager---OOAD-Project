import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {

    private static final String DIVIDER  = "=".repeat(110);
    private static final String DIVIDER2 = "-".repeat(110);

    // ─── Portfolio Summary Report ───
    public static void printPortfolioSummary(Investor investor) {
        Portfolio p = investor.getPortfolio();
        System.out.println("\n" + DIVIDER);
        System.out.println("                        PORTFOLIO SUMMARY REPORT");
        System.out.println("  Investor : " + investor.getName() + " (" + investor.getInvestorId() + ")");
        System.out.println("  Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println(DIVIDER);

        if (p.getHoldings().isEmpty()) {
            System.out.println("  No holdings in portfolio.");
        } else {
            System.out.printf("  %-6s | %-28s | %-8s | %-12s | %-12s | %-15s | %s%n",
                    "SYMBOL", "COMPANY", "QTY", "AVG PRICE", "CMP", "INVESTED", "P&L");
            System.out.println(DIVIDER2);
            for (PortfolioEntry entry : p.getHoldings().values()) {
                String pl = entry.getProfitLoss() >= 0
                        ? String.format("+Rs.%.2f (+%.2f%%)", entry.getProfitLoss(), entry.getProfitLossPercent())
                        : String.format("-Rs.%.2f (%.2f%%)", Math.abs(entry.getProfitLoss()), entry.getProfitLossPercent());
                System.out.printf("  %-6s | %-28s | %-8d | Rs.%-9.2f | Rs.%-9.2f | Rs.%-12.2f | %s%n",
                        entry.getStock().getSymbol(), entry.getStock().getCompanyName(),
                        entry.getQuantity(), entry.getAverageBuyPrice(),
                        entry.getStock().getCurrentPrice(), entry.getTotalInvested(), pl);
            }
        }

        System.out.println(DIVIDER);
        double overallPL = p.getOverallProfitLoss();
        System.out.printf("  %-30s : Rs. %.2f%n", "Cash Balance",              p.getCashBalance());
        System.out.printf("  %-30s : Rs. %.2f%n", "Total Invested in Stocks",  p.getTotalInvestedInStocks());
        System.out.printf("  %-30s : Rs. %.2f%n", "Current Stock Value",       p.getTotalCurrentStockValue());
        System.out.printf("  %-30s : %sRs. %.2f%n","Overall Profit / Loss",
                overallPL >= 0 ? "+" : "-", Math.abs(overallPL));
        System.out.printf("  %-30s : Rs. %.2f%n", "Total Portfolio Value",     p.getTotalPortfolioValue());
        System.out.println(DIVIDER);
    }

    // ─── Trade History Report ───
    public static void printTradeHistory(Investor investor) {
        Portfolio p = investor.getPortfolio();
        System.out.println("\n" + DIVIDER);
        System.out.println("                        TRADE HISTORY REPORT");
        System.out.println("  Investor : " + investor.getName() + " (" + investor.getInvestorId() + ")");
        System.out.println(DIVIDER);

        if (p.getTradeHistory().isEmpty()) {
            System.out.println("  No trades have been made yet.");
        } else {
            System.out.printf("  %-10s | %-4s | %-6s | %-8s | %-12s | %-14s | %s%n",
                    "TRADE ID", "TYPE", "SYMBOL", "QTY", "PRICE", "TOTAL VALUE", "TIMESTAMP");
            System.out.println(DIVIDER2);
            for (Trade trade : p.getTradeHistory()) {
                System.out.println("  " + trade);
            }
        }
        System.out.println(DIVIDER);
        System.out.println("  Total Trades: " + p.getTradeHistory().size());
        System.out.println(DIVIDER);
    }

    // ─── Profit & Loss Report ───
    public static void printProfitLossReport(Investor investor) {
        Portfolio p = investor.getPortfolio();
        System.out.println("\n" + DIVIDER);
        System.out.println("                        PROFIT & LOSS REPORT");
        System.out.println("  Investor : " + investor.getName() + " (" + investor.getInvestorId() + ")");
        System.out.println(DIVIDER);

        if (p.getHoldings().isEmpty()) {
            System.out.println("  No active holdings to report.");
        } else {
            double totalGain = 0, totalLoss = 0;
            System.out.printf("  %-6s | %-28s | %-10s | %-10s | %-12s | %s%n",
                    "SYMBOL", "COMPANY", "AVG COST", "CMP", "P&L", "STATUS");
            System.out.println(DIVIDER2);
            for (PortfolioEntry entry : p.getHoldings().values()) {
                double pl = entry.getProfitLoss();
                if (pl >= 0) totalGain += pl; else totalLoss += Math.abs(pl);
                System.out.printf("  %-6s | %-28s | Rs.%-7.2f | Rs.%-7.2f | %+.2f (%.2f%%) | %s%n",
                        entry.getStock().getSymbol(), entry.getStock().getCompanyName(),
                        entry.getAverageBuyPrice(), entry.getStock().getCurrentPrice(),
                        pl, entry.getProfitLossPercent(),
                        pl >= 0 ? "PROFIT" : "LOSS");
            }
            System.out.println(DIVIDER2);
            System.out.printf("  %-30s : +Rs. %.2f%n", "Total Gains",   totalGain);
            System.out.printf("  %-30s : -Rs. %.2f%n", "Total Losses",  totalLoss);
            System.out.printf("  %-30s : %+.2f%n",     "Net P&L",       totalGain - totalLoss);
        }
        System.out.println(DIVIDER);
    }

    // ─── Market Overview ───
    public static void printMarketOverview(StockMarket market) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                        LIVE MARKET OVERVIEW");
        System.out.println("  Updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        System.out.println(DIVIDER);
        System.out.printf("  %-10s | %-28s | %-12s | %-17s | %s%n",
                "SYMBOL", "COMPANY", "PRICE (Rs.)", "CHANGE", "SECTOR");
        System.out.println(DIVIDER2);
        for (Stock stock : market.getAllStocks()) {
            String chg = String.format("%+.2f (%+.2f%%)", stock.getPriceChange(), stock.getPriceChangePercent());
            System.out.printf("  %-10s | %-28s | %12.2f | %-19s | %s%n",
                    stock.getSymbol(), stock.getCompanyName(),
                    stock.getCurrentPrice(), chg, stock.getSector());
        }
        System.out.println(DIVIDER);
    }

    // ─── Investor Profile ───
    public static void printInvestorProfile(Investor investor) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                        INVESTOR PROFILE");
        System.out.println(DIVIDER);
        System.out.println("  " + investor.toString().replace("\n", "\n  "));
        System.out.printf("  %-30s : Rs. %.2f%n", "Cash Balance",      investor.getPortfolio().getCashBalance());
        System.out.printf("  %-30s : Rs. %.2f%n", "Portfolio Value",   investor.getPortfolio().getTotalPortfolioValue());
        System.out.printf("  %-30s : %d%n",        "Active Holdings",  investor.getPortfolio().getHoldings().size());
        System.out.printf("  %-30s : %d%n",        "Total Trades",     investor.getPortfolio().getTradeHistory().size());
        System.out.println(DIVIDER);
    }

    // ─── Sector Diversification Report ───
    public static void printDiversificationReport(Investor investor) {
        Portfolio p = investor.getPortfolio();
        System.out.println("\n" + DIVIDER);
        System.out.println("                  SECTOR-WISE DIVERSIFICATION REPORT");
        System.out.println("  Investor : " + investor.getName());
        System.out.println(DIVIDER);

        if (p.getHoldings().isEmpty()) {
            System.out.println("  No holdings to analyze.");
            System.out.println(DIVIDER);
            return;
        }

        Map<String, Double>  sectorValue = new LinkedHashMap<>();
        Map<String, Integer> sectorCount = new LinkedHashMap<>();
        double totalStockValue = p.getTotalCurrentStockValue();

        for (PortfolioEntry e : p.getHoldings().values()) {
            String sector = e.getStock().getSector();
            sectorValue.merge(sector, e.getCurrentValue(), Double::sum);
            sectorCount.merge(sector, 1, Integer::sum);
        }

        System.out.printf("  %-14s | %-6s | %-14s | %-10s | %s%n",
                "SECTOR", "STOCKS", "VALUE (Rs.)", "WEIGHT %", "ALLOCATION BAR");
        System.out.println(DIVIDER2);

        for (Map.Entry<String, Double> entry : sectorValue.entrySet()) {
            double pct = (entry.getValue() / totalStockValue) * 100;
            int bars   = (int)(pct / 5);
            String bar = "█".repeat(bars) + "░".repeat(20 - bars);
            System.out.printf("  %-14s | %-6d | %-14.2f | %-10.2f | %s%n",
                    entry.getKey(), sectorCount.get(entry.getKey()),
                    entry.getValue(), pct, bar);
        }

        System.out.println(DIVIDER);
        System.out.printf("  Total Stock Value: Rs.%.2f across %d sector(s)%n",
                totalStockValue, sectorValue.size());
        System.out.println(DIVIDER);
    }

    // ─── Top Gainers & Losers ───
    public static void printTopGainersLosers(StockMarket market) {
        List<Stock> stocks = new ArrayList<>(market.getAllStocks());
        stocks.sort((a, b) -> Double.compare(b.getPriceChangePercent(), a.getPriceChangePercent()));

        System.out.println("\n" + DIVIDER);
        System.out.println("                        TOP GAINERS & LOSERS");
        System.out.println(DIVIDER);

        int top = Math.min(5, stocks.size());

        System.out.println("  ▲  TOP GAINERS");
        System.out.printf("  %-10s | %-28s | %-12s | %s%n", "SYMBOL", "COMPANY", "PRICE", "CHANGE");
        System.out.println(DIVIDER2);
        for (int i = 0; i < top; i++) {
            Stock s = stocks.get(i);
            System.out.printf("  %-10s | %-28s | Rs.%8.2f | +%.2f (+%.2f%%)%n",
                    s.getSymbol(), s.getCompanyName(), s.getCurrentPrice(),
                    s.getPriceChange(), s.getPriceChangePercent());
        }

        System.out.println("\n  ▼  TOP LOSERS");
        System.out.printf("  %-10s | %-28s | %-12s | %s%n", "SYMBOL", "COMPANY", "PRICE", "CHANGE");
        System.out.println(DIVIDER2);
        for (int i = stocks.size() - 1; i >= stocks.size() - top; i--) {
            Stock s = stocks.get(i);
            System.out.printf("  %-10s | %-28s | Rs.%8.2f | %.2f (%.2f%%)%n",
                    s.getSymbol(), s.getCompanyName(), s.getCurrentPrice(),
                    s.getPriceChange(), s.getPriceChangePercent());
        }
        System.out.println(DIVIDER);
    }

    // ─── Watchlist Report ───
    public static void printWatchlist(Watchlist watchlist) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                        MY WATCHLIST — " + watchlist.getName());
        System.out.println(DIVIDER);

        if (watchlist.isEmpty()) {
            System.out.println("  Watchlist is empty. Add stocks to monitor them here.");
        } else {
            System.out.printf("  %-10s | %-28s | %-12s | %-18s | %s%n",
                    "SYMBOL", "COMPANY", "PRICE (Rs.)", "CHANGE", "SECTOR");
            System.out.println(DIVIDER2);
            for (Stock s : watchlist.getWatchedStocks().values()) {
                System.out.printf("  %-10s | %-28s | %12.2f | %+.2f (%+.2f%%)     | %s%n",
                        s.getSymbol(), s.getCompanyName(), s.getCurrentPrice(),
                        s.getPriceChange(), s.getPriceChangePercent(), s.getSector());
            }
        }
        System.out.println(DIVIDER);
        System.out.println("  Total watched: " + watchlist.size());
        System.out.println(DIVIDER);
    }

    // ─── Price Alerts Report ───
    public static void printAlerts(AlertManager alertManager) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                        PRICE ALERTS");
        System.out.println(DIVIDER);

        if (alertManager.isEmpty()) {
            System.out.println("  No price alerts set.");
        } else {
            System.out.printf("  %-8s | %-10s | %-6s | %-16s | %-16s | %s%n",
                    "ALERT ID", "SYMBOL", "TYPE", "TARGET (Rs.)", "CMP (Rs.)", "STATUS");
            System.out.println(DIVIDER2);
            for (PriceAlert a : alertManager.getAllAlerts()) {
                System.out.println("  " + a);
            }
        }
        System.out.println(DIVIDER);
        System.out.println("  Active: " + alertManager.activeCount() +
                " | Total: " + alertManager.getAllAlerts().size());
        System.out.println(DIVIDER);
    }
}
