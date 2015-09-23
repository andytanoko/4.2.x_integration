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
 */
package com.gridnode.pdip.app.workflow.jms.ejb;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.workflow.adaptors.AppAdaptor;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.util.UtilString;

public class GWFSendSignalMDBean implements MessageDrivenBean ,MessageListener{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3319812022467384693L;
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
              Logger.log("[GWFSendSignalMDBean.onMessage] Redelivered msg found. Message: "+msg);
            } 
            
            jmsMessageID = msg.getJMSMessageID();
            ObjectMessage objMsg = (ObjectMessage) msg;
            Map mapMsg=(Map)objMsg.getObject();

            Long rtDocumentUId=(Long)mapMsg.get("rtDocumentUId");
            String documentId=(String)mapMsg.get(IBpssConstants.DOCUMENTID);
            String documentType=(String)mapMsg.get(IBpssConstants.DOCUMENT_TYPE);
            String signalType=(String)mapMsg.get(IBpssConstants.SIGNAL_TYPE);
            Object reason=mapMsg.get(IBpssConstants.REASON);
            String partnerKey=(String)mapMsg.get(IBpssConstants.PARTNER_KEY);
            Long contextUId=(Long)mapMsg.get("contextUId");
            sendSignal(signalType,reason,documentId,documentType,partnerKey,rtDocumentUId,contextUId);
        	
        } catch (JMSException e) {
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFSendSignalMDBean.onMessage] JMSException",e);
        } catch (Throwable th) {
        	
        	if(JMSRedeliveredHandler.isRedeliverableException(th)) //#69 25072008 TWX
        	{
        		Logger.warn("[GWFSendSignalMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
        		m_context.setRollbackOnly();
        	}
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFSendSignalMDBean.onMessage] Exception ",th);
        }
    }

    public void sendSignal(String signalType,Object reason,String documentId,String documentType,String partnerKey,Long rtDocumentUId,Long contextUId) throws Throwable
    {
        HashMap paramMap=new HashMap();
        try{
            Object documentObj=null;
            if(documentType!=null && documentType.trim().length()>0)
                documentObj=WorkflowUtil.getDataManager().getContextData(contextUId,new ContextKey(documentType));
            paramMap.put(IBpssConstants.DOCUMENTID,documentId);
            paramMap.put(IBpssConstants.DOCUMENT_TYPE,documentType);
            paramMap.put(IBpssConstants.SIGNAL_TYPE,signalType);
            paramMap.put(IBpssConstants.REASON,reason);
            paramMap.put(IBpssConstants.PARTNER_KEY,partnerKey);
            paramMap.put(IBpssConstants.DOCUMENT_OBJECT,documentObj);
            Properties prop=WorkflowUtil.getProperties(IWorkflowConstants.CONFIG_WORKFLOW_SENDSIGNAL_APP);
            if(prop==null || prop.size()==0)
                throw new WorkflowException("App properties not found");
            AppAdaptor.callApp(prop,paramMap,null);
        }catch(Throwable th){
            Logger.error(ILogErrorCodes.WORKFLOW_SEND_SINGAL,
                         "[GWFSendSignalMDBean.sendSignal] Exception: "+th.getMessage(),th);
            throw th;
        }
    }
}