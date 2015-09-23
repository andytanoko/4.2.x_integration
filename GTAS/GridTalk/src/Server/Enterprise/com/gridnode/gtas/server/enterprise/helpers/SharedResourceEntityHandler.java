/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SharedResourceEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.enterprise.helpers;

import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.enterprise.entities.ejb.ISharedResourceLocalHome;
import com.gridnode.gtas.server.enterprise.entities.ejb.ISharedResourceLocalObj;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the SharedResource.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public final class SharedResourceEntityHandler
  extends          LocalEntityHandler
{
  private SharedResourceEntityHandler()
  {
    super(SharedResource.ENTITY_NAME);
  }

  /**
   * Get an instance of a SharedResourceEntityHandler.
   */
  public static SharedResourceEntityHandler getInstance()
  {
    SharedResourceEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(SharedResource.ENTITY_NAME, true))
    {
      handler = (SharedResourceEntityHandler)EntityHandlerFactory.getHandlerFor(
                  SharedResource.ENTITY_NAME, true);
    }
    else
    {
      handler = new SharedResourceEntityHandler();
      EntityHandlerFactory.putEntityHandler(SharedResource.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ISharedResourceLocalHome.class.getName(),
      ISharedResourceLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ISharedResourceLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return ISharedResourceLocalObj.class;
  }

  // ********************** Own methods ******************************

  /**
   * Add a resource link into the database.
   *
   * @param link The resource link.
   * @return The UID of the created resource link.
   */
  public Long addSharedResource(SharedResource sharedRes)
    throws Throwable
  {
    SharedResource created = (SharedResource)createEntity(sharedRes);
    Long key = (Long)created.getKey();

    return key;
  }

  public void markDeleteSharedResources(Collection sharedResources)
    throws Throwable
  {
    //check all can be deleted
    for (Iterator i=sharedResources.iterator(); i.hasNext(); )
    {
      checkCanDelete((SharedResource)i.next());
    }

    //delete

    for (Iterator i=sharedResources.iterator(); i.hasNext(); )
    {
      markDelete((SharedResource)i.next());
    }
  }

  public void markDeleteSharedResource(SharedResource sharedRes)
    throws Throwable
  {
    checkCanDelete(sharedRes);
    markDelete(sharedRes);
  }

  private void markDelete(SharedResource sharedRes)
    throws Throwable
  {
    sharedRes.setState(SharedResource.STATE_DELETED);
    update(sharedRes);
  }

  /**
   * Check whether a resource link can be deleted from the database.
   *
   * @param resourceLink The resource link to check
   * @param toDeleteList The list of UIDs of the resource links that can be
   * deleted.
   * @param rootLink The root SharedResource that is checked.
   * @param checkNextLink <b>true</b> to check whether the NextLinks can be
   * deleted, <b>false</b> otherwise.
   * @exception ApplicationException The resourceLink cannot be deleted, or any
   * of it's NextLink cannot be deleted (if <code>checkNextLinks</code> is
   * <b>true</b>).
   */
  private void checkCanDelete(SharedResource sharedRes)
    throws Throwable
  {
    if (!sharedRes.canDelete())
      throw new ApplicationException("SharedResource [" +
        sharedRes.getEntityDescr() + "] not allowed to be deleted!");
  }


  // *********************** Finder methods ******************************

  public SharedResource getSharedResource(Long sharedResUID)
    throws Throwable
  {
    return (SharedResource)getEntityByKeyForReadOnly(sharedResUID);
  }

  public Collection getSharedResourcesByFilter(IDataFilter filter)
    throws Throwable
  {
    return getEntityByFilterForReadOnly(filter);
  }
}