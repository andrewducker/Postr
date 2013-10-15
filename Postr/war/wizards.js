var wizards = new Array();
wizards.closingAllPages = false;
wizards.errorWizardPage = "errorReport";
wizards.errorDiv = "errorText";

wizards.showPage = function(boxToShow){
	var foundBox = j(boxToShow);
	if(wizards.length > 0){
		var currentPage = wizards[wizards.length-1];
		currentPage.dialog( "widget" ).hide();
	}
    wizards.push(foundBox);
    foundBox.dialog("open");
};

wizards.register = function(wizardToRegister, buttons){
	j( wizardToRegister ).dialog({ autoOpen: false },{modal:true}, buttons, {minWidth: 100 }, {width:'auto'}, {height:'auto'} );
	j(wizardToRegister).on( "dialogclose", function( event, ui ) {wizards.rewindPage();} );
};

wizards.rewindPage = function(){
	if(wizards.closingAllPages){
		return;
	}
    wizards.pop();
    if (wizards.length == 0) {
    }
    else{
    	var currentPage = wizards[wizards.length-1];
		currentPage.dialog( "widget" ).show();
    }
};

wizards.closeAllPages = function(){
	wizards.closingAllPages = true;
	while(wizards.length > 0){
    	var currentPage = wizards.pop();
		currentPage.dialog("close");                        		
		}
	wizards.closingAllPages = false;
};

wizards.registerCallForWizardDisplay = function(posting, textToUpdate, wizardPageToShow){
	if (!textToUpdate) {
		textToUpdate = "resultsText";
	}
	if (!wizardPageToShow) {
		wizardPageToShow = "results"; 
	}
	var deferral = $.Deferred();
	posting.done(function(data){
		var parsedData = $.parseJSON(data);
		j(textToUpdate).text(parsedData.message);
		wizards.showPage(wizardPageToShow);
		deferral.resolve(parsedData);
	});
	wizards.registerCallForWizardOnError(posting);
	return deferral;
};

wizards.registerCallForWizardOnError = function(posting){
	posting.fail(function(data){
		wizards.showError("Call Failed: " + data.responseText);
	});
};

wizards.showError = function(errorMessage){
	j(wizards.errorDiv).text(errorMessage);
	wizards.showPage(wizards.errorWizardPage);
};

wizards.setProperty = function(wizardPage, property, value){
	j( wizardPage ).dialog( "option", property, value);
};
                        
$(function(){
	$.el.div({id:'errorReport'},
			$.el.h1('Error!'),
			$.el.div({id:'errorText'},
					$.el.h2('An unknown error occurred'))).appendTo(document.body);
	wizards.register("errorReport",{buttons: [{text:"Cancel", click: wizards.closeAllPages},{text:"Back",click:function(){j("errorReport").dialog("close");}}]});

	$.el.div({id:'results', title:'Details Updated'},
			$.el.div({id:'resultsText'},
					$.el.h2('Did it work?'))).appendTo(document.body);
			
	wizards.register("results",{buttons: [{text:"Done", click: function(){wizards.closeAllPages();}}]});
	});
