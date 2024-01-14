package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
            String sql = "SELECT * FROM RIDES r, USERR u WHERE STATE='REQUESTED' AND r.PASSENGER=u.ID";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        String passengerName = resultSet.getString("NAME");
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

        Button acceptButton = new Button("Send offer");
        acceptButton.setOnAction(event -> handleAcceptRide());

        VBox rideCard = new VBox();
        rideCard.getStyleClass().add("ride-card");
        rideCard.getChildren().addAll(passengerLabel, destinationLabel, acceptButton);
        rideCardsContainer.getChildren().add(rideCard);
    }

    private void handleAcceptRide() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Price");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the price:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(price -> {
            try {
                double parsedPrice = Double.parseDouble(price);
                System.out.println("Entered Price: $" + parsedPrice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric price.");
            }
        });
    }
}
