/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DbArchive.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * 
 * Jan 05 2004    Mahesh              Modified to archive and restore processinstance
 * Jul 29 2004    Mahesh              Modified archival process to be more fault tolerance,
 *                                    Restoring the orginal document timestamp
 * Oct 10 2005    Tam Wei Xiang       Modified archiveProcessInstance(), archiveGridDoc()   
 * Mar 31 2006    Tam Wei Xiang       Modified archiveProcessInstance() to set criteria table
 *                                    into DirectArchiveProcessInstace     
 * Apr 03 2006    Tam Wei Xiang       Added in method getProvider() 
 * Aug 31 2006    Tam Wei Xiang       Merge from ESTORE stream.
 * May 21 2008    Tam Wei Xiang       #45: Archive temp file should be stored under
 *                                    data\GNapps\gtas\data\temp. Currently,
 *                                    it has been stored under <JBOSS_HOME>/bin.                                                             
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.gtas.server.document.model.IGridDocument;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFArchiveProcess;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.xml.adapters.GNElement;

public class DbArchive
{
  //static String REPEAT_RESTORE_EX = "Error in restore archived document--Is it a repeated restore?";

  static String FILE_SEPARATOR = System.getProperty("file.separator");
  static String WORK_DIR = System.getProperty("user.dir");

  static String PATH_GDOC = "gdoc";
  static String PATH_UDOC = "udoc";
  static String PATH_AUDIT = "audit";
  
  static String PATH_ATT = "attachment";
  static String PATH_PROCESSINSTANCE = "processinstance";  
  static String PATH_SUMMARY = "summary";
  static String NAME_SUM_FILE = "archive.ini";
	
  String TMP_DIR = getRootTempDir()+"archive_temp"+System.currentTimeMillis(); //#45
  String TMP_GDOC_DIR = TMP_DIR + FILE_SEPARATOR + PATH_GDOC;
  String TMP_UDOC_DIR = TMP_DIR + FILE_SEPARATOR + PATH_UDOC;
  String TMP_ATT_DIR = TMP_DIR + FILE_SEPARATOR + PATH_ATT;
  String TMP_AUDIT_DIR = TMP_DIR + FILE_SEPARATOR + PATH_AUDIT;
  String TMP_PROCESSINSTANCE_DIR = TMP_DIR + FILE_SEPARATOR + PATH_PROCESSINSTANCE;
  String SUMMARY_FILE = TMP_DIR + FILE_SEPARATOR + NAME_SUM_FILE;

  long MAX_ZIP_SIZE=100 * 1024 *1024;

  static String GTAS = "GTAS_Version";
  static String DATABASE = "Database_Version";
  static String GTAS_VER = "2.3";
  static String DATABASE_VER = "2.3";
  static String TIME = "Time";
  static String ARCHIVE_FOLDER = "ArchiveFolder";
  static String ARCHIVE_FILES = "ArchiveFiles";
  static String PATH_KEY = "PathKey";
  
  private long currentZipSize=0;
  private BackupZipFile arcBackupZipFile;
	
  private DirectArchiveGridDocument archiveGDoc=null;
  private DirectArchiveProcessInstance archiveProcess=null;
  
  private String archiveName = null;
  private String archiveDesc =null;
  private String archiveType =null;
  private ErrorLog errorLog = null;
  private List archiveFileList = null;
  private Hashtable summaryTable = new Hashtable(); 
  private Hashtable criteriaTable = new Hashtable();
    
  private String archiveID = "";
  
  public DbArchive() throws Exception
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(IDBArchivePathConfig.CONFIG_NAME);
    if (config == null)
      throw new Exception("DbArchive configuration file not found!");
    String tmpSize = config.getString(IDBArchivePathConfig.MAX_FILESIZE_KEY,""+15);
    MAX_ZIP_SIZE = Long.parseLong(tmpSize.trim()) * 1024 * 1024 ; //from megabytes to bytes
    Logger.log("[DbArchive.init] MAX_ZIP_SIZE="+MAX_ZIP_SIZE+", tmpSize="+tmpSize);
    
    archiveGDoc= new DirectArchiveGridDocument(this);
    archiveProcess=new DirectArchiveProcessInstance(this,archiveGDoc);
    
