function showException(traceId)
{
  try
  {
    var trace = document.getElementById(traceId);
    if(trace.style.display == 'none')
    {
      trace.style.display = 'block';
    }
    else
    {
      trace.style.display = 'none';
    }
  }
  catch(error)
  {
    alert('Error caught by showException()\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}

function nodeText(node)
{
  try
  {
    if(node == 'null')
    {
      return '';
    }
    if(node.nodeName == '#text')
    {
      return node.nodeValue;
    }
    else
    {
      var val = '';
      for(var i=0; i < node.childNodes.length; i++)
      {
        val = val + nodeText(node.childNodes[i]);
      }
      return val;
    }
  }
  catch(error)
  {
    alert('Error caught by childText()\ntype=' + error.type + '\nname=' + error.name);
    throw error;
  }
}