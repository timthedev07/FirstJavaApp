package GUI.Pages;

import GUI.Control.Router;
import Helper.AutoCompletion;
import Helper.CurrencyExchange;
import Helper.DBConn;
import Helper.Helpers.JFloatField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;

public class CurrencyPanel extends JPanel{
    // Some constant variables
    public static final int YCHANGE = 150;
    public static final int XCHANGE = 60;
    public static final int TITLE_YCHANGE = 0;
    public static final int TITLE_XCHANGE = 0;
    public static final String CONSOLE_RED = "\u001B[31m";
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";
    private static int usdInd;
    private static int eurInd;
    private static JLabel data_source = new JLabel("Data from the European Central Bank");

    // title
    private static JLabel title;
    // label and field for the amount input
    private static JLabel amountLabel;
    private static JFloatField amountField;

    // label and field for the symbol of the money to be converted
    private static JLabel fromLabel;
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
    private static CurrencyExchange exchange;

    // Conversion result;
    private static float conversionResult = 0.0F;

    // Button to home
    private static JButton homeRedirect;

    public CurrencyPanel(int windowWidth, int windowHeight){
        // Initialize the panel
        setLayout(null);
        setSize(windowWidth, windowHeight);


        // Customize my title
        title = new JLabel("REM");
        title.setBounds(
                30+TITLE_XCHANGE,
                30+TITLE_YCHANGE,
                300,
                100);
        title.setName("title");
        title.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 70));
        title.setForeground(new Color(200, 200, 222));

        // Setting the labels and field to lead the user to providing some input value
        amountLabel = new JLabel("Amount of money: ");
        amountLabel.setBounds(10+XCHANGE, 20+YCHANGE, 200, 30);
        amountField = new JFloatField();
        amountField.setActionCommand("updateResult");
        amountField.addActionListener(new CurrencyActionListener());
        amountField.setBounds(10+XCHANGE, 60+YCHANGE, 150, 30);

        // First get the currency list
        Set<Currency> availableCurrencyBuffer = Currency.getAvailableCurrencies();
        int currencySize = availableCurrencyBuffer.size();
        List<String> currStrings = new ArrayList<>(currencySize);
        for(Currency tmp : availableCurrencyBuffer){
            currStrings.add(tmp.toString());
        }
        String[] availableCurrency = new String[currencySize];
        currStrings.toArray(availableCurrency);
        usdInd = currStrings.indexOf("USD");
        eurInd = currStrings.indexOf("EUR");


        // Create the combo box, with USD selected and also create the corresponding label
        fromLabel = new JLabel("To convert from: ");
        fromLabel.setBounds(200+XCHANGE, 20+YCHANGE, 180, 30);

        fromList = new JComboBox(availableCurrency);
        fromList.setSelectedIndex(usdInd);
        fromList.setVisible(true);
        fromList.addActionListener(new CurrencyActionListener());
        fromList.setActionCommand("updateResult");
        fromList.setBounds(200+XCHANGE, 60+YCHANGE, 150, 30);
        AutoCompletion.enable(fromList);


        // Create the next combo box, with EUR selected and also create the corresponding label
        toLabel = new JLabel("To: ");
        toLabel.setBounds(400+XCHANGE, 20+YCHANGE, 180, 30);

        toList = new JComboBox(availableCurrency);
        toList.setSelectedIndex(eurInd);
        toList.setVisible(true);
        toList.addActionListener(new CurrencyActionListener());
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
        resetButton.addActionListener(new CurrencyActionListener());
        resetButton.setActionCommand("resetFields");
        calculateButton.setBounds(140+XCHANGE, 160+YCHANGE, 135, 30);
        calculateButton.addActionListener(new CurrencyActionListener());
        calculateButton.setActionCommand("updateResult");

        // Credit
        data_source.setBounds(30+XCHANGE, 230+YCHANGE, 350, 30);
        data_source.setFont(new Font("Menlos", Font.ITALIC | Font.BOLD, 10));
        data_source.setName("small");

        homeRedirect = new JButton("Home");
        homeRedirect.setActionCommand("homeRedirect");
        homeRedirect.addActionListener(new Router());
        homeRedirect.setBounds(150+XCHANGE, 65+TITLE_YCHANGE, 150, 40);


        exchange = new CurrencyExchange(false);

        // Add everything to the panel
        add(title);
        add(amountLabel);
        add(amountField);
        add(toLabel);
        add(calculateButton);
        add(fromLabel);
        add(fromList);
        add(data_source);
        add(toList);
        add(homeRedirect);
        add(resetButton);
        add(resultLabel);
        add(alert);
        System.out.println(CONSOLE_GREEN+"Finish setting up currency panel"+CONSOLE_RESET);

    }

    public static void resetField(){
        amountField.setText("0");
        fromList.setSelectedIndex(usdInd);
        toList.setSelectedIndex(eurInd);
    }

    public static class CurrencyActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("updateResult")){
                if (amountField.isFloat() || amountField.isInt()){
                    alert.setText("");
                    String resStr = "Result: ";
                    conversionResult = exchange.convert(amountField.getFloat(), String.valueOf(fromList.getSelectedItem()), String.valueOf(toList.getSelectedItem()));
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
