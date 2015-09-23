/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TrailInfoHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 04, 2007    Tam Wei Xiang       Remove all the transaction demarcation code.
 *                                     It will be handled by the invoker.
 * Jan 08, 2007    Tam Wei Xiang       Change the UID from Long to String
 * Feb 02, 2007    Tam Wei Xiang       handle the population of common resource eg
 *                                     pip name, doc type that belong to a particular group.   
 * Mar 01, 2007    Tam Wei Xiang       Init the linkage between the reprocess doc event
 *                                     and the newly created ProcessTransaction after
 *                                     user has performed reprocessing op.
 * Apr 17, 2007    Tam Wei Xiang       To reflect the new customer name in the common resource.
 * May 04, 2007    Tam Wei Xiang       The update of IB/OB docReceivedDate/docSentDate will be 
 *                                     handled by stored procedure. 
 *                                     The creation/update of ProcessTrans now supporting concurrently
 *                                     create/update on the same record
 * Jul 29, 2008    Tam Wei Xiang       #69 Handle the potentially processed AuditTrailData.This can happen
 *                                         when the JMS msg redelivered mechanism kick in.
 *                                         By allowing handling of the redelivered msg,
 *                                         the TXMR event can survive during the Clustering
 *                                         fail over kick in.
 */
package com.gridnode.gtas.audit.tracking;

import java.util.Collection;
import java.util.List;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.model.BizDocument;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.model.DocumentTransaction;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.gtas.audit.common.model.ITrailInfo;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.audit.dao.CommonResourceDAO;
import com.gridnode.gtas.audit.dao.TraceEventInfoDAO;
import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.model.ProcessTransaction;
import com.gridnode.gtas.audit.model.TraceEventInfo;
import com.gridnode.gtas.audit.model.TraceEventInfoHeader;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.tracking.facade.ejb.IProcessTransManagerLocalHome;
import com.gridnode.gtas.audit.tracking.facade.ejb.IProcessTransManagerLocalObj;
import com.gridnode.gtas.audit.tracking.helpers.AbstractInfoHelper;
import com.gridnode.gtas.audit.tracking.helpers.DocInfoHelper;
import com.gridnode.gtas.audit.tracking.helpers.EventInfoHelper;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.ProcessInfoHelper;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class will handle the creation and update of the concrete-class of AbstractAuditTrailEntity
 * record. Concrete sub-class of AbstractInfoHelper will provide necessary methods for this
 * class to perform the operations.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN
 */
public class TrailInfoHandler
{
  private static final TrailInfoHandler _handler = new TrailInfoHandler();
  private static JndiFinder _jndiFinder = null;
  private static final String CLASS_NAME = "TrailInfoHandler";
  private Logger _logger = null;
  
  static
  {
    try
    {
      _jndiFinder = new JndiFinder(null);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      throw new ExceptionInInitializerError("Error in initialise JndiFinder "+ex.getMessage());
    }
  }
  
  private TrailInfoHandler()
  {
    _logger = getLogger();
  }
  
  public static synchronized TrailInfoHandler getInstance()
  {
    return _handler;
  }
  
  /**
   * Process the AuditTrail info and persist in DB.
   * @param trailData the info which emitted from GT component need to be tracked by the OTC
   * @throws AuditTrailTrackingException throw if we have problem in processing the trailData.
   */
  public synchronized void processTrailInfo(AuditTrailData trailData)  throws AuditTrailTrackingException
  {
    String methodName = "processTrailInfo";
    _logger.logMessage(methodName, null, "Start processing TrailInfo");
    
    try
    {
      ITrailInfo trailInfo = trailData.getTrailInfo();
      if(trailInfo instanceof DocInfo)
      {
        importDocInfo(trailData);
      }
      else if(trailInfo instanceof EventInfo)
      {
        importEventInfo(trailData);
      }
      else if(trailInfo instanceof ProcessInfo)
      {
        importProcessInfo(trailData);
      }
      else
      {
        throw new IllegalArgumentException("["+CLASS_NAME+".processTrailInfo]The AuditTrailData's ITrailInfo "+trailInfo+" is not supported !");
      } 
    }
    catch(AuditTrailTrackingException ex)
    {
      throw ex;
    }
    _logger.logMessage(methodName, null, "End Process TrailInfo");
  }
  
