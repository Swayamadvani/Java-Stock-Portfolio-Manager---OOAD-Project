import java.util.*;

public class StockPortfolioApp {

    private static Scanner     scanner      = new Scanner(System.in);
    private static StockMarket market       = StockMarket.getInstance();
    private static Investor    investor     = null;
    private static Watchlist   watchlist    = new Watchlist("My Watchlist");
    private static AlertManager alertManager = new AlertManager();

    private static final String DIV  = "=".repeat(65);
    private static final String DIV2 = "-".repeat(65);

    // ════════════════════════════════════════
    public static void main(String[] args) {
        printBanner();
        handleStartup();

        boolean running = true;
        while (running) {
            checkAndDisplayAlerts();   // auto-check alerts after every action
            printMainMenu();
            int choice = getIntInput("  Enter your choice: ");
            switch (choice) {
                // ── Market ──
                case 1  -> viewMarket();
                case 2  -> showTopGainersLosers();
                // ── Trading ──
                case 3  -> buyStock();
                case 4  -> sellStock();
                // ── Portfolio ──
                case 5  -> Report.printPortfolioSummary(investor);
                case 6  -> Report.printProfitLossReport(investor);
                case 7  -> Report.printTradeHistory(investor);
                case 8  -> Report.printDiversificationReport(investor);
                // ── Watchlist ──
                case 9  -> watchlistMenu();
                // ── Alerts ──
                case 10 -> alertMenu();
                // ── Account ──
                case 11 -> depositFunds();
                case 12 -> Report.printInvestorProfile(investor);
                case 13 -> searchStock();
                case 14 -> browseByStock();
                // ── System ──
                case 15 -> refreshMarket();
                case 16 -> saveData();
                case 0  -> { running = false; saveData(); printGoodbye(); }
                default -> System.out.println("  [!] Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ════ STARTUP ════
    private static void handleStartup() {
        if (DataManager.saveExists()) {
            System.out.println("\n" + DIV);
            System.out.println("  A saved portfolio was found.");
            System.out.print("  Load it? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                Object[] loaded = DataManager.load(market);
                if (loaded != null) {
                    investor     = (Investor)      loaded[0];
                    watchlist    = (Watchlist)     loaded[1];
                    alertManager = (AlertManager)  loaded[2];
                    System.out.println("  Welcome back, " + investor.getName() + "!");
                    System.out.println(DIV);
                    return;
                }
            }
        }
        setupInvestor();
    }

    private static void setupInvestor() {
        System.out.println("\n" + DIV);
        System.out.println("  NEW INVESTOR REGISTRATION");
        System.out.println(DIV);
        System.out.print("  Your name  : "); String name  = scanner.nextLine().trim();
        System.out.print("  Your email : "); String email = scanner.nextLine().trim();
        System.out.print("  Your phone : "); String phone = scanner.nextLine().trim();
        double balance = getDoubleInput("  Initial deposit (Rs.): ");

        investor = new Investor(name, email, phone, balance);
        System.out.println("\n  Account created! ID: " + investor.getInvestorId());
        System.out.println("  Welcome, " + name + "!  Balance: Rs." + String.format("%.2f", balance));
        System.out.println(DIV);
    }

    // ════ BANNER & MENU ════
    private static void printBanner() {
        System.out.println("\n" + "=".repeat(65));
        System.out.println("         STOCK PORTFOLIO MANAGER — NSE SIMULATOR");
        System.out.println("               OOAD Project | Java Edition");
        System.out.println("            Design Patterns: Singleton · Strategy");
        System.out.println("=".repeat(65));
    }

    private static void printMainMenu() {
        System.out.println("\n" + DIV);
        System.out.printf("  %-25s | Balance: Rs.%.2f%n",
                investor.getName(), investor.getPortfolio().getCashBalance());
        System.out.printf("  Holdings: %-5d Trades: %-5d Watchlist: %-4d Alerts: %d%n",
                investor.getPortfolio().getHoldings().size(),
                investor.getPortfolio().getTradeHistory().size(),
                watchlist.size(),
                alertManager.activeCount());
        System.out.println(DIV);
        System.out.println("  ─── MARKET ─────────────────────────────────────────");
        System.out.println("   [1]  View Live Market        [2]  Top Gainers & Losers");
        System.out.println("  ─── TRADING ────────────────────────────────────────");
        System.out.println("   [3]  Buy Stock               [4]  Sell Stock");
        System.out.println("  ─── PORTFOLIO ──────────────────────────────────────");
        System.out.println("   [5]  Portfolio Summary       [6]  P&L Report");
        System.out.println("   [7]  Trade History           [8]  Sector Diversification");
        System.out.println("  ─── TOOLS ──────────────────────────────────────────");
        System.out.println("   [9]  Watchlist              [10]  Price Alerts");
        System.out.println("  ─── ACCOUNT ────────────────────────────────────────");
        System.out.println("  [11]  Deposit Funds          [12]  My Profile");
        System.out.println("  [13]  Search Stock           [14]  Browse by Sector");
        System.out.println("  ─── SYSTEM ─────────────────────────────────────────");
        System.out.println("  [15]  Refresh Market Prices  [16]  Save Data");
        System.out.println("   [0]  Exit");
        System.out.println(DIV);
    }

    // ════ MARKET ════
    private static void viewMarket() {
        Report.printMarketOverview(market);
    }

    private static void showTopGainersLosers() {
        Report.printTopGainersLosers(market);
    }

    private static void refreshMarket() {
        market.refreshAllPrices();
        List<PriceAlert> fired = alertManager.checkAlerts();
        System.out.println("  Market prices refreshed!");
        if (!fired.isEmpty()) {
            System.out.println("\n  *** PRICE ALERTS TRIGGERED ***");
            for (PriceAlert a : fired) System.out.println("  >>> " + a);
        }
        Report.printMarketOverview(market);
    }

    // ════ TRADING ════
    private static void buyStock() {
        System.out.println("\n" + DIV);
        System.out.println("  BUY STOCK");
        System.out.println(DIV);
        System.out.print("  Enter stock symbol (e.g., TCS, RELIANCE): ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (!market.stockExists(symbol)) {
            System.out.println("  [!] Stock '" + symbol + "' not found. Try Search (option 13).");
            return;
        }

        Stock stock = market.getStock(symbol);
        System.out.printf("  %s — %s%n", stock.getSymbol(), stock.getCompanyName());
        System.out.printf("  CMP: Rs.%.2f | Change: %+.2f (%+.2f%%) | Sector: %s%n",
                stock.getCurrentPrice(), stock.getPriceChange(),
                stock.getPriceChangePercent(), stock.getSector());
        System.out.printf("  Your Balance: Rs.%.2f%n", investor.getPortfolio().getCashBalance());

        if (investor.getPortfolio().hasHolding(symbol)) {
            PortfolioEntry ex = investor.getPortfolio().getHolding(symbol);
            System.out.printf("  You already own %d shares (avg buy: Rs.%.2f)%n",
                    ex.getQuantity(), ex.getAverageBuyPrice());
        }

        int qty = getIntInput("  Quantity to buy: ");
        double cost = qty * stock.getCurrentPrice();
        System.out.printf("  Total cost: Rs.%.2f%n", cost);
        System.out.print("  Confirm? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("  " + investor.getPortfolio().buyStock(stock, qty));
        } else {
            System.out.println("  Purchase cancelled.");
        }
    }

    private static void sellStock() {
        System.out.println("\n" + DIV);
        System.out.println("  SELL STOCK");
        System.out.println(DIV);

        if (investor.getPortfolio().getHoldings().isEmpty()) {
            System.out.println("  [!] No stocks in portfolio to sell.");
            return;
        }

        System.out.println("  Your Holdings:");
        List<PortfolioEntry> entries = new ArrayList<>(investor.getPortfolio().getHoldings().values());
        for (int i = 0; i < entries.size(); i++) {
            PortfolioEntry e = entries.get(i);
            System.out.printf("  [%d] %-8s %-26s Qty:%-5d CMP: Rs.%-8.2f P&L: %+.2f%n",
                    i + 1, e.getStock().getSymbol(), e.getStock().getCompanyName(),
                    e.getQuantity(), e.getStock().getCurrentPrice(), e.getProfitLoss());
        }

        System.out.print("\n  Enter symbol to sell: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (!investor.getPortfolio().hasHolding(symbol)) {
            System.out.println("  [!] You do not own '" + symbol + "'.");
            return;
        }

        Stock stock = market.getStock(symbol);
        PortfolioEntry entry = investor.getPortfolio().getHolding(symbol);
        System.out.printf("  Own: %d shares | Avg Buy: Rs.%.2f | CMP: Rs.%.2f | Unrealised P&L: %+.2f%n",
                entry.getQuantity(), entry.getAverageBuyPrice(),
                stock.getCurrentPrice(), entry.getProfitLoss());

        int qty = getIntInput("  Quantity to sell: ");
        System.out.printf("  Estimated proceeds: Rs.%.2f%n", qty * stock.getCurrentPrice());
        System.out.print("  Confirm? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("  " + investor.getPortfolio().sellStock(stock, qty));
        } else {
            System.out.println("  Sale cancelled.");
        }
    }

    // ════ WATCHLIST ════
    private static void watchlistMenu() {
        boolean back = false;
        while (!back) {
            Report.printWatchlist(watchlist);
            System.out.println("  [1] Add stock   [2] Remove stock   [0] Back");
            int choice = getIntInput("  Choice: ");
            switch (choice) {
                case 1 -> {
                    System.out.print("  Symbol to watch: ");
                    String sym = scanner.nextLine().trim().toUpperCase();
                    if (!market.stockExists(sym)) {
                        System.out.println("  [!] Stock not found.");
                    } else {
                        boolean added = watchlist.addStock(market.getStock(sym));
                        System.out.println(added
                                ? "  " + sym + " added to watchlist."
                                : "  " + sym + " is already in your watchlist.");
                    }
                }
                case 2 -> {
                    System.out.print("  Symbol to remove: ");
                    String sym = scanner.nextLine().trim().toUpperCase();
                    System.out.println(watchlist.removeStock(sym)
                            ? "  " + sym + " removed from watchlist."
                            : "  [!] " + sym + " not found in watchlist.");
                }
                case 0 -> back = true;
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    // ════ ALERTS ════
    private static void alertMenu() {
        boolean back = false;
        while (!back) {
            Report.printAlerts(alertManager);
            System.out.println("  [1] Set new alert   [2] Remove alert   [0] Back");
            int choice = getIntInput("  Choice: ");
            switch (choice) {
                case 1 -> {
                    System.out.print("  Stock symbol: ");
                    String sym = scanner.nextLine().trim().toUpperCase();
                    if (!market.stockExists(sym)) {
                        System.out.println("  [!] Stock not found.");
                        break;
                    }
                    Stock stock = market.getStock(sym);
                    System.out.printf("  Current price of %s: Rs.%.2f%n",
                            sym, stock.getCurrentPrice());
                    System.out.println("  Alert type: [1] ABOVE target  [2] BELOW target");
                    int t = getIntInput("  Type: ");
                    AlertType type = (t == 1) ? AlertType.ABOVE : AlertType.BELOW;
                    double target  = getDoubleInput("  Target price (Rs.): ");
                    PriceAlert alert = alertManager.addAlert(stock, target, type);
                    System.out.println("  Alert set! ID: " + alert.getAlertId()
                            + " — Will trigger when " + sym + " goes "
                            + type + " Rs." + String.format("%.2f", target));
                }
                case 2 -> {
                    System.out.print("  Alert ID to remove: ");
                    String id = scanner.nextLine().trim().toUpperCase();
                    System.out.println(alertManager.removeAlert(id)
                            ? "  Alert " + id + " removed."
                            : "  [!] Alert not found.");
                }
                case 0 -> back = true;
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    // ════ ACCOUNT ════
    private static void depositFunds() {
        System.out.println("\n" + DIV);
        System.out.println("  DEPOSIT FUNDS");
        System.out.println(DIV);
        System.out.printf("  Current balance: Rs.%.2f%n",
                investor.getPortfolio().getCashBalance());
        double amount = getDoubleInput("  Deposit amount (Rs.): ");
        if (amount <= 0) { System.out.println("  [!] Amount must be positive."); return; }
        investor.getPortfolio().deposit(amount);
        System.out.printf("  SUCCESS: Rs.%.2f deposited. New Balance: Rs.%.2f%n",
                amount, investor.getPortfolio().getCashBalance());
    }

    private static void searchStock() {
        System.out.println("\n" + DIV);
        System.out.println("  SEARCH STOCKS");
        System.out.println(DIV);
        System.out.print("  Enter symbol or name keyword: ");
        String q = scanner.nextLine().trim().toLowerCase();
        System.out.printf("  %-8s | %-28s | %-12s | %-12s | %s%n",
                "SYMBOL", "COMPANY", "PRICE (Rs.)", "CHANGE", "SECTOR");
        System.out.println(DIV2);
        boolean found = false;
        for (Stock s : market.getAllStocks()) {
            if (s.getSymbol().toLowerCase().contains(q) ||
                    s.getCompanyName().toLowerCase().contains(q) ||
                    s.getSector().toLowerCase().contains(q)) {
                String inWatchlist = watchlist.contains(s.getSymbol()) ? " [W]" : "";
                String inPortfolio = investor.getPortfolio().hasHolding(s.getSymbol()) ? " [H]" : "";
                System.out.printf("  %-8s | %-28s | %12.2f | %+.2f (%+.2f%%) | %s%s%s%n",
                        s.getSymbol(), s.getCompanyName(), s.getCurrentPrice(),
                        s.getPriceChange(), s.getPriceChangePercent(),
                        s.getSector(), inWatchlist, inPortfolio);
                found = true;
            }
        }
        if (!found) System.out.println("  No results for '" + q + "'.");
        System.out.println("  [W] = In Watchlist  [H] = In Portfolio");
    }

    private static void browseByStock() {
        System.out.println("\n" + DIV);
        System.out.println("  BROWSE BY SECTOR");
        System.out.println(DIV);
        List<String> sectors = new ArrayList<>(market.getSectors());
        for (int i = 0; i < sectors.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, sectors.get(i));
        }
        int c = getIntInput("  Select sector: ");
        if (c < 1 || c > sectors.size()) { System.out.println("  [!] Invalid."); return; }
        String sector = sectors.get(c - 1);
        System.out.println("\n  Sector: " + sector);
        System.out.println(DIV2);
        for (Stock s : market.getStocksBySector(sector)) {
            System.out.printf("  %-8s | %-28s | Rs.%9.2f | %+.2f (%+.2f%%)%n",
                    s.getSymbol(), s.getCompanyName(), s.getCurrentPrice(),
                    s.getPriceChange(), s.getPriceChangePercent());
        }
    }

    // ════ SYSTEM ════
    private static void saveData() {
        DataManager.save(investor, watchlist, alertManager);
    }

    private static void checkAndDisplayAlerts() {
        List<PriceAlert> fired = alertManager.checkAlerts();
        if (!fired.isEmpty()) {
            System.out.println("\n  ╔══════════════════════════════════════════╗");
            System.out.println("  ║         *** PRICE ALERT TRIGGERED ***     ║");
            System.out.println("  ╚══════════════════════════════════════════╝");
            for (PriceAlert a : fired) {
                System.out.printf("  >>> [%s] %s is now Rs.%.2f (target was Rs.%.2f %s)%n",
                        a.getAlertId(), a.getStock().getSymbol(),
                        a.getStock().getCurrentPrice(), a.getTargetPrice(), a.getAlertType());
            }
        }
    }

    private static void printGoodbye() {
        System.out.println("\n" + DIV);
        System.out.printf("  Final Portfolio Value: Rs.%.2f%n",
                investor.getPortfolio().getTotalPortfolioValue());
        System.out.println("  Data saved. Goodbye, " + investor.getName() + "!");
        System.out.println(DIV);
    }

    // ════ HELPERS ════
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  [!] Enter a valid number."); }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  [!] Enter a valid amount."); }
        }
    }
}
