package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SignupPage {

    public Database db;
    public Stage stage;

    @FXML
    public TextField inputName;

    @FXML
    public TextField inputEmail;

    @FXML
    public TextField inputPassword;

    @FXML
    public RadioButton driverRadio;

    @FXML
    public RadioButton passengerRadio;

    @FXML
    private ToggleGroup userTypeToggleGroup;

    public SignupPage(Database db, Stage stage) {
        this.db = db;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        userTypeToggleGroup = new ToggleGroup(); // Initializing the ToggleGroup
        driverRadio.setToggleGroup(userTypeToggleGroup);
        passengerRadio.setToggleGroup(userTypeToggleGroup);
    }

    @FXML
    protected void signUp() throws SQLException {
        String name = inputName.getText();
        String email = inputEmail.getText();
        String password = inputPassword.getText();
        RadioButton selectedRadioButton = (RadioButton) userTypeToggleGroup.getSelectedToggle();
        String type = (selectedRadioButton != null) ? selectedRadioButton.getText() : "";

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

        showAlert("Account Registred");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void closeWindow() {
        stage.close();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
