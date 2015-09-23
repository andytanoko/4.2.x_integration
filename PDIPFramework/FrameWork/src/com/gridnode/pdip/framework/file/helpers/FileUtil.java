/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Daniel D'Cotta      Created.
 * Jul 29 2002    Daniel D'Cotta      Moved from gtas-common.
 * Jan 17 2003    Goh Kan Mun         Modified - To use the exist methods on the FileAccess.
 *                                    Modified - Added length methods.
 * Mar 21 2003    Goh Kan Mun         Modified - Added methods to delete folders.
 *                                    Modified - Added method to create new empty file atomicly.
 * Sep 01 2003    Daniel D'Cotta      Fix GNDB00014982 where the case of a directory did not match
 * Jan 28 2004    Neo Sok Lay         Add removeExtension() method to remove file extension.
 * Mar 09 2004    Neo Sok Lay         Auto extract subpath in:-
 *                                    - getFile(pathKey, filename), length(pathKey, filename)
 *                                    - exist(pathKey, filename), create(pathKey, filename, object)
 *                                    - copy(srcPathKey, srcFilename, destPathKey, destFilename)
 *                                    - delete(pathKey, filename), replace(pathKey, filename)
 * Aug 30 2006    Tam Wei Xiang       Add createFolder() method. Bring the changes from ESTORE stream.
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                            
 */
package com.gridnode.pdip.framework.file.helpers;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.access.FileAccess;

import java.io.File;
import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * 
 * Note: Use '/' instead of File.separator due to possible URL based-paths
 *       Also, for compatibility with the framework FileAccess class
 * @author Daniel D'Cotta
 * @since
 * @version GT 4.0 VAN
 */
public class FileUtil
{
  private static Configuration _pathConfig =
    ConfigurationManager.getInstance().getConfig(IPathConfig.CONFIG_NAME);
  protected static FileAccess _fileAccess =
    new FileAccess(_pathConfig.getString(IPathConfig.APP_DOMAIN));

