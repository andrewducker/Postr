var wizards = new Array();
wizards.closingAllPages = false;
wizards.errorWizardPage = "errorReport";
wizards.errorDiv = "errorText";

wizards.showPage = function(boxToShow){
	if(wizards.length > 0){
		var currentPage = wizards[wizards.length-1];
		currentPage.dialog( "widget" ).hide();
	}
    wizards.push(boxToShow);
    boxToShow.dialog("open");
}

wizards.register = function(wizardToRegister, buttons){
	$( wizardToRegister ).dialog({ autoOpen: false },{modal:true}, buttons);
	$(wizardToRegister).on( "dialogclose", function( event, ui ) {wizards.rewindPage();} );
}

wizards.rewindPage = function(){
	if(wizards.closingAllPages){
		return;
	}
    var previousPage = wizards.pop();
    if (wizards.length == 0) {
    }
    else{
    	var currentPage = wizards[wizards.length-1];
		currentPage.dialog( "widget" ).show();
    }
}

wizards.closeAllPages = function(){
	wizards.closingAllPages = true;
	while(wizards.length > 0){
    	var currentPage = wizards.pop();
		currentPage.dialog("close");                        		
		}
	wizards.closingAllPages = false;
}

wizards.registerCallForWizardDisplay = function(posting, textToUpdate, wizardPageToShow){
	posting.done(function(data){
		var parsedData = $.parseJSON(data);
		$("#"+textToUpdate).text(parsedData.result);
		wizards.showPage($("#"+wizardPageToShow));
	});
	wizards.registerCallForWizardOnError(posting);
}

wizards.registerCallForWizardOnError = function(posting){
	posting.fail(function(data){
		wizards.showError("Call Failed: " + data.responseText);
	});
}

wizards.showError = function(errorMessage){
	$("#"+wizards.errorDiv).text(errorMessage);
	wizards.showPage($("#"+wizards.errorWizardPage));
}
                        
$(function(){
	$('body').append('<div id="errorReport"><h1>Error!</h1><div id="errorText"><h2>An unknown error occurred</h2></div></div>');
	wizards.register("#errorReport",{buttons: [{text:"Cancel", click: wizards.closeAllPages},{text:"Back",click:function(){$("#errorReport").dialog("close")}}]});
	});
