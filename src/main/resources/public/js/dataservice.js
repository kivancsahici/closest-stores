JUMBO.DATASERVICE = (function() {
		var subscribers = [];

		var subscribe = function(callbackFunction) {
		  subscribers.push(callbackFunction);
		}

		var publish = function(data) {
		  subscribers.forEach((changeListener) => { changeListener(data); });
		}
		
		var errorCallback = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}
		
		var detailedSearch = function(city, street) {
			$.ajax({
				type : 'GET',
				url: '/geoapi/v1/stores/search.json',
				data: {
					"city": city,
					"street": street
				},
				dataType : 'json',
				success: successCallbackDetailedSearch,
				error : errorCallback
			});
		}		
		
		var searchNearestStores = function(paramLatitude, paramLongitude) {			
			var lat = paramLatitude || latitude;
			var lon = paramLongitude || longitude;
			$.ajax({
				type : 'GET',
				url: '/geoapi/v1/stores/by_geocoord.json',
				data: {"latitude": lat,"longitude": lon, "radius": JUMBO.CONFIG.getRadius(), "maxResult" : JUMBO.CONFIG.getMaxResult(), "showOpen" : JUMBO.CONFIG.getShowOpen()},
				dataType : 'json',
				success : successCallbackGeoSearch,
				error : errorCallback
			});
		}
		
		var successCallbackGeoSearch = function(data) {			
			subscribers.forEach((changeListener) => { changeListener(data, false); });
		}
		
		var successCallbackDetailedSearch = function(data) {			
			subscribers.forEach((changeListener) => { changeListener(data, true); });
		}
		
		var addPlace = function(data) {
			setTimeout(function() {
				publish(data);
			}, 5000);
			
		}
		//reveal public methods
		return {
			subscribe : subscribe,
			publish : publish,
			addPlace : addPlace,
			searchNearestStores : searchNearestStores,
			detailedSearch : detailedSearch
		};

	})();