postrApp.controller('LostController',function ($scope, $location) {
	$scope.location = $location;
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

