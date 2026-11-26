import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class RoomsScreen extends JFrame{
    private CardLayout cardLayout;
    private JPanel container;
    private JPanel roomsScreen;
    private JPanel roomDetailsScreen;
    private JLabel Room1;
    private JLabel Room2;
    private JLabel Room3;
    private Record records;
    public RoomsScreen(Record records) {
        this.records = records;
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

}
