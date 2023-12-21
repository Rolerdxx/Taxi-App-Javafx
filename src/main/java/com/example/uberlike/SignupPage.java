package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SignupPage {

    private Database db;
    private Stage stage;

    @FXML
    private TextField inputName;

    @FXML
    private TextField inputEmail;

    @FXML
    private TextField inputPassword;

    @FXML
    private RadioButton driverRadio;

    @FXML
    private RadioButton passengerRadio;

    public SignupPage(Database db, Stage stage) {
        this.db = db;
        this.stage = stage;
    }

    @FXML
    protected void signUp() throws SQLException {
        String name = inputName.getText();
        String email = inputEmail.getText();
        String password = inputPassword.getText();
        String type = (driverRadio.isSelected()) ? "Driver" : "Passenger";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid email format.");
            return;
        }

        if (password.length() < 8) {
            showAlert("Password must be at least 8 characters long.");
            return;
        }


        db.createUser(name, email, password, type);

        closeWindow();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        stage.close();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
