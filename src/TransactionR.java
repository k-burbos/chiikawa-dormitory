public class TransactionR {
    private Transaction[] tRecord;

    public TransactionR() {
        tRecord = new Transaction[0];
    }

    public TransactionR(Transaction[] tRecord) {
        setRecord(tRecord);
    }

    public Transaction[] getRecord() {
        return tRecord.clone();
    }

    public void setRecord(Transaction[] tRecord) {
        if (tRecord == null) {
            this.tRecord = new Transaction[0];
        } else {
            this.tRecord = tRecord.clone();
        }
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }

        Transaction[] updatedRecord =
                new Transaction[tRecord.length + 1];

        System.arraycopy(
                tRecord,
                0,
                updatedRecord,
                0,
                tRecord.length
        );

        updatedRecord[tRecord.length] = transaction;
        tRecord = updatedRecord;
    }

    public boolean removeTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

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

        Transaction[] updatedRecord =
                new Transaction[tRecord.length - 1];

        System.arraycopy(
                tRecord,
                0,
                updatedRecord,
                0,
                index
        );

        System.arraycopy(
                tRecord,
                index + 1,
                updatedRecord,
                index,
                tRecord.length - index - 1
        );

        tRecord = updatedRecord;
        return true;
    }

    public int getTransactionCount() {
        return tRecord.length;
    }

    public boolean isEmpty() {
        return tRecord.length == 0;
    }

    public void clearTransactions() {
        tRecord = new Transaction[0];
    }
}