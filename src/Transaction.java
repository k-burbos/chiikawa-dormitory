public class Transaction {
    private Tenant tenant;
    private Payment payment;
    private double amount;

    public Transaction(Tenant tenant, double amount, Payment payment) {
        setTenant(tenant);
        setAmount(amount);
        setPayment(payment);
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
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant cannot be null.");
        }
        this.tenant = tenant;
    }

    public void setPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null.");
        }
        this.payment = payment;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.amount = amount;
    }

    public String processPayment() {
        return payment.pay();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "tenant=" + tenant +
                ", amount=" + amount +
                ", payment=" + payment.getClass().getSimpleName() +
                '}';
    }
}