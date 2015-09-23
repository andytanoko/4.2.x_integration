/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FTPBackendConnector.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 6, 2009   Tam Wei Xiang       Created
 * Mar 11, 2009  Tam Wei Xiang       #130 For the document we push to ATX,
 *                                        we will format the filename so that ATX can accept
 *                                        and interpret the necessary info from the filename.
 * Mar 12, 2009  Tam Wei Xiang       #133: Make the FTP pull operation process more atomic
 *                                         to prevent the same file be pushed from ATX again.  
 * Mar 13, 2009  Tam Wei Xiang       #132: Use the variant of ftpListOfFilenames in FTPClientDelegate
 *                                         that support the setting of the timeout props.        
 * May 05, 2009  Tam Wei Xiang       #150: support for linking the OB XML and the 
 *                                         corresponding IB ACK. 
 * May 12, 2009  Tam Wei Xiang       #155: pullDocFromFTP() should catch "Throwable"
 * 
 * Aug 24, 2010  Wong Ming Qian      #1753: Changes include supporting the retrieval of attachment from FTP,
 *                                          and the conversion of document trading partner ID to GridTalk
 *                                          trading partner ID and submitting them into the respective
 *                                          BCs.
 * Aug 31, 2010  Tam Wei Xiang       #1753: Touch up the logging, formatting of code
 * Nov 22, 2010  Tam Wei Xiang       #1977: Add support for configuring the transfer mode:
 *                                          ASCII or binary
 * Dec 09, 2010 Alain Ah Ming        #2021: Fixed a bug where the mapping of partnerId (from the doc) to 
 *                                          GT partner id was not successful.
 *                                        
 */
package com.gridnode.ftp.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import com.gridnode.convertor.ConvertorFacade;
import com.gridnode.ftp.FTPClientDelegate;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.ftp.helper.GdocHelper;
import com.gridnode.ftp.helper.XMLHelper;
import com.gridnode.ftp.notification.EmailNotifier;
import com.gridnode.ftp.notification.EventNotifier;
import com.gridnode.ftp.notification.HTTPBCNotifier;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.base.xml.extension.EnhancedXPathSAXHelper;
import com.gridnode.pdip.base.xml.helpers.ConvertorHelper;
import com.gridnode.pdip.framework.util.UUIDUtil;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.inovis.userproc.util.DocumentUtil;
import com.inovis.userproc.util.FileUtil;
import com.inovis.userproc.util.log.ILoggerConstant;


/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
public class FTPBackendConnector
{

  private static final String FTP_HOST="ftp.host";
  private static final String FTP_PORT="ftp.port";
  private static final String FTP_USER="ftp.username";
  private static final String FTP_PASSWORD="ftp.password";
  private static final String FTP_APPENDED_FILENAME = "append.filename";
  private static final String FTP_FILENAME_FORMAT = "filename.format";
  private static final String FTP_EMBEDDED_CONTENT_TAG = "embed.contentTag";
  private static final String FTP_EMBEDDED_CONTENT_FORMAT = "embed.contentTag.format";
  private static final String FTP_IS_BINARY_TRANSFER = "ftp.isBinary";
  
  //added by Ming Qian for attachment retrieval 20100816
  private static final String ATTACHMENT_HOST = "attachment.host";
  private static final String ATTACHMENT_PORT = "attachment.port";
  private static final String ATTACHMENT_USERNAME = "attachment.username";
  private static final String ATTACHMENT_PASSWORD = "attachment.password";
  private static final String ATTACHMENT_FOLDER = "attachment.folder";
  private static final String ATTACHMENT_FILE_EXTENSION = "attachment.fileExtension";
  private static final String ATTACHMENT_IS_ENFORCE_ATTACHMENT = "attachment.isEnforceAttachment";
  private static final String ATTACHMENT_IS_DELETE_ATTACHMENT = "attachment.isDeleteAttachment";
  private static final String ATTACHMENT_IS_MULTIPLE_ATTACHMENT = "attachment.isMultipleAttachment";
  
  String attachmentFilename = "";
  boolean deleteDocument = true;
  //end of added by Ming Qian for attachment retrieval 20100816
  
  private static final int NUM_FILE_RETRIEVE_DEF = 10;
  private static final String FTP_CLIENT_PROVIDER = "ftp.client.provider";
  
  private static final String NUM_FOLDER_RETRIEVE = "num.doc.folder.retrieve"; //for FTP pull
  private static final String NUM_FILE_FETCH = "num.fetch";
  
  private static final String VALID_FTP_STATUS_CODE = "valid.ftp.status.code";
  
  protected static final String TPKEY_TPID_COUNT = "trading.partner";
  protected static final String TPKEY_TPID_PREFIX = TPKEY_TPID_COUNT+".";
  protected static final String TPKEY_TPID_IN_DOC_SUFFIX = ".doc.partner.id";
  protected static final String TPKEY_TPID_IN_GT_SUFFIX = ".gridtalk.partner.id";
  
  private Logger _logger = null;

  
  /**
   * @param args
   */
  
  //end of remove by Ming Qian for testing purposes
  public FTPBackendConnector()
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
      
      Properties ftpProps = loadDestFolderProperties(ftpPropertiesPath);
      ArrayList<Integer[]> validFTPCodes = loadValidFTPCodes(ftpProps);
      
      key = (key == null || "".equals(key.trim())) ? "" : "."+key;
      folderKey = be+"."+partner+"."+documentType+ key;
      FTPServerInfo[] infoList = getFTPServerInfo(folderKey, ftpProps);
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
          
          ftpResponseCode = pushToFTP(serverInfo.getHostname(), serverInfo.getPort(), serverInfo.getUsername(), serverInfo.getPassword(), filepath, actualFilename, 
                                      serverInfo.getDestFolder(), serverInfo.isBinaryTransfer()); //#1977: add support for binary transfer mode
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
  
