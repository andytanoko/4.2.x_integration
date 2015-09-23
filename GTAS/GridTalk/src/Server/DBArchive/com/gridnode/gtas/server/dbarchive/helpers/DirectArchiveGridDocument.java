/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DirectArchiveGridDocument.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Nov 24, 2004 			Mahesh             	Created
 * Sep 02, 2005				Tam Wei Xiang				added in the EStore feature
 * 																				method modified :archive(),archiveGridDoc()
 * Oct 10, 2005       Tam Wei Xiang       All the gdoc, udoc, audit ,attachmentfile and 
 *                                        gdoc ,att db record will be deleted after the archive process
 *                                        has finished.
 *                                        Add in method deleteCacheFile()
 * Mar 29, 2006       Tam Wei Xiang       modified method archiveGridDoc()  
 * Aug 31, 2006       Tam Wei Xiang       Merge from ESTORE stream.     
 * May 18, 2007       Tam Wei Xiang       Support archived by customer                                       
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.folder.ExportFolder;
import com.gridnode.gtas.server.document.folder.ImportFolder;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.folder.SystemFolder;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.TimeUtil;
//added by WX
import com.gridnode.gtas.server.dbarchive.exception.EStoreException;
import com.gridnode.gtas.server.dbarchive.helpers.EStore;
import com.gridnode.gtas.server.dbarchive.helpers.IEStorePathConfig;
import com.gridnode.gtas.server.dbarchive.helpers.ObjSerializeHelper;
import com.gridnode.gtas.server.rnif.model.RNProfile;

public class DirectArchiveGridDocument
{
  
  public static final String PATH_GDOC = "gdoc";
  public static final String PATH_UDOC = "udoc";
  public static final String PATH_AUDIT= "audit";
  public static final String PATH_AS2_AUDIT = PATH_AUDIT+"/AS2";
  public static final String PATH_ATT  = "attachment";

  private List failedGDocUIdList = new ArrayList();
  private Hashtable sysFolderTable = null;
  private IDocumentManagerObj docMgr = null;
  private DbArchive dbArchive = null;
  private int gridDocumentCount;
  private int failedCount;
  private File tempGdocFile=null; // this file is used to create gdoc file if it is missing
  
  private EStore es;
  private ArrayList<Long> attachmentUID; //will store list of attachUID
  private ArrayList<Long> gdocUID; //will store list of gdocUID
  private final String EXPORT = "Export/";
  private final String INBOUND = "Inbound/";
  private final String IMPORT = "Import/";
  private final String OUTBOUND = "Outbound/";
  private final String AUDIT_SUB_PATH = "audit/";
  private final String ATT_SUB_PATH  = "attachment/";
  private final static int PATH_KEY = 0;  //use in Object[] path
  private final static int SUB_PATH = 1;  //same
  private final static int FILE_NAME = 2; //same
  
  private ArrayList<Object[]> fileToBeDeleted; //cache the file path, we will delete all the file after archival process finish
  private ArrayList<String> tempGridDocument; //cache the temp gdoc we serialize
  
  private int numUDocBeEStored = 0;
  private Hashtable criteriaHTable = null;
  private Hashtable summaryHTable = null;
  private boolean isEnableRestoreArchive = false;
  private int _totalRecordWithinInOperator = 0;
  
  public DirectArchiveGridDocument(DbArchive dbArchive) throws Exception
  {
    this.dbArchive = dbArchive;

    sysFolderTable = new Hashtable();
    sysFolderTable.put(ImportFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(ImportFolder.FOLDER_NAME));
    sysFolderTable.put(ExportFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(ExportFolder.FOLDER_NAME));
    sysFolderTable.put(InboundFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(InboundFolder.FOLDER_NAME));
    sysFolderTable.put(OutboundFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(OutboundFolder.FOLDER_NAME));

    docMgr = ArchiveHelper.getDocumentManager();
    tempGdocFile = File.createTempFile("TempGridDoc",".xml");

    attachmentUID = new ArrayList<Long>();
    gdocUID = new ArrayList<Long>();

    fileToBeDeleted = new ArrayList<Object[]>();
    tempGridDocument = new ArrayList<String>();
    
    _totalRecordWithinInOperator = ArchiveHelper.getTotalAllowedEntryWithinInOperator();
  }

  public int getGridDocumentCount()
  {
    return gridDocumentCount;
  }
  
  public int getFailedCount()
  {
    return failedCount;
  }
    
  //modified by TWX
  public void archive(IDataFilter gridDocFilter, EStore es, boolean isEnableRestoreArchive,
                      List<String> partnerForArchive) throws Throwable
  { 
    archive(gridDocFilter,null, true, null, es, isEnableRestoreArchive, partnerForArchive);
  }

