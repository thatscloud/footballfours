 FootballFoursApp.controller('roundsController', function($scope,$http){
	   
	   $http.get("/rounds",httpConfig).success( function(response) {
	      $scope.rounds = response.rounds;
	   });
	   
   });