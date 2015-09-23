/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NodeDiscoverFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster.helper;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class NodeDiscoverFactory
{
  public static final NodeDiscoverFactory _factory = new NodeDiscoverFactory();
  
  private NodeDiscoverFactory()
  {
    
  }
  
  public static NodeDiscoverFactory getInstance()
  {
    return _factory;
  }
  
  //For future enhancement to support other AS
  public INodeDiscover getNodeDiscover()
  {
    return JbossNodeDiscover.getInstance();
  }
}
