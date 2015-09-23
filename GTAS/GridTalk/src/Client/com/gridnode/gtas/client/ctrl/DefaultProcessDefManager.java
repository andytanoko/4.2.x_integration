/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultProcessDefManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-02-14     Daniel D'Cotta      Added new fields to both ProcessDef and
 *                                    ProcessAct
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2003-08-21     Andrew Hill         userTrackingIdentifier support
 * 2007-11-07     Tam Wei Xiang       Add in new field "is_compress_required"
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.rnif.CreateProcessDefEvent;
import com.gridnode.gtas.events.rnif.DeleteProcessDefEvent;
import com.gridnode.gtas.events.rnif.GetProcessDefEvent;
import com.gridnode.gtas.events.rnif.GetProcessDefListEvent;
import com.gridnode.gtas.events.rnif.UpdateProcessDefEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultProcessDefManager extends DefaultAbstractManager
  implements IGTProcessDefManager
{
  DefaultProcessDefManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PROCESS_DEF, session);
  }

  private HashMap extractProcessAct(IGTProcessDefEntity processDef, Number processActKey)
    throws GTClientException
  {
    try
    {
      if(!IGTProcessActEntity.REQUEST_ACT.equals(processActKey) && !IGTProcessActEntity.RESPONSE_ACT.equals(processActKey))
        throw new java.lang.IllegalArgumentException("Invalid field for ProcessAct=" + processActKey);

      IGTProcessActEntity processAct = (IGTProcessActEntity)processDef.getFieldValue(processActKey);
      if(processAct != null)
      {
        HashMap processActFields = new HashMap();
        processActFields.put(IGTProcessActEntity.MSG_TYPE,                     (Long)processAct.getFieldValue(IGTProcessActEntity.MSG_TYPE));
        processActFields.put(IGTProcessActEntity.DICT_FILE,                    (Long)processAct.getFieldValue(IGTProcessActEntity.DICT_FILE));
        processActFields.put(IGTProcessActEntity.XML_SCHEMA,                   (Long)processAct.getFieldValue(IGTProcessActEntity.XML_SCHEMA));

        processActFields.put(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER,      processAct.getFieldString(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER));
        processActFields.put(IGTProcessActEntity.BIZ_ACTION_CODE,              processAct.getFieldString(IGTProcessActEntity.BIZ_ACTION_CODE));
        processActFields.put(IGTProcessActEntity.RETRIES,                      processAct.getFieldString(IGTProcessActEntity.RETRIES));
        processActFields.put(IGTProcessActEntity.TIME_TO_ACKNOWLEDGE,          processAct.getFieldString(IGTProcessActEntity.TIME_TO_ACKNOWLEDGE));

        processActFields.put(IGTProcessActEntity.IS_AUTHORIZATION_REQUIRED,    processAct.getFieldString(IGTProcessActEntity.IS_AUTHORIZATION_REQUIRED));
        processActFields.put(IGTProcessActEntity.IS_NON_REPUDIATION_REQUIRED,  processAct.getFieldString(IGTProcessActEntity.IS_NON_REPUDIATION_REQUIRED));
        processActFields.put(IGTProcessActEntity.IS_SECURE_TRANSPORT_REQUIRED, processAct.getFieldString(IGTProcessActEntity.IS_SECURE_TRANSPORT_REQUIRED));
        processActFields.put(IGTProcessActEntity.DISABLE_DTD,                  (Boolean)processAct.getFieldValue(IGTProcessActEntity.DISABLE_DTD));
        processActFields.put(IGTProcessActEntity.DISABLE_SCHEMA,               (Boolean)processAct.getFieldValue(IGTProcessActEntity.DISABLE_SCHEMA));
        processActFields.put(IGTProcessActEntity.VALIDATE_AT_SENDER,           (Boolean)processAct.getFieldValue(IGTProcessActEntity.VALIDATE_AT_SENDER));

        processActFields.put(IGTProcessActEntity.DISABLE_ENCRYPTION,           (Boolean)processAct.getFieldValue(IGTProcessActEntity.DISABLE_ENCRYPTION));
        processActFields.put(IGTProcessActEntity.DISABLE_SIGNATURE,            (Boolean)processAct.getFieldValue(IGTProcessActEntity.DISABLE_SIGNATURE));
        processActFields.put(IGTProcessActEntity.ONLY_ENCRYPT_PAYLOAD,         (Boolean)processAct.getFieldValue(IGTProcessActEntity.ONLY_ENCRYPT_PAYLOAD));
        processActFields.put(IGTProcessActEntity.DIGEST_ALGORITHM,             processAct.getFieldString(IGTProcessActEntity.DIGEST_ALGORITHM));
        processActFields.put(IGTProcessActEntity.ENCRYPTION_ALGORITHM,         processAct.getFieldString(IGTProcessActEntity.ENCRYPTION_ALGORITHM));
        processActFields.put(IGTProcessActEntity.ENCRYPTION_ALGORITHM_LENGTH,  (Integer)processAct.getFieldValue(IGTProcessActEntity.ENCRYPTION_ALGORITHM_LENGTH));
        
        processActFields.put(processAct.IS_COMPRESS_REQUIRED,          (Boolean)processAct.getFieldValue(processAct.IS_COMPRESS_REQUIRED));
        
        return processActFields;
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting procDef from " + processDef);
    }
  }

  private HashMap extractOtherFields(IGTProcessDefEntity processDef)
    throws GTClientException
  {
    try
    {
      if(processDef != null)
      {
        HashMap otherFields = new HashMap();
        otherFields.put(IGTProcessDefEntity.DEF_NAME,                            processDef.getFieldString(IGTProcessDefEntity.DEF_NAME));
        otherFields.put(IGTProcessDefEntity.IS_SYNCHRONOUS,                      (Boolean)processDef.getFieldValue(IGTProcessDefEntity.IS_SYNCHRONOUS));
        otherFields.put(IGTProcessDefEntity.PROCESS_TYPE,                        processDef.getFieldString(IGTProcessDefEntity.PROCESS_TYPE));
        otherFields.put(IGTProcessDefEntity.ACTION_TIME_OUT,                     (Integer)processDef.getFieldValue(IGTProcessDefEntity.ACTION_TIME_OUT));
        otherFields.put(IGTProcessDefEntity.RNIF_VERSION,                        processDef.getFieldString(IGTProcessDefEntity.RNIF_VERSION));
        otherFields.put(IGTProcessDefEntity.PROCESS_INDICATOR_CODE,              processDef.getFieldString(IGTProcessDefEntity.PROCESS_INDICATOR_CODE));
        otherFields.put(IGTProcessDefEntity.VERSION_IDENTIFIER,                  processDef.getFieldString(IGTProcessDefEntity.VERSION_IDENTIFIER));
        otherFields.put(IGTProcessDefEntity.USAGE_CODE,                          processDef.getFieldString(IGTProcessDefEntity.USAGE_CODE));
        //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
        //otherFields.put(IGTProcessDefEntity.DIGEST_ALG_CODE,                     processDef.getFieldString(IGTProcessDefEntity.DIGEST_ALG_CODE));

        otherFields.put(IGTProcessDefEntity.FROM_PARTNER_ROLE_CLASS_CODE,        processDef.getFieldString(IGTProcessDefEntity.FROM_PARTNER_ROLE_CLASS_CODE));
        otherFields.put(IGTProcessDefEntity.FROM_BIZ_SERVICE_CODE,               processDef.getFieldString(IGTProcessDefEntity.FROM_BIZ_SERVICE_CODE));
        otherFields.put(IGTProcessDefEntity.FROM_PARTNER_CLASS_CODE,             processDef.getFieldString(IGTProcessDefEntity.FROM_PARTNER_CLASS_CODE));

        otherFields.put(IGTProcessDefEntity.TO_PARTNER_ROLE_CLASS_CODE,          processDef.getFieldString(IGTProcessDefEntity.TO_PARTNER_ROLE_CLASS_CODE));
        otherFields.put(IGTProcessDefEntity.TO_BIZ_SERVICE_CODE,                 processDef.getFieldString(IGTProcessDefEntity.TO_BIZ_SERVICE_CODE));
        otherFields.put(IGTProcessDefEntity.TO_PARTNER_CLASS_CODE,               processDef.getFieldString(IGTProcessDefEntity.TO_PARTNER_CLASS_CODE));

        otherFields.put(IGTProcessDefEntity.REQUEST_DOC_THIS_DOC_IDENTIFIER,     processDef.getFieldString(IGTProcessDefEntity.REQUEST_DOC_THIS_DOC_IDENTIFIER));
        otherFields.put(IGTProcessDefEntity.RESPONSE_DOC_THIS_DOC_IDENTIFIER,    processDef.getFieldString(IGTProcessDefEntity.RESPONSE_DOC_THIS_DOC_IDENTIFIER));
        otherFields.put(IGTProcessDefEntity.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER, processDef.getFieldString(IGTProcessDefEntity.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER));

        otherFields.put(IGTProcessDefEntity.USER_TRACKING_IDENTIFIER,            processDef.getFieldString(IGTProcessDefEntity.USER_TRACKING_IDENTIFIER)); //20030821AH
        
        //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
        //otherFields.put(processDef.DISABLE_DTD,                       (Boolean)processDef.getFieldValue(processDef.DISABLE_DTD));
        //otherFields.put(processDef.DISABLE_SCHEMA,                    (Boolean)processDef.getFieldValue(processDef.DISABLE_SCHEMA));
        //otherFields.put(processDef.DISABLE_SIGNATURE,                 (Boolean)processDef.getFieldValue(processDef.DISABLE_SIGNATURE));
        //otherFields.put(processDef.DISABLE_ENCRYPTION,                (Boolean)processDef.getFieldValue(processDef.DISABLE_ENCRYPTION));
        //otherFields.put(processDef.ENABLE_ENCRYPT_PAYLOAD,            (Boolean)processDef.getFieldValue(processDef.ENABLE_ENCRYPT_PAYLOAD));
        //otherFields.put(processDef.VALIDATE_AT_SENDER,                (Boolean)processDef.getFieldValue(processDef.VALIDATE_AT_SENDER));
        return otherFields;
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting otherFields from " + processDef);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTProcessDefEntity processDef = (IGTProcessDefEntity)entity;

      Long uid = (Long)processDef.getFieldValue(IGTProcessDefEntity.UID);
      HashMap otherFields = extractOtherFields(processDef);
      HashMap requestAct  = extractProcessAct(processDef, IGTProcessDefEntity.REQUEST_ACT);
      HashMap responseAct = null; // must return null; and not empty HashMap
      if(IGTProcessDefEntity.TYPE_TWO_ACTION.equals(processDef.getFieldString(IGTProcessDefEntity.PROCESS_TYPE)))
        responseAct = extractProcessAct(processDef, IGTProcessDefEntity.RESPONSE_ACT);

      UpdateProcessDefEvent event = new UpdateProcessDefEvent(uid, otherFields, requestAct, responseAct);

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
      IGTProcessDefEntity processDef = (IGTProcessDefEntity)entity;

      HashMap otherFields = extractOtherFields(processDef);
      HashMap requestAct  = extractProcessAct(processDef, IGTProcessDefEntity.REQUEST_ACT);
      HashMap responseAct = null; // must return null; and not empty HashMap
      if(IGTProcessDefEntity.TYPE_TWO_ACTION.equals(processDef.getFieldString(IGTProcessDefEntity.PROCESS_TYPE)))
        responseAct = extractProcessAct(processDef, IGTProcessDefEntity.RESPONSE_ACT);

      CreateProcessDefEvent event = new CreateProcessDefEvent(otherFields, requestAct, responseAct);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PROCESS_DEF;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PROCESS_DEF;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcessDefEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcessDefListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteProcessDefEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PROCESS_DEF.equals(entityType))
    {
      return new DefaultProcessDefEntity();
    }
    else if(IGTEntity.ENTITY_PROCESS_ACT.equals(entityType))
    {
      return new DefaultProcessActEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  protected String getDynamicType(Number fieldId,
                                  IDynamicEntityConstraint constraint,
                                  Map serverMap,
                                  IGTEntity entity,
                                  int index)
    throws GTClientException
  {
    if(fieldId.equals(IGTProcessDefEntity.RESPONSE_ACT))
      return IGTEntity.ENTITY_PROCESS_ACT;
    else
      throw new java.lang.UnsupportedOperationException("Cannot get type for field id=" + fieldId
                                                        + " index=" + index
                                                        + " for entity:" + entity);
  }

  public IGTProcessActEntity newProcessAct() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_PROCESS_ACT);
    entity.setNewEntity(true);
    return (IGTProcessActEntity)entity;
  }
}