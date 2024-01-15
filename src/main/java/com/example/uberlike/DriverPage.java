package com.example.uberlike;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DriverPage {

    @FXML
    private VBox rideCardsContainer;

    private Database db;

    private int userid;

    @FXML
    private WebView map;

    @FXML
    private ScrollPane offerscard;

    @FXML
    private Pane rideinfo;

    @FXML
    private Label originlab;

    @FXML
    private Label Destibnationlab;

    private boolean listeningForRideRequests = true;

    public DriverPage(int us){
        db = new Database();
        userid = us;
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
        try{
            String sql = "SELECT * FROM RIDES r, USERR u WHERE STATE='REQUESTED' AND r.PASSENGER=u.ID";
            rideCardsContainer.getChildren().clear();
            try (PreparedStatement pstmt = db.getC().prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        int rideid = resultSet.getInt("ID");
                        String passengerName = resultSet.getString("NAME");
                        String origin = resultSet.getString("ORIGIN");
                        String destination = resultSet.getString("DESTINATION");

                        createRideCard(passengerName, origin, destination, rideid);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createRideCard(String passengerName, String location, String destination, int rideid) {
        Label passengerLabel = new Label("Passenger: " + passengerName);
        Label locationLabel = new Label("Origin: " + location);
        Label destinationLabel = new Label("Destination: " + destination);

        Button acceptButton = new Button("Send offer");
        acceptButton.setUserData(rideid);

        acceptButton.setOnAction(new OfferRide());

        VBox rideCard = new VBox();
        rideCard.getStyleClass().add("ride-card");
        rideCard.getChildren().addAll(passengerLabel, locationLabel, destinationLabel, acceptButton);
        rideCardsContainer.getChildren().add(rideCard);
    }

    private void handleAcceptRide(Button btn) {

    }


    private class OfferRide implements EventHandler<ActionEvent> {
        int offerid;
        @Override
        public void handle(ActionEvent event) {
            // Get the sender (source) button using getSource()
            Object source = event.getSource();


            if (source instanceof Button) {
                Button clickedButton = (Button) source;
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enter Price");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter the price:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(price -> {
                    try {
                        int parsedPrice = Integer.parseInt(price);
                        int rideid = (int)clickedButton.getUserData();
                        db.AddOffer(rideid, userid, parsedPrice);

                        String sql = "SELECT * FROM OFFERS o WHERE o.DRIVER="+userid+" AND o.RIDE="+rideid+" AND PRICE="+price;
                        PreparedStatement pstmt = null;
                        try {
                            pstmt = db.getC().prepareStatement(sql);
                            ResultSet resultSet = pstmt.executeQuery();
                            if (resultSet.next()) {
                                offerid = resultSet.getInt("ID");
                                pstmt.close();
                            }
                            pstmt.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }


                        System.out.println("Entered Price: $" + parsedPrice);
                        ListenForOfferRes(offerid);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid numeric price.");
                    }
                });
            } else {
                System.out.println("Unknown source");
            }
        }
    }

    private void listenForRideRequests() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(()->{
            try (Statement statement = db.getC().createStatement()) {
                statement.execute("LISTEN new_ride_request");
                int num = 0;
                while (listeningForRideRequests) {
                    // Wait for notifications
                    num++;
                    ResultSet rs = statement.executeQuery("SELECT 1");
                    rs.close();
                    //Database.c.commit();
                    System.out.println("Waiting for notifications..."+num);

                    // Process notifications
                    org.postgresql.PGNotification[] notifications = ((org.postgresql.PGConnection) db.getC()).getNotifications();
                    if (notifications != null) {
                        for (org.postgresql.PGNotification notification : notifications) {
                            Platform.runLater(() -> {
                                displayRideData();
                            });
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
        });
        executorService.shutdown();
    }

    private void ListenForOfferRes(int ofid){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(()->{
            try (Statement statement = db.getC().createStatement()) {
                statement.execute("LISTEN offer_res");
                int num = 0;
                while (true) {
                    num++;
                    ResultSet rs = statement.executeQuery("SELECT 1");
                    rs.close();
                    //Database.c.commit();
                    System.out.println("Waiting for notifiddddcations..."+num);

                    org.postgresql.PGNotification[] notifications = ((org.postgresql.PGConnection) db.getC()).getNotifications();
                    if (notifications != null) {
                        for (org.postgresql.PGNotification notification : notifications) {

                            System.out.println("Received notification: " + notification.getName());
                            System.out.println("Notification parameter: " + notification.getParameter());
                            int id = Integer.parseInt(notification.getParameter());
                            //if(id == ofid){
                                Platform.runLater(() -> {
                                    stopListeningForRideRequests();
                                    offerscard.setVisible(false);
                                    rideinfo.setVisible(true);
                                    showRideInfo(ofid);
                                });
                                break;
                            //}
                        }
                    }
                    Thread.sleep(1000);
                }

            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

    public void stopListeningForRideRequests() {
        listeningForRideRequests = false;
    }

    public void showRideInfo(int oid){
        String sql = "SELECT * FROM OFFERS o, RIDE r WHERE o.RIDE=o.ID AND o.ID="+oid;
        PreparedStatement pstmt = null;
        try {
            pstmt = db.getC().prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String origin = resultSet.getString("ORIGIN");
                String destination = resultSet.getString("DESTINATION");
                originlab.setText("Origin: "+origin);
                Destibnationlab.setText(destination);
                WebEngine webEngine = map.getEngine();
                webEngine.loadContent(MapGetter.Route(origin,destination,GoogleMapsGeocoding.GET(origin)));
                pstmt.close();
            }
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
