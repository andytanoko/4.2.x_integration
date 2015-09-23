/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultLookupPropertiesManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 30 Dec 2005		SC									Created
 */

package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Properties;

import javax.naming.Context;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.AssertUtil;

class DefaultLookupPropertiesManager extends DefaultAbstractManager
  implements IGTLookupPropertiesManager
{
	public static final String[] CONTEXT_PROPERTIES = 
	{
		Context.APPLET,
		Context.AUTHORITATIVE,
	  Context.BATCHSIZE,
	  Context.DNS_URL,
	  Context.INITIAL_CONTEXT_FACTORY,
	  Context.LANGUAGE,
	  Context.OBJECT_FACTORIES,
	  Context.PROVIDER_URL,
	  Context.REFERRAL,
	  Context.SECURITY_AUTHENTICATION,
	  Context.SECURITY_CREDENTIALS,
	  Context.SECURITY_PRINCIPAL,
	  Context.SECURITY_PROTOCOL,
	  Context.STATE_FACTORIES,
	  Context.URL_PKG_PREFIXES
	};
	
	public IGTLookupPropertiesEntity newLookupProperties() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_VIRTUAL_LOOKUP_PROPERTIES);
    entity.setNewEntity(true);
    return (IGTLookupPropertiesEntity) entity;
  }
	
	DefaultLookupPropertiesManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_VIRTUAL_LOOKUP_PROPERTIES, session);
  }

  protected void doUpdate(IGTEntity entity) throws GTClientException
  {
    throw new UnsupportedOperationException("LookupProperties may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
  	throw new UnsupportedOperationException("LookupProperties cannot be created in this way.");
  	
//    if(IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals( entity.getType() ))
//    {
//      try
//      {
//        DefaultArchiveDocumentEntity da = (DefaultArchiveDocumentEntity)entity;
//        /*    
//        IDataFilter gdocFilter = (IDataFilter)da.getFieldValue(da.GDOC_FILTER);      
//        ArchiveDocumentEvent event = new ArchiveDocumentEvent(gdocFilter);
//        */
//        ArchiveDocumentEvent event = new ArchiveDocumentEvent();
//        event.setArchiveName        (entity.getFieldString(IGTArchiveDocumentEntity.NAME));
//        event.setArchiveDescription (entity.getFieldString(IGTArchiveDocumentEntity.DESCRIPTION));
//
//        Date fromDate = (Date)entity.getFieldValue(IGTArchiveDocumentEntity.FROM_DATE);
//        Date toDate   = (Date)entity.getFieldValue(IGTArchiveDocumentEntity.TO_DATE);
//
//        String fromTimeStr = entity.getFieldString(IGTArchiveDocumentEntity.FROM_TIME);
//        String toTimeStr   = entity.getFieldString(IGTArchiveDocumentEntity.TO_TIME);
//
//        Date fromTime = DateUtils.parseDate(fromTimeStr, null, null, new SimpleDateFormat("HH:mm"));
//        Date toTime   = DateUtils.parseDate(toTimeStr,   null, null, new SimpleDateFormat("HH:mm"));
//        
//        Timestamp fromTimeStamp  = new Timestamp(fromDate.getTime() + fromTime.getTime());
//        Timestamp toTimeStamp    = new Timestamp(toDate.getTime()   + toTime.getTime());
//
//        event.setFromStartTime      (fromTimeStamp);
//        event.setToStartTime        (toTimeStamp);
//        
//        String archiveType = entity.getFieldString(IGTArchiveDocumentEntity.ARCHIVE_TYPE);
//        event.setArchiveType        (archiveType);
//
//        if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
//        {
//          List processDef = (List)entity.getFieldValue(IGTArchiveDocumentEntity.PROCESS_DEF);
//          Boolean includeIncomplete = (Boolean)entity.getFieldValue(IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE);
//          
//          event.setProcessDefNameList         (processDef);
//          event.setIncludeIncompleteProcesses (includeIncomplete);
//
//          //event.setProcessInstanceFilter(getProcessInstanceFilter(fromTime, toTime, includeIncomplete.booleanValue()));
//        }
//        else if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
//        {
//          List folder   = (List)entity.getFieldValue(IGTArchiveDocumentEntity.FOLDER);
//          List docType  = (List)entity.getFieldValue(IGTArchiveDocumentEntity.DOC_TYPE);
//
//          event.setFolderList       (folder);
//          event.setDocumentTypeList (docType);
//
//          //event.setGDocFilter(getDocumentFilter(fromTime, toTime, folder, docType));
//        }
//        else
//        {
//          throw new GTClientException("Invalid archiveType:" + archiveType);
//        }
//        
//        Map results = (Map)((DefaultGTSession)_session).fireEvent(event);       
//        
//        if(results == null)
//        { // Internal sanity check
//          throw new NullPointerException("ArchiveDocumentEvent event return null result map");
//        }
//        //initVirtualEntityFields(da.getType(), da, results); commented by mahesh
//        da.setNewEntity(false);
//        da.setEntityDirty(false);
//      }
//      catch(Throwable t)
//      {
//        throw new GTClientException("Error creating ArchiveDocument", t);
//      }
//    }
//    else
//    {
//      throw new UnsupportedOperationException("Bad entity:" + entity);
//    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_VIRTUAL_LOOKUP_PROPERTIES;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_VIRTUAL_LOOKUP_PROPERTIES;
  }

  protected IEvent getGetEvent(Long uid)
    throws EventException
  {
    throw new UnsupportedOperationException("No get event for this type");
  }

  protected IEvent getGetListEvent(IDataFilter filter)
    throws EventException
  {
    throw new UnsupportedOperationException("No get list event for this type");
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    throw new UnsupportedOperationException("No delete event for this type");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
  	assertLookupProperties(entityType);
  	return new DefaultLookupPropertiesEntity();
  	
//    if(IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals(entityType))
//    {
//      return new DefaultArchiveDocumentEntity();
//    }
//    else if(IGTEntity.ENTITY_RESTORE_DOCUMENT.equals(entityType))
//    { //20030522AH
//      return new DefaultRestoreDocument();
//    }
//    else
//    {
//      throw new java.lang.UnsupportedOperationException("Manager:" + this
//                  + " cannot create entity object of type " + entityType);
//    }
  }

  public Collection getAll() throws GTClientException
  {
    throw new UnsupportedOperationException("Non persistant virtual entity");
  }

  private void assertLookupProperties(String entityType)
  {
  	AssertUtil.assertTrue(IGTEntity.ENTITY_VIRTUAL_LOOKUP_PROPERTIES.equals(entityType));
  }
  
  boolean isVirtual(String entityType)
  {
  	assertLookupProperties(entityType);
  	return true;
  	
//    if(     IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals(entityType)
//        ||  IGTEntity.ENTITY_RESTORE_DOCUMENT.equals(entityType) )
//    {
//      return true;
//    }
//    else
//    {
//      throw new UnsupportedOperationException("Manager:"
//                                                + this
//                                                + " does not handle entities of type "
//                                                + entityType);
//    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
  	assertLookupProperties(entityType);
  	Properties detail = null;
    IConstraint constraint = null;
    VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[2];
    
    int f=-1;
    {
      f++;
      sharedVfmi[f] = new VirtualSharedFMI("lookupProperties.lp_name", IGTLookupPropertiesEntity.NAME);
      sharedVfmi[f].setMandatoryCreate(true);
      sharedVfmi[f].setMandatoryUpdate(true);
      sharedVfmi[f].setEditableCreate(true);
      sharedVfmi[f].setEditableUpdate(true);
      sharedVfmi[f].setDisplayableCreate(true);
      sharedVfmi[f].setDisplayableUpdate(true);
      sharedVfmi[f].setCollection(false);
      sharedVfmi[f].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type",                                        "enum");
      for (int i = 0; i < CONTEXT_PROPERTIES.length; i++)
      {
      	detail.setProperty("lookupProperties.lp_name." + CONTEXT_PROPERTIES[i], CONTEXT_PROPERTIES[i]);
      }
      constraint = new EnumeratedConstraint(detail);
      sharedVfmi[f].setConstraint(constraint);
    }
    {
      f++;
      sharedVfmi[f] = new VirtualSharedFMI("lookupProperties.lp_value", IGTLookupPropertiesEntity.VALUE);
      sharedVfmi[f].setMandatoryCreate(true);
      sharedVfmi[f].setMandatoryUpdate(true);
      sharedVfmi[f].setEditableCreate(true);
      sharedVfmi[f].setEditableUpdate(true);
      sharedVfmi[f].setDisplayableCreate(true);
      sharedVfmi[f].setDisplayableUpdate(true);
      sharedVfmi[f].setCollection(false);
      sharedVfmi[f].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type",            "text");
      detail.setProperty("text.length.max", "100");
      constraint = new TextConstraint(detail);
      sharedVfmi[f].setConstraint(constraint);
    }
  	
    return sharedVfmi;
  }
}