/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveRNDocMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 20 2002    Neo Sok Lay         Modify parameters to RNDocReceiver.receiveRNDoc().
 * Jan 26 3004    Neo Sok Lay         Check if transformationReq -- try to unpack
 *                                    the RBM here.
 * Mar 11 2004    Jagadeesh           Modified: To upload RNIF Document to GM.
 * Jan 26 2005    Mahesh              Modified to pass unpacked files insted of files from 
 *                                    message header to RNDocReceiver.receiveRNDoc
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * Jul 27, 2008   Tam Wei Xiang       #69: Remove explicitly check for redelivered jms msg.
 */
package com.gridnode.gtas.server.rnif.facade.ejb;

import java.io.File;
import java.util.Hashtable;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.helpers.IDocumentConstants;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.notify.ReceiveRNDocNotification;
import com.gridnode.gtas.server.rnif.helpers.BackwardCompatibleRNHandler;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.RNDocReceiver;
import com.gridnode.gtas.server.rnif.helpers.RelayRNHandler;
import com.gridnode.pdip.base.rnif.helper.IRNHeaderConstants;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;


public class ReceiveRNDocMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -435426307917996226L;
	static final String LogCat = ReceiveRNDocMDBean.class.getName();
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
    Logger.debug("ReceiveRNDocMDBean is Created ");
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
    Logger.debug("ReceiveRNDocMDBean Removed ");
  }

  /**
   * Creates a new SendRNDocMDBean object.
   */
  public ReceiveRNDocMDBean()
  {
  }

  /**
   * DOCUMENT ME!
   *
   * @param l_message DOCUMENT ME!
   */
  public void onMessage(Message l_message)
  { 
    try
    {
      Logger.debug("ReceiveRNDocMDBean Callback ");
      if(l_message.getJMSRedelivered())
      {
        Logger.log("[ReceiveRNDocMDBean.onMessage] Redelivered msg found. Message: "+l_message);
      }
      
      ReceiveRNDocNotification messge =(ReceiveRNDocNotification) ((ObjectMessage)l_message).getObject();
      File[] files = (File[])messge.getFilesReceived();
      Logger.debug("ReceiveRNDocMDBean Call RNDocReceiver with Files of" + files[0].getAbsolutePath()+";"+files[1].getAbsolutePath());

      //040126NSL: unpack the RBM here since the Channel could not handle it
      /*if (messge.isTransformationRequired())
      {
        files = BackwardCompatibleRNHandler.unpack(files);
      }
      */

     //040126NSL: unpack the RBM here since the Channel could not handle it

      Logger.log("[ReceiveRNDocMDBean.onMessage() B4 calling preProcessing Route_Lvl=]"+messge.getFromRoute());
      
      File resFiles[][] = perProcessReceiveMessage(messge);
      Hashtable additionalHeader = new Hashtable();
      if(resFiles[1].length>0)
      {
        File auditFile = (File) resFiles[1][0];
        if(auditFile!=null)
        {
          additionalHeader.put(IRNHeaderConstants.AUDIT_FILE_NAME,auditFile.getName());
        }
      }
      
      //25012005 Mahesh : Added SIGNATURE_VERIFY_EXCEPTION to additional header
      if(messge.getSigVerifyExMsg()!=null)
        additionalHeader.put(IRNHeaderConstants.SIGNATURE_VERIFY_EXCEPTION,messge.getSigVerifyExMsg());
      
      //26012005 Mahesh : Modified to pass unpacked files insted of files from message header 
      //RNDocReceiver.receiveRNDoc(messge.getHeader(), messge.getDataReceived(), files, (GridDocument)messge.getReceivedGdoc(),additionalHeader);
      RNDocReceiver.receiveRNDoc(messge.getHeader(), messge.getDataReceived(), resFiles[0], (GridDocument)messge.getReceivedGdoc(),additionalHeader);
      
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_RECEIVE_RN_DOC_MDB,
                   "[ReceiveRNDocMDBean.onMessage] Exception: "+ex.getMessage(), ex);
    }
  }

  private File[][] perProcessReceiveMessage(ReceiveRNDocNotification message)
      throws Exception
  {
    File[] files = (File[])message.getFilesReceived();
    File resFiles[][]=null;
    Logger.log("[ReceiveRNDocMDBean.perProcessReceiveMessage()][Message Route Level=]"+message.getFromRoute());

    if (IDocumentConstants.ROUTE_GT1_GM == message.getFromRoute()) //From GT1...
    {
      Logger.log("[ReceiveRNDocMDBean.perProcessReceiveMessage()][Calling BackwardCompatibleRNHandler]"+
                   message.getFromRoute());
      resFiles = BackwardCompatibleRNHandler.unpack(files);
      //resFiles[0,0]: RNPackInfo file, [0,1]: unpacked Udoc file, [0,2~]:Attachments, [1,0]:audit file
    }
    if (IDocumentConstants.ROUTE_GM == message.getFromRoute()) //From GT2-GM-GT2
    {
      Logger.log("[ReceiveRNDocMDBean.perProcessReceiveMessage()][Calling RelayRNHandler]"+message.getFromRoute());
      resFiles = RelayRNHandler.unpack(files);
      //resFiles[0,0]: RNPackInfo file, [0,1]: unpacked Udoc file, [0,2~]:Attachments, [1,0]:audit file
    }
    if(resFiles==null)
    {
      resFiles=new File[][] {files,{null}};
      Logger.log("[ReceiveRNDocMDBean.perProcessReceiveMessage()][setting null for audit file] "+message.getFromRoute());
    }
      
    for (int i=0;i<resFiles[0].length;i++)
      Logger.debug("ReceiveRNDocMDBean.perProcessReceiveMessage() After perProcess Files after unPack are]["+i+"]" + resFiles[0][i].getAbsolutePath());
    return resFiles;
  }

}
