public class Investor {
    private static int idCounter = 1001;

    private String investorId;
    private String name;
    private String email;
    private String phone;
    private Portfolio portfolio;

    public Investor(String name, String email, String phone, double initialBalance) {
        this.investorId = "INV" + idCounter++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.portfolio = new Portfolio("PORT-" + investorId, initialBalance);
    }

    public String getInvestorId() { return investorId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Portfolio getPortfolio() { return portfolio; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("Investor ID : %s\nName        : %s\nEmail       : %s\nPhone       : %s",
                investorId, name, email, phone);
    }
}
