public class Transaction {
    private Tenant tenant;
    private string modePayment;
    private double amount;

    public Transaction(Tenant tenant, double amount, string modePayment){
        this.tenant = tenant;
        this.modePayment = modePayment;
        this.amount = amount;
    }
}
