package game.models;

// Holds information about a certain move made by a player or bot in a game for easier handling of DB results and such

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.sql.Date;

public class Move {

    private IntegerProperty moveNbr;
    private IntegerProperty playerNbr;
    private StringProperty playerUsername;
    private IntegerProperty rowCoord;
    private IntegerProperty colCoord;
    private StringProperty playerColor;
    private StringProperty timeStamp;

    public Move(int moveNbr, int playerNbr, String username, int rowCoord, int colCoord, String playerColor, Date timeStamp) {
        this.moveNbr = new SimpleIntegerProperty(moveNbr);
        this.playerNbr = new SimpleIntegerProperty(playerNbr);
        this.playerUsername = new SimpleStringProperty(username);
        this.rowCoord = new SimpleIntegerProperty(rowCoord);
        this.colCoord = new SimpleIntegerProperty(colCoord);
        this.playerColor = new SimpleStringProperty(playerColor.toString());
        this.timeStamp = new SimpleStringProperty(timeStamp.toString());
    }

    public int getMoveNbr() {
        return moveNbr.get();
    }

    public IntegerProperty moveNbrProperty() {
        return moveNbr;
    }

    public void setMoveNbr(int moveNbr) {
        this.moveNbr.set(moveNbr);
    }

    public int getPlayerNbr() {
        return playerNbr.get();
    }

    public IntegerProperty playerNbrProperty() {
        return playerNbr;
    }

    public void setPlayerNbr(int playerNbr) {
        this.playerNbr.set(playerNbr);
    }

    public String getPlayerUsername() {
        return playerUsername.get();
    }

    public StringProperty playerUsernameProperty() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername.set(playerUsername);
    }

    public int getRowCoord() {
        return rowCoord.get();
    }

    public IntegerProperty rowCoordProperty() {
        return rowCoord;
    }

    public void setRowCoord(int rowCoord) {
        this.rowCoord.set(rowCoord);
    }

    public int getColCoord() {
        return colCoord.get();
    }

    public IntegerProperty colCoordProperty() {
        return colCoord;
    }

    public void setColCoord(int colCoord) {
        this.colCoord.set(colCoord);
    }

    public String getPlayerColor() {
        return playerColor.get();
    }

    public StringProperty playerColorProperty() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor.set(playerColor);
    }

    public String getTimeStamp() {
        return timeStamp.get();
    }

    public StringProperty timeStampProperty() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp.set(timeStamp);
    }
}
