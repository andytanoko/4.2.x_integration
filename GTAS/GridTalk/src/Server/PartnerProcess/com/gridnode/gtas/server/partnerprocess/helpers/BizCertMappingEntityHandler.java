/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMappingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Apr 12 2006    Neo Sok Lay         Add method: updateEntity(IEntity entity)
 * Aug 01 2008	  Wong Yee Wah		  #38  Added method: getAllBizCertMapping()
 * 									  Added method: getBizCertMappingDAOHelper()
 * 									  Added Method: getBizCertMapping()                                   
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.partnerprocess.entities.ejb.IBizCertMappingLocalHome;
import com.gridnode.gtas.server.partnerprocess.entities.ejb.IBizCertMappingLocalObj;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.pdip.base.certificate.helpers.CertificateDAOHelper;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the BizCertMappingBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I7
 */
public final class BizCertMappingEntityHandler
  extends          LocalEntityHandler
{
  private BizCertMappingEntityHandler()
  {
    super(BizCertMapping.ENTITY_NAME);
  }

  /**
   * Get an instance of a BizCertMappingEntityHandler.
   */
  public static BizCertMappingEntityHandler getInstance()
  {
    BizCertMappingEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(BizCertMapping.ENTITY_NAME, true))
    {
      handler = (BizCertMappingEntityHandler)EntityHandlerFactory.getHandlerFor(
                  BizCertMapping.ENTITY_NAME, true);
    }
    else
    {
      handler = new BizCertMappingEntityHandler();
      EntityHandlerFactory.putEntityHandler(BizCertMapping.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IBizCertMappingLocalHome.class.getName(),
      IBizCertMappingLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IBizCertMappingLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IBizCertMappingLocalObj.class;
  }

  public void update(IEntity entity) throws java.lang.Throwable
  {
    BizCertMappingDAOHelper.getInstance().checkDuplicate(
      (BizCertMapping)entity, true);

    super.update(entity);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#updateEntity(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	@Override
	public IEntity updateEntity(IEntity entity) throws Throwable
	{
    BizCertMappingDAOHelper.getInstance().checkDuplicate((BizCertMapping)entity, true);
		return super.updateEntity(entity);
	}

	public void removeByFilter(IDataFilter filter) throws java.lang.Throwable
  {
    Long[] affectedUIDs = (Long[])getKeyByFilterForReadOnly(filter).toArray(new Long[0]);
    for (int i=0; i<affectedUIDs.length; i++)
      remove(affectedUIDs[i]);
  }

  public void remove(Long uId) throws java.lang.Throwable
  {
    BizCertMappingDAOHelper.getInstance().checkCanDelete(
      (BizCertMapping)getEntityByKeyForReadOnly(uId));

    super.remove( uId);
  }
  
  public Collection getBizCertMapping(IDataFilter filter) throws Throwable
  {
	 Collection result = null;
     if(filter != null)
     {
        result = getBizCertMappingDAOHelper().getEntityByFilter(filter);
     }else{
    	 result = getAllBizCertMapping();
     }
	 
     return result;
  }
  
  public Collection getAllBizCertMapping() throws Throwable
  {
     DataFilterImpl filter = new DataFilterImpl();
     filter.addSingleFilter(null,BizCertMapping.UID,filter.getNotEqualOperator(),null,false);
     
     Collection result = getBizCertMapping(filter);
     //if(result == null || result.isEmpty())
     //throw new Exception("Cannot Find the Record ");
     return result;
  }
  
  public IEntity getBizCertMappingByKey(long uid)
  {
	  
	  try{
		  return getBizCertMappingDAOHelper().load(uid);
	  }catch(Throwable t)
	  {}
	  
	 return null;
  }
  
  protected BizCertMappingDAOHelper getBizCertMappingDAOHelper()
  {
    return BizCertMappingDAOHelper.getInstance();
  }


}