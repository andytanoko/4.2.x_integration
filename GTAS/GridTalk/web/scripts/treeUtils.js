var ICON_TREE_BLANK           = "images/tree/blank.gif";
var ICON_TREE_ANSBRANCH       = "images/tree/ansBranch.gif";
var ICON_TREE_LASTCHILDBRANCH = "images/tree/lastChildBranch.gif";
var ICON_TREE_MIDCHILDBRANCH  = "images/tree/midChildBranch.gif";
var ICON_TREE_PLUSNODE        = "images/tree/plusNode.gif";
var ICON_TREE_MINUSNODE       = "images/tree/minusNode.gif";
var ICON_TREE_PLUSROOTNODE    = "images/tree/plusRootNode.gif";
var ICON_TREE_MINUSROOTNODE   = "images/tree/minusRootNode.gif";
var ICON_TREE_NODE            = "images/tree/node.gif";

var ICON_TREE_MIDCHILDPLUS    = "images/tree/midChildPlus.gif";
var ICON_TREE_MIDCHILDMINUS   = "images/tree/midChildMinus.gif";
var ICON_TREE_LASTCHILDPLUS  = "images/tree/lastChildPlus.gif";
var ICON_TREE_LASTCHILDMINUS = "images/tree/lastChildMinus.gif";
  
var ICON_TREE_EXPANDEDNODE    = "images/tree/expandedNode.gif";

function NavTreeNode(id, parentNode, initiallyExpanded, expandedIconUrl, collapsedIconUrl)
{
  try
  {
    this.id = id;
    this.parent = parentNode;
    this.children = new Array();
    this.expanded = initiallyExpanded;
    this.expandedIcon = expandedIconUrl ? expandedIconUrl : ICON_TREE_EXPANDEDNODE;
    this.collapsedIcon = collapsedIconUrl ? collapsedIconUrl : ICON_TREE_NODE;

    if(parentNode != null)
    {
      parentNode.children[parentNode.children.length] = this;
    }
  }
  catch(error)
  {
    alert('Error caught by NavTreeNode() [constructor]\ntype=' + error.type + '\nname=' + error.name
    + '\nNavTreeNode.id=' + id);
    throw error;
  }
}

NavTreeNode.prototype.childCount = function()
{
  return this.children.length;
}

NavTreeNode.prototype.hasChildren = function()
{
  return (this.children.length > 0);
}

NavTreeNode.prototype.isLastChild = function()
{
  if(this.parent == null)
  {
    return true;
  }
  else
  {
    var siblings = this.parent.children;
    return(siblings[ siblings.length - 1 ] === this);
  }
}

NavTreeNode.prototype.showChildren = function(showChildNodes)
{
  for(var i=0; i < this.children.length; i++)
  {
    var child = this.children[i];
    var rowNode = document.getElementById(child.id);
    rowNode.className = (showChildNodes && this.expanded) ? 'visibleRow' : 'hiddenRow';
    if(child.expanded)
    {
      child.showChildren(showChildNodes);
    }
    else
    {
      child.showChildren(false);
    }
  }
}


NavTreeNode.prototype.setExpanded = function(expand)
{
  this.expanded = expand;
  var iconNode = document.getElementById(this.id + '_icon');
  var branchIconNode = document.getElementById(this.id + '_branch');
  if(this.parent == null)
  {
    branchIconNode.setAttribute('src',this.expanded ? ICON_TREE_MINUSROOTNODE : ICON_TREE_PLUSROOTNODE);
  }
  else
  {
    if(this.isLastChild())
    {
      branchIconNode.setAttribute('src',this.expanded ? ICON_TREE_LASTCHILDMINUS : ICON_TREE_LASTCHILDPLUS);      
    }
    else
    {
      branchIconNode.setAttribute('src',this.expanded ? ICON_TREE_MIDCHILDMINUS : ICON_TREE_MIDCHILDPLUS);
    }
    iconNode.setAttribute('src',this.expanded ? this.expandedIcon : this.collapsedIcon);
  }
  this.showChildren(expand);
}

NavTreeNode.prototype.toggleExpanded = function()
{
  this.setExpanded( !this.expanded );
}

NavTreeNode.prototype.dump = function()
{
  var parentId = null;
  if(this.parent != null) parentId = this.parent.id;
  alert('id=' + this.id + ' parent=' + parentId + ' expanded=' + this.expanded);
  for(var i=0; i < this.children.length; i++)
  {
    this.children[i].dump();
  }
}

NavTreeNode.prototype.createChild = function(id)
{
  var newNode = new NavTreeNode(id, this);
  return newNode;
}

NavTreeNode.prototype.getNode = function(id)
{
  if(this.id == id)
  {
    return this;
  }
  else
  {
    for(var i=0; i < this.children.length; i++)
    {
      var result = this.children[i].getNode(id);
      if(result != null) return result;
    }
  }
  return null;
}
