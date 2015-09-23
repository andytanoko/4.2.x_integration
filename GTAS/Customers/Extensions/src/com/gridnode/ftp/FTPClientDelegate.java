/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFTPClientManager.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 * Jul 21 2003    Jagadeesh               Modified : Enhanced ftpSend to send
 *                                        to a destination folder.
 *
 *                                        Modified : if could not login to ftp server
 *                                        then return status code - 530: insted of
 *                                        retry to login.
 * Feb 05 2009    Tam Wei Xiang           #125: added method ftpRetrieveListOfFilename(...)
 *                                                           ftpSendToDestFolder(...)
 * Mar 10 2009    Tam Wei Xiang           #130 Modified method ftpSendToDestFolder(..) to allow 
 *                                        user specify the destfilename.  
 * Mar 13 2009    Tam Wei Xiang           #132 Change to use the ftpClientManager that allow setting
 *                                        for socket timeout
 * Oct 20 2010    Tam Wei Xiang           #1830 Support the using of passive mode explicitly                                                                                               
 * Nov 22 2010    Tam Wei Xiang           #1977 Support the use of binary transfer mode. 
 * Dec 08 2010    Alain Ah Ming           #2020 Fixed ftpRetrieveListOfFilename(...) methods to pass "." to 
 *                                              the FTP client manager instead of an empty string which 
 *                                              would return an empty file list.                                                                                              
 */


package com.gridnode.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPFile;

import sun.reflect.generics.tree.FormalTypeParameter;

import com.gridnode.ftp.exception.FTPConnectionException;
import com.gridnode.ftp.exception.FTPException;
import com.gridnode.ftp.exception.FTPServiceException;
import com.gridnode.ftp.facade.IFTPClientManager;
import com.gridnode.ftp.reply.FTPReply;

/**
 * This class acts as a Client Delegate which adapts to the FTP Client Provider.
 * All the method calls are delegated to concrete IFTPClientManager impli class.
 *
 */


public class FTPClientDelegate
{

  private String _ftpClientPropsPath = null;
  private boolean _isUsePassiveMode = false;//whether to use passive mode explicitly
  
  private static Properties _ftpConfig = null;
  private IFTPClientManager _ftpClientManager = null;
  //private FTPParam _ftpParam = null;
  private static boolean debug = true;
  private static final int DEFAULT_FTP_PORT = 21;
  //private static Category _logger = Category.getInstance(FTPClientDelegate.class);


  public FTPClientDelegate(IFTPClientManager ftpClient)
  {
    _ftpClientManager = ftpClient;
  }


  public FTPClientDelegate()
  {
  }


  /**
   * ftpSend performs two specific operations, 1. To FTP Send 2. To Check whether
   * to rename the file, if set then rename the file with specified Rename Prefix,
   * of the new file, to the no of positions from source file name prefix.
   *
   * @param host - Host of FTP Server.
   * @param port - Port of FTP Server (Ex. 21)
   * @param userName - UserName to Login to FTP Server.
   * @param password - Password to Login to FTP Server.
   * @param fileName - FileName to send to FTP Server (Temp file).
   * @param actualFileName - ActualFile name (in relationto fileName parameter -
   * where fileName is full path to temp file).
   * @return - return Code (as per FTP (RFC 986) ) Standard.
   * @throws FTPException thrown when could not perform FTP Command.
   * @throws FTPServiceException thrown upon Initilization error.
   */

  public int ftpSend(
  String host,
  Integer port,
  String userName,
  String password,
  String fileName,
  String actualFileName
  ) throws FTPException,FTPServiceException
  {
    //boolean renameRemoteFile = false;
    int returnCode=0;
    //String srcFileName=null;
    try
    {
      int count = 0;
      FTPParam ftpParam = intiFTPParam(host,port,userName,password,
                                             fileName,actualFileName,null,null, false); //Initilize FTP Param.
      count = ftpParam.getRetryCount();
      do
      {
         try
         {
            doFTPConnect(ftpParam);               //Connect to FTP Server
            doSendAndRename(ftpParam);            //Perform Send And Rename
            returnCode = getFTPReplyCode();       //Get Reply Code
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
           log("Retry To Perform Command To FTP Server-->");
         }
         catch(Exception ex) {
           returnCode = 1;
           break;
         }
      }while( --count>0 ); //End of while
    }
    catch(FTPServiceException ex)
    {
      ex.printStackTrace();
      returnCode = 1;
    }
    finally
    {
      try{
        doFTPConnectionClose();
      }catch(IOException ex){
        ex.printStackTrace();
      }
    }
    return returnCode;
  }
  
