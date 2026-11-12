import javax.swing.*;

public class app extends JFrame {
    private JPanel panel1;
    private JButton playButton;
    private JButton exitButton;

    public app() {

    }

    public static void main(String[] args) {
        app a = new app();
        a.setContentPane(a.panel1);
        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
        a.setVisible(true);
        a.setSize(500, 500);
    }
}
