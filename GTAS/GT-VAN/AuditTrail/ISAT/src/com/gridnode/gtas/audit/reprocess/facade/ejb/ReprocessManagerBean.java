/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReprocessValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 31, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.reprocess.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.dao.DocTransDAO;
import com.gridnode.gtas.audit.dao.ProcessTransactionDAO;
import com.gridnode.gtas.audit.dao.TraceEventInfoDAO;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;
import com.gridnode.gtas.audit.model.BizEntityGroupMapping;
import com.gridnode.gtas.audit.model.DocumentTransaction;
import com.gridnode.gtas.audit.model.ProcessTransaction;
import com.gridnode.gtas.audit.model.TraceEventInfo;
import com.gridnode.gtas.audit.model.TraceEventInfoHeader;
import com.gridnode.gtas.audit.reprocess.IReprocessServletConstant;
import com.gridnode.gtas.audit.reprocess.exception.ReprocessActivityException;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * Prior we send the request for reprocessing a particular doc, 
 * we will go through some checking, and determine whether user are allowed the 
 * reprocess action.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ReprocessManagerBean implements SessionBean
{
  private static final String CLASS_NAME = "ReprocessManagerBean";
  transient private SessionContext _ctxt = null;
  private Logger _logger = null;
  
  /**
   * Determine whether we can perform reprocessing the tracingID correspond doc.
   * @param tracingID
   * @return
   */
  public String[] isAllowedReprocessDoc(String tracingID) throws ReprocessActivityException
  { 
    String methodName = "isAllowedReprocessDoc";
    String[] param = new String[]{tracingID};
    
    _logger.logEntry(methodName, param);
    
    DocTransDAO docDAO = new DocTransDAO();
    Collection<DocumentTransaction> obDocTransList = getDocumentTransactionList(tracingID, IAuditTrailConstant.DIRECTION_OB, false);
    Collection<DocumentTransaction> ibDocTransList = getDocumentTransactionList(tracingID, IAuditTrailConstant.DIRECTION_IB, null);
    
    if(obDocTransList != null && obDocTransList.size() > 0) //as long as we are responding role, and contain ob trans, the reprocess is disallowed
    {
      boolean isInitiatingProcess = isInitiatingProcess(obDocTransList.iterator().next()); 
      if(! isInitiatingProcess) 
      {
        return new String[]{Boolean.FALSE.toString(), IReprocessServletConstant.STATUS_REPRPOCESS_DENIED_HAS_OB};
      }
      else
      {
        return new String[]{Boolean.TRUE.toString()};
      }
    }
    
    if(ibDocTransList == null || ibDocTransList.size() == 0)
    {
      //check whether contain the DocumentReceive, Unpack payload only. If so, check again the status.
      if(isContainedIBPreInsertIntoWFEventOnly(tracingID))
      {
        return new String[]{Boolean.FALSE.toString(), IReprocessServletConstant.STATUS_REPROCESS_DENIED_NO_IB};
      }
      else if(isContainedHTTPBCSendEventOnly(tracingID))
      {
        return new String[]{Boolean.FALSE.toString(), IReprocessServletConstant.STATUS_REPROCESS_DENIED_HAS_HTTP_BC_EVENT};
      }
    }
    
    _logger.logExit(methodName, param);
    return new String[]{Boolean.TRUE.toString()};

  }
  
  /**
   * Get the business entity ID
   * @param tracingID
   * @return
   * @throws ReprocessActivityException
   */
  public String getBeID(String tracingID) throws ReprocessActivityException
  {
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
    
    TraceEventInfoHeader eventHeader = null;
    try
    {
      eventHeader = eventDAO.getTraceEventInfoHeader(tracingID);
    }
    catch(Exception ex)
    {
      throw new ReprocessActivityException("["+CLASS_NAME+".getBeID] Error in getting TraceEventHeader entity given tracingID: "+tracingID);
    }
    String groupName = eventHeader.getGroupName() == null ? "" : eventHeader.getGroupName();
    
    String queryName = BizEntityGroupMapping.class.getName()+".getGroupMappingByGroupName";
    String[] paramNames = new String[]{"groupName"};
    String[] paramValue = new String[]{groupName};
    BizEntityGroupMapping mapping = (BizEntityGroupMapping)eventDAO.queryOne(queryName, paramNames, paramValue);
    
    if(mapping == null)
    {
      return null; //We won't have group name for event Doc Received, Unpack Payload, Process Inject
    }
    
    return mapping.getBeID();
  }
  
  /**
   * Retrieve the approprivate msgID given the tracingID. The msgID can be retrieved in following scenario
   * i) import doc correspond event list ii) IB doc iii) OB signal correspond event list 
   * @param tracingID
   * @return the msgID
   */
  public String getTracingIDCorrespondFirstInsertMsgID(String tracingID) throws ReprocessActivityException
  {
    String methodName = "getTracingIDCorrespondFirstInsertMsgID";
    
    _logger.logMessage(methodName, null, "Entering getTracingIDCorrespondFirstInsertMsgID");
    Collection<TraceEventInfo> eventInfoList = getTraceEventInfoOrderByOccuredTime(tracingID);
    if(eventInfoList == null || eventInfoList.size() == 0)
    {
      //In certain case i) only have event DocumentReceived or Unpack Payload, or Process Injection or all of 3.
      //               ii) Contain HTTP BC send event. Document Received From Backend, Document Delivered To Gateway
      //we will not have the msg ID.
      return "";
      
    }
    
    Iterator i = eventInfoList.iterator();
    
    TraceEventInfo eventInfo = eventInfoList.iterator().next();
    
    /*
    String eventName = eventInfo.getEventName();
    
    if(IAuditTrailConstant.DOCUMENT_IMPORT.equals(eventName)) //Import doc
    {
      return eventInfo.getMessageID();
    }
    else if(IAuditTrailConstant.CHANNEL_CONNECTIVITY.equals(eventName)) //OB signal
    {
      return eventInfo.getMessageID();
    }
    else if(IAuditTrailConstant.DOCUMENT_RECEIVED.equals(eventName)) //IB doc
    {
      DocumentTransaction docTrans = getDocumentTransByDirection(tracingID, IAuditTrailConstant.DIRECTION_IB, false);
      if(docTrans == null)
      {
        return ""; //If user fail at mapping rule, at that moment, no IB doc be created yet
      }
      else
      {
        return docTrans.getMessageID();
      }
    }
    else
    {
      //throw new ReprocessActivityException("["+CLASS_NAME+".getTracingIDCorrespondFirstInsertMsgID] The retrieval of msg ID from event with event name :"+eventName+" currently is not supported !");
      return "";
    }*/
    _logger.logMessage(methodName, null, "Event Info is "+eventInfo+" msgID is "+eventInfo.getMessageID());
    return eventInfo.getMessageID();
  }
  
  private Collection<TraceEventInfo> getTraceEventInfoOrderByOccuredTime(String tracingID)
  {
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO(false);
    return eventDAO.getTraceEventInfoOrderByEventOccurTime(tracingID);
  }
  
  /**
   * Determine whether the docTrans correspond ProcessTransaction is an initiating process.
   * @param docTrans
   * @return
   */
  private boolean isInitiatingProcess(DocumentTransaction docTrans) throws ReprocessActivityException
  {
    ProcessTransaction processTrans = getProcessTransaction(docTrans.getProcessInstanceUID());
    if(processTrans == null)
    {
      throw new ReprocessActivityException("["+CLASS_NAME+".isInitiatingProcess] No ProcessTransaction can be found given processUID "+docTrans.getProcessInstanceUID());
    }
    else
    {
      return processTrans.isInitiator();
    }
  }
  
  private ProcessTransaction getProcessTransaction(Long processUID) throws ReprocessActivityException
  {
    try
    {
      ProcessTransactionDAO processDAO = new ProcessTransactionDAO();
      return processDAO.retrieveProTransactionByProcessInstanceUID(processUID);
    }
    catch(Exception ex)
    {
      throw new ReprocessActivityException("["+CLASS_NAME+".getProcessTransaction] Error in retrieving the entity ProcessTransaction with processUID: "+processUID, ex);
    }
  }
  
  /**
   * Get a collection of DocumentTransaction entity
   * @param tracingID
   * @param direction the direction of the doc (IB or OB)
   * @param isSignal indicate whether to return the signal doc or transaction doc
   * @return
   * @throws ReprocessActivityException
   */
  private Collection<DocumentTransaction> getDocumentTransactionList(String tracingID, String direction, Boolean isSignal) throws ReprocessActivityException
  {
    try
    {
      DocTransDAO dao = new DocTransDAO();
      if(isSignal != null)
      {
        return dao.getDocTransByActionType(direction, tracingID, isSignal);
      }
      else
      {
        return dao.retrieveDocumentTrans(tracingID, direction);
      }
    }
    catch(AuditTrailDBServiceException ex)
    {
      throw new ReprocessActivityException("Error in retrieving document transaction given criteria[tracingID: "+tracingID+" direction: "+direction+" isSignal: "+isSignal+"]", ex);
    }
  }
  
  /**
   * Check whether the tracingID correspond event info only contain the DocumentReceived or Unpack Payload or Process Injection or all. It there is,
   * and one of their status is fail, then return true.
   * @param tracingID
   * @return
   * @throws ReprocessActivityException
   */
  private boolean isContainedIBPreInsertIntoWFEventOnly(String tracingID) throws ReprocessActivityException
  {
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
    Collection<TraceEventInfo> eventInfoList = eventDAO.getTraceEventInfoExcludeEventName(tracingID, IAuditTrailConstant.REPROCESS_DOC);
    if(eventInfoList == null || eventInfoList.size() == 0)
    {
      throw new ReprocessActivityException("["+CLASS_NAME+".isContainedFailedChannelEventOnly] No TraceEventInfo entity can be found given tracingID: "+tracingID);
    }
    else
    {
      if(eventInfoList.size() > 3) //naive way of checking ?
      {
        return false;
      }
      else
      {
        //For now, the channel event include DocumentReceived, Unpack Payload, Process Injection.any of one of their status is fail are not allowed to reprocess.
        Iterator<TraceEventInfo> ite = eventInfoList.iterator();
        while(ite.hasNext())
        {
          TraceEventInfo eventInfo = ite.next();
          _logger.logMessage("isContainedIBPreInsertIntoWFEventOnly", null, "event info is "+eventInfo);
          
          if(IAuditTrailConstant.DOCUMENT_RECEIVED.equals(eventInfo.getEventName()) || IAuditTrailConstant.UNPACK_PAYLOAD.equals(eventInfo.getEventName())
              || IAuditTrailConstant.PROCESS_INJECTION.equals(eventInfo.getEventName()))
          {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Check whether contain the HTTP BC send event from backend eg Documetn Received from backend, Document Delivered to Gateway 
   * @param tracingID
   * @return
   * @throws ReprocessActivityException
   */
  private boolean isContainedHTTPBCSendEventOnly(String tracingID) throws ReprocessActivityException
  {
    String methodName = "isContainedHTTPBCEventOnly";
    _logger.logEntry(methodName, null);
    
    TraceEventInfoDAO eventDAO = new TraceEventInfoDAO();
    Collection<String> eventInfoNameList = eventDAO.getDistintTraceEventInfoName(tracingID, IAuditTrailConstant.REPROCESS_DOC);
    if(eventInfoNameList == null || eventInfoNameList.size() == 0)
    {
      throw new ReprocessActivityException("["+CLASS_NAME+".isContainedHTTPBSEventOnly] No TraceEventInfo entity can be found given tracingID: "+tracingID);
    }
    else
    {
      if(eventInfoNameList.size() > 2)
      {
        
        _logger.logMessage(methodName, null, "contain more event other than HTTP BC event (Document Received from backend, Document Delivered to gateway) only");
        
        Iterator<String> ite = eventInfoNameList.iterator();
        while(ite.hasNext())
        {
          _logger.logMessage(methodName, null, "size > 2 Retrieve event name is "+ite.next());
        }
        return false;
      }
      else
      {
        Iterator<String> ite = eventInfoNameList.iterator();
        while(ite.hasNext())
        {
          String eventName = ite.next();
          _logger.logMessage(methodName, null, "Retrieve event name is "+eventName);
          if(IReprocessServletConstant.HTTPBC_EVENT_DOC_RECEIVED_FROM_BACKEND.equals(eventName) || 
              IReprocessServletConstant.HTTPBC_EVENT_DOC_DELIVERED_TO_GW.equals(eventName))
          {
            _logger.logMessage(methodName, null, "Contain HTTP BC event, reprocess is denied");
            return true;
          }
        }
        return false;
      }
    }
  }
  
  /**
   * Get the response URL that we will forward the status of the reprocessing to the UI.
   * @return the URL where we sent the reprocessing status to.
   */
  public String getResponseURL()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String responseURL = configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.RESPONSE_URL, null);
    return responseURL;
  }
  
  public void ejbCreate() throws CreateException
  {
    _logger = getLogger();
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
    
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
    
  }

  public void ejbRemove() throws EJBException, RemoteException
  {

  }

  public void setSessionContext(SessionContext ctxt) throws EJBException, RemoteException
  {
    _ctxt = ctxt;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
