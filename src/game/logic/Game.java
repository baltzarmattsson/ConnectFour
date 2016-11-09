package game.logic;

// Used to hold the logical data of a game

import game.DataController;
import game.models.Move;
import game.models.PlayerInfo;
import game.view.GameBoardController;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static game.logic.Direction.RIGHT;

public class Game {

    private GameBoardController gameBoardController;

    private boolean wasSavedGame = false;
    private int gameID;
    private int COLUMNS;
    private int ROWS;
    private Date datePlayed;
    private String[][] board;

    // Total empty slots left on the playing field
    private int totalMovesLeft;

    private int numberOfPlayers;

    // Key = playerNbr
    private HashMap<Integer, PlayerInfo> playerInfo;
    private HashMap<Integer, Move> moves;


    //Constructor used as a temporary holder from a DB-query
    public Game(int gameID, Date datePlayed, int columns, int rows, int numberOfPlayers) {
        this.gameID = gameID;
        this.datePlayed = datePlayed;
        this.COLUMNS = columns;
        this.ROWS = rows;
        this.numberOfPlayers = numberOfPlayers;

    }

    // Constructor used for a new game
    public Game(HashMap<Integer, PlayerInfo> playerInfo, int rows, int columns) {

        this.numberOfPlayers = playerInfo.size();
        this.COLUMNS = columns;
        this.ROWS = rows;

        this.gameID = DataController.createNewGame(COLUMNS, ROWS, numberOfPlayers);
        DataController.saveGame(this.gameID, false, false, null);
        this.playerInfo = playerInfo;
        board = new String[ROWS][COLUMNS];

        this.totalMovesLeft = ROWS * COLUMNS;

        initializeBoard();
    }

    // Constructor used for a resumed game
    public Game(int gameID, HashMap<Integer, PlayerInfo> playerInfo, int rows, int columns, HashMap<Integer, Move> moves) {
        this.wasSavedGame = true;

        this.gameID = gameID;
        this.numberOfPlayers = playerInfo.size();
        this.COLUMNS = columns;
        this.ROWS = rows;
        this.board = new String[ROWS][COLUMNS];
        this.playerInfo = playerInfo;
        this.moves = moves;


        this.resetBoard();
        this.resumeBoard(moves);

        this.totalMovesLeft = 0;
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < columns; y++)
                if (board[x][y].equals(" "))
                    this.totalMovesLeft++;
    }

    // Sets each string to " " in the board
    private void initializeBoard() {
        this.resetBoard();
    }


    // Adds a disc to the topmost empty row if there's one, otherwise nothing
    public void addDisc(int column, int playerNbr) {

        int rowOrMinusOne = LogicHelper.getTopmostEmptyRowInColumn(column, ROWS, board);
        if (rowOrMinusOne != -1) {
            board[rowOrMinusOne][column] = playerNbr + "";
            printBoard();
        }

    }

    // Checks if more discs can be added to the column, if yes, returns the topmost empty row-index, else -1
    public int getTopmostEmptyRowInColumn(int column) {
        return LogicHelper.getTopmostEmptyRowInColumn(column, ROWS, board);
    }

    // Printing in System.out.println() for easier debugging in GUI
    private void printBoard() {

        System.out.println();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.format("[%s]", board[i][j]);
            }
            System.out.println();
        }
        System.out.println();

    }

    // Resets the logical board to spaces on all indexes
    public void resetBoard() {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
                board[i][j] = " ";
    }

    // Resumes the logical board (i.e. board[][]) with the moves previously made
    public void resumeBoard(HashMap<Integer, Move> moves) {

        for (Map.Entry<Integer, Move> map : moves.entrySet()) {
            int moveNbr = map.getKey();
            Move move = map.getValue();
            board[move.getRowCoord()][move.getColCoord()] = Integer.toString(move.getPlayerNbr());
        }

    }

    public void saveGame() {
        DataController.saveGame(this.gameID, this.wasSavedGame, false, null);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    public void setCOLUMNS(int COLUMNS) {
        this.COLUMNS = COLUMNS;
    }

    public int getROWS() {
        return ROWS;
    }

    public void setROWS(int ROWS) {
        this.ROWS = ROWS;
    }

    public HashMap<Integer, PlayerInfo> getPlayerInfo() {
        return this.playerInfo;
    }

    public boolean wasSavedGame() {
        return wasSavedGame;
    }

    public void wasSavedGame(boolean wasSavedGame) {
        this.wasSavedGame = wasSavedGame;
    }

    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    public HashMap<Integer, Move> getMoves() {
        return moves;
    }

    public void setMoves(HashMap<Integer, Move> moves) {
        this.moves = moves;
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public int getTotalMovesLeft() {
        return totalMovesLeft;
    }

    public void setTotalMovesLeft(int totalMovesLeft) {
        this.totalMovesLeft = totalMovesLeft;
    }

    public void decrementTotalMovesLeft() {
        this.totalMovesLeft--;
    }
}