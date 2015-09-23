function submitMultipleEntities(actionMethod, msg, submitUrl)
{
  if( noneSelected() )
  {
    return;
  }
  if( msg != '' )
  {
    if(!confirm(msg)) return;
  }
  document.multiEntityForm.elements['method'].value = actionMethod;
  if(submitUrl)
  {
    if(jsOpConId)
    {
      submitUrl = urlAppend(submitUrl, 'operationContextId', jsOpConId);
    }
    document.multiEntityForm.action = submitUrl;
  }
  showListviewUpdatingMsg();
  document.multiEntityForm.submit();
}

function submitMultipleEntitiesNewWindow(actionMethod, msg, pergiUrl)
{
  if( noneSelected() )
  {
    return;
  }
  if( msg != '' )
  {
    if(!confirm(msg)) return;
  }
  document.multiEntityForm.elements['method'].value = actionMethod;
  if(!pergiUrl)
  {
    pergiUrl = document.multiEntityForm.action;
  }
  if(document.multiEntityForm.uid.length == null)
  {
    if(document.multiEntityForm.uid.checked)
    {
      uid = document.multiEntityForm.uid.value;
      pergiUrl = urlAppend(pergiUrl, 'uid', uid)
    }
  }
  else
  {
    for(i = 0; i < document.multiEntityForm.uid.length; i++)
    {
      if(document.multiEntityForm.uid[i].checked)
      {
        uid = document.multiEntityForm.uid[i].value;
        pergiUrl = urlAppend(pergiUrl, 'uid', uid);
      }
    }
  }
  pergi(pergiUrl);
}

function refreshListview(refreshUrl)
{
  if(refreshUrl)
  {
    if(jsOpConId)
    {
      refreshUrl = urlAppend(refreshUrl, 'operationContextId', jsOpConId);
    }
    refreshUrl = urlAppend(refreshUrl, 'refresh', 'true');
    document.multiEntityForm.action = refreshUrl;
    showListviewUpdatingMsg();
    document.multiEntityForm.submit();
  }
}

function showListviewUpdatingMsg()
{
  if(window['showUpdatingMsg']) showUpdatingMsg();
}

function noneSelected()
{
  var checkboxes = document.multiEntityForm.uid;
  if(checkboxes != null)
  {
    if( ""+checkboxes.length != 'undefined' )
      for(var i=0; i < checkboxes.length; i++)
      {
        var checkbox = checkboxes[i];
        if(checkbox.checked) return false;
      }
    else
    {
      if(checkboxes.checked) return false;
    }
  }
  return true;
}

function urlAppend(url, parameter, value)
{
  var delimeter = url.indexOf('?') == -1 ? '?' : '&';
  return url + delimeter + parameter + '=' + value;
}

function changePage(refreshUrl, direction)
{
  if(refreshUrl)
  {
    if(jsOpConId)
    {
      refreshUrl = urlAppend(refreshUrl, 'operationContextId', jsOpConId);
    }
    refreshUrl = urlAppend(refreshUrl, 'page', direction);
    document.multiEntityForm.action = refreshUrl;
    showListviewUpdatingMsg();
    document.multiEntityForm.submit();
  }
}