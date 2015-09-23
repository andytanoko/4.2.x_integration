/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * May 31, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.gridnode.pdip.app.servicemgmt.facade.ejb.IServiceMgmtManagerHome;
import com.gridnode.pdip.app.servicemgmt.facade.ejb.IServiceMgmtManagerObj;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This entity dependent checker checks for entity dependency relationships managed at
 * APP ServiceMgmt module.<p>
 * The following dependencies are currently checked:<p>
 * <PRE>
 * ServiceAssignment - dependent on Partner
 * ServiceAssignment - dependent on WebService
 * </PRE>
 * 
 */
public class EntityDependencyChecker
{
  /**
   * Constructor for EntityDependencyChecker.
   */
  public EntityDependencyChecker()
  {

  }
  
  /**
   * Checks whether there are dependent ServiceAssignments on the specified Partner.
   * 
   * @param partnerId The PartnerID of the Partner.
   * @return A Set of ServiceAssignment entities that are dependent on the Partner, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentServiceAssignmentsForPartner(String partnerId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ServiceAssignment.USER_NAME,filter.getEqualOperator(), partnerId, false);
      filter.addSingleFilter(filter.getAndConnector(), ServiceAssignment.USER_TYPE,filter.getEqualOperator(), ServiceAssignment.PARTNER_TYPE, false);
      Collection serviceAssignmentColl = getServiceMgmtManager().findServiceAssignments(filter);
      dependents = new HashSet(serviceAssignmentColl);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentServiceAssignmentsForPartner] Error", t);
    }
    
    return dependents;
  }

  /**
   * Checks whether there are dependent ServiceAssignment on the specified WebService.
   * 
   * @param webServiceUId The UID of the WebService.
   * @return A Set of ServiceAssignment entities that are dependent on the WebService, or
   * empty set if no dependencies, or <b>null</b> if error occurs during the check.
   */
  public Set checkDependentServiceAssignmentsForWebService(Long webServiceUId)
  {
    Set dependents = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ServiceAssignment.WEBSERVICE_UIDS,filter.getLikeOperator(),"%;"+webServiceUId+";" , false);
      Collection serviceAssignmentColl = getServiceMgmtManager().findServiceAssignments(filter);
      dependents = new HashSet(serviceAssignmentColl);
    }
    catch (Throwable t)
    {
      Logger.warn("[EntityDependencyChecker.checkDependentServiceAssignmentsForWebService] Error", t);
    }
    
    return dependents;
  }



  /**
   * Obtain the EJBObject for the ServiceMgmtManagerBean.
   *
   * @return The EJBObject to the ServiceMgmtManagerBean.
   * @exception ServiceLookupException Error in look up.
   */
  public static IServiceMgmtManagerObj getServiceMgmtManager()
         throws ServiceLookupException
  {
    return (IServiceMgmtManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IServiceMgmtManagerHome.class.getName(),
      IServiceMgmtManagerHome.class,
      new Object[0]);
  }
}
