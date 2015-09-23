/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HousekeepingService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 3, 2003    Mahesh              Created
 * Sep 16,2004    Mahesh              Removed archiving logic from housekeeping
 *                                    module
 * Oct 27 2005    Neo Sok Lay         Use FileUtil to get File handle    
 * Jun 23 2006    Tam Wei Xiang       Delete the XPDL runtime records while doing the
 *                                    housekeeping.  
 * May 21 2008    Tam Wei Xiang       #46 i) improve the delete logic to include deleting the
 *                                        nested folder.
 *                                        ii)Temp file under sys temp dir will be deleted as
 *                                           well. 
 */                                   
package com.gridnode.gtas.server.housekeeping;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gridnode.gtas.server.housekeeping.exceptions.HousekeepingException;
import com.gridnode.gtas.server.housekeeping.helpers.IHousekeepingConfig;
import com.gridnode.gtas.server.housekeeping.helpers.Logger;
import com.gridnode.gtas.server.housekeeping.model.HousekeepingInfo;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * This class handles the housekeeping of log files,
 * temp files,audit files ,process instances and Documents   
 *     
 */

public class HousekeepingService
{
  private static HousekeepingService housekeepingService = new HousekeepingService();
  
  private HousekeepingService()
  {
  }
  
  public static synchronized HousekeepingService getInstance()
  {
    if(housekeepingService==null)
    {
      housekeepingService=new HousekeepingService();
    }
    return housekeepingService;
  }
  
  /**
   * This method loads the settings to HousekeepingInfo object 
   * and returns it. 
   * @return HousekeepingInfo
   * @throws HousekeepingException
   */
  public HousekeepingInfo getHousekeepingInfo() throws HousekeepingException
  {
    try
    {    
      File file=getPropertiesFile();
      if (file == null || !file.exists())
        throw new Exception("Housekeeping properties file not found!, file=" + file);

      HousekeepingInfo hkInfo = new HousekeepingInfo();
      hkInfo=(HousekeepingInfo)hkInfo.deserialize(file.getAbsolutePath());
      return hkInfo;
    }
    catch(Throwable th)
    {
      Logger.warn("[HousekeepingService.getHousekeepingInfo] Error in getting HousekeepingInfo",th);
      throw new HousekeepingException("Error in getting HousekeepingInfo: "+th.getMessage(),th);
    }
  }
  
  /**
   * Saves the housekeeping entity and schedules the timer
   * @param hkInfo
   */ 
  public synchronized void saveHousekeepingInfo(HousekeepingInfo hkInfo) throws HousekeepingException
  {
    Logger.log("[HousekeepingService.saveHousekeepingInfo] "+hkInfo);
    try
    {
      File file=getPropertiesFile();
      Logger.debug("[HousekeepingService.saveHousekeepingInfo] file="+file);
      hkInfo.serialize(file.getAbsolutePath());
    }
    catch(Throwable th)
    {
      Logger.warn("[HousekeepingService.saveHousekeepingInfo] Error saving settings hkInfo="+hkInfo,th);
      throw new HousekeepingException("Failed to save HousekeepingInfo",th);
    }
    finally
    {
      Logger.log("[HousekeepingService.saveHousekeepingInfo] Exit ");
    }
  }
  
