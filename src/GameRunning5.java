import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.io.InputStream; // need it for custom font
import java.util.Enumeration;
import java.util.Random; // makes each request for a room different

public class GameRunning5 extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JPanel baseScreen;
    private JPanel spriteEnter;
    private JPanel spriteTalks;
    private JLabel optnYes;
    private JLabel optnNo;
    private JLabel lblTalk;
    private JPanel noScreen;
    private JLabel lblNo;
    private JPanel yesScreen;
    private JLabel lblYes;
    private JPanel transactionScreen;
    private JLabel lblTransaction;
    private JPanel thankScreen;
    private JLabel lblThank;
    private JPanel rentScreen;
    private JLabel lblRent;
    private JPanel dayOverScreen;
    private boolean showOption = false;
    // This is the glide up function, so the kawas smoothly go up instead of suddenly appearing - BURBOS
    private static final double SPRITE_START_Y = 1.0; // starts off below screen
    private static final double SPRITE_END_Y = 0.209; // where they end up
    private static final int GLIDE_MS = 700; // how fast the kawas r going up
    private static final int POPUP_DELAY_MS = 2000;

    // Rates for the Dorms
    private static final int SINGLE_BASE = 40;
    private static final int SINGLE_MONTHLY = 70;
    private static final int DOUBLE_BASE = 50;
    private static final int DOUBLE_MONTHLY = 80;
    private static final int QUAD_BASE = 80;
    private static final int QUAD_MONTHLY = 120;

    private double spriteYFrac = SPRITE_END_Y;
    private boolean gliding = false;
    private long glideStart;
    private Timer glideTimer;
    // glide down function for when they leave
    private double leaveYFrac = SPRITE_END_Y;
    private boolean leaving = false; // a flag so i know when to toggle off the speech bubble and stop clicking
    private Timer leaveTimer;
    private Font bakso;
    private boolean ending = false; // needs this flag cuz there was a bug where if chiikawa left after no option, they still come back for a second before gamescreen ends
    private final Random random = new Random(); // randomise the request
    private String requestedRoom;
    private int requestedMonths;

    private ArrayList<Tenant> Chii;
    private ArrayList<Room> Rooms;

    public GameRunning5(ArrayList<Tenant> tenants, ArrayList<Room> rooms){
        this.Chii = tenants;
        this.Rooms = rooms;

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        createUIComponents();
        cardContainer.add(baseScreen, "BaseScreen");
        cardContainer.add(spriteEnter, "SpriteEnter");
        cardContainer.add(spriteTalks, "SpriteTalks");
        cardContainer.add(noScreen, "NoScreen");
        cardContainer.add(yesScreen, "YesScreen");
        cardContainer.add(transactionScreen, "TransactionScreen");
        cardContainer.add(thankScreen, "ThankScreen");
        cardContainer.add(rentScreen, "RentScreen");
        cardContainer.add(dayOverScreen, "DayOverScreen");
        setContentPane(cardContainer);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setTitle("Chiikawa Dormitory");
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
        JTextField tf = new JTextField();
        panel.add(tf);

        JRadioButton cash = new JRadioButton("Cash");
        JRadioButton online = new JRadioButton("Online");
        ButtonGroup payments = new ButtonGroup();
        payments.add(cash);
        payments.add(online);

        panel.add(new JLabel("Payment method:"));
        panel.add(cash);
        panel.add(online);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Transaction",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if(result == JOptionPane.CANCEL_OPTION){
                System.out.println("User Pressed Cancel");
                return;
            } else if(result != JOptionPane.OK_OPTION){
                System.out.println("System Closed without selection");
                return;
            }

            int months;
            try {
                months = Integer.parseInt(tf.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panel, "Input a number.");
                continue;
            }

            if(months != requestedMonths) {
                JOptionPane.showMessageDialog(null, "Enter the correct month.");
                continue;
            }

            String roomChoice = getSelectedButtonText(bedrooms);
            if (!requestedRoom.equals(roomChoice)) {
                JOptionPane.showMessageDialog(null, "Not quite right. Try again.");
                continue;
            }

            String paymentChoice = getSelectedButtonText(payments);
            if (paymentChoice == null) {
                JOptionPane.showMessageDialog(null, "Choose a payment method.");
                continue;
            }

            boolean checkedIn = Admin.checkInTenant(
                    this,
                    "Rakko",
                    roomChoice,
                    months,
                    getTotalRent(),
                    paymentChoice
            );

            if (checkedIn) {
                cardLayout.show(cardContainer, "ThankScreen");
            }
            return;
        }
    }

    private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for(Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if(button.isSelected()) return button.getText();
        }
        return null;
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

    private void startSpriteGlide() { // this changes the y of the sprite every 16ms
        if(glideTimer != null) glideTimer.stop(); // in case players keeps clicking so timer will not start twice
        spriteYFrac = SPRITE_START_Y;
        gliding = true;
        glideStart = System.currentTimeMillis();

        glideTimer = new Timer(16, e -> { // every 16ms, runs this code
            double t = (System.currentTimeMillis() - glideStart) / (double) GLIDE_MS;
            if(t >= 1) { // keeps it from overshooting and stops the timer
                t = 1;
                gliding = false;
                glideTimer.stop();
            }
            double eased = 1 - Math.pow(1 - t, 3);
            spriteYFrac = SPRITE_START_Y + (SPRITE_END_Y - SPRITE_START_Y) * eased;
            spriteEnter.repaint(); // makes it so that the sprite reappears, otherwise, visually, nothing happens
        });
        glideTimer.start();
    }

    private void startSpriteLeave(JPanel screen, JLabel label, String nextCard) {
        if(leaving) return; // if its already leaving, ignore any clicks
        leaving = true;
        label.setVisible(false); // Rich's thank you text disappears
        long start = System.currentTimeMillis();

        leaveTimer = new Timer(16, e -> {
            double t = (System.currentTimeMillis() - start) / (double) GLIDE_MS;
            if (t > 1) t = 1;
            double eased = t * t * t; // basically the transition, just makes it speed up
            leaveYFrac = SPRITE_END_Y + (SPRITE_START_Y - SPRITE_END_Y) * eased;
            screen.repaint();

            if (t >= 1) {
                leaveTimer.stop();
                leaving = false;
                leaveYFrac = SPRITE_END_Y;
                label.setVisible(true);
                cardLayout.show(cardContainer, nextCard); // had to change this one kasi they leave at no and after thank you

                ending = true;
                Timer popupTimer = new Timer(POPUP_DELAY_MS,ev -> {
                    lblRent.setVisible(false);
                    JOptionPane.showMessageDialog(
                            this,
                            "You have no tenants due to leave.",
                            "Record of Tenants",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    dispose();
                });
                popupTimer.setRepeats(false);
                popupTimer.start();
            }
        });
        leaveTimer.start();
    }

    private int getTotalRent(){
        switch(requestedRoom) {
            case "Single":
                return SINGLE_BASE + SINGLE_MONTHLY * requestedMonths;
            case "Double":
                return DOUBLE_BASE + DOUBLE_MONTHLY * requestedMonths;
            default:
                return QUAD_BASE + QUAD_MONTHLY * requestedMonths;
        }
    }

    private void pickRandomRequest() {
        requestedMonths = random.nextInt(6) + 1; // basically one month or six
        String monthText = (requestedMonths == 1) ? "1 month" : requestedMonths + " months";

        String talk; // before they pick yes
        String transaction; // what they say after you pick yes

        switch(random.nextInt(3)) { // randomizing room request
            case 0:
                requestedRoom = "Single";
                talk = "Greetings, I'd like one bedroom for myself.";
                transaction = "I want one bedroom for " + monthText + "!";
                break;
            case 1:
                requestedRoom = "Double";
                talk = "Greetings, I would like two bedrooms for me and Hachiware.";
                transaction = "I want two bedrooms for " + monthText + "!";
                break;
            default:
                requestedRoom = "Quad";
                talk = "Greetings, I'd like four bedrooms for Usagi, Chiikawa, Hachiware, and me.";
                transaction = "I want four bedrooms for " + monthText + "!";
                break;
        }

        lblTalk.setText(bubbleText(talk));
        lblTransaction.setText(bubbleText(transaction));
        lblRent.setText("Rakko has paid PHP " + getTotalRent() + " for rent.");
    }

    private Font loadBakso() { // allows me to use the new font, used this in startScreen too
        try (InputStream is = getClass().getResourceAsStream("/Fonts/BaksoSapi.otf")) {
            if(is == null) throw new Exception("Font file not found");
            Font f = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial",Font.BOLD, 24);
        }

    }

    private Rectangle bubbleBounds(JComponent p) { // making this so that there's a box around the speech bubble, text won't go anywhere but there
        return scaled(p, 0.335, 0.10, 0.215, 0.20);
    }

    private String bubbleText(String s) {
        return "<html><center>" + s + "</center></html>"; // wraps the text so the text won't cut off
    }

    private Rectangle xBtnBounds(JPanel p){
        return new Rectangle(
                (int) (p.getWidth()  * 0.95),
                (int) (p.getHeight() * 0.02),
                (int) (p.getWidth()  * 0.03),
                (int) (p.getHeight() * 0.05));
    }

    private void addXClick(JPanel panel, Runnable action){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(xBtnBounds(panel).contains(e.getPoint())){
                    action.run();
                }
            }
        });
    }

    private boolean onX(JPanel p, MouseEvent e){
        return xBtnBounds(p).contains(e.getPoint());
    }

    private void createUIComponents() {
        // font
        bakso = loadBakso();
        // background
        URL bg = getClass().getResource("/Images/Backgrounds/bgBaseScreen.png");
        ImageIcon bgIcon = (bg != null) ? new ImageIcon(bg) : null;

        //sprite
        URL tableUrl = getClass().getResource("/Images/Elements/bsTable.png");
        ImageIcon tableIcon = (tableUrl != null) ? new ImageIcon(tableUrl) : null;

        URL computerUrl = getClass().getResource("/Images/Elements/bsComputer.png");
        ImageIcon computerIcon = (computerUrl != null) ? new ImageIcon(computerUrl) : null;

        URL spriteA = getClass().getResource("/Images/ChiikawasSprites/chiikawaAsking_6.png");
        ImageIcon askingSprite = (spriteA != null) ? new ImageIcon(spriteA) : null;

        URL spriteS = getClass().getResource("/Images/ChiikawasSprites/chiikawaSad_6.png");
        ImageIcon sadSprite = (spriteS != null) ? new ImageIcon(spriteS) : null;

        URL spriteH = getClass().getResource("/Images/ChiikawasSprites/chiikawaHappy_6.png");
        ImageIcon happySprite = (spriteH != null) ? new ImageIcon(spriteH) : null;

        URL chatHead = getClass().getResource("/Images/Elements/bsChatHead.png");
        ImageIcon chatHeadIcon = (chatHead != null) ? new ImageIcon(chatHead) : null;

        URL optionBtn = getClass().getResource("/Images/Elements/bsButton.png");
        ImageIcon optionBtnIcon = (optionBtn != null) ? new ImageIcon(optionBtn) : null;

        URL Xbtn = getClass().getResource("/Images/X.png");
        ImageIcon XbtnIcon = (Xbtn != null) ? new ImageIcon(Xbtn) : null;

        baseScreen = new JPanel(){

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        baseScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(ending) return; // fixed the bug of chiikawa gliding up if you say no
                pickRandomRequest();
                cardLayout.show(cardContainer, "SpriteEnter");
                startSpriteGlide();
            }
        });

        spriteEnter = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, askingSprite, this, 0.457, spriteYFrac, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);

            }
        };
