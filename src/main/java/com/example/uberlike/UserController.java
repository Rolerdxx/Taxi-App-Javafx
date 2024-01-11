package com.example.uberlike;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.List;

public class UserController {
    private Database db;
    private PreparedStatement ps;

    public UserController(Database dbb){
        db = dbb;
    }

    public int addUser(User us) throws SQLException {
        try {
            String sql = "INSERT INTO USERR (NAME, EMAIL, PASSWORD, TYPE) VALUES (?, ?, ?, ?)";
            ps = db.getC().prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPassword());
            ps.setString(4, us.getType());
            int rs = ps.executeUpdate();
            System.out.println("added" + rs);
            return rs;
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return -1;
    }
    public User Login(String email, String pass) throws SQLException {
        try {
            String sql = "SELECT * FROM USERR WHERE EMAIL=? AND PASSWORD=?";
            ps = db.getC().prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new User(rs.getInt("ID"),rs.getString("NAME"),rs.getString("EMAIL"),rs.getString("PASSWORD"),rs.getString("TYPE"),rs.getInt("PHONE"));
            }else {
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return null;
    }
}
