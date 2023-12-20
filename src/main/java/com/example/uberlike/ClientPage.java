package com.example.uberlike;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class ClientPage {

    private final Stage mainStage;
    private final Stage seconsdstagel;
    private UserController ussrconrol;
    private Database db;
    @FXML
    private WebView map1;

    public ClientPage(Stage Seconsdstage, Stage mainStage, Database dbb) throws SQLException, MalformedURLException {
        this.mainStage = mainStage;
        this.seconsdstagel=Seconsdstage;
        db = dbb;
        ussrconrol = new UserController(db);
        User ussr = new User("aa", "bb", "dd", "cc");
        ussrconrol.addUser(ussr);

        User su = ussrconrol.Login("bb","dd");
        if (su != null){
            System.out.println(su.toString());
        }else{
            System.out.println("ERORRrr");
        }

    }

    @FXML
    private Button btnClose;

    @FXML
    private void openmapa() throws IOException {
        WebEngine webEngine = map1.getEngine();
        webEngine.loadContent("<!DOCTYPE html><html><body><div id=\"googleMap\" style=\"width:100%;height:400px;\"></div><script>function myMap() {var mapProp= {center:new google.maps.LatLng(31.6291261,-8.0659142),zoom:14,};var map = new google.maps.Map(document.getElementById(\"googleMap\"),mapProp);" +
                "var marker = new google.maps.Marker({position:{lat:51.508742,lng:-0.120850},map:map});}</script><script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCTMx0vIoFllDwiur0k49JglCGCf7ynmAk&callback=myMap\"></script></body></html> ");


//        webEngine.executeScript("function myMap() {" +
//                "var mapProp= {" +
//                "  center:new google.maps.LatLng(51.508742,-0.120850)," +
//                "  zoom:5," +
//                "};" +
//                "var map = new google.maps.Map(document.getElementById(\"googleMap\"),mapProp);" +
//                "}");
    }

    @FXML
    private void initialize() {
        btnClose.setOnAction(event -> closeNewWindow());
    }

    private void closeNewWindow() {
        // You can interact with the main window (close, etc.) using the reference to its Stage
        seconsdstagel.close();
        mainStage.show();
    }
}
