postrApp.config(function($routeProvider){
	//Main page
	$routeProvider.when('/',{
		templateUrl:"summary.html",
		controller: "SummaryController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})	
	//Appdata
	.when ("/appdata",{
		templateUrl:  function(params){return "appData.html";}  ,
		controller: "EditAppDataController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//New feed
	.when ("/feed/new",{
		templateUrl:  function(params){return "selectOutput.html";}  ,
		controller: "NewFeedOutputSelectionController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//New feed for a specific output
	.when ("/feed/new/:siteName/:outputId",{
		templateUrl:  function(params){return "feedDetails.html";}  ,
		controller: "NewFeedController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//New feed
	.when ("/feed/:siteName/:id",{
		templateUrl:  function(params){return "feedDetails.html";}  ,
		controller: "ExistingFeedController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//New object for a particular site (i.e. Delicious or LJ)
	.when ("/site/new/:siteName",{
		templateUrl:  function(params){return "sites/"+ params.siteName + "/details.html";}  ,
		controller: "NewSiteDataController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//New object of a particular type (i.e. Input or Output)
	.when("/site/:itemType/new",{
		templateUrl: "newSite.html",
		controller: "NewSiteDataSelectionController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//Existing object
	.when ("/site/:siteName/:id",{
		templateUrl:  function(params){return "sites/"+ params.siteName + "/details.html";}  ,
		controller: "EditSiteDataController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//Specifying the Output for a new Post
	.when("/post/new",{
		templateUrl: "selectOutput.html",
		controller: "NewPostOutputSelectionController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//Details for a new post
	.when("/post/new/:siteName/:outputId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "NewPostDataController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//Details for a cloned post
	.when("/post/new/:siteName/from/:cloneId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "NewPostDataController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//Details for an existing post
	.when("/post/:siteName/:postId",{
		templateUrl: function(params){return "sites/"+params.siteName + "/post.html";},
		controller: "ExistingPostDataController",
		resolve : {
			userDataLoaded : function(userData){
				return userData.loaded;
			}
		}
	})
	//Something has gone wrong
	.otherwise({
		template:"<H1>I am lost at {{location.path()}}</H1>",
		controller: "LostController"
			});
});
