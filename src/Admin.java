public class Admin extends Chiikawas{
    private Record record;

    public Admin(String name, int age) {
        super(name, age);
    }

    public void setBill(Tenant tenant){
        float bill = tenant.getBill();
        tenant.setBill(bill);
    }

    public void sendBill(Tenant tenant){

    }

    public void receiveMaintenance(Tenant tenant){

    }

    public void removeTenant(Tenant tenant){

    }

}
