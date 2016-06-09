 FootballFoursApp.controller('tablesController', function($scope,$http){
	   
	   $http.get("/tables",httpConfig).success( function(response) {
		   $scope.tables = response.tables;
	   });
	   
   });