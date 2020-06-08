package net.machina.networkjava.class7;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private Connection conn;
    private Statement stmt;
    private PreparedStatement prepStmt;
    private ResultSet result;

    public DBConnection() {
        conn = null;
        stmt = null;
        prepStmt = null;
        result = null;
    }

    public void connect() {
        String dblocation = "jdbc:sqlite:networkjava.sqlite";
        System.setProperty("jdbc.Drivers", "org.sqlite.JDBC");
        try {
            conn = DriverManager.getConnection(dblocation);
            if (conn != null) {
                System.out.println("Connected to database");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void disconnect() {
        try {
            if (!conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createTables() {
        try {
            stmt = conn.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS links_temp " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "day TEXT)";
            if (!stmt.execute(query)) {
                System.out.println("Table added");
            }

            query = "CREATE TABLE IF NOT EXISTS links_visited " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "day TEXT)";
            if (!stmt.execute(query)) {
                System.out.println("Table added");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropTables() {
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS links_temp");
            stmt.execute("DROP TABLE IF EXISTS links_visited");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public synchronized void addLinkToTempTab(String link) {
        try {
            prepStmt = conn.prepareStatement("INSERT INTO links_temp (name, day) VALUES(?, datetime('now', 'localtime'))");
            prepStmt.setString(1, link);
            int num = prepStmt.executeUpdate();
//            System.out.println("Updated " + num + " row(s)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public synchronized void addLinkToVisitedTab(String link) {
        try {
            prepStmt = conn.prepareStatement("INSERT INTO links_visited (name, day) VALUES(?, datetime('now', 'localtime'))");
            prepStmt.setString(1, link);
            int num = prepStmt.executeUpdate();
//            System.out.println("Updated " + num + " row(s)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getTempSize() {
        try {
            stmt = conn.createStatement();
            String query = "SELECT COUNT(id) FROM links_temp";
            result = stmt.executeQuery(query);
            int number = 0;
            while (result.next()) {
                number = result.getInt(1);
            }
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public int getVisitedSize() {
        try {
            stmt = conn.createStatement();
            String query = "SELECT COUNT(id) FROM links_visited";
            result = stmt.executeQuery(query);
            int number = 0;
            while (result.next()) {
                number = result.getInt(1);
            }
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public synchronized String getLinkToVisit(){
        try {

            prepStmt = conn.prepareStatement("select name from links_temp limit 3");
            result = prepStmt.executeQuery();

            ArrayList<String> tempLink = new ArrayList<>();
            while (result.next()){
                tempLink.add(result.getString("name"));
            }

            for (String s : tempLink) {
                prepStmt = conn.prepareStatement("select name from links_visited where name = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                prepStmt.setString(1, s);
                result = prepStmt.executeQuery();

                result.next();
                int number = result.getRow();
                //System.out.println(number);
                if (number > 0) {
                    prepStmt = conn.prepareStatement("delete from links_temp where name = ?");
                    prepStmt.setString(1, s);
                    int delRow = prepStmt.executeUpdate();
                    System.out.println("Removed " + delRow + " duplicates");
                } else {
                    return s;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "";
    }

    public void deleteFromTemp(String s) {
        try {
            prepStmt = conn.prepareStatement("delete from links_temp where name = ?");
            prepStmt.setString(1, s);
            int delRow = prepStmt.executeUpdate();
            System.out.println("Removed " + delRow + " duplicates");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<String> getMultipleLinksToVisit(int number) {
        List<String> temp = new ArrayList<>();
        int rowNumber = 0;

        try {
            prepStmt = conn.prepareStatement("select name from links_temp limit ?");
            prepStmt.setInt(1, number * 5);
            result = prepStmt.executeQuery();

            ArrayList<String> tempLink = new ArrayList<>();
            while (result.next()){
                tempLink.add(result.getString("name"));
            }

            do {
                prepStmt = conn.prepareStatement("select name from links_visited where name = ?");
                prepStmt.setString(1, tempLink.get(rowNumber));
                result = prepStmt.executeQuery();
                result.next();
                //System.out.println(number);
                if (result.getRow() > 0) {
                    prepStmt = conn.prepareStatement("delete from links_temp where name = ?");
                    prepStmt.setString(1, tempLink.get(rowNumber));
                    int delRow = prepStmt.executeUpdate();
                    System.out.println("Removed " + delRow + " duplicates");
                } else {
                    temp.add(tempLink.get(rowNumber));
                }
                rowNumber++;
            } while(temp.size() < number);
            return temp;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}