/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: UpdateArchiveAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.actions;

import java.util.List;
import java.util.Map;

import com.gridnode.gtas.events.dbarchive.UpdateArchiveEvent;
import com.gridnode.gtas.model.dbarchive.ArchiveEntityFieldID;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveHelper;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class UpdateArchiveAction extends AbstractUpdateEntityAction
{

  private static String ACTION_NAME = "UpdateArchiveAction";
  
  @Override
  protected Map convertToMap(AbstractEntity entity)
  {
    return ArchiveMetaInfo.convertToMap(entity, ArchiveEntityFieldID.getEntityFieldID() , null);
  }

  @Override
  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateArchiveEvent updateEvent = (UpdateArchiveEvent)event;
    return new Object[]{ArchiveMetaInfo.ENTITY_NAME, updateEvent.getUID()};
  }

  @Override
  protected AbstractEntity prepareUpdateData(IEvent event)
  { 
    UpdateArchiveEvent updateEvent = (UpdateArchiveEvent)event;
    ArchiveMetaInfo metainfo = new ArchiveMetaInfo();
    metainfo.setUId(updateEvent.getUID());
    metainfo.setArchiveID(updateEvent.getArchiveID());
    metainfo.setArchiveDescription(updateEvent.getArchiveDescription());
    
    Boolean isEnableSearchArchived = updateEvent.isEnableSearchArchived();
    Boolean isEnableRestoreArchived = updateEvent.isEnableRestoreArchived();
    String archiveType = updateEvent.getArchiveType();
    
    metainfo.setEnableSearchArchived(isEnableSearchArchived);
    metainfo.setEnableRestoreArchived(isEnableRestoreArchived);
    
    //reset those value that exist in either archive Type --> ArchiveProcessInstance or ArchiveDocument
    //or those value fall under isArchiveFrequencyOnce
    metainfo.setIncludeIncompleteProcesses(new Boolean(false));
    metainfo.setProcessDefNameList(null);
    metainfo.setDocumentTypeList(null);
    metainfo.setFolderList(null);
    metainfo.setFromStartDate(null);
    metainfo.setToStartDate(null);
    metainfo.setFromStartTime(null);
    metainfo.setToStartTime(null);
    metainfo.setArchiveRecordOlderThan(null);
    
    metainfo.setPartnerIDForArchive(ArchiveMetaInfo.convertToString(updateEvent.getPartnerIDForArchive()));
    metainfo.setArchiveType(archiveType);
    metainfo.setArchiveRecordOlderThan(updateEvent.getArchiveOlderThan());
    metainfo.setArchiveFrequencyOnce(updateEvent.isArchiveFrequencyOnce());
    metainfo.setClientTz(updateEvent.getClientTZ());

    if(updateEvent.isArchiveFrequencyOnce())
    {
      metainfo.setFromStartDate(updateEvent.getFromStartDate());
      metainfo.setToStartDate(updateEvent.getToStartDate());
      metainfo.setFromStartTime(updateEvent.getFromStartTime());
      metainfo.setToStartTime(updateEvent.getToStartTime());
    }
    
    if (ArchiveMetaInfo.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
    {
      List processDefNameList = updateEvent.getProcessDefNameList();
      metainfo.setProcessDefNameList(ArchiveMetaInfo.convertToString(processDefNameList));
      metainfo.setIncludeIncompleteProcesses(updateEvent.isIncludeIncompleteProcesses());
    }
    else if (ArchiveMetaInfo.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      String docTypeList = ArchiveMetaInfo.convertToString(updateEvent.getDocumentTypeList());
      metainfo.setDocumentTypeList(docTypeList);

      List folderList = updateEvent.getFolderList();
      metainfo.setFolderList(ArchiveMetaInfo.convertToString(folderList));
    }
    return metainfo;
  }

  @Override
  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ArchiveHelper.getArchiveManager().getArchive(key);
  }

  @Override
  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ArchiveMetaInfo metaInfo = (ArchiveMetaInfo)entity;
    ArchiveHelper.getArchiveManager().updateArchiveMetaInfo(metaInfo);
  }

  @Override
  protected String getActionName()
  {
    // TODO Auto-generated method stub
    return ACTION_NAME;
  }

  @Override
  protected Class getExpectedEventClass()
  {
    // TODO Auto-generated method stub
    return UpdateArchiveEvent.class;
  }
  
}
