package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.*;

public class DriverPage {

    @FXML
    private VBox rideCardsContainer;

    @FXML
    private WebView map;

    private boolean listeningForRideRequests = true;

    @FXML
    public void initialize() {
        WebEngine webEngine = map.getEngine();
        webEngine.loadContent(MapGetter.GET());
        displayRideData();
        Thread notificationThread = new Thread(this::listenForRideRequests);
        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    private void displayRideData() {
        try (Connection connection = Database.c) {
            String sql = "SELECT * FROM RIDES";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        String passengerName = resultSet.getString("PASSENGER");
                        String location = resultSet.getString("LOCATION");
                        String destination = resultSet.getString("DESTINATION");

                        createRideCard(passengerName, location, destination);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRideCard(String passengerName, String location, String destination) {
        Label passengerLabel = new Label("Passenger: " + passengerName);
        Label locationLabel = new Label("Location: " + location);
        Label destinationLabel = new Label("Destination: " + destination);

        Button acceptButton = new Button("Accept Ride");
        acceptButton.setOnAction(event -> handleAcceptRide());

        VBox rideCard = new VBox();
        rideCard.getStyleClass().add("ride-card");
        rideCard.getChildren().addAll(passengerLabel, locationLabel, destinationLabel, acceptButton);
        rideCardsContainer.getChildren().add(rideCard);
    }

    private void handleAcceptRide() {
        System.out.println("Ride accepted!");
    }

    private void listenForRideRequests() {
        try (Connection connection = Database.c) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("LISTEN new_ride_request");
            }

            connection.setAutoCommit(false);

            while (listeningForRideRequests) {
                try {
                    PGNotification[] notifications = ((PGConnection) connection).getNotifications();

                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("NOTIFICATION");
                            alert.setHeaderText(null);
                            alert.setContentText("NEW RIDE");
                            alert.showAndWait();
                            displayRideData();
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stopListeningForRideRequests() {
        listeningForRideRequests = false;
    }
}
