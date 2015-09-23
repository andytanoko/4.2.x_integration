/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DirectArchiveProcessInstance.java
 *
 ****************************************************************************
 * Date               Author            Changes
 ****************************************************************************
 * Nov 23, 2004       Mahesh          	Created
 * Feb 18, 2005       Mahesh            Modified rnProfilefilter construction
 *                                      as it is generating wrong query.
 * Mar 15, 2005       Neo Sok Lay       Resolve ProcessDef name to BpssProcessDefinition name pattern
 * Aug 18, 2006       Neo Sok Lay       To handle restore of rtprocessdoc (archived
 *                                      in V2.4.x) correctly for the addition of '_' prefix in
 *                                      the process doc type.                                        
 * Sep 09, 2005       Tam Wei Xiang       Modified archive method in order to let
 *                                        gtas side know it is processing last chunk
 *                                        of process       
 * Oct 10, 2005       Tam Wei Xiang       All the gdoc, udoc, audit ,attachmentfile and 
 *                                        gdoc ,att db record will be deleted after the archive process
 *                                        has finished. Same for rnprofile, rt process, rt processDoc
 *                                        Add in method deleteCacheFile()  
 *                                                      deleteRTProcess()  
 * Mar 30, 2006       Tam Wei Xiang       Added in new trigger for Archive Starts 
 * Aug 31, 2006       Tam Wei Xiang       Merge from ESTORE stream.    
 * May 18, 2007       Tam Wei Xiang       Support archived by customer                                                                                                             
 **/
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.BpssHandler;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;
import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.base.contextdata.entities.model.ContextData;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecEntry;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.db.XmlObjectDeserializer;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UtilString;
//added by WX
import com.gridnode.gtas.server.dbarchive.exception.EStoreException;
import com.gridnode.gtas.server.dbarchive.helpers.EStore;
import com.gridnode.gtas.server.dbarchive.helpers.IEStorePathConfig;
import com.gridnode.gtas.server.dbarchive.helpers.ObjSerializeHelper;

public class DirectArchiveProcessInstance
{
  public static final String PATH_PROCESSINSTANCE = "processinstance";
  
  private Hashtable sysFolderTable = null;
  private long processInstanceCount=0;
  private long failedCount=0;
  IiCalTimeMgrObj iCalMgr = null;
  DbArchive dbArchive = null;

  DirectArchiveGridDocument archiveGridDocument = null;
  XmlObjectDeserializer deser = null;
  //TWX
  private EStore estore;
  private ArrayList<Long> processDocUIDList;
  private ArrayList<Long> rnprofileUIDList;
  private Hashtable<Long, RNProfile> rnprofileInfoHTable; //this info list contain only the info that is needed
  																		 //in EStore side
  private ArrayList<Long> processUIDList;
  private ArrayList<Long> contextUIDList;
  private ArrayList<String> iCalSenderKeyListForRtProDoc;
  
  private Hashtable<String, Object> criteriaHTable = null;
  private Hashtable<String, Object> summaryHTable = null;
  private boolean isEnableRestoreArchive = false;
  private int _totalRecordWithinInOperator = 0;
  
  public DirectArchiveProcessInstance(DbArchive dbArchive,DirectArchiveGridDocument archiveGDoc) throws Exception
  {
    this.dbArchive = dbArchive;
    archiveGridDocument = archiveGDoc;
    deser = new XmlObjectDeserializer();
    iCalMgr= ArchiveHelper.getIiCalTimeMgr();

    processDocUIDList = new ArrayList<Long>();
    rnprofileUIDList = new ArrayList<Long>();
    rnprofileInfoHTable = new Hashtable<Long, RNProfile>(134); //use 134 as initialCapacity since
                                            //we know entry in this hashtable is around 100.
                                            //no rehash operation is required
    processUIDList = new ArrayList<Long>();
    contextUIDList = new ArrayList<Long>();
    iCalSenderKeyListForRtProDoc = new ArrayList<String>();
    
    _totalRecordWithinInOperator = ArchiveHelper.getTotalAllowedEntryWithinInOperator();
  }

  public long getProcessInstanceCount()
  {
    return processInstanceCount;
  }

  public long getFailedCount()
  {
    return failedCount;
  }
  
