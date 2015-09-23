IMG_DIR = 'images/tabs/';
FANCYTAB_FIRST_DOWN = IMG_DIR + 'firstDown.gif';
FANCYTAB_FIRST_UP = IMG_DIR + 'firstUp.gif';
FANCYTAB_MID_DOWN_DOWN = IMG_DIR + 'midDownDown.gif';
FANCYTAB_MID_DOWN_UP = IMG_DIR + 'midDownUp.gif';
FANCYTAB_MID_UP_DOWN = IMG_DIR + 'midUpDown.gif';
FANCYTAB_LAST_DOWN = IMG_DIR + 'lastDown.gif';
FANCYTAB_LAST_UP = IMG_DIR + 'lastUp.gif';
FANCYTAB_BACK_DOWN = IMG_DIR + 'backDown.gif';
FANCYTAB_BACK_UP = IMG_DIR + 'backUp.gif';

function initFancyTabLookup(rowId)
{
  _cellLookup = new Array();
  _indexLookup = new Array();
  _firstTab = new Array();
  _lastTab = new Array();
  
  for(var j = 0; j >=0; j++)
  {
	 if(j > 0)
	 {
	   rowId = rowId+j;
	 }
	  var row = document.getElementById(rowId);
	  if(! row)
	  {
	     break;
	  }
	  
	  if(row.childNodes)
	  {
		  for(var i=0; i < row.childNodes.length; i++)
		  {
		    var child = row.childNodes[i];        
		    if( (child.nodeName == 'td') || (child.nodeName == 'TD') )
		    {
		      var index = _cellLookup.length;
		      _cellLookup[index] = child;
		      var id = child.getAttribute('id'); 
			  
		      if(id)
		      {
		        _indexLookup[id] = index;
				
				//TWX Identify the first tab
				if(i == 1)
				{
				   var currentIndex = _firstTab.length;
				   _firstTab[currentIndex] = index;
				}
		      }
			  
			  //TWX Identify the last tab.
			  if( i == (row.childNodes.length - 1))
			  {
				   var currentIndex = _lastTab.length;
				   _lastTab[currentIndex] = index;
			  }
		    }
		  }
      }  
  }
  
  
}

function selectFancyTabById(navlinkId)
{
  tabId = navlinkId + '_tab';
  var tab = document.getElementById(tabId);
  selectFancyTab(tab, true);
}

function OLDselectFancyTab(tab, isSelected)
{
  var tabId = tab.getAttribute('id'); 
  var index = _indexLookup[tabId];
 
  if(isSelected)
  {
    var frontIndex = index - 1;
    var frontImg = (frontIndex == 0) ? FANCYTAB_FIRST_UP : FANCYTAB_MID_DOWN_UP;
    var frontCell = _cellLookup[frontIndex];
    setCellImage(frontCell, frontImg);
    
    var rearIndex = index + 1;
    var rearImg = (rearIndex == (_cellLookup.length - 1)) ? FANCYTAB_LAST_UP : FANCYTAB_MID_UP_DOWN;
    var rearCell = _cellLookup[rearIndex];
    setCellImage(rearCell, rearImg);
    
    tab.className = 'fancyTabUp';
  }
  else
  {       
    var frontIndex = index - 1;
    var frontImg = (frontIndex == 0) ? FANCYTAB_FIRST_DOWN : FANCYTAB_MID_DOWN_DOWN;
    var frontCell = _cellLookup[frontIndex];
    setCellImage(frontCell, frontImg);
    
    var rearIndex = index + 1;
    var rearImg = (rearIndex == (_cellLookup.length - 1)) ? FANCYTAB_LAST_DOWN : FANCYTAB_MID_DOWN_DOWN;
    var rearCell = _cellLookup[rearIndex];
    setCellImage(rearCell, rearImg);
    
    tab.className = 'fancyTabDown';
  }
}

function selectFancyTab(tab, isSelected)
{
  
  var tabId = tab.getAttribute('id'); 
  var index = _indexLookup[tabId];
  var isFirstTab = checkIsFirstTab(index);
 
  var frontIndex = index - 1;
  var rearIndex = index + 1;
  var isLastTab = checkIsLastTab(rearIndex);
  
  var frontCell = _cellLookup[frontIndex];
  var rearCell = _cellLookup[rearIndex];
  
  var frontImg;
  var rearImg;
  var klassName;
  if(isSelected)
  {
    frontImg = (isFirstTab) ? FANCYTAB_FIRST_UP : FANCYTAB_MID_DOWN_UP;
    rearImg = (isLastTab) ? FANCYTAB_LAST_UP : FANCYTAB_MID_UP_DOWN;
    klassName = 'fancyTabUp';
  }
  else
  {
    frontImg = (isFirstTab) ? FANCYTAB_FIRST_DOWN : FANCYTAB_MID_DOWN_DOWN;
    rearImg = (isLastTab) ? FANCYTAB_LAST_DOWN : FANCYTAB_MID_DOWN_DOWN;
    klassName = 'fancyTabDown';;
  }
  setCellImage(frontCell, frontImg);
  setCellImage(rearCell, rearImg);
  tab.className = klassName;
}

function setCellImage(cell, src)
{
  for(var i=0; i < cell.childNodes.length; i++)
  {
    var child = cell.childNodes[i];
    if( (child.nodeName == 'img') || (child.nodeName == 'IMG') )
    {
      child.setAttribute('src',src);
    }
  }
}

function checkIsFirstTab(index)
{
   for(var i = 0 ; i < _firstTab.length; i++)
   {
      if(_firstTab[i] == index)
	  {
	     return true
	  }
   }
   return false;
}

function checkIsLastTab(index)
{
   for(var i = 0 ; i < _lastTab.length; i++)
   {
      if(_lastTab[i] == index)
	  {
	     return true;
	  }
   }
   return false;
}