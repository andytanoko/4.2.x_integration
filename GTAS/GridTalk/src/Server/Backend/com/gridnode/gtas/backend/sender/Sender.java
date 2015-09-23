/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Sender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 15 2002    Koh Han Sing        Check for empty SMTP settings
 * Mar 16 2002    Koh Han Sing        GNDB00006473 : Multiple triggering of
 *                                    backend send problem
 * Apr 02 2002    Koh Han Sing        Add in attachment features
 * Feb 20 2006    Neo Sok Lay         Add placeholder for process instance id in apiparams array
 */

package com.gridnode.gtas.backend.sender;

import com.gridnode.gtas.backend.util.BackendIni;
import com.gridnode.gtas.backend.util.BackendIniReader;
import com.gridnode.gtas.backend.util.CommandExecutor;
import com.gridnode.gtas.backend.util.IBackendIniReader;
import com.gridnode.gtas.backend.util.InitManager;
import com.gridnode.gtas.backend.util.Log;
import com.gridnode.gtas.backend.exception.*;

import com.gridnode.gtas.server.backend.openapi.core.*;

import com.gridnode.gridmail.*;

import java.util.*;
import java.io.*;

public class Sender
{
  private static final String CATEGORY = "Sender";
  private static final int UNKNOWN_ERROR  = 0;
  private static final int DIRECTORY_NOT_FOUND = 1;
  private static final int UNABLE_TO_SAVE_FILE = 2;
  private static final int CONNECTION_ERROR    = 3;

  // The string in the command to be replaced with the actual filename of the
  // file to be send
  private static final String FILENAME = "%FILENAME";

  // Used to do directory locking
  private static Locker locker = null;

  private static boolean isThereAttachment = false;
  private static boolean isAttachmentSend = false;

  // The index indicating the last attachment File in the File array
  private static int attachmentIndex = -1;

  public static void main(String[] args)
  {
    try
    {
      log("$$$$$$$$$$ Backend Sender Start " + new Date(System.currentTimeMillis())
        + " $$$$$$$$$$");
      if(args.length == 0)
      {
        log("$$$$$$$$$$ Please specify ini file $$$$$$$$$$");
        System.exit(1);
      }
      String iniFile = args[0];
      if(args.length > 1)
      {
        // directory file is used
        sendFromMultiple(iniFile, args[1]);
      }
      else
      {
        // only backend.ini is used
        sendFromSingle(iniFile);
      }
      log("$$$$$$$$$$ Backend Sender End " + new Date(System.currentTimeMillis())
        + " $$$$$$$$$$\n");
      System.exit(0);
    }
    catch(Exception e)
    {
      log("**********Exit with exception**********\n");
      System.exit(2);
    }
  }

  /**
   * This method is used when the directory file is used, it will read the
   * directory to get all the different locations and loops through them
   * one by one. The sender will attempt to lock a directory before sending the
   * files in that directory, if the directory id already locked, it will check
   * if the directory locking has expired. If the lock has exipired, it will
   * send the files else it will proceed to the next directory.
   */
  private static void sendFromMultiple(String iniFile, String dirFile)
  {
    log("[sendFromMultiple] Start");
    IBackendIniReader iniReader = new BackendIniReader();
    BackendIni backendIni = iniReader.readIni(iniFile);
    if (backendIni.getSmtp() != null)
    {
      GridMail.setSMTP(backendIni.getSmtp());
    }
    DirReader dirReader = new DirReader(dirFile);
    Hashtable dirFileTable = dirReader.readFile();
    Enumeration keys = dirFileTable.keys();
    while(keys.hasMoreElements())
    {
      isAttachmentSend = false;
      String directory = keys.nextElement().toString();
      log("[sendFromMultiple] Directory = " + directory);
      ArrayList values = (ArrayList)dirFileTable.get(directory);
      File dir = new File(directory);
      if (!dir.exists())
      {
        log("[sendFromMultiple] Directory Not Found: " + directory);
        ArrayList params = new ArrayList();
        params.add(directory);
        sendFailureMail(backendIni, DIRECTORY_NOT_FOUND, params);
        continue;
      }

      // Koh Han Sing 16/03/2002
      File[] filesInDir = dir.listFiles();
      log("[sendFromMultiple] filesInDir.length = " + filesInDir.length);
      long timestamp = System.currentTimeMillis();
      long timeout = backendIni.getTimeout() * 60000;
      if (lock(dir, backendIni.getLockFilename(), timestamp))
      {
        log("[sendFromMultiple] directory " + dir.getAbsolutePath() + " successfully locked");
        sendFilesLoop(filesInDir, backendIni, values, directory, dir);
      }
      else if (isLockExpired(dir, backendIni.getLockFilename(), timestamp, timeout))
      {
        log("[sendFromMultiple] directory " + dir.getAbsolutePath() + " locked has timeout");
        sendFilesLoop(filesInDir, backendIni, values, directory, dir);
      }
      else
      {
        log("[sendFromMultiple] directory " + dir.getAbsolutePath() + " locked by other sender");
      }
    }
    log("[sendFromMultiple] End");
  }

