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
	
	var JUMBO = (function() {
		var successCallbackFuzzySearch = function(data) {
			var mapholder = document.getElementById('mapholder')
		    mapholder.style.height = '500px';
		    mapholder.style.width = '750px';    
		    
		    var myOptions = {
		      center:latlon,
		      zoom:8,
		      mapTypeId:google.maps.MapTypeId.ROADMAP,
		      mapTypeControl:false,
		      navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
		    }
		    
		    var map = new google.maps.Map(document.getElementById("mapholder"), myOptions);
			
			var i;
			var markers = [];
			for (i = 0; i < data.nearestStores.length; i++) {
			    var latlon = new google.maps.LatLng(data.nearestStores[i].latitude, data.nearestStores[i].longitude);
			    var marker = new google.maps.Marker({position:latlon,map:map,title:data.nearestStores[i].addressName});
			    markers.push(marker);
			}
			
			//var latlon2 = new google.maps.LatLng(52.075854, 4.234678);
			var latlon2 = new google.maps.LatLng(data.latitude, data.longitude);
			var marker2 = new google.maps.Marker(
			  {  position:latlon2,
			     map:map,
			     title:"You are here!",
			     icon: {
			       url: "icon/blue_pin.png"
			     }
			  }
			);
			markers.push(marker2);
			
			
			var bounds = new google.maps.LatLngBounds();
		    for (var i = 0; i < markers.length; i++) {
		     bounds.extend(markers[i].getPosition());
		    }

		    map.fitBounds(bounds);
		}
		
		var errorCallbackFuzzySearch = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}
		
		var searchNearestStores = function(latitude, longitude) {
			$.ajax({
				type : 'GET',
				//url: '/jumbo/stores?latitude=52.075854&longitude=4.234678',
				url: '/jumbo/stores',
				data: {"latitude": latitude,"longitude": longitude},
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
			getLocation : getLocation,
			searchNearestStores : searchNearestStores 
		};	
	})();
	
	$(document).ready(function() {				
		$("button").on("click", function(e){
			JUMBO.searchNearestStores();
		 });
		
		$(".needs-validation").on("keypress", function(e) {		 
			 if (event.keyCode == 13) {
				e.preventDefault();	        
		        var latitude = $("#formInputLatitude").val();
		        var longitude = $("#formInputLongitude").val();
		        JUMBO.searchNearestStores(latitude, longitude);
		        //$(".needs-validation input").toggleClass("is-invalid", true);
		        return false;
		     }
		 });	
	} );