  /**
   * Get a file.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename
   * @return The file or null if file does not exist
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized File getFile(String pathKey, String filename)
    throws FileAccessException
  {
    validateNullFilename(filename);
    return getFile(pathKey, extractPath(filename), extractFilename(filename));
  }

  /**
   * Get a file. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @return The file or null if file does not exist
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized File getFile(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    validateFilename(filename);

    String path = getPath(pathKey) + subPath + filename;
    File file = null;
    Logger.debug("[FileUtil.getFile] path = " + path);
    try
    {
      file = _fileAccess.getFile(path);

      // Returns null if file does not exist
      if (file != null)
        if (!file.exists())
          file = null;
    }
    catch (Exception ex)
    {
      throw new FileAccessException("get file failed", ex);
    }
    Logger.debug("[FileUtil.getFile] Got file from " + path);

    return file; // Returns null if file does not exist
  }

  /**
   * Check if a file exist.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename
   * @return True if the file exist, false if the file does not exist
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized boolean exist(String pathKey, String filename)
    throws FileAccessException
  {
    validateNullFilename(filename);
    return exist(pathKey, extractPath(filename), extractFilename(filename));
  }

  /**
   * Check if a file exist. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @return True if the file exist, false if the file does not exist
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized boolean exist(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    validateFilename(filename);

    String filePath = getPath(pathKey) + subPath + filename;
    Logger.debug("[FileUtil.exist] filePath = " + filePath);
    try
    {
      boolean exist = _fileAccess.exist(filePath);
      Logger.debug(
        "[FileUtil.exist] File " + (exist ? "exist" : "does not exist"));
      return exist;
    }
    catch (Exception ex)
    {
      throw new FileAccessException("Check exist failed", ex);
    }
  }

  /**
   * Returns the length of the file. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename
   * @return the length of the file, in bytes or 0l if file is not found.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized long length(String pathKey, String filename)
    throws FileAccessException
  {
    validateNullFilename(filename);
    return length(pathKey, extractPath(filename), extractFilename(filename));
  }

  /**
   * Returns the length of the file. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @return the length of the file, in bytes or 0l if file is not found.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized long length(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    validateFilename(filename);

    String filePath = getPath(pathKey) + subPath + filename;
    Logger.debug("[FileUtil.length] filePath = " + filePath);
    try
    {
      long length = _fileAccess.length(filePath);
      Logger.debug("[FileUtil.length] File is " + length + " bytes.");
      return length;
    }
    catch (Exception ex)
    {
      throw new FileAccessException("Length failed", ex);
    }
  }

  /**
   * Creates a file from an Object which is either
   * an InputStream or a File.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename
   * @param object The InputStream or File that will be used to create a new file
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String create(
    String pathKey,
    String filename,
    Object object)
    throws FileAccessException
  {
    validateNullFilename(filename);
    return create(pathKey, extractPath(filename), extractFilename(filename), object);
  }

  /**
   * Creates a file from an Object which is either
   * an InputStream or a File. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @param object The InputStream or File that will be used to create a new file
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String create(
    String pathKey,
    String subPath,
    String filename,
    Object object)
    throws FileAccessException
  {
    validateFilename(filename);

    String path = null;
    String filePath =
      getPath(pathKey)
        + subPath
        + getUniqueFilename(pathKey, subPath, filename);
    Logger.debug("[FileUtil.create] filePath = " + filePath);
    try
    {
      if (object instanceof InputStream)
        path = _fileAccess.createFile(filePath, (InputStream) object, false);
      // overwrite: false
      else if (object instanceof File)
        path = _fileAccess.createFile(filePath, (File) object, false);
      // overwrite: false
      else
        throw new java.lang.UnsupportedOperationException(
          "object should be either an InputStream or File");

      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("create failed", ex);
    }
    Logger.log("[FileUtil.create] Create file to " + path);
    return extractRelativeFilePath(pathKey, subPath, path, true);
  }

  /**
   * Creates a file from an Object which is either
   * an InputStream or a File. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @param object The InputStream or File that will be used to create a new file
   * @param overwrite whether to overwrite if the file already exist.
   * @return The filename
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String create(
    String pathKey,
    String subPath,
    String filename,
    Object object,
    boolean overwrite)
    throws FileAccessException
  {
    validateFilename(filename);

    String path = null;
    String filePath = getPath(pathKey) + subPath + filename;
    Logger.debug("[FileUtil.createFile] filePath = " + filePath);
    try
    {
      if (object instanceof InputStream)
        path =
          _fileAccess.createFile(filePath, (InputStream) object, overwrite);
      else if (object instanceof File)
        path = _fileAccess.createFile(filePath, (File) object, overwrite);
      else
        throw new java.lang.UnsupportedOperationException(
          "object should be either an InputStream or File");

      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("Create failed", ex);
    }
    Logger.log("[FileUtil.create] Create file to " + path);
    return extractRelativeFilePath(pathKey, subPath, path, true);
  }

  /**
   * Copies a file to a new location.
   *
   * @param srcPathKey The source IPathConfig constant
   * @param srcFilename The source filename. Supports sub-directory path in filename.
   * @param destPathKey The destination IPathConfig constant
   * @param destFilename The destination filename. Supports sub-directory path in filename.
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String copy(
    String srcPathKey,
    String srcFilename,
    String destPathKey,
    String destFilename)
    throws FileAccessException
  {
    validateNullFilename(srcFilename);
    validateNullFilename(destFilename);
    
    return copy(srcPathKey, extractPath(srcFilename), extractFilename(srcFilename), destPathKey, extractPath(destFilename), extractFilename(destFilename));
  }

  /**
   * Copies a file to a new location. Allows use of subPaths.
   *
   * @param srcPathKey The source IPathConfig constant
   * @param srcSubPath The source relative path based on the pathKey (if any)
   * @param srcFilename The source filename
   * @param destPathKey The destination IPathConfig constant
   * @param destSubPath The destination relative path based on the pathKey (if any)
   * @param destFilename The destination filename
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String copy(
    String srcPathKey,
    String srcSubPath,
    String srcFilename,
    String destPathKey,
    String destSubPath,
    String destFilename)
    throws FileAccessException
  {
    validateFilename(srcFilename);
    validateFilename(destFilename);

    String path = null;
    String srcFilePath = getPath(srcPathKey) + srcSubPath + srcFilename;
    String destFilePath =
      getPath(destPathKey)
        + destSubPath
        + getUniqueFilename(destPathKey, destSubPath, destFilename);
    Logger.debug("[FileUtil.copy] srcFilePath = " + srcFilePath);
    Logger.debug("[FileUtil.copy] destFilePath = " + destFilePath);
    try
    {
      path = _fileAccess.copyFile(srcFilePath, destFilePath, false);
      // overwrite: false
      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("copy failed", ex);
    }
    Logger.log("[FileUtil.copy] Copied file to " + path);
    return extractRelativeFilePath(destPathKey, destSubPath, path, true);
  }

  /**
   * Moves a file to a new location.
   *
   * @param srcPathKey The source IPathConfig constant
   * @param destPathKey The destination IPathConfig constant
   * @param filename The filename
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String move(
    String srcPathKey,
    String destPathKey,
    String filename)
    throws FileAccessException
  {
    return move(srcPathKey, "", destPathKey, "", filename);
  }

  /**
   * Moves a file to a new location. Allows use of subPaths.
   *
   * @param srcPathKey The source IPathConfig constant
   * @param srcSubPath The source relative path based on the pathKey (if any)
   * @param destPathKey The destination IPathConfig constant
   * @param destSubPath The destination relative path based on the pathKey (if any)
   * @param filename The filename
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String move(
    String srcPathKey,
    String srcSubPath,
    String destPathKey,
    String destSubPath,
    String filename)
    throws FileAccessException
  {
    validateFilename(filename);

    String path = null;
    String srcFilePath = getPath(srcPathKey) + srcSubPath + filename;
    String destFilePath =
      getPath(destPathKey)
        + destSubPath
        + getUniqueFilename(destPathKey, destSubPath, filename);
    Logger.debug("[FileUtil.move] srcFilePath = " + srcFilePath);
    Logger.debug("[FileUtil.move] destFilePath = " + destFilePath);
    try
    {
      path = _fileAccess.moveFile(srcFilePath, destFilePath, false);
      // overwrite: false
      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("move failed", ex);
    }
    Logger.log("[FileUtil.move] Moved file to " + path);
    return extractRelativeFilePath(destPathKey, destSubPath, path, true);
  }

  /**
   * Renames a file.
   *
   * @param pathKey The IPathConfig constant
   * @param srcFilename The source filename
   * @param destFilename The destination filename
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String rename(
    String pathKey,
    String srcFilename,
    String destFilename)
    throws FileAccessException
  {
    return rename(pathKey, "", srcFilename, destFilename);
  }

  /**
   * Renames a file. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param srcFilename The source filename
   * @param destFilename The destination filename
   * @return The filename which may be different due to preventing overwritting
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String rename(
    String pathKey,
    String subPath,
    String srcFilename,
    String destFilename)
    throws FileAccessException
  {
    validateFilename(srcFilename);
    validateFilename(destFilename);

    String path = null;
    String srcFilePath = getPath(pathKey) + subPath + srcFilename;
    String destFilePath =
      getPath(pathKey)
        + subPath
        + getUniqueFilename(pathKey, subPath, destFilename);
    Logger.debug("[FileUtil.rename] srcFilePath = " + srcFilePath);
    Logger.debug("[FileUtil.rename] destFilePath = " + destFilePath);
    try
    {
      path = _fileAccess.renameFile(srcFilePath, destFilePath);
      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("rename failed", ex);
    }
    Logger.log("[FileUtil.rename] Renamed file to " + path);
    return extractRelativeFilePath(pathKey, subPath, path, true);
  }

  /**
   * Deletes a file.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename. Supports sub-directory path in filename.
   * @return The filename
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String delete(String pathKey, String filename)
    throws FileAccessException
  {
    validateNullFilename(filename);
    return delete(pathKey, extractPath(filename), extractFilename(filename));
  }

  /**
   * Deletes a file. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @return The filename
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String delete(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    validateFilename(filename);
    String path = null;
    String filePath = getPath(pathKey) + subPath + filename;
    Logger.debug("[FileUtil.delete] filePath = " + filePath);
    try
    {
      path = _fileAccess.deleteFile(filePath);
      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("delete failed", ex);
    }
    Logger.log("[FileUtil.delete] Deleted file from " + path);
    return extractRelativeFilePath(pathKey, subPath, path, true);
  }

  /**
   * Deletes a folder. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param foldername The relative path of the folder based on the pathKey (if any)
   * @return The relative path of the folder deleted
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String deleteFolder(
    String pathKey,
    String foldername)
    throws FileAccessException
  {
    String path = null;
    String filePath = getPath(pathKey) + foldername;
    Logger.debug("[FileUtil.deleteFolder] filePath = " + filePath);
    try
    {
      path = _fileAccess.deleteFolder(filePath);
      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("delete failed", ex);
    }
    Logger.log("[FileUtil.deleteFolder] Deleted folder from " + path);
    return extractRelativeFilePath(pathKey, path, true);
  }

  /**
   * Deletes a folder. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param foldername The relative path of the folder based on the pathKey (if any)
   * @return The relative path of the folder deleted
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String deleteFolder(
    String pathKey,
    String subPath,
    String foldername)
    throws FileAccessException
  {
    String path = null;
    String filePath = getPath(pathKey) + subPath + foldername;
    Logger.debug("[FileUtil.deleteFolder] filePath = " + filePath);
    try
    {
      path = _fileAccess.deleteFolder(filePath);
      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("delete failed", ex);
    }
    Logger.log("[FileUtil.deleteFolder] Deleted folder from " + path);
    return extractRelativeFilePath(pathKey, subPath, path, true);
  }

  /**
   * Replaces a file from an Object which is either
   * an InputStream or a File.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename. Supports sub-directory path in filename.
   * @param object The InputStream or File that will be used to replace the file
   * @return The filename
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String replace(
    String pathKey,
    String filename,
    Object object)
    throws FileAccessException
  {
    validateNullFilename(filename);
    
    return replace(pathKey, extractPath(filename), extractFilename(filename), object);
  }

  /**
   * Replaces a file from an Object which is either
   * an InputStream or a File. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @param object The InputStream or File that will be used to replace the file
   * @return The filename
   * @exception FileAccessException any exception caught while performing file services
   */
  public static synchronized String replace(
    String pathKey,
    String subPath,
    String filename,
    Object object)
    throws FileAccessException
  {
    validateFilename(filename);

    String path = null;
    String filePath = getPath(pathKey) + subPath + filename;
    Logger.debug("[FileUtil.replace] filePath = " + filePath);
    try
    {
      if (object instanceof InputStream)
        path = _fileAccess.createFile(filePath, (InputStream) object, true);
      // overwrite: true
      else if (object instanceof File)
        path = _fileAccess.createFile(filePath, (File) object, true);
      // overwrite: true
      else
        throw new java.lang.UnsupportedOperationException(
          "object should be either an InputStream or File");

      if (path == null)
        throw new java.lang.NullPointerException("path is null");
    }
    catch (Exception ex)
    {
      throw new FileAccessException("replace failed", ex);
    }
    Logger.log("[FileUtil.replace] Replace file to " + path);
    return extractRelativeFilePath(pathKey, subPath, path, true);
  }