  /**
   * This method is used when there is no directory file. Will only scans
   * the host directory specified in the backend.ini file. The sender will
   * attempt to lock a directory before sending the files in that directory,
   * if the directory id already locked, it will check if the directory locking
   * has expired. If the lock has exipired, it will send the files else it will
   * exit.
   */
  private static void sendFromSingle(String iniFile)
  {
    log("[sendFromSingle] Start");
    IBackendIniReader iniReader = new BackendIniReader();
    BackendIni backendIni = iniReader.readIni(iniFile);
    if (backendIni.getSmtp() != null)
    {
      GridMail.setSMTP(backendIni.getSmtp());
    }
    String sourceDir = backendIni.getHostDir();
    File dir = new File(sourceDir);
    if (dir.exists())
    {
      // Koh Han Sing 16/03/2002
      File[] filesInDir = dir.listFiles();
      log("[sendFromSingle] filesInDir.length = " + filesInDir.length);
      long timestamp = System.currentTimeMillis();
      long timeout = backendIni.getTimeout() * 60000;
      if (lock(dir, backendIni.getLockFilename(), timestamp))
      {
        log("[sendFromSingle] directory " + dir.getAbsolutePath() + " successfully locked");
        sendFilesLoop(filesInDir, backendIni, null, backendIni.getHostDir(), dir);
      }
      else if (isLockExpired(dir, backendIni.getLockFilename(), timestamp, timeout))
      {
        log("[sendFromSingle] directory " + dir.getAbsolutePath() + " locked has timeout");
        sendFilesLoop(filesInDir, backendIni, null, backendIni.getHostDir(), dir);
      }
      else
      {
        log("[sendFromSingle] directory " + dir.getAbsolutePath() + " locked by other sender");
      }
    }
    else
    {
      log("[sendFromSingle] Directory Not Found: " + sourceDir);
      ArrayList params = new ArrayList();
      params.add(sourceDir);
      sendFailureMail(backendIni, DIRECTORY_NOT_FOUND, params);
    }
    log("[sendFromSingle] End");
  }

  /**
   * This methods scans a directory for files to be send. As long as there
   * are files in the directory, it will attempt to send them. The files will
   * be moved to the OutToGT directory for preprocessing if any. If there are
   * any errors while trying to move the files to the OutToGT directory, it
   * will retry 200 times before it sends out an email to inform the user of
   * a problem.
   *
   * @param filesInDir  the File array containing the files to be send
   * @param backendIni  the BackendIni object holding the settings in the
   *                    backend.ini file
   * @param values      the ArrayList containing information from the directory
   *                    file, null if directory file is not used.
   * @param currentDir  the current directory
   * @param sourceDir   the directory containing the files to be send.
   */
  private static void sendFilesLoop(File[] filesInDir,
                                    BackendIni backendIni,
                                    ArrayList values,
                                    String currentDir,
                                    File sourceDir)
  {
    log("[sendFilesLoop] Start");
    int moveFileErr = 0;
    File[] oldFiles = null;
    String outputDirPath = currentDir + File.separatorChar + "outToGT";
    filesInDir = removeUnwantedFiles(filesInDir, backendIni.getLockFilename());
    while (filesInDir.length > 0)
    {
      try
      {
        oldFiles = getOldFiles(outputDirPath);
        sendFiles(oldFiles, backendIni, values, outputDirPath, true);
        filesInDir =
          movesFilesToOutToGTDir(filesInDir, backendIni, outputDirPath);
        sendFiles(filesInDir, backendIni, values, outputDirPath, false);
      }
      catch(MoveFileException mfe)
      {
        log("[sendFilesLoop] Exception in moving files", mfe);
        moveFileErr++;
        if (moveFileErr > 200)
        {
          ArrayList params = new ArrayList();
          params.add(mfe);
          sendFailureMail(backendIni, UNKNOWN_ERROR, params);
          break;
        }
      }
      catch(ConnectException ce)
      {
        log("[sendFilesLoop] Exception in connecting to GridTalk", ce);
        ArrayList params = new ArrayList();
        params.add(backendIni.getGT_IP());
        params.add(new Integer(backendIni.getPortNumber()).toString());
        sendFailureMail(backendIni, CONNECTION_ERROR, params);
        break;
      }
      catch(Exception e)
      {
        log("[sendFilesLoop] Exception in sendFiles", e);
        ArrayList params = new ArrayList();
        params.add(e);
        sendFailureMail(backendIni, UNKNOWN_ERROR, params);
        e.printStackTrace(System.out);
        break;
      }
      isThereAttachment = false;
      isAttachmentSend = false;
      attachmentIndex = -1;
      filesInDir = sourceDir.listFiles();
      filesInDir = removeUnwantedFiles(filesInDir, backendIni.getLockFilename());
    }
    log("[sendFilesLoop] End");
  }

