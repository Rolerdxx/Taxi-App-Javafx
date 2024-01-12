package com.example.uberlike;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class DriverPage {
    @FXML
    private WebView map;
    @FXML
    public void initialize() {
        WebEngine webEngine = map.getEngine();
        webEngine.loadContent(MapGetter.GET());

    }

}
