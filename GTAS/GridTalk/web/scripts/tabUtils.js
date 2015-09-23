function setCurrentTab(name,baseId)
{
  try
  {
    var tabField = getTabField(name);
    var oldTabField = getTabField(name + 'Old');
    if(tabField != null)
    {
      var currentId = tabField.value;
      if(currentId != baseId)
      {
        oldTabField.value = currentId;
        tabField.value = baseId;
        if(!refreshIfRqd(name))
        {
          setTabVisible(currentId,false);
        }
        else
        {
          return;
        }
      }
    }
    setTabVisible(baseId,true);
  }
  catch(error)
  {
    alert('Error caught in setCurrentTab()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function getTabField(name)
{
  try
  {
    var checkField = document.entityForm.elements[name];
    if(checkField != null)
    {
      return checkField;
    }
    else
    {
      return null;
    }
  }
  catch(error)
  {
    alert('Error caught in getTabField()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function setTabVisible(baseId, visible)
{
  try
  {
    var panelId = baseId + '_panel';
    var panel = document.getElementById(panelId);
    var title = document.getElementById(baseId);
    if(visible)
    {
      panel.className = "visibleTab";
      title.className = "activeTab";
    }
    else
    {
      panel.className = "hiddenTab";
      title.className = "inactiveTab";
    }
  }
  catch(error)
  {
    alert('Error caught in setTabVisible()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}

function refreshIfRqd(name)
{
  try
  {
    var isRefresh = window[ (name + '_usr') ];
    if( isRefresh == true )
    {
      serverRefresh();
    }
    return isRefresh;
  }
  catch(error)
  {
    alert('Error caught in refreshIfRqd()\ntype=' + error.type + '\nname=' + error.name + '\nmsg=' + error.msg);
    throw error;
  }
}