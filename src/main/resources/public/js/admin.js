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
	var map;
	var markers = [];
	var JUMBO = (function() {
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
		var addMarkerWithTimeout = function(data, timeout) {
			window.setTimeout(function() {
				markers.push(new google.maps.Marker({
					position: data.position,
					map: map,
					title:data.title, 
		    		icon:{url:"icon/shopping-cart-1.png"},
					animation: google.maps.Animation.DROP
		      }));
		    }, timeout);
		}
		var successCallbackFuzzySearch = function(data) {
			//reset markers array
			markers = [];
			
			var mapholder = document.getElementById('mapholder')
		    mapholder.style.height = '360px';
		    mapholder.style.width = '750px';    
		    
		    var myOptions = {
		      center: {lat: data.latitude, lng: data.longitude},
		      zoom:10,
		      mapTypeId:google.maps.MapTypeId.ROADMAP,
		      mapTypeControl:false,
		      navigationControlOptions:{style:google.maps.NavigationControlStyle.SMALL}
		    }
		    
		    map = new google.maps.Map(document.getElementById("mapholder"), myOptions);
			
			var i;
			for (i = 0; i < data.nearestStores.length; i++) {
			    var latlon = new google.maps.LatLng(data.nearestStores[i].latitude, data.nearestStores[i].longitude);
			    
			    var param = {};
			    param["position"] = latlon;
			    param["title"] = data.nearestStores[i].addressName;
			    addMarkerWithTimeout(param, 300 + (i * 500));
			    
			}
			
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
			
			window.setTimeout(function() {
				var bounds = new google.maps.LatLngBounds();
			    for (var i = 0; i < markers.length; i++) {
			     bounds.extend(markers[i].getPosition());
			    }

			    map.fitBounds(bounds);
		    }, 3000);
					    
			renderNearestStores(data);
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
			e.preventDefault();	        
	        var latitude = $("#formInputLatitude").val();
	        var longitude = $("#formInputLongitude").val();
	        JUMBO.searchNearestStores(latitude, longitude);
	        //$(".needs-validation input").toggleClass("is-invalid", true);
	        
	        return false;
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
			markers[storeIndex + 1].setIcon("icon/shopping-cart-1.png");
		});
		
		$(".storeList").on('mouseover', '.list-group a', function(e) {
			e.preventDefault();
			var storeIndex = $(".storeList > .list-group > a").index(this);
			markers[storeIndex + 1].setIcon("icon/shopping-cart.png");		
		});
	} );