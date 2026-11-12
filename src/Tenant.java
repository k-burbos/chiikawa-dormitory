public class Tenant extends Chiikawas {
    private int roomNumber;
    private double bill;

    public Tenant(String name, int age, int roomNumber) {
        super(name, age);
        this.roomNumber = roomNumber;
        bill = 0;
    }

    public void payRent() {
    }

    public void receiveBill(double bill) {
        this.bill = bill;
    }

    public void receiveMaintenance() {

    }

    public static class Chiikawa extends Tenant {
        public Chiikawa(int roomNumber, int age) {
            super("Chiikawa", age, roomNumber);
        }
    }

    public static class Furohonya extends Tenant {
        public Furohonya(int roomNumber, int age) {
            super("Furohonya", age, roomNumber);
        }
    }

    public static class Rakko extends Tenant {
        public Rakko(int roomNumber, int age) {
            super("Rakko", age, roomNumber);
        }
    }

    public static class Kurimanju extends Tenant {
        public Kurimanju(int roomNumber, int age) {
            super("Kurimanju", age, roomNumber);
        }
    }

    public static class Shisa extends Tenant {
        public Shisa(int roomNumber, int age) {
            super("Shisa", age, roomNumber);
        }
    }
    public static class Hachiware extends Tenant {
        public Hachiware(int roomNumber, int age) {
            super("Hachiware", age, roomNumber);
        }
    }
    public static class Usagi extends Tenant {
        public Usagi(int roomNumber, int age) {
            super("Usagi", age, roomNumber);
        }
    }
    public static class Momonga extends Tenant {
        public Momonga(int roomNumber, int age) {
            super("Momonga", age, roomNumber);
        }
    }
}
