/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertCategoryEntityHandler.java
 *
 ****************************************************************************
 * Date           Author           Changes
 ****************************************************************************
 * Dec 05 2002    Srinath		       Created
 * Mar 03 2003    Neo Sok Lay      Check if canDelete during remove.
 * Oct 31 2005    Neo Sok Lay      1. Remove implementation for getHome()
 *                                 2. Implement getHomeInterfaceClass()
 * Mar 03 2006    Neo Sok Lay      Use generics                                   
 */

package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.alert.entities.ejb.IAlertCategoryLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IAlertCategoryLocalObj;
import com.gridnode.pdip.app.alert.model.AlertCategory;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AlertCategoryBean.
 *
 * @author Srinath
 *
 */

public class AlertCategoryEntityHandler extends LocalEntityHandler
{
  private static final String ENTITY_NAME = AlertCategory.ENTITY_NAME;
  private static final Object lock = new Object();

  public AlertCategoryEntityHandler()
  {
    super(ENTITY_NAME);
  }

  /**
   * Get instance of AlertCategoryEntityHandler.
   *
   * @return the singleton Instance of this class.
   *
   */
  public static AlertCategoryEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
           EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new AlertCategoryEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  /**
   * Retrieve the singleton instance of AlertCategoryEntityHandler from the EntityHandlerFactory.
   *
   * @return the singleton Instance of this class.
   *
   */
  private static AlertCategoryEntityHandler getFromEntityHandlerFactory()
  {
    return (AlertCategoryEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IAlertCategoryLocalHome.class.getName(),
      IAlertCategoryLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IAlertCategoryLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IAlertCategoryLocalObj.class;
  }

  public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (Long i : affectedUIDs)
      remove(i);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    AlertCategory cat = (AlertCategory)getEntityByKeyForReadOnly(uId);
    if (cat.canDelete())
      super.remove( uId);
    else
      throw new ApplicationException("AlertCategory not allowed to be deleted");
  }

  // ********************** Own methods *******************************

  /**
   * Get all the categories.
   *
   * @return Collection of all the categories.
   *
   * @exception Throwable thrown when an error occurs.
   *
   */
  public Collection<AlertCategory> getAllAlertCategories() throws Throwable
  {
      return getEntityByFilterForReadOnly(null);
  }

  /**
   * Retrieve an AlertCategory using the category code.
   *
   * @param categoryCode The category code of the AlertCategory.
   * @return The AlertCategory retrieved, or <b>null</b> if none exists with
   * the category code.
   */
  public AlertCategory getAlertCategoryByCode(String categoryCode) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AlertCategory.CODE, filter.getEqualOperator(),
      categoryCode, false);

    Collection<AlertCategory> result = getEntityByFilterForReadOnly(filter);
    if (result != null && !result.isEmpty())
      return result.iterator().next();
    return null;
  }

}