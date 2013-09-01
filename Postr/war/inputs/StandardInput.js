function StandardInput(Name, url){
	var siteName = Name;
	var siteID = url;
	var usernameID = url+"Username";
	var credentialsWizard = url+"Credentials"
		var identityID = url+"Identity"; 
	var verifyID = url+"CredentialsVerify";

	var that = this;

	var saveCredentials = function(){
		if(j(identityID).text() != ""){
			var params = {method:"SaveData", userName:j(usernameID).val()};
			var posting = $.post(siteID,{params:JSON.stringify(params)});
			wizards.registerCallForWizardDisplay(posting).done(function(data){
				data.siteName = siteName;
				data.userName = params.userName;
				that.currentDeferral.resolve(data);
			});
		}
		else{
			wizards.showError("Identity not validated");
		}
	}

	var formCreated = false;

	var addForm = function(){
		$.el.div({id:credentialsWizard,title:siteName+' Details'},
				$.el.form(
						$.el.label({for:usernameID},'User Name:'),
						$.el.input({type:'text',id:usernameID}),
						$.el.br(),
						$.el.button({type:'button',id:verifyID},'Verify'),
						$.el.br(),
						$.el.div({id:identityID})
				)
		).appendTo(document.body);
		wizards.register(credentialsWizard);	

		j(usernameID).change(function(){
			j(identityID).text("");
		})

		j(verifyID).click(function(){
			var params = {method:"VerifyUserExists", userName:j(usernameID).val()};
			var posting = $.post(siteID,{params:JSON.stringify(params)});
			posting.done(function(data){
				var parsedData = $.parseJSON(data);
				j(identityID).text("Verified");
				wizards.setProperty(credentialsWizard,"height","auto");
			});
			wizards.registerCallForWizardOnError(posting);
		});
		formCreated=true;
	};

	var addInput = function(){
		if(!formCreated){
			addForm();
		}
		j(identityID).text("");
		j(usernameID).val("");
		var buttons = j(credentialsWizard ).dialog( "option", "buttons", [{text:"ok", click: function(){saveCredentials()}}]);
		that.currentDeferral = $.Deferred();
		wizards.showPage(credentialsWizard);
		return that.currentDeferral;
	}

	AddInput(siteName, addInput);
};