  //TWX: add in new variable in method signature: isLastProcessGroup, RNProfileInfoHashTable, estore
  public void archive(IDataFilter gridDocFilter,Collection rnProfileUIdForFailedGDocColl, 
  										boolean isLastProcessGroup, Hashtable rnprofileInfoHTable, EStore estore,
  										boolean isEnableRestoreArchive, List<String> partnerForArchive)
  			 throws Throwable
  {
    try
    {
      EntityDAOImpl gridDocumentDAO = (EntityDAOImpl)getEntityDAO(GridDocument.ENTITY_NAME);
      Collection docKeys = gridDocumentDAO.findByFilter(gridDocFilter);
      int totalDocCount=docKeys.size();
      Logger.log("[DirectArchiveGridDocument.archive] Total griddocuments to archive = "+totalDocCount);
      
      int remainingCount = totalDocCount;
      int maxCount = 100;
      List docUIdSubList = new ArrayList();
      
      //TWX
      es = estore;
      this.isEnableRestoreArchive = isEnableRestoreArchive;
      
      boolean isLastGDocGroup = false;
      boolean isArchivedByDoc = rnProfileUIdForFailedGDocColl == null;
      
      
      //TWX 30032006 Trigger archive by DOC startup alert
      if(rnProfileUIdForFailedGDocColl==null)
      {
      	triggerArchiveStartUpAlert(totalDocCount, isEnableRestoreArchive, estore.getIsEstoreEnabled(), partnerForArchive);
      }
      
      for (Iterator i = docKeys.iterator(); i.hasNext();)
      {
        docUIdSubList.add(i.next());
        
        if(docUIdSubList.size()==maxCount || !i.hasNext())
        {
          int subListSize = docUIdSubList.size(); 
          Logger.debug("[DirectArchiveGridDocument.archive] remaining "+remainingCount+" of "+totalDocCount);
          
          //TWX
          if(!i.hasNext() && isLastProcessGroup)
          {
          	isLastGDocGroup = true;
          }
          archiveGridDoc(docUIdSubList,rnProfileUIdForFailedGDocColl, 
          		           isLastGDocGroup, rnprofileInfoHTable);
          
          //07092006 TWX delete the cache while we finished processing the current batch. Since we sure all the doc
          //             in the current bactch has been archived/estored
          if(isArchivedByDoc)
          {
          	Logger.log("[DirectArchiveGridDocument.archive] isArchivedByDoc delete by batch");
          	deleteCacheFile();
          }
          
          docUIdSubList.clear();
          remainingCount=remainingCount-subListSize;
        }        
      }
    }
    catch (Throwable th)
    {
      dbArchive.err("[DirectArchiveGridDocument.archive] Error in archive, gridDocFilter=" + gridDocFilter.getFilterExpr(), th);
      throw th;
    }
  }