    archiveFileList= new ArrayList();
    summaryTable = new Hashtable();
    errorLog = new ErrorLog();
  }

  public void setArchiveName(String name)
  {
    archiveName = name;
  }
  
  public void setDescription(String description)
  {
    archiveDesc = description;
  }

  public void setArchiveType(String archiveType)
  {
    this.archiveType= archiveType;
  }
  
	public void createArchive() throws Exception
	{
		Logger.log("[DbArchive.createArchive] Enter ");
    errorLog.open();
	}
  
 	public void closeArchive() throws Exception
	{
    Logger.log("[DbArchive.closeArchive] Enter ");
    closeZipFile();
    for(int i = 0;i<archiveFileList.size();i++)
    {
      File archiveTempFile =(File)archiveFileList.get(i);
      if(archiveTempFile!=null && archiveTempFile.exists())
        archiveTempFile.delete();
    }
    errorLog.close();
	}
  
  /**
   * Saves the archived files to the specified path and generates 
   * Summary file
   * @param pathKey
   * @return
   * @throws Exception
   */
  public String saveResultTo(String pathKey) throws Exception
  {
    Logger.log("[DbArchive.saveResultTo] Enter, pathKey="+pathKey);
    closeZipFile();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
    String dateStr = df.format(new Date());
    String fileNames="";
    for(int i = 0;i<archiveFileList.size();i++)
    {
      String archiveName = "GTASArchive"+ "_" + dateStr +"_"+i+".zip";
      File archiveTempFile =(File)archiveFileList.get(i);
      try
      {
        archiveName = FileUtil.create(pathKey,dateStr+"/",archiveName, archiveTempFile);
        fileNames+=((i>0)?",":"")+archiveName;
      }
      catch(Throwable th)
      {
        err("[DbArchive.saveResultTo] Error saving file. archiveTempFile="+archiveTempFile+", toFile="+archiveName, th);
      }
    }
    
    errorLog.close();
    File errorLogFile = errorLog.getLogFile();
    String errorLogFileName = FileUtil.create(pathKey,dateStr+"/","error.log", errorLogFile);
    summaryTable.put("ErrorLogFile",errorLogFileName);
    summaryTable.put(ARCHIVE_FOLDER,dateStr);
    if(fileNames.length()>0)
      summaryTable.put(ARCHIVE_FILES,fileNames);
    
    File tmpSummaryFile = File.createTempFile("summary",".xml"); 
    tmpSummaryFile=writeSumInfo(tmpSummaryFile.getAbsolutePath(),summaryTable);

    String summaryFileName = "summary"+"_"+dateStr+".xml";
    summaryFileName = FileUtil.create(pathKey,summaryFileName, tmpSummaryFile);
    return summaryFileName;
  }
  
  private void createNewZipFile() throws Exception
  {
    Logger.log("[DbArchive.createNewZipFile] Enter ");
    File archiveTempFile = File.createTempFile("GTASArchive", "arc");
    archiveFileList.add(archiveTempFile);
    
    arcBackupZipFile = new BackupZipFile(archiveTempFile.getAbsolutePath());
    arcBackupZipFile.open(BackupZipFile.OPEN_WRITE);      
    currentZipSize=0;
    
    LocalFileUtil.deleteTempDir(TMP_DIR);   
    
    //create an "ini" file including some summary infomation and
    //zip it into archiving file
    String tempDir = LocalFileUtil.makeTempDir(TMP_DIR);
    File sumFile = writeSumInfo(SUMMARY_FILE, null);
    arcBackupZipFile.addFile(sumFile, NAME_SUM_FILE, PATH_SUMMARY + "/");       
  }
  
  private void closeZipFile() throws Exception
  {
    Logger.log("[DbArchive.closeZipFile] Enter ");
    if(arcBackupZipFile!=null)
    {
      arcBackupZipFile.close();
      arcBackupZipFile=null;
    }
    LocalFileUtil.deleteTempDir(TMP_DIR);    
  }
	
  private void checkAndCreateZipFile(long fileSize,int fileCount)  throws Exception
  {
    if(arcBackupZipFile==null || (fileSize+currentZipSize)>=MAX_ZIP_SIZE || (fileCount+arcBackupZipFile.getEntryCount())>BackupZipFile.MAX_ENTRY_SIZE)
    {
      if(arcBackupZipFile!=null)
        closeZipFile();
      createNewZipFile();
    } 
    currentZipSize+=fileSize;
  }
  
  public void err(String message,Throwable th)
  {
    errorLog.logError(message,th);
  }
  
  public void err(String message)
  {
    errorLog.logError(message,null);
  }  
	
  /**
   * Archives ProcessInstances satisfied by the filter 
   * @param fromStartTime
   * @param toStartTime
   * @param includeInComplete
   * @param processDefNameList
   * @throws Throwable
   */
  //Modified by TWX 10/10/2005
	public void archiveProcessInstance(Timestamp fromStartTime,Timestamp toStartTime, Boolean includeInComplete, List processDefNameList, 
	                                   boolean isEnableSearchArchive, boolean isEnableRestoreArchive,
	                                   List<String> partnerIDForArchive, List<String> customerIDForArchive,
                                     boolean isArchiveOrphanRecord) throws Throwable
	{
    Logger.log("[DbArchive.archiveProcessInstance] Enter ");
    criteriaTable.put("FromStartTime",fromStartTime);
    criteriaTable.put("ToStartTime",toStartTime);
    criteriaTable.put("IncludeInComplete",includeInComplete);
    criteriaTable.put("ProcessDefNames",processDefNameList);
    criteriaTable.put("IsEnableSearchArchive", isEnableSearchArchive);
    criteriaTable.put("IsEnableRestoreArchive", isEnableRestoreArchive);
    criteriaTable.put("IsArchiveOrphanRecord", isArchiveOrphanRecord);
    
    if(partnerIDForArchive != null)
    {
      criteriaTable.put("Partners", partnerIDForArchive);
    }
    if(customerIDForArchive != null)
    {
      criteriaTable.put("BeIDs", customerIDForArchive);
    }
    summaryTable.put("ArchiveID", getArchiveID());
    
    IDataFilter rtProcessInstanceFilter = getProcessInstanceFilter(fromStartTime,toStartTime,includeInComplete.booleanValue(), 
                                                                   partnerIDForArchive, customerIDForArchive, isArchiveOrphanRecord);
    Logger.debug("[DbArchive.archiveProcessInstance] rtProcessInstanceFilter= "+rtProcessInstanceFilter.getFilterExpr());
    
    //18 OCT 2005 TWX
    EStore es = new EStore(isEnableSearchArchive);
    es.setIsArchiveByProcess(true);
    
    //31 MAR 2006 TWX
    archiveProcess.setCriteriaTable(criteriaTable);
    archiveProcess.setSummaryTable(summaryTable);
    
    try
    {
    	archiveProcess.archive(rtProcessInstanceFilter,processDefNameList,es, isEnableRestoreArchive, partnerIDForArchive, customerIDForArchive);
    }
    catch(Throwable th)
    {
    	summaryTable.put("Remark", "Some errors have occured during archive/estore. Pls verify the problem and restart the operation. (note: some docs may already in archive/estore)");
    	throw th;
    }
    finally
    {
    	summaryTable.put("TotalProcessInstanceCount",""+archiveProcess.getProcessInstanceCount());
    	summaryTable.put("FailedProcessInstanceCount",""+archiveProcess.getFailedCount());
    }
	}
    
  /**
   * Archive GridDocuments
   * @param fromStartTime
   * @param toStartTime
   * @param folderList
   * @param docTypeList
   * @throws Exception
   */
	//Modified by TWX 10/10/2005
	// 18/10/2005  add in throw Throwable
  public void archiveGridDoc(Timestamp fromStartTime,Timestamp toStartTime, List folderList, List docTypeList, boolean isEnableSearchArchive,
                             boolean isEnableRestoreArchive, List<String> partnerID, List<String> customerID,
                             boolean isArchiveOrphanRecord) throws Exception, Throwable
  {
    Logger.log("[DbArchive.archiveGridDoc] Enter ");
    IDataFilter gridDocfilter = getDocumentFilter(fromStartTime,toStartTime,folderList,docTypeList, partnerID, customerID);
    Logger.debug("[DbArchive.archiveGridDoc] gridDocfilter= "+gridDocfilter.getFilterExpr());
    criteriaTable.put("FromStartTime",fromStartTime);
    criteriaTable.put("ToStartTime",toStartTime);
    criteriaTable.put("FolderList",folderList);
    criteriaTable.put("DocTypeList",docTypeList);
    criteriaTable.put("IsEnableSearchArchive", isEnableSearchArchive);
    criteriaTable.put("IsEnableRestoreArchive", isEnableRestoreArchive);
    criteriaTable.put("IsArchiveOrphanRecord", isArchiveOrphanRecord);
    summaryTable.put("ArchiveID", getArchiveID());
    
    if(partnerID != null)
    {
      criteriaTable.put("Partners", partnerID);
    }
    
    if(customerID != null)
    {
      criteriaTable.put("BeIDs", customerID);
    }
    
    if(isArchiveOrphanRecord)
    {
      summaryTable.put("TotalGridDocumentCount",""+0);
      summaryTable.put("FailedGridDocumentCount",""+0);
      return;
    }
    
    //18 OCT 2005 TWX
    EStore es = new EStore(isEnableSearchArchive);
    es.setIsArchiveByProcess(false);

    long fromTime = fromStartTime.getTime();
    long toTime = toStartTime.getTime();
    
    archiveGDoc.setCriteriaTable(criteriaTable);
    archiveGDoc.setSummaryTable(summaryTable);
    
    try
    {
    	archiveGDoc.archive(gridDocfilter,es, isEnableRestoreArchive, partnerID);
    }
    catch(Throwable th)
    {
    	summaryTable.put("Remark", "Some errors have occured during archive/estore. Pls verify the problem and restart the operation. (note: some docs may already in archive/estore)");
    	throw th;
    }
    finally
    {
    	summaryTable.put("TotalGridDocumentCount",""+archiveGDoc.getGridDocumentCount());
    	summaryTable.put("FailedGridDocumentCount",""+archiveGDoc.getFailedCount());
    }
  }
  
  /**
   * This method will parse the summary file and restores the archivefiles specified in
   * the file
   * @param summaryFile
   * @return
   * @throws Exception
   */
  public void restoreFromSummary(String summaryFile) throws Exception
  {
    Logger.log("[DbArchive.restoreFromSummary] Enter, summaryFile="+summaryFile);
    try
    {    
      readSumInfo(summaryFile);
      String pathKey=IDBArchivePathConfig.PATH_ARCHIVE;
      String archiveFolder=(String)summaryTable.get(ARCHIVE_FOLDER);
      String archiveFileNames=(String)summaryTable.get(ARCHIVE_FILES);
      Logger.log("[DbArchive.restoreFromSummary] archiveFolder="+archiveFolder+", archiveFileNames="+archiveFileNames);
      if(archiveFolder==null || archiveFileNames==null)
        throw new Exception("Header elements are missing in summary file");
  
      StringTokenizer strtok = new StringTokenizer(archiveFileNames.trim(),",");
      while(strtok.hasMoreTokens())
      {
        String archiveFileName=strtok.nextToken().trim();
        if(archiveFileName.length()>0)
        {
          File archiveFile = FileUtil.getFile(pathKey,archiveFolder+"/",archiveFileName);
          if(archiveFile!=null)
            restoreFromArchive(archiveFile.getAbsolutePath());
          else Logger.warn("[DbArchive.restoreFromSummary] archiveFile doesnot exist, archiveFileName="+archiveFileName);
        }
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[DbArchive.restoreFromSummary] Restoring Failed. summaryFile="+summaryFile, ex);
      throw ex;
    }
    finally
    {
      Logger.log("[DbArchive.restoreFromSummary] Exit, summaryFile="+summaryFile);
    }
  }
  
  
  /**
   * Restore GridDocuments from archiving file
   * @param file Archiving file from which GridDocuments will be restored
   * @exception java.lang.Exception  Error during restoring GridDocuments
   */
  public void restoreFromArchive(String archiveFile) throws Exception
  {
    Logger.log("[DbArchive.restoreFromArchive] Enter, archiveFile="+archiveFile);
    BackupZipFile backupFile = null;
    try
    {
      //open the archiving file
      backupFile = new BackupZipFile(archiveFile);
      backupFile.open(BackupZipFile.OPEN_READ);
      Logger.log("Start Restoring ...");

      LocalFileUtil.deleteTempDir(TMP_DIR);
      //create temporary directory that GridDocuments will be unzipped into
      LocalFileUtil.makeTempDir(TMP_DIR);
      LocalFileUtil.makeTempDir(TMP_GDOC_DIR);
      LocalFileUtil.makeTempDir(TMP_UDOC_DIR);
      LocalFileUtil.makeTempDir(TMP_AUDIT_DIR);
			LocalFileUtil.makeTempDir(TMP_PROCESSINSTANCE_DIR);
      
      

      //unzip summary file & GridDocuments(gDoc & uDoc) from archiving file
      Hashtable docpathMap = new Hashtable();
      docpathMap.put(PATH_SUMMARY, TMP_DIR);
      docpathMap.put(PATH_GDOC, TMP_GDOC_DIR);
      docpathMap.put(PATH_UDOC, TMP_UDOC_DIR);
      docpathMap.put(PATH_ATT, TMP_ATT_DIR);
      docpathMap.put(PATH_AUDIT, TMP_AUDIT_DIR);
      docpathMap.put(PATH_PROCESSINSTANCE, TMP_PROCESSINSTANCE_DIR);

      ArrayList docFilenames = backupFile.unZipFiles(docpathMap);

      archiveProcess.restoreProcessInstance(docFilenames);
      archiveGDoc.restoreGridDocs(docFilenames);

      //delete the temporary directory
      LocalFileUtil.deleteTempDir(TMP_DIR);

      Logger.log("End Restoring. successful.");
    }
    catch (Exception ex)
    {
      Logger.warn("Restoring Failed.", ex);
      throw ex;
    }
    finally
    {
      try
      {
        //close the archiving file
        if(backupFile!=null) backupFile.close();
      }catch(Throwable th){}
      Logger.log("[DbArchive.restoreFromArchive] Exit, archiveFile="+archiveFile);
    }
  }

  public void addFileToZip(File file,String fileName,String category) throws Exception
  {
    checkAndCreateZipFile(file.length(),1);
    arcBackupZipFile.addFile(file, fileName, category);
  }

  public void addFilesToZip(List files,String fileName,String category) throws Exception
  {
    long fileSize = 0;
    for(int i = 0;i<files.size();i++)
    {
      fileSize+= ((File)files.get(i)).length();
    }
    checkAndCreateZipFile(fileSize,files.size());
    for(int i = 0;i<files.size();i++)
    {
      arcBackupZipFile.addFile((File)files.get(i), fileName, category);
    }
  }

  public void addFilesToZip(List files) throws Exception
  {
    long fileSize = 0;
    for(int i = 0;i<files.size();i++)
    {
      Object tmpArr[]=(Object[])files.get(i);
      fileSize+= ((File)tmpArr[0]).length();
    }
    checkAndCreateZipFile(fileSize,files.size());
    for(int i = 0;i<files.size();i++)
    {
      Object tmpArr[]=(Object[])files.get(i);
      try
      {
        arcBackupZipFile.addFile((File)tmpArr[0], (String)tmpArr[1], (String)tmpArr[2]);
      }
      catch(Throwable th)
      {
        err("[DbArchive.addFilesToZip] Error while adding to zipfile "+tmpArr[0]+", "+tmpArr[1]+", "+tmpArr[2],th);
      }
    }
  }
  
  public File generateRestoreSummaryFile() throws Exception
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
    String dateStr = df.format(new Date());
    
    StringBuffer result = new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    result.append("<properties>\n");
    String str = formatElement("ArchiveName", ""+archiveName,1);
    result.append(str);
    str = formatElement("Description", ""+archiveDesc,1);
    result.append(str);
    
    str = formatElement("TotalProcessInstanceFileCount", ""+archiveProcess.getProcessInstanceCount(),1);
    result.append(str);
    str = formatElement("FailedProcessInstanceFileCount", ""+archiveProcess.getFailedCount(),1);
    result.append(str);

    str = formatElement("TotalGridDocumentCount", ""+archiveGDoc.getGridDocumentCount(),1);
    result.append(str);
    str = formatElement("FailedGridDocumentCount", ""+archiveGDoc.getFailedCount(),1);
    result.append(str);
    
    result.append("</properties>\n");
    File sumFile = File.createTempFile("summary_restore_"+dateStr,".xml");
    FileOutputStream fos = new FileOutputStream(sumFile);
    fos.write(result.toString().getBytes());
    fos.close();
    return sumFile;
    
  }
  
  /**
   * Write some summary infomation to the archived file
   * @param fileName The name of destination file
   */
  private File writeSumInfo(String sumFileName, Hashtable summaryTable) throws Exception
  {
    Date now = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    StringBuffer result = new StringBuffer();
    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    result.append("<properties>\n");

    String str = formatElement(TIME, df.format(now),1);
    result.append(str);
    
    if(archiveName!=null && archiveName.length()>0)
    {
      str = formatElement("ArchiveName", archiveName,1);
      result.append(str);
    }
    if(archiveDesc!=null && archiveDesc.length()>0)
    {
      str = formatElement("Description", archiveDesc,1);
      result.append(str);
    }
    
    str = formatElement("ArchiveType", archiveType,1);
    result.append(str);
    String criteria ="";
    if(archiveType.equals(IDBArchiveConstants.ARCHIVE_PROCESSINSTANCE))
      criteria=generateProcessInstanceCriteria(criteriaTable);
    else criteria=generateGridDocumentCriteria(criteriaTable);
    
    result.append(criteria);
    
    if(summaryTable!=null)
    {
      for(Iterator i = summaryTable.keySet().iterator();i.hasNext();)
      {
        String key = (String)i.next();
        String value = (String)summaryTable.get(key);
        if(value!=null)
        {
          str = formatElement(key, value,1);
          result.append(str);
        }
      }
    }
    result.append("</properties>\n");
    FileOutputStream fos = new FileOutputStream(sumFileName);
    fos.write(result.toString().getBytes());
    fos.close();
    return new File(sumFileName);
  }
  
  private String generateProcessInstanceCriteria(Hashtable criteriaTable)
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String criteria ="\t<Criteria>\n";
    criteria+= formatElement("IsEnableRestoreArchive", ((Boolean)criteriaTable.get("IsEnableRestoreArchive")).toString(), 2);
    criteria+= formatElement("IsEnableSearchArchive", ((Boolean)criteriaTable.get("IsEnableSearchArchive")).toString(), 2);
    criteria+= formatElement("FromStartTime", df.format(criteriaTable.get("FromStartTime")),2);
    criteria+= formatElement("ToStartTime", df.format(criteriaTable.get("ToStartTime")),2);
    criteria+= formatElement("IncludeInComplete", ""+criteriaTable.get("IncludeInComplete"),2);
    criteria+= formatElement("ProcessDefNames", getStrFromList((List)criteriaTable.get("ProcessDefNames")),2);
    criteria+= formatElement("Partners", getStrFromList((List<String>)criteriaTable.get("Partners")), 2);
    criteria+= formatElement("BizEntities", getStrFromList((List<String>)(criteriaTable.get("BeIDs"))), 2);
    criteria+= formatElement("ISArchiveOrphanRecord",  (criteriaTable.get("IsArchiveOrphanRecord") == null? Boolean.FALSE.toString(): ((Boolean)(criteriaTable.get("IsArchiveOrphanRecord"))).toString()), 2);
    
    criteria+= "\t</Criteria>\n";
    return criteria;  
  }
  
  private String generateGridDocumentCriteria(Hashtable criteriaTable)
  {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String criteria ="\t<Criteria>\n";
    criteria+= formatElement("IsEnableRestoreArchive", ((Boolean)criteriaTable.get("IsEnableRestoreArchive")).toString(), 2);
    criteria+= formatElement("IsEnableSearchArchive", ((Boolean)criteriaTable.get("IsEnableSearchArchive")).toString(), 2);
    criteria+= formatElement("FromStartTime", df.format(criteriaTable.get("FromStartTime")),2);
    criteria+= formatElement("ToStartTime", df.format(criteriaTable.get("ToStartTime")),2);
    criteria+= formatElement("FolderList", getStrFromList((List)criteriaTable.get("FolderList")),2);
    criteria+= formatElement("DocTypeList", getStrFromList((List)criteriaTable.get("DocTypeList")),2);
    criteria+= formatElement("Partners", getStrFromList((List<String>)criteriaTable.get("Partners")), 2);
    criteria+= formatElement("BizEntities", getStrFromList((List<String>)(criteriaTable.get("BeIDs"))), 2);
    criteria+= formatElement("ISArchiveOrphanRecord", (criteriaTable.get("IsArchiveOrphanRecord") == null? Boolean.FALSE.toString(): ((Boolean)(criteriaTable.get("IsArchiveOrphanRecord"))).toString()), 2);
    criteria+= "\t</Criteria>\n";
    return criteria;  
  }
  
  private String getStrFromList(List list)
  {
    String listStr="";
    if(list!=null)
    {
      for(Iterator i = list.iterator();i.hasNext();)
        listStr+=i.next()+(i.hasNext()?",":"");
    }
    return listStr;
  }
  
  private String getStrFromSet(Set<String> set)
  {
  	StringBuilder str = new StringBuilder("");
  	if(set != null && ! set.isEmpty())
  	{
  		Iterator<String> ite = set.iterator();
  		while(ite.hasNext())
  		{
  			str.append(ite.next()).append(ite.hasNext() ? ",": "");
  		}
  	}
  	return str.toString();
  }
  
  /**
   * Read the summary infomation from the archived file
   * @param fileName The name of source file
   */
  private void readSumInfo(String fileName) throws Exception
  {
    GNElement root;
    //root = XMLDocumentUtility.getRoot(fileName);
    root = ArchiveHelper.getXMLManager().getRoot(fileName);
    
    String archiveFolder = root.getChildText(ARCHIVE_FOLDER);
    if(archiveFolder!=null && archiveFolder.trim().length()>0)
      summaryTable.put(ARCHIVE_FOLDER,archiveFolder.trim());
      
    String archiveFiles = root.getChildText(ARCHIVE_FILES);
    if(archiveFiles!=null && archiveFiles.trim().length()>0)
      summaryTable.put(ARCHIVE_FILES,archiveFiles.trim());

    try  //this info is only for reference
    {
      archiveName  = root.getChildText("ArchiveName");
      if(archiveName!=null)
        summaryTable.put("ArchiveName",archiveName);

      archiveDesc  = root.getChildText("Description");
      if(archiveDesc!=null)
        summaryTable.put("Description",archiveDesc);
    }
    catch(Throwable th)
    {
      
    }
    //  todo some pre-handling before restoring
  }

  /**
   * Private method to create a XML element.
   */
  static String formatElement(String elementname, String value,int depth)
  {
    StringBuffer buf = new StringBuffer();
    for(int i =0;i<depth;i++)
      buf.append("\t");  
    buf.append('<');
    buf.append(elementname);
    buf.append(">");
    buf.append(normalize(value));
    buf.append("</");
    buf.append(elementname);
    buf.append(">\n");
    return buf.toString();
  }

  /** Normalizes the given string. */
  static String normalize(String s)
  {
    boolean canonical = false;
    StringBuffer str = new StringBuffer();

    int len = (s != null) ? s.length() : 0;
    for (int i = 0; i < len; i++)
    {
      char ch = s.charAt(i);
      switch (ch)
      {
        case '<' :
          {
            str.append("&lt;");
            break;
          }
        case '>' :
          {
            str.append("&gt;");
            break;
          }
        case '&' :
          {
            str.append("&amp;");
            break;
          }
        case '"' :
          {
            str.append("&quot;");
            break;
          }
        case '\r' :
        case '\n' :
          {
            if (canonical)
            {
              str.append("&#");
              str.append(Integer.toString(ch));
              str.append(';');
              break;
            }
            // else, default append char
          }
        default :
          {
            str.append(ch);
          }
      }
    }

    return (str.toString());

  } // normalize(String):String


  protected static IDataFilter getProcessInstanceFilter(Timestamp fromStartTime,Timestamp toStartTime, boolean includeInComplete,
                                                        List<String> partnerIDForArchive, List<String> customerIDForArchive,
                                                        boolean isArchiveOrphanRecord)
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null,                      IGWFArchiveProcess.ENGINE_TYPE,  filter.getEqualOperator(),          "BPSS",                     false);
    filter.addSingleFilter(filter.getAndConnector(),  IGWFArchiveProcess.PROCESS_TYPE, filter.getEqualOperator(),          "BpssBinaryCollaboration",  false);
    filter.addSingleFilter(filter.getAndConnector(),  IGWFArchiveProcess.PROCESS_START_TIME,   filter.getGreaterOrEqualOperator(), fromStartTime,              false);
    filter.addSingleFilter(filter.getAndConnector(),  IGWFArchiveProcess.PROCESS_START_TIME,   filter.getLessOrEqualOperator(),    toStartTime,                false);

    if(!includeInComplete)
    {
      List stateList = new ArrayList();
      stateList.add(new Integer(IGWFRtProcess.CLOSED_COMPLETED));
      stateList.add(new Integer(IGWFRtProcess.CLOSED_ABNORMALCOMPLETED));
      stateList.add(new Integer(IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED));
      stateList.add(new Integer(IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED));
      
      filter.addDomainFilter(filter.getAndConnector(), IGWFArchiveProcess.PROCESS_STATUS, stateList, false);
    }
    
    if(isArchiveOrphanRecord)
    {
      IDataFilter orphanRecordFilter = new DataFilterImpl();
      orphanRecordFilter.addSingleFilter(null, IGWFArchiveProcess.CUSTOMER_BE_ID, orphanRecordFilter.getEqualOperator(), null, false);
      orphanRecordFilter.addSingleFilter(orphanRecordFilter.getOrConnector(), IGWFArchiveProcess.CUSTOMER_BE_ID, orphanRecordFilter.getEqualOperator(), "", false);
      filter.addFilter(filter.getAndConnector(), orphanRecordFilter);
    }
    else if(customerIDForArchive != null && customerIDForArchive.size() > 0)
    {
      filter.addDomainFilter(filter.getAndConnector(), IGWFArchiveProcess.CUSTOMER_BE_ID, customerIDForArchive, false);
    }
    else if(partnerIDForArchive != null && partnerIDForArchive.size() > 0)
    {
      filter.addDomainFilter(filter.getAndConnector(), IGWFArchiveProcess.PARTNER_KEY, partnerIDForArchive, false);
    }
    
    return  filter;   
  }

  protected static IDataFilter getDocumentFilter(Timestamp fromStartTime,Timestamp toStartTime, List folderList, List docTypeList,
                                                 List<String> partnerIDForArchive, List<String> customerIDForArchive)
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null,                      IGridDocument.DT_CREATE, filter.getGreaterOrEqualOperator(), fromStartTime, false);
    filter.addSingleFilter(filter.getAndConnector(),  IGridDocument.DT_CREATE, filter.getLessOrEqualOperator(),    toStartTime,   false);
    filter.addDomainFilter(filter.getAndConnector(),  IGridDocument.FOLDER,                                        folderList,    false);    
    filter.addDomainFilter(filter.getAndConnector(),  IGridDocument.U_DOC_DOC_TYPE,                                docTypeList,   false);
    
    if(partnerIDForArchive != null && partnerIDForArchive.size() > 0)
    {
    	IDataFilter filterForPartner = new DataFilterImpl();
    	filterForPartner.addDomainFilter(null, IGridDocument.S_PARTNER_ID, partnerIDForArchive, false);
    	filterForPartner.addDomainFilter(filterForPartner.getOrConnector(), IGridDocument.R_PARTNER_ID, partnerIDForArchive, false);
    	filter.addFilter(filter.getAndConnector(), filterForPartner);
    }
    else if(customerIDForArchive != null && customerIDForArchive.size() > 0)
    {
      IDataFilter filterForCustomer = new DataFilterImpl();
      filterForCustomer.addDomainFilter(null, IGridDocument.S_BIZ_ENTITY_ID, customerIDForArchive, false);
      filterForCustomer.addDomainFilter(filterForCustomer.getOrConnector(), IGridDocument.R_BIZ_ENTITY_ID, customerIDForArchive, false);
      filter.addFilter(filter.getAndConnector(), filterForCustomer);
    }
    Logger.log("[DBArchive.getDocumentFilter] filter constructed is "+ filter.getFilterExpr());
    return  filter;   
  }  
  
  //TWX 4 April 2006 provider for archive by process
  public ArchiveAlertProvider getArchiveByProcessProvider()
  {
  	Date archiveStartTime = new Date(TimeUtil.localToUtc(Calendar.getInstance().getTimeInMillis())); //the actual day, hour, minute, second
                                                                                                     //have been converted to UTC time     
                                                                                                     //fromTime, toTime are in UTC time
  	Timestamp fromTime = (Timestamp)criteriaTable.get("FromStartTime");
  	Timestamp toTime = (Timestamp)criteriaTable.get("ToStartTime");
  	List processDefList = (List)criteriaTable.get("ProcessDefNames");
  	Boolean includeInComplete = (Boolean)criteriaTable.get("IncludeInComplete");
  	Boolean isEnableSearchArchive = (Boolean)criteriaTable.get("IsEnableSearchArchive") == null ? false : (Boolean)criteriaTable.get("IsEnableSearchArchive");
    Boolean isEnableRestoreArchive = (Boolean)criteriaTable.get("IsEnableRestoreArchive") == null ? false : (Boolean)criteriaTable.get("IsEnableRestoreArchive");
    List partnerIDForArchive = (List)criteriaTable.get("Partners");
  	List beIDForArchive = (List)criteriaTable.get("BeIDs");
    Boolean isArchiveOrphan = (Boolean)criteriaTable.get("IsArchiveOrphanRecord");
    
  	long totalProcess = Long.parseLong((String)summaryTable.get("TotalProcessInstanceCount"));
  	long failedCount = Long.parseLong((String)summaryTable.get("FailedProcessInstanceCount"));
  	String archiveID = (String)summaryTable.get("ArchiveID");
    
  	String numProcessArchived = "" +((totalProcess - failedCount) < 0 ? 0 :(totalProcess - failedCount)); 
  	String failedProcess = "" + failedCount;
  	
  	ArchiveAlertProvider provider = new ArchiveAlertProvider(Boolean.TRUE,archiveStartTime,
  			                                  new Date(fromTime.getTime()),new Date(toTime.getTime()),
  			                                  processDefList,includeInComplete, numProcessArchived,
  			                                  isEnableRestoreArchive, isEnableSearchArchive,
  			                                  partnerIDForArchive, beIDForArchive, archiveID, isArchiveOrphan);
  	provider.setTotalFailed(failedProcess);
  	
  	return provider;
  }
  
  //TWX 4 April 2006 provider for archive by docs
  public ArchiveAlertProvider getArchiveByDocumentProvider()
  {
  	Date archiveStartTime = new Date(TimeUtil.localToUtc(Calendar.getInstance().getTimeInMillis())); //the actual day, hour, minute, second
                                                                                                     //have been converted to UTC time     
    //fromTime, toTime are in UTC time
  	Timestamp fromTime = (Timestamp)criteriaTable.get("FromStartTime");
  	Timestamp toTime = (Timestamp)criteriaTable.get("ToStartTime");
  	List folderList = (List)criteriaTable.get("FolderList");
  	List docTypeList = (List)criteriaTable.get("DocTypeList");
  	Boolean isEnableSearchArchive = (Boolean)criteriaTable.get("IsEnableSearchArchive") == null ? false : (Boolean)criteriaTable.get("IsEnableSearchArchive");
    Boolean isEnableRestoreArchive = (Boolean)criteriaTable.get("IsEnableRestoreArchive") == null ? false : (Boolean)criteriaTable.get("IsEnableRestoreArchive");
    List partnerIDForArchive = (List)criteriaTable.get("Partners");
    List beIDForArchive = (List)criteriaTable.get("BeIDs");
    Boolean isArchiveOrphan = (Boolean)criteriaTable.get("IsArchiveOrphanRecord");
    
  	int totalGDOC = summaryTable.get("TotalGridDocumentCount") != null ? Integer.parseInt((String)summaryTable.get("TotalGridDocumentCount")) : 0;
  	int failedGDOC = summaryTable.get("FailedGridDocumentCount") != null ? Integer.parseInt((String)summaryTable.get("FailedGridDocumentCount")) : 0;
    String archiveID = (String)summaryTable.get("ArchiveID");
    
  	String numArchivedDoc = ""+((totalGDOC-failedGDOC) < 0 ? 0 : (totalGDOC-failedGDOC));
  	String numFailedDoc = ""+failedGDOC;
  	
  	ArchiveAlertProvider provider = new ArchiveAlertProvider(Boolean.FALSE, archiveStartTime, 
  			                                   new Date(fromTime.getTime()),new Date(toTime.getTime()),
  			                                   docTypeList, folderList, numArchivedDoc, isEnableRestoreArchive,
  			                                   isEnableSearchArchive, partnerIDForArchive, beIDForArchive, archiveID, isArchiveOrphan);
  	provider.setTotalFailed(numFailedDoc);
  	return provider;
  }
  
  public String getArchiveID()
  {
    return archiveID;
  }
  
  public void setArchiveID(String archiveID)
  {
    this.archiveID = archiveID;
  }
  
  //#45: Retrieve the Root folder for the temp directory
  private String getRootTempDir()
  {
    File tempRoot = null; 
    try
    {
      tempRoot = FileUtil.getFile(IPathConfig.PATH_TEMP, "");
    }
    catch(Exception ex)
    {
      Logger.warn("Can't get the temp folder for archive", ex);
    }
    if(tempRoot != null)
    {
      return tempRoot.getAbsolutePath();
    }
    else
    {
      return "";
    }
  }
}
