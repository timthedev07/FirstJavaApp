package GUI.pages;

import GUI.Control;
import Helper.Helpers;
import Helper.TicTacToeBoard;

import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JPanel{

    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";
    public static final int YCHANGE = 150;
    public static final int XCHANGE = 60;
    public static final int TITLE_YCHANGE = 0;
    public static final int TITLE_XCHANGE = 0;
    public static final int BOARD_SIDE = 300;
    public JLabel message;
    public static JButton homeRedirect;
    public static JButton reset;

    public TicTacToe(int windowWidth, int windowHeight, char player){
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        setSize(windowWidth, windowHeight);
        // Setting up the label
        JLabel title = Helpers.generateTitle();

        JLabel subtitle = new JLabel("Play Tic Tac Toe against AI!");
        subtitle.setName("title");
        subtitle.setFont(new Font("Monaco", Font.ITALIC | Font.BOLD, 21));
        subtitle.setForeground(new Color(200, 200, 222));

        TicTacToeBoard board = new TicTacToeBoard(BOARD_SIDE, player);

        homeRedirect = new JButton("Home");
        homeRedirect.setActionCommand("homeRedirect");
        homeRedirect.addActionListener(new Control.Router());

        reset = new JButton("Reset Game");
        reset.setActionCommand("resetTTTGame");
        reset.addActionListener(new Control.Router());

        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new GridBagLayout());


        boardContainer.add(board);

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addGroup(
                                                layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(title).addComponent(homeRedirect)
                                        )
                                .addGroup(
                                        layout.createParallelGroup().addComponent(subtitle).addComponent(reset)
                                ).addComponent(board)
                )


        );
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup()
                                        .addGroup(
                                                layout.createSequentialGroup().addComponent(title).addComponent(homeRedirect)
                                        )
                                .addGroup(
                                        layout.createSequentialGroup().addComponent(subtitle).addComponent(reset)
                                ).addComponent(board)

                        )

        );
        System.out.println(CONSOLE_GREEN+"Finish setting up tic tac toe panel"+CONSOLE_RESET);
        repaint();
        revalidate();
        repaint();
    }



}
