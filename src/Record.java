import java.io.*;
import java.util.ArrayList;

public class Record {
    private ArrayList<Room> rooms = new ArrayList<>();

    public void addRecord(){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("", true))){
            for(Room r: rooms){
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showRecord(){
        try (BufferedReader br = new BufferedReader(new FileReader())){
            String line;
            while((line = br.readLine())!=null){
                System.out.println(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteRecord(){


    }
}
