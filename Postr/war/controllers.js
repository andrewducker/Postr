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

//The "alert" box causes the digest cycle to go awry, so we call it inside a timeout.
var myAlert = function(alertMessage){
	setTimeout(function(){ alert(alertMessage);});
};

postrApp.controller('UserDataCtrl',
		function postCtrl($scope, $http, orderByFilter, persona, $modal, $q, $window,$timeout) {
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
		myAlert($scope.currentInput.userName + "@"
				+ $scope.currentInput.siteName);
	};
	
	$scope.updateTimeZone = function(){
		var user = {timeZone : $scope.data.timeZone, method:"UpdateData"};
		$http.post('userdata', user)
		.success(function() {
			myAlert("Successfully updated!");
		}).error(function(data) {
			myAlert("Failed to update data: " + data);
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
				myAlert(response.message);
				deferred.resolve(result);
			}).error(function(err) {
				myAlert("Failure: " + err);
				deferred.reject(err);
			});
		});
		return deferred.promise;
	};
	
	var createOrUpdatePost = function(outputOrPost){
		$modal.open({
			templateUrl : 'sites/' + outputOrPost.siteName + '/post.html',
			controller : PostCtrl,
			resolve : {
				outputOrPost : function() {
					return outputOrPost;
				}
			}
		}).result.then(function(result) {
			result.method = "MakePost";
			result.output = outputOrPost.id;
			$http.post('/' + result.siteName, result)
			.success(function(response) {
				$scope.data.posts.push(result);
				myAlert(response.message);
			}).error(function(err) {
				myAlert("Failure: " + err);
			});
		});
	};
	
	$scope.createPost = function(){
		createOrUpdatePost($scope.currentOutput);
	};
	
	
	$scope.addOutput = function() {
		addOrUpdateData($scope.currentPossibleOutput, "Save").then(function(result){
			$scope.data.outputs.push(result);
			$scope.currentOutput = result;
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

var PostCtrl = function($scope, $modalInstance, outputOrPost, $http) {
	var newPost = angular.copy(outputOrPost);
	newPost.id = null;
	$scope.post = newPost;

	$scope.ok = function() {
			$modalInstance.close($scope.post);	
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};


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
			myAlert("Not yet verified");
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
							myAlert("Login failure: " + err);
						});
					},
					onlogout : function() {
						http.post('/personalogout').success(function() {
							location.reload();
						}).error(function(data) {
							myAlert("Failed to log you out: " + data);
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