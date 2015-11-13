postrApp.controller('EditAppDataController', function($scope, alerter, $location, $http, userData, $filter){
	
	$scope.item =  {wordPressClientId:userData.wordPressClientId,wordPressClientSecret:userData.wordPressClientSecret};
	
	$scope.action = "Update";
	
	$scope.Save = function(){
		$scope.item.method = "UpdateData";
		$http.post('/appdata', $scope.item)
		.success(function(response) {
			userData.wordPressClientId = $scope.item.wordPressClientId
			userData.wordPressClientSecret = $scope.item.wordPressClientSecret
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
