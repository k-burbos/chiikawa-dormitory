public class Transaction {
    private Tenant tenant;
    private Payment payment;
    private double amount;

    public Transaction(Tenant tenant, double amount, Payment payment) {
        if (tenant == null || payment == null) {
            throw new IllegalArgumentException("Tenant and payment are required.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        this.tenant = tenant;
        this.amount = amount;
        this.payment = payment;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Payment getPayment() {
        return payment;
    }

    public double getAmount() {
        return amount;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String processPayment() {
        return payment.pay();
    }
}