  /**
   * This method will retrieve files that are still stuck in the
   * OutToGT directory.
   *
   * @param outputDirPath the path to the OutToGT directory.
   */
  private static File[] getOldFiles(String outputDirPath)
    throws Exception
  {
    log("[getOldFiles] Start");
    File outputDir = new File(outputDirPath);
    if (!outputDir.exists())
    {
      boolean success = outputDir.mkdir();
      if (!success)
      {
        throw new CreateDirectoryException(
          "Unable to create output directory "+outputDir.getAbsolutePath());
      }
    }

    File[] oldFiles = outputDir.listFiles();
    log("[getOldFiles] End");
    return removeUnwantedFiles(oldFiles, "");
  }

  /**
   * This method will move the files to the OutToGT directory, if preprocessing
   * is required, it will preprocess the file after it has been moved to the
   * OutToGT directory.
   */
  private static File[] movesFilesToOutToGTDir(File[] filesInDir,
                                             BackendIni backendIni,
                                             String outputDirPath)
                                             throws Exception
  {
    log("[movesFilesToOutToGTDir] Start");
    String commandFile = backendIni.getCommandFile();
    String lockFilename = backendIni.getLockFilename();
    if ((commandFile != null) && (!commandFile.equals("")))
    {
      filesInDir =
        processFiles(commandFile, outputDirPath, filesInDir, lockFilename);
    }
    else
    {
      filesInDir = moveFiles(outputDirPath, filesInDir, lockFilename);
    }
    log("[movesFilesToOutToGTDir] End");
    return filesInDir;
  }

  /**
   * This method will add the attachments to the file array and sends the
   * files to GridTalk, after that it will move the files send to the
   * sentToGT directory.
   *
   * @param filesInDir the File array containing the files to be send
   * @param values the ArrayList containing directory file information
   * @param outputDirPath the outToGT directory path
   * @param isOldFiles use to indicate if these files are files that are not
   *                   send successfully from the last sender
   */
  private static void sendFiles(File[] filesInDir,
                                BackendIni backendIni,
                                ArrayList values,
                                String outputDirPath,
                                boolean isOldFiles)
                                throws Exception
  {
    log("[sendFiles] Start");
    if (filesInDir.length > 0)
    {
      String attachDir = backendIni.getAttachmentDirectory();
      File fileInfo = null;
      if (values != null)
      {
        if (values.get(0) != null)
        {
          attachDir = values.get(0).toString();
        }
        else
        {
          attachDir = null;
        }
        if (values.get(1) != null)
        {
          String infoFilePath = values.get(1).toString();
          if (!infoFilePath.equals(""))
          {
            fileInfo = new File(infoFilePath);
          }
        }
      }

      if ((attachDir != null) && (!attachDir.equals("")))
      {
        log("[sendFiles] attachDir not empty");
        isThereAttachment = true;
        if (!isAttachmentSend)
        {
          log("[sendFiles] Adding attachments to filelist");
          ArrayList attFiles = getAttachments(attachDir);
          attachmentIndex = attFiles.size() - 1;
          filesInDir = addAttachments(filesInDir, attFiles);
          if (!isOldFiles)
          {
            isAttachmentSend = true;
          }
        }
      }

      log("[sendFiles] Sending import files");
      sendDocuments(backendIni, fileInfo, filesInDir, attachmentIndex);

      // Moving files to sentToGT directory
      moveImportedFiles(backendIni, filesInDir, outputDirPath, attachmentIndex);
      if (isThereAttachment && !isOldFiles)
      {
        log("[sendFiles] attachmentIndex = " + attachmentIndex);
        moveImportedAttFiles(filesInDir, attachDir, attachmentIndex);
      }
    }
    log("[sendFiles] End");
  }

