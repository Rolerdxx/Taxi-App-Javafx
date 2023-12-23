package com.example.uberlike;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.sql.SQLException;
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

    public ClientPage(Stage Seconsdstage, Stage mainStage, Database dbb) throws SQLException, MalformedURLException {
        this.mainStage = mainStage;
        this.seconsdstagel=Seconsdstage;
    }

    public void Initiate(){
        WebEngine webEngine = map1.getEngine();
        System.out.print(MapGetter.GET());
        webEngine.loadContent(MapGetter.GET());
    }

    @FXML
    private void requestride(){
        if(!Objects.equals(origintxt.getValue(), "") && !Objects.equals(destxt.getValue(), "")){
            WebEngine webEngine = map1.getEngine();
            webEngine.loadContent(MapGetter.Route(origintxt.getValue(),destxt.getValue(),GoogleMapsGeocoding.GET(origintxt.getValue())));
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
}
