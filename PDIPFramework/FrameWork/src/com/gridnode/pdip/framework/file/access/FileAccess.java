/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileAccess.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??? ?? ????    ???                 Created.
 * Jun 28 2002    Daniel D'Cotta      - Added a createFile() that uses InputStream.
 *                                    - Added method getPathWithoutDomain().
 * Jul 30 2002    Jagadeesh           Modify all reference of ConfigManager to
 *                                    use new ConfigurationManager.Restructur
 *                                    the Configuration Constants to IFrameworkConfig.
 * 17 Jan 2003    Goh Kan Mun         Modified - writeToFile() - check for buffer
 *                                                               before the write.
 *                                             - writeToFileZipped() - check for buffer
 *                                                                     before the write.
 *                                             - Added exist method.
 *                                             - Added length method.
 *                                             - Added createNewFile method.
 * 21 Mar 2003    Goh Kan Mun         Modified - Change readByteFromFile method to retrieve
 *                                               byte from file based on offset instead of
 *                                               block.
 *                                    Modified - Added createNewLocalFile method.
 * 17 Oct 2005    Neo Sok Lay         For JDK1.5 compliance. Remove double ; in createFolder(String)                                   
 * 09 Nov 2005    Neo Sok Lay         Change to use ServiceLocator instead of ServiceLookup                                   
 */
