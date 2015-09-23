/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveServiceMBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 4, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

import java.util.Date;
import java.util.Hashtable;

import org.jboss.system.ServiceMBean;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface ArchiveServiceMBean extends ServiceMBean
{
  public void checkArchiveStatus() throws Exception;
  
/**
   * Get the last activate time of the MBean. 
   * @return
   */
  public Date getLastActivateTime();
}
