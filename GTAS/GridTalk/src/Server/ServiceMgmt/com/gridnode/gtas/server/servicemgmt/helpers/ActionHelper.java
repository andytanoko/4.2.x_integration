/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.server.servicemgmt.helpers;

import com.gridnode.gtas.model.servicemgmt.ServiceMgmtEntityFieldID;

import com.gridnode.pdip.app.servicemgmt.facade.ejb.IServiceMgmtManagerHome;
import com.gridnode.pdip.app.servicemgmt.facade.ejb.IServiceMgmtManagerObj;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Map;

/**
 * This Action class provides common services used by the action classes.
 *
 */
public class ActionHelper
{
  /**
   * Obtain the EJBObject for the ServiceMgmtManagerBean.
   *
   * @return The EJBObject to the ServiceMgmtManagerBean.
   * @exception ServiceLookupException Error in look up.
   */
  public static IServiceMgmtManagerObj getManager()
         throws ServiceLookupException
  {
    return (IServiceMgmtManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IServiceMgmtManagerHome.class.getName(),
      IServiceMgmtManagerHome.class,
      new Object[0]);
  }

  /**
   * Convert an WebService to Map object.
   *
   * @param WebService The WebService to convert.
   * @return A Map object converted from the specified WebService.
   */
  public static Map convertWebServiceToMap(WebService webService)
  {
    return WebService.convertToMap(
    webService,
    ServiceMgmtEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of WebService to Map objects.
   *
   * @param WebServiceList The collection of WebService to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of WebServices.
   */
  public static Collection convertWebServiceToMapObjects(Collection webServiceList)
  {
    return WebService.convertEntitiesToMap(
             (WebService[])webServiceList.toArray(
             new WebService[webServiceList.size()]),
             ServiceMgmtEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an ServiceAssignment to Map object.
   *
   * @param ServiceAssignment The ServiceAssignment to convert.
   * @return A Map object converted from the specified ServiceAssignment.
   */
  public static Map convertServiceAssignmentToMap(ServiceAssignment serviceAssignment)
  {
    return ServiceAssignment.convertToMap(
    serviceAssignment,
    ServiceMgmtEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of ServiceAssignment to Map objects.
   *
   * @param serviceAssignmentList The collection of ServiceAssignment to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ServiceAssignments.
   */
  public static Collection convertServiceAssignmentToMapObjects(Collection serviceAssignmentList)
  {
    return ServiceAssignment.convertEntitiesToMap(
             (ServiceAssignment[])serviceAssignmentList.toArray(
             new ServiceAssignment[serviceAssignmentList.size()]),
             ServiceMgmtEntityFieldID.getEntityFieldID(),
             null);
  }

}