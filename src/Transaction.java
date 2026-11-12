public class Transaction {
    private Tenant tenant;
    private Payment payment;
    private double amount;

    // Constructors
    public Transaction(Tenant tenant, double amount, Payment payment){
        if(tenant == null || payment == null){
            throw new IllegalArgumentException("Tenant and payment are required.");
        }
        if (amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.tenant = tenant;
        this.payment = payment;
        this.amount = amount;
    }

    // Getter Methods
    public Tenant getTenant() {
        return tenant;
    }

    public Payment getPayment() {
        return payment;
    }

    public double getAmount() {
        return amount;
    }

    // Setter Methods
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // =========================== Methods =====================
    public String processPayment() {
        return payment.pay();
    }
}