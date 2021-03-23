package GUI.Pages;

import Helper.*;
import GUI.Control;
import GUI.Control.Router;
import Helper.DBConn;
import Helper.Hash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class LoginPanel extends JPanel {

    public static Control.CommunicationWithHome communication = new Control.CommunicationWithHome("register");


    // This is to move the entire `div` of things according to the context
    public static int YCHANGE = 150;
    public static int XCHANGE = 60;

    // Some labels, button, groups and fields
    private static JLabel usernameLabel;
    private static JTextField usernameInputField;
    private static JLabel passwordLabel;
    private static JPasswordField passwordInputField;
    private static JButton submit;
    private static JLabel message;
    private static JLabel title = Helpers.generateTitle();
    private static JButton registerRedirect = new JButton("register");
    private static JLabel noAccount = new JLabel("No account? Register here: ");

    // Console color print
    public static final String CONSOLE_RED = "\u001B[31m";
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";

    public LoginPanel(int windowWidth, int windowHeight) {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(windowWidth - 200, windowHeight - 500));


        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new FlowLayout());
        titleContainer.add(title);


        usernameLabel = new JLabel("Username: ");
        usernameInputField = new JTextField(50);

        // The password row
        passwordLabel = new JLabel("Password: ");
        passwordInputField = new JPasswordField(150);

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        form.add(usernameLabel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        form.add(passwordLabel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        form.add(usernameInputField, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        form.add(passwordInputField, c);

        // The message label, the text depends on the query's result.
        message = new JLabel();

        // Configuring the submit button
        submit = new JButton("Sign in");
        submit.addActionListener(new LoginActionListener());
        submit.setActionCommand("login");

        // Configuring the redirecting button
        registerRedirect.setActionCommand("registerRedirect");
        registerRedirect.addActionListener(new Router());


        JPanel submitContainer = new JPanel();
        submitContainer.setLayout(new FlowLayout());
        submitContainer.add(submit);

        // adding everything to the layout

        add(submitContainer, BorderLayout.PAGE_END);
        add(titleContainer, BorderLayout.PAGE_START);
        add(form, BorderLayout.CENTER);

        System.out.println(CONSOLE_GREEN + "Finish setting up login panel" + CONSOLE_RESET);
    }

    public static class LoginActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            // If the user is trying to login
            if (command.equals("login")) {

                String username = usernameInputField.getText();
                String password = Arrays.toString(passwordInputField.getPassword());
                if (username.equals("") || password.toString().equals("")) {
                    message.setText("Missing username/password");
                    message.setForeground(Color.RED);
                    return;
                }
                // If no fields are empty and passes the check
                if (validLogin(username, password)) {
                    message.setText("Successfully logged in!");
                    message.setForeground(Color.GREEN);
                    communication.sendMessage("autoRedirectHome");
                    Connection conn = DBConn.connection("session");

                } else {
                    message.setText("Invalid username/password");
                    message.setForeground(Color.RED);
                }
            }
        }
    }

    private static boolean validLogin(String username, String password) {
        Connection conn = DBConn.connection("users");
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            String command0 = "SELECT id FROM users WHERE username=?";
            ps = conn.prepareStatement(command0);
            ps.setString(1, username);
            boolean validUsername = true;
            try {
                res = ps.executeQuery();
            } catch (java.sql.SQLException e) {
                validUsername = false;
            }
            int i = res.getInt("id");
            if (validUsername == false) {
                return false;
            }
            // if the username is valid.
            boolean checkRes = checkPassword(password, i);
            return checkRes;
        } catch (SQLException e) {
            System.out.println(e.toString());
            message.setText("Operation failed");
            message.setForeground(Color.RED);
        } finally {
            try {
                res.close();
                ps.close();
                conn.close();
            } catch (Exception j) {
                message.setText("Operation failed");
                message.setForeground(Color.RED);
            }
        }
        return false;
    }

    private static boolean checkPassword(String password, int userInd) {
        Connection conn = DBConn.connection("users");
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            // Creating the sql command;
            String command0 = "SELECT * FROM users WHERE id = ?";

            // prepare the statement and execute
            ps = conn.prepareStatement(command0);
            ps.setInt(1, userInd);
            res = ps.executeQuery();

            // get the correct password and the salt from the database
            String correctPassword = res.getString("password").trim();
            byte[] salt = res.getBytes("salt");

            // Hash the given password with the salt taken out from the database
            Hash.PasswordWithSalt hashedPasswordObj = Hash.hashPassword(password.trim(), salt);
            // Store the password attribute in a string
            String hashedPassword = hashedPasswordObj.getPassword().trim();

            //
            boolean finalRes = hashedPassword.equals(correctPassword);
            if (finalRes) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            message.setText("Operation failed");
            message.setForeground(Color.RED);
        } finally {
            try {
                res.close();
                ps.close();
                conn.close();
            } catch (Exception j) {
                message.setText("Operation failed");
                message.setForeground(Color.RED);
            }
        }
        return false;
    }

}
