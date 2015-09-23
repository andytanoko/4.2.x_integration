/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Add findWorkflowActivityKeys(IDataFilter)
 */
package com.gridnode.gtas.server.partnerfunction.facade.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.partnerfunction.entities.ejb.IPartnerFunctionLocalObj;
import com.gridnode.gtas.server.partnerfunction.entities.ejb.IWorkflowActivityLocalObj;
import com.gridnode.gtas.server.partnerfunction.helpers.Logger;
import com.gridnode.gtas.server.partnerfunction.helpers.PartnerFunctionEntityHandler;
import com.gridnode.gtas.server.partnerfunction.helpers.WorkflowActivityEntityHandler;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

public class PartnerFunctionManagerBean
       implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1239475212181313635L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in IPartnerFunctionManagerObj

  // ********************* Methods for PartnerFunction

  /**
   * Create a new PartnerFunction.
   *
   * @param partnerFunction The PartnerFunction entity.
   */
  public Long createPartnerFunction(PartnerFunction partnerFunction)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[PartnerFunctionManagerBean.createPartnerFunction] Enter");

    try
    {
      IPartnerFunctionLocalObj obj =
      (IPartnerFunctionLocalObj)getPartnerFunctionEntityHandler().create(partnerFunction);

      Logger.log("[PartnerFunctionManagerBean.createPartnerFunction] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.createPartnerFunction] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.createPartnerFunction] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.createPartnerFunction] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.createPartnerFunction(PartnerFunction) Error ",
        ex);
    }
  }

  /**
   * Update a PartnerFunction
   *
   * @param partnerFunction The PartnerFunction entity with changes.
   */
  public void updatePartnerFunction(PartnerFunction partnerFunction)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[PartnerFunctionManagerBean.updatePartnerFunction] Enter");

    try
    {
      getPartnerFunctionEntityHandler().update(partnerFunction);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.updatePartnerFunction] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.updatePartnerFunction] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.updatePartnerFunction(PartnerFunction) Error ",
        ex);
    }

    Logger.log("[PartnerFunctionManagerBean.updatePartnerFunction] Exit");
  }

  /**
   * Delete a PartnerFunction.
   *
   * @param partnerFunctionUId The UID of the PartnerFunction to delete.
   */
  public void deletePartnerFunction(Long partnerFunctionUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[PartnerFunctionManagerBean.deletePartnerFunction] Enter");

    try
    {
      getPartnerFunctionEntityHandler().remove(partnerFunctionUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.deletePartnerFunction] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.deletePartnerFunction] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.deletePartnerFunction] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.deletePartnerFunction(partnerFunctionUId) Error ",
        ex);
    }

    Logger.log("[PartnerFunctionManagerBean.deletePartnerFunction] Exit");
  }

  /**
   * Find a PartnerFunction using the PartnerFunction UID.
   *
   * @param partnerFunctionUId The UID of the PartnerFunction to find.
   * @return The PartnerFunction found, or <B>null</B> if none exists with that
   * UID.
   */
  public PartnerFunction findPartnerFunction(Long partnerFunctionUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerFunctionManagerBean.findPartnerFunction] UID: "+
      partnerFunctionUid);

    PartnerFunction partnerFunction = null;

    try
    {
      partnerFunction =
        (PartnerFunction)getPartnerFunctionEntityHandler().
          getEntityByKeyForReadOnly(partnerFunctionUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunction] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunction] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunction] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.findPartnerFunction(partnerFunctionUId) Error ",
        ex);
    }

    return partnerFunction;
  }

  /**
   * Find a PartnerFunction using the PartnerFunctionId
   *
   * @param partnerFunctionId The Id of the PartnerFunction to find.
   * @return The PartnerFunction found, or <B>null</B> if none exists.
   */
  public PartnerFunction findPartnerFunction(String partnerFunctionId)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerFunctionManagerBean.findPartnerFunction] PartnerFunctionId: "
      +partnerFunctionId);

    PartnerFunction partnerFunction = null;

    try
    {
      partnerFunction =
        (PartnerFunction)getPartnerFunctionEntityHandler().
          findByPartnerFunctionId(partnerFunctionId);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunction] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunction] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunction] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.findPartnerFunction(partnerFunctionId) Error ",
        ex);
    }

    return partnerFunction;
  }

  /**
   * Find a number of PartnerFunction that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of PartnerFunction found, or empty collection if none
   * exists.
   */
  public Collection findPartnerFunctions(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerFunctionManagerBean.findPartnerFunctions] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection partnerFunctions = null;
    try
    {
      partnerFunctions =
        getPartnerFunctionEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunctions] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunctions] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunctions] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.findPartnerFunctions(filter) Error ",
        ex);
    }

    return partnerFunctions;
  }

  /**
   * Find a number of PartnerFunction that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of PartnerFunction found, or empty collection if
   * none exists.
   */
  public Collection findPartnerFunctionsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerFunctionManagerBean.findPartnerFunctionsKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection partnerFunctionsKeys = null;
    try
    {
      partnerFunctionsKeys =
        getPartnerFunctionEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunctionsKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunctionsKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findPartnerFunctionsKeys] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.findPartnerFunctionsKeys(filter) Error ",
        ex);
    }

    return partnerFunctionsKeys;
  }

  // ********************* Methods for WorkflowActivity

  /**
   * Create a new WorkflowActivity.
   *
   * @param workflowActivity The WorkflowActivity entity.
   */
  public Long createWorkflowActivity(WorkflowActivity workflowActivity)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[WorkflowActivityManagerBean.createWorkflowActivity] Enter");

    try
    {
      IWorkflowActivityLocalObj obj =
      (IWorkflowActivityLocalObj)getWorkflowActivityEntityHandler().create(workflowActivity);

      Logger.log("[WorkflowActivityManagerBean.createWorkflowActivity] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.createWorkflowActivity] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.createWorkflowActivity] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.createWorkflowActivity] Error ", ex);
      throw new SystemException(
        "WorkflowActivityManagerBean.createWorkflowActivity(WorkflowActivity) Error ",
        ex);
    }
  }

  /**
   * Update a WorkflowActivity
   *
   * @param workflowActivity The WorkflowActivity entity with changes.
   */
  public void updateWorkflowActivity(WorkflowActivity workflowActivity)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[WorkflowActivityManagerBean.updateWorkflowActivity] Enter");

    try
    {
      getWorkflowActivityEntityHandler().update(workflowActivity);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.updateWorkflowActivity] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.updateWorkflowActivity] Error ", ex);
      throw new SystemException(
        "WorkflowActivityManagerBean.updateWorkflowActivity(WorkflowActivity) Error ",
        ex);
    }

    Logger.log("[WorkflowActivityManagerBean.updateWorkflowActivity] Exit");
  }

  /**
   * Delete a WorkflowActivity.
   *
   * @param workflowActivityUId The UID of the WorkflowActivity to delete.
   */
  public void deleteWorkflowActivity(Long workflowActivityUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[WorkflowActivityManagerBean.deleteWorkflowActivity] Enter");

    try
    {
      getWorkflowActivityEntityHandler().remove(workflowActivityUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.deleteWorkflowActivity] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.deleteWorkflowActivity] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.deleteWorkflowActivity] Error ", ex);
      throw new SystemException(
        "WorkflowActivityManagerBean.deleteWorkflowActivity(workflowActivityUId) Error ",
        ex);
    }

    Logger.log("[WorkflowActivityManagerBean.deleteWorkflowActivity] Exit");
  }

  /**
   * Find a WorkflowActivity using the WorkflowActivity UID.
   *
   * @param workflowActivityUId The UID of the WorkflowActivity to find.
   * @return The WorkflowActivity found, or <B>null</B> if none exists with that
   * UID.
   */
  public WorkflowActivity findWorkflowActivity(Long workflowActivityUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[WorkflowActivityManagerBean.findWorkflowActivity] UID: "+
      workflowActivityUid);

    WorkflowActivity workflowActivity = null;

    try
    {
      workflowActivity =
        (WorkflowActivity)getWorkflowActivityEntityHandler().
          getEntityByKeyForReadOnly(workflowActivityUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.findWorkflowActivity] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.findWorkflowActivity] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[WorkflowActivityManagerBean.findWorkflowActivity] Error ", ex);
      throw new SystemException(
        "WorkflowActivityManagerBean.findWorkflowActivity(workflowActivityUId) Error ",
        ex);
    }

    return workflowActivity;
  }

  /**
   * Find a number of WorkflowActivity(s) that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of the WorkflowActivity(s) found, or empty collection if none
   * exists.
   */
  public Collection findWorkflowActivityKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerFunctionManagerBean.findWorkflowActivityKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection workflowActivities = null;
    try
    {
      workflowActivities =
        getWorkflowActivityEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findWorkflowActivityKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findWorkflowActivityKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerFunctionManagerBean.findWorkflowActivityKeys] Error ", ex);
      throw new SystemException(
        "PartnerFunctionManagerBean.findWorkflowActivityKeys(filter) Error ",
        ex);
    }

    return workflowActivities;
  }
  
  // ********************* Methods for EntityHandler

  private PartnerFunctionEntityHandler getPartnerFunctionEntityHandler()
  {
     return PartnerFunctionEntityHandler.getInstance();
  }

  private WorkflowActivityEntityHandler getWorkflowActivityEntityHandler()
  {
     return WorkflowActivityEntityHandler.getInstance();
  }
}