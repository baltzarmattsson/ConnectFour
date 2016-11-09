package game.models;

// Used as a holder for a highscore entry

import javafx.beans.property.*;

public class HighScoreEntry {

    private StringProperty username;
    private IntegerProperty score;
    private IntegerProperty gamesPlayed;
    private DoubleProperty ratio;

    public HighScoreEntry(String username, int score, int gamesPlayed) {
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleIntegerProperty(score);
        this.gamesPlayed = new SimpleIntegerProperty(gamesPlayed);
        this.ratio = new SimpleDoubleProperty((double) score / gamesPlayed);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public int getGamesPlayed() {
        return gamesPlayed.get();
    }

    public IntegerProperty gamesPlayedProperty() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed.set(gamesPlayed);
    }

    public double getRatio() {
        return ratio.get();
    }

    public DoubleProperty ratioProperty() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio.set(ratio);
    }
}
