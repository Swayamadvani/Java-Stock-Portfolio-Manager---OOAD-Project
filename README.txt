📈 Stock Portfolio Manager OOAD Project (Java)

A **console-based Stock Portfolio Manager** that simulates an **NSE stock market environment**.
Built using **core Object-Oriented Programming (OOP)** principles and **design patterns** as part of an OOAD coursework project.

---

##  Features

*  View live market with real-time price simulation
*  Buy stocks with balance validation
*  Sell stocks with profit & loss calculation
*  Portfolio summary report
*  Profit & loss report
*  Trade history (BUY / SELL records)
*  Deposit funds
*  Refresh market prices (random simulation)
*  Search stocks by name or symbol
*  Browse stocks by sector
*  Investor profile

---

##  Design Patterns & OOP Concepts

### 🔹 Design Patterns

* **Singleton Pattern** → `StockMarket` (ensures single market instance)
* **Strategy Pattern** → `Report` (supports multiple report types)

### 🔹 OOP Principles

* **Encapsulation** → Private fields in all classes
* **Abstraction** → `Portfolio` hides internal logic
* **Inheritance** → Class hierarchy & `TradeType` enum
* **Association** → `Investor ↔ Portfolio ↔ Trade`
* **Aggregation** → `Portfolio` contains `PortfolioEntry`
* **Composition** → `PortfolioEntry` contains `Stock`

---

## 🗂️ Project Structure

```bash
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
├── out/
├── README.md
├── run.sh
└── run.bat
```

---

## Class Overview

| Class               | Description                              |
| ------------------- | ---------------------------------------- |
| `Stock`             | Represents stock (symbol, price, sector) |
| `Trade`             | Stores BUY/SELL transactions             |
| `PortfolioEntry`    | Tracks holdings (quantity & avg price)   |
| `Portfolio`         | Manages trades & holdings                |
| `Investor`          | Represents the user                      |
| `MarketPrice`       | Simulates price changes                  |
| `StockMarket`       | Singleton with preloaded stocks          |
| `Report`            | Generates reports                        |
| `StockPortfolioApp` | Main application                         |

---

##  Preloaded NSE Stocks

`RELIANCE`, `TCS`, `HDFCBANK`, `INFY`, `ICICIBANK`,
`HINDUNILVR`, `ITC`, `SBIN`, `BHARTIARTL`, `WIPRO`,
`TATAMOTORS`, `MARUTI`, `SUNPHARMA`, `ONGC`, `AXISBANK`

---

## ⚙️ How to Run

### 1 Check Java Installation

```bash
javac -version
```

### 2️ Compile

```bash
mkdir out
javac -d out src/*.java
```

### 3️ Run

```bash
java -cp out StockPortfolioApp
```

###  Using Scripts

```bash
./run.sh     # Linux / Mac
run.bat      # Windows
```

---

##  Project Objectives

* Apply **Object-Oriented Programming principles**
* Implement **design patterns in a real-world system**
* Simulate a **stock trading environment**
* Build a **modular & scalable application**

---

##  Learnings

* Practical implementation of OOP concepts
* Use of design patterns (Singleton, Strategy)
* System design & modular architecture
* Trade & portfolio management logic

---

##  Future Improvements

* GUI (JavaFX / Web-based)
*  Real-time stock API integration
*  Database integration (persistent storage)
*  Advanced analytics & visualization

---

## 👨‍💻 Author

**Swayam Advani**
AIML Student
