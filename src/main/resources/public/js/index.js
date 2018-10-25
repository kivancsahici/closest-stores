	$(document).ready(function() {		
		JUMBO.init();
		
		JUMBO.loadCityList();		
		
		//render slider values
		$("#formControlRadiusValue").html($("#formControlRadius").val() + " km");
		$("#formControlMaxResultsValue").html($("#formControlMaxResults").val());
		
		$("#formControlRadius").on("input", function(e){
			var value = $(this).val();
			$("#formControlRadiusValue").html(value + " km");
			JUMBO.CONFIG.setRadius(value);
		});
		
		$("#formControlMaxResults").on("input", function(e){
			var value = $(this).val();
			$("#formControlMaxResultsValue").html(value);
			JUMBO.CONFIG.setMaxResult(value);
		});
		
		$(".streetSelectionList").on('change', '.custom-select', function(e) {
			var index = $(".streetSelectionList .custom-select").prop('selectedIndex');
			if(index == 0)
				$("#v-pills-profile .detailedSearch").prop('disabled', true);
			else
				$("#v-pills-profile .detailedSearch").prop('disabled', false);
		});
		
		$(".citySelectionList").on('change', '.custom-select', function(e) {
			//disable search button and wait for street selection to enable it back
			$("#v-pills-profile .detailedSearch").prop('disabled', true);
			
			var index = $(".citySelectionList .custom-select").prop('selectedIndex');
			if(index == 0) {
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
				},
				error : JUMBO.errorCallback
			});
		});
		
		$('#showOpenStores').change(function() {		       
		    if (this.checked) {
		        JUMBO.CONFIG.setShowOpen(true); 
		    } else {
		    	JUMBO.CONFIG.setShowOpen(false);
		    }
		});
		
		$(".btn.findStores").on("click", function(e) {			
			e.preventDefault();
			//JUMBO.DATASERVICE.searchNearestStores();
			JUMBO.findNearestStores();
		 });
		
		$(".btn.detailedSearch").on("click", function(e){
			e.preventDefault();
			var city = $(".citySelectionList .custom-select").val();
			var street = $(".streetSelectionList .custom-select").val();
			JUMBO.DATASERVICE.detailedSearch(city, street);			
		});
		$(".needs-validation").on("keypress", function(e) {		 
			 if (e.keyCode == 13) {
				e.preventDefault();	        
		        var latitude = $("#formInputLatitude").val();
		        var longitude = $("#formInputLongitude").val();
		        JUMBO.DATASERVICE.searchNearestStores(latitude, longitude);
		        return false;
		     }
		 });
		
		$(".storeList").on('mouseleave', '.list-group a', function(e) {
			e.preventDefault();
			var storeIndex = $(".storeList > .list-group > a").index(this);
			JUMBO.undoHighlightStore(storeIndex);			
		});
		
		$(".storeList").on('mouseover', '.list-group a', function(e) {
			e.preventDefault();
			var storeIndex = $(".storeList > .list-group > a").index(this);
			JUMBO.highlightStore(storeIndex);
		});
		JUMBO.getLocation();
	} );