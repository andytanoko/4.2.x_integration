/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultBusinessEntityManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 * 2002-08-30     Andrew Hill         Added send method
 * 2002-10-08     Andrew Hill         "partnerCat" stuff
 * 2003-01-15     Andrew Hill         send(beUids,gnUids)
 * 2003-07-18     Andrew Hill         Support for multiple deletion events, remove deprecated send method
 * 2004-01-02     Daniel D'Cotta      Added DomainIdentifiers
 * 2006-01-23	  SC	              For domain identifier, don't pass UID field to server layer anymore.
 * 2006-02-10     Neo Sok Lay         Temporary undo the above change by SC
 * 2008-07-17	  Teh Yu Phei		  Add activate (Ticket 31)
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.bizreg.ActivateBusinessEntityEvent;
import com.gridnode.gtas.events.bizreg.CreateBusinessEntityEvent;
import com.gridnode.gtas.events.bizreg.DeleteBusinessEntityEvent;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityEvent;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityListEvent;
import com.gridnode.gtas.events.bizreg.UpdateBusinessEntityEvent;
import com.gridnode.gtas.events.enterprise.GetChannelListForBizEntityEvent;
import com.gridnode.gtas.events.enterprise.SendBusinessEntityEvent;
import com.gridnode.gtas.events.enterprise.SetChannelListForBizEntityEvent;
import com.gridnode.gtas.model.bizreg.IWhitePage;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultBusinessEntityManager extends DefaultAbstractManager
  implements IGTBusinessEntityManager
{
  DefaultBusinessEntityManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_BUSINESS_ENTITY, session);
  }

  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
    entity.setNewFieldValue(IGTBusinessEntityEntity.STATE,IGTBusinessEntityEntity.STATE_NORMAL);
  }

  /*20030718AH - co: public void send(Collection beUids, String toEnterpriseId, Long viaChannelUid)
    throws GTClientException
  {
    if(beUids == null)
    {
      throw new java.lang.NullPointerException("null beUids");
    }
    if(toEnterpriseId == null)
    {
      throw new java.lang.NullPointerException("null toEnterpriseId");
    }
    if(viaChannelUid == null)
    {
      throw new java.lang.NullPointerException("null viaChannelUid");
    }
    if(beUids.isEmpty())
    {
      throw new GTClientException("No BusinessEntities specified");
    }
    try
    {
      //Following is deprecated. Later to remove
      SendBusinessEntityEvent event = new SendBusinessEntityEvent(beUids,toEnterpriseId,viaChannelUid);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error sending BusinessEntities",t);
    }
  }*/

  public void send(Collection beUids, Collection gnUids)
    throws GTClientException
  {
    if(beUids == null)
    {
      throw new java.lang.NullPointerException("null beUids");
    }
    if(gnUids == null)
    {
      throw new java.lang.NullPointerException("null gnUids");
    }
    if(beUids.isEmpty())
    {
      throw new GTClientException("No BusinessEntities specified");
    }
    if(gnUids.isEmpty())
    {
      throw new GTClientException("No GridNodes specified");
    }
    try
    {
      SendBusinessEntityEvent event = new SendBusinessEntityEvent(beUids,gnUids);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error sending BusinessEntities",t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)entity;
      boolean doUpdateChannels = entity.isFieldDirty(IGTBusinessEntityEntity.CHANNELS);
      Collection channels = null;
      if(doUpdateChannels)
      {
        channels = (Collection)be.getFieldValue(IGTBusinessEntityEntity.CHANNELS);
      }
      Collection domainIdentifiers = convertDomainIdentifierEntitiesToMaps((List)be.getFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS));
      UpdateBusinessEntityEvent event = new UpdateBusinessEntityEvent(be.getUidLong(),
                                                                      be.getFieldString(IGTBusinessEntityEntity.DESCRIPTION),
                                                                      getWhitePageMap(be),
                                                                      domainIdentifiers);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
      if(doUpdateChannels)
      {
        updateChannels(be,channels);
      }
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
      IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)entity;
      Boolean isPartnerBEObject = (Boolean)be.getFieldValue(IGTBusinessEntityEntity.IS_PARTNER);
      boolean isPartnerBE = isPartnerBEObject.booleanValue();
      boolean doUpdateChannels = entity.isFieldDirty(IGTBusinessEntityEntity.CHANNELS);
      Collection channels = null;
      if(doUpdateChannels)
      {
        channels = (Collection)be.getFieldValue(IGTBusinessEntityEntity.CHANNELS);
      }
      Collection domainIdentifiers = convertDomainIdentifierEntitiesToMaps((List)be.getFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS));
      CreateBusinessEntityEvent event = new CreateBusinessEntityEvent(be.getFieldString(IGTBusinessEntityEntity.ID),
                                                                      be.getFieldString(IGTBusinessEntityEntity.DESCRIPTION),
                                                                      isPartnerBE,
                                                                      getWhitePageMap(be),
                                                                      domainIdentifiers);
      handleCreateEvent(event, (AbstractGTEntity)entity);
      if(doUpdateChannels)
      {
        updateChannels(be,channels);
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  public void updateChannels(IGTBusinessEntityEntity entity, Collection channels)
    throws GTClientException
  {
    if(entity == null)
    {
      throw new java.lang.NullPointerException("null entity reference");
    }
    if(channels == null)
    {
      channels = new Vector(0);
    }
    try
    {
      SetChannelListForBizEntityEvent event = new SetChannelListForBizEntityEvent(entity.getUidLong(),channels);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating channels list for BusinessEntity entity",t);
    }
  }

  protected Map getWhitePageMap(IGTBusinessEntityEntity be)
    throws GTClientException
  {
    try
    {
      IGTWhitePageEntity whitePage = (IGTWhitePageEntity)be.getFieldValue(IGTBusinessEntityEntity.WHITE_PAGE);
      if(whitePage == null)
      {
        throw new java.lang.NullPointerException("Null whitePage entity reference in WHITE_PAGE field of BE");
      }
      Map map = new HashMap();
      map.put( IWhitePage.BE_UID, whitePage.getFieldValue(IGTWhitePageEntity.BE_UID) );
      map.put( IWhitePage.BUSINESS_DESC, whitePage.getFieldValue(IGTWhitePageEntity.BUSINESS_DESC) );
      map.put( IWhitePage.DUNS, whitePage.getFieldValue(IGTWhitePageEntity.DUNS) );
      map.put( IWhitePage.G_SUPPLY_CHAIN_CODE, whitePage.getFieldValue(IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE) );
      map.put( IWhitePage.CONTACT_PERSON, whitePage.getFieldValue(IGTWhitePageEntity.CONTACT_PERSON) );
      map.put( IWhitePage.EMAIL, whitePage.getFieldValue(IGTWhitePageEntity.EMAIL) );
      map.put( IWhitePage.TEL, whitePage.getFieldValue(IGTWhitePageEntity.TEL) );
      map.put( IWhitePage.FAX, whitePage.getFieldValue(IGTWhitePageEntity.FAX) );
      map.put( IWhitePage.WEBSITE, whitePage.getFieldValue(IGTWhitePageEntity.WEBSITE) );
      map.put( IWhitePage.ADDRESS, whitePage.getFieldValue(IGTWhitePageEntity.ADDRESS) );
      map.put( IWhitePage.PO_BOX, whitePage.getFieldValue(IGTWhitePageEntity.PO_BOX) );
      map.put( IWhitePage.CITY, whitePage.getFieldValue(IGTWhitePageEntity.CITY) );
      map.put( IWhitePage.STATE, whitePage.getFieldValue(IGTWhitePageEntity.STATE) );
      map.put( IWhitePage.ZIP_CODE, whitePage.getFieldValue(IGTWhitePageEntity.POSTCODE) );
      map.put( IWhitePage.COUNTRY, whitePage.getFieldValue(IGTWhitePageEntity.COUNTRY) );
      map.put( IWhitePage.LANGUAGE, whitePage.getFieldValue(IGTWhitePageEntity.LANGUAGE) );
      return map;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error creating Map from whitePage entity for use in event",t);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_BUSINESS_ENTITY;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_BUSINESS_ENTITY;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetBusinessEntityEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetBusinessEntityListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteBusinessEntityEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_BUSINESS_ENTITY.equals(entityType))
    {
      return new DefaultBusinessEntityEntity();
    }
    else if(IGTEntity.ENTITY_WHITE_PAGE.equals(entityType))
    {
      return new DefaultWhitePageEntity();
    }
    else if(IGTEntity.ENTITY_DOMAIN_IDENTIFIER.equals(entityType))
    {
      return new DefaultDomainIdentifierEntity();
    }
    throw new java.lang.IllegalArgumentException("Manager:" + this + " cannot create entities of type " + entityType);
  }

  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      if(IGTBusinessEntityEntity.CHANNELS.equals(fieldId))
      {
        if(entity.isNewEntity())
        {
          entity.setNewFieldValue(IGTBusinessEntityEntity.CHANNELS, null);
        }
        else
        {
          IForeignEntityConstraint constraint = (IForeignEntityConstraint)
                                                getSharedFieldMetaInfo(IGTEntity.ENTITY_BUSINESS_ENTITY,
                                                fieldId).getConstraint();
          GetChannelListForBizEntityEvent event = new GetChannelListForBizEntityEvent(entity.getUidLong());
          EntityListResponseData results = (EntityListResponseData)handleEvent(event);
          Collection channels = (Collection)results.getEntityList();
          channels = processMapCollection(constraint,channels);
          channels = extractKeys(constraint,channels);
          entity.setNewFieldValue(IGTBusinessEntityEntity.CHANNELS, channels);
        }
      }
      else
      {
        throw new java.lang.IllegalStateException("Field " + fieldId + " of entity " + entity
                                                  + " is not load-on-demand");
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_BUSINESS_ENTITY.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[1];

      sharedVfmi[0] = new VirtualSharedFMI("businessEntity.channels", IGTBusinessEntityEntity.CHANNELS);
      sharedVfmi[0].setCollection(true);
      sharedVfmi[0].setValueClass("java.utils.Vector");
      sharedVfmi[0].setElementClass("java.lang.Long");
      Properties detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","channelInfo.uid");
      detail.setProperty("foreign.display","channelInfo.name");
      detail.setProperty("foreign.cached","false");
      detail.setProperty("foreign.additionalDisplay","description"); //20021227AH
      IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    entity.setNewFieldValue(IGTBusinessEntityEntity.CHANNELS, new UnloadedFieldToken());
  }

  public IGTDomainIdentifierEntity newDomainIdentifier() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_DOMAIN_IDENTIFIER);
    entity.setNewEntity(true);
    return (IGTDomainIdentifierEntity)entity;
  }
  
  protected List convertDomainIdentifierEntitiesToMaps(List domainIdentifierEntities) throws GTClientException
  {
    List domainIdentifierMaps = new ArrayList(domainIdentifierEntities.size());
    
    Iterator i = domainIdentifierEntities.iterator();
    while(i.hasNext())
    {
      IGTDomainIdentifierEntity domainIdentifier = (IGTDomainIdentifierEntity)i.next();
      String type   = domainIdentifier.getFieldString(IGTDomainIdentifierEntity.TYPE);
      String value  = domainIdentifier.getFieldString(IGTDomainIdentifierEntity.VALUE);
      
      /* 23 Jan 06 [SC] Don't pass UID to server layer anymore (prevent server layer throwing exception) */
      Object uid  = domainIdentifier.getFieldValue(IGTDomainIdentifierEntity.UID);

      Map map = new HashMap();
      map.put(IGTDomainIdentifierEntity.TYPE, type);
      map.put(IGTDomainIdentifierEntity.VALUE, value);
      map.put(IGTDomainIdentifierEntity.UID, uid);
      
      domainIdentifierMaps.add(map);
    }
    
    return domainIdentifierMaps;
  }

  public void activate(long[] bizEntities) throws GTClientException
  {
	  try
	  {
	      ActivateBusinessEntityEvent event = new ActivateBusinessEntityEvent(bizEntities);
	      handleEvent(event);
	    }
	    catch(Exception e)
	    {
	      throw new GTClientException("GTAS Error attempting to activate", e);
	    }
	}
}