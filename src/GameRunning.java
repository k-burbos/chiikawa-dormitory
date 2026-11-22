import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

public class GameRunning extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JPanel baseScreen;
    private JPanel spriteEnter;
    private JPanel spriteTalks;
    private JPanel optionYesNo;
    private JLabel optnYes;
    private JLabel optnNo;
    private JLabel lblTalk;
    private JPanel noScreen;
    private JLabel lblNo;
    private boolean showOption = false;

    private ArrayList<Tenant> Chii = new ArrayList<>();
    private ArrayList<Room> Rooms = new ArrayList<>();

    public GameRunning(){
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        createUIComponents();
        cardContainer.add(baseScreen, "BaseScreen");
        cardContainer.add(spriteEnter, "SpriteEnter");
        cardContainer.add(spriteTalks, "SpriteTalks");
        cardContainer.add(optionYesNo, "OptionYesNo");
        cardContainer.add(noScreen, "NoScreen");
        setContentPane(cardContainer);
        populateTenants(); // populate the tenants
        populateRooms();
    }

    private void showTransactionDialog(){
        JRadioButton one = new JRadioButton("Single");
        JRadioButton two = new JRadioButton("Double");
        JRadioButton four = new JRadioButton("Quad");
        ButtonGroup bedrooms = new ButtonGroup();
        bedrooms.add(one);
        bedrooms.add(two);
        bedrooms.add(four);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Choose number of bedrooms:"));
        panel.add(one);
        panel.add(two);
        panel.add(four);

        panel.add(new JLabel("How many months?"));
        panel.add(new JTextField());

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Transaction",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if(result == JOptionPane.OK_OPTION){
            System.out.println("User Pressed OK");
            ButtonModel roomChoice = bedrooms.getSelection();
            if(roomChoice == one){

            }
        } else if(result == JOptionPane.CANCEL_OPTION){
            System.out.println("User Pressed Cancel");
        } else {
            System.out.println("System Closed without selection");
        }
    }

    private Rectangle scaled(JComponent p, double x, double y, double w, double h){
        return new Rectangle(
                (int) (p.getWidth() * x), (int) (p.getHeight() * y), (int)(p.getWidth() * w), (int) (p.getHeight() * h));
    }

    private void drawImage(Graphics g, ImageIcon icon, JComponent p, double x, double y, double w, double h){
        if(icon==null) return;
        Rectangle r = scaled(p, x, y, w, h);
        g.drawImage(icon.getImage(), r.x, r.y, r.width, r.height, p);
    }
    void populateTenants() {
        ChiikawaFactory cf = new ChiikawaFactory();
        Chii.add(cf.createChiikawa("Chiikawa"));
        Chii.add(cf.createChiikawa("Hachiware"));
        Chii.add(cf.createChiikawa("Usagi"));
        Chii.add(cf.createChiikawa("Momonga"));
        Chii.add(cf.createChiikawa("Rakko"));
        Chii.add(cf.createChiikawa("Kurimanju"));
        Chii.add(cf.createChiikawa("Shisa"));
        Chii.add(cf.createChiikawa("Furuhonya"));
    }

    void populateRooms() {
        Rooms.add(new Room.singleRoom());
        Rooms.add(new Room.doubleRoom());
        Rooms.add(new Room.quadRoom());
    }

    public static void main(String[] args) {
        GameRunning a = new GameRunning();
//        a.setContentPane(a.cardContainer);
        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
        a.setVisible(true);
        a.setSize(1920, 1080);
        a.setResizable(false);
        a.setTitle("Chiikawa Dormitory");
    }

    private void createUIComponents() {
        // background
        URL bg = getClass().getResource("/Images/Backgrounds/bgBaseScreen.png");
        ImageIcon bgIcon = (bg != null) ? new ImageIcon(bg) : null;

        //sprite
        URL tableUrl = getClass().getResource("/Images/Elements/bsTable.png");
        ImageIcon tableIcon = (tableUrl != null) ? new ImageIcon(tableUrl) : null;

        URL computerUrl = getClass().getResource("/Images/Elements/bsComputer.png");
        ImageIcon computerIcon = (computerUrl != null) ? new ImageIcon(computerUrl) : null;

        URL spriteA = getClass().getResource("/Images/ChiikawasSprites/chiikawaAsking_1.png");
        ImageIcon askingSprite = (spriteA != null) ? new ImageIcon(spriteA) : null;

        URL spriteS = getClass().getResource("/Images/ChiikawasSprites/chiikawaSad_1.png");
        ImageIcon sadSprite = (spriteS != null) ? new ImageIcon(spriteS) : null;

        URL chatHead = getClass().getResource("/Images/Elements/bsChatHead.png");
        ImageIcon chatHeadIcon = (chatHead != null) ? new ImageIcon(chatHead) : null;

        URL optionBtn = getClass().getResource("/Images/Elements/bsButton.png");
        ImageIcon optionBtnIcon = (optionBtn != null) ? new ImageIcon(optionBtn) : null;

        baseScreen = new JPanel(){

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
            }
        };

        baseScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "SpriteEnter");
            }
        });

        spriteEnter = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, askingSprite, this, 0.457, 0.209, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);

            }
        };
