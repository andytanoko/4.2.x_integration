/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultBizCertMappingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         getApplicablePartnerCertList(partnerId)
 * 2003-01-30     Andrew Hill         Fixed bug where using wrong mgr to process partner & cert list
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Collections;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.partnerprocess.*;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultBizCertMappingManager extends DefaultAbstractManager
  implements IGTBizCertMappingManager
{
  DefaultBizCertMappingManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_BIZ_CERT_MAPPING, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTBizCertMappingEntity bcm = (IGTBizCertMappingEntity)entity;
      Long uid = bcm.getUidLong();
      Long partnerCertUid = (Long)bcm.getFieldValue(IGTBizCertMappingEntity.PARTNER_CERT);
      Long ownCertUid = (Long)bcm.getFieldValue(IGTBizCertMappingEntity.OWN_CERT);

      IEvent event = new UpdateBizCertMappingEvent(uid,partnerCertUid,ownCertUid);

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTBizCertMappingEntity bcm = (IGTBizCertMappingEntity)entity;
      String partnerId = bcm.getFieldString(IGTBizCertMappingEntity.PARTNER_ID);
      Long partnerCertUid = (Long)bcm.getFieldValue(IGTBizCertMappingEntity.PARTNER_CERT);
      Long ownCertUid = (Long)bcm.getFieldValue(IGTBizCertMappingEntity.OWN_CERT);

      IEvent event = new CreateBizCertMappingEvent(partnerId,partnerCertUid,ownCertUid);

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_BIZ_CERT_MAPPING;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_BIZ_CERT_MAPPING;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetBizCertMappingEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetBizCertMappingListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteBizCertMappingEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_BIZ_CERT_MAPPING.equals(entityType))
    {
      return new DefaultBizCertMappingEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  //Convience methods for getting partners and certs that may be selected for a mapping

  public Collection getApplicableOwnCertList()
    throws GTClientException
  { //20030130AH - Fixed bug where using wrong manager to process list
    try
    {
      IEvent event = new FilterOwnCertListEvent();
      DefaultAbstractManager certMgr =  (DefaultAbstractManager)
                                        _session.getManager(IGTManager.MANAGER_CERTIFICATE);
      try
      {
        return certMgr.handleGetListEvent(event);
      }
      catch(Throwable t)
      { //20030130AH
        throw new GTClientException("Error caught delegating to certificate"
                                    + " manager to process event: " + event,t);
      }
    }
    catch(Throwable t)
    { //20030130AH
      throw new GTClientException("Manager:" + this
        + " encountered error getting"
        + " own cert list",t);
    }
  }

  public Collection getApplicablePartnerCertList(IGTPartnerEntity partner)
    throws GTClientException
  {
    try
    {
      if(partner == null)
      { //Special convienience for our renderer ;-)
        return Collections.EMPTY_LIST;
      }
      Long partnerUid = partner.getUidLong();
      return getApplicablePartnerCertList(partnerUid);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting partner cert list for partner:" + partner,t);
    }
  }

  public Collection getApplicablePartnerCertList(Long partnerUid)
    throws GTClientException
  {
    //20030130AH - Fixed bug where it used wrong manager!
    try
    {
      IEvent event = new FilterPartnerCertListEvent(partnerUid);
      DefaultAbstractManager certMgr =  (DefaultAbstractManager)
                                        _session.getManager(IGTManager.MANAGER_CERTIFICATE);
      try
      {
        return certMgr.handleGetListEvent(event);
      }
      catch(Throwable t)
      { //20030130AH
        throw new GTClientException("Error caught delegating to certificate"
                                    + " manager to process event: " + event,t);
      }
    }
    catch(Throwable t)
    { //20030130AH
      throw new GTClientException("Manager:" + this
        + " encountered error getting"
        + " partner cert list for partner uid="
        + partnerUid,t);
    }
  }

  public Collection getApplicablePartnerCertList(String partnerId)
    throws GTClientException
  { //20030115 - to make use of new event constructor that takes id :-)
    //20030130AH - Fixed bug where it used wrong manager!
    try
    {
      if( (partnerId == null) || "".equals(partnerId) )
      {
        return Collections.EMPTY_LIST;
      }
      else
      {
        IEvent event = new FilterPartnerCertListEvent(partnerId);
        DefaultAbstractManager certMgr =  (DefaultAbstractManager)
                                        _session.getManager(IGTManager.MANAGER_CERTIFICATE);
        try
        { //20030130AH
          return certMgr.handleGetListEvent(event);
        }
        catch(Throwable t)
        { //20030130AH
          throw new GTClientException("Error caught delegating to certificate"
                                      + " manager to process event: " + event,t);
        }
      }
    }
    catch(Throwable t)
    { //20030130AH
      throw new GTClientException("Manager:" + this
        + " encountered error getting"
        + " partner cert list for partner id="
        + partnerId,t);
    }
  }

  public Collection getApplicablePartnerList(Integer condition)
    throws GTClientException
  { //20030130AH - Fixed bug where wrong manager used to process objects
    try
    {
      DefaultAbstractManager ptrMgr = (DefaultAbstractManager)
                                      _session.getManager(IGTManager.MANAGER_PARTNER);
      if(condition == null)
      {
        return ptrMgr.getAll();
      }
      else
      {
        IEvent event = new FilterPartnerListEvent(condition);
        try
        { //20030130AH
          return ptrMgr.handleGetListEvent(event);
        }
        catch(Throwable t)
        { //20030130AH
          throw new GTClientException("Error caught delegating to partner"
                                      + " manager to process event: " + event,t);
        }
      }
    }
    catch(Throwable t)
    { //20030130AH
      throw new GTClientException("Manager:" + this
        + " encountered error getting"
        + " partner list for condition="
        + condition,t);
    }
  }
}