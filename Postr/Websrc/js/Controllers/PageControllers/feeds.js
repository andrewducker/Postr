postrApp.controller('NewFeedOutputSelectionController',function ($scope, userData) {
	$scope.userData = userData;
	$scope.nextUrl = function(output){return "#/feed/new/"+output.siteName+"/"+output.id;};
	$scope.Cancel = function(){
		$location.path("");
	};
});

var setupForm = function($scope, outputId, feed, siteName, userData, inputs, postFunctions, $http, alerter, $location){
	feed = angular.copy(feed);
	var output = userData.getSiteItem(outputId);
	$scope.possibleInputs = inputs;
	if(inputs.length == 1){
		inputs[0].ticked = true;
	}
	$scope.feed = feed;
	feed.outputDescription = userData.describeSite(output);
	$scope.possibleTimes =  postFunctions.possibleTimes;
	$scope.Cancel = function(){
		$location.path("");
	};
	$scope.resetSubject=function(){
		$scope.feed.subject="Interesting Links for $date.format(\"dd-MM-yyyy\",$postingTime)";
	};
	$scope.resetContents=function()
	{
		$scope.feed.contents= ['<dl class="links">',
		               		'#foreach($link in $links)',
		            		'<dt class="link"><a href="$link.URL" rel="nofollow">$link.Title</a></dt>',
		            		'<dd style="margin-bottom: 0.5em;">#if($link.Description)<span class="link-description">$link.Description</span><BR/>#end', 
		            		'<small class="link-tags">(tags:#foreach($tag in $link.Tags) <A href="$tag.TagURL">$tag.Tag</A>#end )</small>',
		            		'</dd>',
		            		'#end',
		            		'</dl>'].join("\n");
	};
	$scope.feedDetailsTemplate = "sites/"+ siteName + "/FeedDetails.html";
	$scope.validation = function(){
		if($scope.feed.selectedInputs && $scope.feed.selectedInputs.length > 0){
			return true;
		}else{
			return false;
		}
	}
	$scope.Save = function(){
		feed.postingTime = postFunctions.processTime(feed.postingTime, feed.postingHour);
		feed.inputs = [];
		angular.forEach($scope.selectedInputs,function(input){feed.inputs.push(input.id);});
		$scope.feed.method = "SaveFeed";
		$http.post('/' + siteName, feed)
		.success(function(response) {
			feed.id = response.data;
			var originalFeed = userData.getFeed(feed.id);
			if(originalFeed == null){
				userData.feeds.push(feed);
			}else{
				angular.copy(feed,originalFeed);
			};
			alerter.alert(response.message);
			$location.path("");
		}).error(function(err) {
			alerter.alert("Failure: " + err);
			$location.path("");
		});
	};
};

postrApp.controller('NewFeedController',function ($scope, userData, postFunctions, $http, alerter, $location,$routeParams) {
	var feed = {
			output: $routeParams.outputId,
			siteName: $routeParams.siteName,
	};
	var outputId = $routeParams.outputId;
	var siteName = $routeParams.siteName;
	setupForm($scope, outputId, feed, siteName, userData, angular.copy(userData.inputs), postFunctions, $http, alerter, $location);
	$scope.resetSubject();
	$scope.resetContents();
	$scope.feed.daysToInclude = 1;
});

postrApp.controller('ExistingFeedController',function ($scope, userData, postFunctions, $http, alerter, $location,$routeParams) {
	var feed = userData.getFeed($routeParams.id);
	$scope.action = "Update";
	var outputId = feed.output;
	var siteName = feed.siteName;
	feed.postingHour = feed.postingTime.getUTCHours();
	$scope.selectedInputs = [];
	var possibleInputs = angular.copy(userData.inputs);
	angular.forEach(possibleInputs, function(input){
		if(feed.inputs.indexOf(input.id) > -1)
		input.ticked = true;
	});
	
	setupForm($scope, outputId, feed, siteName,userData, possibleInputs, postFunctions,  $http, alerter, $location);
	
	$scope.Delete = function(){
		userData.deleteFeed(feed);
		$location.path("");
	};

	

});