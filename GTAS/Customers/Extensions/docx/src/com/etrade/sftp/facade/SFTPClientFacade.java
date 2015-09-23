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
 * File: SFTPBackendConnector.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17, 2010   <Developer Name>       Created
 */
package com.etrade.sftp.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.vfs.FileObject;

import com.etrade.sftp.bl.SFTPClient;
import com.etrade.sftp.exception.SFTPClientException;
import com.etrade.sftp.model.SFTPConnectInfo;
import com.etrade.sftp.util.SFTPLogger;
import com.gridnode.ext.util.DocumentUtil;
import com.gridnode.ftp.notification.EmailNotifier;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.StringUtil;
import com.gridnode.util.SystemUtil;

/**
 * @author Eric Loh
 * @version
 * @since
 * Will use this as the Interface between GridTalk and the SFTPClient
 */
/**
 * @author <developer name>
 * @version
 * @since
 */
public class SFTPClientFacade
{
  private static final String CLASS_NAME = "SFTPClientFacade";

  //Use properties file to contain SFTP Connection Info
  public static final String SFTP_CLIENT_HOME = SystemUtil.getWorkingDirPath() + "/gtas/data/userproc/sftpclient/";
  public static final String SFTP_PRIVATE_KEY_HOME = SFTP_CLIENT_HOME + "/userkeys/"; 
  public static final String SFTP_PROPERTIES_PATH = SFTP_CLIENT_HOME + "sftp.properties";
  
  //Properties Keys
  private static final String DELIM = ".";
  
  private static final String SFTP_HOST = "sftp.host";
  private static final String SFTP_PORT = "sftp.port"; //if not the default port
  private static final String SFTP_USER = "sftp.username";
  private static final String SFTP_PASSWORD = "sftp.password";
  
  private static final String SFTP_REMOTE_FOLDER = "sftp.remote.folder";
  private static final String SFTP_LOCAL_FOLDER = "sftp.local.folder";
  private static final String SFTP_PRIVATE_KEY_FILE = "sftp.private.key"; //TODO: Assume only 1 private key file
  
  //Folder keys used by DocumentUtil
  public static final String INBOUND_FOLDER = "Inbound";
  public static final String OUTBOUND_FOLDER = "Outbound";
  public static final String EXPORT_FOLDER = "Export";
  public static final String IMPORT_FOLDER = "Import";
  
  //TODO Follow Error Codes of FTPBackendConnector
  public static final int SFTP_SUCCESS = 0;
  public static final int SFTP_ERROR = 1;
    
  /**
   * Push Document to SFTP Server
   * @param gridDocumentObject
   * @return
   * @throws Throwable
   */
  public int pushDocToSFTP(Object gridDocumentObject)
  {
    return this.pushDocToSFTP(gridDocumentObject, "");
  }

