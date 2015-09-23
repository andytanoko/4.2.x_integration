/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultArchiveDocumentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 * 2003-06-03     Andrew Hill         Remove useless call to defineVirtualFields() in constructor
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2002-09-22     Daniel D'Cotta      Commented out GDOC_FILTER, added re-designed 
 *                                    ProcessInstance and GridDocument options
 * 2006-04-26     Tam Wei Xiang       i)  Change virtual field for fromDate and toDate.
 *                                    ii) Modified method doCreate() : change the way to parse
 *                                    the fromDate, time and toDate, time (since there will
 *                                    be and extra 30 minute if using previous parsing method.
 *                                    Given sys timezone in Asia/Singapore)    
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream. 
 * 2006-09-14     Tam Wei Xiang       Handle new field isEnabledSearchArchived, isEnabledRestoreArchive,
 *                                    and partner                                                               
 */

package com.gridnode.gtas.client.ctrl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.dbarchive.ArchiveDocumentEvent;
import com.gridnode.gtas.events.dbarchive.RestoreDocumentEvent;
import com.gridnode.gtas.model.document.IGridDocument;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.gtas.client.web.archive.helpers.Logger;

class DefaultArchiveDocumentManager extends DefaultAbstractManager
  implements IGTArchiveDocumentManager
{
  private static final String RESTORE_DOCUMENT_PATH_KEY = IPathConfig.PATH_TEMP; //@todo - set correctly  

  DefaultArchiveDocumentManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_ARCHIVE_DOCUMENT, session);
    
    //RestoreDocument isnt the primary entity so we need to do this explicitly
    //20030603AH - commentout - defineVirtualFields(IGTEntity.ENTITY_RESTORE_DOCUMENT); //20030522AH
    loadFmi(IGTEntity.ENTITY_RESTORE_DOCUMENT);
    //...
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("ArchiveDocument may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    if(IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals( entity.getType() ))
    {
      try
      {
        DefaultArchiveDocumentEntity da = (DefaultArchiveDocumentEntity)entity;
        ArchiveDocumentEvent event = new ArchiveDocumentEvent();
        
        event.setArchiveName        (entity.getFieldString(IGTArchiveDocumentEntity.NAME));
        event.setArchiveDescription (entity.getFieldString(IGTArchiveDocumentEntity.DESCRIPTION));
        
        String fromDateStr = entity.getFieldString(IGTArchiveDocumentEntity.FROM_DATE);
        String toDateStr = entity.getFieldString(IGTArchiveDocumentEntity.TO_DATE);
        Logger.log("Debug Archive time DefaultArchiveDocumentManager fromDate " + fromDateStr);
        Logger.log("Debug Archive time DefaultArchiveDocumentManager toDate "+ toDateStr);
        
        String fromTimeStr = entity.getFieldString(IGTArchiveDocumentEntity.FROM_TIME);
        String toTimeStr   = entity.getFieldString(IGTArchiveDocumentEntity.TO_TIME);
        Logger.log("Debug Archive time DefaultArchiveDocumentManager fromTimeStr "+fromTimeStr);
        Logger.log("Debug Archive time DefaultArchiveDocumentManager toTimeStr "+toTimeStr);
        
        TimeZone defTZ = TimeZone.getDefault();
        Timestamp fromTimeStamp = convertToUTC(fromDateStr, fromTimeStr, defTZ);
        Timestamp toTimeStamp = convertToUTC(toDateStr, toTimeStr, defTZ);
        
        Logger.log("Debug Archive time DefaultArchiveDocumentManager fromTimeStamp "+  fromTimeStamp);
        Logger.log("Debug Archive time DefaultArchiveDocumentManager toTimeStamp "+  toTimeStamp);
        
        event.setFromStartTime      (fromTimeStamp);
        event.setToStartTime        (toTimeStamp);
        
        String archiveType = entity.getFieldString(IGTArchiveDocumentEntity.ARCHIVE_TYPE);
        event.setArchiveType        (archiveType);
        
        //TWX 14092006
        Boolean isEnabledSearchArchived =   (Boolean)entity.getFieldValue(IGTArchiveDocumentEntity.ENABLE_SEARCH_ARCHIVED);
        Boolean isEnabledRestoreArchived =  (Boolean)entity.getFieldValue(IGTArchiveDocumentEntity.ENABLE_RESTORE_ARCHIVED);
        event.setEnableSearchArchived(isEnabledSearchArchived);
        event.setEnableRestoreArchived(isEnabledRestoreArchived);
        Logger.log("[DefaultArchiveDocumentManager ] isEnabledSearchArchived "+isEnabledSearchArchived);
        Logger.log("[DefaultArchiveDocumentManager ] isEnabledRestoreArchived "+isEnabledRestoreArchived);
        
        List partnerList = (List)entity.getFieldValue(IGTArchiveDocumentEntity.PARTNER);
        for(int i = 0; partnerList != null && partnerList.size() > 0 && i < partnerList.size(); i++)
        {
        	Logger.log("[DefaultArchiveDocumentManager] partner selected is "+partnerList.get(i));
        }
        event.setPartnerIDForArchive(partnerList);
        
        if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
        {
          List processDef = (List)entity.getFieldValue(IGTArchiveDocumentEntity.PROCESS_DEF);
          Boolean includeIncomplete = (Boolean)entity.getFieldValue(IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE);
          
          event.setProcessDefNameList         (processDef);
          event.setIncludeIncompleteProcesses (includeIncomplete);

          //event.setProcessInstanceFilter(getProcessInstanceFilter(fromTime, toTime, includeIncomplete.booleanValue()));
        }
        else if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
        {
          List folder   = (List)entity.getFieldValue(IGTArchiveDocumentEntity.FOLDER);
          List docType  = (List)entity.getFieldValue(IGTArchiveDocumentEntity.DOC_TYPE);

          event.setFolderList       (folder);
          event.setDocumentTypeList (docType);

          //event.setGDocFilter(getDocumentFilter(fromTime, toTime, folder, docType));
        }
        else
        {
          throw new GTClientException("Invalid archiveType:" + archiveType);
        }
        
        Map results = (Map)((DefaultGTSession)_session).fireEvent(event);       
        
        if(results == null)
        { // Internal sanity check
          throw new NullPointerException("ArchiveDocumentEvent event return null result map");
        }
        //initVirtualEntityFields(da.getType(), da, results); commented by mahesh
        da.setNewEntity(false);
        da.setEntityDirty(false);
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error creating ArchiveDocument", t);
      }
    }
    else
    {
      throw new UnsupportedOperationException("Bad entity:" + entity);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ARCHIVE_DOCUMENT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ARCHIVE_DOCUMENT;
  }

  protected IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get event for this type");
  }

  protected IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get list event for this type");
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new UnsupportedOperationException("No delete event for this type");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals(entityType))
    {
      return new DefaultArchiveDocumentEntity();
    }
    else if(IGTEntity.ENTITY_RESTORE_DOCUMENT.equals(entityType))
    { //20030522AH
      return new DefaultRestoreDocument();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public Collection getAll() throws GTClientException
  {
    throw new UnsupportedOperationException("Non persistant virtual entity");
  }
  
  public IGTEntity newEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_ARCHIVE_DOCUMENT);
    entity.setNewEntity(true);
    return entity;
  }
  
  public IGTRestoreDocument getRestoreDocument() throws GTClientException
  {
    try
    {
      AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_RESTORE_DOCUMENT);
      entity.setNewEntity(true);
      return (IGTRestoreDocument)entity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error creating IGTRestoreDocument instance",t);
    }
  }

  boolean isVirtual(String entityType)
  {
    if(     IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals(entityType)
        ||  IGTEntity.ENTITY_RESTORE_DOCUMENT.equals(entityType) )
    {
      return true;
    }
    else
    {
      throw new UnsupportedOperationException("Manager:"
                                                + this
                                                + " does not handle entities of type "
                                                + entityType);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals(entityType))
    {
      Properties detail = null;
      IConstraint constraint = null;
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[14];
      
      int f=-1; //Talk about lazy, this is to facilitate cut & paste!
      /*
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.archiveFile",
                                              IGTArchiveDocumentEntity.ARCHIVE_FILE);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(false);
        sharedVfmi[f].setEditableUpdate(false);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("file.downloadable","true");
        detail.setProperty("file.pathKey","archiveFilePath");
        constraint = new FileConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.archiveFilePath",
                                              IGTArchiveDocumentEntity.ARCHIVE_FILE_PATH);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(false);
        sharedVfmi[f].setEditableUpdate(false);
        sharedVfmi[f].setDisplayableCreate(false);
        sharedVfmi[f].setDisplayableUpdate(false);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        constraint = new FileConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.gdocFilter",
                                              IGTArchiveDocumentEntity.GDOC_FILTER);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("com.gridnode.pdip.framework.db.filter.IDataFilter");
        detail = new Properties();
        sharedVfmi[f].setConstraint(null);
      }
      */
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.name", IGTArchiveDocumentEntity.NAME);
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
        detail.setProperty("text.length.max", "30");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.description", IGTArchiveDocumentEntity.DESCRIPTION);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type",            "text");
        detail.setProperty("text.length.max", "80");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.fromDate", IGTArchiveDocumentEntity.FROM_DATE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        /*
        sharedVfmi[f].setValueClass("java.lang.Date");
        detail = new Properties();
        detail.setProperty("type",              "datetime");
        detail.setProperty("datetime.time",     "false");
        detail.setProperty("datetime.date",     "true");
        detail.setProperty("datetime.pattern",  "yyyy-MM-dd");
        constraint = new TimeConstraint(detail);
        */
        //TWX Apr 25 2006
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type",            "text");
        detail.setProperty("text.length.max", "10");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.fromTime", IGTArchiveDocumentEntity.FROM_TIME);
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
        detail.setProperty("text.length.max", "5");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.toDate", IGTArchiveDocumentEntity.TO_DATE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        /*
        sharedVfmi[f].setValueClass("java.lang.Date");
        detail = new Properties();
        detail.setProperty("type",              "datetime");
        detail.setProperty("datetime.time",     "false");
        detail.setProperty("datetime.date",     "true");
        detail.setProperty("datetime.pattern",  "yyyy-MM-dd");
        constraint = new TimeConstraint(detail);
        */
        //TWX Apr 25 2006
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type",            "text");
        detail.setProperty("text.length.max", "10");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.toTime", IGTArchiveDocumentEntity.TO_TIME);
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
        detail.setProperty("text.length.max", "5");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.archiveType", IGTArchiveDocumentEntity.ARCHIVE_TYPE);
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
        detail.setProperty("archiveDocument.archiveType.processInstance", IGTArchiveDocumentEntity.ARCHIVE_TYPE_PROCESS_INSTANCE);
        detail.setProperty("archiveDocument.archiveType.document",        IGTArchiveDocumentEntity.ARCHIVE_TYPE_DOCUMENT);
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.processDef", IGTArchiveDocumentEntity.PROCESS_DEF);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(true);
        sharedVfmi[f].setValueClass("java.utils.Vector");
        sharedVfmi[f].setElementClass("java.lang.Long");
        detail = new Properties();
        detail.setProperty("type",            "foreign");
        detail.setProperty("foreign.key",     "processDef.defName");
        detail.setProperty("foreign.display", "processDef.defName");
        detail.setProperty("foreign.cached",  "false");
        constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.includeIncomplete", IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.Boolean");
        detail = new Properties();
        detail.setProperty("type",          "enum");
        detail.setProperty("generic.yes",   "true");
        detail.setProperty("generic.no",    "false");
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.folder", IGTArchiveDocumentEntity.FOLDER);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(true);
        sharedVfmi[f].setValueClass("java.utils.Vector");
        sharedVfmi[f].setElementClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type",                            "enum");
        detail.setProperty("archiveDocument.folder.import",   IGridDocument.FOLDER_IMPORT);
        detail.setProperty("archiveDocument.folder.export",   IGridDocument.FOLDER_EXPORT);
        detail.setProperty("archiveDocument.folder.inbound",  IGridDocument.FOLDER_INBOUND);
        detail.setProperty("archiveDocument.folder.outbound", IGridDocument.FOLDER_OUTBOUND);
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.docType", IGTArchiveDocumentEntity.DOC_TYPE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(true);
        sharedVfmi[f].setValueClass("java.utils.Vector");
        sharedVfmi[f].setElementClass("java.lang.Long");
        detail = new Properties();
        detail.setProperty("type",            "foreign");
        detail.setProperty("foreign.key",     "documentType.docType");
        detail.setProperty("foreign.display", "documentType.docType");
        detail.setProperty("foreign.cached",  "false");
        constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      {
      	f++;
      	sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.enableSearchArchived", IGTArchiveDocumentEntity.ENABLE_SEARCH_ARCHIVED);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.Boolean");
        detail = new Properties();
        detail.setProperty("type",          "enum");
        detail.setProperty("generic.yes",   "true");
        detail.setProperty("generic.no",    "false");
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      
      //14092006 TWX new archive criteria
      {
      	f++;
      	sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.enableRestoreArchived", IGTArchiveDocumentEntity.ENABLE_RESTORE_ARCHIVED);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.Boolean");
        detail = new Properties();
        detail.setProperty("type",          "enum");
        detail.setProperty("generic.yes",   "true");
        detail.setProperty("generic.no",    "false");
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      
      {
      	f++;
        sharedVfmi[f] = new VirtualSharedFMI("archiveDocument.partner", IGTArchiveDocumentEntity.PARTNER);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(true);
        sharedVfmi[f].setValueClass("java.utils.Vector");
        sharedVfmi[f].setElementClass("java.lang.Long");
        detail = new Properties();
        detail.setProperty("type",            "foreign");
        detail.setProperty("foreign.key",     "partner.partnerId");
        detail.setProperty("foreign.display", "partner.partnerId");
        detail.setProperty("foreign.cached",  "false");
        constraint = new ForeignEntityConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      
      return sharedVfmi;
    }
    else if(IGTEntity.ENTITY_RESTORE_DOCUMENT.equals(entityType))
    { //20030522AH
      Properties detail = null;
      IConstraint constraint = null;
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[1];
      
      int f=-1;
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI("restoreDocument.archiveFile",
                                              IGTRestoreDocument.ARCHIVE_FILE);
        sharedVfmi[f].setMandatoryCreate(true);
        sharedVfmi[f].setMandatoryUpdate(true);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("file.downloadable","true");
        detail.setProperty("file.fixedKey",RESTORE_DOCUMENT_PATH_KEY);
        constraint = new FileConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      
      return sharedVfmi;
    }
    else
    {
      return new IGTFieldMetaInfo[0];
    }
  }
