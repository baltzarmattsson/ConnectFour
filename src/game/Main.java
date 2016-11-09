package game;

// Classic JavaFX Main-class

import game.view.FrameNavigator;
import game.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect 4");
        this.primaryStage = primaryStage;

        primaryStage.setScene(
                createScene(
                        loadMainView()
                )
        );

        primaryStage.show();

    }

    private Scene createScene(Pane mainView) {
        Scene scene = new Scene(
                mainView
        );

        return scene;
    }

    private Pane loadMainView() throws IOException {

        FXMLLoader loader = new FXMLLoader();

        Pane mainView = loader.load(
                getClass().getResourceAsStream(
                        "view/" + FrameNavigator.MAIN_VIEW
                )
        );

        MainController mainController = loader.getController();

        FrameNavigator.setMainController(mainController);
        FrameNavigator.loadView(FrameNavigator.LOGIN_DIALOG);

        return mainView;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
