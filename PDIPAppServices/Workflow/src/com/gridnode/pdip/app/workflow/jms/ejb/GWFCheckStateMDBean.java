/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 05 2002   Mahesh              Created
 *
 */
package com.gridnode.pdip.app.workflow.jms.ejb;


import com.gridnode.pdip.app.workflow.util.*;
import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.base.time.facade.ejb.*;
import com.gridnode.pdip.app.workflow.engine.*;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.xpdl.XpdlWorkflowEngine;

public class GWFCheckStateMDBean extends TimeInvokeMDBean {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2235608529663402280L;

		public void ejbCreate() {
    }

    protected void invoke(AlarmInfo info) {
        super.invoke(info);
        try {
            Logger.debug("[GWFCheckStateMDBean.invoke] category="+info.getCategory()+", senderKey="+info.getSenderKey()+", receiverKey"+info.getReceiverKey());
            if(IWorkflowConstants.XPDL_ENGINE.equals(info.getCategory()))
              XpdlWorkflowEngine.getInstance().checkTimeToPerform(info.getSenderKey(),info.getReceiverKey());
            else GWFFactory.getActivityEngine(info.getCategory()).checkTimeToPerform(info.getSenderKey(),info.getReceiverKey());
        } catch (Throwable th) {
            Logger.error(ILogErrorCodes.WORKFLOW_INVOKE,
                         "[GWFCheckStateMDBean.invoke] Exception: "+th.getMessage(), th);
        }
    }

}
