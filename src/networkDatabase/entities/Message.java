package networkDatabase.entities;

import networkDatabase.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Message {
    private Integer id;
    private String message;
    private Integer destination;
    private static final Connection con = Database.getConnection();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public static void create(String message, Integer destination) {
        try (PreparedStatement s = con.prepareStatement("INSERT INTO message VALUES(?, ?, ?)")) {
            s.setInt(1, count() + 1);
            s.setString(2, message);
            s.setInt(3, destination);

            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int count() {
        try {
            ResultSet r = con.createStatement().executeQuery("SELECT count(id) FROM message;");
            r.next();

            return r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<String> getMessagesFor(int id) {
        List<String> messages = new ArrayList<>();
        try(PreparedStatement s = con.prepareStatement("SELECT message FROM message WHERE destination = ?")){
            s.setInt(1, id);
            ResultSet r = s.executeQuery();

            while (r.next()) {
                messages.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}