public class Admin extends Chiikawa{
    private Record record;

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
