/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSecurityInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.channel.CreateSecurityInfoEvent;
import com.gridnode.gtas.events.channel.DeleteSecurityInfoEvent;
import com.gridnode.gtas.events.channel.GetSecurityInfoEvent;
import com.gridnode.gtas.events.channel.GetSecurityInfoListEvent;
import com.gridnode.gtas.events.channel.UpdateSecurityInfoEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultSecurityInfoManager extends DefaultAbstractManager
  implements IGTSecurityInfoManager
{

  DefaultSecurityInfoManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_SECURITY_INFO, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTSecurityInfoEntity sInfo = (IGTSecurityInfoEntity)entity;
    try
    {
      Long uid = sInfo.getUidLong();
      String name = sInfo.getFieldString(IGTSecurityInfoEntity.NAME);
      String description = sInfo.getFieldString(IGTSecurityInfoEntity.DESCRIPTION);
      String encType = sInfo.getFieldString(IGTSecurityInfoEntity.ENC_TYPE);
      Integer encLevel = (Integer)sInfo.getFieldValue(IGTSecurityInfoEntity.ENC_LEVEL);
      Long encCertUid = (Long)sInfo.getFieldValue(IGTSecurityInfoEntity.ENC_CERT);
      String sigType = sInfo.getFieldString(IGTSecurityInfoEntity.SIG_TYPE);
      String digestAlgorithm = sInfo.getFieldString(IGTSecurityInfoEntity.DIGEST_ALGORITHM);
      Long sigEncCertUid = (Long)sInfo.getFieldValue(IGTSecurityInfoEntity.SIG_ENC_CERT);
      String compressionType = sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_TYPE);            // 20031126 DDJ
      String compressionMethod = sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_METHOD);        // 20031126 DDJ
      Integer compressionLevel = (Integer)sInfo.getFieldValue(IGTSecurityInfoEntity.COMPRESSION_LEVEL); // 20031126 DDJ
      String sequence = sInfo.getFieldString(IGTSecurityInfoEntity.SEQUENCE);                           // 20031126 DDJ
      String encryptionAlgorithm = sInfo.getFieldString(IGTSecurityInfoEntity.ENCRYPTION_ALGORITHM);    // 20031126 DDJ
      
      UpdateSecurityInfoEvent event = new UpdateSecurityInfoEvent(
        uid,
        name,
        description,
        encType,
        encLevel,
        encCertUid,
        sigType,
        digestAlgorithm,
        sigEncCertUid,
        compressionType,      // 20031126 DDJ
        compressionMethod,    // 20031126 DDJ
        compressionLevel,     // 20031126 DDJ
        sequence,             // 20031126 DDJ
        encryptionAlgorithm   // 20031126 DDJ
      );
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
    entity.setNewFieldValue(IGTSecurityInfoEntity.NAME, "");
    entity.setNewFieldValue(IGTSecurityInfoEntity.DESCRIPTION, "");
    entity.setNewFieldValue(IGTSecurityInfoEntity.ENC_TYPE, IGTSecurityInfoEntity.ENC_TYPE_NONE);
    entity.setNewFieldValue(IGTSecurityInfoEntity.SIG_TYPE, IGTSecurityInfoEntity.SIG_TYPE_NONE);
    entity.setNewFieldValue(IGTSecurityInfoEntity.DIGEST_ALGORITHM, "");
    entity.setNewFieldValue(IGTSecurityInfoEntity.IS_PARTNER, Boolean.FALSE);
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTSecurityInfoEntity sInfo = (IGTSecurityInfoEntity)entity;
    try
    {
      String name = sInfo.getFieldString(IGTSecurityInfoEntity.NAME);
      String description = sInfo.getFieldString(IGTSecurityInfoEntity.DESCRIPTION);
      String encType = sInfo.getFieldString(IGTSecurityInfoEntity.ENC_TYPE);
      Integer encLevel = (Integer)sInfo.getFieldValue(IGTSecurityInfoEntity.ENC_LEVEL);
      Long encCertUid = (Long)sInfo.getFieldValue(IGTSecurityInfoEntity.ENC_CERT);
      String sigType = sInfo.getFieldString(IGTSecurityInfoEntity.SIG_TYPE);
      String digestAlgorithm = sInfo.getFieldString(IGTSecurityInfoEntity.DIGEST_ALGORITHM);
      Long sigEncCertUid = (Long)sInfo.getFieldValue(IGTSecurityInfoEntity.SIG_ENC_CERT);
      Boolean isPartner = (Boolean)sInfo.getFieldValue(IGTSecurityInfoEntity.IS_PARTNER);
      String compressionType = sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_TYPE);            // 20031126 DDJ
      String compressionMethod = sInfo.getFieldString(IGTSecurityInfoEntity.COMPRESSION_METHOD);        // 20031126 DDJ
      Integer compressionLevel = (Integer)sInfo.getFieldValue(IGTSecurityInfoEntity.COMPRESSION_LEVEL); // 20031126 DDJ
      String sequence = sInfo.getFieldString(IGTSecurityInfoEntity.SEQUENCE);                           // 20031126 DDJ
      String encryptionAlgorithm = sInfo.getFieldString(IGTSecurityInfoEntity.ENCRYPTION_ALGORITHM);    // 20031126 DDJ

      CreateSecurityInfoEvent event = new CreateSecurityInfoEvent(
        name,
        description,
        encType,
        encLevel,
        encCertUid,
        sigType,
        digestAlgorithm,
        sigEncCertUid,
        isPartner,
        compressionType,      // 20031126 DDJ
        compressionMethod,    // 20031126 DDJ
        compressionLevel,     // 20031126 DDJ
        sequence,             // 20031126 DDJ
        encryptionAlgorithm   // 20031126 DDJ
      );
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }


  protected int getManagerType()
  {
    return IGTManager.MANAGER_SECURITY_INFO;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_SECURITY_INFO;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetSecurityInfoEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetSecurityInfoListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteSecurityInfoEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_SECURITY_INFO.equals(entityType))
    {
      return new DefaultSecurityInfoEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  protected void setDefaultFieldValue(AbstractGTEntity entity)
    throws GTClientException
  {
    entity.setNewFieldValue(IGTSecurityInfoEntity.IS_PARTNER, Boolean.FALSE);
    entity.setNewFieldValue(IGTSecurityInfoEntity.PARTNER_CAT, null);
    entity.setNewFieldValue(IGTSecurityInfoEntity.ENC_TYPE, IGTSecurityInfoEntity.ENC_TYPE_NONE);
    entity.setNewFieldValue(IGTSecurityInfoEntity.SIG_TYPE, IGTSecurityInfoEntity.SIG_TYPE_NONE);
  }
}