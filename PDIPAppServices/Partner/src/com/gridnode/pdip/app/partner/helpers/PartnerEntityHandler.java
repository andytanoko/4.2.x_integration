/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 20 2002    Ang Meng Hua        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Mar 28 2006    Neo Sok Lay         Add methods:
 *                                    1. setMaxActivePartners(int)
 *                                    2. countActivePartners(String)
 *                                    3. deactivatePartners(int) 
 *                                    4. getMaxActivePartbers()                      
 */
package com.gridnode.pdip.app.partner.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.partner.entities.ejb.IPartnerLocalHome;
import com.gridnode.pdip.app.partner.entities.ejb.IPartnerLocalObj;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public final class PartnerEntityHandler extends LocalEntityHandler
{
	private static int _maxActivePartners = 0;
	
  private PartnerEntityHandler()
  {
    super(Partner.ENTITY_NAME);
  }

  /**
   * Get an instance of a PartnerEntityHandler.
   */
  public static PartnerEntityHandler getInstance()
  {
    PartnerEntityHandler handler = null;
    if (EntityHandlerFactory.hasEntityHandlerFor(Partner.ENTITY_NAME, true))
    {
      handler = (PartnerEntityHandler)EntityHandlerFactory.getHandlerFor(
                  Partner.ENTITY_NAME, true);
    }
    else
    {
      handler = new PartnerEntityHandler();
      EntityHandlerFactory.putEntityHandler(Partner.ENTITY_NAME, true, handler);
    }
    return handler;
  }

  /**
   * Delete a  partner. This will not physically delete the partner
   * from the database. This will only mark the partner as deleted.
   *
   * @param uId The UID of the Partner to delete.
   */
  public void markDelete(Long uId) throws Throwable
  {
    Partner partner = (Partner)getEntityByKey(uId);
    if (partner != null)
    {
      partner.setState(Partner.STATE_DELETED);
      update(partner);
    }
  }

  /**
   * Find a Partner using the Partner ID.
   *
   * @param partnerID The Partner ID of the partner to find.
   * @return The Partner found, or <B>null</B> if none exists with that
   * ID.
   */
  public Partner findByID(String partnerID)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Partner.PARTNER_ID, filter.getEqualOperator(), partnerID, false);

    // use this method for fast-lane reader implementation
    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (Partner)result.iterator().next();
  }

  /**
   * Find all Partners using the Partner Name.
   *
   * @param name The Partner Name of the partners to find.
   * @return A Collection of Parners found, or empty collection if none
   * exists.
   */
  public Collection findByName(String name)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Partner.NAME, filter.getEqualOperator(), name, false);

    // use this method for fast-lane reader implementation
    return getEntityByFilterForReadOnly(filter);
  }

 /**
   * Find all Partners using the Partner Type.
   *
   * @param partnerType The Partner Type of the partners to find.
   * @return A Collection of Parners found, or empty collection if none
   * exists.
   */
  public Collection findByPartnerType(String partnerType)
    throws Throwable
  {
    IEntity entity = PartnerTypeEntityHandler.getInstance().findByName(partnerType);

    if (partnerType == null)
      throw new ApplicationException("Partner Type does not exit: " + partnerType);

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      Partner.PARTNER_TYPE,
      filter.getEqualOperator(),
      entity.getKey(),
      false);

    // use this method for fast-lane reader implementation
    return getEntityByFilterForReadOnly(filter);
  }

 /**
   * Find all Partners using the Partner Group.
   *
   * @param partnerGroup The Partner Group of the partners to find.
   * @return A Collection of Parners found, or empty collection if none
   * exists.
   */
  public Collection findByPartnerGroup(String partnerGroup)
    throws Throwable
  {
    IEntity entity = PartnerGroupEntityHandler.getInstance().findByName(partnerGroup);

    if (partnerGroup == null)
      throw new ApplicationException("Partner Group does not exit: " + partnerGroup);

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      Partner.PARTNER_GROUP,
      filter.getEqualOperator(),
      entity.getKey(),
      false);

    // use this method for fast-lane reader implementation
    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IPartnerLocalHome.class.getName(),
      IPartnerLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
  {
  	return IPartnerLocalHome.class;
  }

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IPartnerLocalObj.class;
  }
  
  public void setMaxActivePartners(int maxActivePartners)
  {
  	_maxActivePartners = maxActivePartners;
  }

//	/**
//	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#create(com.gridnode.pdip.framework.db.entity.IEntity)
//	 */
//	@Override
//	public Object create(IEntity entity) throws Throwable
//	{
//		//check maxActivePartners
//		Partner partner = (Partner)entity;
//		if (Partner.STATE_ENABLED == partner.getState() && _maxActivePartners > -1)
//		{
//			int activePartners = countActivePartners(null);
//			if (activePartners >= _maxActivePartners) //i.e. including this one to be created will exceed max
//			{
//				partner.setState(Partner.STATE_DISABLED); //silently change the state to disabled
//			}
//		}
//		
//		return super.create(entity);
//	}

//	/**
//	 * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#setData(java.lang.Object, com.gridnode.pdip.framework.db.entity.IEntity)
//	 */
//	@Override
//	protected void setData(Object obj, IEntity entity) throws Throwable
//	{
//		//check maxActivePartners
//		Partner partner = (Partner)entity;
//		if (Partner.STATE_ENABLED == partner.getState() && _maxActivePartners > -1)
//		{
//			int activePartners = countActivePartners(partner.getPartnerID());
//			if (activePartners >= _maxActivePartners) //i.e. including this one to be created will exceed max
//			{
//				partner.setState(Partner.STATE_DISABLED); //silently change the state to disabled
//			}
//		}
//		super.setData(obj, entity);
//	}
  
	public int countActivePartners(String excludingPartnerID) throws Exception
	{
		DataFilterImpl filter = new DataFilterImpl();
		filter.addSingleFilter(null, Partner.STATE, filter.getEqualOperator(), Partner.STATE_ENABLED, false);
		if (excludingPartnerID != null && excludingPartnerID.trim().length()>0)
		{
			filter.addSingleFilter(filter.getAndConnector(), Partner.PARTNER_ID, filter.getNotEqualOperator(), excludingPartnerID, false);
		}
		return getEntityCount(filter);
	}
	
	public int getMaxActivePartners()
	{
		return _maxActivePartners;
	}
	
	public void deactivePartners(int numToDeactivate) throws Throwable
	{
		DataFilterImpl filter = new DataFilterImpl();
		filter.addSingleFilter(null, Partner.STATE, filter.getEqualOperator(), Partner.STATE_ENABLED, false);
		Collection partnersColl = getEntityByFilter(filter);
		Partner[] partners = (Partner[])partnersColl.toArray(new Partner[partnersColl.size()]);
		if (numToDeactivate <= partners.length)
		{
			for (int i=0; i<numToDeactivate; i++)
			{
				partners[i].setState(Partner.STATE_DISABLED);
				update(partners[i]);
			}
		}
		
	}
}