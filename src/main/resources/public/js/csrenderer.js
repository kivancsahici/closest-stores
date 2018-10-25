	//jumbo client side renderer class
	JUMBO.CSRENDERER = (function() {				
		var storeListTemplate = $("#store-list-template").html();
		var citySelectionListTemplate = $("#city-list-template").html();
		
		//public function
		var init = function() {
			loadCityList();
			JUMBO.DATASERVICE.subscribe(renderNearestStores);
		}			
		
		//private function
		var renderNearestStores = function (data) {
			  //create template function
			  var templateFn = _.template(storeListTemplate);
			  //execute template function with JSON object for the interpolated values			  
			  var templateHTML = templateFn(data);
			  $(".storeList").html(templateHTML);
		}				
		
		//private function
		var loadCityList = function() {			
			$.ajax({
				type : 'GET',				
				url: '/geoapi/v1/cities',
				dataType : 'json',
				success : function(data) {
					renderCitySelectionList(data)
				},
				error : function(jqXHR, textStatus) {
					console.log(jqXHR, textStatus);
				}
			});
		}
		
		//private function
		var renderCitySelectionList = function (data) {
			  //create template function
			  var templateFn = _.template(citySelectionListTemplate);
			  //execute template function with JSON object for the interpolated values			  
			  var templateHTML = templateFn({"stores": data});			  
			  $(".citySelectionList").html(templateHTML);
		}
 		
		//reveal public methods
		return {
			init : init
		};	
	})();