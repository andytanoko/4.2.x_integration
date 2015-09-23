/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	                Created
 * Jan 23 2003    Neo Sok Lay             Use FileUtil to save attachment.
 * Feb 06 2003    Neo Sok Lay             Changes in data type from String to Long.
 * Mar 03 2003    Neo Sok Lay             Check if canDelete during remove.
 * Apr 25 2003    Neo Sok Lay             Refactor.
 * May 14 2003    Neo Sok Lay             Change appendToLog() to writeToLog().
 *                                        Do not throw exception in writeToLog()
 *                                        and sendMail().
 * Jun 20 2003    Neo Sok Lay             Move alert triggering logic to AlertActor.
 * Jun 23 2003    Neo Sok Lay             Do not implement IAlertEntityHandler.
 * Oct 31 2005    Neo Sok Lay             1. Remove implementation for getHome()
 *                                        2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay             Use generics                                   
 */

package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.model.Alert;

import com.gridnode.pdip.app.alert.entities.ejb.IAlertLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IAlertLocalObj;

import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.util.Collection;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AlertBean.
 *
 * @author Srinath
 *
 */

public class AlertEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = Alert.ENTITY_NAME;
  private static final Object lock = new Object();

  public AlertEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of AlertEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static AlertEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new AlertEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of AlertEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   */
  private static AlertEntityHandler getFromEntityHandlerFactory()
  {
    return (AlertEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAlertLocalHome.class.getName(),
      IAlertLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAlertLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IAlertLocalObj.class;
  }

  public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (Long i : affectedUIDs)
      remove(i);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    Alert alert = (Alert)getEntityByKeyForReadOnly(uId);
    if (alert.canDelete())
      super.remove( uId);
    else
      throw new ApplicationException("Alert not allowed to be deleted");
  }

  // ********************** Own methods *******************************


  /**
   * Find the Alert with the specified alert name.
   *
   * @param alertName The name of the Alert.
   *
   * @return The Alert that has the specified Alert name, or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Alert getAlertByAlertName(String alertName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Alert.NAME, filter.getEqualOperator(), alertName, false);
    Collection<Alert> result = getEntityByFilterForReadOnly(filter);
      if (result == null || result.isEmpty())
        return null;
      return result.iterator().next();
  }

  /**
   * Find the Alert with the specified category.
   *
   * @param categoryUid The UID of the AlertCategory .
   *
   * @return The Alert that has the specified category, or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Alert getAlertByCategory(Long categoryUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Alert.CATEGORY_UID, filter.getEqualOperator(), categoryUid, false);
    Collection<Alert> result = getEntityByFilterForReadOnly(filter);
      if (result == null || result.isEmpty())
        return null;
      return result.iterator().next();
  }

  /**
   * Find the Alert with the specified Alert Type.
   *
   * @param alertType The Type of the Alert.
   *
   * @return The Alert that has the specified Alert Type, or <B>null</B> if
   * none found.
   *
   * @exception thrown when an error occurs.
   *
   */
/*
  public Alert getAlertByAlertType(String alertType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Alert.TYPE, filter.getEqualOperator(), alertType, false);
    Collection result = getEntityByFilterForReadOnly(filter);
      if (result == null || result.isEmpty())
        return null;
      return (Alert) result.iterator().next();
  }
*/

  /**
   * Get all the Alerts.
   *
   * @return Collection of all the Alerts.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Collection<Alert> getAllAlerts() throws Throwable
  {
      return getEntityByFilterForReadOnly(null);
  }

  /**
   * Get the Alert Id based on the Alert Name
   *
   * @param alertName	The name of the Alert.
   *
   * @return String Alert UId
   *
   * @exception thrown when an error occurs.
   *
   */
/*
  private Long getAlertUid(String alertName) throws Throwable
  {
    return new Long(getAlertByAlertName(alertName).getUId());
  }
*/
  /**
   * Retrieve the Trigger Level for the specified Alert Name
   *
   * @param alertName		The name of the Alert.
   *
   * @return Level of the Trigger in int
   *
   * @exception thrown when an error occurs.
   *
   */
/*
  private int getTriggerLevel(String alertName) throws Throwable
  {
    int triggerLevel = 0;
    Alert alert;
    try
    {
      alert = (Alert)getAlertByAlertName(alertName);
      triggerLevel = Integer.valueOf(alert.getTrigger()).intValue();
    }
    catch(Exception e)
    {
      triggerLevel = -1;
      System.out.println("e is :"+e);
    }
    return triggerLevel;
  }

*/
}