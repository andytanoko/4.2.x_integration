/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertListEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 26 2002    Srinath	                Created
 * Feb 06 2003    Neo Sok Lay             Change UserId,FromId to UserUid,FromUid.
 * Mar 03 2003    Neo Sok Lay             Check if canDelete during remove.
 * Oct 31 2005    Neo Sok Lay             1. Remove implementation for getHome()
 *                                        2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay             Use generics                                   
 */
package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.alert.entities.ejb.IAlertListLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IAlertListLocalObj;
import com.gridnode.pdip.app.alert.model.AlertList;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;


/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AlertListBean.
 *
 * @author Srinath
 *
 */

public class AlertListEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = AlertList.ENTITY_NAME;
  private static final Object lock = new Object();

  public AlertListEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of AlertListEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static AlertListEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new AlertListEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of AlertListEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   */
  private static AlertListEntityHandler getFromEntityHandlerFactory()
  {
    return (AlertListEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAlertListLocalHome.class.getName(),
      IAlertListLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAlertListLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IAlertListLocalObj.class;
  }

  public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (Long i : affectedUIDs)
      remove(i);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    AlertList list = (AlertList)getEntityByKeyForReadOnly(uId);
    if (list.canDelete())
      super.remove( uId);
    else
      throw new ApplicationException("AlertList not allowed to be deleted");
  }


  // ********************** Own methods *******************************


  /**
   * Find the AlertList for the specified user.
   *
   * @param userUid The UID of the User.
   *
   * @return int The count of no. of alertLists that are new.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public int getAlertListsByUserUid(Long userUid) throws Throwable
  {
    return getAllNewAlertList(userUid).size();
  }



  /**
   * Find the AlertList for the specified user.
   *
   * @param userUid The UID of the User.
   *
   * @return The AlertList that has the specified userId, or <B>null</B> if
   * none found.
   *
   * @exception Throwable thrown when an error occurs.
   */

  public Collection<AlertList> getAllNewAlertList(Long userUid) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();

    //Find all Alert List for the specified User
    filter.addSingleFilter(null, AlertList.USER_UID,
      filter.getEqualOperator(), userUid, false);
    //AND the read status as false.
    filter.addSingleFilter(filter.getAndConnector(), AlertList.READSTATUS,
      filter.getEqualOperator(), Boolean.FALSE, false);

    return getEntityByFilterForReadOnly(filter);
/*
  Collection result = getEntityByFilter(filter);
    if (result == null || result.isEmpty())
        return null;
    return (AlertList) result.iterator().next();
*/
  }


  /**
   * Get all the AlertList.
   *
   * @return Collection of all the AlertList.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Collection<AlertList> getAllAlertLists() throws Throwable
  {
      return getEntityByFilterForReadOnly(null);
  }

  /**
   * Sets a new AlertList.
   *
   * @param from - From whom the message came
   * @param to - To whom the message needs to be sent
   * @param title - The message title
   * @param message - The actual content of the message
   *
   * @return the <code>AlertList</code>entity
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public AlertList createAlertList(Long from, Long to, String title, String message) throws Throwable
  {
    AlertList alertList = new AlertList();
    alertList.setUserUid(to);
    alertList.setFromUid(from);
    alertList.setTitle(title);
    alertList.setMessage(message);
    alertList.setReadStatus(false);
    alertList.setDate(new java.util.Date());
    return alertList;
  }
}