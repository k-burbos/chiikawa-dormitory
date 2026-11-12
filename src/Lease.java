public class Lease {
    private String startDate;
    private String endDate;
    private int leaseID;
    private double rate;

    public Lease(String startDate, String endDate, int leaseID, double rate){
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaseID = leaseID;
        this.rate = rate;
    }

    public Lease(){
        this("", "", -1, 0.0);
    }

    public String getStartDate(){return startDate;}
    public String getEndDate(){return endDate;}
    public int getLeaseID(){return leaseID;}
    public double getRate(){return rate;}

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLeaseID(int leaseID) {
        this.leaseID = leaseID;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