  private ArrayList<Integer[]> loadValidFTPCodes(Properties props)
  {
    ArrayList<Integer[]> validFTPCodeList = new ArrayList<Integer[]>();
    String validFTPCode = props.getProperty(VALID_FTP_STATUS_CODE, null);
    if(validFTPCode == null)
    {
      Integer[] defFTPCode = new Integer[]{200, 300};
      validFTPCodeList.add(defFTPCode);
      return validFTPCodeList;
    }
    else
    {
      String[] validFTPArr = validFTPCode.split(",");
      for(int i = 0; i < validFTPArr.length; i++)
      {
        String ftpCode = validFTPArr[i];
        boolean isRange = ftpCode.indexOf("-") >= 0;
        if(isRange)
        {
          String[] ftpCodeRange = ftpCode.split("-");
          if(ftpCodeRange.length != 2)
          {
            throw new IllegalArgumentException("Invalid FTP range declaration for "+ftpCode);
          }
          Integer[] ftpCodeRangeInt = new Integer[]{Integer.parseInt(ftpCodeRange[0]), Integer.parseInt(ftpCodeRange[1])};
          validFTPCodeList.add(ftpCodeRangeInt);
        }
        else
        {
          Integer[] ftpCodeRangetInt = new Integer[]{Integer.parseInt(ftpCode)};
          validFTPCodeList.add(ftpCodeRangetInt);
        }
      }
      return validFTPCodeList;
    }
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
  
  private void notifyFTPPullFailed(String errorMsg, String stackTrace, String emailProps, String ftpBackendPropsName)
  {
	System.out.println("stack: "+stackTrace);  
    String method = "";
    EmailNotifier notifier = new EmailNotifier(emailProps);
    try
    {
      notifier.triggerFTPPullFailedEmail(errorMsg, stackTrace, ftpBackendPropsName);
    }
    catch(Exception ex)
    {
      _logger.logError(ILoggerConstant.FTP_EMAIL_FAILED, method, null, "failed to trigger email. Error: "+ex.getMessage(), ex);
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
                       boolean isBinaryTransfer) throws Exception
  {
      FTPClientDelegate delegate = new FTPClientDelegate();
      return delegate.ftpSendToDestFolder(host, port, userName, password, filename, actualFilename, destFilePath,
                                          isBinaryTransfer);
  }
  
  /**
   * Pull the document from the folder that define in "ftpBackend.properties".
   * @throws Exception
   */
  
  public void pullDocFromFTP() throws Exception
  {
  	String method = "pullDocFromFTP";
  	    
  	String configPropsPath = "./conf/config.properties";
  	String ftpPropsPath = "./conf/ftpBackend.properties";
  	String jndiPropsPath = "./conf/jndi.properties";
  	String ftpClientPropsPath = "./conf/ftpclient.properties";
  	String tpPropsPath = "./conf/tp.properties";
  	String configKey = "config";
  	
  	boolean isConfigPropsAvailable = true;
  
  	Integer tempInteger;
  	
  	Properties props = new Properties();
  	  
  	try
    {
        FileInputStream fs = new FileInputStream(configPropsPath);
    }
    catch(FileNotFoundException e)
    {
        isConfigPropsAvailable = false;	
    }
      
    if (isConfigPropsAvailable)
    {
        props.load(new FileInputStream(configPropsPath));
        tempInteger = new Integer(props.getProperty(configKey));	
        _logger.debugMessage(method, null, "---------------------------------------");
        _logger.debugMessage(method, null, "Configuration properties is configured.");
        _logger.debugMessage(method, null, "---------------------------------------");
    }
    else
    {
        tempInteger = new Integer(1);
        _logger.debugMessage(method, null, "-------------------------------------------");
        _logger.debugMessage(method, null, "Configuration properties is not configured.");
        _logger.debugMessage(method, null, "-------------------------------------------");
    }
      
      //try to lock the file so that we can ensure only one process is accessing the
      //ATX server even given in cluster env
    if(! FileUtil.lockFile(new File("file.lock")))
    {
        _logger.logMessage(method, null, "Other process is currently accessing the ATX. wait for the next scheduled time.");
        return;
    }
      
    for (int i = 1; i <= tempInteger.intValue(); i++)
    {
        boolean isTPPropsAvailable = true;
        boolean isAttachmentPropsAvailable = true;
        boolean isMappingPropsAvailable = true;    	
        boolean isAttachmentServerValid = true;
        
        
        //if got config.properties, go and find the individual properties files
        if (isConfigPropsAvailable)
        {
          ftpPropsPath = props.getProperty(configKey + "." + i + ".backend");
          tpPropsPath = props.getProperty(configKey + "." + i + ".tp");  
        }
        
        _logger.logMessage(method, null, "Processing ftpbackend props="+ftpPropsPath+" tp mapping props="+tpPropsPath);
        
        //go find if user keyed in values for tp.properties
        if (tpPropsPath == null)
        {
        	isTPPropsAvailable = false;
        	
        	_logger.debugMessage(method, null, "----------------------------------------------");
        	_logger.debugMessage(method, null, "Trading partners properties is not configured.");
        	_logger.debugMessage(method, null, "----------------------------------------------");
        	_logger.debugMessage(method, null, "ftpPropsPath:" + ftpPropsPath);  
        }
        else
        {
        	_logger.debugMessage(method, null, "------------------------------------------");
          _logger.debugMessage(method, null, "Trading partners properties is configured.");
          _logger.debugMessage(method, null, "------------------------------------------");  
        	_logger.debugMessage(method, null, "ftpPropsPath:" + ftpPropsPath);  
        	_logger.debugMessage(method, null, "tpPropsPath:" + tpPropsPath);
        }
        
        try
        {
          File outToHTTPBC = new File("outToHTTPBC");  
          HTTPBCNotifier httpbc = new HTTPBCNotifier(jndiPropsPath);
          Properties ftpProps = loadDestFolderProperties(ftpPropsPath);
          Properties ftpClientProps = loadDestFolderProperties(ftpClientPropsPath);  
          String folderKey = "";
          FTPServerInfo[] infos = getFTPServerInfo(folderKey, ftpProps);
          FTPServerInfo info = infos[0];
          FTPInfo[] ftpInfos = getFTPInfo(ftpProps, info);
          String xPath = "";
          boolean isMappingRequired = false;
          String mappingRule = "";
          
          if(infos.length == 0)
          {
            return;
          }
          if(ftpInfos != null && ftpInfos.length > 0)
          {	
            int counter = 1;
            for(FTPInfo ftpInfo : ftpInfos)
            {
              FTPFolderInfo folderInfo = ftpInfo.getFolderInfo();
              String folder = folderInfo.getFolder();
              String be = folderInfo.getBe();
              String partner = folderInfo.getTp();
              String docType = folderInfo.getDocType();
              FTPClientDelegate delegate = new FTPClientDelegate();
              delegate.setFTPClientPropPath(ftpClientPropsPath);
              String[] filenames = delegate.ftpRetrieveListOfFilename(info.getHostname(), info.getPort(), 
              info.getUsername(), info.getPassword(), folder, folderInfo.getNumFetch());
              FTPAttachmentInfo attachmentInfo = null;
              FTPAttachmentInfo[] attachmentInfos = new FTPAttachmentInfo[ftpInfos.length];
              String attachmentFolderKey = "num.doc.folder.retrieve";
              
              //if invoice folder is null, it means user is trying to be funny to enter num.doc.folder.retrieve value greater
              //than the properties he/she has configured
              if (folder == null || folder.equals(""))
                break;
  
              _logger.logMessage(method, null, "FTP server: "+info);
              _logger.logMessage(method, null, "FTP folder: "+folderInfo);
              _logger.logMessage(method, null, "Document folder: "+folderInfo.getFolder());
              
              //check if there is mapping needed
              xPath = folderInfo.getXPath();
              if (xPath == null || xPath.equals(""))
              {
                isMappingPropsAvailable = false;  
                
                _logger.debugMessage(method, null, "-------------------------------------");
                _logger.debugMessage(method, null, "Mapping properties is not configured.");
                _logger.debugMessage(method, null, "-------------------------------------");
              }
              else
              {
                _logger.debugMessage(method, null, "---------------------------------");
                _logger.debugMessage(method, null, "Mapping properties is configured.");
                _logger.debugMessage(method, null, "---------------------------------");
                _logger.logMessage(method, null, "XPath: " + folderInfo.getXPath());
                _logger.logMessage(method, null, "Mapping required: " + folderInfo.getIsMappingRequired());
                _logger.logMessage(method, null, "Mapping rule: " + folderInfo.getMappingRule());
              }
              
              //check if there is attachment needed
              attachmentInfos = getFTPAttachmentInfo(attachmentFolderKey, ftpProps);
              ftpProps = loadDestFolderProperties(ftpPropsPath);
              
              //if (attachmentInfos.length == 0)
              if (attachmentInfos[counter - 1] == null)
              {
                isAttachmentPropsAvailable = false;
                _logger.debugMessage(method, null, "----------------------------------------");
                _logger.debugMessage(method, null, "Attachment properties is not configured.");
                _logger.debugMessage(method, null, "----------------------------------------");
              }
              else
              {
                isAttachmentPropsAvailable = true;	
                _logger.debugMessage(method, null, "------------------------------------");	
                _logger.debugMessage(method, null, "Attachment properties is configured.");
                _logger.debugMessage(method, null, "------------------------------------");
              }
              
              if(filenames != null && filenames.length > 0)
              {
                //#133 Make FTP pull operation more atomic NOTE: It is not under a JTA transaction
                for(String filename : filenames)
                {
                  byte[] docForImport = delegate.getFileInByte(filename);
                  //byte[] mappingRuleForImport;  
                  byte[] attachment = null;	
              	  
                  File processedFile = null;
                
                  _logger.debugMessage(method, null, "Filename retrieved: "+filename);
                
                  if(isFileProcessed(new File(outToHTTPBC.getAbsolutePath()+"/"+filename)))
                  {
                    _logger.logMessage(method, null, "File :"+filename+" has been processed before, ignored.");
                  
                    processedFile = new File(outToHTTPBC.getAbsolutePath()+"/"+filename);
                  }
                  else
                  {
                	  //check for mapping status and DO IT!
                	  if (isMappingPropsAvailable)
                	  {
                	    isMappingRequired = folderInfo.getIsMappingRequired();
                      xPath = folderInfo.getXPath();
                      mappingRule = folderInfo.getMappingRule();
                      
                      //this step is for document  
                      String tempFile = getTempDirPath() + UUIDUtil.getRandomUUIDInStr();
                  	
                      //write byte to a temp location
                      FileOutputStream fos = new FileOutputStream(tempFile);
                      fos.write(docForImport);
                      
                      if (isMappingRequired)
                        partner = retrieveTPInfo(tempFile, mappingRule, tpPropsPath, xPath, true);
                      else
                        partner = retrieveTPInfo(tempFile, mappingRule, tpPropsPath, xPath, false);
                      
                      fos.close();
                      
                      //_logger.logMessage(method, null, "Counter: "+counter);
                      _logger.logMessage(method, null, "Mapping Required: "+isMappingRequired);  
                      //_logger.logMessage(method, null, "Removed " + tempFile + " file: "+new File(tempFile).delete()); 
                	  }
                	  _logger.logMessage(method, null, "GridTalk partner ID : " + partner);
                  }
                  
                  //retrieve document
                  //mappingRuleForImport = delegate.getFileInByte(mappingRule);
                  
                  //retrieve attachment
                  if (isAttachmentPropsAvailable)
                  {                 
                    //if user configures attachment	
                    if (attachmentInfos[counter - 1] != null)
                    {
                      attachmentInfo = attachmentInfos[counter - 1];	
                    
                      //if user is trying to be funny again and not enter attachment host information
                      if (attachmentInfo.getHost() == null || attachmentInfo.getHost().equals("") || attachmentInfo.getPort() == 0)
                      {
                        attachmentInfo.setHost(info.getHostname());
                        attachmentInfo.setPort(info.getPort());
                        attachmentInfo.setUsername(info.getUsername());
                        attachmentInfo.setPassword(info.getPassword());
                      }
                      
                      if (isAttachmentServerValid)
                      {
                        _logger.logMessage(method, null, "FTP server information is configured");		
                        try
                        {
                          attachment = retrieveAttachment(attachmentInfo, info, filename, ftpClientPropsPath, ftpPropsPath, folder, isAttachmentServerValid);	
                        }
                        catch (Exception ex)
                        {
                          _logger.logError(null, method, null, "Unable to retrieve attachment: " + attachmentInfo.getHost() + " Error: "+ex.getMessage(), ex);	
                        }
                      	  
                        //with attachment properties configured and attachment available
                        if (attachment != null)
                        {
                          attachmentFound(httpbc, delegate, attachmentInfo, processedFile, outToHTTPBC, docForImport, attachment, 
                                          filename, be, partner, docType, ftpPropsPath);
                        }
                        else if (attachment == null)
                        {
                          try
                          {
                            //with attachment properties configured and no attachment available	
                        	  noAttachmentFound(httpbc, delegate, processedFile, outToHTTPBC, docForImport, filename, be, partner, 
                        	    docType, ftpPropsPath);	
                          }
                          catch (Exception ex)
                          {
                            _logger.logError(null, method, null, "Unable to retrieve attachment: " + attachmentInfo.getHost() + " Error: "+ex.getMessage(), ex);	
                          }
                        }  
                      }
                      else
                      {
                        attachment = retrieveAttachment(attachmentInfo, info, filename, ftpClientPropsPath, ftpPropsPath, folder, isAttachmentServerValid);		
                        _logger.logMessage(method, null, "FTP server information is not configured");	
                      }
                    }
                    //if user didnt configure attachment properties
                    else
                    {
                      noAttachmentFound(httpbc, delegate, processedFile, outToHTTPBC, docForImport, filename, be, partner, 
                    	  docType, ftpPropsPath); 
                    }
                  }
                  else
                  {
                    //with no attachment properties configured and no attachment available	
                    noAttachmentFound(httpbc, delegate, processedFile, outToHTTPBC, docForImport, filename, be, partner, 
                  	docType, ftpPropsPath);
                  }
                }
              }
              else
              {
                _logger.logMessage(method, null, "No files can be found in folder: "+folder);
              }
              
              counter++;
            }
          }
          //#133 cleanup the file in the outToHTTPBC
          FileUtil.deleteFiles(outToHTTPBC, getHouseKeepInterval());
  
          _logger.debugMessage(method, null, "Housekeep "+outToHTTPBC.getAbsolutePath()+ " completed.");
        }
        catch(Throwable ex)
        {
          _logger.logError(ILoggerConstant.FTP_PULL_FAILED, method, null, "Error in pulling doc from FTP. Error: "+ex.getMessage(), ex);
          notifyFTPPullFailed(ex.getMessage(), ExceptionUtil.getStackStraceStr(ex), ftpPropsPath, ftpPropsPath);
          //throw new Exception("FTP Pull is failed ! "+ex.getMessage());
        }
        _logger.logMessage(method, null, "End processed ftpbackend props="+ftpPropsPath+" tp mapping props="+tpPropsPath);
    }
  }
  
  private void noAttachmentFound(HTTPBCNotifier httpbc, FTPClientDelegate delegate, File processedFile, File outToHTTPBC, 
    byte[] docForImport, String filename, String be, String partner, String docType, String ftpPropsPath) throws Exception
  {
    String method = "noAttachmentFound";

	  if (deleteDocument)
	  {
	    httpbc.notifyHTTPBCToReceive(filename, docForImport, be, partner, docType);	
	    processedFile = FileUtil.saveFiles(outToHTTPBC, filename, docForImport);
	    delegate.removeFile(filename);
	       
	    _logger.debugMessage(method, null, "Delegate to HTTPBC without attachment with filename: "+filename);
	    _logger.logMessage(method, null, "Removed file: "+filename+" from ATX.");
	        
	    if (processedFile != null)
	      processedFile.delete();	
	  }
	  else
	  {
	    _logger.logMessage(method, null, "Removed file: "+filename+" from ATX is unsuccessful.");
	  }	
	}
  
  private void attachmentFound(HTTPBCNotifier httpbc, FTPClientDelegate delegate, FTPAttachmentInfo attachmentInfo, File processedFile, File outToHTTPBC, 
    byte[] docForImport, byte[] attachment, String filename, String be, String partner, String docType, String ftpPropsPath) throws Exception
  {
    String method = "attachmentFound";
  			
    httpbc.notifyHTTPBCToReceive(filename, extractFilename(filename) + '.' + attachmentInfo.getFileExtension(), docForImport, attachment, be, partner, docType);
  	  //httpbc.notifyHTTPBCToReceive(filename, UUIDUtil.getRandomUUIDInStr() + '.' + attachmentInfo.getFileExtension(), docForImport, attachment, be, partner, docType);
    _logger.debugMessage(method, null, "Delegate to HTTPBC with attachment with filename: "+filename);
  	  
    processedFile = FileUtil.saveFiles(outToHTTPBC, filename, docForImport);
    delegate.removeFile(filename);
    _logger.logMessage(method, null, "Removed file:"+filename+" from ATX.");
        
    if (processedFile != null)
    {
          processedFile.delete();
  	  //processedFile = FileUtil.saveFiles(outToHTTPBC, attachmentFilename, attachment);
    }       
  }
  
  //added by Ming Qian 20100823 for trading partner info retrieval
  private String retrieveTPInfo(String filename, String mappingRule, String tpPropsPath, String xPath, boolean isMappingRequired) throws Exception
  {
    String method = "retrieveTPInfo";  
    String tpInfo = "";
    String docPartnerId = "";
    String gridTalkPartnerId = null;
    //String tpPropsPath = "./conf/tp.properties";
    String tradingPartnerKey = "trading.partner";
    File tempFile;
    
    _logger.logMessage(method, null, "Retrieving trading partner information....");
    
    try
    {  
      if (isMappingRequired)
      {
        ConvertorFacade convertorFacade = new ConvertorFacade();
        //converts flat file to xml
     
        tempFile = ConvertorHelper.convert(convertorFacade, filename, mappingRule);
        _logger.debugMessage(method, null, "Converted file="+ (tempFile != null ? tempFile.getAbsolutePath() : ""));
      }
      else
      {
        tempFile = new File(filename);
      }      
      tpInfo = (String)EnhancedXPathSAXHelper.getValuesAtXPathUnique(tempFile.getAbsolutePath(), xPath).get(0);

      gridTalkPartnerId = retrieveGridTalkPartnerId(tpInfo, tpPropsPath);
      
      _logger.logMessage(method, null, "Removed " + tempFile.getAbsolutePath() + " file: "+tempFile.delete());
    }
    catch (Throwable ex)
    {
      _logger.logError(null, method, null, "failed to convert doc id to gridtalk partner id. Error: "+ex.getMessage(), ex);	
      throw new Exception("Conversion failed for: "+ filename + ". Mapping Rule: " + mappingRule + ". Reason: " + ex);
    }
    
    //if user did not configure the trading partner information in properties file
    if (gridTalkPartnerId == null)
    {
      //we take the default value in the translated xml file according to the xpath	
      gridTalkPartnerId = tpInfo; 
    }
    
    return gridTalkPartnerId;
  }
  //end of added by Ming Qian 20100823 for trading partner info retrieval
  
  /**
   * Returns the GridTalk partner Id from a TP-mapping properties file given a partner Id from the document.
   * @param partnerIdFromDoc The partnerId from the document
   * @param tpPropsPath The path (relative or absolute) of the TP-mapping properties file.
   * @return The GridTalk partner Id if it is found. Null otherwise.
   * @throws IOException If any error occurs while reading the properties file.
   * @since atxPull 1.2 p01
   * @author Alain
   */
  protected String retrieveGridTalkPartnerId(String partnerIdFromDoc, String tpPropsPath) throws IOException
  {
      String mn = "retrieveGridTalkPartnerId";
      _logger.logMessage(mn, null, "partnerIdFromDoc: ["+ partnerIdFromDoc+"] tpPropsPath: ["+tpPropsPath+"]");
      String gtPartnerId = null;
      
      //retrieve partner info from the properties file
      if (nullEmptyString(tpPropsPath) || nullEmptyString(partnerIdFromDoc))
      {
          _logger.logWarn(mn, null, "Null/empty partnerIdFromDoc or tpPropsPath. See previous log statement.", null);
      } else
      {
        tpPropsPath = tpPropsPath.trim();
        partnerIdFromDoc = partnerIdFromDoc.trim();
        
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(tpPropsPath);
        props.load(fis);
        fis.close();
        
        int tpidCount = Integer.parseInt(props.getProperty(TPKEY_TPID_COUNT, "0"));  
        _logger.debugMessage(mn, null, "Size of TP list to scan : ["+ tpidCount+"]");
              
        //scans the whole file to look for suitable match
        boolean gtPartnerIdFound = false;
        for (int i = 1; i <= tpidCount && !gtPartnerIdFound; i++)
        {            
          if (partnerIdFromDoc.equals(props.getProperty(TPKEY_TPID_PREFIX + i + TPKEY_TPID_IN_DOC_SUFFIX)))
          {
            gtPartnerId = props.getProperty(TPKEY_TPID_PREFIX+ i + TPKEY_TPID_IN_GT_SUFFIX);
            _logger.logMessage(mn, null, "Found TpIdInGt : ["+ gtPartnerId+"]");
            gtPartnerIdFound = true;
          }
        }
        
      }
      
      return gtPartnerId;
      
  }
  
  private boolean nullEmptyString(String s)
  {
      return s == null || s.trim().equals("");
  }
  
  //added by Ming Qian 20100816 for attachment retrieval
  private byte[] retrieveAttachment(FTPAttachmentInfo attachmentInfo, FTPServerInfo info, String filename, String ftpClientPropsPath, String ftpPropsPath, String folder, boolean isAttachmentServerValid) throws Exception
  {
  	String method = "retrieveAttachment";  
  	_logger.logMessage(method, null, "Retrieving attachment....");
  	byte[] attachment = null;  
	//check if there is an attachment configured for this document
	//if the folder is specified
	
	
  	if (attachmentInfo.getFolder() != null && !attachmentInfo.getFolder().equals(""))
  	{
  	  //ftp to folder 
  	  _logger.logMessage(method, null, ""+ attachmentInfo);	
  	  FTPClientDelegate attachmentDelegate = new FTPClientDelegate();
  	  attachmentDelegate.setFTPClientPropPath(ftpClientPropsPath);
  	  /*try
  	  {
  		attachmentDelegate.getFTPReplyCode();		  
  	  }
  	  catch (Exception ex)
  	  {
  		isFTPAvailable = false;
  		deleteDocument = false;
  		_logger.logError(null, method, null, "Unable to access attachment FTP server: " + attachmentInfo.getHost() + " Error: "+ex.getMessage(), ex);	
  	  }*/
  	  
  	  //if attachment server information is not provided
  	  //if (isAttachmentServerValid)
  	  //{
  	    if (attachmentInfo.getHost() == null || attachmentInfo.getHost().equals("") || 
  		      attachmentInfo.getPort() == 0 || attachmentInfo.getUsername() == null
  		      || attachmentInfo.getUsername().equals("") || attachmentInfo.getPassword() == null
  		      || attachmentInfo.getPassword().equals(""))
  	    {
    		  try
    		  {
  
  		    String[] attachmentFilenames = attachmentDelegate.ftpRetrieveListOfFilename(info.getHostname(), info.getPort(), 
  		      info.getUsername(), info.getPassword(), attachmentInfo.getFolder(), 1);			
    		  }
    		  catch (Throwable ex)
    		  {
    		    deleteDocument = false;    
    		    _logger.logError(null, method, null, "Unable to retrieve attachmentFilenames from FTP server. Error: "+ex.getMessage(), ex);	
    		    throw new Exception("Unable to retrieve attachmentFilenames from FTP server. Error: "+ex.getMessage());
  		    
    		  }
  	    }
  	    else
  	    {
  		  //last parameter is put as 1 for now for single attachment retrieval
  		  //this is a workaround to "bluff" attachmentDelegate for the sign in to ftp server
    		  try
    		  {
    		    String[] attachmentFilenames = attachmentDelegate.ftpRetrieveListOfFilename(attachmentInfo.getHost(), attachmentInfo.getPort(), 
    			  attachmentInfo.getUsername(), attachmentInfo.getPassword(), attachmentInfo.getFolder(), 1);			
    		  }
    		  catch (Throwable ex)
    		  {
    			deleteDocument = false; 	  
    		    _logger.logError(null, method, null, "Unable to retrieve attachmentFilenames from FTP server. Error: "+ex.getMessage(), ex);	
    		    throw new Exception("Unable to retrieve attachmentFilenames from FTP server. Error: "+ex.getMessage());
    		  }
  		
  	    }
  	    
  	    //full name of attachment to retrieve, including extension
  	    attachmentFilename = extractFilename(filename) + "." + attachmentInfo.getFileExtension();
  	    
  	    byte[] attachmentForImport = attachmentDelegate.getFileInByte(attachmentFilename);
  	    
  	    Properties ftpProps = loadDestFolderProperties(ftpPropsPath);
  
  	    //if the attachment cannot be found
  	    if (attachmentForImport.length == 0)
  	    {
  	      attachment = isEnforceAttachment(attachmentInfo.getHost(), attachmentInfo.getPort(), attachmentInfo, attachment, folder, filename, ftpClientPropsPath, ftpPropsPath);     
  	    }
  	    //if there is attachment
  	    else
  	    {
  	      attachment = attachmentForImport;
  		
  		  //if user request to delete the file
  	      if (attachmentInfo.getIsDeleteAttachment())
  	      {
  	        deleteDocument = true;
  	        attachmentDelegate.removeFile(attachmentFilename);
  	      }
  	    }
  	  //}
   	  //else
  	  //{
   	//	 attachment = isEnforceAttachment(info.getHostname(), info.getPort(), attachmentInfo, attachment, folder, filename, ftpClientPropsPath, ftpPropsPath);
  	  //}
  	}
	
    return attachment;
  }
  
  private byte[] isEnforceAttachment(String hostname, int port, FTPAttachmentInfo attachmentInfo, byte[] attachment, String filename, String folder, String ftpClientPropsPath, String ftpPropsPath) throws Exception
  {
    String method = "isEnforceAttachment";
	
    if (attachmentInfo.getIsEnforceAttachment())
    {
      EmailNotifier notifier = new EmailNotifier(ftpPropsPath);
      try
      {
        deleteDocument = false;
        notifier.triggerIsEnforceAttachmentFailedEmail(hostname, port, filename, attachmentFilename, attachmentInfo.getFileExtension(), folder, attachmentInfo.getFolder(), ftpPropsPath, attachmentInfo.getUsername());
      }
      catch(Throwable ex)
      {
        _logger.logError(ILoggerConstant.FTP_EMAIL_FAILED, method, null, "Failed to trigger email. Error: "+ex.getMessage(), ex);
      }
    }
    else
    {
      deleteDocument = true;
      attachment = null;
	  //user is ok with it so just ignore	
    }  
	
    return attachment;
  }
  
  private String extractFilename(String filename)
  {
    String name = "";
    //this means that the document has no extension
    if (filename.lastIndexOf(".") == -1)
    {
      name = filename;	
    }
    else
    {
      name = filename.substring(0, filename.lastIndexOf("."));
    }
	
    return name;
  }
  //end of added by Ming Qian 20100816 for attachment retrieval
  
  private boolean isFileProcessed(File processedFile)
  {
    return processedFile.exists();
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getOneTimeInstance().getLogger(ILoggerConstant.LOG_TYPE, "FTPBackendConnector");
  }
  
  /**
   * Load the FTPInfo from the given props. If there is FTP connection info associated to the
   * folder info, the FTP folder info will be used instead of using the defailt "defServerInfo".
   * @param props
   * @param defServerInfo
   * @return
   */
  private FTPInfo[] getFTPInfo(Properties props, FTPServerInfo defServerInfo)
  {
    String numFolderToRetrieve = props.getProperty(NUM_FOLDER_RETRIEVE, null);
    String defNumFileFetch = props.getProperty(NUM_FILE_FETCH, NUM_FILE_RETRIEVE_DEF+"");
    
    if(numFolderToRetrieve == null || "".equals(numFolderToRetrieve))
    {
      return new FTPInfo[]{};
    }
    else
    {
      int numFolder = Integer.parseInt(numFolderToRetrieve);
      FTPInfo[] ftpInfo = new FTPInfo[numFolder];
      
      for(int i = 1; i <= numFolder; i++)
      {
        String folderKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.FOLDER;
        String beKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.BE;
        String partnerKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.PARTNER;
        String docTypeKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.DOCTYPE;
        String numFileKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.NUM_FILE;
        String isMappingRequiredKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.IS_MAPPING_REQUIRED;
        String xPathKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.X_PATH;
        String mappingRuleKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.MAPPING_RULE;
        
        String folder = props.getProperty(folderKey);
        String be = props.getProperty(beKey);
        String partner = props.getProperty(partnerKey);
        String docType = props.getProperty(docTypeKey);
        String numFetch = props.getProperty(numFileKey, defNumFileFetch);
        String isMappingRequired = props.getProperty(isMappingRequiredKey);
        String xPath = props.getProperty(xPathKey);
        String mappingRule = props.getProperty(mappingRuleKey);
        
        FTPFolderInfo folderInfo = new FTPFolderInfo(be,partner, docType, folder, Integer.parseInt(numFetch), Boolean.parseBoolean(isMappingRequired), xPath, mappingRule);
        
        ftpInfo[i-1] = new FTPInfo(defServerInfo, folderInfo);
      }
      
      return ftpInfo;
    }
  }

  
  //added by Ming Qian for attachment retrieval 20100816
  private FTPAttachmentInfo[] getFTPAttachmentInfo(String destKey, Properties props) throws Exception
  {
	  String host = "";
      String port ="";
      String username = "";
      String password = "";
      String folder = "";
      String fileExtension = "";
      String isEnforceAttachment = "";
      String isDeleteAttachment = "";
      String isMultipleAttachment = "";
	  
	  ArrayList<FTPAttachmentInfo> serverInfoList = new ArrayList<FTPAttachmentInfo>();
      String destFolder = props.getProperty(destKey, "");
      
      String numFolderToRetrieve = props.getProperty(NUM_FOLDER_RETRIEVE, null);
      int numFolder = Integer.parseInt(numFolderToRetrieve);
      for(int i = 1; i <= numFolder; i++)
      {
          host = props.getProperty(destKey+"."+i+"."+ATTACHMENT_HOST);
          port = props.getProperty(destKey+"."+i+"."+ATTACHMENT_PORT);
          username = props.getProperty(destKey+"."+i+"."+ATTACHMENT_USERNAME);
          password = props.getProperty(destKey+"."+i+"."+ATTACHMENT_PASSWORD);
          folder = props.getProperty(destKey+"."+i+"."+ATTACHMENT_FOLDER);
          fileExtension = props.getProperty(destKey+"."+i+"."+ATTACHMENT_FILE_EXTENSION);
          isEnforceAttachment = props.getProperty(destKey+"."+i+"."+ATTACHMENT_IS_ENFORCE_ATTACHMENT);
          isDeleteAttachment = props.getProperty(destKey+"."+i+"."+ATTACHMENT_IS_DELETE_ATTACHMENT);
          isMultipleAttachment = props.getProperty(destKey+"."+i+"."+ATTACHMENT_IS_MULTIPLE_ATTACHMENT);

          if (port == null || port.equals(""))
          {
            port = "0";
          }
          Integer integerPort = new Integer(port);

          if (folder != null)
          {
        	FTPAttachmentInfo serverInfo = new FTPAttachmentInfo(host, integerPort.intValue(), username, password, folder, fileExtension, 
                		Boolean.parseBoolean(isEnforceAttachment), Boolean.parseBoolean(isDeleteAttachment), Boolean.parseBoolean(isMultipleAttachment));
            serverInfoList.add(serverInfo);  
          }
          else
          {
        	serverInfoList.add(null);    
          }
      }

      return serverInfoList.toArray(new FTPAttachmentInfo[]{});
  }
  //end of added by Ming Qian for attachment retrieval 20100816
  
  /**
   * Retrieve the FTP connection info from the given props.
   * @param destKey
   * @param props
   * @return
   * @throws Exception
   */
  private FTPServerInfo[] getFTPServerInfo(String destKey, Properties props) throws Exception
  {
      ArrayList<FTPServerInfo> serverInfoList = new ArrayList<FTPServerInfo>();
      String destFolder = props.getProperty(destKey, "");
    
      //pull from FTP other than the default one. If none can be found, default FTP connection will be used
      String hostname = props.getProperty(destKey+"."+FTP_HOST, props.getProperty(FTP_HOST));
      String port = props.getProperty(destKey+"."+FTP_PORT, props.getProperty(FTP_PORT));
      String username = props.getProperty(destKey+"."+FTP_USER, props.getProperty(FTP_USER));
      String password = props.getProperty(destKey+"."+FTP_PASSWORD, props.getProperty(FTP_PASSWORD));
      String appendedFilename = props.getProperty(destKey+"."+FTP_APPENDED_FILENAME, "");
      String filenameFormat = props.getProperty(destKey+"."+FTP_FILENAME_FORMAT, null);
      String embeddedContentTag = props.getProperty(destKey+"."+FTP_EMBEDDED_CONTENT_TAG, null);
      String embeddedContentTagFormat = props.getProperty(destKey+"."+FTP_EMBEDDED_CONTENT_FORMAT, null);
      String isBinaryTransfer = props.getProperty(destKey+"."+FTP_IS_BINARY_TRANSFER, Boolean.FALSE.toString());
      
      //String numFileToFetch = props.getProperty(destKey+"."+NUM_FILE_RETRIEVE, ""+NUM_FILE_RETRIEVE_DEF);
      //int pageSize = Integer.parseInt(numFileToFetch);
      
      int ftpPort = Integer.parseInt(port);
      
      //Support pushing the same document to more than one destination folder
      //NOTE: we may have difficulty to track which document failed to be sent to FTP in TXMR
      String[] folderList = destFolder.split(",");
      if(folderList.length > 0)
      {
        for(String folder: folderList)
        {
          FTPServerInfo serverInfo = new FTPServerInfo(hostname, ftpPort, username, password, folder);
          serverInfo.setAppendFilename(appendedFilename);
          serverInfo.setFilenameFormat(filenameFormat);
          serverInfo.setEmbeddedContentTag(embeddedContentTag);
          serverInfo.setEmbeddedContentTagFormat(embeddedContentTagFormat);
          serverInfo.setBinaryTransfer(Boolean.parseBoolean(isBinaryTransfer));
          serverInfoList.add(serverInfo);
        }
      }
      
      return serverInfoList.toArray(new FTPServerInfo[]{});
  }
  
  private Properties loadDestFolderProperties(String propertiesPath) throws Exception
  {
      File props = new File(propertiesPath);
      Properties destFolderProperties = new Properties();
      destFolderProperties.load(new FileInputStream(props));
      return destFolderProperties;
  }
  
  private static long getHouseKeepInterval() throws Exception
  {
    try
    {
      Properties configProps = new Properties();
      configProps.load(new FileInputStream(new File("./conf/ftp.cfg")));
      String runInterval = configProps.getProperty("file.keep.for", "30");
      Calendar calendar = Calendar.getInstance(); 
      calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)-Integer.parseInt(runInterval));
      return calendar.getTimeInMillis();
    }
    catch(Exception ex)
    {
      throw new Exception("Configuration can't be read properly", ex);
    }
  }
  
