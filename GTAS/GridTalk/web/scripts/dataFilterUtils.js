function doFilterUpdateAction(fieldName, updateAction)
{
  try
  {
    var elementName = fieldName + ".updateAction";
    document.entityForm.elements[elementName].value = updateAction;
    serverRefresh(); 
  }
  catch(error)
  {
    alert('Error caught by doFilterUpdateAction('
          + fieldName + ',' + updateAction
          + ')\ntype=' + error.type
          + '\nname=' + error.name );
    throw error;
  }
}
