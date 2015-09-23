/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2003    Neo Sok Lay         Add methods:
 *                                    findProcessInstanceKeysByUIDs(Collection),
 *                                    findProcessInstancesByUIDs(Collection)
 * Aug 22 2003    Guo Jianyu          Modified getProcessInstanceMapFromGWFProcess()
 *                                    to add USER_TRACKING_ID in the returned hashtable
 * Jan 19 2004    Daniel D'Cotta      Modified findProcessInstancesKeys(IDataFilter) to
 *                                    handle filter with no value condition, for sorting
 * Jul 01 2004    Neo Sok Lay         Add method: 
 *                                    - isProcessInstanceRunning(Long)
 * Jan 19 2006    Neo Sok Lay         GNDB00026521: Add methods:
 *                                    - cancelProcessInstance() on received NoF
 *                                    - findBizDocKeys()
 * Jan 26 2006    Tam Wei Xiang       Modifiy method cancelProcessInstance()   
 * Feb 06 2006    Tam Wei Xiang       Added method : findInBoundGDocByProfileUID()     
 * Feb 08 2006    Neo Sok Lay         Change cancelProcessInstance() to cancelProcessInstanceOnRecvNoF()
 *                                    for received NoF.                           
 * May 30 2006    Neo Sok Lay         GNDB00027212: To handle case whereby initiator
 *                                    cannot abort process on receive NoF from responder.                                   
 * Sep 29 2005    Tam Wei Xiang	      Add method:
 * 				      - getProcessInstanceInfoMapFromGWFProcess
 *                                    - findProcessInstanceMapsByPIUID   
 * Dec 18 2006    Tam Wei Xiang       Added in failedReason and DetailReason      
 * Jul 23 2007    Tam Wei Xiang       Fix defect for GNDB00028432. The domain filter for fecthing
 *                                    the process id is removed.                                                                                                
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.model.rnif.IProcessInstance;
import com.gridnode.gtas.model.rnif.RnifFieldID;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.IGridDocument;
import com.gridnode.gtas.server.rnif.model.ProcessInstance;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.helpers.BpssGenerator;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssDefinitionHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.filter.IValueFilter;
import com.gridnode.pdip.framework.db.filter.SingleValueFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the Rnif module.
 *
 */
public final class ProcessInstanceActionHelper implements IProcessInstance
{

  static HashMap _map= null;
  static {
    _map= setUpFailReasonHashTable();
  }

  // ***************** Get Manager Helpers *****************************
  public static IGWFWorkflowManagerObj getWorkflowMgr() throws ServiceLookupException
  {
    return (IGWFWorkflowManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IGWFWorkflowManagerHome.class.getName(),
      IGWFWorkflowManagerHome.class,
      new Object[0]);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of a ProcessInstance based on the specified uID.
   * @since 2.0
   */
  public static void verifyProcessInstance(Long instUid) throws Exception
  {
    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    wfMgr.getProcessInstance(instUid);
  }

  /**
   * Get the state of the process instance
   * 
   * @param instUid The UID of the ProcessInstance
   * @return the state of the processinstance
   */
  public static Integer getProcessInstanceState(Long instUid)
  {
    Integer state = null;
    try
    {
      GWFRtProcess process = getWorkflowMgr().getProcessInstance(instUid);
      if (process != null)
        state = process.getState();
    }
    catch (Exception ex)
    {
      Logger.warn("[ProcessInstanceActionHelper.isProcessInstanceRunning] Unable to retrieve processinstance: "+
        ex.getMessage());
    }
    return state;
  }
  // ******************** Conversion Helpers ******************************

  /**
   * Retrieve the ProcessInstances using the specified process instance UIDs.
   *
   * @param instanceUids The UIDs of the ProcessInstances to retrieve.
   * @return Collection of ProcessInstance <b>entities<b> retrieved.
   * @since GT 2.2 I1
   */
  public static Collection findProcessInstanceMapsByUIDs(Collection instanceUids) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GWFRtProcess.UID, instanceUids, false);

    IGWFWorkflowManagerObj wfMgr = getWorkflowMgr();
    Collection rtProcessList = wfMgr.getProcessInstanceList(filter);

