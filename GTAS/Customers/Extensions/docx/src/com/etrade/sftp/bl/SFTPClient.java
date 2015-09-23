/**
 * 
 */
package com.etrade.sftp.bl;

import java.io.File;

import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.UserAuthenticator;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.auth.StaticUserAuthenticator;
import org.apache.commons.vfs.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs.provider.local.LocalFile;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;

import com.etrade.sftp.exception.SFTPClientException;
import com.etrade.sftp.util.SFTPLogger;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.StringUtil;

/**
 * @author EricLoh
 * 
 */
public class SFTPClient
{
  private FileSystemManager fsManager = null;
  private FileSystemOptions fsOptions = null;

  private FileObject remoteFileObj = null;
  
  private static final String CLASS_NAME = "SFTPClient";
  /**
   * @param userName
   * @param password
   * @param disableHostKeyCheck
   * @throws SFTPClientException 
   */
  public SFTPClient(String userName, File[] identities, boolean disableHostKeyCheck, boolean userDirAsRoot) throws SFTPClientException
  {
    super();
    this.initialize(userName, null, identities, disableHostKeyCheck, userDirAsRoot);
  }

  /**
   * @param userName
   * @param password
   * @param disableHostKeyCheck
   * @throws SFTPClientException 
   */
  public SFTPClient(String userName, char[] password, boolean disableHostKeyCheck, boolean userDirAsRoot) throws SFTPClientException
  {
    super();
    this.initialize(userName, password, null, disableHostKeyCheck, userDirAsRoot);
  }

  /**
   * Initialise the fileSystemOptions, and fileSystemManager
   * 
   * @param userName
   * @param password
   * @param identities
   * @param disableHostKeyCheck
   * @throws SFTPClientException 
   */
  public void initialize(String userName, char[] password, File[] identities, boolean disableHostKeyCheck, boolean userDirAsRoot) throws SFTPClientException
  {
    String logPrefix = CLASS_NAME + ".initialize - ";
    String passwordStr = null;
    
    try
    {
      this.fsOptions = new FileSystemOptions();

      if (StringUtil.isBlank(userName))
      {
        throw new SFTPClientException("Exception at initialize(): UserName must be provided");
      }
      
      if (disableHostKeyCheck) // Disable strict Hostkey verification
      {
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(this.fsOptions, "no");
      }

      //Set User Directory as Root, Use relative path to User Directory 
      SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(this.fsOptions, userDirAsRoot);

      if (password != null && password.length != 0)
      {
        passwordStr = String.valueOf(password);
        SFTPLogger.debug(logPrefix + "Connecting using password...");
      }
      else
      {
        passwordStr = null;
        
        //Check if any private key file provided
        if (identities == null || 
            (identities != null && identities.length == 0) ||
            (identities.length == 1 && identities[0] == null))
        {
          throw new SFTPClientException("Exception at initialize(): Either password or private key file must be provided");
        }
        else //Add private key identities if present
        {
          SFTPLogger.debug(logPrefix + "Connecting using private key(s)...");
          
          SftpFileSystemConfigBuilder.getInstance().setIdentities(fsOptions, identities);
        }
      }

      // Sets the userName and password if provided
      UserAuthenticator auth = new StaticUserAuthenticator(null, userName, passwordStr);

      // Adds the userCredentials to the fileSystem options
      DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(fsOptions, auth);

      this.fsManager = VFS.getManager();
    }
    catch (FileSystemException e)
    {
      throw new SFTPClientException("Exception at initialize()", e); //wrap into SFTPClientException
    }
  }

  /**
   * Upload file with default port of 22
   * @param remoteHostName
   * @param remoteFolder
   * @param localFilePath
   * @param remoteFileName
   * @throws SFTPClientException 
   * @throws FileSystemException
   */
  public void uploadFile(String remoteHostName, String remoteFolder, String localFilePath, String remoteFileName) throws SFTPClientException
  {
    this.uploadFile(remoteHostName, "", remoteFolder, new File(localFilePath), remoteFileName);
  }

  /**
   * Upload file specifying a port
   * @param remoteHostName
   * @param remoteFolder
   * @param localFilePath
   * @param remoteFileName
   * @throws SFTPClientException 
   */
  public void uploadFile(String remoteHostName, String port, String remoteFolder, String localFilePath, String remoteFileName) throws SFTPClientException
  {
    this.uploadFile(remoteHostName, port, remoteFolder, new File(localFilePath), remoteFileName);
  }
  
