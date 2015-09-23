/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRNProcessDefManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 14 2003    Neo Sok Lay         Add method: 
 *                                    findProcessDefsByProcessAct(IDataFilter)
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface must throw java.rmi.RemoteException
 *                                    - The business method deleteProcessDef does not throw java.rmi.RemoteException
 *                                    - The business method findProcessDefsKeys does not throw java.rmi.RemoteException
 *                                    - The business method findProcessDefs does not throw java.rmi.RemoteException                                   
 */
package com.gridnode.pdip.app.rnif.facade.ejb;

import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import java.rmi.RemoteException;
import java.util.Collection;

public interface IRNProcessDefManager
{
  /**
   * Create a new ProcessDef.
   *
   * @param def The ProcessDef entity.
   * @return The UID of the created ProcessDef
   */
  public Long createProcessDef(ProcessDef def) throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a ProcessDef.
   *
   * @param def The ProcessDef entity with changes.
   */
  public void updateProcessDef(ProcessDef def) throws UpdateEntityException, SystemException, RemoteException;

  /**
  * Delete a ProcessDef.
  *
  * @param defUId The UID of the ProcessDef to delete.
  */
  public void deleteProcessDef(Long defUId) throws DeleteEntityException, SystemException, RemoteException;

  /**
  * Find a ProcessDef using the ProcessDef UID.
  *
  * @param uID The UID of the ProcessDef to find.
  * @return The ProcessDef found, or <B>null</B> if none exists with that
  * UID.
  */
  public ProcessDef findProcessDef(Long uID) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a ProcessDef using the ProcessDef UID.
   *
   * @param defName The name of  the ProcessDef to find.
   * @return The ProcessDef found, or <B>null</B> if none exists with that
   * UID.
   */
  public ProcessDef findProcessDefByName(String defName) throws FindEntityException, SystemException, RemoteException;

  /**
  * Find a ProcessDef using the ProcessDef ID and Enterprise ID.
  * @param defName The name of  the ProcessDef to find.  *
  * @return The UID of the ProcessDef found, or <B>null</B> if none
  * exists with the specified inputs.
  */
  public Long findProcessDefKeyByName(String defName) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the ProcessDefs that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of ProcessDefs found, or empty
   * collection if none.
   * @excetpion FindProcessDefException Error in executing the finder.
   */
  public Collection findProcessDefsKeys(IDataFilter filter) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ProcessDefs that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of ProcessDefs found, or empty collection if none
   * exists.
   */
  public Collection findProcessDefs(IDataFilter filter) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ProcessDefs whose ResponseAct or RequestAct satisfy the
   * specified filter condition.
   * 
   * @param filter The Filtering condition on ProcessAct entity.
   * @return Collection of ProcessDef entities found.
   */
  public Collection findProcessDefsByProcessAct(IDataFilter filter) throws FindEntityException, SystemException, RemoteException;
  
}