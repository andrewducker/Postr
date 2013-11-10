"use strict";
var postrApp = angular.module('postrApp', []);

var populateData = function($scope,orderByFilter,data){
	$scope.data = data;
	$scope.data.inputs = orderByFilter($scope.data.inputs,'userName');
	$scope.data.outputs = orderByFilter($scope.data.outputs,'userName');
	$scope.currentInput = $scope.data.inputs[0];
	$scope.currentOutput = $scope.data.outputs[0];
	$scope.loggedIn = true;
};

postrApp.controller('UserDataCtrl', function postCtrl($scope, $http, orderByFilter,persona) {
	$http.get('userdata').success(function(data) {
		if(data.inputs != null){
			populateData($scope, orderByFilter,data);
			persona.initialise(data.persona);
		}else{
			persona.initialise(null,function(newData){populateData($scope, orderByFilter,newData);});
			$scope.loggedOut = true;
		}
	});
	$scope.showInput = function(){
		alert($scope.currentInput.userName+"@"+$scope.currentInput.siteName);
	};
	$scope.showOutput = function(){
		alert($scope.currentOutput.userName+"@"+$scope.currentOutput.siteName);
	};
	$scope.login = function(){
		persona.login();
	};
	$scope.logout = function(){
		persona.logout();
	};
});

postrApp.factory('persona',function(){
	return persona;
});