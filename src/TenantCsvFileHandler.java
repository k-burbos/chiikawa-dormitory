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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Saves and loads tenants and transactions using the Bridge Pattern.
 *
 * abstraction (what is saved):  RecordFile -> TenantFile, TransactionFile
 * implementor (how it is saved): RecordStorage -> CsvStorage, TextStorage

 */
public final class TenantCsvFileHandler {
    // Bridge: change this to new TextStorage() if we want to save .txt files instead
    private static final RecordStorage STORAGE = new CsvStorage();

    private static final TenantFile TENANTS = new TenantFile(
            Paths.get("tenants" + STORAGE.getFileExtension()),
            STORAGE
    );

    private static final TransactionFile TRANSACTIONS = new TransactionFile(
            Paths.get("transactions" + STORAGE.getFileExtension()),
            STORAGE
    );

    private TenantCsvFileHandler() {
    }

    public static synchronized void initializeFiles() {
        TENANTS.initialize();
        TRANSACTIONS.initialize();
    }

    // Saves a new tenant or replaces the tenant with the same name.
    public static synchronized void saveTenant(Tenant tenant) {
        TENANTS.save(tenant);
    }

    public static synchronized void updateTenant(Tenant tenant) {
        TENANTS.save(tenant);
    }

    public static synchronized List<Tenant> loadTenants() {
        return TENANTS.loadAll();
    }

    public static synchronized Tenant findTenantByName(String tenantName) {
        return TENANTS.find(tenantName);
    }

    public static synchronized boolean deleteTenant(String tenantName) {
        return TENANTS.delete(tenantName);
    }

    public static synchronized void saveTransaction(
            String tenantName,
            int numberOfBeds,
            int monthsLeft,
            double amount,
            String paymentMethod
    ) {
        TRANSACTIONS.add(
                tenantName,
                numberOfBeds,
                monthsLeft,
                amount,
                paymentMethod
        );
    }

    public static synchronized List<TransactionEntry> loadTransactions() {
        return TRANSACTIONS.loadAll();
    }

    public static synchronized List<TransactionEntry>
    loadTransactionsForTenant(String tenantName) {
        return TRANSACTIONS.loadFor(tenantName);
    }


    // IMPLEMENTOR


    public interface RecordStorage {
        String getFileExtension();

        void createIfMissing(Path path, List<String> columns);

        List<List<String>> readRows(Path path, List<String> columns);

        void writeRows(
                Path path,
                List<String> columns,
                List<List<String>> rows
        );

        void appendRow(Path path, List<String> columns, List<String> row);
    }
     // Concrete implementor: comma-separated values with quoting.

    public static final class CsvStorage implements RecordStorage {
        @Override
        public String getFileExtension() {
            return ".csv";
        }

        @Override
        public void createIfMissing(Path path, List<String> columns) {
            createWithHeader(path, toLine(columns));
        }

        @Override
        public List<List<String>> readRows(
                Path path,
                List<String> columns
        ) {
            createIfMissing(path, columns);
            List<List<String>> rows = new ArrayList<>();

            for (String line : readDataLines(path, toLine(columns))) {
                rows.add(parseLine(line));
            }

            return rows;
        }

        @Override
        public void writeRows(
                Path path,
                List<String> columns,
                List<List<String>> rows
        ) {
            List<String> lines = new ArrayList<>();
            for (List<String> row : rows) {
                lines.add(toLine(row));
            }

            replaceFile(path, toLine(columns), lines);
        }

        @Override
        public void appendRow(
                Path path,
                List<String> columns,
                List<String> row
        ) {
            createIfMissing(path, columns);
            appendLine(path, toLine(row));
        }

        private String toLine(List<String> values) {
            StringBuilder line = new StringBuilder();

            for (int index = 0; index < values.size(); index++) {
                if (index > 0) {
                    line.append(',');
                }
                line.append(escape(values.get(index)));
            }

            return line.toString();
        }

        private String escape(String value) {
            String safeValue = value == null ? "" : value;

            if (safeValue.contains(",")
                    || safeValue.contains("\"")
                    || safeValue.contains("\n")
                    || safeValue.contains("\r")) {
                return "\"" + safeValue.replace("\"", "\"\"") + "\"";
            }

            return safeValue;
        }

        private List<String> parseLine(String line) {
            List<String> values = new ArrayList<>();
            StringBuilder value = new StringBuilder();
            boolean insideQuotes = false;

            for (int index = 0; index < line.length(); index++) {
                char current = line.charAt(index);

                if (current == '"') {
                    if (insideQuotes
                            && index + 1 < line.length()
                            && line.charAt(index + 1) == '"') {
                        value.append('"');
                        index++;
                    } else {
                        insideQuotes = !insideQuotes;
                    }
                } else if (current == ',' && !insideQuotes) {
                    values.add(value.toString());
                    value.setLength(0);
                } else {
                    value.append(current);
                }
            }

            if (insideQuotes) {
                throw new IllegalStateException(
                        "CSV row contains an unclosed quote.");
            }

            values.add(value.toString());
            return values;
        }
    }


