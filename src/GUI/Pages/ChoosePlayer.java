package GUI.Pages;

import GUI.Control;
import Helper.WrapLayout;

import javax.swing.*;
import java.awt.*;

public class ChoosePlayer extends JPanel {
    JLabel title = new JLabel("Choose player");
    JButton X = new JButton("X");
    JButton O = new JButton("O");
    public ChoosePlayer(){
        WrapLayout layout = new WrapLayout();
        setLayout(layout);

        title.setName("title");
        title.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 70));

        JPanel buttons = new JPanel();
        X.setActionCommand("chooseX");
        O.setActionCommand("chooseO");
        X.addActionListener(new Control.Router());
        O.addActionListener(new Control.Router());
        buttons.setPreferredSize(new Dimension(550, 50));
        add(title);
        buttons.add(X);
        buttons.add(O);
        add(buttons);
    }
}