  /**
   * Push Document to SFTP Server
   * Allow setting a remoteFileName to override the default of using uDocFileName
   * @param gridDocumentObject
   * @param remoteFileName
   * @return
   */
  public int pushDocToSFTP(Object gridDocumentObject, String remoteFileName)
  {
    //////////////////////////////////////////////////////////
    String logPrefix = CLASS_NAME + ".pushDocToSFTP - ";
    SFTPLogger.debug(logPrefix + "started");
    
    SFTPClient client = null;
    SFTPConnectInfo connectInfo = null;
    File identities[] = null;
    
    boolean disableStrictHostKeyCheck = true;
    boolean userDirAsRoot = true;
    
    String propertyKeyPrefix = null; 
    //////////////////////////////////////////////////////////
    
    GridDocument gridDocument = (GridDocument) gridDocumentObject;
    
    String uDocFileName = gridDocument.getUdocFilename(); //fileName
    String folder = gridDocument.getFolder();

    String gridDocumentId = "" + gridDocument.getGdocId();
    String messageId = DocumentUtil.getMessageID(folder, gridDocumentId);
    String tracingId = gridDocument.getTracingID();
    
    String localFilePath = DocumentUtil.getUDocFilePath(folder, uDocFileName); 
    
    String sender = DocumentUtil.getOwnBEID(folder, gridDocument.getRecipientBizEntityId(), gridDocument.getSenderBizEntityId(), gridDocument.getSrcFolder());
    
    String partnerId = DocumentUtil.getPartnerID(folder, gridDocument.getRecipientPartnerId(), gridDocument.getSenderPartnerId(), gridDocument.getSrcFolder());
    //////////////////////////////////////////////////////////
    
    SFTPLogger.debug(logPrefix + "messageId: [" + messageId + "]");
    SFTPLogger.debug(logPrefix + "uDocFileName: [" + uDocFileName + "] folder: [" + folder + "] localFilePath: [" + localFilePath + "]");
    SFTPLogger.debug(logPrefix + "sender: [" + sender + "] partnerId: [" + partnerId + "]");
    
    try
    {
      File uDocFile = new File(localFilePath);
      if (!uDocFile.exists())
      {
        throw new SFTPClientException(" uDoc File: [" + uDocFile.getAbsolutePath() + "] cannot be found.");
      }      
      
      //Derive Connection Info
      propertyKeyPrefix = this.getPropertyKeyPrefix(sender, partnerId, gridDocument.getUdocDocType(), "");      
      SFTPLogger.debug(logPrefix + "propertyKeyPrefix: [" + propertyKeyPrefix + "]");

      connectInfo = this.getConnectInfo(propertyKeyPrefix);
      
      if (StringUtil.isBlank(remoteFileName)) //if remoteFileName not provided, use uDocFileName
      {
        remoteFileName = uDocFileName; //Use uDocFileName as remoteFileName
      }
      connectInfo.setRemoteFileName(remoteFileName); //Use uDocFileName as remoteFileName
        
      //Populate identities with private keys if its present
      if (connectInfo.getPrivateKeyFiles() != null && connectInfo.getPrivateKeyFiles().size() > 0)
      {
        identities = new File[connectInfo.getPrivateKeyFiles().size()];
        
        for (int i = 0; i < identities.length; i++)
        {
          identities[i] = connectInfo.getPrivateKeyFile(i);
        }
        
        client = new SFTPClient(connectInfo.getUserName(), identities, disableStrictHostKeyCheck, userDirAsRoot);
      }
      else //else try using password
      {
        client = new SFTPClient(connectInfo.getUserName(), connectInfo.getPassword(), disableStrictHostKeyCheck, userDirAsRoot);
      }
      
      client.uploadFile(connectInfo.getHostName(), connectInfo.getPort(), connectInfo.getRemoteFolder(), localFilePath, connectInfo.getRemoteFileName());      
    }
    catch (SFTPClientException e)
    {
      String errorMessage = "Exception at pushDocToSFTP: ";
      
      SFTPLogger.error(errorMessage + ExceptionUtil.getStackStraceStr(e));      
      this.sendEmail(uDocFileName, messageId, tracingId, errorMessage, ExceptionUtil.getStackStraceStr(e), propertyKeyPrefix, SFTP_PROPERTIES_PATH);
      
      return SFTP_ERROR;
    }
    SFTPLogger.debug(logPrefix + "done");
    return SFTP_SUCCESS;
  }
  
  /**
   * Assume using default parameters from sftp.properties
   */
  public void pullDocFromSFTP()
  {
    //////////////////////////////////////////////////////////
    String logPrefix = CLASS_NAME + ".pullDocFromSFTP - ";
    SFTPLogger.debug(logPrefix + "started");
    
    SFTPClient client = null;
    SFTPConnectInfo connectInfo = null;
    File identities[] = null;
    
    boolean disableStrictHostKeyCheck = true;
    boolean userDirAsRoot = true;
    
    boolean deleteAfterDownload = true; //TODO: Indicate to delete remote file after downloading
    
    //////////////////////////////////////////////////////////
    
    try
    {
      connectInfo = this.getConnectInfo(""); //Use default parameters
      
      if (StringUtil.isBlank(connectInfo.getLocalFolder()))
      {
        throw new SFTPClientException("Local Folder to download files to is missing.");
      }
      //Populate identities with private keys if its present
      if (connectInfo.getPrivateKeyFiles() != null && connectInfo.getPrivateKeyFiles().size() > 0)
      {
        identities = new File[connectInfo.getPrivateKeyFiles().size()];
        
        for (int i = 0; i < identities.length; i++)
        {
          identities[i] = connectInfo.getPrivateKeyFile(i);
        }
        
        client = new SFTPClient(connectInfo.getUserName(), identities, disableStrictHostKeyCheck, userDirAsRoot);
      }
      else //else try using password
      {
        client = new SFTPClient(connectInfo.getUserName(), connectInfo.getPassword(), disableStrictHostKeyCheck, userDirAsRoot);
      }
                  
      client.downloadFiles(connectInfo.getHostName(), connectInfo.getRemoteFolder(), connectInfo.getLocalFolder(), deleteAfterDownload);
    }
    catch (SFTPClientException e)
    {
      String errorMessage = "Exception at pullDocFromSFTP: ";
      
      SFTPLogger.error(errorMessage + ExceptionUtil.getStackStraceStr(e));      
    }
  }
  
