package com.example.uberlike;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class ClientPage {

    private final Stage mainStage;
    private final Stage seconsdstagel;
    private UserController ussrconrol;
    private Database db;
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

    public ClientPage(Stage Seconsdstage, Stage mainStage, Database dbb){
        this.mainStage = mainStage;
        this.seconsdstagel=Seconsdstage;
    }

    public void Initiate(){
        WebEngine webEngine = map1.getEngine();
        webEngine.loadContent(MapGetter.GET());
        db = new Database();
    }

    @FXML
    private void requestride(){
        if (!Objects.equals(origintxt.getValue(), "") && !Objects.equals(destxt.getValue(), "")) {
            WebEngine webEngine = map1.getEngine();
            webEngine.loadContent(MapGetter.Route(origintxt.getValue(), destxt.getValue(), GoogleMapsGeocoding.GET(origintxt.getValue())));
            String passengerName = "PassengerName";
            String location = origintxt.getValue();
            String destination = destxt.getValue();

            db.AddRides(passengerName, location, destination);
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
        Label nameLabel = new Label("Product Name:");
        Label nameValueLabel = new Label("Sample Product");
        Label priceLabel = new Label("Price:");
        Label priceValueLabel = new Label("$19.99");

        offerbox.getChildren().addAll(new HBox(nameLabel, priceLabel),
                new HBox(nameValueLabel, priceValueLabel));
    }
}
