var persona = {
		initialise : function(currentUser, onLoggedIn){
			navigator.id.watch({  	
				loggedInUser: currentUser,  
				onlogin: function(assertion) {   
					jQuery.ajax({  	
						type: 'POST',	        
						url: '/personaverification', 
						data: {assertion: assertion},
						success: function(res, status, xhr) { onLoggedIn(res); },	        
						error: function(xhr, status, err) {	          
							navigator.id.logout();	          
							alert("Login failure: " + err);	        
						}
					});  
				},
				onlogout: function() {
					jQuery.ajax({  	
						type: 'POST',        
						url: '/personalogout',
						success: function(res,status,xhr) {location.reload();},
						error: function(xhr, status, err) {
							alert("Failed to log you out: "+err);
							location.reload();
						}
					}); 
				}
			});	
		},
		login : function(){ navigator.id.request();},
		logout: function() {navigator.id.logout();}
};