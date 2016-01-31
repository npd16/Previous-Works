

google.maps.event.addDomListener(window, "load", initMap);

var GeoMarker;
var infowindow, service;
var pittsburgh = {lat: 40.4446, lng: -79.9533};
var place;
var start, end;
var rendererOptions = {
    draggable: true
  };
var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);;
var directionsService = new google.maps.DirectionsService();


function initMap(){
	infowindow = new google.maps.InfoWindow();
	directionsDisplay = new google.maps.DirectionsRenderer();
	
	//create new map
	map = new google.maps.Map(document.getElementById('map'), {
  	  center: pittsburgh,
  	  zoom: 15
 	});
  	directionsDisplay.setMap(map);
  	
  	//get user's location and perform search
	if (navigator.geolocation) {  	
	navigator.geolocation.getCurrentPosition(function(position) {
      var pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      infowindow.setPosition(pos);
      infowindow.setContent('Location found.');
      map.setCenter(pos);

      directionsDisplay.setPanel(document.getElementById("directionsPanel"));
      service = new google.maps.places.PlacesService(map);
   	service.nearbySearch({
		location: pos,
   		radius: 1500,//in meters, 
   		rankby: 'DISTANCE',
   		types: ['bar'],
   		keyword: 'bar OR beer OR pub OR saloon OR tavern'
  	}, callback); 
      
    }, function() {
      handleLocationError(true, infoWindow, map.getCenter());
    });
	}else {
	    // Browser doesn't support Geolocation
	    handleLocationError(false, infoWindow, map.getCenter());
	  }
  
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pittsburgh);
  infoWindow.setContent(browserHasGeolocation ?
                        'Error: The Geolocation service failed.' :
                        'Error: Your browser doesn\'t support geolocation.');
  var service = new google.maps.places.PlacesService(map);
 	service.nearbySearch({
   		location: pittsburgh,
   		radius: 1500,//in meters, 
   		rankby: 'DISTANCE',
   		types: ['bar'],
   		keyword: 'bar OR beer OR pub OR saloon OR tavern'
  	}, callback);
  	
  		

}
		//place marker at current location
		GeoMarker = new GeolocationMarker(map);
        GeoMarker.setCircleOptions({fillColor: '#808080'});
		
        google.maps.event.addListenerOnce(GeoMarker, 'position_changed', function() {
          map.setCenter(this.getPosition());
          //map.fitBounds(this.getBounds());
        });

        google.maps.event.addListener(GeoMarker, 'geolocation_error', function(e) {
          alert('There was an error obtaining your position. Message: ' + e.message);
        });

        //GeoMarker.setMap(map);
        
        var input = document.getElementById('pac-input');
  var searchBox = new google.maps.places.SearchBox(input);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
  searchBox.addListener('places_changed', function() {
    var places = searchBox.getPlaces();
    console.log("places: " + places[0].formatted_address);
    var new_window = window.open('/location?addr='+places[0].formatted_address);
    });
        
        
}//end initMap

function callback(results, status) {
  if (status === google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      createMarker(results[i]);
    }
   
  }
}

//get directions from user's current location to the selected place
function calcRoute(place) {
    var geocoder = new google.maps.Geocoder;
    var start = GeoMarker.getPosition();
    geocoder.geocode({'location': start}, function(results, status) {
    	start = results[0].formatted_address;
    	 var end = place.geometry.location;
    	geocoder.geocode({'location': end}, function(results, status) {
    		end = results[0].formatted_address;
    		var name = place.name;
    		var new_window = window.open('/directions?start='+start+'&end='+end+'&name='+name);
    	});	
    });
}//end calcroute
	

function createMarker(place) {
  var placeLoc = place.geometry.location;
  var address;
  var marker = new google.maps.Marker({
    map: map,
    position: place.geometry.location
  });

	//for each marker, create an info window
  google.maps.event.addListener(marker, 'click', function(marker) {
   	  infowindow.setContent(place.name + "<br><button id='getinfo' class='infobtn'>Specials</button><button id='Directions' class='infobtn'>Directions</button>");
      infowindow.open(map, this);
      
  	 var dir = document.getElementById("Directions");
 	 dir.addEventListener('click', function(){
 	   calcRoute(place);
	}); 
	
	var getinfo = document.getElementById("getinfo");
	getinfo.addEventListener ('click', function(){
		text = place.name;
  		text = text.replace(/amp;/g, '');
  		text = text.replace(/[^a-z0-9\s]/gi, '')
  		text = text.replace(/\s/g, '');
  		text = text.toLowerCase();
  		 $('#barname').val(text);
  		 $("#submit").click();
  	/*	$.get("/barinfo?barinfo="+text, function(){
  		 	location.reload();
  		 });*/
	});
	
	
  });//click marker
  
 
 
  

  var list = document.getElementById("list");
  var myDiv = document.createElement('BUTTON');
  
  myDiv.onclick = function(){
  	google.maps.event.trigger(marker, 'click');
  }
  
  myDiv.innerHTML = place.name;
  list.appendChild(myDiv);
  myDiv.className = 'barNames btn-block';
  

  

}//end create marker

