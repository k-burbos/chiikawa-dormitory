import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class Admin extends Chiikawas {
    // Used by the game screens to check in tenants and end the day
    private static final Admin DORM_HEAD = new Admin("Dorm Head");

    private Record record;
    private final TransactionR transactionRecord;

    public Admin(String name) {
        super(name);
        this.record = new Record();
        this.transactionRecord = new TransactionR();
    }

    public Tenant createTenant(
            String tenantName,
            int numberOfBeds,
            int monthsLeft
    ) {
        if (tenantName == null || tenantName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Tenant name cannot be empty.");
        }

        if (numberOfBeds <= 0) {
            throw new IllegalArgumentException(
                    "Number of beds must be greater than zero.");
        }

        if (monthsLeft < 0) {
            throw new IllegalArgumentException(
                    "Months left cannot be negative.");
        }

        Tenant tenant = new Tenant(
                tenantName.trim(),
                numberOfBeds,
                monthsLeft
        );

        tenant.setTransactionRecord(transactionRecord);
        TenantCsvFileHandler.saveTenant(tenant);

        return tenant;
    }

    public Tenant createTenantFromDialog() {
        JTextField nameField = new JTextField();
        JTextField bedsField = new JTextField();
        JTextField monthsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Tenant name:"));
        panel.add(nameField);
        panel.add(new JLabel("Number of beds:"));
        panel.add(bedsField);
        panel.add(new JLabel("How many months left:"));
        panel.add(monthsField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Create Tenant",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            String tenantName = nameField.getText().trim();
            int numberOfBeds =
                    Integer.parseInt(bedsField.getText().trim());
            int monthsLeft =
                    Integer.parseInt(monthsField.getText().trim());

            return createTenant(
                    tenantName,
                    numberOfBeds,
                    monthsLeft
            );
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    null,
                    "Beds and months must be valid numbers.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    null,
                    exception.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

    public void setBill(Tenant tenant, double bill) {
        tenant.receiveBill(bill);
        TenantCsvFileHandler.updateTenant(tenant);
    }

    public void setBill(Tenant tenant) {
        System.out.println("Your bill is " + tenant.getBill());
    }

    public void sendBill(Tenant tenant) {
        JOptionPane.showMessageDialog(
                null,
                "Pay your bill now: P" + tenant.getBill()
        );
    }

    public void receiveMaintenance(Tenant tenant) {
    }

    public void removeTenant(Tenant tenant, Room[] rooms) {
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant cannot be null.");
        }

        TenantCsvFileHandler.deleteTenant(tenant.getName());
    }

    public static boolean showCheckInDialog(
            Component parent,
            String tenantName,
            String requestedRoom,
            int requestedMonths,
            int totalRent
    ) {
        JRadioButton single = new JRadioButton("Single");
        JRadioButton doubleRoom = new JRadioButton("Double");
        JRadioButton quad = new JRadioButton("Quad");
        ButtonGroup rooms = new ButtonGroup();
        rooms.add(single);
        rooms.add(doubleRoom);
        rooms.add(quad);

        JTextField monthsField = new JTextField();

        JRadioButton cash = new JRadioButton("Cash");
        JRadioButton online = new JRadioButton("Online");
        ButtonGroup payments = new ButtonGroup();
        payments.add(cash);
        payments.add(online);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Choose number of bedrooms:"));
        panel.add(single);
        panel.add(doubleRoom);
        panel.add(quad);
        panel.add(new JLabel("How many months?"));
        panel.add(monthsField);
        panel.add(new JLabel("Payment method:"));
        panel.add(cash);
        panel.add(online);

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Transaction",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        String chosenRoom = single.isSelected() ? "Single"
                : doubleRoom.isSelected() ? "Double"
                : quad.isSelected() ? "Quad"
                : null;

        if (chosenRoom == null) {
            showError(parent, "Please choose a room type.");
            return false;
        }

        int chosenMonths;
        try {
            chosenMonths = Integer.parseInt(monthsField.getText().trim());
        } catch (NumberFormatException exception) {
            showError(parent, "Months must be a whole number.");
            return false;
        }

        if (chosenMonths <= 0) {
            showError(parent, "Months must be greater than zero.");
            return false;
        }

        Payment payment = cash.isSelected() ? new Cash()
                : online.isSelected() ? new Online()
                : null;

        if (payment == null) {
            showError(parent, "Please choose a payment method.");
            return false;
        }

        if (!chosenRoom.equals(requestedRoom)
                || chosenMonths != requestedMonths) {
            showError(
                    parent,
                    tenantName + " asked for a " + requestedRoom
                            + " room for " + requestedMonths
                            + (requestedMonths == 1 ? " month." : " months.")
            );
            return false;
        }

        try {
            Tenant tenant = DORM_HEAD.createTenant(
                    tenantName,
                    bedsFor(chosenRoom),
                    chosenMonths
            );
            DORM_HEAD.setBill(tenant, totalRent);
            tenant.payRent(totalRent, payment);
            return true;
        } catch (RuntimeException exception) {
            showError(
                    parent,
                    "Could not save the tenant: " + exception.getMessage()
            );
            return false;
        }
    }

    /**
     * Ends the day: tenants with no months left move out, and everyone else
     * has one month taken off their stay
     *
     * @return the message shown in the "Record of Tenants" popup
     */
    public static String endDay() {
        try {
            List<String> movedOut = new ArrayList<>();
            List<String> staying = new ArrayList<>();

            for (Tenant tenant : TenantCsvFileHandler.loadTenants()) {
                if (tenant.getMonthsLeft() <= 0) {
                    TenantCsvFileHandler.deleteTenant(tenant.getName());
                    movedOut.add(tenant.getName());
                } else {
                    tenant.setMonthsLeft(tenant.getMonthsLeft() - 1);
                    TenantCsvFileHandler.updateTenant(tenant);
                    staying.add(describe(tenant));
                }
            }

            StringBuilder message = new StringBuilder();

            if (movedOut.isEmpty()) {
                message.append("You have no tenants due to leave.");
            } else {
                message.append("Tenants who moved out today:");
                for (String name : movedOut) {
                    message.append("\n- ").append(name);
                }
            }

            message.append("\n\nCurrent tenants:");
            if (staying.isEmpty()) {
                message.append("\n- None");
            } else {
                for (String tenant : staying) {
                    message.append("\n- ").append(tenant);
                }
            }

            return message.toString();
        } catch (RuntimeException exception) {
            return "Could not read the tenant records: "
                    + exception.getMessage();
        }
    }

    private static String describe(Tenant tenant) {
        int monthsLeft = tenant.getMonthsLeft();
        String stay = monthsLeft == 0
                ? "moves out next day"
                : monthsLeft + (monthsLeft == 1 ? " month left" : " months left");

        return tenant.getName() + " (" + roomFor(tenant.getNumberOfBeds())
                + ", " + stay + ")";
    }

    private static int bedsFor(String room) {
        switch (room) {
            case "Single":
                return 1;
            case "Double":
                return 2;
            default:
                return 4;
        }
    }

    private static String roomFor(int beds) {
        switch (beds) {
            case 1:
                return "Single";
            case 2:
                return "Double";
            case 4:
                return "Quad";
            default:
                return beds + " beds";
        }
    }

    private static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Invalid Transaction",
                JOptionPane.ERROR_MESSAGE
        );
    }
}