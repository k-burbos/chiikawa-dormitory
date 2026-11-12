public class TransactionR {
    private Transaction[] tRecord;

    public TransactionR() {
        tRecord = new Transaction[0];
    }

    public TransactionR(Transaction[] tRecord) {
        this.tRecord = tRecord == null ? new Transaction[0] : tRecord;
    }

    public Transaction[] getTRecord() {
        return tRecord;
    }

    public void setRecord(Transaction[] tRecord) {
        this.tRecord = tRecord == null ? new Transaction[0] : tRecord;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }

        Transaction[] updatedRecord = new Transaction[tRecord.length + 1];

        for (int i = 0; i < tRecord.length; i++) {
            updatedRecord[i] = tRecord[i];
        }

        updatedRecord[tRecord.length] = transaction;
        tRecord = updatedRecord;
    }

    public boolean removeTransaction(Transaction transaction) {
        int index = -1;

        for (int i = 0; i < tRecord.length; i++) {
            if (tRecord[i] == transaction) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        Transaction[] updatedRecord = new Transaction[tRecord.length - 1];

        for (int i = 0, j = 0; i < tRecord.length; i++) {
            if (i != index) {
                updatedRecord[j++] = tRecord[i];
            }
        }

        tRecord = updatedRecord;
        return true;
    }

    public int getTransactionCount() {
        return tRecord.length;
    }
}