package Helper;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToeLogic {
    public static final char X = 'X';
    public static final char O = 'O';
    public static final char EMPTY = ' ';

    /**
     * Returns starting state of the board.
     * @return char[][]
     */
    public static char[][] initialState(){
        char[][] res = {
                {EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY}
        };
        return res;
    }

    /**
     * Returns player who has the next turn on a board.
     * @param board
     * @return X || O
     */
    public static char player(char[][] board){
        int xCounter = 0;
        int oCounter = 0;


        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] == X){
                    xCounter++;
                } else if (board[i][j] == O){
                    oCounter++;
                }
            }
        }
        if (xCounter > oCounter){
            return O;
        } else {
            return X;
        }
    }

    /**
     * Returns all possible actions for the current board.
     * @param board
     * @return
     */
    public static int[][] actions(char[][] board){
        List<int[]> tmp = new ArrayList();
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] == EMPTY){
                    tmp.add(new int[] {i, j});
                }
            }
        }

        int[][] res = new int[tmp.size()][2];

        for (int k = 0; k < tmp.size(); k++){
            res[k] = tmp.get(k);
        }
        return res;
    }

    /**
     * Returns the board that results from making move (i, j) on the board.
     * @param board
     * @param action
     * @return
     */
    public static char[][] result(char[][] board, int[] action){
        char currentPlayer = player(board);
        char[][] copy = new char[3][3];
        for(int i = 0; i < board.length; i++) {
            copy[i] = board[i].clone();
        }
        try {
            copy[action[0]][action[1]] = currentPlayer;
        } catch(java.lang.NullPointerException e){
            return board;
        }
        return copy;
    }


    /**
     * Returns the winner of the game, if there is one.
     * @param board
     * @return
     */
    public static char winner(char[][] board){
        char h = check_horizontally(board);
        char v = check_vertically(board);
        char d = check_diagonally(board);
        if (v != EMPTY || d != EMPTY || h != EMPTY) {
            if (h == X || v == X || d == X) {
                return X;
            }
            else if (h == O || v == O || d == O){
                return O;
            } else {
                return EMPTY;
            }
        } else {
            return EMPTY;
        }
    }

    /**
     * Returns 1 if X has won the game, -1 if O has won, 0 otherwise.
     * @param board
     * @return
     */
    public static boolean terminal(char[][] board){
        if (winner(board) != EMPTY || !hasEmpty(board)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns 1 if X has won the game, -1 if O has won, 0 otherwise.
     * @param board
     * @return
     */
    public static int utility(char[][] board){
        if (winner(board) == X) {
            return 1;
        } else if (winner(board) == O) {
            return -1;
        } else {
            return 0;
        }

    }

    public static int[] minimax(char[][] board){
        char curr_player = player(board);
        if (terminal(board)) {
            return null;
        }
        else{
            MinimaxChoice optimal;
            if (curr_player == X) {
                optimal = max_value(board);
            } else {
                optimal = min_value(board);
            }
            return optimal.getMove();
        }
    }

    private static MinimaxChoice max_value(char[][] board){
        if (terminal(board)) {
            return new MinimaxChoice(utility(board), null);
        }

        float curr_best = Float.POSITIVE_INFINITY * -1;
        int[] optimalMove = null;
        MinimaxChoice buffer;
        for (int[] action : actions(board)){
            buffer = min_value(result(board, action));
            int v = buffer.getValue();
            if (v > curr_best) {
                curr_best = v;
                optimalMove = action;
                if (curr_best == 1) {
                    return new MinimaxChoice((int) curr_best,optimalMove);
                }
            }
        }
        return new MinimaxChoice((int) curr_best, optimalMove);
    }

    private static MinimaxChoice min_value(char[][] board){
        if (terminal(board)){
            return new MinimaxChoice(utility(board), null);
        }

        float curr_best = Float.POSITIVE_INFINITY;
        int[] optimalMove = null;
        MinimaxChoice buffer;
        for (int[] action : actions(board)) {
            buffer = max_value(result(board, action));
            int v = buffer.getValue();
            if (v < curr_best) {
                curr_best = v;
                optimalMove = action;
                if (v == -1) {
                    return new MinimaxChoice(v,optimalMove);
                }
            }

        }
        return new MinimaxChoice((int) curr_best, optimalMove);
    }

    private static class MinimaxChoice{
        private int[] move;
        private int value;
        public MinimaxChoice(int value, int[] move){
            this.move = move;
            this.value = value;
        }

        public MinimaxChoice(){

        }

        public void setMove(int[] move) {
            this.move = move;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public int[] getMove() {
            return move;
        }
    }

    // Helpers
    // =================================================================================
    /**
     * Checks for any three-in-a-row horizontally on a board, if there is a winner, return it, otherwise, return EMPTY.
     * @param board
     * @return
     */
    private static char check_horizontally(char[][] board) {
        for (char[] row : board) {
            if (row[0] == row[1] && row[1] == row[2] && row[0] != EMPTY){
                return row[0];
            }
        }
        return EMPTY;

    }

    /**
     * Checks for any three-in-a-row vertically on a board, if there is a winner, return it, otherwise, return EMPTY.
     * @param board
     * @return
     */
    private static char check_vertically(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != EMPTY){
                return board[0][i];
            }
        }
        return EMPTY;

    }

    /**
     * Checks for any three-in-a-row diagonally on a board, if there is a winner, return it, otherwise, return EMPTY.
     * @param board
     * @return
     */
    private static char check_diagonally(char[][] board){
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != EMPTY){
            return board[0][0];
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != EMPTY){
            return board[0][2];
        }
        return EMPTY;
    }

    private static boolean hasEmpty(char[][] board){
        for (char[] row : board){
            for (char c : row){
                if (c == EMPTY){
                    return true;
                }
            }
        }
        return false;
    }

}
