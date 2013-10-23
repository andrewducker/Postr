function Livejournal(displayName, url){
	var siteName = displayName;
	var siteID = url;
	var usernameID = url+"Username";
	var passwordID = url+"Password";
	var timezoneID = url+"TimeZone";
	var credentialsWizard = url+"Credentials";
	var identityID = url+"Identity"; 
	var verifyID = url+"CredentialsVerify";
	var postingWizard = url+"PostMaker";
	var subjectID = url+"SubjectLine";
	var textID = url+"Text";
	var tagsID = url+"Tags";
	var visibilityID = url+"Visibility";
	var autoFormatID = url+"AutoFormat";
	var allPostingSourcesID = url+"AllPostingSourcesID";
	
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
		var params = {method:"MakePost", contents:j(textID).val(), subject:j(subjectID).val(),tags:j(tagsID).val(),visibility:j(visibilityID).val(), output:that.currentOutput.id, autoFormat:j(autoFormatID).is(':checked')};
		var posting = $.post(siteID,{params:JSON.stringify(params)});
		wizards.registerCallForWizardDisplay(posting).done(function(data){

			var result = new Object();
			result.message = data.message;

			data.outputObject = that.currentOutput;
			data.result = result;
			data.siteName = siteName;
			data.contents = params.contents;
			data.subject = params.subject;
			data.tags = params.tags;
			data.visibility = params.visibility;
			data.output = params.output;
			data.autoFormat = params.autoFormat;
			that.currentDeferral.resolve(data);
		});
	};

	var addPosting = function(output){
		if (!postingWizardCreated) {
			createPostingWizard();
		}
		j(subjectID).val("");
		j(textID).val("");
		j(tagsID).val("");
		j(visibilityID).val("");
		j(autoFormatID).prop('checked', false);
		j(postingWizard).dialog( "option", "buttons", [{text:"ok", click: function(){makePost();}}]);
		that.currentDeferral = $.Deferred();
		that.currentOutput = output;
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
		if (existingPost.autoFormat) {
			j(autoFormatID).prop('checked', true);
		}else{
			j(autoFormatID).prop('checked', false);
		}
		j(postingWizard).dialog( "option", "buttons", [{text:"ok", click: function(){makePost();}}]);
		that.currentDeferral = $.Deferred();
		that.currentOutput = existingPost.outputObject;
		wizards.showPage(postingWizard);
		return that.currentDeferral;
	};

	var createPostingWizard = function(){
		$.el.div({id:postingWizard, title:siteName+' Posting'},
				$.el.form(
						$.el.div(
								$.el.label({"for":subjectID},'Subject Line:'),
								$.el.input({type:'text', id:subjectID})),
						$.el.div(
								$.el.label({"for":textID},'Text:'),
								$.el.textarea({rows:5,id:textID})),
						$.el.div(
								$.el.label({"for":visibilityID},'Visibility:'),
								$.el.select({id:visibilityID},
										$.el.option("Public"),
										$.el.option("FriendsOnly"),
										$.el.option("Private"))),
						$.el.div(
								$.el.label({"for":autoFormatID},'Auto Format:'),
								$.el.input({type:'checkbox', id:autoFormatID})),
						$.el.div(
								$.el.label({"for":tagsID},'Tags:'),
								$.el.input({type:'text',id:tagsID}))
				)
		)
		.appendTo(document.body);

		wizards.register(postingWizard);
		postingWizardCreated = true;
	};

	var templateWizardCreated = false;
	
	var createTemplateWizard = function(){
		$.el.div({id:postingWizard, title:siteName+' Posting'},
				$.el.form(
						$.el.div(
								$.el.label({"for":templateSubjectID},'Subject Line:'),
								$.el.input({type:'text', id:templateSubjectID})),
						$.el.div(
								$.el.label({"for":textID},'Text:'),
								$.el.textarea({rows:5,id:textID})),
						$.el.div(
								$.el.label({"for":visibilityID},'Visibility:'),
								$.el.select({id:visibilityID},
										$.el.option("Public"),
										$.el.option("FriendsOnly"),
										$.el.option("Private"))),
						$.el.div(
								$.el.label({"for":autoFormatID},'Auto Format:'),
								$.el.input({type:'checkbox', id:autoFormatID})),
						$.el.div(
								$.el.label({"for":tagsID},'Tags:'),
								$.el.input({type:'text',id:tagsID})),
						$.el.div(
								$.el.label({"for":allPostingSourcesID},'All Sources'),
								$.el.select({id:allPostingSourcesID}))
				)
		)
		.appendTo(document.body);

		wizards.register(postingWizard);
		templateWizardCreated = true;
	};

	
	var outputWizardCreated = false;

	var createOutputWizard = function(){
		var timezoneElement = $.el.select({id:timezoneID});
		timeZones.forEach(function(timeZone){
			$.el.option(timeZone).appendTo(timezoneElement);
		});
		$.el.div({id:credentialsWizard, title:siteName+' Details'},
				$.el.form(
						$.el.div(
							$.el.label({"for":usernameID},'User Name:'),
							$.el.input({type:'text',id:usernameID})
						),
						$.el.div(
							$.el.label({"for":passwordID},'Password:'),
							$.el.input({type:'password', id:passwordID})
						),
						$.el.div(
								$.el.button({type:'button', id:verifyID},'Verify')
						),
						$.el.div({id:identityID}),
						$.el.div(
						$.el.label({"for":timezoneID},'Time Zone:'),
						timezoneElement
						)
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
				j(identityID).text(parsedData.message);
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

	var addTemplate = function(){
		if (!templateWizardCreated) {
			createTemplateWizard();
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

	AddOutput(siteName, addOutput,updateOutput,addPosting, displayExistingPost,addTemplate);
};