package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage {

    public Database db;
    public Stage primalStage;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    public LoginPage(Database db, Stage primalStage) {
        this.db = db;
        this.primalStage = primalStage;
    }

    private String getUserType(String email) throws SQLException {
        String userType = null;
        String sql = "SELECT TYPE FROM USERR WHERE EMAIL = ?";
        try (PreparedStatement pstmt = Database.c.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    userType = resultSet.getString("TYPE");
                    System.out.println("Fetched user type: " + userType);
                }
            }
        }
        return userType;
    }



    @FXML
    protected void onLoginButtonClick() throws IOException, SQLException {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (authenticateUser(email, password)) {
            String userType = getUserType(email); // New method to get the user type

            FXMLLoader fxmlLoader;
            Scene scene;
            Stage stage = new Stage();

            if ("Passenger".equals(userType)) {
                fxmlLoader = new FXMLLoader(main.class.getResource("ClientPage.fxml"));
                ClientPage clientController = new ClientPage(primalStage, stage, db);
                fxmlLoader.setController(clientController);
                scene = new Scene(fxmlLoader.load());
                clientController.Initiate();
            } else if ("Driver".equals(userType)) {
                fxmlLoader = new FXMLLoader(main.class.getResource("DriverPage.fxml"));
                DriverPage driverController = new DriverPage();
                fxmlLoader.setController(driverController);
                scene = new Scene(fxmlLoader.load());
            } else {
                showAlert("User type not recognized.");
                return;
            }

            stage.setTitle("Welcome!");
            stage.setScene(scene);
            stage.show();
            primalStage.hide();
        } else {
            showAlert("Invalid email or password.");
        }
    }

    private boolean authenticateUser(String email, String password) throws SQLException {
        String sql = "SELECT PASSWORD FROM USERR WHERE EMAIL = ?";
        PreparedStatement pstmt = db.getC().prepareStatement(sql);
        pstmt.setString(1, email);
        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            String hashedPassword = resultSet.getString("PASSWORD");
            pstmt.close();
            return BCrypt.checkpw(password, hashedPassword);
        }

        pstmt.close();
        return false;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);

    }

    @FXML
    protected void goNext() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("SignupPage.fxml"));
        Stage stage = new Stage();
        SignupPage newController = new SignupPage(db,stage);
        fxmlLoader.setController(newController);
        Scene scene = new Scene(fxmlLoader.load(), 633, 487);
        stage.setTitle("Hello!222");
        stage.setScene(scene);


        stage.show();
        primalStage.hide();


    }
}