//
        spriteEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "SpriteTalks");
            }
        });
//
//
        spriteTalks = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, askingSprite, this, 0.457, 0.209, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, chatHeadIcon, this, 0.136, -0.074, 0.585, 0.644);
                if (showOption) {
                    drawImage(g, optionBtnIcon, this, 0.224, 0.787, 0.200, 0.180);
                    drawImage(g, optionBtnIcon, this, 0.526, 0.787, 0.200, 0.180);
                }
            }
        };

        spriteTalks.setLayout(null);

        lblTalk = new JLabel("i want one bedroom! uwa");
        lblTalk.setFont(new Font("/Fonts/BaksoSapi.otf", Font.BOLD, 28));
        lblTalk.setForeground(Color.BLACK);
        spriteTalks.add(lblTalk);

        optnYes = new JLabel("YES", SwingConstants.CENTER);
        optnYes.setFont(new Font("/Fonts/BaksoSapi.otf", Font.BOLD, 24));
        optnYes.setForeground(Color.WHITE);
        optnYes.setVisible(false);
        spriteTalks.add(optnYes);

        optnNo = new JLabel("NO", SwingConstants.CENTER);
        optnNo.setFont(new Font("/Fonts/BaksoSapi.otf", Font.BOLD, 24));
        optnNo.setForeground(Color.WHITE);
        optnNo.setVisible(false);
        spriteTalks.add(optnNo);

        spriteTalks.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblTalk.setBounds(scaled(spriteTalks,0.330, 0.085, 0.417, 0.231));
                optnYes.setBounds(scaled(optionYesNo, 0.224, 0.787, 0.200, 0.180));
                optnNo.setBounds(scaled(optionYesNo, 0.526, 0.787, 0.200, 0.180));
            }
        });

        spriteTalks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!showOption){
                    showOption = true;
                    optnNo.setVisible(true);
                    optnYes.setVisible(true);
                    spriteTalks.repaint();
                }
            }
        });

        optnNo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showOption = false;
                optnNo.setVisible(false);
                optnYes.setVisible(false);
                cardLayout.show(cardContainer, "NoScreen");
            }
        });

        noScreen = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, sadSprite, this, 0.457, 0.209, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, chatHeadIcon, this, 0.136, -0.074, 0.585, 0.644);
            }
        };

        noScreen.setLayout(null);
        lblNo = new JLabel("...uww...");
        lblNo.setFont(new Font("Arial", Font.BOLD, 24));
        lblNo.setForeground(Color.BLACK);
        noScreen.add(lblNo);

        noScreen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblNo.setBounds(scaled(noScreen,0.410, 0.085, 0.417, 0.231));
            }
        });

    }
}
