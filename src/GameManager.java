import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private static final ArrayList<Tenant> tenants = new ArrayList<>();
    private static final ArrayList<Room> rooms = new ArrayList<>();
    private static final Random random = new Random();
    private static boolean running = true;

    static { // makes it run exactly once
        ChiikawaFactory cf = new ChiikawaFactory();
        tenants.add(cf.createChiikawa("Chiikawa"));
        tenants.add(cf.createChiikawa("Hachiware"));
        tenants.add(cf.createChiikawa("Usagi"));
        tenants.add(cf.createChiikawa("Momonga"));
        tenants.add(cf.createChiikawa("Rakko"));
        tenants.add(cf.createChiikawa("Kurimanju"));
        tenants.add(cf.createChiikawa("Shisa"));
        tenants.add(cf.createChiikawa("Furuhonya"));

        rooms.add(new Room.singleRoom());
        rooms.add(new Room.doubleRoom());
        rooms.add(new Room.quadRoom());
    }

    public static void start() {
        openNextScreen();
    }

    public static void stop() {
        running = false;
    }

    private static void openNextScreen() {
        if (!running) return;
        JFrame screen = pickScreen();
        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) { // basically if the gamescreen ends, load another
                openNextScreen();
            }
        });
        screen.setVisible(true);
    }

    private static JFrame pickScreen() {
        switch (random.nextInt(8)) {
            case 0: return new GameRunning(tenants, rooms);   // Chiikawa
            case 1: return new GameRunning1(tenants, rooms);  // Hachiware
            case 2: return new GameRunning2(tenants, rooms);  // Usagi
            case 3: return new GameRunning3(tenants, rooms);  // Momonga
            case 4: return new GameRunning4(tenants, rooms);  // Furuhonya (crab)
            case 5: return new GameRunning5(tenants, rooms);  // Rakko
            case 6: return new GameRunning6(tenants, rooms);  // Shisa
            default: return new GameRunning(tenants, rooms); // Kurimanju
        }
    }
}
