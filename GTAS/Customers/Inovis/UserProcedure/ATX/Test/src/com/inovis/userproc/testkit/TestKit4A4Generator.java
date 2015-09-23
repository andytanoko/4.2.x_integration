/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestKit4A4Generator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 4, 2009    Tam Wei Xiang       Created
 * Aug 30,2010    Tam Wei Xiang       Cater for attachment.
 */
package com.inovis.userproc.testkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.gridnode.ftp.FTPClientDelegate;
import com.gridnode.ftp.FTPParam;
import com.gridnode.ftp.exception.FTPConnectionException;
import com.gridnode.ftp.exception.FTPException;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * Test kit for generating test data and push to FTP server. This is mainly for simulating the 4A4 test data in ATX.
 * @author <developer name>
 * @version
 * @since
 */
public class TestKit4A4Generator
{
  private static final String FTP_HOST="ftp.host";
  private static final String FTP_PORT="ftp.port";
  private static final String FTP_USER="ftp.username";
  private static final String FTP_PASSWORD="ftp.password";
  private static final String VALID_FTP_STATUS_CODE = "valid.ftp.status.code";
  
  private static final int FTP_HOST_INDEX=0;
  private static final int FTP_PORT_INDEX=1;
  private static final int FTP_USER_INDEX=2;
  private static final int FTP_PASSWORD_INDEX=3;
  private static final int FTP_FOLDER_INDEX = 4;
  private static final int FTP_ATT_FOLDER_INDEX = 5;
  
  private Logger _logger = null;
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    // TODO Auto-generated method stub
    int interval = Integer.parseInt(args[0]);
    int numSend = Integer.parseInt(args[1]);
    int totalInterval = Integer.parseInt(args[2]);
    TestKit4A4Generator generator = new TestKit4A4Generator();
    
