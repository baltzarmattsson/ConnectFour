package game.database;

// This handles ALL the traffic between the DB and the game. No further comments are added since
// they are quite self explanatory by reading the method names.
// All methods are called by DataController-class and no other classes/objects

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import game.logic.Difficulty;
import game.logic.Game;
import game.models.HighScoreEntry;
import game.models.Move;
import game.models.PlayerInfo;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class DAL {

    public ArrayList<Move> loadGame(int gameID) {

        ArrayList<Move> retMoves = new ArrayList<Move>();

        String query = "select movenbr, player, username, rowCoord, colCoord, color, date from Moves " +
                "where Moves.gameid = ?";

        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        queryFiller.put(1, gameID);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs;

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            while (rs.next())
                retMoves.add(new Move(
                        rs.getInt("movenbr"),
                        rs.getInt("player"),
                        rs.getString("username"),
                        rs.getInt("rowCoord"),
                        rs.getInt("colCoord"),
                        rs.getString("color"),
                        rs.getDate("date")
                ));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }

        return retMoves;

    }

    public void registerMove(int gameID, int moveNbr, int playerNbr, String username, int rowCoord, int colCoord, Color color) {


        String query;
        if (username.length() > 0) {
            query = "insert into Moves (gameid, movenbr, player, username, rowCoord, colCoord, color) values (?, ?, ?, ?, ?, ?, ?);";
        } else {
            query = "insert into Moves (gameid, movenbr, player, rowCoord, colCoord, color) values (?, ?, ?, ?, ?, ?);";
        }

        Connection con = null;
        PreparedStatement ps;

        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        int indexCounter = 1;
        queryFiller.put(indexCounter++, gameID);
        queryFiller.put(indexCounter++, moveNbr);
        queryFiller.put(indexCounter++, playerNbr);
        if (username.length() > 0)
            queryFiller.put(indexCounter++, username);
        queryFiller.put(indexCounter++, rowCoord);
        queryFiller.put(indexCounter++, colCoord);
        queryFiller.put(indexCounter++, color);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            System.out.println("gameid: " + gameID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }

    }

    public int createNewGame(int columns, int rows, int nbrOfPlayers) {
        String query = "insert into Game values (DEFAULT, DEFAULT, ?, ?, ?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs;

        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        queryFiller.put(1, columns);
        queryFiller.put(2, rows);
        queryFiller.put(3, nbrOfPlayers);

        int last_insert_id = 0;

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            ps.executeUpdate();

            query = "select last_insert_id() from Game";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next())
                last_insert_id = rs.getInt(1);

            return last_insert_id;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }

        return last_insert_id;

    }

    public Game findGameByGameID(int gameID) {
        String query = "select * from Game where id = ?";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs;

        queryFiller.put(1, gameID);

        Game retGame = null;

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            if (rs.next())
                retGame = new Game(
                        rs.getInt("id"),
                        rs.getDate("datePlayed"),
                        rs.getInt("nbrofcols"),
                        rs.getInt("nbrofrows"),
                        rs.getInt("nbrofplayers")
                );


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
        return retGame;
    }

    public void saveGame(int gameId, boolean wasSavedGame, boolean isFinished, String winner) {
        String query = "";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps = null;

        if (wasSavedGame && isFinished) {
            if (winner != null) {
                query = "update Gamestatus set finished = true, winner = ? where gameid = ?";
                queryFiller.put(1, winner);
                queryFiller.put(2, gameId);
            } else {
                query = "update Gamestatus set finished = true, winner = null where gameid = ?";
                queryFiller.put(1, gameId);
            }

        } else if (wasSavedGame && isFinished == false) {
            // Do nothing, the entry is already present in the database
            return;
        } else if (wasSavedGame == false && isFinished == false) {
            query = "insert into Gamestatus values (?, false, DEFAULT, DEFAULT)";
            queryFiller.put(1, gameId);
        } else if (wasSavedGame == false && isFinished) {
            query = "insert into Gamestatus values (?, true, DEFAULT, DEFAULT)";
            queryFiller.put(1, gameId);
        }

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
    }

    public void createUser(String username, String password) throws MySQLIntegrityConstraintViolationException {

        String query = "insert into Credentials values (?, ?)";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps;

        queryFiller.put(1, username);
        queryFiller.put(2, password);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);

            ps.executeUpdate();

        } catch (MySQLIntegrityConstraintViolationException ce) {
            throw ce;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
    }

    public boolean loginUser(String username, String password) {

        String query = "select password from Credentials where username = ?";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs;

        queryFiller.put(1, username);


        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            if (rs.next())
                return rs.getString(1).equals(password);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
        return false;
    }

    public boolean checkUsername(String username) {
        if (username.length() == 0)
            return true; // Since this will be handled as a null value

        String query = "select username from Credentials where username = ?";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;

        queryFiller.put(1, username);


        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            // If rs.next() works, it means theres a match; username exists
            return rs.next();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
        return false;

    }

    public ArrayList<Integer> findGamesByUsername(String username, boolean currentlyPlaying) {

        ArrayList<Integer> gameIDs = new ArrayList<Integer>();

        String tableName = currentlyPlaying ? "Playing" : "Played";
        String query = "select gameid from " + tableName + "  where username = ?";

        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        queryFiller.put(1, username);

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;

        queryFiller.put(1, username);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            // Adding all found gameids to list
            while (rs.next())
                gameIDs.add(rs.getInt(1));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }

        return gameIDs;
    }

    public void registerPlayerOnGame(int playerNbr, int gameID, Color color, String username, boolean isAI, Difficulty aiDifficulty) {
        String query = "insert into Playing values (?, ?, ?, ?, ?, ?)";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps;

        int indexCounter = 1;
        queryFiller.put(indexCounter++, playerNbr);
        queryFiller.put(indexCounter++, gameID);
        queryFiller.put(indexCounter++, color);
        queryFiller.put(indexCounter++, username);
        queryFiller.put(indexCounter++, isAI);
        queryFiller.put(indexCounter++, aiDifficulty);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
    }

    public void deregisterUsernameFromGameAndRegisterInPlayedTable(int gameID, String username) {
        String query = "delete from Playing where username = ? and gameid = ?";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps;

        queryFiller.put(1, username);
        queryFiller.put(2, gameID);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);

            ps.executeUpdate();


            // Adding into Played-table
            query = "insert into Played values (?, ?)";
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }

    }

    public HashMap<Integer, Move> findMovesByGameID(int gameID) {

        HashMap<Integer, Move> moves = new HashMap<Integer, Move>();

        String query = "select movenbr, player, username, rowCoord, colCoord, color, date from Moves where gameid = ?";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;

        queryFiller.put(1, gameID);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            while (rs.next())
                moves.put(rs.getInt("movenbr"), new Move(
                        rs.getInt("movenbr"),
                        rs.getInt("player"),
                        rs.getString("username"),
                        rs.getInt("rowCoord"),
                        rs.getInt("colCoord"),
                        rs.getString("color"),
                        rs.getDate("date")
                ));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }

        return moves;
    }

    public HashMap<Integer, PlayerInfo> findPlayerInfoByGameID(int gameID) {

        HashMap<Integer, PlayerInfo> players = new HashMap<Integer, PlayerInfo>();

        String query = "select * from Playing where gameid = ?";
        HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;

        queryFiller.put(1, gameID);

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            PsFiller.fill(ps, queryFiller);
            rs = ps.executeQuery();

            while (rs.next())
                players.put(rs.getInt("playernbr") - 1, new PlayerInfo(
                        rs.getInt("playernbr"),
                        rs.getString("username") == null ? Optional.empty() : Optional.ofNullable(rs.getString("username")),
                        Color.web(rs.getString("color")),
                        rs.getBoolean("isAI"),
                        rs.getString("aiDifficulty") == null ? null
                                : (rs.getString("aiDifficulty").equals(Difficulty.EASY.toString())) ? Difficulty.EASY
                                : (rs.getString("aiDifficulty").equals(Difficulty.MEDIUM.toString())) ? Difficulty.MEDIUM
                                : (rs.getString("aiDifficulty").equals(Difficulty.HARD.toString())) ? Difficulty.HARD
                                : null
                ));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
        return players;
    }

    public ArrayList<HighScoreEntry> findHighscores() {
        ArrayList<HighScoreEntry> highScores = new ArrayList<HighScoreEntry>();

        String query = "select winner as username, count(winner) as score, (select count(*) from Played where Played.username = Gamestatus.winner) as gamesPlayed\n" +
                "from Gamestatus\n" +
                "inner join Credentials\n" +
                "on winner = username\n" +
                "group by winner;";

        Connection con = null;
        PreparedStatement ps;
        ResultSet rs;

        try {
            con = Connector.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next())
                    highScores.add(new HighScoreEntry(
                            rs.getString("username"),
                            rs.getInt("score"),
                            rs.getInt("gamesPlayed")
                            )
                    );

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                }
        }
        return highScores;
    }

}
