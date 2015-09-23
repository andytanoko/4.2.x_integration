/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    MAHESH              Created
 * Dec 13 2005    Tam Wei Xiang       To resolve the pass by value issue, 
 *                                    as the changes we make on the obj will not be reflected 
 *                                    in the caller class.
 *                                    Modified method processRequestDocument,
 *                                                    performRequestingActivity,
 *                                                    processResponseDocument,
 *                                                    performRespondingActivity,
 *                                                    receivedSignal, checkAndComplete
 *                                                    abort
 * Jan 09 2005   Neo Sok Lay          Synchronize the methods that update entity.
 *                                    Re-fetch the entity before update. 
 * Jun 14 2006   Tam Wei Xiang        Modified method checkTimeTo(String, String) :
 *                                      To trigger 0A1 if the GWFRtProcessDoc's exception
 *                                      type is EXCEPTION_TIMETO_PERFORM    
 * Nov 16 2006   Tam Wei Xiang        To notify the creation of the RTProcess to OTC.                                                                                                                        
 * Jan 30 2007   Neo Sok Lay          Send message to generic destination.
 * May 30 2007    Neo Sok Lay         GNDB00028368: Use concurrent locking instead of method synchronization. 
 * Jul 15 2008   Tam Wei Xiang        #69: Handle the concurrency request cache if we have redelivered
 *                                         JMS msg. Modified addConcurrentRequest(...), processConcurrentRequests
 * Sep 07 2011   Tam Wei Xiang        #2594: Allow the invoker to manage the Reentrant Lock themselves by defining
 *                                           method processRequestDocumentNoLock().
 */

package com.gridnode.pdip.app.workflow.impl.bpss.helpers;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import javax.ejb.FinderException;

import com.gridnode.pdip.app.workflow.adaptors.AppAdaptor;
import com.gridnode.pdip.app.workflow.engine.GWFFactory;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotifyHandler;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtActivity;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerObj;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.UtilEntity;
import com.gridnode.pdip.framework.util.UtilString;

public class BpssBusinessTransHelper
{
  static Hashtable concurrentReqHt = new Hashtable();
  private static final String JMS_MSG_ID = "jmsMsgID";
  
  //NSL20070530
  private static final ReentrantLock _requestRoleLock = new ReentrantLock(true);
  private static final ReentrantLock _respondRoleLock = new ReentrantLock(true);

