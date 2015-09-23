/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveInitiatorFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

import com.gridnode.gtas.audit.archive.cluster.ClusterArchivalInitiator;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;

/**
 * This class help to determine which archival initiator to be used in cluster env or single node mode.
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class ArchiveInitiatorFactory
{
  private static ArchiveInitiatorFactory _factory = new ArchiveInitiatorFactory();
  
  private ArchiveInitiatorFactory()
  {
    
  }
  
  public static ArchiveInitiatorFactory getInstance()
  {
    return _factory;
  }
  
  /**
   * Get the appropriate archival initiator given the setup of the env. 
   * @return the archival initiator
   */
  public IArchivalInitiator getArchivalInitiator()
  {
    if(ArchiveActivityHelper.isEnabledClustered())
    {
      return new ClusterArchivalInitiator();
    }
    else
    {
      return ArchivalInitiator.getInstance();
    }
  }
}