  /**
   * #69 29072008 TWX Handle the AuditTrailData that maybe processed before.
   * @param trailData
   * @throws AuditTrailTrackingException
   */
  public void processRedeliveredTrailInfo(AuditTrailData trailData) throws AuditTrailTrackingException
  {
    String methodName = "processRedeliveredTrailInfo";
    try
    {
      ITrailInfo trailInfo = trailData.getTrailInfo();
      _logger.logMessage(methodName, null, "Start processing redelivered TrailInfo "+trailInfo);
      
      if(trailInfo instanceof DocInfo)
      {
        if(! isDocInfoProcessed( (DocInfo)trailInfo))
        {
          importDocInfo(trailData);
        }
        else
        {
          _logger.logMessage(methodName, null, "Processed Doc Info "+trailInfo+" detected. Ignored");
        }
      }
      else if(trailInfo instanceof EventInfo)
      {
        if(! isEventInfoProcessed((EventInfo) trailInfo))
        {
          importEventInfo(trailData);
        }
        else
        {
          _logger.logMessage(methodName, null, "Processed EventInfo "+ trailInfo+" detected. Ignored");
        }
      }
      else if(trailInfo instanceof ProcessInfo)
      {
        importProcessInfo(trailData);
      }
      else
      {
        throw new IllegalArgumentException("["+CLASS_NAME+".processTrailInfo]The AuditTrailData's ITrailInfo "+trailInfo+" is not supported !");
      }
    }
    catch(AuditTrailTrackingException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      
    }
    _logger.logMessage(methodName, null, "End Process TrailInfo");
  }
  
  /**
   * Import the Doc info that is emited from the GT into OTC. 
   * @param trailData The trailData consist of the EventInfo and the BusinessDocument(if any)
   */
  private void importDocInfo(AuditTrailData trailData) throws AuditTrailTrackingException
  {
    String methodName = "importDocInfo";
    _logger.logEntry(methodName, null);
    
    DocInfoHelper infoHelper = new DocInfoHelper();
    DocInfo docInfo = (DocInfo)trailData.getTrailInfo();
    
    _logger.logMessage(methodName, null, "import doc Info "+docInfo);
    
    BusinessDocument[] businessDocs = trailData.getBizDocuments();
    String groupName = getGroupName(docInfo.getBeID(), infoHelper);
      
    //update or create the ProcessTransaction
    handleProcessTransaction(docInfo, groupName);
      
    //updateIBDocReceivedDate(docInfo);
      
//    update the OB doc sent date (for the case where we process the event Documetn Delivered earlier than OB Document)
    //updateOBDocSentDate(docInfo);
      
    String bizDocUID = saveBizDocument(businessDocs, infoHelper, groupName);
    String docTransUID = saveDocInfo(docInfo, groupName, bizDocUID);
      
    //update the Channel Event
    //handleChannelEvent(docInfo, groupName);
    
    //update common resource
    //handleCommonResource(docInfo, groupName);
    
    //update reprocess event linkage
    updateProcessLinkage(docInfo);
    
    docInfo = null;
    businessDocs = null;
    
    _logger.logExit(methodName, null);
  }
  
  /**
   * Import the process info that is emited from the GT into OTC. 
   * @param trailData The trailData consist of the EventInfo and the BusinessDocument(if any)
   */
  private void importProcessInfo(AuditTrailData trailData) throws AuditTrailTrackingException
  {
    String methodName = "importProcessInfo";
    
    try
    {
      _logger.logEntry(methodName, null);
      
      ProcessInfo proInfo = (ProcessInfo)trailData.getTrailInfo();
      
      _logger.logMessage(methodName, null, "Start importing process info ...."+proInfo);
      
      ProcessInfoHelper infoHelper = new ProcessInfoHelper();
    
        ProcessTransaction processTrans = null;
        
        
        obtainProcessLock();
        processTrans = infoHelper.getProcessTransaction(proInfo.getProcessInstanceUID());  
        
        
        if(processTrans == null)
        {
          processTrans = infoHelper.createProcessTransaction(proInfo);
          
          try
          {
            getProcessTransMgr().persistProcessTrans(processTrans);
          }
          catch(Exception ex)
          {
            
            if(infoHelper.isProcessTransConstraintViolated(ex))
            {
              _logger.logMessage(methodName, null,"importProcessInfo: Duplicate process trans detected : nested exception msg "+ex.getMessage());
              if(! getProcessTransMgr().updateProcessTrans(proInfo))
              {
                throw ex;
              }
            }
            else
            {
              throw ex;
            }
          } 
        }
        else
        {
          _logger.logMessage(methodName, null, "Updating Process Trans .....");
          //update the process trans
          getProcessTransMgr().updateProcessTrans(proInfo);
        }
        
        
        proInfo = null;
        processTrans = null;
        
        _logger.logExit(methodName, null);
    }
    catch(AuditTrailTrackingException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Error in importing process info", ex);
    }
  }
  
