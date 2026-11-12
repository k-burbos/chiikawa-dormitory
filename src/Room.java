import java.util.Scanner
public class Room {
//    TransactionR tRecord;
    private static int roomNumber = 0;
    Tenant[] isAvailable;
    private int kitchen, diningRoom, livingRoom, noOfBed;
    String[] bed;
    //noOfBed is the number of bedrooms
    public Room(Tenant p){
        kitchen = 1;
        diningRoom = 1;
        livingRoom = 1;
        noOfBed = 1;
        bed = new String[noOfBed];
        isAvailable = new Tenant[1];
        isAvailable[1] = p;
        ++roomNumber;
    }
    public Room(Tenant[] p, int noOfBed){
        kitchen = 1;
        diningRoom = 1;
        livingRoom = 1;
        this.noOfBed = noOfBed;
        isAvailable = p;
        bed = whatBeds(noOfBed);
        ++roomNumber;
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
