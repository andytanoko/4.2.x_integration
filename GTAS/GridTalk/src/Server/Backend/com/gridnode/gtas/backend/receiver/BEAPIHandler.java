/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BEAPIHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 31 2001    Neo Sok Lay         GNDB00005394: Change file extension as
 *                                    specified.
 * Feb 15 2002    Koh Han Sing        Check for empty SMTP settings
 * Apr 24 2002    Koh Han Sing        Add in attachment feature
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 * Sep 30 2003    Koh Han Sing        Changed to use log4j
 * May 31 2004    Neo Sok Lay         GNDB00024914: Concurrent creation of
 *                                    host dir.
 * Aug 27 2005    Tam Wei Xiang       Updated export capabilty of RFC to follow
 *                                    the same way as Export to local(eg all related
 *                                    udoc,gdoc,attachment will be stored in the
 *                                    same folder)
 * Mar 02 2006    Tam Wei Xiang       Add new port's field fileGrouping which make
 *                                    the orgarnize of the gdoc, udoc and attachment
 *                                    more flexible                                   
 * Apr 05 2006    Neo Sok Lay         GNDB00026789: Remove '_' between filename and filename extension
 * Jun 23 2006    Tam Wei Xiang       GNDB00026716: The working dir for the new subprocess that execute
 *                                                  the cmd file will be the commandDir.
 * Apr 17 2006    Tam Wei Xiang       GNDB00028306: Remove the extra slash at the end of the host dir                                                 
 *
 * Export
 * ------
 * paramArray[0]  Command Directory
 * paramArray[1]  Command File
 *
 * Import
 * ------
 * paramArray[0]  RFC Host
 * paramArray[1]  RFC Port Number
 * paramArray[2]  Host Directory
 * paramArray[3]  New Filename
 * paramArray[4]  isOverwrite
 * paramArray[5]  Command Directory
 * paramArray[6]  Command File
 * paramArray[7]  Command Line
 * paramArray[8]  addFileExt
 * paramArray[9]  fileExtFormat
 * paramArray[10] fileExtValue
 * paramArray[11] Email Receiptient
 * paramArray[12] isExportGdoc
 * paramArray[13] Attachment start index in file array
 * paramArray[14] ArrayList of orginal attachment filenames
 * paramArray[15] fileGrouping
 */
package com.gridnode.gtas.backend.receiver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import com.gridnode.gridmail.GridMail;
import com.gridnode.gridmail.IGmail;
import com.gridnode.gtas.backend.util.Log;
import com.gridnode.gtas.backend.util.StreamGobbler;
import com.gridnode.gtas.backend.util.XMLAppend;
import com.gridnode.gtas.server.backend.model.IPort;
import com.gridnode.gtas.server.backend.openapi.core.APIParams;
import com.gridnode.gtas.server.backend.openapi.core.IAPIService;
import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.gtas.server.backend.openapi.net.APIConnect;

public class BEAPIHandler extends Thread
{
  private APIComm comm;
  private ObjectInputStream input;
  private ObjectOutputStream output;
  private String callbackIP = null;
  private int callbackPort = 0;
  private String osName;
  private String[] cmd = null;
  private String mailReceipient = "";
  private String smtp = "";   // KHS 15/02/2002

  private static final int UNKNOWN_ERROR = 0;
  private static final int UNABLE_TO_SAVE_DOCUMENT = 1;
  private static final int UNABLE_TO_INVOKE_CMD_FILE = 2;
  private static final int UNABLE_TO_CONNECT_GT = 3;
  private static final int TRANSMISSION_FAIL = 4;
  private static final int APPEND_FAIL = 5;
  private static final int UNABLE_TO_SAVE_ATTACHMENT = 6;

  private static final String CATEGORY = "BEAPIHandler";

  private static final String UDOC = "%UDOC%";
  private static final String ATTACHMENTS = "%ATTACHMENTS%";

  public BEAPIHandler(APIComm commSrc, String smtp, String email)
  {
    try
    {
      comm = commSrc;
      input = comm.getInput();
      output = comm.getOutput();
      input.readObject();
      input.readObject();
      APIParams parameters = new  APIParams(new Object[1], null);
      output.writeObject(parameters);
      output.flush();
      osName = System.getProperty("os.name" );
      GridMail.setSMTP(smtp);
      this.smtp = smtp;
      this.mailReceipient = email;
    }
    catch (Exception e)
    {
    	handleException(UNABLE_TO_CONNECT_GT, e);
    }
  }

