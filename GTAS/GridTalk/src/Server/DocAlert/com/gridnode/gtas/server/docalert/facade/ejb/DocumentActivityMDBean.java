/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentActivityMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 04 2002    Neo Sok Lay         Created
 * Jun 20 2003    Neo Sok Lay         To raise DOCUMENT_STATUS_UPDATE alert
 *                                    when notification of type: END_SEND, END_TRANS,
 *                                    REJECT_TRANS.
 * Nov 23 2005    Neo Sok Lay         Change interface in DocAlertManager                                  
 */
package com.gridnode.gtas.server.docalert.facade.ejb;

import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.gtas.server.docalert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.notify.DocumentActivityNotification;
import com.gridnode.gtas.server.notify.IAlertTypes;

import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * This MDBean listens to the Notifier topic for notifications when status
 * has changed for a document, particularly the DocumentActivityNotification(s).<p>
 * This MDBean handles the following status type:<p>
 * <OL>
 *   <LI>START_SEND - start the document response tracking for the document.</LI>
 *   <LI>START_SEND, END_SEND, END_TRANS, REJECT_TRANS - Raise DOCUMENT_STATUS_UPDATE
 *       alert type.</LI>
 * </OL>
 * @author Neo Sok Lay
 *
 * @version 2.1 I1
 * @since 2.0 I7
 */
public class DocumentActivityMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5304104292448636029L;
	private MessageDrivenContext _mdx = null;

  public DocumentActivityMDBean()
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getDocSendFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);
      
      DocumentActivityNotification notification = (DocumentActivityNotification)((ObjectMessage)message).getObject();

      //Don't check here
      //if (notification.getType() == DocumentActivityNotification.TYPE_START_SEND)
      handleNotification(notification);
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the DocumentActivityNotification.<p>
   * On Start Sending of document, document response tracking will be 
   * started for the document that is being sent (as per configuration).
   * On all occasions, a DOCUMENT_STATUS_UPDATE alert type will be triggered.
   */
  private void handleNotification(DocumentActivityNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getDocSendFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      IDocAlertManagerObj mgr = ServiceLookupHelper.getDocAlertMgr();
      GridDocument gdoc = (GridDocument)notification.getDocument();
      
      if (notification.getType() == DocumentActivityNotification.TYPE_START_SEND)
        mgr.startResponseTracking(gdoc);
          
      // raise DOCUMENT_STATUS_UPDATE alert
      mgr.raiseDocAlert(IAlertTypes.DOCUMENT_STATUS_UPDATE,
                        gdoc,
                        (IDataProvider[])null);    //NSL20051123
    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }
}