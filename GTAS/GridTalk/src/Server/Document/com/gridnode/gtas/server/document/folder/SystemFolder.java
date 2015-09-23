/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SystemFolder.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Koh Han Sing        Created
 * Mar 27 2003    Koh Han Sing        Delete temp files
 * May 22 2003    Koh Han Sing        Set UdocFullPath
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * Feb 03 2004    Koh Han Sing        Store name of temp udoc file in different
 *                                    field.
 * Mar 26 2004    Neo Sok Lay         Change the udoc file extension during save
 *                                    in case the Mapping rule change the file extension.
 * Nov 16 2005    Tam Wei Xiang       Modified method doEnter(), doExist()   
 * Mar 20 2006    Tam Wei Xiang       Modified method extractUdocElementInfo()
 * Mar 12 2007    Neo Sok Lay         Use UUID for unique filename.
 * May 15 2007    Tam Wei Xiang       Trigger Partner function failure alert if
 *                                    save in folder failed.
 * Apr 04 2008    Tam Wei Xiang       #12 Modified doExist() to trigger partner function failure alert
 *                                    if udoc ele extraction failed.                                
 * Oct 13 2008    Tam WeiXiang        #86 - Trigger Partner function failure if the extraction
 *                                    of udoc content failed in doExit(...)                                                                   
 */
package com.gridnode.gtas.server.document.folder;

import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.helpers.*;
import com.gridnode.gtas.server.document.model.Activity;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.TriggerInfo;
import com.gridnode.gtas.server.mapper.helpers.MapperDelegate;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

public abstract class SystemFolder
{
  public static final String EXIT_TO_FOLDER = "Exit to System Folder";
  public static final String EXIT_FROM_FOLDER = "Exit from System Folder";
  
  //TWX: 16 NOV 2005
  protected final String SIGNAL_MSG_RN_ACK = "RN_ACK";
	protected final String SIGNAL_MSG_RN_EXCEPTION = "RN_EXCEPTION";
  
  public GridDocument doEnter(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[SystemFolder.doEnter] Start");

    if (gdoc.getFolder() != null)
    {
      gdoc.setSrcFolder(gdoc.getFolder());
    }

    String tempUdocFilename = gdoc.getTempUdocFilename();
    String udocFilename = gdoc.getUdocFilename();
    if (tempUdocFilename.length() == 0)
    {
      File udocFile = FileHelper.getUdocFile(gdoc);
      tempUdocFilename = FileHelper.copyToTemp(udocFile.getAbsolutePath());
      gdoc.setTempUdocFilename(tempUdocFilename);
    }

    gdoc = doDefaultHeaderTransform(gdoc);
    
    //TWX: 15 NOV 2005
    //extract out the values of the elements from udoc and put inside gdoc
    extractUdocElementInfo(gdoc, FileHelper.getUdocFile(gdoc));
    
    gdoc.setFolder(getFolderName());

    Activity newActivity = new Activity();
    newActivity.setActivityType(EXIT_TO_FOLDER);
    newActivity.setDescription(EXIT_TO_FOLDER+" "+getFolderName());
    newActivity.setDateTime(new Date(TimeUtil.localToUtc()));
    gdoc.addActivity(newActivity);

    Logger.debug("[SystemFolder.doEnter] End");
    return gdoc;
  }

  public GridDocument doExit(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[SystemFolder.doExit] Start");
    
    
    
    try
    {
    	//TWX: 15 NOV 2005
        //check if the udoc file is in xml format
        //extract out the values of the elements from udoc and put inside gdoc
    	extractUdocElementInfo(gdoc, FileHelper.getUdocFile(gdoc));
    }
    catch(Exception ex)
    {
    	//TWX #12 Trigger the partner function failure alert in case the udoc ele extraction failed.
    	
    	String folderName = getFolderName();
    	if(folderName != null && (ImportFolder.FOLDER_NAME.equals(folderName) || InboundFolder.FOLDER_NAME.equals(folderName)))
    	{
    		String failureType = "";
    		if(ImportFolder.FOLDER_NAME.equals(folderName))
    		{
    			failureType = PartnerFunctionFailure.TYPE_IMPORT_DOC_FAILURE;
    		}
    		else
    		{
    			failureType = PartnerFunctionFailure.TYPE_RECEIVE_DOC_FAILURE;
    		}
    		
    		PartnerFunctionFailure pfx = new PartnerFunctionFailure(gdoc, failureType, PartnerFunctionFailure.REASON_GENERAL_ERROR, ex);
    		pfx.raiseAlert();
    	}
    	throw ex;
    }
    
    Activity newActivity = new Activity();
    newActivity.setActivityType(EXIT_FROM_FOLDER);
    newActivity.setDescription(EXIT_FROM_FOLDER+" "+getFolderName());
    newActivity.setDateTime(new Date(TimeUtil.localToUtc()));
    gdoc.addActivity(newActivity);

    Logger.debug("[SystemFolder.doExit] End");
    return gdoc;
  }

