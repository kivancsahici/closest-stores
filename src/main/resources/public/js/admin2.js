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
		var DEFAULT_LATITUDE = 52.040853;
		var DEFAULT_LONGITUDE = 5.315468;
		var latitude;
		var longitude;
		var radius = 25;
		var maxResult = 5;
		var setRadius = function(paramRadius) {
			radius = paramRadius;
		}
		var setMaxResult = function(paramMaxResult) {
			maxResult = paramMaxResult;
		}
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
			  $(".storeList").html(templateHTML);
		}
		
		//private function
		var renderCitySelectionList = function (data) {
			  var citySelectionListTemplate = $("#city-list-template").html();
			  //create template function
			  var templateFn = _.template(citySelectionListTemplate);
			  //execute template function with JSON object for the interpolated values			  
			  var templateHTML = templateFn({"nearestStores": data});			  
			  $(".citySelectionList").html(templateHTML);
		}
		var initMap = function(latitude, longitude) {			
			latitude = latitude || DEFAULT_LATITUDE;
			longitude = longitude || DEFAULT_LONGITUDE;

			var mapholder = document.getElementById('mapholder')
		    mapholder.style.height = '458px';
		    mapholder.style.width = '420px';    
		    
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
	    	    	JUMBO.searchNearestStores(e.latLng.lat(), e.latLng.lng());
	    	    }
	    	  }]
	    	});
		    
		    //searchNearestStores(latitude, longitude);
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
			if(data.stores.length == 0) {
				alert("sorry");
				return false;
			}
			removeMarkers();
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
			for (var i = 0; i < data.stores.length; i++) {			    
			    addMarkerWithTimeout(data.stores[i], 300 + (i * 500));
			}
							    			
			window.setTimeout(function() {				
				var bounds = [];
			    for (var i = 0; i < markers.length; i++) {			     			      
			      bounds.push(new google.maps.LatLng(markers[i].internalPosition.lat(), markers[i].internalPosition.lng()));
			    }
			    map.fitLatLngBounds(bounds);			    
		    }, 300 + data.stores.length * 500);
			
			
			window.setTimeout(function() {				
				JUMBO.renderNearestStores(data);			    
		    }, 300 + data.stores.length * 500);
	
		}
		
		var errorCallbackFuzzySearch = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}
		
		var searchNearestStores = function(latitude, longitude) {			
			$.ajax({
				type : 'GET',
				url: '/geoapi/v1/stores/by_geocoord.json',
				data: {"latitude": latitude,"longitude": longitude, "radius": radius, "maxResult" : maxResult},
				dataType : 'json',
				success : successCallbackFuzzySearch,
				error : errorCallbackFuzzySearch
			});
		}
		
		var showPosition = function(position)  {
			//TODO replace with below
		    searchNearestStores(52.040853, 5.315468);
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
		
		var loadCityList = function() {			
			$.ajax({
				type : 'GET',				
				url: '/geoapi/v1/cities',
				dataType : 'json',
				success : function(data) {
					renderCitySelectionList(data)
				},
				error : errorCallbackFuzzySearch
			});
		}
 		
		//reveal public methods
		return {
			showError : showError,
			getLocation : getLocation,
			searchNearestStores : searchNearestStores,
			renderNearestStores: renderNearestStores,
			initMap : initMap,
			highlightStore: highlightStore,
			undoHighlightStore: undoHighlightStore,
			setMaxResult: setMaxResult,
			setRadius : setRadius,
			loadCityList : loadCityList
			
		};	
	})();
	
	$(document).ready(function() {
		JUMBO.loadCityList();		
		
		//render slider values
		$("#formControlRadiusValue").html($("#formControlRadius").val() + " km");
		$("#formControlMaxResultsValue").html($("#formControlMaxResults").val());
		
		$("#formControlRadius").on("input", function(e){
			var value = $(this).val();
			$("#formControlRadiusValue").html(value + " km");
			JUMBO.setRadius(value);
		});
		
		$("#formControlMaxResults").on("input", function(e){
			var value = $(this).val();
			$("#formControlMaxResultsValue").html(value);
			JUMBO.setMaxResult(value);
		});
		
		$(".streetSelectionList").on('change', '.custom-select', function(e) {
			var index = $(".streetSelectionList .custom-select").prop('selectedIndex');
			if(index == 0)
				$("#v-pills-profile .detailedSearch").prop('disabled', true);
			else
				$("#v-pills-profile .detailedSearch").prop('disabled', false);
		});
		
		$(".citySelectionList").on('change', '.custom-select', function(e) {
			//alert($(e.target).val());
			var index = $(".citySelectionList .custom-select").prop('selectedIndex');
			if(index == 0) {
				$("#v-pills-profile .detailedSearch").prop('disabled', true);
				var temp = "<option selected>Select street</option>";
				$(".streetSelectionList select").html(temp);
				return false;
			}
			$.ajax({
				type : 'GET',				
				url: '/geoapi/v1/cities/' + $(e.target).val(),
				dataType : 'json',
				success : function(data) {
					if(data.length > 0){
						var temp = "<option selected>Select street</option>";
						for(var i = 0; i < data.length; i++) {
							temp += "<option>" + data[i].street + "</option>";
						}
						$(".streetSelectionList select").html(temp);
					}
				}/*,
				error : errorCallbackFuzzySearch*/
			});
		});
				
		$(".btn.findStores").on("click", function(e){
			e.preventDefault();
			JUMBO.getLocation();
		 });
		
		$(".btn.detailedSearch").on("click", function(e){
			e.preventDefault();
			var city = $(".citySelectionList .custom-select").val();
			var street = $(".streetSelectionList .custom-select").val();
			$.ajax({
				type : 'GET',
				url: '/geoapi/v1/stores/search.json',
				data: {
					"city": city,
					"street": street
				},
				dataType : 'json',
				success : function(data) {
					console.log(data);
				}/*,
				error : errorCallbackFuzzySearch*/
			});
		});
		$(".needs-validation").on("keypress", function(e) {		 
			 if (e.keyCode == 13) {
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
			JUMBO.undoHighlightStore(storeIndex + 1);			
		});
		
		$(".storeList").on('mouseover', '.list-group a', function(e) {
			e.preventDefault();
			var storeIndex = $(".storeList > .list-group > a").index(this);
			JUMBO.highlightStore(storeIndex + 1);
		});
		JUMBO.initMap();
	} );