  /**
   * This method moves the files to the outToGT directory one by one and
   * execute the command to do preprocessing on each of the files.
   *
   * @param command the command to be executed
   * @param outoutDirPath the outToGT directory
   * @param filesInDir the files to be moved
   * @param lockFilename the filename of the lockfile, so that it will not
   *                     be moved along with the other files.
   */
  private static File[] processFiles(String command,
                                     String outputDirPath,
                                     File[] filesInDir,
                                     String lockFilename)
                                     throws Exception
  {
    log("[processFiles] Start");
    ArrayList tempList = new ArrayList();
    String commandFile = "";
    for (int i = 0; i < filesInDir.length; i++)
    {
      ArrayList params = new ArrayList();
      File importFile = filesInDir[i];
      log("[processFiles] importfile = " + importFile.getName());
      log("[processFiles] lockFilename = " + lockFilename);
      if ((!(importFile.getName().equals(lockFilename))) &&
          (!importFile.isDirectory()))
      {
        String moveFilePath =
          outputDirPath + File.separatorChar + importFile.getName();
        File moveFile = new File(moveFilePath);
        if (!importFile.renameTo(moveFile))
        {
          if (i == filesInDir.length-1)
          {
            throw new MoveFileException(
              "Unable to move file "+importFile.getAbsolutePath()+
              " to "+moveFile.getAbsolutePath());
          }
        }
        else
        {
          StringTokenizer st = new StringTokenizer(command);
          if (st.hasMoreTokens())
          {
            commandFile = st.nextToken();
          }
          while (st.hasMoreTokens())
          {
            String token = st.nextToken();
            if (token.indexOf(FILENAME) > -1)
            {
              StringBuffer sb = new StringBuffer();
              sb.append(token.substring(0, token.indexOf(FILENAME)));
              sb.append(moveFile.getAbsolutePath());
              sb.append(token.substring(token.indexOf(FILENAME)+FILENAME.length()));
              //log("StringBuffer = " + sb.toString());
              params.add(sb.toString());
            }
            else
            {
              params.add(token);
            }
          }
          log("[processFiles] Executing " + commandFile + " " + params);
          CommandExecutor.execute(commandFile, params);
          File newImportFile = new File(moveFilePath);
          tempList.add(newImportFile);
        }
      }
    }

    File[] processedFiles = new File[tempList.size()];
    for (int i = 0; i < tempList.size(); i++)
    {
      processedFiles[i] = (File)tempList.get(i);
    }
    log("[processFiles] Return processedFiles = " + processedFiles.length);
    log("[processFiles] End");
    return processedFiles;
  }

  /**
   * This method moves the files from the current directory to the outToGT
   * directory.
   */
  private static File[] moveFiles(String outputDirPath,
                                  File[] filesInDir,
                                  String lockFilename)
                                  throws Exception
  {
    log("[moveFiles] Start");
    ArrayList tempList = new ArrayList();
    for (int i = 0; i < filesInDir.length; i++)
    {

      File importFile = filesInDir[i];
      if ((!(importFile.getName().equals(lockFilename))) &&
          (!importFile.isDirectory()))
      {
        String moveFilePath =
          outputDirPath + File.separatorChar + importFile.getName();
        File moveFile = new File(moveFilePath);
        log("[moveFiles] Moving " + importFile.getAbsolutePath() + " to " + moveFile.getAbsolutePath());
        if (!importFile.renameTo(moveFile))
        {
          if (i == filesInDir.length-1)
          {
            throw new MoveFileException(
              "Unable to move file "+importFile.getAbsolutePath()+
              " to "+moveFile.getAbsolutePath());
          }
        }
        else
        {
          File newImportFile = new File(moveFilePath);
          tempList.add(newImportFile);
        }
      }
    }

    File[] processedFiles = new File[tempList.size()];
    for (int i = 0; i < tempList.size(); i++)
    {
      processedFiles[i] = (File)tempList.get(i);
    }
    log("[moveFiles] End");
    return processedFiles;
  }

