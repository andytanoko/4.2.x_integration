/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDocumentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2002    Koh Han Sing        Created
 * May 07 2003    Neo Sok Lay         Raise alerts at points when export cannot
 *                                    continue: Unknown port, port connect fail,
 *                                    file processing errors, other general errors.
 * May 27 2003    Jagadeesh           Added : To generate sequential running no.
 * Sep 25 2003    Koh Han Sing        Regulate the number of export at any one
 *                                    time.
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * Jun 07 2004    Neo Sok Lay         Modified: prepareFilesToExport() and 
 *                                    getOrgFileNames()to only process the attachments 
 *                                    if attachments have already linked to gdoc.
 * Aug 23 2005    Tam Wei Xiang       Commented 'portInfo.add(port.getAttachmentDir())',
 *                                              'String attachDir = port.getAttachmentDir();'
 *                                    Class Port no longer contain instance variable 
 *                                    AttachmentDir, See class Port for detail.
 * Nov 09 2005    Neo Sok Lay         When export to local, if the port hostDir is not absolute,
 *                                    export the files relative to the SystemUtil.workingDir.    
 * Mar 01 2006    Tam Wei Xiang       Add in fileGrouping option in port configuration.
 * Apr 05 2006    Neo Sok Lay         GNDB00026789: Remove '_' between filename and filename extension
 */
package com.gridnode.gtas.server.document.helpers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.gtas.server.backend.model.IPort;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.document.exceptions.ExportException;
import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.model.Activity;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.pdip.framework.util.TimeUtil;

public class ExportDocumentHelper
  implements IDocProcessingErrorConstants

