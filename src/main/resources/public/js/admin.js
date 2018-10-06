	jQuery.fn.visible = function() {
	    return this.css('visibility', 'visible');
	};
	
	jQuery.fn.invisible = function() {
	    return this.css('visibility', 'hidden');
	};
	
	jQuery.fn.visibilityToggle = function() {
	    return this.css('visibility', function(i, visibility) {
	        return (visibility == 'visible') ? 'hidden' : 'visible';
	    });
	};
	
	var LUNATECH = (function() {
		/*
		var airportsTableCreated = false;		
		var countriesByCode = {};		
		*/
		var successCallbackFuzzySearch = function(data) {
			//TODO
			console.log(data);
		}
		
		var errorCallbackFuzzySearch = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}
		
		var searchNearestStores = function() {
			$.ajax({
				type : 'GET',
				url : '/jumbo/stores?latitude=51.778461&longitude=4.615551',
				dataType : 'json',
				success : successCallbackFuzzySearch,
				error : errorCallbackFuzzySearch
			});
		}
		
		var getLocation = function() {
		    if (navigator.geolocation) {
		        navigator.geolocation.getCurrentPosition(showPosition, showError);
		    } else { 
		        x.innerHTML = "Geolocation is not supported by this browser.";
		    }
		}

		var showPosition = function(position) {
		    var lat = "41.046769";//position.coords.latitude;
		    var lon = "29.004151";//position.coords.longitude;    
		    var lat2 = "41.024402"; 
		    var lon2 = "29.124634";
		    var latlon = new google.maps.LatLng(lat, lon);
		    var latlon2 = new google.maps.LatLng(lat2, lon2);
		    var latlon3 = new google.maps.LatLng("41.046939", "28.892764");
		    var mapholder = document.getElementById('mapholder')
		    mapholder.style.height = '250px';
		    mapholder.style.width = '500px';    
		    
		    var myOptions = {
		      center:latlon,
		      //zoom:14,
		      zoom:8,
		      mapTypeId:google.maps.MapTypeId.ROADMAP,
		      mapTypeControl:false,
		      navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
		    }
		    
		    var map = new google.maps.Map(document.getElementById("mapholder"), myOptions);
		    var marker1 = new google.maps.Marker({position:latlon,map:map,title:"You are one"});
		    var marker2 = new google.maps.Marker({position:latlon2,map:map,title:"You are two"});
		    var marker3 = new google.maps.Marker({position:latlon3,map:map,title:"You are three"});
		    var markers = [];//some array
		    markers.push(marker1);
		    markers.push(marker2);
		    markers.push(marker3);
		    
		    var bounds = new google.maps.LatLngBounds();
		    for (var i = 0; i < markers.length; i++) {
		     bounds.extend(markers[i].getPosition());
		    }

		    map.fitBounds(bounds);
		}

		var showError = function(error) {
		    switch(error.code) {
		        case error.PERMISSION_DENIED:
		            x.innerHTML = "User denied the request for Geolocation."
		            break;
		        case error.POSITION_UNAVAILABLE:
		            x.innerHTML = "Location information is unavailable."
		            break;
		        case error.TIMEOUT:
		            x.innerHTML = "The request to get user location timed out."
		            break;
		        case error.UNKNOWN_ERROR:
		            x.innerHTML = "An unknown error occurred."
		            break;
		    }
		}
		
		//reveal public methods
		return {
			showError : showError,
			showPosition: showPosition,
			getLocation : getLocation,
			searchNearestStores : searchNearestStores 
		};	
	})();
	
	$(document).ready(function() {
		var x = document.getElementById("demo");
		
		$("button").on("click", function(e){
			LUNATECH.searchNearestStores();
			//LUNATECH.showPosition();
		 });
		/*
		LUNATECH.initFuzzySearch();
		 $(".displayReports").on("click", function(e){
			 LUNATECH.displayReports();
		 });
		 
		 $(".searchAirports").on("click", function(e) {
			 LUNATECH.searchAirports(e);
		 });
		 */
	} );