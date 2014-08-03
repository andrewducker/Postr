"use strict";
var postrApp = angular.module('postrApp', [ 'ui.bootstrap','ngRoute' ]);

postrApp.controller('LostController',["$scope", "$location", function ($scope, $location) {
	$scope.location = $location;
}]);

postrApp.controller('NewPostDataController',["$scope", "$routeParams", "userData", "$http", "alerter", "$location", "dates", "postFunctions", function($scope, $routeParams,userData, $http, alerter, $location, dates, postFunctions){
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
}]);

postrApp.controller('ExistingPostDataController',["$scope", "$routeParams", "userData", "$http", "alerter", "$location", "dates", "postFunctions", function($scope, $routeParams,userData, $http, alerter, $location, dates, postFunctions){
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
}]);

postrApp.controller('NewPostOutputSelectionController',["$scope", "userData", function ($scope, userData) {
	$scope.userData = userData;
	$scope.Cancel = function(){
		$location.path("");
	};
}]);

postrApp.controller('NewSiteDataSelectionController',["$routeParams", "$scope", "userData", function ($routeParams, $scope, userData) {
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
}]);

postrApp.controller('NewSiteDataController', ["$routeParams", "$scope", "alerter", "$location", "$http", "userData", function($routeParams, $scope, alerter, $location, $http, userData){
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
}]);

postrApp.controller('EditSiteDataController', ["$routeParams", "$scope", "alerter", "$location", "$http", "userData", "$filter", function($routeParams, $scope, alerter, $location, $http, userData, $filter){
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
}]);


postrApp.controller('SummaryController',["$scope", "userData", "$filter", "$http", "alerter", function summaryController($scope,userData,$filter,$http,alerter){
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
	
}]);

postrApp.controller('UserDataCtrl', ["$scope", "persona", "userData", function postCtrl($scope, persona, userData) {
	$scope.data = userData;

	$scope.login = function() {
		persona.login();
	};
	$scope.logout = function() {
		persona.logout();
	};
}]);

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


