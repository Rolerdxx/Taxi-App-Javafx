//package com.example.uberlike;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UserController {
//    private static PreparedStatement ps;
//
//    public UserController(){
//
//    }
//
//    public static int addUser(User us){
//        try {
//            String sql = "INSERT INTO USERR (NAME, EMAIL, PASSWORD, TYPE) VALUES (?, ?, ?, ?)";
//            ps = Database.c.prepareStatement(sql);
//            ps.setString(1, us.getName());
//            ps.setString(2, us.getEmail());
//            ps.setString(3, us.getPassword());
//            ps.setString(4, us.getType());
//            int rs = ps.executeUpdate();
//            return rs;
//        }catch (Exception e) {
//            System.err.println(e.getClass().getName()+": "+e.getMessage());
//            System.exit(0);
//        }
//        return -1;
//    }
//    public static User Login(String email, String pass) throws SQLException {
//        try {
//            String sql = "SELECT * FROM USERR WHERE EMAIL=? AND PASSWORD=?";
//            ps = Database.c.prepareStatement(sql);
//            ps.setString(1, email);
//            ps.setString(2, pass);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()){
//                return new User(rs.getInt("ID"),rs.getString("NAME"),rs.getString("EMAIL"),rs.getString("PASSWORD"),rs.getString("TYPE"),rs.getInt("PHONE"));
//            }else {
//                return null;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.err.println(e.getClass().getName()+": "+e.getMessage());
//            System.exit(0);
//        }
//        return null;
//    }
//}
