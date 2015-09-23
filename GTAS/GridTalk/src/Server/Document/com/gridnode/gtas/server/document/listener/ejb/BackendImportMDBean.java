/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendImportMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 22 2002    Koh Han Sing        Created
 * Jun 20 2003    Koh Han Sing        Add in unique document id.
 * 9 Dec 2005     SC                  Add support for process instance id.
 * Nov 10 2006    Tam Wei Xiang       Added tracingID (for use in Audit-Trail).
 * Mar 15 2007    Neo Sok Lay         To call doc mgr direct if clientctrl not specified.
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */
package com.gridnode.gtas.server.document.listener.ejb;

import java.util.ArrayList;

import javax.ejb.EJBException;
import javax.ejb.Handle;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.events.document.ImportBackendDocumentEvent;
import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.notify.ImportBackendDocNotification;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This message driven bean is used to perform importing of files from
 * the backend system
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class BackendImportMDBean
       implements MessageDrivenBean, MessageListener
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 7151349644553251780L;
  private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message msg)
  {
    Logger.log("[BackendImportMDBean.onMessage] Enter");
    try
    {
      if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
      {
        Logger.log("[BackendImportMDBean.onMessage] Redelivered msg found, ignored it. Message: "+msg);
        return;
      }
      
      ImportBackendDocNotification noti = (ImportBackendDocNotification)((ObjectMessage)msg).getObject();
      ArrayList inputFilenames = noti.getInputFilenames();
      Logger.log("[BackendImportMDBean.onMessage] inputFilenames ="+inputFilenames);
      ArrayList attFilenames = noti.getAttachmentFilenames();
      ArrayList partners = noti.getPartners();
      String bizEntId = noti.getBizEntId();
      String docType = noti.getDocType();
      String rnProfile = noti.getRnProfile();
      String uniqueDocId = noti.getUniqueDocId();
      String processInstanceId = noti.getProcessInstanceId();
      String tracingID = noti.getTracingID(); //TWX 10112006
      
      //SC LOG
      log("processInstanceId = " + processInstanceId+" tracingID "+tracingID);
      
      Handle ejbClientCntHandle = noti.getEjbClientCntHandle();
      if (ejbClientCntHandle == null)
      {
        String userId = noti.getUserId();
        String userName = noti.getUserName();
        String pathKey = noti.getSourcePathKey();
        String subPath = noti.getSourceSubPath();
        
        getDocumentManager().importDocument(userId, userName, null, bizEntId, partners, docType, inputFilenames, attFilenames, 
                                            pathKey, subPath, rnProfile, uniqueDocId, processInstanceId, tracingID);
      }
      else
      {
        IGridTalkClientControllerObj cnt = (IGridTalkClientControllerObj)ejbClientCntHandle.getEJBObject();
  
        ImportBackendDocumentEvent event = new ImportBackendDocumentEvent(
                                                 bizEntId,
                                                 docType,
                                                 inputFilenames,
                                                 partners,
                                                 attFilenames,
                                                 rnProfile,
                                                 uniqueDocId, 
                                                 processInstanceId,
                                                 tracingID);
        cnt.processEvent(event);
      }
    }
    catch (EJBException ejbEx)
    {
      Logger.error(ILogErrorCodes.GT_BACKEND_IMPORT_MDB,
                   "[BackendImportMDBean.onMessage] EJB Exception: "+ejbEx.getMessage(), ejbEx);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_BACKEND_IMPORT_MDB,
                   "[BackendImportMDBean.onMessage] Exception: "+ex.getMessage(), ex);
    }
    Logger.log("[BackendImportMDBean.onMessage] End");
  }

  private IDocumentManagerObj getDocumentManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  //SC LOG
  private void log(String message)
  {
  	Logger.log("[BackendImportMDBean] " + message);
  }
}