  //modified by TWX 9/9/2005
  public void archive(IDataFilter rtProcessInstanceFilter,List processDefNameList,EStore es,
                      boolean isEnableRestoreArchive, List<String> partnerIDForArchive, List<String> customerBEIDForArchive) throws Exception
  {
    try
    {
    	//18 OCt 2005 TWX
    	estore = es;
    	this.isEnableRestoreArchive = isEnableRestoreArchive;
    	
      if (rtProcessInstanceFilter == null)
      {
        Logger.debug("[DirectArchiveProcessInstance.archive] rtProcessInstanceFilter is null");
        return;
      }
      Logger.debug("[DirectArchiveProcessInstance.archive] rtProcessInstanceFilter=" + rtProcessInstanceFilter.getFilterExpr()+", processDefNameList="+processDefNameList);
      
      List<String> resolvedProcessDefNameList = resolveToBpssProcessName(processDefNameList); //NSL20060314 Resolve to the BpssProcessDefinition name pattern
      //if processDefNameList is empty or it contains all the definations 
      //it will archive processinstances including those that have no definations
      boolean isAllDefsSelected = isAllProcessDefNamesSelected(resolvedProcessDefNameList);   
      if(!isAllDefsSelected && (processDefNameList!=null && !processDefNameList.isEmpty()))
      {
        Collection binCollUIdColl = getBpssBinaryCollaborationUIds(resolvedProcessDefNameList);
        if(binCollUIdColl==null || binCollUIdColl.isEmpty())
          throw new Exception("No definitions found with the processdefnames");
        
        //TODO DOMAIN
        rtProcessInstanceFilter.addDomainFilter(rtProcessInstanceFilter.getAndConnector(), IGWFArchiveProcess.PROCESS_UID, binCollUIdColl, false);
      }
      Collection rtProcessKeys = getEntityDAO(GWFArchiveProcess.ENTITY_NAME).getFieldValuesByFilter(IGWFArchiveProcess.UID, rtProcessInstanceFilter);
      
      int totalRtProcessCount=rtProcessKeys.size();
      Logger.debug("[DirectArchiveProcessInstance.archive] Total processinstances to archive = "+totalRtProcessCount);
      
      int remainingCount = totalRtProcessCount;
      int maxCount = 100;
      List rtProcessUIdSubList = new ArrayList();
      
      Map<String, String> partnerID = convertListToMap(partnerIDForArchive);
      
      //Trigger archive start alert 30 Mar 2006
      triggerArchiveStartUpAlert(partnerIDForArchive, criteriaHTable, summaryHTable, estore.getIsEstoreEnabled(), isEnableRestoreArchive);
      
      boolean isLastChunk = false;
      
      for (Iterator i = rtProcessKeys.iterator(); i.hasNext();)
      {
        rtProcessUIdSubList.add(i.next());
        if(rtProcessUIdSubList.size()==maxCount || !i.hasNext())
        {
          int subListSize = rtProcessUIdSubList.size(); 
          //processInstanceCount+=subListSize;
          Logger.debug("[DirectArchiveProcessInstance.archive] remaining "+remainingCount+" of "+totalRtProcessCount);
          if(!i.hasNext())
          {
          	isLastChunk = true;
          }
          archiveBinCollaboration(rtProcessUIdSubList, isLastChunk, partnerID);
          rtProcessUIdSubList.clear();
          remainingCount=remainingCount-subListSize;
        }        
      }
    }
    
    catch (Throwable th)
    {
      dbArchive.err("[DirectArchiveProcessInstance.archive] Error in archive", th);
      throw new Exception(th); //TWX: 
    }
  }
  
