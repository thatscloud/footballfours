 var FootballFoursApp = angular.module("FootballFoursApp",[
 'ngRoute'
 ]);
 
 var httpConfig = {headers: {'Accept': 'application/json;odata=verbose'}};
 
 
 FootballFoursApp.config(function ($routeProvider) {
     $routeProvider.when('/', {
     })
     .when('/rounds', {
         templateUrl: 'pages/rounds.html',
         controller: 'roundsController',
         pageHeading: 'fixtures'
         
     });
 });
 
 FootballFoursApp.run(['$rootScope', function($rootScope) {
	    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
	        $rootScope.pageHeading = current.$$route.pageHeading;
	    });
 }]);