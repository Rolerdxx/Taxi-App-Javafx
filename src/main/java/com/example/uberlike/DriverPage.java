package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverPage {

    @FXML
    private VBox rideCardsContainer;

    @FXML
    private WebView map;

    @FXML
    public void initialize() {
        WebEngine webEngine = map.getEngine();
        webEngine.loadContent(MapGetter.GET());
        displayRideData();
    }

    private void displayRideData() {
        try (Connection connection = Database.c) {
            String sql = "SELECT * FROM RIDES";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        String passengerName = resultSet.getString("PASSENGER");
                        String destination = resultSet.getString("DESTINATION");

                        createRideCard(passengerName, destination);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRideCard(String passengerName, String destination) {
        Label passengerLabel = new Label("Passenger: " + passengerName);
        Label destinationLabel = new Label("Destination: " + destination);

        Button acceptButton = new Button("Accept Ride");
        acceptButton.setOnAction(event -> handleAcceptRide());

        VBox rideCard = new VBox();
        rideCard.getStyleClass().add("ride-card");
        rideCard.getChildren().addAll(passengerLabel, destinationLabel, acceptButton);
        rideCardsContainer.getChildren().add(rideCard);
    }

    private void handleAcceptRide() {
        System.out.println("Ride accepted!");
    }
}