  private String getFTPClientProvider(Properties props)
  {
    String defProvider = "com.gridnode.ftp.adaptor.NetCommonsFTPClientManager";
    if(props == null)
    {
      return defProvider;
    }
    else
    {
      return props.getProperty(FTP_CLIENT_PROVIDER, defProvider);
    }
  }

  private String getTempDirPath() throws Exception
  {
    Properties configProps = new Properties();
    try
    {
      configProps.load(new FileInputStream(new File("./conf/ftp.cfg")));
    }
    catch(Exception ex)
    {
      throw new Exception("ftp.cfg can not be read properly", ex);
    }
    
    String workingDir = System.getenv("GRIDTALK_HOME");
    String tempDirPath = "";
    if(workingDir == null || workingDir.trim().length()== 0)
    {
      tempDirPath = "temp"; //use current working Dir
    }
    else
    {
      //write to GT temp dir
      String gtTempDir = configProps.getProperty("gt.temp.dir", null);
      if(gtTempDir == null)
      {
        tempDirPath = null;
      }
      else
      {
        tempDirPath = workingDir + "/" + gtTempDir;
      }
    }
    
    return tempDirPath + "/";
  }

  private class FTPInfo
  {
    private FTPServerInfo serverInfo;
    private FTPFolderInfo folderInfo;
    
