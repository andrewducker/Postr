postrApp.factory('userData', function(persona, $http,orderByFilter, $filter, alerter,$q){
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
});