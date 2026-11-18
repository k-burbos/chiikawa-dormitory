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

    public GameRunning(){
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        createUIComponents();
        cardContainer.add(baseScreen, "BaseScreen");
        cardContainer.add(spriteEnter, "SpriteEnter");
        setContentPane(cardContainer);
    }

    public static void main(String[] args) {
        GameRunning a = new GameRunning();
//        a.setContentPane(a.cardContainer);
        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
        a.setVisible(true);
        a.setSize(735, 455);
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

        URL sprite = getClass().getResource("/Images/ChiikawasSprites/chiikawaHappy_1.png");
        ImageIcon spriteIcon = (sprite != null) ? new ImageIcon(sprite) : null;


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
            }
        });

        spriteEnter = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(bgIcon!=null) g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                if(spriteIcon!=null) g.drawImage(spriteIcon.getImage(),0, 0, this);
                if(tableIcon!=null) g.drawImage(tableIcon.getImage(),0,  10, getWidth(), getHeight(), this);
                if(computerIcon!=null) g.drawImage(computerIcon.getImage(),0,  10, getWidth(), getHeight(), this);
            }
        };
    }
}