  /**
   * 11 May 2007 Obtain a global lock to avoid the concurrent creation of Process Transaction record
   */
  private void obtainProcessLock()
  {
    CommonResourceDAO dao = new CommonResourceDAO();
    List list =  dao.lockResource(IISATConstant.TYPE_LOCK, IISATConstant.PROCESS_LOCK); 
    if(list != null && list.size() > 0)
    {
      CommonResource cr = (CommonResource)list.iterator().next();
    }
    else
    {
      throw new NullPointerException("Can't find process transaction lock in table isat_resource with type: "+IISATConstant.TYPE_LOCK+" and code: "+IISATConstant.PROCESS_LOCK);
    }
  }
  
  /**
   * Import the event info that is emited from the GT into OTC. 
   * @param trailData The trailData consist of the EventInfo and the BusinessDocument(if any)
   */
  private void importEventInfo(AuditTrailData trailData) throws AuditTrailTrackingException
  {
    String methodName= "importEventInfo";
    
    _logger.logEntry(methodName, null);
    
    EventInfo eventInfo = null;
    try
    {
      EventInfoHelper helper = new EventInfoHelper();
      eventInfo = (EventInfo)trailData.getTrailInfo();
      
      _logger.logMessage(methodName, null, "Processing event info "+eventInfo);
      
      BusinessDocument[] businessDoc = trailData.getBizDocuments();
      
      String groupName = getGroupName(eventInfo.getBeID(), helper); //we may not get the groupName since certain event will not contain beID
      
      //save the business doc if any
      String bizDocumentUID = saveBizDocument(businessDoc, helper, groupName);
      
      //create and persist the TraceEventInfo
      saveEventInfo(eventInfo, groupName, bizDocumentUID);
      
      //create or update TraceEventHeader
      //handleTraceEventHeader(eventInfo, groupName);
      
      //update the doc delivered date
      //updateOBDocSentDate(eventInfo);
      
      //updateIBDocReceivedDate(eventInfo);
      
      
      businessDoc = null;
      eventInfo = null;
      
      _logger.logExit(methodName, null);
    }
    catch(AuditTrailTrackingException ex)
    {
      throw ex;
    }
  }
  
  private String getGroupName(String beID, AbstractInfoHelper helper) throws AuditTrailTrackingException
  {
    if(beID == null || "".equals(beID))
    {
      return "";
    }
    else
    {
      return helper.getGroupName(beID);
    }
  }
  
  private String saveBizDocument(BusinessDocument[] businessDocs, AbstractInfoHelper helper, String groupName) throws AuditTrailTrackingException
  {
    BizDocument bizDoc = helper.createBizDocument(businessDocs, groupName);
    if(bizDoc != null)
    {
        _logger.logMessage("saveBizDocument", null, "["+CLASS_NAME+".saveBizDocument] BizDocument created. Persisting bizDocument .... ");
        String bizDocUID = helper.persistAuditTrailEntity(bizDoc);
        bizDoc = null;
        return bizDocUID;
    }
    return null;
  }
  
  private String saveEventInfo(EventInfo eventInfo, String groupName, String bizDocumentUID)
    throws AuditTrailTrackingException
  {
    EventInfoHelper eventHelper = new EventInfoHelper();
    TraceEventInfo info = eventHelper.createTraceEventInfo(eventInfo, groupName, bizDocumentUID);
    String uid = eventHelper.persistAuditTrailEntity(info);
    info = null;
    _logger.logMessage("saveEventInfo", null, "Set event info to null ");
    return uid;
  }
  
