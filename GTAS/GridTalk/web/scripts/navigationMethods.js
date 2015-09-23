function checkWindowClosing()
{
  try
  {
    var notClosing = window['notClosing'];
    if(notClosing)
    {
      ;
    }
    else
    {
      var rmocOnClose = window['rmocOnClose'];
      if(rmocOnClose)
      {
        rmocOnClose();
      }
    }
  }
  catch(error)
  {
    alert('Error caught in checkWindowClosing();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function performRmoc(rmocUrl, forceNewWin)
{
  try
  {
    if(rmocUrl == null) return;
    rmocUrl = appendParameterToUrl(rmocUrl, 'isDetailView','true');
    var pw = null;
    if(forceNewWin && (forceNewWin == true))
    {
      pw = null;
    }
    else
    {
      pw = window['opener'];
    }
    var win = null;
    if( (pw != null) &&( ("" + pw) != "") )
    {
      try
      {
        win = pw.open(rmocUrl,'','width=100,height=100');
      }
      catch(error0)
      {
        ;
      }
    }
    else
    {
      try
      {
        win = window.open(rmocUrl,'','width=100,height=100');
      }
      catch(error1)
      {
        ;
      }
    }
    if(win) win.blur();
  }
  catch(error)
  {
    alert('Error caught in performRmoc(' + rmocUrl + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}


function jalan(url)
{
  try
  {
    if(url == null) return;
    if(confirmRmoc(url))
    {
      var showUpdatingMsg = window['showUpdatingMsg'];
      if(showUpdatingMsg) showUpdatingMsg();
      window.notClosing = true;
      location.replace(url);
    }
  }
  catch(error)
  {
    alert('Error caught in jalan(' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function isRmoc(url)
{
  try
  {
    var index = url.indexOf('rmoc');
    return (index != -1);
  }
  catch(error)
  {
    alert('Error caught in isRmoc(' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function confirmRmoc(url)
{
  try
  {
    if(isRmoc(url))
    {
      var undefined;
      if(window['rmocConfirmMsg'] === undefined)
      {
        return true;
      }
      else
      {
        return confirm(window.rmocConfirmMsg);
      }
    }
    else
    {
      return true;
    }
  }
  catch(error)
  {
    alert('Error caught in confirmRmoc(' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function pergi(url)
{
  try
  {
    if(url == null) return;
    url = appendParameterToUrl(url, 'isDetailView','true');
    var win = window.open(url,'','scrollbars,status,width=640,height=480,resizable');
    registerDetailView(win);
  }
  catch(error)
  {
    alert('Error caught in pergi(' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function openPwl(url)
{
  try
  {
    if(url == null) return;
    url = appendParameterToUrl(url, 'isDetailView','true');
    var win = null;
    try
    {
      win = getPwl();
      if(win)
      {
        ;
      }
      else
      {
        win = window.open(url,'GTASPWL','width=250,height=100',true);
        registerPwl(win);
      }
    }
    catch(error0)
    {
      alert('openPwl() encountered an error calling window.open()');
      throw error0;
    }
    try
    {
      if(win) win.focus();
    }
    catch(error1)
    {
      ; //Ignore. IE throws 'unspecified error' here quite often!
    }
  }
  catch(error)
  {
    alert('Error caught in openPwl(' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function appendParameterToUrl(url, parameter, value)
{
  var delimeter = url.indexOf('?') == -1 ? '?' : '&';
  return url + delimeter + parameter + '=' + value;
}

function getHighlightNode(nodeId)
{
  try
  {
    var retval = new Object();
    retval.node = null;
    retval.type = null;
    var hNode = document.getElementById(nodeId);
    var klass = null;
    if(hNode)
    {
      var klass = hNode.className;
      if( ('visibleRow' == klass) || ('hiddenRow' == klass) )
      {
        retval.type = 'navTreeNode';
        retval.node = document.getElementById(nodeId + '_anchor');
      }
      else
      {
        var parent = hNode.parentNode;
        var pKlass = parent.className;
        if( ('activeNavTab' == pKlass) || ('inactiveNavTab' == pKlass) )
        {
          retval.type = 'navTab';
          retval.node = parent;
        }
        else if( ('fancyTabUp' == pKlass) || ('fancyTabDown' == pKlass) )
        {
          retval.type = 'fancyTab';
          retval.node = parent;
        }
        else
        {
          retval.type = 'navlink';
          retval.node = hNode;
        }
      }
    }
    return retval;
  }
  catch(error)
  {
    alert('Error caught in getHighlightNode(' + nodeId + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function setHighlight(nodeId)
{
  try
  {
    if(nodeId)
    {
      var nodeData = getHighlightNode(nodeId);
      if(nodeData.node)
      {
        var oldData = window['_oldNodeData'];
        if(oldData)
        {
          if(oldData.node)
          {
            if('navlink' == oldData.type)
            {
              oldData.node.className = 'inactiveAnchor';
            }
            else if('navTreeNode' == oldData.type)
            {
              oldData.node.className = 'inactiveAnchor';
            }
            else if('navTab' == oldData.type)
            {
              oldData.node.className = 'inactiveNavTab';
            }
            else if('fancyTab' == oldData.type)
            {
              selectFancyTab(oldData.node, false);
            }
            else alert('odt=' + oldData.type);
          }
        }
        if('navlink' == nodeData.type)
        {
          nodeData.node.className = 'activeAnchor';
        }
        else if('navTreeNode' == nodeData.type)
        {
          nodeData.node.className = 'activeAnchor';
        }
        else if('navTab' == nodeData.type)
        {
          nodeData.node.className = 'activeNavTab';
        }
        else if('fancyTab' == nodeData.type)
        {
          selectFancyTab(nodeData.node, true);
        }
        window._oldNodeData = nodeData;
      }
    }
  }
  catch(error)
  {
    alert('Error caught in setHighlight(' + nodeId + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

//Frame methods:

var rootFrameName = 'gtas_frame_root';

function getRootFrame()
{
  try
  {
    var f = window;
    while(f.name != rootFrameName)
    {
       f = f.parent;
       if(f == f.top) break;
    }
    if(f.name != rootFrameName)
    {
      return null;
    }
    else
    {
      return f;
    }
  }
  catch(error)
  {
	/* Sumedh (15 Nov 05) - netweaver porting: for now I slience the error because I don't know how to fix this error. 
	 * I make entire gtas loaded into new popup window and when close button is clicked, 
	 * the error appears.
	 */
    //alert('Error caught in getRootFrame();\ntype=' + error.type + '\nname=' + error.name);
    //throw error;
  }
}

function loadFrame(frameName, url)
{
  try
  {
    var frame = findFrame(frameName, getRootFrame() );
    if(frame == null)
    {
      alert('Unable to find frame named "' + frameName + '"');
    }
    else
    {
      var showUpdatingMsg = window['showUpdatingMsg'];
      if(showUpdatingMsg) showUpdatingMsg()

      var jalan = frame['jalan'];
      if(jalan)
      {
        var opConId = frame['jsOpConId'];
        if(opConId) url = appendParameterToUrl(url, 'rmoc', opConId);
        jalan(url);
      }
      else
      {
        frame.location.replace(url);
      }
    }
  }
  catch(error)
  {
    alert('Error caught in loadFrame(' + frameName + ',' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function findFrame(frameName, searchRoot)
{
  try
  {
    if(searchRoot == null) searchRoot = window.top;
    try
    {
      if(frameName == searchRoot.name)
      {
        return searchRoot;
      }
    }
    catch(error)
    {
      try
      {
        return searchRoot.parent[frameName];
      }
      catch(error1)
      {
        null;
      }

    }
    var length = searchRoot.frames.length;
    for(var i=0; i < length; i++)
    {
      var childFrame = searchRoot.frames[i]
      var retVal = findFrame(frameName, childFrame );
      if(retVal != null)
      {
        return retVal;
      }
    }
    return null;
  }
  catch(error)
  {
    alert('Error caught in findFrame(' + frameName + ',' + searchRoot + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function loadHeader(navgroupId, url, highlightId)
{
  try
  {
    var headerFrame = findFrame('gtas_header_frame');
    var headerNavgroupId = headerFrame['navgroupId'];
    if(navgroupId != headerNavgroupId)
    {
      if(highlightId) url = appendParameterToUrl(url,'highlightId',highlightId);
      loadFrame('gtas_header_frame',url);
    }
    else if(highlightId)
    {
      var highlightFunction = headerFrame['setHighlight'];
      if(highlightFunction)
      {
        headerFrame.setHighlight(highlightId);
      }
    }
  }
  catch(error)
  {
    alert('Error caught in loadHeader(' + navgroupId + ',' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }

}

function reloadHeader()
{
  try
  {
    var headerFrame = findFrame('gtas_header_frame');
    if(headerFrame)
    {
    	var highlightFunction = headerFrame['setHighlight'];
      if(highlightFunction)
      {
      	headerFrame.location = headerFrame.location;
      }
    }
  }
  catch(error)
  {
    alert('Error caught in reloadHeader();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }

}

function loadNav(navgroupId, url, highlightId)
{
  try
  {
    var navFrame = findFrame('gtas_nav_frame');
    var navNavgroupId = navFrame['navgroupId'];
    if(navgroupId != navNavgroupId)
    {
      if(highlightId) url = appendParameterToUrl(url,'highlightId',highlightId);
      navFrame.location.replace(url);
    }
    else if(highlightId)
    {
      var highlightFunction = navFrame['setHighlight'];
      if(highlightFunction)
      {
        navFrame.setHighlight(highlightId);
      }
    }
  }
  catch(error)
  {
    alert('Error caught in loadNav(' + navgroupId + ',' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

//NSL20061226 Recursive get the root frame
function getRecurRootFrame(win)
{
  var rootFrame;
  if (isChild(win))
  {
    var winOpener = win.opener;
    rootFrame = getRecurRootFrame(winOpener);
  }
  else
  {
    rootFrame = win.getRootFrame();
  }
  return rootFrame;
}

function getDetailViewRegistry(cleanup)
{
  if(cleanup == null)
  {
    cleanup = true;
  }
  try
  {
    // var rootFrame = getRootFrame();
    var rootFrame;
    //if(isChild(window))
    //{
    //  var winOpener = window.opener;
    //  rootFrame = winOpener.getRootFrame();
    //}
    //else
    //{
    //  rootFrame = getRootFrame();
    //}
    //NSL20061226
    rootFrame = getRecurRootFrame(window);
    
    if(rootFrame)
    {
      var detailViews = rootFrame['detailViews'];
      if(detailViews)
      {
        ;
      }
      else
      {
        rootFrame.detailViews = new Array();
        detailViews = rootFrame.detailViews;
      }
      if(cleanup == true) cleanupDetailViewRegistry(detailViews);
      return detailViews;
    }
  }
  catch(error)
  {
    alert('Error caught in getDetailViewRegistry(' + cleanup + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function registerPwl(pwl)
{
  try
  {
    if(pwl == null) return;
    var detailViews = getDetailViewRegistry(true);
    detailViews.pwl = pwl;
  }
  catch(error)
  {
    alert('Error caught in registerPwl(' + pwl + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function getPwl()
{
  try
  {
    var detailViews = getDetailViewRegistry(false);
    var pwl = detailViews['pwl'];
    if(pwl)
    {
      if(pwl.closed == false)
      {
        return pwl;
      }
      else
      {
        delete detailViews['pwl'];
      }
    }
    else
    {
      return null;
    }
  }
  catch(error)
  {
    alert('Error caught in getPwl();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function registerDetailView(dvWindow)
{
  try
  {
    if(dvWindow == null) return;
    var detailViews = getDetailViewRegistry(true);
	//TEST
	try
	{
		detailViews[detailViews.length] = dvWindow;
	} catch (error)
	{
	}
  }
  catch(error)
  {
    alert('Error caught in registerDetailView(' + dvWindow + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function cleanupDetailViewRegistry(detailViews)
{
  try
  {
    if(detailViews)
    {
      ;
    }
    else
    {
      detailViews = getDetailViewRegistry();
    }
    for(var i=0; i < detailViews.length; i++)
    {
      var dv = detailViews[i];
      if(dv)
      {
        if(dv.closed == true)
        {
          delete detailViews[i];
        }
      }
    }
    var pwl = detailViews['pwl'];
    if(pwl)
    {
      if(pwl.closed == true) delete detailViews.pwl;
    }
    var help = detailViews['help'];
    if(help)
    {
      if(help.closed == true) delete detailViews.help;
    }
  }
  catch(error)
  {
    alert('Error caught in cleanupDetailViewRegistry();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function closeDetailViews()
{
  try
  {
    var detailViews = getDetailViewRegistry(false);
    if(detailViews)
    {
      for(var i=0; i < detailViews.length; i++)
      {
        var dv = detailViews[i];
        if(dv)
        {
          if(dv.closed == false)
          {
            dv.rmocOnClose = false;
            if(dv['rmocUrl'])
            {
              if(dv['performRmoc'])
              {
                dv.performRmoc(dv.rmocUrl,true);
              }
            }
            dv.close();
          }
        }
      }
      var pwl = getPwl();
      if(pwl)
      {
        if(pwl.closed == false)
        {
          pwl.close();
        }
      }
      var help = getHelp();
      if(help)
      {
        if(help.closed == false)
        {
          help.close();
        }
      }
      cleanupDetailViewRegistry();
    }
  }
  catch(error)
  {
    alert('Error caught in closeDetailViews();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function isChild(win)
{
  retval = false;
  if(win)
  {
    var winOpener = win.opener;
    if(winOpener)
    {
      if(winOpener['getRootFrame'])
      {
        retval = true;
      }
    }
  }
  return retval;
}

function showInMaster()
{
  try
  {
    if(isChild(window))
    {
      var winOpener = window.opener;
      var thisUrl = window.location;
      var rootFrame = winOpener.getRootFrame();
      if(rootFrame)
      {
        rootFrame.location = thisUrl;
      }
    }
  }
  catch(error)
  {
    alert('Error caught in showInMaster();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function reloadNavFrame()
{
  try
  {
    var frame = findFrame('gtas_nav_frame', getRootFrame());
    frame.location = frame.location;
  }
  catch(error)
  {
    alert('Error caught in reloadNavFrame();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function openHelp(url)
{
  try
  {
    if(url == null) return;
    url = appendParameterToUrl(url, 'isDetailView','true');
    var win = null;
    try
    {
      win = getHelp();
      if(win)
      {
        win.location = url;
      }
      else
      {
        win = window.open(url,'helpWindow','scrollbars,status,width=640,height=480,resizable');
        registerHelp(win);
      }
    }
    catch(error0)
    {
      alert('openHelp() encountered an error calling window.open()');
      throw error0;
    }
    try
    {
      if(win) win.focus();
    }
    catch(error1)
    {
      ; //Ignore. IE throws 'unspecified error' here quite often!
    }
  }
  catch(error)
  {
    alert('Error caught in openHelp(' + url + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function registerHelp(help)
{
  try
  {
    if(help == null) return;
    var detailViews = getDetailViewRegistry(true);
    detailViews.help = help;
  }
  catch(error)
  {
    alert('Error caught in registerHelp(' + help + ');\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function getHelp()
{
  try
  {
    var detailViews = getDetailViewRegistry(false);
    var help = detailViews['help'];
    if(help)
    {
      if(help.closed == false)
      {
        return help;
      }
      else
      {
        delete detailViews['help'];
      }
    }
    else
    {
      return null;
    }
  }
  catch(error)
  {
    alert('Error caught in getHelp();\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}


// sumedh - netweaver porting 
// purpose: popup a new window
function openNewWindow(url)
{
	var newwindow=window.open(url,'gtas_frame_root','height=600,width=800');
	if (window.focus) {newwindow.focus()}
}

