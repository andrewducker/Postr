postrApp.controller('LostController',function ($scope, $location) {
	$scope.location = $location;
});

postrApp.controller('NewPostDataController',function($scope, $routeParams,userData, $http, alerter, $location, dates, postFunctions){
	if($routeParams.cloneId == null){
		$scope.post = {output: $routeParams.outputId, siteName: $routeParams.siteName};
	}
	else{
		var postToClone = userData.getPost($routeParams.cloneId); 
		$scope.post = angular.copy(postToClone);
		delete $scope.post.result;	
		delete $scope.post.id;
		delete $scope.post.postingTime;
	}

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

postrApp.controller('ExistingPostDataController',function($scope, $routeParams,userData, $http, alerter, $location, dates, postFunctions){
	$scope.action = "Update";
	var originalPost = userData.getPost($routeParams.postId);
	$scope.possibleTimes= postFunctions.possibleTimes;
	$scope.postInFutureChanged = function(){
		postFunctions.postInFutureChanged($scope.post);
	};
	$scope.post = angular.copy(originalPost);
	
	//Has it posted?
	if ($scope.post.awaitingPostingTime) {
		$scope.post.postingHour = $scope.post.postingTime.getHours();
		$scope.post.postingInFuture = true;
		
	}else{
		$scope.readOnly = true;
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
	
	$scope.Clone = function(){
		$location.path("/post/new/"+$scope.post.siteName+ "/from/"+$scope.post.id);
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
	$scope.item = {
			siteName : siteName 
			};
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
	
	$scope.Delete = function(){
		userData.deleteSiteItem(item);
		$location.path("");
	};

	$scope.Cancel = function(){
		$location.path("");
	};
});


postrApp.controller('SummaryController',function summaryController($scope,userData,$filter,$http,alerter){
	$scope.data = userData;
	$scope.postDestination = function(post){
		var result = userData.getSiteItem(post.output);
		if (result != null) {
			return result.userName + "@" + result.siteName; 
		}
		return "Output not found";
	};
	
	$scope.timeZones = timeZones;

	$scope.updateTimeZone = function(){
		var userData = {timeZone : $scope.data.timeZone, method:"UpdateData"};
		$http.post('userdata', userData)
		.success(function() {
			alerter.alertAndReload("Successfully updated!");
		}).error(function(data) {
			alerter.alertAndReload("Failed to update data: " + data);
		});
	};
	
});

postrApp.controller('UserDataCtrl', function postCtrl($scope, persona, userData) {
	$scope.data = userData;

	$scope.login = function() {
		persona.login();
	};
	$scope.logout = function() {
		persona.logout();
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

