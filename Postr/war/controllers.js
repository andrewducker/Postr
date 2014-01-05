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
		function postCtrl($scope, $http, orderByFilter, persona, $modal, $q, $window,$timeout, $filter) {
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

	var getOutput = function(post){
		return $filter('filter')($scope.data.outputs,function(outputToTest){return outputToTest.id == post.output;});
	};

	//Removes an object from an array, and returns what the current Object should be, once it's been removed.
	//Would update the pointer itself, but JS doesn't do Pass By Reference.
	var removeFromArray = function(object, array){
		var index = array.indexOf(object);
		if (index > -1) {
			array.splice(index,1);
			if (index > array.length - 1) {
				index = array.length - 1;
			}
			if (index > -1) {
				return array[index];	
			}
		}
		return null;
	};

	$scope.removeOutput = function(){
		removeData($scope.currentOutput).then(function(){
			$scope.currentOutput = removeFromArray($scope.currentOutput, $scope.data.outputs);
		});
	};

	$scope.removeInput = function(){
		removeData($scope.currentInput).then(function(){
			$scope.currentInput = removeFromArray($scope.currentInput, $scope.data.inputs);
		});
	};

	var removeData = function(data){
		var deferred = $q.defer();
		data.method = "RemoveData";
		$http.post('/DataManagement', data)
		.success(function(response) {
			myAlert(response.message);
			deferred.resolve();
		}).error(function(err) {
			myAlert("Failure: " + err);
		});
		return deferred.promise;
	};

	$scope.postDescription = function(post){
		var result = getOutput(post);
		if (result.length > 0) {
			return result[0].userName + "@" + result[0].siteName; 
		}
		return "Output not found";
	};
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
		//Posts have outputs - so if output is set then this is an existing post to clone.
		//Otherwise it's an output, so we use its id as the output on a brand new post.
		if (outputOrPost.output) {
			var newPost = angular.copy(outputOrPost);
			delete newPost.result;
			//We can update ones in the future, but not in the past.
			//Hence, we delete the id if the posting time is in the past, because we're creating a new one.
				if (newPost.postingTime < Date.now()) {
					newPost.postingTime = new Date();
					delete newPost.id;
				}else{
					newPost.postInFuture = true;
				}
			var outputs = getOutput(newPost);
			if (outputs.length == 0) {
				myAlert("Unknown output for this post, cannot display");
				return;
			}
			newPost.siteName = outputs[0].siteName;
		}else{
			newPost = {output : outputOrPost.id, siteName : outputOrPost.siteName, postingTime : new Date()};
		}
		$modal.open({
			templateUrl : 'sites/' + newPost.siteName + '/post.html',
			controller : PostCtrl,
			resolve : {
				post : function() {
					return newPost;
				}
			}
		}).result.then(function(result) {
			result.method = "MakePost";
			$http.post('/' + result.siteName, result)
			.success(function(response) {
				result.result = response;
				result.postingTime = Date.now();
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

	$scope.displayPost = function(post){
		createOrUpdatePost(post);
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

var PostCtrl = function($scope, $modalInstance, post, $http) {
	$scope.post = post;
	
	$scope.possibleTimes= ["0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"];

	$scope.post.postingHour = post.postingTime.getHours().toString();
	
	$scope.ok = function() {
		if ($scope.post.postingInFuture) {
			$scope.post.postingTime.setHours($scope.post.postingHour);
		}else{
			delete $scope.post.postingTime;
		}
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