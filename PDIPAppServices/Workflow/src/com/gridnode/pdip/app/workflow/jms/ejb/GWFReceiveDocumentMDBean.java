/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 * Oct 20 2005    Neo Sok Lay         Throws clause of ejbCreate method must 
 *                                    not define any application exceptions.
 * Oct 11 2005    Neo Sok Lay         Change for J2EE compliance.
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Rollback the entire transaction if encounter retryable exception.
 * Nov 21, 2011   Tam Wei Xiang       #2773: receiveDocument, activityQueue built up and causing backlog                                        
 */
package com.gridnode.pdip.app.workflow.jms.ejb;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssBusinessTransHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssDefinitionHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssKeyHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFActivity;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.util.UtilEntity;
import com.gridnode.pdip.framework.util.UtilString;

public class GWFReceiveDocumentMDBean implements MessageDrivenBean ,MessageListener{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7901450947928467304L;
		private MessageDrivenContext m_context;

    public void ejbRemove() {
    }

    /**
     * Sets the session context.
     *
     * @param ctx       MessageDrivenContext Context for session
     */
    public void setMessageDrivenContext(MessageDrivenContext ctx) {
        m_context = ctx;
    }

    public void ejbCreate() {
    }

    // Implementation of MessageListener
    public void onMessage(Message msg) {
    	
    	 String jmsMessageID = "";  
        try {
            if(msg.getJMSRedelivered())
            {
              Logger.log("[GWFReceiveDocumentMDBean.onMessage] Redelivered msg found. Message: "+msg);
            }
          
            jmsMessageID = msg.getJMSMessageID();
            ObjectMessage objMsg = (ObjectMessage) msg;
            Map mapMsg=(Map)objMsg.getObject();

            String senderKey=(String)mapMsg.get(IBpssConstants.SENDER_KEY);
            String documentId=(String)mapMsg.get(IBpssConstants.DOCUMENTID);
            String documentType=(String)mapMsg.get(IBpssConstants.DOCUMENT_TYPE);
            Object documentObject=mapMsg.get(IBpssConstants.DOCUMENT_OBJECT);
            String initiatorPartnerKey=(String)mapMsg.get(IBpssConstants.INITIATOR_PARTNERKEY);
            String responderPartnerKey=(String)mapMsg.get(IBpssConstants.RESPONDER_PARTNERKEY);
            //Boolean isRequestDocument=(Boolean)mapMsg.getObject(IBpssConstants.ISREQUEST_DOCUMENT);
            receiveDocument(documentId,documentType,documentObject,senderKey,initiatorPartnerKey,responderPartnerKey, msg.getJMSRedelivered(), msg.getJMSMessageID());
            
        } catch (JMSException e) {
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFReceiveDocumentMDBean.onMessage] JMSException",e);
        } catch (Throwable th) {
        	
        	if(JMSRedeliveredHandler.isRedeliverableException(th)) //#69 25072008 TWX
        	{
        		Logger.warn("[GWFReceiveDocumentMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
        		m_context.setRollbackOnly();
        	}
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFReceiveDocumentMDBean.onMessage] Exception ",th);
        }
    }

    public void receiveDocument(String documentId,String documentType,Object documentObject,String senderKey,String initiatorPartnerKey,String responderPartnerKey, 
    		boolean isMsgRedelivered, String jmsMsgID) throws Throwable{
        Logger.log("[GWFReceiveDocumentMDBean.receiveDocument] received document, documentId="+documentId+", documentType="+documentType+", documentObject="+documentObject+", senderKey="+senderKey);

        // senderKey will be null if the document is originated from the same engine
        if(senderKey!=null && senderKey.trim().length()==0)
            senderKey=null;

        // document id will be null only for the first time on requesting side
        // if that document is ment to trigger the process
        GWFRtProcessDoc rtProcessDoc=null;
        if(documentId!=null){
            rtProcessDoc=BpssBusinessTransHelper.getActiveRtProcessDoc(documentId);
            if(rtProcessDoc==null){
                String roleType=IBpssConstants.INITIATING_ROLE;
                if(initiatorPartnerKey!=null && initiatorPartnerKey.equals(IBpssConstants.PARTNER_CONSTANT))
                    roleType=IBpssConstants.INITIATING_ROLE;
                else if(responderPartnerKey!=null && responderPartnerKey.equals(IBpssConstants.PARTNER_CONSTANT))
                    roleType=IBpssConstants.RESPONDING_ROLE;

                IDataFilter filter=new DataFilterImpl();
                filter.addSingleFilter(null,GWFRtProcessDoc.DOCUMENT_ID,filter.getEqualOperator(),documentId,false);
                filter.addSingleFilter(filter.getAndConnector(),GWFRtProcessDoc.ROLE_TYPE,filter.getEqualOperator(),roleType,false);
                Logger.debug("[GWFReceiveDocumentMDBean.receiveDocument] Looking for existing rtProcessDoc with documentId="+documentId+",roleType="+roleType);
                Collection rtProcessDocColl=UtilEntity.getEntityByFilter(filter,GWFRtProcessDoc.ENTITY_NAME,true);
                if(rtProcessDocColl!=null && rtProcessDocColl.size()>0){
                    for(Iterator iterator=rtProcessDocColl.iterator();iterator.hasNext();){
                        GWFRtProcessDoc tmpRtProcessDoc=(GWFRtProcessDoc)iterator.next();
                        if(tmpRtProcessDoc.getRequestDocType()!=null && tmpRtProcessDoc.getRequestDocType().equals(documentType)){
                            rtProcessDoc=tmpRtProcessDoc;
                            break;
                        } else if(tmpRtProcessDoc.getResponseDocTypes()!=null){
                            Map resTypeMap=UtilString.strToMap(tmpRtProcessDoc.getResponseDocTypes());
                            if(resTypeMap.get(documentType)!=null){
                                rtProcessDoc=tmpRtProcessDoc;
                                break;
                            }
                        }
                    }
                }
            }
        }

        //check for concurrent requestsDocs on responder side
        if(documentId!=null && rtProcessDoc==null){
            if(senderKey!=null && responderPartnerKey!=null && IBpssConstants.PARTNER_CONSTANT.equals(responderPartnerKey)){
                //means responder
                boolean isConcurrentRequest=BpssBusinessTransHelper.addConcurrentRequest(documentId,documentObject,senderKey, jmsMsgID); //15072008 CHANGE by TWX
                
                if(isConcurrentRequest) return;
            }
        }

        if(documentId==null || rtProcessDoc==null ){
            Logger.debug("[GWFReceiveDocumentMDBean.receiveDocument] new document , creating rtProcess , documentId="+documentId+",documentType="+documentType);
            //means there is no process for handling this document ,so start new process
            HashMap contextData=new HashMap();
            //if(senderKey!=null)
            //    contextData.put("workflow.control.partnerKey",senderKey);
            if(initiatorPartnerKey!=null)
                contextData.put("workflow.control.initiatorPartnerKey",initiatorPartnerKey);
            if(responderPartnerKey!=null)
                contextData.put("workflow.control.responderPartnerKey",responderPartnerKey);

            if(documentId!=null)
                contextData.put("workflow.control.documentId",documentId);
            contextData.put(documentType,documentObject);
            BpssDefinitionCache defCache=BpssDefinitionHelper.getBpssDefCacheFromDocumentType(documentType);
            contextData.put("workflow.control.defCacheKey",defCache.getCacheKey());
            String processDefinitionKey=BpssKeyHelper.getProcessDefinitionKey(documentType,defCache.getCacheKey());
            WorkflowUtil.getWorkflowManager().createRtProcess(processDefinitionKey,null,contextData);
        } else {
            Logger.debug("[GWFReceiveDocumentMDBean.receiveDocument] existing document, documentId="+documentId+",documentType="+documentType);
            //means either requesting/responding activity is waiting for this document in BusinessTransaction
            String reqDocType=rtProcessDoc.getRequestDocType();
            String resDocTypes=rtProcessDoc.getResponseDocTypes();

            if(IBpssConstants.INITIATING_ROLE.equals(rtProcessDoc.getRoleType())){ //InitiatingRole

                if(reqDocType.equals(documentType)){ //means this is request document which has to be sent to responder
                    BpssBusinessTransHelper.performRequestingActivity(documentType,documentObject,rtProcessDoc);
                } else if(resDocTypes!=null){
                    Map resTypeMap=UtilString.strToMap(resDocTypes);
                    if(resTypeMap.get(documentType)!=null){ // means response document which is received from responder
                        BpssBusinessTransHelper.processResponseDocument(documentType,documentObject,rtProcessDoc,senderKey);
                    }
                }
            }else { //"RespondingRole"

                if(reqDocType.equals(documentType)){ //means request document which is received from requester
                  
                    //#2773 We only allow calling the receive of the incoming doc if the status of the BpssResBusinessActivity
                    //is not "Not Running". This prevent two thread concurrently update the same GWFActivity record. See
                    //mantis ticket for details.
                    if(rtProcessDoc.getRtBusinessTransactionUId()!=null && !isBpssResBizActivityNotRunning(rtProcessDoc))
                    {
                        BpssBusinessTransHelper.processRequestDocument(documentType,documentObject,rtProcessDoc,senderKey);
                    }
                    else
                    {
                      Logger.debug("Responding role= for docId="+rtProcessDoc.getDocumentId() +" rtBusinessTransUid = "+rtProcessDoc.getRtBusinessTransactionUId());    
                      BpssBusinessTransHelper.addConcurrentRequest(documentId,documentObject,senderKey, jmsMsgID); //15072008 CHANGE by TWX
                    }
                } else if(resDocTypes!=null){
                    Map resTypeMap=UtilString.strToMap(resDocTypes);
                    if(resTypeMap.get(documentType)!=null){ // means this is response which has to be sent to requester
                        if(rtProcessDoc.getStatus()!=null &&
                            ( rtProcessDoc.getStatus().intValue()==GWFRtProcess.CLOSED_COMPLETED ||
                              rtProcessDoc.getStatus().intValue()==GWFRtProcess.CLOSED_ABNORMALCOMPLETED ||
                              rtProcessDoc.getStatus().intValue()==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED ||
                              rtProcessDoc.getStatus().intValue()==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED ) ) {
                              //if it is already completed dont do any thing,log it
                              Logger.debug("[GWFReceiveDocumentMDBean.receiveDocument] Process already completed ,documentId="+documentId+", documentType="+documentType+", senderKey="+senderKey);
                        } else BpssBusinessTransHelper.performRespondingActivity(documentType,documentObject,rtProcessDoc);
                    }
                }
            }
        }
    }

    //TWX: #2773: receiveDocument, activityQueue built up and causing backlog
    //The following query will cause the thread to wait if there is other thread is updating the entity and
    //not yet commited.
    private boolean isBpssResBizActivityNotRunning(GWFRtProcessDoc rtProcessDoc) throws Throwable
    {
      long bpssBizTransUid = rtProcessDoc.getRtBusinessTransactionUId();
      GWFRtProcess rtBusinessTrans =
        (GWFRtProcess) UtilEntity.getEntityByKey(bpssBizTransUid,GWFRtProcess.ENTITY_NAME,true);
      
      //Thread may suspend if other Thread is updating the same RtActivity entity
      Logger.debug("isBpssResBizActivityNotRunning: Getting GWFRTActivity given GWFRtProcess uid="+rtBusinessTrans.getUId());
      
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null,GWFRtActivity.RT_PROCESS_UID,filter.getEqualOperator(),rtBusinessTrans.getUId(),false);
      Collection coll = UtilEntity.getEntityByFilter(filter, GWFRtActivity.ENTITY_NAME, true);
      if(coll != null && coll.size() > 0)
      {
        GWFRtActivity rtActivity = (GWFRtActivity) coll.iterator().next();
        Logger.debug("isBpssResBizActivityNotRunning status="+rtActivity.getState());
        
        return Integer.valueOf(GWFRtActivity.OPEN_NOTRUNNING).equals(rtActivity.getState());
      }
      else
      {
        Logger.debug("isBpssResBizActivityNotRunning GWFRtProcess with uid= "+rtBusinessTrans.getUId()+" related rtActivity can not be found!");
        return true; //other thread (GWFActivityMDBean) may in the process of creating the GWFActivity entity; hence the status we treat as NotRunning
      }
      
    }
}