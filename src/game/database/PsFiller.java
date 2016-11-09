package game.database;

// Fills a PreparedStatement with information in the HashMap provided. This is used for easier handling the DAL-queries

import game.logic.Difficulty;
import javafx.scene.paint.Color;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class PsFiller {

    public static PreparedStatement fill(PreparedStatement ps, HashMap<Integer, Object> queryFiller) throws SQLException {

        if (queryFiller != null) {
            for (Map.Entry<Integer, Object> map : queryFiller.entrySet()) {
                Object value = map.getValue();
                if (value instanceof String) {
                    ps.setString(map.getKey(), (String) value);
                } else if (value instanceof Integer) {
                    ps.setInt(map.getKey(), (int) value);
                } else if (value instanceof Date) {
                    ps.setDate(map.getKey(), (Date) value);
                } else if (value instanceof Color) {
                    ps.setString(map.getKey(), value.toString());
                } else if (value instanceof Boolean) {
                    ps.setBoolean(map.getKey(), (boolean) value);
                } else if (value == null || value instanceof Difficulty) {
                    if (value == null)
                        ps.setNull(map.getKey(), Types.VARCHAR);
                    else
                        ps.setString(map.getKey(), value.toString());
                } else {
                    ps.setString(map.getKey(), value.toString());
                    System.out.println("CANT HANDLE THIS STUFF /PSFILLER");
                }
            }
        }
        return ps;
    }
}