  /**
   * Gets the GTAS domain key.
   *
   * @return The domain key.
   * @exception FileAccessException because unable to get domain key
   */
  public static String getDomain() throws FileAccessException
  {
    String domain = _pathConfig.getString(IPathConfig.APP_DOMAIN);
    if (domain == null)
      throw new FileAccessException("unable to get GTAS domain");
    return domain;
  }

  /**
   * Gets the path of the pathKey with relation to the domain.
   *
   * @param pathKey The IPathConfig constant
   * @return The path of the pathKey with relation to the domain
   * @exception FileAccessException because unable to get path for pathKey
   */
  public static String getPath(String pathKey) throws FileAccessException
  {
    String path = _pathConfig.getString(pathKey);
    if (path == null
      || path.length() == 0) // 20021231 DDJ: Also check for empty string
      throw new FileAccessException(
        "unable to get path for pathKey=" + pathKey);
    return path;
  }

  /**
   * Gets an unique filename. A filename which will guarantee no overwritting.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename
   * @return The unique filename which will guarantee no overwritting.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String getUniqueFilename(String pathKey, String filename)
    throws FileAccessException
  {
    return getUniqueFilename(pathKey, filename, -1);
  }

  /**
   * Gets an unique filename. A filename which will guarantee no overwritting.
   * Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @return The unique filename which will guarantee no overwritting.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String getUniqueFilename(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    return getUniqueFilename(pathKey, subPath, filename, -1);
  }

  /**
   * Gets an unique filename. A filename which will guarantee no overwritting,
   * unless the <I>limit</I> has been reached.
   *
   * @param pathKey The IPathConfig constant
   * @param filename The filename
   * @return The unique filename which will guarantee no overwritting.
   * @param limit The limit for generating unique filenames. <I>-1</I> for
   * unlimitted searching for a unique filename.
   * @return The unique filename which will guarantee no overwritting if <I>limit</I> is -1
   * or <I>limit</I> has not been reached. Otherwise, if the limit is reached
   * and a unique filename  still cannot be found, the filename with <I>limit</I>
   * as the postfix is returned.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String getUniqueFilename(
    String pathKey,
    String filename,
    int limit)
    throws FileAccessException
  {
    return getUniqueFilename(pathKey, "", filename, limit);
  }

  /**
   * Gets an unique filename. A filename which will guarantee no overwritting,
   * unless the <I>limit</I> has been reached. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param filename The filename
   * @return The unique filename which will guarantee no overwritting.
   * @param limit The limit for generating unique filenames. <I>-1</I> for
   * unlimitted searching for a unique filename.
   * @return The unique filename which will guarantee no overwritting if <I>limit</I> is -1
   * or <I>limit</I> has not been reached. Otherwise, if the limit is reached
   * and a unique filename  still cannot be found, the filename with <I>limit</I>
   * as the postfix is returned.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String getUniqueFilename(
    String pathKey,
    String subPath,
    String filename,
    int limit)
    throws FileAccessException
  {
    validateFilename(filename);

    int index;
    String name = null;
    String ext = null;
    for (int i = 1;
      exist(pathKey, subPath, filename) && (i <= limit || limit < 0);
      i++)
    {
      // Check if name and ext have been initialised
      if (name == null)
      {
        index = filename.lastIndexOf('.');
        if (index != -1)
        {
          ext = filename.substring(index);
          name = filename.substring(0, index);
        }
        else
        {
          name = filename;
          ext = "";
        }
      }

      filename = name + "_" + i + ext;
    }
    return filename;
  }

  /**
   * Get the relative path from a location to another location.
   * Determine the argument for a <B>cd</B> from a directory to another.
   *
   * @param basePathKey The base IPathConfig constant
   * @param destPathKey The destination IPathConfig constant
   * @return The relative path from base directory to destination directorty.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String getRelativePathFrom(
    String basePathKey,
    String destPathKey)
    throws FileAccessException
  {
    return getRelativePathFrom(basePathKey, "", destPathKey, "");
  }

  /**
   * Get the relative path from a location to another location.
   * Determine the argument for a <B>cd</B> from a directory to another.
   *
   * @param basePathKey The base IPathConfig constant
   * @param baseSubPath The base relative path based on the pathKey (if any)
   * @param destPathKey The destination IPathConfig constant
   * @param destSubPath The destination relative path based on the pathKey (if any)
   * @return The relative path from base directory to destination directorty.
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String getRelativePathFrom(
    String basePathKey,
    String baseSubPath,
    String destPathKey,
    String destSubPath)
    throws FileAccessException
  {
    String basePath = getPath(basePathKey) + baseSubPath;
    String destPath = getPath(destPathKey) + destSubPath;

    if (basePath == null
      || basePath.length() == 0
      || destPath == null
      || destPath.length() == 0)
      return null;

    // Nothing to cd for same directory
    if (basePath.equalsIgnoreCase(destPath))
      return "";

    // Path separtor can be '/' or '\'
    StringTokenizer st1 = new StringTokenizer(basePath, "/\\");
    StringTokenizer st2 = new StringTokenizer(destPath, "/\\");
    int baseCount = st1.countTokens();
    int destCount = st2.countTokens();

    String base = "";
    String dest = "";
    int i = 0;
    // Compare until a pair of corresponding dir components do not match
    while (i < baseCount && i < destCount)
    {
      base = st1.nextToken();
      dest = st2.nextToken();
      if (!base.equalsIgnoreCase(dest))
        break;

      i++;
    }

    // Each of the directories left in basePath constitutes a "../" in the relative path
    String relPath = "";
    for (int j = baseCount - i; j > 0; j--)
      relPath += "../";

    // Rest of the directories in destPath append to the relative path
    if (i < destCount)
    {
      relPath += dest + "/";
      while (st2.hasMoreTokens())
      {
        relPath += (st2.nextToken() + "/");
      }
    }
    return relPath;
  }

  /**
   * Get the filepath without the pathKey's path prefix. Also removes the domain
   * if <I>removeDomain</I> is true.
   *
   * @param pathKey The IPathConfig constant
   * @param fullPath The full path which contains the the PathKey as a prefix
   * @param removeDomain True is the fullPath which also contains the domain as a prefix,
   * false if fullPath does not contain the domain
   * @return The remaining path (which may just be the file name)
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String extractRelativeFilePath(
    String pathKey,
    String fullPath,
    boolean removeDomain)
    throws FileAccessException
  {
    return extractRelativeFilePath(pathKey, "", fullPath, removeDomain);
  }

  /**
   * Get the filepath without the pathKey's path prefix. Also removes the domain
   * if <I>removeDomain</I> is true. Allows use of a subPath.
   *
   * @param pathKey The IPathConfig constant
   * @param subPath The relative path based on the pathKey (if any)
   * @param fullPath The full path which contains the the PathKey as a prefix
   * @param removeDomain True is the fullPath which also contains the domain as a prefix,
   * false if fullPath does not contain the domain
   * @return The remaining path (which may just be the file name)
   * @exception FileAccessException any exception caught while performing file services
   */
  public static String extractRelativeFilePath(
    String pathKey,
    String subPath,
    String fullPath,
    boolean removeDomain)
    throws FileAccessException
  {
    Logger.debug(
      "[FileUtil.extractRelativeFilePath] removeDomain = "
        + (removeDomain ? "true" : "false"));
    Logger.debug("[FileUtil.extractRelativeFilePath] fullPath = " + fullPath);

    String path = null;
    if (removeDomain)
    {
      path = _fileAccess.getPathWithoutDomain(fullPath);
    }
    else
    {
      path = fullPath;
    }
    String basePath = getPath(pathKey) + subPath;

    Logger.debug("[FileUtil.extractRelativeFilePath] path = " + path);
    Logger.debug("[FileUtil.extractRelativeFilePath] basePath = " + basePath);

    // Use '/' instead of File.separator due to possible URL based-paths
    path = convertPath(path);
    basePath = convertPath(basePath);

    // 01092003 DDJ: Fix GNDB00014982 where the case of a directory did not match
    //if(path.startsWith(basePath))
    if (path.substring(0, basePath.length()).equalsIgnoreCase(basePath))
    {
      String relativePath = path.substring(basePath.length());

      // Remove leading file seperator
      while (relativePath.charAt(0) == '/' || relativePath.charAt(0) == '\\')
        relativePath = relativePath.substring(1);

      Logger.debug(
        "[FileUtil.extractRelativeFilePath] relativePath = " + relativePath);
      return relativePath;
    }
    else
    {
      throw new FileAccessException("path does not match basePath");
    }
  }

