package Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TicTacToeBoard extends JPanel implements ActionListener{

    // Declaring the variables
    public static char[][] board = new char[3][3];
    public static char user;
    public boolean aiTurn = false;
    public static JPanel boardUpdateContainer;
    public static int state = 0;
    private static char current_player;

    // Constant variables
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";
    public static int BOARD_SIDE = 300;
    public static String INITIAL = "initial";


    // Empty constructor for actionListener object instantiations
    public TicTacToeBoard() {

    }

    public TicTacToeBoard(int boardSide, char chosenPlayer){
        // Setting up the container for updating the board, the logic used
        // here is that each time after getting the action,
        // we get the new panel that has the target cell filled with the player,
        // and taking advantage of cardlayout, we switch to add and show the next panel.
        boardUpdateContainer = new JPanel();
        boardUpdateContainer.setLayout(new CardLayout());
        setPreferredSize(new Dimension(BOARD_SIDE, BOARD_SIDE));


        user = chosenPlayer;
        current_player = TicTacToeLogic.player(board);


        // Initializing the board and set each grid to be empty
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                board[i][j] = TicTacToeLogic.EMPTY;
            }
        }

        // Setting up the panel
        setLayout(new FlowLayout());
        setSize(boardSide, boardSide);
        BOARD_SIDE = boardSide;

        // if the the user chooses O, meaning to always make moves after X
        // then make ai move
        if (user == TicTacToeLogic.O){
            // Getting ai move and apply to the board
            int[] aimove = new int[]{Helpers.getRandomNumber(0, 2), Helpers.getRandomNumber(0, 2)};
            board = TicTacToeLogic.result(board, aimove);

            // update board including the new ai move
            update();
            state++;
        }

        JPanel biggerContainer = new JPanel();
        biggerContainer.setSize(300, 300);

        // Container
        boardUpdateContainer.add(renderBoard(board), INITIAL);
        biggerContainer.add(boardUpdateContainer);
        add(biggerContainer);
        System.out.println(CONSOLE_GREEN+"Finish setting up tic tac toe board component"+CONSOLE_RESET);
    }

    @Override

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        if (command.equals("resetGame")){
            resetGame();
            return;
        }

        // Getting information about the grid
        int i = Character.getNumericValue(command.charAt(0));
        int j = Character.getNumericValue(command.charAt(1));
        int[] action = new int[]{i, j};

        // Make user move
        board = TicTacToeLogic.result(board, new int[]{i, j});

        // update board including the new user move
        update();
        state++;

        // Getting ai move and apply to the board
        int[] aimove = TicTacToeLogic.minimax(board);
        board = TicTacToeLogic.result(board, aimove);

        // update board including the new ai move
        update();
        state++;


        current_player = TicTacToeLogic.player(board);


        displayBoard(board);

    }

    /**
     * Render the board with the given 2d array
     * @param board
     */
    public static JPanel renderBoard(char[][] board){
        JPanel res = new JPanel();
        res.setPreferredSize(new Dimension(BOARD_SIDE, BOARD_SIDE));
        res.setLayout(new GridLayout(3, 3));
        Grid[] grids = new Grid[9];

        int count = 0;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++) {
                String translatedCoord = Helpers.intToCoorStr(count);
                if (board[i][j] == '~'){
                    grids[count] = new Grid(100, 100, Color.LIGHT_GRAY, 2, ""+board[i][j]);
                } else if (board[i][j] == TicTacToeLogic.EMPTY){
                    grids[count] = new Grid(100, 100, Color.LIGHT_GRAY, 2, "EMPTY", translatedCoord);
                } else {
                    grids[count] = new Grid(100, 100, Color.LIGHT_GRAY, 2, ""+board[i][j]);
                }
                grids[count].setSize(new Dimension(100, 100));
                grids[count].setToolTipText(translatedCoord);
                res.add(grids[count]);
                count++;
            }
        }

        int x = 0;
        int y = 0;


        res.revalidate();
        res.repaint();
        return res;
    }

    private void updateBoard(JPanel panel){

        boardUpdateContainer.add(panel, "state" + state);
        CardLayout cl = (CardLayout) boardUpdateContainer.getLayout();
        cl.show(boardUpdateContainer, "state" + state);
    }


    /**
     * Called if there is no winner, but the game ends, in other words, the game ends up being a tie.<br>
     *     It will show the corresponding message.
     * @param panel
     */
    private void updateBoardTerminal(JPanel panel){
        JLabel tieMessage = new JLabel("Tie");
        panel = renderBoard(board);
        tieMessage.setFont(new Font("menlo", Font.ITALIC | Font.BOLD, 45));
        JPanel container = new JPanel();
        GroupLayout layout = new GroupLayout(container);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(panel).addComponent(tieMessage)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup().addComponent(panel).addComponent(tieMessage)
        );

        boardUpdateContainer.add(container, "state" + state);
        CardLayout cl = (CardLayout) boardUpdateContainer.getLayout();
        cl.show(boardUpdateContainer, "state" + state);
    }

    /**
     * Called if there someone wins.<br>
     *     It will show the corresponding message.
     * @param panel
     */
    private void updateBoardWinner(JPanel panel, char winner){
        displayBoard(board);
        panel = renderBoard(board);
        JLabel tieMessage = new JLabel(winner+" wins!");
        tieMessage.setFont(new Font("menlo", Font.ITALIC | Font.BOLD, 45));
        JPanel container = new JPanel();
        GroupLayout layout = new GroupLayout(container);
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(panel).addComponent(tieMessage)
        );
        layout.setHorizontalGroup(
                layout.createSequentialGroup().addComponent(panel).addComponent(tieMessage)
        );

        boardUpdateContainer.add(container, "state" + state);
        CardLayout cl = (CardLayout) boardUpdateContainer.getLayout();
        cl.show(boardUpdateContainer, "state" + state);
    }

    private static void displayBoard(char[][] board){
        String res = "+-----------+\n";
        for(char[] row : board){
            res = res.concat("|");
            for (char c : row){
                if (c == TicTacToeLogic.EMPTY){
                    res = res.concat(" "+c+" |");
                    continue;
                }
                res = res.concat(" "+c+" |");
            }
            res = res.concat("\n+-----------+\n");
        }
        System.out.println(res);
    }

    /**
     * Resets the board 2d array by filling all of them with empty
     */
    private void resetGame(){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                board[i][j] = TicTacToeLogic.EMPTY;
            }
        }

        updateBoard(renderBoard(board));

    }

    private void update(){
        JPanel newPanel = renderBoard(board);
        char winner = TicTacToeLogic.winner(board);
        boolean someoneWon = winner != TicTacToeLogic.EMPTY;
        boolean gameover = TicTacToeLogic.terminal(board);
        if (someoneWon) {
            for (int ii = 0, l = board.length; ii < l; ii++) {
                for (int jj = 0; jj < l; jj++) {
                    if (board[ii][jj] == TicTacToeLogic.EMPTY) {
                        board[ii][jj] = '~';
                    }
                }
            }
            displayBoard(board);
            updateBoardWinner(newPanel, winner);


        } else if (gameover) {
            for (int ii = 0, l = board.length; ii < l; ii++) {
                for (int jj = 0; jj < l; jj++) {
                    if (board[ii][jj] == TicTacToeLogic.EMPTY) {
                        board[ii][jj] = '~';
                    }
                }
            }
            updateBoardTerminal(newPanel);

        } else {
            updateBoard(newPanel);
        }
    }
}
