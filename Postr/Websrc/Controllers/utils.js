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