  /**
   * List files in the relativeFolder
   * @param relativeFolderPath - use this path if provided, if not use path in sftp.properties
   * @return Array of FileObject
   */
  public FileObject[] listFilesFromSFTP(String relativeFolderPath)
  {
    FileObject[] result = null;
    //////////////////////////////////////////////////////////
    String logPrefix = CLASS_NAME + ".listFilesFromSFTP - ";
    SFTPLogger.debug(logPrefix + "started");
    
    SFTPClient client = null;
    SFTPConnectInfo connectInfo = null;
    File identities[] = null;
    
    boolean disableStrictHostKeyCheck = true;
    boolean userDirAsRoot = true;
    
    //////////////////////////////////////////////////////////
    try
    {
      connectInfo = this.getConnectInfo(""); //Use default parameters
      
      //Populate identities with private keys if its present
      if (connectInfo.getPrivateKeyFiles() != null && connectInfo.getPrivateKeyFiles().size() > 0)
      {
        identities = new File[connectInfo.getPrivateKeyFiles().size()];
        
        for (int i = 0; i < identities.length; i++)
        {
          identities[i] = connectInfo.getPrivateKeyFile(i);
        }
        
        client = new SFTPClient(connectInfo.getUserName(), identities, disableStrictHostKeyCheck, userDirAsRoot);
      }
      else //else try using password
      {
        client = new SFTPClient(connectInfo.getUserName(), connectInfo.getPassword(), disableStrictHostKeyCheck, userDirAsRoot);
      }
      
      if (StringUtil.isBlank(relativeFolderPath)) //TODO Use remoteFolder from properties if relativeFolderPath is not provided
      {
        relativeFolderPath = connectInfo.getRemoteFolder();
      }
      result = client.listFiles(connectInfo.getHostName(), connectInfo.getPort(), relativeFolderPath);
    }
    catch (SFTPClientException e)
    {
      String errorMessage = "Exception at pullDocFromSFTP: ";
      
      SFTPLogger.error(errorMessage + ExceptionUtil.getStackStraceStr(e));      
    }
    finally
    {
      client.release(); //need to call release() explicitly for listFiles as listFiles is used internally by downloadFiles() in SFTPClient
    }
    return result;
  }
  
  /**
   * Derives the connection info for sFTP
   * @param sender
   * @param partnerId
   * @param docType
   * @param addKey - Additional Key - If provided, append this to the key
   * @return
   * @throws SFTPClientException 
   */
  private SFTPConnectInfo getConnectInfo(String propertyKeyPrefix) throws SFTPClientException
  {
    String logPrefix = CLASS_NAME + ".getConnectInfo - ";
    SFTPLogger.debug(logPrefix + "started");
    
    SFTPConnectInfo connectInfo = new SFTPConnectInfo();
    Properties sftpProperties = null;
    String password = null;
    
    try
    {
      sftpProperties = loadProperties(SFTP_PROPERTIES_PATH);
    }
    catch (FileNotFoundException e)
    {
      throw new SFTPClientException(e);
    }
    catch (IOException e)
    {
      throw new SFTPClientException(e);
    }
    
    if (sftpProperties != null && !sftpProperties.isEmpty())
    {            
      //HOST and PORT
      connectInfo.setHostName(sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_HOST, sftpProperties.getProperty(SFTP_HOST)));
      connectInfo.setPort(sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_PORT, sftpProperties.getProperty(SFTP_PORT)));

      SFTPLogger.debug(logPrefix + "hostName: [" + connectInfo.getHostName() + "]");