  public void doActivityBegin(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[SystemFolder.doActivityBegin] Start");
    PartnerProcessDelegate.triggerPartnerFunction(gdoc, getTriggerInfo(gdoc));
    Logger.debug("[SystemFolder.doActivityBegin] End");
  }

  protected GridDocument doDefaultHeaderTransform(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[SystemFolder.doDefaultHeaderTransform] Start");

    Long mappingRuleUID = getDefaultHeaderMappingRuleUID(gdoc);
    gdoc = (GridDocument)MapperDelegate.doHeaderTransform(gdoc, mappingRuleUID);

    String refUdocFilename = gdoc.getRefUdocFilename();
    if (refUdocFilename.indexOf(":") > -1)
    {
      refUdocFilename = refUdocFilename.substring(0, refUdocFilename.indexOf(":"));
      gdoc.setRefUdocFilename(refUdocFilename);
    }

    Logger.debug("[SystemFolder.doDefaultHeaderTransform] End");
    return gdoc;
  }

  public GridDocument createHeader(GridDocument gdoc)
    throws Throwable
  {
    return createHeader(gdoc, true);
  }

  public GridDocument createHeader(GridDocument gdoc, boolean updateGDocId)
    throws Throwable
  {
    gdoc = (GridDocument)GridDocumentEntityHandler.getInstance().createEntity(gdoc, updateGDocId);
    String gdocFilename = getGdocFilename(gdoc);
    gdoc.setGdocFilename(gdocFilename);
    GridDocumentEntityHandler.getInstance().update(gdoc);
    File tempGdoc = File.createTempFile(generateRandomFileName(), ".xml");
    String gdocFullPath = tempGdoc.getAbsolutePath();
    gdoc.serialize(gdocFullPath);

    FileInputStream fis = new FileInputStream(tempGdoc);  //KHS20030327
    FileUtil.create(IDocumentPathConfig.PATH_GDOC,
                    gdoc.getFolder()+File.separator,
                    gdocFilename,
                    fis);                                 //KHS20030327
    Logger.debug("[SystemFolder.createHeader] GridDocument "+gdocFullPath+
                                              " created");
//    gdoc.setUdocFilename(newUdocFilename+":"+tempUdocFilename);
    fis.close();        //KHS20030327
    tempGdoc.delete();  //KHS20030327
    Logger.debug("[SystemFolder.createHeader] End");
    return gdoc;
  }

  public GridDocument updateHeader(GridDocument gdoc)
    throws Throwable
  {
    Logger.debug("[SystemFolder.updateHeader] Start");

    String udocFilename = gdoc.getUdocFilename();
    String tempUdocFilename = gdoc.getTempUdocFilename();

    if (tempUdocFilename.length() > 0)
    {
      gdoc.setTempUdocFilename("");
      File udoc = FileHelper.getUdocFile(gdoc);
      gdoc.setUdocFullPath(udoc.getAbsolutePath());
    }

    GridDocumentEntityHandler.getInstance().update(gdoc);
    File tempGdoc = File.createTempFile(generateRandomFileName(), ".xml");
    String gdocFullPath = tempGdoc.getAbsolutePath();
    gdoc.serialize(gdocFullPath);
    FileUtil.delete(IDocumentPathConfig.PATH_GDOC,
                    gdoc.getFolder()+File.separator,
                    gdoc.getGdocFilename());
    FileInputStream fis = new FileInputStream(tempGdoc);  //KHS20030327
    FileUtil.create(IDocumentPathConfig.PATH_GDOC,
                    gdoc.getFolder()+File.separator,
                    gdoc.getGdocFilename(),
                    fis);                                 //KHS20030327

    if (tempUdocFilename.length() > 0)
    {
      gdoc.setTempUdocFilename(tempUdocFilename);
      File udoc = FileHelper.getUdocFile(gdoc);
      gdoc.setUdocFullPath(udoc.getAbsolutePath());
    }
    fis.close();        //KHS20030327
    tempGdoc.delete();  //KHS20030327
    Logger.debug("[SystemFolder.updateHeader] End");
    return gdoc;
  }

