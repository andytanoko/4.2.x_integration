/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 09, 2007   i00107              Created
 */

package com.gridnode.util.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.FileUtils;

/**
 * @author i00107
 * A FileClient allows accessing files in a destination folder and its subfolders.
 */
public class FileClient
{
  public static final String PATH_TEMP = "temp.basedir";
  public static final String PATH_DOC = "doc.basedir";
  public static final String PATH_WAIT = "wait.basedir";

  
  private File _destRoot;
  
  /**
   * Construct a FileClient instance for the specified folder  
   * @param root the base directory that this FileClient instance will work in.
   */
  public FileClient(String root) throws IOException
  {
    _destRoot = new File(root).getCanonicalFile();
  }

  /**
   * Transfer the specified file to the configured destination root folder. If the destination root folder already
   * contains a file with the specified name, the file will be overwritten. The specified file will remain in its source location
   * after the transfer.
   * @param fileUri The URI of the file to transfer
   * @return The filename of the transferred file at the destination root folder.
   * @throws IOException If there is problem transferring the specified file.
   */
  public String transferFile(URI fileUri) throws IOException
  {
    return transferFile(fileUri, false);
  }
  
  /**
   * Transfer the specified file to the configured destination root folder. If the destination root folder already
   * contains a file with the specified name, the file will be overwritten. 
   * @param fileUri The URI of the file to transfer
   * @param deleteSourceAfterTransfer <b>true</b> to delete the source file after the transfer, <b>false</b> otherwise.
   * @return The filename of the transferred file at the destination root folder.
   * @throws IOException If there is problem transferring the specified file.
   */
  public String transferFile(URI fileUri, boolean deleteSourceAfterTransfer) throws IOException
  {
    return transferFile(fileUri, null, deleteSourceAfterTransfer);
  }

  /**
   * Get the URI of the file at the destination root folder.
   * @param filename The filename
   * @param subPath The sub pathname of the file under the destination root folder.
   * @return The URI of the file.
   */
  public URI getFileURI(String filename, String subPath)
  {
    File parentDir = (subPath==null) ? _destRoot : new File(_destRoot, subPath);
    File f = new File(parentDir, filename);
    return f.toURI();
  }
  
  /**
   * Transfer the specified file to the subpath at the configured destination root folder. If the subpath already contains a file
   * with the specified name, the file will be overwritten.
   * @param fileUri The URI of the file to transfer.
   * @param subPath The subpath under the destination root folder where the file will be transferred to.
   * @param deleteSourceAfterTransfer <b>true</b> to delete the source file after the transfer, <b>false</b> otherwise.
   * @return The filename of the transferred file at the destination root/subpath folder.
   * @throws IOException If there is problem transferring the specified file.
   */
  public String transferFile(URI fileUri, String subPath, boolean deleteSourceAfterTransfer) throws IOException
  {
    File fileToTrf = FileUtils.toFile(fileUri.toURL());
    System.out.println("[FileClient.transferFile] fileToTrf="+fileToTrf.getCanonicalPath());
    File destDir = (subPath==null)? _destRoot : new File(_destRoot, subPath);
    System.out.println("[FileClient.transferFile] destDir="+destDir.getCanonicalPath());
    
    FileUtils.copyFileToDirectory(fileToTrf, destDir);
    if (deleteSourceAfterTransfer)
    {
      FileUtils.forceDelete(fileToTrf);
    }
    return fileToTrf.getName();
  }
  
  /**
   * To store the specified contents to a file. If the file already exists, it will be overwritten with the 
   * specified contents.
   * @param content The contents to store
   * @param subPath The subpath under the destination root folder where the file will be stored.
   * @param filename The filename of the file to store. 
   * @throws IOException If there is problem storing the file.
   */
  public void storeFile(byte[] content, String subPath, String filename) throws IOException
  {
    File parentDir = getDirectory(subPath, true);
    File f = new File(parentDir, filename);
    FileUtils.writeByteArrayToFile(f, content);
  }
  
  /**
   * To load the contents of the specified file.
   * @param subPath The subpath under the destination root folder where the file is located.
   * @param filename The filename of the file to load
   * @return the file content or <b>null</b> if read failed.
   * @throws IOException If there is problem loading the file.
   */
  public byte[] loadFile(String subPath, String filename) throws IOException
  {
    File parentDir = getDirectory(subPath, true);
    File f = new File(parentDir, filename);
    return FileUtils.readFileToByteArray(f);
  }

  protected File getDirectory(String subPath, boolean createIfNotExists) throws IOException
  {
    File dir = (subPath==null) ? _destRoot : new File(_destRoot, subPath);
    if (createIfNotExists && !dir.exists())
    {
      FileUtils.forceMkdir(dir);
    }
    return dir;
  }
  
  /**
   * To delete a file.
   * @param subPath The subpath under the destination root folder where the file is located.
   * @param filename The filename of the file to delete.
   * @throws IOException If there is problem deleting the file.
   */
  public void deleteFile(String subPath, String filename) throws IOException
  {
    File parentDir = (subPath==null) ? _destRoot : new File(_destRoot, subPath);
    File f = new File(parentDir, filename);
    if (f.exists())
    {
      FileUtils.forceDelete(f);
    }
  }
}
