package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("3la slamtek");
    }
}