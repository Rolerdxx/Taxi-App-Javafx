package com.example.uberlike;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientPage {

    private final Stage mainStage;
    private final Stage seconsdstagel;
   // private UserController ussrconrol;
    private Database db;
    private int userid;
    private String username;
    @FXML
    private WebView map1;

    @FXML
    private Button requestridebtn;

    @FXML
    private ComboBox<String> origintxt;
    @FXML
    private ComboBox<String> destxt;

    @FXML
    private Pane requestpanel;

    @FXML
    private Button testbtn;

    @FXML
    private Pane waitingpane;

    @FXML
    private ScrollPane offerscards;

    @FXML
    private VBox offerbox;

    @FXML
    private VBox offerCardsContainer;

    @FXML
    private Pane rideinfo;

    @FXML
    private Pane rideinfo2;

    @FXML
    private Label drivername;

    @FXML
    private Label originin;

    @FXML
    private Label destinationin;

    @FXML
    private Label prix;

    public ClientPage(Stage Seconsdstage, Stage mainStage, Database dbb , int id, String name){
        this.mainStage = mainStage;
        this.seconsdstagel=Seconsdstage;
        db = dbb;
        userid = id;
        username = name;
    }

    public void Initiate(){
        WebEngine webEngine = map1.getEngine();
        webEngine.loadContent(MapGetter.GET());
    }

    private int rideid;

    @FXML
    private void requestride() throws SQLException {
        if (!Objects.equals(origintxt.getValue(), "") && !Objects.equals(destxt.getValue(), "")) {
            WebEngine webEngine = map1.getEngine();
            webEngine.loadContent(MapGetter.Route(origintxt.getValue(),destxt.getValue(),GoogleMapsGeocoding.GET(origintxt.getValue())));
            db.AddRide(userid, origintxt.getValue(), destxt.getValue());
            requestpanel.setVisible(false);
            waitingpane.setVisible(true);
            offerscards.setVisible(true);
            String sql = "SELECT ID FROM RIDES WHERE PASSENGER = ? AND ORIGIN = ? AND DESTINATION = ? AND STATE = ?";
            PreparedStatement pstmt = db.getC().prepareStatement(sql);
            pstmt.setInt(1, userid);
            pstmt.setString(2, origintxt.getValue());
            pstmt.setString(3, destxt.getValue());
            pstmt.setString(4, "REQUESTED");
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                rideid = resultSet.getInt("ID");
                pstmt.close();
            }
            pstmt.close();

            listenForOffers();

        }
    }

    private void displayOfferData() {
        try{
            String sql = "SELECT * FROM OFFERS s,USERR u WHERE RIDE="+rideid+" AND STATE='WAITING' AND s.DRIVER=u.ID";
            offerCardsContainer.getChildren().clear();
            try (PreparedStatement pstmt = db.getC().prepareStatement(sql)) {
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        int offerid = resultSet.getInt("ID");
                        String drivername = resultSet.getString("NAME");
                        int price = resultSet.getInt("PRICE");

                        createRideCard(drivername, price, offerid);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void createRideCard(String drivername, int price, int offid) {
        Label passengerLabel = new Label("Driver: " + drivername);
        Label locationLabel = new Label("Origin: " + origintxt.getValue());
        Label destinationLabel = new Label("Destination: " + destxt.getValue());
        Label pricee = new Label("Price: " + price);

        Button acceptButton = new Button("Accept Offer");
        acceptButton.setUserData(offid);

        acceptButton.setOnAction(new OfferAccept());

        VBox rideCard = new VBox();
        rideCard.getStyleClass().add("ride-card");
        rideCard.getChildren().addAll(passengerLabel, locationLabel, destinationLabel, pricee, acceptButton);
        offerCardsContainer.getChildren().add(rideCard);
    }

    private class OfferAccept implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Object source = event.getSource();

            if (source instanceof Button) {
                Button clickedButton = (Button) source;
                int offerid = (int)clickedButton.getUserData();
                db.AcceptOffer(offerid, rideid);
                offerscards.setVisible(false);
                waitingpane.setVisible(false);

                String sql = "SELECT * FROM OFFERS o,USERR u WHERE o.DRIVER=u.ID AND o.ID="+offerid;
                PreparedStatement pstmt = null;
                try {
                    pstmt = db.getC().prepareStatement(sql);
                    ResultSet resultSet = pstmt.executeQuery();
                    if (resultSet.next()) {
                        String drivernamee = resultSet.getString("NAME");
                        String origin = origintxt.getValue();
                        String destination = destxt.getValue();
                        String price = resultSet.getString("PRICE");
                        drivername.setText("Driver Name: "+drivernamee);
                        originin.setText("Origin: "+origin);
                        destinationin.setText("Destination: "+destination);
                        prix.setText("Price: "+price);
                        rideinfo2.setVisible(true);
//                        rideinfo.setVisible(true);
                        pstmt.close();
                    }
                    pstmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Unknown source");
            }
        }
    }


    @FXML
    private void origintyped(){
        List<String> tips = AutoCompleteAdresse.GET(origintxt.getEditor().getText());
        assert tips != null;
        origintxt.setItems(FXCollections.observableArrayList(tips));
        origintxt.show();
    }

    @FXML
    private void destyped(){
        List<String> tips = AutoCompleteAdresse.GET(destxt.getEditor().getText());
        assert tips != null;
        destxt.setItems(FXCollections.observableArrayList(tips));
        destxt.show();
    }

    @FXML
    private void testlol(){
        try (Statement statement = db.getC().createStatement()) {
            statement.execute("LISTEN ch1");

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

    private void listenForOffers() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(()->{
            try (Statement statement = db.getC().createStatement()) {
                statement.execute("LISTEN new_offer");
                int num = 0;
                while (true) {
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
                                displayOfferData();
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
}
