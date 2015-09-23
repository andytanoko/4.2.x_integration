/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WebServiceEntityHandler.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add method: updateEntity(IEntity entity)                                   
 */
package com.gridnode.pdip.app.servicemgmt.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.servicemgmt.entities.ejb.IWebServiceLocalHome;
import com.gridnode.pdip.app.servicemgmt.entities.ejb.IWebServiceLocalObj;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

public class WebServiceEntityHandler extends LocalEntityHandler
{
  private static final WebServiceEntityHandler _self = new WebServiceEntityHandler(WebService.ENTITY_NAME);

  /**
   * Constructs an WebServiceHelper instance.
   * 
   * @param entityName
   */
  private WebServiceEntityHandler(String entityName)
  {
    super(entityName);
    EntityHandlerFactory.putEntityHandler(entityName,
       true, this);
  }

  /**
   * Gets an instance of WebServiceHelper.
   * 
   * @return An instance of WebServiceHelper
   */
  public static WebServiceEntityHandler getInstance()
  {
    return _self;
  }
  
  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getHome()
   *//*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IWebServiceLocalHome.class.getName(),
      IWebServiceLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IWebServiceLocalHome.class;
	}

	/**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getProxyInterfaceClass()
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IWebServiceLocalObj.class;
  }

  /**
   * Check if the specified WebService will result in duplicate when
   * created or updated.
   *
   * @param serviceAssignment The WebService to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the WebService itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * WebService will result in duplicates.
   */
  public void checkDuplicate(
    WebService serviceAssignment, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, WebService.SERVICE_NAME, filter.getEqualOperator(),
    serviceAssignment.getServiceName(), false);
    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), WebService.UID,
        filter.getNotEqualOperator(), serviceAssignment.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException("WebService of Name "+serviceAssignment.getServiceName()
        + " already exists");
  }

  /**
   * Check whether a WebService can be deleted.
   *
   * @param uid The UID of WebService to check.
   *
   * @exception ApplicationException The WebService is not allowed to be
   * deleted.
   */
  public void checkCanDelete(Long uid) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, WebService.UID, filter.getEqualOperator(),
      uid, false);
    filter.addSingleFilter(filter.getAndConnector(), WebService.CAN_DELETE,
      filter.getEqualOperator(), Boolean.TRUE, false);
      
    if (getDAO().getEntityCount(filter) == 0)
      throw new ApplicationException("WebService not allowed to be deleted!");
  }
  
  /**
   * Check whether some WebService(s) can be deleted.
   *
   * @param filter The filtering condition for the WebService(s) to be deleted.
   *
   * @exception ApplicationException Any WebService that is not allowed to be
   * deleted.
   */
  public void checkCanDelete(IDataFilter filter) throws Exception
  {
    Collection uids = getKeyByFilterForReadOnly(filter);
    if (uids.size() > 0)
    {
      DataFilterImpl checkFilter = new DataFilterImpl();
    
      checkFilter.addDomainFilter(null, WebService.UID, uids, false);
      checkFilter.addSingleFilter(checkFilter.getAndConnector(), WebService.CAN_DELETE,
      checkFilter.getEqualOperator(), Boolean.TRUE, false);
      
      if (getDAO().getEntityCount(filter) != uids.size())
        throw new ApplicationException("Some WebService is not allowed to be deleted!");
    }
  }

  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
    checkDuplicate((WebService)entity, true);
    super.update(entity);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    checkDuplicate((WebService)entity, true);
		return super.updateEntity(entity);
	}

	/**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#remove(java.lang.Long)
   */
  public void remove(Long uid) throws Throwable
  {
    checkCanDelete(uid);
    super.remove(uid); 
  }

  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#removeByFilter(com.gridnode.pdip.framework.db.filter.IDataFilter)
   */
  public void removeByFilter(IDataFilter filter) throws Throwable
  {
    checkCanDelete(filter);
    super.removeByFilter(filter);
  }

  /**
   * Find the WebService with specified serviceName,serviceGroup.
   *
   * @param serviceName The userName of the ServiceAssignment.
   * @param serviceGroup The userType of the ServiceAssignment.
   * @return The WebService having the specified serviceName and serviceGroup, 
   * or <B>null</B> if none found.
   */
  public WebService findWebService(String serviceName, String serviceGroup) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, WebService.SERVICE_NAME, filter.getEqualOperator(),serviceName, false);
    filter.addSingleFilter(filter.getAndConnector(), WebService.SERVICE_GROUP,filter.getEqualOperator(), serviceGroup, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (WebService)result.iterator().next();
  }


}
