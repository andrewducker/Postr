"use strict";
var postrApp = angular.module('postrApp', []);

var populateData = function($scope, orderByFilter, result) {
	$scope.data = result.data;
	$scope.data.inputs = orderByFilter($scope.data.inputs, 'userName');
	$scope.data.outputs = orderByFilter($scope.data.outputs, 'userName');
	$scope.currentInput = $scope.data.inputs[0];
	$scope.currentOutput = $scope.data.outputs[0];
	$scope.loggedIn = true; 
	$scope.loggedOut = false;
};

var persona = {
	initialise : function(http, currentUser, onLoggedIn) {
		navigator.id.watch({
			loggedInUser : currentUser,
			onlogin : function(assertion) {
				http.post('/personaverification', {assertion:assertion}).
				success(function(res, status, xhr) {onLoggedIn(res);}).
				error(function(xhr, status, err) {
					navigator.id.logout();
					alert("Login failure: " + err);
				});
			},
			onlogout : function() {
				http.post('/personalogout').success(function(res, status, xhr) {
					location.reload();
				}).error(function(xhr, status, err) {
					alert("Failed to log you out: " + err);
					location.reload();
				});
			}
		});
	},
	login : function() {
		navigator.id.request();
	},
	logout : function() {
		navigator.id.logout();
	}
};

postrApp.controller('UserDataCtrl',
		function postCtrl($scope, $http, orderByFilter, persona) {
			$http.get('userdata').success(function(data) {
				if (data.inputs != null) {
					populateData($scope, orderByFilter, data);
					persona.initialise($http, data.persona);
					$scope.loggedOut = false;
					$scope.loggedIn = true;
				} else {
					persona.initialise($http, null, function(newData) {
						populateData($scope, orderByFilter, newData);
					});
					$scope.loggedOut = true;
					$scope.loggedIn = false;
				}
			});
			$scope.showInput = function() {
				alert($scope.currentInput.userName + "@"
						+ $scope.currentInput.siteName);
			};
			$scope.showOutput = function() {
				alert($scope.currentOutput.userName + "@"
						+ $scope.currentOutput.siteName);
			};
			$scope.login = function() {
				persona.login();
			};
			$scope.logout = function() {
				persona.logout();
			};
		});

postrApp.factory('persona', function() {
	return persona;
});