package com.example.uberlike;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SignupPage {

    private Database db;
    private Stage stage;

    @FXML
    private TextField getinputname;

    @FXML
    private TextField getinputemail;

    @FXML
    private TextField getinputpassword;

    @FXML
    private TextField getinputtype;

    public SignupPage(Database db, Stage stage) {
        this.db = db;
        this.stage = stage;
    }

    @FXML
    protected void signUp() throws SQLException {
        String name = getinputname.getText();
        String email = getinputemail.getText();
        String password = getinputpassword.getText();
        String type = getinputtype.getText();

        db.createUser(name, email, password, type);


        closeWindow();
        stage.close();
    }

    private void closeWindow() {
        stage.close();
    }
}