  /**
   * TWX 03022008 1) FTP the file to the subfolder 2. To Check whether
   * to rename the file, if set then rename the file with specified Rename Prefix,
   * of the new file, to the no of positions from source file name prefix.
   * 
   * @param host - Host of FTP Server.
   * @param port - Port of FTP Server (Ex. 21)
   * @param userName - UserName to Login to FTP Server.
   * @param password - Password to Login to FTP Server.
   * @param fileName - FileName to send to FTP Server (Temp file).
   * @param actualFilename - the filename that will be stored in the dest folder
   * @param destFolder The subfolder that the file will be stored to.
   * @param isBinaryTransfer - Indicate whether transfer the document content in binary mode. Default will be using
   *                           ASCII mode.
   * @return return Code (as per FTP (RFC 986) ) Standard.
   * @throws FTPException
   * @throws FTPServiceException
   */
  public int ftpSendToDestFolder(
                     String host,
                     Integer port,
                     String userName,
                     String password,
                     String fileName,
                     String actualFilename,
                     String destFolder,
                     boolean isBinaryTransfer) throws FTPException,FTPServiceException
  {
     //boolean renameRemoteFile = false;
     int returnCode=0;
     //String srcFileName=null;
     try
     {
       int count = 0;
       
       if(actualFilename == null || "".equals(actualFilename.trim()))
       {
         actualFilename = null;
       }
       
       FTPParam ftpParam = intiFTPParam(host,port,userName,password,
                                                                fileName,actualFilename,null,destFolder, isBinaryTransfer); //Initilize FTP Param.
       
       count = ftpParam.getRetryCount();
       do
       {
         try
         {
           doFTPConnect(ftpParam);               //Connect to FTP Server
           doSendAndRename(ftpParam);            //Perform Send And Rename
           returnCode = getFTPReplyCode();       //Get Reply Code
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
           log("Retry To Perform Command To FTP Server-->");
         }
         catch(Exception ex) {
           returnCode = 1;
           break;
         }
         }while( --count>0 ); //End of while
     }
     catch(FTPServiceException ex)
     {
       ex.printStackTrace();
       returnCode = 1;
     }
     finally
     {
       try
       {
         doFTPConnectionClose();
       }
       catch(IOException ex){
         ex.printStackTrace();
       }
     }
     return returnCode;
   }
  
  /**
   * #132 TWX 13032009 Retrieve list of filenames in the given subDirectory
   * @param hostname Host of FTP Server
   * @param port Port of FTP Server (Ex. 21)
   * @param username UserName to Login to FTP Server
   * @param password Password to Login to FTP Server.
   * @param subDirectory The sub folder within the root folder we FTP to.
   * @param pageSize indicate the total number of filename we will fetch. This significantly
   *                 reduce the overhead of fetching all the file in the folder.
   * @return list of filename including folder name
   * @throws FTPServiceException
   * @throws FTPException
   */
  public String[] ftpRetrieveListOfFilename(String hostname, Integer port, String username, String password,
                                          String subDirectory, int pageSize) throws FTPServiceException, FTPException
  {
    try
    {
      FTPParam ftpParam = intiFTPParam(hostname,port,username,password,
                                                               null,null,getFTPClientPropPath(),null, false); //Initilize FTP Param.
      doFTPConnect(ftpParam);               //Connect to FTP Server
      setTimeOut(ftpParam);
      ftpSetCurrentDirectory(subDirectory);
      return ftpRetrieveListOfFilename(".", pageSize);
    }
    catch(FTPServiceException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new FTPException("Error in retrieving list of filenames "+ex.getMessage(), ex);
    }
  }
  