  // Koh Han Sing 02/04/2002
  /**
   * This method retrieve the attachments files from the attachment directory
   *
   * @param attachmentDir the directory containing the attachments
   * @return an ArrayList containing the attachments
   */
  private static ArrayList getAttachments(String attachmentDir)
    throws Exception
  {
    log("[getAttachments] Start");
    log("[getAttachments] attachmentDir " + attachmentDir);
    File attachDir = new File(attachmentDir);
    if (attachDir.exists())
    {
      File[] attachFileList = attachDir.listFiles();
      int numFiles = attachFileList.length;
      ArrayList attFiles = new ArrayList();
      for(int i = 0; i < numFiles; i++)
      {
        if(!attachFileList[i].isDirectory())
        {
          log("[getAttachments] attachFileList["+i+"] = " + attachFileList[i].getAbsolutePath());
          attFiles.add(attachFileList[i]);
        }
      }
      log("[getAttachments] End");
      return attFiles;
    }
    else
    {
      log("[getAttachments] attachment directory "+attachmentDir+" does not exist!");
      log("[getAttachments] End");
      return new ArrayList();
    }
  }

  /**
   * This methods adds the attachment files in the File array containing the
   * files to be sent to the GridTalk, the attachments will be added at the
   * begining of the file array
   */
  private static File[] addAttachments(File[] filesInDir, ArrayList attFiles)
  {
    log("[addAttachments] Start");
    int newFileArraySize = filesInDir.length + attFiles.size();
    File[] newFileArray = new File[newFileArraySize];
    int counter = -1;
    for (int i = 0; i < attFiles.size(); i++)
    {
      counter++;
      newFileArray[counter] = (File)attFiles.get(i);
    }
    for (int j = 0; j < filesInDir.length; j++)
    {
      counter++;
      newFileArray[counter] = filesInDir[j];
    }
    log("[addAttachments] End");
    return newFileArray;
  }

  /**
   * This method makes a connection to the GridTalk server, passes information
   * like the partner ID, document type and attachment index to the GridTalk
   * server and sends the files in the file array over.
   */
  private static void sendDocuments(BackendIni backendIni,
                                    File fileInfo,
                                    File[] filesInDir,
                                    int attachmentIndex)
                                    throws Exception
  {
    log("[sendDocuments] Start");
    log("[sendDocuments] sendFiles IP = " + backendIni.getGT_IP() + " Port = " +
      backendIni.getPortNumber());
    APIConfig config = new APIConfig(backendIni.getGT_IP(),
                                     backendIni.getPortNumber(),
                                     backendIni.getUserName(),
                                     backendIni.getPassword());
    APIServiceInterface serviceInterface = new APIServiceInterface(config);

    APIParams apiParams = null;
    try
    {
      apiParams = serviceInterface.serviceConnect();
    }
    catch(Exception ex)
    {
      throw new ConnectException("Unable to connect to GridTalk");
    }

    Object[] connectID = apiParams.getParamArray();

    // Filling in return APIParams object array
    Object[] returnObjs = new Object[10];
    returnObjs[0] = connectID[0];
    returnObjs[1] = backendIni.getUserName();
    if(fileInfo != null)
    {
      InitManager im = new InitManager();
      im.loadProperties(fileInfo);
      returnObjs[2] = im.getProperty("partnerID");
      returnObjs[3] = im.getProperty("docType");
    }
    else
    {
      returnObjs[2] = null;
      returnObjs[3] = null;
    }
    if (isThereAttachment)
    {
      returnObjs[4] = new Integer(attachmentIndex);
    }
    else
    {
      returnObjs[4] = new Integer(-1);
    }
    returnObjs[5] = backendIni.getBizEntId();
    returnObjs[6] = null; //placeholder for rnprofile uid
    returnObjs[7] = null; //placeholder for unique id
    returnObjs[8] = null; //placeholder for process instance id
    returnObjs[9] = null; //placeholder for tracing id

    for(int i = 0; i < returnObjs.length; i ++)
    {
      log("[sendDocuments] Object " + i + " = " + returnObjs[i]);
    }

    APIParams parameters = new  APIParams(returnObjs, filesInDir);
    serviceInterface.performService(IAPIService.IMPORT, parameters);
    serviceInterface.serviceDisconnect();
    log("[sendDocuments] End");
  }

