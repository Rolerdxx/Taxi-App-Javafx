package com.example.uberlike;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Database db = new Database();
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("LoginPage.fxml"));
        LoginPage c = new LoginPage(db, stage);
        fxmlLoader.setController(c);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login page");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}