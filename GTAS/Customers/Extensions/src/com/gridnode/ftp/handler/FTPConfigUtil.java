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
 * File: FTPConfigUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.ftp.handler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.gridnode.ftp.model.FTPAttachmentInfo;
import com.gridnode.ftp.model.FTPFolderInfo;
import com.gridnode.ftp.model.FTPInfo;
import com.gridnode.ftp.model.FTPServerInfo;

/**
 * This class handling the FTP configuration that is common between the FTP Pull and FTP Push client.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class FTPConfigUtil
{
  private static final String FTP_HOST="ftp.host";
  private static final String FTP_PORT="ftp.port";
  private static final String FTP_USER="ftp.username";
  private static final String FTP_PASSWORD="ftp.password";
  private static final String FTP_USE_PASSIVE_MODE="ftp.passive.mode";
  
  //added by Ming Qian for MCI 20101007
  private static final String FTP_PUSH_DIRECT_TO_GT="ftp.push.direct.to.gt";
  private static final String FTP_PUSH_GT_USERNAME="ftp.push.gt.username";
  private static final String FTP_PUSH_GT_PASSWORD="ftp.push.gt.password";
  
  
  private static final String FTP_APPENDED_FILENAME = "append.filename";
  private static final String FTP_FILENAME_FORMAT = "filename.format";
  private static final String FTP_EMBEDDED_CONTENT_TAG = "embed.contentTag";
  private static final String FTP_EMBEDDED_CONTENT_FORMAT = "embed.contentTag.format";
  
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
  
  private static final int NUM_FILE_RETRIEVE_DEF = 10;
  private static final String FTP_CLIENT_PROVIDER = "ftp.client.provider";
  private static final String NUM_FOLDER_RETRIEVE = "num.doc.folder.retrieve"; //for FTP pull
  private static final String NUM_FILE_FETCH = "num.fetch";
  private static final String VALID_FTP_STATUS_CODE = "valid.ftp.status.code";
  
  /**
   * Load the properties given the properties path.
   * @param propertiesPath the full path to the properties file
   * @return the Properties instance
   * @throws Exception thrown if error when loading the Properties
   */
  public static Properties loadProperties(String propertiesPath) throws Exception
  {
      File props = new File(propertiesPath);
      Properties destFolderProperties = new Properties();
      destFolderProperties.load(new FileInputStream(props));
      return destFolderProperties;
  }
  
  /**
   * Retrieve the FTP connection info from the given props.
   * @param destKey
   * @param props
   * @return
   * @throws Exception
   */
  public static FTPServerInfo[] getFTPServerInfo(String destKey, Properties props) throws Exception
  {
      ArrayList<FTPServerInfo> serverInfoList = new ArrayList<FTPServerInfo>();
      String destFolder = props.getProperty(destKey, "");
    
      //pull from FTP other than the default one. If none can be found, default FTP connection will be used
      String hostname = props.getProperty(destKey+"."+FTP_HOST, props.getProperty(FTP_HOST));
      String port = props.getProperty(destKey+"."+FTP_PORT, props.getProperty(FTP_PORT));
      String username = props.getProperty(destKey+"."+FTP_USER, props.getProperty(FTP_USER));
      String password = props.getProperty(destKey+"."+FTP_PASSWORD, props.getProperty(FTP_PASSWORD));
      String usePassiveMode = props.getProperty(destKey+"."+FTP_USE_PASSIVE_MODE,props.getProperty(FTP_USE_PASSIVE_MODE));
      
      boolean isUsePassive = true; //1830 support use of passive mode explicitly
      if(usePassiveMode==null || usePassiveMode.trim().length() == 0 || Boolean.FALSE.toString().equals(usePassiveMode))
      {
        isUsePassive = false;
      }
      
       
      
      //added by Ming Qian for MCI 20101007
      String pushDirectToGT = props.getProperty(destKey+"."+FTP_PUSH_DIRECT_TO_GT, props.getProperty(FTP_PUSH_DIRECT_TO_GT));
      String pushGTUsername = props.getProperty(destKey+"."+FTP_PUSH_GT_USERNAME, props.getProperty(FTP_PUSH_GT_USERNAME));
      String pushGTPassword = props.getProperty(destKey+"."+FTP_PUSH_GT_PASSWORD, props.getProperty(FTP_PUSH_GT_PASSWORD));
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
          FTPServerInfo serverInfo = new FTPServerInfo(hostname, ftpPort, username, password, folder, new Boolean(pushDirectToGT), pushGTUsername, pushGTPassword);
          serverInfo.setAppendFilename(appendedFilename);
          serverInfo.setFilenameFormat(filenameFormat);
          serverInfo.setEmbeddedContentTag(embeddedContentTag);
          serverInfo.setEmbeddedContentTagFormat(embeddedContentTagFormat);
          serverInfo.setUsePassiveMode(isUsePassive);
          serverInfoList.add(serverInfo);
          
        }
      }
      
      return serverInfoList.toArray(new FTPServerInfo[]{});
  }
  
  /**
   * Load the FTPInfo from the given props. If there is FTP connection info associated to the
   * folder info, the FTP folder info will be used instead of using the defailt "defServerInfo".
   * @param props
   * @param defServerInfo
   * @return
   */
  public static FTPInfo[] getFTPInfo(Properties props, FTPServerInfo defServerInfo)
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
        String docXPathKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.DOC_TYPE_XPATH;
        
        String mappingRuleKey = NUM_FOLDER_RETRIEVE+"."+i+FTPFolderInfo.MAPPING_RULE;
        
        String folder = props.getProperty(folderKey);
        String be = props.getProperty(beKey);
        String partner = props.getProperty(partnerKey);
        String docType = props.getProperty(docTypeKey);
        String numFetch = props.getProperty(numFileKey, defNumFileFetch);
        String isMappingRequired = props.getProperty(isMappingRequiredKey);
        String xPath = props.getProperty(xPathKey);
        String mappingRule = props.getProperty(mappingRuleKey);
        String docTypeXpath = props.getProperty(docXPathKey);
        
        FTPFolderInfo folderInfo = new FTPFolderInfo(be,partner, docType, folder, Integer.parseInt(numFetch), Boolean.parseBoolean(isMappingRequired), xPath, mappingRule);
        folderInfo.setDocTypeXPath(docTypeXpath);
        
        ftpInfo[i-1] = new FTPInfo(defServerInfo, folderInfo);
      }
      
      return ftpInfo;
    }
  }
  
//added by Ming Qian for attachment retrieval 20100816
  public static FTPAttachmentInfo[] getFTPAttachmentInfo(String destKey, Properties props) throws Exception
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
  
  public static String getTempDirPath() throws Exception
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
  
  public static String getDocumentTempDirPath() throws Exception
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
      String gtTempDir = configProps.getProperty("gt.doc.temp.dir", null);
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
  
  /**
   * Load the list of valid FTP status code given the ftp config properties (ftpBackend.properties). If the properties
   * does not contain the ftp status code property, a default valid ftp code will be returned.
   * 
   * @param props The ftp configuration properties
   * @return
   */
  public static ArrayList<Integer[]> loadValidFTPCodes(Properties props)
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
}
