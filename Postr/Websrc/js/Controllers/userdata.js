postrApp.factory('userData', function(persona, $http,orderByFilter, $filter, alerter,$q,$location){
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
			describeSite : function(site){
				if (typeof(site) == "number" || typeof(site) == "string"){
					site = this.getSiteItem(site);
				}
				return  site.userName + "@" + site.siteName;
				},
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
			deleteFeed : function(feed){
				feed.method = "RemoveData";
				var self = this;
				$http.post('/DataManagement', feed)
				.success(function(response) {
					removeFromArray(feed, self.feeds);
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
			describeFeed : 	function(feed){
				var inputDescriptions = feed.inputs.map(function(input){return this.describeSite(input);},this);
				var outputDescriptor = this.describeSite(feed.output);
				return  inputDescriptions.join("+") + "->" + outputDescriptor;
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
			getFeed : function(id){
				var search = {id:parseInt(id)};
				var found;
				found = $filter('filter')(this.feeds, search, true);
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
				this.posts.forEach(function(post){
					post.postingTime = new Date(post.postingTime);
				});
				this.feeds.forEach(function(feed){
					feed.postingTime = new Date(feed.postingTime);
				});
				var redirect = $location.protocol()+"://"+$location.host();
				if($location.port() != 80 && $location.port() != 443){
					redirect += ":"+$location.port();
				}
				redirect += "/WordPressAuthorisationComplete";
				this.wordPressValidationURL = "https://public-api.wordpress.com/oauth2/authorize?client_id="+this.wordPressClientId + "&redirect_uri="+redirect + "&response_type=code";
				this.loggedOut = false;
				this.loggedIn = true;
				loadedDeferral.resolve();
			},
			loaded : loadedDeferral.promise
	};
	$http.post('userdata',{method:"GetData", URL:$location.absUrl()}).success(function(result) {
		if (result.data.userEmail != null) {
			data.populate(result);
		} else {
			data.logoutURL = result.data.logoutURL
			data.loginURL = result.data.loginURL
			data.loggedOut = true;
			data.loggedIn = false;
		}
	});
	return data;
});