    ArrayList result = new ArrayList();
    if (rtProcessList == null)
       return result;
    for (Iterator iterator= rtProcessList.iterator(); iterator.hasNext();)
    {
      GWFRtProcess process= (GWFRtProcess) iterator.next();
      Map map= getProcessInstanceMapFromGWFProcess(wfMgr, process);
      result.add(map);
    }
    return result;
  }
  
  /**
   * TWX: Created for estore logic. this method perform similiar action to
   * findProcessInstanceMapsByUIDs.
   * Retrieve the ProcessInstances using the specified process instance UIDs.
   * 
   * @param instanceUids The UIDs of the ProcessInstances to retrieve.
   * @return Collection of ProcessInstance <b>entities<b> retrieved.
   * @since 
   */
  public static Collection<Map<Integer, Object> > findProcessInstanceMapsByPIUID(Collection instanceUids) 
         throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GWFRtProcess.UID, instanceUids, false);

    IGWFWorkflowManagerObj wfMgr = getWorkflowMgr();
    Collection rtProcessList = wfMgr.getProcessInstanceList(filter);

    ArrayList<Map<Integer, Object>> result = new ArrayList<Map<Integer, Object>>();
    if (rtProcessList == null)
       return result;
    for (Iterator iterator= rtProcessList.iterator(); iterator.hasNext();)
    {
      GWFRtProcess process= (GWFRtProcess) iterator.next();
      Map<Integer, Object> map= getProcessInstanceInfoMapFromGWFProcess(wfMgr, process);
      result.add(map);
    }
    return result;
  }
  
  public static Collection findProcessInstancesByUIDs(Collection instanceUids) throws Exception
  {
    Collection mapCollection = findProcessInstanceMapsByUIDs(instanceUids);

    ArrayList result = new ArrayList();
    for (Iterator iterator= mapCollection.iterator(); iterator.hasNext();)
    {
      Map map= (Map) iterator.next();
      if(map!=null)
      {
        ProcessInstance instance = (ProcessInstance)ProcessInstance.convertMapToEntity(
                                      RnifFieldID.getProcessInstanceFieldID(),
                                      new HashMap(map));
        result.add(instance);
      }
    }
    return result;
  }

  /**
   * Retrieve the keys of ProcessInstances using the specified uids.
   * This method effectively filter off those ProcessInstance UIDs
   * that are non-existence in GWFRtProcess.
   *
   * @param instanceUids UIDs of the ProcessInstances to retrieve.
   * @return Collection of keys (uids) of ProcessInstances retrieved.
   * @since GT 2.2 I1
   */
  public static Collection findProcessInstanceKeysByUIDs(Collection instanceUids) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GWFRtProcess.UID, instanceUids, false);

    IGWFWorkflowManagerObj wfMgr = getWorkflowMgr();
    Collection instanceUidList = wfMgr.getProcessInstanceKeys(filter);

    return instanceUidList;
  }

  public static void deleteProcessInstance(Long instUid, boolean alsoDeleteDocument)
    throws java.lang.Exception
  {
    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    //GWFRtProcess process= wfMgr.getProcessInstance(instUid);
    Collection docList= wfMgr.getRtProcessDocList(instUid);
    GWFRtProcessDoc processDoc= null;
    if (docList != null && !docList.isEmpty())
    {
      processDoc= (GWFRtProcessDoc) docList.iterator().next();
    }
    String docId= processDoc.getDocumentId();
    //String[] seperateInstId= getSeperateIdsFromBPSSDocId(docId);
    String[] instId= getSeperateIdsFromBPSSDocId(docId);
    new ProfileUtil().deleteProfileofInstance(instId, alsoDeleteDocument);
    wfMgr.removeProcessInstance(instUid);
  }

  public static void cancelProcessInstance(Long instUid, String reason) throws java.lang.Exception
  {
    getWorkflowMgr().cancelProcessInstance(instUid, reason);
  }

  public static Map getProcessInstance(Long instUid) throws Exception
  {
    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    GWFRtProcess process= wfMgr.getProcessInstance(instUid);
    return getProcessInstanceMapFromGWFProcess(wfMgr, process);
  }
  public static Map getProcessInstance(Long instUid, String defName) throws Exception
  {
    Map res= getProcessInstance(instUid);
    //040129NSL Should not overwrite, the Map already contains the defName.
    //res.put(PROCESS_DEF_NAME, defName);
    return res;
  }

  static Map getProcessInstanceMapFromGWFProcess(
    IGWFWorkflowManagerObj wfMgr,
    GWFRtProcess process)
    throws Exception
  {
    Long instUid= (Long) process.getKey();
    Collection docList= wfMgr.getRtProcessDocList(instUid);

    if (docList != null && !docList.isEmpty())
    {
    GWFRtProcessDoc processDoc= (GWFRtProcessDoc) docList.iterator().next();

    String defName= null;
    //String docType= processDoc.getDocumentType();
    String docType= processDoc.getRequestDocType(); //NSL20060213 should use request doctype as document type is not populated for received doc
    if (docType != null)
    {
      int index= docType.lastIndexOf('_');
      defName= docType.substring(0, index);
      
      //NSL20060106 strip the first '_'
      if (defName.charAt(0) == '_')
      {
        defName = defName.substring(1);
      }
    }
    String docId= processDoc.getDocumentId();
    String[] seperateInstId= getSeperateIdsFromBPSSDocId(docId);
    Map res= new HashMap();
    res.put(UID, instUid);
    res.put(PROCESS_DEF_NAME, defName);
    res.put(ROLE_TYPE, processDoc.getRoleType());
    res.put(PARTNER, processDoc.getPartnerKey());
    res.put(PROCESS_INSTANCE_ID, seperateInstId[IRnifConstant.INDEX_PROCESS_INSTANCE_ID]);
    res.put(STATE, process.getState());
    res.put(START_TIME, process.getStartTime());
    res.put(END_TIME, process.getEndTime());
    Integer retryNum= processDoc.getRetryCount();
    if (retryNum != null && (retryNum.intValue() > 0))
      retryNum= new Integer(retryNum.intValue() - 1);
    res.put(RETRY_NUM, retryNum);
    res.put(IS_FAILED, new Boolean(process.isFailed())); // Boolean
    String exceptionString= processDoc.getExceptionSignalType();
    if (exceptionString != null)
    {
      Integer failReason= getFailedReason(exceptionString);
      res.put(FAIL_REASON, failReason); //@@todo
      res.put(DETAIL_REASON, BpssHandler.getReasonStr(processDoc.getReason())); // String
    }
    Collection docs= findDocumentKeysOfInstance(seperateInstId);
    if ((docs != null) && (!docs.isEmpty()))
    {
      Long docUid = (Long)docs.iterator().next();
      GridDocument gdoc = DocumentUtil.getDocumentByKey(docUid);
      if (gdoc != null)
        res.put(USER_TRACKING_ID, gdoc.getUserTrackingID());
      else
        res.put(USER_TRACKING_ID, null);
    }
    else
    {
      res.put(USER_TRACKING_ID, null);
      docs = new ArrayList();
    }
    res.put(ASSOC_DOCS, docs);

    Logger.debug("[getProcessInstanceMapFromGWFProcess] returns:" + instMap2Str(res));
    return res;
    } else return null;


  }
  
  /**
   * TWX: Created for EStore Logic
   * It will return a processInstance info map. The function is similiar to
   * getProcessInstanceMapFromGWFProcess except the info we retrieve is needed by
   * estore.
   */
  static Map<Integer, Object> getProcessInstanceInfoMapFromGWFProcess( IGWFWorkflowManagerObj wfMgr,
             GWFRtProcess process)
         throws Exception
    {
  		Logger.log("[ProcessInstanceActionHelper.getProcessInstanceInfoMapFromGWFProcess] entering....");
  	  final Integer ProcessInstance_ID = new Integer(0);
  	  final Integer Process_State = new Integer(1);
  	  final Integer Start_Time = new Integer(2);
  	  final Integer End_Time = new Integer(3);
  	  //final Integer Partner_Duns = new Integer(4);
  	  final Integer Process_Def = new Integer(4);
  	  final Integer Role_Type = new Integer(5);
  	  final Integer Originator_ID = new Integer(6); 
      final Integer FAILED_REASON = new Integer(7);
      final Integer DETAIL_REASON = new Integer(8);
      final Integer RETRY_NUMBER = new Integer(9);
      
      Long instUid= (Long) process.getKey();
      Collection docList= wfMgr.getRtProcessDocList(instUid);

      if (docList != null && !docList.isEmpty())
      {
      	GWFRtProcessDoc processDoc= (GWFRtProcessDoc) docList.iterator().next();

      	String defName= null;
      	String processDef= processDoc.getRequestDocType();
      	if (processDef != null)
      	{
      		int index= processDef.lastIndexOf('_');
      		defName= processDef.substring(0, index);

          //NSL20060106 strip the first '_'
          if (defName.charAt(0) == '_')
          {
            defName = defName.substring(1);
          }

      	}
      	String docId= processDoc.getDocumentId();
      	String[] seperateInstId= getSeperateIdsFromBPSSDocId(docId);
      	
      	Map<Integer, Object> res= new HashMap<Integer, Object>();
      	res.put(Process_Def, defName);
      	res.put(Role_Type, processDoc.getRoleType());
      	
      	String processInstanceID = seperateInstId[IRnifConstant.INDEX_PROCESS_INSTANCE_ID];
      	
      	res.put(ProcessInstance_ID, processInstanceID);
      	
      	res.put(Process_State, process.getState());
      	res.put(Start_Time, process.getStartTime());
      	res.put(End_Time, process.getEndTime());
      	
      	String originatorID = seperateInstId[IRnifConstant.INDEX_PROCESS_INITIATOR];
      	
      	//String partnerDuns = rnproInfo!=null?rnproInfo[1]:"";
      	
      	//res.put(Partner_Duns, partnerDuns);
      	res.put(Originator_ID, originatorID);
      	
        String exceptionString= processDoc.getExceptionSignalType();
        if (exceptionString != null)
        {
          Integer failReason= getFailedReason(exceptionString);
          res.put(FAILED_REASON, failReason); 
          res.put(DETAIL_REASON, BpssHandler.getReasonStr(processDoc.getReason())); // String
        }
        
        Integer retryNum= processDoc.getRetryCount();
        if (retryNum != null && (retryNum.intValue() > 0))
        {
          retryNum= new Integer(retryNum.intValue() - 1);
        }
        res.put(RETRY_NUMBER, retryNum);
        
      	return res;
      } 
      else
      {
      	Logger.log("[ProcessInstanceActionHelper.getProcessInstanceInfoMapFromGWFProcess] No correspond rtprocess doc match given rtprocess uid "+instUid);
      	return null;
      }		
    }

  //only accept filter based on ProcessName
  public static Collection findProcessInstancesByName(IDataFilter filter) throws Exception
  {
    if (filter == null)
    {
      filter=
        EntityUtil.getEqualFilter(
          new Number[] { IGWFRtProcess.ENGINE_TYPE, IGWFRtProcess.PROCESS_TYPE },
          new Object[] { "BPSS", "BpssBinaryCollaboration" });
      return findProcessInstances(filter);
    }

    DataFilterImpl filterImpl= (DataFilterImpl) filter;
    IValueFilter valueFilter= filterImpl.getValueFilter();
    SingleValueFilter singleValueFilter= (SingleValueFilter) valueFilter;
    Object processDefName= singleValueFilter.getSingleValue();
    if (processDefName == null || !(processDefName instanceof String))
      throw new FindEntityException("GetProcessInstanceList must be based on a processdef Name!");

    return findProcessInstances((String) processDefName);
  }

  public static Collection findProcessInstances(String defName) throws Exception
  {
    Logger.debug("[ProcessInstanceActionHelper.findProcessInstances] defName=" + defName);
    ProcessDef def= ProcessUtil.getProcessDef(defName);
    if (def == null)
    {
      Logger.log("[ProcessInstanceActionHelper.findProcessInstances(defName) no processdef found.");
    }
    BpssGenerator generator= new BpssGenerator(def);
    String processId= generator.getTransactionName();
    String processType= "BpssBinaryCollaboration";
    String processSpecName= generator.getProcessName();
    String processSpecVersion= generator.getVersion();
    String processSpecUUid= generator.getUuid();
    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    Collection processes=
      wfMgr.getBpssProcessInstanceList(
        processId,
        processType,
        processSpecName,
        processSpecVersion,
        processSpecUUid);
    if (processes == null)
      return new ArrayList();

    ArrayList res= new ArrayList();
    for (Iterator iterator= processes.iterator(); iterator.hasNext();)
    {
      GWFRtProcess process= (GWFRtProcess) iterator.next();
      Map map= getProcessInstanceMapFromGWFProcess(wfMgr, process);
      if(map!=null){
    map.put(PROCESS_DEF_NAME, defName);
    res.add(map);
    }
    }
    return res;
  }

  public static Collection findProcessInstancesKeys(String defName) throws Exception
  {
    Logger.debug("[ProcessInstanceActionHelper.findProcessInstanceKeys] defName=" + defName);
    ProcessDef def= ProcessUtil.getProcessDef(defName);
    if (def == null)
    {
      Logger.log("[ProcessInstanceActionHelper.findProcessInstanceKeys(defName) no processdef found.");
    }
    BpssGenerator generator= new BpssGenerator(def);
    String processId= generator.getTransactionName();
    String processType= "BpssBinaryCollaboration";
    String processSpecName= generator.getProcessName();
    String processSpecVersion= generator.getVersion();
    String processSpecUUid= generator.getUuid();
    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    Collection keys=
      wfMgr.getBpssProcessInstanceKeys(
        processId,
        processType,
        processSpecName,
        processSpecVersion,
        processSpecUUid);
    if (keys == null)
      return new ArrayList();

    return keys;
  }

  private static IEntity getBpssDefinition(String defName) throws Exception
  {
    ProcessDef def= ProcessUtil.getProcessDef(defName);
    if (def == null)
    {
      throw new NullPointerException("[ProcessInstanceActionHelper.getBpssDefinition("+defName+") no processdef found.");
    }
    BpssGenerator generator= new BpssGenerator(def);
    String processId= generator.getTransactionName();
    String processType= "BpssBinaryCollaboration";
    String processSpecName= generator.getProcessName();
    String processSpecVersion= generator.getVersion();
    String processSpecUUid= generator.getUuid();
    IEntity entity = null;
    try
    {
      entity = (IEntity) BpssDefinitionHelper.getBpssDefinition(processId, processType, processSpecName, processSpecVersion, processSpecUUid);
    }
    catch(Throwable th)
    {
        throw new SystemException(
          "[ProcessInstanceActionHelper.getBpssDefinition] Error retrieving BpssDefinition given (processId,processType,processSpecName,processSpecVersion,processSpecUUid)=("
            + processId
            + ","
            + processType
            + ","
            + processSpecName
            + ","
            + processSpecVersion
            + ","
            + processSpecUUid
            + ")",
          th);
    }
    return entity;
  }
  
  public static Collection findProcessInstances(IDataFilter filter) throws Exception
  {
    Logger.debug(
      "[ProcessInstanceActionHelper.findProcessInstances] origfilter=" + filter == null
        ? null
        : filter.getFilterExpr());
    filter= convertToWorkflowFilter(filter);
    Logger.debug(
      "[ProcessInstanceActionHelper.findProcessInstances] converted filter="
        + filter.getFilterExpr());

    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    Collection processes= wfMgr.getProcessInstanceList(filter);
    if (processes == null)
      return new ArrayList();

    ArrayList res= new ArrayList();
    for (Iterator iterator= processes.iterator(); iterator.hasNext();)
    {
      GWFRtProcess process= (GWFRtProcess) iterator.next();
      Map map= getProcessInstanceMapFromGWFProcess(wfMgr, process);
    if(map!=null)
    res.add(map);
    }
    return res;
  }

  // 20040126 DDJ: only allow filter by name or uid, but allows sorting on most fields
  public static Collection findProcessInstancesKeys(IDataFilter filter) throws Exception
  {
    Logger.debug( "[ProcessInstanceActionHelper.findProcessInstancesKeys] original filter=" 
                  + (filter == null ? null : filter.getFilterExpr()));
//System.out.println( "### DEBUG: ProcessInstanceActionHelper.findProcessInstancesKeys():  original filter=" 
//                    + (filter == null ? null : filter.getFilterExpr()));
                  
/* 20040126 DDJ: Refactored
    IGWFWorkflowManagerObj wfMgr= getWorkflowMgr();
    Collection keys =null;

    //if (filter == null)
    if (filter == null || filter.getValueFilter() == null) // 20040119 DDJ
    {
      filter = EntityUtil.getEqualFilter(
      new Number[] { IGWFRtProcess.ENGINE_TYPE, IGWFRtProcess.PROCESS_TYPE },
      new Object[] { "BPSS", "BpssBinaryCollaboration" });
      keys=wfMgr.getProcessInstanceKeys(filter);
    }
    else
    {
      DataFilterImpl filterImpl= (DataFilterImpl) filter;
      IValueFilter valueFilter= filterImpl.getValueFilter();
      SingleValueFilter singleValueFilter= (SingleValueFilter) valueFilter;
      Object value = singleValueFilter.getSingleValue();

      if (value == null)
      {
        throw new FindEntityException("A single filter must be supplied to GetProcessInstanceListAction.retrieveEntityList()!");
      }
      else if (value instanceof String)
      {
        keys = findProcessInstancesKeys((String)value);
      }
      else if (value instanceof Long)
      {
        ArrayList uids = new ArrayList();
        uids.add(value);
        keys = findProcessInstanceKeysByUIDs(uids);
      }
    }

    if (keys == null)
    {
      return new ArrayList();
    }

    return keys;
*/
    
    // create base filter
    IGWFWorkflowManagerObj wfMgr = getWorkflowMgr();
    Collection keys = null;
    
    IDataFilter wfFilter = EntityUtil.getEqualFilter( new Number[] { IGWFRtProcess.ENGINE_TYPE, IGWFRtProcess.PROCESS_TYPE },
                                                      new Object[] { "BPSS", "BpssBinaryCollaboration" }); 
    
    if (filter != null)
    {
      // filtering
      if(filter.getValueFilter() != null)
      {
        // extract the original filter
        DataFilterImpl filterImpl = (DataFilterImpl) filter;
        IValueFilter valueFilter = filterImpl.getValueFilter();
        SingleValueFilter singleValueFilter;
        try
        {
          singleValueFilter = (SingleValueFilter)valueFilter;
        }
        catch(Throwable t)
        {
          throw new FindEntityException("A single filter must be supplied to GetProcessInstanceListAction.retrieveEntityList()!", t);  
        }
        
        // extract the filter details
        Object value = singleValueFilter.getSingleValue();
        Number field = (Number)singleValueFilter.getFilterField();
        FilterOperator operator = singleValueFilter.getOperator();
        
        // add original filter details to the base filter
        if(IProcessInstance.UID.equals(field))
        {
          wfFilter.addSingleFilter(filter.getAndConnector(), IGWFRtProcess.UID, operator, value, false);
        }
        else if(IProcessInstance.PROCESS_DEF_NAME.equals(field))
        {
          //TWX 23072007: fix defect for GNDB00028432. Remove the domain filter            
          IEntity bpssDefinition = getBpssDefinition((String)value);
          wfFilter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.PROCESS_UID, filter.getEqualOperator(), bpssDefinition.getKey(), false);
          wfFilter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(), bpssDefinition.getEntityName(), false);
        }
        else
        {
          Logger.debug("[ProcessInstanceActionHelper.findProcessInstancesKeys] Ignoring unsupported filter field=" + field);
//System.out.println("### DEBUG: ProcessInstanceActionHelper.findProcessInstancesKeys(): Ignoring unsupported filter field=" + field);      
        }
      }
      
      // sorting (only support single field sort)
      Object[] orderFields = filter.getOrderFields();
