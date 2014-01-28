"use strict";
var testApp = angular.module('testApp', ['ui.bootstrap' ]);

testApp.controller('UserDataCtrl',function ($scope, $modal) {
	$scope.hour = 12;
	$scope.showPopup = function() {
		$modal.open({
			templateUrl : 'popup.html',
			controller : PopupCtrl,
			resolve : {
				hour : function() {
					return $scope.hour;
				},
				oldScope : function(){
					return $scope;
				}
		
			}
		}).result.then(function(result) {
			$scope.hour = result;
		});
	};

});

var PopupCtrl = function($scope, $modalInstance, $http, hour, oldScope) {
	var myOldScope = oldScope;
	$scope.level="PopupCtrl";

	$scope.possibleTimes= [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];

	$scope.hour = hour;

	$scope.ok = function() {
		$modalInstance.close($scope.hour);	
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

	$scope.changing = function(){
		var x = 5;
	};

};
