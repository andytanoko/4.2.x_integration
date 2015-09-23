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
 * File: FTPPullClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16, 2010   Tam Wei Xiang       Created, port from GT4.1.4
 * OCT ??, 2010   Wong Ming Qian      #1830 - Support pushing to GT direct instead
 *                                            of via HTTPBC
 * OCT 20, 2010   Tam Wei Xiang       #1830 - 1) Support the setting of Passive mode
 *                                            2) Support deriving the DocType from the input
 *                                               file from backend                         
 */
package com.gridnode.ftp.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.gridnode.convertor.ConvertorFacade;
import com.gridnode.ext.util.FileUtil;
import com.gridnode.ext.util.exceptions.ILoggerConstant;
import com.gridnode.ftp.FTPClientDelegate;
import com.gridnode.ftp.model.DocExtractInfo;
import com.gridnode.ftp.model.FTPAttachmentInfo;
import com.gridnode.ftp.model.FTPFolderInfo;
import com.gridnode.ftp.model.FTPInfo;
import com.gridnode.ftp.model.FTPServerInfo;
import com.gridnode.ftp.notification.EmailNotifier;
import com.gridnode.ftp.notification.HTTPBCNotifier;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.helpers.ImportDocumentHelper;
import com.gridnode.pdip.base.xml.extension.EnhancedXPathSAXHelper;
import com.gridnode.pdip.base.xml.helpers.ConvertorHelper;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.UUIDUtil;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class handle the pulling of document from FTP server
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class FTPPullClient
{
  
  String attachmentFilename = "";
  boolean deleteDocument = true;
  //end of added by Ming Qian for attachment retrieval 20100816
  private Logger _logger = null;
  
  public FTPPullClient()
  {
    _logger = getLogger();
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
        //reset to default value for next batch of ftp pull props
        boolean isTPPropsAvailable = true;
        boolean isAttachmentPropsAvailable = true;
        boolean isMappingPropsAvailable = true;     
        boolean isAttachmentServerValid = true;
        deleteDocument = true;
        
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
          FTPServerInfo[] infos = FTPConfigUtil.getFTPServerInfo(folderKey, ftpProps);
          FTPServerInfo info = infos[0];
          FTPInfo[] ftpInfos = FTPConfigUtil.getFTPInfo(ftpProps, info);
          String xPath = "";
          boolean isMappingRequired = false;
          String mappingRule = "";
       
          //System.out.println("push to ftp: "+info.getPushDirectToGT());
          
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
              delegate.setUsePassiveMode(info.isUsePassiveMode()); //#1830 allow user to configure whether to use passive mode explicitly
              
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
              if (!isXpathRetrieveRequired(folderInfo))
              {
                isMappingPropsAvailable = false;  
                
                _logger.debugMessage(method, null, "-------------------------------------");
                _logger.debugMessage(method, null, "Mapping properties is not configured.");
                _logger.debugMessage(method, null, "-------------------------------------");
              }
              else
              {
                //we may need to convert the file we downloaded from ftp to the xml format
                isMappingPropsAvailable = true;
                _logger.debugMessage(method, null, "---------------------------------");
                _logger.debugMessage(method, null, "Mapping properties is configured.");
                _logger.debugMessage(method, null, "---------------------------------");
                _logger.logMessage(method, null, "Mapping required: " + folderInfo.getIsMappingRequired());
                _logger.logMessage(method, null, "Mapping rule: " + folderInfo.getMappingRule());
              }
              
              //check if there is attachment needed
              attachmentInfos = FTPConfigUtil.getFTPAttachmentInfo(attachmentFolderKey, ftpProps);
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
                String tempFile = "";
                //#133 Make FTP pull operation more atomic NOTE: It is not under a JTA transaction
                int counterFile = 0; ///
                
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
                      tempFile = FTPConfigUtil.getTempDirPath() + UUIDUtil.getRandomUUIDInStr();
                      
                      //write byte to a temp location
                      FileOutputStream fos = new FileOutputStream(tempFile);
                      fos.write(docForImport);
                      
                      DocExtractInfo docExtractInfo = null;
                      if (isMappingRequired)
                      {
                        docExtractInfo = retrieveTPInfo(tempFile,tpPropsPath, folderInfo, true);
                      }
                      else
                      {
                        docExtractInfo = retrieveTPInfo(tempFile,tpPropsPath, folderInfo, false);
                      }
                       
                      //overwrite the default Partner, Doc Type that is retrieved from the xml file
                      partner = docExtractInfo.getPartner();
                      docType = docExtractInfo.getDocType();
                      
                      fos.close();
                      
                      //_logger.logMessage(method, null, "Counter: "+counter);
                      _logger.logMessage(method, null, "Mapping Required: "+isMappingRequired);  
                      //_logger.logMessage(method, null, "Removed " + tempFile + " file: "+new File(tempFile).delete()); 
                    }
                    _logger.logMessage(method, null, "GridTalk partner ID: " + partner+" Document type: " + folderInfo.getDocType());
                    
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
                          throw ex;
                        }
                     
                        //with attachment properties configured and attachment available
                        if (attachment != null)
                        {
                          if (!info.getPushDirectToGT())
                          {
                            attachmentFound(httpbc, delegate, attachmentInfo, processedFile, outToHTTPBC, docForImport, attachment, 
                                            filename, be, partner, docType, ftpPropsPath);
                          }
                          else
                          {
                            pushDirectToGT(true, info, jndiPropsPath, folderInfo, partner, filenames[counterFile], 
                                           docForImport, attachment, attachmentInfo, delegate, docType);
                          }
                        }
                        else if (attachment == null)
                        {
                          
                              //with attachment properties configured and no attachment available 
                              if (!info.getPushDirectToGT())
                              {
                                noAttachmentFound(httpbc, delegate, processedFile, outToHTTPBC, docForImport, filename, be, partner, 
                                                  docType, ftpPropsPath);
                              }
                              else
                              {
                                pushDirectToGT(false, info, jndiPropsPath, folderInfo, partner, filenames[counterFile], 
                                               docForImport, attachment, attachmentInfo, delegate, docType);
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
                      if (!info.getPushDirectToGT())
                      {
                        noAttachmentFound(httpbc, delegate, processedFile, outToHTTPBC, docForImport, filename, be, partner, 
                                          docType, ftpPropsPath);
                      }
                      else
                      {
                        pushDirectToGT(false, info, jndiPropsPath, folderInfo, partner, filenames[counterFile], 
                                       docForImport, attachment, attachmentInfo, delegate, docType);
                      }
                    }
                  }
                  else
                  {
                    if (!info.getPushDirectToGT())
                    {
                      //with no attachment properties configured and no attachment available  
                      noAttachmentFound(httpbc, delegate, processedFile, outToHTTPBC, docForImport, filename, be, partner, 
                                        docType, ftpPropsPath);
                    }
                    else
                    {
                      pushDirectToGT(false, info, jndiPropsPath, folderInfo, partner, filenames[counterFile], 
                                     docForImport, attachment, attachmentInfo, delegate, docType);
                    }
                  }
                  counterFile++;
                  
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
  
  //added by Ming Qian 20101011 for pushing directly to GT without HTTP BC
  private void pushDirectToGT(boolean hasAttachment, FTPServerInfo serverInfo, String jndiPropsPath, FTPFolderInfo folderInfo, String partner, String filenames, byte[] docForImport, byte[] attachment, 
                              FTPAttachmentInfo attachmentInfo, FTPClientDelegate delegate, String docType) throws Exception
  {
    String method = "pushDirectToGT";  
    
    if(!deleteDocument)
    {
      _logger.logMessage(method, null, "Transaction doc: "+filenames+" will not be deleted");
      return;
    }
    
    String gtUsername = serverInfo.getPushGTUsername();
    _logger.logMessage(method, null, "GridTalk username: " + gtUsername);
    
    try
    {
      List partnerList = Arrays.asList(new String[]{partner});
      //_logger.logMessage(method, null, "ff size: " + filenames.length);
      List docList = Arrays.asList(new String[]{filenames});
      //List docList = Arrays.asList(filenames);
      String tracingID = UUIDUtil.getRandomUUIDInStr();
      
      //write file to server
      _logger.logMessage(method, null, "Create new user folder: "+new File(FTPConfigUtil.getDocumentTempDirPath()+gtUsername).mkdir());
      _logger.logMessage(method, null, "Create new tracing ID folder: "+new File(FTPConfigUtil.getDocumentTempDirPath()+gtUsername+"/"+tracingID).mkdir());
      
      //FileOutputStream fos = new FileOutputStream("/home/weixiang/GT423/GridTalk/data/GNapps/gtas/data/temp/"+gtUsername+"/"+tracingID+"/"+filenames[0]);
      FileOutputStream fos = new FileOutputStream(FTPConfigUtil.getDocumentTempDirPath()+gtUsername+"/"+tracingID+"/"+filenames);
      fos.write(docForImport);
      fos.close();
      
      if (hasAttachment)
      {  
        List attachmentList = Arrays.asList(new String[]{extractFilename(filenames) + '.' + attachmentInfo.getFileExtension()});
        
        //write attachment to server
        //FileOutputStream afos = new FileOutputStream("/home/weixiang/GT423/GridTalk/data/GNapps/gtas/data/temp/"+gtUsername+"/"+tracingID+"/"+extractFilename(filenames[0]) + '.' + attachmentInfo.getFileExtension());
        FileOutputStream afos = new FileOutputStream(FTPConfigUtil.getDocumentTempDirPath()+gtUsername+"/"+tracingID+"/"+extractFilename(filenames) + '.' + attachmentInfo.getFileExtension());
        afos.write(attachment);
        afos.close();
        
        _logger.logMessage(method, null, "Pushing directly to GT without HTTPBC with attachment.");
        _logger.logMessage(method, null, "------------------------------------------------------");
        _logger.logMessage(method, null,gtUsername);
        _logger.logMessage(method, null,folderInfo.getBe());
        _logger.logMessage(method, null,folderInfo.getBe());
        _logger.logMessage(method, null,""+partnerList.size());
        _logger.logMessage(method, null,docType);
        _logger.logMessage(method, null,""+docList.size());
        _logger.logMessage(method, null,""+attachmentList.size());
        _logger.logMessage(method, null,IDocumentPathConfig.PATH_TEMP);
        _logger.logMessage(method, null,gtUsername+"/"+tracingID+"/");
        _logger.logMessage(method, null,tracingID);
   
        getDocMgr(jndiPropsPath).importDocument(gtUsername, folderInfo.getBe(), null, folderInfo.getBe(), partnerList,
                                                docType, docList, attachmentList, IDocumentPathConfig.PATH_TEMP, gtUsername+"/"+tracingID+"/", null,
                                                null, null, tracingID);
      }
      else
      {
        _logger.logMessage(method, null, "Pushing directly to GT without HTTPBC without attachment.");
        _logger.logMessage(method, null, "---------------------------------------------------------");
        
        _logger.logMessage(method, null,gtUsername);
        _logger.logMessage(method, null,folderInfo.getBe());
        _logger.logMessage(method, null,folderInfo.getBe());
        _logger.logMessage(method, null,""+partnerList.size());
        _logger.logMessage(method, null,docType);
        _logger.logMessage(method, null,""+docList.size());
       
        _logger.logMessage(method, null,IDocumentPathConfig.PATH_TEMP);
        _logger.logMessage(method, null,gtUsername+"/"+tracingID+"/");
        _logger.logMessage(method, null,tracingID);

        getDocMgr(jndiPropsPath).importDocument(gtUsername, folderInfo.getBe(), null, folderInfo.getBe(), partnerList,
                                                docType, docList, new ArrayList(), IDocumentPathConfig.PATH_TEMP, gtUsername+"/"+tracingID+"/", null,
                                                null, null, tracingID);
      }
      delegate.removeFile(filenames);
    }
    catch (Exception ex)
    {
      _logger.logError(ILoggerConstant.FTP_PULL_FAILED, method, null, "Error in pushing directly to GT. Error: "+ex.getMessage(), ex);
      //notifyFTPPullFailed(ex.getMessage(), ExceptionUtil.getStackStraceStr(ex), null, null);
      throw ex;
    }
  }
 
  //added by Ming Qian on 20101011, specially for pushing directly to GT without HTTPBC
  private IDocumentManagerObj getDocMgr(String jndiPropsPath) throws Exception
  {
    String method = "getDocMgr";  
    //IDocumentManagerHome home = null;
    
    try
    {
      Properties jndiProps = loadDestFolderProperties(jndiPropsPath);

      JNDIFinder jndiFinder = new JNDIFinder(jndiProps);
      IDocumentManagerHome home = (IDocumentManagerHome)jndiFinder.lookup(IDocumentManagerHome.class.getName(), IDocumentManagerHome.class);
      //IDocumentManagerHome home = (IDocumentManagerHome)jndiFinder.lookup("AA", IDocumentManagerHome.class);
      IDocumentManagerObj alertManagerObj = home.create();
      return alertManagerObj;
    }
    catch(Exception ex)
    {
      _logger.logError(ILoggerConstant.FTP_PULL_FAILED, method, null, "Error in getting Document Manager. Error: "+ex.getMessage(), ex);    
      throw ex;
    }
  }
  
  
  
  /**
   * Handle the retrieving of the attachment. It also control whether to remove the transaction doc if the transaction doc
   * related attachment can not be found.
   *  
   * @param attachmentInfo
   * @param info
   * @param filename
   * @param ftpClientPropsPath
   * @param ftpPropsPath
   * @param folder
   * @param isAttachmentServerValid
   * @return
   * @throws Exception
   */
  //added by Ming Qian 20100816 for attachment retrieval
  // 2 Nov: TWX refactor logic
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
      _logger.logMessage(method, null, "Attachment enforcement: " + attachmentInfo.getIsEnforceAttachment());
      FTPClientDelegate attachmentDelegate = new FTPClientDelegate();
      attachmentDelegate.setFTPClientPropPath(ftpClientPropsPath);
      
      //init the FTP connection to the attachment folder
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

      //check whether there is matched attachment given the attachment filename
      //full name of attachment to retrieve, including extension
      attachmentFilename = extractFilename(filename) + "." + attachmentInfo.getFileExtension();
      byte[] attachmentForImport = attachmentDelegate.getFileInByte(attachmentFilename);

      //if the attachment cannot be found
      if (attachmentForImport == null || attachmentForImport.length == 0)
      {
        isEnforceAttachment(attachmentInfo,filename, folder,  ftpClientPropsPath, ftpPropsPath);     
      }
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
  
  /**
   * Handle the attachment policy if the attachment can not be found. Mark the transaction doc as "deletable"
   * if no enforcement of the attachment is configured. Mark the transaction doc as not "deletable" if otherwise. 
   * 
   * @param attachmentInfo the FTP AttachmentInfo
   * @param filename The filename of the transaction doc
   * @param folder The transaction doc's ftp folder
   * @param ftpClientPropsPath
   * @param ftpPropsPath
   */
  private void isEnforceAttachment(FTPAttachmentInfo attachmentInfo,String filename, String folder, String ftpClientPropsPath, String ftpPropsPath) 
  {
    String method = "isEnforceAttachment";
  
    if (attachmentInfo.getIsEnforceAttachment())
    {
      EmailNotifier notifier = new EmailNotifier(ftpPropsPath);
      try
      {
        deleteDocument = false;
        notifier.triggerIsEnforceAttachmentFailedEmail(attachmentInfo.getHost(), attachmentInfo.getPort(), filename, attachmentFilename, attachmentInfo.getFileExtension(), folder, attachmentInfo.getFolder(), ftpPropsPath, attachmentInfo.getUsername());
      }
      catch(Throwable ex)
      {
        _logger.logError(ILoggerConstant.FTP_EMAIL_FAILED, method, null, "Failed to trigger email. Error: "+ex.getMessage(), ex);
      }
    }
    else
    {
      deleteDocument = true;
    //user is ok with it so just ignore 
    }  
  }
  
  
  private Properties loadDestFolderProperties(String propertiesPath) throws Exception
  {
      File props = new File(propertiesPath);
      Properties destFolderProperties = new Properties();
      destFolderProperties.load(new FileInputStream(props));
      return destFolderProperties;
  }
  
  private void notifyFTPPullFailed(String errorMsg, String stackTrace, String emailProps, String ftpBackendPropsName)
  {
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
      _logger.logMessage(method, null, "Transaction file: "+filename+" will not be deleted.");
    } 
  }
    
