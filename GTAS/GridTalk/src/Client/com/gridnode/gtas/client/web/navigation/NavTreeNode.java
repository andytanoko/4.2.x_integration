/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavTreeNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-03     Andrew Hill         Created
 * 2003-03-27     Andrew Hill         Modified to extend Navlink
 * 2003-06-13     Andrew Hill         Allow node icon to be customised
 * 2003-06-24     Andrew Hill         Fix customisation bug, add hasChildren() convienience method
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.*;

import com.gridnode.gtas.client.utils.*;

public class NavTreeNode extends Navlink
{
  protected ArrayList _children = null;
  protected NavTreeNode _parent = null;
  protected boolean _initiallyExpanded;
  protected String _expanded = null;
  protected String _collapsed = null;

  public NavTreeNode()
  {
    _clear = true;
    _initiallyExpanded = false;
  }

  public String toString()
  {
    return "NavTreeNode[" + getId() + "," + _label + "," + _value + "]";
  }

  public void addChild(NavTreeNode child)
  {
    assertNotFrozen();
    if(_children == null) _children = new ArrayList(1);
    _children.add(child);
    child.setParent(this);
  }

  void setParent(NavTreeNode parent)
  {
    _parent = parent;
  }

  public NavTreeNode getParent()
  {
    return _parent;
  }

  public Iterator getChildren()
  {
    if(_children != null)
      return _children.iterator();
    else
      return null;
  }

  public int getChildCount()
  {
    if(_children == null)
      return 0;
    else
      return(_children.size() );
  }

  public boolean isLastChild()
  {
    if(_parent == null)
    {
      return true;
    }
    else
    {
      ArrayList siblings = _parent._children;
      if(siblings == null)
      {
        throw new NullPointerException("Internal assertion error: parent has no children");
      }
      int last = siblings.size() - 1;
      return(siblings.get(last) == this);
    }
  }

  public int getMaxChildDepth()
  {
    if(_children == null)
    {
      return 1;
    }
    else
    {
      int maxChildDepth = 0;
      Iterator iterator = _children.iterator();
      while(iterator.hasNext())
      {
        NavTreeNode child = (NavTreeNode)iterator.next();
        int childDepth = child.getMaxChildDepth();
        if(childDepth > maxChildDepth)
        {
          maxChildDepth = childDepth;
        }
      }
      return maxChildDepth + 1;
    }
  }

  public int getDepth()
  {
    if(_parent == null)
    {
      return 1;
    }
    else
    {
      return _parent.getDepth() + 1;
    }
  }

  public void freeze()
  {
    //hmmm... this method seems to be cut&paste a lot. Time to factor out into a superclass?
    super.freeze();
    Iterator i = getChildren();
    if(i != null)
    {
      while(i.hasNext())
      {
        ((FreezeableConfigObject)i.next()).freeze();
      }
    }
  }

  public void setClear(boolean removeOpContext)
  {
    assertNotFrozen();
    _clear = removeOpContext;
  }

  public boolean isClear()
  { return _clear; }

  public void setInitiallyExpanded(boolean initiallyExpanded)
  {
    assertNotFrozen();
    _initiallyExpanded = initiallyExpanded;
  }

  public void setIe(boolean initiallyExpanded)
  { //20030317AH - A less verbose name for setInitiallyExpanded!
    setInitiallyExpanded(initiallyExpanded);
  }

  public boolean isInitiallyExpanded()
  {
    return _initiallyExpanded;
  }
  
  public String getCollapsed()
  {
    if(_collapsed == null)
    {
      return getDefaultIcon(false);
    }
    return _collapsed;
  }

  public String getExpanded()
  {
    if(_expanded == null)
    {
      return _collapsed == null ? getDefaultIcon(true) : _collapsed;
    }
    return _expanded;
  }

  public void setCollapsed(String string)
  {
    _collapsed = string;
  }

  public void setExpanded(String string)
  {
    _expanded = string;
  }
  
  private String getDefaultIcon(boolean expanded)
  { //20030624AH
    if(_parent == null)
    {
      return expanded ? NavPageRenderer.ICON_TREE_MINUSROOTNODE : NavPageRenderer.ICON_TREE_PLUSROOTNODE;
    }
    else
    {
      if(hasChildren())
      {
        return expanded ? NavPageRenderer.ICON_TREE_EXPANDEDNODE : NavPageRenderer.ICON_TREE_NODE;
      }
      else
      {
        return NavPageRenderer.ICON_TREE_NODE;
      }
    }
  }
  
  public boolean hasChildren()
  { //20030624AH
    return getChildCount() > 0;
  }

}