  /**
   * This method moves the sent files to the sentToGT directory after they
   * have been sent.
   */
  private static void moveImportedFiles(BackendIni backendIni,
                                        File[] filesInDir,
                                        String currentDir,
                                        int attachmentIndex)
                                        throws Exception
  {
    log("[moveImportedFiles] Start");
    String tempFileDir;
    if(backendIni.getImportedDir() != null &&
      !backendIni.getImportedDir().equals(""))
    {
      tempFileDir = backendIni.getImportedDir();
    }
    else
    {
      tempFileDir = currentDir + File.separatorChar + "sentToGT";
    }
    for(int i = attachmentIndex + 1; i < filesInDir.length; i++)
    {
      File tempFile = filesInDir[i];
      String oldFileDir = currentDir + File.separatorChar + tempFile.getName();
      File oldFileObj = new File(oldFileDir);
      String newFile = tempFileDir + File.separatorChar + tempFile.getName();
      File newFileObj = new File(newFile);
      newFileObj.getAbsoluteFile().getParentFile().mkdirs();
      log("[moveImportedFiles] Moving " + oldFileObj.getAbsolutePath() + " to " +
        newFileObj.getAbsolutePath());
      if(!oldFileObj.renameTo(newFileObj))
      {
        log("[moveImportedFiles] Problem moving file");
        String newTempFileDir;
        if(backendIni.getExceptionDir() != null &&
          !backendIni.getExceptionDir().equals(""))
        {
          newTempFileDir = backendIni.getExceptionDir();
        }
        else
        {
          newTempFileDir = currentDir + File.separatorChar + "fileCollision";
        }
        String uniqueFN = getUniqueFilename(tempFile.getName(), newTempFileDir);
        File uniqueFNFileObj =
          new File(newTempFileDir + File.separatorChar + uniqueFN);
        uniqueFNFileObj.getAbsoluteFile().getParentFile().mkdirs();
        log("[moveImportedFiles] Moving " + oldFileObj.getAbsolutePath() + " to " +
          uniqueFNFileObj.getAbsolutePath());
        if (!oldFileObj.renameTo(uniqueFNFileObj))
        {
          throw new FileInUseException(
            "Unable to move file "+oldFileObj.getAbsolutePath()+
            " to "+uniqueFNFileObj.getAbsolutePath());
        }
      }
    }
    log("[moveImportedFiles] End");
  }

  /**
   * This method moves the attachment files to the sentToGT directory
   * after they have been sent.
   */
  private static void moveImportedAttFiles(File[] filesInDir,
                                           String attachmentDir,
                                           int attachmentIndex)
  {
    log("[moveImportedAttFiles] Start");
    String tempFileDir = attachmentDir + File.separatorChar + "sentToGT";
    for(int i = 0; i <= attachmentIndex; i++)
    {
      File tempFile = filesInDir[i];
      String oldFileDir = attachmentDir + File.separatorChar + tempFile.getName();
      File oldFileObj = new File(oldFileDir);
      String newFile = tempFileDir + File.separatorChar + tempFile.getName();
      File newFileObj = new File(newFile);
      newFileObj.getAbsoluteFile().getParentFile().mkdirs();
      log("[moveImportedAttFiles] Moving " + oldFileObj.getAbsolutePath() + " to " +
        newFileObj.getAbsolutePath());
      if(!oldFileObj.renameTo(newFileObj))
      {
        log("[moveImportedAttFiles] Problem moving file");
        String newTempFileDir = attachmentDir + File.separatorChar + "fileCollision";
        String uniqueFN = getUniqueFilename(tempFile.getName(), newTempFileDir);
        File uniqueFNFileObj =
          new File(newTempFileDir + File.separatorChar + uniqueFN);
        uniqueFNFileObj.getAbsoluteFile().getParentFile().mkdirs();
        log("[moveImportedAttFiles] Moving " + oldFileObj.getAbsolutePath() + " to " +
          uniqueFNFileObj.getAbsolutePath());
        oldFileObj.renameTo(uniqueFNFileObj);
      }
    }
    log("[moveImportedAttFiles] End");
  }

  /**
   * This method gets a uniquefilename for the file.
   *
   * @param the filename of the file
   * @param the path where the file will be stored.
   * @return the unique filename of the file in the path stated
   */
  static private String getUniqueFilename(String filename, String path)
  {
    int index = filename.lastIndexOf('.');
    String ext = "";
    String name = "";

    if (index != -1)
    {
      ext = filename.substring(index);
      name = filename.substring(0, index);
    }
    else
      name = filename;

    String fname = name + ext;
    File f = new File(path + File.separatorChar + fname);
    for ( int i=1; f.exists(); i++ )
    {
      fname = name + "_" + i + ext;
      f = new File(path + File.separatorChar + fname);
    }
    return fname;
  }