  private String saveDocInfo(DocInfo docInfo, String groupName, String bizDocumentUID) throws AuditTrailTrackingException
  { 
    DocInfoHelper infoHelper = new DocInfoHelper();
    DocumentTransaction docTrans = infoHelper.createDocTransaction(docInfo, groupName, bizDocumentUID);
    String uid = infoHelper.persistAuditTrailEntity(docTrans);
    docTrans = null;
    
    return uid;
    }
  
  /**
   * If the eventInfo correspond TraceEventHeader is existed, we MAY perform an update. If not,
   * we will create a new TraceEventHeader and persist into DB;
   * It will be handled in stored procedure
   * @param eventInfo
   */
  private void handleTraceEventHeader(EventInfo eventInfo, String groupName) throws AuditTrailTrackingException
  {
    //we maybe process two TraceEventInfo which related to the
    //* same business doc at the same time. To avoid the concurrent of handling the 
    //creation or update the TraceEventInfoHeader
    String methodName = "handleTraceEventHeader";
    Object[] param = new Object[]{eventInfo, groupName};
    
    System.out.println("Handling TraceEventHeader ");
    try
    {
      EventInfoHelper helper = new EventInfoHelper();
        String tracingID = eventInfo.getTracingID();
        
        TraceEventInfoHeader eventHeader = null;
        try
        {
          eventHeader = helper.getTraceEventInfoHeader(tracingID);
        }
        catch(Exception ex)
        {
          throw new AuditTrailTrackingException("["+CLASS_NAME+".handleTraceEventHeader] Error in retrieving the TraceEventInfoHeader !", ex);
        }
        _logger.debugMessage(methodName, param, "Retrieve EventHeader is "+eventHeader);
        System.out.println("Retrieve EventHeader is "+eventHeader);
        
        if(eventHeader == null)
        {
          eventHeader = helper.createTraceEventInfoHeader(eventInfo, groupName);
          helper.persistAuditTrailEntity(eventHeader);
          _logger.debugMessage(methodName, null, "Persisted TraceEventInfoHeader "+eventHeader);
          System.out.println("Persisted TraceEventInfoHeader "+eventHeader);
        }
        else
        {
          _logger.debugMessage(methodName, null, "Retrieve TraceEventHeader is "+eventHeader.toString()+". Start Perform update !");
          _logger.debugMessage(methodName, null, "Last eventTime in Milli "+eventHeader.getLastEventOccurredTime().getTime()+" current EventTime in milli "+eventInfo.getEventOccurredTime().getTime());
          long lastEventTimeInMilliSecond = eventHeader.getLastEventOccurredTime().getTime();
          if(lastEventTimeInMilliSecond < eventInfo.getEventOccurredTime().getTime())
          {
            helper.updateTraceEventInfoHeader(eventInfo, eventHeader, groupName); 
            _logger.debugMessage(null, null, "Updated EventHeader is "+eventHeader);
          }
          else if(IAuditTrailConstant.STATUS_FAIL.equals(eventInfo.getStatus())) //require update the error count
          {
            eventHeader.setErrorCount(eventHeader.getErrorCount()+ helper.getErrorCount(eventInfo.getStatus()));
            helper.updateAuditTrailEntity(eventHeader);
            _logger.debugMessage(null, null, "After update error count only. Updated EventHeader is "+eventHeader);
          }
        }
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Error in handling the traceEventHeader", ex);
    }
  }
  