  /**
   * Upload file specifying a port
   * @param remoteHostName
   * @param remoteFolder
   * @param localFile
   * @param remoteFileName
   * @throws SFTPClientException 
   * @throws FileSystemException
   */
  public void uploadFile(String remoteHostName, String port, String remoteFolder, File localFile, String remoteFileName) throws SFTPClientException
  {
    String logPrefix = CLASS_NAME + "." + "uploadFile" + " - ";
    String remotePath = null;

    try
    {
      if (!StringUtil.isBlank(port))
      {
        port = ":" + port;
      }
      else
      {
        port = "";
      }
      
      ///////////////////////////////////////////////////////////////////////
      // Check parameters before uploading
      ///////////////////////////////////////////////////////////////////////
      if (!localFile.exists())
      {
        throw new SFTPClientException("Exception at uploadFile() - localFile is missing: [" + localFile.getAbsolutePath() + "]");
      }
      else
      if (StringUtil.isBlank(remoteFolder)) //Check if remoteFolder is blank
      {
        throw new SFTPClientException("Exception at uploadFile() - remoteFolder name is missing");
      }
      else
      if (StringUtil.isBlank(remoteFileName)) //Check if remoteFileName is blank
      {
        throw new SFTPClientException("Exception at uploadFile() - remoteFileName is missing");
      }
      ///////////////////////////////////////////////////////////////////////
      
      remotePath = "sftp://" + remoteHostName + port + "/" + remoteFolder + "/" + remoteFileName;
      
      this.remoteFileObj = this.fsManager.resolveFile(remotePath, fsOptions);
                 
      SFTPLogger.debug(logPrefix + "SFTP connection successfully established to " + remoteHostName);

      FileObject localFileObj = this.fsManager.resolveFile(localFile.getAbsolutePath());

      this.remoteFileObj.copyFrom(localFileObj, Selectors.SELECT_SELF); // copy from localFileObj to remoteFileObj

      SFTPLogger.debug(logPrefix + "File: " + localFile.getAbsolutePath() + " is uploaded successfully to " + remoteHostName + "/<UserHomeDir>/" + remoteFolder + "/" + remoteFileName);
    }
    catch (FileSystemException e)
    {
      throw new SFTPClientException("Exception at uploadFile()", e); //wrap into SFTPClientException
    }
    finally
    {
      release();
    }
  }

  /**
   * Download files using default port
   * 
   * @param remoteHostName
   * @param remoteFolder
   * @param localFolder
   * @param deleteAfterDownload
   *          - if true, deletes each file after downloading
   * @throws SFTPClientException 
   */
  public void downloadFiles(String remoteHostName, String remoteFolder, String localFolder, boolean deleteAfterDownload) throws SFTPClientException
  {
    this.downloadFiles(remoteHostName, "", remoteFolder, localFolder, deleteAfterDownload);
  }

  /**
   * Download files from a remote folder specifying a port
   * 
   * @param remoteHostName
   * @param port
   * @param remoteFolder
   * @param localFolder
   * @param deleteAfterDownload
   *          - if true, deletes each file after downloading
   * @throws SFTPClientException 
   */
  public void downloadFiles(String remoteHostName, String port, String remoteFolder, String localFolder, boolean deleteAfterDownload) throws SFTPClientException
  {
    String logPrefix = CLASS_NAME + "." + "downloadFiles" + " - ";

    FileObject[] children = null;

    String relativePath = null;
    String localUrl = null;
    // String standardPath = null;
    
    children = this.listFiles(remoteHostName, port, remoteFolder);

    try
    {
      // Loop through files/folders under remoteFolder
      for (FileObject childFileObj : children)
      {
        try
        {
          if (childFileObj.getType() == FileType.FILE)
          {
            relativePath = File.separatorChar + childFileObj.getName().getBaseName();

            SFTPLogger.debug(logPrefix + "relativePath: " + relativePath);
            // {
            // standardPath = localFolder + relativePath;
            // SFTPLogger.debug("Standard local path is " + standardPath);
            // }

            {
              localUrl = "file://" + localFolder + relativePath;

              LocalFile localFile = (LocalFile) this.fsManager.resolveFile(localUrl);

              SFTPLogger.debug(logPrefix + "Resolved Local FilePath: " + localFile.getName());

              if (!localFile.getParent().exists()) //Create Local Folder if it doesn't exists
              {
                localFile.getParent().createFolder();
              }

              SFTPLogger.debug(logPrefix + "Downloading remote file: " + relativePath);
              localFile.copyFrom(childFileObj, new AllFileSelector());

              if (deleteAfterDownload)
              {
                childFileObj.delete();
                SFTPLogger.debug(logPrefix + "Deleted remote file: " + relativePath);
              }
            }
          }
          else
          {
            SFTPLogger.debug(logPrefix + "Ignoring folder: " + childFileObj.getName());
          }
        }
        catch (FileSystemException e)
        {          
          throw new SFTPClientException("Exception at downloadFiles()", e); //wrap into SFTPClientException
        }
      }//for
    }
    finally
    {
      release();
    }
  }
  
