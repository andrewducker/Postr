postrApp.controller('NewFeedOutputSelectionController',function ($scope, userData) {
	$scope.userData = userData;
	$scope.nextUrl = function(output){return "#/feed/new/"+output.siteName+"/"+output.id;};
	$scope.Cancel = function(){
		$location.path("");
	};
});

var setupForm = function($scope, outputId, feed, siteName, userData, inputs, postFunctions, $http, alerter, $location){
	var output = userData.getSiteItem(outputId);
	$scope.possibleInputs = inputs;
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
		$scope.feed.contents= ['<ul class="links">',
		               		'#foreach($link in $links)',
		            		'<li class="link"><A href="$link.URL">$link.Title</A>',
		            		'#if($link.Description)<BR/><span class="link-description">$link.Description</span>#end', 
		            		'<BR/><span class="link-tags">(tags:#foreach($tag in $link.Tags) <A href="$tag.TagURL">$tag.Tag</A>#end )</span>',
		            		'</li>',
		            		'#end',
		            		'</ul>'].join("\n");
	};
	$scope.feedDetailsTemplate = "sites/"+ siteName + "/FeedDetails.html";
	$scope.Save = function(){
		feed.postingTime = postFunctions.processTime(feed.postingTime, feed.postingHour);
		feed.inputs = [];
		angular.forEach($scope.selectedInputs,function(input){feed.inputs.push(input.id);});
		$scope.feed.method = "SaveFeed";
		$http.post('/' + siteName, feed)
		.success(function(response) {
			feed.id = response.data;
			if(userData.feeds.indexOf(feed) == -1){
				userData.feeds.push(feed);
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
});

postrApp.controller('ExistingFeedController',function ($scope, userData, postFunctions, $http, alerter, $location,$routeParams) {
	var feed = userData.getFeed($routeParams.id);
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
	

});