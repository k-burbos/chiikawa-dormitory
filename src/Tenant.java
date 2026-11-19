public class Tenant extends Chiikawas {
    private Room roomNumber;
    private double bill;

    public Tenant(String name, int age, Room roomNumber) {
        super(name, age);
        this.roomNumber = roomNumber;
        bill = 0;
    }

    public Tenant(String name) {
        super(name, 0);
    }

    public void askForDorm() {

    }

    public void payRent() {
    }

    public double getBill() {
        return bill;
    }

    public void receiveBill(double bill) {
        this.bill = bill;
    }

    public Room getRoomNumber() {
        return roomNumber;
    }
    public void requestMaintenance() {

    }

    public static class Chiikawa extends Tenant {
        public Chiikawa(Room roomNumber, int age) {
            super("Chiikawa", age, roomNumber);
        }

        public Chiikawa() {
            super("Chiikawa");
        }
    }

    public static class Furuhonya extends Tenant {
        public Furuhonya(Room roomNumber, int age) {
            super("Furuhonya", age, roomNumber);
        }

        public Furuhonya() {
            super("Furuhonya");
        }
    }

    public static class Rakko extends Tenant {
        public Rakko(Room roomNumber, int age) {
            super("Rakko", age, roomNumber);
        }

        public Rakko() {
            super("Rakko");
        }
    }

    public static class Kurimanju extends Tenant {
        public Kurimanju(Room roomNumber, int age) {
            super("Kurimanju", age, roomNumber);
        }

        public Kurimanju() {
            super("Kurimanju");
        }
    }

    public static class Shisa extends Tenant {
        public Shisa(Room roomNumber, int age) {
            super("Shisa", age, roomNumber);
        }

        public Shisa() {
            super("Shisa");
        }
    }
    public static class Hachiware extends Tenant {
        public Hachiware(Room roomNumber, int age) {
            super("Hachiware", age, roomNumber);
        }

        public Hachiware() {
            super("Hachiware");
        }
    }
    public static class Usagi extends Tenant {
        public Usagi(Room roomNumber, int age) {
            super("Usagi", age, roomNumber);
        }

        public Usagi() {
            super("Usagi");
        }
    }
    public static class Momonga extends Tenant {
        public Momonga(Room roomNumber, int age) {
            super("Momonga", age, roomNumber);
        }

        public Momonga() {
            super("Momonga");
        }
    }
}
