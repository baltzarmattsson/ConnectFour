package game;

// Acts as the main controller, mainly to act as a middle layer between DAL methods and the callers,
// in case of switching the database for something more convenient

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import game.database.DAL;
import game.logic.Difficulty;
import game.logic.Game;
import game.models.HighScoreEntry;
import game.models.Move;
import game.models.PlayerInfo;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class DataController {

    private static DAL dalInstance = new DAL();


    public static ArrayList<Move> loadGame(int gameID) {
        return dalInstance.loadGame(gameID);
    }

    public static void registerMove(int gameID, int moveNbr, int playerNbr, String username, int rowCoord, int colCoord, Color color) {
        dalInstance.registerMove(gameID, moveNbr, playerNbr, username, rowCoord, colCoord, color);
    }

    public static int createNewGame(int cols, int rows, int nbrOfPlayers) {
        return dalInstance.createNewGame(cols, rows, nbrOfPlayers);
    }

    public static void saveGame(int gameId, boolean wasSavedGame, boolean isFinished, String winner) {
        dalInstance.saveGame(gameId, wasSavedGame, isFinished, winner);
    }

    public static void createUser(String username, String password) throws MySQLIntegrityConstraintViolationException {
        dalInstance.createUser(username, password);
    }

    public static boolean loginUser(String username, String password) {
        return dalInstance.loginUser(username, password);
    }

    public static boolean checkUsername(String username) {
        return dalInstance.checkUsername(username);
    }

    public static ArrayList<Integer> findGamesByUsername(String username, boolean currentlyPlaying) {
        return dalInstance.findGamesByUsername(username, currentlyPlaying);
    }

    public static void registerUsernameOnGame(int playerNbr, int gameID, Color color, String username, boolean isAI, Difficulty aiDifficulty) {
        dalInstance.registerPlayerOnGame(playerNbr, gameID, color, username, isAI, aiDifficulty);
    }

    public static void deregisterUsernameFromGameAndRegisterInPlayedTable(int gameID, String username) {
        dalInstance.deregisterUsernameFromGameAndRegisterInPlayedTable(gameID, username);
    }

    public static HashMap<Integer, Move> findMovesByGameID(int gameID) {
        return dalInstance.findMovesByGameID(gameID);
    }

    public static HashMap<Integer, PlayerInfo> findPlayerInfoByGameID(int gameID) {
        return dalInstance.findPlayerInfoByGameID(gameID);
    }
    public static Game findGameByGameID(int gameID) {
        return dalInstance.findGameByGameID(gameID);
    }

    public static ArrayList<HighScoreEntry> findHighscores() {
        return dalInstance.findHighscores();
    }

}
