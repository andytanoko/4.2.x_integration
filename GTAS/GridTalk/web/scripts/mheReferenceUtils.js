//depends tableUtils.js

var ICON_SECTION_IS_OPEN    = "images/controls/sectionIsOpen.gif";
var ICON_SECTION_IS_LOCKED_OPEN = "images/controls/sectionIsLockedOpen.gif";
var ICON_SECTION_IS_CLOSED  = "images/controls/sectionIsClosed.gif";
var HIDDEN_REF_CLASS = "hiddenSection";
var VISIBLE_REF_CLASS = "visibleSection";

function getAllSelected(parent, fieldName)
{
  if(parent == null) return false;
  
  var as = true;
  if('' + parent.childNodes != 'undefined')
  {
    for(var i=0; i < parent.childNodes.length; i++)
    {
      var child = parent.childNodes[i];
      if( nodeIsTagType(child,'input') && (fieldName == child.getAttribute('name')) )
      {
        as = child.checked ? as : false;
      }
      else
      {
        as = getAllSelected(child, fieldName) ? as : false;
      }
    }
  }
  return as;
}

function getNoneSelected(parent, fieldName)
{
  if(parent == null) return true;
  
  var ns = true;
  if('' + parent.childNodes != 'undefined')
  {
    for(var i=0; i < parent.childNodes.length; i++)
    {
      var child = parent.childNodes[i];
      if( nodeIsTagType(child,'input') && (fieldName == child.getAttribute('name')) )
      {
        ns = child.checked ? false : ns;
      }
      else
      {
        ns = getNoneSelected(child, fieldName) ? ns : false;
      }
    }
  }
  return ns;
}

function setAllSelected(parent, fieldName, selected)
{
  if('' + parent.childNodes != 'undefined')
  {
    for(var i=0; i < parent.childNodes.length; i++)
    { 
      var child = parent.childNodes[i];
      if( nodeIsTagType(child,'input') && (fieldName == child.getAttribute('name')) )
      {
        child.checked = selected;
      }
      else
      {
        setAllSelected(child, fieldName, selected);
      }
    }
  }
}

//.......................................................

function updateGroupSelector(fieldName, entityType, udms)
{
  var groupElement = getEntityGroupElement(fieldName, entityType);
  var as = getAllSelected(groupElement, fieldName);
  var allSelector = getAllSelector(fieldName,entityType);
  allSelector.checked = as;
  if(as)
  {
    setGroupExpansion(fieldName, entityType, true);
  }
  else
  {
    var ns = getNoneSelected(groupElement, fieldName);    
    if(ns)
    {
      ;
    }
    else
    {
      setGroupExpansion(fieldName, entityType, true);
    }
  }
  if(udms)
  {
    updateMasterSelector(fieldName);
  }
}

function setAllInGroup(fieldName, entityType)
{
  setAllSelected( getEntityGroupElement(fieldName,entityType),
                  fieldName,
                  getAllSelector(fieldName, entityType).checked );               
  updateGroupSelector(fieldName, entityType, true);
}

function toggleGroupExpansion(fieldName, entityType)
{
  var expander = getEntityGroupExpander(fieldName, entityType);
  var groupElement = getEntityGroupElement(fieldName, entityType);
  
  var display = HIDDEN_REF_CLASS == groupElement.className;
  
  setGroupExpansion(fieldName, entityType, display);
}

function setGroupExpansion(fieldName, entityType, display)
{
  var expander = getEntityGroupExpander(fieldName, entityType);
  var groupElement = getEntityGroupElement(fieldName, entityType);
  var src = display ? ICON_SECTION_IS_OPEN : ICON_SECTION_IS_CLOSED;
  if(!getAllSelected(groupElement, fieldName) && !getNoneSelected(groupElement, fieldName) )
  {
    display = true;
    src = ICON_SECTION_IS_LOCKED_OPEN;
  }
  groupElement.className = display ? VISIBLE_REF_CLASS : HIDDEN_REF_CLASS;
  expander.setAttribute('src', src );
}

//...........................................................

function getAllSelector(fieldName, entityType)
{
  if('' + entityType == 'undefined')
  {
    return document.getElementById( fieldName + '_selectAll' );
  }
  else
  {
    return document.getElementById( fieldName + '_' + entityType + '_selectAll' );
  }
}

function getEntityGroupElement(fieldName, entityType)
{
  return document.getElementById(fieldName + '_' + entityType + '_group');
}

function getEntityGroupExpander(fieldName, entityType)
{
  return document.getElementById(fieldName + '_' + entityType + '_expander');
}

//function checkAll(fieldName, checked)
//{ 
//  var fieldElement = document.entityForm.elements[fieldName];
//  if(fieldElement)
//  {
//    if(! ""+fieldElement.length == 'undefined' )
//    {
//      for(var i=0; i < fieldElement.length; i++)
//      {
//        fieldElement[i].checked = checked;
//      }
//    }
//    else
//    {
//      fieldElement.checked = checked;
//    }
//  }
//}

function setAllRecordsSelected(fieldName, selected)
{
  if('' + selected == 'undefined')
  {
    var allSelector = getAllSelector(fieldName);
    selected = allSelector.checked;
  }
  var parent = document.getElementById(fieldName + '_value');
  setAllSelected(parent, fieldName, selected);
  
  updateSelectorStates(fieldName);
}

function updateMasterSelector(fieldName)
{
  var parent = document.getElementById(fieldName + '_value');
  var masterSelector = getAllSelector(fieldName);
  masterSelector.checked = getAllSelected(parent, fieldName);;
}

function updateSelectorStates(fieldName)
{
  var etProperty = '_' + fieldName + '_entityTypes';
  var entityTypes = window[etProperty];
  for(var i=0; i < entityTypes.length; i++)
  {
    updateGroupSelector(fieldName, entityTypes[i], false);
  }
  updateMasterSelector(fieldName);
}