  /**
   * Checks that the filename is not null and has no path.
   * @exception FileAccessException because filename is null or contains path
   */
  public static void validateFilename(String filename)
    throws FileAccessException
  {
    if (filename == null
      || filename.indexOf('/') != -1
      || filename.indexOf('\\') != -1)
    {
      Logger.warn("[FileUtil.validateFilename] Failed: filename = " + filename);
      throw new FileAccessException("filename is null or contains path");
    }
  }

  /**
   * Checks that the filename is not null
   * @exception FileAccessException because filename is null
   */
  public static void validateNullFilename(String filename)
    throws FileAccessException
  {
    if(filename == null)
    {
      Logger.warn("[FileUtil.validateNullFilename] Failed: filename is null");
      throw new FileAccessException("filename is null");
    }
  }

  /**
   * Get the filename without any path.
   */
  public static String extractFilename(String filePath)
  {
    return filePath.substring(
      Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\')) + 1);
  }

  /**
   * Get the path without any filename (includes the last '/' or '\').
   */
  public static String extractPath(String filePath)
  {
    return filePath.substring(
      0,
      Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\')) + 1);
  }

  /**
   * Convert '\' into '/'.
   */
  public static String convertPath(String path)
  {
    return path.replace('\\', '/');
  }

  /**
   * Atomically creates a new, empty file named by the specified path if and
   * only if a file with this path does not yet exist.
   *
   * @param         pathKey The IPathConfig constant
   * @param         subPath The sub path name of the file to be created.
   * @param         filename The filename
   * @return        true if the named file does not exist and was successfully created; false if the named file already exists
   *
   * @exception FileAccessException any exception caught while performing file services
   *
   * @author Goh Kan Mun
   * @since 2.0
   * @version 2.0
   *
   */
  public static File createNewLocalFile(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    validateFilename(filename);
    String filePath = getPath(pathKey) + subPath + filename;
    Logger.debug("[FileUtil.createNewLocalFile] filePath = " + filePath);
    return _fileAccess.createNewLocalFile(filePath);
  }

  /**
   * Removes the file extension from a filename
   *
   * @param filename the Filename
   * @return The filename, with its extension removed, if any.
   */
  public static String removeExtension(String filename)
  {
    int start = filename.lastIndexOf('.');
    if (start > -1)
    {
      return filename.substring(0, start);
    }
    else
      return filename;
  }

  /**
   * Get a modified version of a filename by adding a prefix and/or replacing the suffix.
   * This method assumes no path component is provided in the oriFilename.
   * 
   * @param oriFilename The filename to base on, e.g. test.txt
   * @param prefix The prefix to be prepend to the filename, e.g. "pre-"
   * @param suffix The suffix to be replaced in the filename, e.g. "ing.doc"
   * 
   * @return the resulting name, e.g. "pre-testing.doc"
   */
  public static String changeName(String oriFilename, String prefix, String suffix)
  {
    String newBasename = removeExtension(oriFilename);
    StringBuffer buff = new StringBuffer(newBasename);
    if (prefix != null)
      buff.insert(0, prefix);
    if (suffix != null)
      buff.append(suffix);
    
    return buff.toString();    
  }
  
/**
   * Creates a new folder in a webdav server using the current domain.
   * @param pathKey The IPathConfig constant
   * @param folderPath The name of the folder to be created. It can also be a nested
   * 				subpath name. eg data/nested1/nested2
   * @return the complete path of the folder created or null if not created.
   */
  public static synchronized String createFolder(String pathKey, String folderPath)
  	throws FileAccessException
  {
  	try
  	{
  		validateNullFilename(folderPath);
  		String filePath = getPath(pathKey)+folderPath;
  		String completePath =  _fileAccess.createFolder(filePath);
  		Logger.log("[FileUtil.createFolder] folder "+completePath+" has been created.");
  		return completePath;
  	}
  	catch(Exception ex)
  	{
  		throw new FileAccessException("[FileUtil.createFolder] create folder failed",ex);
  	}
  }

}