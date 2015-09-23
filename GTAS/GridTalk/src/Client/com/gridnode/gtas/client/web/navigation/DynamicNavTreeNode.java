/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DynamicNavTreeNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 * 2003-11-13     Daniel D'Cotta      Added rContext support
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.renderers.RenderingContext;

public class DynamicNavTreeNode extends NavTreeNode
{
  protected DynamicNavTreeNodeInfo _navTreeNodeInfo = null;
  
  public DynamicNavTreeNode()
  {
    super();
  }

  public String toString()
  {
    return "DynamicNavTreeNode[" + getId() + "," + _label + "," + _value + "," + _navTreeNodeInfo.getClass() + "]";
  }
  
  public void setInfoClass(String infoClass) throws GTClientException
  {
    try
    {
      _navTreeNodeInfo = (DynamicNavTreeNodeInfo)Class.forName(infoClass).newInstance();
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to create object, infoClass=" + infoClass, t);
    }    
  }
  
  public void initDynamicChildNodes(RenderingContext rContext) throws GTClientException
  {
    _navTreeNodeInfo.setRenderingContext(rContext);

    // also set up the dynamic children
    _children = _navTreeNodeInfo.getChildTreeNodes();
    for(int i = 0; i < _children.size(); i++)
    {
      ((NavTreeNode)_children.get(i)).setParent(this);    
    }
  }
}