    public FTPInfo(FTPServerInfo serverInfo, FTPFolderInfo folderInfo)
    {
      setServerInfo(serverInfo);
      setFolderInfo(folderInfo);
    }

    public FTPFolderInfo getFolderInfo()
    {
      return folderInfo;
    }

    public void setFolderInfo(FTPFolderInfo folderInfo)
    {
      this.folderInfo = folderInfo;
    }

    public FTPServerInfo getServerInfo()
    {
      return serverInfo;
    }

    public void setServerInfo(FTPServerInfo serverInfo)
    {
      this.serverInfo = serverInfo;
    }
    
    
  }
  
  //added by Ming Qian for attachment retrieval 20100816
  private class FTPAttachmentInfo
  {
    private String _host;
    private int _port;
    private String _username;
    private String _password;
    private String _folder;
    private String _fileExtension;
    private boolean _isEnforceAttachment;
    private boolean _isDeleteAttachment;
    private boolean _isMultipleAttachment;
    
    public FTPAttachmentInfo(String host, int port, String username, String password, String folder, String fileExtension, 
    		boolean isEnforceAttachment, boolean isDeleteAttachment, boolean isMultipleAttachment)
    {
      setHost(host);
      setPort(port);
      setUsername(username);
      setPassword(password);
      setFolder(folder);
      setFileExtension(fileExtension);
      setIsEnforceAttachment(isEnforceAttachment);
      setIsDeleteAttachment(isDeleteAttachment);
      setIsMultipleAttachment(isMultipleAttachment);      
    }
    