      //USER NAME
      connectInfo.setUserName(sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_USER, sftpProperties.getProperty(SFTP_USER)));
      
      SFTPLogger.debug(logPrefix + "userName: [" + connectInfo.getUserName() + "]");

      //REMOTE FOLDER (relative to User Home Directory)
      connectInfo.setRemoteFolder(sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_REMOTE_FOLDER, sftpProperties.getProperty(SFTP_REMOTE_FOLDER)));
      
      //LOCAL FOLDER (For downloads)
      connectInfo.setLocalFolder(sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_LOCAL_FOLDER, sftpProperties.getProperty(SFTP_LOCAL_FOLDER)));
      
      //PASSWORD
      password = sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_PASSWORD, sftpProperties.getProperty(SFTP_PASSWORD));
      if (!StringUtil.isBlank(password))
      {
        connectInfo.setPassword(password.toCharArray());
      }
      
      //PRIVATE KEY FILE PATH (Assume relative to SFTP_PRIVATE_KEY_HOME)
      String privateKeyFilePath = sftpProperties.getProperty(propertyKeyPrefix + DELIM + SFTP_PRIVATE_KEY_FILE, sftpProperties.getProperty(SFTP_PRIVATE_KEY_FILE));
      
      if (!StringUtil.isBlank(privateKeyFilePath)) //TODO: Must be an absolute path
      {
        File privateKeyFile = new File(SFTP_PRIVATE_KEY_HOME + privateKeyFilePath);
        if (!privateKeyFile.exists())
        {
          SFTPLogger.error(logPrefix + "privateKey File does not exists: [" + privateKeyFile.getAbsolutePath() +"]");
        }
        else
        {
          connectInfo.addPrivateKeyFile(privateKeyFile);  
        }
        
        SFTPLogger.debug(logPrefix + "privateKeyFilePath: [" + privateKeyFile.getAbsolutePath() + "]");
      }      
    }
    else
    {
      throw new SFTPClientException("Please check -- SFTP Properties file is either empty or cannot be loaded: [" + SFTP_PROPERTIES_PATH + "]");        
    }
    return connectInfo;
  }
  
  /**
   * @param sender
   * @param partnerId
   * @param documentType
   * @param addKey
   * @return
   */
  private String getPropertyKeyPrefix(String sender, String partnerId, String documentType, String addKey)
  {
    String propertyKeyPrefix = null;
    
    if (!StringUtil.isBlank(addKey))
    {
      addKey = DELIM + addKey;
    }
    else
    {
      addKey = "";
    }
    
    propertyKeyPrefix = sender + DELIM + partnerId + DELIM + documentType + addKey;
    
    return propertyKeyPrefix;
  }
  
  /**
   * 
   */
  private void sendEmail(String udocFileName, String messageId, String tracingId, String errorMessage, String stackTrace, String folderKey, String propertiesPath)
  {
    EmailNotifier email = new EmailNotifier(propertiesPath);
    
    try
    {
      email.triggerEmail(udocFileName, messageId, tracingId, errorMessage, stackTrace, folderKey);
    }
    catch (Exception e)
    {
      SFTPLogger.error(CLASS_NAME + ".sendEmail - Failed to trigger Email");
      SFTPLogger.error(ExceptionUtil.getStackStraceStr(e));
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //Re-usable functions (temporarily placed here)
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////  
  /**
   * @param filePath
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static Properties loadProperties(String filePath) throws FileNotFoundException, IOException
  {
      return loadProperties(new File(filePath));
  }
  
  /**
   * @param filePath
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static Properties loadProperties(File file) throws FileNotFoundException, IOException
  {
      Properties properties = new Properties();
      
      properties.load(new FileInputStream(file));
      
      return properties;
  }
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * @param args
   */
  public static void main(String args[])
  {
    SFTPLogger.debug(SFTPClientFacade.class.getSimpleName() + " started");
    
    SFTPClientFacade facade = new SFTPClientFacade();
    
    GridDocument gridDocument = new GridDocument();
    
    gridDocument.setUdocFilename("Dummy_ACORD2.xml");
    gridDocument.setFolder(SFTPClientFacade.OUTBOUND_FOLDER);
    
    gridDocument.setSenderBizEntityId("QBE_PORTAL");
    gridDocument.setRecipientPartnerId("TXC_DOCX");
    gridDocument.setUdocDocType("MCI_DOC");
    
    //TODO: Load Test
    int LOAD_SIZE = 10;
    for (int i = 0; i < LOAD_SIZE; i++)
    {
      String remoteFileName = "loadtest_" + i + ".xml";
      facade.pushDocToSFTP(gridDocument, remoteFileName);  
    }    
    
    FileObject[] fileObjects = facade.listFilesFromSFTP("upload/Defaultfolder");
    
    for (FileObject childFileObj: fileObjects)
    {
        String relativePath = File.separatorChar + childFileObj.getName().getBaseName();
        
        SFTPLogger.debug(SFTPClientFacade.class.getSimpleName() + " relativePath: " + relativePath);
    }
    
    facade.pullDocFromSFTP();
    
    SFTPLogger.debug(SFTPClientFacade.class.getSimpleName() + " done");
  }
}
