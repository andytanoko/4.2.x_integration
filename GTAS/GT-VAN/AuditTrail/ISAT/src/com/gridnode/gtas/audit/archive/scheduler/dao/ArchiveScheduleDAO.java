/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveScheduleDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 12, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.scheduler.dao;

import java.util.Collection;
import java.util.Date;

import com.gridnode.gtas.audit.archive.scheduler.model.ArchiveScheduler;
import com.gridnode.util.db.DAO;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveScheduleDAO extends DAO
{
  public ArchiveScheduleDAO()
  {
    super(false);
  }
  
  public ArchiveScheduleDAO(boolean newSession)
  {
    super(newSession);
  }
  
  /**
   * Get a list of ArchiveScheduler record that fall within the given date range based on 
   * the ArchiveScheduler's nextRuntime
   * @param startDate 
   * @param endDate
   * @return
   */
  public Collection<ArchiveScheduler> getArchiveScheduleTask(Date startDate, Date endDate, boolean isActive)
  {
    String queryName = getEntityClass().getName()+".getArchiveScheduler";
    String[] paramName = new String[]{"startDate", "endDate", "isActive"};
    Object[] paramValues = new Object[]{startDate, endDate, isActive};
    return query(queryName, paramName, paramValues); 
  }
  
  private Class getEntityClass()
  {
    return ArchiveScheduler.class;
  }
}