  //modified by TWX
  private void archiveGridDoc(Collection docUIdColl,Collection rnProfileUIdForFailedGDocColl, 
  		                        boolean isLastGDocGroup, Hashtable<Long, RNProfile> rnprofileHTable)
  				throws Throwable
  {
    Logger.log("[DirectArchiveGridDocument.archiveGridDoc] Enter, docUIdColl size=" + docUIdColl.size());
    try
    {
      if(docUIdColl==null || docUIdColl.isEmpty())
        return;
      IDataFilter gdocFilter = new DataFilterImpl();
      gdocFilter.addDomainFilter(null,GridDocument.UID,docUIdColl,false);
      EntityDAOImpl gridDocumentDAO = (EntityDAOImpl)getEntityDAO(GridDocument.ENTITY_NAME);
      Collection<GridDocument> gdocColl = gridDocumentDAO.getEntityByFilter(gdocFilter);
      
      //by TWX
      boolean isEStoreEnabled = es.getIsEstoreEnabled();
      boolean isArchivedByPI = rnProfileUIdForFailedGDocColl != null;
      
      for(Iterator<GridDocument> i= gdocColl.iterator();i.hasNext();)
      {
      	String[] gdocFilesArray = new String[5]; //0 GDOC file, 1 UDOC file, 2 AUDIT file, 3 Receipt Audit File(for AS2),4 Attachment filename(0...*)
//        gridDocumentCount++;
        GridDocument gDoc = i.next();
        
        Long docUId = (Long)gDoc.getKey();
        Long gDocId = gDoc.getGdocId();
        Logger.log("[DirectArchiveGridDocument.archiveGridDoc] Enter, docUId=" + docUId +", gDocId="+gDocId);
        try
        {
          List<Object[]> filesToAdd= new ArrayList<Object[]>(3);
          //get the gDoc absolute filepath and zip it
          File gDocFile = getGdocFile(gDoc,true);
          if (gDocFile != null && gDocFile.exists())
          {
            String fileName = gDoc.getGdocFilename();
            String category = PATH_GDOC + "/";
            addFilesToBeZipped(new Object[]{gDocFile,fileName,category}, filesToAdd);
            gdocFilesArray[0] = gDocFile.getAbsolutePath();
          }
          else
          {
            dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] gDocFile doesnot exists for docUId=" + docUId+ ", gDocId=" + gDocId + ", gDocFile=" + gDocFile);
            gdocFilesArray[0] = gDoc.getGdocFilename();
          }
          //get the uDoc absolute filepath and zip it
          File uDocFile = FileHelper.getUdocFile(gDoc);
          if (uDocFile != null && uDocFile.exists())
          {
            String fileName = gDoc.getUdocFilename();
            String category = PATH_UDOC + "/" + gDoc.getFolder() + "/";
            addFilesToBeZipped(new Object[]{uDocFile,fileName,category}, filesToAdd);
            gdocFilesArray[1] = uDocFile.getAbsolutePath();
          }
          else
          {
          	if( (gDoc.getTempUdocFilename()==null && gDoc.getUdocFilename() == null) || 
          			          ("".equals(gDoc.getTempUdocFilename()) && "".equals(gDoc.getUdocFilename())))
          	{
          		gdocFilesArray[1] = ""; //indicate the estore the gdoc actually dun have udoc, so no need to note down in remark.
          	}
          	else if(gDoc.getTempUdocFilename() != null && gDoc.getTempUdocFilename().length() > 0)
          	{
          		gdocFilesArray[1] = gDoc.getTempUdocFilename();
          	}
          	else if(gDoc.getUdocFilename() != null && gDoc.getUdocFilename().length() > 0)
          	{
          		gdocFilesArray[1] = gDoc.getUdocFilename();
          	}
            dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] uDocFile doesnot exists for docUId=" + docUId + ", gDocId=" + gDocId+ ", UdocFilename="+gDoc.getUdocFilename()+", uDocFile=" + uDocFile);
          }
          
          //get the auditFile absolute filepath and zip it
          //audit file only exist in inbound or outbound. Export only make a reference to it.
          String auditFileName = gDoc.getAuditFileName();
          handleAuditFile(IDocumentPathConfig.PATH_AUDIT, auditFileName,gdocFilesArray, filesToAdd, 2, PATH_AUDIT+"/");
          
          //handle receipt audit file. Needed in AS2. receiptAuditFilename = /AS2/filename
          String receiptAuditFilename = gDoc.getReceiptAuditFileName();
          handleAuditFile(IDocumentPathConfig.PATH_AUDIT, receiptAuditFilename, gdocFilesArray, filesToAdd, 3,PATH_AUDIT+"/");
          
          Collection attachementsColl = null;
          if (gDoc.hasAttachment().booleanValue())
          {
            //archive all attachements belonging to GridDocument
            try
            {
              attachementsColl=archiveAttachments(gDoc.getAttachments(),filesToAdd, gdocFilesArray);
            }
            catch(Throwable th)
            {
              dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] Error while archiveing attachements for GridDocument with gDocId=" + gDocId,th);
            }
          }
          
          //TWX 04092006 Store into estore
          if(isEStoreEnabled && isArchivedByPI &&
          		   (gDoc.getFolder().equals("Inbound") || gDoc.getFolder().equals("Outbound") || gDoc.getFolder().equals("Export")))
          {
          	populateIntoEStore(gDoc, rnprofileHTable, gdocFilesArray);
          }
          else if(isEStoreEnabled && ! isArchivedByPI) //Archive By Doc
          {
          	populateIntoEStore(gDoc, null, gdocFilesArray);
          }
          
          if(isEnableRestoreArchive)
          {
          	dbArchive.addFilesToZip(filesToAdd); //using this method will ensure all these files will be in one zip file 
          }
          
          //TWX: the old delete logic will cause problem(all the audit file, db record have already been deleted, we can't restart the estore process again)
          //     if EStore side fail.
          //     Start caching gdoc, udoc, audit, receipt audit
          
          addFileToBeDeleted(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder()+File.separator, gDoc.getGdocFilename(), fileToBeDeleted);
          addFileToBeDeleted(IDocumentPathConfig.PATH_UDOC,gDoc.getFolder()+File.separator,gDoc.getUdocFilename(),fileToBeDeleted);
          
          if(auditFileName != null && auditFileName.length() > 0)
          {
          	String auditFName = FileUtil.extractFilename(auditFileName); //To handle AS2 case
          	String auditSubPath = FileUtil.extractPath(auditFileName);
          	addFileToBeDeleted(IDocumentPathConfig.PATH_AUDIT, auditSubPath, auditFName, fileToBeDeleted);
          }
        	
        	if(receiptAuditFilename != null && receiptAuditFilename.length() > 0)
          {
        		String receiptFilename = FileUtil.extractFilename(receiptAuditFilename);
        		String receiptSubPath = FileUtil.extractPath(receiptAuditFilename);
        		addFileToBeDeleted(IDocumentPathConfig.PATH_AUDIT, receiptSubPath, receiptFilename, fileToBeDeleted);
          }
          
          //cache attachment physical filepath and its record UID
          if(attachementsColl!=null && attachementsColl.size()>0)
          {
          	try
            {
              for(Iterator j= attachementsColl.iterator();j.hasNext();)
              {
                Attachment attachment = (Attachment)j.next();
                String fileName = attachment.getFilename();
                addFileToBeDeleted(IDocumentPathConfig.PATH_ATTACHMENT, "" ,fileName, fileToBeDeleted);
              }
              
              if(gDoc.getAttachments().size()>0)
              {
              	addAttachmentUID(attachmentUID, gDoc.getAttachments());
              	Logger.log("[DirectArchiveGridDocument.archiveGridDoc ] caching attachment UID "+gDoc.getAttachments());
              }
            }
            catch(Throwable th)
            {
              dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] Error while deleting attachements for GridDocument with gDocId=" + gDocId,th);
            }

          }
          gridDocumentCount++;
          
        }
        catch(Throwable th)
        {
          failedCount++;
          docUIdColl.remove(docUId);
          if(rnProfileUIdForFailedGDocColl!=null && gDoc.getRnProfileUid()!=null)
            rnProfileUIdForFailedGDocColl.add(gDoc.getRnProfileUid());
          dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] Error for GridDocument with docUId="+docUId+", GDocId="+gDocId,th);
          throw th;
        }
      }
      
      //Cache the docUID, start delete it when estore process has been finished.
      if(docUIdColl.size()>0)
      {
      	Iterator ite = docUIdColl.iterator();
      	while(ite.hasNext())
      	{
      		Long uid = (Long)ite.next();
      		//Logger.log("[DirectArchiveGridDocument.archiveGridDoc] Caching gdoc record with uid "+uid);
      		gdocUID.add(uid);      		
      	}
      }
    }
    catch(Throwable th)
    {
      dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] Error while archiveing, docUIdColl="+docUIdColl,th);
      throw th;
    }
  }
  
  //add the attachment uid to the arraylist that we use to cache.
  private void addAttachmentUID(ArrayList<Long> attachment, List attUIDList)
  {
  	for(int i = 0; i <  attUIDList.size(); i++)
  	{
  		//attachment.add((Long)attUIDList.get(i));
  		Logger.log("ATT UID is "+attUIDList.get(i).toString());
  		attachment.add(new Long(attUIDList.get(i).toString()));
  	}
  }

  
  /**
   * Populate the GDoc correspond docs into ESTORE. They include gdocs, udocs, audit files, attachments.
   * @param gDoc 
   * @param rnprofileHTB
   * @param gdocFilesArray keep track of the abs path for audit, gdoc, udoc and filename for attachment
   * @throws Exception
   */
  private void populateIntoEStore(GridDocument gDoc, Hashtable<Long, RNProfile> rnprofileHTB, String[] gdocFilesArray) throws Exception
  {
    try
    {
    	es.populateDoc(gDoc, rnprofileHTB, gdocFilesArray);
    }
    catch(Exception exc)
    {
    	Logger.warn("[DirectArchiveGridDocument.archiveGridDoc ] error caching audit file.", exc);
    	throw new EStoreException("[DirectArchiveGridDoc.archiveGDoc] Error occured while caching the audit file",exc);
    }
  }
  
  private void addFileToBeDeleted(String pathKey, String subFolder, String filename, ArrayList<Object[]> filesToBeDeleted)
  	throws Exception
  {
  	if(filename == null || filename.trim().length() == 0)
  	{
  		return;
  	}
  	
  	boolean isFileExist = FileUtil.exist(pathKey, subFolder, filename);
  	if(isFileExist)
  	{
  		filesToBeDeleted.add(new Object[]{pathKey, subFolder, filename});
  	}
  }
  
  /**
   * Handling the audit file
   * @param pathKey Path key which represent the folder path for audit file or receipt audit file.
   * @param auditFilename Name of the audit file
   * @param gdocFilesArray Store the abs file path for gdoc related file(include gdoc, udoc, audit, attachment)
   * @param filesToAdd Indicate the files that will be added into archived.zip
   * @param index The index where we will store the audit abs filepath. (index 2 AUDIT file, 3 Receipt Audit File)
   * @throws Exception
   */
  private void handleAuditFile(String pathKey, String auditFilename,
                               String[] gdocFilesArray, List<Object[]> filesToAdd, int index, String category)
  	throws Exception
  {
  	if(auditFilename!=null && auditFilename.trim().length()>0)
    {
//      StringTokenizer strTok = new StringTokenizer(auditFileNames,";"); //TWX 05092006 RNIF will always create a new gdoc during resend case. AS2 is also no longer append the audit filename
//      while(strTok.hasMoreTokens())
//      {
//        String auditFileName = strTok.nextToken();
        File auditFile = FileUtil.getFile(pathKey, auditFilename);
        
        if(auditFile!=null)
        {
        	String auditSubPath = FileUtil.extractPath(auditFilename);
          String fileName = auditFile.getName();
          addFilesToBeZipped(new Object[]{auditFile,fileName,category+auditSubPath}, filesToAdd);
          gdocFilesArray[index] = auditFile.getAbsolutePath();
        }
        else
        {
        	gdocFilesArray[index] = auditFilename;
        }
//      }
    }
    else if(auditFilename == null || "".equals(auditFilename))
    {
    	gdocFilesArray[index] = ""; //indicate the estore the gdoc actually dun have audit file, so no need to note down in remark.
    }
  }
  
  /**
   * 
   * @param attachmentUIdList
   * @param filesToAdd
   * @param gdocFilesArray an array which we use to keep track the attachment file that correspond to a GDOC. 
   *                       It will be used while processing estore.
   * @return
   * @throws Exception
   */
  private Collection archiveAttachments(List attachmentUIdList,List<Object[]> filesToAdd, String[] gdocFilesArray) throws Exception
  {
    Collection attachementsColl = null;
    //archive all attachements belonging to above set of GridDocuments
    if(attachmentUIdList!=null && attachmentUIdList.size()>0)
    {
      IDataFilter attachementFilter = new DataFilterImpl();
      attachementFilter.addDomainFilter(null,Attachment.UID,attachmentUIdList,false);
        
      EntityDAOImpl attachementDAO = (EntityDAOImpl)getEntityDAO(Attachment.ENTITY_NAME);
      attachementsColl = attachementDAO.getEntityByFilter(attachementFilter);
        
      Iterator i = attachementsColl.iterator();  
      while (i.hasNext())
      {
        Long attachmentUId = null;
        try
        {
          Attachment attachment = (Attachment)i.next();
          attachmentUId = (Long)attachment.getKey();
             
          String fileName = attachment.getFilename();
          File attachmentFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, fileName);
          if (attachmentFile != null)
          {
            String category = PATH_ATT + "/" + attachmentUId + "/";
            addFilesToBeZipped(new Object[]{attachmentFile,fileName,category}, filesToAdd);
            //dbArchive.addFileToZip(attachmentFile,fileName,category);
            //FileUtil.delete(IDocumentPathConfig.PATH_ATTACHMENT, fileName);
          }
          else
          {
          	Logger.debug("[DirectArchiveGridDocument.archiveGridDoc] attachmentFile doesnot exists for attachmentUId="+ attachmentUId+ ", attachmentFile="+ attachmentFile);
          }
          
          //TWX 05092006
          String attachmentFilenames = gdocFilesArray[4];
          if(attachmentFilenames == null)
          {
          	attachmentFilenames = fileName;
          }
          else
          {
          	attachmentFilenames += ":"+fileName;
          }
          gdocFilesArray[4] = attachmentFilenames;
        }
        catch (Throwable th)
        {
          dbArchive.err("[DirectArchiveGridDocument.archiveGridDoc] Error while archiveing attachment, attachmentUId:" + attachmentUId, th);
        }
      }
    }
    return attachementsColl;
  }

  public void restoreGridDocs(ArrayList filenames)
  {
    
    int maxcount = 100;
    List gDocList = new ArrayList();
    //insert GridDocument to database and handle gDoc & uDoc.
    for (int i = 0; i < filenames.size(); i++)
    {
      String filename = (String) filenames.get(i);
      //if this is a gDoc file(not a uDoc file)
      if (filename!=null && filename.startsWith(dbArchive.TMP_GDOC_DIR))
      {
        gridDocumentCount++;
        GridDocument gDoc = new GridDocument();
        try
        { //construct GridDocument entity from gDoc file
          gDoc = (GridDocument) gDoc.deserialize(filename);
          if(gDoc!=null)
          {
            //here i am setting the complete file path because i need it to
            //get the timestamp of the file in later part
            gDoc.setGdocFilename(filename);
            
            gDocList.add(gDoc);       
          } 
          else throw new Exception("gDoc.deserialize returned null");
        }
        catch(Throwable th)
        {
          failedCount++;  
          dbArchive.err("[DirectArchiveGridDocument.restoreGridDocs] Error while restoring filename=" + filename, th);      
        }
      }
      
      if(gDocList.size()==maxcount || (i+1)==filenames.size())
      {
        importGridDocuments(gDocList);
        gDocList.clear();
      }
    }
  }

  private void importGridDocuments(List gDocList)
  {
    if(gDocList.size()==0)
      return;
    try
    {
      IDataFilter gDocfilter = null;
      for(Iterator i = gDocList.iterator();i.hasNext();) //collect all gdocId
      {
        GridDocument gDoc = (GridDocument)i.next();
        
        IDataFilter tempGDocfilter = new DataFilterImpl();
        tempGDocfilter.addSingleFilter(null,GridDocument.G_DOC_ID,tempGDocfilter.getEqualOperator(),gDoc.getGdocId(),false);
        tempGDocfilter.addSingleFilter(tempGDocfilter.getAndConnector(),GridDocument.FOLDER,tempGDocfilter.getEqualOperator(),gDoc.getFolder(),false);
        if(gDocfilter==null)
          gDocfilter=tempGDocfilter;
        else
        {
          //gDocfilter.addFilter(gDocfilter.getOrConnector(),tempGDocfilter);
          gDocfilter=new DataFilterImpl(gDocfilter,gDocfilter.getOrConnector(),tempGDocfilter);
        }
      }
      
      EntityDAOImpl gridDocumentDAO = (EntityDAOImpl)getEntityDAO(GridDocument.ENTITY_NAME);
      Collection existingGDocColl = gridDocumentDAO.getEntityByFilter(gDocfilter);
      
      for(Iterator i = gDocList.iterator();i.hasNext();) //restore only those that are not existing
      {
        GridDocument gDocFromArchiveFile = (GridDocument)i.next();
        if(!checkInExistingGridDoc(gDocFromArchiveFile,existingGDocColl))
        {
          try
          { 
            importGridDocument(gDocFromArchiveFile);
          }
          catch (Exception ex)
          {
            failedCount++;
            dbArchive.err("[DirectArchiveGridDocument.importGridDocuments] Error restoring GridDocument with GDOCID=" + gDocFromArchiveFile.getGdocId() + " AND FOLDER= " + gDocFromArchiveFile.getFolder(), ex);
          }
        }
        else
        {
          failedCount++;
          dbArchive.err("[DirectArchiveGridDocument.importGridDocuments] GridDocument with GDOCID=" + gDocFromArchiveFile.getGdocId() + " AND FOLDER= " + gDocFromArchiveFile.getFolder() + " already exist! ");
        }
         
      }
    }
    catch (Exception ex)
    {
      dbArchive.err("[DirectArchiveGridDocument.importGridDocuments] Error restoring, gDocList="+gDocList, ex);
    }
  }
  
  private boolean checkInExistingGridDoc(GridDocument gDoc,Collection existingGDocColl)
  {
    if(existingGDocColl!=null)
    {
      for(Iterator i = existingGDocColl.iterator();i.hasNext();)
      {
        GridDocument extGDoc = (GridDocument)i.next();
        if(extGDoc.getGdocId().equals(gDoc.getGdocId()) && extGDoc.getFolder().equals(gDoc.getFolder()))
          return true;
      }
    }
    return false;
  }
  
  /**
   * Create GridDocument entity from gdoc file and insert the entity
   * into database
   * @param fileName The name of gdoc xml file
   */
  private void importGridDocument(GridDocument gDoc) throws Exception
  {
    boolean regenGDocId = false;
    Number gDocId = gDoc.getGdocId();
    String folder = (String) gDoc.getFolder();
    boolean receiptAuditFileCreated = false;
    
    //construct new udoc filename
    boolean uDocFileCreated=false;
    String uDocFilename = (String) gDoc.getFieldValue(GridDocument.U_DOC_FILENAME);
    File tmpUDocFile = new File(dbArchive.TMP_UDOC_DIR + DbArchive.FILE_SEPARATOR + folder + DbArchive.FILE_SEPARATOR + uDocFilename);
    if (tmpUDocFile.isFile())
    {
      //save udoc file to respective directory
      if(!FileUtil.exist(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename))
      {
        uDocFilename = FileUtil.create(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename, tmpUDocFile);
        File newUDocFile = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename);
        if(newUDocFile!=null)
          newUDocFile.setLastModified(tmpUDocFile.lastModified());
        uDocFileCreated=true;          
      }
    }
    gDoc.setFieldValue(GridDocument.U_DOC_FILENAME, uDocFilename);

    //construct audit filename
    List auditFileNameList = new ArrayList();
    String auditFileNames = (String) gDoc.getFieldValue(GridDocument.AUDIT_FILE_NAME);
    if(auditFileNames!=null && auditFileNames.trim().length()>0)
    {
      StringTokenizer strTok = new StringTokenizer(auditFileNames,";");
      auditFileNames="";
      while(strTok.hasMoreTokens())
      {
        String auditFileName=strTok.nextToken();
        if(auditFileName.trim().length()>0)
        {
        	String auditSubPath = "";
          try
          {
            File tmpAuditFile = new File(dbArchive.TMP_AUDIT_DIR + DbArchive.FILE_SEPARATOR + auditFileName);
            if(tmpAuditFile.exists())
            {
            	auditSubPath = FileUtil.extractPath(auditFileName); //For AS2, there will be a subPath follow by audit filename eg AS2/filename
              auditFileName = FileUtil.create(IDocumentPathConfig.PATH_AUDIT , auditFileName, tmpAuditFile);
              File newAuditFile = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, auditSubPath ,auditFileName);
              if(newAuditFile!=null)
              {
                newAuditFile.setLastModified(tmpAuditFile.lastModified());
              }
            }
          }
          catch(Exception ex)
          {
            dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error restoring auditfile, auditFileName="+auditFileName, ex);      
          }
          auditFileNameList.add(auditSubPath+auditFileName);
          auditFileNames+=auditSubPath+auditFileName+(strTok.hasMoreTokens()?";":"");
        }
      }
    }
    
    //TWX 12092006 support for AS2 receipt audit filename
    String receiptAuditFilename = (String)gDoc.getFieldValue(GridDocument.RECEIPT_AUDIT_FILE_NAME); //AS2/filename
    String receiptAuditSubPath = "";
    if(receiptAuditFilename != null && receiptAuditFilename.trim().length() > 0)
    {
    	try
    	{
    		File tmpReceiptAuditFile = new File(dbArchive.TMP_AUDIT_DIR+DbArchive.FILE_SEPARATOR+receiptAuditFilename);
    		if(tmpReceiptAuditFile.exists())
    		{
    			receiptAuditSubPath = FileUtil.extractPath(receiptAuditFilename);
    			receiptAuditFilename = FileUtil.create(IDocumentPathConfig.PATH_AUDIT, receiptAuditFilename, tmpReceiptAuditFile);
    			File newReceiptAuditFile = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT,receiptAuditSubPath, receiptAuditFilename);
    			if(newReceiptAuditFile != null)
    			{
    				newReceiptAuditFile.setLastModified(tmpReceiptAuditFile.lastModified());
    				//gDoc.setReceiptAuditFileName("AS2/"+receiptAuditFilename);
    				gDoc.setFieldValue(GridDocument.RECEIPT_AUDIT_FILE_NAME, receiptAuditSubPath+receiptAuditFilename);
    			}
    			receiptAuditFileCreated = true;
    		}
    	}
    	catch(Exception ex)
    	{
    		dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error restoring receipt auditfile, receipt auditFileName="+receiptAuditFilename, ex);
    	}
    }
    
    gDoc.setFieldValue(GridDocument.AUDIT_FILE_NAME, auditFileNames);

    Hashtable attachmentTable = new Hashtable();
    //construct attachments
    if (gDoc.hasAttachment().booleanValue())
    {
      try
      {
        gDoc = processAttachments(gDoc,attachmentTable);
      }
      catch (Exception ex)
      {
        dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error restoring attachments", ex);
      }
    }

    String gDocFileNameFromArchive = gDoc.getGdocFilename();
    //construct new gdoc filename
    String gdocFilename = getGdocFilename(gDoc);
    gDoc.setGdocFilename(gdocFilename);    
    
    File tempGdoc = tempGdocFile;
    String gdocFullPath = tempGdoc.getAbsolutePath();
    gDoc.serialize(gdocFullPath);
    
    FileInputStream fis = new FileInputStream(tempGdoc);  //KHS20030327
    String newGdocFileName = FileUtil.create(IDocumentPathConfig.PATH_GDOC,gDoc.getFolder()+File.separator,gdocFilename,fis);
    //MAHESH: The logic in document manager class is not setting the new gdoc  
    //        filename to gdoc object so i am also not setting it.         
    fis.close();        //KHS20030327
    tempGdoc.delete();  //KHS20030327
    File newGdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gDoc.getGdocFilename());
    if(newGdocFile!=null)
    {
      newGdocFile.setLastModified((new File(gDocFileNameFromArchive)).lastModified());
    }
    
    try
    { 
      EntityDAOImpl gridDocumentDAO = (EntityDAOImpl)getEntityDAO(GridDocument.ENTITY_NAME);
      //create GridDocument entity
      
      //this way of creating griddocument is too slow
      //gridDocumentDAO.create(gDoc);  
      Long primaryKey = gridDocumentDAO.createNewKey(false);
      gDoc.setKey(primaryKey);
      gridDocumentDAO.create(gDoc,true);
    }
    catch(Exception ex)
    {
      if(uDocFileCreated)
        FileUtil.delete(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename);
      if(!auditFileNameList.isEmpty())
      {
        for(Iterator i = auditFileNameList.iterator();i.hasNext();)
        {
          String auditFileName = (String)i.next();
          try
          {
            FileUtil.delete(IDocumentPathConfig.PATH_AUDIT,auditFileName);
          }
          catch(Throwable th)
          {
            dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error while deleting auditfile ="+auditFileName, th);
          }
        }      
      }
      
      if(receiptAuditFileCreated)
      {
      	try
      	{
      		FileUtil.delete(IDocumentPathConfig.PATH_AUDIT, receiptAuditSubPath,receiptAuditFilename);
      	}
      	catch(Throwable th)
      	{
      		dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error while deleting receipt auditfile ="+receiptAuditSubPath+receiptAuditFilename);
      	}
      }
      
      if(attachmentTable.size()>0)
      {
        List attUIdList = new ArrayList();
        Enumeration enu = attachmentTable.elements();
        while (enu.hasMoreElements())
        {
          Attachment att = (Attachment) enu.nextElement();
          attUIdList.add(att.getKey());
          try
          {
            if(FileUtil.exist(IDocumentPathConfig.PATH_ATTACHMENT, att.getFilename()))
              FileUtil.delete(IDocumentPathConfig.PATH_ATTACHMENT, att.getFilename());  
          }
          catch(Throwable th)
          {
            dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error while deleting attachement file ="+att.getFilename(), th);
          }
        }
        try
        {
          IDataFilter attachementFilter = new DataFilterImpl();
          attachementFilter.addDomainFilter(null,Attachment.UID,attUIdList,false);
          EntityDAOImpl attachmentDAO = (EntityDAOImpl)getEntityDAO(Attachment.ENTITY_NAME);
          attachmentDAO.removeByFilter(attachementFilter);    
        }
        catch(Throwable th)
        {
          dbArchive.err("[DirectArchiveGridDocument.importGridDocument] Error while deleting attachement entities, attUIdList="+attUIdList, th);
        }
      }
      throw ex;
    }
  }

  private GridDocument processAttachments(GridDocument gDoc,Hashtable attachmentTable) throws Exception
  {
    List oldAttUIdList = gDoc.getAttachments();
    if(oldAttUIdList==null || oldAttUIdList.size()==0)
      return gDoc;
    ArrayList newAttUids = new ArrayList();  
    Iterator i = oldAttUIdList.iterator();
    while (i.hasNext())
    {
      Long attUid = new Long(i.next().toString());
      //no need to check again
      //IDataFilter filter = new DataFilterImpl();
      //filter.addSingleFilter(null, Attachment.UID, filter.getEqualOperator(), attUid, false);
      //Collection attList = docMgr.findAttachments(filter);
      try
      {
        Long newAttUid = constructAttachment(attUid, gDoc,attachmentTable);
        newAttUids.add(newAttUid);
      }
      catch(Exception ex)
      {
        dbArchive.err("[DirectArchiveGridDocument.processAttachments] Error while constructAttachment, attUid="+attUid,ex);
      }
    }
    gDoc.setAttachments(newAttUids);
    return gDoc;
  }

  private Long constructAttachment(Long orgUid, GridDocument gDoc,Hashtable attachmentTable) throws Exception
  {
    Long newAttUid = (Long) attachmentTable.get(orgUid);
    if (newAttUid == null)
    {
      File tmpAttDir = new File(dbArchive.TMP_ATT_DIR + DbArchive.FILE_SEPARATOR + orgUid);
      if (tmpAttDir.exists())
      {
        File[] tmpAttFiles = tmpAttDir.listFiles();
        if (tmpAttFiles.length > 0)
        {
          File tmpAttFile = tmpAttFiles[0];
          String attFilename =
            FileUtil.create(IDocumentPathConfig.PATH_ATTACHMENT, tmpAttFile.getName(), new FileInputStream(tmpAttFile));
          Attachment att = new Attachment();
          att.setOriginalFilename(tmpAttFile.getName());
          att.setFilename(attFilename);
          if (gDoc.getFolder().equals(InboundFolder.FOLDER_NAME))
          {
            att.setOutgoing(Boolean.FALSE);
          }
          else
          {
            att.setOutgoing(Boolean.TRUE);
          }
          EntityDAOImpl attachmentDAO = (EntityDAOImpl)getEntityDAO(Attachment.ENTITY_NAME);
          newAttUid = attachmentDAO.create(att);
          att.setKey(newAttUid);
          attachmentTable.put(orgUid, att);
        }
      }
    }
    return newAttUid;
  }
  
  
  private File getGdocFile(GridDocument gDoc,boolean generateNew) throws Exception
  {
    File gdocFile = null;
    String gdocFilename = gDoc.getGdocFilename();

    if (gdocFilename == null || gdocFilename.trim().length() == 0)
    {
      gdocFilename = getGdocFilename(gDoc);
      gDoc.setGdocFilename(gdocFilename);
      gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gdocFilename);
    }
    else
    {
      gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gdocFilename);
      
      if(gdocFile==null)
      {
        String newGdocFilename = getGdocFilename(gDoc);
        if(!newGdocFilename.equals(gdocFilename))
        {
          gdocFilename = newGdocFilename;
          gDoc.setGdocFilename(gdocFilename);
          gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gdocFilename);
        }
      }
    }

    if (gdocFile == null && generateNew) //if still gdocFile is null create the gdocFile
    {
    	/*
    	 * TWX 07092006 For archive by ProcessInstance, due to we will process the gdoc associated files
    	 *              in later stage, we have to ensure that each temp gdoc we serialized out is still exist. 
      gDoc.serialize(tempGdocFile.getAbsolutePath());
      gdocFile = tempGdocFile;
      */
    	File tempGdoc = File.createTempFile("TempGridDoc",".xml");
    	gDoc.serialize(tempGdoc.getAbsolutePath());
      gdocFile = tempGdoc;
      tempGridDocument.add(tempGdoc.getAbsolutePath());
    }
    return gdocFile;
  }

  private String getGdocFilename(GridDocument gDoc) throws Exception
  {
    SystemFolder sysFolder = (SystemFolder) sysFolderTable.get(gDoc.getFolder());
    return sysFolder.getGdocFilename(gDoc);
  }
  
  private IEntityDAO getEntityDAO(String entityName)
  {
    IEntityDAO entityDAO = EntityDAOFactory.getInstance().getDAOFor(entityName);
    return entityDAO;
  }  
  
  /**
   * TWX: delete the files(physical gdoc, udoc, audit, attachment)/ db records(gdoc, udoc, audit, attachment)
   *      we cache so far
   */
  public void deleteCacheFile() throws Exception
  {
  	Logger.log("[DirectArchiveGridDocument.deleteCacheFile] start delete cache file");
  	
  	//DELETE the files(physical gdoc, udoc, audit, attachment) we cache
  	deleteDoc(fileToBeDeleted);
  	Logger.log("[DirectArchiveGridDocument.deleteCacheFile] total file to be deleted "+fileToBeDeleted.size());
  	
  	//DELETE the gdoc record
    Logger.log("[[DirectArchiveGridDocument.deleteCacheFile]] start DELETE gdoc. total gdoc record be deleted "+gdocUID.size());
  	deleteDBRecord(gdocUID, (Integer)GridDocument.UID, GridDocument.ENTITY_NAME);
  	Logger.log("[[DirectArchiveGridDocument.deleteCacheFile]] end total gdoc record be deleted "+gdocUID.size());
  	
  	//DELETE the attachment record
    Logger.log("[DirectArchiveGridDocument.deleteCacheFile] start DELETE attachment. total attachment be deleted "+attachmentUID.size());
  	deleteDBRecord(attachmentUID, (Integer)Attachment.UID, Attachment.ENTITY_NAME);
  	Logger.log("[DirectArchiveGridDocument.deleteCacheFile] end total attachment be deleted "+attachmentUID.size());
  	
  	//DELETE the temp gdoc we serialized
  	for(int i = 0; i < tempGridDocument.size(); i++)
  	{
  		File f = new File(tempGridDocument.get(i));
  		boolean isDeleted = f.delete();
  		Logger.log("[DirectArchiveGridDocument.deleteCacheFile] Delete status for gdoc temp file "+ f.getAbsolutePath()+" is "+isDeleted);
  	}
  	
  	fileToBeDeleted.clear();
  	gdocUID.clear();
  	attachmentUID.clear();
  	tempGridDocument.clear();
  	
  	Logger.log("[DirectArchiveGridDocument.deleteCacheFile] end delete cache file");
  }
  
  /**
   * Delete the physical documents include audit, udoc, gdoc file given the doc info.
   * @param fileToBeDeleted contain list of object []. object[0] = pathKey, object[1]= subPath
   *                                           object[2] = filename 
   */
  private void deleteDoc(ArrayList fileToBeDeleted)
  {
  	for(int i=0; fileToBeDeleted!=null && i < fileToBeDeleted.size(); i++)
  	{
  		Object path [] = (Object[])fileToBeDeleted.get(i);
  		try
  		{
        String pathKey = (String)path[PATH_KEY];
        String subPath = (String)path[SUB_PATH];
        String filename = (String)path[FILE_NAME];
        if(FileUtil.exist(pathKey, subPath, filename))
        {
          FileUtil.delete(pathKey, subPath, filename);
        }
        else
        {
          Logger.log("[DirectArchiveGridDocument.deleteDoc] File not exist. Maybe it is deleted previously since the file can be shared by some gdoc.");
        }
  		}
  		catch(Exception ex)
  		{
  			Logger.warn("[DirectArchiveGridDocument.deleteDoc] Error delete doc file with filename "+(String)path[SUB_PATH]+"/"+(String)path[FILE_NAME], ex);
  		}
  	}
  }
  
  /**
   * TWX Delete the db record include gdoc, attachment if any
   * @param uid a list of uid that will be deleted
   * @param field the field which involve in delete criteria
   * @param entityName the entity name that we are gonna to remove from db
   * @throws SystemException
   */
  private void deleteDBRecord(ArrayList uid, Integer field, String entityName)
  	throws SystemException
  {
  	if(uid == null || uid.isEmpty())
  	{
  		return;
  	}
  	
    if(uid.size() <= _totalRecordWithinInOperator)
    {
      
      IDataFilter recordFilter = new DataFilterImpl();
      recordFilter.addDomainFilter(null,field,uid,false);
      EntityDAOImpl dao = (EntityDAOImpl)getEntityDAO(entityName);
      try
      {
        dao.removeByFilter(recordFilter);
      }
      catch(Exception ex)
      {
        throw new SystemException("[DirectArchiveGridDocument.deleteDBRecord] error while deleting "+ entityName+ "'s db record.",ex);
      }
    }
    else
    {
      int deletedSoFar = 0;
      while(deletedSoFar < uid.size())
      {
        ArrayList batchToBeDelete = new ArrayList();
        int currentInBatch = 0;
        while(currentInBatch < _totalRecordWithinInOperator && deletedSoFar < uid.size())
        {
          batchToBeDelete.add(uid.get(deletedSoFar));
          ++deletedSoFar;
          ++currentInBatch;
        }
        deleteDBRecord(batchToBeDelete, field, entityName);
      }
    }
  }
  
  //twx
  private String folderInfer(String folder)
  {
  	if(folder.equals("Inbound"))
  	{
  		return this.INBOUND;
  	}
  	else if(folder.equals("Export"))
  	{
  		return this.EXPORT;
  	}
  	else if(folder.equals("Import"))
  	{
  		return this.IMPORT;
  	}
  	else if(folder.equals("Outbound"))
  	{
  		return this.OUTBOUND;
  	}
  	else
  	{
  		return "";
  	}
  }
  
  private synchronized String getTimestampFormat(String format)
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
  
  private void triggerArchiveStartUpAlert(int totalRTProcess, boolean isEnableRestoreArchive, boolean isEnableSearchArchive,
                                          List<String> partnerForArchive)
  {
    Date archiveStartTime = new Date(TimeUtil.localToUtc(Calendar.getInstance().getTimeInMillis())); //the actual day, hour, minute, second
                                                                                                     //have been converted to UTC time     
    //fromTime, toTime are in UTC time
    Timestamp fromTime = (Timestamp)criteriaHTable.get("FromStartTime");
    Timestamp toTime = (Timestamp)criteriaHTable.get("ToStartTime");
    
    List folderList = (List)criteriaHTable.get("FolderList");
    List docTypeList = (List)criteriaHTable.get("DocTypeList");
    
    Boolean isArchiveOrphan = (Boolean)criteriaHTable.get("IsArchiveOrphanRecord");
    String archiveID = (String)summaryHTable.get("ArchiveID");
    List beIDs = (List)criteriaHTable.get("BeIDs");
    
    DefaultProviderList providerList = new DefaultProviderList();
    ArchiveAlertProvider provider = new ArchiveAlertProvider(Boolean.FALSE, archiveStartTime, new Date(fromTime.getTime()),
    		                                                     new Date(toTime.getTime()),docTypeList, folderList,
    		                                                     ""+totalRTProcess,isEnableRestoreArchive, isEnableSearchArchive,
    		                                                     partnerForArchive, beIDs, archiveID, isArchiveOrphan);
    providerList.addProvider(provider);
    
    try
    {
    	IAlertManagerObj alertMgr = ArchiveHelper.getAlertManager();
    	alertMgr.triggerAlert(IDBArchiveConstants.ARCHIVE_START_ALERT, providerList, (String)null);
    }
    catch(Exception ex)
    {
    	Logger.error(ILogErrorCodes.GT_GRIDDOC_ARCHIVE,
    	             "[DirectArchiveGridDocument.triggerArchiveStartUpAlert] Error in sending alert. Error: ", ex);
    }
  }
  
  public void setCriteriaTable(Hashtable criteriaHTB)
  {
  	criteriaHTable = criteriaHTB;
  }
  
  public void setSummaryTable(Hashtable summaryHTB)
  {
    summaryHTable = summaryHTB;
  }
  
  private void addFilesToBeZipped(Object[] fileInfo, List<Object[]> fileToBeZipped)
  {
  	if(! isEnableRestoreArchive) return;
  	
  	fileToBeZipped.add(fileInfo);
  }
}
