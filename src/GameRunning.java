import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class GameRunning extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JPanel baseScreen;
    private JPanel spriteEnter;
    private JPanel spriteTalks;

    public GameRunning(){
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        createUIComponents();
        cardContainer.add(baseScreen, "BaseScreen");
        cardContainer.add(spriteEnter, "SpriteEnter");
        cardContainer.add(spriteTalks, "SpriteTalks");
        setContentPane(cardContainer);
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
//                JRadioButton one = new JRadioButton("1");
//                JRadioButton two = new JRadioButton("2");
//                JRadioButton three = new JRadioButton("3");
//                ButtonGroup bedrooms = new ButtonGroup();
//                bedrooms.add(one);
//                bedrooms.add(two);
//                bedrooms.add(three);
//
//                JPanel panel = new JPanel(new GridLayout(0, 1));
//                panel.add(new JLabel("Choose number of bedrooms:"));
//                panel.add(one);
//                panel.add(two);
//                panel.add(three);
//
//                panel.add(new JLabel("How many months?"));
//                panel.add(new JTextField());
//
//                int result = JOptionPane.showConfirmDialog(
//                        null,
//                        panel,
//                        "Transaction",
//                        JOptionPane.OK_CANCEL_OPTION,
//                        JOptionPane.PLAIN_MESSAGE
//                );
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
            protected void paintComponent(Graphics g){
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
                if(chatHeadIcon!=null) g.drawImage(chatHeadIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };


    }
}
