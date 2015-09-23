/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Jul 25 2002    Neo Sok Lay             Remove dependency on ejbEntityMap.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.base.acl.helpers;

import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.entities.ejb.IRoleLocalHome;
import com.gridnode.pdip.base.acl.entities.ejb.IRoleLocalObj;

import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the RoleBean.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class RoleEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = Role.ENTITY_NAME;
  private static final Object lock = new Object();

  public RoleEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of RoleEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  public static RoleEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new RoleEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of RoleEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static RoleEntityHandler getFromEntityHandlerFactory()
  {
    return (RoleEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IRoleLocalHome.class.getName(),
      IRoleLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IRoleLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IRoleLocalObj.class;
  }

  // ****************************** Own methods ************************

  /**
   * Find the Role with the specified role name.
   *
   * @param role The name of the role.
   *
   * @return The Role that has the specified role name, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Role getRoleByRoleName(String role) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Role.ROLE, filter.getEqualOperator(),
      role, false);

//    try
//    {
      Collection result = getEntityByFilterForReadOnly(filter);
      if (result == null || result.isEmpty())
        return null;
      return (Role) result.iterator().next();
//    }
//    catch (Throwable t)
//    {
//      ACLLogger.errorLog("RoleEntityHandler", "findByRole",
//                         "Unable to retrieve Role with name = " + role , t);
//      throw new Exception(t.getMessage());
//    }
  }

  /**
   * Find the Role with the specified uId.
   *
   * @param uId The uId of the role.
   *
   * @return The Role that has the specified role name, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
//  public Role getRoleByUId(Long uId) throws Exception
//  {
//    DataFilterImpl filter = new DataFilterImpl();
//    filter.addSingleFilter(null, Role.UID, filter.getEqualOperator(),
//      uId, false);
//
//    try
//    {
//      Collection result = getEntityByFilterForReadOnly(filter);
//      if (result == null || result.isEmpty())
//        return null;
//      return (Role) result.iterator().next();
//    }
//    catch (Throwable t)
//    {
//      ACLLogger.errorLog("RoleEntityHandler", "findByUId",
//                         "Unable to retrieve Role with uId = " + uId , t);
//      throw new Exception(t.getMessage());
//    }
//  }

}