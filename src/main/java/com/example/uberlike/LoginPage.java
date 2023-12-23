package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class LoginPage {

    private Database db;
    @FXML
    private Stage primalstage;

    public LoginPage(Stage s ,Database dbb){
        primalstage = s;
        db = dbb;
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("ClientPage.fxml"));


        Stage stage = new Stage();
        ClientPage newController = new ClientPage(primalstage,stage,db);
        fxmlLoader.setController(newController);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!222");
        stage.setScene(scene);
        newController.Initiate();


        stage.show();
        primalstage.hide();
    }

    @FXML
    protected void goNext() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("SignupPage.fxml"));


        Stage stage = new Stage();
        SignupPage newController = new SignupPage(db,stage);
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