    public void setHost(String host)
    {
      _host = host;
    }
    
    public String getHost()
    {
      return _host;
    }
    
    public void setPort(int port)
    {
      _port  = port;
    }
    
    public int getPort()
    {
      return _port;
    }
    
    public void setUsername(String username)
    {
      _username = username;
    }
    
    public String getUsername()
    {
      return _username;
    }
    
    public void setPassword(String password)
    {
      _password = password;
    }
    
    public String getPassword()
    {
      return _password;
    }
    
    public void setFolder(String folder)
    {
      _folder = folder;
    }
    
    public String getFolder()
    {
      return _folder;
    }
    
    public void setFileExtension(String fileExtension)
    {
      _fileExtension = fileExtension;
    }
    
    public String getFileExtension()
    {
      return _fileExtension;
    }
    
    public void setIsEnforceAttachment(boolean isEnforceAttachment)
    {
      _isEnforceAttachment = isEnforceAttachment;
    }
    
    public boolean getIsEnforceAttachment()
    {
      return _isEnforceAttachment;
    }
    
    public void setIsDeleteAttachment(boolean isDeleteAttachment)
    {
      _isDeleteAttachment = isDeleteAttachment;
    }
    
    public boolean getIsDeleteAttachment()
    {
      return _isDeleteAttachment;
    }
    