    public static final class TextStorage implements RecordStorage {
        @Override
        public String getFileExtension() {
            return ".txt";
        }

        @Override
        public void createIfMissing(Path path, List<String> columns) {
            createWithHeader(path, toLine(columns));
        }

        @Override
        public List<List<String>> readRows(
                Path path,
                List<String> columns
        ) {
            createIfMissing(path, columns);
            List<List<String>> rows = new ArrayList<>();

            for (String line : readDataLines(path, toLine(columns))) {
                rows.add(Arrays.asList(line.split("\t", -1)));
            }

            return rows;
        }

        @Override
        public void writeRows(
                Path path,
                List<String> columns,
                List<List<String>> rows
        ) {
            List<String> lines = new ArrayList<>();
            for (List<String> row : rows) {
                lines.add(toLine(row));
            }

            replaceFile(path, toLine(columns), lines);
        }

        @Override
        public void appendRow(
                Path path,
                List<String> columns,
                List<String> row
        ) {
            createIfMissing(path, columns);
            appendLine(path, toLine(row));
        }

        private String toLine(List<String> values) {
            List<String> safeValues = new ArrayList<>();

            for (String value : values) {
                // Tabs and line breaks would break the row format.
                safeValues.add(value == null
                        ? ""
                        : value.replaceAll("[\\t\\r\\n]", " "));
            }

            return String.join("\t", safeValues);
        }
    }

    // ABSTRACTION: what kind of records a file holds

    public abstract static class RecordFile {
        protected final Path path;
        protected final RecordStorage storage; // the bridge

        protected RecordFile(Path path, RecordStorage storage) {
            if (path == null || storage == null) {
                throw new IllegalArgumentException(
                        "Path and storage are required.");
            }
            this.path = path;
            this.storage = storage;
        }

        protected abstract List<String> columns();

        public void initialize() {
            storage.createIfMissing(path, columns());
        }

        protected List<List<String>> readRows() {
            List<List<String>> rows = storage.readRows(path, columns());

            for (int index = 0; index < rows.size(); index++) {
                if (rows.get(index).size() != columns().size()) {
                    throw invalidRow(index, "expected "
                            + columns().size() + " columns");
                }
            }

            return rows;
        }

        protected IllegalStateException invalidRow(
                int rowIndex,
                String reason
        ) {
            return new IllegalStateException(
                    "Invalid record " + (rowIndex + 1) + " in " + path
                            + ": " + reason + ".");
        }
    }

    public static final class TenantFile extends RecordFile {
        private static final List<String> COLUMNS = Collections.unmodifiableList(
                Arrays.asList("name", "beds", "monthsLeft", "bill")
        );

        public TenantFile(Path path, RecordStorage storage) {
            super(path, storage);
        }

        @Override
        protected List<String> columns() {
            return COLUMNS;
        }

        public List<Tenant> loadAll() {
            List<List<String>> rows = readRows();
            List<Tenant> tenants = new ArrayList<>();

            for (int index = 0; index < rows.size(); index++) {
                tenants.add(toTenant(rows.get(index), index));
            }

            return tenants;
        }

        public Tenant find(String tenantName) {
            if (isBlank(tenantName)) {
                return null;
            }

            for (Tenant tenant : loadAll()) {
                if (sameName(tenant.getName(), tenantName)) {
                    return tenant;
                }
            }

            return null;
        }

        public void save(Tenant tenant) {
            requireTenant(tenant);

            List<Tenant> tenants = loadAll();
            boolean replaced = false;

            for (int index = 0; index < tenants.size(); index++) {
                if (sameName(tenants.get(index).getName(), tenant.getName())) {
                    tenants.set(index, tenant);
                    replaced = true;
                    break;
                }
            }

            if (!replaced) {
                tenants.add(tenant);
            }

            writeAll(tenants);
        }

        public boolean delete(String tenantName) {
            if (isBlank(tenantName)) {
                return false;
            }

            List<Tenant> tenants = loadAll();
            boolean removed = tenants.removeIf(
                    tenant -> sameName(tenant.getName(), tenantName)
            );

            if (removed) {
                writeAll(tenants);
            }

            return removed;
        }

        private void writeAll(List<Tenant> tenants) {
            List<List<String>> rows = new ArrayList<>();

            for (Tenant tenant : tenants) {
                requireTenant(tenant);
                rows.add(Arrays.asList(
                        tenant.getName().trim(),
                        Integer.toString(tenant.getNumberOfBeds()),
                        Integer.toString(tenant.getMonthsLeft()),
                        Double.toString(tenant.getBill())
                ));
            }

            storage.writeRows(path, COLUMNS, rows);
        }

        private Tenant toTenant(List<String> row, int rowIndex) {
            String name = row.get(0).trim();

            if (name.isEmpty()) {
                throw invalidRow(rowIndex, "tenant name is empty");
            }

            try {
                Tenant tenant = new Tenant(
                        name,
                        Integer.parseInt(row.get(1).trim()),
                        Integer.parseInt(row.get(2).trim())
                );
                tenant.receiveBill(Double.parseDouble(row.get(3).trim()));
                return tenant;
            } catch (IllegalArgumentException exception) {
                throw invalidRow(rowIndex, "invalid tenant data");
            }
        }
    }