//added by Ming Qian 20100823 for trading partner info retrieval
  private DocExtractInfo retrieveTPInfo(String filename,String tpPropsPath, FTPFolderInfo folderInfo, boolean isMappingRequired) throws Exception
  {
    String method = "retrieveTPInfo";  
    String tpInfo = "";
    String docType = null;
    
    String docPartnerId = "";
    String gridTalkPartnerId = "";
    //String tpPropsPath = "./conf/tp.properties";
    String tradingPartnerKey = "trading.partner";
    File tempFile = null;
    
    _logger.logMessage(method, null, "Retrieving trading partner information....");
    
    //Perform conversion if it is configured
    try
    {  
      if (isMappingRequired)
      {
        ConvertorFacade convertorFacade = new ConvertorFacade();
        //converts flat file to xml
     
        tempFile = ConvertorHelper.convert(convertorFacade, filename, folderInfo.getMappingRule());
        _logger.debugMessage(method, null, "Converted file="+ (tempFile != null ? tempFile.getAbsolutePath() : ""));
      }
      else
      {
        tempFile = new File(filename);
      }      
      
    }
    catch(Throwable ex)
    {
      _logger.logError(null, method, null, "Failed to convert for file: "+filename+" Mapping Rule: " + folderInfo.getMappingRule(), ex);
      throw new Exception("Failed to convert for file: "+filename+" Mapping Rule: " + folderInfo.getMappingRule(), ex);
    }
     
    //Extract DocType, Partner from the xml file
    if(! isEmptyOrNull(folderInfo.getXPath()))
    {
      tpInfo = getValueAtXPath(tempFile, folderInfo.getXPath());
    }
    
    if(! isEmptyOrNull(folderInfo.getDocTypeXPath()))
    {
      docType = getValueAtXPath(tempFile, folderInfo.getDocTypeXPath());
    }
    
    
    try
    {
      //retrieve partner info from the properties file
      if (tpPropsPath != null && tpInfo != null)
      {
        Properties props = new Properties();
        props.load(new FileInputStream(tpPropsPath));
        Integer tempInteger = new Integer(props.getProperty(tradingPartnerKey));  
            
        //scans the whole file to look for suitable match
        for (int i = 1; i <= tempInteger.intValue(); i++)
        {
          if (tpInfo.equals(props.getProperty(tradingPartnerKey + "." + i + ".doc.partner.id")))
          {
            gridTalkPartnerId = props.getProperty(tradingPartnerKey + "." + i + ".gridtalk.partner.id");
            break;
          }
        }
      }
      
      _logger.logMessage(method, null, "Removed " + tempFile.getAbsolutePath() + " file: "+tempFile.delete());
    }
    catch (Throwable ex)
    {
      _logger.logError(null, method, null, "failed to convert doc id to gridtalk partner id. Error: "+ex.getMessage(), ex); 
      throw new Exception("Conversion failed for: "+ filename + ". Mapping Rule: " + folderInfo.getMappingRule() + ". Reason: " + ex);
    }
    
    //if user did not configure the trading partner information in properties file
    if (isEmptyOrNull(gridTalkPartnerId))
    {
      //we take the default value in the translated xml file according to the xpath 
      gridTalkPartnerId = tpInfo; 
    }
    
    if(isEmptyOrNull(gridTalkPartnerId))
    {
      //TWX if xpath return value is empty or null, we use the default user configure doc type
      gridTalkPartnerId = folderInfo.getTp();
    }
    
    if(isEmptyOrNull(docType))
    {
      docType = folderInfo.getDocType();
    }
    
    //#1830 TWX encapsulate all the info we retrieve from xml file and the TP Properties file if any.
    DocExtractInfo extractInfo = new DocExtractInfo();
    extractInfo.setPartner(gridTalkPartnerId);
    extractInfo.setDocType(docType);
    
    _logger.debugMessage(method, null, "DocExtract partnerId: "+gridTalkPartnerId+" docType: "+docType);
    
    return extractInfo;
  }
  
  /**
   * Determine whether we need to do any xpath extraction
   * @param folderInfo The FTPFolderInfo
   * @return true if we need to perform XPath extraction; false otherwise;
   */
  private boolean isXpathRetrieveRequired(FTPFolderInfo folderInfo)
  {
    String partnerXpath = folderInfo.getXPath();
    String docTypeXpath = folderInfo.getDocTypeXPath();
    
    if(isEmptyOrNull(partnerXpath) && isEmptyOrNull(docTypeXpath))
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  
  /**
   * Get the xpath value from the xml file.
   * @param file The xml file
   * @param xpath The xpath
   * @return the xpath related value or null of no value matched the given xpath
   * @throws Exception
   */
  private String getValueAtXPath(File file, String xpath) throws Exception
  {
    _logger.debugMessage("getValueAtXPath", null, "Retrieving xpath value: "+xpath);
    
    List xpathValues = EnhancedXPathSAXHelper.getValuesAtXPathUnique(file.getAbsolutePath(), xpath);
    if(xpathValues == null || xpathValues.size() == 0)
    {
      return null;
    }
    else
    {
      return (String)xpathValues.iterator().next();
    }
  }
  
  private boolean isEmptyOrNull(String s)
  {
    if(s == null || s.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
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
  
  private Logger getLogger()
  {
    return LoggerManager.getOneTimeInstance().getLogger(ILoggerConstant.LOG_TYPE, "FTPPullClient");
  }
}
