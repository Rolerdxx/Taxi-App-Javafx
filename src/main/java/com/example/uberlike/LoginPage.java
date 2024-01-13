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

    @FXML
    protected void onLoginButtonClick() throws IOException, SQLException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Email and Password are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid email format.");
            return;
        }

        if (authenticateUser(email, password)) {
            // User authentication successful, proceed to ClientPage
            FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("ClientPage.fxml"));
            Stage stage = new Stage();
            ClientPage newController = new ClientPage(primalStage, stage, db);
            fxmlLoader.setController(newController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Hello!222");
            stage.setScene(scene);
            newController.Initiate();

            stage.show();
            primalStage.hide(); // Hides the login stage
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
            return BCrypt.checkpw(password, hashedPassword); // Correctly using BCrypt to compare the password
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
