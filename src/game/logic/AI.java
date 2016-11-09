package game.logic;

// The bot of the game. Takes help from LogicHelper in order to make a good move, depending on which
// difficulty from the Difficulty class it has.
// The moves are classified with an internal ranking system, and it then chooses between different bounds
// depending on the difficulty. For example, if Difficulty = HARD, it will always make the best move.
// If it's MEDIUM, it will take a move somewhere in the middle, which means a medium-good move,
// and if it's EASY, it will always take the worst move available. It also priorities screwing up for other players instead of winning.

import game.models.Coordinate;
import game.models.PlayerInfo;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AI {

    private Difficulty difficulty;
    private int playerNbr;
    private Color playerColor;

    private final int OTHER_PLAYER_MULTIPLIER = 2;
    private final int OWN_PLAYER_MULTIPLIER = 1;

    // Key == How many "points" the move will return
    // Value == An arraylist containing on which coordinates the score (key) is

    // AI uses a point-system, if it disrupts another player on a certain point, that point will yield
    // a higher "score" than on a Coordinate that is not disupting anything. Each KEY determines how many points
    // a disc on a Coordinate will yield, and then it choses from that ArrayList<Coordinate> randomly, but from a certain
    // KEY depending on the difficulty. Higher difficulty = higher KEY, and therefore harder to play against
    private HashMap<Integer, ArrayList<Coordinate>> pointSystem;

    public AI(Difficulty difficulty, int playerNbr, Color playerColor) {
        this.difficulty = difficulty;
        this.playerNbr = playerNbr;
        this.playerColor = playerColor;
    }


    // Returns a coordinate of a move which is generated with the information given and the this.difficulty setting
    public Coordinate makeMove(HashMap<Integer, PlayerInfo> playerInfo, int rows, int columns, String[][] board) {

        pointSystem = new HashMap<Integer, ArrayList<Coordinate>>();

        // Bounds to know which indexes we can look between in the end
        int lowerBound = -1, upperBound = -1;

        for (PlayerInfo pi : playerInfo.values()) {
            int pointsForCoordinate;
            int playerNumber = pi.getPlayerNumber();

            for (int column = 0; column < columns; column++) {
                int topmostEmptyRowOrMinusOne = LogicHelper.getTopmostEmptyRowInColumn(column, rows, board);

                // -1 if the column is full, we do nothing since nothing can be added to the current row
                if (topmostEmptyRowOrMinusOne != -1) {

                    // Will check the top-most index of the column for moves in all directions.
                    for (Direction possibleDirections : Direction.values()) {

                        // Temporarily adding playerNumber to the index to see how much points it would yield
                        board[topmostEmptyRowOrMinusOne][column] = Integer.toString(playerNumber);
                        pointsForCoordinate = LogicHelper.checkDirection(possibleDirections, playerNumber, topmostEmptyRowOrMinusOne, column, board);

                        // Removing the temporary playerNumber
                        board[topmostEmptyRowOrMinusOne][column] = " ";

                        // Since this bot favors stopping opponents before winning, the points are
                        // multiplied to a higher priority if it's another player compared to the bots own playerNbr
                        if (playerNumber != this.playerNbr) {
                            pointsForCoordinate *= OTHER_PLAYER_MULTIPLIER;
                        } else {
                            pointsForCoordinate *= OWN_PLAYER_MULTIPLIER;
                        }

                        // If there's no arraylist on the index, i.e. this is the first tiem this point has been generated, we add one
                        if (pointSystem.containsKey(pointsForCoordinate) == false)
                            pointSystem.put(pointsForCoordinate, new ArrayList<Coordinate>());

                        // Adds the coordinates (value) of points (key) to the list
                        pointSystem.get(pointsForCoordinate).add(new Coordinate(column, topmostEmptyRowOrMinusOne));

                        // Updating the bounds for easier looping in the end of this method
                        if (upperBound == -1 || pointsForCoordinate > upperBound)
                            upperBound = pointsForCoordinate;
                        if (lowerBound == -1 || pointsForCoordinate < lowerBound)
                            lowerBound = pointsForCoordinate;

                    }
                }
            }
        }


        ArrayList<Coordinate> selectedCoordinates = null;

        switch (this.difficulty) {
            case EASY:
                // Always selecting the worst coordinate, i.e. the lowest points
                selectedCoordinates = pointSystem.get(lowerBound);
                break;
            case MEDIUM:
                // Selects somewhere in the middle. If upperBound / 2 yields nothing, it expands the search
                // radius by decrementing lowerBound and incrementing upperBound
                int middleBound = upperBound / 2;

                lowerBound = middleBound;
                upperBound = middleBound;

                while (selectedCoordinates == null) {
                    for (middleBound = lowerBound; middleBound < upperBound; middleBound++)
                        selectedCoordinates = pointSystem.get(middleBound);

                    lowerBound--;
                    upperBound++;
                }

                break;
            case HARD:
                // Always selecting the best coordinate
                selectedCoordinates = pointSystem.get(upperBound);
                break;
        }

        // Can be null if nothing can be added
        if (selectedCoordinates != null)
            return selectedCoordinates.get(new Random().nextInt(selectedCoordinates.size()));
        else
            return null;

    }


    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getPlayerNbr() {
        return playerNbr;
    }

    public void setPlayerNbr(int playerNbr) {
        this.playerNbr = playerNbr;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }


}
