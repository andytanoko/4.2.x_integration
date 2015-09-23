/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTypeEntityHandler.java
 *
 ****************************************************************************
 * Date           Author             Changes
 ****************************************************************************
 * Dec 05 2002    Srinath			       Created
 * Mar 03 2003    Neo Sok Lay        Check if canDelete during remove.
 * Oct 31 2005    Neo Sok Lay        1. Remove implementation for getHome()
 *                                   2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay        Use generics                                   
 */
package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.alert.entities.ejb.IAlertTypeLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IAlertTypeLocalObj;
import com.gridnode.pdip.app.alert.model.AlertType;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AlertTypeBean.
 *
 * @author Srinath
 *
 */

public class AlertTypeEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = AlertType.ENTITY_NAME;
  private static final Object lock = new Object();

  public AlertTypeEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of AlertTypeEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static AlertTypeEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new AlertTypeEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of AlertTypeEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   */
  private static AlertTypeEntityHandler getFromEntityHandlerFactory()
  {
    return (AlertTypeEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAlertTypeLocalHome.class.getName(),
      IAlertTypeLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAlertTypeLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IAlertTypeLocalObj.class;
  }

  public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (Long i : affectedUIDs)
      remove(i);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    AlertType type = (AlertType)getEntityByKeyForReadOnly(uId);
    if (type.canDelete())
      super.remove( uId);
    else
      throw new ApplicationException("AlertType not allowed to be deleted");
  }

  // ********************** Own methods *******************************

  /**
   * Find the Action with the specified Action name.
   *
   * @param actionName The name of the action.
   *
   * @return The Action that has the specified Action name, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   */
/*
  public Action getActionByActionName(String actionName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Action.NAME, filter.getEqualOperator(), actionName, false);
    Collection result = getEntityByFilterForReadOnly(filter);
      if (result == null || result.isEmpty())
        return null;
      return (Action) result.iterator().next();
  }
*/
  /**
   * Get all the Actions.
   *
   * @return Collection of all the Actions.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */

  public Collection<AlertType> getAllAlertTypes() throws Throwable
  {
      return getEntityByFilterForReadOnly(null);
  }
}