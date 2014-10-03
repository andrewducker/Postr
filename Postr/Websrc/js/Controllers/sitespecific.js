postrApp.factory('siteSpecific', function(){
	return {
		initialisePost : function(post){
			if(post.siteName == "Dreamwidth" || post.siteName == "Livejournal")
			post.autoFormat = true;
		}
	};
});

