package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.sql.*;
import java.util.Optional;


public class DriverPage {

    @FXML
    private VBox rideCardsContainer;

    private Database db;

    @FXML
    private WebView map;

    private boolean listeningForRideRequests = true;

    public DriverPage(){
        db = new Database();
    }
    @FXML
    public void initialize() {
        WebEngine webEngine = map.getEngine();
        webEngine.loadContent(MapGetter.GET());
        displayRideData();
        listenForRideRequests();
//        Thread notificationThread = new Thread(this::listenForRideRequests);
//        notificationThread.setDaemon(true);
//        notificationThread.start();
    }

    private void displayRideData() {
        try (Connection connection = db.getC()) {
            String sql = "SELECT * FROM RIDES r, USERR u WHERE STATE='REQUESTED' AND r.PASSENGER=u.ID";
            rideCardsContainer.getChildren().clear();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        String passengerName = resultSet.getString("NAME");
                        String origin = resultSet.getString("ORIGIN");
                        String destination = resultSet.getString("DESTINATION");

                        createRideCard(passengerName, origin, destination);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRideCard(String passengerName, String location, String destination) {
        Label passengerLabel = new Label("Passenger: " + passengerName);
        Label locationLabel = new Label("Origin: " + location);
        Label destinationLabel = new Label("Destination: " + destination);

        Button acceptButton = new Button("Send offer");
        acceptButton.setOnAction(event -> handleAcceptRide());

        VBox rideCard = new VBox();
        rideCard.getStyleClass().add("ride-card");
        rideCard.getChildren().addAll(passengerLabel, locationLabel, destinationLabel, acceptButton);
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

    private void listenForRideRequests() {
        try (Statement statement = db.getC().createStatement()) {
            statement.execute("LISTEN new_ride_request");

            while (true) {
                // Wait for notifications
                ResultSet rs = statement.executeQuery("SELECT 1");
                rs.close();
                //Database.c.commit();
                System.out.println("Waiting for notifications...");

                // Process notifications
                org.postgresql.PGNotification[] notifications = ((org.postgresql.PGConnection) db.getC()).getNotifications();
                if (notifications != null) {
                    for (org.postgresql.PGNotification notification : notifications) {
                        displayRideData();
                        System.out.println("Received notification: " + notification.getName());
                        System.out.println("Notification parameter: " + notification.getParameter());
                    }
                }

                // Sleep for a while before checking again
                Thread.sleep(1000);
            }

        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopListeningForRideRequests() {
        listeningForRideRequests = false;
    }
}
