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

postrApp.controller('NewSiteDataController', function($routeParams, $scope, alerter, $location, $http, userData,siteSpecific){
	var siteName = $routeParams.siteName;
	$scope.item = {
			siteName : siteName 
			};
	siteSpecific.initialiseSiteData($scope.item);
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

postrApp.controller('EditSiteDataController', function($routeParams, $scope, alerter, $location, $http, userData,siteSpecific){
	var siteName = $routeParams.siteName;
	var item = userData.getSiteItem($routeParams.id);
	
	if(item){
		$scope.item =  angular.copy(item);
	}else{
		alerter.alert("Error - item not found.  Please report this!");
		$location.path("");
		return;
	}

	siteSpecific.initialiseSiteData($scope.item);

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