//System.out.println("### DEBUG: ProcessInstanceActionHelper.findProcessInstancesKeys(): orderFields.class=" + (orderFields == null ? null : orderFields.getClass().getName()));      
      if(orderFields != null && orderFields.length > 0)
      {
        // map IProcessInstance fields to IGWFRtProcess fields
        Number orderField = (Number)orderFields[0];
//System.out.println("### DEBUG: ProcessInstanceActionHelper.findProcessInstancesKeys(): orderField=" + orderField);      
        if(IProcessInstance.PROCESS_INSTANCE_ID.equals(orderField))
        {
          orderField = IGWFRtProcess.CONTEXT_UID;
        }
        else if(IProcessInstance.STATE.equals(orderField))
        {
          orderField = IGWFRtProcess.STATE;
        }
        else if(IProcessInstance.START_TIME.equals(orderField))
        {
          orderField = IGWFRtProcess.START_TIME;
        }
        else if(IProcessInstance.END_TIME.equals(orderField))
        {
          orderField = IGWFRtProcess.END_TIME;
        }
        else if(IProcessInstance.IS_FAILED.equals(orderField))
        {
          orderField = IGWFRtProcess.STATE; // closest match
        }
        else
        {
          Logger.debug("[ProcessInstanceActionHelper.findProcessInstancesKeys] Ignoring unsupported order field=" + orderField);
//System.out.println( "### DEBUG: ProcessInstanceActionHelper.findProcessInstancesKeys(): Ignoring unsupported order field=" + orderField);
          orderField = IGWFRtProcess.PROCESS_UID;
        }
        
        boolean sortOrder = filter.getSortOrders()[0];
        wfFilter.addOrderField(orderField, sortOrder);
      }
    }

    Logger.debug( "[ProcessInstanceActionHelper.findProcessInstancesKeys] converted filter=" 
                  + (wfFilter == null ? null : wfFilter.getFilterExpr()));
