/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoHelper.java
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2003    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add method: updateEntity(IEntity entity)                                   
 */
package com.gridnode.pdip.app.bizreg.helpers;

import com.gridnode.pdip.app.bizreg.entities.ejb.IRegistryConnectInfoLocalHome;
import com.gridnode.pdip.app.bizreg.entities.ejb.IRegistryConnectInfoLocalObj;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

import java.util.Collection;

/**
 * This is a helper class for managing the persistency of RegistryConnectInfo entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryConnectInfoHelper extends LocalEntityHandler
{
  private static final RegistryConnectInfoHelper _self = new RegistryConnectInfoHelper(RegistryConnectInfo.ENTITY_NAME);

  /**
   * Constructs an RegistryConnectInfoHelper instance.
   * 
   * @param entityName
   */
  private RegistryConnectInfoHelper(String entityName)
  {
    super(entityName);
    EntityHandlerFactory.putEntityHandler(entityName,
       true, this);
  }

  /**
   * Gets an instance of RegistryConnectInfoHelper.
   * 
   * @return An instance of RegistryConnectInfoHelper
   */
  public static RegistryConnectInfoHelper getInstance()
  {
    return _self;
  }
  
  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getHome()
   *//*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IRegistryConnectInfoLocalHome.class.getName(),
      IRegistryConnectInfoLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IRegistryConnectInfoLocalHome.class;
	}

	/**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getProxyInterfaceClass()
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IRegistryConnectInfoLocalObj.class;
  }

  /**
   * Check if the specified RegistryConnectInfo will result in duplicate when
   * created or updated.
   *
   * @param connInfo The RegistryConnectInfo to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the RegistryConnectInfo itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * RegistryConnectInfo will result in duplicates.
   */
  public void checkDuplicate(
    RegistryConnectInfo connInfo, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryConnectInfo.NAME, filter.getEqualOperator(),
      connInfo.getName(), false);
    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), RegistryConnectInfo.UID,
        filter.getNotEqualOperator(), connInfo.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException("RegistryConnectInfo of Name "+connInfo.getName()
        + " already exists");
  }

  /**
   * Check whether a RegistryConnectInfo can be deleted.
   *
   * @param uid The UID of RegistryConnectInfo to check.
   *
   * @exception ApplicationException The RegistryConnectInfo is not allowed to be
   * deleted.
   */
  public void checkCanDelete(Long uid) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryConnectInfo.UID, filter.getEqualOperator(),
      uid, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryConnectInfo.CAN_DELETE,
      filter.getEqualOperator(), Boolean.TRUE, false);
      
    if (getDAO().getEntityCount(filter) == 0)
      throw new ApplicationException("RegistryConnectInfo not allowed to be deleted!");
  }
  
  /**
   * Check whether some RegistryConnectInfo(s) can be deleted.
   *
   * @param filter The filtering condition for the RegistryConnectInfo(s) to be deleted.
   *
   * @exception ApplicationException Any RegistryConnectInfo that is not allowed to be
   * deleted.
   */
  public void checkCanDelete(IDataFilter filter) throws Exception
  {
    Collection uids = getKeyByFilterForReadOnly(filter);
    if (uids.size() > 0)
    {
      DataFilterImpl checkFilter = new DataFilterImpl();
    
      checkFilter.addDomainFilter(null, RegistryConnectInfo.UID, uids, false);
      checkFilter.addSingleFilter(checkFilter.getAndConnector(), RegistryConnectInfo.CAN_DELETE,
      checkFilter.getEqualOperator(), Boolean.TRUE, false);
      
      if (getDAO().getEntityCount(filter) != uids.size())
        throw new ApplicationException("Some RegistryConnectInfo is not allowed to be deleted!");
    }
  }

  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#update(com.gridnode.pdip.framework.db.entity.IEntity)
   */
  public void update(IEntity entity) throws Throwable
  {
    checkDuplicate((RegistryConnectInfo)entity, true);
    super.update(entity);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    checkDuplicate((RegistryConnectInfo)entity, true);
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

}
