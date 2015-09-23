/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerFunctionManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 * Jul 10 2003    Neo Sok Lay         Add findWorkflowActivityKeys(IDataFilter).
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException
 */
package com.gridnode.gtas.server.partnerfunction.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * Remote interface for PartnerFunctionManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public interface IPartnerFunctionManagerObj
  extends        EJBObject
{
  /**
   * Create a new PartnerFunction.
   *
   * @param partnerFunction The PartnerFunction entity.
   */
  public Long createPartnerFunction(PartnerFunction partnerFunction)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a PartnerFunction
   *
   * @param partnerFunction The PartnerFunction entity with changes.
   */
  public void updatePartnerFunction(PartnerFunction partnerFunction)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a PartnerFunction.
   *
   * @param partnerFunctionUId The UID of the PartnerFunction to delete.
   */
  public void deletePartnerFunction(Long partnerFunctionUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a PartnerFunction using the PartnerFunction UID.
   *
   * @param partnerFunctionUId The UID of the PartnerFunction to find.
   * @return The PartnerFunction found, or <B>null</B> if none exists with that
   * UID.
   */
  public PartnerFunction findPartnerFunction(Long partnerFunctionUid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a PartnerFunction using the PartnerFunctionId
   *
   * @param partnerFunctionId The Id of the PartnerFunction to find.
   * @return The PartnerFunction found, or <B>null</B> if none exists.
   */
  public PartnerFunction findPartnerFunction(String partnerFunctionId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of PartnerFunction that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of PartnerFunction found, or empty collection if none
   * exists.
   */
  public Collection findPartnerFunctions(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of PartnerFunction that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of PartnerFunction found, or empty collection if
   * none exists.
   */
  public Collection findPartnerFunctionsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Create a new WorkflowActivity.
   *
   * @param workflowActivity The WorkflowActivity entity.
   */
  public Long createWorkflowActivity(WorkflowActivity workflowActivity)
    throws CreateEntityException, SystemException, DuplicateEntityException, RemoteException;

  /**
   * Update a WorkflowActivity
   *
   * @param workflowActivity The WorkflowActivity entity with changes.
   */
  public void updateWorkflowActivity(WorkflowActivity workflowActivity)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a WorkflowActivity.
   *
   * @param workflowActivityUId The UID of the WorkflowActivity to delete.
   */
  public void deleteWorkflowActivity(Long workflowActivityUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a WorkflowActivity using the WorkflowActivity UID.
   *
   * @param workflowActivityUId The UID of the WorkflowActivity to find.
   * @return The WorkflowActivity found, or <B>null</B> if none exists with that
   * UID.
   */
  public WorkflowActivity findWorkflowActivity(Long workflowActivityUid)
    throws FindEntityException, SystemException, RemoteException;
    
  /**
   * Find a number of WorkflowActivity(s) that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of the WorkflowActivity(s) found, or empty collection if none
   * exists.
   */
  public Collection findWorkflowActivityKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;
    
}