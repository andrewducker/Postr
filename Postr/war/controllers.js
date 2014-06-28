"use strict";
var postrApp = angular.module('postrApp', [ 'ui.bootstrap','ngRoute' ]);

postrApp.config(function($routeProvider){
	//Main page
	$routeProvider.when('/',{
		templateUrl:"summary.htm",
			controller: "SummaryController"
	})	
	//New object for a particular site (i.e. Delicious or LJ)
	.when ("/site/new/:siteName",{
		templateUrl:  function(params){return "sites/"+ params.siteName + "/details.html";}  ,
		controller: "NewSiteDataController"
	})
	//New object of a particular type (i.e. Input or Output)
	.when("/site/:itemType/new",{
		templateUrl: "newSite.htm",
		controller: "NewSiteDataSelectionController"
	})
	//Existing object
	.when ("/site/:siteName/:id",{
		templateUrl:  function(params){return "sites/"+ params.siteName + "/details.html";}  ,
		controller: "EditSiteDataController"
	})
	//Specifying the Output for a new Post
	.when("/post/new",{
		templateUrl: "newPost.htm",
		controller: "NewPostOutputSelectionController"
	})
	//Details for a new post
	.when("/post/new/:siteName/:outputId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "NewPostDataController"
	})
	//Details for an existing post
	.when("/post/:siteName/:postId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "ExistingPostDataController"
	})
	//Something has gone wrong
	.otherwise({
		template:"<H1>I am lost at {{location.path()}}</H1>",
		controller: "LostController"
			});
});

postrApp.controller('LostController',function ($scope, $location) {
	$scope.location = $location;
});

postrApp.factory('dates',function(){
	var dateUtilities = {
		now : function(){
			return this.convertToUTC(new Date());
		},
		convertFromUTC : function(dateToConvert){
			return new Date(dateToConvert.getUTCFullYear(), dateToConvert.getUTCMonth(),
			dateToConvert.getUTCDate(), dateToConvert.getUTCHours(), dateToConvert.getMinutes(),dateToConvert.getSeconds());
		},
		convertToUTC : function(dateToConvert){
			var offset = dateToConvert.getTimezoneOffset();
			offset = offset * 60000;
			return new Date(dateToConvert.getTime()-offset);
		}
	};
	return dateUtilities;
});

postrApp.factory('userData', function(persona, $http,orderByFilter, $filter){
	var data = {
			loggedOut : false,
			loggedIn : false,
			describeSite : function(site){return  site.userName + "@" + site.siteName;},
			getSiteItem : function(id){
				var search = {id:parseInt(id)};
				var found;
				found = $filter('filter')(this.inputs, search, true);
				if(found.length){
					return found[0];
				}
				found = $filter('filter')(this.outputs, search, true);
				if(found.length){
					return found[0];
				}
				return null;
			},			
			getPost : function(id){
				var search = {id:parseInt(id)};
				var found;
				found = $filter('filter')(this.posts, search, true);
				if(found && found.length){
					return found[0];
				}
				return null;
			},
			postingTimeAsText : function(post){
				return post.postingTime.toUTCString().replace(" GMT","");
			},
			populate : function(result) {
				angular.extend(this,result.data);
				this.currentInput = this.inputs[0];
				this.currentOutput = this.outputs[0];
				this.currentPossibleOutput = this.possibleOutputs[0];
				this.currentPossibleInput = this.possibleInputs[0];
				this.posts.forEach(function(post){
					post.postingTime = new Date(post.postingTime);
				});
				this.loggedOut = false;
				this.loggedIn = true;
			}
	};
	$http.post('userdata',{method:"GetData"}).success(function(result) {
		if (result.data != null) {
			data.populate(result);
			persona.initialise($http, result.data.persona);
		} else {
			persona.initialise($http, null, function(newData) {
				data.populate(newData);
			});
			data.loggedOut = true;
			data.loggedIn = false;
		}
	});

	return data;
});

postrApp.controller('NewPostDataController',function($scope, $routeParams,userData, $http, alerter, $location, dates, postFunctions){
	$scope.post = {output: $routeParams.outputId, siteName: $routeParams.siteName};
	$scope.possibleTimes= postFunctions.possibleTimes;
	$scope.postInFutureChanged = function(){
			postFunctions.postInFutureChanged($scope.post);
		};
	$scope.Cancel = function(){
		$location.path("");
	};
	
	$scope.Save = function() {
		$scope.post.method = "MakePost";
		postFunctions.processPostingTime($scope.post);

		$http.post('/' + $scope.post.siteName, $scope.post)
		.success(function(response) {
			$scope.post.result = response;
			if (response.data.state == "posted") {
				$scope.post.postingTime = dates.now();
			}else{
				$scope.post.awaitingPostingTime = true;
			}
			$scope.post.id = response.data.id;
			$scope.data.posts.push($scope.post);	
			alerter.alert(response.message);
			$location.path("");
		}).error(function(err) {
			alerter.alertAndReload("Failure: " + err);
		});
	};
});

