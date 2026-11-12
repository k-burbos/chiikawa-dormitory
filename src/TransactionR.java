public class TransactionR {
    private Transaction[] tRecord;
    public TransactionR(Transaction[] tRecord){
        this.tRecord = tRecord;
    }

    public Transaction[] getTRecord(){
        return tRecord;
    }

    public void setRecord(Transaction[] tRecord){
        this.tRecord = tRecord;
    }
}