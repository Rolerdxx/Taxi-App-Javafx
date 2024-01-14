package com.example.uberlike;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class ClientPage {

    private final Stage mainStage;
    private final Stage seconsdstagel;
    private UserController ussrconrol;
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
    private VBox offerbox;

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

    @FXML
    private void requestride(){
        if(!Objects.equals(origintxt.getValue(), "") && !Objects.equals(destxt.getValue(), "")){
            WebEngine webEngine = map1.getEngine();
            webEngine.loadContent(MapGetter.Route(origintxt.getValue(),destxt.getValue(),GoogleMapsGeocoding.GET(origintxt.getValue())));
            db.AddRide(userid, origintxt.getValue(), destxt.getValue());
            requestpanel.setVisible(false);
            waitingpane.setVisible(true);
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
//        Label nameLabel = new Label("Product Name:");
//        Label nameValueLabel = new Label("Sample Product");
//        Label priceLabel = new Label("Price:");
//        Label priceValueLabel = new Label("$19.99");
//
//        offerbox.getChildren().addAll(new HBox(nameLabel, priceLabel),
//                new HBox(nameValueLabel, priceValueLabel));
        try (Statement statement = Database.c.createStatement()) {
            statement.execute("LISTEN ch1");

            while (true) {
                // Wait for notifications
                ResultSet rs = statement.executeQuery("SELECT 1");
                rs.close();
                //Database.c.commit();
                System.out.println("Waiting for notifications...");

                // Process notifications
                org.postgresql.PGNotification[] notifications = ((org.postgresql.PGConnection) Database.c).getNotifications();
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
}
