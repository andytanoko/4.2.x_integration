function setUpdateAction(updateAction) 
{ 
	try 
	{ 
		var elementName = "updateAction"; 
		document.entityForm.elements[elementName].value = updateAction; 
		serverRefresh();
	} catch(error) 
	{ 
		alert('Error caught by setUpdateAction(' + updateAction + ')\ntype=' + error.type + '\nname=' + error.name ); 
		throw error; 
	} 
}

function setAddUpdateAction()
{
	setUpdateAction('addUpdateAction');
}

function setRemoveUpdateAction()
{
	setUpdateAction('removeUpdateAction');
}