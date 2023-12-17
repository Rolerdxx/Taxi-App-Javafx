package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginPage {
    @FXML
    private Stage primalstage;

    public LoginPage(Stage s){
        primalstage = s;
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("3la slamtek");
    }

    @FXML
    protected void goNext() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("SignUpPage.fxml"));


        Stage stage = new Stage();
        SignUpPage newController = new SignUpPage();
        fxmlLoader.setController(newController);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!222");
        stage.setScene(scene);


        stage.show();
        primalstage.hide();

//

//        loader.setController(newController);
//
//        Scene scene = new Scene(loader.load(), 300, 200);
//        newStage.setScene(scene);
//
//        newStage.initModality(Modality.APPLICATION_MODAL);
//        newStage.show();
    }
}