  private List<String> resolveToBpssProcessName(List<String> processDefNameList)
  {
  	List<String> resolvedNames = new ArrayList<String>();
  	for (String processDefName : processDefNameList)
  	{
  		//append '_' in front of the ProcessDef name to get the BpssProcessDefintion Name
  		resolvedNames.add("_"+processDefName); 
  	}
  	return resolvedNames;
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
    if(processDefNameList==null || processDefNameList.isEmpty())
      return null;
    
    IDataFilter filter = new DataFilterImpl();
    filter.addDomainFilter(null, BpssProcessSpecification.NAME, processDefNameList, false);
    Collection specUIdColl = getEntityDAO(BpssProcessSpecification.ENTITY_NAME).findByFilter(filter);
    Logger.debug("[DirectArchiveProcessInstance.getBpssBinaryCollaborationUIds] specUIdColl="+specUIdColl);    
    if(specUIdColl!=null && ! specUIdColl.isEmpty())
    { 
      filter = new DataFilterImpl();
      filter.addSingleFilter(null,BpssProcessSpecEntry.ENTRY_TYPE,filter.getEqualOperator(),BpssBinaryCollaboration.ENTITY_NAME,false);
      filter.addDomainFilter(filter.getAndConnector(), BpssProcessSpecEntry.SPEC_UID,specUIdColl, false);
      Collection specEntryColl  = getEntityDAO(BpssProcessSpecEntry.ENTITY_NAME).getEntityByFilter(filter);
      Logger.debug("[DirectArchiveProcessInstance.getBpssBinaryCollaborationUIds] specEntryColl size="+specEntryColl.size());
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
    
  private boolean isAllProcessDefNamesSelected(List processDefNameList) throws Exception 
  {
    if(processDefNameList==null || processDefNameList.isEmpty())
      return false;
    
    HashSet processDefNameSet = new HashSet(processDefNameList); 

    //get all processdef names in BpssProcessSpecification table
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, BpssProcessSpecification.NAME,filter.getNotEqualOperator(), null, false);
    EntityDAOImpl processSpecificationDAO = (EntityDAOImpl)getEntityDAO(BpssProcessSpecification.ENTITY_NAME);
    Collection bpssProcessDefNameColl = processSpecificationDAO.getFieldValuesByFilter(BpssProcessSpecification.NAME,filter);
    if(bpssProcessDefNameColl!=null && bpssProcessDefNameColl.size()>0)
    {
      boolean isAllSelected = processDefNameSet.containsAll(bpssProcessDefNameColl);
      Logger.debug("[DirectArchiveProcessInstance.isAllProcessDefNamesSelected] isAllSelected="+isAllSelected+", processDefNameSet="+processDefNameSet+", bpssProcessDefNameColl="+bpssProcessDefNameColl);
      
      return isAllSelected;
    } else Logger.debug("[DirectArchiveProcessInstance.isAllProcessDefNamesSelected] returned bpssProcessDefNameColl is empty or null, filter="+filter.getFilterExpr());
    return false;
  }
  
  //modifed by TWX
  private void archiveBinCollaboration(List rtProcessUIdList, boolean isLastChunk, Map<String, String> partnerIDForArchive) throws Throwable
  {
    
    archiveRtProcessDoc(rtProcessUIdList, isLastChunk, partnerIDForArchive);
    
    archiveRtProcess(rtProcessUIdList, isLastChunk);  
    
    if(rnprofileInfoHTable!=null && rnprofileInfoHTable.size() > 0)
    {
    	rnprofileInfoHTable.clear();
    }
    
    //remove all alarms on BinaryCollaboration
    List iCalSenderKeyList = new ArrayList();
    for(Iterator i = rtProcessUIdList.iterator();i.hasNext();)
    {
      iCalSenderKeyList.add("http://BPSS/"+i.next()+"/"+GWFRtProcess.ENTITY_NAME);
    }
    
    //delete alarms related to BinaryCollaboration
    cancelAlarmBySenderKeys(iCalSenderKeyList);
    
    //07092006 finished process the current batch, start delete the gdoc, udoc, audit files and record we cache
    //         Delete rtprocess, rtprocessdoc, rnprofile record.
    //         We are safely to do this since we know the current batch has been archived/estored
    deleteCacheFile();
    Logger.log("[DirectArchiveProcessInstance.archiveBinCollaboration] deleted the batch.");
  }
      
  private void archiveRtProcessDoc(List rtProcessUIdList, boolean isLastChunk, Map<String, String> partnerIDForArchive) throws Throwable
  {
    if(rtProcessUIdList==null || rtProcessUIdList.isEmpty())
      return;
    IDataFilter rtProcessDocFilter = new DataFilterImpl();
    
    //TODO DOMAIN
    rtProcessDocFilter.addDomainFilter(null,GWFRtProcessDoc.RT_BINARY_COLLABORATION_UID,rtProcessUIdList,false);
    EntityDAOImpl rtProcessDocDAO = (EntityDAOImpl) getEntityDAO(GWFRtProcessDoc.ENTITY_NAME);
    Collection rtProcessDocColl = rtProcessDocDAO.getEntityByFilter(rtProcessDocFilter);

    if(rtProcessDocColl.size()>0)
    {
      List rtProcessDocIdList = new ArrayList();
      for (Iterator i = rtProcessDocColl.iterator(); i.hasNext();)
      {
        GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) i.next();
        rtProcessDocIdList.add(rtProcessDoc.getDocumentId());
        iCalSenderKeyListForRtProDoc.add("http://BPSS/"+rtProcessDoc.getKey()+"/"+GWFRtProcessDoc.ENTITY_NAME); //cache 1st, delete later
        
        /*
        // TWX 15092006 To Support Archive By PartnerID
        String partnerID = rtProcessDoc.getPartnerKey();
        if(partnerIDForArchive.containsKey(partnerID) ||  partnerIDForArchive.isEmpty())
        {
        }
        else
        {
        	 rtProcessUIdList.remove(rtProcessDoc.getRtBinaryCollaborationUId());
        	 Logger.log("[DirectArchiveProcessInstance.archiveRtProcessDoc] rtprocessUID "+rtProcessDoc.getRtBinaryCollaborationUId()+" not fulfill the archive PArtnerID list");
        }*/
      }
      
      if(rtProcessDocIdList.isEmpty())
      {
      	return;
      }
      	
      //make filter to find the rnProfiles related to this GWFRtProcessDoc entities
      IDataFilter rnProfileFilter = null; 
      for (Iterator i = rtProcessDocIdList.iterator(); i.hasNext();)
      {
        String[] seperateIds = BpssHandler.getSeperateIdsFromBPSSDocId((String)i.next());
        IDataFilter tempRnFilter = new DataFilterImpl();
        tempRnFilter.addSingleFilter(null,RNProfile.PROCESS_ORIGINATOR_ID,tempRnFilter.getEqualOperator(),seperateIds[IRnifConstant.INDEX_PROCESS_INITIATOR],false);
        tempRnFilter.addSingleFilter(tempRnFilter.getAndConnector(),RNProfile.PROCESS_INSTANCE_ID,tempRnFilter.getEqualOperator(),seperateIds[IRnifConstant.INDEX_PROCESS_INSTANCE_ID],false);
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
      EntityDAOImpl rnProfileDAO = (EntityDAOImpl) getEntityDAO(RNProfile.ENTITY_NAME);
      Collection rnProfileColl = rnProfileDAO.getEntityByFilter(rnProfileFilter); 
      
      if (rnProfileColl != null && rnProfileColl.size() > 0)
      {
        List rnProfileUIdColl = new ArrayList();
        for (Iterator i = rnProfileColl.iterator(); i.hasNext();)
        {
          RNProfile rnProfile = (RNProfile) i.next();
          rnProfileUIdColl.add(rnProfile.getKey()); //collect all rnProfileUIds
          
          //TWX: Collecting RNProfile info that is required by estore.
          //     We can use PartnerID and OriginatorID(Uinique identitfier for a ProcessInstance) 
          //     to link from Process Instance and its related audit file.
          if(rnprofileInfoHTable ==null )
          {
          	rnprofileInfoHTable = new Hashtable(134); //capacity 134, see declaration for detail
          }
          rnprofileInfoHTable.put((Long)rnProfile.getKey(),rnProfile);
          
        }

        //archive griddocuments related to these rnprofiles
        List rnProfileUIdForFailedGDocList = new ArrayList();
        
        //start
        if(rnProfileUIdColl.size() > _totalRecordWithinInOperator)
        {
          int processedRNProfileSize = 0;
          
          while(processedRNProfileSize < rnProfileUIdColl.size())
          {
            int count = 0;
            ArrayList rnProfileUIDSubList = new ArrayList();
            while( (count < _totalRecordWithinInOperator) && (processedRNProfileSize < rnProfileUIdColl.size()))
            {
              rnProfileUIDSubList.add(rnProfileUIdColl.get(processedRNProfileSize));
              ++count;
              ++processedRNProfileSize;
            }  
            //Two action: 4 doc, all doc has been resend/receive by/from own/partner for 3 times. Total doc in PI
            //for such a case is 16 doc. For safety, we break them into batch for deletion.
            Logger.log("Archive : rn profile subList "+rnProfileUIDSubList.size()+" is "+rnProfileUIDSubList.size());
            IDataFilter gridDocFilter = new DataFilterImpl();
            gridDocFilter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, rnProfileUIDSubList, false);
            archiveGridDocument.archive(gridDocFilter,rnProfileUIdForFailedGDocList,
                                        isLastChunk,rnprofileInfoHTable,estore, isEnableRestoreArchive, null); //archive gridDocuments dependent on rnProfile
            Logger.log("Archive : rn profile correspond gdoc end.");
            
          }
        }
        else
        {
          Logger.log("RN PROFILE UID COLL is within range ......");
          IDataFilter gridDocFilter = new DataFilterImpl();
          gridDocFilter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, rnProfileUIdColl, false);
          archiveGridDocument.archive(gridDocFilter,rnProfileUIdForFailedGDocList,
                                      isLastChunk,rnprofileInfoHTable,estore, isEnableRestoreArchive, null); //archive gridDocuments dependent on rnProfile
        }
        //end
        
        for (Iterator i = rnProfileColl.iterator(); i.hasNext();)
        {
          RNProfile rnProfile = (RNProfile) i.next();
          Long rnProfileUId = (Long) rnProfile.getKey();
          boolean isGDocArchiveFailed = rnProfileUIdForFailedGDocList.contains(rnProfileUId);
          boolean isRNProfileArchiveFailed=false;
          if(!isGDocArchiveFailed)
          {
            try
            {
              archiveEntity(rnProfile, PATH_PROCESSINSTANCE + "/");
            }
            catch(Throwable th)
            {
              isRNProfileArchiveFailed=true;
              dbArchive.err("[DirectArchiveProcessInstance.archiveRtProcessDoc] Error while archiveing rnProfile with UId="+rnProfile.getKey(),th);
            }
          }
          if(isGDocArchiveFailed || isRNProfileArchiveFailed) //dont delete rnprofile,rtprocessdoc
          { 
            String rtProcessDocId = rnProfile.getProcessInstanceId()+"/"+rnProfile.getProcessOriginatorId();
            rnProfileUIdColl.remove(rnProfileUId);
            rtProcessDocIdList.remove(rtProcessDocId);
            Logger.debug("[DirectArchiveProcessInstance.archiveRtProcessDoc] dont remove rnProfile with UId="+rnProfileUId+", rtProcessDocId="+rtProcessDocId);
          }
        }
        
        //TWX: cache first, delete after the whole archive process finished
        if(rnProfileUIdColl.size() > 0)
        {
        	for(Iterator uidList = rnProfileUIdColl.iterator(); uidList.hasNext(); )
        	{
        		Long rnUID = (Long)uidList.next();
        		rnprofileUIDList.add(rnUID);
        		//Logger.log("[DirectArchiveProcessInstance.archiveRtProcessDoc] cache rn profile ID is "+rnUID);
        	}
        }
      }

      //archive rtprocessdoc entities
      Collection rtProcessDocUIdList = new ArrayList(); //these UID's will be used to delete rtProcessDoc
      for (Iterator i = rtProcessDocColl.iterator(); i.hasNext();)
      {
        GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) i.next();
        boolean canArchiveRtprocessDoc = rtProcessDocIdList.contains(rtProcessDoc.getDocumentId());
        boolean isArchiveRtProcessDocFailed = false;
        if(canArchiveRtprocessDoc)
        {
          try
          {
            archiveEntity(rtProcessDoc,PATH_PROCESSINSTANCE + "/");
          }
          catch(Throwable th)
          {
            isArchiveRtProcessDocFailed=true;
            dbArchive.err("[DirectArchiveProcessInstance.archiveRtProcessDoc] Error while archiveing rtProcessDoc with UId="+rtProcessDoc.getKey(),th);
          }
        }
        if(canArchiveRtprocessDoc && !isArchiveRtProcessDocFailed)
        {
          rtProcessDocUIdList.add(rtProcessDoc.getKey());
        } 
        else //dont remove rtprocess entity
        {
          failedCount++;
          rtProcessUIdList.remove(rtProcessDoc.getRtBinaryCollaborationUId());
          Logger.debug("[DirectArchiveProcessInstance.archiveRtProcessDoc] dont remove rtProcessDoc with UId="+rtProcessDoc.getKey()+", rtProcessDocId="+rtProcessDoc.getDocumentId()+", rtProcessUId="+rtProcessDoc.getRtBinaryCollaborationUId());
        }
      }
      
