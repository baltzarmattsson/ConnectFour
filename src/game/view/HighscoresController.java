package game.view;

// Presents all the highscores in the game. A highscore entry counts as a highscore when the player has won at least one game

import game.DataController;
import game.models.HighScoreEntry;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HighscoresController extends Controller {

    @FXML
    private TableView<HighScoreEntry> highscoreTable;
    @FXML
    private TableColumn<HighScoreEntry, String> usernameCol;
    @FXML
    private TableColumn<HighScoreEntry, Integer> scoreCol;
    @FXML
    private TableColumn<HighScoreEntry, Integer> gamesPlayedCol;
    @FXML
    private TableColumn<HighScoreEntry, Double> ratioCol;

    @FXML
    private void initialize() {

        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        scoreCol.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
        gamesPlayedCol.setCellValueFactory(cellData -> cellData.getValue().gamesPlayedProperty().asObject());
        ratioCol.setCellValueFactory(cellData -> cellData.getValue().ratioProperty().asObject());

        highscoreTable.getItems().addAll(DataController.findHighscores());

    }


}
