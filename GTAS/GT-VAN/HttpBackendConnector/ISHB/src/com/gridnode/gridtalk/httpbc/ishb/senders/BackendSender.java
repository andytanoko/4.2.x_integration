/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Feb 24 2007    i00107              Variable command args.
 * Mar 05 2007		Alain Ah Ming				Added error code to error logs
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import com.gridnode.gridtalk.httpbc.common.model.FileContent;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.StringUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.io.StreamGobbler;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This class is responsible for sending the transaction doc to GridTalk.
 */
public class BackendSender
{
  private Logger _logger;
  private BackendSendConfig _config;
  private File _workDir;
  private int _retCode;
  
  /**
   * Constructs a BackendSender to send the files to GridTalk
   */
  public BackendSender()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "BackendSender");
    _config = getConfig();
  }
  
  /**
   * Get the configuration for backend send.
   * @return The BackendSendConfig containing the properties required for backend send.
   */
  private BackendSendConfig getConfig()
  {
    Properties props = ConfigurationStore.getInstance().getProperties(IConfigCats.BACKEND_SEND);
    return new BackendSendConfig(props);
  }

  /**
   * Send the specified document.
   * 
   * @param tDoc The document
   * @throws SenderException Unable to send the specified document
   */
  public void send(TransactionDoc tDoc) throws SenderException
  {
    String mtdName = "send";
    Object[] params = {tDoc};
    
    try
    {
      _logger.logEntry(mtdName, params);
      
      //create the temp folder base on the tracingId of the document
      createWorkDir(tDoc.getTracingId());
      
      //write doc to temp folder
      String docFilename = writeDoc(tDoc.getDocContent());
      String attFilenames = writeAttachments(tDoc.getAttContent());
      
      //write sender command using template
      String[] cmd = generateCmd(tDoc, docFilename, attFilenames);
      
      //invoke sender command
      StringBuffer errorBuff = new StringBuffer();
      _retCode = invokeCmd(cmd, errorBuff);
      
      //if fail, throw exception
      if (_retCode != 0)
      {
        String errorMsg = "Backend sender invocation returns error [ErrorCode,ErrorDetails]=["+
        _retCode+","+errorBuff.toString()+"]";
        _logger.logWarn(mtdName, params, errorMsg, null);
        //generate the error file
        File errorFile = new File(_workDir, "error.txt");
        IOUtil.write(errorFile, errorMsg);
        throw new SenderException(errorMsg);
      }
      else
      {
        //cleanup the temp files in the temp folder
        cleanupTempFiles(_workDir, "");
      }
    }
    catch (IOException ex)
    {
      _logger.logWarn(mtdName, params, "Error sending transaction via backend sender", ex); 
      throw new SenderException("Error sending transaction via backend sender", ex);
    }
    finally
    {
      //cleanup temp folder
      _logger.logExit(mtdName, params);
    }
  }

  /**
   * Get sender return code.
   * @return The return code from executing the backend sender.
   */
  public int getReturnCode()
  {
    return _retCode;
  }
  
  /**
   * Cleanup temporary folder after successful send.
   * 
   * @param parent The parent directory
   * @param subDir The directory to delete.
   */
  private void cleanupTempFiles(File parent, String subDir)
  {
    File dir = new File(parent, subDir);
    File[] files = dir.listFiles();
    if (files != null)
    {
      for (File f : files)
      {
        if (f.isDirectory())
        {
          cleanupTempFiles(f.getParentFile(), f.getName());
        }
        else
        {
          f.delete();
        }
      }
    }
    if (dir.exists())
    {
      dir.delete();
    }
  }
  
  /**
   * Invoke the specified command.
   * 
   * @param cmd The command string
   * @param errorBuff StringBuffer to contain the error details if fail to execute the command
   * @return Return code from the execution of the command. 0 indicates a successful execution, other values
   * indicates an error has occurred during the execution of the command.
   */
  private int invokeCmd(String[] cmd, StringBuffer errorBuff)
  {
    int retCode;
    StreamGobbler sGobbler = null;
    try
    {
      ProcessBuilder procBlder = new ProcessBuilder(cmd);
      procBlder.redirectErrorStream(true);
      procBlder.directory(_workDir);
      Process proc = procBlder.start();
      sGobbler = new StreamGobbler(proc.getInputStream(), _config.getCmdFilename(), true);
      sGobbler.start();
      
      retCode = proc.waitFor();
    }
    catch (InterruptedException ex)
    {
      errorBuff.append(ExceptionUtil.getStackStraceStr(ex));
      return -2;
    }
    catch (IOException ex)
    {
      errorBuff.append(ExceptionUtil.getStackStraceStr(ex));
      return -1;
    }
    if (retCode != 0)
    {
      String errors = sGobbler.getCachedGobbledData();
      errorBuff.append(errors);
    }
    return retCode;
  }
  
  /**
   * Generate the command to run to send the specified document to GridTalk. The command file
   * to run will be written to file system under the working directory.
   * 
   * @param tDoc The document to send
   * @param docFilename The filename of the physical file of the document
   * @param attFilenames A concatenated string of the physical filenames of the attachments of
   * the document. The filenames are concatenated using semi-colon (;).
   * @return The generated command.
   * @throws IOException An error occurred when trying to write the command file to the file system.
   */
  private String[] generateCmd(TransactionDoc tDoc, String docFilename, String attFilenames)
    throws IOException
  {
    String cmdTemplate = _config.getCommandTemplate(tDoc.getPartnerId());
    if (StringUtil.isBlank(cmdTemplate))
    {
      cmdTemplate = _config.getDefaultCommandTemplate();
    }
    
    String cmd = MessageFormat.format(cmdTemplate, _config.getSenderJarPath(),
                                      tDoc.getBizEntId(), tDoc.getPartnerId(),
                                      tDoc.getDocType(), tDoc.getTracingId(),
                                      docFilename, attFilenames);
    File cmdFile = new File(_workDir, _config.getCmdFilename());
    IOUtil.write(cmdFile, cmd);
    
    String[] invokeCmd = _config.getInvokeCommandTemplate();
    for (int i=0; i<invokeCmd.length; i++)
    {
      invokeCmd[i] = MessageFormat.format(invokeCmd[i], cmdFile.getAbsolutePath());
    }
    return invokeCmd;
  }
  
  /**
   * Create the working directory for sending a document.
   * @param tracingId The tracingId of the document to send.
   */
  private void createWorkDir(String tracingId)
  {
    _workDir = new File(_config.getWorkingDir(), tracingId);
    _workDir.mkdirs();
  }
  
  /**
   * Write the contents out to a file.
   * @param doc The contents to be written out.
   * @return The absolute path of the file written out.
   * @throws IOException
   */
  private String writeDoc(FileContent doc) throws IOException
  {
    File f = new File(_workDir, doc.getFilename());
    IOUtil.write(f, doc.getContent());
    return f.getAbsolutePath();
  }
  
  /**
   * Write the contents of attachments out as physical files.
   * @param attachments The attachments to write out.
   * @return Concatenated string of the absolute filenames of the attachments written out.
   * @throws IOException
   */
  private String writeAttachments(FileContent[] attachments)
    throws IOException
  {
    if (attachments != null)
    {
      String fns = "";
      for (FileContent f : attachments)
      {
        File file = new File(_workDir, "att/"+f.getFilename());
        IOUtil.write(file, f.getContent());
        if (fns.length() > 0)
        {
          fns += ";";
        }
        fns += file.getAbsolutePath();
      }
      return fns;
    }
    else
    {
      return null;
    }
  }
}
