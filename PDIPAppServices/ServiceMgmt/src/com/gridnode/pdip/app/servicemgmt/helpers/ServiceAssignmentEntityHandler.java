/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceAssignmentEntityHandler.java
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

import com.gridnode.pdip.app.servicemgmt.entities.ejb.IServiceAssignmentLocalHome;
import com.gridnode.pdip.app.servicemgmt.entities.ejb.IServiceAssignmentLocalObj;
import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

public class ServiceAssignmentEntityHandler extends LocalEntityHandler
{
  private static final ServiceAssignmentEntityHandler _self = new ServiceAssignmentEntityHandler(ServiceAssignment.ENTITY_NAME);

  /**
   * Constructs an ServiceAssignmentHelper instance.
   * 
   * @param entityName
   */
  private ServiceAssignmentEntityHandler(String entityName)
  {
    super(entityName);
    EntityHandlerFactory.putEntityHandler(entityName,
       true, this);
  }

  /**
   * Gets an instance of ServiceAssignmentHelper.
   * 
   * @return An instance of ServiceAssignmentHelper
   */
  public static ServiceAssignmentEntityHandler getInstance()
  {
    return _self;
  }
  
  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getHome()
   *//*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IServiceAssignmentLocalHome.class.getName(),
      IServiceAssignmentLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IServiceAssignmentLocalHome.class;
	}

	/**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getProxyInterfaceClass()
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IServiceAssignmentLocalObj.class;
  }

  /**
   * Check if the specified ServiceAssignment will result in duplicate when
   * created or updated.
   *
   * @param serviceAssignment The ServiceAssignment to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the ServiceAssignment itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * ServiceAssignment will result in duplicates.
   */
  public void checkDuplicate(
    ServiceAssignment serviceAssignment, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ServiceAssignment.USER_NAME, filter.getEqualOperator(),
    serviceAssignment.getUserName(), false);
    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), ServiceAssignment.UID,
        filter.getNotEqualOperator(), serviceAssignment.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException("ServiceAssignment of Name "+serviceAssignment.getUserName()
        + " already exists");
  }

  /**
   * Check whether a ServiceAssignment can be deleted.
   *
   * @param uid The UID of ServiceAssignment to check.
   *
   * @exception ApplicationException The ServiceAssignment is not allowed to be
   * deleted.
   */
  public void checkCanDelete(Long uid) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ServiceAssignment.UID, filter.getEqualOperator(),
      uid, false);
    filter.addSingleFilter(filter.getAndConnector(), ServiceAssignment.CAN_DELETE,
      filter.getEqualOperator(), Boolean.TRUE, false);
      
    if (getDAO().getEntityCount(filter) == 0)
      throw new ApplicationException("ServiceAssignment not allowed to be deleted!");
  }
  
  /**
   * Check whether some ServiceAssignment(s) can be deleted.
   *
   * @param filter The filtering condition for the ServiceAssignment(s) to be deleted.
   *
   * @exception ApplicationException Any ServiceAssignment that is not allowed to be
   * deleted.
   */
  public void checkCanDelete(IDataFilter filter) throws Exception
  {
    Collection uids = getKeyByFilterForReadOnly(filter);
    if (uids.size() > 0)
    {
      DataFilterImpl checkFilter = new DataFilterImpl();
    
      checkFilter.addDomainFilter(null, ServiceAssignment.UID, uids, false);
      checkFilter.addSingleFilter(checkFilter.getAndConnector(), ServiceAssignment.CAN_DELETE,
      checkFilter.getEqualOperator(), Boolean.TRUE, false);
      
      if (getDAO().getEntityCount(filter) != uids.size())
        throw new ApplicationException("Some ServiceAssignment is not allowed to be deleted!");
    }
  }

  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
    checkDuplicate((ServiceAssignment)entity, true);
    super.update(entity);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    checkDuplicate((ServiceAssignment)entity, true);
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
   * Find the ServiceAssignment with specified userName,userType.
   *
   * @param userName The userName of the ServiceAssignment.
   * @param userType The userType of the ServiceAssignment.
   * @return The ServiceAssignment having the specified userName and userType, 
   * or <B>null</B> if none found.
   */
  public ServiceAssignment findServiceAssignment(String userName, String userType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ServiceAssignment.USER_NAME, filter.getEqualOperator(),userName, false);
    filter.addSingleFilter(filter.getAndConnector(), ServiceAssignment.USER_TYPE,filter.getEqualOperator(), userType, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (ServiceAssignment)result.iterator().next();
  }
  

}
