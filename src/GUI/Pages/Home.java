package GUI.Pages;

import GUI.Control.Router;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {

    // some constant variables
    public static int YCHANGE = 150;
    public static int XCHANGE = 60;
    public static int TITLE_YCHANGE = 0;
    public static int TITLE_XCHANGE = 0;
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";

    private static final JLabel currencyLabel = new JLabel("Currency converter: ");
    private static final JButton currencyRedirect = new JButton("Click here");

    private static final JLabel temperatureLabel = new JLabel("Temperature converter: ");
    private static final JButton temperatureRedirect = new JButton("Click here");

    private static final JLabel loginLabel = new JLabel("Login: ");
    private static final JButton loginRedirect = new JButton("Click here");

    private static final JLabel registerLabel = new JLabel("Register: ");
    private static final JButton registerRedirect = new JButton("Click here");

    private static final JLabel tictactoeLabel = new JLabel("Tic Tac Toe: ");
    private static final JButton tictactoeRedirect = new JButton("Click here");


    public Home(int windowWidth, int windowHeight) {

        setLayout(null);
        setSize(windowWidth, windowHeight);

        // Page title
        // Panels, labels, etc
        JLabel title = new JLabel("REM");
        title.setBounds(
                30 + TITLE_XCHANGE,
                30 + TITLE_YCHANGE,
                300,
                100);
        title.setName("title");
        title.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 70));
        title.setForeground(new Color(200, 200, 222));

        JLabel subtitle = new JLabel("Try out our services:");
        subtitle.setBounds(
                30 + TITLE_XCHANGE,
                100 + TITLE_YCHANGE,
                600,
                100);
        subtitle.setName("subtitle");
        subtitle.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 25));
        subtitle.setForeground(new Color(200, 200, 222));

        currencyRedirect.setBounds(
                250 + XCHANGE,
                20 + YCHANGE,
                165,
                30);
        currencyLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 21));
        currencyLabel.setBounds(5 + XCHANGE, 23 + YCHANGE, 180, 25);
        currencyRedirect.setActionCommand("currencyRedirect");
        currencyRedirect.addActionListener(new Router());

        temperatureRedirect.setBounds(
                250 + XCHANGE,
                50 + YCHANGE,
                165,
                30);
        temperatureLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 21));
        temperatureLabel.setBounds(5 + XCHANGE, 53 + YCHANGE, 250, 25);
        temperatureRedirect.setActionCommand("temperatureRedirect");
        temperatureRedirect.addActionListener(new Router());

        tictactoeRedirect.setBounds(
                250 + XCHANGE,
                80 + YCHANGE,
                165,
                30);
        tictactoeLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 21));
        tictactoeLabel.setBounds(5 + XCHANGE, 83 + YCHANGE, 250, 25);
        tictactoeRedirect.setActionCommand("chooseRedirect");
        tictactoeRedirect.addActionListener(new Router());

        loginRedirect.setBounds(
                250 + XCHANGE,
                110 + YCHANGE,
                165,
                30);
        loginLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 21));
        loginLabel.setBounds(5 + XCHANGE, 113 + YCHANGE, 250, 25);
        loginRedirect.setActionCommand("loginRedirect");
        loginRedirect.addActionListener(new Router());

        registerRedirect.setBounds(
                250 + XCHANGE,
                140 + YCHANGE,
                165,
                30);
        registerLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 21));
        registerLabel.setBounds(5 + XCHANGE, 143 + YCHANGE, 250, 25);
        registerRedirect.setActionCommand("registerRedirect");
        registerRedirect.addActionListener(new Router());

        add(subtitle);
        add(title);
        add(currencyLabel);
        add(currencyRedirect);
        add(temperatureLabel);
        add(temperatureRedirect);
        add(tictactoeLabel);
        add(tictactoeRedirect);
        add(registerLabel);
        add(registerRedirect);
        add(loginLabel);
        add(loginRedirect);
        System.out.println(CONSOLE_GREEN + "Finish setting up the main panel" + CONSOLE_RESET);

    }
}