  /**
   * TWX 03022008 Retrieve a list of filename from the given subFolder.
   * @param subFolder The sub folder within the root folder we FTP to.
   * @param pageSize indicate the total number of filename we will fetch. This significantly
   *                 reduce the overhead of fetching all the file in the folder.
   * @return list of filename including folder name
   * @throws Exception
   */
  public String[] ftpRetrieveListOfFilename(String subFolder, int pageSize) throws Exception
  {
          ArrayList<String> fileNames = null;
          
          FTPFile[] files = _ftpClientManager.listFilesByPage(subFolder, pageSize);
          if(files != null && files.length > 0)
          {
            fileNames = new ArrayList<String>();
            for(FTPFile file : files)
            {
              if(file.isFile())
              {
                String ftpFilename = file.getName();
                fileNames.add(ftpFilename);
              }
            }
            
            return fileNames.toArray(new String[]{});
          }
          else
          {
            return null;
          }
  }
  
  /**
   * #132 TWX 13032009, set the Data connection timeout, and soTimeout
   * @param param
   */
  private void setTimeOut(FTPParam param)
  {
    _ftpClientManager.setDataConnTimeout(param.getDataConnTimeout());
    _ftpClientManager.setSoTimeout(param.getSoTimeout());
  }
  
  public byte[] getFileInByte(String filename) throws Exception
  {
    return _ftpClientManager.getBinaryFile(filename);
  }
  
  public void removeFile(String filename) throws Exception
  {
    _ftpClientManager.fileDelete(filename);
  }
  
  /**
   * ftpSend performs two specific operations, 1. To FTP Send 2. To Check whether
   * to rename the file, if set then rename the file with specified Rename Prefix,
   * of the new file, to the no of positions from source file name prefix.
   *
   * @param host - Host of FTP Server.
   * @param port - Port of FTP Server (Ex. 21)
   * @param userName - UserName to Login to FTP Server.
   * @param password - Password to Login to FTP Server.
   * @param fileName - FileName to send to FTP Server.
   * @return - return Code (as per FTP (RFC 986) ) Standard.
   * @throws FTPException thrown when could not perform FTP Command.
   * @throws FTPServiceException thrown upon Initilization error.
   */

  public int ftpSend(
  String host,
  Integer port,
  String userName,
  String password,
  String fileName
  ) throws FTPException,FTPServiceException
  {
    //boolean renameRemoteFile = false;
    int returnCode=0;
    //String srcFileName=null;
    try
    {
      int count = 0;
      FTPParam ftpParam =  intiFTPParam(host,port,userName,
            password,fileName,null,null,null, false); //Initilize FTP Param.
      count = ftpParam.getRetryCount();
      do
      {
         try
         {
            doFTPConnect(ftpParam);               //Connect to FTP Server
            doSendAndRename(ftpParam);            //Perform Send And Rename
            returnCode = getFTPReplyCode();       //Get Reply Code
            break;
         }
         catch(FTPServiceException ex) {
           returnCode = 1;
           break;
         }
         catch(FTPException ex) {
           returnCode = ex.getReplyCode();
         }
         catch(FTPConnectionException ex)  {     //Connection Exception Perform Retry.
           returnCode = 1;
           log("Retry To Perform Command To FTP Server-->");
         }
         catch(Exception ex)
         {
           returnCode = 1;
           break;
         }
      }while( --count>0 ); //End of while
    }
    catch(FTPServiceException ex)
    {
      ex.printStackTrace();
      returnCode = 1;
    }
    finally
    {
      try{
        doFTPConnectionClose();
      }catch(IOException ex){
        ex.printStackTrace();
      }
    }
    return returnCode;
  }


  /**
   * Rename the File given SrcFile Name. renameFile constructs a newfile name
   * given the srcFile Rename Prefix, renames to renamePrefix configured.
   * @param ftpParam - FTPParam represents ftp specific parameters.
   * @param srcFileName - source file to rename.
   * @throws FTPException - thrown when cannot perform FTP Specific operations.
   * @throws IOException - thrown upon IO Error.
   */


  private void renameFile(FTPParam ftpParam,String srcFileName)
    throws FTPException,IOException,FTPServiceException
  {
    String newFileName = null;

    String srcRenamePrefix   = ftpParam.getSrcFileRenamePrefix();
    //String newFileNamePrefix = ftpParam.getFileNamePrefix();

    if (srcRenamePrefix != null)
    {
      if (srcFileName.startsWith(srcRenamePrefix))
      {
        String suffix = srcFileName.substring(srcRenamePrefix.length());
        newFileName = ftpParam.getFileNamePrefix()+suffix;
      }
      else
      {
        newFileName = ftpParam.getFileNamePrefix()+srcFileName;
      }
    }
    else
    {
      newFileName = ftpParam.getFileNamePrefix()+srcFileName;
    }
    _ftpClientManager.fileRename(srcFileName,newFileName);
  }

