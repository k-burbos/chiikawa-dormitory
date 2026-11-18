import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameRunning extends JFrame{
    private JPanel cardContainer;
    private JPanel baseScreen;
    private JPanel spriteEnter;

    public GameRunning(){
        cardContainer = new JPanel(new CardLayout());
        createUIComponents();
        cardContainer.add(baseScreen, "BaseScreen");
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
        URL bg = getClass().getResource("/Images/bgBaseScreen.png");
        ImageIcon bgIcon = (bg != null) ? new ImageIcon(bg) : null;

        //sprite
        URL tableUrl = getClass().getResource("/Images/bsTable.png");
        ImageIcon tableIcon = (tableUrl != null) ? new ImageIcon(tableUrl) : null;

        baseScreen = new JPanel(){

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(bgIcon!=null) g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                if(tableIcon!=null) g.drawImage(tableIcon.getImage(),0,  10, getWidth(), getHeight(), this);
            }
        };

    }
}
