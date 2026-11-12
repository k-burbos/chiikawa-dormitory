public class Admin extends Chiikawas{
    private Record record;

    public Admin(String name, int age) {
        super(name, age);
    }

    public void setBill(Tenant tenant){
        double bill = tenant.getBill();
        System.out.println("Your bill is " + bill);
    }

    public void sendBill(Tenant tenant){
        System.out.println("Pay your bill now P" + tenant.getBill());
        tenant.receiveBill(tenant.getBill());
    }

    public void receiveMaintenance(Tenant tenant){

    }

    public void removeTenant(Tenant tenant, Room[] rooms){
//        for(Room r: rooms){
//            if(r != null && r.toString().split(",")[0].equals(tenant.getName())){
//            }
//        }
    }

}
