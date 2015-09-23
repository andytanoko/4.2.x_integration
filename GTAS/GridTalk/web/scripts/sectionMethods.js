var SECTION_STATE_OPEN = "isOpen";
var SECTION_STATE_CLOSED = "isClosed";
//var SECTION_STATE_LOCKED_OPEN = "isLockedOpen";
//var SECTION_STATE_LOCKED_CLOSED = "isLockedClosed";

function toggleSectionExpansion(fieldName)
{
  var expander = getSectionExpander(fieldName);
  var contentElement = getSectionContentElement(fieldName);
  
  var display = 'hiddenSection' == contentElement.className;
  
  setSectionExpansion(fieldName, display);
}

function setSectionExpansion(fieldName, display)
{
  var expander = getSectionExpander(fieldName);
  var contentElement = getSectionContentElement(fieldName);
  var src = display ? getIconUrlFor(fieldName, SECTION_STATE_OPEN) : getIconUrlFor(fieldName, SECTION_STATE_CLOSED);
  contentElement.className = display ? 'visibleSection' : 'hiddenSection';
  expander.setAttribute('src', src );
}

function getSectionContentElement(fieldName)
{
  return document.getElementById(fieldName + '_section_content');
}

function getSectionExpander(fieldName)
{
  return document.getElementById(fieldName + '_section_expander');
}

function getIconUrlFor(fieldName, state)
{
  var iconVarName = 'iconUrlSection_' + fieldName + state;
  return window[iconVarName];
}