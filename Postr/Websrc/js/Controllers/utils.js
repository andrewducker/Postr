postrApp.factory('alerter',function($timeout){
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
});

postrApp.factory('persona', function(alerter) {
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
});

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

postrApp.factory('postFunctions', function(){
	var processTime = function(localTime, localHour){
		serverTime = new Date();
		serverTime.setUTCFullYear(localTime.getFullYear(), localTime.getMonth(), localTime.getDate());
		serverTime.setUTCHours(localHour,0,0,0);
		return serverTime;
	};
	
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
				post.postingTime = processTime(post.postingTime,post.postingHour);
			}else{
				delete post.postingTime;
			}
		},
		processTime : processTime
	};
});