      //By TWX: remove the rtProcessDoc later
      if(rtProcessDocUIdList.size()>0)
      {
      	for(Iterator i= rtProcessDocUIdList.iterator(); i.hasNext();)
      	{
      		Long processDocUID = (Long)i.next();
      		//processDocUIDList.add(rtProcessUIdList.get(i));
      		processDocUIDList.add(processDocUID);
      		//Logger.log("[DirectArchiveProcessInstance.archiveRtProcessDoc] cache processDoc UID is "+processDocUID);
      	}
      }
    }

  }
  

  
  private void archiveRtProcess(Collection rtProcessUIdColl, boolean isLastChunk) throws Throwable
  {
    if(rtProcessUIdColl==null || rtProcessUIdColl.isEmpty())
      return;
    IDataFilter rtProcessFilter = new DataFilterImpl();
    rtProcessFilter.addDomainFilter(null,GWFRtProcess.UID,rtProcessUIdColl,false);
    Collection<GWFRtProcess> rtProcessColl = getEntityDAO(GWFRtProcess.ENTITY_NAME).getEntityByFilter(rtProcessFilter);
    
    for(Iterator i = rtProcessColl.iterator();i.hasNext();)
    {
      GWFRtProcess rtProcess = (GWFRtProcess)i.next();
      archiveEntity(rtProcess, PATH_PROCESSINSTANCE + "/");
      
      if(! estore.getIsEstoreEnabled())
      {
        ++processInstanceCount;
      }
    }
    
    if(estore.getIsEstoreEnabled())
    {	
    	//Retrieve the ProcessInstances using the process instance UIDs
    	Collection<Map<Integer, Object>> processInstanceMap = ProcessInstanceActionHelper.findProcessInstanceMapsByPIUID(
    																		     rtProcessUIdColl);
    	try
    	{
    		Iterator<Map<Integer, Object>> ite = processInstanceMap.iterator();
    		while(ite.hasNext())
    		{
    			estore.populateProcessInstance(ite.next());
    			++processInstanceCount;
    		}
    	}
    	catch(Exception ex)
    	{
    		Logger.warn("[DirectArchiveProcessInstance.archiveRtProcess] error while caching process instance map.",ex);
    		throw new EStoreException("[DirectArchiveProcessInstance.archiveRtProcess] Error while caching process instance map. ",ex);
    	}
    }
    
    //cache first, delete when arhive process finished
    Object[] processContextUID = cacheProcessContextUIDList(rtProcessColl);
    
    //copy rtprocessUID and context UID
    if(processContextUID[0]!=null)
    {
    	copyProcessContextUID((ArrayList)processContextUID[0], (ArrayList)processContextUID[1]);
    }
  }

  private void removeRtRestrictionsOfRtProcesses(Collection rtProcessUIdColl) throws Throwable
  {
    if(rtProcessUIdColl==null || rtProcessUIdColl.size()==0)
      return;

    //get transActStateListUIdColl
    EntityDAOImpl rtRestrictionDAO = (EntityDAOImpl)getEntityDAO(GWFRtRestriction.ENTITY_NAME);
    IDataFilter rtRestrictionFilter = new DataFilterImpl();
    rtRestrictionFilter.addDomainFilter(null,GWFRtRestriction.RT_PROCESS_UID,rtProcessUIdColl,false);
    Collection transActStateListUIdColl = rtRestrictionDAO.getFieldValuesByFilter(GWFRtRestriction.TRANS_ACTIVATION_STATE_LIST_UID,rtRestrictionFilter);
    
    if(transActStateListUIdColl!=null && transActStateListUIdColl.size()>0)
    {
      //remove transActivationStates
      EntityDAOImpl transActivationStateDAO = (EntityDAOImpl)getEntityDAO(GWFTransActivationState.ENTITY_NAME);
      IDataFilter transActivationStateFilter = new DataFilterImpl();
      transActivationStateFilter.addDomainFilter(null,GWFTransActivationState.LIST_UID,transActStateListUIdColl,false);
      transActivationStateDAO.removeByFilter(transActivationStateFilter);
    }    
    rtRestrictionDAO.removeByFilter(rtRestrictionFilter);
    
  }


  private void cancelAlarmBySenderKeys(Collection iCalSenderKeyList)
  {
    if(iCalSenderKeyList==null || iCalSenderKeyList.size()==0)
      return;
    try
    {
      Logger.log("[DirectArchiveProcessInstance.cancelAlarmBySenderKeys] iCalSenderKeyList =" + iCalSenderKeyList);
      IDataFilter iCalAlarmFilter = new DataFilterImpl(); //make filter to cancel the iCalAlarms related to this GWFRtProcessDoc entities
      iCalAlarmFilter.addDomainFilter(null,iCalAlarm.SENDER_KEY,iCalSenderKeyList,false);
      iCalMgr.cancelAlarmByFilter(iCalAlarmFilter);
    }
    catch(Throwable th)
    {
      dbArchive.err("[DirectArchiveProcessInstance.archive] Error while deleting iCalAlarms ", th);
    } 
  }  

  private void archiveEntity(AbstractEntity entity, String category) throws Exception
  {
  	if(! isEnableRestoreArchive)
  	{
  		return;
  	}
  	
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
        Logger.log("[DirectArchiveProcessInstance.archiveEntity] File length is 0 for entity " + entity);
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
        Logger.log("[DirectArchiveProcessInstance.restoreEntity] restoring file fileName="+fileName);
        IEntityDAO entityDAO = getEntityDAO(entity.getEntityName());

        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, entity.getKeyId(), filter.getEqualOperator(), entity.getKey(), false);
        Collection entityColl = entityDAO.findByFilter(filter);
        if (entityColl != null && !entityColl.isEmpty())
        {
          throw new ApplicationException(
            "Repeated Restore:" + entity.getEntityName() + " with UID=" + entity.getKey() + " already exist! ");
        }
        else
        {
          //NSL20060818
          if (entity instanceof GWFRtProcessDoc)
          {
            convertDocType((GWFRtProcessDoc)entity);
          }
          entityDAO.create(entity, true);
        }
      }
    }
  }
  
  /**
   * To handle the additional '_' in front of the process doc type due to
   * BPSS validation constraint on names starting with digit, e.g. 3A4. 
   * This is to ensure the restored process docs (archived before V4.0) can be linked back correctly to the
   * process whose definition now contains the additional '_'.
   * @param rtProcessDoc
   */
  private void convertDocType(GWFRtProcessDoc rtProcessDoc)
  {
    String docType = rtProcessDoc.getDocumentType();
    String reqDocType = rtProcessDoc.getRequestDocType();
    String resDocTypes = rtProcessDoc.getResponseDocTypes();
    
    if (reqDocType != null && reqDocType.length()>0 && !reqDocType.startsWith("_"))
    {
      rtProcessDoc.setRequestDocType("_"+reqDocType);
      if (docType != null && docType.length()>0)
      {
        rtProcessDoc.setDocumentType("_"+docType);
      }
      
      if (resDocTypes != null && resDocTypes.length()>0)
      {
        Map respDocTypeMap = UtilString.strToMap(resDocTypes);
        
        Map newMap = new HashMap();
        Iterator iter = respDocTypeMap.keySet().iterator();
        while (iter.hasNext())
        {
          String resDocType = (String)iter.next();
          if (resDocType.length() > 0)
          {
            newMap.put('_'+resDocType, respDocTypeMap.get(resDocType));
          }
          else
          {
            newMap.put(resDocType, respDocTypeMap.get(resDocType));
          }
        }
        resDocTypes = UtilString.mapToStr(newMap);
        rtProcessDoc.setResponseDocTypes(resDocTypes);
      }      
    }
  }

  private IEntityDAO getEntityDAO(String entityName)
  {
    IEntityDAO entityDAO = EntityDAOFactory.getInstance().getDAOFor(entityName);
    return entityDAO;
  }
  
  /* moved under gtas.server.estore.helper.EStore.java
  private void transferCertificate() throws Exception
  {
    Logger.log("[DirectArchiveProcessInstance::transferCertificate] BEGIN");
    EStoreProxy proxy = new EStoreProxy();
    proxy.connect("192.168.213.68", 6763, "admin", "admin");
    TransferCertificateHelper helper = new TransferCertificateHelper(proxy);
    helper.transfer();
    proxy.close();
    Logger.log("[DirectArchiveProcessInstance::transferCertificate] END");
  } */
  
  /**
   * TWX
   * All the cached files(audit file, grid doc, udoc, attachment), db-records(rtProcessDoc, rnProfile) will be deleted.
   * @throws Throwable
   */
  public void deleteCacheFile() throws Throwable
  {
  	Logger.log("[DirectArchiveProcessInstance.deleteCacheFile] start delete cache file");

  	//Delete rtprocessdoc record
  	if(processDocUIDList != null && processDocUIDList.size() > 0)
  	{
  		deleteDBRecord(processDocUIDList, (Integer)GWFRtProcessDoc.UID, GWFRtProcessDoc.ENTITY_NAME);
  		Logger.log("[DirectArchiveProcessInstance.deleteCacheFile] total rtprocess doc be deleted "+processDocUIDList.size());
  	}
  	
  	//Delete rnprofile record: Possible exceed the max allow record in IN
  	if(rnprofileUIDList != null && rnprofileUIDList.size() > 0)
  	{
  		deleteDBRecord(rnprofileUIDList, (Integer)RNProfile.UID, RNProfile.ENTITY_NAME);
  		Logger.log("[DirectArchiveProcessInstance.deleteCacheFile] total rnprofile be deleted "+ rnprofileUIDList.size());
  	}
  	
  	//Delete rtprocess
    deleteRTProcess(processUIDList, contextUIDList);
    Logger.log("[DirectArchiveProcessInstance.deleteCacheFile] total rtprocess be deleted "+ processUIDList.size());
    
    //Delete the rtprocess doc correspond alarm
    cancelAlarmBySenderKeys(iCalSenderKeyListForRtProDoc);
    
    processUIDList.clear();
    contextUIDList.clear();
    processDocUIDList.clear();
  	rnprofileUIDList.clear();
    iCalSenderKeyListForRtProDoc.clear();
    
    //delete all the gdoc related document
    archiveGridDocument.deleteCacheFile();

    Logger.log("[DirectArchiveProcessInstance.deleteCacheFile] end delete cache file");
  }
  
  /**
   * TWX: It will remove the rt process related document.
   * and perform the delete action in one call
   * @param processUIDList
   * @param contextUIDList
   * @throws Throwable
   */
  
  private void deleteRTProcess(ArrayList processUIDList, ArrayList contextUIDList)
          throws Throwable
  {
  	if(processUIDList ==null || contextUIDList==null || processUIDList.isEmpty() || contextUIDList.isEmpty())
  		 return;
  	
  	Logger.log("[DirectArchiveProcessInstance.deleteRTProcess] deleteRTProcess");
  	//remove context data for this rtProcesses
    if(contextUIDList!=null && contextUIDList.size()>0)
    {
      IDataFilter contextDataFilter = new DataFilterImpl();
      contextDataFilter.addDomainFilter(null,ContextData.CONTEXT_UID,contextUIDList,false);
      EntityDAOImpl contextDataDAO = (EntityDAOImpl)getEntityDAO(ContextData.ENTITY_NAME);
      contextDataDAO.removeByFilter(contextDataFilter);
    }
    
    //remove all rtrestrictions and transactivation states for this rtprocesses
    removeRtRestrictionsOfRtProcesses(processUIDList);
    
    //get all activity uids belonging to this rtprocesses
    EntityDAOImpl rtProcessDAO = (EntityDAOImpl)getEntityDAO(GWFRtProcess.ENTITY_NAME);
    EntityDAOImpl rtActivityDAO = (EntityDAOImpl)getEntityDAO(GWFRtActivity.ENTITY_NAME);
    IDataFilter rtActivityFilter = new DataFilterImpl();
    rtActivityFilter.addDomainFilter(null,GWFRtActivity.RT_PROCESS_UID,processUIDList,false);
    Collection rtActivityUIdColl=rtActivityDAO.findByFilter(rtActivityFilter);
    
    for(int j = 0; j < processUIDList.size(); j++)
    {
    	Logger.log("process uid is "+processUIDList.get(j));
    }
    
    //remove subprocesses
    //ArrayList proUIDList = processUIDList; //store the old value of processUIDList
    
    if(rtActivityUIdColl!=null && rtActivityUIdColl.size()>0)
    {
      IDataFilter subRtProcessFilter  = new DataFilterImpl();
      subRtProcessFilter.addDomainFilter(null,GWFRtProcess.PARENT_RTACTIVITY_UID,rtActivityUIdColl,false);
      Collection subProcessColl = rtProcessDAO.getEntityByFilter(subRtProcessFilter);
      
      //by twx
      Object[] o = cacheProcessContextUIDList(subProcessColl);
      
      deleteRTProcess((ArrayList)o[0], (ArrayList)o[1]);
    }
    
    
    
    //remove activities
    rtActivityDAO.removeByFilter(rtActivityFilter);
    
    //remove rtProcesses
    IDataFilter rtProcessFilter = new DataFilterImpl();
    rtProcessFilter.addDomainFilter(null,GWFRtProcess.UID,processUIDList,false);
    rtProcessDAO.removeByFilter(rtProcessFilter);
    processUIDList = null;
    return;
  }
  
  //TWX
  private Object[] cacheProcessContextUIDList(Collection rtProcessColl)
  {
  	if(rtProcessColl==null || rtProcessColl.isEmpty())
  	{
  		Object [] o = {null, null};
  		return o;
  	}
  	
  	ArrayList<Long> proUID = new ArrayList<Long>();
  	ArrayList<Long> contextUID = new ArrayList<Long>();
  	
    for(Iterator i = rtProcessColl.iterator();i.hasNext();)
    {
      GWFRtProcess rtProcess = (GWFRtProcess)i.next();      
      proUID.add((Long)rtProcess.getKey());
      contextUID.add((Long)rtProcess.getContextUId());
    }
    Object[] a = {proUID, contextUID};
    return a;
  }
  
  //TWX
  private synchronized String getTimestampFormat(String format)
  {
    try
    {
      Thread.sleep(5);
    }
    catch (InterruptedException ex)
    {
    }
    long currentTime1 = System.currentTimeMillis();
    long currentTime2 = System.currentTimeMillis();
    while (currentTime1 == currentTime2)
    {
      currentTime2 = System.currentTimeMillis();
    }
    SimpleDateFormat df = new SimpleDateFormat(format);
    return df.format(new Date(currentTime2));
  }
  
