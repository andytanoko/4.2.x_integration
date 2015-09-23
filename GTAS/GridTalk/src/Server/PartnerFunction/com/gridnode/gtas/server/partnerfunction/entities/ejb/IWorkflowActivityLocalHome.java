/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IWorkflowActivityLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 03 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * LocalHome interface for WorkflowActivityBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IWorkflowActivityLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new WorkflowActivity
   *
   * @param workflowActivity The WorkflowActivity entity.
   * @return EJBLocalObject as a proxy to WorkflowActivityBean for the created
   *         WorkflowActivity.
   */
  public IWorkflowActivityLocalObj create(IEntity workflowActivity)
    throws CreateException;

  /**
   * Load a WorkflowActivityBean
   *
   * @param primaryKey The primary key to the WorkflowActivity record
   * @return EJBLocalObject as a proxy to the loaded WorkflowActivityBean.
   */
  public IWorkflowActivityLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find WorkflowActivity records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IWorkflowActivityLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}