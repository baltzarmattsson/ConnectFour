package game.view;

// Is used to load a previous game

import game.DataController;
import game.logic.Game;
import game.models.Move;
import game.user.LoggedInUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LoadGameController extends Controller {

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
    private ComboBox<Integer> loadedGamesCombobox;

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

        // If the user is logged in, data is added to the Dropdown-menu (ComboBox), otherwise does nothing
        if (LoggedInUser.getLoggedInUser() != null && LoggedInUser.getLoggedInUser().length() > 0) {
            ArrayList<Integer> loadedGames = DataController.findGamesByUsername(LoggedInUser.getLoggedInUser(), true);
            if (loadedGames.size() > 0)
                loadedGamesCombobox.getItems().addAll(loadedGames);
        }

        // Listener for the ComboBox. If an item is clicked, it automatically fills the table with its previous moves
        loadedGamesCombobox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (loadedGamesCombobox.getSelectionModel().getSelectedItem() != null) {
                    int gameID = loadedGamesCombobox.getSelectionModel().getSelectedItem();
                    auditTable.setItems(FXCollections.observableArrayList(DataController.findMovesByGameID(gameID).values()));
                }
            }
        });

    }

    // Is used when "Enter game" button is clicked. Finds info from the DB and creates a new game with the same information
    @FXML
    private void enterGame() {
        int gameid;
        if (loadedGamesCombobox.getSelectionModel().getSelectedItem() != null) {
            gameid = loadedGamesCombobox.getSelectionModel().getSelectedItem();

            Game infoAboutGame = DataController.findGameByGameID(gameid);

            if (infoAboutGame != null) {
                Game game = new Game(
                        gameid,
                        DataController.findPlayerInfoByGameID(gameid),
                        infoAboutGame.getROWS(),
                        infoAboutGame.getCOLUMNS(),
                        DataController.findMovesByGameID(gameid)
                        );


                game.wasSavedGame(true);
                NewGameController.resumeLoadedGame(game);

            }

        }
    }
}
