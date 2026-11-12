public class Transaction {
    private Tenant tenant;
    private string modePayment;
    private double amount;

    public Transaction(Tenant tenant, double amount, string modePayment){
        this.tenant = tenant;
        this.modePayment = modePayment;
        this.amount = amount;
    }

    public Tenant getTenant(){
        return tenant;
    }

    public string getModePayment(){
        return modePayment;
    }

    public double getAmount(){
        return amount;
    }

    public void setTenant(Tenant tenant){
        this.tenant = tenant;
    }

    public void setModePayment(string modePayment){
        this.modePayment = modePayment;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }
}
