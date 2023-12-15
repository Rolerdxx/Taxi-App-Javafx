package com.example.uberlike;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SignUpPage {

    private final Stage mainStage;
    private final Stage seconsdstagel;

    public SignUpPage(Stage Seconsdstage, Stage mainStage) {
        this.mainStage = mainStage;
        this.seconsdstagel=Seconsdstage;
    }

    @FXML
    private Button btnClose;

    @FXML
    private void initialize() {
        btnClose.setOnAction(event -> closeNewWindow());
    }

    private void closeNewWindow() {
        // You can interact with the main window (close, etc.) using the reference to its Stage
        seconsdstagel.close();
        mainStage.show();
    }
}