    public void setIsMultipleAttachment(boolean isMultipleAttachment)
    {
      _isMultipleAttachment = isMultipleAttachment;
    }
    
    public boolean getIsMultipleAttachment()
    {
      return _isMultipleAttachment;
    }
    
    public String toString()
    {
      return "FTPAttachmentInfo: host="+getHost()+" port="+getPort()+" username="+getUsername()+" password="+getPassword()+" folder="+getFolder()+" fileExtension="+getFileExtension()+" isEnforceAttachment="+getIsEnforceAttachment()+" isDeleteAttachment="+getIsDeleteAttachment()+" isMultipleAttachment="+getIsMultipleAttachment();
    }
  }
  
  
  //end of added by Ming Qian for attachment retrieval 20100816
  
  private class FTPFolderInfo
  {
	private String _be;
    private String _docType;
    private String _tp;
    private String _folder;
    private int _numFetch;
    private boolean _isMappingRequired;
    private String _xPath;
    private String _mappingRule;
    private static final String DOCTYPE= ".docType";
    private static final String BE = ".be";
    private static final String PARTNER = ".partner";
    private static final String FOLDER = ".folder";
    private static final String NUM_FILE = ".num.fetch";
    private static final String IS_MAPPING_REQUIRED = ".isMappingRequired";
    private static final String X_PATH = ".xPath";
    private static final String MAPPING_RULE = ".mappingRule";
    
