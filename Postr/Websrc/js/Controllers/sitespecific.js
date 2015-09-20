postrApp.factory('siteSpecific', function(userData,userData,$window){
	return {
		initialisePost : function(post){
			if(post.siteName == "Dreamwidth" || post.siteName == "Livejournal")
			{
				post.autoFormat = true;
			}
		},
		initialiseSiteData:function(siteData){
			if(siteData.siteName == "WordPress"){
				siteData.verifyWordPress = function(){
					$window.open(userData.wordPressValidationURL);
				}
			}
		}
	};
});

