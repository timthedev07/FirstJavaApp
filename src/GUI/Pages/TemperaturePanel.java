package GUI.Pages;

import GUI.Control.Router;
import Helper.AutoCompletion;
import Helper.TemperatureConverter;
import Helper.Helpers.JFloatField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class TemperaturePanel extends JPanel{
    // Some constant variables
    public static final int YCHANGE = 150;
    public static final int XCHANGE = 60;
    public static final int TITLE_YCHANGE = 0;
    public static final int TITLE_XCHANGE = 0;
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";
    private static int cInd;
    private static int fInd;

    private static JFloatField amountField;

    private static JComboBox fromList;

    // label and field for the symbol of the converted money
    private static JComboBox toList;
    private static JLabel toLabel;

    // and display the result/alert
    private static JLabel resultLabel = new JLabel("Result: ");
    private static JLabel alert = new JLabel();

    // Reset/calculate button
    private static JButton resetButton = new JButton("Reset");
    private static JButton calculateButton = new JButton("Calculate");

    // Setting up converter
    private static TemperatureConverter converter;

    // Conversion result;
    private static float conversionResult = 0.0F;

    // Button to home
    private static JButton homeRedirect;

    public TemperaturePanel(int windowWidth, int windowHeight){
        // Initialize the panel
        setLayout(null);
        setSize(windowWidth, windowHeight);


        // Customize my title
        // title
        JLabel title = new JLabel("REM");
        title.setBounds(
                30+TITLE_XCHANGE,
                30+TITLE_YCHANGE,
                300,
                100);
        title.setName("title");
        title.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 70));
        title.setForeground(new Color(200, 200, 222));

        // Setting the labels and field to lead the user to providing some input value
        // label and field for the amount input
        JLabel amountLabel = new JLabel("Temperature of: ");
        amountLabel.setBounds(10+XCHANGE, 20+YCHANGE, 200, 30);
        amountField = new JFloatField();
        amountField.setActionCommand("updateResult");
        amountField.addActionListener(new TemperatureActionListener());
        amountField.setBounds(10+XCHANGE, 60+YCHANGE, 150, 30);

        // Create the list with all of the temperature units
        String[] buffer = {"C", "F", "K", "Ra", "Re"};
        List<String> units = Arrays.asList(buffer);
        cInd = units.indexOf("C");
        fInd = units.indexOf("F");


        // Create the combo box, with USD selected and also create the corresponding label
        // label and field for the symbol of the money to be converted
        JLabel fromLabel = new JLabel("To be converted from: ");
        fromLabel.setBounds(200+XCHANGE, 20+YCHANGE, 180, 30);

        fromList = new JComboBox(buffer);
        fromList.setSelectedIndex(cInd);
        fromList.setVisible(true);
        fromList.addActionListener(new TemperatureActionListener());
        fromList.setActionCommand("updateResult");
        fromList.setBounds(200+XCHANGE, 60+YCHANGE, 150, 30);
        AutoCompletion.enable(fromList);


        // Create the next combo box, with EUR selected and also create the corresponding label
        toLabel = new JLabel("To: ");
        toLabel.setBounds(400+XCHANGE, 20+YCHANGE, 180, 30);

        toList = new JComboBox(buffer);
        toList.setSelectedIndex(fInd);
        toList.setVisible(true);
        toList.addActionListener(new TemperatureActionListener());
        toList.setActionCommand("updateResult");
        toList.setBounds(400+XCHANGE, 60+YCHANGE, 150, 30);
        AutoCompletion.enable(toList);

        // configure the result/alert
        resultLabel.setBounds(320+XCHANGE, 160+YCHANGE, 200, 40);
        resultLabel.setFont(new Font("Menlos", Font.BOLD | Font.ITALIC, 18));
        alert.setForeground(Color.RED);
        alert.setBounds(320+XCHANGE, 130+YCHANGE, 200, 40);

        // Reset button
        resetButton.setBounds(10+XCHANGE, 160+YCHANGE, 105, 30);
        resetButton.addActionListener(new TemperatureActionListener());
        resetButton.setActionCommand("resetFields");
        calculateButton.setBounds(140+XCHANGE, 160+YCHANGE, 135, 30);
        calculateButton.addActionListener(new TemperatureActionListener());
        calculateButton.setActionCommand("updateResult");

        homeRedirect = new JButton("Home");
        homeRedirect.setActionCommand("homeRedirect");
        homeRedirect.addActionListener(new Router());
        homeRedirect.setBounds(150+XCHANGE, 65+TITLE_YCHANGE, 150, 40);

        converter = new TemperatureConverter();



        // Add everything to the panel
        add(title);
        add(amountLabel);
        add(amountField);
        add(toLabel);
        add(calculateButton);
        add(fromLabel);
        add(fromList);
        add(toList);
        add(homeRedirect);
        add(resetButton);
        add(resultLabel);
        add(alert);
        System.out.println(CONSOLE_GREEN+"Finish setting up temperature panel"+CONSOLE_RESET);

    }

    public static void resetField(){
        amountField.setText("0");
        fromList.setSelectedIndex(cInd);
        toList.setSelectedIndex(fInd);
    }

    public static class TemperatureActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("updateResult")){
                if (amountField.isFloat() || amountField.isInt()){
                    alert.setText("");
                    String resStr = "Result: ";
                    conversionResult = converter.convert(amountField.getFloat(), String.valueOf(fromList.getSelectedItem()), String.valueOf(toList.getSelectedItem()));
                    resultLabel.setText(resStr+conversionResult);

                } else {
                    alert.setText("Invalid value!");
                }
            } else if (command.equals("resetFields")){
                resetField();
            }
        }

    }

}
