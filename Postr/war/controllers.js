"use strict";
var postrApp = angular.module('postrApp', [ 'ui.bootstrap' ]);

postrApp.controller('UserDataCtrl',
		function postCtrl($scope, $http, orderByFilter, persona, $modal, $q, $window,$timeout, $filter, alerter) {
	$scope.level = "UserDataCtrl";
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

	var postingTimeToText = function(){
		var time = this.postingTime;
		return time.toUTCString().replace(" GMT","");
		//return time.getUTCFullYear()+"/"+time.getUTCMonth()+"/"+time.getUTCDate()+" - "+time.getUTCHours()+":"+time.getUTCMinutes()+":"+time.getUTCSeconds();
	};

	var populateData = function(scope, orderByFilter, result) {
		scope.data = result.data;
		scope.data.inputs = orderByFilter(scope.data.inputs, 'userName');
		scope.data.outputs = orderByFilter(scope.data.outputs, 'userName');
		scope.currentInput = scope.data.inputs[0];
		scope.currentOutput = scope.data.outputs[0];
		scope.currentPossibleOutput = scope.data.possibleOutputs[0];
		scope.currentPossibleInput = scope.data.possibleInputs[0];
		scope.data.posts.forEach(function(post){
			post.postingTime = new Date(post.postingTime);
			post.postingTimeText = postingTimeToText; 
		});
		scope.loggedIn = true;
		scope.loggedOut = false;
		scope.timeNow = Date();
	};

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
			alerter.alert(response.message);
			deferred.resolve();
		}).error(function(err) {
			alerter.alert("Failure: " + err);
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
		alerter.alert($scope.currentInput.userName + "@"
				+ $scope.currentInput.siteName);
	};

	$scope.updateTimeZone = function(){
		var user = {timeZone : $scope.data.timeZone, method:"UpdateData"};
		$http.post('userdata', user)
		.success(function() {
			alerter.alertAndReload("Successfully updated!");
		}).error(function(data) {
			alerter.alertAndReload("Failed to update data: " + data);
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
				alerter.alert(response.message);
				deferred.resolve(result);
			}).error(function(err) {
				alerter.alert("Failure: " + err);
				deferred.reject(err);
			});
		});
		return deferred.promise;
	};


	var createOrUpdatePost = function(outputOrPost){
		//Posts have outputs - so if output is set then this is an existing post to clone.
		//Otherwise it's an output, so we use its id as the output on a brand new post.
		var editingExistingPost = false;
		var newPost;
		if (outputOrPost.output) {
			var outputs = getOutput(outputOrPost);
			if (outputs.length == 0) {
				alerter.alert("Unknown output for this post, cannot display");
				return;
			}
			newPost = angular.copy(outputOrPost);
			//The post is awaiting posting time then we edit it.
			// Otherwise create a new post based on it, rather than editing it.
			if (outputOrPost.awaitingPostingTime) {
				editingExistingPost = true;
				newPost.postingTime = new Date(newPost.postingTime
						.getUTCFullYear(), newPost.postingTime.getUTCMonth(),
						newPost.postingTime.getUTCDate(), newPost.postingTime.getUTCHours());
			}
			else{
				delete newPost.result;	
				delete newPost.id;
				newPost.postingTime = new Date();
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
				if (response.data.state == "posted") {
					result.postingTime = Date.now();
				}else{
					result.awaitingPostingTime = true;
				}
				result.postingTimeText = postingTimeToText;
				result.id = response.data.id;
				if (editingExistingPost) {
					angular.copy(result, outputOrPost);
				}else{
					$scope.data.posts.push(result);	
				}
				alerter.alert(response.message);
			}).error(function(err) {
				alerter.alertAndReload("Failure: " + err);
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
	$scope.level="PostCtrl";
	$scope.post = post;

	$scope.possibleTimes= [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];

	if (post.awaitingPostingTime) {
		$scope.post.postingHour = post.postingTime.getHours();
		$scope.post.postingInFuture = true;
	}

	$scope.ok = function() {
		if ($scope.post.postingInFuture) {
			var localPostingTime = $scope.post.postingTime;
			$scope.post.postingTime = new Date();
			$scope.post.postingTime.setUTCFullYear(localPostingTime.getFullYear(), localPostingTime.getMonth(), localPostingTime.getDate());
			$scope.post.postingTime.setUTCHours($scope.post.postingHour,0,0,0);
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
	$scope.level = "DataPopupCtrl";
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
			alerter.alert("Not yet verified");
		}

	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
};

postrApp.factory('alerter',function($timeout){
	return {
		alert : function(alertMessage){
			return $timeout(function(){ 
				alert(alertMessage);
			});},
			alertAndReload : function(alertMessage){
				$timeout(function(){
					alert(alertMessage + " The page will now reload to get clean data.");
				})
				.then(function(){window.location.reload();});
			}
	};
});

postrApp.factory('persona', function(alerter) {
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
						alerter.alert("Login failure: " + err);
					});
				},
				onlogout : function() {
					http.post('/personalogout').success(function() {
						location.reload();
					}).error(function(data) {
						alerter.alert("Failed to log you out: " + data);
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