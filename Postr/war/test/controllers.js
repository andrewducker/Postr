"use strict";
var postrApp = angular.module('postrApp', []);

postrApp.controller('UserDataCtrl',
		function postCtrl($scope, $http, orderByFilter) {
			$http.get('/userdata').success(function(result) {
				if (result.data != null) {
					$scope.data = result.data;
					$scope.currentInput = $scope.data.inputs[0];
					$scope.currentOutput = $scope.data.outputs[0];
					$scope.currentPossibleOutput = $scope.data.possibleOutputs[0];
					$scope.loggedIn = true;
					$scope.loggedOut = false;
				} else {
					$scope.loggedOut = true;
					$scope.loggedIn = false;
				}
			});
			$scope.showInput = function() {
				alert($scope.currentInput.userName + "@"
						+ $scope.currentInput.siteName);
			};
		});