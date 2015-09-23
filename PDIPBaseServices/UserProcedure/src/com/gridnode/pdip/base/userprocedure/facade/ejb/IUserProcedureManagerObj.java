/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserProcedureManagerObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 * Jul 15 2003    Neo Sok Lay             Add methods:
 *                                        getProcedureDefFileKeys(IDataFilter),
 *                                        getUserProcedureKeys(IDataFilter)
 * Oct 17 2005    Neo Sok Lay             For J2EE compliance: all remote methods
 *                                        must throw RemoteException.                                       
 */
package com.gridnode.pdip.base.userprocedure.facade.ejb;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;


public interface IUserProcedureManagerObj extends EJBObject
{

  /**
   * Create a new User Procedure.
   *
   * @param userProcedure The User Procedure entity.
   * @return The UID of the created entity.
   */
  public Long createUserProcedure(UserProcedure userProcedure)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a User Procedure.
   *
   * @param userProcedure The User Procedure entity with changes.
   */
  public void updateUserProcedure(UserProcedure userProcedure)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a User Procedure.
   *
   * @param userProcedureUID The UID of the User Procedure to delete.
   */
  public void deleteUserProcedure(Long uID)
    throws DeleteEntityException, SystemException, RemoteException;


  /**
   * To retrieve a <code>UserProcedure</code> entity with the specified uId.
   */
  public UserProcedure getUserProcedure(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>UserProcedure</code> entity with the specified filter.
   */
  public Collection getUserProcedure(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of the <code>UserProcedure</code> entities with the specified filter.
   */
  public Collection getUserProcedureKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Create a new ProcedureDefinition File.
   *
   * @param procedureDefFile The ProcedureDefinition File entity.
   * @return The UID of the created ProcedureDefinition File.
   */
  public Long createProcedureDefinitionFile(ProcedureDefFile procedureDefFile)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a ProcedureDefinition File.
   *
   * @param procedureDef The ProcedureDefinition File entity with changes.
   */
  public void updateProcedureDefinitionFile(ProcedureDefFile procedureDefFile)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a ProcedureDefinition File.
   *
   * @param procedureDefFileUId The UID of the  ProcedureDefinition File to delete.
   */
  public void deleteProcedureDefinitionFile(Long uID)
    throws DeleteEntityException, SystemException, RemoteException;


  /**
   * To retrieve a <code>ProcedureDefFile</code> entity with the specified uId.
   */
  public ProcedureDefFile getProcedureDefinitionFile(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>ProcedureDefFile</code> entity with the specified filter.
   */
  public Collection getProcedureDefinitionFile(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of the <code>ProcedureDefFile</code> entities with the specified filter.
   */
  public Collection getProcedureDefFileKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   *
   * @param paramMap - Map of Key-Values - Values are the Values used as Parameter Values for UserProcedure.
   * @param userProcedure - UserProcedure Entity.
   * @return - Object - Value after Executing the UserProcedure.
   * @throws UserProcedureExecutionException - thrown when UserProcedure cannot be executed.
   * @throws SystemException.
   * @throws RemoteException - EJB Spec- RemoteException.
   */
  public Object execute(Vector paramDefVect,UserProcedure userProcedure)
    throws UserProcedureExecutionException,SystemException,RemoteException;

  /**
   * Retrieve methods details from WSDL.
   *
   * @param wsdlURL the URL to the WSDL.
   */
  public Collection getMethodDetailsFromWSDL(Long wsdlUid) throws Exception, RemoteException;

}