  /**
   * Some of the process info eg the PIP name, PIP version is relying on the DocInfo to be provided.
   * @param docInfo
   * @param groupName
   * @throws AuditTrailTrackingException
   */
  private void handleProcessTransaction(DocInfo docInfo, String groupName) throws AuditTrailTrackingException
  {
    String methodName = "handleProcessTransaction";
    
    
    try
    {
      DocInfoHelper infoHelper = new DocInfoHelper();
      ProcessInfoHelper processHelper = new ProcessInfoHelper();
    
      ProcessSummary summary = infoHelper.getProcessSummary(docInfo);
    
      _logger.logMessage(methodName, null, "Making use of DocInfo to handle ProcessTrans. Doc Info is "+docInfo.getDocumentDirection()+" "+docInfo.getMessageID()+docInfo);

        //Perform update some Process related information given the docInfo
      
        if(summary ==null || summary.getProcessInstanceUID() == null) 
        {
          throw new AuditTrailTrackingException("DocInfo ["+docInfo+"] has no ProcessSummary or the ProcessSummary has no processInstanceUID");
        }
        else if(! infoHelper.isSignalMsg(docInfo)) //ACK doc will not have doc number
        {
          int docTransCount = infoHelper.getActionDocTransCountByDirection(docInfo.getDocumentDirection(), docInfo.getTracingID(), summary.getProcessInstanceUID());
          if(docTransCount > 0) //we can use the ! docInfo.isDuplicate and !docInfo.isRetry to determine whether to update the ProcessTrans,
          {                      //however we can't make sure the original doc can be created successfully, so we may rely on those
                              //isDuplicate or isRetr doc to update the ProcessTrans.
            _logger.logMessage(methodName, null, "Action document tie link to the Process with UID "+summary.getProcessInstanceUID()+" already exist in DB, no update of ProcessTrans is required");
            return;
          }
          _logger.logMessage(methodName, null, "Start handle the ProcessTrans given DocInfo "+docInfo);
        
          ProcessTransaction proTrans = null;
          
          obtainProcessLock();
          proTrans = processHelper.getProcessTransaction(summary.getProcessInstanceUID());
        
        
        if(proTrans != null)
        {
          _logger.logMessage("handleProcessTransaction", null, "Updating Process Trans "+proTrans);
          getProcessTransMgr().updateProcessTrans(summary, docInfo, groupName);
        }
        else
        {
            _logger.logMessage("handleProcessTransaction", null, "Creating Process Trans");
            proTrans = processHelper.createProcessTransaction(summary, docInfo, groupName);
          
            try
            {
              getProcessTransMgr().persistProcessTrans(proTrans);
         
            }
            catch(Exception ex)
            {
              
              if(processHelper.isProcessTransConstraintViolated(ex))
              {
                _logger.logMessage("handleProcessTransaction", null, "use docInfo: Create ProcessDoc using processSummary. Duplicate process trans "+proTrans+" detected !!!");
                if (! getProcessTransMgr().updateProcessTrans(summary, docInfo, groupName) )
                {
                  throw new AuditTrailTrackingException("Error in updating process transaction using doc info "+docInfo, ex);
                }
              }
              else
              {
                throw new AuditTrailTrackingException("Error in creating process transaction using doc info "+docInfo, ex);
              }
            }
         }
       }
       else
       {
         _logger.logMessage(methodName, null, "The docInfo "+docInfo+" is signal msg  ! No update/create on the process trans");
       }
    }
    catch(AuditTrailTrackingException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Error in updating/creating process transaction record using doc info "+docInfo, ex);
    }
  }
  
  private Collection<DocumentTransaction> getTransactionDocs(String tracingID, String direction) throws AuditTrailTrackingException
  {
    String methodName = "getTransactionDocs";
    Collection<DocumentTransaction> docTransList = null;
    DocInfoHelper docHelper = new DocInfoHelper();
    
      try
      {
        docTransList = docHelper.getDocumentTransactions(tracingID, direction);
      }
      catch(Exception ex)
      {
        throw new AuditTrailTrackingException("Error in retrieving the DocumentTransaction", ex);
      }
      
      return docTransList;
  }
  
  private Collection<DocumentTransaction> getTransactionDocsByTraceIDAndMsgID(String tracingID, String messageID)throws AuditTrailTrackingException
  {
    String methodName = "getTranDocsByTraceIDAndMsgID";
    Collection<DocumentTransaction> docTransList = null;
    DocInfoHelper docHelper = new DocInfoHelper();
    
      try
      {
        docTransList = docHelper.getDocumentTransByTraceIDAndMsgID(tracingID, messageID);
      }
      catch(Exception ex)
      {
        throw new AuditTrailTrackingException("Error in retrieving the DocumentTransaction", ex);
      }
      
      return docTransList;
  }
  
  /**
   * Return the eventType based on whether the docType is correspond to a signal msg
   * or action msg.
   * @param docType
   * @return 
   */
  private String getEventType(String docType) throws AuditTrailTrackingException
  {
    if(docType == null || "".equals(docType))
    {
      throw new AuditTrailTrackingException("Null/Empty docType");
    }
    
    return (IAuditTrailConstant.SIGNAL_MESSAGE_ACK.equals(docType)|| IAuditTrailConstant.SIGNAL_MESSAGE_EXP.equals(docType)) ? 
                                                      IAuditTrailConstant.EVENT_TYPE_SIGNAL: IAuditTrailConstant.EVENT_TYPE_TRANS;
                                                                                                                              
  }
  
