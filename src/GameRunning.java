import javax.swing.*;
import java.awt.*;
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
        setContentPane(cardContainer);
        populateTenants(); // populate the tenants
        populateRooms();
    }

    void populateTenants() {
        Chii.add(new Tenant.Chiikawa());
        Chii.add(new Tenant.Momonga());
        Chii.add(new Tenant.Hachiware());
        Chii.add(new Tenant.Usagi());
        Chii.add(new Tenant.Furuhonya());
        Chii.add(new Tenant.Kurimanju());
        Chii.add(new Tenant.Shisa());
        Chii.add(new Tenant.Rakko());
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

        URL sprite = getClass().getResource("/Images/ChiikawasSprites/chiikawaAsking_1.png");
        ImageIcon spriteIcon = (sprite != null) ? new ImageIcon(sprite) : null;

        URL chatHead = getClass().getResource("/Images/Elements/bsChatHead.png");
        ImageIcon chatHeadIcon = (chatHead != null) ? new ImageIcon(chatHead) : null;

        URL optionBtn = getClass().getResource("/Images/Elements/bsButton.png");
        ImageIcon optionBtnIcon = (optionBtn != null) ? new ImageIcon(optionBtn) : null;

        baseScreen = new JPanel(){

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(bgIcon!=null) g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                if(tableIcon!=null) g.drawImage(tableIcon.getImage(),0,  10, getWidth(), getHeight(), this);
                if(computerIcon!=null) g.drawImage(computerIcon.getImage(),0,  10, getWidth(), getHeight(), this);
            }
        };

        baseScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "SpriteEnter");
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
        });

        spriteEnter = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(bgIcon!=null) g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                if (spriteIcon != null) {
                    int x = (int) (getWidth() * 0.457f);
                    int y = (int) (getHeight() * 0.209f);

                    int width = (int) (getWidth() * 0.463f);
                    int height = (int) (getHeight() * 0.747f);

                    g.drawImage(spriteIcon.getImage(),
                            x, y, width, height, this);
                }

                if(tableIcon!=null) g.drawImage(tableIcon.getImage(),0,  10, getWidth(), getHeight(), this);
                if(computerIcon!=null) g.drawImage(computerIcon.getImage(),0,  10, getWidth(), getHeight(), this);
            }
        };

        spriteEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "SpriteTalks");
            }
        });
        spriteTalks = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgIcon != null) g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                if (spriteIcon != null) {
                    int x = (int) (getWidth() * 0.457f);
                    int y = (int) (getHeight() * 0.209f);

                    int width = (int) (getWidth() * 0.463f);
                    int height = (int) (getHeight() * 0.747f);

                    g.drawImage(spriteIcon.getImage(),
                            x, y, width, height, this);
                }

                if (tableIcon != null) g.drawImage(tableIcon.getImage(), 0, 10, getWidth(), getHeight(), this);
                if (computerIcon != null) g.drawImage(computerIcon.getImage(), 0, 10, getWidth(), getHeight(), this);
                if (chatHeadIcon != null) g.drawImage(chatHeadIcon.getImage(), 261, -80, 1124, 696, this);
            }
        };

        spriteTalks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "OptionYesNo");
            }
        });
        optionYesNo = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgIcon != null) g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                if (spriteIcon != null) {
                    int x = (int) (getWidth() * 0.457f);
                    int y = (int) (getHeight() * 0.209f);

                    int width = (int) (getWidth() * 0.463f);
                    int height = (int) (getHeight() * 0.747f);

                    g.drawImage(spriteIcon.getImage(),
                            x, y, width, height, this);
                }

                if (tableIcon != null) g.drawImage(tableIcon.getImage(), 0, 10, getWidth(), getHeight(), this);
                if (computerIcon != null) g.drawImage(computerIcon.getImage(), 0, 10, getWidth(), getHeight(), this);
                if (chatHeadIcon != null) g.drawImage(chatHeadIcon.getImage(), 261, -80, 1124, 696, this);
                if(optionBtnIcon!=null) g.drawImage(optionBtnIcon.getImage(), 430, 850, 532, 156, this);
                if(optionBtnIcon!=null) g.drawImage(optionBtnIcon.getImage(), 1010, 850, 532, 156, this);
            }
        };

    }
}
