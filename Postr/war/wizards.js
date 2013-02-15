var wizards = new Array();
closingAllPages = false;

function showWizardPage(boxToShow){
	if(wizards.length > 0){
		var currentPage = wizards[wizards.length-1];
		currentPage.dialog( "widget" ).hide();
	}
    wizards.push(boxToShow);
    boxToShow.on( "dialogclose", function( event, ui ) {rewindWizardPage();} );
    boxToShow.dialog("open");
}

function rewindWizardPage(){
	if(closingAllPages){
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
	closingAllPages = true;
	while(wizards.length > 0){
    	var currentPage = wizards.pop();
		currentPage.dialog("close");                        		
		}
	closingAllPages = false;
}
                        
