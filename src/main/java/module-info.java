
module com.example.uberlike {
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.web;
       // requires google.maps.services;

        requires org.controlsfx.controls;
        requires com.dlsc.formsfx;
        requires org.kordamp.ikonli.javafx;
        requires eu.hansolo.tilesfx;
        requires java.sql;
        requires org.postgresql.jdbc;


        opens com.example.uberlike to javafx.fxml;
        exports com.example.uberlike;
        }