{
  public static final String EXIT_TO_PORT = "Exit to Port";
  public static final int MAX_EXPORTS = 20;

  private static final String CLASS_NAME = ExportDocumentHelper.class.getName();
  private static int _currentExports = 0;

  protected transient PartnerFunctionFailure _failure = null;

  public GridDocument prepareExportInfo(GridDocument gdoc, Long portUid)
  {
    Logger.debug("[ExportDocumentHelper.prepareExportInfo] Enter");

    try
    {
      gdoc.setPortUid(portUid);

      Port port = BackendDelegate.getManager().findPort(portUid);
      gdoc.setPortName(port.getName());
    }
    catch (Throwable t)
    {
      Logger.warn("[ExportDocumentHelper.prepareExportInfo] Error", t);
    }

    return gdoc;
  }

  public GridDocument doExport(GridDocument gdoc) throws Throwable
  {
    Token token = TokenManager.getInstance().getToken();

    Long portUid = gdoc.getPortUid();

    Port port  = null;

    try
    {
      port = findPort(portUid);

      Activity newActivity = new Activity();
      newActivity.setActivityType(EXIT_TO_PORT);
      newActivity.setDescription(EXIT_TO_PORT+" "+gdoc.getPortName());
      newActivity.setDateTime(new Date(TimeUtil.localToUtc()));
      gdoc.addActivity(newActivity);

      if (port.isRfc().booleanValue())
      {
        gdoc = exportViaRFC(gdoc, port);
      }
      else
      {
        gdoc = exportLocal(gdoc, port);
      }

      AlertDelegate.raiseDocExportedAlert(gdoc, port);
    }
    catch (PartnerFunctionFailure fail)
    {
      _failure = fail;
    }
    catch (Throwable t)
    {
      _failure = new PartnerFunctionFailure(
                       TYPE_EXPORT_DOC_FAILURE,
                       REASON_GENERAL_ERROR,
                       t);
    }

    if (_failure != null)
    {
      _failure.raiseAlert(gdoc);
      throw _failure.getException();
    }

    TokenManager.getInstance().releaseToken(token);
    return gdoc;
  }

  private Port findPort(Long portUid) throws PartnerFunctionFailure
  {
    Port port  = null;

    if (portUid == null)
    {
      throw new PartnerFunctionFailure(
                  TYPE_EXPORT_DOC_FAILURE,
                  REASON_UNKNOWN_DESTINATION,
                  NO_EXPORT_PORT);
    }

    try
    {
      port = BackendDelegate.getManager().findPort(portUid);
      if (port == null)
        throw new ExportException("Unknown Port");
    }
    catch (Throwable t)
    {
      throw new PartnerFunctionFailure(
                  TYPE_EXPORT_DOC_FAILURE,
                  REASON_UNKNOWN_PORT,
                  t);
    }

    return port;
  }

  protected GridDocument exportViaRFC(GridDocument gdoc, Port port)
    throws Exception, PartnerFunctionFailure
  {
    Rfc rfc = port.getRfc();

    if (rfc == null)
    {
      throw new ExportException("Rfc is null");
    }

    String ip = rfc.getHost();
    int portNum = rfc.getPortNumber().intValue();

    try
    {
      Logger.log("[ExportDocumentHelper.exportViaRFC] Exporting to backend via RFC.");

      Logger.log("[ExportDocumentHelper.exportViaRFC] Invoking transport to export to backend"
          + " at IP : " + ip + " Port Number : " + portNum);

      Vector portInfo = new Vector();
      portInfo.add(ip);
      portInfo.add(rfc.getPortNumber());
      portInfo.add(port.getHostDir());
      portInfo.add(port.getFileName());
      portInfo.add(port.isOverwrite());
      portInfo.add(rfc.getCommandFileDir());
      portInfo.add(rfc.getCommandFile());
      portInfo.add(rfc.getCommandLine());

      portInfo.add(port.isAddFileExt());
      if (port.isAddFileExt().booleanValue())
      {
        Integer fileExtType = port.getFileExtType();
        if (fileExtType.equals(Port.DATE_TIME)) 
        {
          String fileExtFormat = port.getFileExtValue();
          portInfo.add(fileExtFormat);
        }
        else
        {
          portInfo.add("");
        }
        String fileExt = getFileExt(port, gdoc);
        portInfo.add(fileExt);
        Logger.log("fileExt = " + fileExt);
      }
      else
      {
        //to ensure backward compatibility, we have to fill in the blanks
        portInfo.add("");
        portInfo.add("");
      }

      //File udocFile = FileHelper.getUdocFile(gdoc);
      String udocFilename = gdoc.getUdocFilename();
      File udocFile = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC,
                                       gdoc.getFolder()+File.separator,
                                       udocFilename);
      File[] exportFiles = new File[] {udocFile};

      File gdocFile = null;
      portInfo.add("");   //email recipient
      portInfo.add(port.isExportGdoc());
      //Comment by Wei Xiang
      //portInfo.add(port.getAttachmentDir());
      portInfo.add(new Integer(exportFiles.length));
      if (port.isExportGdoc().booleanValue())
      {
        gdocFile = FileHelper.getGdocFile(gdoc);
        exportFiles = new File[] {udocFile, gdocFile};
      }
      portInfo.add(getOrgFilenameList(gdoc));
      
      //TWX 02032006 Added in file grouping option
      portInfo.add(port.getFileGrouping());
      
      File[] filesToExport = prepareFilesToExport(gdoc, port, exportFiles);

      if (sendToBackend(portInfo, filesToExport))
      {
        gdoc.setExported(Boolean.TRUE);
        gdoc.setDateTimeExport(new Date(TimeUtil.localToUtc()));
      }
      else
      {
        throw new PartnerFunctionFailure(TYPE_EXPORT_DOC_FAILURE,
                                         REASON_CONNECT_PORT_FAIL,
                                         ExportException.portConnectFail(port));
        /*
        if (filesToExport.length > exportFiles.length)
        {
          // attachments did not export out
          //unregisterExportAttachments(gDoc, port);
        }
        */
      }
    }
    catch(Exception ex)
    {
      Logger.warn("[ExportDocumentHelper.exportEntity] Error exporting documents!" +
          " File processing errors!", ex);
      throw new PartnerFunctionFailure(TYPE_EXPORT_DOC_FAILURE,
                                       REASON_FILE_PROCESSING_ERROR,
                                       ex);
    }
    return gdoc;
  }

  //01032006 TWX add in file grouping option. The following method also be refactored
  protected GridDocument exportLocal(GridDocument gdoc, Port port)
    throws Exception, PartnerFunctionFailure
  {
    String udocFilename = gdoc.getUdocFilename();
    File udoc = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC,
                                 gdoc.getFolder()+File.separator,
                                 udocFilename);
    String fileName = udoc.getName(); //different between udocFilename ??
    //String fullPathName = udoc.getAbsolutePath();
 
    String exportDir = port.getHostDir();
    
    //To cater for previous port configuration.
		Integer fileGroupingOption = port.getFileGrouping()== null || port.getFileGrouping()==0 ? 
		                                                    IPort.FILE_GROUPING_OPTION_ATTACHMENT_GDOC : 
		                                                    	                          port.getFileGrouping();
		
    String exportFileName = getExportedFilename(port, gdoc);
		String exportSubDir = getExportSubDir(port, exportFileName);
		
    //NSL20051109 if exportDir is not absolute, use it as relative to the workingDir
    if (!new File(exportDir).isAbsolute())
    {
    	exportDir = new File(SystemUtil.getWorkingDir(), exportDir).getCanonicalPath();
    }
    
    //create the exportDir
    FileHelper.createDirectory(new File(exportDir));
    
    /*
    if(IPort.FILE_GROUPING_OPTION_ATTACHMENT_GDOC.equals(fileGroupingOption) || 
    		 IPort.FILE_GROUPING_OPTION_GROUP_ALL.equals(fileGroupingOption))
    {
    	createSubDirectory(port, exportDir, exportSubDir);
    } */
    
    try
    {
    	//export UDOC
    	gdoc.setExportedUdocFullPath(
    	                             exportUDOC(port, gdoc, exportDir, exportSubDir, 
                                            exportFileName, udoc)
                                   );
    	Logger.log("[ExportDocumentHelper ] exportSubDir is "+exportSubDir);
    	//export GDOC
    	exportGDOC(port, gdoc, exportDir, exportSubDir);
    
    	//export Attachment
    	File[] filesToExport = prepareFilesToExport(gdoc, port, new File[] {});
    	ArrayList fileNames = getOrgFilenameList(gdoc);
    	exportAttachment(port, filesToExport, fileNames, exportDir, exportSubDir);
    
    
    	gdoc.setExported(Boolean.TRUE);
    	gdoc.setDateTimeExport(new Date(TimeUtil.localToUtc()));
    }
    catch (Exception ex)
    {
      Logger.warn("[ExportDocumentHelper.exportLocal] Error exporting " +
                 fileName + " to " + exportDir + "/" + exportFileName, ex);

      throw new PartnerFunctionFailure(TYPE_EXPORT_DOC_FAILURE,
                                       REASON_FILE_PROCESSING_ERROR,
                                       ex);
    }
    return gdoc;
  }

  protected String getFileExt(Port port, GridDocument gDoc) throws Exception
  {
    String fileExt = "";
    String fileExtValue = port.getFileExtValue();
    Integer fileExtType = port.getFileExtType();
    if (fileExtType.equals(Port.DATE_TIME))
    {
      fileExt = getTimestampFormat(fileExtValue);
    }
    else if(fileExtType.equals(Port.SEQ_RUNNING_NUM))
    {
      try
      {
        //Genterate sequential no as file extension.
        Logger.debug("["+CLASS_NAME+"][getFileExt()]"+"Port UID='"+port.getUId()+"'");

        fileExt = BackendDelegate.getManager().getNextSeqRunningNo(
          new Long(port.getUId()));

        Logger.debug("["+CLASS_NAME+"][getFileExt()]"+"FileExt Generated ='"+fileExt+"'");
      }
      catch(Throwable th)
      {
        throw new Exception(th.getMessage());
      }
    }
    else
    {
      Object object = gDoc.getFieldValue(new Integer(fileExtValue));
      if (object != null)
      {
        fileExt = (object).toString();
      }
      else
      {
        Logger.log("[ExportDocumentHelper.getFileExt] GridDocument field " + fileExtValue + " is null");
      }
    }
    fileExt = stripUnuseChars(fileExt);
    return fileExt;
  }

  protected String stripUnuseChars(String stringToStrip)
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
  }

  protected ArrayList getOrgFilenameList(GridDocument gDoc)
    throws Exception
  {
    ArrayList<String> orgFilenames = new ArrayList<String>();
    if (gDoc.getAttachments() != null && gDoc.isAttachmentLinkUpdated().booleanValue())
    {
      List attachmentList = gDoc.getAttachments();
      for (Iterator i = attachmentList.iterator(); i.hasNext();)
      {
        Long attUID = new Long(i.next().toString());
        Attachment attachment =
          (Attachment)AttachmentEntityHandler.getInstance().getEntityByKeyForReadOnly(attUID);
        if (attachment != null)
        {
          orgFilenames.add(attachment.getOriginalFilename());
        }
        else
        {
          throw new ExportException("Unable to find attachment with Uid "+attUID);
        }
      }
    }
    return orgFilenames;
  }

  /**
   * Prepare the file array to be sent to the other GridNode, check whether
   * to include the attachments. It will only check the first attachment,
   * if no other document that contains the same set of attachments has started
   * exporting them, it will register it so that the other documents will not
   * have to reexport the same set of attachments.
   *
   * @param gdoc  the griddocument to be exported
   * @param port    the port to export to
   * @param files   the file array of griddocument and user document
   * @returns       the file array with the attachments if they have not
   *                been exported
   */
  protected File[] prepareFilesToExport(GridDocument gdoc, Port port, File[] files)
    throws Exception
  {
    //only process the attachments if attachments have already linked to gdoc
    if (gdoc.hasAttachment().booleanValue() && gdoc.isAttachmentLinkUpdated().booleanValue())
    {
      Logger.debug("[ExportDocumentHelper.prepareFilesToExport] hasAttachment");
      ArrayList<File> fileList = new ArrayList<File>();
      for (int j = 0; j < files.length; j++)
      {
        fileList.add(files[j]);
      }

      List attachmentUids = gdoc.getAttachments();
      if (attachmentUids != null && !attachmentUids.isEmpty())
      {
        for (Iterator i = attachmentUids.iterator(); i.hasNext(); )
        {
          Long attachmentUid = new Long(i.next().toString());
          Attachment attachment = null;
          try
          {
            attachment = (Attachment)AttachmentEntityHandler.getInstance().getEntityByKey(attachmentUid);
          }
          catch (Throwable ex)
          {
            throw new ExportException("Unable to retrieve attachment "+attachmentUid+" from database", ex);
          }

          try
          {
            File attFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT,
                                            attachment.getFilename());
            fileList.add(attFile);
          }
          catch (Exception ex)
          {
            throw new ExportException("Unable to find attachment file "+attachment.getFilename()+" in attachment directory", ex);
          }
        }
      }
      files = convertToFileArray(fileList);
    }
    Logger.log("[ExportDocumentHelper.PrepareFilesToExport] returning files.size() = " + files.length);
    return files;
  }

  protected File[] convertToFileArray(ArrayList filesList)
  {
    File[] filesToSend = new File[filesList.size()];
    for (int i = 0; i < filesList.size(); i++)
    {
      filesToSend[i] = (File)filesList.get(i);
    }
    return filesToSend;
  }

  protected boolean sendToBackend(Vector portInfo, File[] filesToExport)
  {
    boolean success = true;
    try
    {
      success = BackendDelegate.getManager().sendFile(portInfo, filesToExport);
    }
    catch (Exception ex)
    {
      Logger.warn("[ExportDocumentHelper.sendToBackend] Send to backend failed", ex);
      return false;
    }
    return success;
  }

  protected synchronized String getTimestampFormat(String format)
  {
    long currentTime1 = System.currentTimeMillis();
    long currentTime2 = System.currentTimeMillis();
    while (currentTime1 == currentTime2)
    {
      currentTime2 = System.currentTimeMillis();
    }
    SimpleDateFormat df = new SimpleDateFormat(format);
    return df.format(new Date(currentTime2));
  }

  protected synchronized int getCurrentExports()
  {
    return _currentExports;
  }

  protected synchronized void increaseCurrentExports()
  {
    _currentExports++;
  }

  protected synchronized void decreaseCurrentExports()
  {
    _currentExports--;
  }
  
  /*NSL20060405 Not used anymore
  //added by wei xiang
  private String dash(String fileNameExt) {
  	fileNameExt +="";
  	if(fileNameExt!=null && fileNameExt.compareTo("")==0) {
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
   * @param port
   * @param exportedFilename The exported filename of udoc.
   * @return The name of the folder to export file to. 
   */
  private String getExportSubDir(Port port, String exportedFilename)
  {
  	String exportSubDir = "";
  	
  	if(IPort.FILE_GROUPING_OPTION_ATTACHMENT_GDOC.equals(port.getFileGrouping())
  			|| IPort.FILE_GROUPING_OPTION_GROUP_ALL.equals(port.getFileGrouping()))
  	{
  		exportSubDir = getFilenameWithoutFilenameExt(exportedFilename);
  	}
  	
  	return exportSubDir;
  }
  
  /**
   * Return the exported filename of the udoc. We will use either the user specify
   * filename(define in the port) or udoc filename. File Extension value will be appended
   * on the filename if user has specify.
   * @param port
   * @return Name of the udoc file in the exported directory
   */
  private String getExportedFilename(Port port, GridDocument gdoc)
  	throws Exception
  {
  	String exportedFilename = "";
  	String userDefineFilename = port.getFileName();
  	
  	String fileExtensionValue = getFileExtensionValue(port, gdoc);
  	
  	//NSL20060405 Remove dash '_' between filename and extension value
  	if(userDefineFilename == null || userDefineFilename.equals(""))
  	{
  		String udocFilename = gdoc.getUdocFilename();
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
  
  private String getFileExtensionValue(Port port, GridDocument gdoc)
  	throws Exception
  {
  	if(port.isAddFileExt())
  	{
  		return getFileExt(port, gdoc);
  	}
  	return "";
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
	 * TWX
	 * If the file object is a directory, it will delete all the files and subfolder within it. Included the 
	 * passed in file object as well.
	 * If the file object is a file, the file will be deleted.
	 * @param fileToDelete a folder or a file
	 */
	private void deleteAllFiles(File fileToDelete)
		throws Exception
	{
		//The File object is a file
		if(fileToDelete.isFile())
		{
			deleteFile(fileToDelete);
			return;
		}
		
		//File object is a directory
		File[] f = fileToDelete.listFiles();
		if(f.length == 0)
		{
			deleteFile(fileToDelete);
			return;
		}
		else
		{
			//directory contain more than 1 file
			for(int i =0; i< f.length; i++)
			{
				deleteAllFiles(f[i]);
			}
			deleteFile(fileToDelete);
		}
	}
	
	//TWX
	private void deleteFile(File file)
		throws Exception
	{
		boolean isDeleted = file.delete();
		if(!isDeleted)
		{
			throw new ApplicationException("[ExportDocumentHelper.deleteFile] Error deleting file "+file.getAbsolutePath());
		}
		Logger.log("[ExportDocumentHelper.deleteFile] File "+file.getAbsolutePath()+" deleted.");
	}
	
	/**
	 * Create the sub dir based on user specify export filename or udoc filename depend on user has selected
	 * the different filename checkbox or not.
	 * The subdirectory will be used to group either the gdoc, udoc and attachment or gdoc and attachments
	 * @param port
	 * @param exportDir
	 * @param exportSubDir
	 */
	private void createSubDirectory(Port port, String exportDir, String exportSubDir)
		throws Exception
	{
		boolean dirExists = (new File(exportDir, exportSubDir)).exists();
		
		if(!dirExists)
		{
			FileHelper.createDirectory(new File(exportDir, exportSubDir));
		}
	}
	
	private String exportUDOC(Port port, GridDocument gdoc, String exportDir, String exportSubDir, String exportFileName,
	                          File udoc)
		throws Exception
	{
			String fileName = udoc.getName();
			String fullPathName = udoc.getAbsolutePath();
			String exportFullPath = exportDir + "/" + exportFileName;
			String exportFolderPath = exportDir+ "/";
			
    	if(IPort.FILE_GROUPING_OPTION_GROUP_ALL.equals(port.getFileGrouping()))
    	{
    		exportFullPath = exportDir+ "/" +exportSubDir+ "/" + exportFileName;
    		exportFolderPath = exportDir + "/" + exportSubDir +"/";
    	}
    	
    	File directory = new File(exportFolderPath);
    	if(! directory.exists())
    	{
    		FileHelper.createDirectory(directory);
    	}
    	
      Logger.log("[ExportDocumentHelper.exportLocal] Exporting " + fileName +
          " to " + exportFullPath);

      if (!port.isOverwrite() && (new File(exportFullPath).exists()))
      {
        Logger.debug("[ExportDocumentHelper.exportLocal] Export append " + fileName +
            " to " + exportFullPath);

        if (gdoc.getUdocFileType().equals("xml"))
        {
          String tempPathname = System.getProperty("java.io.tmpdir") + "/" + exportFileName;
          File oldFile = new File(exportFullPath);
          File tempFile = new File(tempPathname);
          File newFile = new File(fullPathName);
          oldFile.renameTo(tempFile);
          Logger.debug("[ExportDocumentHelper.exportLocal] xml, Export append " + fileName +
                      " to " + exportFullPath);
          XMLDelegate.getManager().appendXML(tempFile.getAbsolutePath(),
                                             newFile.getAbsolutePath());
          tempFile.renameTo(oldFile);                                             
        }
        else
        {
          Logger.debug("[ExportDocumentHelper.exportLocal] normal, Export append " + fileName +
                      " to " + exportFullPath);
          FileAppender.appendFile(fullPathName,exportFullPath);
        }
      }
      else // just do a copy , replace the existing file
      {
        Logger.log("[ExportDocumentHelper.exportLocal] Export overwrite " + fileName +
            " to " + exportFullPath);   
        FileHelper.copy(fullPathName, exportFullPath);
      }
      
      return exportFullPath;
	}
	
	private void exportGDOC(Port port, GridDocument gdoc, String exportDir, String exportSubDir)
		throws Exception
	{
		boolean isExportGDOC = port.isExportGdoc();
		if(!isExportGDOC)
		{
			return;
		}
		String exportFolderPath = exportDir + "/" + exportSubDir + "/";
		if(IPort.FILE_GROUPING_OPTION_FLAT.equals(port.getFileGrouping()))
		{
			exportFolderPath = exportDir+"/";
		}
		
		File directory = new File(exportFolderPath);
		if(! directory.exists())
		{
			FileHelper.createDirectory(directory);
		}
		
		//Get the actual gdoc file
		File gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC,
                                     gdoc.getFolder()+File.separator,
                                     gdoc.getGdocFilename());
		
		saveaFile(gdocFile, exportFolderPath, gdocFile.getName(), port.isOverwrite());
	}
	
	private void exportAttachment(Port port, File[] filesToExport, ArrayList fileNames, String exportDir, 
	                              String exportSubDir)
		throws Exception
	{
		if(filesToExport == null || filesToExport.length == 0)
		{
			return;
		}
		
		String exportAttachmentFolderPath = exportDir + "/" + exportSubDir + "/";
		if(IPort.FILE_GROUPING_OPTION_FLAT.equals(port.getFileGrouping()))
		{
			exportAttachmentFolderPath = exportDir + "/";
		}
		
		File directory = new File(exportAttachmentFolderPath);
		if(! directory.exists())
		{
			FileHelper.createDirectory(directory);
		}
		
		//	copy attachment
    for (int i = 0; filesToExport!= null && i < filesToExport.length; i++)
    {
        File orgAttFile = filesToExport[i];
        
        saveaFile(orgAttFile, exportAttachmentFolderPath, (String)fileNames.get(i), 
                    port.isOverwrite());
    }
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
	private void saveaFile(File source, String exportDir,
	                         String filename, boolean isOverwrite)
		throws Exception
	{
		File newFile = new File(exportDir + filename);
	  if (newFile.exists() && ! isOverwrite)
	  {
	  	String currentTimestamp = getTimestampFormat("ddMMyyHHmmssSSS");
	    String originFilename = source.getName();
	    
	    originFilename = getFilenameWithoutFilenameExt(originFilename)+currentTimestamp+
                           getFilenameExtension(originFilename);
	    
	    newFile = new File(exportDir+originFilename);
	  }
	  
	  FileHelper.copy(source.getAbsolutePath(), newFile.getAbsolutePath());

	}
}
