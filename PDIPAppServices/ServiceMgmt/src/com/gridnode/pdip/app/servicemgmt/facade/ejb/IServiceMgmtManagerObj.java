/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IServiceMgmtManagerObj.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 * Oct 17 2005    Neo Sok Lay         Temp remove implement IServiceMgmtManager
 */
package com.gridnode.pdip.app.servicemgmt.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

//import com.gridnode.pdip.app.servicemgmt.facade.IServiceMgmtManager;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

public interface IServiceMgmtManagerObj extends EJBObject //,IServiceMgmtManager
{
  /**
   * Create a new WebService.
   *
   * @param webService The WebService entity.
   * @return The UID of the created WebService
   */
  public Long createWebService(WebService webService)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a WebService.
   *
   * @param webService The WebService entity with changes.
   */
  public void updateWebService(WebService webService)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a WebService.
   *
   * @param uid The UID of the WebService to delete.
   */
  public void deleteWebService(Long uid)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Find a WebService using the WebService UID.
   *
   * @param uID The UID of the WebService to find.
   * @return The WebService found, or <B>null</B> if none exists with that
   * UID.
   */
  public WebService findWebService(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of WebServices using a filtering condition
   *
   * @param filter The filtering condition of the WebServices to find.
   * @return A Collection of WebServices found, or empty collection if none
   * exists.
   */
  public Collection findWebServices(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the WebServices that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of WebServices found, or empty
   * collection if none.
   */
  public Collection findWebServicesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Create a new ServiceAssignment.
   *
   * @param assignment The ServiceAssignment entity.
   * @return The UID of the created ServiceAssignment
   */
  public Long createServiceAssignment(ServiceAssignment assignment)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a ServiceAssignment.
   *
   * @param assignment The ServiceAssignment entity with changes.
   */
  public void updateServiceAssignment(ServiceAssignment assignment)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Find a ServiceAssignment using the ServiceAssignment UID.
   *
   * @param uID The UID of the ServiceAssignment to find.
   * @return The ServiceAssignment found
   * @throws FindEntityException No ServiceAssignment found with the specified UID
   * @throws SystemException Unexpected system error.
   */
  public ServiceAssignment findServiceAssignment(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a ServiceAssignment using the ServiceAssignment UserName and UserType.
   *
   * @param userName The UserName of the ServiceAssignment to find.
   * @param userType The UserType of the ServiceAssignment to find.
   * @return The ServiceAssignment found, or <B>null</B> if none exists with that
   * userName and userType.
   */
  public ServiceAssignment findServiceAssignment(String userName, String userType)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of ServiceAssignment(s) using a filtering condition
   *
   * @param filter The filtering condition of the ServiceAssignment(s) to find.
   * @return A Collection of ServiceAssignment(s) found, or empty collection if none
   * exists.
   */
  public Collection findServiceAssignments(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the ServiceAssignment(s) that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of ServiceAssignment(s) found, or empty
   * collection if none.
   */
  public Collection findServiceAssignmentKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Delete a ServiceAssignment. 
   *
   * @param uId The UID of the ServiceAssignment to delete.
   */
  public void deleteServiceAssignment(Long uId)
    throws DeleteEntityException, SystemException, RemoteException;
 
  /**
   * Find a WebService base on the Service Name, that is assigned to the specified user.
   *
   * @param userName The User name
   * @param userType The User Type 
   * @param serviceName The Name of the WebService to find.
   * @param serviceGroup The Group to which this service belongs.
   * @return The WebService found and is assigned to the specified user, or <B>null</B> if none exists with that
   * serviceName or not assigned to the specified user.
   */
  public WebService findAssignedWebService(String userName, String userType, String serviceName,String serviceGroup)
    throws FindEntityException, SystemException, RemoteException;
    
  /**
   * Find a WebService base on the Service Name, that is assigned to the specified user.
   * serviceGroup value is taken as INTERNAL
   * 
   * @param userName The User name
   * @param userType The User Type 
   * @param serviceName The Name of the WebService to find.
   * @return The WebService found and is assigned to the specified user, or <B>null</B> if none exists with that
   * serviceName or not assigned to the specified user.
   */
  public WebService findAssignedWebService(String userName, String userType, String serviceName)
    throws FindEntityException, SystemException, RemoteException;   
}
