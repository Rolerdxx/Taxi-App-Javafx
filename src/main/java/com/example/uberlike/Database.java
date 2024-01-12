package com.example.uberlike;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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
                    "postgres", "123");
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
    }
}