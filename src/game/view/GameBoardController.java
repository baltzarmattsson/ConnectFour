package game.view;

import game.DataController;
import game.logic.AI;
import game.logic.Game;
import game.logic.LogicHelper;
import game.models.Coordinate;
import game.models.Move;
import game.models.PlayerInfo;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class GameBoardController {

    private int numberOfPlayers;
    private Color currentPlayerColor;
    private int currentPlayerIndex = -1;
    private int currentPlayerNbr;
    private String currentPlayerUsername;
    private boolean currentPlayerIsAI;
    private HashMap<Integer, PlayerInfo> playerInfo;
    private Game gameInstance;

    private AI currentAI;

    private int currentMove = 1;

    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Circle playerColorCircle;

    @FXML
    private GridPane visualBoard;

    @FXML
    private TableView<Move> auditTable;
    @FXML
    private TableColumn<Move, Integer> moveNbrColumn;
    @FXML
    private TableColumn<Move, Integer> playerNbrColumn;
    @FXML
    private TableColumn<Move, String> playerUsernameColumn;
    @FXML
    private TableColumn<Move, Integer> rowCoordColumn;
    @FXML
    private TableColumn<Move, Integer> colCoordColumn;
    @FXML
    private TableColumn<Move, String> playerColorColumn;
    @FXML
    private TableColumn<Move, String> timeStampColumn;

    @FXML
    private void initialize() {

        // Initializes the table and its columns
        moveNbrColumn.setCellValueFactory(cellData -> cellData.getValue().moveNbrProperty().asObject());
        playerNbrColumn.setCellValueFactory(cellData -> cellData.getValue().playerNbrProperty().asObject());
        playerUsernameColumn.setCellValueFactory(cellData -> cellData.getValue().playerUsernameProperty());
        rowCoordColumn.setCellValueFactory(cellData -> cellData.getValue().rowCoordProperty().asObject());
        colCoordColumn.setCellValueFactory(celLData -> celLData.getValue().colCoordProperty().asObject());
        playerColorColumn.setCellValueFactory(cellData -> cellData.getValue().playerColorProperty());
        timeStampColumn.setCellValueFactory(cellData -> cellData.getValue().timeStampProperty());

        // Initializing the gameInstance and creating the board according to the gameInstances ROWS and COLS values
        gameInstance = NewGameController.getGameInstance();

        // Pairing the current Game with this GameBoardController
        gameInstance.setGameBoardController(this);

        this.createBoard();
        playerInfo = gameInstance.getPlayerInfo();
        if (gameInstance.wasSavedGame()) {
            initializeBoard();
            resumeBoard(gameInstance.getMoves());
            this.currentMove = gameInstance.getMoves().size() + 1;

            // In order for the player queue to be correct, we must see what move has the currently highest moveNbr
            // and what its getPlayerNbr is, and then go forward one step in the queue to the next playernbr
            int highestMoveNbr = 0;
            for (Move move : gameInstance.getMoves().values()) {
                if (move.getMoveNbr() > highestMoveNbr) {
                    highestMoveNbr = move.getMoveNbr();

                    // Since playerIndex == getPlayerNbr - 1 it means that if getPlayerNbr == playerInfo.size()
                    // the index is out of bounds, and we must reset to 0
                    if (move.getPlayerNbr() == playerInfo.size()) {
                        this.currentPlayerIndex = 0;
                    } else {
                        this.currentPlayerIndex = move.getPlayerNbr() - 1;
                    }

                }
            }
            this.cyclePlayer(false);
        } else {
            this.initializeBoard();
            this.cyclePlayer(true);
        }

        numberOfPlayers = playerInfo.size();

        // Adding players to Played-table if it's a new game
        if (gameInstance.wasSavedGame() == false)
            for (PlayerInfo pi : playerInfo.values()) {
                if ((pi.getUsername().length() > 0 && DataController.checkUsername(pi.getUsername())) || pi.isAI())
                    DataController.registerUsernameOnGame(
                            pi.getPlayerNumber(),
                            gameInstance.getGameID(),
                            pi.getDiscColor(),
                            (pi.isAI()) ? null : pi.getUsername(),
                            pi.isAI(),
                            pi.getAiDifficulty()
                    );
            }
    }

    // initializes the visual board with panes on each grid, each having a mouse-click listener as well
    private void initializeBoard() {
        for (int y = 0; y < gameInstance.getROWS(); y++) {
            for (int x = 0; x < gameInstance.getCOLUMNS(); x++) {
                Pane pane = new Pane();

                final int xx = x;
                final int yy = y;
                // Adding listener to each grid, when pressed it adds a circle to the desired column (x)
                // and cycles the player
                pane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    addDisc(xx);
                });

                pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                visualBoard.add(pane, x, y);
            }
        }
    }

    // Resumes the board from a saved game
    private void resumeBoard(HashMap<Integer, Move> moves) {
        for (int i = 0; i < moves.size(); i++) {

            // i + 1 since it's 0-based index in the DB, and 1-based index in game
            Move move = moves.get(i + 1);

            // This is some messy GUI code
            int circleRadius = 0;
            if (gameInstance.getCOLUMNS() < 5)
                circleRadius = 30;
            else if (gameInstance.getCOLUMNS() < 10)
                circleRadius = 15;
            else if (gameInstance.getCOLUMNS() < 15)
                circleRadius = 12;
            else if (gameInstance.getCOLUMNS() < 20)
                circleRadius = 10;
            else if (gameInstance.getCOLUMNS() > 25)
                circleRadius = 5;

            // Creating new disc with the players color
            Circle disc = new Circle(circleRadius);
            Color currentPlayerColor = Color.web(move.getPlayerColor());

            disc.setFill(currentPlayerColor);

            // Adding listener to the circle as well, so that when the player clicks on existing circles
            // a disc is added in the same column, otherwise it just listens to the pane behind the circle
            disc.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                addDisc(move.getColCoord());
            });

            // Adding the previous Moves to the audit table
            auditTable.getItems().add(move);

            //Adding circle to the visual board
            visualBoard.add(disc, move.getColCoord(), move.getRowCoord());

        }
    }

    // Adding disc to BOTH the logical board (i.e. String[][] in gameInstance,
    // and the visual board
    private void addDisc(int colIndex) {

        int rowIndexOrMinusOne = gameInstance.getTopmostEmptyRowInColumn(colIndex);

        if (rowIndexOrMinusOne != -1 && gameInstance.getTotalMovesLeft() > 0) {

            // Adding to logical board
            gameInstance.addDisc(colIndex, currentPlayerNbr);

            // Again, messy GUI code
            int circleRadius = 0;
            if (gameInstance.getCOLUMNS() < 5)
                circleRadius = 30;
            else if (gameInstance.getCOLUMNS() < 10)
                circleRadius = 15;
            else if (gameInstance.getCOLUMNS() < 15)
                circleRadius = 12;
            else if (gameInstance.getCOLUMNS() < 20)
                circleRadius = 10;
            else if (gameInstance.getCOLUMNS() > 25)
                circleRadius = 5;

            Circle disc = new Circle(circleRadius);
            disc.setFill(currentPlayerColor);

            // Adding listener to the circle as well, so that when the player clicks on existing circles
            // a disc is added in the same column, otherwise it just listens to the pane behind the circle
            disc.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                addDisc(colIndex);
            });

            // Adding a Move object to the table
            auditTable.getItems().add(currentMove - 1, new Move(currentMove, currentPlayerNbr, currentPlayerUsername, rowIndexOrMinusOne, colIndex, currentPlayerColor.toString(), new Date(System.currentTimeMillis())));
            // Adding the move to the DB
            DataController.registerMove(gameInstance.getGameID(), currentMove, currentPlayerNbr, currentPlayerUsername, rowIndexOrMinusOne, colIndex, currentPlayerColor);

            currentMove++;

            //Adding disc to visual board
            visualBoard.add(disc, colIndex, rowIndexOrMinusOne);

            // Checks for winner and if there's any moves left to make
            if (this.checkForWinner() == false && gameInstance.getTotalMovesLeft() > 0) {
                gameInstance.decrementTotalMovesLeft();

                cyclePlayer(true);

                if (currentPlayerIsAI) {
                    Coordinate aiChosenCoordinate = currentAI.makeMove(playerInfo, gameInstance.getROWS(), gameInstance.getCOLUMNS(), gameInstance.getBoard());
                    if (aiChosenCoordinate != null)
                        addDisc(aiChosenCoordinate.getX());
                }
            } else if (gameInstance.getTotalMovesLeft() == 0) {
                endGame("No more moves", null, "No moves can be made", null);
            }
        } else if (gameInstance.getTotalMovesLeft() == 0) {
            endGame("No more moves", null, "No moves can be made", null);
        }

    }

    // Ends the game with an Alert
    private void endGame(String alertTitle, String headerText, String contentText, String optionalUsername) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();

        // Updating the Gamestatus-table if it was a saved game, or inserting a new row if it was a new game
        // Either way, this game is marked as finished
        DataController.saveGame(gameInstance.getGameID(),
                true,
                true,
                optionalUsername == null ? null : optionalUsername.length() > 0 ? optionalUsername : null
        );

        // Removing players from Playing-table and adding to Played-table
        for (PlayerInfo pi : playerInfo.values()) {
            if (pi.getUsername().length() > 0 && DataController.checkUsername(pi.getUsername()))
                DataController.deregisterUsernameFromGameAndRegisterInPlayedTable(gameInstance.getGameID(), pi.getUsername());
        }

        // Exits the game and loads a new game view
        FrameNavigator.loadView(FrameNavigator.NEW_GAME);
    }

    // Returns true if winner is found, else false
    private boolean checkForWinner() {
        int winnerPlayerNbr = LogicHelper.checkBoardForWinner(playerInfo, gameInstance.getROWS(), gameInstance.getCOLUMNS(), gameInstance.getBoard());
        if (winnerPlayerNbr != -1) {
            String winnerUsername = playerInfo.get(winnerPlayerNbr - 1).getUsername();
            this.endGame("WINNER!", "Congratulations", "Player " + winnerPlayerNbr + " has won!", winnerUsername);
            return true;
        }
        return false;
    }

    // Cycles the player to the next in the queue, and updates the player
    // attributes, i.e. Color, PlayerNbr, Username, IsAI
    private void cyclePlayer(boolean cycleIndex) {

        if (cycleIndex) {
            if (currentPlayerIndex == -1 || currentPlayerIndex == playerInfo.size() - 1) {
                currentPlayerIndex = 0;
            } else {
                ++currentPlayerIndex;
            }
        }

        PlayerInfo currentPlayerInfo = playerInfo.get(currentPlayerIndex);

        System.out.println(currentPlayerIndex);
        currentPlayerNbr = currentPlayerInfo.getPlayerNumber();
        currentPlayerColor = currentPlayerInfo.getDiscColor();
        currentPlayerUsername = currentPlayerInfo.getUsername();
        currentPlayerIsAI = currentPlayerInfo.isAI();

        // Updating the example in the view
        playerColorCircle.setFill(currentPlayerColor);
        currentPlayerLabel.setText(currentPlayerNbr + "\n" + currentPlayerUsername);

        if (currentPlayerIsAI) {
            currentAI = new AI(currentPlayerInfo.getAiDifficulty(), currentPlayerInfo.getPlayerNumber(), currentPlayerInfo.getDiscColor());
        }

    }

    // Creates the visual board
    private void createBoard() {

        final int COLUMNS = gameInstance.getCOLUMNS();
        final int ROWS = gameInstance.getROWS();

        for (int x = 0; x < COLUMNS; x++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COLUMNS);
            colConst.setHalignment(HPos.CENTER);
            visualBoard.getColumnConstraints().add(colConst);
        }
        for (int y = 0; y < ROWS; y++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROWS);
            rowConst.setValignment(VPos.CENTER);
            visualBoard.getRowConstraints().add(rowConst);
        }
    }

    // Resets the visual AND logical board. Very buggy in the GUI. Sorry
    @FXML
    private void resetBoard() {
        gameInstance.resetBoard();

        visualBoard.getChildren().clear();

        this.createBoard();
        this.initializeBoard();

    }

}
