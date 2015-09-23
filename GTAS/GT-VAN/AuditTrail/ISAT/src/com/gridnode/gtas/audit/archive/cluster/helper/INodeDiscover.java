/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INodeDiscover.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster.helper;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface INodeDiscover
{
  /**
   * Get the total number of node within the cluster
   * @return 0 if no cluster is setup
   */
  public int getAvailableNodes() throws ArchiveTrailDataException;
}
