
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ScrollableDecorator extends JScrollPane {

    public ScrollableDecorator(String text, int width, int height) {

        // Create text area
        JTextArea textArea = new JTextArea(text);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        textArea.setForeground(new Color(75, 48, 35));
        textArea.setBackground(Color.WHITE);

        textArea.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // Add text area to scroll pane
        setViewportView(textArea);

        // Configure scrolling
        setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // Configure dimensions
        setPreferredSize(new Dimension(width, height));

        // Remove default border
        setBorder(null);

        // White background
        setBackground(Color.WHITE);
        getViewport().setBackground(Color.WHITE);
    }
}
