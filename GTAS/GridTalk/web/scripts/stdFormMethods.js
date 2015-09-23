function submitWithMethod(sMethod, reset)
{
  try
  {
    selectMultiselectorChoices();
    if(reset)
    {
      document.entityForm.action = appendParameter(document.entityForm.action,'reset','true');
    }
    moveOperationContextId();
    showUpdatingMsg();
    document.entityForm.elements['method'].value=sMethod;
    window.notClosing = true;
    document.entityForm.submit();
  }
  catch(error)
  {
    alert('Error caught by submitWithMethod()\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function showUpdatingMsg()
{
  try
  {
    var entityForm = document['entityForm'];
    if(entityForm) entityForm.style.visibility='hidden';
    var contentParent = document.getElementById('content_parent');
    if(contentParent) contentParent.style.visibility='hidden';
    var multiEntityForm = document['multiEntityForm'];
    if(multiEntityForm) multiEntityForm.style.visibility='hidden';
    var errorParent = document.getElementById('insert_error_parent');
    if(errorParent) errorParent.style.display='none';
    var processingRequestParent = document.getElementById('processing_request_parent');
    if(processingRequestParent) processingRequestParent.style.display='block';
  }
  catch(error)
  {
    alert('Error caught by showUpdatingMethod()\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}


function moveOperationContextId()
{
  var ocField = document.entityForm.elements['operationContextId'];
  if(""+ocField != 'undefined')
  {
    document.entityForm.action = appendParameter(document.entityForm.action,'operationContextId',ocField.value);
    ocField.disabled = true;
  }
}

function serverRefresh()
{
  submitWithMethod('update',true);
}

function appendParameter(url, parameter, value)
{
  var delimeter = url.indexOf('?') == -1 ? '?' : '&';
  return url + delimeter + parameter + '=' + value;
}

function divertToMapping(mappingName)
{
  if( 0 == mappingName.indexOf('field:') )
  {
    var splitDivPath = mappingName.split(':');
    var fieldName = splitDivPath[1];
    var field = document.getElementById(fieldName + '_value');
    if(field)
    {
      if( (field.type == 'select-multiple') || (field.type == 'select-one' ) )
      {
        if( field.selectedIndex != -1 )
        {
          var value = field.options[field.selectedIndex].value;
          if(value)
          {
            splitDivPath[3] = value;
          }
          else
          {
            splitDivPath[2] = 'create';
          }
        }
        else
        {
          splitDivPath[2] = 'create';
        }
        mappingName = splitDivPath.join(':');
      }
    }
  }
  var dtField = document.entityForm.elements['divertTo'];
  if(dtField)
  {
    dtField.value=mappingName;
  }
  else
  {
    document.entityForm.action = appendParameter(document.entityForm.action,'divertTo',mappingName);
  }
  submitWithMethod('divert',true);
}

function setFocusField(fieldname)
{
  document.entityForm.elements[fieldname].focus();
}

function selectMultiselectorChoices()
{
  var selectTags = document.getElementsByTagName('select');
  for(var i=0; i < selectTags.length; i++)
  {
    if(selectTags[i].parentNode.className == 'multiselectorParent')
    {
      selectAll(selectTags[i], selectTags[i].className != 'multiselectorChoice');
    }
  }
}

function getFieldValue(fieldName)
{
  //not supported: select_multiple, radio, multiple checkboxes
  var fieldElement = document.entityForm.elements[fieldName];
  if(fieldElement)
  {
    var type = fieldElement.type;
    if( type == 'select-one' )
    {
      if( fieldElement.selectedIndex )
      {
        return fieldElement.options[fieldElement.selectedIndex].value;
      }
      else
      {
        return fieldElement.options ? fieldElement.options[0].value : null;
      }
    }
    else if( type == 'text' || type == 'hidden' )
    {
      return fieldElement.value;
    }
    else if( type == 'checkbox' )
    {
      if( fieldElement.checked )
      {
        return fieldElement.value;
      }
    }
  }
  return null; 
}

function getMultipleCheckboxValue(fieldName)
{
  var fieldElement = document.entityForm.elements[fieldName];
  if(fieldElement)
  {
    var retval = new Array();
    if(! ""+fieldElement.length == 'undefined' )
    {
      for(var i=0; i < fieldElement.length; i++)
      {
        if(fieldElement[i].checked)
        {
          retval[i] = fieldElement[i].value;
        }
      }
    }
    else
    {
      if(fieldElement.checked)
      {
        retval[0] = fieldElement.value;
      }
    }
    return retval;
  }
  return null;
}

function handleFormKeypress(e)
{
  if(!e)
  {
    e = window.event;
  }
  switch(e.keyCode)
  {
    case 13:
      submitWithMethod('save',true);
      break;
  }
}

function handleFormKeypress(e)
{
  if(!e)
  {
    e = window.event;
  }
  switch(e.keyCode)
  {
    case 13:
      submitWithMethod('save',true);
      break;
  }
}

function filterChars(e, validChars)
{
  try
  {
    if(!e)
    {
      alert("e = window.event;");
      e = window.event;
    }
    switch(e.keyCode)
    {
      case 8:
      case 13:
      case 37:
      case 39:
      case 36:
      case 35:
        break;
 
      default:
        // 20040223 DDJ: Fix for Mozilla
        //var fieldName = e.srcElement.name;
        var node = e.srcElement; // IE
        if('' + node == 'undefined')
        {
          node = e.target; // Mozilla
        }

        var fieldName = node.name;
        var fieldValue = getFieldValue(fieldName);
        for(i = 0; i < fieldValue.length; i++)
        {
          if(validChars.indexOf(fieldValue.charAt(i)) == -1)
          {
            fieldValue = fieldValue.substr(0, i) + fieldValue.substr(i + 1, fieldValue.length);
            i--;
          }
        }
        if(document.entityForm.elements[fieldName].value != fieldValue)
        {
          document.entityForm.elements[fieldName].value = fieldValue;
        }
    }
  }
  catch(error)
  {
    alert('Error caught by filterChars()\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

//TWX 17072007 Check is there any error msg in the error fields of the tabs within the html page
function tabErrorNotifier()
{
   try
   {
      var errMsg = "";
      var tabColumnIDs = retrieveTabColumnIDs();
      if(tabColumnIDs != null && tabColumnIDs.length > 0)
      {
	  
        for(var i = 0; i < tabColumnIDs.length; i++)
	    {
          var tabID = tabColumnIDs[i];
		  if(tabID != null && tabID.length > 0)
		  {
		    var panelID = tabID+'_panel';
		    var tabDesc = document.getElementById(tabID+"").innerHTML;
            errMsg += validateFieldsInPanel(panelID, tabDesc);
		  }
	    }
      }

      if(errMsg.length > 0)
      {
          alert("The tabs below are containing error\n\n"+errMsg+"\n Please review it.");
      }
   }
   catch(error)
   {
	   alert("Error caught by tabErrorNotifier()\ntype="+error.type +" \nname="+error.name);
	   throw error;
   }
}

function validateFieldsInPanel(panelID, tabDesc)
{
   var errorMsg = "";
   var panel = document.getElementById(panelID);
   var fieldsInPanel = panel.getElementsByTagName('td');

   for(var i = 0; fieldsInPanel && i < fieldsInPanel.length; i++)
   {
      var field = fieldsInPanel[i];
	  var fieldID = field.id;

	  if(fieldID && fieldID.indexOf('_error') > 0)
	  {
         if(field.innerHTML)
		 {
            var fieldErrMsg = field.innerHTML;
			if(trimAll(fieldErrMsg).length > 0)
			{
			  //alert("Found Error in field: "+ field.id+" under ##tab:"+tabDesc);
			  /*
              var e_index = fieldID.indexOf('_error');
			  var label = fieldID.substring(0, e_index);
			  var failedField = document.getElementById(label+'_label');
              
              errorMsg += "Tab="+tabDesc+'   ##field=';
			  errorMsg += failedField.innerHTML+"-->"+ fieldErrMsg+"\n";*/
			  errorMsg +="\t"+tabDesc+"\n";
              break;
			}
	     }
      }
   }

   return errorMsg;
}

function trimAll(sString)
{
   if(sString)
   {
      while (sString.substring(0,1) == ' ')
      {
         sString = sString.substring(1, sString.length);
      }
      while (sString.substring(sString.length-1, sString.length) == ' ')
      {
         sString = sString.substring(0,sString.length-1);
      }
      return sString;
   }
   return sString;
}

//TWX 18072007 Retrieves the ID of the tab header
function retrieveTabColumnIDs()
{
   var tabTable = document.getElementById('tabTable');
   var tabColumnArr = new Array();
   if(tabTable)
   {
      var tabTableRows = tabTable.getElementsByTagName('tr');
      if(tabTableRows && tabTableRows.length > 0)
      {
         var tabHeaderRow = tabTableRows[0]; //all the tab header id is resides in first row
         var tabHeaderColumn = tabHeaderRow.getElementsByTagName('td');
	     
	     if(tabHeaderColumn && tabHeaderColumn.length > 0)
	     { 
		   var index = 0;
           for(var i = 0; tabHeaderColumn && i < tabHeaderColumn.length; i++)
	       {
             var td = tabHeaderColumn[i];
			 var tdID = td.id;
		     if(tdID && tdID.indexOf('_tab'))
		     {
                tabColumnArr[i] = td.id;
				index++;
		     }
	       }
	    }
      }
   }
   return tabColumnArr;
}