  public GridDocument saveInFolder(GridDocument gdoc) throws Throwable
  {
    try
    {
      Logger.debug("[SystemFolder.saveInFolder] Start");
      if (gdoc.getGdocId() != null)
      {
        gdoc.setRefGdocId(gdoc.getGdocId());
      }
      String tempUdocFilename = gdoc.getTempUdocFilename();
      String udocFilename = FileHelper.changeFileExtension(gdoc.getUdocFilename(), tempUdocFilename);
      Logger.debug("[SystemFolder.saveInFolder] udocFilename : "+udocFilename);
    
      /*040326NSL The FileUtil.create will create unique filename
       String newUdocFilename = FileUtil.getUniqueFilename(
                               IDocumentPathConfig.PATH_UDOC,
                               gdoc.getFolder()+File.separator,
                               udocFilename);
                               */
      File udocFile = FileHelper.getUdocFile(gdoc);
      long fileSize = Math.round(udocFile.length()/1024);
      if (fileSize == 0)
      {
        fileSize = 1;
      }
      gdoc.setUdocFileSize(new Long(fileSize));

      String newUdocFilename = FileUtil.create(IDocumentPathConfig.PATH_UDOC,
                                      gdoc.getFolder()+File.separator,
                                      udocFilename, 
                                      new FileInputStream(udocFile));

      Logger.debug("[SystemFolder.saveInFolder] newUdocFilename : "+newUdocFilename);
      gdoc.setUdocFilename(newUdocFilename);
      gdoc.setTempUdocFilename("");
      udocFile = FileHelper.getUdocFile(gdoc);
      gdoc.setUdocFullPath(udocFile.getAbsolutePath());

      gdoc = checkAttachmentStatus(gdoc);
      gdoc = createHeader(gdoc);
      if (tempUdocFilename.length() > 0)
      {
        Logger.debug("[SystemFolder.saveInFolder] tempUdocFilename : "+tempUdocFilename);
        gdoc.setTempUdocFilename(tempUdocFilename);
        // Get temp udoc file
        udocFile = FileHelper.getUdocFile(gdoc);
        gdoc.setUdocFullPath(udocFile.getAbsolutePath());
      }
      Logger.debug("[SystemFolder.saveInFolder] End");
      return gdoc;
    }
    catch(Exception ex)
    {
      if(gdoc != null)
      {
        //TWX 15 May 2007 Trigger partner function failure alert
        triggerPFFailure(gdoc, ex);
      }
      
      throw ex;
    }
  }
  
  private void triggerPFFailure(GridDocument gdoc, Throwable th)
  {
    String errType = "";
    if(OutboundFolder.FOLDER_NAME.equals(getFolderName()))
    {
      errType = PartnerFunctionFailure.TYPE_SEND_DOC_FAILURE;
    }
    else if(ImportFolder.FOLDER_NAME.equals(getFolderName()))
    {
      errType = PartnerFunctionFailure.TYPE_IMPORT_DOC_FAILURE;
    }
    else if(InboundFolder.FOLDER_NAME.equals(getFolderName()))
    {
      errType = PartnerFunctionFailure.TYPE_RECEIVE_DOC_FAILURE;
    }
    else if(ExportFolder.FOLDER_NAME.equals(getFolderName()))
    {
      errType = PartnerFunctionFailure.TYPE_EXPORT_DOC_FAILURE;
    }
    else
    {
      throw new IllegalArgumentException("The folder "+getFolderName()+" currently is not supported. Partner Function failure alert will not be trigger !");
    }
    PartnerFunctionFailure pff = new PartnerFunctionFailure(gdoc,
                                                            errType,
                                                            PartnerFunctionFailure.REASON_FILE_PROCESSING_ERROR,
                                                            th);
    pff.raiseAlert();
  }
  
