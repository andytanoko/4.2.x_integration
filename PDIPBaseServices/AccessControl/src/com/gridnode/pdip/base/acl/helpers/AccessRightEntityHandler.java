/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 21 2002    Neo Sok Lay             Created
 * Jul 25 2002    Neo Sok Lay             Remove dependency on ejbEntityMap.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.base.acl.helpers;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.pdip.base.acl.entities.ejb.IAccessRightLocalHome;
import com.gridnode.pdip.base.acl.entities.ejb.IAccessRightLocalObj;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AccessRightBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */

public class AccessRightEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = AccessRight.ENTITY_NAME;
  private static final Object lock = new Object();

  public AccessRightEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of AccessRightEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static AccessRightEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true,
             new AccessRightEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of AccessRightEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static AccessRightEntityHandler getFromEntityHandlerFactory()
  {
    return (AccessRightEntityHandler)EntityHandlerFactory.getHandlerFor(
             ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAccessRightLocalHome.class.getName(),
      IAccessRightLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAccessRightLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IAccessRightLocalObj.class;
  }

  // ********************** Own methods **************************

  /**
   * Find the AccessRight with the specified uId.
   *
   * @param uId The uId of the AccessRight.
   *
   * @return The AccessRight that has the specified UID, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public AccessRight getAccessRightByUId(Long uId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AccessRight.UID, filter.getEqualOperator(),
      uId, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      return null;
    return (AccessRight)result.iterator().next();
  }

  /**
   * Find the AccessRight with the specified roleUID.
   *
   * @param roleUID The roleUId of the AccessRight.
   *
   * @return A collection of AccessRights that have the specified roleUID,
   * or empty list if none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getAccessRightsByRoleUId(Long roleUID) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AccessRight.ROLE, filter.getEqualOperator(),
      roleUID, false);

    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Find the AccessRight with the specified roleUID and feature.
   *
   * @param roleUID The roleUId of the AccessRight.
   * @param feature The feature name.
   *
   * @return A collection of AccessRights that have the specified roleUID and
   * feature, or empty list if none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getAccessRightsBy(Long roleUID, String feature) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AccessRight.ROLE, filter.getEqualOperator(),
      roleUID, false);
    filter.addSingleFilter(filter.getAndConnector(), AccessRight.FEATURE,
      filter.getEqualOperator(), feature, false);

    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Find the AccessRight with the specified roleUID, feature, and action.
   *
   * @param roleUID The roleUId of the AccessRight.
   * @param feature The feature name.
   * @param action The action
   *
   * @return A collection of AccessRights that have the specified roleUID,
   * feature, and action, or empty list if none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection getAccessRightsBy(Long roleUID, String feature, String action)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AccessRight.ROLE, filter.getEqualOperator(),
      roleUID, false);
    filter.addSingleFilter(filter.getAndConnector(), AccessRight.FEATURE,
      filter.getEqualOperator(), feature, false);
    filter.addSingleFilter(filter.getAndConnector(), AccessRight.ACTION,
      filter.getEqualOperator(), action, false);

    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Remove the access rights for a Role
   *
   * @param roleUID UID of the role whose access rights are to be removed.
   *
   * @exception Throwable Thrown when an error occurs.
   */
  public void removeByRoleUID(Long roleUID) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AccessRight.ROLE, filter.getEqualOperator(),
      roleUID, false);

    removeByFilter(filter);
  }

  /**
   * Remove the access rights defined with respect to a particular feature.
   *
   * @param feature The feature.
   *
   * @exception Throwable Thrown when an error occurs.
   */
  public void removeByFeature(String feature) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AccessRight.FEATURE, filter.getEqualOperator(),
      feature, false);

    removeByFilter(filter);
  }

  public void checkDuplicate(AccessRight acr, boolean existingAcr)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();

    //default check based on Role, Feature, Action
    filter.addSingleFilter(null, AccessRight.ROLE, filter.getEqualOperator(),
      acr.getRoleUID(), false);
    filter.addSingleFilter(filter.getAndConnector(), AccessRight.FEATURE,
      filter.getEqualOperator(), acr.getFeature(), false);
    filter.addSingleFilter(filter.getAndConnector(), AccessRight.ACTION,
      filter.getEqualOperator(), acr.getAction(), false);

    if (existingAcr)
    {
      filter.addSingleFilter(filter.getAndConnector(), AccessRight.UID,
        filter.getNotEqualOperator(), new Long(acr.getUId()), false);
    }

    //data type is specified, additional criteria
    if (acr.getDataType() != null && acr.getDataType().length() > 0)
    {
      ArrayList dataTypes = new ArrayList();
      dataTypes.add(acr.getDataType());
      dataTypes.add("");

      DataFilterImpl filter2 = new DataFilterImpl();
      filter2.addDomainFilter(null, AccessRight.DATA_TYPE,
        dataTypes, false);
      filter2.addSingleFilter(filter.getOrConnector(), AccessRight.DATA_TYPE,
        filter.getEqualOperator(), null, false);

      filter.addFilter(filter.getAndConnector(), filter2);
    }

ACLLogger.debugLog("AccessRightEntityHandler", "checkDuplicate", "Constructed filter: "+filter.getFilterExpr());
    int count = getDAO().getEntityCount(filter);
ACLLogger.debugLog("AccessRightEntityHandler", "checkDuplicate", "return count:"+count);
    if (count > 0)
    {
      throw new DuplicateEntityException(
                  "Access right already defined for: Feature("+
                  acr.getFeature() + "), Action("+acr.getAction() +
                  "), DataType(" + acr.getDataType() + ")");
    }
  }
}