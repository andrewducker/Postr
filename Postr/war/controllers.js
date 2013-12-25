"use strict";
var postrApp = angular.module('postrApp', [ 'ui.bootstrap' ]);

var populateData = function(scope, orderByFilter, result) {
	scope.data = result.data;
	scope.data.inputs = orderByFilter(scope.data.inputs, 'userName');
	scope.data.outputs = orderByFilter(scope.data.outputs, 'userName');
	scope.currentInput = scope.data.inputs[0];
	scope.currentOutput = scope.data.outputs[0];
	scope.currentPossibleOutput = scope.data.possibleOutputs[0];
	scope.loggedIn = true;
	scope.loggedOut = false;
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
	
	var verifyPassword = function(toVerify){
		toVerify.method = "VerifyPassword";
		$http.post('/' + toVerify.siteName.toLowerCase(), toVerify)
		.success(function(data) {
			alert("Successfully verified!" + data.message);
		}).error(function(data) {
			alert("Failed to verify: " + data);
		});
	};

	
	$scope.addOutput = function() {
		$modal.open({
			templateUrl : 'outputs/' + $scope.currentPossibleOutput
			+ '/details.html',
			controller : DetailPopupCtrl,
			resolve : {
				output : function() {
					var newOutput = new Object();
					newOutput.siteName = $scope.currentPossibleOutput
					.toLowerCase();
					return newOutput;
				},
				action : function() {
					return "Add";
				},
				verify : function(){
					return verifyPassword;
				}
			}
		}).result.then(function(result) {
			result.method = "SaveData";
			$http.post('/' + result.siteName.toLowerCase(), result)
			.success(function(res, status, xhr) {
				$scope.data.outputs.push(result);
				$scope.currentOutput = result;
				alert("Successfully added!");
			}).error(function(xhr, status, err) {
				alert("Failed to save data: " + err);
			});
		});
	};
		
	$scope.updateOutput = function() {
		$modal.open({
			templateUrl : 'outputs/' + $scope.currentOutput.siteName
			+ '/details.html',
			controller : DetailPopupCtrl,
			resolve : {
				output : function() {
					return angular.copy($scope.currentOutput);
				},
				action : function() {
					return "Update";
				},
				verify : function(){
					return verifyPassword;
				}
			}
		}).result.then(function(result) {
			result.method = "UpdateData";
			$http.post('/' + result.siteName.toLowerCase(), result)
			.success(function() {
				angular.copy(result, $scope.currentOutput);
				alert("Successfully updated!");
			}).error(function(data) {
				alert("Failed to update data: " + data);
			});

		});
	};
	$scope.login = function() {
		persona.login();
	};
	$scope.logout = function() {
		persona.logout();
	};
});

var DetailPopupCtrl = function($scope, $modalInstance, output, action, verify, $http) {
	$scope.output = output;
	$scope.timeZones = timeZones;
	$scope.action = action;

	$scope.verify = function(){
		$scope.verificationSuccess = false;
		$scope.output.method = "VerifyPassword";
		$http.post('/' + $scope.output.siteName.toLowerCase(), $scope.output)
		.success(function(data) {
			$scope.verificationMessage = data.message;
			$scope.verificationSuccess = true;
		}).error(function(data) {
			if(data.message){
				$scope.verificationMessage = data.message;
			}else{
				$scope.verificationMessage = data;
			}
		});
	};

	$scope.ok = function() {
		if($scope.verificationSuccess){
			$modalInstance.close($scope.output);	
		}else{
			alert("Not yet verified");
		}
		
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};

postrApp.factory('persona', function() {
	return {
			initialise : function(http, currentUser, onLoggedIn) {
				navigator.id.watch({
					loggedInUser : currentUser,
					onlogin : function(assertion) {
						http.post('/personaverification', {
							assertion : assertion
						}).then(function(response) {
							onLoggedIn(response.data);
						}, function(data) {
							navigator.id.logout();
							alert("Login failure: " + err);
						});
					},
					onlogout : function() {
						http.post('/personalogout').success(function() {
							location.reload();
						}).error(function(data) {
							alert("Failed to log you out: " + data);
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
});