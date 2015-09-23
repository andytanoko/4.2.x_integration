/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDocumentManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005   Tam Wei Xiang       Created
 * Apr 26, 2006   Tam Wei Xiang       To handle the duplicate process instance
 *                                    (they share the same process instance ID
 *                                    and originatorID, but diff set of documents.
 *                                    This can happened while the supplier crash, 
 *                                    and we restore the GT from their backup and we use
 *                                    back the same running number.) 
 *                                    exist in estore sys.
 * May 08, 2006   Tam Wei Xiang       i) To handle the transaction timeout exception.
 *                                       Instead of process everything in one shoot, we
 *                                       break them into batch. See EStoreService for detail.
 *                                    ii)To save the disk space. For PI, if the user re-estore
 *                                       the pi, the zip file that group docs that correspond
 *                                       to that pi may be recreated. Modified method
 *                                       generateProcessInstanceRelatedInfo();
 * May 10, 2006   Tam Wei Xiang       modified method generateUniqueFolder. Instead of
 *                                    each time we list the files in a particular folder,
 *                                    we cache the total number of file.   
 * June 02, 2006  Tam Wei Xiang       Due to the undesire behaviour of estore eg if
 *                                    no docs associated to a PI and the PI's state is
 *                                    completed then we will fail the entire estore process.
 *                                    
 *                                    Instead of failing the whole estore process,
 *                                    we try to identify all known exceptions that will 
 *                                    happen in estore. If the PI that fall into one of
 *                                    the known exceptions, we will skip processing of that
 *                                    PI and attach the PIID+OriginatorID of the fail PI
 *                                    in the estore complete alert.                                                                                                     
 */

/**
 * Handle the document received from GT. 
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
*/
package com.gridnode.gtas.server.dbarchive.facade.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Date;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;


import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;


import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.dbarchive.helpers.DocumentMetaInfoEntityHandler;
import com.gridnode.gtas.server.dbarchive.helpers.ProcessInstanceMetaInfoEntityHandler;
import com.gridnode.gtas.server.dbarchive.helpers.EStoreHelper;
import com.gridnode.gtas.server.dbarchive.helpers.IEStorePathConfig;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.dbarchive.model.DocInfo;
import com.gridnode.gtas.server.dbarchive.model.DocumentMetaInfo;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceInfo;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceMetaInfo;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.pdip.framework.db.code.CodeValue;
import com.gridnode.pdip.framework.db.code.CodeValueHelper;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

import static com.gridnode.gtas.server.dbarchive.helpers.IEStoreConstants.*;

public class ProcessDocumentManagerBean implements SessionBean //a Stateful SessionBean
{
	private SessionContext _sessionConx = null;
	private EStoreHelper _esHelper;
	private ArrayList _processInstanceFileList; //contain the filenames of files
	private ArrayList _udocFileList; //store the udoc absolute filepath
	private ArrayList _zipFileList; //store the audit filename
	private ArrayList _xmlFileList;  //store the xml filename
	
	private ArrayList _tempFileList;  //store the filenames of files that we serialize obj on them.
	private ArrayList _udocFilenameList; //store the filename of files that we serialize abs file path of udoc on it. use when archive by document
	
	private ArrayList _piFileList;  //store the filename of files that we serialize filename of process instance xml on it
	private Hashtable<String, String> _docType;     //store the udocTypes that have been estored so far. 
	private Hashtable<String, String> _processDef;  //store the processDef that have been estored so far.
	private Hashtable<String, String> _processState;//store the processState that have been estored so far. 
	private Hashtable<String, String> _partnerID;  //store the partnerID that have been estored so far
	
	private Hashtable _numFileInFolder;  //store the total num of file in a particular folder
	
	//31 MAR 2006
	private ArrayList _failedList; //store the gdoc filename that failed to process (eg missing udoc. for Archive by doc) or 
	                               //store the process instance ID that failed(due to missing udoc, audit, or no associated doc at all).
	private int _numRecordBeEStored; //indicates how many process instances or how many udoc have been estored. (use in alert)
  
  private SimpleDateFormat _dateFormatter;
  
	public void ejbCreate() throws CreateException
	{
		try
		{
			_esHelper = EStoreHelper.getInstance();
			_processInstanceFileList = new ArrayList();
			_udocFileList = new ArrayList();
			
			_zipFileList = new ArrayList();
			_xmlFileList = new ArrayList();
			
			_tempFileList = new ArrayList();
			_udocFilenameList = new ArrayList();
			_piFileList = new ArrayList();
			
			_docType = retrieveCodeValue(DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Doc_Type);
			_processDef = retrieveCodeValue(ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_Def);
			_processState = retrieveCodeValue(ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_State);
			_partnerID = retrieveCodeValue(DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Partner_ID);
			
			_failedList = new ArrayList();
			_numFileInFolder = new Hashtable();
			
			_dateFormatter = new SimpleDateFormat(DATE_TIME_PATTERN); //use to format the process time, doc time
		}
		catch(Exception ex) 
		{
			Logger.warn("[ProcessDocumentManagerBean.ejbCreate]", ex);
			throw new CreateException(ex.getMessage());
		}
	}
	
	public void ejbRemove()
	{
		
	}
	
	public void ejbActivate()
	{
		try
		{
			_esHelper = EStoreHelper.getInstance();
		}
		catch(Exception ex)
		{
			Logger.error(ILogErrorCodes.GT_DBARCHIVE_ESTORE_INSTANCE,
			             "[ProcessDocumentManagerBean.ejbActivate] Error: "+ex.getMessage(), ex);
		}
	}
	
	public void ejbPassivate()
	{
		_esHelper = null;
	}
	
	public void setSessionContext(SessionContext sc)
	{
		_sessionConx = sc;
	}
	
	/**
	 * Populate the DocumentMEtaInfo table.
	 * Need to be updated while all the file have been transfered over from GTAS side
	 * @param zipFile Contain the actual audit files which related to ProcessInstances
	 * @param docInfoXML A XML file which contain the docInfo that describe the AuditFiles inside zipFile
	 */
	public void processDocInfo(DocInfo docInfo)
		     throws Exception
	{
		try
		{
			if(docInfo == null) return ;
			
			if(_docType == null)
			{
				_docType = retrieveCodeValue(DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Doc_Type);
			}

			DocumentMetaInfo docMetaInfo = getDocumentMetaInfoObject(docInfo);
			boolean isArchiveByDocument = ! docMetaInfo.isArchivedByPI();
			if(isArchiveByDocument) //we will move it directly to its destination folder
			{
				processArchivedByDocument(docMetaInfo);
			}
			
			//store the docInfo object into DB
			createDocumentMetaInfo(docMetaInfo);
			Logger.log("[ProcessDocumentManagerBean] finished insert docMetaInfo with gdocID "+docMetaInfo.getGdocID() +" to DB");
			
			//28 Nov 2005 store the docType into codeValue table so that we can retrieve them faster
			handleDocumentType(docMetaInfo.getDocType());
			handlePartnerID(docInfo.getPartnerID());
		}
		catch(Exception ex)
		{
			//reset counter for the record we have estored so far
			_numRecordBeEStored = 0;
			
			Logger.warn("[ProcessDocumentManagerBean.processDocFile] error is "+ex.getMessage(),ex);
			throw new ApplicationException("[ProcessDocumentManagerBean.processDocFile] error inserting document_meta_info. "+ex.getMessage(),ex);
		}
	}
	
	/**
	 * The archiveType for the docInfo is archivedByDocument, we will directly
	 * store the docInfo correspond physical docs (gdoc, udoc, audit) and group them together.
	 * @param docInfo
	 * @return 
	 */
	private String processArchivedByDocument(DocumentMetaInfo docInfo) throws Exception
	{
		String destRelativePath = ""; //the path where we store the docInfo related docs.
		
		//To check if the record already in ESTORE DB. If so we will use back the filePath of the zip we export before
		//This can help to reduce the space consumption if user re-estore the same doc multiple times.
		DocumentMetaInfo info = getDocumentMetaInfoEntityHandler().findByGdocIDAndFolder(docInfo.getGdocID(), docInfo.getFolder());
		if(info != null)
		{
			destRelativePath = info.getFilePath();
		}
		
		boolean isValid = validateDocumentMetaInfo(docInfo);
		String filePath = "";
		
		File estoreDataDir = FileUtil.getFile(IEStorePathConfig.ESTORE_DATA_DIRECTORY,"");
		filePath = groupGdocRelatedFiles(docInfo, estoreDataDir, destRelativePath);
		docInfo.setFilePath(filePath);
			
		//update the gdoc,udoc,audit's filepath to just store the filename.
		updateDocFilename(docInfo);
			
		//count how many udoc we have estored so far (for archive by document)
		_numRecordBeEStored++;
		return filePath;
	}
	
