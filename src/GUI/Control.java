package GUI;

import GUI.Pages.*;
import GUI.Pages.TicTacToe;
import Helper.CurrencyExchange;
import Helper.CurrencyUpdate;
import Helper.DBConn;
import Helper.TicTacToeLogic;

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
import java.util.Date;


public class Control extends JFrame {
    public final static int WINDOW_WIDTH = 750;
    public final static int WINDOW_HEIGHT = 550;

    // Defining the panel names
    final static String LOGIN_PANEL_NAME = "login";
    final static String HOME_PANEL_NAME = "home";
    final static String TEMPERATURE_PANEL_NAME = "temperature";
    final static String REGISTER_PANEL_NAME = "register";
    final static String CURRENCY_PANEL_NAME = "currency";
    final static String TICTACTOE_PANEL_NAME = "tictactoe";
    final static String CHOOSE_PLAYER_PANEL_NAME = "choose";

    private static CurrencyPanel currency_panel;
    private static TemperaturePanel temperature_panel;
    private static TicTacToe tictactoe_panel;

    // declaring the frame and the main panel
    public static JPanel mainPanel = new JPanel();

    private static boolean updateDB = false;

    public Control() {
        // Giant *of code to configure the theme
        UIManager.put("control", new Color(50, 50, 62));
        UIManager.put("info", new Color(128, 128, 128));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(115, 164, 209));
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
        UIManager.put("text", new Color(230, 230, 230));
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (javax.swing.UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Bringing in the component panels
        LoginPanel login_panel = new LoginPanel(WINDOW_WIDTH, WINDOW_HEIGHT);
        RegisterPanel register_panel = new RegisterPanel(WINDOW_WIDTH, WINDOW_HEIGHT);
        Home home_panel = new Home(WINDOW_WIDTH, WINDOW_HEIGHT);
        currency_panel = new CurrencyPanel(WINDOW_WIDTH, WINDOW_HEIGHT);
        temperature_panel = new TemperaturePanel(WINDOW_WIDTH, WINDOW_HEIGHT);
        ChoosePlayer choosePlayer_panel = new ChoosePlayer();

        // Setting up the connection
        Connection conn = DBConn.connection("currency");
        PreparedStatement ps = null;
        ResultSet res = null;

        // Date formatter
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Getting the date string for today
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = dateFormat.format(new Date());

        try {
            ps = conn.prepareStatement("SELECT * FROM logs");
            res = ps.executeQuery();
            String dateString = res.getString("date");
            try {
                conn.close();
                res.close();
                ps.close();
            } catch (SQLException ee){

            }

            LocalDate date1 = LocalDate.parse(dateString, dtf);
            LocalDate today = LocalDate.parse(todayString, dtf);
            final long days = Math.abs(ChronoUnit.DAYS.between(today, date1));
            if (days > 0){
                CurrencyUpdate.update();
            }

        } catch(SQLException eee){

        } finally {

        }

        // organize the font
        changeFont(login_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
        changeFont(temperature_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
        changeFont(register_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
        changeFont(home_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
        changeFont(currency_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
        changeFont(choosePlayer_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));

        // Setting up the card layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        mainPanel.add(home_panel, HOME_PANEL_NAME);
        mainPanel.add(currency_panel, CURRENCY_PANEL_NAME);
        mainPanel.add(register_panel, REGISTER_PANEL_NAME);
        mainPanel.add(login_panel, LOGIN_PANEL_NAME);
        mainPanel.add(temperature_panel, TEMPERATURE_PANEL_NAME);
        mainPanel.add(choosePlayer_panel, CHOOSE_PLAYER_PANEL_NAME);

        // Setting up the window with constants defined above
        add(mainPanel);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("My First App");
        setLocationByPlatform(true);
        setVisible(false);
    }

    public static void changeFont(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                try {
                    if (child.getName().equals("title") || child.getName().equals("small")) {
                        continue;
                    }
                } catch (Exception e) {
                    changeFont(child, font);
                }

            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Control();
        frame.setVisible(true);
    }

    public static class CommunicationWithHome {
        public static String from;

        public CommunicationWithHome(String panelName) {
            from = panelName;
        }

        public void sendMessage(String message) {
            if (message.equals("autoRedirectHome")) {
                JOptionPane.showMessageDialog(null, "Successfully logged in/registered. You'll soon be directed to the home page.");
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, HOME_PANEL_NAME);
            }
        }
    }

    public static class Router implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("registerRedirect")) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, REGISTER_PANEL_NAME);
            } else if (command.equals("loginRedirect")) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, LOGIN_PANEL_NAME);
            } else if (command.equals("currencyRedirect")) {

                CardLayout cl = (CardLayout) mainPanel.getLayout();
                currency_panel.resetField();
                cl.show(mainPanel, CURRENCY_PANEL_NAME);
            } else if (command.equals("homeRedirect")) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, HOME_PANEL_NAME);
            } else if (command.equals("temperatureRedirect")) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                temperature_panel.resetField();
                cl.show(mainPanel, TEMPERATURE_PANEL_NAME);
            } else if (command.equals("tictactoeRedirect")) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, TICTACTOE_PANEL_NAME);
            } else if (command.equals("chooseRedirect")){
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, CHOOSE_PLAYER_PANEL_NAME);
            } else if (command.equals("chooseX")){
                tictactoe_panel = new TicTacToe(WINDOW_WIDTH, WINDOW_HEIGHT, TicTacToeLogic.X);
                mainPanel.add(tictactoe_panel, TICTACTOE_PANEL_NAME);
                changeFont(tictactoe_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, TICTACTOE_PANEL_NAME);
            } else if (command.equals("chooseO")){
                tictactoe_panel = new TicTacToe(WINDOW_WIDTH, WINDOW_HEIGHT, TicTacToeLogic.O);
                mainPanel.add(tictactoe_panel, TICTACTOE_PANEL_NAME);
                changeFont(tictactoe_panel, new Font("Menlo", Font.ITALIC | Font.BOLD, 15));
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, TICTACTOE_PANEL_NAME);
            }

        }
    }
}
