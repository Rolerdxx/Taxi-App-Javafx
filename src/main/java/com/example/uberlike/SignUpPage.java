package com.example.uberlike;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignUpPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        GridPane.setConstraints(nomField, 0, 0);

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        GridPane.setConstraints(prenomField, 0, 1);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        GridPane.setConstraints(emailField, 0, 2);

        TextField passwordField = new TextField();
        passwordField.setPromptText("Mot de passe");
        GridPane.setConstraints(passwordField, 0, 3);

        TextField numTeleField = new TextField();
        numTeleField.setPromptText("Numéro de téléphone");
        GridPane.setConstraints(numTeleField, 0, 4);

        Button signUpButton = new Button("S'inscrire");
        GridPane.setConstraints(signUpButton, 0, 5);

        Button backButton = new Button("Retour");
        GridPane.setConstraints(backButton, 1, 5);

        grid.getChildren().addAll(nomField, prenomField, emailField, passwordField, numTeleField, signUpButton, backButton);

        Scene scene = new Scene(grid, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Page d'inscription");
        primaryStage.show();
    }
}
