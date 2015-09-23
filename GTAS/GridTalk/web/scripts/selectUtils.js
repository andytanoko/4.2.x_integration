function selectAll(selector, selected)
{
  for(var i=0; i < selector.options.length; i++)
  {
    selector.options[i].selected = selected;
  }
}

function invertSelection(selector)
{
  for(var i=0; i < selector.options.length; i++)
  {
    selector.options[i].selected = !selector.options[i].selected;
  }
}

function addToSelector(selector, newOption)
{
  if(newOption.constructor == Array)
  {
    for(var i=0; i < newOption.length; i++)
    {
      selector.options[selector.options.length] = new Option(newOption[i].text, newOption[i].value);
    }
  }
  else
  {
    selector.options[selector.options.length] = newOption;
  }
}

function removeSelected(selector)
{
  for(var i=0; i < selector.options.length; i++)
  {
    if(selector.options[i].selected)
    {
      selector.options[i--] = null;
    }
  }
}

function getSelectedOptions(selector)
{
  var selection = new Array();
  for(var i=0; i < selector.options.length; i++)
  {
    if(selector.options[i].selected)
    {
      selection[selection.length] = selector.options[i];
    }
  }
  return selection;
}

function transferSelection(fromSelector, toSelector)
{
  var selection = getSelectedOptions(fromSelector);
  addToSelector(toSelector,selection);
  removeSelected(fromSelector);
}

function doTransfer(fromName, toName)
{
  try
  {
    var fromSelector = document.entityForm.elements[fromName];
    var toSelector = document.entityForm.elements[toName];
    transferSelection(fromSelector,toSelector);
  }
  catch(error)
  {
    showError('doTransfer',error);
    throw error;
  }
}

function showError(functionName, error)
{
  alert('Error caught by ' + functionName + '\ntype=' + error.type + '\nname=' + error.name);
}

function moveSelectedOptions(selectorName, up)
{
  try
  {
    var selector = document.entityForm.elements[selectorName];
    var selection = getSelectedOptions(selector);
    if(selection.length == 0) return;
    if(up)
    {
      if(selection[0].index == 0) return;
    }
    else
    {
      if(selection[selection.length-1].index == selector.options.length-1) return;
    }
    var d = up ? -1 : 1;
    var start = up ? 0 : selection.length-1;
    for(var i=start; (i >= 0) && (i < selection.length); i-=d)
    {
      var index = selection[i].index;
      var currentOption = selection[i];
      var swapOption = selector.options[index + d];
      selector.options[index] = new Option();
      selector.options[index+d] = currentOption;
      selector.options[index] = swapOption;
    }
  }
  catch(error)
  {
    showError('moveUp',error);
    throw error;
  }
}