//System.out.println( "### DEBUG: ProcessInstanceActionHelper.findProcessInstancesKeys(): converted filter=" 
//                    + (wfFilter == null ? null : wfFilter.getFilterExpr()));
                  
    // get keys from manager using filter
    keys = wfMgr.getProcessInstanceKeys(wfFilter);
    if (keys == null)
    {
      return new ArrayList();
    }
    return keys;
  }

  static String[] getSeperateIdsFromBPSSDocId(String docId)
  {
    return BpssHandler.getSeperateIdsFromBPSSDocId(docId);
  }

  static HashMap<String,Integer> setUpFailReasonHashTable()
  {
    HashMap<String,Integer> map= new HashMap<String,Integer>();
    map.put(IBpssConstants.EXCEPTION_SIGNAL, new Integer(FR_RECEIVE_EX));
    map.put(IBpssConstants.EXCEPTION_VALIDATE, new Integer(FR_VALIDATE_EX));
    map.put(IBpssConstants.EXCEPTION_TIMETO_ACK, new Integer(FR_RETRY_OUT));
    map.put(IBpssConstants.EXCEPTION_TIMETO_PERFORM, new Integer(FR_ACT_OUT));
    map.put(IBpssConstants.EXCEPTION_CANCEL, new Integer(FR_USER_CANCEL));
    return map;
  }

  static Integer getFailedReason(String exceptionString)
  {
    return (Integer) _map.get(exceptionString);
  }

  public static Collection findDocumentKeysOfInstance(String[] instId) throws Exception
  {
    ProfileUtil profileUtil= new ProfileUtil();
    Collection profileKeys= profileUtil.getProfileKeysOfInstance(instId);
    if (profileKeys != null && !profileKeys.isEmpty())
    {
      return DocumentUtil.findGridDocKeysByProfileUid(null, profileKeys, IGridDocument.UID, true);
    }
    return null;
  }

  static IDataFilter convertToWorkflowFilter(IDataFilter filter)
  {
    //    if(filter == null)
    //    {
    //      filter = new DataFilterImpl();
    //      filter.addSingleFilter(null, IGWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(), "BpssBinaryCollaboration", false);
    //    }
    //    else
    //      filter.addSingleFilter(filter.getAndConnector(), IGWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(), "BpssBinaryCollaboration", false);
    return filter;
  }

  public static Collection findAckedSendBizDocKeys(GridDocument signalDoc) throws Exception
  {
    ProfileUtil profileUtil= new ProfileUtil();
    RNProfile profile = profileUtil.getProfileMaytExist(signalDoc);
    if(profile == null)
    {
      Logger.warn("cannot found the RNProfile assoced with the signal doc, RNProfile Uid is:" + signalDoc.getRnProfileUid());
      return new ArrayList();
    }
    if(!profile.getIsSignalDoc())
    {
      Logger.warn("the input argument is not a signal document!");
      return new ArrayList();
    }
    String instId = profile.getProcessInstanceId();
    String originatorId = profile.getProcessOriginatorId();
    String senderDUNS = profile.getReceiverGlobalBusIdentifier();

    Collection profileKeys= profileUtil.getProfileKeysOfBizDocSendByDUNS(instId, originatorId, senderDUNS);
    if (profileKeys != null && !profileKeys.isEmpty())
    {
      return DocumentUtil.findGridDocKeysByProfileUid(null, profileKeys);
    }
    return null;
  }
  /**
   * Find the GridDocument UIDs for the business document sent by the specified party
   * @param instId The process instance id
   * @param senderDUNS The sender's DUNS
   * @param isRequestMsg Whether the business document is a request message
   * @return Collection of UIDs for the GridDocument
   * @throws Exception
   */
  public static Collection findBizDocKeys(String instId, String senderDUNS, boolean isRequestMsg) throws Exception
  {
    ProfileUtil profileUtil= new ProfileUtil();
    Collection profileKeys= profileUtil.getProfileKeysOfBizDocSendByDUNS(instId, senderDUNS, isRequestMsg);
    if (profileKeys != null && !profileKeys.isEmpty())
    {
      return DocumentUtil.findGridDocKeysByProfileUid(null, profileKeys);
    }
    return null;
  }

  /**
   * Cancel the process instance(s) due to NoF received
   * @param failurePipGDoc The GridDocument of the NoF received
   * @param pipInstanceIdentifier The PIPInstanceIdentifier in the requesting message of the failing process.
   * @param reason The reason of the failure.
   * @throws Exception
   */
  public static void cancelProcessInstanceOnRecvNoF(GridDocument failurePipGDoc, String pipInstanceIdentifier, String reason, 
                                           String proprietaryDocIdentifier) throws Exception
  {
    ProfileUtil profileUtil= new ProfileUtil();
    RNProfile rnProfileNoF = profileUtil.getProfileMustExist(failurePipGDoc);
    ArrayList<String> possibleOriginatorIds = new ArrayList<String>();
    possibleOriginatorIds.add(rnProfileNoF.getSenderGlobalBusIdentifier());
    possibleOriginatorIds.add(rnProfileNoF.getReceiverGlobalBusIdentifier());
    
    //This would identify, at least, the rnprofile with the supposedly unique proprietaryDocIdentifier.
    //Here, we can only assume that the two partners would not send the same process instance id and proprietary document identifier.
    Collection profileKeys = profileUtil.getProfileKeysOfRequestingMsg(pipInstanceIdentifier, possibleOriginatorIds, 
                                                                       proprietaryDocIdentifier);
    if (profileKeys != null && !profileKeys.isEmpty())
    {
    	//TWX 06022006 retrieve one gdoc and it is reside in inbound
      //NSL20060530 also include the outbound docs. 
      Collection gDocs = findIBOBGDocByProfileUID(profileKeys);
      if (gDocs != null && !gDocs.isEmpty())
      {
        GridDocument gdoc = (GridDocument)gDocs.iterator().next();
        Long instUid = gdoc.getProcessInstanceUid();
        if (instUid != null)
        {
          //issit safe enough ?
          cancelProcessInstance(instUid, reason);
            
          //TWX: 26012006raise alert while we receive 0A1 from partner
          raiseProcessInstanceFailureAlert(gdoc, reason);
        }
      }
    }
  }
  
  //TWX: 26012006
  private static void raiseProcessInstanceFailureAlert(GridDocument gdoc, String reason)
  {
  	AlertUtil.alertFailureNotificationReceived(gdoc, reason);
  }
  
  /**
   * NSL20060530 Also include the outbound docs. 
   * TWX 06022006 Find a list of gdoc which reside in inbound folder given the profile UID
   * @param profileUids the profile UID that tie to GDOC
   * @return Collection of GridDocuments found.
   * @throws Exception
   */
  private static Collection findIBOBGDocByProfileUID(Collection profileUids)
  	throws Exception
  {
  	IDataFilter filter = new DataFilterImpl();
  	filter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, profileUids, false);
    String[] folders = {GridDocument.FOLDER_INBOUND, GridDocument.FOLDER_OUTBOUND};
    filter.addDomainFilter(filter.getAndConnector(), GridDocument.FOLDER, Arrays.asList(folders), false);
  	//filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(), "Inbound", false);
  	return getDocumentManager().findGridDocuments(filter);
  }
  
  private static IDocumentManagerObj getDocumentManager() throws ServiceLookupException
  {
    return (IDocumentManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }
  
  static String instMap2Str(Map map)
  {
    if(map == null)
      return "";
    String res= "\t";
    res= "UID=" + map.get(UID);
    res += "\tPROCESS_DEF_NAME=" + map.get(PROCESS_DEF_NAME);
    res += "\tROLE_TYPE=" + map.get(ROLE_TYPE);
    res += "\tPARTNER" + map.get(PARTNER);
    res += "\tPROCESS_INSTANCE_ID=" + map.get(PROCESS_INSTANCE_ID);
    res += "\tSTATE=" + map.get(STATE);
    res += "\tSTART_TIME=" + map.get(START_TIME);
    res += "\tEND_TIME=" + map.get(END_TIME);
    res += "\tRETRY_NUM=" + map.get(RETRY_NUM);
    res += "\tIS_FAILED=" + map.get(IS_FAILED);
    res += "\tFAIL_REASON=" + map.get(FAIL_REASON);
    res += "\tDETAIL_REASON=" + map.get(DETAIL_REASON);
    res += "\tASSOC_DOCS=" + map.get(ASSOC_DOCS);
    res += "\tUSER_TRACKING_ID=" + map.get(USER_TRACKING_ID);
    return res;
  }
}