/**
   * TWX: Delete the db record include gdoc, attachment if any
   * @param uid a list of uid that will be deleted
   * @param field the field which involve in delete criteria
   * @param entityName the entity name that we are gonna to remove from db
   * @throws SystemException
   */
  private void deleteDBRecord(ArrayList uid, Integer field, String entityName)
  	throws SystemException
  {
  	Logger.log("[DirectArchiveProcessInstance.deleteDBRecord] Start deleting entity "+entityName+"'s db record.");
    
    if(uid.size() <= _totalRecordWithinInOperator)
    {
      IDataFilter recordFilter = new DataFilterImpl();
      recordFilter.addDomainFilter(null,field,uid,false);
      EntityDAOImpl dao = (EntityDAOImpl)getEntityDAO(entityName);
      try
      {
        dao.removeByFilter(recordFilter);
      }
      catch(Exception ex)
      {
        throw new SystemException("[DirectArchiveProcessInstance.deleteDBRecord] error while deleting "+ entityName+ "'s db record.",ex);
      }
    }
    else
    {
      //Let's break into batch !
      int deletedSoFar = 0;
      while(deletedSoFar < uid.size())
      {
        ArrayList batchToBeDelete = new ArrayList();
        int currentInBatch = 0;
        while(currentInBatch < _totalRecordWithinInOperator && deletedSoFar < uid.size())
        {
          batchToBeDelete.add(uid.get(deletedSoFar));
          ++deletedSoFar;
          ++currentInBatch;
        }
        deleteDBRecord(batchToBeDelete, field, entityName);
      }
    }
    
    Logger.log("[DirectArchiveProcessInstance.deleteDBRecord] end delete entity "+entityName+"'s db record.");
  }
  
  //TWX
  private void copyProcessContextUID(ArrayList<Long> rtProcessUID, ArrayList<Long> contextUID)
  {
  	//rtProcesUID and contextUID store at parralel
  	for(int i =0; i < rtProcessUID.size(); i++)
  	{
  		processUIDList.add(rtProcessUID.get(i));
  		contextUIDList.add(contextUID.get(i));
  	}
  }
  
  private void triggerArchiveStartUpAlert(List<String> partnerIDForArchive, Hashtable<String, Object> criteriaTable, 
                                          Hashtable<String, Object> summaryHTable, boolean isEnableSearchArchive, boolean isEnableRestoreArchive)
  {
    Date archiveStartTime = new Date(TimeUtil.localToUtc(Calendar.getInstance().getTimeInMillis())); //the actual day, hour, minute, second
                                                                                                     //have been converted to UTC time     
    //fromTime, toTime are in UTC time
    Timestamp fromTime = (Timestamp)criteriaTable.get("FromStartTime");
    Timestamp toTime = (Timestamp)criteriaTable.get("ToStartTime");
    
    List processDefList = (List)criteriaTable.get("ProcessDefNames");
    Boolean includeInComplete = (Boolean)criteriaTable.get("IncludeInComplete");
    
    Boolean isArchiveOrphan = (Boolean)criteriaTable.get("IsArchiveOrphanRecord");
    String archiveID = (String)summaryHTable.get("ArchiveID");
    List beIDs = (List)criteriaTable.get("BeIDs");
    
    DefaultProviderList providerList = new DefaultProviderList();
    ArchiveAlertProvider provider = new ArchiveAlertProvider(Boolean.TRUE,archiveStartTime,new Date(fromTime.getTime()),new Date(toTime.getTime()),processDefList,includeInComplete,
    		                                                     "",isEnableRestoreArchive, isEnableSearchArchive,
    		                                                     partnerIDForArchive, beIDs, archiveID, isArchiveOrphan);
    providerList.addProvider(provider);
    
    try
    {
    	IAlertManagerObj alertMgr = ArchiveHelper.getAlertManager();
    	alertMgr.triggerAlert(IDBArchiveConstants.ARCHIVE_START_ALERT,providerList, (String)null);
    }
    catch(Exception ex)
    {
    	Logger.error(ILogErrorCodes.GT_PROCESS_INSTANCE_ARCHIVE,
    	             "[DirectArchiveProcessInstance.triggerArchiveStartUpAlert] Error in sending alert. Error: ", ex);
    }
  }
  
  public void setCriteriaTable(Hashtable criteriaHTB)
  {
  	criteriaHTable = criteriaHTB;
  }
  
  public void setSummaryTable(Hashtable summaryHTB)
  {
    summaryHTable = summaryHTB;
  }
  
  private Map<String, String> convertListToMap(List<String> list)
  {
  	Map<String, String> map = new Hashtable<String, String>();
  	if(list != null && list.size() > 0)
  	{
  		for(String s : list)
  		{
  			map.put(s, "");
  		}
  	}
  	return map;
  }
    
}
