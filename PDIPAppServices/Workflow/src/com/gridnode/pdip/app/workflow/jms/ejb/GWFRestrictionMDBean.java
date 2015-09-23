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
 *                                    Use ServiceLocator instead of ServiceLookup.
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

import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFRestrictionManagerHome;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFRestrictionManagerObj;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GWFRestrictionMDBean implements MessageDrivenBean ,MessageListener {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7635240903439655004L;

		private MessageDrivenContext m_context;

    private IGWFRestrictionManagerHome restrictionHome = null;

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
        try {
            restrictionHome=(IGWFRestrictionManagerHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(IGWFRestrictionManagerHome.class);
        } catch (Exception e) {
            Logger.warn("[GWFRestrictionMDBean.ejbCreate] Exception",e);
        }
    }

    // Implementation of MessageListener
    public void onMessage(Message msg) {
    	
    	String jmsMessageID = "";  
        try {        	
            if(msg.getJMSRedelivered())
            {
              Logger.log("[GWFRestrictionMDBean.onMessage] Redelivered msg found. Message: "+msg);
            }
            
            jmsMessageID = msg.getJMSMessageID();
            ObjectMessage objMsg = (ObjectMessage) msg;
            Map mapMsg=(Map)objMsg.getObject();

            int eventId = ((Integer) mapMsg.get(IGWFRouteDispatcher.EVENT_ID)).intValue();
            IGWFRestrictionManagerObj restrictionObject =restrictionHome.create();
            Logger.debug("[GWFRestrictionMDBean.onMessage] eventId="+eventId);
            switch (eventId) {
                case IGWFRouteDispatcher.ROUTE_EVENT:
                    restrictionObject.selectRestrictionRoute((Long) mapMsg.get(IGWFRouteDispatcher.RTRESTRICTION_UID), (String)mapMsg.get(IGWFRouteDispatcher.ENGINE_TYPE));
                    break;
                default: throw new Exception("[GWFRestrictionMDBean.onMessage] Invalid eventId, eventId="+eventId);
            }
            
        } catch (JMSException e) {
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFRestrictionMDBean.onMessage] JMSException",e);
        } catch (Throwable th) {
        	
        	if(JMSRedeliveredHandler.isRedeliverableException(th)) //#69 25072008 TWX
        	{
        		Logger.warn("[GWFRestrictionMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
        		m_context.setRollbackOnly();
        	}        	
            Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                         "[GWFRestrictionMDBean.onMessage] Exception "+th.getLocalizedMessage(),th);
        }

    }

}
