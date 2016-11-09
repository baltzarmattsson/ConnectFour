package game.view;

// Is used for logging in, logging out, or creating a new user in the LoginLogoutView.fxml

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import game.DataController;
import game.user.LoggedInUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginLogoutController extends Controller {

    @FXML
    private void initialize() {
    }

    @FXML
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label responseLabel;

    @FXML
    private void handleLoginButton() {
        this.login();
    }

    // Checks if login is valid, and if so, logs in, else gives feedback
    private void login() {
        if (DataController.loginUser(usernameField.getText(), passwordField.getText())) {
            LoggedInUser.setLoggedInUser(usernameField.getText());
            responseLabel.setText("LOGGED IN");
        } else {
            LoggedInUser.setLoggedInUser(null);
            responseLabel.setText("FAILED AUTH");
        }
    }

    @FXML
    private void handleLogoutButton() {
        LoggedInUser.setLoggedInUser(null);
        responseLabel.setText("LOGGED OUT");
    }

    // Creates new account and logs in automatically
    @FXML
    private void handleCreateAccountButton() {
        try {
            if (usernameField.getText().length() == 0 || passwordField.getText().length() == 0) {
                responseLabel.setText("ENTER VALID VALUES");
                return;
            }
            DataController.createUser(usernameField.getText(), passwordField.getText());
            this.login();
            responseLabel.setText("USER CREATED AND LOGGED IN");
        } catch (MySQLIntegrityConstraintViolationException e) {
            responseLabel.setText("USER ALREADY EXISTS");
        }
    }


}
