package game.view;

// Navigates between the different views

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FrameNavigator {

    public static final String MAIN_VIEW = "MainView.fxml";
    public static final String NEW_GAME = "NewGameView.fxml";
    public static final String LOAD_GAME = "LoadGameView.fxml";
    public static final String HIGHSCORES = "HighscoresView.fxml";
    public static final String LOGIN_DIALOG = "LoginLogoutView.fxml";
    public static final String GAME_BOARD = "GameBoard.fxml";

    private static MainController mainController;

    public static void setMainController(MainController mainController) {
        FrameNavigator.mainController = mainController;
    }

    public static void loadView(String fxml) {

        try {
        mainController.setPane(FXMLLoader.load(FrameNavigator.class.getResource(fxml)));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
