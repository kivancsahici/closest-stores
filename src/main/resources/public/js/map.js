/*	
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
	*/
	var JUMBO = (function() {
		var map;
		var markers = [];
		var DEFAULT_LATITUDE = 52.040853;
		var DEFAULT_LONGITUDE = 5.315468;
		var latitude = DEFAULT_LATITUDE;
		var longitude = DEFAULT_LONGITUDE;		
		var detailedSearchOn = false;		
		var storeListTemplate = $("#store-list-template").html();
		var citySelectionListTemplate = $("#city-list-template").html();
		
		var init = function() {
			JUMBO.DATASERVICE.subscribe(successCallbackSearch);
			JUMBO.DATASERVICE.subscribe(renderNearestStores);
		}
		
		var findNearestStores = function() {
			JUMBO.DATASERVICE.searchNearestStores(latitude, longitude);
		}
		
		var detailedSearch = function(city, street) {
			JUMBO.DATASERVICE.detailedSearch(city, street);
		}
		
		var removeMarkers = function(data) {
			for(var i=0; i < markers.length; i++){
		        // remove the marker
		        markers[i].setMap(null);		        
		    }
			markers = [];
		}
		
		var renderNearestStores = function (data) {
			  //create template function
			  var templateFn = _.template(storeListTemplate);
			  //execute template function with JSON object for the interpolated values			  
			  var templateHTML = templateFn(data);
			  $(".storeList").html(templateHTML);
		}
		
		//private function
		var renderCitySelectionList = function (data) {
			  //create template function
			  var templateFn = _.template(citySelectionListTemplate);
			  //execute template function with JSON object for the interpolated values			  
			  var templateHTML = templateFn({"stores": data});			  
			  $(".citySelectionList").html(templateHTML);
		}
		var initMap = function() {
			if(typeof map === "undefined") {//to avoid re-init
			    $("#mapholder").height(458);
			    $("#mapholder").width(420);
			    
			    map = new GMaps({
			    	  el: '#mapholder',
			    	  zoom: 10,
			    	  lat: latitude,
			    	  lng: longitude,
			    	  disableDefaultUI: true
			    });		    		    
			    
			    map.setContextMenu({
		    	  control: 'map',
		    	  options: [{
		    	    title: 'Find stores around here',
		    	    name: 'add_marker',
		    	    action: function(e) {		    	    
		    	    	JUMBO.DATASERVICE.searchNearestStores(e.latLng.lat(), e.latLng.lng());
		    	    }
		    	  }]
		    	});
			}
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
		
		var successCallbackSearch = function(data, paramDetailedSearchOn) {		    
			if(data.stores.length == 0) {
				$('.modal').modal();
				return false;
			}
			removeMarkers();
			
			if(!paramDetailedSearchOn) {
				detailedSearchOn = false;
				markers.push(map.addMarker({
					lat: data.latitude,
					lng: data.longitude,
					infoWindow: {
						content: '<p>You are here</p>'
					},
					draggable: false,
					icon: {
						url: "icon/blue_pin.png"
					}
				}));
		  }
		  else {
		    detailedSearchOn = true;
		  }        
			for (var i = 0; i < data.stores.length; i++) {			    
			    addMarkerWithTimeout(data.stores[i], 300 + (i * 500));
			}
							    			
			window.setTimeout(function() {
				if(markers.length == 1)
					map.setCenter(markers[0].internalPosition.lat(), markers[0].internalPosition.lng(),function() {
						map.setZoom(18);
					});					
				else {
					var bounds = [];
				    for (var i = 0; i < markers.length; i++) {			     			      
				      bounds.push(new google.maps.LatLng(markers[i].internalPosition.lat(), markers[i].internalPosition.lng()));
				    }
				    map.fitLatLngBounds(bounds);
				}
		  }, 300 + data.stores.length * 500);					
		}
		var errorCallback = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}		
		var showPosition = function(position)  {
			latitude = position.coords.latitude;
			longitude = position.coords.longitude;
		    initMap();
		}

		var getLocation = function() {
		    if (navigator.geolocation) {
		        navigator.geolocation.getCurrentPosition(showPosition, showError);
		    } else { 
		        //Geolocation is not supported by this browser
		    	initMap();
		    }
		}

		var showError = function(error) {
		    switch(error.code) {
		        case error.PERMISSION_DENIED:
		            console.log("User denied the request for Geolocation.");
		            break;
		        case error.POSITION_UNAVAILABLE:
		        	console.log("Location information is unavailable.");
		            break;
		        case error.TIMEOUT:
		        	console.log("The request to get user location timed out.");
		            break;
		        case error.UNKNOWN_ERROR:
		        	console.log("An unknown error occurred.");
		            break;
		    }
		    initMap();
		}
		
		var undoHighlightStore = function(index) {
			if(detailedSearchOn == false)
				markers[++index].setIcon("icon/shopping-cart-1.png");
			else
				markers[index].setIcon("icon/shopping-cart-1.png");
		}
		
		var highlightStore = function(index) {
			if(detailedSearchOn == false)
				markers[++index].setIcon("icon/shopping-cart.png");
			else
				markers[index].setIcon("icon/shopping-cart.png");
		}
		
		var loadCityList = function() {			
			$.ajax({
				type : 'GET',				
				url: '/geoapi/v1/cities',
				dataType : 'json',
				success : function(data) {
					renderCitySelectionList(data)
				},
				error : errorCallback
			});
		}
 		
		//reveal public methods
		return {
			showError : showError,
			getLocation : getLocation,			
			renderNearestStores: renderNearestStores,
			initMap : initMap,
			highlightStore: highlightStore,
			undoHighlightStore: undoHighlightStore,			
			loadCityList : loadCityList,			
			init : init,
			findNearestStores : findNearestStores,
			detailedSearch : detailedSearch
		};	
	})();