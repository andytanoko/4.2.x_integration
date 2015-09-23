/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: FTPPushClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16, 2010   Tam Wei Xiang       Created, reference to FTPBackendConnector
 *                                    from GT414.X
 */
package com.gridnode.ftp.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import com.gridnode.ext.util.DocumentUtil;
import com.gridnode.ext.util.exceptions.ILoggerConstant;
import com.gridnode.ftp.FTPClientDelegate;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.ftp.helper.GdocHelper;
import com.gridnode.ftp.helper.XMLHelper;
import com.gridnode.ftp.model.FTPServerInfo;
import com.gridnode.ftp.notification.EmailNotifier;
import com.gridnode.ftp.notification.EventNotifier;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class handle the pushing of document to the FTP folder.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class FTPPushClient
{

  private Logger _logger = null;
  
  public FTPPushClient()
  {
    _logger = getLogger();
  }
  
  /**
   * Given the gdoc object, we will FTP the corresponding user document to the FTP server.
   * @param objGdoc The GDOC
   * @param isLastCheckedPoint indicate whether we should trigger the "DocumentDeliveredToBackend"
   *                           event after the document has been pushed to FTP.
   * @return
   * @throws Throwable
   */
  public int pushDocToFTP(Object objGdoc, Boolean isLastCheckedPoint) throws Throwable
  {
    return pushDocToFTP(objGdoc, isLastCheckedPoint, "");
  }
  
  /**
   * Given the gdoc object, we will FTP the corresponding user document to the FTP server. As the
   * default folder key constructed by the FTP client may not be unique. Thus user is allowed
   * to  specify the folder key. 
   * @param objGdoc The GDOC
   * @param isLastCheckedPoint indicate whether we should trigger the "DocumentDeliveredToBackend"
   *                           event after the document has been pushed to FTP.
   * @param folderKey The additional folder key that make the default folder key unique.
   * @return
   * @throws Throwable
   */
  public int pushDocToFTP(Object objGdoc, Boolean isLastCheckedPoint, String folderKey) throws Throwable
  {
    GridDocument gdoc = (GridDocument)objGdoc;
    String udocFilename = gdoc.getUdocFilename();
    String folder = gdoc.getFolder();
    String gdocID = ""+gdoc.getGdocId();
    String be = DocumentUtil.getOwnBEID(folder, gdoc.getRecipientBizEntityId(), gdoc.getSenderBizEntityId(), gdoc.getSrcFolder());
    String partnerID = DocumentUtil.getPartnerID(folder, gdoc.getRecipientPartnerId(), gdoc.getSenderPartnerId(), gdoc.getSrcFolder());
    Long processInstanceUID = gdoc.getProcessInstanceUid();
    return pushDocToFTP(udocFilename, folder, gdocID, be, partnerID, gdoc.getUdocDocType(), gdoc.getTracingID(), isLastCheckedPoint, 
                        folderKey, processInstanceUID);
  }
  
  /**
   * 
   * @param udocFilename
   * @param folder
   * @param gdocID
   * @param be
   * @param partner
   * @param documentType
   * @param tracingID
   * @param isLastCheckedPoint
   * @param key This is useful if the default folderKey (be.partner.docType) is not unique within the config file for example we need to send
   *            the same doc from the same TP to two different location.
   * @return
   * @throws Exception
   */
  public int pushDocToFTP(String udocFilename, String folder, String gdocID, String be, String partner, String documentType,String tracingID, 
                          Boolean isLastCheckedPoint, String key, Long processInstanceUID) throws Throwable
  {
    
    String method = "pushDocToFTP";
    
    String jmsPropsPath = SystemUtil.getWorkingDirPath()+"/gtas/data/userproc/ftpclient/jndi.properties";
    String ftpPropertiesPath = SystemUtil.getWorkingDirPath()+"/gtas/data/userproc/ftpclient/ftpBackend.properties";
    EventNotifier notifier = new EventNotifier(jmsPropsPath);
    int ftpResponseCode = 0;
    String messageID = DocumentUtil.getMessageID(folder, gdocID); 
    String udocFilePath = DocumentUtil.getUDocFilePath(folder, udocFilename);
    String folderKey = "";
    
    _logger.logMessage(method, null, "filename: "+udocFilename+" tracingID: "+tracingID+" messageID: "+messageID+" be:"+be+" partner: "+partner+" docType:"+documentType+" folderKey:"+key);
    
    File udoc = new File(udocFilePath);
    if(! udoc.exists())
    {
      _logger.logMessage(method, null,"UDoc file: "+udoc.getAbsolutePath()+" can not be found !!! GDOC messageID: "+messageID);
      throw new Exception("UDoc file: "+udoc.getAbsolutePath()+" can not be found !!! GDOC messageID: "+messageID);
    }
    
    try
    {
      
      Properties ftpProps = FTPConfigUtil.loadProperties(ftpPropertiesPath);
      ArrayList<Integer[]> validFTPCodes = FTPConfigUtil.loadValidFTPCodes(ftpProps);
      
      key = (key == null || "".equals(key.trim())) ? "" : "."+key;
      folderKey = be+"."+partner+"."+documentType+ key;
      FTPServerInfo[] infoList = FTPConfigUtil.getFTPServerInfo(folderKey, ftpProps);
      GdocHelper gdocHelper = new GdocHelper(jmsPropsPath);
      String filepath = udoc.getAbsolutePath();
      
      for(int i =0; infoList != null && infoList.length > i; i++)
      {
          FTPServerInfo serverInfo = infoList[i];
          if("".equals(serverInfo.getDestFolder()))
          {
            throw new Exception("Can't find the target FTP folder given folderKey:"+folderKey);
          }
          
          //#130 Format the filename that is expected in ATX base on the filename format we configured
          Object[] substitudeResources = gdocHelper.getSubstitutionResourcesList(udoc, gdocID, folder, 
                                                               documentType, processInstanceUID, serverInfo.getFilenameFormat(), 
                                                               serverInfo.getAppendFilename(), tracingID, messageID,
                                                               serverInfo.getEmbeddedContentTagFormat());
          //#150 20090505 TWX
          String actualFilename = udocFilename;
          if(substitudeResources != null)
          {
            actualFilename = GdocHelper.formatFilename(substitudeResources, serverInfo.getFilenameFormat());
            filepath = embedAdditionalTag(substitudeResources, serverInfo.getEmbeddedContentTagFormat(), serverInfo.getEmbeddedContentTag(), filepath);
          }
          
          ftpResponseCode = pushToFTP(serverInfo.getHostname(), serverInfo.getPort(), serverInfo.getUsername(), serverInfo.getPassword(), filepath, actualFilename, serverInfo.getDestFolder(),
                                      serverInfo.isUsePassiveMode());
          boolean isValidFTPResponse = isValidFTPCode(ftpResponseCode, validFTPCodes);
          
          //Indicate to TXMR that all the previous FTP operation is successful
          if((infoList.length == (i+1)) && isLastCheckedPoint && isValidFTPResponse)
          {
            notifier.notifyDocumentDeliveredToBackend(tracingID, messageID, ftpResponseCode, filepath);
          }
          
          _logger.logMessage(method, null,"responseCode: "+ftpResponseCode);
          if(! isValidFTPResponse)
          {
            throw new FTPServiceException("FTP action to folder "+serverInfo.getDestFolder()+" is not succeeded. Response code:"+ftpResponseCode);
          }
        
      }
      
      return ftpResponseCode;
    }
    catch(Throwable th)
    {
      if(th instanceof FTPServiceException)
      {
        String errorMessage = th.getMessage();
        notifier.notifyDocumentToBackendFailed(tracingID, messageID, ftpResponseCode, errorMessage);
      }
      _logger.logError(ILoggerConstant.FTP_PUSH_FAILED, method, null, "Push Doc to FTP failed. Error: "+th.getMessage(), th);
      notifyFTPPushFailed(udocFilename, messageID, tracingID, th.getMessage(), ExceptionUtil.getStackStraceStr(th), ftpPropertiesPath, folderKey);
      throw new Exception("FTP Push failed, error: "+th.getMessage());
    } 
  }
  
  /**
   * If the be.tp.docType approach is not able to cater the scenario, user can directly specify the
   * FTP dest folder in UP level.
   * 
   * TODO: 2) Proper error code return
   * @param host
   * @param port
   * @param userName
   * @param password
   * @param filename
   * @param destFilePath
   * @return
   * @throws Exception
   */
  public int pushToFTP(String host, int port, String userName, String password,
                       String filename, String actualFilename, String destFilePath,
                       boolean isUsePassiveMode) throws Exception
  {
      FTPClientDelegate delegate = new FTPClientDelegate();
      delegate.setUsePassiveMode(isUsePassiveMode);
      
      return delegate.ftpSendToDestFolder(host, port, userName, password, filename, actualFilename, destFilePath, true);
  }
  
  private void notifyFTPPushFailed(String udocFilename, String mesageID, String tracingID, String errorMsg, String stackTrace, String emailProps,
                                   String folderKey)
  {
    String method = "notifyFTPServiceFailed";
    EmailNotifier notifier = new EmailNotifier(emailProps);
    
    try
    {
      //#135 20 Mar Added folder key to alert template
      notifier.triggerEmail(udocFilename, mesageID, tracingID, errorMsg, stackTrace, folderKey);
    }
    catch(Exception mailEx)
    {
      _logger.logError(ILoggerConstant.FTP_EMAIL_FAILED, method, null, "failed to trigger email. Error: "+mailEx.getMessage(), mailEx);
    }
  }
  
  /**
   * #150 20090505 
   * Embed additional content into the udoc file. This is specifically required in ATX document linkage requirement.
   * The embed of the content is only for OB RN Doc as well as the corresponding RN Signal (could be RN ACK or RN Exception)
   * 
   *  
   * @param substitutionList Contain the list of resources that we can append into the udoc content.
   * @param embeddedFormat Indicate the pattern string
   * @param embeddedTagName Indicate the tag name we embedded into the udoc file
   * @param udocFilePath Indicate the file path to the Udoc
   * @return the udocFilePath that contain the additional embeddedTag. If no embeddedFormat is specified, 
   *         the original udocFilePath will return instead.
   * @throws Exception
   */
  private String embedAdditionalTag(Object[] substitutionList, String embeddedFormat, String embeddedTagName, String udocFilePath) throws Exception
  {
    if(embeddedFormat == null)
    {
      return udocFilePath;
    }
    
    String embeddedContent = GdocHelper.formatFilename(substitutionList, embeddedFormat);
    File withEmbeddedValue = XMLHelper.embedXMLContent(embeddedTagName, embeddedContent, new File(udocFilePath));
    return withEmbeddedValue.getAbsolutePath();
  }
  
  private boolean isValidFTPCode(int ftpCode, ArrayList<Integer[]> validFTPCodes)
  {
    for(int i = 0; i < validFTPCodes.size() ; i++)
    {
      Integer[] validFTPCode = validFTPCodes.get(i);
      if(validFTPCode.length ==2 )
      {
        boolean isValid = (ftpCode >= validFTPCode[0]) && (ftpCode < validFTPCode[1]);
        if(isValid)
        {
          return true;
        }
      }
      else
      {
        boolean isValid = ftpCode == validFTPCode[0];
        if(isValid)
        {
          return true;
        }
      }
    }
    return false;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getOneTimeInstance().getLogger(ILoggerConstant.LOG_TYPE, "FTPPushClient");
  }
}
