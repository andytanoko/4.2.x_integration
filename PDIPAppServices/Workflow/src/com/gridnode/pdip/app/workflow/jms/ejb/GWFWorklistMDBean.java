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
 * Oct 11 2005    Neo Sok Lay         Change for J2EE compliance
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
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
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

public class GWFWorklistMDBean implements MessageDrivenBean,MessageListener {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5388883633085755272L;
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
              Logger.log("[GWFWorklistMDBean.onMessage] Redelivered msg found. Message: "+msg);
            } 
          
            ObjectMessage objMsg = (ObjectMessage) msg;
            Map mapMsg=(Map)objMsg.getObject();

            Long rtActivityUId=(Long)mapMsg.get(IWorkflowConstants.RTACTIVITY_UID);
            String processDefKey = (String)  mapMsg.get(IWorkflowConstants.PROCESSDEF_KEY);
            String engineType = processDefKey.indexOf(IWorkflowConstants.XPDL_ENGINE)>-1?IWorkflowConstants.XPDL_ENGINE:IWorkflowConstants.BPSS_ENGINE;
            WorkflowUtil.getWorkflowManager().worklistCallback(rtActivityUId,GWFRtActivity.OPEN_RUNNING,engineType);
            
        } catch (JMSException e) {
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFWorklistMDBean.onMessage] JMSException",e);
        } catch (Throwable th) {
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFWorklistMDBean.onMessage] Exception: "+th.getLocalizedMessage(),th);
        }

    }

}
