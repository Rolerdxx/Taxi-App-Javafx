package com.example.uberlike;

import javafx.scene.control.Alert;

import java.sql.*;

public class Database {
    public static Connection c;
    private Statement stmt;

    public Connection getC() {
        return c;
    }

    public Database(){
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "postgres");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USERR (" +
                    "ID SERIAL PRIMARY KEY NOT NULL, " +
                    "NAME TEXT NOT NULL, " +
                    "EMAIL TEXT NOT NULL, " +
                    "PASSWORD TEXT NOT NULL, " +
                    "TYPE TEXT NOT NULL, " +
                    "PHONE INT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully and table created");
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