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
 * Feb 03 2009    Tam Wei Xiang           #125 Added method listFilesByPage(...)
 */

package com.gridnode.ftp.facade;

import com.gridnode.ftp.exception.FTPException;

import java.io.InputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPFile;


public interface IFTPClientManager
{

    /**
     * Connect to Ftp server and login.
     * @param server Name of server
     * @param user User name for login
     * @exception FTPException if a ftp error occur (eg. Login fail in this case).
     * @exception IOException if an I/O error occur
     */
    public boolean ftpConnect(String username, String password)
        throws FTPException,IOException;

    /**
     * Connect to Ftp server and login.
     * @param server Name of server
     * @param user User name for login
     * @param password Password for login
     * @exception FTPException if a ftp error occur (eg. Login fail in this case).
     * @exception IOException if an I/O error occur
     */
   public void ftpConnect(String server, String user, String password)
        throws  FTPException,IOException;

    /**
     * Connect to FTP server and login.
     * @param server Name of server
     * @param user User name for login
     * @param password Password for login
     * @param acct account information
     * @exception FTPException if a ftp error occur (eg. Login fail in this case).
     * @exception IOException if an I/O error occur
     */
/*    public void ftpConnect(String server, String user, String password, String acct)
        throws FTPException,IOException;
*/

    /**
     * Close FTP connection.
     * @exception IOException if an I/O error occur
     * @exception FTPException if a ftp error occur
     */
    public void close()
        throws IOException, FTPException;

    /**
     * Delete a file at the FTP server.
     * @param filename Name of the file to be deleted.
     * @exception FTPException if a ftp error occur. (eg. no such file in this case)
     * @exception IOException if an I/O error occur.
     */
    public void fileDelete(String filename)
        throws IOException, FTPException;

    /**
     * Rename a file at the FTP server.
     * @param oldfilename The name of the file to be renamed
     * @param newfilename The new name of the file
     * @exception FTPException if a ftp error occur. (eg. A file named the new file name already in this case.)
     * @exception IOException if an I/O error occur.
     */
    public void fileRename(String oldfilename, String newfilename)
        throws IOException, FTPException;

    /**
     * Get an ASCII file from the server and return as String.
     * @param filename Name of ASCII file to be getted.
     * @param separator The line separator you want in the return String (eg. "\r\n", "\n", "\r")
     * @return The Ascii content of the file. It uses parameter 'separator' as the line separator.
     * @exception FTPException if a ftp error occur. (eg. no such file in this case)
     * @exception IOException if an I/O error occur.
     */
    public void getAsciiFile(String ftpfile, String localfile)
        throws IOException, FTPException;

    public String getAsciiFile(String ftpfile)
        throws IOException, FTPException;


    /**
     * Get an ASCII file from the server and return as String.
     * @param filename Name of ASCII file to be getted.
     * @param separator The line separator you want in the return String (eg. "\r\n", "\n", "\r").
     * @param observer The observer of the downloading progress
     * @return The Ascii content of the file. It uses parameter 'separator' as the line separator.
     * @exception FTPException if a ftp error occur. (eg. no such file in this case)
     * @exception IOException if an I/O error occur.
     * @see FtpObserver
     */
//    public String getAsciiFile(String filename, String separator, FtpObserver observer)
//        throws IOException, FTPException;

    /**
     * Get an ascii file from the server and write to local file system.
     * @param ftpfile Name of ascii file in the server side.
     * @param localfile Name of ascii file in the local file system.
     * @param separator The line separator you want in the local ascii file (eg. "\r\n", "\n", "\r").
     * @exception FTPException if a ftp error occur. (eg. no such file in this case)
     * @exception IOException if an I/O error occur.
     */
   // public void getAsciiFile(String ftpfile, String localfile, String separator)
    //   throws IOException, FTPException;

    /**
     * Get an ascii file from the server and write to local file system.
     * @param ftpfile Name of ascii file in the server side.
     * @param localfile Name of ascii file in the local file system.
     * @param separator The line separator you want in the local ascii file (eg. "\r\n", "\n", "\r").
     * @param observer The observer of the downloading progress.
     * @exception FTPException if a ftp error occur. (eg. no such file in this case)
     * @exception IOException if an I/O error occur.
     * @see FtpObserver
     */
//    public void getAsciiFile(String ftpfile, String localfile, String separator, FtpObserver observer)
//        throws IOException, FTPException;

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
  //  public void appendAsciiFile(String filename, String content, String separator)
  //     throws IOException, FTPException;

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
    //public void putAsciiFile(String filename, String content, String separator)
       // throws IOException, FTPException;

    public void putAsciiFile(String localfile,String remotefile)
        throws IOException,FTPException;

    public void putAsciiFile(InputStream localfile, String remotefile)
        throws IOException,FTPException;


