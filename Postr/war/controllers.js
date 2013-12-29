"use strict";
var postrApp = angular.module('postrApp', [ 'ui.bootstrap' ]);

var populateData = function(scope, orderByFilter, result) {
	scope.data = result.data;
	scope.data.inputs = orderByFilter(scope.data.inputs, 'userName');
	scope.data.outputs = orderByFilter(scope.data.outputs, 'userName');
	scope.currentInput = scope.data.inputs[0];
	scope.currentOutput = scope.data.outputs[0];
	scope.currentPossibleOutput = scope.data.possibleOutputs[0];
	scope.currentPossibleInput = scope.data.possibleInputs[0];
	scope.loggedIn = true;
	scope.loggedOut = false;
};

postrApp.controller('UserDataCtrl',
		function postCtrl($scope, $http, orderByFilter, persona, $modal, $q) {
	$http.post('userdata',{method:"GetData"}).success(function(result) {
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
	$scope.timeZones = timeZones;
	$scope.showInput = function() {
		alert($scope.currentInput.userName + "@"
				+ $scope.currentInput.siteName);
	};
	
	$scope.updateTimeZone = function(){
		var user = {timeZone : $scope.data.timeZone, method:"UpdateData"};
		$http.post('userdata', user)
		.success(function() {
			alert("Successfully updated!");
		}).error(function(data) {
			alert("Failed to update data: " + data);
		});
	};

	
	var addOrUpdateData = function(itemOrID, method) {
		if (itemOrID.siteName) {
			var item = angular.copy(itemOrID);
		}else{
			item = {siteName : itemOrID}; 
		}
		var deferred = $q.defer();
		$modal.open({
			templateUrl : 'sites/' + item.siteName + '/details.html',
			controller : DataPopupCtrl,
			resolve : {
				output : function() {
					return item;
				},
				action : function() {
					return method;
				}
			}
		}).result.then(function(result) {
			result.method = method+"Data";
			$http.post('/' + result.siteName, result)
			.success(function(response) {
				if(!result.id){
					result.id = response.data;
				}
				console.log("About to alert");
				alert(response.message);
				console.log("About to resolve");
				deferred.resolve(result);
				console.log("Resolved");
			}).error(function(err) {
				alert("Failure: " + err);
				deferred.reject(err);
			});
		});
		return deferred.promise;
	};
	
	
	$scope.addOutput = function() {
		addOrUpdateData($scope.currentPossibleOutput, "Save").then(function(result){
			console.log("here");
			$scope.data.outputs.push(result);
			console.log("here 2");
			$scope.currentOutput = result;
			console.log("here 3");
			});
	};
		
	$scope.addInput = function() {
		addOrUpdateData($scope.currentPossibleInput, "Save").then(function(result){
			$scope.data.inputs.push(result);
			$scope.currentInput = result;
		});
	};
	
	$scope.updateOutput = function() {
		addOrUpdateData($scope.currentOutput, "Update").then(function(result){
			console.log("About to copy data");
			angular.copy(result, $scope.currentOutput);
		});
	};
	
	$scope.login = function() {
		persona.login();
	};
	$scope.logout = function() {
		persona.logout();
	};
});

var DataPopupCtrl = function($scope, $modalInstance, output, action, $http) {
	$scope.output = output;
	$scope.action = action;

	$scope.verify = function(){
		$scope.verificationSuccess = false;
		$scope.output.method = "Verify";
		$http.post('/' + $scope.output.siteName, $scope.output)
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