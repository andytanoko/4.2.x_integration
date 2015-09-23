/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPackagingInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2003-12-08     Guo Jianyu          added "getDynamicType()"
 * 2003-12-22     Daniel D'Cotta      Moved Zip & ZipTreshold to FlowControlInfo
 * 2006-01-19			SC									Unsupport PackagingInfo.ZIP and PackagingInfo.ZIP_THRESHOLD
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.channel.CreatePackagingInfoEvent;
import com.gridnode.gtas.events.channel.DeletePackagingInfoEvent;
import com.gridnode.gtas.events.channel.GetPackagingInfoEvent;
import com.gridnode.gtas.events.channel.GetPackagingInfoListEvent;
import com.gridnode.gtas.events.channel.UpdatePackagingInfoEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultPackagingInfoManager extends DefaultAbstractManager
  implements IGTPackagingInfoManager
{

  DefaultPackagingInfoManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_PACKAGING_INFO, session);
  }

  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
    entity.setNewFieldValue(IGTPackagingInfoEntity.NAME, "");
    entity.setNewFieldValue(IGTPackagingInfoEntity.DESCRIPTION,"");
    entity.setNewFieldValue(IGTPackagingInfoEntity.ENVELOPE, "");
    //entity.setNewFieldValue(IGTPackagingInfoEntity.ZIP, Boolean.FALSE);
    //entity.setNewFieldValue(IGTPackagingInfoEntity.ZIP_THRESHOLD, new Integer(0));
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTPackagingInfoEntity pInfo = (IGTPackagingInfoEntity)entity;
    try
    {
      Long uid = pInfo.getUidLong();
      String name = pInfo.getFieldString(IGTPackagingInfoEntity.NAME);
      String description = pInfo.getFieldString(IGTPackagingInfoEntity.DESCRIPTION);
      String envelope = pInfo.getFieldString(IGTPackagingInfoEntity.ENVELOPE);
      //Boolean zip = (Boolean)pInfo.getFieldValue(IGTPackagingInfoEntity.ZIP);
      //Integer zipThreshold = (Integer)pInfo.getFieldValue(IGTPackagingInfoEntity.ZIP_THRESHOLD);
      HashMap as2 = convertAs2PackagingExtensionEntityToHashMap((IGTAs2PackagingInfoExtensionEntity)pInfo.getFieldValue(IGTPackagingInfoEntity.PKG_INFO_EXTENSION));

      UpdatePackagingInfoEvent event = new UpdatePackagingInfoEvent(
        uid,
        name,
        description,
        envelope,
        //zip,  //Commented by Jagadeesh for 2.3 P2P. 12/01/2004
        //zipThreshold, //Commented by Jagadeesh for 2.3 P2P. 12/01/2004
        as2
        //pInfo.getFieldString(pInfo.ENVELOPE) //Commented by Jagadeesh for 2.3 P2P. 12/01/2004
        //(Boolean)pInfo.getFieldValue(pInfo.ZIP),  //Commented by Jagadeesh for 2.3 P2P. 12/01/2004
        //(Integer)pInfo.getFieldValue(pInfo.ZIP_THRESHOLD)//Commented by Jagadeesh for 2.3 P2P. 12/01/2004
      );
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTPackagingInfoEntity pInfo = (IGTPackagingInfoEntity)entity;
    try
    {
      String name = pInfo.getFieldString(IGTPackagingInfoEntity.NAME);
      String description = pInfo.getFieldString(IGTPackagingInfoEntity.DESCRIPTION);
      String envelope = pInfo.getFieldString(IGTPackagingInfoEntity.ENVELOPE);
      //Boolean zip = (Boolean)pInfo.getFieldValue(IGTPackagingInfoEntity.ZIP);
      //Integer zipThreshold = (Integer)pInfo.getFieldValue(IGTPackagingInfoEntity.ZIP_THRESHOLD);
      Boolean isPartner = (Boolean)pInfo.getFieldValue(IGTPackagingInfoEntity.IS_PARTNER);
      HashMap as2 = convertAs2PackagingExtensionEntityToHashMap((IGTAs2PackagingInfoExtensionEntity)pInfo.getFieldValue(IGTPackagingInfoEntity.PKG_INFO_EXTENSION));

      CreatePackagingInfoEvent event = new CreatePackagingInfoEvent(
        name,
        description,
        envelope,
       // zip,        //Commented by Jagadeesh for 2.3 P2P. 12/01/2004
       // zipThreshold,  //Commented by Jagadeesh for 2.3 P2P.12/01/2004
        isPartner,
        as2 // 20031120 DDJ
      );
                                                                    //zip,
                                                                    //zipThreshold,
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }


  protected int getManagerType()
  {
    return IGTManager.MANAGER_PACKAGING_INFO;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PACKAGING_INFO;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPackagingInfoEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPackagingInfoListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeletePackagingInfoEvent(uids);
  }

  protected String getDynamicType(Number fieldId,
                                  IDynamicEntityConstraint constraint,
                                  Map serverMap,
                                  IGTEntity entity,
                                  int index)
    throws GTClientException
  {
    IGTPackagingInfoEntity packagingInfo = (IGTPackagingInfoEntity)entity;
    String envelopeType = (String)packagingInfo.getFieldValue(IGTPackagingInfoEntity.ENVELOPE);
    if(IGTPackagingInfoEntity.AS2_ENVELOPE_TYPE.equals(envelopeType))
    {
      return IGTEntity.ENTITY_AS2_PACKAGING_INFO_EXTENSION;
    }

    return null;
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PACKAGING_INFO.equals(entityType))
    {
      return new DefaultPackagingInfoEntity();
    }
    else if(IGTEntity.ENTITY_AS2_PACKAGING_INFO_EXTENSION.equals(entityType))
    {
      return new DefaultAs2PackagingInfoExtensionEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  public IGTAs2PackagingInfoExtensionEntity newAs2PackagingInfoExtension() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_AS2_PACKAGING_INFO_EXTENSION);
    entity.setNewEntity(true);
    return (IGTAs2PackagingInfoExtensionEntity)entity;
  }

  protected HashMap convertAs2PackagingExtensionEntityToHashMap(IGTAs2PackagingInfoExtensionEntity as2) throws GTClientException
  { // 20031120 DDJ
    HashMap map = new HashMap();
    if(as2 != null)
    {
      map.put(IGTAs2PackagingInfoExtensionEntity.IS_ACK_REQ, as2.getFieldValue(IGTAs2PackagingInfoExtensionEntity.IS_ACK_REQ));
      map.put(IGTAs2PackagingInfoExtensionEntity.IS_ACK_SIGNED, as2.getFieldValue(IGTAs2PackagingInfoExtensionEntity.IS_ACK_SIGNED));
      map.put(IGTAs2PackagingInfoExtensionEntity.IS_NRR_REQ, as2.getFieldValue(IGTAs2PackagingInfoExtensionEntity.IS_NRR_REQ));
      map.put(IGTAs2PackagingInfoExtensionEntity.IS_ACK_SYN, as2.getFieldValue(IGTAs2PackagingInfoExtensionEntity.IS_ACK_SYN));
      map.put(IGTAs2PackagingInfoExtensionEntity.RETURN_URL, as2.getFieldValue(IGTAs2PackagingInfoExtensionEntity.RETURN_URL));
    }
    return map;
  }
}