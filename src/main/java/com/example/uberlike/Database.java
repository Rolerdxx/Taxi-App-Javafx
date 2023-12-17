package com.example.uberlike;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private Connection c = null;

    public Database(){
        try {
            Statement stmt = null;
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/dbtest",
                            "postgres", "123");
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully and table created");
    }
}