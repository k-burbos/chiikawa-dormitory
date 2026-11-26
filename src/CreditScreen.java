import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.net.URL;

public class CreditScreen extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardContainer;
//    private JPanel baseScreen;
    private JLabel creditTitleLbl;
    private JLabel borjaLbl;
    private JLabel burbosLbl;
    private JLabel canonigoLbl;
    private JLabel cornejoLbl;
    private JLabel pescaderoLbl;
    private JLabel gameTitleLbl;
    private JButton backBtn;
    private JPanel backPnl;

    public CreditScreen(){
//        cardLayout = new CardLayout();
//        cardContainer = new JPanel(cardLayout);
//        createUIComponents();
//        cardContainer.add(baseScreen, "BaseScreen");
//        setContentPane(cardContainer);

        backPnl.setPreferredSize(new Dimension(200, 100));
        backPnl.setOpaque(false);
        gameTitleLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 70f));
        gameTitleLbl.setForeground(Color.BLACK);
        creditTitleLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 48f));
        creditTitleLbl.setForeground(Color.BLACK);
        borjaLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 30f));
        borjaLbl.setForeground(Color.BLACK);
        burbosLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 30f));
        burbosLbl.setForeground(Color.BLACK);
        canonigoLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 30f));
        canonigoLbl.setForeground(Color.BLACK);
        cornejoLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 30f));
        cornejoLbl.setForeground(Color.BLACK);
        pescaderoLbl.setFont(loadFont("/Fonts/BaksoSapi.otf", 30f));
        pescaderoLbl.setForeground(Color.BLACK);

        setContentPane(cardContainer);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setTitle("Credits");

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                startScreen.start();
            }
        });

    }

//    public static void main(String[] args) {
//        CreditScreen a = new CreditScreen();
//        a.setContentPane(a.cardContainer);
//        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        a.setVisible(true);
//        a.setSize(1920, 1080);
//        a.setTitle("Credits");
//    }

    private Font loadFont(String path, float size) { // alows using of custom fonts
        try (InputStream in = getClass().getResourceAsStream(path)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, in);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size);
        }
    }

    private void createUIComponents() { // makes the background of the panel into a custom image
        URL url = getClass().getResource("/Images/Backgrounds/creditBG.png");
        final ImageIcon bgIcon = (url != null) ? new ImageIcon(url) : null;

        cardContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(bgIcon == null) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int iw = bgIcon.getIconWidth();
                int ih = bgIcon.getIconHeight();
                double scale = Math.max((double) getWidth() / iw, (double)getHeight() / ih);
                int w = (int) (iw*scale);
                int h = (int) (ih*scale);
                int x = (getWidth() - w) / 2;
                int y = (getHeight() - h) / 2;

                g2.drawImage(bgIcon.getImage(),x, y, w, h, this);
                g2.dispose();
            }
        };
    }

    public static void start(){
        new CreditScreen().setVisible(true);
    }
}
