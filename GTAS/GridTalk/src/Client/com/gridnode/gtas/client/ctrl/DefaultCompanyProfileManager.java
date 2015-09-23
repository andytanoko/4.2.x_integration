/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCompanyProfileManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.gridnode.GetMyCompanyProfileEvent;
import com.gridnode.gtas.events.gridnode.SaveMyCompanyProfileEvent;
import com.gridnode.gtas.model.gridnode.ICompanyProfile;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;


class DefaultCompanyProfileManager extends DefaultAbstractManager
  implements IGTCompanyProfileManager
{
  DefaultCompanyProfileManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_COMPANY_PROFILE, session);
  }

  public IGTCompanyProfileEntity getMyCompanyProfile() throws GTClientException
  {
    try
    {
      IEvent event = new GetMyCompanyProfileEvent();
      DefaultCompanyProfileEntity profile = (DefaultCompanyProfileEntity)handleGetEvent(event);
      profile.setNewFieldValue(IGTCompanyProfileEntity.IS_PARTNER,Boolean.FALSE); //20020911AH temp hack
      return profile;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting my company profile",t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)entity;
      Boolean isPartner = (Boolean)profile.getFieldValue(IGTCompanyProfileEntity.IS_PARTNER);
      if(isPartner != null)
      {
        if(isPartner.booleanValue())
        {
          throw new GTClientException("Partner CompanyProfile may not be modified");
        }
      }
      Map map = getProfileMap(profile);
      IEvent event = new SaveMyCompanyProfileEvent(map);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating company profile",t);
    }
  }

  Map getProfileMap(IGTCompanyProfileEntity profile) throws GTClientException
  {
    if(profile == null) throw new java.lang.NullPointerException("null companyProfile entity");
    Map map = new HashMap();
    map.put(ICompanyProfile.UID, profile.getFieldValue(IGTCompanyProfileEntity.UID));
    map.put(ICompanyProfile.COY_NAME, profile.getFieldValue(IGTCompanyProfileEntity.COY_NAME));
    map.put(ICompanyProfile.EMAIL, profile.getFieldValue(IGTCompanyProfileEntity.EMAIL));
    map.put(ICompanyProfile.ALT_EMAIL, profile.getFieldValue(IGTCompanyProfileEntity.ALT_EMAIL));
    map.put(ICompanyProfile.TEL, profile.getFieldValue(IGTCompanyProfileEntity.TEL));
    map.put(ICompanyProfile.ALT_TEL, profile.getFieldValue(IGTCompanyProfileEntity.ALT_TEL));
    map.put(ICompanyProfile.FAX, profile.getFieldValue(IGTCompanyProfileEntity.FAX));
    map.put(ICompanyProfile.ADDRESS, profile.getFieldValue(IGTCompanyProfileEntity.ADDRESS));
    map.put(ICompanyProfile.CITY, profile.getFieldValue(IGTCompanyProfileEntity.CITY));
    map.put(ICompanyProfile.STATE, profile.getFieldValue(IGTCompanyProfileEntity.STATE));
    map.put(ICompanyProfile.ZIP_CODE, profile.getFieldValue(IGTCompanyProfileEntity.POSTCODE));
    map.put(ICompanyProfile.COUNTRY, profile.getFieldValue(IGTCompanyProfileEntity.COUNTRY));
    map.put(ICompanyProfile.LANGUAGE, profile.getFieldValue(IGTCompanyProfileEntity.LANGUAGE));
    map.put(ICompanyProfile.IS_PARTNER, profile.getFieldValue(IGTCompanyProfileEntity.IS_PARTNER));
    return map;
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Company Profiles may not be created");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_COMPANY_PROFILE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_COMPANY_PROFILE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new java.lang.UnsupportedOperationException("Company profiles may not be accessed by uid!");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new java.lang.UnsupportedOperationException("Company profile list may not be obtained");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("Company Profile may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_COMPANY_PROFILE.equals(entityType))
    {
      return new DefaultCompanyProfileEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }
}