  protected abstract GridDocument checkAttachmentStatus(GridDocument gdoc)
    throws Throwable;

  protected String generateRandomFileName()
  {
    //Random random = new Random();
    //return String.valueOf(random.nextInt());
    //NSL20070312 Ensure uniqueness
    return UUIDUtil.getRandomUUIDInStr();
  }

  protected GridDocumentEntityHandler getGridDocumentEntityHandler()
  {
    return GridDocumentEntityHandler.getInstance();
  }

  protected File getTempUdocFile(String tempUdocFilename)
  {
    File tempUdocFile = null;
    String tmpdir = System.getProperty("java.io.tmpdir");
    tempUdocFile = new File(tmpdir+"/"+tempUdocFilename);
    return tempUdocFile;
  }

  protected abstract String getFolderName();

  public abstract String getGdocFilename(GridDocument gdoc)
    throws Exception;

  protected abstract Long getDefaultHeaderMappingRuleUID(GridDocument gdoc);

  protected abstract TriggerInfo getTriggerInfo(GridDocument gdoc);


  static public SystemFolder getSpecificFolder(String folderName)
  {
    if (folderName.equals(ImportFolder.FOLDER_NAME))
      return new ImportFolder();

    if (folderName.equals(OutboundFolder.FOLDER_NAME))
      return new OutboundFolder();

    if (folderName.equals(InboundFolder.FOLDER_NAME))
      return new InboundFolder();

    if (folderName.equals(ExportFolder.FOLDER_NAME))
      return new ExportFolder();

    return null;
  }
  
  //TWX: 15 NOV 2005
  protected IMappingManagerObj getMappingMgr()
  	throws ServiceLookupException
  {
  	return (IMappingManagerObj)ServiceLocator.instance(
  			ServiceLocator.CLIENT_CONTEXT).getObj(
  			IMappingManagerHome.class.getName(), 
  			IMappingManagerHome.class,
  			new Object[0]);
  }
  
  //TWX
  protected void extractUdocElementInfo(GridDocument gdoc, File udoc)
  	throws Exception
  {
  	if(!isFileExtXML(FileHelper.getUdocFile(gdoc)) || gdoc.getUdocDocType().compareTo(SIGNAL_MSG_RN_ACK)==0
  			|| gdoc.getUdocDocType().compareTo(SIGNAL_MSG_RN_EXCEPTION)==0)
    {
  		return;
    }
  	
  	Hashtable htable = getMappingMgr().getUDocElementInfo(udoc);
  	//TWX 20 Mar 2006 : not all doc in xml format(eg not Rosettanet format) is required xpath extraction.                  
  	if(htable == null)
  	{
  		return;
  	}
  		
  	Enumeration elementID = htable.keys();
  	Enumeration udocElementInfo = htable.elements();
  	
  	while(udocElementInfo.hasMoreElements())
  	{
  		gdoc.setFieldValue(Integer.valueOf((String)elementID.nextElement()), udocElementInfo.nextElement());
  		
  		Logger.log("[SystemFolder.extractUdocElementInfo]extract udoc info "+gdoc.getDocDateGen()+" docType "+gdoc.getUdocDocType());
  	}
  	
  }
  
  /**
   * check whether the file's extension is xml
   * @param doc
   * @return
   */
  protected boolean isFileExtXML(File doc)
  {
  	if(doc!=null)
  	{
  		String filename = doc.getName();
  		int extPosition = filename.lastIndexOf(".");
  		if(extPosition >= 0)
  		{
  			String fileExt = filename.substring(filename.lastIndexOf("."), filename.length());
  			if(fileExt.equalsIgnoreCase(".xml"))
    		{
    			return true;
    		}
  		}		
  	}
  	return false;
  }
}
