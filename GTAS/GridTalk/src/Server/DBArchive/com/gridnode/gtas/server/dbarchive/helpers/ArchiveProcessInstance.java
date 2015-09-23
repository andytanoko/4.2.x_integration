/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveProcessInstance.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 29, 2004 			Mahesh             	Created
 * Feb 18, 2005       Mahesh              Modified rnProfilefilter construction
 *                                        as it is generating wrong query.
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.helpers.BpssHandler;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.XmlObjectDeserializer;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.UtilEntity;

public class ArchiveProcessInstance
{
  public static final String PATH_PROCESSINSTANCE = "processinstance";

  //private Hashtable sysFolderTable = null;
  private long processInstanceCount=0;
  private long failedCount=0;
  
  DbArchive dbArchive = null;

  IGWFWorkflowManagerObj wfMgr = null;
  IRnifManagerObj rnifMgr = null;
  ArchiveGridDocument archiveGridDocument = null;
  XmlObjectDeserializer deser = null;
  
  public ArchiveProcessInstance(DbArchive dbArchive,ArchiveGridDocument archiveGDoc) throws Exception
  {
    this.dbArchive = dbArchive;
    archiveGridDocument = archiveGDoc;
    deser = new XmlObjectDeserializer();
    wfMgr = ArchiveHelper.getWorkflowMgr();
    rnifMgr = ArchiveHelper.getRnifManager();
  }

  public long getProcessInstanceCount()
  {
    return processInstanceCount;
  }

  public long getFailedCount()
  {
    return failedCount;
  }
  
  public void archive(IDataFilter rtProcessInstanceFilter,List processDefNameList)
  {
    try
    {
      if (rtProcessInstanceFilter == null)
      {
        Logger.debug("[ArchiveProcessInstance.archive] rtProcessInstanceFilter is null");
        return;
      }
      Logger.log("[ArchiveProcessInstance.archive] rtProcessInstanceFilter=" + rtProcessInstanceFilter.getFilterExpr()+", processDefNameList="+processDefNameList);  
      if(processDefNameList!=null && !processDefNameList.isEmpty())
      {
        Collection binCollUIdColl = getBpssBinaryCollaborationUIds(processDefNameList);
        if(binCollUIdColl==null || binCollUIdColl.isEmpty())
          throw new Exception("No definitions found with the processdefnames");
        rtProcessInstanceFilter.addDomainFilter(rtProcessInstanceFilter.getAndConnector(), GWFRtProcess.PROCESS_UID, binCollUIdColl, false);
      }
      Collection rtProcessKeys = wfMgr.getProcessInstanceKeys(rtProcessInstanceFilter);
     
      Logger.log("[ArchiveProcessInstance.archive] Total processinstances to archive = "+rtProcessKeys.size());
      for (Iterator i = rtProcessKeys.iterator(); i.hasNext();)
      {
        Long rtProcessUId = (Long) i.next();
        processInstanceCount++;
        try
        {
          archiveProcessInstance(rtProcessUId);
        }
        catch (Throwable th)
        {
          failedCount++;
          dbArchive.err("[ArchiveProcessInstance.archive] Error while archiveing ProcessInstance with rtProcessUId=" + rtProcessUId, th);
        }
      }
    }
    catch (Throwable th)
    {
      dbArchive.err("[ArchiveProcessInstance.archive] Error in archive", th);
    }
  }

  /**
   * Restores processinstance
   * @param docFilenames
   */
  public void restoreProcessInstance(ArrayList filenames)
  {
    try
    {
      //restore processinstance and its related entities
      for (int i = 0; i < filenames.size(); i++)
      {
        String filename = (String) filenames.get(i);
        try
        {
          if (filename != null && filename.startsWith(dbArchive.TMP_PROCESSINSTANCE_DIR))
          {
            processInstanceCount++;
            restoreEntity(filename);
          }
        }
        catch (Throwable th)
        {
          failedCount++; 
          dbArchive.err("[ArchiveProcessInstance.restoreProcessInstance] Error while restoring filename=" + filename, th);
        }
      }
    }
    catch (Throwable th)
    {
      dbArchive.err("[ArchiveProcessInstance.restoreProcessInstance] Error while restoring", th);
    }
  }

