package com.example.uberlike;
import java.sql.*;

public class Database {
    public Connection c;
    private Statement stmt;

    public Connection getC() {
        return c;
    }

    public Database() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dbtest",
                    "postgres", "123");
            stmt = c.createStatement();

            createUsersTable();
            createRidesTable();
            createOFFERTable();

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully and tables created");
    }

    private void createUsersTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS USERR (" +
                    "ID SERIAL PRIMARY KEY NOT NULL, " +
                    "NAME TEXT NOT NULL, " +
                    "EMAIL TEXT NOT NULL, " +
                    "PASSWORD TEXT NOT NULL, " +
                    "TYPE TEXT NOT NULL, " +
                    "PHONE INT NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("USERR table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void createRidesTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS RIDES(" +
                    "ID SERIAL PRIMARY KEY NOT NULL, " +
                    "PASSENGER INTEGER REFERENCES USERR(ID)," +
                    "ORIGIN TEXT NOT NULL," +
                    "DESTINATION TEXT NOT NULL," +
                    "STATE TEXT NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("RIDES table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void createOFFERTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS OFFERS(" +
                    "ID SERIAL PRIMARY KEY NOT NULL, " +
                    "RIDE INTEGER REFERENCES RIDES(ID)," +
                    "DRIVER INTEGER REFERENCES USERR(ID)," +
                    "PRICE INTEGER," +
                    "STATE TEXT NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("OFFERS table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void AddRide(int passenger, String origin, String destination) {
        try {
            String sql = "INSERT INTO RIDES (PASSENGER, ORIGIN, DESTINATION, STATE) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, passenger);
            pstmt.setString(2, origin);
            pstmt.setString(3, destination);
            pstmt.setString(4, "REQUESTED");
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public void AddOffer(int ride, int driver, int price) {
        try {
            String sql = "INSERT INTO OFFERS (RIDE, DRIVER, PRICE, STATE) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, ride);
            pstmt.setInt(2, driver);
            pstmt.setInt(3, price);
            pstmt.setString(4, "WAITING");
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void AcceptOffer(int offid, int rideid){
        try {
            String sql = "UPDATE OFFERS SET STATE='ACCEPTED' WHERE ID="+offid;
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "UPDATE RIDES SET STATE='ONGOING' WHERE ID="+rideid;
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createUser(String name, String email, String password, String type , int phone) {
        try {
            String sql = "INSERT INTO USERR (NAME, EMAIL, PASSWORD, TYPE, PHONE) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, type);
            pstmt.setInt(5, phone);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public boolean doesUserExist(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM userr WHERE email = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            pstmt.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}