postrApp.controller('NewPostDataController',function($scope, $routeParams,userData, $http, alerter, $location, dates, postFunctions,siteSpecific){
	if($routeParams.cloneId == null){
		$scope.post = {output: $routeParams.outputId, siteName: $routeParams.siteName};
		siteSpecific.initialisePost($scope.post);
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
		$scope.post.postingHour = $scope.post.postingTime.getUTCHours();
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
	$scope.nextUrl = function(output){return "#/post/new/"+output.siteName+"/"+output.id;};
	$scope.Cancel = function(){
		$location.path("");
	};
});