    public static final class TransactionFile extends RecordFile {
        private static final List<String> COLUMNS = Collections.unmodifiableList(
                Arrays.asList(
                        "tenantName",
                        "beds",
                        "monthsLeft",
                        "amount",
                        "paymentMethod",
                        "dateTime"
                )
        );

        public TransactionFile(Path path, RecordStorage storage) {
            super(path, storage);
        }

        @Override
        protected List<String> columns() {
            return COLUMNS;
        }

        public void add(
                String tenantName,
                int numberOfBeds,
                int monthsLeft,
                double amount,
                String paymentMethod
        ) {
            if (isBlank(tenantName)) {
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
            if (amount <= 0) {
                throw new IllegalArgumentException(
                        "Transaction amount must be greater than zero.");
            }
            if (isBlank(paymentMethod)) {
                throw new IllegalArgumentException(
                        "Payment method cannot be empty.");
            }

            storage.appendRow(path, COLUMNS, Arrays.asList(
                    tenantName.trim(),
                    Integer.toString(numberOfBeds),
                    Integer.toString(monthsLeft),
                    Double.toString(amount),
                    paymentMethod.trim(),
                    LocalDateTime.now().toString()
            ));
        }

        public List<TransactionEntry> loadAll() {
            List<List<String>> rows = readRows();
            List<TransactionEntry> transactions = new ArrayList<>();

            for (int index = 0; index < rows.size(); index++) {
                transactions.add(toTransaction(rows.get(index), index));
            }

            return Collections.unmodifiableList(transactions);
        }

        public List<TransactionEntry> loadFor(String tenantName) {
            if (isBlank(tenantName)) {
                return Collections.emptyList();
            }

            List<TransactionEntry> matches = new ArrayList<>();
            for (TransactionEntry transaction : loadAll()) {
                if (sameName(transaction.getTenantName(), tenantName)) {
                    matches.add(transaction);
                }
            }

            return Collections.unmodifiableList(matches);
        }

        private TransactionEntry toTransaction(
                List<String> row,
                int rowIndex
        ) {
            try {
                return new TransactionEntry(
                        row.get(0),
                        Integer.parseInt(row.get(1).trim()),
                        Integer.parseInt(row.get(2).trim()),
                        Double.parseDouble(row.get(3).trim()),
                        row.get(4),
                        LocalDateTime.parse(row.get(5).trim())
                );
            } catch (IllegalArgumentException | DateTimeParseException exception) {
                throw invalidRow(rowIndex, "invalid transaction data");
            }
        }
    }

    private static void createWithHeader(Path path, String header) {
        try {
            if (Files.notExists(path)) {
                Files.write(
                        path,
                        (header + System.lineSeparator())
                                .getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE_NEW
                );
            }
        } catch (IOException exception) {
            throw fileError("create", path, exception);
        }
    }

    private static List<String> readDataLines(
            Path path,
            String expectedHeader
    ) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                path,
                StandardCharsets.UTF_8
        )) {
            String header = reader.readLine();

            if (!expectedHeader.equals(header)) {
                throw new IllegalStateException(
                        "Unexpected header in " + path + ". Expected: "
                                + expectedHeader);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException exception) {
            throw fileError("read", path, exception);
        }

        return lines;
    }

    private static void appendLine(Path path, String line) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND
        )) {
            writer.write(line);
            writer.newLine();
        } catch (IOException exception) {
            throw fileError("write", path, exception);
        }
    }

    private static void replaceFile(
            Path path,
            String header,
            List<String> lines
    ) {
        Path temporaryFile = path.resolveSibling(path.getFileName() + ".tmp");

        try (BufferedWriter writer = Files.newBufferedWriter(
                temporaryFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(header);
            writer.newLine();

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException exception) {
            throw fileError("write", path, exception);
        }

        try {
            Files.move(
                    temporaryFile,
                    path,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (AtomicMoveNotSupportedException exception) {
            try {
                Files.move(
                        temporaryFile,
                        path,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException moveException) {
                throw fileError("replace", path, moveException);
            }
        } catch (IOException exception) {
            throw fileError("replace", path, exception);
        }
    }

    private static void requireTenant(Tenant tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant cannot be null.");
        }
        if (isBlank(tenant.getName())) {
            throw new IllegalArgumentException(
                    "Tenant name cannot be empty.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static boolean sameName(String firstName, String secondName) {
        return firstName != null
                && secondName != null
                && firstName.trim().equalsIgnoreCase(secondName.trim());
    }

    private static RuntimeException fileError(
            String action,
            Path path,
            Exception exception
    ) {
        return new RuntimeException(
                "Unable to " + action + " " + path + ".",
                exception
        );
    }

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
            return tenantName + " paid " + amount + " via "
                    + paymentMethod + " on " + dateTime;
        }
    }
}
