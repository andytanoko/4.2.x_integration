/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetCommonsFTPClientManager.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 * Feb 03 2009    Tam Wei Xiang           #125 Added method listFilesByPage(...)    
 * Mar 13 2009    Tam Wei Xiang           #132: Allow to set timeout for "dataTimeout",
 *                                              and "soTimeout" for FTPPull and FTPPush     
 */

package com.gridnode.ftp.adaptor;

import com.gridnode.ftp.facade.IFTPClientManager;
import com.gridnode.ftp.exception.FTPException;
import com.gridnode.ftp.exception.FTPConnectionException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPListParseEngine;

import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetCommonsFTPClientManager implements IFTPClientManager
{

  private org.apache.commons.net.ftp.FTPClient _ftpClient = null;
  private String _host = "";
  private String _user="";
  private String _account="";
  private String _password="";
  private int _port = 21;
  private int _timeout = 0;
  private int _dataTimeout = 0;
  private int _soTimeout = 0;
  private boolean _isPassive = false;


  public NetCommonsFTPClientManager(String host, int port)
    throws FTPException,FTPConnectionException
  {
    this(host, port, 0);
  }
  
  public NetCommonsFTPClientManager(String host, int port, int socketTimeout)
    throws FTPException,FTPConnectionException
  {
    int reply = 0;
    try
    {
      _host = host;
      _port = port;
      _ftpClient = new org.apache.commons.net.ftp.FTPClient();
      _ftpClient.setDefaultTimeout(socketTimeout);

      _ftpClient.connect(host,port);
      reply = _ftpClient.getReplyCode();
      if(!org.apache.commons.net.ftp.FTPReply.isPositiveCompletion(reply))
      {
        if(_ftpClient.isConnected())
          _ftpClient.disconnect();
      }
      else
      {
        reply = _ftpClient.getReplyCode();
      }
    }
    catch(UnknownHostException ex)
    {
      System.out.println("Unknown host exception -->");
      throw new FTPConnectionException(ex.getMessage());
    }
    catch(IOException ex)
    {
      if(_ftpClient.isConnected())
      {
        try{
          _ftpClient.disconnect();
        }
        catch(IOException iex){
        }
      }
      throw new FTPConnectionException(ex.getMessage());
    }
 }

  /**
   * Connect to Ftp server and login.
   * @param server Name of server
   * @param user User name for login
   * @exception FTPException if a ftp error occur (eg. Login fail in this case).
   * @exception IOException if an I/O error occur
   */
  public boolean ftpConnect(String username, String password)
      throws FTPException
  {
    try
    {
      _user = username;
      _password = password;
     if(!_ftpClient.login(username,password))
     {
      throw new FTPException("Could Not Login Check for UserName & Password",
          replyCodeToString(_ftpClient.getReplyCode()));
     }
     return true;
    }
    catch(FTPConnectionClosedException ex)
    {
      System.out.println("Reply Code = "+replyCodeToString(_ftpClient.getReplyCode()));
      throw new FTPException(ex.getMessage(),replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      throw new FTPException(ex.getMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
  }


  /**
   * Connect to FTP server and login.
   * @param server Name of server
   * @param user User name for login
   * @param password Password for login
   * @param acct account information
   * @exception FTPException if a ftp error occur (eg. Login fail in this case).
   * @exception IOException if an I/O error occur
   */
  public void ftpConnect(String server, String username, String password, String acct)
      throws FTPException,IOException
  {
    try
    {
      _host = server;
      _port = _ftpClient.getDefaultPort();
      _user = username;
      _password = password;
      _ftpClient.connect(server);
      _ftpClient.login(username,password,acct);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      throw new FTPException(ex.getMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }

  }


  public void ftpConnect(String server, String username, String password)
      throws FTPException,IOException
  {
    try
    {
      _host = server;
      _port = _ftpClient.getDefaultPort();
      _user = username;
      _password = password;
      _ftpClient.connect(server);
      _ftpClient.login(username,password);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      throw new FTPException(ex.getMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Close FTP connection.
   * @exception IOException if an I/O error occur
   * @exception FTPException if a ftp error occur
   */
  public void close()
      throws FTPException
  {
    try
    {
      if(_ftpClient.isConnected())
      {
        _ftpClient.disconnect();
        //_ftpClient.logout();
      }
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
  }

  /**
   * Delete a file at the FTP server.
   * @param filename Name of the file to be deleted.
   * @exception FTPException if a ftp error occur. (eg. no such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public void fileDelete(String filename)
      throws FTPException
  {
    try
    {
      _ftpClient.deleteFile(filename);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }
  }


  /**
   * Rename a file at the FTP server.
   * @param oldfilename The name of the file to be renamed
   * @param newfilename The new name of the file
   * @exception FTPException if a ftp error occur. (eg. A file named the new file name already in this case.)
   * @exception IOException if an I/O error occur.
   */
  public void fileRename(String oldfilename, String newfilename)
      throws FTPException
  {
    try
    {
      _ftpClient.rename(oldfilename,newfilename);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }
  }

  /**
   * Get an ASCII file from the server and return as String.
   * @param filename Name of ASCII file to be getted.
   * @return The Ascii content of the file. It uses parameter 'separator' as the line separator.
   * @exception FTPException if a ftp error occur. (eg. no such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public String getAsciiFile(String filename)
      throws FTPException
  {
    ByteArrayOutputStream out = null;
    try
    {
      out = new ByteArrayOutputStream();
      _ftpClient.retrieveFile(filename,out);
      out.flush();
      return out.toString();
    }
    catch(FTPConnectionClosedException ex)
    {
      ex.printStackTrace();
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
      if(out != null)
      {
        try
        {
          out.close();
        }
        catch(IOException ex){
        }
      }
    }

  }


  /**
   * Get an ascii file from the server and write to local file system.
   * @param ftpfile Name of ascii file in the server side.
   * @param localfile Name of ascii file in the local file system.
   * @param separator The line separator you want in the local ascii file (eg. "\r\n", "\n", "\r").
   * @exception FTPException if a ftp error occur. (eg. no such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public void getAsciiFile(String ftpfile, String localfile)
      throws FTPException
  {
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(localfile);
      _ftpClient.retrieveFile(ftpfile,fos);
      fos.flush();
    }
    catch(FTPConnectionClosedException ex)
    {
      ex.printStackTrace();
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
      if( fos != null )
      {
        try
        {
           fos.close();
        }
        catch(IOException ex){
        }
      }
    }

  }

  /**
   * Append an ascii file to the server.
   * <br>Remark:<br>
   * this method convert the line separator of the String content to <br>
   * NVT-ASCII format line separator "\r\n". Then the ftp daemon will <br>
   * convert the NVT-ASCII format line separator into its system line separator.
   * @param filename The name of file
   * @param content The String content of the file
   * @param separator Line separator of the content
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void appendAsciiFile(String filename, String content)
      throws FTPException
  {
    try
    {
      _ftpClient.appendFile(filename,new ByteArrayInputStream(content.getBytes()));
    }
    catch(FTPConnectionClosedException ex)
    {
      ex.printStackTrace();
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Save an ascii file to the server.
   * <br>Remark:<br>
   * this method convert the line separator of the String content to <br>
   * NVT-ASCII format line separator "\r\n". Then the ftp daemon will <br>
   * convert the NVT-ASCII format line separator into its system line separator.
   * @param filename The name of file
   * @param content The String content of the file
   * @param separator Line separator of the content
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void putAsciiFile(String localfile,String remotefile)
      throws FTPException
  {
    FileInputStream fis = null;
    try
    {
       fis = new FileInputStream(localfile);
       _ftpClient.setSoTimeout(getSoTimeout());
       _ftpClient.setDataTimeout(getDataConnTimeout());
       _ftpClient.storeFile(remotefile,fis);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
      if(fis != null)
      {
        try {
          fis.close();
        }catch(IOException ex) {

        }
      }//End of finally

    }
  }

  /**
   * Save the File to Server with the given InputStream.
   * @param localfile LocalFile as InputStream
   * @param remotefile File name to store on the server.
   * @throws FTPException - thrown upon FTP Error.
   */

  public void putAsciiFile(InputStream localfile, String remotefile)
      throws FTPException
  {
    try
    {
      _ftpClient.setSoTimeout(getSoTimeout());
      _ftpClient.setDataTimeout(getDataConnTimeout());
      _ftpClient.storeFile(remotefile,localfile);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Get a binary file and return a byte array.
   * @param filename The name of the binary file to be got.
   * @return An array of byte of the content of the binary file.
   * @exception FTPException if a ftp error occur. (eg. No such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public byte[] getBinaryFile(String filename)
      throws FTPException
  {
    ByteArrayOutputStream bos = null;
    try
    {
      bos = new ByteArrayOutputStream();
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.retrieveFile(filename,bos);
      bos.flush();
      return bos.toByteArray();
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( bos != null)
          bos.close();
     }catch(IOException ex) {

     }
    }

  }


  /**
   * Get a binary file at a restarting point.
   * Return null if restarting point is less than zero.
   * @param filename Name of binary file to be getted.
   * @param restart Restarting point, ignored if less than or equal to zero.
   * @return An array of byte of the content of the binary file.
   * @exception FTPException if a ftp error occur. (eg. No such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public byte[] getBinaryFile(String filename, long restart)
      throws IOException, FTPException
  {
    ByteArrayOutputStream bos = null;
    try
    {
      bos = new ByteArrayOutputStream();
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.setRestartOffset(restart);
      _ftpClient.retrieveFile(filename,bos);
      bos.flush();
      return bos.toByteArray();
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( bos != null)
          bos.close();
     }catch(IOException ex) {

     }
    }

  }


  /**
   * Read file from ftp server and write to a file in local hard disk.
   * This method is much faster than those method which return a byte array<br>
   * if the network is fast enough.<br>
   * <br>Remark:<br>
   * Cannot be used in unsigned applet.
   * @param ftpfile Name of file to be get from the ftp server, can be in full path.
   * @param localfile Name of local file to be write, can be in full path.
   * @exception FTPException if a ftp error occur. (eg. No such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public void getBinaryFile(String ftpfile, String localfile)
      throws FTPException
  {

    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(localfile);
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.retrieveFile(ftpfile,fos);
      fos.flush();
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( fos != null)
          fos.close();
     }catch(IOException ex) {

     }
    }

  }

  /**
   * Read file from ftp server and write to a file in local hard disk.
   * This method is much faster than those method which return a byte array<br>
   * if the network is fast enough.<br>
   * <br>Remark:<br>
   * Cannot be used in unsigned applet.
   * @param ftpfile Name of file to be get from the ftp server, can be in full path.
   * @param localfile Name of local file to be write, can be in full path.
   * @param restart Restarting point
   * @exception FTPException if a ftp error occur. (eg. No such file in this case)
   * @exception IOException if an I/O error occur.
   */
  public void getBinaryFile(String ftpfile, String localfile, long restart)
      throws FTPException
  {
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(localfile);
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.setRestartOffset(restart);
      _ftpClient.retrieveFile(ftpfile,fos);
      fos.flush();
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( fos != null)
          fos.close();
     }catch(IOException ex) {

     }
    }
  }

  /**
   * Put a binary file to the server from an array of byte.
   * @param filename The name of file.
   * @param content The byte array to be written to the server.
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void putBinaryFile(String filename, byte[] content)
      throws FTPException
  {

    ByteArrayInputStream binstream = null;
    try
    {
       binstream  = new ByteArrayInputStream(content);
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.setSoTimeout(getSoTimeout());
      _ftpClient.setDataTimeout(getDataConnTimeout());
      _ftpClient.storeFile(filename,binstream);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( binstream != null )
          binstream.close();
     }catch(IOException ex) {

     }
    }

  }

  /**
   * Put a binary file to the server from an array of byte with a restarting point
   * @param filename The name of file.
   * @param content The byte array to be write to the server.
   * @param restart The restarting point, ingored if less than or equal to zero.
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void putBinaryFile(String filename, byte[] content, long restart)
      throws IOException, FTPException,Exception
  {

    ByteArrayInputStream binstream = null;
    try
    {
       binstream  = new ByteArrayInputStream(content);
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.setSoTimeout(getSoTimeout());
      _ftpClient.setDataTimeout(getDataConnTimeout());
      _ftpClient.setRestartOffset(restart);
      _ftpClient.storeFile(filename,binstream);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( binstream != null )
          binstream.close();
     }catch(IOException ex) {

     }
    }

  }


  /**
   * Read a file from local hard disk and write to the server.
   * <br>Remark:<br>
   * <br>Cannot be used in unsigned applet.
   * @param local_file Name of local file, can be in full path.
   * @param remote_file Name of file in the ftp server, can be in full path.
   * @exception FTPException if a ftp error occur. (eg. permission denied)
   * @exception IOException if an I/O error occur.
   */
  public void putBinaryFile(String local_file, String remote_file)
      throws FTPException
  {

    FileInputStream finstream = null;
    try
    {
       finstream  = new FileInputStream(local_file);
       _ftpClient.setSoTimeout(getSoTimeout());
       _ftpClient.setDataTimeout(getDataConnTimeout());
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.storeFile(remote_file,finstream);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( finstream != null )
          finstream.close();
     }catch(IOException ex) {

     }
    }

  }


  /**
   * Read a file from local hard disk and write to the server with restarting point.
   * Remark:<br>
   * Cannot be used in unsigned applet.
   * @param local_file Name of local file, can be in full path.
   * @param remote_file Name of file in the ftp server, can be in full path.
   * @param restart The restarting point, ignored if less than or greater than zero.
   * @exception FTPException if a ftp error occur. (eg. permission denied)
   * @exception IOException if an I/O error occur.
   */
  public void putBinaryFile(String local_file, String remote_file, long restart)
      throws IOException, FTPException,Exception
  {
    FileInputStream finstream = null;
    try
    {
       finstream  = new FileInputStream(local_file);
       _ftpClient.setSoTimeout(getSoTimeout());
       _ftpClient.setDataTimeout(getDataConnTimeout());
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.setRestartOffset(restart);
      _ftpClient.storeFile(remote_file,finstream);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( finstream != null )
          finstream.close();
     }catch(IOException ex) {

     }
    }

  }


  /**
   * Read a file from local hard disk and append to the server with restarting point.
   * Remark:<br>
   * Cannot be used in unsigned applet.
   * @param local_file Name of local file, can be in full path.
   * @param remote_file Name of file in the ftp server, can be in full path.
   * @exception FTPException if a ftp error occur. (eg. permission denied)
   * @exception IOException if an I/O error occur.
   */
  public void appendBinaryFile(String local_file, String remote_file)
      throws FTPException
  {

    FileInputStream finstream = null;
    try
    {
       finstream  = new FileInputStream(local_file);
      _ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
      _ftpClient.appendFile(remote_file,finstream);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
    finally
    {
     try
     {
      _ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
      if( finstream != null )
          finstream.close();
     }catch(IOException ex) {

     }
    }

  }


  /**
   * Get current directory name.
   * @return The name of the current directory.
   * @exception FTPException if a ftp error occur.
   * @exception IOException if an I/O error occur.
   */
  public String getDirectory()
      throws  FTPException
  {
    try
    {
      return _ftpClient.printWorkingDirectory();
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Change directory.
   * @param directory Name of directory
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void setDirectory(String directory)
      throws FTPException
  {

    try
    {
      _ftpClient.changeWorkingDirectory(directory);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Change to parent directory.
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void toParentDirectory()
      throws FTPException
  {
    try
    {
      _ftpClient.changeToParentDirectory();
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Get the content of current directory.
   * @return A list of directories, files and links in the current directory.
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public String getDirectoryContentAsString()
      throws FTPException
  {
    try
    {
       StringBuffer sb = new StringBuffer();
       String[] ftpList =  _ftpClient.listNames();
       for(int i=0;i<ftpList.length;i++)
       {
         sb.append(ftpList[i]);
         sb.append("\n");
       }
       return sb.toString();
     }
     catch(FTPConnectionClosedException ex)
     {
       throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
     }
     catch(IOException fex)
     {
       throw new FTPException(fex.getLocalizedMessage(),
         replyCodeToString(_ftpClient.getReplyCode()));
     }

  }


  /**
   * Make a directory in the server.
   * @param directory The name of directory to be made.
   * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
   * @exception IOException if an I/O error occur.
   */
  public void makeDirectory(String directory)
      throws FTPException
  {
    try
    {
      _ftpClient.makeDirectory(directory);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
         replyCodeToString(_ftpClient.getReplyCode()));
    }

  }


  /**
   * Execute a command using ftp.
   * e.g. chmod 700 file
   * @param exec The command to execute.
   * @exception FTPException if a ftp error occur. (eg. command not understood)
   * @exception IOException if an I/O error occur.
   */
  public void execute(String exec)
      throws FTPException
  {
    try
    {
      _ftpClient.sendCommand(exec);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
           replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
         replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  /**
   * Get the type of operating system of the server.
   * Return null if it is not currently connected to any ftp server.
   * @return Name of the operating system.
   */
  public String getSystemType()
      throws FTPException
  {
   try
   {
     return _ftpClient.getSystemName();
   }
   catch(FTPConnectionClosedException ex)
   {
     throw new FTPException(ex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
   }
   catch(IOException fex)
   {
     throw new FTPException(fex.getLocalizedMessage(),
       replyCodeToString(_ftpClient.getReplyCode()));
   }

  }

  public int getReplyCode()
  {
    return _ftpClient.getReplyCode();
  }

  /**
   * Set port number if the port number of ftp if not 21
   */
  public void setPort(int port)
  {
    _port = port;
  }


 /**
  * Set timeout when creating a socket.
  * This include trying to connect to the ftp server at the beginnig and
  * trying to connect to the server in order to establish a data connection.
  * @param timeout Timeout in milliseconds, 0 means infinity.
  */
  public void setSocketTimeout(int timeout)
  {
    _timeout = timeout;
    _ftpClient.setDefaultTimeout(timeout);
  }


 /**
  * Get timeout when creating socket.
  * @return Timeout for creating socket in milliseconds, 0 means infinity.
  */
  public int getSocketTimeout()
  {
    return _timeout;
    //return _timeout;
  }


  /**
   * Return the account information. Return "" if it is not connected to any server.
   */
  public String getAcctInfo()
  {
    return _account;
  }

  /**
   * Return the server name. Return "" if it is not connected to any server.
   */
  public String getServerName() throws FTPException
  {
   try
   {
      return _ftpClient.getSystemName();
   }
   catch(FTPConnectionClosedException ex)
   {
     throw new FTPException(ex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
   }
   catch(IOException fex)
   {
     throw new FTPException(fex.getLocalizedMessage(),
       replyCodeToString(_ftpClient.getReplyCode()));
   }

  }

  /**
   * Return the user name. Return "" if it is not connected to any server.
   */
  public String getUserName()
  {
    return _user;
  }

  /**
   * Get reply of the last command.
   * @return Reply of the last comomand<br>for example: 250 CWD command successful
   */
  public String getReply()
  {
      return String.valueOf(_ftpClient.getReplyCode());
  }

  /**
   * Return true if it is using passive transfer mode.
   */
  public boolean isPassiveModeTransfer()
  {
    return _isPassive;
  }

  /**
   * Set passive transfer mode. Default is true.
   * @param passive Using passive transfer if true.
   */
  public void setPassiveModeTransfer(boolean passive)
  {
    if(passive)
    {
      _isPassive = passive;
      _ftpClient.enterLocalPassiveMode();
    }
  }


  public int getPort()
  {
    return _port;
  }

  private String replyCodeToString(int replyCode)
  {
    return Integer.toString(replyCode);
  }


  public void removeDirectory(String directory)
      throws FTPException
  {
    try
    {
      _ftpClient.removeDirectory(directory);
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
         replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }

  }

  public boolean isConnected()
  {
    return _ftpClient.isConnected();
  }

  public boolean isPositiveCompletion() {
	  return true;
  }
  
  public FTPFile[] listFiles(String directory) {
	  return null;
  }

  /**
   * TWX 20090203 Retrieve a list of file from the given directory and limit the num file returned.
   */
  public FTPFile[] listFilesByPage(String directory, int pageSize) throws FTPException
  {
    try
    {
      _ftpClient.setSoTimeout(getSoTimeout());
      _ftpClient.setDataTimeout(getDataConnTimeout());
      
      FTPListParseEngine engine = _ftpClient.initiateListParsing(directory);
      if(engine.hasNext())
      {
        FTPFile[] fileList = engine.getNext(pageSize);
        return fileList;
      }
      else
      {
        return null;
      }
    }
    catch(FTPConnectionClosedException ex)
    {
      throw new FTPException(ex.getLocalizedMessage(),
          replyCodeToString(_ftpClient.getReplyCode()));
    }
    catch(IOException fex)
    {
      throw new FTPException(fex.getLocalizedMessage(),
        replyCodeToString(_ftpClient.getReplyCode()));
    }
  }

  public static void main(String args[])
  {
    try
    {
    	NetCommonsFTPClientManager ftp =
            new NetCommonsFTPClientManager("192.168.1.111",21, 0);
        ftp.ftpConnect("admin","admin");
       //String file = ftp.getAsciiFile("testftp.txt");
       //ftp.putAsciiFile("c:/testftp.txt","testftp.txt");
       //byte[] file = ftp.getBinaryFile("testftp.txt",1999);
     }
     catch(FTPException ex)
     {
      System.out.println("In FTPException .....>"+ex.getMessage());
      System.out.println(ex.getReplyCode());
      ex.printStackTrace(System.err);
     }
     catch(Exception ex)
     {
      System.out.println("In Normal Exception .....>"+ex.getMessage());
      ex.printStackTrace(System.err);
     }
  }
  
  
  public int getDataConnTimeout()
  {
    return _dataTimeout;
  }

  public int getSoTimeout()
  {
    return _soTimeout;
  }

  public void setDataConnTimeout(int timeout)
  {
    _dataTimeout = timeout;
  }

  public void setSoTimeout(int timeout)
  {
    _soTimeout = timeout;
  }

}