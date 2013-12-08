"use strict";
var postrApp = angular.module('postrApp', [ 'ui.bootstrap' ]);

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
				http.post('/personaverification', {
					assertion : assertion
				}).success(function(res, status, xhr) {
					onLoggedIn(res);
				}).error(function(xhr, status, err) {
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
		function postCtrl($scope, $http, orderByFilter, persona, $modal) {
			$http.get('userdata').success(function(result) {
				if (result.data != null) {
					populateData($scope, orderByFilter, result);
					persona.initialise($http, result.data.persona);
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
				$modal.open({
					templateUrl : 'outputs/LJDetails.html',
					controller : ModalInstanceCtrl,
					resolve : {
						output : function() {
							return angular.copy($scope.currentOutput);
						}
					}
				}).result.then(function(result) {
					result.method = "UpdateData";
					$http.post('/dreamwidth', result).success(
							function(res, status, xhr) {
								angular.copy(result, $scope.currentOutput);
								alert("Successfully updated!");
							}).error(function(xhr, status, err) {
						alert("Login failure: " + err);
					});

				});
			};
			$scope.login = function() {
				persona.login();
			};
			$scope.logout = function() {
				persona.logout();
			};

			$scope.open = function() {

			};
		});

var ModalInstanceCtrl = function($scope, $modalInstance, output) {
	$scope.output = output;
	$scope.timeZones = timeZones;

	$scope.ok = function() {
		$modalInstance.close($scope.output);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};

postrApp.factory('persona', function() {
	return persona;
});