/* commented by mahesh
  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    try
    {
      if(IGTEntity.ENTITY_ARCHIVE_DOCUMENT.equals(entityType))
      { //20021209
        DefaultArchiveDocumentEntity da = (DefaultArchiveDocumentEntity)entity;
        String archiveFile = (String)fieldMap.get(ArchiveDocumentEvent.ARCHIVE_FILE);
        String archiveFilePath = (String)fieldMap.get(ArchiveDocumentEvent.ARCHIVE_FILE_PATH);        
        da.setNewFieldValue(da.ARCHIVE_FILE, archiveFile );
        da.setNewFieldValue(da.ARCHIVE_FILE_PATH, archiveFilePath );
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error initialising virtual entity fields for " + entity,t);
    }
  }
*/  
 
  public void restoreDocuments(IGTRestoreDocument restoreDocument)
    throws GTClientException
  {
    try
    {
      String archiveFile = restoreDocument.getFieldString(IGTRestoreDocument.ARCHIVE_FILE);
      if (archiveFile == null)
        throw new IllegalArgumentException("archiveFile is not specified");
            
      RestoreDocumentEvent event = new RestoreDocumentEvent(archiveFile);
      
      Map results = (Map)((DefaultGTSession)_session).fireEvent(event);     
      
      if(results == null)
      { // Internal sanity check
        throw new NullPointerException("RestoreDocumentEvent event returned null result map");
      }
      //What to do here? Anything?
      //20030718AH - guess not ;-)
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error restoring documents",t);
    }
  }