postrApp.controller('ExistingPostDataController',function($scope, $routeParams,userData, $http, alerter, $location, dates){
	var originalPost =userData.getPost($routeParams.postId);
	$scope.possibleTimes= postFunctions.possibleTimes;
	$scope.postInFutureChanged = function(){
		postFunctions.postInFutureChanged($scope.post);
	};
	$scope.post = angular.copy(originalPost);
	
	if (post.awaitingPostingTime) {
		$scope.post.postingHour = post.postingTime.getHours();
		$scope.post.postingInFuture = true;
	}

	$scope.Cancel = function(){
		$location.path("");
	};
	$scope.Save = function() {
		$scope.post.method = "MakePost";
		postFunctions.processPostingTime($scope.post);
		$http.post('/' + $scope.post.siteName, $scope.post)
		.success(function(response) {
			$scope.post.result = response;
			if (response.data.state == "posted") {
				$scope.post.postingTime = Date.now();
			}else{
				$scope.post.awaitingPostingTime = true;
			}
			$scope.post.id = response.data.id;
			angular.copy($scope.post, originalPost);
			alerter.alert(response.message);
			$location.path("");
		}).error(function(err) {
			alerter.alertAndReload("Failure: " + err);
		});
	};
});

postrApp.controller('NewPostOutputSelectionController',function ($scope, userData) {
	$scope.userData = userData;
	$scope.Cancel = function(){
		$location.path("");
	};
});

postrApp.controller('NewSiteDataSelectionController',function ($routeParams, $scope, userData) {
	$scope.siteList = function(){
		if($routeParams.itemType == "input"){
			return userData.possibleInputs;
		}
		else{
			return userData.possibleOutputs;
		}
	};
	$scope.Cancel = function(){
		$location.path("");
	};
});

postrApp.controller('NewSiteDataController', function($routeParams, $scope, alerter, $location, $http, userData){
	var siteName = $routeParams.siteName;
	$scope.item = {siteName : siteName};
	$scope.verify = function(){
		$scope.verificationSuccess = false;
		$scope.item.method = "Verify";
		$http.post('/' + siteName, $scope.item)
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
	
	$scope.Save = function(){
		if(!$scope.verificationSuccess){
			alerter.alert("Not yet verified");
			return;
		}

		$scope.item.method = "SaveData";
		$http.post('/' + siteName, $scope.item)
		.success(function(response) {
			$scope.item.id = response.data;
			//Is the item an input?  Check if the site name is in the list of possible inputs
			if(userData.possibleInputs.indexOf($scope.item.siteName) > -1){
				userData.inputs.push($scope.item);
			}else{
				userData.outputs.push($scope.item);
			}
			alerter.alert(response.message);
			$location.path("");
		}).error(function(err) {
			alerter.alert("Failure: " + err);
			$location.path("");
		});
	};

	$scope.Cancel = function(){
		$location.path("");
	};
});

postrApp.controller('EditSiteDataController', function($routeParams, $scope, alerter, $location, $http, userData, $filter){
	var siteName = $routeParams.siteName;
	var item = userData.getSiteItem($routeParams.id);
	
	if(item){
		$scope.item =  angular.copy(item);
	}else{
		alerter.alert("Error - item not found.  Please report this!");
		$location.path("");
		return;
	}
	
	$scope.action = "Update";
	
	$scope.verify = function(){
		$scope.verificationSuccess = false;
		$scope.item.method = "Verify";
		$http.post('/' + siteName, $scope.item)
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
	
	$scope.Save = function(){
		if(!$scope.verificationSuccess){
			alerter.alert("Not yet verified");
			return;
		}

		$scope.item.method = "UpdateData";
		$http.post('/' + siteName, $scope.item)
		.success(function(response) {
			angular.copy($scope.item,item);
			alerter.alert(response.message);
			$location.path("");
		}).error(function(err) {
			alerter.alert("Failure: " + err);
			$location.path("");
		});
	};

	$scope.Cancel = function(){
		$location.path("");
	};
});


postrApp.controller('SummaryController',function summaryController($scope,userData){
	$scope.data = userData;
});

postrApp.controller('UserDataCtrl',
		function postCtrl($scope, $http, persona, $modal, $q, $window,$timeout, $filter, alerter,userData) {
	$scope.data = userData;

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
		var userData = {timeZone : $scope.data.timeZone, method:"UpdateData"};
		$http.post('userdata', userData)
		.success(function() {
			alerter.alertAndReload("Successfully updated!");
		}).error(function(data) {
			alerter.alertAndReload("Failed to update data: " + data);
		});
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

postrApp.factory('postFunctions', function(){
	return {
		possibleTimes : [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23],
		postInFutureChanged : function(post){
			if(post.postingInFuture){
				var newTime = new Date();
				post.postingTime = new Date(newTime.getFullYear(), newTime.getMonth(),newTime.getDate());
				post.postingHour = newTime.getHours();
			}
			else{
				delete post.postingHour;
				delete post.postingTime;
			}
		},
		processPostingTime : function(post){
			if (post.postingInFuture) {
				var localPostingTime = post.postingTime;
				post.postingTime = new Date();
				post.postingTime.setUTCFullYear(localPostingTime.getFullYear(), localPostingTime.getMonth(), localPostingTime.getDate());
				post.postingTime.setUTCHours(post.postingHour,0,0,0);
			}else{
				delete post.postingTime;
			}
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