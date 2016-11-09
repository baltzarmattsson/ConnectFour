package game.models;

// Used to hold information about a player, both human and bot, for easier handling of DB results and such

import game.logic.Difficulty;
import javafx.scene.paint.Color;

import java.util.Optional;

public class PlayerInfo {

    private int playerNumber;
    private boolean hasUsername;
    private String username;
    private Color discColor;
    private boolean isAI;
    private Difficulty aiDifficulty;
    private int points;

    public PlayerInfo(int playerNumber, Optional<String> username, Color discColor, boolean isAI, Difficulty aiDifficulty) {
        this.playerNumber = playerNumber;
        this.hasUsername = username.isPresent();
        this.username = username.isPresent() ? username.get() : "";
        this.discColor = discColor;
        this.isAI = isAI;
        this.aiDifficulty = aiDifficulty;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public boolean isHasUsername() {
        return hasUsername;
    }

    public void setHasUsername(boolean hasUsername) {
        this.hasUsername = hasUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Color getDiscColor() {
        return discColor;
    }

    public void setDiscColor(Color discColor) {
        this.discColor = discColor;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Difficulty getAiDifficulty() {
        return aiDifficulty;
    }

    public void setAiDifficulty(Difficulty aiDifficulty) {
        this.aiDifficulty = aiDifficulty;
    }
}
