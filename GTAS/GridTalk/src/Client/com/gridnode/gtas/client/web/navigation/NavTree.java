/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavTree.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-03     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.IdentifiedBean;

public class NavTree extends IdentifiedBean
{
  protected NavTreeNode _rootNode;

  public NavTree()
  {
    
  }

  public String toString()
  {
    return "NavTree[" + getId() + "]";
  }

  public void freeze()
  {
    super.freeze();
    if(_rootNode == null) throw new NullPointerException("No rootNode setup for NavTree:" + getId());
    _rootNode.freeze();
  }

  public void addChild(NavTreeNode child)
  {
    assertNotFrozen();
    if(_rootNode == null)
    {
      _rootNode = child;
    }
    else
    {
      throw new java.lang.IllegalStateException("Root node of navtree '"
                                                + this.getId() + "' already set to navtreenode '"
                                                + _rootNode.getId() +"'");
    }
  }

  public NavTreeNode getTree()
  {
    return _rootNode;
  }
}