package Helper;

import javax.swing.*;
import java.awt.*;

public class EmptyBox extends JPanel {
    public EmptyBox(int boxWidth, int boxHeight, Color borderColor, int borderWidth){
        setLayout(null);
        setPreferredSize(new Dimension(boxWidth, boxHeight));
        setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
    }

}
