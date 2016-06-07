 var FootballFoursApp = angular.module("FootballFoursApp",[
 'ngRoute'
 ]);

 FootballFoursApp.config(function ($routeProvider) {
     $routeProvider.when('/', {
     });
//     .when('/players', {
//         templateUrl: 'pages/players.html',
//         controller: 'playersController'
//     })
//     .when('/competitions', {
//         templateUrl: 'pages/competitions.html',
//         controller: 'competitionsController'
//     })
//     .when('/teams', {
//         templateUrl: 'pages/teams.html',
//         controller: 'teamsController'
//     });
 });
 
 var httpConfig = {headers: {'Accept': 'application/json;odata=verbose'}};