public class Transaction {
    private Tenant tenant;
    private String modePayment;
    private double amount;

    public Transaction(Tenant tenant, double amount, String modePayment) {
        this.tenant = tenant;
        this.amount = amount;
        this.modePayment = modePayment;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public String getModePayment() {
        return modePayment;
    }

    public double getAmount() {
        return amount;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setModePayment(String modePayment) {
        this.modePayment = modePayment;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}