	private void handleDocumentType(String docType) throws Exception
	{
		if(docType != null && _docType.get(docType) == null)
		{
				Logger.log("Doc type "+docType+" not exist in DB.");
				CodeValueHelper cvHelper = CodeValueHelper.getInstance();
				cvHelper.addCodeValue(new CodeValue(docType, docType, DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Doc_Type));
				_docType.put(docType,"");
		}
	}
	
	/**
	 * Populate the process instance record into DB. All the related documents (eg audit, udoc, gdoc, attachment)
	 * will be packaged into one zip file.
	 * @param proInfo
	 */
	public void populateProcessInstance(ProcessInstanceInfo proInfo) throws ApplicationException
	{
		try
		{
			SimpleDateFormat dateFormatter = _dateFormatter;
			File dummy = FileUtil.getFile(IEStorePathConfig.ESTORE_DATA_DIRECTORY,"");
			
			//populate the process instance info into DB
				Collection<DocumentMetaInfo> docInfoList = retrieveCorrespondDocs(proInfo);
				
				if(docInfoList!=null && !docInfoList.isEmpty())
				{	
					//29 Mar 2006 validate the document meta info
					boolean isValid = validateDocumentMetaInfoList(docInfoList);
					if(!isValid)
					{
						handleFailedProcessInstance(proInfo, ESTORE_FAILED_DOCS_FAILED);
					}
					
					//get the documentMetainfo which udoc doc type belong to action msg category(not rn_ack nor rn_exception)
					DocumentMetaInfo actionMsg = getActionMsg(docInfoList);
					
					ProcessInstanceMetaInfo proIMetaInfo = getProcessInstanceMetaInfoObject(proInfo,actionMsg ,dateFormatter, docInfoList);

					Long uid = createProcessInstanceMetaInfo(proIMetaInfo); //if uid is null, meaning the such a record is existed
					Logger.log("[ProcessDocManagerBean.generateProcessInstanceRelatedInfo] Process intance meta info uid is "+ uid);
					_numRecordBeEStored++;
					
					//generate the unique folder path
					String zipSubPath = getZipSubPath(proIMetaInfo, actionMsg, dummy, uid==null);
					String absolutePath = dummy.getAbsolutePath()+"/"+zipSubPath;
					
					//Get all the process instance related files that need to be zipped
					Collection<Object[]> piCorrespondFiles = getProcessInstanceRelatedFiles(docInfoList);
					
					//zip the audit related file. zipFilename in format originatorID_processID
					addToZip(piCorrespondFiles, absolutePath, new Hashtable<String, String>());
					
					//update the docInfo's gdocFilename, auditFilename, udocFilename to include the filename only
					updateDocsFilename(docInfoList);
					
					//update the filepath of the process instance related doc meta info
					modifiedDocumentMetaInfo(docInfoList, zipSubPath, uid);
					
					//Keep track the processDef and processState. In future, the fetching for the two fields can be faster.
					handleProcessDef(proInfo.getProcessDef());
					handleProcessState(proInfo.getProcessState());
          
          Logger.log("Populated ProcessTransaction: "+proIMetaInfo);
				}
				//29 Mar 2006 
				else
				{
					handleFailedProcessInstance(proInfo, ESTORE_FAILED_NO_DOCS);
					ProcessInstanceMetaInfo proIMetaInfo = getProcessInstanceMetaInfoObject(proInfo, null, dateFormatter, null);
					Long uid = createProcessInstanceMetaInfo(proIMetaInfo); //if uid is null, meaning the such a record is existed
					Logger.log("[ProcessDocManagerBean.generateProcessInstanceRelatedInfo] Process intance meta info uid is "+ uid);
					_numRecordBeEStored++;
				}
		}
		catch(Exception ex)
		{
			_numRecordBeEStored = 0;
			Logger.warn("[ProcessDocumentManagerBean.generateProcessInstanceRelatedInfo] Error is ",ex);
			throw new ApplicationException("[ProcessDocumentManagerBean.generateProcessInstanceRelatedInfo] "+ex.getMessage(), ex);
		}
	}
	
	/**
	 * Get the sub zip file path that we will store the process instance related files in.
	 * The hieracy of the folder is processDef/year/month  **year and month is derived from processStartTime.
	 * There is a total allowable number of files that can be stored in a folder as controlled 
	 * by estore.properties.
	 * @param proInfo
	 * @param docMetaInfo
	 * @param estoreDataDir The folder dir where contain all the estore files.
	 * @param isPIRecordExisted indicate whether the proInfo existed in DB
	 * @return
	 */
	private String getZipSubPath(ProcessInstanceMetaInfo proInfo, DocumentMetaInfo docMetaInfo, File estoreDataDir,
	                             boolean isPIRecordExisted)
		throws Exception
	{
		//09052006 To save the disk space. 
		//         Previously implementation may recreate the existing zipFile in diff folder.
		//         eg. The same zip file can exist in folder audit_1 and audit_2
		String filePath = docMetaInfo !=null ? docMetaInfo.getFilePath() : "";
		if(docMetaInfo != null && !"".equals(filePath) && isPIRecordExisted
				&& isFileExist(estoreDataDir.getAbsolutePath()+"/"+filePath))
		{
			//overwrite the existing zip file
			return filePath;
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date processStartTime = proInfo.getProcessStartDate();
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(processStartTime.getTime());
		
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = monthInterpreter(c.get(Calendar.MONTH));
		
		String folderPath = BY_PI_FOLDER_NAME+illegalCharParser(proInfo.getProcessDef())+"/"+(year);
		String folderName = month;
		
		String uniqueFoldername = generateUniqueFolder(IEStorePathConfig.ESTORE_DATA_DIRECTORY, folderPath,folderName,
				                                           _esHelper.getTotalFileInFolder(), estoreDataDir.getAbsolutePath());
		//26 Apr 2006: two exactly same process instance but with diff docs associated to them
		//             can happen ... so use process start time to make filename unique.
		String processStartDateTime = processStartTime == null? "" 
				                                      : "_"+df.format(processStartTime);
		String filename = illegalCharParser(proInfo.getOriginatorID()+"_"+proInfo.getProcessInstanceID())+ 
		                                                     processStartDateTime+".zip";
		return folderPath +"/"+ uniqueFoldername+filename;
	}
	
	/**
	 * Store the processDef in seperate table so that we can retrieve them faster
	 * @param processDef
	 * @throws Exception
	 */
	private void handleProcessDef(String processDef) throws Exception
	{
		if(_processDef.get(processDef) == null)
		{
			CodeValueHelper cvHelper = CodeValueHelper.getInstance();
			cvHelper.addCodeValue(new CodeValue(processDef, processDef, ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_Def));
			_processDef.put(processDef,"");
		}
	}
	
	/**
	 * Store the processState in seperate so that we can retrieve them faster
	 * @param processState
	 * @throws Exception
	 */
	private void handleProcessState(String processState) throws Exception
	{
		if(_processState.get(processState) == null)
		{
			CodeValueHelper cvHelper = CodeValueHelper.getInstance();
			cvHelper.addCodeValue(new CodeValue(processState, processState, ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_State));
			_processState.put(processState,"");
		}
	}
	
	/**
	 * Store the partnerID in seperate so that we can retrieve them faster
	 * @param partnerID
	 * @throws Exception
	 */
	private void handlePartnerID(String partnerID) throws Exception
	{
		if(partnerID == null || partnerID.length() == 0) return;
		
		if(_partnerID.get(partnerID) == null)
		{
			CodeValueHelper cvHelper = CodeValueHelper.getInstance();
			cvHelper.addCodeValue(new CodeValue(partnerID, partnerID, DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Partner_ID));
			_partnerID.put(partnerID, "");
		}
	}
	
	/**
	 * Get all the physical docs that belong to the collection of docMetaInfo.
	 * @param docMetaInfoList
	 * @return
	 * @throws Exception
	 */
	private Collection<Object[]> getProcessInstanceRelatedFiles(Collection<DocumentMetaInfo> docMetaInfoList) throws Exception
	{
		ArrayList<Object[]> fileToBeZipped = new ArrayList<Object[]>();
		Iterator<DocumentMetaInfo> ite = docMetaInfoList.iterator();
		while(ite.hasNext())
		{
			DocumentMetaInfo docInfo = ite.next();
			getDocumentInfoRelatedFiles(docInfo, fileToBeZipped);
		}
		return fileToBeZipped;
	}
	
	/**
	 * Get all the physical docs that associated to the docInfo
	 * @param docInfo 
	 * @param fileToBeZipped a list of files that we will zip
	 * @throws Exception
	 */
	private void getDocumentInfoRelatedFiles(DocumentMetaInfo docInfo, Collection<Object[]> fileToBeZipped) throws Exception
	{
		File auditFile = getFile(docInfo.getFilename()); //In AS2, audit filepath will contain AS2/
		File udocFile = getFile(docInfo.getUDocFilename());
		File gdocFile = getFile(docInfo.getGdocFilename());
		File receiptAuditFile = getFile(docInfo.getReceiptAuditFilename());
		
		if(auditFile != null && auditFile.isFile())
		{
      File auditCommonDir = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, "");      
			String subPath = getSubPath(auditCommonDir, auditFile);
			fileToBeZipped.add(new Object[]{auditFile, AUDIT_FOLDER+subPath, auditFile.getName()});
		}
		
		if(receiptAuditFile != null && receiptAuditFile.isFile())
		{
      File auditCommonDir = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, "");
      String subPath = getSubPath(auditCommonDir, receiptAuditFile);
			fileToBeZipped.add(new Object[]{receiptAuditFile, AUDIT_FOLDER+subPath, receiptAuditFile.getName()});
		}
		