  public void performCallback(int serviceID, APIParams params)
    throws Exception
  {
    APIConnect callbackConnect = new APIConnect(callbackIP, callbackPort);
    APIComm callbackComm = callbackConnect.getCommunication();
    ObjectOutputStream callbackOutput = callbackComm.getOutput();
    callbackOutput.writeObject(new Integer(serviceID));
    callbackOutput.flush();
    callbackOutput.writeObject(params);
    callbackOutput.flush();
    params.writeFiles(callbackComm);
    callbackComm.close();
  }

  public void run()
  {
    log("BEAPIHandler.run()");
    Object received;
    APIParams params = null;
    boolean runHandler = true;
    while(runHandler)
    {
      try
      {
        try
        {
          received = input.readObject();
        }
        catch (Exception ex)
        {
        	handleException(TRANSMISSION_FAIL, ex);
          break;
        }

        if (received instanceof Integer) // handleService
        {
          handleService(received, params);
        }
        else if (received instanceof String) // handleCommand
        {
          runHandler = handleCommand(received);
        }
      }
      catch (Exception e)
      {
      	handleException(TRANSMISSION_FAIL, e);
      }
    }
  }

  private void handleService(Object received, APIParams params)
  {
    try
    {
      try
      {
        params = (APIParams)(input.readObject());
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
      Boolean hasFileArray = (Boolean)(input.readObject());
      if(hasFileArray.booleanValue())
      {
        params.readFiles(comm);
      }
      Object[] success = new Object[1];
      success[0] = new Boolean(true);
      APIParams parameters = new  APIParams(success, null);
      output.writeObject(parameters);
      output.flush();
      service(((Integer)received).intValue(), params);
    }
    catch (Exception e)
    {
    	handleException(TRANSMISSION_FAIL, e);
    }
  }

  private void handleException(int errorCode, Exception e, Object... params)
  {
    e.printStackTrace(System.out);
    log(e.getMessage(), e);
    if (params != null)
    {
    	ArrayList<Object> params2 = new ArrayList<Object>();
    	for (Object param : params)
    	{
    		params2.add(param);
    	}
    	sendFailureMail(errorCode, params2);
    }
    else
    {
    	ArrayList<Exception> params2 = new ArrayList<Exception>();
    	params2.add(e);
    	sendFailureMail(errorCode, params2);
    }
  }
  
  private boolean handleCommand(Object received)
  {
    boolean runHandler = true;
    try
    {
      String command = (String)received;
      String keyword = command.substring(0, command.indexOf(' '));
      if (keyword.equals("DISCONNECT"))
      {
        output.writeBoolean(true);
        output.flush();
        comm.close();
        runHandler = false;
      }
    }
    catch (Exception e)
    {
    	handleException(TRANSMISSION_FAIL, e);
    }
    return runHandler;
  }

  public void service(int serviceID, APIParams params)
  {
    switch (serviceID)
    {
      case IAPIService.CONNECT:

      case IAPIService.DISCONNECT:

      case IAPIService.EXPORT:
        exportFile(params);
        break;

      case IAPIService.IMPORT:
        importFile(params);
        break;
    }
  }

  private void exportFile(APIParams params)
  {
    log("Exporting files from backend to GridTalk");
    Object[] paramArray = params.getParamArray();
    String commandDir = (String)paramArray[0];
    String commandFile = (String)paramArray[1];
    String commandLine = (String)paramArray[2];
    log("Command dir = " + commandDir + "Command File = " + commandFile);
    executeBatchFile(commandDir, commandFile, commandLine, null, null, null);
  }

  /**
   * <p>
   * Execute the batch file to process the file received. The batch file
   * can be the SQL batch file or any other batch file depending on whether
   * there are any mapping file defined in the backend ini file
   * </p>
   *
   * @param commandDir The working directory for the command file to execute
   * @param commandFile The command file to execute
   * @param commandParams The argument options to the command file
   * @param dataFile The user document file to pass to the command file
   * @param attFilenames Concatenated string of gdoc and attachment filenames to pass
   * to the command file
   * @param rfc The name of the RFC this execution is performed
   */
  public void executeBatchFile(String commandDir, String commandFile,
         String commandParams, String dataFile, String attFilenames, String rfc)
  {
    try
    {
      if(cmd == null)
      {
        String commandLine = null;
        if(commandDir != null)
        {
          commandLine = commandDir;
        }
        commandLine = commandLine.trim() + File.separatorChar + commandFile.trim() + " " + commandParams;   // new
        log("Executing : " + commandLine + " in " + osName);
        ArrayList<String> temp = new ArrayList<String>();
        if(osName.equals("Windows NT"))
        {
          temp.add("cmd.exe");
          temp.add("/C");
          addCommandLine(temp, commandLine, dataFile, attFilenames);
        }
        else if(osName.equals("Windows 95") || osName.equals("Windows 98"))
        {
          temp.add("command.com");
          temp.add("/C");
          addCommandLine(temp, commandLine, dataFile, attFilenames);
        }
        else
        {
          log("CommandLine = "+commandLine);
          addCommandLine(temp, commandLine, dataFile, attFilenames);
        }
        int size = temp.size();
        cmd = new String[size];
        log("cmd.length = "+cmd.length);
        for(int i = 0; i < size; i++)
        {
          log("PARAM "+i+ " =" + temp.get(i)+"=");
          cmd[i] = temp.get(i);
        }
      }
      Runtime runtime = Runtime.getRuntime();
      Process proc = runtime.exec(cmd, null, new File(commandDir)); //TWX 23062006 The working dir of the new subprocess 
                                                                    //that run the cmd file will be the commandDir.
      StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
      StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
      errorGobbler.start();
      outputGobbler.start();
      int returnValue = proc.waitFor();
      log("Return value from command call : " + returnValue);
    }
    catch(Exception e)
    {
    	handleException(UNABLE_TO_INVOKE_CMD_FILE, e, commandFile, dataFile, rfc, commandDir);
    }
  }

  private void importFile(APIParams params)
  {
    Object[] paramArray = params.getParamArray();
    log(paramArray.toString());
    String hostDir = (String)paramArray[2];
    //String exportSubDir = "";
    String newFilename = (String)paramArray[3];
    String serverMailReceipient = (String)paramArray[11];
    File[] files = params.getFileArray();
    String udocFilename = "";
    //boolean isDiffFilename;
    if(files[0]!= null)
    {
    	udocFilename = files[0].getName();
    }
    
    if ((serverMailReceipient != null) && (!serverMailReceipient.equals("")))
    {
      mailReceipient = serverMailReceipient;
    }
    //Boolean isExportGdoc = (Boolean)paramArray[12];
    int attachIndex = ((Integer)paramArray[13]).intValue();
    ArrayList<String> fileNames = (ArrayList<String>)paramArray[14];
    fileNames.add(0,udocFilename); //add in udoc filename

    log("Importing " + attachIndex + " files from GridTalk to Backend");
    log("Importing " + (files.length - attachIndex) +
        " attachments from GridTalk to Backend");
    
    hostDir = hostDir.replace('\\', '/');
    
    log("importFile::Host dir = " + hostDir);
    boolean success = makeDirectory(hostDir);
    if (success)
    {
      String attFilenames = saveFiles(files, hostDir, 
             fileNames, paramArray);   
      log("Imported attFiles: "+attFilenames);
    }
    else
    {
      //Koh Han Sing 13/12/2001
      //modified by Wei Xiang 
      ArrayList<String> params2 = new ArrayList<String>();
      if (newFilename != null)
      {
        params2.add(newFilename);
      }
      else
      {
        params2.add(files[0].getName());
      }
      
      //add in the gdoc, attachment filename if any
      for(int i=1; i < files.length; i++) 
      {
        	params2.add(files[i].getName());	
      }
      params2.add(hostDir);
      sendFailureMail(UNABLE_TO_SAVE_DOCUMENT, params2);
    }
  }
	
  /*
  //modified by wei xiang
  //this method is no longer used by any method in this class
  private void importaFile(File importFile, Object[] paramArray,
    					String hostDir, String attFilenames)
  {
    String rfc = (String)paramArray[0];
    String newFilename = (String)paramArray[3];
    boolean isOverwrite = ((Boolean)paramArray[4]).booleanValue();
    String commandDir = (String)paramArray[5];
    String commandFile = (String)paramArray[6];
    String commandLine = (String)paramArray[7];
    boolean addFileExt = ((Boolean)paramArray[8]).booleanValue();
    String fileExtFormat = (String)paramArray[9];
    String fileExtValue = (String)paramArray[10];

    String returnFile = "";
    String fileExt = "";
    String fileName = importFile.getName();
    
    returnFile = writeFile(importFile, hostDir, isOverwrite);
    log("Write to " + returnFile);

    if(commandFile != null && !commandFile.equals(""))
    {
      executeBatchFile(commandDir, commandFile, commandLine, hostDir+returnFile, attFilenames, rfc);
    }
  }
	*/
	
	//modify by Wei Xiang
  private String saveFiles(File[] files, String hostDir, 
          ArrayList fileNames, Object[] paramArray)
  {
  	//WX: attr initiliaze
  	String exportSubDir = "";
  	int attachIndex =((Integer)paramArray[13]).intValue();
  	String exportFileName = (String) paramArray[3];
  	String udocFilename = files[0].getName();
  	boolean isExportGdoc = ((Boolean)paramArray[12]).booleanValue();
  	boolean isOverwrite = ((Boolean)paramArray[4]).booleanValue();
  	boolean isAddFileExt = ((Boolean)paramArray[8]).booleanValue();
  	//String fileExtFormat = (String)paramArray[9];
    String fileExtValue = (String)paramArray[10];
    //String fileNameExt = "";
    
    String commandDir = (String)paramArray[5];
    String commandFile = (String)paramArray[6];
    String commandLine = (String)paramArray[7];
    String rfc = (String)paramArray[0];
    
    Integer fileGrouping = (Integer)paramArray[15]; //indicate the file grouping option
                                                    //refer to IPort.java
    
    boolean isDiffFilename = false; 
    if(exportFileName != null && exportFileName.length() > 0)
    {
    	isDiffFilename = true;
    }

  	StringBuffer attFilenames = new StringBuffer();
		
  	Integer fileGroupingOption = fileGrouping == null || fileGrouping==0 ? 
  	                                 		IPort.FILE_GROUPING_OPTION_ATTACHMENT_GDOC : 
  	                                 			                                 fileGrouping;
  	
		//WX: process the exportFilename, include add in file extension value if any. 
    exportFileName = getExportedFilename(exportFileName, udocFilename, fileExtValue, isAddFileExt);
    exportSubDir = getExportSubDir(fileGroupingOption, exportFileName);
    
    /*
    if(IPort.FILE_GROUPING_OPTION_ATTACHMENT_GDOC.equals(fileGroupingOption) || 
   		 IPort.FILE_GROUPING_OPTION_GROUP_ALL.equals(fileGroupingOption))
    {
    	exportSubDir = createSubDirectory(isExportGdoc, exportSubDir, exportFileName );
    } */
		
    
    hostDir = hostDir.replace('\\', '/');
		String exportDir = hostDir;
    boolean successAtt = makeDirectory(exportDir);
    if (successAtt)
    {
      //export UDOC
    	String udocDestFolderPath = getUDocDestFolderPath(fileGrouping, hostDir, exportSubDir);
      writeFile(files[0], udocDestFolderPath, exportFileName,isOverwrite); //use writeFile, can handle append case
      	
      if (isExportGdoc)
      {
        String gdocFilename = files[attachIndex].getName();
        
        String gdocDestFolderPath = getGDOCDestFolderPath(fileGrouping, hostDir, exportSubDir);
        
        gdocFilename = saveaFile(files[attachIndex], gdocDestFolderPath, gdocFilename, isOverwrite);
        attFilenames.append(gdocFilename);
        attFilenames.append(";");
        attachIndex=attachIndex+1;
      }
        
      //Export the attachment files if any
      String attachDestFolderPath = getExportAttachmentFolderPath(fileGrouping, hostDir, exportSubDir,attachIndex < files.length);
      //Iterator attNameItr = fileNames.iterator();
      for (int j = attachIndex; j < files.length; j++)
      {
        String attFilename = files[j].getName();
        attFilename = saveaFile(files[j], attachDestFolderPath, attFilename, isOverwrite);
        attFilenames.append(attFilename);
        attFilenames.append(";");
      }
      
      //Execute user defined batch File if any
      //added by wei xiang
      if(commandFile != null && !commandFile.equals(""))
      {
        File udocAbsPath = new File(udocDestFolderPath+ exportFileName);
        executeBatchFile(commandDir, commandFile, commandLine, udocAbsPath.getAbsolutePath(), attFilenames.toString(), rfc);
      }
    }
    else
    {
      String newFilename = (String)paramArray[3];
      ArrayList<String> params2 = new ArrayList<String>();
      if (newFilename != null)
      {
        params2.add(newFilename);
      }
      else
      {
        params2.add(files[0].getName());
      }
      
      //add in the gdoc, attachment filename if any
      for(int i=1; i < files.length; i++) 
      {
        	params2.add(files[i].getName());	
      }
      params2.add(hostDir);
      sendFailureMail(UNABLE_TO_SAVE_DOCUMENT, params2);
    }

    return attFilenames.toString();
  }

/**
	 * TWX: Save a file to the given exportDir. If isOverwrite specify, and the file we
	 * want to export already exist, such an existing file will be overwrite.
	 * If no overwrite specify but the file we export already exist, such existing
	 * file's name will be append with timestamp.
	 * @param source
	 * @param exportDir
	 * @param filename
	 * @param isOverwrite
	 * @throws Exception
	 */
	private String saveaFile(File source, String exportDir,
	                         String filename, boolean isOverwrite)
	{
		File newFile = new File(exportDir + filename);
	  if (newFile.exists() && ! isOverwrite)
	  {
	  	String currentTimestamp = getTimestampFormat("ddMMyyHHmmssSSS");
	    String originFilename = source.getName();
	    
	    originFilename = getFilenameWithoutFilenameExt(originFilename)+currentTimestamp+
                           getFilenameExtension(originFilename);
	    
	    newFile = new File(exportDir+ originFilename);
	  }
	  else if(newFile.exists() && isOverwrite)
	  {
	  	newFile.delete();
	  	newFile = new File(exportDir + filename);
	  }
	  source.renameTo(newFile);
	  return newFile.getAbsolutePath();
	}

  /*NSL20060405 Not used
  // Add by Koh Han Sing 12/10/2001
  private String addFileExt(String newFilename, String fileExtFormat,
    String fileExtValue)
  {
    String fileExt = "";
    if (fileExtFormat.equals(""))
    {
      fileExt = fileExtValue;
    }
    else
    {
//      SimpleDateFormat formatter = new SimpleDateFormat(fileExtFormat);
//      fileExt = formatter.format(new Date(System.currentTimeMillis()));
      fileExt = getTimestampFormat(fileExtFormat);
    }
    log("fileExt = "+ fileExt);
    fileExt = stripUnuseChars(fileExt);
    log("fileExt after strip = "+ fileExt);

    if (newFilename.indexOf(".") > -1)
    {
      newFilename =
        newFilename.substring(0, newFilename.indexOf(".")) +
        fileExt +
        newFilename.substring(newFilename.indexOf("."));
    }
    else
    {
      newFilename = newFilename + fileExt;
    }
    return newFilename;
  }*/

  /*NSL20060405 Not Used
  // Add by Koh Han Sing 12/10/2001
  private String stripUnuseChars(String stringToStrip)
  {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i=0; i < stringToStrip.length(); i++)
    {
      char aChar = stringToStrip.charAt(i);
      if ((aChar >= 'a' && aChar <= 'z') || (aChar >= 'A' && aChar <= 'Z') ||
          (aChar >= '0' && aChar <= '9') || (aChar == '_'))
      {
        stringBuffer.append(aChar);
      }
    }
    return stringBuffer.toString();
  } */

  private synchronized boolean makeDirectory(String dirName)
  {
    File dir = new File(dirName);
    if (!dir.exists())
    {
      log("makeDirectory::Dir = " + dirName);
      dir.mkdirs();
    }
    return dir.exists();
  }

//  /**
//   * Create the parent directories (if do not exist) of the file.
//   *
//   * @param f The file whose parent directories are to be created
//   */
//  private void makeParentDirs(File f)
//  {
//    File parentF = f.getParentFile();
//
//    if (parentF != null && !parentF.exists())
//    {
//      makeParentDirs(parentF);
//      parentF.mkdir();
//    }
//  }

  /*NSL20060405 Not used
  private static String getUniqueFilename(String fullfilename)
  {
    int index = fullfilename.lastIndexOf('.');
    String ext = "";
    String name = "";

    if (index != -1)
    {
      ext = fullfilename.substring(index);
      name = fullfilename.substring(0, index);
    }
    else
      name = fullfilename;

    String fname = name + ext;
    File f = new File(fname);
    for ( int i=1; f.exists(); i++ )
    {
      fname = name + "_" + i + ext;
      f = new File(fname);
    }
    return fname;
  }*/

  private File writeTemp(File input, String filename)
  {
    log("writeTemp : filename = " + filename);
    makeDirectory("temp");
    File tempFile = new File("temp" + File.separatorChar + filename);
    input.renameTo(tempFile);
    return tempFile;
  }


  private String writeFile(File source, String hostDir, String fixFilename,
    boolean isOverwrite)
  {
    File newFile = new File(hostDir + fixFilename);
    if(newFile.exists())
    {
      if(isOverwrite)
      {
        newFile.delete();
        newFile = new File(hostDir + fixFilename);
        source.renameTo(newFile);
        log("Importing " + newFile.getName());
      }
      else
      {
        append(newFile, source);
      }
    }
    else
    {
      source.renameTo(newFile);
    }
    return newFile.getName();
  }

  /*NSL20060405 Not used
  private String writeFile(File source, String hostDir, boolean isOverwrite)
  {
  	//by wx
    //String filename = source.getName();
    String filename = source.getAbsolutePath();
    //File newFile = new File(hostDir + filename);
    File newFile = new File(filename);
    if(newFile.exists())
    {
      if(isOverwrite)
      {
        newFile.delete();
        newFile = new File(hostDir + filename);
        source.renameTo(newFile);
      }
      else
      {
        append(newFile, source);
      }
    }
    else
    {
      source.renameTo(newFile);
    }
    return newFile.getName();
  }*/

  private void append(File original, File newFile)
  {
    String path = original.getPath();
    log("append::Path = " + path);
    String filename = original.getName();
    String fileExt = filename.substring(filename.lastIndexOf('.') + 1,
                     filename.length());
    File temp = writeTemp(original, filename);
    if(fileExt != null && fileExt.equalsIgnoreCase("xml"))
    {
      XMLAppend.appendXML(temp, newFile);
    }
    else
    {
      appendStream(temp, newFile);
    }
    newFile.delete();
    File destination = new File(path);
    temp.renameTo(destination);
  }

  private void appendStream(File original, File newFile)
  {
    try
    {
      FileInputStream ofis = new FileInputStream(original);
      int size = ofis.available();
      byte[] odata = new byte[size];
      ofis.read(odata);
      ofis.close();

      FileInputStream nfis = new FileInputStream(newFile);
      size = nfis.available();
      byte[] ndata = new byte[size];
      nfis.read(ndata);
      nfis.close();
      FileOutputStream ofos = new FileOutputStream(original);
      ofos.write(odata);
      ofos.write(ndata);
      ofos.close();
    }
    catch(Exception e)
    {
    	handleException(APPEND_FAIL, e);
    }
  }

  private void addCommandLine(ArrayList<String> temp, String commandLine,
    String inputFile, String attFilenames)
  {
    StringTokenizer st = new StringTokenizer(commandLine);
    while (st.hasMoreTokens())
    {
      String cmdString = st.nextToken();
      if (cmdString.equals(UDOC))
      {
        cmdString = inputFile;
      }
      if (cmdString.equals(ATTACHMENTS))
      {
        cmdString = attFilenames;
      }

      temp.add(cmdString);
    }
  }

/*  private void sendFailureMail(Exception e, String errMessage)
  {
    log("Exception in Backend Listener", e);
//    if(backendIni.getSmtp() != null)
//    {
//      GridMail.setSMTP(backendIni.getSmtp());
//      IGmail mail = GridMail.createGmail();
//      mail.setSender("khong.hai.wang@gridnode.com");
//      log("Sending email for failure to " + backendIni.getEmail());
//      mail.setRecipient(backendIni.getEmail());
//      mail.setSubject("Export fails");
//      String message = "GridTalk Ver 1.1 Backend " + errMessage;
//      log("Sending email for failure message: " + message);
//      mail.setMessage(message);
//      log("Sending mail");
//      GridMail.send(mail);
//    }
//    else
//    {
//      log("No smtp defined");
//    }
  }
*/

  private void sendFailureMail(int errorCode, ArrayList params)
  {
    if (!smtp.equals("") && !mailReceipient.equals("")) // KHS 15/02/2002
    {
      IGmail mail = GridMail.createGmail();
      mail.setSender("gt_backend@gridnode.com");
      log("Sending email for failure to " + mailReceipient);
      mail.setRecipient(mailReceipient);

      StringBuffer message = new StringBuffer();
      switch (errorCode)
      {
        case UNABLE_TO_INVOKE_CMD_FILE:
          mail.setSubject("Backend Listener Exception : Fails to invoke command file");
          message.append("Backend listener fails to invoke command ");
          message.append(params.get(0).toString());
          message.append(" while processing documents ");
          message.append(params.get(1).toString());
          message.append(" exported from GridTalk via Port ");
          message.append(params.get(2).toString());
          message.append(".");
          message.append(" Please ensure that the command exist in the path ");
          message.append(params.get(3).toString());
          message.append(".");
          log("Sending email for failure message for UNABLE_TO_INVOKE_CMD_FILE");
          break;
				
				//modified by wei xiang
				//all udoc, gdoc, attachment(if any) will be placed in the same folder: the export dir
        case UNABLE_TO_SAVE_DOCUMENT:
          mail.setSubject("Backend Listener Exception : Missing export directory");
          message.append("Backend listener fails to save document ");
          
          for(int i=0; i < params.size()-1; i++) 
          {
          	message.append(params.get(i).toString());
          }
          message.append(" to export directory ");
          message.append(params.get(params.size()-1).toString());
          message.append(".");
          message.append(" Please ensure sufficient right is granted to allow");
          message.append(" the system to create the host directory.");
          log("Sending email for failure message for UNABLE_TO_SAVE_FILE");
          break;
				
        case UNABLE_TO_SAVE_ATTACHMENT:
          mail.setSubject("Backend Listener Exception : Missing attachment directory");
          message.append("Backend listener fails to save attachment to attachment directory ");
          message.append(params.get(0).toString());
          message.append(".");
          message.append(" Please ensure sufficient right is granted to allow");
          message.append(" the system to create the attachment directory.");
          log("Sending email for failure message for UNABLE_TO_SAVE_ATTACHMENT");
          break;

        default:
          //params contain exception
          mail.setSubject("Backend export fail");
          message.append("GridTalk Ver 2.1 Backend ");
          message.append(((Exception)params.get(0)).getMessage());
          log("Sending email for failure message " + message);
          break;
      }
      mail.setMessage(message.toString());
      log("Sending mail");
      GridMail.send(mail);
    }
    else
    {
      log("Missing email settings: SMTP = \"" + smtp + "\"" +
          " email = \"" + mailReceipient + "\"");
    }
  }

  public static synchronized String getTimestampFormat(String format)
  {
    try
    {
      Thread.sleep(5);
    }
    catch (InterruptedException ex)
    {
    }
    long currentTime1 = System.currentTimeMillis();
    long currentTime2 = System.currentTimeMillis();
    while (currentTime1 == currentTime2)
    {
      currentTime2 = System.currentTimeMillis();
    }
    SimpleDateFormat df = new SimpleDateFormat(format);
    return df.format(new Date(currentTime2));
  }

  private void log(String msg, Exception e)
  {
    Log.err(CATEGORY, msg, e);
//    System.out.println("[BEAPIHandler] " + msg + " Ex msg = " + e.getMessage());
  }

  private void log(String msg)
  {
    Log.log(CATEGORY, msg);
//    System.out.println("[BEAPIHandler] " + msg);
  }
  
  /*NSL20060405 Not used
  //added by wei xiang
  private String dash(String fileExtValue) {
  	fileExtValue +="";
  	if(fileExtValue!=null && fileExtValue.compareTo("")==0) {
  		return "";
  	}
  	else {
  		return "_";
  	}
  }*/
  
/**
   * Get the folder name for the exportDir. Based on the file grouping option in the configuration
   * of the port, we will return appropriate value.
   * If the file grouping option is not FLAT, folder name for export dir will be based on
   * the passed in exporteFilename without the filename extension.
   * @param fileGrouping The file grouping option
   * @param exportedFilename The exported filename of udoc.
   * @return Name of the export directory
   */
  private String getExportSubDir(Integer fileGrouping, String exportedFilename)
  {
  	String exportSubDir = "";
  	
  	if(IPort.FILE_GROUPING_OPTION_ATTACHMENT_GDOC.equals(fileGrouping)
  			|| IPort.FILE_GROUPING_OPTION_GROUP_ALL.equals(fileGrouping))
  	{
  		exportSubDir = getFilenameWithoutFilenameExt(exportedFilename);
  	}
  	
  	return exportSubDir;
  }
  
  /**
   * Return the exported filename of the udoc. We will use either the user specify
   * filename(define in the port) or udoc filename. File Extension value will be appended
   * on the filename if user has specify.
   * @param userDefineFilename
   * @param udocFilename
   * @param fileExtensionValue
   * @param isAddFileExt
   * @return export udoc filename
   */
  private String getExportedFilename(String userDefineFilename, String udocFilename, String fileExtensionValue, 
                                     boolean isAddFileExt)
  {
  	String exportedFilename = "";
  	
  	if(!isAddFileExt)
  	{
  		fileExtensionValue = "";
  	}
  	
  	//NSL20060405 Remove dash '_' between filename and file extension value
  	if(userDefineFilename == null || userDefineFilename.equals(""))
  	{
  		exportedFilename = getFilenameWithoutFilenameExt(udocFilename)+ //dash(fileExtensionValue)+
  		                       fileExtensionValue+getFilenameExtension(udocFilename);
  	}
  	else
  	{
  		exportedFilename = getFilenameWithoutFilenameExt(userDefineFilename)+ //dash(fileExtensionValue)+
  		                       fileExtensionValue+getFilenameExtension(userDefineFilename);
  	}
  	return exportedFilename;
  }

  private String getFilenameExtension(String filename)
  {
  	if(filename!= null && filename.lastIndexOf(".") > -1)
  	{
  		return filename.substring(filename.lastIndexOf(".")); 
  	}
  	return "";
  }

  private String getFilenameWithoutFilenameExt(String filename)
  {
  	if(filename != null && filename.lastIndexOf(".") > -1)
  	{
  		return filename.substring(0, filename.lastIndexOf("."));
  	}
  	return filename;
  }
  
/**
	 * Create the sub dir based on user specify export filename or udoc filename depend on user has selected
	 * the different filename checkbox or not.
	 * The subdirectory will be used to group either the gdoc, udoc and attachment or gdoc and attachments
	 * @param isDiffFileName
	 * @param isAddFileExt
	 * @param isOverWrite
	 * @param exportDir
	 * @param exportSubDir
	 * @param exportFileName
	 * @return the subDirectory name and the filename.
	 */
	private String createSubDirectory(boolean isExportGdoc, String exportDir, String exportSubDir)
	{
		if(! isExportGdoc)
  	{
  		return "";
  	}
		
		boolean dirExists = (new File(exportDir, exportSubDir)).exists();
		
		if(!dirExists)
		{
			makeDirectory(exportDir+"/"+exportSubDir);
		}
		return exportSubDir;
	}
	
	private String getUDocDestFolderPath(Integer fileGrouping, String exportDir, String exportSubDir)
	{
			String exportFullPath = exportDir + "/";
			
    	if(IPort.FILE_GROUPING_OPTION_GROUP_ALL.equals(fileGrouping))
    	{
    		exportFullPath = exportDir+ "/" +exportSubDir+ "/";
    	}
      makeDirectory(exportFullPath);
    	
      log("[BEAPIHandler.getUDocDestFolderPath] udoc dest folder path "+ exportFullPath);
      
      return exportFullPath;
	}
	
	private String getGDOCDestFolderPath(Integer fileGrouping, String exportDir, String exportSubDir)
	{
		String exportFolderPath = exportDir + "/" + exportSubDir + "/";
		if(IPort.FILE_GROUPING_OPTION_FLAT.equals(fileGrouping))
		{
			exportFolderPath = exportDir+"/";
		}

		makeDirectory(exportFolderPath);
		log("[BEAPIHandler.getGDOCDestFolderPath] gdoc dest folder path "+exportFolderPath);
		
		return exportFolderPath;
	}
	
	private String getExportAttachmentFolderPath(Integer fileGrouping, String exportDir, String exportSubDir,
	                                             boolean isContainAttachment)
	{
		if(! isContainAttachment)
		{
			return "";
		}
		
		String exportAttachmentFolderPath = exportDir + "/" + exportSubDir + "/";
		if(IPort.FILE_GROUPING_OPTION_FLAT.equals(fileGrouping))
		{
			exportAttachmentFolderPath = exportDir + "/";
		}
		
		makeDirectory(exportAttachmentFolderPath);
		log("[BEAPIHandler.getExportAttachmentFolderPath] attachment folder path "+exportAttachmentFolderPath);
		
		return exportAttachmentFolderPath;
	}
}