package com.gridnode.pdip.framework.file.access;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.util.HttpURL;
import org.apache.webdav.lib.WebdavResource;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.ejb.IFileMgr;
import com.gridnode.pdip.framework.file.ejb.IFileMgrHome;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class FileAccess {
  //private IFileMgrHome _home;
  private IFileMgr _remote;

  private String _domain=null;
  private File _tempFile;
  private File _rootFile;
  private boolean _isLocal;

  public final int DEFAULT_BUFFER_SIZE = 20000;

  public FileAccess() {
    _isLocal = Util.isLocal();
    if (_isLocal) {
      return;
    }
    try{
    	/*
      _home=(IFileMgrHome)ServiceLookup.getInstance(
        ServiceLookup.CLIENT_CONTEXT).getHome(IFileMgrHome.class);
      if (_home == null) {
        System.out.println("Lookup failure: Home should not be null.");
        throw new Exception();
      }
      _remote = _home.create();
      */
    	_remote = (IFileMgr)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IFileMgrHome.class.getName(),
    	                                                                                  IFileMgrHome.class,
    	                                                                                  new Object[0]);
      if (_remote == null) {
        System.out.println("Lookup failure: Remote should not be null.");
        throw new Exception();
      }
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  public FileAccess(String domain) {
    _domain = domain;
    _isLocal = Util.isLocal();
    if (_isLocal) {
      _rootFile = Util.initLocalResource(_domain);
      return;
    }
    try{
      //_home=(IFileMgrHome)ServiceLookup.getInstance(
      //  ServiceLookup.CLIENT_CONTEXT).getHome(IFileMgrHome.class);
      //if (_home == null) {
      //  System.out.println("Lookup failure: Home should not be null.");
      //  throw new Exception();
      //}
      //_remote = _home.create();
    	_remote = (IFileMgr)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IFileMgrHome.class.getName(),
    	                                                                                  IFileMgrHome.class,
    	                                                                                  new Object[0]);
      if (_remote == null) {
        System.out.println("Lookup failure: Remote should not be null.");
        throw new Exception();
      }
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
      ex.printStackTrace();
    }

  }

  public void setDomain(String domain) {
    if (!domain.equals(_domain)) {
      _domain = domain;
      if (_isLocal)
        _rootFile = Util.initLocalResource(_domain);
    }
  }

  public String getDomain() {
    return _domain;
  }

  /**
   * Returns the path after removing the domain from the front.
   *
   * @param fullPath The full path with the domain
   *
   * @return The path without the domain
   *
   * @author Daniel D'Cotta
   */
  public String getPathWithoutDomain(String fullPath) throws FileAccessException
  {
    if(fullPath == null)
      throw new FileAccessException("fullpath is null");

    String domainPath = null;
    if (_isLocal)
    {
      try
      {
        domainPath = _rootFile.getCanonicalPath();
      }
      catch(IOException ex)
      {
        throw new FileAccessException("could not get local root path", ex);
      }
    }
    else
    {
      Configuration config = ConfigurationManager.getInstance().getConfig(
                               IFrameworkConfig.FRAMEWORK_WEBDAV_CONFIG);

//     ConfigManager configManager = ConfigManager.getInstance("file" +
//                                                              File.separatorChar +
//                                                              "webdav.properties");

      HttpURL httpURL = new HttpURL(config.getString(IFrameworkConfig.WEBDAV_SERVER+_domain));

//      HttpURL httpURL = new HttpURL(configManager.get("webdav.server." + _domain));
      domainPath = httpURL.getPath();
    }

    if(fullPath.startsWith(domainPath))
    {
      String path = fullPath.substring(domainPath.length());

      // Remove leading file seperator
      while(path.charAt(0) == '/' || path.charAt(0) == '\\')
        path = path.substring(1);

      return path;
    }
    else
    {
      throw new FileAccessException("fullPath does not start with domainPath");
    }
  }

  /**
   * creates a new folder in a webdav server
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param newPath The path relative to the domain for the new folder
   *
   * @return The complete path of the folder created or null if not created
   */
  public String createFolder(String domain, String newPath) {
    setDomain(domain);
    return createFolder(newPath);
  }

  /**
   * creates a new folder in a webdav server using the current domain
   *
   * @param newPath The path relative to the domain for the new folder
   *
   * @return The complete path of the folder created or null if not created
   */
  public String createFolder(String newPath) {
    String result=null;

    try {
      if (_isLocal) {
        File tmpFile = new File(_rootFile.getCanonicalPath()+File.separatorChar+newPath);
        if (tmpFile.mkdirs())
          result = tmpFile.getCanonicalPath();
      } else {
        result = _remote.createFolder(_domain, newPath);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
//    System.out.println(_domain+result);
    return result;
  }

  /**
   * creates a new file in a webdav server
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the new file
   *
   * @param file The contents of the new file in local storage or null if the
   * new file is empty
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String createFile(String domain, String path, File file, boolean overwrite)
          throws IOException
  {
    setDomain(domain);
    return createFile(path, file, overwrite);
  }

  /**
   * creates a new file in a webdav server using the current domain
   *
   * @param path The path relative to the domain for the new file
   *
   * @param file The contents of the new file in local storage or null if the
   * new file is empty
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String createFile(String newPath, File file, boolean overwrite)
          throws IOException
  {
    String result = null;
    try
    {
      byte[] inBytes = null;
      if (file.length() < DEFAULT_BUFFER_SIZE)
        inBytes = new byte[(int)file.length()];
      else
        inBytes = new byte[DEFAULT_BUFFER_SIZE];
      int len = 0;
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

      if (_isLocal)
      {
        Util.createHigherLevelFolders(_rootFile.getCanonicalPath(), newPath);
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + newPath);
        if (!overwrite && tmpFile.exists())
          throw new IOException("File already exist: " + tmpFile.getAbsolutePath());
        FileOutputStream out = new FileOutputStream(tmpFile);
        long readLength = 0;
        while (readLength < file.length())
        {
          len = in.read(inBytes);
          out.write(inBytes, 0, len);
          readLength += len;
        }
        out.close();
        result = tmpFile.getCanonicalPath();
      }
      else
      {
        long readLength = in.read(inBytes);
        result = _remote.createFile(_domain, newPath, inBytes, overwrite);
        if (result == null)
          throw new IOException("Error creating the file: " + newPath);
        while (readLength < file.length()) {
          len = in.read(inBytes);
          readLength += len;
          boolean last = (readLength >= file.length());
          if (last) { // 040702 DDJ: Modified to shorten byte array
            inBytes = shortenByteArray(inBytes, len);
          }
          if (writeToFile(newPath, inBytes, last)!=readLength) {
            throw new Exception();
          }
        }
      }
      in.close();
    }
    catch (IOException ioe)
    {
      throw ioe;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * creates a new file in a webdav server using the current domain
   *
   * @param path The path relative to the domain for the new file
   *
   * @param inputStream The contents of the new file in local storage or null if the
   * new file is empty
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String createFile(String newPath, InputStream inputStream, boolean overwrite)
          throws IOException
  {
    String result = null;

    try
    {
      int len = 0;
      int readLength = 0;
      byte[] inBytes = new byte[DEFAULT_BUFFER_SIZE];
      BufferedInputStream in = new BufferedInputStream(inputStream);

      if(_isLocal)
      {
        Util.createHigherLevelFolders(_rootFile.getCanonicalPath(), newPath);
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + newPath);
        if (!overwrite && tmpFile.exists())
          throw new IOException("File already exist: " + tmpFile.getAbsolutePath());
        FileOutputStream out = new FileOutputStream(tmpFile);

        len = in.read(inBytes);
        while(len != -1) // Check for EOF
        {
          out.write(inBytes, 0, len);
          len = in.read(inBytes);
        }
        out.close();
        result = tmpFile.getCanonicalPath();
      }
      else
      {
        len = in.read(inBytes);
        while(len != -1)// Check for EOF
        {
          readLength += len;

          if(readLength == len)
          { // Write first block of the file
            inBytes = shortenByteArray(inBytes, len);
            result = _remote.createFile(_domain, newPath, inBytes, overwrite);
            if (result == null)
              throw new IOException("Error creating the file: " + newPath);
          }
          else
          { // Continue writing remaining blocks of the file
            boolean last = (len < DEFAULT_BUFFER_SIZE);
            if(last)
              inBytes = shortenByteArray(inBytes, len);

            int writeLength = (int) writeToFile(newPath, inBytes, last);
            if(writeLength != readLength)
            {
              throw new Exception("Write length does not match Read Length");
            }
          }
          len = in.read(inBytes);
        }
      }
    }
    catch (IOException ioe)
    {
      throw ioe;
    }
    catch(Exception ex)
    {
      System.out.println("[FileAccess.createFile] Error uploading file");
      ex.printStackTrace();
    }

    return result;
  }

  /**
   * moves a folder from one location in a webdav server to another
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param src The path relative to the domain for the source folder
   *
   * @param des The path relative to the domain for the destination folder
   *
   * @param overwrite If there is an existing folder, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the folder created or null if not created
   */
  public String moveFolder(String domain, String src, String des, boolean overwrite) {
    setDomain(domain);
    return moveFolder(src, des, overwrite);
  }

  /**
   * moves a folder from one location in a webdav server to another using the
   * current domain
   *
   * @param src The path relative to the domain for the source folder
   *
   * @param des The path relative to the domain for the destination folder
   *
   * @param overwrite If there is an existing folder, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the folder created or null if not created
   */
  public String moveFolder(String src, String des, boolean overwrite) {
    String result=null;
    try {
      if (_isLocal) {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + src);
        if (tmpFile.renameTo(new File(_rootFile.getCanonicalPath() + File.separatorChar + des))) {
          tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + des);
          result = tmpFile.getCanonicalPath();
        }
      } else {
        result = _remote.moveFolder(_domain, src, des, overwrite);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * moves a file from one location in a webdav server to another
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param src The path relative to the domain for the source file
   *
   * @param des The path relative to the domain for the destination file
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String moveFile(String domain, String src, String des, boolean overwrite) {
    setDomain(domain);
    return moveFile(src, des, overwrite);
  }

  /**
   * moves a file from one location in a webdav server to another using the
   * current domain
   *
   * @param src The path relative to the domain for the source file
   *
   * @param des The path relative to the domain for the destination file
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String moveFile(String src, String des, boolean overwrite) {
    String result=null;
    try {
      if (_isLocal) {
        Util.createHigherLevelFolders(_rootFile.getCanonicalPath(), des);
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + src);
        if (tmpFile.renameTo(new File(_rootFile.getCanonicalPath() + File.separatorChar + des))) {
          tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + des);
          result = tmpFile.getCanonicalPath();
        }
      } else {
        result = _remote.moveFile(_domain, src, des, overwrite);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    return result;
  }

  /**
   * copies a folder from one location in a webdav server to another
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param src The path relative to the domain for the source folder
   *
   * @param des The path relative to the domain for the destination folder
   *
   * @param overwrite If there is an existing folder, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the folder created or null if not created
   */
   public String copyFolder(String domain, String src, String des, boolean overwrite) {
    setDomain(domain);
    return copyFolder(src, des, overwrite);
  }

  /**
   * copies a folder from one location in a webdav server to another using the
   * current domain
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param src The path relative to the domain for the source folder
   *
   * @param des The path relative to the domain for the destination folder
   *
   * @param overwrite If there is an existing folder, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the folder created or null if not created
   */
   public String copyFolder(String src, String des, boolean overwrite) {
    String result=null;
    try {
      if (_isLocal) {
        File srcFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + src);
        if (srcFile.isDirectory()) {
          File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + des);
          result = tmpFile.getCanonicalPath();
          if (tmpFile.mkdirs()) {
            File[] files = srcFile.listFiles();
            for (int i=0; i<files.length; i++) {
              String tmpPath = File.separatorChar+files[i].getName();
              if (files[i].isDirectory()) {
                if (!copyFolder(src+tmpPath, des+tmpPath, overwrite).equals(
                  _rootFile.getCanonicalPath() + File.separatorChar+des+tmpPath))
                  result = null;
              } else if (files[i].isFile())
                if (!copyFile(src+tmpPath, des+tmpPath, overwrite).equals(
                  _rootFile.getCanonicalPath() + File.separatorChar+des+tmpPath))
                  result = null;
            }
          }
        }
      } else {
        result = _remote.copyFolder(_domain, src, des, overwrite);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * copies a file from one location in a webdav server to another
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param src The path relative to the domain for the source file
   *
   * @param des The path relative to the domain for the destination file
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String copyFile(String domain, String src, String des, boolean overwrite) {
    setDomain(domain);
    return copyFile(src, des, overwrite);
  }

  /**
   * copies a file from one location in a webdav server to another using the
   * current domain
   *
   * @param src The path relative to the domain for the source file
   *
   * @param des The path relative to the domain for the destination file
   *
   * @param overwrite If there is an existing file, it will be overwritten if
   * this parameter is set to true
   *
   * @return The complete path of the file created or null if not created
   */
  public String copyFile(String src, String des, boolean overwrite) {
    String result=null;
    try {
      if (_isLocal) {
        File srcFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + src);
        if (srcFile.isFile()) {
          Util.createHigherLevelFolders(_rootFile.getCanonicalPath(), des);
          BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFile));
          if(des.lastIndexOf(File.separatorChar)>0){
              File folderFile=new File(_rootFile.getCanonicalPath() + File.separatorChar + des.substring(0,des.lastIndexOf(File.separatorChar)));
              folderFile.mkdirs();
          }
          File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + des);
          FileOutputStream out = new FileOutputStream(tmpFile);
          int len=0;
          long readLength=0;
          byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
          while (readLength < srcFile.length()) {
            len = in.read(buffer);
            out.write(buffer, 0, len);
            readLength += len;
          }
          in.close();
          out.close();
          result = tmpFile.getCanonicalPath();
        }
      } else {
        result = _remote.copyFile(_domain, src, des, overwrite);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * creates a new folder in a webdav server
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param delPath The path relative to the domain for the folder to be deleted
   *
   * @return The complete path of the folder deleted or null if not deleted
   */
  public String deleteFolder(String domain, String delPath) {
    setDomain(domain);
    return deleteFolder(delPath);
  }

  /**
   * creates a new folder in a webdav server using the current domain
   *
   * @param delPath The path relative to the domain for the folder to be deleted
   *
   * @return The complete path of the folder deleted or null if not deleted
   */
  public String deleteFolder(String delPath) {
    String result=null;
    try {
      if (_isLocal) {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + delPath);
        if (tmpFile.isDirectory()) {
          File[] file = tmpFile.listFiles();
          for (int i=0; i<file.length; i++) {
            if (file[i].isDirectory())
              deleteFolder(delPath + File.separatorChar + file[i].getName());
            else if (file[i].isFile())
              deleteFile(delPath + File.separatorChar + file[i].getName());
          }
          if (tmpFile.delete())
            result = tmpFile.getCanonicalPath();
        }
      } else {
        result = _remote.deleteFolder(_domain, delPath);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * delete a file in a webdav server
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param delPath The path relative to the domain for the file to be deleted
   *
   * @return The complete path of the file deleted or null if not deleted
   */
  public String deleteFile(String domain, String delPath) {
    setDomain(domain);
    return deleteFile(delPath);
  }

  /**
   * delete a file in a webdav server using the current domain
   *
   * @param delPath The path relative to the domain for the file to be deleted
   *
   * @return The complete path of the file deleted or null if not deleted
   */
  public String deleteFile(String delPath) {
    String result=null;
    try {
      if (_isLocal) {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + delPath);
        
        if (tmpFile.isFile())
        {
          if (tmpFile.delete())
            result = tmpFile.getCanonicalPath();
        }   
            
      } else {
        result = _remote.deleteFile(_domain, delPath);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * rename an existing folder
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param oldPath The current path relative to the domain for the file
   *
   * @param newName The new name of the folder
   *
   * @return The complete path of the file or null if method failed
   */
  public String renameFolder(String domain, String oldPath, String newName) {
    setDomain(domain);
    return renameFolder(oldPath, newName);
  }

  /**
   * rename an existing file using the current domain
   *
   * @param oldPath The current path relative to the domain for the file
   *
   * @param newName The new name of the file
   *
   * @return The complete path of the file or null if method failed
   */
  public String renameFolder(String oldPath, String newName) {
    String result=null;
    String newPath=null;
    int endPoint = oldPath.lastIndexOf(File.separatorChar);
    if (endPoint == oldPath.length()-1)
      newPath = oldPath.substring(0, oldPath.lastIndexOf(File.separatorChar, endPoint-1)) + newName;
    else if (endPoint == -1)
      newPath = newName;
    else
      newPath = oldPath.substring(0, endPoint + 1) + newName;

    try {
      if (_isLocal)
        result = moveFolder(_domain, oldPath, newPath, false);
      else
        result = _remote.moveFolder(_domain, oldPath, newPath, false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * rename an existing file
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param oldPath The current path relative to the domain for the file
   *
   * @param newName The new name of the file
   *
   * @return The complete path of the file or null if method failed
   */
  public String renameFile(String domain, String oldPath, String newName) {
    setDomain(domain);
    return renameFile(oldPath, newName);
  }

  /**
   * rename an existing file using the current domain
   *
   * @param oldPath The current path relative to the domain for the file
   *
   * @param newName The new name of the file
   *
   * @return The complete path of the file or null if method failed
   */
  public String renameFile(String oldPath, String newName) {
    String result=null;
    String newPath=null;
    int endPoint = oldPath.lastIndexOf(File.separatorChar);
    if (endPoint == oldPath.length()-1)
      return null;
    else if (endPoint == -1)
      newPath = newName;
    else
      newPath = oldPath.substring(0, endPoint + 1) + newName;

    try {
      if (_isLocal)
        result = moveFile(_domain, oldPath, newPath, false);
      else
        result = _remote.moveFile(_domain, oldPath, newPath, false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * retrieves the content of an existing file
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @return The contents of the file by storing them in local storage or null
   * if method failed
   */
  public File getFile(String domain, String path) {
    setDomain(domain);
    return getFile(path);
  }

  /**
   * retrieves the content of an existing file using the current domain
   *
   * @param path The path relative to the domain for the file
   *
   * @return The contents of the file by storing them in local storage or null
   * if method failed
   */
  public File getFile(String path) {
    try {
      if (_isLocal) {
System.out.println("_rootFile.getCanonicalPath() is >>" + _rootFile.getCanonicalPath()+ File.separatorChar + path);
        return new File(_rootFile.getCanonicalPath() +
          File.separatorChar + path);
      } else {
        byte[] outBytes = _remote.getFile(_domain, path);
        if (outBytes != null) {
          File file = File.createTempFile(Util.generateUniqueFileName(), ".dat");
          FileOutputStream fOut = new FileOutputStream(file);
          fOut.write(outBytes);
          fOut.close();
          return file;
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * write to an existing file in a webdav server
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @param buffer The contents of the file
   *
   * @param last true if this is the last block of data
   *
   * @return The total number of bytes stored or null if method failed
   */
  public long writeToFile(String domain, String path, byte[] buffer, boolean last) {
    setDomain(domain);
    return writeToFile(path, buffer, last);
  }

  /**
   * write to an existing file in a webdav server using the current domain
   *
   * @param path The path relative to the domain for the file
   *
   * @param buffer The contents of the file
   *
   * @param last true if this is the last block of data
   *
   * @return The total number of bytes stored or null if method failed
   */
  public long writeToFile(String path, byte[] buffer, boolean last) {
    long result=-1L;
    if (_tempFile == null)
      _tempFile = Util.createCache();
    try {
      if (buffer != null && buffer.length > 0)
      {
        FileOutputStream fOut = new FileOutputStream(_tempFile.getCanonicalPath(), true);
        fOut.write(buffer);
        fOut.close();
      }
      if (last) {
        if (_isLocal) {
          File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + path);
          if (_tempFile.renameTo(tmpFile)) {
            _tempFile = null;
            return tmpFile.length();
          }
        } else {
          WebdavResource webdavRes = Util.initWebdavResource(_domain);
          if (webdavRes == null) {
            _tempFile.delete();
            _tempFile = null;
            return result;
          }
          result = Util.putFile(webdavRes, _domain, path, _tempFile);
        }
      } else {
        return _tempFile.length();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * write zipped data to an existing file in a webdav server to be stored in
   * the original form
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @param buffer The zipped contents of the block of data to be written
   *
   * @param last true if this is the last block of data
   *
   * @return The total number of bytes stored or null if method failed
   */
  public long writeToFileZipped(String domain, String path, byte[] buffer, boolean last) {
    setDomain(domain);
    return writeToFileZipped(path, buffer, last);
  }

  /**
   * write zipped data to an existing file in a webdav server to be stored in
   * the original form
   *
   * @param path The path relative to the domain for the file
   *
   * @param buffer The zipped contents of the block of data to be written
   *
   * @param last true if this is the last block of data
   *
   * @return The total number of bytes stored or null if method failed
   */
  public long writeToFileZipped(String path, byte[] buffer, boolean last) {
    long result=-1L;
    if (_tempFile == null)
      _tempFile = Util.createCache();

    try {
      if (buffer != null && buffer.length > 0)
      {
        ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);
        GZIPInputStream zIn = new GZIPInputStream(bIn);
        int bufSize = 0;
        byte[] tmpBuf = new byte[buffer.length];
        FileOutputStream fOut = new FileOutputStream(_tempFile.getCanonicalPath(), true);
        do {
          bufSize = zIn.read(tmpBuf);
          if (bufSize != -1)
            fOut.write(tmpBuf, 0, bufSize);
        } while (bufSize == tmpBuf.length);
        zIn.close();
        bIn.close();
        fOut.close();
      }
      if (last) {
        if (_isLocal) {
          File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + path);
          if (_tempFile.renameTo(tmpFile)) {
            _tempFile = null;
            return tmpFile.length();
          }
        } else {
          WebdavResource webdavRes = Util.initWebdavResource(_domain);
          if (webdavRes == null)
            return result;
          result = Util.putFile(webdavRes, _domain, path, _tempFile);
        }
      } else {
        return _tempFile.length();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    _tempFile.delete();
    _tempFile = null;
    return result;
  }

  /**
   * write zipped data to a group of existing files in a webdav server to be
   * stored in the original form
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param paths The paths relative to the domain for the files to be written
   *
   * @param block The zipped contents of the block of data to be written and
   * the description for each file
   *
   * @param last true if this is the last block of data
   *
   * @return The total number of bytes stored or null if method failed
   */
  public long writeToStreamZipped(String domain, String[] paths, MultiFileBlock block, boolean last) {
    setDomain(domain);
    return writeToStreamZipped(paths, block, last);
  }

  /**
   * write zipped data to a group of existing files in a webdav server to be
   * stored in the original form using the current domain
   *
   * @param paths The paths relative to the domain for the files to be written
   *
   * @param block The zipped contents of the block of data to be written and
   * the description for each file
   *
   * @param last true if this is the last block of data
   *
   * @return The total number of bytes stored or null if method failed
   */
  public long writeToStreamZipped(String[] paths, MultiFileBlock block, boolean last) {
    long result=-1L;
    if (_tempFile == null)
      _tempFile = Util.createCache();

    try {
      byte[] buffer = block.getBlockData();
      ByteArrayInputStream bIn = new ByteArrayInputStream(buffer);
      GZIPInputStream zIn = new GZIPInputStream(bIn);
      int bufSize = 0;

      byte[] tmpBuf = new byte[buffer.length];
      FileOutputStream fOut = new FileOutputStream(_tempFile.getCanonicalPath(), true);
      BlockFileDescriptor des = block.getBlockFileDescriptor(0);
      int i=1;
      do {
        bufSize = zIn.read(tmpBuf);
        if (bufSize != -1) {
          long partSize = des.getFileSize() - _tempFile.length();
          if (partSize < bufSize) {
            fOut.write(tmpBuf, 0, (int)partSize);
            fOut.close();
            if (_isLocal) {
              File partRootFile = Util.initLocalResource(des.getFileDomain());
              File tmpFile = new File(partRootFile.getCanonicalPath() +
                File.separatorChar + des.getFilePath());
              if (_tempFile.renameTo(tmpFile)) {
                result = tmpFile.length();
              }
            } else {
              WebdavResource webdavRes = Util.initWebdavResource(_domain);
              if (webdavRes == null) {
                _tempFile.delete();
                _tempFile = null;
                return result;
              }
              result = Util.putFile(webdavRes, des.getFileDomain(), des.getFilePath(), _tempFile);
            }
            _tempFile = Util.createCache();
            des = block.getBlockFileDescriptor(i);
            i++;
            fOut = new FileOutputStream(_tempFile.getCanonicalPath(), true);
            fOut.write(tmpBuf, (int)partSize, (int)(bufSize-partSize));
          } else {
            fOut.write(tmpBuf, 0, bufSize);
          }
          fOut.flush();
        }
      } while (bufSize == tmpBuf.length);
      fOut.close();

      zIn.close();
      bIn.close();

      if (last) {
        if (_isLocal) {
          File partRootFile = Util.initLocalResource(des.getFileDomain());
          File tmpFile = new File(partRootFile.getCanonicalPath() +
            File.separatorChar + des.getFilePath());
          if (_tempFile.renameTo(tmpFile)) {
            _tempFile = null;
            return tmpFile.length();
          }
        } else {
          WebdavResource webdavRes = Util.initWebdavResource(_domain);
          if (webdavRes == null) {
            _tempFile.delete();
            _tempFile = null;
            return result;
          }
          result = Util.putFile(webdavRes, des.getFileDomain(), des.getFilePath(), _tempFile);
        }
      } else {
        return _tempFile.length();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * reads the content of an existing file
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @param block The sequential block number for the data to be read starting
   * with 1
   *
   * @param len The length of the block in bytes
   *
   * @return The contents of the file
   */
  public byte[] readFromFile(String domain, String path, int block, int len) {
    setDomain(domain);
    return readFromFile(path, block, len);
  }

  /**
   * reads the content of an existing file using the current domain
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @param block The sequential block number for the data to be read starting
   * with 1
   *
   * @param len The length of the block in bytes
   *
   * @return The contents of the file
   */
  public byte[] readFromFile(String path, int block, int len) {
    byte[] buffer=null;
    try {
      if (_isLocal) {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar +
          path);
        if (tmpFile.isFile()) {
          RandomAccessFile in = new RandomAccessFile(tmpFile, "r");
          in.seek((block-1)*len);
          int result=-1;
          buffer = new byte[len];
          if (in.getFilePointer() < in.length()) {
            result = in.read(buffer);
            ByteArrayOutputStream out = new ByteArrayOutputStream(result);
            out.write(buffer, 0, result);
            in.close();
            return out.toByteArray();
          } else {
            in.close();
          }
        }
      } else {
        buffer = _remote.readFromFile(_domain, path, block, len);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return buffer;
  }

  /**
   * reads the content of an existing file using the current domain
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @param offset the offset from the beginning of the file.
   *
   * @param len The length of the block in bytes
   *
   * @return The contents of the file
   */
  public byte[] readByteFromFile(String path, long offset, int len)
          throws EOFException, FileAccessException
  {
    try
    {
      if (_isLocal)
      {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar +
          path);
        if (tmpFile.isFile())
        {
          RandomAccessFile raf = new RandomAccessFile(tmpFile, "r");
          byte[] buffer = new byte[len];
          raf.seek(offset);
          int totalByteRead = 0;
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          int result = 0;
          while (totalByteRead < len && raf.getFilePointer() < raf.length())
          {
            result = raf.read(buffer, 0, (len - totalByteRead));
            if (result != -1)
            {
              out.write(buffer, 0 , result);
              totalByteRead += result;
            }
            else
              if (totalByteRead == 0)
                throw new EOFException("End of file reached!");
              else
                break;
          }
          raf.close();
          out.flush();
          out.close();
          return out.toByteArray();
        }
        else
          throw new FileAccessException("Path is not a file");
      }
      else
        return _remote.readByteFromFile(_domain, path, offset, len);
    }
    catch (EOFException eofe)
    {
      throw eofe;
    }
    catch (FileAccessException fae)
    {
      throw fae;
    }
    catch (Exception e)
    {
      throw new FileAccessException(e);
    }
  }

  /**
   * reads the content of an existing file in zipped blocks
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param path The path relative to the domain for the file
   *
   * @param offset The offset in the file to start reading from
   *
   * @param len The length of the block in bytes in zipped form
   *
   * @return The contents of the file in zipped form
   */
  public byte[] readFromFileZipped(String domain, String path, long offset, int len) {
    setDomain(domain);
    return readFromFileZipped(path, offset, len);
  }

  /**
   * reads the content of an existing file in zipped blocks using the current domain
   *
   * @param path The path relative to the domain for the file
   *
   * @param offset The offset in the file to start reading from
   *
   * @param len The length of the block in bytes in zipped form
   *
   * @return The contents of the file in zipped form
   */
  public byte[] readFromFileZipped(String path, long offset, int len) {
    //int result=-1;
    byte[] buffer=null;
    try {
      if (_isLocal) {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar +
          path);
        if (tmpFile.isFile()) {
          RandomAccessFile in = new RandomAccessFile(tmpFile, "r");
          in.seek(offset);
          int totalBufSize = 0;
          int readResult=0;
          buffer = new byte[len];
          ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
          GZIPOutputStream zOut = new GZIPOutputStream(bOut, len);

          while (totalBufSize < len && in.getFilePointer() < in.length()) {
            readResult = in.read(buffer);
            if (readResult != -1) {
              zOut.write(buffer);
              //bufSize = bOut.size();
              totalBufSize += readResult;
            } else {
              break;
            }
          }
          zOut.close();
          in.close();
          return bOut.toByteArray();
        }
      } else {
        buffer = _remote.readFromFileZipped(_domain, path, offset, len);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return buffer;
  }

  /**
   * reads the content of a group of existing files in a zipped block
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param paths The paths relative to the domain for the files to be read
   *
   * @param offset The offset in the file to start reading from
   *
   * @param len The length of the block in bytes in zipped form
   *
   * @return The contents of the file in zipped form together with the
   * description of the files
   */
  public MultiFileBlock readFromStreamZipped(String domain, String[] paths, long offset, int len) {
    setDomain(domain);
    return readFromStreamZipped(paths, offset, len);
  }

  /**
   * reads the content of a group of existing files in a zipped block
   *
   * @param domain The namespace used to identify the application domain (1st
   * level directory) to use
   *
   * @param paths The paths relative to the domain for the files to be read
   *
   * @param offset The offset in the file to start reading from
   *
   * @param len The length of the block in bytes in zipped form
   *
   * @return The contents of the file in zipped form together with the
   * description of the files
   */
  public MultiFileBlock readFromStreamZipped(String[] paths, long offset, int len) {
    MultiFileBlock block=null;
    try {
      if (_isLocal) {
        int startFileIndex = Util.getStartFileIndex(_rootFile.getCanonicalPath()
          +File.separatorChar, paths, offset);
        long startFileOffset = Util.getStartFileOffset(_rootFile.getCanonicalPath()
          +File.separatorChar, paths, offset);
        int i = startFileIndex;
        long j = startFileOffset;
        //int bufSize=0;
        int totalBufSize=0;
        int readResult=0;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
        GZIPOutputStream zOut = new GZIPOutputStream(bOut, len);
        block = new MultiFileBlock();
        while (totalBufSize < len && i < paths.length) {
          File tmpFile = new File(_rootFile.getCanonicalPath()+File.separatorChar+paths[i]);
          if (tmpFile.isFile() && tmpFile.exists()) {
            RandomAccessFile in = new RandomAccessFile(tmpFile, "r");
            byte[] tmpBuf = new byte[len-totalBufSize];
            in.seek(j);
            while (totalBufSize < len && in.getFilePointer() < in.length()) {
              readResult = in.read(tmpBuf);
              if (readResult != -1) {
                zOut.write(tmpBuf, 0, readResult);
                //bufSize = bOut.size();
                totalBufSize += readResult;
              } else {
                break;
              }
            }
            BlockFileDescriptor des = new BlockFileDescriptor(_domain, paths[i], in.length());
            block.addBlockFileDescriptor(des);
            in.close();
            i++;
            j=0;
          } else {
            return null;
          }
        }
        zOut.close();
        block.setBlockData(bOut.toByteArray());
      } else {
        block = _remote.readFromStreamZipped(_domain, paths, offset, len);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return block;
  }

  /**
   * Tests whether the file denoted by the specified path exists.
   *
   * @param            domain  the domain which the file is to be checked.
   * @param            path    the full path name of the file to be checked.
   * @return           true if and only if the file denoted by the specified path exists; false otherwise
   * @see java.io.File.exist()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public boolean exist(String domain, String path)
  {
    setDomain(domain);
    return exist(path);
  }

  /**
   * Tests whether the file denoted by the specified path exists.
   *
   * @param            path    the full path name of the file to be checked.
   * @return           true if and only if the file denoted by the specified path exists; false otherwise
   * @see java.io.File.exist()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public boolean exist(String path)
  {
    try
    {
      if (_isLocal)
      {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar +
          path);
        return (tmpFile.exists());
      }
      else
      {
        return _remote.exist(_domain, path);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return false;
  }

  /**
   * Returns the length of the file denoted by the specified path.
   *
   * @param           domain  the domain which the file is to be checked.
   * @param           path    the full path name of the file to be checked.
   * @return          The length, in bytes, of the file denoted by this pathname, or 0L if the file does not exist
   * @see java.io.File.length()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public long length(String domain, String path)
  {
    setDomain(domain);
    return length(path);
  }

  /**
   * Returns the length of the file denoted by the specified path.
   *
   * @param           path    the full path name of the file to be checked.
   * @return          The length, in bytes, of the file denoted by this pathname, or 0L if the file does not exist
   * @see java.io.File.length()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public long length(String path)
  {
    try
    {
      if (_isLocal)
      {
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar +
          path);
        return (tmpFile.length());
      }
      else
      {
        return _remote.length(_domain, path);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return 0l;
  }

  /**
   * Atomically creates a new, empty file named by the specified path if and
   * only if a file with this path does not yet exist.
   *
   * @param           domain  the domain which the file is to be created.
   * @param           path    the full path name of the file to be created.
   * @return          true if the named file does not exist and was successfully created; false if the named file already exists
   *
   * @see java.io.File.createNewFile()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public boolean createNewFile(String domain, String path)
  {
    setDomain(domain);
    return createNewFile(path);
  }

  /**
   * Atomically creates a new, empty file named by the specified path if and
   * only if a file with this path does not yet exist.
   *
   * @param           path    the full path name of the file to be created.
   * @return          true if the named file does not exist and was successfully created; false if the named file already exists
   *
   * @see java.io.File.createNewFile()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public boolean createNewFile(String path)
  {
    try
    {
      if (_isLocal)
      {
        Util.createHigherLevelFolders(_rootFile.getCanonicalPath(), path);
        File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + path);
        return tmpFile.createNewFile();
      }
      else
      {
        return _remote.createNewFile(_domain, path);
      }
    }
    catch (IOException ioe)
    {
      return false;
    }
  }

  /**
   * Atomically creates a new, empty file named by the specified path if and
   * only if a file with this path does not yet exist.
   *
   * @param           path    the full path name of the file to be created.
   * @return          the File object if it does not previously exist and was successfully created; false if the named file already exists
   *
   * @see java.io.File.createNewFile()
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   */
  public File createNewLocalFile(String path)
  {
    try
    {
      Util.createHigherLevelFolders(_rootFile.getCanonicalPath(), path);
      File tmpFile = new File(_rootFile.getCanonicalPath() + File.separatorChar + path);
      if (tmpFile.createNewFile())
        return tmpFile;
      else
        return null;
    }
    catch (IOException ioe)
    {
      return null;
    }
  }

  // DDJ: Helper method to shorten byte arrays
  private static byte[] shortenByteArray(byte byteArray[], int newLength)
  {
    if(newLength >= byteArray.length)
      return byteArray;

    byte newByteArray[] = new byte[newLength];
    for(int i = 0; i < newLength; i++)
      newByteArray[i] = byteArray[i];

    return newByteArray;
  }
}