    public FTPFolderInfo(String be, String tp, String docType, String folder, int numFetch, 
      boolean isMappingRequired, String xPath, String mappingRule)
    {
      setBe(be);
      setTp(tp);
      setDocType(docType);
      setFolder(folder);
      setNumFetch(numFetch);
      setIsMappingRequired(isMappingRequired);
      setXPath(xPath);
      setMappingRule(mappingRule);
    }
    
    public String toString()
    {
      return "be: "+getBe()+" tp: "+getTp()+" docType: "+getDocType()+" folder:"+getFolder()+" numFetch:"+getNumFetch() + 
        " isMappingRequired:"+getIsMappingRequired() + " xPath:"+getXPath() + " mappingRule:"+getMappingRule();
    }
    
    public int getNumFetch()
    {
      return _numFetch;
    }

    public void setNumFetch(int fetch)
    {
      _numFetch = fetch;
    }

    public String getFolder()
    {
      return _folder;
    }

    public void setFolder(String _folder)
    {
      this._folder = _folder;
    }

    public String getBe()
    {
      return _be;
    }

    public void setBe(String _be)
    {
      this._be = _be;
    }

    public String getDocType()
    {
      return _docType;
    }

    public void setDocType(String type)
    {
      _docType = type;
    }

    public String getTp()
    {
      return _tp;
    }

