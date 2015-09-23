/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileDescriptor.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 30 2003    Goh Kan Mun                 Created
 * Feb 06 2003    Goh Kan Mun                 Modified - to fix close reader/writer
 *                                                       twice bug.
 * Mar 21 2003    Goh Kan Mun                 Modified - to fix bug in retrieving file size.
 * Apr 03 2003    Goh Kan Mun                 Modified - to fix bug in concurrency process.
 *                                                       Provide synchronize methods to
 *                                                       retrieve the FileDescriptor so that
 *                                                       concurrent creation of the physical
 *                                                       file will not occur.
 */
package com.gridnode.pdip.base.packaging.helper;

import com.gridnode.pdip.base.packaging.helper.IPackagingConstants;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This Class is used to store the information of files sent during
 * the split.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FileDescriptor
{

  private final static String CLASS_NAME = "FileDescriptor";
  private final static String FILE_DESC_FILENAME = "fileDesc.txt";
  private String _subPath = "";
  private String _fileDesc = "";
  private String _fileId = "";

  /**
   * Constructor. This was used when sending packets of files.
   *
   * @param           files     an array of Files which needs to be sent.
   * @param           fileId    the file Id which is used when the files are sent.
   *
   * @exception       FileAccessException thrown when an error occurs in file operation.
   * @exception       ApplicationException thrown when an error occurs.
   */
  private FileDescriptor(File[] files, String fileId) throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot be null!");
    _fileId = fileId;
    _subPath = _fileId + File.separator;
    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH,
                         _subPath,
                         FILE_DESC_FILENAME))
      getFilesDescFromFile();
    else
    {
      createFilesDesc(files);
      storeFileDescIntoFile();
    }
  }

  /**
   * Constructor. This was used when receiving acknowlegement from the recipient.
   *
   * @param           fileId    the file Id which is used when the files are sent.
   *
   * @exception       FileAccessException thrown when an error occurs in file operation.
   * @exception       ApplicationException thrown when an error occurs.
   */
  private FileDescriptor(String fileId) throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot be null!");
    _fileId = fileId;
    _subPath = _fileId + File.separator;
    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH,
                         _subPath,
                         FILE_DESC_FILENAME))
      getFilesDescFromFile();
    else
    {
      throw new FileAccessException("File Descriptor not found!");
    }
  }

  /**
   * Constructor. This was used when receiving packets of files.
   *
   * @param           fileDesc  file descriptor in String form.
   * @param           fileId    the file Id which is used when the files are sent.
   *
   * @exception       FileAccessException thrown when an error occurs in file operation.
   * @exception       ApplicationException thrown when an error occurs.
   */
  private FileDescriptor(String fileDesc, String fileId)
          throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot be null!");
    if (fileDesc == null)
      throw new ApplicationException("File descriptor cannot be null!");
    _fileId = fileId;
    _subPath = _fileId + File.separator;
    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH,
                         _subPath,
                         FILE_DESC_FILENAME))
      getFilesDescFromFile();
    else
    {
      createFilesDesc(fileDesc);
      storeFileDescIntoFile();
    }
  }

  /**
   * To create the file descriptor in String form.
   *
   * @param           files   the files to be sent.
   */
  private void createFilesDesc(File[] files)
  {
    if (files != null || files.length > 0)
    {
      StringBuffer str = new StringBuffer();
      for (int i = 0; i < files.length; i++)
      {
        if (files[i] == null)
          str.append('\n');
        else
        {
          str.append(files[i].getName());
          str.append('\n');
          str.append(files[i].length());
          str.append('\n');
        }
      }
      _fileDesc = str.toString();
    }
    else
      _fileDesc = null;
  }

  /**
   * To store the file descriptor.
   *
   * @param           fileDesc   the file Descriptor to be stored.
   */
  private void createFilesDesc(String fileDesc)
  {
    _fileDesc = fileDesc;
  }

  /**
   * To store the file descriptor into a physical file.
   *
   * @exception       FileAccessException thrown when error occurs writing to file.
   */
  private synchronized void storeFileDescIntoFile() throws FileAccessException
  {
    File tmpFile = null;
    FileWriter file = null;
    BufferedWriter bw = null;
    PrintWriter pw = null;
    try
    {
      tmpFile = new File(FILE_DESC_FILENAME);
      file = new FileWriter(tmpFile);
      bw = new BufferedWriter(file);
      pw = new PrintWriter(bw);
      pw.println(_fileId);
      pw.println(_subPath);
      if (_fileDesc != null)
        pw.println(_fileDesc.replace('\n', '|'));
      else
        pw.println();
      close(pw);
      pw = null;
      FileHelper.create(IPackagingConstants.PACKAGING_PATH,
                        _subPath,
                        FILE_DESC_FILENAME,
                        tmpFile,
                        true
                       );
      tmpFile.delete();
    }
    catch (IOException ioe)
    {
      throw new FileAccessException("Unable to store File Descriptor to file.", ioe);
    }
    finally
    {
      close(pw);
//      close(bw);
//      close(file);
    }
  }

  /**
   * To retrieve the file description from a stored physical file.
   *
   * @exception       FileAccessException thrown when error occurs writing to file.
   */
  private synchronized void getFilesDescFromFile() throws FileAccessException
  {
    File fileDesc = null;
    FileReader reader = null;
    BufferedReader breader = null;
    try
    {
      fileDesc = FileHelper.getFile(IPackagingConstants.PACKAGING_PATH,
                                    _subPath,
                                    FILE_DESC_FILENAME);
      reader = new FileReader(fileDesc);
      breader = new BufferedReader(reader);
      _fileId = breader.readLine();
      _subPath = breader.readLine();
      _fileDesc = breader.readLine();
      if (_fileDesc == null || _fileDesc.equals(""))
        _fileDesc = null;
      else
        _fileDesc = _fileDesc.replace('|', '\n');
    }
    catch (IOException ioe)
    {
      throw new FileAccessException("Unable to get File Descriptor from file.", ioe);
    }
    finally
    {
      close(breader);
//      close(reader);
    }
  }

  /**
   * Close the reader.
   *
   * @param           r     the reader to be closed.
   */
  private void close(Reader r)
  {
    try
    {
      if (r != null)
        r.close();
    }
    catch (IOException ioe)
    {
      PackagingLogger.warnLog(CLASS_NAME, "close", "Unable to close Reader", ioe);
    }
  }

  /**
   * Close the writer.
   *
   * @param           w     the writer to be closed.
   */
  private void close(Writer w)
  {
    try
    {
      if (w != null)
      {
        w.flush();
        w.close();
      }
    }
    catch (IOException ioe)
    {
      PackagingLogger.warnLog(CLASS_NAME, "close", "Unable to close Reader", ioe);
    }
  }

  /**
   * To retrieve the file descriptor in String form.
   *
   * @return          the file descriptor in String.
   */
  public String getFilesDesciptor()
  {
    return _fileDesc;
  }

  /**
   * To retrieve the sizes of files in the sequence of which they are joined.
   *
   * @return          an array of sizes in the sequence which the
   *                  files were previously joined.
   */
  public long[] getFileSize()
  {
    Vector v = new Vector();
    StringTokenizer st = new StringTokenizer(_fileDesc, "\n", false);
    String token = null;
    while (st.hasMoreTokens())
    {
      token = st.nextToken();
      if (token == null || token.equals(""))
        v.add(null);
      else
        v.add(st.nextToken());
    }
    long[] size = new long[v.size()];
    for (int i = 0; i < v.size(); i++)
    {
      token = (String) v.get(i);
      if (token != null)
        size[i] = Long.parseLong(token);
      else
        size[i] = 0l;
    }
    return size;
  }

  /**
   * To retrieve the filenames in the sequence of the file joined.
   *
   * @return          an array of filenames in the sequence which the
   *                  files were previously joined.
   */
  public String[] getFilename()
  {
    Vector v = new Vector();
    StringTokenizer st = new StringTokenizer(_fileDesc, "\n", false);
    String token = null;
    while (st.hasMoreTokens())
    {
      token = st.nextToken();
      if (token == null || token.equals(""))
        v.add(null);
      else
      {
        v.add(token);
        st.nextToken();
      }
    }
    String[] filename = new String[v.size()];
    for (int i = 0; i < v.size(); i++)
      filename[i] = (String) v.get(i);
    return filename;
  }

  public static synchronized FileDescriptor retrieve(File[] files, String fileId)
          throws FileAccessException, ApplicationException
  {
    return new FileDescriptor(files, fileId);
  }

  public static synchronized FileDescriptor retrieve(String fileDesc, String fileId)
          throws FileAccessException, ApplicationException
  {
    return new FileDescriptor(fileDesc, fileId);
  }

  public static synchronized FileDescriptor retrieve(String fileId) throws FileAccessException, ApplicationException
  {
    return new FileDescriptor(fileId);
  }

}