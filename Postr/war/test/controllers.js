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

testApp.controller('NewFriendController',function ($scope, $routeParams, myData, $filter, $location) {
	$scope.data = {friend: {id: myData.friends.length+1}};
	$scope.Save = function(){
		myData.friends.push($scope.data.friend);
		$location.path("");
	};
	$scope.Cancel = function(){
		$location.path("");
	};
});


testApp.factory('myData', function(){
	return {
		friends : [
		           {id:"1",name: "Andy", petType:"Dog", dogName: "Woof" }, 
		           {id:"2", name: "Bob", petType:"Cat", catName: "Miaow"},
		           {id:"3", name: "Charlie", petType:"Dog", dogName: "Bark"}],
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
	.when("/friend/new",{
		templateUrl:"FriendDetails.htm",
		controller: "NewFriendController"
	})
	.when ("/friend/:petType/:friendID",{
		templateUrl:  function(params){return params.petType + "FriendDetails.htm";}  ,
		controller: "FriendController"
	})
	.otherwise({
		template:"<H1>I am lost at {{data.location.path()}}</H1>",
		controller: "MainController"
			});
});