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
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Rollback the entire transaction if encounter retryable exception.
 */
package com.gridnode.pdip.app.workflow.jms.ejb;

import java.util.Map;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFProcessManagerObj;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.xpdl.XpdlWorkflowEngine;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

public class GWFProcessMDBean implements MessageDrivenBean, MessageListener
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -1372888976620385644L;
  private MessageDrivenContext m_context;

  public void ejbRemove()
  {
  }

  /**
   * Sets the session context.
   *
   * @param ctx       MessageDrivenContext Context for session
   */
  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    m_context = ctx;
  }

  public void ejbCreate()
  {
  }

  // Implementation of MessageListener
  public void onMessage(Message msg)
  {
    Map mapMsg = null;
    String jmsMessageID = "";  
    
    try
    {      
      if(msg.getJMSRedelivered())
      {
        Logger.log("[GWFProcessMDBean.onMessage] Redelivered msg found. Message: "+msg);
      }
      
      jmsMessageID = msg.getJMSMessageID();
      ObjectMessage objMsg = (ObjectMessage) msg;
      mapMsg = (Map) objMsg.getObject();

      int eventId = ((Integer) mapMsg.get(IGWFRouteDispatcher.EVENT_ID)).intValue();
      String engineType = (String) mapMsg.get(IGWFRouteDispatcher.ENGINE_TYPE);
      IGWFProcessManagerObj processRemote = WorkflowUtil.getProcessManager();
      Logger.debug("[GWFProcessMDBean.onMessage] eventId=" + eventId);
      switch (eventId)
      {
        case IGWFRouteDispatcher.CREATE_EVENT :
          GWFRtProcess rtProcess = new GWFRtProcess();
          rtProcess.setProcessDefKey((String) mapMsg.get(IGWFRouteDispatcher.PROCESSDEF_KEY));
          rtProcess.setProcessUId((Long) mapMsg.get(IGWFRouteDispatcher.PROCESS_UID));
          rtProcess.setProcessType((String) mapMsg.get(IGWFRouteDispatcher.PROCESS_TYPE));
          rtProcess.setParentRtActivityUId((Long) mapMsg.get(IGWFRouteDispatcher.RTACTIVITY_UID));
          rtProcess.setContextUId((Long) mapMsg.get(IGWFRouteDispatcher.CONTEXT_UID));
          rtProcess.setEngineType((String) mapMsg.get(IGWFRouteDispatcher.ENGINE_TYPE));
          if (IWorkflowConstants.XPDL_ENGINE.equals(engineType))
            XpdlWorkflowEngine.getInstance().createRtProcess(rtProcess);
          else
            processRemote.createRtProcess(rtProcess);
          break;

        case IGWFRouteDispatcher.CHANGE_STATE_EVENT :
          processRemote.changeProcessState((Long) mapMsg.get(IGWFRouteDispatcher.RTPROCESS_UID), (Integer) mapMsg.get(IGWFRouteDispatcher.STATE));
          break;

        case IGWFRouteDispatcher.CANCEL_EVENT :
          processRemote.cancelRtProcess((Long) mapMsg.get(IGWFRouteDispatcher.RTPROCESS_UID), mapMsg.get(IGWFRouteDispatcher.REASON));
          break;

        case IGWFRouteDispatcher.REMOVE_EVENT :
          processRemote.removeRtProcess((Long) mapMsg.get(IGWFRouteDispatcher.RTPROCESS_UID));
          break;

        case IGWFRouteDispatcher.ROUTE_EVENT :
          processRemote.selectProcessRoute((Long) mapMsg.get(IGWFRouteDispatcher.RTPROCESS_UID));
          break;
        default :
          throw new Exception("[GWFProcessMDBean.onMessage] Invalid eventId, eventId=" + eventId);
      }
    }
    catch (Throwable th)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(th)) //#69 25072008 TWX
      {
    		Logger.warn("[GWFProcessMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
    		m_context.setRollbackOnly();
      }
      Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                   "[GWFProcessMDBean.onMessage] Exception ,msg=" + mapMsg, th);
    }
  }

}
