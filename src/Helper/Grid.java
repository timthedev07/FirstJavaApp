package Helper;
import javax.swing.*;
import java.awt.*;
import Helper.TicTacToeBoard;

public class Grid extends EmptyBox{

    /**
     * Generates a grid with a button saying empty
     * @param boxWidth
     * @param boxHeight
     * @param borderColor
     * @param borderWidth
     * @param buttonText
     * @param id
     */
    public Grid(int boxWidth, int boxHeight, Color borderColor, int borderWidth, String buttonText, String id) {
        super(boxWidth, boxHeight, borderColor, borderWidth);

        JButton button = new JButton(buttonText);
        button.setBounds(10, 10, 80, 80);
        button.setActionCommand(id);
        button.setFont(new Font("menlo", Font.ITALIC | Font.BOLD, 15));

        button.addActionListener(new TicTacToeBoard());
        add(button);

    }

    /**
     * Generates a grid that has a label(displaying the given text) rather than a clickable button.
     * @param boxWidth
     * @param boxHeight
     * @param borderColor
     * @param borderWidth
     * @param text
     */
    public Grid(int boxWidth, int boxHeight, Color borderColor, int borderWidth, String text) {
        super(boxWidth, boxHeight, borderColor, borderWidth);
        // Creating label
        JLabel label = new JLabel(text);
        label.setFont(new Font("menlo", Font.ITALIC | Font.BOLD, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(boxWidth, boxHeight));
        add(label, BorderLayout.CENTER);


    }


}
