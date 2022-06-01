package networkDatabase.entities;

import networkDatabase.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private int id;
    private String name;
    private static final Connection con = Database.getConnection();

    Person(){

    }

    Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int count() {
        try {
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery("SELECT count(id) FROM person;");
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void create(String name) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "INSERT INTO person VALUES(?, ?);")) {

            pstmt.setInt(1, count() + 1);
            pstmt.setString(2, name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static List<Person> getPersons() {
        try{
            Statement s = con.createStatement();
            List<Person> persons = new ArrayList<>();
            ResultSet r = s.executeQuery("SELECT * FROM person;");

            while (r.next()) {
                persons.add(new Person(r.getInt(1), r.getString(2)));
            }
            return persons;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void renamePerson(String name, String newName) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "UPDATE person SET name = ? WHERE name = ?;")) {

            pstmt.setString(1, name);
            pstmt.setString(2, newName);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static void deletePerson(int id) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "DELETE FROM person WHERE id = ?")) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public static Person getPerson(int id) {
        try (PreparedStatement pstmt = con.prepareStatement(
                "SELECT * FROM person WHERE id = ?;")) {

            pstmt.setInt(1, id);

            ResultSet r = pstmt.executeQuery();
            r.next();
            return new Person(id, r.getString(2));

        } catch (SQLException e) {
            System.err.println(e);
        }
        return new Person();
    }

    public static boolean userExists(String name) {
        try (PreparedStatement s = con.prepareStatement("SELECT * FROM person WHERE name = ?")) {
            s.setString(1, name);
            ResultSet r = s.executeQuery();
            if(r.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getIdByName(String name) {
        try(PreparedStatement s =
                    con.prepareStatement("SELECT id FROM person WHERE name = ?")) {
            s.setString(1, name);
            ResultSet r = s.executeQuery();
            r.next();
            return r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}