    public void setTp(String _tp)
    {
      this._tp = _tp;
    }
    
    public boolean getIsMappingRequired()
    {
      return _isMappingRequired;
    }
    
    public void setIsMappingRequired(boolean isMappingRequired)
    {
      	
      _isMappingRequired = isMappingRequired;
    }
    
    public String getXPath()
    {
      return _xPath;
    }
    
    public void setXPath(String xPath)
    {
      _xPath = xPath;	
    }
    
    public String getMappingRule()
    {
      return _mappingRule;  	
    }
    
    public void setMappingRule(String mappingRule)
    {
      _mappingRule = mappingRule;
    }
    
  }
  
  private class FTPServerInfo
  {
    private String _hostname;
    private int _port;
    private String _username;
    private String _password;
    private String _destFolder;
    private String _filenameFormat;
    private String _appendFilename;
    private String _embeddedContentTag;
    private String _embeddedContentTagFormat;
    private boolean _isBinaryTransfer;

    
    public FTPServerInfo(String hostname, int port, String username, String password, String destFolder)
    {
      setHostname(hostname);
      setPort(port);
      setPassword(password);
      setUsername(username);
      setDestFolder(destFolder);
    }

    public String toString()
    {
      return "hostname:"+getHostname()+" port:"+getPort()+" username:"+getUsername()+" password:"+getPassword()+" destFolder:"+getDestFolder()+" isBinaryTransfer:"+isBinaryTransfer();
    }
    
    public String getAppendFilename()
    {
      return _appendFilename;
    }

    public void setAppendFilename(String filename)
    {
      _appendFilename = filename;
    }

    public String getFilenameFormat()
    {
      return _filenameFormat;
    }

    public void setFilenameFormat(String format)
    {
      _filenameFormat = format;
    }

    public String getHostname()
    {
      return _hostname;
    }

    public void setHostname(String _hostname)
    {
      this._hostname = _hostname;
    }

    public String getPassword()
    {
      return _password;
    }

    public void setPassword(String _password)
    {
      this._password = _password;
    }

    public int getPort()
    {
      return _port;
    }

    public void setPort(int _port)
    {
      this._port = _port;
    }

    public String getUsername()
    {
      return _username;
    }

    public void setUsername(String _username)
    {
      this._username = _username;
    }

    public String getDestFolder()
    {
      return _destFolder;
    }

    public void setDestFolder(String folder)
    {
      _destFolder = folder;
    }

    public String getEmbeddedContentTag()
    {
      return _embeddedContentTag;
    }

    public void setEmbeddedContentTag(String contentTag)
    {
      _embeddedContentTag = contentTag;
    }

    public String getEmbeddedContentTagFormat()
    {
      return _embeddedContentTagFormat;
    }

    public void setEmbeddedContentTagFormat(String contentTagFormat)
    {
      _embeddedContentTagFormat = contentTagFormat;
    }

    public boolean isBinaryTransfer()
    {
      return _isBinaryTransfer;
    }

    public void setBinaryTransfer(boolean binaryTransfer)
    {
      _isBinaryTransfer = binaryTransfer;
    }
    
  }
  
}


