 FootballFoursApp.controller('roundsController', function($scope,$http){
	   
	   $http.get("/rounds",httpConfig).success( function(response) {
		   $scope.pageHeading= "Fixtures";
		   $scope.rounds = response.rounds;
	   });
	   
   });