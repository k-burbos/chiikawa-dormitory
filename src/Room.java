import java.util.Scanner
public class Room {
//    TransactionR tRecord;
//    Tenant[] isAvailable;
    int kitchen, diningRoom, livingRoom, noOfBed;
    String[] bed;
    //noOfBed is the number of bedrooms
    public Room(){
        kitchen = 1;
        diningRoom = 1;
        livingRoom = 1;
        noOfBed = 1;
        bed = new String[noOfBed];
    }
    public Room(int noOfBed){
        kitchen = 1;
        diningRoom = 1;
        livingRoom = 1;
        this.noOfBed = noOfBed;
        bed = whatBeds(noOfBed);
    }
    private String[] whatBeds(int noOfBed){
        Scanner s = new Scanner(System.in);
        String[] whatBeds = new String[noOfBed];
        for(int i = 0; i < noOfBed ; i++){
            whatBeds[i] = s.nextLine();
        }
        return whatBeds;
    }

}
