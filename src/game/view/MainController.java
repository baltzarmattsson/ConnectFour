package game.view;

// Handles the Menubar

import game.Main;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class MainController extends Controller {

    private Main mainApp;

    @FXML
    private GridPane gridPane;

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    public void setPane(Node node) {
        gridPane.getChildren().setAll(node);
    }

    @FXML
    private void newGame() {
        FrameNavigator.loadView(FrameNavigator.NEW_GAME);
    }

    @FXML
    private void loadGame() {
        FrameNavigator.loadView(FrameNavigator.LOAD_GAME);
    }

    @FXML
    private void highscores() {
        FrameNavigator.loadView(FrameNavigator.HIGHSCORES);
    }

    @FXML
    private void logIn() {
        FrameNavigator.loadView(FrameNavigator.LOGIN_DIALOG);
    }


}
