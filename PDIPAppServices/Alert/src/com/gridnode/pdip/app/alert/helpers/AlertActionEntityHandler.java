/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertActionEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 14 2002    Srinath	                Created
 * Feb 06 2003    Neo Sok Lay             Change AlertId,ActionId to AlertUid and
 *                                        ActionUid respectively.
 *                                        getAlertActionsByAlertUid replace
 *                                        getAlertActionByAlertId to return
 *                                        Collection of AlertAction(s) instead
 *                                        of one AlertAction.
 * Jul 10 2003    Neo Sok Lay             Add getAlertActionsByActionUid() method.
 * Oct 31 2005    Neo Sok Lay             1. Remove implementation for getHome()
 *                                        2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay             Use generics                                   
 */

package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.alert.entities.ejb.IAlertActionLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IAlertActionLocalObj;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AlertActionBean.
 *
 * @author Srinath
 *
 */

public class AlertActionEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = AlertAction.ENTITY_NAME;
  private static final Object lock = new Object();

  public AlertActionEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of AlertActionEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static AlertActionEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new AlertActionEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of AlertActionEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   */
  private static AlertActionEntityHandler getFromEntityHandlerFactory()
  {
    return (AlertActionEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAlertActionLocalHome.class.getName(),
      IAlertActionLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAlertActionLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IAlertActionLocalObj.class;
  }

  // ********************** Own methods *******************************


  /**
   * Find the AlertAction with the specified alert Id.
   *
   * @param alertUid The UId of the Alert.
   *
   * @return Collection of AlertAction(s) that has the specified Alert UId.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Collection<AlertAction> getAlertActionsByAlertUid(Long alertUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AlertAction.ALERT_UID, filter.getEqualOperator(), alertUid, false);
    return getEntityByFilterForReadOnly(filter);
  }
  
  /**
   * Find the AlertAction with the specified action UId.
   *
   * @param actionUid The UId of the Action.
   *
   * @return Collection of AlertAction(s) that has the specified Action UId.
   *
   * @exception Throwable thrown when an error occurs.
   */
  public Collection<AlertAction> getAlertActionsByActionUid(Long actionUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AlertAction.ACTION_UID, filter.getEqualOperator(), actionUid, false);
    return getEntityByFilterForReadOnly(filter);
  }
  
}