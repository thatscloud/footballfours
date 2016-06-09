 FootballFoursApp.controller('userController', function($scope,$http){
	 var url = "/players";
	   
	   $http.get(url,httpConfig).success( function(response) {
	      $scope.user = response.user;
	   });
	   
	   $scope.login = function()
	   {
		   $http.post("login",httpConfig).success( function(response) {
			      $scope.user = response.user;
			   });
	   }
	   
	   $scope.logout = function()
	   {
		   $http.get("logout",httpConfig).success( function(response) {
			      $scope.user = response.user;
			   });
	   }
   });