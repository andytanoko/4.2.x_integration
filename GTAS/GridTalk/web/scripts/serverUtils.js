function askForSecurityPassword()
{
  document.getElementById('menu').style.display='none';
  document.getElementById('menu2').style.display='none';
  document.getElementById('connect').style.display='block';
  setFocusField('securityPassword');
}
function cancelSecurityPassword()
{
  document.getElementById('menu').style.display='block';
  document.getElementById('menu2').style.display='block';
  document.getElementById('connect').style.display='none';
}
function connectToGridMaster()
{
  var connectWaitSection = document.getElementById('connect_wait_section');
  if(connectWaitSection != null)
  {
    connectWaitSection.style.display='block';
  }
  submitWithMethod('connect',false);
}
function disconnectFromGridMaster()
{
  submitWithMethod('disconnect',false);
}
function startBackendListener()
{
  submitWithMethod('startBackendListener',false);
}

function handleCtgmKeyboard(e)
{
  if(!e)
  {
    e = window.event;
  }
  switch(e.keyCode)
  {
    case 13:
      connectToGridMaster();
      break;
    
    case 27:
      cancelSecurityPassword();
  }
}