  public static void performRequestingActivity(
    String documentType,
    Object documentObject,
    GWFRtProcessDoc rtProcessDoc)
    throws WorkflowException
  {
    Logger.debug(
      "[BpssBusinessTransHelper.performRequestingActivity] documentType="
        + documentType
        + ",documentObject="
        + documentObject);
    //this will be called by initiating role
    
    acquireRequestRoleLock();
    try
    {
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      //get the runtime for business transaction
      GWFRtProcess rtProcess =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      String defCacheKey =
        (String) dataManager.getContextData(
          rtProcess.getContextUId(),
          new ContextKey("workflow.control.defCacheKey"));
      BpssDefinitionCache defCache =
        BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

      //put the document in context
      dataManager.setContextData(
        rtProcess.getContextUId(),
        new ContextKey(documentType),
        documentObject);

      //validate document
      BpssBusinessDocument businessDoc =
        defCache.getBusinessDocumentFromDocumentType(documentType);
      boolean isValid =
        validateDocument(
          rtProcessDoc,
          rtProcessDoc.getDocumentId(),
          documentType,
          documentObject,
          rtProcessDoc.getPartnerKey(),
          businessDoc);

      if (!isValid)
      {
        return;
      }

      //NSL20060109 Refetch entity before update
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.getEntityByKey((Long)rtProcessDoc.getKey(), GWFRtProcessDoc.ENTITY_NAME, true);
      
      if (rtProcessDoc.getDocumentType() == null
        || !rtProcessDoc.getDocumentType().equals(documentType))
      {
        rtProcessDoc.setDocumentType(documentType);
      }

      rtProcessDoc.setStatus(new Integer(GWFRtProcess.OPEN_RUNNING));
      
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
      
      //TWX 16112006 notify the creation of the Process (note: we can't do the notification in GWFProcessManagerBean.createProcess
      //             since at the time we do the notification, the rtprocess corresponsd rtprocessdoc is not yet be created)
      ProcessTransactionNotifyHandler.triggerProcessTransaction(rtProcessDoc);
      
      Logger.debug(
        "[BpssBusinessTransHelper.performRequestingActivity] After Updating Status "
          + rtProcessDoc);

      //add timers to check timeout
      BpssBusinessTrans bpssBusinessTrans =
        (BpssBusinessTrans) defCache.getDefinationEntity(
          rtProcess.getProcessUId(),
          BpssBusinessTrans.ENTITY_NAME);
      BpssReqBusinessActivity reqActivity =
        (BpssReqBusinessActivity) defCache.getDefinationEntity(
          bpssBusinessTrans.getBpssReqBusinessActivityUId(),
          BpssReqBusinessActivity.ENTITY_NAME);

      long timeToAckReceipt =
        WorkflowUtil.getTimeInterval(reqActivity.getTimeToAckReceipt());
      long timeToAckAccept =
        WorkflowUtil.getTimeInterval(reqActivity.getTimeToAckAccept());

      HashMap contextData = new HashMap();

      if (timeToAckReceipt > 0)
      {
        //means it has to wait for Ack Receipt until the specified time
        contextData.put("workflow.control.hasAckReceipt", Boolean.TRUE);
        String senderKey =
          KeyConverter.getKey(
            (Long) rtProcessDoc.getKey(),
            rtProcessDoc.getEntityName(),
            IWorkflowConstants.BPSS_ENGINE);
        String receiverKey = IBpssConstants.CHECK_TIMETO_ACK_RECEIPT;
        String category = IWorkflowConstants.BPSS_ENGINE;

        WorkflowUtil.addRetryAlarm(
          senderKey,
          receiverKey,
          category,
          timeToAckReceipt);
      }

      if (timeToAckAccept > 0)
      {
        //means it has to wait for Ack Acceptance until the specified time
        contextData.put("workflow.control.hasAckAccept", Boolean.TRUE);
        String senderKey =
          KeyConverter.getKey(
            (Long) rtProcessDoc.getKey(),
            rtProcessDoc.getEntityName(),
            IWorkflowConstants.BPSS_ENGINE);
        String receiverKey = IBpssConstants.CHECK_TIMETO_ACK_ACCEPT;
        String category = IWorkflowConstants.BPSS_ENGINE;

        WorkflowUtil.addRetryAlarm(
          senderKey,
          receiverKey,
          category,
          timeToAckAccept);
      }

      if (contextData.size() > 0)
        dataManager.setContextData(rtProcess.getContextUId(), contextData);

      //need to send the request document
      sendRequestDocument(rtProcessDoc);

      checkAndComplete(rtProcessDoc, false);

    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.performRequestingActivity] ",
        th);
    }
    finally
    {
      releaseRequestRoleLock();
    }
  }

  public static void processResponseDocument(
    String documentType,
    Object documentObject,
    GWFRtProcessDoc rtProcessDoc,
    String senderKey)
    throws WorkflowException
  {
    Logger.debug(
      "[BpssBusinessTransHelper.processResponseDocument] documentType="
        + documentType
        + ",senderKey="
        + senderKey
        + ",documentObject="
        + documentObject);
    //this will be called by initiating role
    
    acquireRequestRoleLock();
    try
    {
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      GWFRtProcess rtProcess =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      String defCacheKey =
        (String) dataManager.getContextData(
          rtProcess.getContextUId(),
          new ContextKey("workflow.control.defCacheKey"));
      BpssDefinitionCache defCache =
        BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

      //put the document in context
      dataManager.setContextData(
        rtProcess.getContextUId(),
        new ContextKey(documentType),
        documentObject);

      //validate document
      BpssBusinessDocument businessDoc =
        defCache.getBusinessDocumentFromDocumentType(documentType);
      boolean isValid =
        validateDocument(
          rtProcessDoc,
          rtProcessDoc.getDocumentId(),
          documentType,
          documentObject,
          rtProcessDoc.getPartnerKey(),
          businessDoc);

      //notify  received doc
      notifyReceivedDoc(rtProcessDoc, documentType, documentObject, isValid);

      if (!isValid)
      {
        return;
      }
      
      //NSL20060109 Refetch entity before update
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.getEntityByKey((Long)rtProcessDoc.getKey(), GWFRtProcessDoc.ENTITY_NAME, true);
      
      int rtProcessDocStatus = rtProcessDoc.getStatus().intValue();

      if (rtProcessDocStatus == GWFRtProcess.OPEN_NOTRUNNING
        || rtProcessDocStatus == GWFRtProcess.OPEN_RUNNING
        || rtProcessDocStatus == GWFRtProcess.CLOSED_COMPLETED)
      {

        //Ack signals should be sent for above conditions

        BpssBusinessTrans bpssBusinessTrans =
          (BpssBusinessTrans) defCache.getDefinationEntity(
            rtProcess.getProcessUId(),
            BpssBusinessTrans.ENTITY_NAME);
        BpssResBusinessActivity resActivity =
          (BpssResBusinessActivity) defCache.getDefinationEntity(
            bpssBusinessTrans.getBpssResBusinessActivityUId(),
            BpssResBusinessActivity.ENTITY_NAME);

        //if responding activity has timeToAckReceipt then send the ack receipt signal to responder
        long timeToAckReceipt =
          WorkflowUtil.getTimeInterval(resActivity.getTimeToAckReceipt());

        if (timeToAckReceipt > 0)
          sendSignal(
            IBpssConstants.ACK_RECEIPT_SIGNAL,
            null,
            documentType,
            rtProcessDoc,
            rtProcess.getContextUId());

        if (!rtProcessDoc.getDocProcessedFlag().booleanValue())
        {
          String resTypes = rtProcessDoc.getResponseDocTypes();

          if (resTypes != null)
          {
            Map resTypeMap = UtilString.strToMap(resTypes);

            rtProcessDoc.setIsPositiveResponse(
              new Boolean((String) resTypeMap.get(documentType)));
          }
          //call xpdl to process responding document
          /* should not call on response document
           HashMap contextData=new HashMap();
           contextData.put(IBpssConstants.DOCUMENTID,rtProcessDoc.getDocumentId());
           contextData.put(IBpssConstants.DOCUMENT_TYPE,documentType);
           contextData.put(IBpssConstants.DOCUMENT_OBJECT,documentObject);
           contextData.put(IBpssConstants.PARTNER_KEY,rtProcessDoc.getPartnerKey());
           //callXpdlProcess(resActivity,contextData);
           */
          rtProcessDoc.setDocProcessedFlag(Boolean.TRUE);
          
          rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
          if (rtProcessDocStatus != GWFRtProcess.CLOSED_COMPLETED)
            checkAndComplete(rtProcessDoc, false);
        }

      }
      else
        sendSignal(
          rtProcessDoc.getExceptionSignalType(),
          rtProcessDoc.getReason(),
          documentType,
          rtProcessDoc,
          rtProcess.getContextUId());
    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.processResponseDocument] ",
        th);
    }
    finally
    {
      releaseRequestRoleLock();
    }
  }

  public static void performRespondingActivity(
    String documentType,
    Object documentObject,
    GWFRtProcessDoc rtProcessDoc)
    throws WorkflowException
  {
    Logger.debug(
      "[BpssBusinessTransHelper.performRespondingActivity] documentType="
        + documentType
        + ",documentObject="
        + documentObject);
    //this will be called by responding role
    
    acquireRespondRoleLock();
    try
    {
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      //means this is the response document which has to be sent to requester
      GWFRtProcess rtProcess =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      String defCacheKey =
        (String) dataManager.getContextData(
          rtProcess.getContextUId(),
          new ContextKey("workflow.control.defCacheKey"));
      BpssDefinitionCache defCache =
        BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

      //put the document in context
      dataManager.setContextData(
        rtProcess.getContextUId(),
        new ContextKey(documentType),
        documentObject);

      //validate document
      BpssBusinessDocument businessDoc =
        defCache.getBusinessDocumentFromDocumentType(documentType);
      boolean isValid =
        validateDocument(
          rtProcessDoc,
          rtProcessDoc.getDocumentId(),
          documentType,
          documentObject,
          rtProcessDoc.getPartnerKey(),
          businessDoc);

      if (!isValid)
      {
        return;
      }

      //NSL20060109 Refetch entity before update
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.getEntityByKey((Long)rtProcessDoc.getKey(), GWFRtProcessDoc.ENTITY_NAME, true);

      String resTypes = rtProcessDoc.getResponseDocTypes();

      if (resTypes != null)
      {
        Map resTypeMap = UtilString.strToMap(resTypes);

        rtProcessDoc.setIsPositiveResponse(
          new Boolean((String) resTypeMap.get(documentType)));
      }
      if (rtProcessDoc.getDocumentType() == null
        || !rtProcessDoc.getDocumentType().equals(documentType))
      {
        rtProcessDoc.setDocumentType(documentType);
      }
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);

      //add timers to check timeout
      BpssBusinessTrans bpssBusinessTrans =
        (BpssBusinessTrans) defCache.getDefinationEntity(
          rtProcess.getProcessUId(),
          BpssBusinessTrans.ENTITY_NAME);
      BpssResBusinessActivity resActivity =
        (BpssResBusinessActivity) defCache.getDefinationEntity(
          bpssBusinessTrans.getBpssResBusinessActivityUId(),
          BpssResBusinessActivity.ENTITY_NAME);

      HashMap contextData = new HashMap();
      long timeToAckReceipt =
        WorkflowUtil.getTimeInterval(resActivity.getTimeToAckReceipt());

      if (timeToAckReceipt > 0)
      {
        //means it has to wait for Ack Receipt until the specified time
        contextData.put("workflow.control.hasAckReceipt", Boolean.TRUE);
        String senderKey =
          KeyConverter.getKey(
            (Long) rtProcessDoc.getKey(),
            rtProcessDoc.getEntityName(),
            IWorkflowConstants.BPSS_ENGINE);
        String receiverKey = IBpssConstants.CHECK_TIMETO_ACK_RECEIPT;
        String category = IWorkflowConstants.BPSS_ENGINE;

        WorkflowUtil.addRetryAlarm(
          senderKey,
          receiverKey,
          category,
          timeToAckReceipt);
      }

      if (contextData.size() > 0)
        dataManager.setContextData(rtProcess.getContextUId(), contextData);

      sendResponseDocument(rtProcessDoc);

      checkAndComplete(rtProcessDoc, false);

    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.performRespondingActivity] ",
        th);
    }
    finally
    {
      releaseRespondRoleLock();
    }
  }

  //TWX 20110907 2594: The invoker of such method need to handle the Reentrant lock EXPLICITLY by invoking 
  //                   BpssBusinessTransHelper.acquireRespondRoleLock()/releaseRespondRoleLock().
  //                   If the invoker is not going to manage the lock, it should invoke 
  //                   BpssBusinessTransHelper.processRequestDocument() instead.
  public static void processRequestDocumentNoLock(String documentType, Object documentObject, GWFRtProcessDoc rtProcessDoc,
                                            String partnerKey) throws WorkflowException
  {
    Logger.debug("[BpssBusinessTransHelper.processRequestDocument] documentType="
                 + documentType
                 + ",partnerKey="
                 + partnerKey
                 + ",documentObject="
                 + documentObject);
    
    //this will be called by responding role
    
    try
    {
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      GWFRtProcess rtProcess =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      String defCacheKey =
        (String) dataManager.getContextData(
          rtProcess.getContextUId(),
          new ContextKey("workflow.control.defCacheKey"));
      BpssDefinitionCache defCache =
        BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

      String inPartnerKey =
        (String) dataManager.getContextData(
          rtProcess.getContextUId(),
          new ContextKey("workflow.control.partnerKey"));

      if (inPartnerKey != null)
      {
        if (!partnerKey.equals(inPartnerKey))
          throw new WorkflowException(
            "[BpssBusinessTransHelper.processRequestDocument] received document from invalid partner "
              + partnerKey
              + " but expecting from "
              + inPartnerKey);
      }
      //put the document in context
      dataManager.setContextData(
        rtProcess.getContextUId(),
        new ContextKey(documentType),
        documentObject);

      //validate document
      BpssBusinessDocument businessDoc =
        defCache.getBusinessDocumentFromDocumentType(documentType);
      boolean isValid =
        validateDocument(
          rtProcessDoc,
          rtProcessDoc.getDocumentId(),
          documentType,
          documentObject,
          rtProcessDoc.getPartnerKey(),
          businessDoc);

      //notify  received doc
      notifyReceivedDoc(rtProcessDoc, documentType, documentObject, isValid);

      if (!isValid)
      {
        return;
      }
      
      //NSL20060109 Refetch entity before update
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.getEntityByKey((Long)rtProcessDoc.getKey(), GWFRtProcessDoc.ENTITY_NAME, true);

      int rtProcessDocStatus = rtProcessDoc.getStatus().intValue();

      if (rtProcessDocStatus == GWFRtProcess.OPEN_NOTRUNNING
        || rtProcessDocStatus == GWFRtProcess.OPEN_RUNNING
        || rtProcessDocStatus == GWFRtProcess.CLOSED_COMPLETED)
      {

        //Ack signals should be sent for above conditions

        BpssBusinessTrans bpssBusinessTrans =
          (BpssBusinessTrans) defCache.getDefinationEntity(
            rtProcess.getProcessUId(),
            BpssBusinessTrans.ENTITY_NAME);
        BpssReqBusinessActivity reqActivity =
          (BpssReqBusinessActivity) defCache.getDefinationEntity(
            bpssBusinessTrans.getBpssReqBusinessActivityUId(),
            BpssReqBusinessActivity.ENTITY_NAME);
        BpssResBusinessActivity resActivity =
          (BpssResBusinessActivity) defCache.getDefinationEntity(
            bpssBusinessTrans.getBpssResBusinessActivityUId(),
            BpssResBusinessActivity.ENTITY_NAME);

        long timeToAckReceipt =
          WorkflowUtil.getTimeInterval(reqActivity.getTimeToAckReceipt());
        long timeToAckAccept =
          WorkflowUtil.getTimeInterval(reqActivity.getTimeToAckAccept());

        if (timeToAckReceipt > 0)
        {
          sendSignal(
            IBpssConstants.ACK_RECEIPT_SIGNAL,
            null,
            documentType,
            rtProcessDoc,
            rtProcess.getContextUId());
        }

        if (timeToAckAccept > 0)
        {
          sendSignal(
            IBpssConstants.ACK_ACCEPTANCE_SIGNAL,
            null,
            documentType,
            rtProcessDoc,
            rtProcess.getContextUId());
        }

        if (!rtProcessDoc.getDocProcessedFlag().booleanValue())
        {
          rtProcessDoc.setDocProcessedFlag(Boolean.TRUE);
          rtProcessDoc.setStatus(new Integer(GWFRtProcess.OPEN_RUNNING));
          
          Logger.log("BpssBusinessTransHelper updateEntity");
          //TWX: pass by value
          rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
          
          
          //TWX 14112006 notify the creation of the Process (note: we can't do the notification in GWFProcessManagerBean.createProcess
          //             since at the time we do the notification, the rtprocess corresponsd rtprocessdoc is not yet be created)
          ProcessTransactionNotifyHandler.triggerProcessTransaction(rtProcessDoc);
          
          //if there are no response document for this request dont call xpdl.
          if (rtProcessDoc.getResponseDocTypes() != null)
          {
            //call xpdl to process request document
            HashMap contextData = new HashMap();

            contextData.put(
              IBpssConstants.DOCUMENTID,
              rtProcessDoc.getDocumentId());
            contextData.put(IBpssConstants.DOCUMENT_TYPE, documentType);
            contextData.put(IBpssConstants.DOCUMENT_OBJECT, documentObject);
            contextData.put(
              IBpssConstants.PARTNER_KEY,
              rtProcessDoc.getPartnerKey());
            callXpdlProcess(resActivity, contextData);
          }
          else
          {
            if (rtProcessDocStatus != GWFRtProcess.CLOSED_COMPLETED)
              checkAndComplete(rtProcessDoc, false);
          }
          Logger.debug(
            "[BpssBusinessTransHelper.processRequestDocument] After Updating Status "
              + rtProcessDoc);
        }
      }
      else
        sendSignal(
          rtProcessDoc.getExceptionSignalType(),
          rtProcessDoc.getReason(),
          documentType,
          rtProcessDoc,
          rtProcess.getContextUId());
    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.processRequestDocument] ",
        th);
    }
  }
  
  public static void processRequestDocument(
    String documentType,
    Object documentObject,
    GWFRtProcessDoc rtProcessDoc,
    String partnerKey)
    throws WorkflowException
  {
    //this will be called by responding role
    
    acquireRespondRoleLock();
    try
    {
      processRequestDocumentNoLock(documentType, documentObject, rtProcessDoc, partnerKey);
    }
    finally
    {
      releaseRespondRoleLock();
    }
  }

  public static boolean validateDocument(
    GWFRtProcessDoc rtProcessDoc,
    String documentId,
    String documentType,
    Object documentObj,
    String partnerKey,
    BpssBusinessDocument businessDoc)
    throws SystemException
  {

    try
    {
      String specLocation = null;
      String specElement = null;

      //get the business document
      if (businessDoc != null)
      {
        specLocation = businessDoc.getSpecLocation();
        specElement = businessDoc.getSpecElement();
      }

      Properties prop =
        WorkflowUtil.getProperties(
          IWorkflowConstants.CONFIG_WORKFLOW_VALIDATEDOC_APP);
      HashMap dataMap = new HashMap();

      dataMap.put(IBpssConstants.DOCUMENT_OBJECT, documentObj);
      dataMap.put(IBpssConstants.DOCUMENTID, documentId);
      dataMap.put(IBpssConstants.DOCUMENT_TYPE, documentType);
      dataMap.put(IBpssConstants.PARTNER_KEY, partnerKey);
      dataMap.put("specLocation", specLocation);
      dataMap.put("specElement", specElement);
      Object obj = AppAdaptor.callApp(prop, dataMap, null);

      if (obj != null)
      { //means invalid ,the return is some exception message
        Logger.debug(
          "[BpssBusinessTransHelper.validateDocument] Invalid Document "
            + dataMap);
        int rtProcessDocStatus = rtProcessDoc.getStatus().intValue();

        if (rtProcessDocStatus == GWFRtProcess.OPEN_NOTRUNNING
          || rtProcessDocStatus == GWFRtProcess.OPEN_RUNNING)
        {
          ReentrantLock l = getLockByDocId(documentId);
          l.lock();
          try
          {
            BpssBusinessTransHelper.abort(
              (Long) rtProcessDoc.getKey(),
              GWFRtProcess.CLOSED_ABNORMALCOMPLETED,
              IBpssConstants.EXCEPTION_VALIDATE,
              obj,
              documentType,
              null);
          }
          finally
          {
            l.unlock();
          }
        }
        return false;
      }
      else
        return true;
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[BpssBusinessTransHelper.validateDocument] Exception ",
        th);
    }
  }

  private static ReentrantLock getLockByDocId(String docId)
  {
    return BpssKeyHelper.isRequestingRole(docId) ? _requestRoleLock : _respondRoleLock;
  }
  
  public static void acquireRequestRoleLock()
  {
    _requestRoleLock.lock();
  }
  
  public static void acquireRespondRoleLock()
  {
    _respondRoleLock.lock(); 
  }
  
  public static void releaseRequestRoleLock()
  {
    _requestRoleLock.unlock(); 
  }
  
  public static void releaseRespondRoleLock()
  {
    _respondRoleLock.unlock();
  }
  
  //NOT IN USED?
  public static void processInActive(
    String documentId,
    String documentType,
    Object documentObject,
    String senderKey,
    GWFRtProcessDoc rtProcessDoc)
  {
    try
    {
      GWFRtProcess rtProcess =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      if (documentType != null)
      {
        //put the document in context
        dataManager.setContextData(
          rtProcess.getContextUId(),
          new ContextKey(documentType),
          documentObject);
      }
      //sendSignal(IBpssConstants.EXCEPTION_SIGNAL,rtProcessDoc.getReason(),documentType,rtProcessDoc,rtProcess.getContextUId());
      sendSignal(
        rtProcessDoc.getExceptionSignalType(),
        rtProcessDoc.getReason(),
        documentType,
        rtProcessDoc,
        rtProcess.getContextUId());
    }
    catch (Throwable th)
    {
      Logger.error(ILogErrorCodes.WORKFLOW_BPSS_PROCESS_IN_ACTIVE,
                   "[BpssBusinessTransHelper.processInActive] Exception: "+th.getMessage(), th);
    }
  }

  public static void notifyReceivedDoc(
    GWFRtProcessDoc rtProcessDoc,
    String documentType,
    Object documentObject,
    boolean isValid)
    throws SystemException
  {
    try
    {
      Properties prop =
        WorkflowUtil.getProperties(
          IWorkflowConstants.CONFIG_WORKFLOW_NOTIFY_RECEIVEDDOC_APP);
      HashMap dataMap = new HashMap();

      dataMap.put(IBpssConstants.DOCUMENT_OBJECT, documentObject);
      dataMap.put(IBpssConstants.DOCUMENTID, rtProcessDoc.getDocumentId());
      dataMap.put(IBpssConstants.DOCUMENT_TYPE, documentType);
      dataMap.put("isRetry", rtProcessDoc.getDocProcessedFlag());
      dataMap.put("isValid", new Boolean(isValid));
      //Object obj = 
      AppAdaptor.callApp(prop, dataMap, null);
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[BpssBusinessTransHelper.notifyReceivedDoc] Exception ",
        th);
    }
  }

  public static void sendRequestDocument(GWFRtProcessDoc rtProcessDoc)
    throws WorkflowException
  {
    try
    {
      GWFRtProcess rtBusinessTrans =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      //IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      //increment the retry count
      /*
       int retryCount=rtProcessDoc.getRetryCount().intValue()+1;
       rtProcessDoc.setRetryCount(new Integer(retryCount));
       UtilEntity.update(rtProcessDoc,true);
       Logger.log("[BpssBusinessTransHelper.sendRequestDocument] retryCount="+retryCount+","+rtProcessDoc);
       */
      //send the request document
      HashMap paramMap = new HashMap();

      paramMap.put(IBpssConstants.DOCUMENTID, rtProcessDoc.getDocumentId());
      paramMap.put(
        IBpssConstants.DOCUMENT_TYPE,
        rtProcessDoc.getDocumentType());
      //paramMap.put(IBpssConstants.DOCUMENT_OBJECT,dataManager.getContextData(rtBusinessTrans.getContextUId(),new ContextKey(rtProcessDoc.getDocumentType())));
      paramMap.put(IBpssConstants.RETRY_COUNT, rtProcessDoc.getRetryCount());
      paramMap.put(IBpssConstants.PARTNER_KEY, rtProcessDoc.getPartnerKey());
      paramMap.put(IBpssConstants.RTPROCESSDOC_UID, rtProcessDoc.getKey());
      paramMap.put("contextUId", rtBusinessTrans.getContextUId());
      WorkflowUtil.sendMessage(
        WorkflowUtil.getProperty(
          IWorkflowConstants.WORKFLOW_SENDREQDOCUMENT_DEST),
        paramMap);

    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.sendRequestDocument] ",
        th);
    }
  }

  public static void sendResponseDocument(GWFRtProcessDoc rtProcessDoc)
    throws WorkflowException
  {
    try
    {
      GWFRtProcess rtBusinessTrans =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);

      /*
       //increment the retry count
       int retryCount=rtProcessDoc.getRetryCount().intValue()+1;
       rtProcessDoc.setRetryCount(new Integer(retryCount));
       UtilEntity.update(rtProcessDoc,true);
       */
      //send the response document
      HashMap paramMap = new HashMap();

      paramMap.put(IBpssConstants.DOCUMENTID, rtProcessDoc.getDocumentId());
      paramMap.put(
        IBpssConstants.DOCUMENT_TYPE,
        rtProcessDoc.getDocumentType());
      //paramMap.put(IBpssConstants.DOCUMENT_OBJECT,dataManager.getContextData(rtBusinessTrans.getContextUId(),new ContextKey(rtProcessDoc.getDocumentType())));
      paramMap.put(IBpssConstants.RETRY_COUNT, rtProcessDoc.getRetryCount());
      paramMap.put(IBpssConstants.PARTNER_KEY, rtProcessDoc.getPartnerKey());
      paramMap.put(IBpssConstants.RTPROCESSDOC_UID, rtProcessDoc.getKey());
      paramMap.put("contextUId", rtBusinessTrans.getContextUId());
      WorkflowUtil.sendMessage(
        WorkflowUtil.getProperty(
          IWorkflowConstants.WORKFLOW_SENDRESDOCUMENT_DEST),
        paramMap);

    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.sendResponseDocument] ",
        th);
    }
  }

  public static void sendSignal(
    String signalType,
    Object reason,
    String documentType,
    GWFRtProcessDoc rtProcessDoc,
    Long contextUId)
    throws WorkflowException
  {
    HashMap paramMap = new HashMap();

    try
    {
      paramMap.put("rtDocumentUId", rtProcessDoc.getKey());
      paramMap.put(IBpssConstants.DOCUMENTID, rtProcessDoc.getDocumentId());
      paramMap.put(IBpssConstants.DOCUMENT_TYPE, documentType);
      paramMap.put(IBpssConstants.SIGNAL_TYPE, signalType);
      paramMap.put(IBpssConstants.REASON, reason);
      paramMap.put(IBpssConstants.PARTNER_KEY, rtProcessDoc.getPartnerKey());
      paramMap.put("contextUId", contextUId);
      Logger.debug("[BpssBusinessTransHelper.sendSignal] paramMap=" + paramMap);
      WorkflowUtil.sendMessage(
        WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_SENDSIGNAL_DEST),
        paramMap);
    }
    catch (Throwable th)
    {
      throw new WorkflowException(
        "Unable to send signal: "+th.getMessage(),
        th);
    }
  }

  public static void receivedSignal(
    String signalType,
    Object reason,
    String documentId,
    String senderKey)
    throws WorkflowException
  {
    Logger.debug(
      "[BpssBusinessTransHelper.receivedSignal] signalType="
        + signalType
        + ", documentId="
        + documentId
        + ", reason="
        + reason
        + ", senderKey="
        + senderKey);
    
    ReentrantLock l = getLockByDocId(documentId);
    l.lock();
    try
    {
      //notifyReceivedSignal(signalType,reason,documentId,senderKey);

      GWFRtProcessDoc rtProcessDoc = getActiveRtProcessDoc(documentId);

      if (rtProcessDoc != null)
      {

        Logger.debug(
          "[BpssBusinessTransHelper.receivedSignal] rtProcessDoc="
            + rtProcessDoc);
        if (IBpssConstants.ACK_RECEIPT_SIGNAL.equals(signalType))
        {
          rtProcessDoc.setAckReceiptSignalFlag(Boolean.TRUE);
          
          //TWX: pass by value
          rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
          String key =
            KeyConverter.getKey(
              (Long) rtProcessDoc.getKey(),
              rtProcessDoc.getEntityName(),
              IWorkflowConstants.BPSS_ENGINE);

          WorkflowUtil.cancelAlarm(
            key,
            IBpssConstants.CHECK_TIMETO_ACK_RECEIPT,
            IWorkflowConstants.BPSS_ENGINE);
          checkAndComplete(rtProcessDoc, false);
        }
        else if (IBpssConstants.ACK_ACCEPTANCE_SIGNAL.equals(signalType))
        {
          rtProcessDoc.setAckAcceptSignalFlag(Boolean.TRUE);
          //TWX: pass by value
          rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
          String key =
            KeyConverter.getKey(
              (Long) rtProcessDoc.getKey(),
              rtProcessDoc.getEntityName(),
              IWorkflowConstants.BPSS_ENGINE);

          WorkflowUtil.cancelAlarm(
            key,
            IBpssConstants.CHECK_TIMETO_ACK_ACCEPT,
            IWorkflowConstants.BPSS_ENGINE);
          checkAndComplete(rtProcessDoc, false);
        }
        else if (
          IBpssConstants.EXCEPTION_VALIDATE.equals(signalType)
            || IBpssConstants.EXCEPTION_SIGNAL.equals(signalType))
        {
          abort(
            (Long) rtProcessDoc.getKey(),
            GWFRtProcess.CLOSED_ABNORMALCOMPLETED,
            signalType,
            reason,
            null,
            senderKey);
        }
        else
          Logger.debug(
            "[BpssBusinessTransHelper.receivedSignal] Signal type did not match (ACK_RECEIPT_SIGNAL,ACK_ACCEPTANCE_SIGNAL,EXCEPTION_VALIDATE,EXCEPTION_SIGNAL), signalType="
              + signalType);

        Logger.debug(
          "[BpssBusinessTransHelper.receivedSignal] after changing flag ,rtProcessDoc="
            + rtProcessDoc);
      }
      else
        Logger.debug(
          "[BpssBusinessTransHelper.receivedSignal] rtProcessDoc is null for documentId="
            + documentId);

    }
    catch (Throwable th)
    {
      throw new WorkflowException(
        "[BpssBusinessTransHelper.receivedSignal] Exception ,signalType:"
          + signalType
          + ",documentId="
          + documentId,
        th);
    }
    finally
    {
      l.unlock();
    }
  }

  public static void notifyReceivedSignal(
    String signalType,
    Object reason,
    String documentId,
    String senderKey)
    throws SystemException
  {
    try
    {
      Properties prop =
        WorkflowUtil.getProperties(
          IWorkflowConstants.CONFIG_WORKFLOW_NOTIFY_RECEIVEDSIGNAL_APP);
      HashMap dataMap = new HashMap();

      dataMap.put(IBpssConstants.SIGNAL_TYPE, signalType);
      dataMap.put(IBpssConstants.REASON, reason);
      dataMap.put(IBpssConstants.DOCUMENTID, documentId);
      dataMap.put(IBpssConstants.SENDER_KEY, senderKey);
      //Object obj = 
      AppAdaptor.callApp(prop, dataMap, null);
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[BpssBusinessTransHelper.notifyReceivedSignal] Exception ",
        th);
    }
  }

  public static boolean checkCanComplete(GWFRtProcessDoc rtProcessDoc)
    throws Exception
  {
    try
    {
      GWFRtProcess rtBusinessTrans =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      HashMap contextData =
        WorkflowUtil.getDataManager().getContextData(
          rtBusinessTrans.getContextUId());

      if (IBpssConstants.INITIATING_ROLE.equals(rtProcessDoc.getRoleType()))
      {
        if (rtProcessDoc.getRetryCount().intValue() == 0)
          //means document is not yet sent to responder
          return false;
        if (rtProcessDoc.getResponseDocTypes() != null
          && !rtProcessDoc.getDocProcessedFlag().booleanValue())
          return false;
      }
      else
      {
        if (!rtProcessDoc.getDocProcessedFlag().booleanValue())
          return false;
      }

      boolean completeFlag = false;
      boolean hasAckReceipt =
        (contextData.get(new ContextKey("workflow.control.hasAckReceipt"))
          != null);
      boolean hasAckAccept =
        (contextData.get(new ContextKey("workflow.control.hasAckAccept"))
          != null);
      boolean ackReceiptFlag =
        rtProcessDoc.getAckReceiptSignalFlag().booleanValue();
      boolean ackAcceptFlag =
        rtProcessDoc.getAckAcceptSignalFlag().booleanValue();

      int ind = 0;

      while (ind != -1)
      {
        switch (ind)
        {
          case 0 :
            ind = (hasAckReceipt) ? 1 : 0;
            ind = (ind == 0) ? 2 : ind;
            break;

          case 1 :
            ind = (ackReceiptFlag) ? 2 : 1;
            ind = (ind == 1) ? -1 : ind;
            break;

          case 2 :
            ind = (hasAckAccept) ? 3 : 2;
            ind = (ind == 2) ? 4 : ind;
            break;

          case 3 :
            ind = (ackAcceptFlag) ? 4 : 3;
            ind = (ind == 3) ? -1 : ind;
            break;

          case 4 :
            completeFlag = true;
            ind = -1;
            break;

          default :
            ind = -1;
        }
      }
      return completeFlag;
    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.checkAndComplete] Unable to check for completion ",
        th);
    }
  }

  public static void checkAndComplete(
    GWFRtProcessDoc rtProcessDoc,
    boolean canAbort)
    throws Exception
  {
    ReentrantLock l = getLockByDocId(rtProcessDoc.getDocumentId());
    
    l.lock();
    try
    {
      //NSL20060109 Refetch entity before update
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.getEntityByKey((Long)rtProcessDoc.getKey(), GWFRtProcessDoc.ENTITY_NAME, true);
      
      if (GWFRtProcess.OPEN_NOTRUNNING != rtProcessDoc.getStatus().intValue()
        && GWFRtProcess.OPEN_RUNNING != rtProcessDoc.getStatus().intValue())
        return;
      boolean completeFlag = checkCanComplete(rtProcessDoc);

      Logger.debug(
        "[BpssBusinessTransHelper.checkAndComplete] completeFlag="
          + completeFlag
          + ", canAbort="
          + canAbort
          + ", rtProcessDoc="
          + rtProcessDoc);
      if (completeFlag || canAbort)
      {
        GWFRtProcess rtBusinessTrans =
          (GWFRtProcess) UtilEntity.getEntityByKey(
            rtProcessDoc.getRtBusinessTransactionUId(),
            GWFRtProcess.ENTITY_NAME,
            true);
        IDataFilter filter = new DataFilterImpl();

        filter.addSingleFilter(
          null,
          GWFRtActivity.RT_PROCESS_UID,
          filter.getEqualOperator(),
          rtBusinessTrans.getKey(),
          false);
        Collection coll =
          UtilEntity.getEntityByFilter(filter, GWFRtActivity.ENTITY_NAME, true);
        GWFRtActivity rtActivity = (GWFRtActivity) coll.iterator().next();

        if (completeFlag)
        {
          rtProcessDoc.setStatus(new Integer(GWFRtProcess.CLOSED_COMPLETED));
          UtilEntity.update(rtProcessDoc, true);
          GWFFactory
            .getActivityEngine(IWorkflowConstants.BPSS_ENGINE)
            .changeRtActivityState(
              rtActivity,
              new Integer(GWFRtActivity.CLOSED_COMPLETED));
        }
        else if (canAbort)
        {
          abort(
            (Long) rtProcessDoc.getKey(),
            GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED,
            IBpssConstants.EXCEPTION_TIMETO_PERFORM,
            "Exceeded Time To Perform",
            rtProcessDoc.getDocumentType(),
            rtProcessDoc.getPartnerKey());
          
        }
      }
    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new WorkflowException(
        "[BpssBusinessTransHelper.checkAndComplete] Unable to check for completion ",
        th);
    }
    finally
    {
      l.unlock();
    }
  }

  public static void abort(
    Long rtProcessDocUId,
    int state,
    String exceptionType,
    Object reason,
    String documentType,
    String senderKey)
    throws SystemException
  {
    Logger.debug(
      "[BpssBusinessTransHelper.abort] rtProcessDocUId="
        + rtProcessDocUId
        + ", exceptionType="
        + exceptionType
        + ", reason="
        + reason);
    try
    {
      removeTimers(rtProcessDocUId);
      GWFRtProcessDoc rtProcessDoc =
        (GWFRtProcessDoc) UtilEntity.getEntityByKey(
          rtProcessDocUId,
          GWFRtProcessDoc.ENTITY_NAME,
          true);
      GWFRtProcess rtProcess =
        (GWFRtProcess) UtilEntity.getEntityByKey(
          rtProcessDoc.getRtBusinessTransactionUId(),
          GWFRtProcess.ENTITY_NAME,
          true);
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();

      if (senderKey != null)
      { // exception is raised by other engine,set set some flag in context
        dataManager.setContextData(
          rtProcess.getContextUId(),
          new ContextKey(IBpssConstants.EXCEPTION_SENDERKEY),
          senderKey);
      }
      if (documentType != null)
      {
        dataManager.setContextData(
          rtProcess.getContextUId(),
          new ContextKey(IBpssConstants.EXCEPTION_DOCUMENTTYPE),
          documentType);
      }
      dataManager.setContextData(
        rtProcess.getContextUId(),
        new ContextKey(IBpssConstants.EXCEPTION_RTPROCESSDOC_UID),
        rtProcessDoc.getKey());
      rtProcessDoc.setStatus(new Integer(state));
      rtProcessDoc.setExceptionSignalType(exceptionType);
      rtProcessDoc.setReason(reason);
      
      rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        GWFRtActivity.RT_PROCESS_UID,
        filter.getEqualOperator(),
        rtProcessDoc.getRtBusinessTransactionUId(),
        false);
      Collection coll =
        UtilEntity.getEntityByFilter(filter, GWFRtActivity.ENTITY_NAME, true);

      Logger.debug("[BpssBusinessTransHelper.abort] coll=" + coll.size());
      if (coll != null && coll.size() > 0)
      {
        GWFRtActivity rtActivity = (GWFRtActivity) coll.iterator().next();

        GWFFactory
          .getActivityEngine(IWorkflowConstants.BPSS_ENGINE)
          .changeRtActivityState(
            rtActivity,
            new Integer(GWFRtActivity.CLOSED_ABNORMALCOMPLETED));
      }
      else
        GWFFactory
          .getProcessEngine(IWorkflowConstants.BPSS_ENGINE)
          .changeRtProcessState(rtProcess, new Integer(state));
    }
    catch (Throwable th)
    {
      throw new SystemException("[BpssBusinessTransHelper.abort]", th);
    }
  }

  public static void removeTimers(Long rtProcessDocUId) throws SystemException
  {
    String key =
      KeyConverter.getKey(
        rtProcessDocUId,
        GWFRtProcessDoc.ENTITY_NAME,
        IWorkflowConstants.BPSS_ENGINE);

    Logger.debug(
      "[BpssBusinessTransHelper.removeTimers] rtProcessDocUId="
        + rtProcessDocUId
        + ",key="
        + key);
    WorkflowUtil.cancelAlarm(
      key,
      IBpssConstants.CHECK_TIMETO_ACK_RECEIPT,
      IWorkflowConstants.BPSS_ENGINE);
    WorkflowUtil.cancelAlarm(
      key,
      IBpssConstants.CHECK_TIMETO_ACK_ACCEPT,
      IWorkflowConstants.BPSS_ENGINE);
    WorkflowUtil.cancelAlarm(
      key,
      BpssBusinessTransActivity.ENTITY_NAME,
      IWorkflowConstants.BPSS_ENGINE);
  }

  public static void checkTimeTo(String senderKey, String receiverKey)
    throws WorkflowException
  {
    Logger.debug(
      "[BpssBusinessTransHelper.checkTimeTo] senderKey="
        + senderKey
        + ", receiverKey="
        + receiverKey);
    try
    {
      Long uId = KeyConverter.getUID(senderKey);
      String entityName = KeyConverter.getEntityName(senderKey);

      if (BpssBinaryCollaboration.ENTITY_NAME.equals(receiverKey))
      {
        try
        {
          GWFRtProcess rtProcess =
            (GWFRtProcess) UtilEntity.getEntityByKey(
              uId,
              GWFRtProcess.ENTITY_NAME,
              true);
          int state = rtProcess.getState().intValue();

          if (state == GWFRtProcess.OPEN_NOTRUNNING
            || state == GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED
            || state == GWFRtProcess.OPEN_RUNNING)
            GWFFactory
              .getProcessEngine(IWorkflowConstants.BPSS_ENGINE)
              .changeRtProcessState(
                rtProcess,
                new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
        }
        catch (Throwable th)
        {
          th.printStackTrace();
        }
      }
      else if (GWFRtProcessDoc.ENTITY_NAME.equals(entityName))
      {
        GWFRtProcessDoc rtProcessDoc = null;
        try
        {
          //senderKey holds the documentId,use this to get GWFRtProcessDoc
          rtProcessDoc =
            (GWFRtProcessDoc) UtilEntity.getEntityByKey(uId, entityName, true);
        }
        catch (FinderException ex)
        {
          Logger.debug(
            "[BpssBusinessTransHelper.checkTimeTo] No rtProcessDoc with UID="
              + uId
              + ", senderKey="
              + senderKey
              + ", receiverKey="
              + receiverKey);
          WorkflowUtil.cancelAlarm(
            senderKey,
            receiverKey,
            IWorkflowConstants.BPSS_ENGINE);
          return;
        }

        String roleType = rtProcessDoc.getRoleType();

        if (rtProcessDoc != null && receiverKey != null)
        {
          int status = rtProcessDoc.getStatus().intValue();

          if (receiverKey.equals(IBpssConstants.CHECK_TIMETO_ACK_RECEIPT))
          {
            if (status != GWFRtProcess.OPEN_NOTRUNNING
              && status != GWFRtProcess.OPEN_RUNNING)
            {
              Logger.debug(
                "[BpssBusinessTransHelper.checkTimeTo] CHECK_TIMETO_ACK_RECEIPT,rtProcessDoc status is already aborted "
                  + rtProcessDoc);
              return;
            }
            if (!rtProcessDoc.getAckReceiptSignalFlag().booleanValue())
            {
              if (roleType.equals(IBpssConstants.INITIATING_ROLE))
                sendRequestDocument(rtProcessDoc);
              else if (roleType.equals(IBpssConstants.RESPONDING_ROLE))
                sendResponseDocument(rtProcessDoc);
            }
            else
            {
              //already received receipt signal
              checkAndComplete(rtProcessDoc, false);
            }
          }
          else if (receiverKey.equals(IBpssConstants.CHECK_TIMETO_ACK_ACCEPT))
          {
            if (status != GWFRtProcess.OPEN_NOTRUNNING
              && status != GWFRtProcess.OPEN_RUNNING)
            {
              Logger.debug(
                "[BpssBusinessTransHelper.checkTimeTo] CHECK_TIMETO_ACK_ACCEPT,rtProcessDoc status is already aborted "
                  + rtProcessDoc);
              return;
            }

            if (!rtProcessDoc.getAckAcceptSignalFlag().booleanValue())
            {
              if (roleType.equals(IBpssConstants.INITIATING_ROLE))
              {

                /**@todo:need to nullify this transaction*/
              }
            }
          }
          else if (BpssBusinessTransActivity.ENTITY_NAME.equals(receiverKey))
          {
            //GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(uId,GWFRtProcess.ENTITY_NAME,true);
            //this is for BusinessTransActivity
            //if(rtProcessDoc.getStatus().intValue()!=GWFRtActivity.CLOSED_COMPLETED && rtProcessDoc.getStatus().intValue()!=GWFRtActivity.CLOSED_ABNORMALCOMPLETED){
            checkAndComplete(rtProcessDoc, true);
            
            //TWX 14062006 Trigger 0A1 if the exception type is IBpssConstants.EXCEPTION_TIMETO_PERFORM
            rtProcessDoc = (GWFRtProcessDoc)UtilEntity.getEntityByKey((Long)rtProcessDoc.getKey(), GWFRtProcessDoc.ENTITY_NAME, true);
            if(IBpssConstants.EXCEPTION_TIMETO_PERFORM.equals(
                               rtProcessDoc.getExceptionSignalType()))
            {
            	GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(
            	                                         rtProcessDoc.getRtBusinessTransactionUId(),
            	                                         GWFRtProcess.ENTITY_NAME,
            	                                         true);
            	sendSignal(
                         rtProcessDoc.getExceptionSignalType(),
                         rtProcessDoc.getReason(),
                         rtProcessDoc.getDocumentType(),
                         rtProcessDoc,
                         rtProcess.getContextUId());
            }
            //}
          }
        }
      }
      else
        throw new WorkflowException(
          "[BpssBusinessTransHelper.checkTimeTo] Unknown keys, senderKey="
            + senderKey
            + ",receiverKey="
            + receiverKey);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new WorkflowException(
        "[BpssBusinessTransHelper.checkTimeTo] Exception, senderKey="
          + senderKey
          + ",receiverKey="
          + receiverKey,
        th);
    }
  }

  //when failure state is reached
  public static void doFailure(GWFRtProcess rtProcess) throws SystemException
  {
    try
    {
      String senderKey =
        (String) WorkflowUtil.getDataManager().getContextData(
          rtProcess.getContextUId(),
          new ContextKey(IBpssConstants.EXCEPTION_SENDERKEY));
      Long rtProcessDocUId =
        (Long) WorkflowUtil.getDataManager().getContextData(
          rtProcess.getContextUId(),
          new ContextKey(IBpssConstants.EXCEPTION_RTPROCESSDOC_UID));
      String documentType =
        (String) WorkflowUtil.getDataManager().getContextData(
          rtProcess.getContextUId(),
          new ContextKey(IBpssConstants.EXCEPTION_DOCUMENTTYPE));

      if (senderKey == null && rtProcessDocUId != null)
      {
        GWFRtProcessDoc rtProcessDoc =
          (GWFRtProcessDoc) UtilEntity.getEntityByKey(
            rtProcessDocUId,
            GWFRtProcessDoc.ENTITY_NAME,
            true);
        String exSignalType = rtProcessDoc.getExceptionSignalType();

        if (exSignalType != null)
        {
          if (IBpssConstants.EXCEPTION_TIMETO_ACK.equals(exSignalType))
          {
            sendSignal(
              exSignalType,
              rtProcessDoc.getReason(),
              documentType,
              rtProcessDoc,
              rtProcess.getContextUId());
          }
          else if (
            IBpssConstants.EXCEPTION_TIMETO_PERFORM.equals(exSignalType))
          {
            sendSignal(
              exSignalType,
              rtProcessDoc.getReason(),
              documentType,
              rtProcessDoc,
              rtProcess.getContextUId());
          }
          else if (IBpssConstants.EXCEPTION_VALIDATE.equals(exSignalType))
          {
            sendSignal(
              exSignalType,
              rtProcessDoc.getReason(),
              documentType,
              rtProcessDoc,
              rtProcess.getContextUId());
          }
        }
      }
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[BpssBusinessTransHelper.doFailure] Exception ",
        th);
    }
  }

  public static BpssBusinessDocument getBusinessDocument(String documentType)
    throws SystemException
  {
    try
    {
      //get the business document
      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        BpssDocumentEnvelope.BUSINESS_DOCUMENTID_REF,
        filter.getEqualOperator(),
        documentType,
        false);
      Collection envColl =
        UtilEntity.getEntityByFilter(
          filter,
          BpssDocumentEnvelope.ENTITY_NAME,
          true);

      if (envColl != null && envColl.size() > 0)
      {
        for (Iterator iterator = envColl.iterator(); iterator.hasNext();)
        {
          BpssDocumentEnvelope docEnvelop =
            (BpssDocumentEnvelope) iterator.next();
          String businessDocName = docEnvelop.getBusinessDocumentName();
          BpssProcessSpecEntry processSpec =
            BpssDefinitionHelper.getProcessSpecEntry(
              (Long) docEnvelop.getKey(),
              docEnvelop.getEntityName());

          filter = new DataFilterImpl();
          filter.addSingleFilter(
            null,
            BpssProcessSpecEntry.SPEC_UID,
            filter.getEqualOperator(),
            new Long(processSpec.getSpecUId()),
            false);
          filter.addSingleFilter(
            filter.getAndConnector(),
            BpssProcessSpecEntry.ENTRY_NAME,
            filter.getEqualOperator(),
            businessDocName,
            false);
          filter.addSingleFilter(
            filter.getAndConnector(),
            BpssProcessSpecEntry.ENTRY_TYPE,
            filter.getEqualOperator(),
            BpssBusinessDocument.ENTITY_NAME,
            false);
          Collection specColl =
            UtilEntity.getEntityByFilter(
              filter,
              BpssProcessSpecEntry.ENTITY_NAME,
              true);

          if (specColl != null && specColl.size() == 1)
          {
            processSpec = (BpssProcessSpecEntry) specColl.iterator().next();
            return (BpssBusinessDocument) UtilEntity.getEntityByKey(
              processSpec.getEntryUId(),
              processSpec.getEntryType(),
              true);
          }
        }
      } //else throw new WorkflowException("[BpssBusinessTransHelper.validateDocument] No documentEnvelop exists with documentType="+documentType);
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[BpssBusinessTransHelper.getBusinessDocument] Exception ",
        th);
    }
    return null;
  }

  public static BpssDocumentEnvelope getRequestDocumentEnvelope(BpssReqBusinessActivity reqActivity)
    throws Throwable
  {
    Collection docEnvelopes =
      (Collection) BpssDefinitionHelper.getDocumentEnvelopes(
        (Long) reqActivity.getKey(),
        BpssReqBusinessActivity.ENTITY_NAME);

    if (docEnvelopes != null && docEnvelopes.size() > 0)
      return (BpssDocumentEnvelope) docEnvelopes.iterator().next();
    return null;
  }

  public static Collection getResponseDocumentEnvelopes(BpssResBusinessActivity resActivity)
    throws Throwable
  {
    Collection docEnvelopes =
      (Collection) BpssDefinitionHelper.getDocumentEnvelopes(
        (Long) resActivity.getKey(),
        BpssResBusinessActivity.ENTITY_NAME);

    if (docEnvelopes != null && docEnvelopes.size() > 0)
      return docEnvelopes;
    return null;
  }

  public static Long callXpdlProcess(
    BpssBusinessAction businessAction,
    HashMap contextData)
  {
    try
    {
      Collection documentationColl =
        BpssDefinitionHelper.getChildEntities(
          (Long) businessAction.getKey(),
          businessAction.getEntityName(),
          BpssDocumentation.ENTITY_NAME);

      if (documentationColl != null)
      {
        for (Iterator iterator = documentationColl.iterator();
          iterator.hasNext();
          )
        {
          BpssDocumentation bpssDocumentation =
            (BpssDocumentation) iterator.next();

          if (bpssDocumentation.getUri() != null
            && bpssDocumentation.getUri().equals("XpdlProcess"))
          {
            return WorkflowUtil.getWorkflowManager().createRtProcess(
              bpssDocumentation.getDocumentation(),
              null,
              contextData);
          }
        }
      }
    }
    catch (Throwable th)
    {
      Logger.warn("[BpssBusinessTransHelper.callXpdlProcess] Exception ", th);
    }
    return null;
  }

  //15072008: we will pass in the jmsMsgID as well since we want to diffrentiate whether the doc
  //          is a resend from TP or a JMS-internal resend.
  public static boolean addConcurrentRequest(
    String documentId,
    Object documentObject,
    String senderKey,
    String jmsMsgID)
  {
    Collection coll = (Collection) concurrentReqHt.get(documentId);
    boolean isConcurrentRequest = false;
    
    if (coll == null)
    {
      synchronized (concurrentReqHt)
      {
        coll = (Collection) concurrentReqHt.get(documentId);
        if (coll == null)
        {
          coll = new Vector();
          concurrentReqHt.put(documentId, coll);
          
          //#69 TWX: Allow us to keep track the 1st receive req doc jms msg id, we will ignore such mapMsg when processConcurrentRequests
          Map mapMsg = new HashMap(4);
	      mapMsg.put(IBpssConstants.DOCUMENTID, documentId);
	      mapMsg.put(IBpssConstants.DOCUMENT_OBJECT, documentObject);
	      mapMsg.put(IBpssConstants.SENDER_KEY, senderKey);
	      mapMsg.put(JMS_MSG_ID, jmsMsgID);
	      coll.add(mapMsg);
          
          return false;
        }
      }
    }
    synchronized (coll)
    {
      boolean isCachedBefore = false;
      boolean isMatchFirstCache = false;
      int nth = 0;
      
      if(coll != null )
      {
    	  for(Iterator<Map> ite = coll.iterator(); ite.hasNext(); nth ++)
    	  {
    		  Map mapMsg = ite.next();
    		  if(jmsMsgID.equals(mapMsg.get(JMS_MSG_ID)))
    		  {
    			  isCachedBefore = true;
    			  Logger.log("[BpssBusinessTransHelper.addConcurrentRequest] JMS message ID :"+jmsMsgID+", DocumentID :"+documentId+" has been cached before.");
    			  
    			  if(nth == 0)
    			  {
    				  isMatchFirstCache = true;
    			  }
    			  
    			  break;
    		  }
    	  }
      }
      
      //TWX: keep track concurrent req doc. If global Transaction failed,
      //the cache in the memory will not be rollback. We need to check
      //if such document has been cached before to avoid additional processing 
      //on the same document.
      if(! isCachedBefore)
      {
	      Map mapMsg = new HashMap(4);
	      mapMsg.put(IBpssConstants.DOCUMENTID, documentId);
	      mapMsg.put(IBpssConstants.DOCUMENT_OBJECT, documentObject);
	      mapMsg.put(IBpssConstants.SENDER_KEY, senderKey);
	      mapMsg.put(JMS_MSG_ID, jmsMsgID);
	      coll.add(mapMsg);
      }
      
      if( isMatchFirstCache) //the first receive of req doc has been rolled back, we need to continue process such document.
      {
    	  isConcurrentRequest = false;
    	  Logger.log("[BpssBusinessTransHelper.addConcurrentRequest] JMS message ID :"+jmsMsgID+", DocumentID :"+documentId+" has been cached before and it match the first cache. Is Concurrent Request = false");
      }
      else
      {
    	  isConcurrentRequest = true;
      }
    }
    Logger.log(
      "[BpssBusinessTransHelper.addConcurrentRequest] documentId="
        + documentId);
    return isConcurrentRequest;
  }

  public static void processConcurrentRequests(
    String documentId,
    Long rtProcessDocUId)
    throws Throwable
  {
    Collection coll = null;
    synchronized (concurrentReqHt)
    {
      coll = (Collection) concurrentReqHt.remove(documentId);
    }
    if (coll != null)
    {
      synchronized (coll)
      {
        //TWX 15072008  
    	int nth = 0;  
    	  
        for (Iterator iterator = coll.iterator(); iterator.hasNext(); nth++)
        {
          Map mapMsg = (Map) iterator.next();
          
          if(nth > 0) //TWX 15072008  the 1st cache is for keeping track purpose, can ignored such msg
          {
	          Object documentObject = mapMsg.get(IBpssConstants.DOCUMENT_OBJECT);
	          String senderKey = (String) mapMsg.get(IBpssConstants.SENDER_KEY);
	          GWFRtProcessDoc rtProcessDoc =
	            (GWFRtProcessDoc) UtilEntity.getEntityByKey(
	              rtProcessDocUId,
	              GWFRtProcessDoc.ENTITY_NAME,
	              true);
	          Logger.log(
	            "[BpssBusinessTransHelper.processConcurrentRequests] documentId="
	              + documentId);
            
            //TWX: Change to use no lock version
	          processRequestDocumentNoLock(
	            rtProcessDoc.getRequestDocType(),
	            documentObject,
	            rtProcessDoc,
	            senderKey);
          }
        }
      }
    }
  }

  public static void clearConcurrentRequests(String documentId)
  {
    if (documentId != null)
    {
      synchronized (concurrentReqHt)
      {
        concurrentReqHt.remove(documentId);
      }
    }
  }

  public static GWFRtProcessDoc getRtProcessDoc(String documentId)
    throws WorkflowException
  {
    try
    {
      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        GWFRtProcessDoc.DOCUMENT_ID,
        filter.getEqualOperator(),
        documentId,
        false);
      Collection rtProcessDocColl =
        UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

      if (rtProcessDocColl == null || rtProcessDocColl.size() == 0)
        return null;
      if (rtProcessDocColl.size() == 1)
        return (GWFRtProcessDoc) rtProcessDocColl.iterator().next();
      else
        throw new WorkflowException(
          "[BpssBusinessTransHelper.getRtProcessDoc] more than one GWFRtProcessDoc exists with documentId="
            + documentId);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new WorkflowException(
        "[BpssBusinessTransHelper.getRtProcessDoc] unable to get rtProcessDoc,documentId="
          + documentId,
        th);
    }
  }

  public static GWFRtProcessDoc getActiveRtProcessDoc(String documentId)
    throws WorkflowException
  {
    try
    {
      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        GWFRtProcessDoc.DOCUMENT_ID,
        filter.getEqualOperator(),
        documentId,
        false);
      IDataFilter subFilter = new DataFilterImpl();

      subFilter.addSingleFilter(
        null,
        GWFRtProcessDoc.STATUS,
        subFilter.getEqualOperator(),
        new Integer(IGWFRtActivity.OPEN_NOTRUNNING),
        false);
      subFilter.addSingleFilter(
        subFilter.getOrConnector(),
        GWFRtProcessDoc.STATUS,
        subFilter.getEqualOperator(),
        new Integer(IGWFRtActivity.OPEN_RUNNING),
        false);
      filter.addFilter(filter.getAndConnector(), subFilter);
      Collection rtProcessDocColl =
        UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

      if (rtProcessDocColl == null || rtProcessDocColl.size() == 0)
        return null;
      if (rtProcessDocColl.size() == 1)
        return (GWFRtProcessDoc) rtProcessDocColl.iterator().next();
      else
        throw new WorkflowException(
          "[BpssBusinessTransHelper.getActiveRtProcessDoc] more than one GWFRtProcessDoc exists with documentId="
            + documentId);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new WorkflowException(
        "[BpssBusinessTransHelper.getActiveRtProcessDoc] unable to get rtProcessDoc,documentId="
          + documentId,
        th);
    }
  }

  public static GWFRtProcessDoc getInActiveRtProcessDoc(String documentId)
    throws WorkflowException
  {
    try
    {
      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        GWFRtProcessDoc.DOCUMENT_ID,
        filter.getEqualOperator(),
        documentId,
        false);
      IDataFilter subFilter = new DataFilterImpl();

      subFilter.addSingleFilter(
        null,
        GWFRtProcessDoc.STATUS,
        subFilter.getNotEqualOperator(),
        new Integer(IGWFRtActivity.OPEN_NOTRUNNING),
        false);
      subFilter.addSingleFilter(
        subFilter.getOrConnector(),
        GWFRtProcessDoc.STATUS,
        subFilter.getNotEqualOperator(),
        new Integer(IGWFRtActivity.OPEN_RUNNING),
        false);
      filter.addFilter(filter.getAndConnector(), subFilter);
      Collection rtProcessDocColl =
        UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

      if (rtProcessDocColl == null || rtProcessDocColl.size() == 0)
        return null;
      if (rtProcessDocColl.size() == 1)
        return (GWFRtProcessDoc) rtProcessDocColl.iterator().next();
      else
        throw new WorkflowException(
          "[BpssBusinessTransHelper.getInActiveRtProcessDoc] more than one GWFRtProcessDoc exists with documentId="
            + documentId);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new WorkflowException(
        "[BpssBusinessTransHelper.getInActiveRtProcessDoc] unable to get rtProcessDoc,documentId="
          + documentId,
        th);
    }
  }

  public static boolean isNewRtProcessDoc(String documentId)
    throws SystemException
  {
    try
    {
      IDataFilter filter = new DataFilterImpl();

      filter.addSingleFilter(
        null,
        GWFRtProcessDoc.DOCUMENT_ID,
        filter.getEqualOperator(),
        documentId,
        false);
      Collection rtProcessDocColl =
        UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

      if (rtProcessDocColl == null || rtProcessDocColl.size() == 0)
        return true;
      else
        return false;
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[BpssBusinessTransHelper.isNewRtProcessDoc] unable to get rtProcessDoc,documentId="
          + documentId,
        th);
    }
  }
}
