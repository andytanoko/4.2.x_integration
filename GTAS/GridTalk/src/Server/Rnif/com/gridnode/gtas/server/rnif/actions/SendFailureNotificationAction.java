/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendFailureNotificationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 19, 2006   i00107              Created
 * Feb 06 2006    Tam Wei Xiang       modified method getReceivedGDoc()
 * Feb 08 2006    i00107              Enhance to allow send NoF for process that
 *                                    has completed. Change getReceivedGDoc() to
 *                                    getOriginalGDoc().
 * Jun 16 2006    Tam Wei Xiang       change the method 
 *                                    sendFailureNotification(GridDocument, String)
 *                                    modifier from private to public.                                   
 */

package com.gridnode.gtas.server.rnif.actions;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.events.rnif.SendFailureNotificationEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.server.rnif.act.FailureNotificationAction;
import com.gridnode.gtas.server.rnif.act.FailureNotificationAction_11;
import com.gridnode.gtas.server.rnif.helpers.*;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * @author i00107
 * This action handles initiating of a Notification of Failure to the
 * partner of a particular process. This is used for cases whereby processing fails
 * after returning an RN_ACK to the partner for a received document, or after the process
 * has completed.
 * The process will be updated to fail state.
 * <p>
 * The difference between this SendFailureNotificationAction and CancelProcessInstanceAction
 * is that the latter is to be used when the user initiated a cancellation of a process, whereas
 * the former is used when the backend system discovered problem with a process and wants to fail/cancel
 * the process.
 */
public class SendFailureNotificationAction extends AbstractGridTalkAction
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -9063658429704319880L;

  /**
   * 
   */
  public SendFailureNotificationAction()
  {
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
   */
  @Override
  protected IEventResponse constructErrorResponse(IEvent event,
                                                  TypedException ex)
  {
    SendFailureNotificationEvent sendEvent = (SendFailureNotificationEvent)event;
    
    Object[] params = new Object[] {
        sendEvent.getProcessInstanceId(),
        sendEvent.getSenderDUNS(),
        sendEvent.getIsRequestMsg(),
        sendEvent.getReasonStr()
    };
    
    return constructEventResponse(IErrorCode.GENERAL_ERROR,  params, ex);

  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(com.gridnode.pdip.framework.rpf.event.IEvent)
   */
  @Override   
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    SendFailureNotificationEvent sendEvent = (SendFailureNotificationEvent)event;
    
    GridDocument originalGDoc = getOriginalGDoc(sendEvent.getProcessInstanceId(), sendEvent.getIsRequestMsg(), sendEvent.getSenderDUNS());
    if (originalGDoc == null)
    {
      logEventStatus(event, "Could not find the original business document");
    }
    else
    {
      sendFailureNotification(originalGDoc, sendEvent.getReasonStr());
    }
    
    return constructEventResponse();
  }

  private GridDocument getOriginalGDoc(String instId, boolean isRequestMsg, String senderDUNS) throws Exception
  {
    Collection gdocKeys = ProcessInstanceActionHelper.findBizDocKeys(instId, senderDUNS, isRequestMsg);
    if (gdocKeys == null || gdocKeys.isEmpty())
    {
      return null;
    }
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, gdocKeys, false);
    //NSL20060209 include all Rnif docs instead of just received Rnif doc
    ArrayList<String> folders = new ArrayList<String>();
    folders.add(GridDocument.FOLDER_INBOUND);
    folders.add(GridDocument.FOLDER_OUTBOUND);
    filter.addDomainFilter(filter.getAndConnector(), GridDocument.FOLDER, folders, false);
    //TWX 06022006 change the order to true
    filter.addOrderField(GridDocument.DT_SEND_START, true);
    filter.addOrderField(GridDocument.DT_RECEIVE_END, true);
    Collection gdocs = DocumentUtil.getDocumentManager().findGridDocuments(filter);
    if (gdocs != null && !gdocs.isEmpty())
    {
      return (GridDocument)gdocs.iterator().next();
    }
    return null;
  }
  
  public void sendFailureNotification(GridDocument originalGDoc, String reasonStr) throws Exception
  {
    String defName = originalGDoc.getProcessDefId();
    if (defName == null || defName.length()==0)
    {
      //not RN doc, no NoF required - precaution check
      logEventStatus(null, "Not a process-linked document. Weird finding...");
      return;
    }
    
    //NSL20060209 check if process already failed
    if (isProcessFailed(originalGDoc.getProcessInstanceUid()))
    {
      logEventStatus(null, "Process already failed.");
      return;
    }
    
    logEventStatus(null, "Cancelling the original process uid="+originalGDoc.getProcessInstanceUid());
    //cancel the original process
    ProcessInstanceActionHelper.cancelProcessInstance(originalGDoc.getProcessInstanceUid(), reasonStr);
   
    //Send NoF to partner
    ProcessDef def = ProcessUtil.getProcessDef(defName); //original process def
    String rnifVersion = def.getRNIFVersion();
    if (IRnifConstant.RN_FAILNOTIFY_DEFNAME20.equals(defName) ||
        IRnifConstant.RN_FAILNOTIFY_DEFNAME11.equals(defName))
    {
      //do not send the NoF for NoF - precaution check
      logEventStatus(null, "Fail for a NoF, not required to send NoF for NoF.");
    }
    else
    {
      logEventStatus(null, "Sending NoF for original doc uid="+originalGDoc.getUId());
      Object[] params = {reasonStr, 
          new Boolean(originalGDoc.getFolder().equals(GridDocument.FOLDER_OUTBOUND)), //NSL20060209 use the same sender & recipient role if outbound doc
          this.getUserID()};
      if ((rnifVersion != null)&&(rnifVersion.equals(ProcessDef.RNIF_1_1)))
      {
        def = ProcessUtil.getProcessDef(IRnifConstant.RN_FAILNOTIFY_DEFNAME11);
        FailureNotificationAction_11 action = new FailureNotificationAction_11();
        action.execute(originalGDoc, true, def, def.getRequestAct(), params);
      }
      else
      {
        def = ProcessUtil.getProcessDef(IRnifConstant.RN_FAILNOTIFY_DEFNAME20);
        FailureNotificationAction action = new FailureNotificationAction();
        action.execute(originalGDoc, true, def, def.getRequestAct(), params);
      }
      
      //TWX: 26012006raise alert while we send 0A1 to partner
      raiseProcessInstanceFailureAlert(originalGDoc, reasonStr);
    }

  }
  
  private boolean isProcessFailed(Long processInstanceUID)
  {
    boolean failed = true;
    Integer state = ProcessInstanceActionHelper.getProcessInstanceState(processInstanceUID);
    if (state != null) 
    {
      int intState = state.intValue();
      failed = (intState == GWFRtProcess.CLOSED_ABNORMALCOMPLETED) ||
                (intState == GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED) ||
                (intState == GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED);
    }
    //cannot find process (which should not happen after coming to this stage...)
    //is also treated as "failed", in which case no NoF will be sent out
    
    return failed;
  }
  
  private void raiseProcessInstanceFailureAlert(GridDocument originalDoc, String reasonStr)
  {
  	AlertUtil.alertFailureNotificationInitiated(originalDoc, reasonStr);
  }
  
  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   */
  @Override
  protected String getActionName()
  {
    return "SendFailureNotificationAction";
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   */
  @Override
  protected Class getExpectedEventClass()
  {
    return SendFailureNotificationEvent.class;
  }

}