		if(udocFile != null && udocFile.isFile())
		{
			fileToBeZipped.add(new Object[]{udocFile, UDOC_FOLDER+docInfo.getFolder()+"/", udocFile.getName()});
		}
		
		if(gdocFile != null && gdocFile.isFile())
		{
			fileToBeZipped.add(new Object[]{gdocFile, GDOC_FOLDER, gdocFile.getName()});
		}
		
		String attachmentFilenames = docInfo.getAttachmentFilenames();
		if(attachmentFilenames != null)
		{
			StringTokenizer st = new StringTokenizer(attachmentFilenames, ":");
			while(st.hasMoreTokens())
			{
				String filename = st.nextToken();
				File attachmentFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, filename);
				if(attachmentFile != null && attachmentFile.isFile())
				{
					Object[] fileToAdd = new Object[]{attachmentFile, ATTACHMENT_FOLDER, filename};
					fileToBeZipped.add(fileToAdd);
				}
			}
		}
	}
	
	/**
	 * Update list of docInfo's gdocFilename, auditFilename, udocFilename to include the filename only.
	 * Initially, we store it as abs filePath in order to facilitate the processing.
	 * @param docInfoList
	 */
	private void updateDocsFilename(Collection<DocumentMetaInfo> docInfoList) throws Exception
	{
		Iterator<DocumentMetaInfo> i = docInfoList.iterator();
		while(i.hasNext())
		{
			DocumentMetaInfo docInfo = i.next();
			updateDocFilename(docInfo);
		}
	}
	
	/**
	 * Update the docInfo's gdocFilename, auditFilename, udocFilename to include the filename only.
	 * Initially, we store it as abs filePath in order to facilitate the processing.
	 * @param docInfo
	 */
	private void updateDocFilename(DocumentMetaInfo docInfo) throws Exception
	{
		String gdocFilePath = docInfo.getGdocFilename();
		String udocFilePath = docInfo.getUDocFilename();
		String auditFilePath = docInfo.getFilename();
		String receiptAuditFilePath = docInfo.getReceiptAuditFilename();
		
		if(gdocFilePath != null && !"".equals(gdocFilePath))
		{
			docInfo.setGdocFilename(getFilename(gdocFilePath));
		}
		
		if(udocFilePath != null && !"".equals(udocFilePath))
		{
			docInfo.setUDocFilename(getFilename(udocFilePath));
		}
		
		//For AS2 case, the subPath AS2/ will be included in the auditFilename.
		//For RNIF case, we will just store the filename
		if(auditFilePath != null && ! "".equals(auditFilePath))
		{
      File auditCommonDir = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, ""); 
			File auditFile = new File(auditFilePath);
			
			String subPath = getSubPath(auditCommonDir, auditFile);
			docInfo.setFilename(subPath+getFilename(auditFilePath));
		}
		
		if(receiptAuditFilePath != null && ! "".equals(receiptAuditFilePath))
		{
      File auditCommonDir = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, ""); 
      File receiptAudit = new File(receiptAuditFilePath);
      
      String subPath = getSubPath(auditCommonDir, receiptAudit);
			docInfo.setReceiptAuditFilename(subPath+getFilename(receiptAuditFilePath));
		}
		
		//attachment filename is not necessary since we store it as filename instead of file path
	}
	
  //15122006 to better support the similiar case as AS2 audit. Previously only hardcoded to deal with AS2
  private String getSubPath(File rootDir, File fileInRootDir)
  {
    String rootDirPath = rootDir.getAbsolutePath()+"/";
    String fileInRootDirPath = fileInRootDir.getAbsolutePath();
    rootDirPath = rootDirPath.replace('\\', '/');
    fileInRootDirPath = fileInRootDirPath.replace('\\', '/');
    
    String subPath = "";
    if(fileInRootDirPath.indexOf(rootDirPath) >= 0) //support AS2 case
    {
      
      subPath = fileInRootDirPath.substring(rootDirPath.length(), fileInRootDirPath.lastIndexOf(fileInRootDir.getName()));
    }
    return subPath;
  }
  
	/**
	 * Validate all the existing of the doc
	 * Validate the document meta info that associated with the given piInfo.
	 * @param docMetaInfoList
	 * @param piInfo
	 * @return
	 */
	private boolean validateDocumentMetaInfoList(Collection<DocumentMetaInfo> docMetaInfoList)
		throws Exception
	{
		boolean isValid = true;
		
		for(Iterator<DocumentMetaInfo> i = docMetaInfoList.iterator(); i.hasNext(); )
		{
			DocumentMetaInfo docInfo = i.next();
			if(! validateDocumentMetaInfo(docInfo))
			{
				Logger.log("DocMeta folder "+docInfo.getFolder()+" id "+docInfo.getGdocID()+" invalid");
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 * Validate the validity of the docInfo. The checking include the exist of audit file,
	 * udoc file, gdoc file, and attachment. If the docInfo is invalid, the failed reason
	 * will be set into the remark fied of docInfo
	 * @param docInfo
	 * @return
	 * @throws Exception
	 */
	private boolean validateDocumentMetaInfo(DocumentMetaInfo docInfo) throws Exception
	{
	  boolean isValid = true;
		StringBuilder failedReason = new StringBuilder("");
		String auditFilePath = docInfo.getFilename();
		String receiptAuditFilePath = docInfo.getReceiptAuditFilename();
		String udocFilePath = docInfo.getUDocFilename();
		String gdocFilePath = docInfo.getGdocFilename();
		String docType = docInfo.getDocType();
		
		if(!"".equals(auditFilePath) && ! isFileExist(auditFilePath))
		{
			Logger.log("missing audit");
			failedReason.append(ESTORE_FAILED_MISSING_AUDIT_FILES).append(";");
		}
		
		if(!"".equals(receiptAuditFilePath) && ! isFileExist(receiptAuditFilePath))
		{
			Logger.log("Missing receipt audit");
			failedReason.append(ESTORE_FAILED_MISSING_RECEIPT_AUDIT_FILE).append(";");
		}
		
		if(!"".equals(udocFilePath) && ! isFileExist(udocFilePath))
		{
			Logger.log("mising udoc");
			failedReason.append(ESTORE_FAILED_MISSING_UDOC).append(";");
		}
		if(!"".equals(gdocFilePath) && ! isFileExist(gdocFilePath))
		{
			Logger.log("missing gdoc");
			failedReason.append(ESTORE_FAILED_MISSING_GDOC).append(";");
		}
		
		String remark = verifyAttachment(docInfo.getAttachmentFilenames(), docInfo.isContainAttachment());
		if(remark != null && remark.length() > 0)
		{
			Logger.log("missing attachment");
			failedReason.append(ESTORE_FAILED_MISSING_ATTACHMENT).append(";");
		}
		/*
		//02062006
		if(docType.compareTo(SIGNAL_MSG_RN_ACK)!=0 &&
								docType.compareTo(SIGNAL_MSG_RN_EXCEPTION)!=0 &&
				           			isEmptyDocNumber(docInfo))
		{
			//the doc info for not RN_ACK, RN_EXP should have doc date and docNumber
			failedReason.append(ESTORE_FAILED_EMPTY_DOC_NO).append(";");
		}
		if(docType.compareTo(SIGNAL_MSG_RN_ACK)!=0 &&
								docType.compareTo(SIGNAL_MSG_RN_EXCEPTION)!=0 &&
           							isEmptyDocDate(docInfo))
		{
			//the doc info for not RN_ACK, RN_EXP should have doc date and docNumber
			failedReason.append(ESTORE_FAILED_EMPTY_DOC_DATE).append(";");
		} */
		
		if(! "".equals(failedReason.toString()))
		{
			isValid = false;
			docInfo.setRemark(formatFailedReasonStr(failedReason.toString()));
		}
		return isValid;
	}
	
	private boolean isFileExist(String absFilePath)
	{
		if(absFilePath == null)
		{
			return false;
		}
		else
		{
			return new File(absFilePath).exists();
		}
	}
	
	private File getFile(String absFilePath)
	{
		if(isFileExist(absFilePath))
		{
			return new File(absFilePath);
		}
		else
		{
			return null;
		}
	}
	
	private String verifyAttachment(String attachmentFilenames, boolean isContainAttachment) throws FileAccessException
	{
		String remark = "";
		if(isContainAttachment && attachmentFilenames == null)
		{
			return remark;
		}
		else if(! isContainAttachment)
		{
			return null;
		}
		
		StringTokenizer st = new StringTokenizer(attachmentFilenames, ":");
		while(st.hasMoreTokens())
		{
			String filename = st.nextToken();
			File attachmentFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, filename);
			if(attachmentFile == null)
			{
				remark = "".equals(remark) ? filename : remark+","+filename;
			}
		}
		return remark;
	}
	
	private String formatFailedReasonStr(String failedReasonStr)
	{
		StringBuilder formattedStr = new StringBuilder("");
		StringTokenizer st = new StringTokenizer(failedReasonStr, ";");
		while(st.hasMoreTokens())
		{
			formattedStr.append(st.nextToken()).append("\n");
		}
		if(! "".equals(formattedStr.toString()) && formattedStr.lastIndexOf("\n") > -1 &&
				                              formattedStr.lastIndexOf("\n") ==( formattedStr.length()-1) )
		{
			return formattedStr.toString().substring(0, formattedStr.lastIndexOf("\n"));
		}
		else
		{
			return formattedStr.toString();
		}
	}
	
	/**
	 * 02062006: Check whether the doc number is empty
	 * @param docInfo
	 * @return
	 */
	private boolean isEmptyDocNumber(DocumentMetaInfo docInfo)
	{
		String docNumber = docInfo.getDocNumber();
		if(docNumber == null ||  "".equals(docNumber.trim()))
		{
			Logger.log("[ProcessDocumentManagerBean.isEmptyDocNumber] doc number for DocInfo with gdocID "+ docInfo.getGdocID()+" folder "+docInfo.getFolder()+" is empty !");
			return true;
		}
		return false;
	}
	
	private void handleFailedProcessInstance(ProcessInstanceInfo proInfo, String reason)
	{
		proInfo.setRemark(reason);
	}
	
	private void modifiedDocumentMetaInfo(Collection docInfoList, String filePath,
			                                  Long UID)
		throws SystemException
	{
		Logger.log("[ProcessDocumentManagerBean.modifiedDocumentMetaInfo] modifieying");
		Iterator i = docInfoList.iterator();
		while(i.hasNext())
		{
			try
			{
				DocumentMetaInfo docInfo = (DocumentMetaInfo)i.next();
				docInfo.setFilePath(filePath);
				
				if(UID != null) //26 Apr 2006 docInfo itself already contain the UID of process instance meta info
				{
					docInfo.setProcessInstanceInfoUID(UID);
				}
				//docInfo.setPartnerDuns(partnerDuns);
				updateDocumentMetaInfo(docInfo);
			}
			catch(Exception ex)
			{
				Logger.warn("[ProcessDocumentManagerBean.modifiedDocumentMetaInfo] Exception ",ex );
				throw new SystemException("Error occured while updating the doc meta info",ex);
			}
			
		}
	}
	
	private void printInstanceInfo(ProcessInstanceMetaInfo info)
	{
		Logger.log("ProcessInstanceMEta Info");
		Logger.log("Instance MetaInfo ID "+info.getProcessInstanceID()+" state is "+info.getProcessState());
		Logger.log("start date "+info.getProcessStartDate()+" end date "+info.getProcessEndDate());
		Logger.log("duns no "+info.getPartnerDuns()+" partnerID "+info.getPartnerID());
		Logger.log("process def "+info.getProcessDef()+" roleType "+info.getRoleType());
		Logger.log("partner name "+info.getPartnerName()+" originatorID "+info.getOriginatorID());
		Logger.log("process instance info UID "+info.getKey());
	}
	
	/**
	 * Get the first document from a group of doc info that tie to a processInstance.
	 * The first doc's udoc doc type must be action msg and it has the smallest
	 * docDateGenerated date(in milli second) among the group of doc info
	 * @param docInfoList a group of doc info that tie to a processInstance
	 * @return the first document
	 */
	private DocumentMetaInfo getActionMsg(Collection<DocumentMetaInfo> docInfoList)
	{
		Iterator<DocumentMetaInfo> i = docInfoList.iterator();
		DocumentMetaInfo firstDoc = null; 
		String smallestDateTime = "";
		
		while(i.hasNext())
		{
			DocumentMetaInfo docInfo = (DocumentMetaInfo)i.next();
			String docDateGen = docInfo.getDocDateGenerated();
			
			//Logger.log("[ProcessDocumentManagerBean.GetFirstRequestDoc] docType "+docInfo.getDocType().compareTo(SIGNAL_MSG_RN_ACK)+" "+docInfo.getDocType());
			if(docInfo.getDocType().compareTo(SIGNAL_MSG_RN_ACK)!=0 &&
					docInfo.getDocType().compareTo(SIGNAL_MSG_RN_EXCEPTION)!=0 && docInfo.isOriginalDoc() &&
					docDateGen != null && ! "".equals(docDateGen))
			{
				if("".equals(smallestDateTime) || docDateGen.compareTo(smallestDateTime) < 0)
				{
					smallestDateTime = docDateGen;
					firstDoc = docInfo;
				}
			}
		}
		
		return firstDoc;
	}
	
	private void printMe(DocumentMetaInfo doc)
	{
		Logger.log("Doc Type "+doc.getDocType()+ " doc number "+doc.getDocNumber());
		Logger.log("TP Duns "+doc.getPartnerDuns()+" TP ID "+ doc.getPartnerID());
		Logger.log("TP name "+doc.getPartnerName()+" doc date generated "+doc.getDocDateGenerated());
		Logger.log("date time send start "+doc.getDateTimeSendStart()+" date send end "+doc.getDateTimeSendEnd());
		Logger.log("receive start "+doc.getDateTimeReceiveStart()+" receive end "+doc.getDateTimeReceiveEnd());
		Logger.log("created date "+doc.getDateTimeCreate()+ " datet import "+doc.getDateTimeImport());
		Logger.log("date time export "+doc.getDateTimeExport()+" process def "+doc.getProcessDef());
		Logger.log("process I ID "+doc.getProcessInstanceID()+" audit name "+doc.getFilename());
		Logger.log("folder "+doc.getFolder()+ " OriginatorID "+doc.getOriginatorID());
		//Logger.log("zip filename is "+doc.getZipFileName());
		Logger.log("archive type is "+doc.isArchivedByPI()+" udoc doc type ");
		Logger.log("udoc filename is "+doc.getUDocFilename());
	}
	
	private void copyFilenameList(ArrayList target, ArrayList source)
	{
		for(int i = 0; i < source.size(); i++)
		{
			target.add(source.get(i));
		}
	}
	
	
	/**
	 * Insert a new DocumentMetaInfo record into DB
	 * @param docInfo DocumentMetaInfo object
	 * @param doc DocInfo object
	 */
	private void createDocumentMetaInfo(DocumentMetaInfo docInfo)
		throws CreateEntityException, SystemException, DuplicateEntityException
	{
		try
		{
			this.getDocumentMetaInfoEntityHandler().createDocumentMetaInfo(docInfo);
		}
		catch(CreateException ex)
		{
			Logger.warn("[ProcessDocumentManagerBean.createDocumentMetaInfo] Exception ",ex);
			throw new CreateEntityException(ex);
		}
		catch(DuplicateEntityException ex)
		{
			Logger.warn("[ProcessDocumentManagerBean.createDocumentMetaInfo] Exception ",ex);
			throw new DuplicateEntityException(ex);
		}
		catch(Throwable ex)
		{
      Logger.warn("[ProcessDocumentManagerBean.createDocumentMetaInfo] Error ", ex);
      throw new SystemException(
        "[ProcessDocumentManagerBean.createDocumentMetaInfo] Error ",
        ex);
		}
	}
	
	/**
	 * Insert a new ProcessInstanceMetaInfo record into DB
	 * @param docInfo ProcessInstanceMetaInfo object
	 */
	private Long createProcessInstanceMetaInfo(ProcessInstanceMetaInfo docInfo)
		throws CreateEntityException, SystemException, DuplicateEntityException
	{
		try
		{
			return this.getProcessInstanceMetaInfoEntityHandler().createProcessInstanceMetaInfo(docInfo);
			//Logger.log("[ProcessDocumentManagerBean.createProcessInstanceMetaInfo] success processIOnstaceID is " +docInfo.getProcessInstanceID());
		}
		catch(CreateException ex)
		{
			Logger.warn("[ProcessDocumentManagerBean.createProcessInstanceMetaInfo] Exception ",ex);
			throw new CreateEntityException(ex.getMessage());
		}
		catch(DuplicateEntityException ex)
		{
			Logger.warn("[ProcessDocumentManagerBean.createProcessInstanceMetaInfo] Exception ",ex);
			throw ex;
		}
		catch(Throwable ex)
		{
      Logger.warn("[ProcessDocumentManagerBean.createProcessInstanceMetaInfo] Error ", ex);
      throw new SystemException(
        "ProcessDocumentManagerBean.createProcessInstanceMetaInfo(ProcessInstanceMetaInfo) Error ",
        ex);
		}
	}
  
  private void updateDocumentMetaInfo(DocumentMetaInfo docInfo)
  	throws UpdateEntityException, SystemException
  {
  	try 
  	{
  		this.getDocumentMetaInfoEntityHandler().update(docInfo);
  	}
  	catch(EntityModifiedException ex)
  	{
  		Logger.warn("[ProcessDocumentManagerBean.updateDocumentMetaInfo] App Exception ",ex);
  		throw new UpdateEntityException(ex);
  	}
  	catch(Throwable ex)
  	{
  		Logger.warn("[ProcessDocumentManagerBean.updateDocumentMetaInfo] Sys Exception",ex);
  		throw new SystemException("[ProcessDocumentManagerBean.updateDocumentMetaInfo] Sys Exception",ex);
  	}
  }
  
  /**
   * The zip file specify at the zipFileLocation is containing audit file or udocFile.
   * This method will unzip the audit or udoc file from the zip file.
   * @param zipFileLocation
   * @param entryName
   * @return a file which is unzip from the zip file where located in zipFileLocation
   */
  private File unzipFromZipFile(String zipFileLocation, String entryName)
          throws Exception
  {
  	ZipFile zipFile = null;
  	FileOutputStream output = null;
  	InputStream input = null;
  	try
  	{
  		Logger.log("unzipFromZipFile zipFileLocation "+zipFileLocation+" entryName "+entryName);
  		
  		zipFile = new ZipFile(zipFileLocation);
  		ZipEntry entry = new ZipEntry(entryName);
  		
  		//To make the output filename unique
  		String outputFilename = "estoreTemp_"+getTimestampFormat("yyyyMMddHHmmssSSS")+".tmp";
  		
  		File outputFile = FileUtil.createNewLocalFile(IEStorePathConfig.ESTORE_TEMP,TEMP_SUB_FOLDER+"/",outputFilename);
  	
  		output = new FileOutputStream(outputFile);
  	
  		input = zipFile.getInputStream(entry);
  		byte [] byteArray = new byte[512];
  		int byteRead;
  		while((byteRead = input.read(byteArray)) > -1)
  		{
  			output.write(byteArray,0, byteRead);
  		}
  		return outputFile;
  	}
  	catch(Exception ex)
  	{
  		Logger.warn("[ProcessDocumentManagerBean.unzipFromZipFile] error is ",ex);
  		throw new SystemException("[ProcessDocumentManagerBean.unzipFromZipFile] Error in extracting doc "+entryName+" from zip file "+zipFileLocation, ex);
  	}
  	finally
  	{
  		if(zipFile != null)
  		{
  			zipFile.close();
  		}
  		if(output != null)
  		{
  			output.close();
  			output = null;
  		}
  		if(input != null)
  		{
  			input.close();
  		}
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
  
	/**
	 * A method to create the DocumentMetaInfo object
	 * @return a DocumentMetaInfo object
	 */
  

	private DocumentMetaInfo getDocumentMetaInfoObject(DocInfo doc)
			throws Exception
	{
		String filePath = "";
		SimpleDateFormat DF = _dateFormatter;
		
		String dateTimeSendStart = doc.getDateTimeSendStart();
		String dateTimeSendEnd = doc.getDateTimeSendEnd();
		String dateTimeReceiveStart = doc.getDateTimeReceiveStart();
		String dateTimeReceiveEnd = doc.getDateTimeReceiveEnd();
		String dateTimeCreate = doc.getDateTimeCreate();
		String dateTimeImport = doc.getDateTimeImport();
		String dateTimeExport = doc.getDateTimeExport();
		String docDateGenerated = doc.getDocDateGenerated();
		String gdocID = doc.getGdocID();

		Date d;
		Date dtSendStart = null, dtSendEnd = null, dtReceiveStart = null;
		Date dtReceiveEnd = null, dtCreate = null,dtImport = null,dtExport = null;
		
		/*
		Timestamp date = rsDateConverter(docDateGenerated);
		Long dateGenerated = date!=null?new Long(date.getTime()):null;
		*/
		
		Long gUID = (gdocID+"").compareTo("")!=0?new Long(Long.parseLong(gdocID)):null; 
		
		try
		{
				dtSendStart = dateTimeSendStart.compareTo("")!=0?DF.parse(dateTimeSendStart):null;
				dtSendEnd = dateTimeSendEnd.compareTo("")!=0?DF.parse(dateTimeSendEnd):null;
				dtReceiveStart = dateTimeReceiveStart.compareTo("")!=0?DF.parse(dateTimeReceiveStart):null;
				dtReceiveEnd = dateTimeReceiveEnd.compareTo("")!=0?DF.parse(dateTimeReceiveEnd):null;
				dtCreate = dateTimeCreate.compareTo("")!=0?DF.parse(dateTimeCreate):null;
				dtImport = dateTimeImport.compareTo("")!=0?DF.parse(dateTimeImport):null;
				dtExport = dateTimeExport.compareTo("")!=0?DF.parse(dateTimeExport):null;
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[ProcessDocumentManagerBean.getDocumentMetaInfoObject] Error occured while "+ 
													" creating DocumentMetaInfo Obj.",ex);
		}
		
		return new DocumentMetaInfo(doc.getDocType(),doc.getDocNumber(),doc.getPartnerDuns(), 
				doc.getPartnerID(), doc.getPartnerName(),docDateGenerated,dtSendStart, 
				dtSendEnd,dtReceiveStart,dtReceiveEnd, 
				dtCreate, dtImport, dtExport, doc.getProcessDef(), 
				doc.getProcessInstanceID(), doc.getFilename(), doc.getFolder(),filePath,
				doc.getOriginatorID(),gUID, doc.isArchivedByPI(),doc.getSenderCertUID(),doc.getReceiverCertUID(), "" ,doc.getUdocFilename(),
				doc.getRnifVersion(), new Long(0), doc.getGdocFilename(), doc.isOriginalDoc(), doc.getAttachmentFilename(), doc.isContainAttachment(),
				doc.getReceiptAuditFilename(), doc.getDocTransStatus(), doc.getUserTrackingID());
	}
	
	/**
	 * Generate the ProcessInstanceMetaInfo obj. 
	 * @param proInfo capture some info related to a process instance
	 * @param docInfo The docInfo is an action msg (non-ack, and non-rn_exception)
	 * @param DF SimpleDateFormat obj
	 * @param docInfoList List of docInfo obj.
	 * @return
	 * @throws Exception
	 */
	private ProcessInstanceMetaInfo getProcessInstanceMetaInfoObject(ProcessInstanceInfo proInfo, DocumentMetaInfo docInfo,
	                                                                 SimpleDateFormat DF, Collection<DocumentMetaInfo> docInfoList)
					throws Exception
	{
		String partnerDuns = "", partnerID = "", docNumber = "", partnerName = "";
		String docDateGenerated = "", userTrackingID = "";
		
		if(docInfo != null)
		{
			partnerDuns = docInfo.getPartnerDuns();
			partnerName = docInfo.getPartnerName();
			docNumber = docInfo.getDocNumber();
			docDateGenerated = docInfo.getDocDateGenerated();
			partnerID = docInfo.getPartnerID();
			userTrackingID = docInfo.getUserTrackingID();
		}
		else // In case we can't find any actionMsg, we still can using the signal msg to populate the necessary field
		{
			if(docInfoList != null && docInfoList.size() > 0)
			{
				Iterator<DocumentMetaInfo> i = docInfoList.iterator();
				while(i.hasNext())
				{
					DocumentMetaInfo doc = i.next();
					if(! "Export".equals(doc.getFolder())) //Export folder may not have partnerDuns
					{
						partnerDuns = doc.getPartnerDuns();
						partnerName = doc.getPartnerName();
						partnerID = doc.getPartnerID();
						userTrackingID = doc.getUserTrackingID();
					}
				}
			}
		}
		
		String proStartDate = proInfo.getProcessStartDate();
		String proEndDate = proInfo.getProcessEndDate();
		
		Date startDate = null, endDate = null;
		startDate = proStartDate.compareTo("")!=0 ? DF.parse(proStartDate) : null;
		endDate = proEndDate.compareTo("")!=0 ? DF.parse(proEndDate) : null;
		
    Integer failedReason = proInfo.getFailedReason();
    String detailReason = proInfo.getDetailReason() == null ? "" : proInfo.getDetailReason();
    Integer retryCount = proInfo.getRetryNumber();
    
		return new ProcessInstanceMetaInfo(proInfo.getProcessInstanceID(), proInfo.getProcessState(), startDate, endDate,
								partnerDuns, proInfo.getProcessDef(), proInfo.getRoleType(), partnerID, partnerName, docNumber,
								docDateGenerated, proInfo.getOriginatorID(),null, proInfo.getRemark(), userTrackingID, failedReason,
                detailReason, retryCount);
	}
	
	/**
	 * Zip all the files together into a zip file as specify by the zipAbsPath.
	 * @param fileToBeZipped contain the file that will be zipped into a zip file
	 * @param zipAbsPath the absolute path for the zip file.
	 * @param addedZipEntry to keep track of the zip entry we have added so far. If null is specify,
	 *                      the detect on duplicate entry will be turned off.
	 */
	private void addToZip(Collection<Object[]> fileToBeZipped, String zipAbsPath, Hashtable<String, String> addedZipEntry) throws Exception
	{
		boolean isDuplicatedEntryDetectOff = addedZipEntry == null;
		FileOutputStream output = null;
		ZipOutputStream zipOutput = null;
		try
		{
			output = new FileOutputStream(zipAbsPath);
			zipOutput = new ZipOutputStream(output);
			
			for(Object[] fileInfoArr : fileToBeZipped)
			{
				File fileToZip = (File)fileInfoArr[0];
				String entryName = (String)fileInfoArr[1] + (String)fileInfoArr[2];
				FileInputStream in = null;
				try
				{
					in = new FileInputStream(fileToZip);
					ZipEntry entry = new ZipEntry(entryName);
					entry.setTime(fileToZip.lastModified());
					
					if(isDuplicatedEntryDetectOff || ! addedZipEntry.containsKey(entryName))
					{
						zipOutput.putNextEntry(entry);
					
					
						byte[] buffer = new byte[512];
						int readSoFar = 0;
						while( (readSoFar = in.read(buffer)) > -1)
						{
							zipOutput.write(buffer, 0, readSoFar);
						}
						zipOutput.closeEntry();
						
						if(! isDuplicatedEntryDetectOff)
						{
							addedZipEntry.put(entryName, "");
						}
					}
					else
					{
						Logger.log("[ProcessDocumentManagerBean.addToZip] Duplicate entry "+entryName+" found in zipFile "+zipAbsPath);
					}
				}
				catch(Exception ex)
				{
					throw ex;
				}
				finally
				{
					if(in != null)
					{
						in.close();
					}
				}
			}
		}
		catch(Exception ex)
		{
			Logger.warn("[ProcessDocumentManagerBean.addToZip] Error adding files to zip file "+zipAbsPath, ex);
			throw new SystemException("[ProcessDocumentManagerBean.addToZip] Error adding files to zip file "+zipAbsPath, ex);
		}
		finally
		{
			if(zipOutput != null)
			{
				zipOutput.close();
			}
			if(output != null)
			{
				output.close();
			}
			if(! isDuplicatedEntryDetectOff)
			{
				addedZipEntry.clear();
			}
		}
	}
	
	/**
	 * @param month
	 * @return
	 */
	private String monthInterpreter(int month)
	{
		switch(month)
		{
			case 0: 
				return "January";
			case 1:
				return "February";
			case 2:
				return "March";
			case 3:
				return "April";
			case 4:
				return "May";
			case 5:
				return "June";
			case 6:
				return "July";
			case 7:
				return "August";
			case 8:
				return "September";
			case 9:
				return "October";
			case 10:
				return "November";
			case 11:
				return "December";
			default:
				throw new IllegalArgumentException("[ProcessDocumentManagerBean.monthInterpreter] The pass in month "+month+" is invalid.");
		}
	}
	
/**
	 * 10052006 : To resolve the performance issue we encounter. Instead of each time
	 *            we list the file in a particular folder, we cache the total file
	 *            of a particular folder. This can help to reduce the overhead to
	 *            list the total num file.  
	 * 
	 * Verify whether the folder exceed the number of file it can hold.
	 * A new folder will be generated if it doesn't exist or it exceed the number
	 * of file it can store. 
	 * @param folderPath
	 * @return a new folder name
	 */
	private String generateUniqueFolder(String pathKey, String subFolderPath, String foldername,
			                                int totalAllowedFile, String pathKeyAbsPath)
	        throws Exception
	{
		String folderFullPath = subFolderPath+"/"+foldername;
		boolean isExist = FileUtil.exist(pathKey,folderFullPath,"");
		if(!isExist)
		{
			String completePath = FileUtil.createFolder(pathKey,folderFullPath);
			if(completePath==null)
			{
				throw new Exception("[ProcessDocumentManagerBean.getUniqueFolder] subfolder "+subFolderPath+"/"+foldername+
																	" cannot be created.");
			}
			
			_numFileInFolder.put(folderFullPath, new Integer(1));
			
			return foldername+"/";
		}
		
		if(_numFileInFolder.containsKey(folderFullPath))
		{
			int totalFile = ((Integer)_numFileInFolder.get(folderFullPath)).intValue();
			if(totalFile >= totalAllowedFile)
			{
				//search for applicable folder
				foldername = getApplicableFolder(pathKey, subFolderPath, foldername, totalAllowedFile, pathKeyAbsPath);
			}
			else
			{
				_numFileInFolder.put(folderFullPath, new Integer(totalFile+1));
			}
		}
		else
		{
			int totalFile = FileUtil.getFile(pathKey,subFolderPath+"/", foldername).list().length;
			if(totalFile >= totalAllowedFile)
			{
				//search for applicable folder
				foldername = getApplicableFolder(pathKey, subFolderPath, foldername, totalAllowedFile, pathKeyAbsPath);
			}
			else
			{
				_numFileInFolder.put(folderFullPath, new Integer(totalFile+1));
			}
		}
		
		
		return foldername+"/";
	}
	
	/**
	 * 10052006 It will return a unique folder by appending a number behind the folder.
	 * @param pathKey
	 * @param subFolderPath
	 * @param foldername
	 * @param totalAllowedFile
	 * @param pathKeyAbsPath the path that represent the path key
	 * @return
	 * @throws Exception
	 */
	private String getApplicableFolder(String pathKey, String subFolderPath, String foldername,
                                     int totalAllowedFile, String pathKeyAbsPath)
		throws Exception
	{
		//check from the cache
		
		//Try the folder with append some number
		int i =1;
		foldername = appendNumToFolder(foldername, i);
		File f = new File(pathKeyAbsPath+"/"+subFolderPath+"/"+foldername);
		
		while(f.exists() && 
				     isTotalFileExceedLimit(pathKeyAbsPath,subFolderPath+"/"+foldername, totalAllowedFile))
		{
			++i;
			foldername = appendNumToFolder(foldername, i);
			f = new File(pathKeyAbsPath+"/"+subFolderPath+"/"+foldername);
		}
		
		if(!f.exists())
		{
			String completePath = FileUtil.createFolder(pathKey,subFolderPath+"/"+foldername);
			if(completePath==null)
			{
				throw new Exception("[ProcessDocumentManagerBean.getApplicableFolder] subfolder "+subFolderPath+"/"+foldername+
																	" cannot be created.");
			}
			
			_numFileInFolder.put(subFolderPath+"/"+foldername, new Integer(1));
			Logger.log("[ProcessDocumentManagerBean.getApplicableFolder] directory "+completePath+" created");
		}
		
		return foldername;
	}
	
	/**
	 * 
	 * @param pathKeyAbsPath
	 * @param folderPath
	 * @param totalAllowedFile
	 * @return
	 */
	private boolean isTotalFileExceedLimit(String pathKeyAbsPath, String folderPath, 
			                                   int totalAllowedFile)
	{
		if(_numFileInFolder.containsKey(folderPath))
		{
			int totalFileInFolder = ((Integer)_numFileInFolder.get(folderPath)).intValue();
			if(totalFileInFolder < totalAllowedFile)
			{
				_numFileInFolder.put(folderPath, new Integer(totalFileInFolder+1));
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			File f = new File(pathKeyAbsPath+"/"+folderPath);
			int totalFileInFolder = f.list().length;
			if(totalFileInFolder < totalAllowedFile)
			{
				_numFileInFolder.put(folderPath, new Integer(totalFileInFolder+1));
				return false;
			}
			else
			{
				_numFileInFolder.put(folderPath, new Integer(totalFileInFolder));
				return true;
			}
		}
	}
	
	private String appendNumToFolder(String folderName, int numToAppend)
	{
		int index = folderName.lastIndexOf("_");
		if(index >= 0)
		{
			return folderName.substring(0,folderName.lastIndexOf("_"))+"_"+numToAppend;
		}
		else
		{
			return folderName+"_"+numToAppend;
		}
	}
	
	/**
	 * Parse the filename so that the syntax of the filename is correct
	 * Note: The syntax only aplicable for window environment
	 * @param filename The filename may not be a valid window filename
	 * @return a valid window filename
	 */
	private String illegalCharParser(String filename)
	{
		StringTokenizer st =  new StringTokenizer(filename,ILLEGAL_CHAR);
		String validFilename = "";
		while(st.hasMoreTokens())
		{
			validFilename += (String)st.nextToken()+"_";

		}
		return validFilename.substring(0,validFilename.lastIndexOf("_"));
	}
	
	/**
	 * Identify the date pattern that is match to the given rsDate
	 * @param patternList contain the date pattern eg yyyyMMddhhmmss
	 * @param rsDate Rosettanet format date
	 * @return
	 * @throws Exception
	 */
	private String identifyDatePattern(ArrayList patternList, String rsDate)
					throws ApplicationException
	{
		for(int i=0; i< patternList.size(); i++)
		{
			String patternToUse = (String)patternList.get(i);
			
			//length match, need to check whether the location of 'T' n 'Z' match
			//note: 'T' n 'Z' is included in udoc date format
			if(rsDate.length() == patternToUse.length())
			{
				if(patternToUse.indexOf('T')==rsDate.indexOf('T') &&
						patternToUse.indexOf('Z')==rsDate.indexOf('Z'))
				{
					return patternToUse;
				}
			}
		}
		throw new ApplicationException("[ProcessDocumentManagerBean.identifyDatePattern] Unable " +
				"to find appropriate UDOC date pattern for rsDate ["+rsDate+"]. Pls refer to estore.properties to add in " +
				"correct date format pattern");
	}
	
	/**
	 * All the gdoc related docs (audit, udoc, gdoc, attachment files) will be grouped into one zip file
	 * named by the gdoc filename.
	 * @param doc
	 * @param estoreDataDir the folder path where we store the estore files
	 * @param subFilePath if it is not specify, an appropriate destSubPath will be generated. Else such a relative path
	 *                    will be used.
	 * @return the destSubFilePath where we will store the gdoc related docs (relative path). 
	 * @throws Exception
	 */
	private String groupGdocRelatedFiles(DocumentMetaInfo docInfo, File estoreDataDir, String subFilePath) 
		throws Exception
	{
		//NOTE udoc folder hieracy: _UDOC_FOLDER_NAME/udocType/year/month  **year n month will be based on docInfo's dateTimeCreated
		String destSubFilePath = subFilePath;
		if("".equals(subFilePath))
		{
			destSubFilePath = getArchivedByDocOutputDestPath(docInfo,estoreDataDir);
		}
		
		//Get all the docInfo associated doc(audit, gdoc, udoc, attachment files)
		ArrayList<Object[]> fileToBeZipped = new ArrayList<Object[]>();
		getDocumentInfoRelatedFiles(docInfo, fileToBeZipped);
		
		//Add to zip file
		String destZipFilePath = estoreDataDir.getAbsolutePath()+"/"+destSubFilePath;
		addToZip(fileToBeZipped, destZipFilePath, new Hashtable<String, String>());
		
		return destSubFilePath;
	}
	
	private String getArchivedByDocOutputDestPath(DocumentMetaInfo docInfo, File estoreDataDir) throws Exception
	{
		//archive by doc folder hieracy: UDOC_FOLDER_NAME/udocType/year/month  **year n month will be based on docInfo's dateTimeCreated
		Date dateTimeCreate = docInfo.getDateTimeCreate();
		if(dateTimeCreate == null)
		{
			dateTimeCreate = Calendar.getInstance().getTime();
			//throw new ApplicationException("[ProcessDocumentManagerBean.moveFileToEStoreDataDirec] DateTimeCreated for doc "+doc.getFilename()+" cannot be null.");
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(dateTimeCreate);
		
		String folderPath = BY_DOC_FOLDER_NAME+illegalCharParser(docInfo.getDocType())+"/"+(c.get(Calendar.YEAR));
		String folderName = monthInterpreter(c.get(Calendar.MONTH));
		String uniqueFoldername = generateUniqueFolder(IEStorePathConfig.ESTORE_DATA_DIRECTORY, 
				                                           folderPath,folderName, _esHelper.getTotalFileInFolder(),
				                                           estoreDataDir.getAbsolutePath());
		
		//check if the file already exist in the new folder, if it did, a new filebname will be generated.
		String gdocFilePath = docInfo.getGdocFilename(); //in case we couldn't get the physical gdoc
		if(gdocFilePath == null || "".equals(gdocFilePath))
		{
			gdocFilePath = docInfo.getFolder()+docInfo.getGdocID();
		}
		
		String filenameWithoutExt = truncateExt(getFilename(gdocFilePath));
		String outputZipFilename = illegalCharParser(filenameWithoutExt)+".zip";
		String newFileName = getUniqueFilename(IEStorePathConfig.ESTORE_DATA_DIRECTORY, folderPath+"/"+uniqueFoldername, outputZipFilename);
		
		return folderPath+"/"+uniqueFoldername+newFileName;
	}
	
	private String getFilename(String absPath)
	{
		return FileUtil.extractFilename(absPath);
	}
	
	private String truncateExt(String filename)
	{
		if(filename.lastIndexOf(".") >= 0)
		{
			return filename.substring(0, filename.lastIndexOf("."));
		}
		return filename;
	}
	
	/**
	 * This method will check if the file already exist in the folder as specify. 
	 * @param estoreDatePathKey
	 * @param filename
	 * @return a new Filename if the file already exist in the folder, else jiust return the
	 *         original name.
	 */
	private String getUniqueFilename(String estoreDataPathKey, String subFolderPath, String filename)
		throws SystemException
	{
		try
		{
			boolean isExist = FileUtil.exist(estoreDataPathKey, subFolderPath+"/", filename);
			
			int i = 0;
			while(isExist)
			{
				++i;
				filename = appendPaddingToFilename(filename,String.valueOf(i));
				isExist = FileUtil.exist(estoreDataPathKey, subFolderPath+"/", filename);
			}
			
			return filename;
		}
		catch(Exception ex)
		{
			throw new SystemException("[ProcessDocumentManagerBean.getUniqueFilename] Error occured while generating unique filename",ex);
		}
	}
	
	/**
	 * It will always add a padding on the end of the filename. (just before file extension)
	 * Format:  filename_1.xml
	 * @param filename
	 * @param padding The padding we want to add behind the filename.
	 * @return
	 */
	private String appendPaddingToFilename(String filename, String padding)
	{
		if(filename.lastIndexOf("_") >= 0 && filename.lastIndexOf(".") >= 0)
		{
			String subFilename = filename.substring(0,filename.lastIndexOf("_"));
			
			String fileExtension = filename.substring(filename.lastIndexOf("."),filename.length());
			
			return subFilename+"_"+padding+fileExtension;
		}
		
		else if(filename.lastIndexOf(".") >= 0)
		{	
			String fileExtension = filename.substring(filename.lastIndexOf("."),filename.length());
			return filename.substring(0, filename.lastIndexOf("."))+"_"+padding+fileExtension;
		}
		
		else if(filename.lastIndexOf("_") >= 0)
		{
			return filename.substring(0, filename.lastIndexOf("_"))+"_"+padding;
		}
		//no file extension found
		return filename+"_"+padding;
	}
	
	private ISearchESDocumentManagerObj getSearchESDocMgr()
		throws ServiceLookupException
	{
		return (ISearchESDocumentManagerObj)ServiceLocator.instance(
				    ServiceLocator.CLIENT_CONTEXT).getObj(
						ISearchESDocumentManagerHome.class.getName(),
						ISearchESDocumentManagerHome.class,
						new Object[0]);
	}
	
	private void deleteUDocFile(ArrayList absPathList)
		throws ApplicationException
	{
		for(int i= 0; i < absPathList.size(); i++)
		{
			File tmpFile = new File((String)absPathList.get(i));
			boolean isDeleted = tmpFile.delete();
			Logger.log("[ProcessDocumentManagerBean.deleteUDocFile] delete status of file "+tmpFile.getAbsolutePath()+" is "+isDeleted);
			if(!isDeleted)
			{
				Exception e = new Exception("File "+tmpFile.getAbsolutePath()+" can't be deleted.");
				throw new ApplicationException(e);
			}
		}
		absPathList.clear();
	}
	
	/**
	 * Cache a list of docType
	 * @return
	 * @throws ApplicationException
	 */
	private Hashtable importDocType()
		throws ApplicationException
	{
		try
		{
			Logger.log("[ProcessDocumentManagerBean.importDocType] Start caching doc type.");
			CodeValueHelper cvHelper = CodeValueHelper.getInstance();
			Collection c = cvHelper.retrieveByEntityTypeAndFieldID(DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Doc_Type);
			
			return convertToHashtable(c);
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[ProcessDocumentManagerBean.importDocType] Unable to fetch list of doc type from DB.", ex);
		}
	}
	
	/**
	 * Cache a list of processDef. 
	 */
	private Hashtable importProcessDef()
		throws ApplicationException
	{
		try
		{
			Logger.log("[ProcessDocumentManagerBean.importDocType] Start caching process def.");
			CodeValueHelper cvHelper = CodeValueHelper.getInstance();
			Collection c = cvHelper.retrieveByEntityTypeAndFieldID(ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_Def);
			return convertToHashtable(c);
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[ProcessDocumentManagerBean.importProcessDef] Unable to fetch list of process def from DB.", ex);
		}
	}
	
	/**
	 * Retrieve a list of code value from DB given the entityClassname and fieldID.
	 * We will cache such a list which help us to detect is there any duplicate entry prior
	 * we store the code value (for our case, they include processDef, docType, processState).
	 * @param entityClassName
	 * @param fieldID
	 * @return
	 */
	private Hashtable<String,String> retrieveCodeValue(String entityClassName, Integer fieldID)
		throws ApplicationException
	{
		try
		{
			Logger.log("[ProcessDocumentManagerBean.importCodeValue] Start caching code value with entityType= "+entityClassName +" and entityFieldID= "+fieldID+" .");
			CodeValueHelper cvHelper = CodeValueHelper.getInstance();
			Collection<CodeValue> c = cvHelper.retrieveByEntityTypeAndFieldID(entityClassName, fieldID);
			return convertToHashtable(c);
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[ProcessDocumentManagerBean.importCodeValue] Unable to fetch list of process def from DB.", ex);
		}
	}
	
	private Hashtable<String, String> convertToHashtable(Collection<CodeValue> c)
	{
		Hashtable<String, String> h = new Hashtable<String, String>();
		if(c != null)
		{
			Iterator<CodeValue> i = c.iterator();
			while(i.hasNext())
			{
				CodeValue cv = (CodeValue)i.next();
				h.put(cv.getCode(),"");
			}
		}
		return h;
	}
	
	/**
	 * TWX: 26 Apr 2006
	 * To retrieve the correspond document meta info based on the given process instance
	 * info. 
	 * It handle the duplicate process instance exist in estore but with diff 
	 * associated docs case.
	 * 
	 * @param proInfo value obj for ProcessInstanceMetaInfo
	 * @return a collection of document meta info obj
	 */
	private Collection<DocumentMetaInfo> retrieveCorrespondDocs(ProcessInstanceInfo proInfo)
		throws ApplicationException
	{
		try
		{
			SimpleDateFormat DF = _dateFormatter;
			Date processStartDate = proInfo.getProcessStartDate() == null ? null : DF.parse(proInfo.getProcessStartDate());
			
			ProcessInstanceMetaInfo metaInfo = getProcessInstanceMetaInfoEntityHandler().
		                                         findByPIIDOriginatorIDAndPIStartDate(
		                                        		 proInfo.getProcessInstanceID(), proInfo.getOriginatorID(),
		                                        		 processStartDate);
			if(metaInfo == null)
			{
				Logger.log("[ProcessDocumentManagerBean.retrieveCorrespondDocs] process instance meta info with process instance ID "+ proInfo.getProcessInstanceID()+
						        " originatorID "+ proInfo.getOriginatorID()+" process startDate "+ processStartDate+" not exist in estore.");
				return getDocumentMetaInfoEntityHandler().findByPIIDAndOriginatorIDAndPIUID(
						                                         proInfo.getProcessInstanceID(),
						                                         proInfo.getOriginatorID(), new Long(0), null);
			}
			else
			{
				Logger.log("[ProcessDocumentManagerBean.retrieveCorrespondDocs] process instance meta info with process instance ID "+ proInfo.getProcessInstanceID()+
		        " originatorID "+ proInfo.getOriginatorID()+" process startDate "+ processStartDate+", UID "+metaInfo.getKey()+ " existed in estore.");
				return getDocumentMetaInfoEntityHandler().findByPIIDAndOriginatorIDAndPIUID(
                                                     proInfo.getProcessInstanceID(),
                                                     proInfo.getOriginatorID(), (Long)metaInfo.getKey(), 
                                                     null);
			}
			
		}
		catch(Exception ex)
		{
			throw new ApplicationException("[ProcessDocumentManagerBean.retrieveCorrespondDocs] Error retrieving the process instance correspond documents.", ex);
		}
	}
	/*
	private boolean isFileExist(String filePath)
	{
		File f = new File(filePath);
		return f.exists();
	}*/
	
	//******************** EntityHandler *******************************
	private DocumentMetaInfoEntityHandler getDocumentMetaInfoEntityHandler()
	{
		return DocumentMetaInfoEntityHandler.getInstance();
	}
	
	private ProcessInstanceMetaInfoEntityHandler getProcessInstanceMetaInfoEntityHandler()
	{
		return ProcessInstanceMetaInfoEntityHandler.getInstance();
	}
}	

