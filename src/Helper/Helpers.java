package Helper;

import javax.swing.*;
import java.awt.*;

public class Helpers {
    public static class JFloatField extends JFormattedTextField{

        public boolean isFloat(){
            String content = this.getText();
            try{
                float f = Float.parseFloat(content);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }

        public boolean isInt(){
            String content = this.getText();
            try{
                float f = Integer.parseInt(content);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }

        /**
         * Returns the floating number of the current field.<br>
         *     Returns -1.0F if the field does not fully consists of a float or int;
         * @return
         */
        public float getFloat(){
            if (!this.isInt() || !this.isFloat()){
                return -1.0F;
            }
            return Float.parseFloat(this.getText());
        }
    }

    public static int[] intToCoorArr(int i){
        int[] res;
        if (i == 0){
            res = new int[]{0, 0};
        } else if (i == 1){
            res = new int[]{0, 1};
        } else if (i == 2){
            res = new int[]{0, 2};
        } else if (i == 3){
            res = new int[]{1, 0};
        } else if (i == 4){
            res = new int[]{1, 1};
        } else if (i == 5){
            res = new int[]{1, 2};
        } else if (i == 6){
            res = new int[]{2, 0};
        } else if (i == 7){
            res = new int[]{2, 1};
        } else if (i == 8){
            res = new int[]{2, 2};
        } else {
            return null;
        }
        return res;
    }

    public static String intToCoorStr(int i){
        String res;
        if (i == 0){
            res = "00";
        } else if (i == 1){
            res = "01";
        } else if (i == 2){
            res = "02";
        } else if (i == 3){
            res = "10";
        } else if (i == 4){
            res = "11";
        } else if (i == 5){
            res = "12";
        } else if (i == 6){
            res = "20";
        } else if (i == 7){
            res = "21";
        } else if (i == 8){
            res = "22";
        } else {
            return null;
        }
        return res;
    }

    public static JLabel generateTitle(){
        JLabel title;
        title = new JLabel("REM  ");
        title.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 70));
        title.setForeground(new Color(200, 200, 222));
        title.setName("title");
        title.setSize(400, 80);
        return title;
    }


}