  /**
   * Update the process instance uid to the reprocess event. This handle the linkage from
   * the reprocess event to the new process transaction. We only update if the DocInfo direction
   * is OB.
   * @param docInfo The OB direction docInfo. This OB doc is newly generated by GT after user has reprocessing the OB doc.
   */
  private void updateProcessLinkage(DocInfo docInfo) throws AuditTrailTrackingException
  {
    if(! IAuditTrailConstant.DIRECTION_OB.equals(docInfo.getDocumentDirection()) ||  docInfo.isRetry())
    {
      //By enforcing the doc info is not a retry doc, we ensure that the Resend doc (Part of RN process) which origin from the root Process instance
      //will not update the reprocess event that is newly initiated by user.
      return;
    }
    String methodName = "updateProcessLinkage";
    
    _logger.debugMessage(methodName, null, "Updating process linkage into the reprocess doc event .... with docInfo "+docInfo);
    
    EventInfoHelper infoHelper = new EventInfoHelper();
    String tracingID = docInfo.getTracingID();
    TraceEventInfo eventInfo = infoHelper.getTraceEventInfoOrderByDescOccuredTime(tracingID, IAuditTrailConstant.REPROCESS_DOC);
    _logger.debugMessage(methodName, null, "Retrieve Reprocess Doc event is "+eventInfo);
    
    if(eventInfo != null)
    {
      ProcessSummary summary = docInfo.getProcessSummary();
      if(summary != null)
      {
        if(eventInfo.getReprocessLinkageUID() == null && IAuditTrailConstant.STATUS_SUCCESS.equals(eventInfo.getStatus()))
        {
          eventInfo.setReprocessLinkageUID(summary.getProcessInstanceUID());
          infoHelper.updateAuditTrailEntity(eventInfo);
          _logger.debugMessage(methodName, null, "Link to process instance uid "+summary.getProcessInstanceUID());
        }
        else
        {
          _logger.logMessage(methodName, null, "The given TraceEventInfo "+eventInfo+" already associated to Process with UID "+eventInfo.getReprocessLinkageUID()+" or the event info status is "+IAuditTrailConstant.STATUS_FAIL);
        }
      }
      else
      {
        throw new AuditTrailTrackingException("The ProcessSummary for DocInfo with msgID:"+docInfo.getMessageID()+" is null. Update linkage to the "+IAuditTrailConstant.REPROCESS_DOC+" failed");
      }
    }
  }
  
  /**
   * #69 TWX 29072008 Check if the docInfo be processed before.
   * @param docInfo
   * @return true if the docInfo has been processed before; false otherwise
   * @throws Exception
   */
  private boolean isDocInfoProcessed(DocInfo docInfo)throws AuditTrailTrackingException
  {
    Collection<DocumentTransaction> docTransList = getTransactionDocsByTraceIDAndMsgID(docInfo.getTracingID(), docInfo.getMessageID());
    if(docTransList != null && docTransList.size() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   * #69 TWX 29072008 Check if a particular EventInfo has been processed before.
   * @param eventInfo
   * @return true if the EventInfo has been processed before; false otherwise
   * @throws Exception
   */
  private boolean isEventInfoProcessed(EventInfo eventInfo) throws Exception
  {
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
    if(eventInfo.getMessageID() == null || eventInfo.getMessageID().trim().equals(""))
    {
      return eventDAO.getEventCountByEventNameTracingIDEventOccur(eventInfo.getTracingID(), eventInfo.getEventName(), eventInfo.getEventOccurredTime()) > 0;
    }
    else
    {
      return eventDAO.getEventCountByEventNameTracingIDMsgIDEventOccur(eventInfo.getTracingID(), eventInfo.getEventName(), eventInfo.getMessageID(), eventInfo.getEventOccurredTime()) > 0;
    }
  }
  
  private IProcessTransManagerLocalObj getProcessTransMgr() throws Exception
  {
    IProcessTransManagerLocalHome home = (IProcessTransManagerLocalHome)_jndiFinder.lookup(IProcessTransManagerLocalHome.class.getName(), IProcessTransManagerLocalHome.class);
    return home.create();
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
