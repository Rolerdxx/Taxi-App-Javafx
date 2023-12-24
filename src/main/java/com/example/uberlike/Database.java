package com.example.uberlike;

import javafx.scene.control.Alert;

import java.sql.*;

public class Database {
    private Connection c;
    private Statement stmt;

    public Connection getC() {
        return c;
    }

    public Database(){
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "hamza2001");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USERR " +
                    "(ID SERIAL PRIMARY KEY    NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " EMAIL          TEXT     NOT NULL, " +
                    " PASSWORD        TEXT, " +
                    " TYPE         TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully and table created");
    }

    public void createUser(String name, String email, String password, String type) {
        try {
            String sql = "INSERT INTO USERR (NAME, EMAIL, PASSWORD, TYPE) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, type);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}