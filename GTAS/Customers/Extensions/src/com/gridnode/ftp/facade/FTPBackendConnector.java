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
 */
package com.gridnode.ftp.facade;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;


import com.gridnode.ext.util.DocumentUtil;
import com.gridnode.ext.util.FileUtil;
import com.gridnode.ext.util.exceptions.ILoggerConstant;
import com.gridnode.ftp.FTPClientDelegate;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.ftp.helper.GdocHelper;
import com.gridnode.ftp.helper.XMLHelper;
import com.gridnode.ftp.notification.EmailNotifier;
import com.gridnode.ftp.notification.EventNotifier;
import com.gridnode.ftp.notification.HTTPBCNotifier;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;


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
  
  private static final int NUM_FILE_RETRIEVE_DEF = 10;
  private static final String FTP_CLIENT_PROVIDER = "ftp.client.provider";
  
  private static final String NUM_FOLDER_RETRIEVE = "num.doc.folder.retrieve"; //for FTP pull
  private static final String NUM_FILE_FETCH = "num.fetch";
  
  private static final String VALID_FTP_STATUS_CODE = "valid.ftp.status.code";
  private Logger _logger = null;
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    // TODO Auto-generated method stub
    String host = "xxxxx";
    int port = 21;
    String userName = "xxxx";
    String password = "xxxx";
    String filename = "c:/ftpBackend.properties";
    String be = "dell";
    String partner = "intel";
    String documentType = "4A5";
    String destPropsPath = "ftpBackend.properties";
    String jmsPropsPath = "jms_ftpbackend.properties";
    
    //Push test case
    /*
    FTPBackendConnector conn = new FTPBackendConnector();
    Properties ftpProps = new Properties();
    ftpProps.load(new FileInputStream(new File("conf/ftpBackend.properties")));
    
    ArrayList ftpCodes = conn.loadValidFTPCodes(ftpProps);
    System.out.println("is valid: "+conn.isValidFTPCode(143, ftpCodes)); */
    
    /*
    FTPBackendConnector conn = new FTPBackendConnector();
    long time = conn.getHouseKeepInterval();
    System.out.println(new Date(time)); */
  }
  
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
          
          ftpResponseCode = pushToFTP(serverInfo.getHostname(), serverInfo.getPort(), serverInfo.getUsername(), serverInfo.getPassword(), filepath, actualFilename, serverInfo.getDestFolder());
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
  
  private void notifyFTPPullFailed(String errorMsg, String stackTrace, String emailProps)
  {
    String method = "";
    EmailNotifier notifier = new EmailNotifier(emailProps);
    try
    {
      notifier.triggerFTPPullFailedEmail(errorMsg, stackTrace, null); //TODO: FIXME Added null parameter to prevent compile error
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
                       String filename, String actualFilename, String destFilePath) throws Exception
  {
      FTPClientDelegate delegate = new FTPClientDelegate();
      return delegate.ftpSendToDestFolder(host, port, userName, password, filename, actualFilename, destFilePath, true);
  }
  
  /**
   * Pull the document from the folder that define in "ftpBackend.properties".
   * @throws Exception
   */
  public void pullDocFromFTP() throws Exception
  {
    String method = "pullDocFromFTP";
    String ftpPropsPath = "./conf/ftpBackend.properties";
    String jndiPropsPath = "./conf/jndi.properties";
    String ftpClientPropsPath = "./conf/ftpclient.properties";
    File outToHTTPBC = new File("outToHTTPBC");
    
    try
    {
      //try to lock the file so that we can ensure only one process is accessing the
      //ATX server even given in cluster env
      if(! FileUtil.lockFile(new File("file.lock")))
      {
        _logger.logMessage(method, null, "Other process is currently accessing the ATX. wait for the next scheduled time.");
        return;
      }
      
      HTTPBCNotifier httpbc = new HTTPBCNotifier(jndiPropsPath);
      Properties ftpProps = loadDestFolderProperties(ftpPropsPath);
      Properties ftpClientProps = loadDestFolderProperties(ftpClientPropsPath);
      //String ftpClientProvider = getFTPClientProvider(ftpClientProps);
      
      String folderKey = "";
      FTPServerInfo[] infos = getFTPServerInfo(folderKey, ftpProps);
      if(infos.length == 0)
      {
        return;
      }
      
      FTPServerInfo info = infos[0];
      FTPInfo[] ftpInfos = getFTPInfo(ftpProps, info);
      
      if(ftpInfos != null && ftpInfos.length > 0)
      {
        for(FTPInfo ftpInfo : ftpInfos)
        {
          FTPFolderInfo folderInfo = ftpInfo.getFolderInfo();
          String folder = folderInfo.getFolder();
          String be = folderInfo.getBe();
          String partner = folderInfo.getTp();
          String docType = folderInfo.getDocType();
         
          _logger.logMessage(method, null, "Working on FTPFolder: "+folderInfo);
          _logger.logMessage(method, null, "FTPServerInfo: "+info);
          
          FTPClientDelegate delegate = new FTPClientDelegate();
          delegate.setFTPClientPropPath(ftpClientPropsPath);
          /*
          FTPParam ftpParam = new FTPParam(info.getHostname(), info.getPort(), info.getUsername(), info.getPassword(), null, null, null);
          ftpParam.setProvider(ftpClientProvider);
          delegate.doFTPConnect(ftpParam);
          _logger.debugMessage(method, null, "Connection established,set working folder to:"+folder);
          
          delegate.ftpSetCurrentDirectory(folder); 
          String[] filenames = delegate.ftpRetrieveListOfFilename("", folderInfo.getNumFetch()); */
          
          //#132 TWX 13032009 use the variant of retrieve list of filename that allow specify the timeout props 
          String[] filenames = delegate.ftpRetrieveListOfFilename(info.getHostname(), info.getPort(), 
                                                                  info.getUsername(), info.getPassword(), folder, folderInfo.getNumFetch());
          
          if(filenames != null && filenames.length > 0)
          {
            //#133 Make FTP pull operation more atomic NOTE: It is not under a JTA transaction
            for(String filename : filenames)
            {
              File processedFile = null;
              _logger.debugMessage(method, null, "Filename retrieved: "+filename);
              if(isFileProcessed(new File(outToHTTPBC.getAbsolutePath()+"/"+filename)))
              {
                _logger.logMessage(method, null, "File :"+filename+" has been processed before, ignored.");
                processedFile = new File(outToHTTPBC.getAbsolutePath()+"/"+filename);
              }
              else
              {
                byte[] docForImport = delegate.getFileInByte(filename);
                _logger.debugMessage(method, null, "Delegate to HTTPBC with filename: "+filename);
                httpbc.notifyHTTPBCToReceive(filename, docForImport, be, partner, docType);
                processedFile = FileUtil.saveFiles(outToHTTPBC, filename, docForImport);
              }
              
              delegate.removeFile(filename);
              _logger.logMessage(method, null, "Removed file:"+filename+" from ATX.");
              processedFile.delete();
              
              
            }
          }
          else
          {
            _logger.logMessage(method, null, "No files can be found in folder: "+folder);
          }
        }
      }
      
      //#133 cleanup the file in the outToHTTPBC
      FileUtil.deleteFiles(outToHTTPBC, getHouseKeepInterval());
      _logger.debugMessage(method, null, "Housekeep "+outToHTTPBC.getAbsolutePath()+ " completed.");
    }
    catch(Throwable ex)
    {
      _logger.logError(ILoggerConstant.FTP_PULL_FAILED, method, null, "Error in pulling doc from FTP. Error: "+ex.getMessage(), ex);
      notifyFTPPullFailed(ex.getMessage(), ExceptionUtil.getStackStraceStr(ex), ftpPropsPath);
      throw new Exception("FTP Pull is failed ! "+ex.getMessage());
    }
  }
  
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
        
        String folder = props.getProperty(folderKey);
        String be = props.getProperty(beKey);
        String partner = props.getProperty(partnerKey);
        String docType = props.getProperty(docTypeKey);
        String numFetch = props.getProperty(numFileKey, defNumFileFetch);
        
        FTPFolderInfo folderInfo = new FTPFolderInfo(be,partner, docType, folder, Integer.parseInt(numFetch));
        
        ftpInfo[i-1] = new FTPInfo(defServerInfo, folderInfo);
      }
      
      return ftpInfo;
    }
  }
  
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
  
  private class FTPFolderInfo
  {
    private String _be;
    private String _docType;
    private String _tp;
    private String _folder;
    private int _numFetch;
    private static final String DOCTYPE= ".docType";
    private static final String BE = ".be";
    private static final String PARTNER = ".partner";
    private static final String FOLDER = ".folder";
    private static final String NUM_FILE = ".num.fetch";
    
    public FTPFolderInfo(String be, String tp, String docType, String folder, int numFetch)
    {
      setBe(be);
      setTp(tp);
      setDocType(docType);
      setFolder(folder);
      setNumFetch(numFetch);
    }
    
    public String toString()
    {
      return "be :"+getBe()+" tp: "+getTp()+" docType: "+getDocType()+" folder:"+getFolder()+" numFetch:"+getNumFetch();
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
      return "hostname:"+getHostname()+" port:"+getPort()+" username:"+getUsername()+" password:"+getPassword()+" destFolder:"+getDestFolder();
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
    
    
    
  }
  
}


