public class Tenant extends Chiikawas {
    private Room roomNumber;
    private double bill;
    private int numberOfBeds;
    private int monthsLeft;
    private TransactionR transactionRecord;

    public Tenant(String name, int age, Room roomNumber) {
        super(name);
        this.roomNumber = roomNumber;
        this.bill = 0;
        this.numberOfBeds = 1;
        this.monthsLeft = 0;
        this.transactionRecord = new TransactionR();
    }

    public Tenant(String name, int numberOfBeds, int monthsLeft) {
        super(name);
        this.roomNumber = null;
        this.bill = 0;
        this.numberOfBeds = numberOfBeds;
        this.monthsLeft = monthsLeft;
        this.transactionRecord = new TransactionR();
    }

    public double getBill() {
        return bill;
    }

    public void receiveBill(double bill) {
        if (bill < 0) {
            throw new IllegalArgumentException("Bill cannot be negative.");
        }

        this.bill = bill;
    }

    public Room getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Room roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        if (numberOfBeds <= 0) {
            throw new IllegalArgumentException(
                    "Number of beds must be greater than zero.");
        }

        this.numberOfBeds = numberOfBeds;
    }

    public int getMonthsLeft() {
        return monthsLeft;
    }

    public void setMonthsLeft(int monthsLeft) {
        if (monthsLeft < 0) {
            throw new IllegalArgumentException(
                    "Months left cannot be negative.");
        }

        this.monthsLeft = monthsLeft;
    }

    public TransactionR getTransactionRecord() {
        return transactionRecord;
    }

    public void setTransactionRecord(TransactionR transactionRecord) {
        this.transactionRecord = transactionRecord == null
                ? new TransactionR()
                : transactionRecord;
    }

    public void payRent(double amount) {
        payRent(amount, new Cash());
    }

    public void payRent(double amount, Payment payment) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero.");
        }

        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment cannot be null.");
        }

        if (amount > bill) {
            throw new IllegalArgumentException(
                    "Payment cannot exceed the tenant's bill.");
        }

        bill -= amount;

        Transaction transaction =
                new Transaction(this, amount, payment);

        transactionRecord.addTransaction(transaction);
        transaction.processPayment();

        TenantCsvFileHandler.saveTransaction(
                getName(),
                numberOfBeds,
                monthsLeft,
                amount,
                payment.pay()
        );

        TenantCsvFileHandler.updateTenant(this);
    }

    @Override
    public String toString() {
        return getName() + "," +
                numberOfBeds + "," +
                monthsLeft + "," +
                bill;
    }

    public void requestMaintenance() {
    }

    public static class Chiikawa extends Tenant {
        public Chiikawa(Room roomNumber, int age) {
            super("Chiikawa", age, roomNumber);
        }
    }

    public static class Furuhonya extends Tenant {
        public Furuhonya(Room roomNumber, int age) {
            super("Furuhonya", age, roomNumber);
        }
    }

    public static class Rakko extends Tenant {
        public Rakko(Room roomNumber, int age) {
            super("Rakko", age, roomNumber);
        }
    }

    public static class Kurimanju extends Tenant {
        public Kurimanju(Room roomNumber, int age) {
            super("Kurimanju", age, roomNumber);
        }
    }

    public static class Shisa extends Tenant {
        public Shisa(Room roomNumber, int age) {
            super("Shisa", age, roomNumber);
        }
    }

    public static class Hachiware extends Tenant {
        public Hachiware(Room roomNumber, int age) {
            super("Hachiware", age, roomNumber);
        }
    }

    public static class Usagi extends Tenant {
        public Usagi(Room roomNumber, int age) {
            super("Usagi", age, roomNumber);
        }
    }

    public static class Momonga extends Tenant {
        public Momonga(Room roomNumber, int age) {
            super("Momonga", age, roomNumber);
        }
    }
}