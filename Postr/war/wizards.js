var wizards = new Array();
wizards.closingAllPages = false;

function showWizardPage(boxToShow){
	if(wizards.length > 0){
		var currentPage = wizards[wizards.length-1];
		currentPage.dialog( "widget" ).hide();
	}
    wizards.push(boxToShow);
    boxToShow.dialog("open");
}

function registerWizard(wizardToRegister, buttons){
	$( wizardToRegister ).dialog({ autoOpen: false },{modal:true}, buttons);
	$(wizardToRegister).on( "dialogclose", function( event, ui ) {rewindWizardPage();} );
}

function rewindWizardPage(){
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

function closeAllPages(){
	wizards.closingAllPages = true;
	while(wizards.length > 0){
    	var currentPage = wizards.pop();
		currentPage.dialog("close");                        		
		}
	wizards.closingAllPages = false;
}

function registerCallForWizardDisplay(posting, textToUpdate, wizardPageToShow){
	posting.done(function(data){
		var parsedData = $.parseJSON(data);
		$("#"+textToUpdate).text(parsedData.result);
		showWizardPage($("#"+wizardPageToShow));
	});
	posting.fail(function(data){
		$("#"+textToUpdate).text("Call Failed: " + data.responseText);
		showWizardPage($("#"+wizardPageToShow));
	});

}
                        
