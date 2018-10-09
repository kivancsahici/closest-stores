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
		var map;
		var markers = [];
		var defaultLatitude = 52.040853;
		var defaultLongitude = 5.315468;
		var removeMarkers = function(data) {
			for(var i=0; i < markers.length; i++){
		        // remove the marker
		        markers[i].setMap(null);		        
		    }
			markers = [];
		}
		var renderNearestStores = function (data) {
			  var storeListTemplate = document.getElementById("store-list-template").innerHTML;

			  //create template function
			  var templateFn = _.template(storeListTemplate);

			  //execute template function with JSON object for the interpolated values			  
			  var templateHTML = templateFn(data);

			  //append rather than replace!
			  //commentsDiv.innerHTML = templateHTML;
			  $(".storeList").html(templateHTML);
		}
		var initMap = function(latitude, longitude) {			
			var latitude = latitude || defaultLatitude;
			var longitude = longitude || defaultLongitude;

			var mapholder = document.getElementById('mapholder')
		    mapholder.style.height = '458px';
		    mapholder.style.width = '750px';    
		    
		    map = new GMaps({
		    	  el: '#mapholder',
		    	  zoom: 10,
		    	  lat: latitude,
		    	  lng: longitude,
		    	  mapTypeId:google.maps.MapTypeId.ROADMAP,
			      mapTypeControl:false,
			      navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
		    });
		    
		    var m = map.addMarker({
		    	  lat: latitude,
		    	  lng: longitude,
		    	  infoWindow: {
		    		content: '<p>You are here</p>'
		    	  },
		    	  draggable: true,
		    	  icon: {
				       url: "icon/blue_pin.png"
				  }
		    	});
		    
		    map.setContextMenu({
		    	  control: 'marker',
		    	  options: [{
		    	    title: 'Find stores around here',
		    	    name: 'add_marker',
		    	    action: function(e) {		    	    
		    	    	JUMBO.searchNearestStores(e.latLng.lat(), e.latLng.lng());
		    	    }
		    	  }]
		    	});
		}
		
		var addMarkerWithTimeout = function(data, timeout) {			
			window.setTimeout(function() {
				markers.push(map.addMarker({
			    	  lat: data.latitude,
			    	  lng: data.longitude,
			    	  title: data.addressName,			    	  
			    	  icon:{url:"icon/shopping-cart-1.png"},
					  animation: google.maps.Animation.DROP
			    	}));
		    }, timeout);
		}
		var successCallbackFuzzySearch = function(data) {
			removeMarkers();			
			for (var i = 0; i < data.nearestStores.length; i++) {			    
			    addMarkerWithTimeout(data.nearestStores[i], 300 + (i * 500));
			}
							    			
			window.setTimeout(function() {				
				var bounds = [];
			    for (var i = 0; i < markers.length; i++) {			     			      
			      bounds.push(new google.maps.LatLng(markers[i].internalPosition.lat(), markers[i].internalPosition.lng()));
			    }
			    map.fitLatLngBounds(bounds);			    
		    }, 3000);
			
			
			window.setTimeout(function() {				
				JUMBO.renderNearestStores(data);			    
		    }, 3100);
	
		}
		
		var errorCallbackFuzzySearch = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}
		
		var searchNearestStores = function(latitude, longitude) {			
			$.ajax({
				type : 'GET',				
				url: '/jumbo/stores',
				data: {"latitude": latitude,"longitude": longitude},
				dataType : 'json',
				success : successCallbackFuzzySearch,
				error : errorCallbackFuzzySearch
			});
		}
		
		var showPosition = function(position)  {
			JUMBO.initMap(position.coords.latitude, position.coords.longitude);
		    //searchNearestStores(position.coords.latitude, position.coords.longitude);
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
		
		var undoHighlightStore = function(index) {
			markers[index].setIcon("icon/shopping-cart-1.png");
		}
		
		var highlightStore = function(index) {
			markers[index].setIcon("icon/shopping-cart.png");
		}
		
		//reveal public methods
		return {
			showError : showError,
			getLocation : getLocation,
			searchNearestStores : searchNearestStores,
			renderNearestStores: renderNearestStores,
			initMap : initMap,
			highlightStore: highlightStore,
			undoHighlightStore: undoHighlightStore
			
		};	
	})();
	
	$(document).ready(function() {
		//JUMBO.initMap();
		$(".btn.findStores").on("click", function(e){
			e.preventDefault();
			JUMBO.getLocation();
			/*
	        var latitude = $("#formInputLatitude").val();
	        var longitude = $("#formInputLongitude").val();
	        JUMBO.searchNearestStores(latitude, longitude);
	        //$(".needs-validation input").toggleClass("is-invalid", true);
	        
	        return false;
	        */
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
		
		$(".storeList").on('mouseleave', '.list-group a', function(e) {
			e.preventDefault();
			var storeIndex = $(".storeList > .list-group > a").index(this);
			JUMBO.undoHighlightStore(storeIndex);
			//JUMBO.markers[storeIndex].setIcon("icon/shopping-cart-1.png");
		});
		
		$(".storeList").on('mouseover', '.list-group a', function(e) {
			e.preventDefault();
			var storeIndex = $(".storeList > .list-group > a").index(this);
			JUMBO.highlightStore(storeIndex);
			//JUMBO.markers[storeIndex].setIcon("icon/shopping-cart.png");		
		});
	} );