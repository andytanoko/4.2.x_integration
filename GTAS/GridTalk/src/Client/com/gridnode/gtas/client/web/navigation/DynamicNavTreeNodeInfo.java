/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DynamicNavTreeNodeInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 * 2003-11-13     Daniel D'Cotta      Added rContext support
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.ArrayList;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.renderers.RenderingContext;

public abstract class DynamicNavTreeNodeInfo extends NavTreeNode
{
  protected RenderingContext _rContext;

  public abstract ArrayList getChildTreeNodes() throws GTClientException;
  
  public void setRenderingContext(RenderingContext rContext)
  {
    _rContext = rContext;
  }
  
  protected NavTreeNode createNavTreeNode(String id, String value, String label, String collapsed)
  {
    NavTreeNode navTreeNode = new NavTreeNode();
    navTreeNode.setId(id);
    navTreeNode.setValue(value);
    navTreeNode.setLabel(label);
    navTreeNode.setCollapsed(collapsed);
    return navTreeNode;
  }
}