    /**
     * Get a binary file and return a byte array.
     * @param filename The name of the binary file to be got.
     * @return An array of byte of the content of the binary file.
     * @exception FTPException if a ftp error occur. (eg. No such file in this case)
     * @exception IOException if an I/O error occur.
     */
    public byte[] getBinaryFile(String filename)
        throws IOException, FTPException;
    /**
     * Get a binary file and return a byte array.
     * @param filename The name of the binary file to be got.
     * @param observer The observer of the downloading progress.
     * @return An array of byte of the content of the binary file.
     * @exception FTPException if a ftp error occur. (eg. No such file in this case)
     * @exception IOException if an I/O error occur.
     */
//    public byte[] getBinaryFile(String filename, FtpObserver observer)
//        throws IOException, FTPException;

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
        throws IOException, FTPException,Exception;

    /**
     * Get a binary file at a restarting point.
     * Return null if restarting point is less than zero.
     * @param filename Name of binary file to be getted.
     * @param restart Restarting point, ignored if less than or equal to zero.
     * @param observer The FtpObserver which monitor this downloading progress
     * @return An array of byte of the content of the binary file.
     * @exception FTPException if a ftp error occur. (eg. No such file in this case)
     * @exception IOException if an I/O error occur.
     * @see FtpObserver
     */
//    public byte[] getBinaryFile(String filename, long restart, FtpObserver observer)
//        throws IOException, FTPException;

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
        throws IOException, FTPException;

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
        throws IOException, FTPException,Exception;

    /**
     * Read file from ftp server and write to a file in local hard disk.
     * This method is much faster than those method which return a byte array<br>
     * if the network is fast enough.<br>
     * <br>Remark:<br>
     * Cannot be used in unsigned applet.
     * @param ftpfile Name of file to be get from the ftp server, can be in full path.
     * @param localfile Name of local file to be write, can be in full path.
     * @param observer The FtpObserver which monitor this downloading progress
     * @exception FTPException if a ftp error occur. (eg. No such file in this case)
     * @exception IOException if an I/O error occur.
     * @see FtpObserver
     */
//    public void getBinaryFile(String ftpfile, String localfile, FtpObserver observer)
//        throws IOException, FTPException;

    /**
     * Read from a ftp file and restart at a specific point.
     * This method is much faster than those method which return a byte array<br>
     * if the network is fast enough.<br>
     * Remark:<br>
     * Cannot be used in unsigned applet.
     * @param ftpfile Name of file to be get from the ftp server, can be in full path.
     * @param localfile File name of local file
     * @param restart Restarting point, ignored if equal or less than zero.
     * @param observer The FtpObserver which monitor this downloading progress
     * @exception FTPException if a ftp error occur. (eg. No such file in this case)
     * @exception IOException if an I/O error occur.
     * @see FtpObserver
     */
//    public void getBinaryFile(String ftpfile, String localfile, long restart, FtpObserver observer)
//        throws IOException, FTPException;

    /**
     * Put a binary file to the server from an array of byte.
     * @param filename The name of file.
     * @param content The byte array to be written to the server.
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public void putBinaryFile(String filename, byte[] content)
        throws IOException, FTPException;

    /**
     * Put a binary file to the server from an array of byte with a restarting point
     * @param filename The name of file.
     * @param content The byte array to be write to the server.
     * @param restart The restarting point, ingored if less than or equal to zero.
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public void putBinaryFile(String filename, byte[] content, long restart)
        throws IOException, FTPException,Exception;

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
        throws IOException, FTPException;

    /**
     * Read a file from local hard disk and write to the server.
     * <br>Remark:<br>
     * <br>Cannot be used in unsigned applet.
     * @param local_file Name of local file, can be in full path.
     * @param remote_file Name of file in the ftp server, can be in full path.
     * @param observer The FtpObserver which monitor this uploading progress.
     * @exception FTPException if a ftp error occur. (eg. permission denied)
     * @exception IOException if an I/O error occur.
     */
//    public void putBinaryFile(String local_file, String remote_file, FtpObserver observer)
//        throws IOException, FTPException;

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
        throws IOException, FTPException,Exception;

    /**
        * Read a file from local hard disk and write to the server with restarting point.
     * Remark:<br>
     * Cannot be used in unsigned applet.
     * @param local_file Name of local file, can be in full path.
     * @param remote_file Name of file in the ftp server, can be in full path.
     * @param observer The FtpObserver which monitor this uploading progress
     * @exception FTPException if a ftp error occur. (eg. permission denied)
     * @exception IOException if an I/O error occur.
    */
//    public void putBinaryFile(String local_file, String remote_file, long restart, FtpObserver observer)
//        throws IOException, FTPException;


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
        throws IOException, FTPException;

