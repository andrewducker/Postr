"use strict";
var testApp = angular.module('testApp', ['ngRoute' ]);

testApp.controller('MainController',function ($scope, $location, myData) {
	$scope.data = myData;
	$scope.data.message = "Hello!";
	$scope.data.location = $location;
});

testApp.controller('FriendController',function ($scope, $routeParams, myData, $filter, $location) {
	
	var search = {id: $routeParams.friendID};
	var found = $filter('filter')(myData.friends, search, true);
	$scope.data = {friend: angular.copy(found[0])};
	$scope.Save = function(){
		angular.copy($scope.data.friend, found[0]);
		$location.path("");
	};
	$scope.Cancel = function(){
		$location.path("");
	};
});

testApp.factory('myData', function(){
	return {
		friends : [
		           {id:"1",name: "Andy"}, 
		           {id:"2", name: "Bob"},
		           {id:"3", name: "Charlie"}],
		wife: "Julie"
	};
});


testApp.config(function($routeProvider){
	$routeProvider.when('/',{
		templateUrl:"MainPage.htm",
			controller: "MainController"
	})
	.when ("/subpage/",{
		templateUrl:"ILove.htm",
		controller: "MainController"
	})
	.when ("/friend/:friendID",{
		templateUrl: "FriendDetails.htm",
		controller: "FriendController"
	})
	.otherwise({
		template:"<H1>I am lost at {{data.location.path()}}</H1>",
		controller: "MainController"
			});
});