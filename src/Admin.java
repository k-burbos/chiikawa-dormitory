import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class Admin extends Chiikawas {
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

    public void removeTenant(Tenant tenant, Room[] rooms){
        if (tenant == null){
            throw new IllegalArgumentException(
                    "Tennant cannot be null."
            );
        }
        TenantCsvFileHandler.deleteTenant(
                tenant.getName()
        );
    }

}
