/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 11 2002    Srinath		              Created
 * Mar 03 2003    Neo Sok Lay             Check if canDelete during remove.
 * Oct 31 2005    Neo Sok Lay             1. Remove implementation for getHome()
 *                                        2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay             Use generics                                   
 */
package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.alert.entities.ejb.IActionLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IActionLocalObj;
import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ActionBean.
 *
 * @author Srinath
 *
 */

public class ActionEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = Action.ENTITY_NAME;
  private static final Object lock = new Object();

  public ActionEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of ActionEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static ActionEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new ActionEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of ActionEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of ActionEntityHandler class.
   *
   * @since   2.0
   * @version 2.0
   */
  private static ActionEntityHandler getFromEntityHandlerFactory()
  {
    return (ActionEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IActionLocalHome.class.getName(),
      IActionLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IActionLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IActionLocalObj.class;
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
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Action getActionByActionName(String actionName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Action.NAME, filter.getEqualOperator(), actionName, false);
    Collection<Action> result = getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty())
      return null;
    return result.iterator().next();
  }

  /**
   * Get all the Actions.
   *
   * @return Collection of all the Actions.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Collection<Action> getAllActions() throws Throwable
  {
    return getEntityByFilterForReadOnly(null);
  }

  public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (Long i : affectedUIDs)
      remove(i);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    Action action = (Action)getEntityByKeyForReadOnly(uId);
    if (action.canDelete())
      super.remove( uId);
    else
      throw new ApplicationException("Action not allowed to be deleted");
  }
}