JUMBO.CONFIG = (function() {
		var showOpen = false; //applicable for geo search only
		var radius = 25;
		var maxResult = 5;
		
		var setShowOpen = function(param) {
			showOpen = param;
		}		
		var setRadius = function(paramRadius) {
			radius = paramRadius;
		}
		var setMaxResult = function(paramMaxResult) {
			maxResult = paramMaxResult;
		}		
		var getShowOpen = function() {
			return showOpen;
		}		
		var getRadius = function() {
			return radius;
		}
		var getMaxResult = function() {
			return maxResult;
		}
		
		//reveal public methods
		return {
			setShowOpen : setShowOpen,
			setRadius : setRadius,
			setMaxResult : setMaxResult,
			getShowOpen : getShowOpen,
			getRadius : getRadius,
			getMaxResult : getMaxResult
		};
	})();