  /**
   * This method locks the directory specified by creating a file with the
   * filename specified. The lockfile will contain a timestamp of when the
   * directory is locked.
   *
   * @param directory the directory to be locked
   * @param lockFilename the filename of the lock file
   * @param timestamp the timestamp to be stored in the lock file
   * @return whether the directory is successfully locked
   */
  private static boolean lock(File directory, String lockFilename, long timestamp)
  {
    if (locker == null)
    {
      locker = new Locker();
    }
    return locker.lock(directory, lockFilename, timestamp);

  }

  /**
   * This method checks if a locked directory has exipired. A locked directory
   * is exipired when the timestamp stored in the lockfile and exceed the
   * timeout time defined in the backend ini file.
   */
  private static boolean isLockExpired(File directory, String lockFilename,
    long timestamp, long timeout)
  {
    if (locker == null)
    {
      locker = new Locker();
    }
    return locker.isLockExpired(directory, lockFilename, timestamp, timeout);

  }

  /**
   * This methods removes unwanted File objects from the File array like the
   * lockfile and directorys.
   */
  private static File[] removeUnwantedFiles(File[] files, String lockFilename)
  {
    ArrayList filelist = new ArrayList();
    for (int i = 0; i < files.length; i++)
    {
      File aFile = files[i];
      if ((!aFile.isDirectory()) && (!(aFile.getName().equals(lockFilename))))
      {
        filelist.add(aFile);
      }
    }
    File[] returnlist = new File[filelist.size()];
    for (int j = 0; j < filelist.size(); j++)
    {
      returnlist[j] = (File)filelist.get(j);
    }
    return returnlist;
  }

  /**
   * This method sends error emails to the recipent set in the backend.ini
   * file.
   */
  private static void sendFailureMail(BackendIni backendIni,
                                      int errorCode,
                                      ArrayList params)
  {
    if ( (!backendIni.getSmtp().equals("")) &&
         (!backendIni.getEmail().equals("")) )     // Koh Han Sing 15/02/2002
    {
      IGmail mail = GridMail.createGmail();
      mail.setSender("gt_backend@gridnode.com");
      log("[sendFailureMail] Sending email for failure to " + backendIni.getEmail());
      mail.setRecipient(backendIni.getEmail());

      StringBuffer message = new StringBuffer();
      switch (errorCode)
      {
        case DIRECTORY_NOT_FOUND:
          mail.setSubject("Backend Sender Exception : Missing host directory");
          message.append("Backend sender fails to find host directory ");
          message.append(params.get(0).toString());
          message.append(" while importing documents into GridTalk.");
          message.append(" Please ensure that the host directory exists.");
          log("[sendFailureMail] Sending email for failure message for DIRECTORY_NOT_FOUND");
          break;

        case CONNECTION_ERROR:
          mail.setSubject("Backend Sender Exception : Connection error");
          message.append("Backend sender fails to connect to GridTalk IP : ");
          message.append(params.get(0).toString());
          message.append(" with port : ");
          message.append(params.get(1).toString());
          message.append(" while importing documents into GridTalk.");
          message.append(" Please ensure that the network settings are");
          message.append(" correct and that GridTalk is started.");
          log("[sendFailureMail] Sending email for failure message for CONNECTION_ERROR");
          break;

        default:
          //params contain exception
          mail.setSubject("Backend import fail");
          message.append("GridTalk Ver 1.2 Backend ");
          message.append(((Exception)params.get(0)).getMessage());
          log("[sendFailureMail] Sending email for failure message " + message);
          break;
      }
      mail.setMessage(message.toString());
      log("[sendFailureMail] Sending mail");
      GridMail.send(mail);
    }
    else
    {
      log("[sendFailureMail] Missing email settings: SMTP = \"" + backendIni.getSmtp() + "\"" +
          " email = \"" + backendIni.getEmail() + "\"");
    }
  }

  private static void log(String msg, Exception e)
  {
    Log.err(CATEGORY, msg, e);
//    System.out.println(msg);
  }

  private static void log(String msg)
  {
    Log.log(CATEGORY, msg);
//    System.out.println("[sender] " + msg);
  }
}