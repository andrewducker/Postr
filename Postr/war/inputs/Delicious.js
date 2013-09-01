function Delicious(){
	var siteName = "Delicious";
	var siteID = "delicious";
	var usernameID = "delUsername";
	var credentialsWizard = "delCredentials"
		var identityID = "delIdentity"; 
	var verifyID = "delCredentialsVerify";

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
		$.el.div({id:'delCredentials',title:'Delicious Details'},
				$.el.form(
						$.el.label({for:'delUsername'},'User Name:'),
						$.el.input({type:'text',id:'delUsername'}),
						$.el.br(),
						$.el.button({type:'button',id:'delCredentialsVerify'},'Verify'),
						$.el.br(),
						$.el.div({id:'delIdentity'})
				)
		).appendTo(document.body);
		wizards.register("delCredentials");	

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

}