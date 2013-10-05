function Livejournal(displayName, url){
	var siteName = displayName;
	var siteID = url;
	var prefix = url;
	var usernameID = prefix+"Username";
	var passwordID = prefix+"Password";
	var timezoneID = prefix+"TimeZone";
	var credentialsWizard = prefix+"Credentials";
	var identityID = prefix+"Identity"; 
	var verifyID = prefix+"CredentialsVerify";
	var postingWizard = prefix+"PostMaker";
	var subjectID = prefix+"SubjectLine";
	var textID = prefix+"Text";
	var tagsID = prefix+"Tags";
	var visibilityID = prefix+"Visibility";
	
	var that = this;

	var postingWizardCreated = false;
	
	var saveCredentials = function(){
		if(j(identityID).text() != ""){
			var params = {method:"SaveData", userName:j(usernameID).val(), password:j(passwordID).val(), timeZone:j(timezoneID).val()};
			var posting = $.post(siteID,{params:JSON.stringify(params)});
			wizards.registerCallForWizardDisplay(posting).done(function(data){
				data.siteName = siteName;
				data.userName = params.userName;
				data.timeZone = params.timeZone;
				that.currentDeferral.resolve(data);
			});
		}
		else{
			wizards.showError("Identity not validated");
		}
	};

	var updateCredentials = function(){
		if(j(identityID).text() != ""){
			var params = {method:"UpdateData", password:j(passwordID).val(), timeZone:j(timezoneID).val(), id:that.currentOutput.id};
			var posting = $.post(siteID,{params:JSON.stringify(params)});
			wizards.registerCallForWizardDisplay(posting).done(function(data){
				that.currentOutput.timeZone = params.timeZone;
				that.currentDeferral.resolve();
			});
		}
		else{
			wizards.showError("Identity not validated");
		}
	};
	
	var makePost = function(){
		var params = {method:"MakePost", contents:j(textID).val(), subject:j(subjectID).val(),tags:j(tagsID).val(),visibility:j(visibilityID).val(), output:that.currentOutput};
		var posting = $.post(siteID,{params:JSON.stringify(params)});
		wizards.registerCallForWizardDisplay(posting).done(function(data){
				
			var result = new Object();
				result.message = data.result;
			
				data.result = result;
				data.siteName = siteName;
				data.contents = params.contents;
				data.subject = params.subject;
				data.tags = params.tags;
				data.visibility = params.visibility;
				data.output = params.output;
				that.currentDeferral.resolve(data);
			});
	};
	
	var addPosting = function(outputId){
		if (!postingWizardCreated) {
			createPostingWizard();
		}
		j(subjectID).val("");
		j(textID).val("");
		j(tagsID).val("");
		j(visibilityID).val("");
		j(postingWizard).dialog( "option", "buttons", [{text:"ok", click: function(){makePost();}}]);
		that.currentDeferral = $.Deferred();
		that.currentOutput = outputId;
		wizards.showPage(postingWizard);
		return that.currentDeferral;
	};
	
	var displayExistingPost = function(existingPost){
		if (!postingWizardCreated) {
			createPostingWizard();
		}
		j(subjectID).val(existingPost.subject);
		j(textID).val(existingPost.contents);
		j(tagsID).val(existingPost.tags);
		j(visibilityID).val(existingPost.visibility);
		j(postingWizard).dialog( "option", "buttons", [{text:"ok", click: function(){makePost();}}]);
		that.currentDeferral = $.Deferred();
		that.currentOutput = existingPost.output;
		wizards.showPage(postingWizard);
		return that.currentDeferral;
	};
	
	
	var createPostingWizard = function(){
		var timezoneElement = $.el.select({id:timezoneID});
		timeZones.forEach(function(timeZone){
			$.el.option(timeZone).appendTo(timezoneElement);
		});

		$.el.div({id:postingWizard, title:siteName+' Posting'},
				$.el.form(
						$.el.label({"for":subjectID},'Subject Line:'),
						$.el.input({type:'text', id:subjectID}),
						$.el.br(),
						$.el.label({"for":textID},'Text:'),
						$.el.textarea({rows:5,id:textID}),
						$.el.br(),
						$.el.label({"for":visibilityID},'Visibility:'),
						$.el.select({id:visibilityID},
							$.el.option("Public"),
							$.el.option("FriendsOnly"),
							$.el.option("Private")
						),
						$.el.br(),
						$.el.label({"for":tagsID},'Tags:'),
						$.el.input({type:'text',id:tagsID})
				)
		)
		.appendTo(document.body);
		
		wizards.register(postingWizard);
		postingWizardCreated = true;
	};
	
	var outputWizardCreated = false;
	
	var createOutputWizard = function(){
		var timezoneElement = $.el.select({id:timezoneID});
		timeZones.forEach(function(timeZone){
			$.el.option(timeZone).appendTo(timezoneElement);
		});
		$.el.div({id:credentialsWizard, title:siteName+' Details'},
				$.el.form(
					$.el.label({"for":usernameID},'User Name:'),
					$.el.input({type:'text',id:usernameID}),
					$.el.br(),
					$.el.label({"for":passwordID},'Password:'),
					$.el.input({type:'password', id:passwordID}),
					$.el.br(),
					$.el.button({type:'button', id:verifyID},'Verify'),
					$.el.br(),
					$.el.div({id:identityID}),
					$.el.br(),
					$.el.label({"for":timezoneID},'Time Zone:'),
					timezoneElement
				)
			)
			.appendTo(document.body);
		wizards.register(credentialsWizard);
		
		j(usernameID).change(function(){
			j(identityID).text("");
		});

		j(passwordID).change(function(){
			j(identityID).text("");
		});

		j(verifyID).click(function(){
			var params = {method:"VerifyPassword", userName:j(usernameID).val(), password:j(passwordID).val()};
			var posting = $.post(siteID,{params:JSON.stringify(params)});
			posting.done(function(data){
				var parsedData = $.parseJSON(data);
				j(identityID).text(parsedData.result);
				wizards.setProperty(credentialsWizard,"height","auto");
			});
			wizards.registerCallForWizardOnError(posting);
		});
		outputWizardCreated = true;
	};
	
	var addOutput = function(){
		if (!outputWizardCreated) {
			createOutputWizard();
		}
		j(identityID).text("");
		j(timezoneID).val("");
		j(passwordID).val("");
		j(usernameID).val("");
		j(usernameID).removeProp('disabled');
		j(credentialsWizard ).dialog( "option", "buttons", [{text:"ok", click: function(){saveCredentials();}}]);
		that.currentDeferral = $.Deferred();
		wizards.showPage(credentialsWizard);
		return that.currentDeferral;
	};

	var updateOutput = function(existingData){
		if (!outputWizardCreated) {
			createOutputWizard();
		}
		j(identityID).text("");
		j(passwordID).val("");
		j(usernameID).val(existingData.userName);
		j(timezoneID).val(existingData.timeZone);
		j(usernameID).prop('disabled','disabled');
		j( credentialsWizard ).dialog( "option", "buttons", [{text:"ok", click: function(){updateCredentials();}}]);
		that.currentOutput = existingData;
		that.currentDeferral = $.Deferred();
		wizards.showPage(credentialsWizard);
		return that.currentDeferral;
	};
	
	AddOutput(siteName, addOutput,updateOutput,addPosting, displayExistingPost);
};