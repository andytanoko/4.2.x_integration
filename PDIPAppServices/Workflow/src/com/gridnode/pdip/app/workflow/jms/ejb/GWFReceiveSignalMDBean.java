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


import java.util.Map;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssBusinessTransHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

public class GWFReceiveSignalMDBean implements MessageDrivenBean ,MessageListener{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -398705240955339892L;
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
              Logger.log("[GWFReceiveSignalMDBean.onMessage] Redelivered msg found. Message: "+msg);
            }
            
            jmsMessageID = msg.getJMSMessageID();
            ObjectMessage objMsg = (ObjectMessage) msg;
            Map mapMsg=(Map)objMsg.getObject();

            String signalType=(String)mapMsg.get(IBpssConstants.SIGNAL_TYPE);
            Object reason=mapMsg.get(IBpssConstants.REASON);
            String documentId=(String)mapMsg.get(IBpssConstants.DOCUMENTID);
            String senderKey=(String)mapMsg.get(IBpssConstants.SENDER_KEY);
            BpssBusinessTransHelper.receivedSignal(signalType,reason,documentId,senderKey);
            
        } catch (JMSException e) {
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFReceiveSignalMDBean.onMessage] JMSException",e);
        } catch (Throwable th) {
        	
            if(JMSRedeliveredHandler.isRedeliverableException(th)) //#69 25072008 TWX
            {
          		Logger.warn("[GWFReceiveSignalMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
          		m_context.setRollbackOnly();
            }
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFReceiveSignalMDBean.onMessage] Exception ",th);
        }
    }

}