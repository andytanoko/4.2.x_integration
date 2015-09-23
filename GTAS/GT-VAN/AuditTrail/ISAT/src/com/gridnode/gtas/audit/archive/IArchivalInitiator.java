/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchivalInitiator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

import java.util.Hashtable;

/**
 * To cater for different deploy scenario of the GTVAN module (either as a cluster mode or
 * single node mode), all the archive initiator in both env is implementing this interface.
 * 
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IArchivalInitiator
{
  /**
   * Init the archival process given the criteria.
   * @param criteria The archive criteria
   * @throws Exception
   */
  public void initArchive(Hashtable criteria) throws Exception;
  
  /**
   * Check on the on going status of a particular archival task.
   * @throws Exception
   */
  public void checkArchiveStatus() throws Exception;
}
