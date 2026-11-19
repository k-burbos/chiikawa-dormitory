import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TenantCsvFileHandler {
    private static final Path TENANTS_FILE =
            Paths.get("tenants.csv");

    private static final Path TRANSACTIONS_FILE =
            Paths.get("transactions.csv");

    public static void saveTenant(Tenant tenant) {
        try {
            if (!Files.exists(TENANTS_FILE)) {
                Files.write(
                        TENANTS_FILE,
                        "name,beds,monthsLeft,bill\n"
                                .getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    TENANTS_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND
            )) {
                writer.write(
                        escapeCsv(tenant.getName()) + "," +
                                tenant.getNumberOfBeds() + "," +
                                tenant.getMonthsLeft() + "," +
                                tenant.getBill()
                );
                writer.newLine();
            }
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Unable to save tenant to tenants.csv.",
                    exception
            );
        }
    }

    public static void updateTenant(Tenant updatedTenant) {
        List<String> tenantRows = loadTenantRows();

        try (BufferedWriter writer = Files.newBufferedWriter(
                TENANTS_FILE,
                StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write("name,beds,monthsLeft,bill");
            writer.newLine();

            boolean tenantUpdated = false;

            for (String row : tenantRows) {
                String[] values = row.split(",", -1);

                if (values.length > 0
                        && values[0].equals(updatedTenant.getName())) {
                    writer.write(
                            escapeCsv(updatedTenant.getName()) + "," +
                                    updatedTenant.getNumberOfBeds() + "," +
                                    updatedTenant.getMonthsLeft() + "," +
                                    updatedTenant.getBill()
                    );
                    tenantUpdated = true;
                } else {
                    writer.write(row);
                }

                writer.newLine();
            }

            if (!tenantUpdated) {
                writer.write(
                        escapeCsv(updatedTenant.getName()) + "," +
                                updatedTenant.getNumberOfBeds() + "," +
                                updatedTenant.getMonthsLeft() + "," +
                                updatedTenant.getBill()
                );
                writer.newLine();
            }
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Unable to update tenant in tenants.csv.",
                    exception
            );
        }
    }

    public static void saveTransaction(
            String tenantName,
            int numberOfBeds,
            int monthsLeft,
            double amount,
            String paymentMethod
    ) {
        try {
            if (!Files.exists(TRANSACTIONS_FILE)) {
                Files.write(
                        TRANSACTIONS_FILE,
                        ("tenantName,beds,monthsLeft,amount," +
                                "paymentMethod,dateTime\n")
                                .getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    TRANSACTIONS_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND
            )) {
                writer.write(
                        escapeCsv(tenantName) + "," +
                                numberOfBeds + "," +
                                monthsLeft + "," +
                                amount + "," +
                                escapeCsv(paymentMethod) + "," +
                                escapeCsv(LocalDateTime.now().toString())
                );
                writer.newLine();
            }
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Unable to save transaction to transactions.csv.",
                    exception
            );
        }
    }

    private static List<String> loadTenantRows() {
        List<String> rows = new ArrayList<>();

        if (!Files.exists(TENANTS_FILE)) {
            return rows;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                TENANTS_FILE,
                StandardCharsets.UTF_8
        )) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line);
            }
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Unable to read tenants.csv.",
                    exception
            );
        }

        return rows;
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}