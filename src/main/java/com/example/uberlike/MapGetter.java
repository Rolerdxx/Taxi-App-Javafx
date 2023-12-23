package com.example.uberlike;

public class MapGetter {


    public static String GET(){
        return "<!DOCTYPE html><html><body><div id=\"googleMap\" style=\"width:100%;height:400px;\"></div><script>function myMap() {" +
                "var directionsService = new google.maps.DirectionsService();var directionsRenderer = new google.maps.DirectionsRenderer();" +
                "var mapProp = {center: new google.maps.LatLng(51.508742, -0.120850),zoom: 5,};" +
                "var map = new google.maps.Map(document.getElementById(\"googleMap\"), mapProp);" +
                "directionsRenderer.setMap(map);" +
                "var request = {origin: 'New York, NY',destination: 'Los Angeles, CA',travelMode: 'DRIVING'};" +
                "directionsService.route(request, function (response, status) {if (status == 'OK') {directionsRenderer.setDirections(response);}});}" +
                "</script><script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCTMx0vIoFllDwiur0k49JglCGCf7ynmAk&callback=myMap\"></script></body></html>";
    }

    public static String Route(String origin, String destination, String cords){
        return "<!DOCTYPE html><html><body><div id=\"googleMap\" style=\"width:100%;height:400px;\"></div><script>function myMap() {" +
                "var directionsService = new google.maps.DirectionsService();var directionsRenderer = new google.maps.DirectionsRenderer();" +
                "var mapProp = {center: new google.maps.LatLng("+cords+"),zoom: 10,};" +
                "var map = new google.maps.Map(document.getElementById(\"googleMap\"), mapProp);" +
                "directionsRenderer.setMap(map);" +
                "var request = {origin: '"+origin+"',destination: '"+destination+"',travelMode: 'DRIVING'};" +
                "directionsService.route(request, function (response, status) {if (status == 'OK') {directionsRenderer.setDirections(response);}});}" +
                "</script><script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCTMx0vIoFllDwiur0k49JglCGCf7ynmAk&callback=myMap\"></script></body></html>";
    }
}