  /**
   * This method handles the housekeeping tasks ,such as archiveing and deleting the 
   * log,temp,doc files and process instances 
   * @throws HousekeepingException
   */
  public synchronized void doHousekeeping() throws HousekeepingException
  {
    Logger.log("[HousekeepingService.doHousekeeping] Enter ");
    try
    {
      HousekeepingInfo hkInfo=getHousekeepingInfo();
      Logger.log("[HousekeepingService.doHousekeeping] HousekeepingInfo="+hkInfo);
      
      //boolean delete=true;  
      //long currentTime=System.currentTimeMillis(); 
      
			if(hkInfo.getLogFilesDaysToKeep()!=null && hkInfo.getLogFilesDaysToKeep().intValue()>-1)
			{
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1 * hkInfo.getLogFilesDaysToKeep().intValue());
				long timeOut=cal.getTimeInMillis();
				File logFolder = FileUtil.getFile(IHousekeepingConfig.PATH_LOG, ""); //NSL20051027 Use file util to return the file
				//File logFolder=new File(FileUtil.getDomain()+File.separator+FileUtil.getPath(IHousekeepingConfig.PATH_LOG));
        Logger.debug("[HousekeepingService.doHousekeeping] Before deleting log files from "+logFolder);    
				deleteFiles(logFolder,timeOut, false, true);
			}

			if(hkInfo.getTempFilesDaysToKeep()!=null && hkInfo.getTempFilesDaysToKeep().intValue()>-1)
			{
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1 * hkInfo.getTempFilesDaysToKeep().intValue());
        long timeOut=cal.getTimeInMillis();
				//File tempFolder=new File(FileUtil.getDomain()+File.separator+FileUtil.getPath(IPathConfig.PATH_TEMP));
        File tempFolder = FileUtil.getFile(IPathConfig.PATH_TEMP, ""); //NSL20051027 Use FileUtil to return the file
        Logger.debug("[HousekeepingService.doHousekeeping] Before deleting temp files from "+tempFolder);    
				deleteFiles(tempFolder,timeOut, true, true);
        Logger.debug("[HousekeepingService.doHousekeeping] Complete housekeep on "+tempFolder); 
        
        //#46 Now the File Temp dir used in GTAS is under gtas dir. It is no longer base on the value return from the OS 
        File sysTempFolder = new File(SystemUtil.getSysTempDir());
        if(sysTempFolder.exists() && sysTempFolder.isDirectory())
        {
          Logger.debug("[HousekeepingService.doHousekeeping] Before deleting temp files from "+sysTempFolder);
          deleteFiles(sysTempFolder, timeOut, true, true);
          Logger.debug("[HousekeepingService.doHousekeeping] Complete housekeep on "+sysTempFolder);
        }
			}
			
