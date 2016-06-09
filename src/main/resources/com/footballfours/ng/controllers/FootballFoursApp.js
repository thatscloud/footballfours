var FootballFoursApp = angular.module("FootballFoursApp", [ 'ngRoute' ]);

var httpConfig = {
	headers : {
		'Accept' : 'application/json;odata=verbose'
	}
};

FootballFoursApp.config(function($routeProvider) {
	$routeProvider.when('/', {}).when('/rounds', {
		templateUrl : 'pages/rounds.html',
		controller : 'roundsController',
		pageHeading : 'fixtures'

	}).when('/tables', {
		templateUrl : 'pages/tables.html',
		controller : 'tablesController',
		pageHeading : 'tables'

	});
});

FootballFoursApp.run([ '$rootScope', function($rootScope) {
	$rootScope.$on('$routeChangeSuccess', function(event, current, previous) {
		$rootScope.pageHeading = current.$$route.pageHeading;
	});
} ]);

FootballFoursApp.filter('prettyJSON', function() {
	function prettyPrintJson(json) {
		return JSON ? JSON.stringify(json, null, '  ')
				: 'your browser doesnt support JSON so cant pretty print';
	}
	return prettyPrintJson;
});