    for(int i = 0; i < totalInterval; i++)
    {
      generator.pushDocToFTP(numSend);
      try
      {
        Thread.sleep(interval * 1000);
      }
      catch(Exception ex) {}
    }
  }

  public TestKit4A4Generator()
  {
    _logger = getLogger();
  }
  
  public void pushDocToFTP(int numFile) throws Exception
  {
    String udocFilePath = "sample1.txt";
    String attachmentFilePath = "att.pdf";
    File udoc = new File(udocFilePath);
    File attachment = new File(attachmentFilePath);
    Properties ftpProps = loadDestFolderProperties("conf/ftpBackend.properties");
    String[] serverInfo = getFTPServerInfo(ftpProps, "test.partner.folderkey", "test.attachment.folderkey");
    ArrayList validFTPCodes = loadValidFTPCodes(ftpProps);
    
    String method = "pushDocToFTP";
    String attachmentPath = null;
    
    if(attachment.exists())
    {
      attachmentPath = udoc.getAbsolutePath();
    }
    
    for(int i =0; i < numFile; i++)
    {
      try
      {
        //20100830: handle attachment
        String filename = System.currentTimeMillis()+"_"+udoc.getName(); //the filename will be used by both transaction doc and attachment
        if(attachmentPath != null)
        {
          int responseCode = ftpSendToDestFolder(serverInfo[FTP_HOST_INDEX], Integer.parseInt(serverInfo[FTP_PORT_INDEX]), serverInfo[FTP_USER_INDEX], serverInfo[FTP_PASSWORD_INDEX], attachmentFilePath,filename , serverInfo[FTP_ATT_FOLDER_INDEX]);
          isValidFTP(responseCode, validFTPCodes, "attachment");
        }
        
        int responseCode = ftpSendToDestFolder(serverInfo[FTP_HOST_INDEX], Integer.parseInt(serverInfo[FTP_PORT_INDEX]), serverInfo[FTP_USER_INDEX], serverInfo[FTP_PASSWORD_INDEX], udoc.getAbsolutePath(), filename, serverInfo[FTP_FOLDER_INDEX]);
        _logger.logMessage(method, null, "FTP Response code:"+responseCode);
        isValidFTP(responseCode, validFTPCodes, "transaction doc");
       
        
        try
        {
          Thread.sleep(10);
        }
        catch(Exception ex) {}
      }
      catch(Throwable th)
      {
        _logger.logError("", method, null, "PushDocToFTP failed: "+th.getMessage(), th);
      }
    }
    
  }
  
  private void isValidFTP(int responseCode, ArrayList validFTPCodes, String coment) throws FTPServiceException
  {
    boolean isValidFTPResponse = isValidFTPCode(responseCode, validFTPCodes);
    if(! isValidFTPResponse)
    {
      throw new FTPServiceException("PushDocToFTP "+coment+" failed: response code: "+responseCode, null);
    }
  }
  
  public int ftpSendToDestFolder(
                                 String host,
                                 Integer port,
                                 String userName,
                                 String password,
                                 String fileName,
                                 String actualFilename,
                                 String destFolder
                                 ) throws FTPException,FTPServiceException
  {
    
    int returnCode=0;
    FTPClientDelegate delegate = new FTPClientDelegate();
    
    try
    {
      int count = 0;
      FTPParam ftpParam = intiFTPParam(host,port,userName,password,
                                                                            fileName,actualFilename,destFolder); //Initilize FTP Param.
      ftpParam.setDataConnTimeout(60 * 1000);
      ftpParam.setSocketTimeout(60 * 1000);
      ftpParam.setSocketTimeout(60 * 1000);
      
      count = ftpParam.getRetryCount();
      do
      {
        try
        {
          _logger.logMessage("", null, "Connecting to "+host+" port:"+port);
          delegate.doFTPConnect(ftpParam);
          
          _logger.logMessage("", null, "Connection establish to "+host+" port:"+port+" settingWorking dir to: "+destFolder+" sending file:"+ftpParam.getFileName());
          delegate.ftpSetCurrentDirectory(destFolder);
          delegate.doSendAndRename(ftpParam);
          
          returnCode = delegate.getFTPReplyCode();
          break;
        }
        catch(FTPServiceException ex) {
           returnCode = 1;
           break;
        }
        catch(FTPException ex) {
           returnCode = ex.getReplyCode();
        }
        catch(FTPConnectionException ex) {      //Connection Exception Perform Retry.{
           returnCode = 1;
           
        }
        catch(Exception ex) {
          returnCode = 1;
          break;
        }
      }while( --count>0 ); //End of while
    }
    catch(Exception ex)
    {
       ex.printStackTrace();
       returnCode = 1;
    }
    finally
    {
       try
       {
          delegate.doFTPConnectionClose();
       }
       catch(IOException ex){
          ex.printStackTrace();
       }
    }
    return returnCode;
  }
  
  private FTPParam intiFTPParam(String host, int port, String username, String password, String filename, String actualFilename, String destFolder)
  {
    FTPParam ftpParam = new FTPParam(host, port, username, password, filename, actualFilename, destFolder);
    ftpParam.setProvider("com.gridnode.ftp.adaptor.NetCommonsFTPClientManager");
    return ftpParam;
  }
  
  private Logger getLogger()
  {
    return (LoggerManager.getOneTimeInstance().getLogger("FTPUTIL", "TestKit4A4Generator"));
  }
  
  /*
  public int pushDocToFTP(int numFile) throws Throwable
  {
    
    String method = "pushDocToFTPForGeneratingTestData";
    
    String jmsPropsPath = SystemUtil.getWorkingDirPath()+"/gtas/data/userproc/ftpclient/jndi.properties";
    String ftpPropertiesPath = SystemUtil.getWorkingDirPath()+"/gtas/data/userproc/ftpclient/ftpBackend.properties";
    EventNotifier notifier = new EventNotifier(jmsPropsPath);
    int ftpResponseCode = 0; 
    String udocFilePath = "sample1.txt";
    
    _logger.logMessage(method, null, "filename: "+udocFilePath);
    
    for(int j = 0; j < numFile; j++)
    {
      //Generating test file from "sample1.txt"
      File udoc = new File(udocFilePath);
      byte[] testData = IOUtil.read(new FileInputStream(udoc));
      File tempFile = File.createTempFile(udoc.getName()+"_"+System.currentTimeMillis(), ".txt");
      
      
      String filepath = tempFile.getAbsolutePath();
      
      try
      {
        
        Properties ftpProps = loadDestFolderProperties(ftpPropertiesPath);
        ArrayList<Integer[]> validFTPCodes = loadValidFTPCodes(ftpProps);
        
        String folderKey = "test"+"."+"partner"+"."+"docType";
        String[] serverInfo = getFTPServerInfo(ftpProps, folderKey);
        
        
        if("".equals(serverInfo[FTP_FOLDER_INDEX]))
        {
           throw new Exception("Can't find the target FTP folder given folderKey:"+folderKey);
        }
            
        ftpResponseCode = pushToFTP(serverInfo[FTP_HOST_INDEX], Integer.parseInt(serverInfo[FTP_PORT_INDEX]), serverInfo[FTP_USER_INDEX], serverInfo[FTP_PASSWORD_INDEX], filepath, serverInfo[FTP_FOLDER_INDEX]);
        boolean isValidFTPResponse = isValidFTPCode(ftpResponseCode, validFTPCodes);
            
        _logger.logMessage(method, null,"responseCode: "+ftpResponseCode);
        if(! isValidFTPResponse)
        {
              throw new FTPServiceException("FTP action to folder "+serverInfo.getDestFolder()+" is not succeeded. Response code:"+ftpResponseCode);
        }
          
        }
        
        
      }
      catch(Throwable th)
      {
        if(th instanceof FTPServiceException)
        {
          String errorMessage = th.getMessage();
          //notifier.notifyDocumentToBackendFailed(tracingID, messageID, ftpResponseCode, errorMessage);
        }
        _logger.logError(ILoggerConstant.FTP_PUSH_FAILED, method, null, "Push Doc to FTP failed. Error: "+th.getMessage(), th);
        //notifyFTPPushFailed(udocFilename, messageID, tracingID, th.getMessage(), ExceptionUtil.getStackStraceStr(th), ftpPropertiesPath);
        throw new Exception("FTP Push failed, error: "+th.getMessage());
      }
      
      boolean isDeleted = tempFile.delete();
      _logger.logMessage(method, null, "Is temp file deleted: "+isDeleted);
    }
    
    return ftpResponseCode;
  }
  
  private int pushToFTP(String host, int port, String userName, String password,
                      String filename, String destFilePath) throws Exception
 {
     FTPClientDelegate delegate = new FTPClientDelegate();
     return delegate.ftpSendToDestFolder(host, port, userName, password, filename, destFilePath);
 }




  
   */
  
  private Properties loadDestFolderProperties(String propertiesPath) throws Exception
  {
    File props = new File(propertiesPath);
    Properties destFolderProperties = new Properties();
    destFolderProperties.load(new FileInputStream(props));
    return destFolderProperties;
  }
  
  private String[] getFTPServerInfo(Properties ftpProps, String folderKey, String attachmentFolderKey)
  {
    String ftpHost = ftpProps.getProperty(FTP_HOST);
    String ftpPort = ftpProps.getProperty(FTP_PORT);
    String user = ftpProps.getProperty(FTP_USER);
    String pw = ftpProps.getProperty(FTP_PASSWORD);
    String destFolder = ftpProps.getProperty(folderKey);
    String attachmentFolder = ftpProps.getProperty(attachmentFolderKey);
    
    return new String[]{ftpHost, ftpPort, user,pw, destFolder, attachmentFolder};
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
}