  public static void main(String args[])
      throws FTPException, FTPServiceException,Exception
  {
    FTPClientDelegate ftpClientDelegate = null;
   /* if(args.length == 0 || args.length < 10)
    {
      printUsage();
      System.exit(1);
    }

    FTPParam ftpParam = initParams(args);

    IFTPClientManager ftpClientManager =
          FTPClientManagerFactory.getFTPClientManager(ftpParam);

    ftpClientDelegate = new FTPClientDelegate(ftpClientManager);

    int reply = ftpClientDelegate.ftpSend(ftpParam,true);
    */
    
    /*
    //test purpose only...
    int reply=0;
    reply = ftpClientDelegate.ftpSend("ftp://admin:admin@192.168.213.167:21","c:/antsrc/new000009.txt","jaggs/test");
    //"ftp://admin1:admin@192.168.213.167:21","c:/antsrc/new000008.txt","jaggs/test");
    //"fpt://i00082:jagadeesh@192.168.213.51:21","c:/antsrc/new000001.txt","jaggs/test");
    System.out.println("Reply from FTP Server ... "+reply);
    //int reply = ftpClientDelegate.ftpSend("192.168.213.167",new Integer(21),"admin","admin","c:/antsrc/antsrc.zip","antsrc.zip:1234566");
    //log(reply);
    System.exit(reply); */
  }

  private static FTPParam initParams(String[] args) throws FTPServiceException
  {
    try
    {
      FTPParam ftpParam = new FTPParam(args);
      String provider   = ftpParam.getProvider();
      String filePrefix = ftpParam.getFileNamePrefix();
      if(provider == null)
      {
        provider = loadConfig(args[1]).getProperty(IFTPConfig.FTP_CLIENT_PROVIDER);
        ftpParam.setProvider(provider);
      }
      if(filePrefix == null)
      {
        filePrefix = loadConfig(args[1]).getProperty(IFTPConfig.FTP_FILE_PERFIX);
        ftpParam.setFileNamePrefix(filePrefix);
      }
      return ftpParam;
    }
    catch (Exception ex)
    {
      throw new FTPServiceException("[FTPClientDelegate][initParams()] "+
                "Could Not Initilize ClientDelegate",ex);
    }
  }


