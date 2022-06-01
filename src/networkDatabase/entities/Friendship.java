package networkDatabase.entities;

import networkDatabase.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Friendship {
    private int requestId;
    private int addressee;
    private static final Connection con = Database.getConnection();

    public Friendship(int requestId, int addressee) {
        this.requestId = requestId;
        this.addressee = addressee;
    }

    public static List<Integer> getAllFriendsId(int requestId) {
        try(PreparedStatement friendsStatement =
                    con.prepareStatement("SELECT \"addresseeId\" FROM friendship WHERE \"requestId\" = ?")){
            friendsStatement.setInt(1, requestId);
            ResultSet friends = friendsStatement.executeQuery();

            List<Integer> friendsId = new ArrayList<>();
            while(friends.next()) {
                friendsId.add(friends.getInt(1));
            }
            return friendsId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getAddressee() {
        return addressee;
    }

    public void setAddressee(int addressee) {
        this.addressee = addressee;
    }

    public static void create(int id1, int id2) throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(
                "INSERT INTO friendship VALUES(?, ?);")) {

            pstmt.setInt(1, id1);
            pstmt.setInt(2, id2);

            pstmt.executeUpdate();

            pstmt.setInt(1, id2);
            pstmt.setInt(2, id1);

            pstmt.executeUpdate();

        }
    }


    public static List<Friendship> getFriendships() {
        try{
            Statement s = con.createStatement();
            List<Friendship> friendships = new ArrayList<>();
            ResultSet r = s.executeQuery("SELECT * FROM friendship;");

            while (r.next()) {
                friendships.add(new Friendship(r.getInt(1), r.getInt(2)));
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static int countFriends(int requestId) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "INSERT count(*) FROM friendship WHERE requestId = ?;")) {
            pstmt.setInt(1, requestId);

            ResultSet r = pstmt.executeQuery();
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return 0;
    }


}