			//TWX 23062006 Cleanup XPDL runtime records
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1 * hkInfo.getWfRecordsDaysToKeep().intValue());  
			cleanUpXPDLRuntimeRecord(cal.getTimeInMillis());
			
      Logger.log("[HousekeepingService.doHousekeeping] Housekeeping is successfull");
    }
    catch(Throwable th)
    {
      Logger.warn("[HousekeepingService.doHousekeeping]",th);
      throw new HousekeepingException("Failed while housekeeping: "+th.getMessage(),th);          
    } 
    finally
    {
      Logger.log("[HousekeepingService.doHousekeeping] Exit ");
    }
  }


  /**
   * This method deletes the files whose lastModified <= timeout
   * If the file object represents the folder then it will delete
   * all the files from folder and its subfolders matching the 
   * above criteria
   * 
   * @param file
   * @param timeOut
   * @param isDeleteNestedFolder to indicate whether the folder itself need to be deleted. 
   * @param isRootFolder
   * @throws Exception
   */
  protected void deleteFiles(File file,long timeOut, boolean isDeleteNestedFolder, boolean isRootFolder) throws Exception
  {
    if(file!=null)
    {
      if(file.isFile())
      {
        if(file.lastModified() <=timeOut)
          file.delete();
      }
      else if(file.isDirectory())
      {
        File[] files=file.listFiles();
        if(files!=null)
        {
          for(int i=0;i<files.length;i++)
          {
            if(files[i].isDirectory())
            {
              deleteFiles(files[i],timeOut, isDeleteNestedFolder, false);
            }
            else
            {
              if(files[i].lastModified() <=timeOut)
                files[i].delete();
            }
          }
        }
        
        //#46 TWX Delete the nested folder
        if(isDeleteNestedFolder && ! isRootFolder && file.lastModified() <=timeOut)
        {
          file.delete();
        }
      }
    }
  }

  /**
   * Loads the setting  data into the HousekeepingInfo object 
   * @return HousekeepingInfo
   * @throws Exception
   */    
  protected HousekeepingInfo loadHousekeepingInfo() throws Exception
  {
    File file=getPropertiesFile();
    if (file == null || !file.exists())
      throw new Exception("Housekeeping properties file not found!, file=" + file);

    HousekeepingInfo hkInfo = new HousekeepingInfo();
    hkInfo=(HousekeepingInfo)hkInfo.deserialize(file.getAbsolutePath());
    return hkInfo;
  }
  
  private File getPropertiesFile() throws Exception
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(IHousekeepingConfig.CONFIG_NAME);
    if (config == null)
      throw new Exception("Housekeeping configuration file not found!");
    String hkFileName = config.getString(IHousekeepingConfig.SETTING_FILE);
    File hkFile = FileUtil.getFile(IHousekeepingConfig.PATH_SETTING,hkFileName);
    if(hkFile==null)
      throw new Exception("Housekeeping setting file not found, hkFileName="+hkFileName);
    return hkFile;
  }
  
  /**
   * TWX: Perform clean up on the runtime workflow DB record (the type is XPDL) 
   * @param timeOut specify the date time that we will keep the record (eg the record with date earlier than
   *        the timeOut will be the candidate to be deleted)
   * @throws Exception
   */
  private void cleanUpXPDLRuntimeRecord(long timeOut)
  	throws Exception
  {
  	IDataFilter rtProcessFilter = getProcessInstanceFilter(timeOut);
  	Logger.log("[HouseKeepingService.cleanUpXPDLRuntimeRecord] filter construct is "+rtProcessFilter.getFilterExpr());
  	
  	Collection rtprocessKeys = getEntityDAO(GWFRtProcess.ENTITY_NAME).findByFilter(rtProcessFilter);
  	
  	//Follow the similiar logic of Archival
  	ArrayList<Long> rtprocessKeySubList = new ArrayList<Long>();
  	
  	int count = 100;
  	for(Iterator<Long> i = rtprocessKeys.iterator(); i.hasNext();)
  	{
  		rtprocessKeySubList.add(i.next());
  		if(!i.hasNext() || count == rtprocessKeySubList.size())
  		{
  			cleanUpRtActivity(rtprocessKeySubList);
  			cleanUpRtProcess(rtprocessKeySubList);
  			rtprocessKeySubList.clear();
  		}
  	}
  }
  
  /**
   * Return the ProcessInstance Filter
   * @param timeOut
   * @return
   */
  private IDataFilter getProcessInstanceFilter(long timeOut)
  {
  	long utcTimeOut = TimeUtil.localToUtc(timeOut);
  	
  	IDataFilter filter = new DataFilterImpl();
  	filter.addSingleFilter(null,IGWFRtProcess.ENGINE_TYPE, filter.getEqualOperator(), "XPDL", false);
  	filter.addSingleFilter(filter.getAndConnector(), IGWFRtProcess.STATE, filter.getNotEqualOperator(), IGWFRtProcess.OPEN_RUNNING,false);
  	filter.addSingleFilter(filter.getAndConnector(), IGWFRtProcess.STATE, filter.getNotEqualOperator(), IGWFRtProcess.OPEN_NOTRUNNING,false);
  	filter.addSingleFilter(filter.getAndConnector(), IGWFRtProcess.STATE, filter.getNotEqualOperator(), IGWFRtProcess.OPEN_NOTRUNNING_SUSPENDED,false);
  	filter.addSingleFilter(filter.getAndConnector(), IGWFRtProcess.END_TIME, filter.getLessOperator(), new Date(utcTimeOut), false);
  	return filter;
  }
  
  /**
   * Return the RtActivityFilter
   * @param rtProcessKeys a collection of RtProcess UID
   * @return
   */
  private IDataFilter getRtActivityFilter(Collection<Long> rtProcessKeys)
  {
  	IDataFilter filter = new DataFilterImpl();
  	filter.addDomainFilter(null, IGWFRtActivity.RT_PROCESS_UID, rtProcessKeys, false);
  	return filter;
  }
  
  /**
   * Get the EntityDAO for a particular Entity
   * @param entityName
   * @return
   */
  private IEntityDAO getEntityDAO(String entityName)
  {
    IEntityDAO entityDAO = EntityDAOFactory.getInstance().getDAOFor(entityName);
    return entityDAO;
  }
  
  /**
   * Remove the RtActivity record given the RTProcess UID
   * @param rtProcessKeys
   * @throws Exception
   */
  private void cleanUpRtActivity(Collection<Long> rtProcessKeys)
  	throws Exception
  {
  	IDataFilter rtActivityFilter = getRtActivityFilter(rtProcessKeys);
  	((EntityDAOImpl)getEntityDAO(GWFRtActivity.ENTITY_NAME)).removeByFilter(rtActivityFilter);
  }
  
  /**
   * Remove the RtProcess record given the RtPRocess UID
   * @param rtProcessKeys
   * @throws Exception
   */
  private void cleanUpRtProcess(Collection<Long> rtProcessKeys)
  	throws Exception
  {
  	IDataFilter rtProcessFilter = new DataFilterImpl();
  	rtProcessFilter.addDomainFilter(null, IGWFRtProcess.UID, rtProcessKeys, false);
  	
  	((EntityDAOImpl)getEntityDAO(GWFRtProcess.ENTITY_NAME)).removeByFilter(rtProcessFilter);
  }
}
