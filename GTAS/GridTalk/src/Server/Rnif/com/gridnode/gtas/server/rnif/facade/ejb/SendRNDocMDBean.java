/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendRNDocMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 29 2004    Neo Sok Lay         Handle resume send (NOT RE-SEND!!!)
 * Jul 01 2004    Neo Sok Lay         GNDB00025083: Check if process still 
 *                                    running before resume send.
 *                                    GNDB00025085: Check isInitiator() of
 *                                    process instead of isRequest() from gdoc
 *                                    -- isRequest does not match with ProcessMapping.
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69:1) Remove explicitly checked for redeliverd msg and dropped that msg.
 *                                        2) Rollback the entire transaction if encounter retryable exception.
 */
package com.gridnode.gtas.server.rnif.facade.ejb;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.notify.SendRNDocNotification;
import com.gridnode.gtas.server.rnif.helpers.*;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


public class SendRNDocMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1915378917221594129L;
	static final String LogCat = SendRNDocMDBean.class.getName();
  public MessageDrivenContext _mdx = null;

  /**
   * DOCUMENT ME!
   *
   * @param _ctx DOCUMENT ME!
   */
  public void setMessageDrivenContext(MessageDrivenContext _ctx)
  {
    this._mdx = _ctx;
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbCreate()
  {
    Logger.debug("SendRNDocMDBean is Created ");
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
    Logger.debug("SendRNDocMDBean Removed ");
  }

  /**
   * Creates a new SendRNDocMDBean object.
   */
  public SendRNDocMDBean()
  {
  }

  /**
   * DOCUMENT ME!
   *
   * @param l_message DOCUMENT ME!
   */
  public void onMessage(Message l_message)
  {
	String jmsMessageID = "";
    try
    {
      Logger.debug("SendRNDocMDBean Callback ");
      
      if(l_message.getJMSRedelivered())
      {
        Logger.log("[SendRNDocMDBean.onMessage] Redelivered msg found. Message: "+l_message);
      }
      
      jmsMessageID = l_message.getJMSMessageID();
      SendRNDocNotification message = (SendRNDocNotification)((ObjectMessage)l_message).getObject();
      GridDocument  gDoc= (GridDocument)message.getGdocs()[0];
      String   defName = message.getProcessDefId();
      Boolean   isRequest = message.getIsRequest();
      
      if (message.isResumeSend())
      {
        if (shouldResend(gDoc))
        {
          Boolean isInitiator = isInitiator(gDoc)?Boolean.TRUE:Boolean.FALSE; //check if isInitiator of process
          Logger.debug("[SendRNDocMDBean.onMessage] Call RNDocSender to resume send with gDoc="+gDoc +";defName=" + defName + ";isInitiator="+isInitiator);
           
          RNDocSender docSender= new RNDocSender();
          docSender.setSendingInfo(gDoc, defName, isInitiator);
          docSender.send(gDoc);
        }
        else
          Logger.debug("[SendRNDocMDBean.onMessage] Process "+gDoc.getProcessInstanceID()+" not running, not resume sending...");
      }
      else //???Will SignalDoc come to this stage???
      {
        Logger.debug("SendRNDocMDBean Call insertBizDocToSend2BPSS with gDoc="+gDoc +";defName=" + defName + ";isRequest="+isRequest);
        
        BpssInvoker.insertBizDocToSend2BPSS(gDoc, defName, isRequest);
      }
    }
    catch (Throwable ex)
    {
      if (JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
      	Logger.warn("[SendRNDocMDBean.onMessage] encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
    		_mdx.setRollbackOnly();
      }
      Logger.error(ILogErrorCodes.GT_SEND_RN_DOC_MDB,
                   "[SendRNDocMDBean.onMessage] Exception: "+ex.getMessage(), ex);
    }
  }
  
  private boolean shouldResend(GridDocument gdoc)
  {
    boolean should = false;
    try
    {
      Integer state = ProcessInstanceActionHelper.getProcessInstanceState(gdoc.getProcessInstanceUid());
      if (state != null)
      {
        switch (state.intValue())
        {
          case GWFRtProcess.OPEN_RUNNING:
            should = true;
            break;
          case GWFRtProcess.CLOSED_COMPLETED:
            //RN_ACK
            should = IRnifConstant.RN2_ACK.equals(gdoc.getUdocDocType()) ||
                     IRnifConstant.RN1_ACK.equals(gdoc.getUdocDocType());
            break;
          case GWFRtProcess.CLOSED_ABNORMALCOMPLETED:
          case GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED:
          case GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED:
            //RN_EXCEPTION
            should = IRnifConstant.RN1_EXCEPTION.equals(gdoc.getUdocDocType()) ||
                     IRnifConstant.RN2_EXCEPTION.equals(gdoc.getUdocDocType());
            break;
        }
      }
    }
    catch (Exception e)
    {
      Logger.warn("[SendRNDocMDBean.shouldResend] Error retrieving process state: ", e);
    }
    
    return should;
  }
  
  private boolean isInitiator(GridDocument gdoc) throws Exception
  {
    boolean initiator = false;
    RNProfile profile = new ProfileUtil().getProfileMustExist(gdoc);
    initiator = (IBpssConstants.PARTNER_CONSTANT.equals(profile.getProcessOriginatorId()));
    
    return initiator;
  }
}