  /**
   * @param remoteHostName
   * @param port
   * @param remoteFolder
   * @return
   * @throws SFTPClientException
   */
  public FileObject[] listFiles(String remoteHostName, String port, String remoteFolder) throws SFTPClientException
  {
    String logPrefix = CLASS_NAME + "." + "listFiles" + " - ";

    String remotePath = null;
    FileObject[] children = null;

    if (!StringUtil.isBlank(port))
    {
      port = ":" + port;
    }
    else
    {
      port = "";
    }

    remotePath = "sftp://" + remoteHostName + port + "/" + remoteFolder + "/";
    try
    {
      this.remoteFileObj = this.fsManager.resolveFile(remotePath, fsOptions);

      SFTPLogger.debug(logPrefix + "SFTP connection successfully established to " + remotePath);

      children = remoteFileObj.getChildren();
    }
    catch (FileSystemException e)
    {
      throw new SFTPClientException("Exception at listFiles()", e); //wrap into SFTPClientException
    }
    return children;
  }

  /**
   * Release system resources, close connection to the filesystem.
   * @throws SFTPClientException 
   */
  public void release()
  {
    try
    {
      if (this.remoteFileObj != null)
      {
        this.remoteFileObj.close();
        FileSystem fileSystem = this.remoteFileObj.getFileSystem();
        
        this.fsManager.closeFileSystem(fileSystem);
      }
    }
    catch (FileSystemException e)
    {
      SFTPLogger.error(ExceptionUtil.getStackStraceStr(e));
    }
    SFTPLogger.debug(CLASS_NAME + ".release - done");
  }

  /**
   * Checks if the String is null or empty String *
   * 
   * @param str
   * @return true if the String is null or an empty String, else returns false
   */
  public static boolean isEmptyString(String str)
  {
    boolean isEmpty = true;

    if (str != null && str.trim().length() != 0)
    {
      isEmpty = false;
    }
    else
    {
      isEmpty = true;
    }
    return isEmpty;
  }
  
  /**
   * @param args
   */
  public static void main(String args[])
  {
    SFTPLogger.debug(SFTPClient.CLASS_NAME + ".main - started");

    String userName = "eric";
//    String password = "eric";
    boolean disableHostKeyCheck = true;
    boolean userDirAsRoot = true; //i.e. Use relative path to User Home Directory

    String hostName = "172.20.31.29";

    String localFilePath = "D:/Temp/Test.xml";

    String remoteDownloadFolder = "/export/home/eric/test/";
    String localDownloadFolder = "D:/Temp/sftptest/download/";

    File identities[] = new File[1];

    // identities[0] = new File("D:/Temp/SSH_Keys/id_dsa"); //Test private key
    // identities[0] = new File("D:/Temp/SSH_Keys/dsa_key2"); //Test private key2
    // identities[0] = new File("D:/Temp/SSH_Keys/dsa_2048"); //Test private key 2048 bits
//    identities[0] = new File("D:/Temp/SSH_Keys/testkey3a"); //Test converted SSH2 key to OpenSSH key
    identities[0] = new File("D:/Temp/yfb1001_rsa1024_openssh"); //Test converted another SSH2 key to OpenSSH key

    // SFTPClient client = SFTPClient.getInstance(userName, password,
    // disableHostKeyCheck);
    try
    {
      SFTPClient client = new SFTPClient(userName, identities, disableHostKeyCheck, userDirAsRoot);

      client.uploadFile(hostName, remoteDownloadFolder, localFilePath, "dummyFile1.xml");
      client.uploadFile(hostName, remoteDownloadFolder, localFilePath, "dummyFile2.xml");

      // TODO: Tests download
      {
//         client.downloadFiles(hostName, remoteDownloadFolder, localDownloadFolder, true);
      }      
    }
    catch (SFTPClientException e)
    {
      SFTPLogger.error(ExceptionUtil.getStackStraceStr(e));
    }

    SFTPLogger.debug(SFTPClient.CLASS_NAME + ".main - done");
  }
}
