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
		var airportsTableCreated = false;
		var reportsTableCreated = false;
		var countriesByCode = {};
		var countriesByName = {};
		var airportsTable = {};
		var reportsTableMax = {};
		var reportsTableMin = {};
		
		/* Formatting function for row details - modify as you need */
		var dataTableFormat = function (d) {
			// `d` is the original data object for the row
			var retval;
			for (i = 0; i < d.runways.length; i++) { 		
			    retval += 
			  '<tr>'+
		        '<td>Surface:</td>'+
		        '<td>'+d.runways[i].surface +'</td>'+
		        '<td>Identification:</td>'+ 
		        '<td>'+d.runways[i].le_ident+'</td>'
		      '</tr>';
			}
			return retval;
		};

		var loadDataTable = function(countryName) {
			if(!airportsTableCreated) {
				airportsTable = $('#example').DataTable({
		     	   'ajax': {
		             'url': '/countries/' + countryName,
		             'dataSrc': 'airports'		             
		            },
		            "deferRender": true,
		            "columns": [
		                {
		                    "className":      'details-control',
		                    "orderable":      false,
		                    "data":           null,
		                    "defaultContent": ''
		                },
		                { "data": "id"},
		                { "data": "name"}
		            ],
		            "order": [[1, 'asc']]
		    	});
		      
		     // Add event listener for opening and closing details
		     $('#example tbody').on('click', 'td.details-control', function () {
		         var tr = $(this).closest('tr');
		         var row = airportsTable.row( tr );
		  
		         if ( row.child.isShown() ) {
		             // This row is already open - close it
		             row.child.hide();
		             tr.removeClass('shown');
		         }
		         else {        	 
		         	if(row.data().runways.length > 0)
		         	{
		 	            // Open this row
		 	            row.child( dataTableFormat(row.data()) ).show();
		 	            tr.addClass('shown');
		         	}
		         }
		     } );
		     airportsTableCreated = true;
		     $('.card.airports').visible();
		    }
		    else {
		    	airportsTable.ajax.url('/countries/' + countryName ).load();
		    }
		};

		var displayReports = function() {			
			$.get( "/report" )
			  .done(function( data ) {
				  if(!reportsTableCreated) {
					  reportsTableMax = $('#airportCountsReportMax').DataTable({			     	    
							'data': data.max,
							'searching': false,
				            'paging': false,
				            "info": false,
				            'columns': [
				                { data: 'id' },
				                { data: 'name' },
				                { data: 'runwayTypes' },
				                { data: 'runwayIdentifications' }
				            ],
				            "order": [[0, 'desc']]
				    	});
					  reportsTableMin = $('#airportCountsReportMin').DataTable({			     	    
						'data': data.min,
						'searching': false,
			            'paging': false,
			            "info": false,
			            'columns': [
			                { data: 'id' },
			                { data: 'name' },
			                { data: 'runwayTypes' },
			                { data: 'runwayIdentifications' }
			            ],
			            "order": [[0, 'desc']]				  				  
					  });
					  $('.card.reports').visible();
					  reportsTableCreated = true;
				  } else {
					  reportsTableMax.clear().draw();
					  reportsTableMin.clear().draw();
					  
					  reportsTableMax.rows.add(data.max).draw();
					  reportsTableMin.rows.add(data.min).draw();
					  
				  }
			  })
			  .fail(function() {
			    console.log("ajax failed");
			  });	    
		}
		
		var successCallbackFuzzySearch = function(data) {
			var countries = data;
			
			countriesByCode = _.keyBy(data, 'code');
			countriesByName = _.keyBy(data, 'name');						
			
			var fuseOptions = {
					  shouldSort: true,
					  threshold: 0.3,
					  location: 0,
					  distance: 100,
					  maxPatternLength: 32,
					  minMatchCharLength: 1,
					  keys: [ 
						  { name: "code", weight: 0.1 }, 
						  { name: "name", weight: 0.9 }
					  ]					
					};
			
			var fuzzyCompleteOptions = { display: "name", key: "code", fuseOptions: fuseOptions };
			$("#country").fuzzyComplete(countries, fuzzyCompleteOptions);						
		}
		
		var errorCallbackFuzzySearch = function(jqXHR, textStatus) {
			console.log(jqXHR, textStatus);
		}
		
		var initFuzzySearch = function() {
			$.ajax({
				type : 'GET',
				url : '/countries',
				dataType : 'json',
				success : successCallbackFuzzySearch,
				error : errorCallbackFuzzySearch
			});
		}
		
		var searchAirports = function(event) {
			//Fetch form to apply custom Bootstrap validation
		    var form = $(".needs-validation");
		    		    
		    if (form[0].checkValidity() === false) {	
		      //event.preventDefault();
		      //event.stopPropagation();
		      $("#country").toggleClass("is-invalid", true);		      
		    }else {
		    	var formValue = $("#country").val();
		    	if(_.has(countriesByCode, formValue)) {
		    		$("#country").toggleClass("is-invalid", false);
		    		LUNATECH.loadDataTable(countriesByCode[formValue].name);
		    	}
		    	else if( _.has(countriesByName, formValue)) {
		    		$("#country").toggleClass("is-invalid", false);
		    		LUNATECH.loadDataTable(formValue);
		    	}
		    	else {
		    		//event.preventDefault();
				    //event.stopPropagation();				    
				    $("#country").toggleClass("is-invalid", true);

		    	}
		    }
		    // the following is not needed because we already use is-invalid for this purpose
		    //form.addClass('was-validated')
			
		}
		//reveal public methods
		return {
			loadDataTable : loadDataTable,
			displayReports : displayReports,
			initFuzzySearch : initFuzzySearch,
			searchAirports : searchAirports
		};	
	})();
	
	$(document).ready(function() {
		LUNATECH.initFuzzySearch();
		 $(".displayReports").on("click", function(e){
			 LUNATECH.displayReports();
		 });
		 
		 $(".searchAirports").on("click", function(e) {
			 LUNATECH.searchAirports(e);
		 });
		 
	} );