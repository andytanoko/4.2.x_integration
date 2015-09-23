/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentFlowNotifyHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       Changed the documentFlow type in str 
 *                                     to EDocumentFlowType.
 * Apr 10, 2008    Tam Wei Xiang       #17: The determination of the actual folder
 *                                     has caused the MappingRule (HeaderTransformation)
 *                                     failed.
 * Jul 25, 2008     Tam Wei Xiang        #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.
 */
package com.gridnode.pdip.framework.notification;

import java.util.Date;
import java.util.List;

import javax.ejb.TransactionRolledbackLocalException;
import javax.transaction.TransactionRolledbackException;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.log.ILog;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class is reponsible to trigger the status of the activity that a transaction doc has gone through. 
 * (EG we received it from Partner, perform unpackaging etc)
 * 
 * Differenct variant of trigger method for triggering the status have been provided.  
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class DocumentFlowNotifyHandler
{
  
  private static String LOG_CATEGORY = ILog.NOTIFIER;
  
  /**
   * Return a DocumentFlowNotification instance
   * @return
   */
  private static DocumentFlowNotification createDocFlowNotification(EDocumentFlowType docFlowType, Date occurTime,
                                                                   boolean isSuccess, String errorReason,
                                                                   String msgType,
                                                                   String tracingID, byte[] data, String filePath, boolean isRequiredPack,
                                                                   String beID, String msgID)
  {
    List attachmentUIDs = null;
    return new DocumentFlowNotification(docFlowType, occurTime, msgID, isSuccess, errorReason,
                                        tracingID, msgType, data, beID, attachmentUIDs, filePath, isRequiredPack, "");
  }
  
  /**
   * Return a DocumentFlowNotification instance
   * @return
   */
  private static DocumentFlowNotification createDocFlowNotification(EDocumentFlowType docFlowType, Date eventOccurTime,
                                                                   String messageID, boolean isSuccess, String errorReason,
                                                                   String tracingID, String msgType, String beID,
                                                                   String docFlowAddInfo)
  {
    byte[] docInByte = null;
    List attachmentUIDs = null;
    String filePath = "";
    boolean isRequiredPack = false;
    return new DocumentFlowNotification(docFlowType, eventOccurTime, messageID, isSuccess,
                                        errorReason, tracingID, msgType, docInByte, beID, attachmentUIDs, filePath,
                                        isRequiredPack, docFlowAddInfo);
  }
  
  /**
   * Return a DocumentFlowNotification instance
   * @return
   */
  private static DocumentFlowNotification createDocFlowNotification(EDocumentFlowType docFlowType, Date eventOccurTime,
                                                                   String messageID, boolean isSuccess, String errorReason,
                                                                   String tracingID, String msgType, String filePath,
                                                                   String beID, List attachmentUIDs, boolean isRequiredPack,
                                                                   String docFlowAddInfo)
  {
    byte[] docInByte = null;
    
    return new DocumentFlowNotification(docFlowType, eventOccurTime, messageID, isSuccess,
                                        errorReason, tracingID, msgType, docInByte, beID, attachmentUIDs, filePath,
                                        isRequiredPack, docFlowAddInfo);
  }
  
  /**
   * Trigger the status of the activity that the transaction doc has gone through. 
   * @param docFlowType The document flow type
   * @param eventOccurTime The time that the business transaction has gone through the flow.
   * @param folder The GridDocument's folder
   * @param gdocID The GridDocument's gdocID
   * @param isSuccess To indicate whether the execution of the activity is successful or not.
   * @param errorReason To indicate the reason that the activity has fail to perform. (if isSuccess is false)
   * @param tracingID The unique identifier that can link up all the activities that we have performed on the transaction docs
   * @param msgType The gdoc's udocType
   * @param senderBizEntityID The gdoc's senderBizEntityID
   * @param recepientBizEntityID The gdoc's recepientBizEntityID
   * @param docFlowAddInfo To include additional information about the docFlowType. Eg we can include
   *                       the port name for the docFlowType IDocumentFlow.DOCUMENT_EXPORT
   * @param gdocUID gdoc entity PK.
   * @param srcFolder gdoc's srcFolder                      
   */
  public static void triggerNotification(EDocumentFlowType docFlowType, Date eventOccurTime,
                                          String folder, Long gdocID, boolean isSuccess, String errorReason,
                                          String tracingID, String msgType, String senderBizEntityID, String recepientBizEntityID,
                                          String docFlowAddInfo, Long gdocUID, String srcFolder,
                                          Throwable th) throws SystemException
  {
    if(assertTracingID(tracingID))
    {
      String beID = getOwnBEID(folder, recepientBizEntityID, senderBizEntityID, srcFolder);
      
      folder = getActualFolder(gdocUID, folder, srcFolder);
      String messageID = getMessageID(folder, gdocID);
      DocumentFlowNotification notification = createDocFlowNotification(docFlowType, eventOccurTime,
                                                                     messageID, isSuccess, errorReason, tracingID,
                                                                     msgType, beID, docFlowAddInfo);
      broadCastNotification(notification, th);
    }
  }
  
  /**
   * Trigger the status of the activity that the transaction doc has gone through.
   * @param docFlowType The document flow type
   * @param occurTime The time that the business transaction has gone through the flow.
   * @param isSuccess To indicate whether the execution of the activity is successful or not.
   * @param errorReason To indicate the reason that the activity has fail to perform. (if isSuccess is false)
   * @param tracingID The unique identifier that can link up all the activities that we have performed on the transaction docs
   * @param data The data content that we will sent over to OTC.
   * @param filePath The File that we will sent over to OTC.
   * @param isRequiredPack Indicate whether we need to pack the payload into Mime format
   * @param beID customer's own beID
   */
  public static void triggerNotification(EDocumentFlowType docFlowType, Date occurTime,
                                         boolean isSuccess, String errorReason,
                                         String tracingID, byte[] data, String filePath, boolean isRequiredPack,
                                         String beID, Throwable th) throws SystemException
  {
    if(assertTracingID(tracingID))
    {
      String msgType = "";
      DocumentFlowNotification notification = createDocFlowNotification(docFlowType, occurTime,
                                                                      isSuccess, errorReason,msgType,
                                                                      tracingID, data, filePath, isRequiredPack, beID, null);
      broadCastNotification(notification, th);
    }
  }
  
  public static void triggerNotification(EDocumentFlowType docFlowType, Date occurTime,
                                         boolean isSuccess, String errorReason,
                                         String tracingID, byte[] data, String filePath, boolean isRequiredPack,String folder, String gdocID,
                                         Throwable th) throws SystemException
  {
    if(assertTracingID(tracingID))
    {
      String msgType = "";
      String beID = "";
      Long gdocIdentifier = (gdocID == null || "".equals(gdocID)) ? null : new Long(gdocID);
      String msgID = getMessageID(folder, gdocIdentifier);
      DocumentFlowNotification notification = createDocFlowNotification(docFlowType, occurTime,
                                                                      isSuccess, errorReason,msgType,
                                                                      tracingID, data, filePath, isRequiredPack,beID, msgID);
      broadCastNotification(notification, th);
    }
  }
  
  /**
   * Trigger the status of the activity that the transaction doc has gone through.
   * @param docFlowType The document flow type
   * @param eventOccurTime The time that the business transaction has gone through the flow.
   * @param folder The GridDocument's folder
   * @param gdocID The GridDocument's gdocID
   * @param isSuccess To indicate whether the execution of the activity is successful or not.
   * @param errorReason To indicate the reason that the activity has fail to perform. (if isSuccess is false)
   * @param tracingID The unique identifier that can link up all the activities that we have performed on the transaction docs
   * @param msgType The gdoc's udocType
   * @param filePath The File that we will sent over to OTC.
   * @param recipientBEID The gdoc's recepientBizEntityID
   * @param senderBEID The gdoc's senderBizEntityID
   * @param attachmentUID The list of attachment, which identified by the UID, that need to sent over to OTC
   * @param docFlowAddInfo To include additional information about the docFlowType. Eg we can include
   *                       the port name for the docFlowType IDocumentFlow.DOCUMENT_EXPORT
   * @param gdocUID The PK of the gridDocument entity
   * @param srcFolder The src folder of the gdoc.                      
   */
  public static void triggerNotification(EDocumentFlowType docFlowType, Date eventOccurTime,
                                         String folder, Long gdocID, boolean isSuccess, String errorReason,
                                         String tracingID, String msgType, String filePath,
                                         String recipientBEID, String senderBEID, List attachmentUID, boolean isRequiredPack,
                                         String docFlowAddInfo, Long gdocUID, String srcFolder,
                                         Throwable th) throws SystemException
  {
  	String mn = "triggerNotification";
    if(assertTracingID(tracingID))
    {
      String beID = getOwnBEID(folder, recipientBEID, senderBEID, srcFolder);
      
      folder = getActualFolder(gdocUID, folder, srcFolder);
      String messageID = getMessageID(folder, gdocID);
      
    
        DocumentFlowNotification notification = createDocFlowNotification(docFlowType, eventOccurTime,
                                                                      messageID, isSuccess, errorReason,
                                                                      tracingID, msgType, filePath,
                                                                      beID, attachmentUID, isRequiredPack, docFlowAddInfo);
        broadCastNotification(notification, th);

    }
  }
  
  private static void broadCastNotification(DocumentFlowNotification notification, Throwable th) throws SystemException
  {
  	String mn = "broadCastNotification";
    try
    {
      if(JMSRetrySender.isSendViaDefMode())
      {
      	if(isTransactionRollback(th)) //If the exception indicated the transaction is rolling back, we need to fire some 
        {                             //TXMR event under new transaction since user need to keep track what is happening.
      		logWarn(mn, "Transaction Rollback detected, fire event via new transaction", null);
      		Notifier.getInstance().broadcastInNewTrans(notification);
        }
      	else
      	{
      		Notifier.getInstance().broadcast(notification);
      	}
      }
      else
      {
      	if(isTransactionRollback(th))
        {
      		logWarn(mn, "Transaction Rollback detected, fire event via new transaction", null);
      		JMSRetrySender.broadcastInNewTrans(notification);
        }
      	else
      	{
      		JMSRetrySender.broadcast(notification);
      	}
      }
    }
    catch(Throwable ex)
    {
    	if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
        {
          logWarn(mn, "Error in broadcasting the TXMR DocumentFlow event", ex);
      	  throw new SystemException("Error in broadcasting the TXMR DocumentFlow event "+notification, ex);
        }
        logError(ILogErrorCodes.NOTIFICATION_GENERIC, mn, "Failed to trigger DocumentFlow notification"+ notification+". Error: "+ex.getMessage(), ex);
    }
  }
  
  /**
   * TWX 31102006 Get the MessageID given the gdoc.
   * @param gdoc
   * @return the concatenate str of gdoc folder(short form) and gdocID
   */
  private static String getMessageID(String folder, Long gdocID)
  {
    if(gdocID == null || "".equals(gdocID))
    {
      throw new IllegalArgumentException("[DocumentFlowNotifyHelper.getMessageID] GDOC ID is expected. gdocID is null or empty string !");
    }
    
    String folderAbbreviate = "";
    if("Inbound".equals(folder))
    {
      folderAbbreviate = "IB";
    }
    else if("Outbound".equals(folder))
    {
      folderAbbreviate = "OB";
    }
    else if("Export".equals(folder))
    {
      folderAbbreviate = "EP";
    }
    else if("Import".equals(folder))
    {
      folderAbbreviate = "IP";
    }
    else
    {
      throw new IllegalArgumentException("[DocumentFlowNotifyHelper.getMessageID] Folder "+folder+" is not supported !");
    }
    return folderAbbreviate+"-"+gdocID;
  }
  
  /**
   * 
   * @param folder
   * @param recipientBizEntityID
   * @param senderBizEntityID
   * @param srcFolder
   * @return
   */
  private static String getOwnBEID(String folder, String recipientBizEntityID, String senderBizEntityID, String srcFolder)
  {
    if("Inbound".equals(folder))
    {
      return recipientBizEntityID;
    }
    else if("Outbound".equals(folder))
    {
      return senderBizEntityID;
    }
    else if("Inbound".equals(srcFolder) && "Export".equals(folder)) // IB -> EP case
    {
      return recipientBizEntityID;
    }
    else if("Import".equals(srcFolder) && "Export".equals(folder)) //IP -> EP case
    {
      return senderBizEntityID;
    }
    else if("Import".equals(folder))
    {
      return senderBizEntityID;
    }
    else
    {
      throw new IllegalArgumentException("[DocumentFlowNotifyHelper.getOwnBEID] folder "+folder+" is not supported !");
    }
  }
  
  /**
   * As the tracingID is the core identifier for the OTC to link up all the DocumentFlow that the transaction document
   * has gone through, we has to ensure its value is not null or empty.
   * @param tracingID
   * @return
   */
  private static boolean assertTracingID(String tracingID)
  {
  	String mn = "assertTracingID";
    if(tracingID == null || "".equals(tracingID))
    {
      ApplicationException ex = new ApplicationException("[DocumentFlowNotifyHelper.assertTracingID] TracingID can't be null or empty ! No DocumentFlowNotification wull be triggered !");
      logError(ILogErrorCodes.NOTIFICATION_GENERIC, mn, "TracingID NULL OR Empty", ex);
      return false;
    }
    return true;
  }
  
  /**
   * Depending on the creation of the gdoc entity (as identified by the gdocUID), we will
   * make use either the gdoc's srcFolder or the gdoc's folder as the actual folder. 
   * 
   * This is to handle the case for example (exit to OB-> user procedure), (exit to export -> mappingRule).
   * In the above example, we will be using the srcFolder as the actual folder because while we
   * executing the user procedure activity, we still haven't persisted that gdoc.  
   * @param gdocUID
   * @param folder
   * @param srcFolder
   * @return
   */
  private static String getActualFolder(Long gdocUID, String folder, String srcFolder)
  {
    if(gdocUID == null || gdocUID == 0)
    {
      if(srcFolder != null && srcFolder.trim().length() > 0)
      {
        return srcFolder;
      }
      else
      {
        //#17 TWX:10 Apr 2008 To handle the case HeaderTransformation -> Exit to Export --> Exit to port --> Exit WF
        return folder;
      }
      
    }
    else
    {
      return folder;
    }
  }
  
  public static boolean isTransactionRollback(Throwable th)
  {
	  if(th == null)
	  {
		  return false;
	  }
	  else if(th instanceof TransactionRolledbackException || th instanceof TransactionRolledbackLocalException)
	  {
		  return true;
	  }
	  else
	  {
		  Throwable nestedCause = th.getCause();
		  return isTransactionRollback(nestedCause);
	  }
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(DocumentFlowNotifyHandler.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, LOG_CATEGORY, buf.toString(), t);
  }
  
  private static void logWarn(String methodName, String msg, Throwable th)
  {
	Log.warn(LOG_CATEGORY, "["+DocumentFlowNotifyHandler.class.getSimpleName()+"."+methodName+"]"+msg, th);  
  }
  /*
  private static File convertByteToFile(byte[] messageData) throws SystemException
  {
    if(messageData != null && messageData.length > 0)
    {
      FileOutputStream out = null;
      ByteArrayInputStream in = null;
      
      try
      {
        File f = new File("c:/receiveFile.txt");
        out = new FileOutputStream(f);
      
        byte[] b = new byte[512];
        int byteRead = 0;
        in = new ByteArrayInputStream(messageData);
        while( (byteRead = in.read(b)) > -1)
        {
          out.write(b, 0 , byteRead);
        }
        
        out.close();
        in.close();
        
        return f;
      }
      catch(Exception ex)
      {
        throw new SystemException("Error in convert byte to file", ex);
      }
    }
    else
    {
      System.out.println("message data is null !!");
      return null;
    }
  } */
}
