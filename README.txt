=========================================================
   STOCK PORTFOLIO MANAGER — OOAD Project (Java)
=========================================================

DESCRIPTION
-----------
A console-based Stock Portfolio Manager simulating an NSE
stock market environment. Built using core OOP principles
and design patterns for OOAD coursework.

DESIGN PATTERNS USED
--------------------
  - Singleton Pattern  → StockMarket (only one market)
  - Strategy Pattern   → Report (multiple report types)
  - Encapsulation      → All classes with private fields
  - Inheritance        → TradeType enum, class hierarchy
  - Abstraction        → Portfolio hides trade internals
  - Association        → Investor ↔ Portfolio ↔ Trade
  - Aggregation        → Portfolio contains PortfolioEntry
  - Composition        → PortfolioEntry contains Stock

CLASS STRUCTURE
---------------
  Stock.java              — Stock entity (symbol, price, sector)
  TradeType.java          — Enum: BUY / SELL
  Trade.java              — A single transaction record
  PortfolioEntry.java     — Tracks holding (qty + avg price)
  Portfolio.java          — Manages all holdings & trades
  Investor.java           — The user/investor entity
  MarketPrice.java        — Simulates live price movement
  StockMarket.java        — Singleton: 15 preloaded stocks
  Report.java             — Generates 4 report types
  StockPortfolioApp.java  — Main app with menu system

FEATURES
--------
  [1]  View Live Market with real-time price display
  [2]  Buy Stock with balance validation
  [3]  Sell Stock with P&L calculation
  [4]  Portfolio Summary Report
  [5]  Profit & Loss Report
  [6]  Trade History (all BUY/SELL records)
  [7]  Deposit Funds
  [8]  Refresh Market Prices (random simulation)
  [9]  Search Stock by name/symbol
  [10] Browse Stocks by Sector
  [11] Investor Profile

15 PRELOADED STOCKS (NSE)
--------------------------
  RELIANCE, TCS, HDFCBANK, INFY, ICICIBANK,
  HINDUNILVR, ITC, SBIN, BHARTIARTL, WIPRO,
  TATAMOTORS, MARUTI, SUNPHARMA, ONGC, AXISBANK

HOW TO COMPILE & RUN
---------------------
  Step 1: Make sure Java (JDK 8+) is installed
          javac -version

  Step 2: Compile all Java files
          mkdir out
          javac -d out src/*.java

  Step 3: Run the application
          java -cp out StockPortfolioApp

  OR use the run script:
          ./run.sh         (Linux/Mac)
          run.bat          (Windows)

PROJECT FOLDER STRUCTURE
-------------------------
  StockPortfolio/
  ├── src/
  │   ├── Stock.java
  │   ├── TradeType.java
  │   ├── Trade.java
  │   ├── PortfolioEntry.java
  │   ├── Portfolio.java
  │   ├── Investor.java
  │   ├── MarketPrice.java
  │   ├── StockMarket.java
  │   ├── Report.java
  │   └── StockPortfolioApp.java
  ├── out/             (compiled .class files)
  ├── README.txt
  ├── run.sh
  └── run.bat

=========================================================