    /**
     * Read a file from local hard disk and append to the server with restarting point.
     * Remark:<br>
     * Cannot be used in unsigned applet.
     * @param local_file Name of local file, can be in full path.
     * @param remote_file Name of file in the ftp server, can be in full path.
     * @param observer The FtpObserver which monitor this uploading progress
     * @exception FTPException if a ftp error occur. (eg. permission denied)
     * @exception IOException if an I/O error occur.
     */
//    public void appendBinaryFile(String local_file, String remote_file, FtpObserver observer)
//        throws IOException, FTPException;

    /**
     * Get current directory name.
     * @return The name of the current directory.
     * @exception FTPException if a ftp error occur.
     * @exception IOException if an I/O error occur.
     */
    public String getDirectory()
        throws IOException, FTPException;

    /**
     * Change directory.
     * @param directory Name of directory
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public void setDirectory(String directory)
        throws IOException, FTPException;

    /**
     * Change to parent directory.
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public void toParentDirectory()
        throws IOException, FTPException,Exception;

    /**
     * Get the content of current directory
     * @return A FtpListResult object, return null if it is not connected.
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     * @see FtpListResult
     */
 //   public FtpListResult getDirectoryContent()
//        throws IOException, FTPException;

    /**
     * Get the content of current directory.
     * @return A list of directories, files and links in the current directory.
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public String getDirectoryContentAsString()
        throws IOException, FTPException;

    /**
     * Make a directory in the server.
     * @param directory The name of directory to be made.
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public void makeDirectory(String directory)
        throws IOException, FTPException;

    /**
     * Remove a directory in the server
     * @param directory The name of directory to be removed
     * @exception FTPException if a ftp error occur. (eg. permission denied in this case)
     * @exception IOException if an I/O error occur.
     */
    public void removeDirectory(String directory)
        throws IOException, FTPException;

    /**
     * Execute a command using ftp.
     * e.g. chmod 700 file
     * @param exec The command to execute.
     * @exception FTPException if a ftp error occur. (eg. command not understood)
     * @exception IOException if an I/O error occur.
     */
    public void execute(String exec)
        throws IOException, FTPException,Exception;

    /**
     * Get the type of operating system of the server.
     * Return null if it is not currently connected to any ftp server.
     * @return Name of the operating system.
     */
    public String getSystemType()
        throws IOException, FTPException;

    /**
     * Return the port number
     */
    public int getPort();

    /**
     * Set port number if the port number of ftp if not 21
     */
    public void setPort(int port);

    /**
     * Set timeout when creating a socket.
     * This include trying to connect to the ftp server at the beginnig and
     * trying to connect to the server in order to establish a data connection.
     * @param timeout Timeout in milliseconds, 0 means infinity.
     */
    public void setSocketTimeout(int timeout) throws SocketException,IOException;

    /**
     * Get timeout when creating socket.
     * @return Timeout for creating socket in milliseconds, 0 means infinity.
     */
    public int getSocketTimeout();

    /**
     * Sets the timeout in milliseconds to use when reading from the data connection
     * @param timeout Timeout in milliseconds, 0 means infinity
     */
    public void setDataConnTimeout(int timeout);
    
    /**
     * Get the timeout when reading from the data connection
     * @return timeout in milli
     */
    public int getDataConnTimeout();
    
    /**
     * Set the timeout in milliseconds of a currently opened connection
     * @param timeout in milli sec
     */
    public void setSoTimeout(int timeout);
    
    /**
     * Get the timeout on a currently opened connection.
     * @return timeout in milli
     */
    public int getSoTimeout();
    
    /**
     * Return the account information. Return "" if it is not connected to any server.
     */
    public String getAcctInfo();

    /**
     * Return the server name. Return "" if it is not connected to any server.
     */
    public String getServerName() throws FTPException;

    /**
     * Return the user name. Return "" if it is not connected to any server.
     */
    public String getUserName();

    /**
     * Get reply of the last command.
     * @return Reply of the last comomand<br>for example: 250 CWD command successful
     */
    public String getReply() throws FTPException;

    /**
     * Get reply message of the last command.
     * @return Reply message of the last command<br>for example:<br>
     * 250-Please read the file README<br>
     * 250-it was last modified on Wed Feb 10 21:51:00 1999 - 268 days ago
     */
   // public String getReplyMessage();

    /**
     * Return true if it is using passive transfer mode.
     */
    public boolean isPassiveModeTransfer();

    /**
     * Set passive transfer mode. Default is true.
     * @param passive Using passive transfer if true.
     */
    public void setPassiveModeTransfer(boolean passive);

    public boolean isPositiveCompletion();
    
    public FTPFile[] listFiles(String directory);
    
    public FTPFile[] listFilesByPage(String directory, int pageSize) throws FTPException;
}