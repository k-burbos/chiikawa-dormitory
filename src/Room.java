import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Room {
    //    TransactionR tRecord;
    private static int roomNumber = 0;
    private List<Tenant> isAvailable = new ArrayList<>();
    private int kitchen, diningRoom, livingRoom, noOfBed;
    public Room(int noOfBed){
        kitchen = 1;
        diningRoom = 1;
        livingRoom = 1;
        this.noOfBed = noOfBed;
        ++roomNumber;
    }

    public static class singleRoom extends Room {
        public singleRoom() {
            super(1);
        }
    }
    public static class doubleRoom extends Room {
        public doubleRoom(){
            super(2);
        }
    }
    public static class quadRoom extends Room {

    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder("");
        for(Tenant p : isAvailable){
            sb.append(p.getName()+",");
        }
        return roomNumber + "," + noOfBed + "LDK,"+sb;
    }

}
