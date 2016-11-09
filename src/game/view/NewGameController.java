package game.view;

// Handles the user input when creating a new game

import game.DataController;
import game.logic.Difficulty;
import game.logic.Game;
import game.models.PlayerInfo;
import game.user.LoggedInUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Optional;

public class NewGameController extends Controller {

    private static Game gameInstance;

    public static Game getGameInstance() {
        return NewGameController.gameInstance;
    }

    private HashMap<Integer, PlayerInfo> playerInfo;

    // Adding each "player detail" related editor in the view, in convenient arrays for easier looping
    private CheckBox[] enabledPlayers = new CheckBox[4];
    private ComboBox[] playerIsAI = new ComboBox[4];
    private TextField[] usernames = new TextField[4];
    private Color[] playerColors = new Color[4];

    private int desiredColumns = 7;
    private int desiredRows = 6;

    @FXML
    private CheckBox playerTwoIsPlaying;
    @FXML
    private CheckBox playerThreeIsPlaying;
    @FXML
    private CheckBox playerFourIsPlaying;

    @FXML
    private ComboBox<String> aiBoxP2;
    @FXML
    private ComboBox<String> aiBoxP3;
    @FXML
    private ComboBox<String> aiBoxP4;

    @FXML
    private ColorPicker colorP1;
    @FXML
    private ColorPicker colorP2;
    @FXML
    private ColorPicker colorP3;
    @FXML
    private ColorPicker colorP4;

    @FXML
    private TextField textFieldP1;
    @FXML
    private TextField textFieldP2;
    @FXML
    private TextField textFieldP3;
    @FXML
    private TextField textFieldP4;

    @FXML
    private Slider rowsSlider;
    @FXML
    private Slider columnsSlider;

    @FXML
    private Label rowsLabel;
    @FXML
    private Label columnsLabel;

    @FXML
    private void initialize() {

        // Checks the username against the DB, if it exists
        setUsername();
        playerInfo = new HashMap<Integer, PlayerInfo>();

        setComboBoxValues();

        rowsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                desiredRows = newValue.intValue();
                rowsLabel.setText("Rows: " + desiredRows);
            }
        });

        columnsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                desiredColumns = newValue.intValue();
                columnsLabel.setText("Columns: " + desiredColumns);
            }
        });

    }

    // Adding all the different Difficulties from the Difficult class to each ComboBox (dropdown menu)
    private void setComboBoxValues() {
        playerIsAI[0] = null;
        playerIsAI[1] = aiBoxP2;
        playerIsAI[2] = aiBoxP3;
        playerIsAI[3] = aiBoxP4;

        for (ComboBox<String> aiBox : playerIsAI) {
            if (aiBox == null)
                continue;
            aiBox.getItems().add("Human");
            for (Difficulty d : Difficulty.values())
                aiBox.getItems().add(d.toString());
            aiBox.getSelectionModel().select(0);
        }
    }

    @FXML
    private void handleStartGameButton() {
        startNewGame();
    }

    // Reads all the user input data and creates a new game from the information
    private void startNewGame() {

        enabledPlayers[0] = null;
        enabledPlayers[1] = playerTwoIsPlaying;
        enabledPlayers[2] = playerThreeIsPlaying;
        enabledPlayers[3] = playerFourIsPlaying;

        playerIsAI[0] = null;
        playerIsAI[1] = aiBoxP2;
        playerIsAI[2] = aiBoxP3;
        playerIsAI[3] = aiBoxP4;

        usernames[0] = textFieldP1;
        usernames[1] = textFieldP2;
        usernames[2] = textFieldP3;
        usernames[3] = textFieldP4;

        playerColors[0] = colorP1.getValue();
        playerColors[1] = colorP2.getValue();
        playerColors[2] = colorP3.getValue();
        playerColors[3] = colorP4.getValue();

        // Checking usernames
        boolean atLeastOneUsernameInvalid = false;
        for (TextField usernameField : usernames) {
            if (DataController.checkUsername(usernameField.getText()) == false) {
                usernameField.setText("CREATE USER OR LEAVE BLANK");
                atLeastOneUsernameInvalid = true;
            }
        }

        if (atLeastOneUsernameInvalid)
            return;

        // Loops through all Arrays, and adds the information to separate PlayerInfo entities if valid
        // The playerInfo entities are then passed as a list to the Game instance
        for (int i = 0; i < 4; i++) {

            int playerNbr;
            Optional<String> username;
            Color discColor;
            boolean playerIsAI;

            // Is the main player
            if (i == 0) {
                playerNbr = 1;
                username = textFieldP1.getText() == null ? Optional.empty() : Optional.ofNullable(textFieldP1.getText());
                discColor = playerColors[i];
                playerIsAI = false;

                playerInfo.put(i, new PlayerInfo(playerNbr, username, discColor, playerIsAI, null));

            // Else, is not the main player
            } else if (i != 0 && enabledPlayers[i].isSelected()) {
                playerNbr = i + 1;

                String selectedAIDifficulty = this.playerIsAI[i].getSelectionModel().getSelectedItem().toString();
                playerIsAI = !selectedAIDifficulty.equals("Human");

                username = Optional.empty();
                if (playerIsAI == false && usernames[i].getText().length() > 0)
                    username = Optional.ofNullable(usernames[i].getText());
                discColor = playerColors[i];

                Difficulty aiDifficulty = null;
                if (playerIsAI) {
                    if (selectedAIDifficulty.equals(Difficulty.EASY.toString()))
                        aiDifficulty = Difficulty.EASY;
                    else if (selectedAIDifficulty.equals(Difficulty.MEDIUM.toString()))
                        aiDifficulty = Difficulty.MEDIUM;
                    else if (selectedAIDifficulty.equals(Difficulty.HARD.toString()))
                        aiDifficulty = Difficulty.HARD;

                }

                playerInfo.put(i, new PlayerInfo(playerNbr, username, discColor, playerIsAI, aiDifficulty));
            }
        }

        // Creates a new game with the information and loads the frame
        Game game = new Game(playerInfo, desiredRows, desiredColumns);
        NewGameController.gameInstance = game;
        FrameNavigator.loadView(FrameNavigator.GAME_BOARD);
    }

    // Used to resume from a previous game
    public static void resumeLoadedGame(Game game) {
        NewGameController.gameInstance = game;
        FrameNavigator.loadView(FrameNavigator.GAME_BOARD);
    }
    // Sets the username to the currently logged in user, or gives super friendly feedback to user
    private void setUsername() {
        String loggedInUsername = LoggedInUser.getLoggedInUser();
        if (loggedInUsername != null) {
            textFieldP1.setText(loggedInUsername);
            textFieldP1.setEditable(false);
        } else {
            textFieldP1.setText("CREATE USER OR LEAVE BLANK");
            textFieldP1.setEditable(true);
        }
    }
}