  private static Properties loadConfig(String propsPath) throws Exception
  {
    try
    {
      if(_ftpConfig == null)
      {
        _ftpConfig = new Properties();
        if(propsPath == null)
          _ftpConfig = FTPConfigDelegate.getFTPConfig();
        else
        _ftpConfig.load(new FileInputStream(propsPath));
      }
      return _ftpConfig;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * Initlize the all the required parameters to perform FTP Operation.
   * The params are initilized to a value object,(i.e) FTPParam.
   *
   * @param host Host of the FTP Server.
   * @param port Port of the FTP Server.
   * @param userName UserName to login to FTP Server.
   * @param password Password to authenticate.
   * @param filename FileName to FTP to FTPServer.
   * @param actualFileName ActualFileName (if the fileName is Temp File).
   * @param propsFilePath - FTPClient Properties File.
   * @return FTPParam object.
   * @throws FTPServiceException thrown when cannot initilize.
   */


  private static FTPParam intiFTPParam(
      String host,
      Integer port,
      String userName,
      String password,
      String filename,
      String actualFileName,
      String propsFilePath,
      String destDir,
      boolean isBinaryTransfer
  ) throws FTPServiceException
  {
    try
    {
      String provider = null;
      String fileNamePrefix = null;
      boolean isRename=false;
      Properties ftpProps = null;

      FTPParam ftpParam = new FTPParam(host,port,
          userName,password,filename,actualFileName,destDir);
      //propsFilePath = "C:/ftpclient.properties";
      ftpProps = loadConfig(propsFilePath);

      provider =  ftpProps.getProperty(IFTPConfig.FTP_CLIENT_PROVIDER);
      fileNamePrefix = ftpProps.getProperty(IFTPConfig.FTP_FILE_PERFIX);
      String renameProp = ftpProps.getProperty(IFTPConfig.IS_RENAME_FILE);
      String srcFileNamePrefix = ftpProps.getProperty(IFTPConfig.SRC_FILE_PREFIX);
      String retryCount = ftpProps.getProperty(IFTPConfig.RETRY_COUNT);
      String isMove = ftpProps.getProperty(IFTPConfig.IS_MOVE);
      String moveDir = ftpProps.getProperty(IFTPConfig.MOVE_FOLDER);
      String dataTimeout = ftpProps.getProperty(IFTPConfig.DATA_CONN_TIMEOUT, null);
      String currentConnTimeout = ftpProps.getProperty(IFTPConfig.CURRENT_CONN_TIMEOUT, null);
      String connectionTimeout = ftpProps.getProperty(IFTPConfig.CONNECT_TIMEOUT, null);

      if (checkEmptyNullString(provider))
        throw new FTPServiceException("[FTPClientDelegate][intiFTPParam()] "+
                  "Could not initilize ClientDelegate check for Provider property");
      ftpParam.setProvider(provider);

      if (!checkEmptyNullString(renameProp))
      {
        isRename = renameProp.equalsIgnoreCase("true");
        ftpParam.setFileRename(isRename);
        if (isRename &&  (!checkEmptyNullString(fileNamePrefix)))
          ftpParam.setFileNamePrefix(fileNamePrefix);
        else
        throw new FTPServiceException("[FTPClientDelegate][intiFTPParam()] "+
                  "Could not initilize ClientDelegate check for FileNamePrefix property");
      }
      if(!checkEmptyNullString(isMove))
      {
        boolean isMoveFile = isMove.equalsIgnoreCase("true");
        ftpParam.setIsMoveFile(isMoveFile);
        if (isMoveFile &&  (!checkEmptyNullString(moveDir)))
          ftpParam.setMoveDirectory(moveDir);
        else
        throw new FTPServiceException("[FTPClientDelegate][intiFTPParam()] "+
                  "Could not initilize ClientDelegate check for MoveDir property");
      }

      if(srcFileNamePrefix != null)
      {
        ftpParam.setSrcFileRenamePrefix(srcFileNamePrefix);
      }
      if(retryCount != null)
        ftpParam.setRetryCount(Integer.parseInt(retryCount));
      
      if(dataTimeout != null)
      {
        ftpParam.setDataConnTimeout(Integer.parseInt(dataTimeout));
      }
      
      if(currentConnTimeout != null)
      {
        ftpParam.setSoTimeout(Integer.parseInt(currentConnTimeout));
      }
      
      if(connectionTimeout != null)
      {
        ftpParam.setSocketTimeout(Integer.parseInt(connectionTimeout));
      }
      
      //#1977 TWX Allow to configure to transfer via binary encoding.
      ftpParam.setBinaryTransfer(isBinaryTransfer);
      
      return ftpParam;
    }
    catch(Exception ex)
    {
      throw new FTPServiceException("[FTPClientDelegate][intiFTPParam()] " +
                "Could Not Initilize ClientDelegate",ex);
    }
  }


  private static FTPParam initFTPParam(String url,String fileName)
    throws FTPServiceException
  {
    try
    {
      URLParser urlParser = new URLParser(url);
      String host = urlParser.getHost();
      int port = urlParser.getPort();
      String userName = urlParser.getUser();
      String password = urlParser.getPassword();
      if (port == -1)
      {
        port = DEFAULT_FTP_PORT;
      }

      return intiFTPParam(host,new Integer(port),userName,password,
                  fileName,null,null,null, false);
    }
    catch(Exception ex)
    {
      throw new FTPServiceException("[FTPClientDelegate][initFTPParam()] " +
                "Could Not Initilize ClientDelegate",ex);
    }
  }


  private static FTPParam initFTPParam(String url,String fileName,String destDir, boolean isBinaryTransfer)
    throws FTPServiceException
  {
    try
    {
      URLParser urlParser = new URLParser(url);
      String host = urlParser.getHost();
      int port = urlParser.getPort();
      String userName = urlParser.getUser();
      String password = urlParser.getPassword();
      if (port == -1)
      {
        port = DEFAULT_FTP_PORT;
      }

      return intiFTPParam(host,new Integer(port),userName,password,
                  fileName,null,null,destDir, isBinaryTransfer);
    }
    catch(Exception ex)
    {
      throw new FTPServiceException("[FTPClientDelegate][initFTPParam()] " +
                "Could Not Initilize ClientDelegate",ex);
    }
  }



  private static void printUsage()
  {
    String lSep = System.getProperty("line.separator");
    StringBuffer msg = new StringBuffer();
    msg.append("ftpclient [options] " +lSep);
    msg.append("Options: " + lSep);
    msg.append("  -host                  FTP Host IP Address (or) FTP ServerName." + lSep);
    msg.append("  -port                  Port (EX:-port 21)." + lSep);
    msg.append("  -username              FTP username to log in." + lSep);
    msg.append("  -password              Password to log in." + lSep);
    msg.append("  -filename <filename>   Fullpath to the file." + lSep);
    msg.append("  [Optional]                                  " + lSep);
    msg.append("  -provider <class>      the class which will handle FTP request. " + lSep);
    msg.append("  -prefix                FileName Prefix. " + lSep);
    msg.append("  [Example Usage]                         " + lSep);
    msg .append("[-host 192.213.111.111 -port 21 -username guest -password guest]" );
    msg.append("[ -filename c:/test.txt].");
    log(msg.toString());
  }

  /**
   * #132 TWX 13032009 change to initiate the FTPclient with connectionTimeout.
   * 
   * Connects to Remote/Local FTP Server, and authenticate to FTP Server.
   * @param ftpParam FTP Param encapsulates the required parameters for FTP Operations.
   * @throws FTPException thrown when cannot perform FTP Operation.
   * @throws FTPServiceException encapsulates user specific Exceptions.
   * @throws Exception
   */

  public boolean doFTPConnect(FTPParam ftpParam)
    throws FTPException,FTPServiceException,Exception
  {
    _ftpClientManager = FTPClientManagerFactory.getFTPClientManager(ftpParam.getHost(), 
                                                                    ftpParam.getPort(), ftpParam.getSocketTimeout(), ftpParam.getProvider());
    boolean isConnected =  _ftpClientManager.ftpConnect(ftpParam.getUserName(),ftpParam.getPassword());
    
    //#1830 - explicitly set passive mode here if configured
    if(isUsePassiveMode())
    {
      _ftpClientManager.setPassiveModeTransfer(true);
      log("Set to use passive mode");
    }
    
    return isConnected;
  }

  /**
   * Performs FTP Send and Renames the file, if rename property is set.
   * FTPParam encapsulates the required parameters.
   * @param ftpParam FTPParam encapsulates the required parameters.
   * @throws FTPException thrown when cannot perform FTP Operation.
   * @throws FTPServiceException encapsulates user specific Exceptions.
   */

  public void doSendAndRename(FTPParam ftpParam)
      throws FTPException,FTPServiceException
  {
   FileInputStream ftpinStream = null;
   try
   {
      String actualFileName = null;
      String srcFileName = ftpParam.getFileName();
      File ftpFile = new File(srcFileName);

      if (ftpParam.getActualFileName() != null)
      {
        actualFileName = getActualFilename(ftpParam.getActualFileName());
      }
      else
      {
        actualFileName = ftpFile.getName();
      }
      
      setTimeOut(ftpParam);                 //#132: TWX set timeout for current connection
      
      if(ftpParam.getDestinationDir() != null)
        _ftpClientManager.setDirectory(ftpParam.getDestinationDir());

      if(ftpParam.isBinaryTransfer())
      {
        //#1977 TWX 20101122, set to use binary mode
        _ftpClientManager.putBinaryFile(ftpFile.getAbsolutePath(), actualFileName);
      }
      else
      {
        ftpinStream = new FileInputStream(ftpFile);
        _ftpClientManager.putAsciiFile(ftpinStream,actualFileName);
      }
      
      boolean renameRemoteFile = ftpParam.isFileRename();
      if (renameRemoteFile) //If Rename Remote File
      {
        renameFile(ftpParam,actualFileName);
      }
    }
    catch(FileNotFoundException ex)
    {
      ex.printStackTrace();
      throw new FTPServiceException(ex.getMessage(),ex);
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      throw new FTPServiceException(ex.getMessage(),ex);
    }
    finally
    {
      try
      {
        if (ftpinStream != null)
        {
          ftpinStream.close();
        }
      }
      catch(IOException ex)
      {
      }
    }
  }

  public void doFTPConnectionClose() throws FTPException,IOException
  {
    if (_ftpClientManager != null)
      _ftpClientManager.close();
  }

  public int getFTPReplyCode() throws FTPException
  {
    return Integer.parseInt(_ftpClientManager.getReply());
  }

  private String getActualFileFullPath(String filePath,String fileName)
  {
     File f = new File(filePath);
     String actualFileName = getActualFilename(fileName);
     String cnstPath = filePath.substring(0,filePath.lastIndexOf(File.separator));
     String actFilePath =  cnstPath.concat(File.separator+actualFileName);
     f.renameTo(new File(actFilePath));
     return actFilePath;
  }

   private String getActualFilename(String filename)
   {
    String actualFn = filename;

    int sepIdx = filename.indexOf(":");
    if (sepIdx > -1)
    {
      actualFn = filename.substring(0, sepIdx);
    }
    return actualFn;
  }

  public static void log(String logMessage)
  {
    if (debug)
      System.out.println(logMessage);
  }

  /**
   * This method accepts a url in, and parses the this url to construct, ftp host,
   * ftp port,ftp username,ftp password.
   *
   * The method is essentially responsible to perform FTP SEND and Rename the
   * file. External properties determine weather a file needs to be renamed.
   *
   * @param URL URL for FTP
   * @param fileName
   * @return
   * @throws FTPException
   * @throws FTPServiceException
   */


  public int ftpSend(String url,String fileName)
      throws FTPException,FTPServiceException
  {
    return ftpSend(url,fileName,null, false);

   /* boolean renameRemoteFile = false;
    int returnCode=0;
    String srcFileName=null;
    try
    {
      int count = 0;
      log("b4 init ftp param ");
      FTPParam ftpParam = initFTPParam(url,fileName); //Initiliz e FTP Param.
      count = ftpParam.getRetryCount();
      do
      {
         try
         {
            doFTPConnect(ftpParam);               //Connect to FTP Server
            doSendAndRename(ftpParam);            //Perform Send And Rename
            returnCode = getFTPReplyCode();       //Get Reply Code
            break;
         }
         catch(FTPServiceException ex) {
           returnCode = 1;
           break;
         }
         catch(FTPException ex) {
           returnCode = ex.getReplyCode();
         }
         catch(FTPConnectionException ex)  {     //Connection Exception Perform Retry.
           returnCode = 1;
           log("Retry To Perform Command To FTP Server-->");
         }
         catch(Exception ex)
         {
           returnCode = 1;
           break;
         }
      }while( --count>0 ); //End of while
    }
    catch(FTPServiceException ex)
    {
      ex.printStackTrace();
      returnCode = 1;
    }
    finally
    {
      try{
        doFTPConnectionClose();
      }catch(IOException ex){
        ex.printStackTrace();
        returnCode = 1;
      }
      return returnCode;
    }
  */
  }

  /**
   * This method accepts a url in, and parses the this url to construct, ftp host,
   * ftp port,ftp username,ftp password. It can also allow the configuration on the transfer
   * mode.
   *
   * The method is essentially responsible to perform FTP SEND and Rename the
   * file. External properties determine weather a file needs to be renamed.
   * @param url
   * @param fileName
   * @param isBinaryTransfer
   * @return
   * @throws FTPException
   * @throws FTPServiceException
   */
  public int ftpSend(String url,String fileName, boolean isBinaryTransfer)
    throws FTPException,FTPServiceException
  {
    return ftpSend(url,fileName,null, isBinaryTransfer);
  }

  /**
   * This method accepts a url in, and parses the this url to construct, ftp host,
   * ftp port,ftp username,ftp password.
   *
   * The method is essentially responsible to perform FTP SEND and Rename the
   * file. External properties determine weather a file needs to be renamed.
   *
   * @param URL URL for FTP
   * @param fileName
   * @return
   * @throws FTPException
   * @throws FTPServiceException
   *
   */

  public int ftpSend(String url,String fileName,String destDirectory, boolean isBinaryTransfer)
      throws FTPException,FTPServiceException
  {
    //boolean renameRemoteFile = false;
    int returnCode=0;
    //String srcFileName=null;
    int count = 0;
    FTPParam ftpParam = initFTPParam(url,fileName,destDirectory, isBinaryTransfer); //Initiliz e FTP Param.
    count = ftpParam.getRetryCount();
    boolean isConnected = false;
    do
    {
      try
      {
        isConnected = doFTPConnect(ftpParam);
      }
      catch(FTPException ex)
      {
        returnCode = ex.getReplyCode();
        if(FTPReply.isNegativePermanent(returnCode))
          break;        //FTP Command Not Accepted.
      }
      catch (FTPConnectionException ex)
      {
        returnCode = 1;
        log("Retry To Perform Command To FTP Server-->");
      }
      catch(Exception ex)
      {
        returnCode = 1;
        break;
      }
    }while (--count>0);   /** @todo  To handle Unknownhost exception. */

    if (isConnected)
    {
      returnCode = doConnectionSend(ftpParam);
    }
    return returnCode;
  }

  /**
   * Set the working directory for the current FTP connection to the currentDirectory
   * @param currentDirectory
   * @throws FTPException if a ftp error occur. (eg. permission denied in this case)
   * @throws IOException if an I/O error occur.
   */
  public void ftpSetCurrentDirectory(String currentDirectory) throws FTPException, IOException
  {
    _ftpClientManager.setDirectory(currentDirectory);
  }
  
  private int doConnectionSend(FTPParam ftpParam)
  {
    int returnCode = 1;
    try
    {
      doSendAndRename(ftpParam);
      if(ftpParam.isMoveFile() && !checkEmptyNullString(ftpParam.getDestinationDir()))
        moveFileFromOrigin(ftpParam,ftpParam.getFileName()); // Move file after successful send
      returnCode = getFTPReplyCode();       //Get Reply Code
    }
    catch (FTPException ex)
    {
      returnCode = ex.getReplyCode();
    }
    catch (Exception ex)
    {
      returnCode = 1;
    }
    finally
    {
      try{
        doFTPConnectionClose();
      }catch (IOException ex) {
        ex.printStackTrace();
        returnCode = 1;
      }catch (FTPException ex) {
        returnCode = ex.getReplyCode();
      }
    }
    return returnCode;
  }

  public void setFTPClientPropPath(String ftpClientProps)
  {
    _ftpClientPropsPath = ftpClientProps;
  }
  
  public String getFTPClientPropPath()
  {
    return _ftpClientPropsPath;
  }
  
  public boolean isUsePassiveMode()
  {
    return _isUsePassiveMode;
  }

  public void setUsePassiveMode(boolean usePassiveMode)
  {
    _isUsePassiveMode = usePassiveMode;
  }


  private static void moveFileFromOrigin(FTPParam ftpParam,String fileName)
    throws Exception
  {
    try
    {
      String moveDirectory = ftpParam.getMoveDirectory();
      if (moveDirectory != null)
      {
        File srcFile = new File(fileName);
        String name = srcFile.getName();
        String toDir = srcFile.getParent()+File.separator+moveDirectory;
        if (srcFile.exists())
        {
          File toMoveFile = new File(toDir,name);
          if (!toMoveFile.exists())
          {
            toMoveFile.getParentFile().mkdirs(); //Makes dirs only if not exists
            srcFile.renameTo(toMoveFile);
          }
        }
      }
    }
    catch (Exception ex)
    {
      throw new Exception("Could Not Move the File '"+fileName+"'");
    }
  }

  private static boolean checkEmptyNullString(String checkString)
  {
    return (checkString == null || checkString.trim().equals("")) ;
  }
}

/*
class Perform implements Runnable
{
  FTPClientDelegate deligate = null;
  FTPParam ftpParam = null;
  public void run()
  {
    try
    {
      deligate.ftpSend(ftpParam,false);
    }catch(Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  public void setDelegate(FTPClientDelegate deligate,FTPParam ftpParam)
  {
    this.deligate = deligate;
    this.ftpParam = ftpParam;
  }

}
*/