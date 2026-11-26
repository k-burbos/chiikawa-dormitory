import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TenantCsvFileHandler {
    private static final Path TENANTS_FILE = Paths.get("tenants.csv");
    private static final Path TRANSACTIONS_FILE =
            Paths.get("transactions.csv");

    private static final String TENANT_HEADER =
            "name,beds,monthsLeft,bill";

    private static final String TRANSACTION_HEADER =
            "tenantName,beds,monthsLeft,amount,paymentMethod,dateTime";

    private TenantCsvFileHandler() {
    }

    public static synchronized void initializeFiles() {
        ensureFile(TENANTS_FILE, TENANT_HEADER);
        ensureFile(TRANSACTIONS_FILE, TRANSACTION_HEADER);
    }

    // * Saves a new tenant or replaces a tenant
    public static synchronized void saveTenant(Tenant tenant) {
        requireTenant(tenant);

        List<Tenant> tenants = loadTenants();
        boolean replaced = false;

        for (int index = 0; index < tenants.size(); index++) {
            Tenant savedTenant = tenants.get(index);

            if (sameName(savedTenant.getName(), tenant.getName())) {
                tenants.set(index, tenant);
                replaced = true;
                break;
            }
        }

        if (!replaced) {
            tenants.add(tenant);
        }

        writeTenants(tenants);
    }

    public static synchronized void updateTenant(Tenant tenant) {
        saveTenant(tenant);
    }

    // Reads all tenants from tenants.csv

    public static synchronized List<Tenant> loadTenants() {
        ensureFile(TENANTS_FILE, TENANT_HEADER);

        List<Tenant> tenants = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                TENANTS_FILE,
                StandardCharsets.UTF_8
        )) {
            String header = reader.readLine();
            validateHeader(header, TENANT_HEADER, TENANTS_FILE);

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> values = parseCsvLine(line);

                if (values.size() != 4) {
                    throw invalidRow(
                            TENANTS_FILE,
                            lineNumber,
                            4
                    );
                }

                String tenantName = values.get(0).trim();

                if (tenantName.isEmpty()) {
                    throw new IllegalStateException(
                            "Tenant name is empty in "
                                    + TENANTS_FILE
                                    + " at line "
                                    + lineNumber
                                    + "."
                    );
                }

                try {
                    int numberOfBeds =
                            Integer.parseInt(values.get(1).trim());

                    int monthsLeft =
                            Integer.parseInt(values.get(2).trim());

                    double bill =
                            Double.parseDouble(values.get(3).trim());

                    Tenant tenant = new Tenant(
                            tenantName,
                            numberOfBeds,
                            monthsLeft
                    );

                    tenant.receiveBill(bill);
                    tenants.add(tenant);
                } catch (IllegalArgumentException exception) {
                    throw new IllegalStateException(
                            "Invalid tenant data in "
                                    + TENANTS_FILE
                                    + " at line "
                                    + lineNumber
                                    + ".",
                            exception
                    );
                }
            }
        } catch (IOException exception) {
            throw fileError("read", TENANTS_FILE, exception);
        }

        return tenants;
    }
    // Finds a tenant returns null if the tenant was not found
    public static synchronized Tenant findTenantByName(
            String tenantName
    ) {
        if (tenantName == null || tenantName.trim().isEmpty()) {
            return null;
        }

        for (Tenant tenant : loadTenants()) {
            if (sameName(tenant.getName(), tenantName)) {
                return tenant;
            }
        }

        return null;
    }
     // Deletes a tenant from tenant.csv
    public static synchronized boolean deleteTenant(
            String tenantName
    ) {
        if (tenantName == null || tenantName.trim().isEmpty()) {
            return false;
        }

        List<Tenant> tenants = loadTenants();

        boolean removed = tenants.removeIf(
                tenant -> sameName(
                        tenant.getName(),
                        tenantName
                )
        );

        if (removed) {
            writeTenants(tenants);
        }

        return removed;
    }
    // Adds a payment transaction to transactions.csv.
    public static synchronized void saveTransaction(
            String tenantName,
            int numberOfBeds,
            int monthsLeft,
            double amount,
            String paymentMethod
    ) {
        if (tenantName == null || tenantName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Tenant name cannot be empty."
            );
        }

        if (numberOfBeds <= 0) {
            throw new IllegalArgumentException(
                    "Number of beds must be greater than zero."
            );
        }

        if (monthsLeft < 0) {
            throw new IllegalArgumentException(
                    "Months left cannot be negative."
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero."
            );
        }

        if (paymentMethod == null
                || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment method cannot be empty."
            );
        }

        ensureFile(
                TRANSACTIONS_FILE,
                TRANSACTION_HEADER
        );

        try (BufferedWriter writer = Files.newBufferedWriter(
                TRANSACTIONS_FILE,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND
        )) {
            writer.write(toCsvRow(
                    tenantName.trim(),
                    Integer.toString(numberOfBeds),
                    Integer.toString(monthsLeft),
                    Double.toString(amount),
                    paymentMethod.trim(),
                    LocalDateTime.now().toString()
            ));

            writer.newLine();
        } catch (IOException exception) {
            throw fileError(
                    "write",
                    TRANSACTIONS_FILE,
                    exception
            );
        }
    }

    // Reads all transactions from transactions.csv
    public static synchronized List<TransactionEntry>
    loadTransactions() {
        ensureFile(
                TRANSACTIONS_FILE,
                TRANSACTION_HEADER
        );

        List<TransactionEntry> transactions =
                new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                TRANSACTIONS_FILE,
                StandardCharsets.UTF_8
        )) {
            String header = reader.readLine();

            validateHeader(
                    header,
                    TRANSACTION_HEADER,
                    TRANSACTIONS_FILE
            );

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                List<String> values = parseCsvLine(line);

                if (values.size() != 6) {
                    throw invalidRow(
                            TRANSACTIONS_FILE,
                            lineNumber,
                            6
                    );
                }

                try {
                    TransactionEntry transaction =
                            new TransactionEntry(
                                    values.get(0),
                                    Integer.parseInt(
                                            values.get(1).trim()
                                    ),
                                    Integer.parseInt(
                                            values.get(2).trim()
                                    ),
                                    Double.parseDouble(
                                            values.get(3).trim()
                                    ),
                                    values.get(4),
                                    LocalDateTime.parse(
                                            values.get(5).trim()
                                    )
                            );

                    transactions.add(transaction);
                } catch (IllegalArgumentException exception) {
                    throw new IllegalStateException(
                            "Invalid transaction data in "
                                    + TRANSACTIONS_FILE
                                    + " at line "
                                    + lineNumber
                                    + ".",
                            exception
                    );
                }
            }
        } catch (IOException exception) {
            throw fileError(
                    "read",
                    TRANSACTIONS_FILE,
                    exception
            );
        }

        return Collections.unmodifiableList(
                transactions
        );
    }


    //  Reads transactions belonging to one tenant

    public static synchronized List<TransactionEntry>
    loadTransactionsForTenant(String tenantName) {
        if (tenantName == null
                || tenantName.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<TransactionEntry> matches =
                new ArrayList<>();

        for (TransactionEntry transaction :
                loadTransactions()) {
            if (sameName(
                    transaction.getTenantName(),
                    tenantName
            )) {
                matches.add(transaction);
            }
        }

        return Collections.unmodifiableList(matches);
    }

     // Safely rewrites tenants.csv using a temporary file.

    private static void writeTenants(
            List<Tenant> tenants
    ) {
        Path temporaryFile =
                TENANTS_FILE.resolveSibling(
                        TENANTS_FILE.getFileName()
                                + ".tmp"
                );

        try (BufferedWriter writer = Files.newBufferedWriter(
                temporaryFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(TENANT_HEADER);
            writer.newLine();

            for (Tenant tenant : tenants) {
                requireTenant(tenant);

                writer.write(toCsvRow(
                        tenant.getName(),
                        Integer.toString(
                                tenant.getNumberOfBeds()
                        ),
                        Integer.toString(
                                tenant.getMonthsLeft()
                        ),
                        Double.toString(
                                tenant.getBill()
                        )
                ));

                writer.newLine();
            }
        } catch (IOException exception) {
            throw fileError(
                    "write",
                    TENANTS_FILE,
                    exception
            );
        }

        try {
            Files.move(
                    temporaryFile,
                    TENANTS_FILE,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (AtomicMoveNotSupportedException exception) {
            try {
                Files.move(
                        temporaryFile,
                        TENANTS_FILE,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException moveException) {
                throw fileError(
                        "replace",
                        TENANTS_FILE,
                        moveException
                );
            }
        } catch (IOException exception) {
            throw fileError(
                    "replace",
                    TENANTS_FILE,
                    exception
            );
        }
    }

     // Creates a CSV file and writes its header if it does not exist.

    private static void ensureFile(
            Path path,
            String header
    ) {
        try {
            if (Files.notExists(path)) {
                Files.write(
                        path,
                        (
                                header
                                        + System.lineSeparator()
                        ).getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE_NEW
                );
            }
        } catch (IOException exception) {
            throw fileError(
                    "create",
                    path,
                    exception
            );
        }
    }

    private static void validateHeader(
            String actualHeader,
            String expectedHeader,
            Path path
    ) {
        if (!expectedHeader.equals(actualHeader)) {
            throw new IllegalStateException(
                    "Unexpected header in "
                            + path
                            + ". Expected: "
                            + expectedHeader
            );
        }
    }

     // Splits a CSV line while supporting quoted values.

    private static List<String> parseCsvLine(
            String line
    ) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();

        boolean insideQuotes = false;

        for (int index = 0;
             index < line.length();
             index++) {
            char current = line.charAt(index);

            if (current == '"') {
                boolean escapedQuote =
                        insideQuotes
                                && index + 1 < line.length()
                                && line.charAt(index + 1) == '"';

                if (escapedQuote) {
                    value.append('"');
                    index++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (current == ','
                    && !insideQuotes) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(current);
            }
        }

        if (insideQuotes) {
            throw new IllegalStateException(
                    "CSV row contains an unclosed quote."
            );
        }

        values.add(value.toString());

        return values;
    }

    private static String toCsvRow(
            String... values
    ) {
        StringBuilder row = new StringBuilder();

        for (int index = 0;
             index < values.length;
             index++) {
            if (index > 0) {
                row.append(',');
            }

            row.append(escapeCsv(values[index]));
        }

        return row.toString();
    }

    private static String escapeCsv(
            String value
    ) {
        String safeValue =
                value == null ? "" : value;

        boolean mustBeQuoted =
                safeValue.contains(",")
                        || safeValue.contains("\"")
                        || safeValue.contains("\n")
                        || safeValue.contains("\r");

        if (mustBeQuoted) {
            return "\""
                    + safeValue.replace(
                    "\"",
                    "\"\""
            )
                    + "\"";
        }

        return safeValue;
    }

    private static void requireTenant(
            Tenant tenant
    ) {
        if (tenant == null) {
            throw new IllegalArgumentException(
                    "Tenant cannot be null."
            );
        }

        if (tenant.getName() == null
                || tenant.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Tenant name cannot be empty."
            );
        }
    }

    private static boolean sameName(
            String firstName,
            String secondName
    ) {
        return firstName != null
                && secondName != null
                && firstName.trim().equalsIgnoreCase(
                secondName.trim()
        );
    }

    private static IllegalStateException invalidRow(
            Path path,
            int lineNumber,
            int expectedColumns
    ) {
        return new IllegalStateException(
                "Invalid CSV row in "
                        + path
                        + " at line "
                        + lineNumber
                        + ". Expected "
                        + expectedColumns
                        + " columns."
        );
    }

    private static RuntimeException fileError(
            String action,
            Path path,
            Exception exception
    ) {
        return new RuntimeException(
                "Unable to "
                        + action
                        + " "
                        + path
                        + ".",
                exception
        );
    }

    // Represents one row from transactions.csv

    public static final class TransactionEntry {
        private final String tenantName;
        private final int numberOfBeds;
        private final int monthsLeft;
        private final double amount;
        private final String paymentMethod;
        private final LocalDateTime dateTime;

        public TransactionEntry(
                String tenantName,
                int numberOfBeds,
                int monthsLeft,
                double amount,
                String paymentMethod,
                LocalDateTime dateTime
        ) {
            this.tenantName = tenantName;
            this.numberOfBeds = numberOfBeds;
            this.monthsLeft = monthsLeft;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.dateTime = dateTime;
        }

        public String getTenantName() {
            return tenantName;
        }

        public int getNumberOfBeds() {
            return numberOfBeds;
        }

        public int getMonthsLeft() {
            return monthsLeft;
        }

        public double getAmount() {
            return amount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        @Override
        public String toString() {
            return tenantName
                    + " paid "
                    + amount
                    + " via "
                    + paymentMethod
                    + " on "
                    + dateTime;
        }
    }
}