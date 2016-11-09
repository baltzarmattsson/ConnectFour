package game.logic;

// The main source of "intelligence" in the game. Helps both AI and when checking the board for a winner

import game.models.PlayerInfo;

import java.util.HashMap;


public class LogicHelper {

    // Takes in direction to check in, which playerNbr to compare to the next 3
    // numbers in the board, from the row/col coordinate specified in said direction
    public static int checkDirection(Direction direction, int playerNumber, int row, int col, String[][] board) {

        int timesInARow = 0;

        switch (direction) {
            case LEFT:
                while (col >= 0 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    col--;
                }
                break;
            case RIGHT:
                while (col < board[row].length - 1 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    col++;
                }
                break;
            case DIAGONAL_LEFT_TO_RIGHT_UP:
                while (row >= 0 && col < board[row].length - 1 && row >= 0 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    col++;
                    row--;
                }
                break;
            case DIAGONAL_LEFT_TO_RIGHT_DOWN:
                while (col < board[row].length - 1 && row < board.length - 1 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    col++;
                    row++;
                }
                break;
            case DOWN:
                while (row < board.length - 1 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    row++;
                }
                break;
            case UP:
                while (row >= 0 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    row--;
                }
                break;
            case DIAGONAL_RIGHT_TO_LEFT_UP:
                while (col >= 0 && row >= 0 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    col--;
                    row--;
                }
                break;
            case DIAGONAL_RIGHT_TO_LEFT_DOWN:
                while (col >= 0 && row < board.length - 1 && Integer.toString(playerNumber).equals(board[row][col])) {
                    ++timesInARow;
                    col--;
                    row++;
                }
                break;
        }

        // Returns how many times in a row the playernbr has appeared. Always returns at least 1 since
        // it also checks the index from [row][col] given in the parameters
        return timesInARow;
    }

    // Checks the whole board for winners (i.e. timesInARow == 4), returns -1 if noone has won, or <playerNbr> if there's a winner
    public static int checkBoardForWinner(HashMap<Integer, PlayerInfo> playerInfo, int rows, int columns, String[][] board) {

        for (PlayerInfo player : playerInfo.values()) {
            int playerNumber = player.getPlayerNumber();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {

                    if (col != -1) {
                        if ((LogicHelper.checkDirection(Direction.LEFT, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.RIGHT, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.UP, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.DOWN, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.DIAGONAL_RIGHT_TO_LEFT_DOWN, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.DIAGONAL_RIGHT_TO_LEFT_UP, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.DIAGONAL_LEFT_TO_RIGHT_DOWN, playerNumber, row, col, board) == 4)
                                || (LogicHelper.checkDirection(Direction.DIAGONAL_LEFT_TO_RIGHT_UP, playerNumber, row, col, board) == 4)) {

                            return playerNumber;
                        }
                    }
                }
            }
        }
        return -1;
    }

    // Checks if more discs can be added to the column, if yes, returns the topmost empty row-index, else -1
    public static int getTopmostEmptyRowInColumn(int column, int nbrOfRows, String[][] board) {

        for (int i = nbrOfRows - 1; i >= 0; i--) {
            if (board[i][column].equals(" ")) {
                return i;
            }
        }
        return -1;
    }
}