postrApp.config(["$routeProvider", function($routeProvider){
	//Main page
	$routeProvider.when('/',{
		templateUrl:"summary.html",
		controller: "SummaryController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})	
	//New object for a particular site (i.e. Delicious or LJ)
	.when ("/site/new/:siteName",{
		templateUrl:  function(params){return "sites/"+ params.siteName + "/details.html";}  ,
		controller: "NewSiteDataController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//New object of a particular type (i.e. Input or Output)
	.when("/site/:itemType/new",{
		templateUrl: "newSite.html",
		controller: "NewSiteDataSelectionController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//Existing object
	.when ("/site/:siteName/:id",{
		templateUrl:  function(params){return "sites/"+ params.siteName + "/details.html";}  ,
		controller: "EditSiteDataController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//Specifying the Output for a new Post
	.when("/post/new",{
		templateUrl: "newPost.html",
		controller: "NewPostOutputSelectionController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//Details for a new post
	.when("/post/new/:siteName/:outputId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "NewPostDataController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//Details for a cloned post
	.when("/post/new/:siteName/from/:cloneId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "NewPostDataController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//Details for an existing post
	.when("/post/:siteName/:postId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "ExistingPostDataController",
		resolve : {
			userDataLoaded : ["userData", function(userData){
				return userData.loaded;
			}]
		}
	})
	//Something has gone wrong
	.otherwise({
		template:"<H1>I am lost at {{location.path()}}</H1>",
		controller: "LostController"
			});
}]);

postrApp.factory('userData', ["persona", "$http", "orderByFilter", "$filter", "alerter", "$q", function(persona, $http,orderByFilter, $filter, alerter,$q){
	var removeFromArray = function(object, array){
		var index = array.indexOf(object);
		if (index > -1) {
			array.splice(index,1);
		}
	};
	
	var loadedDeferral = $q.defer();
	
	var data = {
			loggedOut : false,
			loggedIn : false,
			describeSite : function(site){return  site.userName + "@" + site.siteName;},
			deleteSiteItem : function(item){
				item.method = "RemoveData";
				var self = this;
				$http.post('/DataManagement', item)
				.success(function(response) {
					removeFromArray(item, self.inputs);
					removeFromArray(item, self.outputs);
					alerter.alert(response.message);
				}).error(function(err) {
					alerter.alert("Failure: " + err);
				});
			},
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
				loadedDeferral.resolve();
			},
			loaded : loadedDeferral.promise
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
}]);
postrApp.factory('alerter',["$timeout", function($timeout){
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
}]);

postrApp.factory('persona', ["alerter", function(alerter) {
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
}]);

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
angular.module("postrApp").run(["$templateCache", function($templateCache) {$templateCache.put("newPost.html","Please select a site to post to:<BR>\r\n<div ng-repeat=\"output in userData.outputs | orderBy:\'userName\'\"><a ng-href=\"#/post/new/{{output.siteName}}/{{output.id}}\">{{userData.describeSite(output)}}</a></div>");
$templateCache.put("newSite.html","Please select a site:<BR>\r\n<div ng-repeat=\"site in siteList()  | orderBy:\'toString()\'\"><a ng-href=\"#/site/new/{{site}}\">{{site}}</a></div>");
$templateCache.put("summary.html","<form class=\"form-horizontal\">\r\n	<fieldset>\r\n		<legend>User</legend>\r\n		{{data.persona}} - <a href=# ng-click=\"logout()\">sign out</a>\r\n		<div class=\"control-group\">\r\n			<label class=\"control-label\" for=\"timeZone\">Time Zone:</label>\r\n			<div class=\"controls\">\r\n				<select ng-model=\"data.timeZone\" id=\"timeZone\"\r\n					ng-options=\"zone for zone in timeZones\"></select>\r\n				<button ng-click=\"updateTimeZone()\">Update</button>\r\n			</div>\r\n		</div>\r\n	</fieldset>\r\n\r\n	<fieldset>\r\n		<legend>Inputs</legend>\r\n		<div ng-show=\"data.inputs.length > 0\">\r\n			<table id=\"existingInputsTable\" border=1>\r\n				<tr>\r\n					<th>Input</th>\r\n				</tr>\r\n				<tr ng-repeat=\"input in data.inputs | orderBy:\'userName\'\">\r\n					<td><A href=\"#/site/{{input.siteName}}/{{input.id}}\">{{data.describeSite(input)}}</A></td>\r\n				</tr>\r\n			</table>\r\n		</div>\r\n		<A href=\"#/site/input/new\">Add New Input</A>\r\n	</fieldset>\r\n	<fieldset>\r\n		<legend>Outputs</legend>\r\n		<div class=\"control-group\" ng-show=\"data.outputs.length > 0\">\r\n			<table id=\"existingOutputsTable\" border=1>\r\n				<tr>\r\n					<th>Output</th>\r\n				</tr>\r\n				<tr ng-repeat=\"output in data.outputs | orderBy:\'userName\'\">\r\n					<td><A href=\"#/site/{{output.siteName}}/{{output.id}}\">{{data.describeSite(output)}}</A></td>\r\n				</tr>\r\n			</table>\r\n		</div>\r\n		<A href=\"#/site/output/new\">Add New Output</A>\r\n	</fieldset>\r\n	<fieldset>\r\n		<legend>Posts</legend>\r\n		<A href=\"#/post/new\">Create New Post</A>\r\n		<table id=\"existingPostsTable\" border=1>\r\n			<tr>\r\n				<th>Posted</th>\r\n				<th>Subject</th>\r\n				<th>Destination</th>\r\n				<th>Result</th>\r\n			</tr>\r\n			<tr ng-repeat=\"post in data.posts | orderBy:\'postingTime\'\">\r\n				<td><A href=\"#/post/{{post.siteName}}/{{post.id}}\">{{data.postingTimeAsText(post)}}</A></td>\r\n				<td>{{post.subject}}</td>\r\n				<td>{{postDestination(post)}}</td>\r\n				<td>{{post.result.message}}</td>\r\n			</tr>\r\n		</table>\r\n	</fieldset>\r\n</form>");
$templateCache.put("sites/Delicious/details.html","<div>\r\n	<div>\r\n		<h3>Delicious details</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"userName\">User Name:</label>\r\n				<div class=\"controls\">\r\n					<input ng-readonly=\"action==\'Update\'\" ng-model=\"item.userName\" name=\"userName\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<div class=\"controls\">\r\n					<button class=\"btn\" ng-hide=\"action==\'Update\'\" ng-click=\"verify()\">Verify</button>\r\n				</div>\r\n			</div>\r\n			<div>{{verificationMessage}}</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-hide=\"action==\'Update\'\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-danger\" ng-show=\"action==\'Update\'\" ng-click=\"Delete()\">Delete</button>\r\n	</div>\r\n</div>");
$templateCache.put("sites/Dreamwidth/details.html","<div>\r\n	<div>\r\n		<h3>Dreamwidth details</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"userName\">User Name:</label>\r\n				<div class=\"controls\">\r\n					<input ng-readonly=\"action==\'Update\'\" ng-model=\"item.userName\"\r\n						name=\"userName\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"password\">Password:</label>\r\n				<div class=\"controls\">\r\n					<input ng-model=\"item.password\" type=\"password\"\r\n						autocomplete=\"off\" name=\"password\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<div class=\"controls\">\r\n					<button class=\"btn\" ng-click=\"verify()\">Verify</button>\r\n				</div>\r\n			</div>\r\n			<div>{{verificationMessage}}</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-danger\" ng-show=\"action==\'Update\'\" ng-click=\"Delete()\">Delete</button>\r\n	</div>\r\n</div>");
$templateCache.put("sites/Dreamwidth/post.html","<div>\r\n	<div>\r\n		<h3>Dreamwidth post</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"subject\">Subject Line:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" id=\"subject\" ng-readonly=\"readOnly\" ng-model=\"post.subject\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"contents\">Text:</label>\r\n				<div class=\"controls\">\r\n					<textarea rows=5 id=\"contents\" ng-readonly=\"readOnly\" ng-model=\"post.contents\"></textarea>\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"visibility\">Visibility</label>\r\n				<div class=\"controls\">\r\n					<select id=\"visibility\" ng-readonly=\"readOnly\" ng-model=\"post.visibility\"><option>Public</option>\r\n						<option>FriendsOnly</option>\r\n						<option>Private</option>\r\n					</select>\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"format\">Auto Format:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"checkbox\" id=\"format\" ng-disabled=\"readOnly\" ng-model=\"post.autoFormat\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"tags\">Tags:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" id=\"tags\" ng-readonly=\"readOnly\" ng-model=\"post.tags\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"postInFuture\">Post in\r\n					future?</label>\r\n				<div class=\"controls\">\r\n					<input type=\"checkbox\" \r\n						id=postInFuture\r\n						ng-model=post.postingInFuture\r\n						ng-disabled=\"readOnly\"\r\n						ng-change=\"postInFutureChanged()\">\r\n				</div>\r\n			</div>\r\n			<div ng-show=\"post.postingInFuture\">\r\n				<label class=\"control-label\" for=\"postingHour\">Time:</label>\r\n				<div class=\"controls\">\r\n					<select \r\n						ng-model=\"post.postingHour\"\r\n						id=\"postingHour\"\r\n						ng-readonly=\"readOnly\"\r\n						ng-options=\"possibleTime for possibleTime in possibleTimes\"\r\n						>\r\n					</select>\r\n				</div>\r\n				<label class=\"control-label\" for=\"postingDay\">Day:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" class=\"form-control\"  ng-readonly=\"readOnly\"\r\n						datepicker-popup=\"yyyy-MM-dd\" ng-model=\"post.postingTime\" />\r\n				</div>\r\n			</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-show=\"!readOnly\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-inverse\"  ng-show=\"action==\'Update\'\" ng-click=\"Clone()\">Clone</button>\r\n	</div>\r\n</div>");
$templateCache.put("sites/Livejournal/details.html","<div>\r\n	<div>\r\n		<h3>Livejournal details</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"userName\">User Name:</label>\r\n				<div class=\"controls\">\r\n					<input ng-readonly=\"action==\'Update\'\" ng-model=\"item.userName\"\r\n						name=\"userName\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"password\">Password:</label>\r\n				<div class=\"controls\">\r\n					<input ng-model=\"item.password\" type=\"password\"\r\n						autocomplete=\"off\" name=\"password\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<div class=\"controls\">\r\n					<button class=\"btn\" ng-click=\"verify()\">Verify</button>\r\n				</div>\r\n			</div>\r\n			<div>{{verificationMessage}}</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-danger\" ng-show=\"action==\'Update\'\" ng-click=\"Delete()\">Delete</button>\r\n	</div>\r\n</div>");
$templateCache.put("sites/Livejournal/post.html","<div>\r\n	<div class=\"modal-header\">\r\n		<h3>Livejournal post</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"subject\">Subject Line:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" id=\"subject\" ng-readonly=\"readOnly\" ng-model=\"post.subject\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"contents\">Text:</label>\r\n				<div class=\"controls\">\r\n					<textarea rows=5 id=\"contents\" ng-readonly=\"readOnly\" ng-model=\"post.contents\"></textarea>\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"visibility\">Visibility</label>\r\n				<div class=\"controls\">\r\n					<select id=\"visibility\" ng-readonly=\"readOnly\" ng-model=\"post.visibility\"><option>Public</option>\r\n						<option>FriendsOnly</option>\r\n						<option>Private</option>\r\n					</select>\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"format\">Auto Format:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"checkbox\" id=\"format\" ng-disabled=\"readOnly\" ng-model=\"post.autoFormat\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"tags\">Tags:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" id=\"tags\" ng-readonly=\"readOnly\" ng-model=\"post.tags\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"postInFuture\">Post in\r\n					future?</label>\r\n				<div class=\"controls\">\r\n					<input type=\"checkbox\" \r\n						id=postInFuture\r\n						ng-model=post.postingInFuture\r\n						ng-disabled=\"readOnly\"\r\n						ng-change=\"postInFutureChanged()\">\r\n				</div>\r\n			</div>\r\n			<div ng-show=\"post.postingInFuture\">\r\n				<label class=\"control-label\" for=\"postingHour\">Time:</label>\r\n				<div class=\"controls\">\r\n					<select \r\n						ng-model=\"post.postingHour\"\r\n						id=\"postingHour\"\r\n						ng-readonly=\"readOnly\"\r\n						ng-options=\"possibleTime for possibleTime in possibleTimes\"\r\n						>\r\n					</select>\r\n				</div>\r\n				<label class=\"control-label\" for=\"postingDay\">Day:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" class=\"form-control\"  ng-readonly=\"readOnly\"\r\n						datepicker-popup=\"yyyy-MM-dd\" ng-model=\"post.postingTime\" />\r\n				</div>\r\n			</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-show=\"!readOnly\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-inverse\"  ng-show=\"action==\'Update\'\" ng-click=\"Clone()\">Clone</button>\r\n	</div>\r\n</div>");
$templateCache.put("sites/Pinboard/details.html","<div>\r\n	<div>\r\n		<h3>Pinboard details</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"userName\">User Name:</label>\r\n				<div class=\"controls\">\r\n					<input ng-readonly=\"action==\'Update\'\" ng-model=\"item.userName\" name=\"userName\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<div class=\"controls\">\r\n					<button class=\"btn\" ng-hide=\"action==\'Update\'\" ng-click=\"verify()\">Verify</button>\r\n				</div>\r\n			</div>\r\n			<div>{{verificationMessage}}</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-hide=\"action==\'Update\'\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-danger\" ng-show=\"action==\'Update\'\" ng-click=\"Delete()\">Delete</button>\r\n</div>");
$templateCache.put("sites/TestOutput/details.html","<div>\r\n	<div>\r\n		<h3>Test Output details</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"userName\">User Name:</label>\r\n				<div class=\"controls\">\r\n					<input ng-readonly=\"action==\'Update\'\" ng-model=\"item.userName\"\r\n						name=\"userName\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"password\">Password:</label>\r\n				<div class=\"controls\">\r\n					<input ng-model=\"item.password\" type=\"password\"\r\n						autocomplete=\"off\" name=\"password\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<div class=\"controls\">\r\n					<button class=\"btn\" ng-click=\"verify()\">Verify</button>\r\n				</div>\r\n			</div>\r\n			<div>{{verificationMessage}}</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-danger\" ng-show=\"action==\'Update\'\" ng-click=\"Delete()\">Delete</button>\r\n	</div>\r\n</div>");
$templateCache.put("sites/TestOutput/post.html","<div>\r\n	<div>\r\n		<h3>Test post</h3>\r\n	</div>\r\n	<div>\r\n		<form class=\"form-horizontal\">\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"subject\">Subject Line:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" id=\"subject\" ng-readonly=\"readOnly\" ng-model=\"post.subject\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"contents\">Text:</label>\r\n				<div class=\"controls\">\r\n					<textarea rows=5 id=\"contents\" ng-readonly=\"readOnly\" ng-model=\"post.contents\"></textarea>\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"visibility\">Visibility</label>\r\n				<div class=\"controls\">\r\n					<select id=\"visibility\" ng-readonly=\"readOnly\" ng-model=\"post.visibility\"><option>Public</option>\r\n						<option>FriendsOnly</option>\r\n						<option>Private</option>\r\n					</select>\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"format\">Auto Format:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"checkbox\" id=\"format\" ng-disabled=\"readOnly\" ng-model=\"post.autoFormat\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"tags\">Tags:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" id=\"tags\" ng-readonly=\"readOnly\" ng-model=\"post.tags\">\r\n				</div>\r\n			</div>\r\n			<div class=\"control-group\">\r\n				<label class=\"control-label\" for=\"postInFuture\">Post in\r\n					future?</label>\r\n				<div class=\"controls\">\r\n					<input type=\"checkbox\" \r\n						id=postInFuture\r\n						ng-model=post.postingInFuture\r\n						ng-disabled=\"readOnly\"\r\n						ng-change=\"postInFutureChanged()\">\r\n				</div>\r\n			</div>\r\n			<div ng-show=\"post.postingInFuture\">\r\n				<label class=\"control-label\" for=\"postingHour\">Time:</label>\r\n				<div class=\"controls\">\r\n					<select \r\n						ng-model=\"post.postingHour\"\r\n						id=\"postingHour\"\r\n						ng-readonly=\"readOnly\"\r\n						ng-options=\"possibleTime for possibleTime in possibleTimes\"\r\n						>\r\n					</select>\r\n				</div>\r\n				<label class=\"control-label\" for=\"postingDay\">Day:</label>\r\n				<div class=\"controls\">\r\n					<input type=\"text\" class=\"form-control\"  ng-readonly=\"readOnly\"\r\n						datepicker-popup=\"yyyy-MM-dd\" ng-model=\"post.postingTime\" />\r\n				</div>\r\n			</div>\r\n		</form>\r\n	</div>\r\n	<div>\r\n		<button class=\"btn btn-primary\" ng-show=\"!readOnly\" ng-click=\"Save()\">OK</button>\r\n		<button class=\"btn btn-warning\" ng-click=\"Cancel()\">Cancel</button>\r\n		<button class=\"btn btn-inverse\"  ng-show=\"action==\'Update\'\" ng-click=\"Clone()\">Clone</button>\r\n	</div>\r\n</div>");}]);
var timeZones = ["Africa/Abidjan","Africa/Accra","Africa/Addis_Ababa","Africa/Algiers","Africa/Asmara","Africa/Asmera","Africa/Bamako","Africa/Bangui","Africa/Banjul","Africa/Bissau","Africa/Blantyre","Africa/Brazzaville","Africa/Bujumbura","Africa/Cairo","Africa/Casablanca","Africa/Ceuta","Africa/Conakry","Africa/Dakar","Africa/Dar_es_Salaam","Africa/Djibouti","Africa/Douala","Africa/El_Aaiun","Africa/Freetown","Africa/Gaborone","Africa/Harare","Africa/Johannesburg","Africa/Juba","Africa/Kampala","Africa/Khartoum","Africa/Kigali","Africa/Kinshasa","Africa/Lagos","Africa/Libreville","Africa/Lome","Africa/Luanda","Africa/Lubumbashi","Africa/Lusaka","Africa/Malabo","Africa/Maputo","Africa/Maseru","Africa/Mbabane","Africa/Mogadishu","Africa/Monrovia","Africa/Nairobi","Africa/Ndjamena","Africa/Niamey","Africa/Nouakchott","Africa/Ouagadougou","Africa/Porto-Novo","Africa/Sao_Tome","Africa/Timbuktu","Africa/Tripoli","Africa/Tunis","Africa/Windhoek","America/Adak","America/Anchorage","America/Anguilla","America/Antigua","America/Araguaina","America/Argentina/Buenos_Aires","America/Argentina/Catamarca","America/Argentina/ComodRivadavia","America/Argentina/Cordoba","America/Argentina/Jujuy","America/Argentina/La_Rioja","America/Argentina/Mendoza","America/Argentina/Rio_Gallegos","America/Argentina/Salta","America/Argentina/San_Juan","America/Argentina/San_Luis","America/Argentina/Tucuman","America/Argentina/Ushuaia","America/Aruba","America/Asuncion","America/Atikokan","America/Atka","America/Bahia","America/Bahia_Banderas","America/Barbados","America/Belem","America/Belize","America/Blanc-Sablon","America/Boa_Vista","America/Bogota","America/Boise","America/Buenos_Aires","America/Cambridge_Bay","America/Campo_Grande","America/Cancun","America/Caracas","America/Catamarca","America/Cayenne","America/Cayman","America/Chicago","America/Chihuahua","America/Coral_Harbour","America/Cordoba","America/Costa_Rica","America/Creston","America/Cuiaba","America/Curacao","America/Danmarkshavn","America/Dawson","America/Dawson_Creek","America/Denver","America/Detroit","America/Dominica","America/Edmonton","America/Eirunepe","America/El_Salvador","America/Ensenada","America/Fort_Wayne","America/Fortaleza","America/Glace_Bay","America/Godthab","America/Goose_Bay","America/Grand_Turk","America/Grenada","America/Guadeloupe","America/Guatemala","America/Guayaquil","America/Guyana","America/Halifax","America/Havana","America/Hermosillo","America/Indiana/Indianapolis","America/Indiana/Knox","America/Indiana/Marengo","America/Indiana/Petersburg","America/Indiana/Tell_City","America/Indiana/Vevay","America/Indiana/Vincennes","America/Indiana/Winamac","America/Indianapolis","America/Inuvik","America/Iqaluit","America/Jamaica","America/Jujuy","America/Juneau","America/Kentucky/Louisville","America/Kentucky/Monticello","America/Knox_IN","America/Kralendijk","America/La_Paz","America/Lima","America/Los_Angeles","America/Louisville","America/Lower_Princes","America/Maceio","America/Managua","America/Manaus","America/Marigot","America/Martinique","America/Matamoros","America/Mazatlan","America/Mendoza","America/Menominee","America/Merida","America/Metlakatla","America/Mexico_City","America/Miquelon","America/Moncton","America/Monterrey","America/Montevideo","America/Montreal","America/Montserrat","America/Nassau","America/New_York","America/Nipigon","America/Nome","America/Noronha","America/North_Dakota/Beulah","America/North_Dakota/Center","America/North_Dakota/New_Salem","America/Ojinaga","America/Panama","America/Pangnirtung","America/Paramaribo","America/Phoenix","America/Port-au-Prince","America/Port_of_Spain","America/Porto_Acre","America/Porto_Velho","America/Puerto_Rico","America/Rainy_River","America/Rankin_Inlet","America/Recife","America/Regina","America/Resolute","America/Rio_Branco","America/Rosario","America/Santa_Isabel","America/Santarem","America/Santiago","America/Santo_Domingo","America/Sao_Paulo","America/Scoresbysund","America/Shiprock","America/Sitka","America/St_Barthelemy","America/St_Johns","America/St_Kitts","America/St_Lucia","America/St_Thomas","America/St_Vincent","America/Swift_Current","America/Tegucigalpa","America/Thule","America/Thunder_Bay","America/Tijuana","America/Toronto","America/Tortola","America/Vancouver","America/Virgin","America/Whitehorse","America/Winnipeg","America/Yakutat","America/Yellowknife","Antarctica/Casey","Antarctica/Davis","Antarctica/DumontDUrville","Antarctica/Macquarie","Antarctica/Mawson","Antarctica/McMurdo","Antarctica/Palmer","Antarctica/Rothera","Antarctica/South_Pole","Antarctica/Syowa","Antarctica/Vostok","Arctic/Longyearbyen","Asia/Aden","Asia/Almaty","Asia/Amman","Asia/Anadyr","Asia/Aqtau","Asia/Aqtobe","Asia/Ashgabat","Asia/Ashkhabad","Asia/Baghdad","Asia/Bahrain","Asia/Baku","Asia/Bangkok","Asia/Beirut","Asia/Bishkek","Asia/Brunei","Asia/Calcutta","Asia/Choibalsan","Asia/Chongqing","Asia/Chungking","Asia/Colombo","Asia/Dacca","Asia/Damascus","Asia/Dhaka","Asia/Dili","Asia/Dubai","Asia/Dushanbe","Asia/Gaza","Asia/Harbin","Asia/Hebron","Asia/Ho_Chi_Minh","Asia/Hong_Kong","Asia/Hovd","Asia/Irkutsk","Asia/Istanbul","Asia/Jakarta","Asia/Jayapura","Asia/Jerusalem","Asia/Kabul","Asia/Kamchatka","Asia/Karachi","Asia/Kashgar","Asia/Kathmandu","Asia/Katmandu","Asia/Khandyga","Asia/Kolkata","Asia/Krasnoyarsk","Asia/Kuala_Lumpur","Asia/Kuching","Asia/Kuwait","Asia/Macao","Asia/Macau","Asia/Magadan","Asia/Makassar","Asia/Manila","Asia/Muscat","Asia/Nicosia","Asia/Novokuznetsk","Asia/Novosibirsk","Asia/Omsk","Asia/Oral","Asia/Phnom_Penh","Asia/Pontianak","Asia/Pyongyang","Asia/Qatar","Asia/Qyzylorda","Asia/Rangoon","Asia/Riyadh","Asia/Saigon","Asia/Sakhalin","Asia/Samarkand","Asia/Seoul","Asia/Shanghai","Asia/Singapore","Asia/Taipei","Asia/Tashkent","Asia/Tbilisi","Asia/Tehran","Asia/Tel_Aviv","Asia/Thimbu","Asia/Thimphu","Asia/Tokyo","Asia/Ujung_Pandang","Asia/Ulaanbaatar","Asia/Ulan_Bator","Asia/Urumqi","Asia/Ust-Nera","Asia/Vientiane","Asia/Vladivostok","Asia/Yakutsk","Asia/Yekaterinburg","Asia/Yerevan","Atlantic/Azores","Atlantic/Bermuda","Atlantic/Canary","Atlantic/Cape_Verde","Atlantic/Faeroe","Atlantic/Faroe","Atlantic/Jan_Mayen","Atlantic/Madeira","Atlantic/Reykjavik","Atlantic/South_Georgia","Atlantic/St_Helena","Atlantic/Stanley","Australia/ACT","Australia/Adelaide","Australia/Brisbane","Australia/Broken_Hill","Australia/Canberra","Australia/Currie","Australia/Darwin","Australia/Eucla","Australia/Hobart","Australia/LHI","Australia/Lindeman","Australia/Lord_Howe","Australia/Melbourne","Australia/NSW","Australia/North","Australia/Perth","Australia/Queensland","Australia/South","Australia/Sydney","Australia/Tasmania","Australia/Victoria","Australia/West","Australia/Yancowinna","Brazil/Acre","Brazil/DeNoronha","Brazil/East","Brazil/West","CET","CST6CDT","Canada/Atlantic","Canada/Central","Canada/East-Saskatchewan","Canada/Eastern","Canada/Mountain","Canada/Newfoundland","Canada/Pacific","Canada/Saskatchewan","Canada/Yukon","Chile/Continental","Chile/EasterIsland","Cuba","EET","EST","EST5EDT","Egypt","Eire","Etc/GMT","Etc/GMT+0","Etc/GMT+1","Etc/GMT+10","Etc/GMT+11","Etc/GMT+12","Etc/GMT+2","Etc/GMT+3","Etc/GMT+4","Etc/GMT+5","Etc/GMT+6","Etc/GMT+7","Etc/GMT+8","Etc/GMT+9","Etc/GMT-0","Etc/GMT-1","Etc/GMT-10","Etc/GMT-11","Etc/GMT-12","Etc/GMT-13","Etc/GMT-14","Etc/GMT-2","Etc/GMT-3","Etc/GMT-4","Etc/GMT-5","Etc/GMT-6","Etc/GMT-7","Etc/GMT-8","Etc/GMT-9","Etc/GMT0","Etc/Greenwich","Etc/UCT","Etc/UTC","Etc/Universal","Etc/Zulu","Europe/Amsterdam","Europe/Andorra","Europe/Athens","Europe/Belfast","Europe/Belgrade","Europe/Berlin","Europe/Bratislava","Europe/Brussels","Europe/Bucharest","Europe/Budapest","Europe/Busingen","Europe/Chisinau","Europe/Copenhagen","Europe/Dublin","Europe/Gibraltar","Europe/Guernsey","Europe/Helsinki","Europe/Isle_of_Man","Europe/Istanbul","Europe/Jersey","Europe/Kaliningrad","Europe/Kiev","Europe/Lisbon","Europe/Ljubljana","Europe/London","Europe/Luxembourg","Europe/Madrid","Europe/Malta","Europe/Mariehamn","Europe/Minsk","Europe/Monaco","Europe/Moscow","Europe/Nicosia","Europe/Oslo","Europe/Paris","Europe/Podgorica","Europe/Prague","Europe/Riga","Europe/Rome","Europe/Samara","Europe/San_Marino","Europe/Sarajevo","Europe/Simferopol","Europe/Skopje","Europe/Sofia","Europe/Stockholm","Europe/Tallinn","Europe/Tirane","Europe/Tiraspol","Europe/Uzhgorod","Europe/Vaduz","Europe/Vatican","Europe/Vienna","Europe/Vilnius","Europe/Volgograd","Europe/Warsaw","Europe/Zagreb","Europe/Zaporozhye","Europe/Zurich","GB","GB-Eire","GMT","GMT+0","GMT-0","GMT0","Greenwich","HST","Hongkong","Iceland","Indian/Antananarivo","Indian/Chagos","Indian/Christmas","Indian/Cocos","Indian/Comoro","Indian/Kerguelen","Indian/Mahe","Indian/Maldives","Indian/Mauritius","Indian/Mayotte","Indian/Reunion","Iran","Israel","Jamaica","Japan","Kwajalein","Libya","MET","MST","MST7MDT","Mexico/BajaNorte","Mexico/BajaSur","Mexico/General","NZ","NZ-CHAT","Navajo","PRC","PST8PDT","Pacific/Apia","Pacific/Auckland","Pacific/Chatham","Pacific/Chuuk","Pacific/Easter","Pacific/Efate","Pacific/Enderbury","Pacific/Fakaofo","Pacific/Fiji","Pacific/Funafuti","Pacific/Galapagos","Pacific/Gambier","Pacific/Guadalcanal","Pacific/Guam","Pacific/Honolulu","Pacific/Johnston","Pacific/Kiritimati","Pacific/Kosrae","Pacific/Kwajalein","Pacific/Majuro","Pacific/Marquesas","Pacific/Midway","Pacific/Nauru","Pacific/Niue","Pacific/Norfolk","Pacific/Noumea","Pacific/Pago_Pago","Pacific/Palau","Pacific/Pitcairn","Pacific/Pohnpei","Pacific/Ponape","Pacific/Port_Moresby","Pacific/Rarotonga","Pacific/Saipan","Pacific/Samoa","Pacific/Tahiti","Pacific/Tarawa","Pacific/Tongatapu","Pacific/Truk","Pacific/Wake","Pacific/Wallis","Pacific/Yap","Poland","Portugal","ROC","ROK","Singapore","Turkey","UCT","US/Alaska","US/Aleutian","US/Arizona","US/Central","US/East-Indiana","US/Eastern","US/Hawaii","US/Indiana-Starke","US/Michigan","US/Mountain","US/Pacific","US/Pacific-New","US/Samoa","UTC","Universal","W-SU","WET","Zulu"];