  private Collection getBpssBinaryCollaborationUIds(List processDefNameList) throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addDomainFilter(null, BpssProcessSpecification.NAME, processDefNameList, false);
    AbstractEntityHandler handler =EntityHandlerFactory.getHandlerFor(BpssProcessSpecification.ENTITY_NAME,false);
    Collection specUIdColl = handler.getKeyByFilterForReadOnly(filter);
    Logger.debug("[ArchiveProcessInstance.getBpssBinaryCollaborationUIds] specUIdColl="+specUIdColl);    
    if(specUIdColl!=null && ! specUIdColl.isEmpty())
    { 
      filter = new DataFilterImpl();
      filter.addSingleFilter(null,BpssProcessSpecEntry.ENTRY_TYPE,filter.getEqualOperator(),BpssBinaryCollaboration.ENTITY_NAME,false);
      filter.addDomainFilter(filter.getAndConnector(), BpssProcessSpecEntry.SPEC_UID,specUIdColl, false);
      handler =EntityHandlerFactory.getHandlerFor(BpssProcessSpecEntry.ENTITY_NAME,false);
      Collection specEntryColl  = handler.getEntityByFilterForReadOnly(filter);
      Logger.debug("[ArchiveProcessInstance.getBpssBinaryCollaborationUIds] specEntryColl size="+specEntryColl.size());
      if(specEntryColl!=null && ! specEntryColl.isEmpty())
      {
        List binCollUIdColl = new ArrayList();
        for(Iterator i = specEntryColl.iterator();i.hasNext();)
        {
          BpssProcessSpecEntry specEntry = (BpssProcessSpecEntry)i.next();
          binCollUIdColl.add(new Long(specEntry.getEntryUId()));
        }
        return binCollUIdColl;
      }
    }
    //means no definitions with this processDefNames found in database
    return null;    
  }
  private void archiveProcessInstance(Long rtProcessUId) throws Throwable
  {
    Logger.log("[ArchiveProcessInstance.archiveProcessInstance] Enter, rtProcessUId=" + rtProcessUId);

    GWFRtProcess rtProcess = wfMgr.getProcessInstance(rtProcessUId);
    archiveRtProcess(rtProcess);

    //find the GWFRtProcessDoc related to this process
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, GWFRtProcessDoc.RT_BINARY_COLLABORATION_UID, filter.getEqualOperator(), rtProcess.getKey(), false);

    Collection rnProfileUIdColl = null;
    Collection rtProcessDocColl = wfMgr.getRtProcessDocList(filter);
    if (!rtProcessDocColl.isEmpty())
    {
      IDataFilter rnProfileFilter = null;
      for (Iterator i = rtProcessDocColl.iterator(); i.hasNext();)
      {
        GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) i.next();
        archiveEntity(rtProcessDoc, PATH_PROCESSINSTANCE + "/");

        String[] seperateIds = BpssHandler.getSeperateIdsFromBPSSDocId(rtProcessDoc.getDocumentId());
        IDataFilter tempRnFilter = new DataFilterImpl();
        tempRnFilter.addSingleFilter(
          null,
          RNProfile.PROCESS_ORIGINATOR_ID,
          tempRnFilter.getEqualOperator(),
          seperateIds[IRnifConstant.INDEX_PROCESS_INITIATOR],
          false);
        tempRnFilter.addSingleFilter(
          tempRnFilter.getAndConnector(),
          RNProfile.PROCESS_INSTANCE_ID,
          tempRnFilter.getEqualOperator(),
          seperateIds[IRnifConstant.INDEX_PROCESS_INSTANCE_ID],
          false);
        if (rnProfileFilter == null)
          rnProfileFilter = tempRnFilter;
        else
        {
          //MAHESH 20050218: modified the filter query, as it is generating wrong query
          //rnProfileFilter.addFilter(rnProfileFilter.getOrConnector(), tempRnFilter);
          rnProfileFilter=new DataFilterImpl(rnProfileFilter,rnProfileFilter.getOrConnector(),tempRnFilter);
        }        
      }
      //find the rnProfiles related to this GWFRtProcessDoc
      Collection rnProfileColl = rnifMgr.findRNProfiles(rnProfileFilter); //find rnProfiles dependent on processDoc
      if (rnProfileColl != null && rnProfileColl.size() > 0)
      {
        rnProfileUIdColl = new ArrayList();
        for (Iterator i = rnProfileColl.iterator(); i.hasNext();)
        {
          RNProfile rnProfile = (RNProfile) i.next();
          archiveEntity(rnProfile, PATH_PROCESSINSTANCE + "/");
          rnProfileUIdColl.add(rnProfile.getKey());
        }
        if (rnProfileUIdColl != null && !rnProfileUIdColl.isEmpty())
        {
          IDataFilter gridDocFilter = new DataFilterImpl();
          gridDocFilter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, rnProfileUIdColl, false);
          archiveGridDocument.archive(gridDocFilter); //archive gridDocuments dependent on rnProfile
        }
      }
    }
    if (rnProfileUIdColl != null && !rnProfileUIdColl.isEmpty())
    {
      for (Iterator i = rnProfileUIdColl.iterator(); i.hasNext();)
      {
        Long rnProfileUId = (Long) i.next();
        rnifMgr.deleteRNProfile(rnProfileUId);
      }
    }
    wfMgr.removeProcessInstance((Long) rtProcess.getKey());
  }
  /*
  private Collection getRtProcessKeysFromRtProcessDoc(IDataFilter rtProcessDocFilter) throws Exception
  {
    Collection rtProcessDocColl = wfMgr.getRtProcessDocList(rtProcessDocFilter);
    List rtProcessUIdList = new ArrayList();
    for (Iterator i = rtProcessDocColl.iterator(); i.hasNext();)
    {
      GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) i.next();
      rtProcessUIdList.add(rtProcessDoc.getRtBinaryCollaborationUId());
    }
    return rtProcessUIdList;
  }*/

  /**
   * Archives RtProcess and its dependent entitities
   * @param rtProcessColl Collection of RtProcess Objects
   * @throws Throwable 
   */
  private void archiveRtProcess(GWFRtProcess rtProcess) throws Throwable
  {
    archiveEntity(rtProcess, PATH_PROCESSINSTANCE + "/");

    //look for activities in the process
    IDataFilter activityFilter = new DataFilterImpl();
    activityFilter.addSingleFilter(null, GWFRtActivity.RT_PROCESS_UID, activityFilter.getEqualOperator(), rtProcess.getKey(), false);
    Collection rtActivityColl = UtilEntity.getEntityByFilter(activityFilter, GWFRtActivity.ENTITY_NAME, true);
    if (rtActivityColl != null && !rtActivityColl.isEmpty())
    {
      Collection rtActivityUIdColl = new ArrayList();
      for (Iterator i = rtActivityColl.iterator(); i.hasNext();)
      {
        GWFRtActivity rtActivity = (GWFRtActivity) i.next();
        archiveEntity(rtActivity, PATH_PROCESSINSTANCE + "/");
        rtActivityUIdColl.add(rtActivity.getKey());
      }

      //look for sub processes      
      IDataFilter processFilter = new DataFilterImpl();
      processFilter.addDomainFilter(null, GWFRtProcess.PARENT_RTACTIVITY_UID, rtActivityUIdColl, false);
      Collection rtSubProcessColl = UtilEntity.getEntityByFilter(processFilter, GWFRtProcess.ENTITY_NAME, true);
      if (rtSubProcessColl != null && !rtSubProcessColl.isEmpty())
      {
        //archive sub processes
        for (Iterator i = rtSubProcessColl.iterator(); i.hasNext();)
        {
          GWFRtProcess rtSubProcess = (GWFRtProcess) i.next();
          archiveEntity(rtSubProcess, PATH_PROCESSINSTANCE + "/");
        }
      }
    }
  }

  private void archiveEntity(AbstractEntity entity, String category) throws Exception
  {
    File file = File.createTempFile("tempentity", ".tmp");
    try
    {
      entity.serialize(file.getAbsolutePath());
      if (file.length() > 0)
      {
        String fileName = entity.getEntityName() + "-" + entity.getUId() + ".xml";
        dbArchive.addFileToZip(file, fileName, category);
      }
      else
      {
        Logger.log("[ArchiveProcessInstance.archiveEntity] File length is 0 for entity " + entity);
      }
    }
    finally
    {
      try
      {
        if (file != null && file.exists())
          file.delete();
      }
      catch (Throwable th)
      {
      }
    }
  }

  private void restoreEntity(String filepath) throws Exception
  {
    File f = new File(filepath);
    Class entityClass = null;

    String fileName = f.getName();
    if (fileName.startsWith(GWFRtProcessDoc.ENTITY_NAME))
      entityClass = GWFRtProcessDoc.class;
    else if (fileName.startsWith(GWFRtProcess.ENTITY_NAME))
      entityClass = GWFRtProcess.class;
    else if (fileName.startsWith(GWFRtActivity.ENTITY_NAME))
      entityClass = GWFRtActivity.class;
    else if (fileName.startsWith(RNProfile.ENTITY_NAME))
      entityClass = RNProfile.class;

    if (entityClass != null)
    {
      AbstractEntity entity = (AbstractEntity) deser.deserialize(entityClass, filepath);
      if (entity != null)
      {
        IEntityDAO entityDAO = EntityDAOFactory.getInstance().getDAOFor(entity.getEntityName());

        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, entity.getKeyId(), filter.getEqualOperator(), entity.getKey(), false);
        Collection entityColl = entityDAO.findByFilter(filter);
        if (entityColl != null && !entityColl.isEmpty())
        {
          throw new ApplicationException(
            "Repeated Restore:" + entity.getEntityName() + " with UID=" + entity.getKey() + " already exist! ");
        }
        else
          entityDAO.create(entity, true);
      }
    }
  }

}
