/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveZipFileHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.helper;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.xml.XMLBeanUtil;

/**
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveZipFile implements Serializable
{
  public static final String OPEN_MODE_READ = "read";
  public static final String OPEN_MODE_WRITE = "write";
  
  private static final String CLASS_NAME = "ArchiveZipFile";
  private static final int BUFFER_SIZE = 512;
  private static Calendar _calendar = Calendar.getInstance();
  
  private Logger _logger;
  private ZipFile _zipFile;
  private ZipOutputStream _zipOutput;
  private String _zipFileAbsPath;
  private int _totalZipEntries;
  private long _currentZipFileSize;
  
  private String _openMode;
  
  public ArchiveZipFile(String zipFilePath)
  {
    _zipFileAbsPath = zipFilePath;
    _logger = getLogger();
  }
  
  public void open(String openMode) throws Exception
  { 
    if(OPEN_MODE_READ.equals(openMode))
    {
      _zipFile = new ZipFile(_zipFileAbsPath);
    }
    else if(OPEN_MODE_WRITE.equals(openMode))
    {
      FileOutputStream output = new FileOutputStream(_zipFileAbsPath);
      _zipOutput = new ZipOutputStream(output);
    }
    else
    {
      throw new IllegalArgumentException("The open mode "+openMode+" is not supported !");
    }
    
    _openMode = openMode;
  }
  
  /**
   * Add the content (in byte[] format) into the zip file.
   * @param content the content that we will be adding into zip file.
   * @param filename the filename for the content.
   * @param folder the folder name or formed by a hieracy of folder name (eg folder1/folder2) that
   *               will be used to contain the content. 
   */
  public void addByteArrayToZip(byte[] content, String filename, String folder) throws ArchiveTrailDataException
  {
    assertWriteMode();
    String methodName = "addByteArrayToZip";
    
    if(content == null || content.length == 0)
    {
      _logger.logMessage(methodName, null, "The given content is null or empty !");
      return;
    }
    
    //Setting for ZipEntry
    String entryName = validateFolderPath(folder)+filename;
    ZipEntry zipEntry = new ZipEntry(entryName);
    zipEntry.setSize(content.length);
    zipEntry.setTime(_calendar.getTimeInMillis());
    
    //Start Zip !
    ByteArrayInputStream input = new ByteArrayInputStream(content);
    int readSoFar = 0;
    byte[] buffer = new byte[BUFFER_SIZE];
    
    try
    {
      _zipOutput.putNextEntry(zipEntry);
      while( (readSoFar = input.read(buffer)) > 0)
      {
        _zipOutput.write(buffer, 0, readSoFar);
      }
      _zipOutput.closeEntry();
      ++_totalZipEntries;
      _currentZipFileSize += content.length;
    }
    catch(Exception ex)
    {
      closeOutputStream(_zipOutput);
      throw new ArchiveTrailDataException("["+CLASS_NAME+".addByteArrayToZip] Error in adding byte content into zip file "+_zipFileAbsPath, ex);
    }
    finally
    {
      closeInputStream(input);
    }
  }
  
  public byte[] getZipEntryContentInByte(ZipEntry zipEntry) throws Exception
  {
    assertReadMode();
    
    InputStream input = null;
    BufferedInputStream buffInput = null;
    ByteArrayOutputStream out = null;
    try
    {
      input = _zipFile.getInputStream(zipEntry);
      buffInput = new BufferedInputStream(input);
      out = new ByteArrayOutputStream();
    
      int readSoFar = 0;
      byte[] buffer = new byte[BUFFER_SIZE];
    
      while( (readSoFar = buffInput.read(buffer)) != -1)
      {
        out.write(buffer, 0, readSoFar);
      }
      return out.toByteArray();
    }
    catch(Exception ex)
    {
      throw ex;
    }
    finally
    {
      closeInputStream(input);
      closeInputStream(buffInput);
      closeOutputStream(out);
    }
  }
  
  /**
   * 
   * @param fileToBeAdded The file to be added. File only.
   * @param folder the folder name or formed by a hieracy of folder name
   */
  public void addFileToZip(File fileToBeAdded, String folder) throws ArchiveTrailDataException
  {
    assertWriteMode();
    
    if(fileToBeAdded == null || fileToBeAdded.isDirectory())
    {
      throw new ArchiveTrailDataException("["+CLASS_NAME+".addFileToZip] The given file cannot be null or we expecting a file instead of a directory");
    }
    folder = validateFolderPath(folder);
    
    String entryName = folder + fileToBeAdded.getName();
    ZipEntry zipEntry = new ZipEntry(entryName);
    zipEntry.setTime(fileToBeAdded.lastModified());
    
    FileInputStream input = null;
    try
    {
      _zipOutput.putNextEntry(zipEntry);
    
      input = new FileInputStream(fileToBeAdded);
      byte[] buffer = new byte[BUFFER_SIZE];
      int readSoFar = 0;
      while( (readSoFar = input.read(buffer)) >= 0)
      {
        _zipOutput.write(buffer, 0 ,readSoFar);
      }
      _zipOutput.closeEntry();
      input.close();
      
      ++_totalZipEntries;
      _currentZipFileSize += fileToBeAdded.length();
    }
    catch(Exception ex)
    {
      closeInputStream(input);
      closeOutputStream(_zipOutput);
      throw new ArchiveTrailDataException("["+CLASS_NAME+".addFileToZip] Error in adding the file "+fileToBeAdded.getAbsolutePath()+" into zip file "+_zipFileAbsPath, ex);
    }
  }
  
  public void closeZipFile() throws IOException
  {
    assertWriteMode();
    if(_zipOutput != null)
    {
      _zipOutput.close();
    }
  }
  
  public int getTotalEntries()
  {
    if(_zipFile != null)
    {
      return _zipFile.size();
    }
    return _totalZipEntries;
  }
  
  public String getFilePath()
  {
    if(_zipFile != null)
    {
      return _zipFile.getName();
    }
    
    return _zipFileAbsPath;
  }
  
  public long getZipFileSize()
  {
    return _currentZipFileSize;
  }
  
  /**
   * 
   * @return The zip entries within the zip file.
   */
  public Enumeration getZipEntries()
  {
    assertReadMode();
    return _zipFile.entries();
  }
  
  public ZipEntry getZipEntry(String entryName)
  {
    assertReadMode();
    return _zipFile.getEntry(entryName);
  }
  
  private String validateFolderPath(String folderPath)
  {
    folderPath = replaceSlash(folderPath);
    folderPath = appendSlash(folderPath);
    return folderPath;
  }
  
  private String replaceSlash(String folderPath)
  {
    if(folderPath == null)
    {
      return folderPath;
    }
    else
    {
      folderPath = folderPath.replace('\\', '/');
      return folderPath;
    }
  }
  
  private String appendSlash(String folderPath)
  {
    if(folderPath == null || folderPath.length() == 0) return "";
    
    if( folderPath.lastIndexOf("/") < (folderPath.length() - 1))
    {
      return folderPath+"/";
    }
    else
    {
      return folderPath;
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  private void closeInputStream(InputStream input)
  {
    String methodName = "closeInputStream";
    if(input != null)
    {
      try
      {
        input.close();
      }
      catch(Exception ex)
      {
        _logger.logWarn(methodName, null, "Error in closing the input stream.....", ex);
      }
    }
  }
  
  private void closeOutputStream(OutputStream output)
  {
    String methodName = "closeOutputStream";
    if(output != null)
    {
      try
      {
        output.close();
      }
      catch(Exception ex)
      {
        _logger.logWarn(methodName, null, "Error in closing the output stream.....", ex);
      }
    }
  }
  
  private void assertWriteMode()
  {
    if(! OPEN_MODE_WRITE.equals(_openMode))
    {
      throw new IllegalStateException("The openMode is "+_openMode+". The operation is illegal.");
    }
  }
  
  private void assertReadMode()
  {
    if(! OPEN_MODE_READ.equals(_openMode))
    {
      throw new IllegalStateException("The openMode is "+_openMode+". The operation is illegal.");
    }
  }
}