/*
  protected static IDataFilter getProcessInstanceFilter(Timestamp fromStartTime,Timestamp toStartTime, boolean includeInComplete)
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null,                      IGTArchiveDocumentEntity.IGWFRtProcess.ENGINE_TYPE,  filter.getEqualOperator(),          "BPSS",                     false);
    filter.addSingleFilter(filter.getAndConnector(),  IGTArchiveDocumentEntity.IGWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(),          "BpssBinaryCollaboration",  false);
    filter.addSingleFilter(filter.getAndConnector(),  IGTArchiveDocumentEntity.IGWFRtProcess.START_TIME,   filter.getGreaterOrEqualOperator(), fromStartTime,              false);
    filter.addSingleFilter(filter.getAndConnector(),  IGTArchiveDocumentEntity.IGWFRtProcess.START_TIME,   filter.getLessOrEqualOperator(),    toStartTime,                false);

    if(!includeInComplete)
    {
      List stateList = new ArrayList();
      stateList.add(new Integer(IGTArchiveDocumentEntity.IGWFRtProcess.CLOSED_COMPLETED));
      stateList.add(new Integer(IGTArchiveDocumentEntity.IGWFRtProcess.CLOSED_ABNORMALCOMPLETED));
      stateList.add(new Integer(IGTArchiveDocumentEntity.IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED));
      stateList.add(new Integer(IGTArchiveDocumentEntity.IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED));
      
      filter.addDomainFilter(filter.getAndConnector(), IGTArchiveDocumentEntity.IGWFRtProcess.STATE, stateList, false);
    }
    return  filter;   
  }

  protected static IDataFilter getDocumentFilter(Timestamp fromStartTime,Timestamp toStartTime, List folder, List docType)
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null,                      IGTGridDocumentEntity.DT_CREATE, filter.getGreaterOrEqualOperator(), fromStartTime, false);
    filter.addSingleFilter(filter.getAndConnector(),  IGTGridDocumentEntity.DT_CREATE, filter.getLessOrEqualOperator(),    toStartTime,   false);
    filter.addDomainFilter(filter.getAndConnector(),  IGTGridDocumentEntity.FOLDER,                                        folder,        false);    
    filter.addDomainFilter(filter.getAndConnector(),  IGTGridDocumentEntity.U_DOC_DOC_TYPE,                                docType,       false);    
    return  filter;   
  }
*/  
  
  /**
   * TWX: 26 Apr 2006
   */
  private Timestamp convertToUTC(String date, String time, TimeZone defTZ)
  	throws ParseException
  { 
  	if(defTZ == null)
  	{
  		defTZ = TimeZone.getDefault();
  	}
  	Calendar c = Calendar.getInstance(defTZ);
  	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  	formatter.setTimeZone(defTZ);
  	Date givenDate = formatter.parse(date);
  	c.setTimeInMillis(givenDate.getTime());
  	int year = c.get(Calendar.YEAR);
  	int month = c.get(Calendar.MONTH);
  	int day = c.get(Calendar.DAY_OF_MONTH);
  	
  	c = Calendar.getInstance(defTZ);
  	formatter = new SimpleDateFormat("HH:mm");
  	formatter.setTimeZone(defTZ);
  	Date givenTime = formatter.parse(time);
  	c.setTimeInMillis(givenTime.getTime());
  	int hour = c.get(Calendar.HOUR_OF_DAY); //24 hour
  	int minute = c.get(Calendar.MINUTE);
  	
  	c = Calendar.getInstance(defTZ);
  	c.set(Calendar.YEAR, year);
  	c.set(Calendar.MONTH, month);
  	c.set(Calendar.DAY_OF_MONTH, day);
  	c.set(Calendar.HOUR_OF_DAY, hour);
  	c.set(Calendar.MINUTE, minute);
  	c.set(Calendar.SECOND, 0);
  	c.set(Calendar.MILLISECOND, 0);
  	
  	return new Timestamp(TimeUtil.localToUtc(c.getTimeInMillis()));
  }
}