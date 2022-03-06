<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!--Material Icons-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Outlined" rel="stylesheet">
    <!--fonts-->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat&family=Prosto+One&display=swap" rel="stylesheet">

    <title>Minicab</title>
  </head>
    <body>
        <nav class="navbar navbar-dark bg-dark">
            <h4 class="text-center text-warning logo">MINIC<span class="material-icons-outlined text-warning mt-2">local_taxi</span>B
            </h4>

            <ul class="nav-links">
                <li> <a href="admin" class="text-warning">Admin</a></li>
                <li> <a href="" class="text-warning">Customer</a></li>
                <li> <a href="driver" class="text-warning">Driver</a></li>
            </ul>
            <ul class="Login">
                <li><a href="register.html">Sign Up</a></li>
                <li><a href="index.html">Login</a></li>
                <li style="color: yellow;"><%= session.getAttribute("username")%></li>
                </ul>
         </nav>
        <div class="container">
            <div class="row">
                <div class="col">
                    <div class="views mb-5">
                        <div class="customer my-4">
                            <h1> Request a Cab Now</h1>
                            <h3>Choose your destination on the map</h3>
                        </div>
                        <button type="button"  class="btn btn-block btn btn-light d-flex justify-start">
                            Pickup Location <span id="pickup"></span>
                        </button>
                        <button type="button" class="btn btn-block btn btn-light d-flex justify-start">
                            Destination <span id="desti"></span>
                        </button>
                    </div>
                    <div>
                        Fixed Charge: MVR <span id="fixed"></span> <br/>
                        Fare Charge: MVR <span id="fare"></span> <br/>
                        GST: <span id="gst">6%</span> <br/>
                        Total: MVR <span id="total"></span>
                    </div>
                    <form method="post" action="customer">
                        <button type="submit" id="btn-book" disabled="true" class="mdc-button mdc-button--raised mt-4">  
                            <span class="mdc-button__ripple"></span> Book A Ride
                        </button>
                        <!--            <input type="submit" value="book ride" id="btn-book" onclick="myFunction()" disabled="true" />-->
                        <input type="hidden" id="distance" name="distance"/>
                        <input type="hidden" id="fixedPrice" name="fixedPrice"/>
                        <input type="hidden" id="farePrice" name="farePrice"/>
                        <input type="hidden" id="totalPrice" name="totalPrice"/>
                        <input type="hidden" id="pp" name="pickup"/>
                        <input type="hidden" id="dd" name="desti"/>

                    </form>
                </div>
                <div class="col mt-4">
                    <div id="googleMap"></div>
                </div>
            </div>
        </div>
            
        
        <script>
            var startAddress, endAddress;
            var currentPosMarker, destinationMarker, distanceInMeters;
            var startPoint = [];
            var endPoint = [];
            var area;
            var price = 25;

            function myMap() {
                var geocoder = new google.maps.Geocoder;
                var directionsService = new google.maps.DirectionsService();
                var directionsRenderer = new google.maps.DirectionsRenderer();
                var mapProp = {
                    center: new google.maps.LatLng(4.1755, 73.5093),
                    zoom: 15
                };
                var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
                //get current location and mark it in map
                getGeoLocation(map);

                //Destination picking when tapped on map - delets old destination and adds new destination and draws route
                google.maps.event.addListener(map, 'click', function (event) {
                    deleteDestinationMarker();
                    placeDestinationMarker(event.latLng, map);
                    calcRoute(startPoint, endPoint, directionsRenderer, directionsService, map);
                    //diatance calculation
                    distanceInMeters = google.maps.geometry.spherical.computeDistanceBetween(startPoint[0].getPosition(), endPoint[0].getPosition());
                    //gets the name of location
                    geocodeLatLng(geocoder);
                    geocodeLatLng(geocoder);

                    google.maps.event.addListener(currentPosMarker, 'dragend', function (event) {
                        //alert('Drag end');
                        for (var i = 0; i < startPoint.length; i++) {
                            startPoint[i].setMap(null);
                        }
                        startPoint = [];
                        currentPosMarker = new google.maps.Marker({
                            position: event.latLng,
                            lable: "your current location",
                            map: map,
                            icon: 'imgs/baseline_gps_fixed_black_18dp.png',
                            draggable: true
                        });
                        startPoint.push(currentPosMarker);
                        distanceInMeters = google.maps.geometry.spherical.computeDistanceBetween(startPoint[0].getPosition(), endPoint[0].getPosition());
                        geocodeLatLng(geocoder);
                        geocodeLatLng(geocoder);
                        calcRoute(startPoint, endPoint, directionsRenderer, directionsService, map);
                    });
                });
            }

            //outside map function 
            function calcRoute(locationstart, locationend, directionsRenderer, directionsService, map) {
                directionsRenderer.setMap(map);
                directionsRenderer.setOptions({suppressMarkers: true});
                var start = new google.maps.LatLng(locationstart[0].getPosition().lat(), locationstart[0].getPosition().lng());
                var end = new google.maps.LatLng(locationend[0].getPosition().lat(), locationend[0].getPosition().lng());
                var request = {
                    origin: start,
                    destination: end,
                    travelMode: 'DRIVING'
                };
                directionsService.route(request, function (result, status) {
                    if (status === 'OK') {
                        directionsRenderer.setDirections(result);
                        //disable btn
                        document.getElementById("btn-book").disabled = false;
                        console.log(startAddress);
                        console.log(endAddress);
                        myFunction();
                    }
                });
            }
            //place destination location
            function placeDestinationMarker(location, map) {
                destinationMarker = new google.maps.Marker({
                    position: location,
                    lable: "Destination",
                    icon: 'imgs/baseline_local_taxi_black_18dp.png',
                    map: map
                });
                endPoint.push(destinationMarker);
            }
            //when to move the destination - use deleteMarker
            function deleteDestinationMarker() {
                for (var i = 0; i < endPoint.length; i++) {
                    endPoint[i].setMap(null);
                }
                endPoint = [];
            }

            // Try HTML5 geolocation.
            function getGeoLocation(map) {
                infoWindow = new google.maps.InfoWindow;
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        var pos = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };

                        currentPosMarker = new google.maps.Marker({
                            position: pos,
                            lable: "your current location",
                            map: map,
                            icon: 'imgs/baseline_gps_fixed_black_18dp.png',
                            draggable: true
                        });

                        startPoint.push(currentPosMarker);
                        infoWindow.setPosition(pos);
                        infoWindow.setContent('Your current location');
                        infoWindow.open(map);
                        map.setCenter(pos);
                    }, function () {
                        handleLocationError(true, infoWindow, map.getCenter(), map);
                    });
                } else {
                    // Browser doesn't support Geolocation
                    handleLocationError(false, infoWindow, map.getCenter(), map);
                }
            }
            //error handling for geolocation
            function handleLocationError(browserHasGeolocation, infoWindow, pos, map) {
                infoWindow.setPosition(pos);
                infoWindow.setContent(browserHasGeolocation ?
                        'Error: The Geolocation service failed.' :
                        'Error: Your browser doesn\'t support geolocation.');
                infoWindow.open(map);
                currentPosMarker = new google.maps.Marker({
                    position: pos,
                    lable: "your current location",
                    icon: 'imgs/baseline_gps_fixed_black_18dp.png',
                    map: map,
                    draggable: true
                });
                startPoint.push(currentPosMarker);
            }
            function myFunction() {
                var fixedPrice = 25;
                var farePrice = (distanceInMeters * 0.0025).toFixed();
                var gst = 1.06;
                var totalPrice = ((parseFloat(fixedPrice) + parseFloat(farePrice))*gst).toFixed();
                var start = ": " + startPoint[0].getPosition().lat().toString() + ", " + startPoint[0].getPosition().lng();
                var end = ": " + endPoint[0].getPosition().lat().toString() +  ", " + endPoint[0].getPosition().lng().toString();
                //window.alert("distance " + distanceInMeters + "\n" + "start " + startPoint[0].getPosition() + "\n" + "end " + endPoint[0].getPosition());
                document.getElementById("distance").value = distanceInMeters;
                document.getElementById("fixedPrice").value = fixedPrice;
                document.getElementById("farePrice").value = farePrice;
                document.getElementById("totalPrice").value = totalPrice;
                document.getElementById("fixed").innerHTML = fixedPrice;
                document.getElementById("fare").innerHTML = farePrice;
                document.getElementById("total").innerHTML = totalPrice;
                document.getElementById("pickup").innerHTML = start;
                document.getElementById("desti").innerHTML = end;
                document.getElementById("pp").value = start;
                document.getElementById("dd").value = end;
                
            }

            function geocodeLatLng(geocoder) {
                geocoder.geocode({'location': startPoint[0].getPosition()}, function (results, status) {
                    if (status === 'OK') {
                        if (results[0]) {
                            startAddress = results[0].formatted_address.toString();
                        } else {
                            window.alert('No results found');
                        }
                    } else {
                        window.alert('Geocoder failed due to: ' + status);
                    }
                });
                geocoder.geocode({'location': endPoint[0].getPosition()}, function (results, status) {
                    if (status === 'OK') {
                        if (results[0]) {
                            endAddress = results[0].formatted_address.toString();
                        } else {
                            window.alert('No results found');
                        }
                    } else {
                        window.alert('Geocoder failed due to: ' + status);
                    }
                });
            }
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDcPWOAHkDaW9qtn3qxiqzbMpm-lAFTdOY&libraries=geometry&callback=myMap"></script>
        <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
        <style>
            #googleMap {
                width: 600px !important;
                height: 400px !important;
            }
            body {
                margin: 0;
                padding: 0;
                background-color: #ffc107;
                height: 100vh;
                font-family: 'Montserrat', sans-serif;
            }

            .Login{
                margin-left: 40px;
            }

            .nav-links{
                flex:1;
            }

            .nav-links ul{
                margin-left: 50px;
                display: inline;
                padding: 8px 25px;
            }

            .customer{
            }

            button{
                color: #212529;
                padding: 10px 25px;
                background: transparent;
                border: 1px solid #212529;
                border-radius: 20px;
                outline: none;
                cursor: pointer;
            }

            nav ul li{
                list-style: none;
                display: inline-block;
                margin-left: 60px;
            }

            nav ul li a{
                text-decoration: none;
                color:#ffc107;
            }

            h4 {
                font-family: 'Prosto One', cursive !important;
            }
        </style>
</body>

</html>      
