function getSelectedRows(tbodyId)
{
  try
  {
    var selection = new Array();
    var table = document.getElementById(tbodyId);
    var count = 0;
    for(var i=0; i < table.childNodes.length; i++)
    {
      var row = table.childNodes[i];
      if(nodeIsTagType(row,'tr'))
      {
        var checkbox = getRowCheckbox(row);
        if(checkbox.checked)
        {
          selection[selection.length] = count;
        }
        count++;
      }
    }
    return selection;
  }
  catch(error)
  {
    alert('Error caught in getSelectedRows()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function invertSelectedRows(tbodyId)
{
  try
  {
    var table = document.getElementById(tbodyId);
    for(var i=0; i < table.childNodes.length; i++)
    {
      var row = table.childNodes[i];
      if(nodeIsTagType(row,'tr'))
      {
        var checkbox = getRowCheckbox(row);
        if(checkbox != null)
        {
          checkbox.checked = !checkbox.checked;
        }
      }
    }
    highlightRows(tbodyId);
  }
  catch(error)
  {
    alert('Error caught in invertSelectedRows()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function getRowCheckbox(row)
{
  var checkbox = findChild(row,'input','type','checkbox');
  if(checkbox == null)
  {
    var error = new Error('getRowCheckbox() Couldnt find checkbox');
    throw error;
  }
  return checkbox;
}

function findChild(parent, tagType, attributeName, attributeValue)
{
  for(var i=0; i < parent.childNodes.length; i++)
  {
    var child = parent.childNodes[i];
    if(nodeIsTagType(child,tagType))
    {
      if(attributeName != null)
      {
        if(child.getAttribute(attributeName) == attributeValue)
        {
          return child;
        }
      }
      else
      {
        return child;
      }
    }
    else
    {
      var candidate = findChild(child, tagType, attributeName, attributeValue);
      if(candidate != null)
      {
        return candidate;
      }
    }
  }
  return null;
}

function getRowNode(tbodyId, index)
{
  var table = document.getElementById(tbodyId);
  var count = 0;
  var rowNode = null;
  for(var i=0; i <= table.childNodes.length; i++)
  {
    var child = table.childNodes[i];
    if( nodeIsTagType(child,'tr') )
    {
      if(count == index) return child;
      count++;
    }
  }
  return rowNode;
}

function getTableLength(tbodyId)
{
  var table = document.getElementById(tbodyId);
  var count = 0;
  for(var i=0; i < table.childNodes.length; i++)
  {
    var child = table.childNodes[i];
    if( nodeIsTagType(child,'tr') )
    {
      count++;
    }
  }
  return count;
}

function move(tbodyId,up,selectionFieldName)
{
  //alert('move(' + tbodyId + ',' + up + ',' + selectionFieldName +')');
  try
  {
    var selection = getSelectedRows(tbodyId);
    if(selection.length == 0) return;
    var numRows = getTableLength(tbodyId);
    if(up)
    {
      if(selection[0] == 0) return;
    }
    else
    {
      if(selection[selection.length - 1] == (numRows - 1)) return;
    }

    var d = up ? -1 : 1;
    var start = up ? 0 : selection.length-1;
    for(var i=start; (i >= 0) && (i < selection.length); i -= d)
    {
      var index = selection[i];
      var currentRow = getRowNode(tbodyId, index);
      var swapIndex = index + d;
      var swapRow = getRowNode(tbodyId, swapIndex);
      var rowParent = currentRow.parentNode;
      if(up)
      {
        rowParent.insertBefore(currentRow,swapRow);
      }
      else
      {
        rowParent.insertBefore(swapRow,currentRow);
      }
      var checkbox = getRowCheckbox(currentRow);
      checkbox.checked = true;
      updateSelectionField(tbodyId,selectionFieldName);
    }
  }
  catch(error)
  {
    alert('Error caught in move()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function highlightRows(tbodyId)
{
  try
  {
    var table = document.getElementById(tbodyId);
    if(table == null)
    {
      var error = new Error();
      error.msg = 'Couldnt find table body with id=' + tbodyId;
      throw error;
    }
    var numRows = table.childNodes.length;
    var someNotSelected = numRows > 0 ? false : true;
    for(var i=0; i < numRows; i++)
    {
      var row = table.childNodes[i];
      var isRow = nodeIsTagType(row,'tr');
      if(isRow)
      {
        var checkbox = getRowCheckbox(row);
        if(checkbox != null)
        {
          if(checkbox.checked)
          {
            setRowCellBackground(row, 'ddddff');
          }
          else
          {
            someNotSelected = true;
            setRowCellBackground(row, '');
          }
        }
      }
    }
    var selectAllBox = document.getElementById(tbodyId + '_selectAll');
    if(selectAllBox != null)
    {
      if(someNotSelected)
      {
        selectAllBox.checked = false;
      }
      else
      {
        selectAllBox.checked = true;
      }
    }
  }
  catch(error)
  {
    alert('Error caught in highlightRows()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function setRowCellBackground(row, rowCol)
{
  for(var i=0; i < row.childNodes.length; i++)
  {
    var child = row.childNodes[i];
    if(nodeIsTagType(child,'td'))
    {
      child.style.backgroundColor = rowCol;
    }
  }
}

function nodeIsTagType(testNode, testTagType)
{
  testTagType = testTagType.toUpperCase();
  var candidateType = testNode.nodeName.toUpperCase();
  var result = (candidateType == testTagType);
  return result;
}

function getAllValuesInOrder(tbodyId)
{
  try
  {
    var values = new Array();
    var table = document.getElementById(tbodyId);
    for(var i=0; i < table.childNodes.length; i++)
    {
      var row = table.childNodes[i];
      if(nodeIsTagType(row,'tr'))
      {
        var checkbox = getRowCheckbox(row);
        values[values.length] = checkbox.value;
      }
    }
    return values;
  }
  catch(error)
  {
    alert('Error caught in getAllValuesInOrder()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function updateSelectionField(tbodyId, selectionFieldName)
{
  //alert('updateSelectionField(' + tbodyId + ',' + selectionFieldName + ')');
  try
  {
    var values = getAllValuesInOrder(tbodyId);
    document.entityForm.elements[selectionFieldName].value = values;
  }
  catch(error)
  {
    alert('Error caught in updateSelectionField()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function initTable(tbodyId,selectionFieldName)
{
  highlightRows(tbodyId);
  updateSelectionField(tbodyId,selectionFieldName);
}

function removeRows(tbodyId,selectionFieldName)
{
  try
  {
    var selection = getSelectedRows(tbodyId);
    for(var i = 0; i < selection.length; i ++)
    {
      var rowNode = getRowNode(tbodyId, selection[i]);
      var checkbox = getRowCheckbox(rowNode);
      checkbox.value = '';
    }
    updateSelectionField(tbodyId,selectionFieldName);
    serverRefresh();
  }
  catch(error)
  {
    alert('Error caught in updateSelectionField()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function removeElvRow(tbodyId, selectionFieldName)
{
  try
  {
    var selection = getSelectedRows(tbodyId);
	var concatenateID = "";
	for(var i = 0; i < selection.length; i++)
	{
		concatenateID = concatenateID + selection[i]+";";
	}
	document.entityForm.elements[selectionFieldName].value = concatenateID;
    serverRefresh();
  }
  catch(error)
  {
    alert('Error caught in updateSelectionField()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

var tabs = new Array();

function initTab(tabName, size)
{
  tabs[tabs.length] = new Array(tabName, 0);

  for(i = 1; i < size; i++)
  {
    var extraTab = document.getElementById(tabName + i);
    extraTab.style.display = "none";
  }
}

function setActiveTabIndex(tabName, index)
{
  for(i = 0; i < tabs.length; i++)
    if(tabs[i][0] == tabName)
    {
      tabs[i][1] = index;
      return;
    }
}

function getActiveTabIndex(tabName)
{
  for(i = 0; i < tabs.length; i++)
    if(tabs[i][0] == tabName)
      return tabs[i][1];
}

function switchTab(tabName, index)
{
  var oldTab = document.getElementById(tabName + getActiveTabIndex(tabName));
  oldTab.style.display = "none";

  var newTab = document.getElementById(tabName + index);
  newTab.style.display = "";

  setActiveTabIndex(tabName, index);
}

function ieTableHeaderStyleHack(formId)
{
  try
  {
    //Hack to solve IE style application issue in table header
    var formElement = document.getElementById(formId);
    if(formElement != null)
    {
      formElement.style.visibility = 'hidden';
      setTimeout("document.getElementById('" + formId + "').style.visibility = 'visible';",50);
    }
  }
  catch(error)
  {
    //ignore
  }
  return false;
}

function selectAllRows(tbodyId)
{
  try
  {
    var selectAllBox = document.getElementById( tbodyId + '_selectAll' );
    if(selectAllBox == null) return;
    var doCheck = selectAllBox.checked;
    var table = document.getElementById(tbodyId);
    for(var i=0; i < table.childNodes.length; i++)
    {
      var row = table.childNodes[i];
      if(nodeIsTagType(row,'tr'))
      {
        var checkbox = getRowCheckbox(row);
        if(checkbox != null)
        {
          checkbox.checked = doCheck;
        }
      }
    }
    highlightRows(tbodyId);
  }
  catch(error)
  {
    alert('Error caught in invertSelectedRows()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}
