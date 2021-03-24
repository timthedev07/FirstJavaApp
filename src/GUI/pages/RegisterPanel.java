package GUI.pages;


import GUI.Control.*;
import Helper.Hash;
import javax.swing.*;
import Helper.DBConn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;


public class RegisterPanel extends JPanel{

    // This is to move the entire `div` of things according to the context
    public static CommunicationWithHome communication = new CommunicationWithHome("register");
    public static int YCHANGE = 150;
    public static int XCHANGE = 60;
    public static int TITLE_YCHANGE = 0;
    public static int TITLE_XCHANGE = 0;
    public static final String CONSOLE_RED = "\u001B[31m";
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";

    // Some labels, button, groups and fields

    private static JLabel usernameLabel;
    private static JTextField usernameInputField;
    private static JLabel passwordLabel;
    private static JPasswordField passwordInputField;
    private static JButton submit;
    private static JLabel message;
    private static JLabel title;
    private static JButton loginRedirect = new JButton("login");
    private static JLabel haveAccount = new JLabel("Already have an account? Login here: ");
    private static JLabel genderLabel = new JLabel("Choose gender: ");
    private static JRadioButton male = new JRadioButton("male");
    private static JRadioButton female = new JRadioButton("female");
    private static JRadioButton other = new JRadioButton("other");
    private static ButtonGroup group = new ButtonGroup();

    public RegisterPanel(int windowWidth, int windowHeight){

        setLayout(null);
        setSize(windowWidth, windowHeight);

        // Page title
        title = new JLabel("REM");
        title.setBounds(
                30+TITLE_XCHANGE,
                30+TITLE_YCHANGE,
                300,
                100);
        title.setName("title");
        title.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 70));
        title.setForeground(new Color(200, 200, 222));


        // The username row
        usernameLabel = new JLabel("Username: ");
        usernameInputField = new JTextField(50);
        usernameLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 17));
        usernameLabel.setBounds(
                10+XCHANGE,
                25+YCHANGE,
                90,
                25);
        usernameInputField.setBounds(
                110+XCHANGE,
                20+YCHANGE,
                165,
                30);

        // The password row
        passwordLabel = new JLabel("Password: ");
        passwordInputField = new JPasswordField(150);
        passwordLabel.setBounds(10+XCHANGE, 60+YCHANGE, 90,25);
        passwordLabel.setFont(new Font("Menlo", Font.ITALIC | Font.BOLD, 17));
        passwordInputField.setBounds(110+XCHANGE, 55+YCHANGE, 165, 30);

        // Setting up the message text label
        message = new JLabel();
        message.setBounds(20+XCHANGE, 120, 700, 25);



        // Gender label
        genderLabel.setBounds(35+XCHANGE, 90+YCHANGE, 180, 30);

        // Button group of radio buttons
        male.setBounds(40+XCHANGE, 120+YCHANGE, 60, 30);
        female.setBounds(110+XCHANGE, 120+YCHANGE, 90, 30);
        other.setBounds(200+XCHANGE, 120+YCHANGE, 100, 30);

        // ADding the radio buttons to the group
        JRadioButton[] radioButtons = {male, female, other};
        for (JRadioButton button : radioButtons){
            group.add(button);
            add(button);
        }

        // A label asking if the user does have an account
        haveAccount.setBounds(10+XCHANGE,160+YCHANGE,335, 30);
        // Configuring the redirecting button
        loginRedirect.setBounds(345+XCHANGE, 160+YCHANGE, 100, 30);
        loginRedirect.setActionCommand("loginRedirect");
        loginRedirect.addActionListener(new Router());


        // Setting up the submit button
        submit = new JButton("Register");
        submit.addActionListener(new RegisterActionListener());
        submit.setBounds(10+XCHANGE, 200+YCHANGE, 140, 35);
        submit.setActionCommand("register");

        add(title);
        add(submit);
        add(genderLabel);
        add(usernameLabel);
        add(passwordLabel);
        add(message);
        add(passwordInputField);
        add(usernameInputField);
        add(haveAccount);
        add(loginRedirect);
        System.out.println(CONSOLE_GREEN+"Finish setting up register panel"+CONSOLE_RESET);
    }


    public static class RegisterActionListener implements ActionListener{

        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            // If the user is trying to register
            if (command.equals("register")) {
                // Check for any empty field
                String username = usernameInputField.getText();
                char[] password = passwordInputField.getPassword();
                if (username.equals("") || password.length == 0){
                    message.setText("Missing username/password");
                    message.setForeground(Color.RED);
                    return;
                }
                String selectedButton = null;
                if (male.isSelected()){
                    selectedButton = male.getText();
                } else if (female.isSelected()){
                    selectedButton = female.getText();
                } else {
                    selectedButton = "other";
                }
                if (register(username, Arrays.toString(password), selectedButton)){

                    System.out.println("Redirecting user to the main page");
                    communication.sendMessage("autoRedirectHome");

                }


            }
        }
    }

    /**
     * Registers the user and inserts into database, automatically hashes the password string provided.
     * CREATE TABLE users (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     username TEXT NOT NULL UNIQUE,
     password TEXT NOT NULL,
     gender TEXT NOT NULL);
     CREATE TABLE sqlite_sequence(name,seq);
     * @param username
     * @param password
     * @param gender
     */
    public static boolean register(String username, String password, String gender) {


        Connection conn = DBConn.connection("users");
        PreparedStatement ps = null;
        try {
            Hash.PasswordWithSalt hashedPassword = Hash.hashPassword(password);
            String command = "INSERT INTO users(username, password, gender, salt) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(command);
            ps.setString(1, username);
            ps.setString(2, hashedPassword.getPassword());
            ps.setString(3, gender);
            ps.setBytes(4, hashedPassword.getSalt());


            ps.execute();
            System.out.println(CONSOLE_GREEN+"Register success"+CONSOLE_RESET);


        } catch(SQLException e) {
            System.out.println(CONSOLE_RED+e.toString()+CONSOLE_RESET);
            message.setText("Username already taken");
            message.setForeground(Color.RED);
            return false;
        } finally {
            try {
                conn.close();
                ps.close();
                return true;
            } catch(java.sql.SQLException e){
                message.setText("Operation failed");
                message.setForeground(Color.RED);
                return false;
            }
        }






    }

}
