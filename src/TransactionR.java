public class TransactionR {
    private Transaction[] tRecord;

    public TransactionR(Transaction[] tRecord) {
        this.tRecord = tRecord;
    }

    public Transaction[] getTRecord() {
        return tRecord;
    }

    public void setRecord(Transaction[] tRecord) {
        this.tRecord = tRecord;
    }

    public void addTransaction(Transaction transaction) {
        Transaction[] updatedRecord = new Transaction[tRecord.length + 1];

        for (int i = 0; i < tRecord.length; i++) {
            updatedRecord[i] = tRecord[i];
        }

        updatedRecord[tRecord.length] = transaction;
        tRecord = updatedRecord;
    }
}