//
        spriteEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(gliding) return;
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
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        spriteTalks.setLayout(null);

        lblTalk = new JLabel(bubbleText("Uuww... I'd like to live with Momonga!"));
        lblTalk.setHorizontalAlignment(SwingConstants.CENTER);
        lblTalk.setVerticalAlignment(SwingConstants.CENTER);
        lblTalk.setFont(bakso.deriveFont(Font.PLAIN, 28f));
        lblTalk.setForeground(Color.BLACK);
        spriteTalks.add(lblTalk);

        optnYes = new JLabel("YES", SwingConstants.CENTER);
        optnYes.setFont(bakso.deriveFont(Font.PLAIN, 24f));
        optnYes.setForeground(Color.WHITE);
        optnYes.setVisible(false);
        spriteTalks.add(optnYes);

        optnNo = new JLabel("NO", SwingConstants.CENTER);
        optnNo.setFont(bakso.deriveFont(Font.PLAIN, 24f));
        optnNo.setForeground(Color.WHITE);
        optnNo.setVisible(false);
        spriteTalks.add(optnNo);

        spriteTalks.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblTalk.setBounds(bubbleBounds(spriteTalks));
                optnYes.setBounds(scaled(spriteTalks, 0.224, 0.787, 0.200, 0.180));
                optnNo.setBounds(scaled(spriteTalks, 0.526, 0.787, 0.200, 0.180));
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

        //no screen
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
                drawImage(g, sadSprite, this, 0.457, leaveYFrac, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                if(!leaving) drawImage(g, chatHeadIcon, this, 0.136, -0.074, 0.585, 0.644);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        noScreen.setLayout(null);
        lblNo = new JLabel(bubbleText("I understand."));
        lblNo.setHorizontalAlignment(SwingConstants.CENTER);
        lblNo.setVerticalAlignment(SwingConstants.CENTER);
        lblNo.setFont(bakso.deriveFont(Font.PLAIN, 24f));
        lblNo.setForeground(Color.BLACK);
        noScreen.add(lblNo);

        noScreen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblNo.setBounds(bubbleBounds(noScreen));
            }
        });

        noScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startSpriteLeave(noScreen, lblNo, "BaseScreen");
            }
        });
        //yes screen

        optnYes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showOption = false;
                optnNo.setVisible(false);
                optnYes.setVisible(false);
                cardLayout.show(cardContainer, "YesScreen");
            }
        });

        yesScreen = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, happySprite, this, 0.457, 0.209, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, chatHeadIcon, this, 0.136, -0.074, 0.585, 0.644);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        yesScreen.setLayout(null);
        lblYes = new JLabel(bubbleText("Thank you."));
        lblYes.setHorizontalAlignment(SwingConstants.CENTER);
        lblYes.setVerticalAlignment(SwingConstants.CENTER);
        lblYes.setFont(bakso.deriveFont(Font.PLAIN, 24f));
        lblYes.setForeground(Color.BLACK);
        yesScreen.add(lblYes);

        yesScreen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblYes.setBounds(bubbleBounds(yesScreen));
            }
        });


        //transaction screen
        yesScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardContainer, "TransactionScreen");
            }
        });

        transactionScreen = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, happySprite, this, 0.457, 0.209, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, chatHeadIcon, this, 0.136, -0.074, 0.585, 0.644);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        transactionScreen.setLayout(null);
        lblTransaction = new JLabel(bubbleText("i want two bedrooms for 2 months! uwa"));
        lblTransaction.setHorizontalAlignment(SwingConstants.CENTER);
        lblTransaction.setVerticalAlignment(SwingConstants.CENTER);
        lblTransaction.setFont(bakso.deriveFont(Font.PLAIN, 20f));
        lblTransaction.setForeground(Color.BLACK);
        transactionScreen.add(lblTransaction);

        transactionScreen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblTransaction.setBounds(bubbleBounds(transactionScreen));
            }
        });

        transactionScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTransactionDialog();
            }
        });

        thankScreen = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, happySprite, this, 0.457, leaveYFrac, 0.463, 0.747);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                if (!leaving) drawImage(g, chatHeadIcon, this, 0.136, -0.074, 0.585, 0.644);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        thankScreen.setLayout(null);
        lblThank = new JLabel(bubbleText("Thank you...!"));
        lblThank.setHorizontalAlignment(SwingConstants.CENTER);
        lblThank.setVerticalAlignment(SwingConstants.CENTER);
        lblThank.setFont(bakso.deriveFont(Font.PLAIN, 24f));
        lblThank.setForeground(Color.BLACK);
        thankScreen.add(lblThank);

        thankScreen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblThank.setBounds(bubbleBounds(thankScreen));
            }
        });

        thankScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startSpriteLeave(thankScreen, lblThank, "RentScreen");
            }
        });

        rentScreen = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        rentScreen.setLayout(null);
        lblRent = new JLabel("Chiikawa has paid [totalfee] for rent."); // temporary
        lblRent.setFont(bakso.deriveFont(Font.PLAIN, 24f));
        lblRent.setForeground(Color.BLACK);
        rentScreen.add(lblRent);

        rentScreen.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lblRent.setBounds(scaled(rentScreen,0, -0.450, 1, 1));
            }
        });

        rentScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(ending)return; // fixes chiikawa bug too
                cardLayout.show(cardContainer, "DayOverScreen");
            }
        });

        dayOverScreen = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImage(g, bgIcon, this, 0, 0, 1, 1);
                drawImage(g, tableIcon, this, 0, 0, 1, 1);
                drawImage(g, computerIcon, this, 0, 0, 1, 1);
                drawImage(g, XbtnIcon, this, 0.95, 0.02, 0.03, 0.05);
            }
        };

        JPanel[] screens = {baseScreen, spriteEnter, spriteTalks, noScreen,
                yesScreen, transactionScreen, thankScreen, rentScreen, dayOverScreen};

        for(JPanel s:screens){
            addXClick(s, ()-> {
                for (java.awt.event.WindowListener wl : getWindowListeners()) {
                    removeWindowListener(wl);   // stops GameManager from opening the next screen
                }
                new startScreen().setVisible(true);
                dispose();
            });
        }

    }
}
