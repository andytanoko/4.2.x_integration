/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchDocumentManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 8, 2005        Tam Wei Xiang       Created
 * Mar 31,2005        Tam Wei Xiang       Modified method getDocumentFileContent()
 *                                           -make it support missing of udoc, audit
 *                                            filename
 */
package com.gridnode.gtas.server.dbarchive.facade.ejb;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.dbarchive.helpers.DocumentMetaInfoEntityHandler;
import com.gridnode.gtas.server.dbarchive.helpers.IEStoreConstants;
import com.gridnode.gtas.server.dbarchive.helpers.IEStorePathConfig;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.dbarchive.helpers.ProcessInstanceMetaInfoEntityHandler;
import com.gridnode.gtas.server.dbarchive.helpers.RNFileExtracter;
import com.gridnode.gtas.server.dbarchive.model.AuditFileInfo;
import com.gridnode.gtas.server.dbarchive.model.DocumentMetaInfo;
import com.gridnode.gtas.server.dbarchive.model.FieldValueCollection;
import com.gridnode.gtas.server.dbarchive.model.IFieldValueCollection;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceMetaInfo;
import com.gridnode.gtas.server.dbarchive.model.RNFileContainer;
import com.gridnode.gtas.server.document.helpers.GridDocumentEntityHandler;  
import com.gridnode.pdip.framework.db.code.CodeValue;                    
import com.gridnode.pdip.framework.db.code.CodeValueHelper;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
/**
 * This class will handle all the EStore search query and the retrieval 
 * of estore audit file or udoc file for viewing. 
 *
 * @author Tam Wei Xiang
 * 
 * @version
 * @since GT 2.4.7
 */
public class SearchESDocumentManagerBean implements SessionBean //Stateless Session Bean
{
	transient private SessionContext _sessionCtx = null;
	private final String _ARCHIVE_BY_DOCUMENT = "Doc"; //the document is archived based on document
	private final String _TEMP_SUB_FOLDER = "tempContent";
	private final String _RETRIEVE_TYPE_AUDIT_INFO = "auditInfo";
	
  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }
  
/**
   * Locate a number of DocumentMEtaInfo Obj that satisfy the
   * filtering condition.
   *
   * @param processInstanceID 
   * @param originatorID
   * @return a Collection of DocumentMEtaInfo found, or empty collection if none
   * exists.
   */
	public Collection findByProcessInstanceIDAndOriginatorID(String processInstanceID, String originatorID)
		throws FindEntityException, SystemException
	{
		Collection docInfo = null;
		try
		{
			docInfo = getDocumentMetaInfoEntityHandler().findByProcessInstanceIDAndOriginatorID(
							processInstanceID, originatorID, null);
		}
		catch(ApplicationException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] exception",ex);
			throw new FindEntityException(ex.getMessage());
		}
		catch(SystemException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] exception",ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] error", ex);
			throw new SystemException("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] error",ex);
		}
		return docInfo;
	}
	
/**
   * Locate a number of DocumentMEtaInfo Obj that satisfy the
   * filtering condition.
   *
   * @param processInstanceID 
   * @param originatorID
   * @param processInstanceInfoUID the process instance meta info uid.
   * @param sortFilter contain the sorting info about which field we need to sort.
   * @return a Collection of DocumentMEtaInfo found, or empty collection if none
   * exists.
   */
	private Collection findByProcessInstanceIDAndOriginatorID(String processInstanceID, String originatorID, 
			                                                      Long processInstanceInfoUID, IDataFilter sortFilter)
		throws FindEntityException, SystemException
	{
		Collection docInfo = null;
		try
		{
			docInfo = getDocumentMetaInfoEntityHandler().findByPIIDAndOriginatorIDAndPIUID(
							processInstanceID, originatorID, processInstanceInfoUID, sortFilter);
		}
		catch(ApplicationException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] exception",ex);
			throw new FindEntityException(ex.getMessage());
		}
		catch(SystemException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] exception",ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] error", ex);
			throw new SystemException("[SearchESDocumentManagerBean.findByProcessInstanceIDAndDuns] error",ex);
		}
		return docInfo;
	}
	
/**
   * Locate audit file for viewing
   * @param info 
   */
  private RNFileContainer getDocumentFileContent(DocumentMetaInfo info, String auditType)
  	     throws ApplicationException
  {
  		RNFileContainer container = new RNFileContainer();
  		RNFileExtracter fileExtract = RNFileExtracter.getInstance();
  		File auditF = null;
  		
    	try
    	{
    		File dummy = FileUtil.getFile(IEStorePathConfig.ESTORE_DATA_DIRECTORY,"");
    		String filepath = dummy.getAbsolutePath()+"/"+info.getFilePath();
    		
    		String entryname = "";
    		File tempFile = null;
    		
    		//extract out audit file
    		String auditFilename = "";
    		if(IEStoreConstants.AUDIT_TYPE_AUDIT.equals(auditType))
    		{
    			auditFilename = info.getFilename();
    		}
    		else if(IEStoreConstants.AUDIT_TYPE_RECEIPT_AUDIT.equals(auditType))
    		{
    			auditFilename = info.getReceiptAuditFilename();
    		}
    		else
    		{
    			throw new IllegalArgumentException("[SearchESDocumentManagerBean.getDocumentFileContent] Audit type "+auditType+" is not supported !");
    		}
    		
    		//31 Mar 2006
    		if(auditFilename != null && ! "".equals(auditFilename) && 
    				(info.getFilePath()!=null && info.getFilePath().length() > 0))
    		{
    				entryname = IEStoreConstants.AUDIT_FOLDER+ auditFilename;
    				auditF = unzipFromZipFile(filepath, entryname, "");
    						
    				if(auditF != null)
    				{
    					//Extract out the content from rn file
    					container = fileExtract.extractRNContent(auditF);		
    				
    					//TEST
    					if(container != null)
    					{
    						String preamble = container.getPreamble();
    						String delivery = container.getDeliveryHeader();
    						String serHeader = container.getServiceHeader();
    						String serContent = container.getServiceContent();
    					}
    				}
    		}
    					
    		return container;
    	}
    	catch(Exception ex)
    	{
    		throw new ApplicationException("[SearchESDocumentManagerBean.getDocumentFileContent] error",ex);
    	}
    	finally
    	{
    		if(auditF != null)
    		{
//    		delete the temp file
					deleteTempFile(IEStorePathConfig.ESTORE_TEMP, _TEMP_SUB_FOLDER, auditF.getName(), auditF);
    		}
    	}
  }
  
  /**
   * Get the physical gdoc file given the DocumentMetaInfo UID. Udoc file will also be extracted.
   * @param uid the UID of DocumentMetaInfo obj.
   * @return the physical gdoc file or Null if the gdoc file is not existed
   * @throws ApplicationException if we have problem in getting the necessary file eg the gdoc and udoc file. 
   */
  public File getGDocInfoByDocUID(Long uid) throws ApplicationException
  {
  	try
  	{
  		DocumentMetaInfo docInfo = getDocumentMetaInfoEntityHandler().findByUID(uid);
  		if(docInfo == null)
  		{
  			throw new ApplicationException("[SearchESDocumentManagerBean.getGDocInfoByDocUID] Can't find DocumentMetaInfo given UID "+ uid);
  		}
  		File dummy = FileUtil.getFile(IEStorePathConfig.ESTORE_DATA_DIRECTORY,"");
  		String zipFilepath = dummy.getAbsolutePath()+"/"+docInfo.getFilePath(); 
  		File gdocFile = getGDocFile(zipFilepath, docInfo);
		
  		//Prepare the udoc for download
  		String udocFilename = docInfo.getUDocFilename();
  		if(udocFilename != null && ! "".equals(udocFilename))
  		{
  			String udocEntryName = IEStoreConstants.UDOC_FOLDER+ docInfo.getFolder()+"/"+udocFilename; 
        String outputUdocFilename = FileUtil.extractFilename(udocFilename); 
  			unzipFromZipFile(zipFilepath, udocEntryName, outputUdocFilename);
  		}
  		
  		//Prepare the attachment for download
  		prepareAttachmentFileForDownlaod(zipFilepath, docInfo);
  		
      //prepare audit file for download
      prepareAuditFileForDownload(zipFilepath, docInfo);
      
  		return gdocFile;
  	}
  	catch(Exception ex)
  	{
  		throw new ApplicationException("[SearchESDocumentManagerBean.getDocumentFileContent] Error in getting the griddocument", ex);
  	}
  }
  
  private File getGDocFile(String zipFilePath, DocumentMetaInfo docInfo) throws Exception
  {
  	String gdocFilename = docInfo.getGdocFilename();
  	if(gdocFilename != null && !"".equals(gdocFilename))
  	{
  		String entryName = IEStoreConstants.GDOC_FOLDER+gdocFilename;
  		String outputFileName = FileUtil.extractFilename(entryName);
  		return unzipFromZipFile(zipFilePath, entryName, outputFileName);
  	}
  	return null;
  }
  
  private void prepareAttachmentFileForDownlaod(String zipFilePath, DocumentMetaInfo docInfo) throws Exception
  {
  	String attachmentFilenames = docInfo.getAttachmentFilenames();
  	if(attachmentFilenames != null && ! "".equals(attachmentFilenames))
  	{
  		StringTokenizer st = new StringTokenizer(attachmentFilenames, ":");
  		while(st.hasMoreTokens())
  		{
  			String attName = st.nextToken();
  			String entryName = IEStoreConstants.ATTACHMENT_FOLDER +attName;
  			unzipFromZipFile(zipFilePath, entryName, attName);
  		}
  	}
  }
  
  private void prepareAuditFileForDownload(String zipFilePath, DocumentMetaInfo docInfo) throws Exception
  {
    String auditFilename = docInfo.getFilename();
    String receiptAuditFilename = docInfo.getReceiptAuditFilename();
    
    if(auditFilename != null && ! "".equals(auditFilename))
    {
      String entryName = IEStoreConstants.AUDIT_FOLDER+auditFilename;
      unzipFromZipFile(zipFilePath, entryName, FileUtil.extractFilename(auditFilename));
    }
    
    if(receiptAuditFilename != null && ! "".equals(receiptAuditFilename))
    {
      String entryName = IEStoreConstants.AUDIT_FOLDER+receiptAuditFilename;
      unzipFromZipFile(zipFilePath, entryName, FileUtil.extractFilename(receiptAuditFilename));
    }
  }
  
/**
   * The zip file specify at the zipFileLocation is containing audit file or udocFile.
   * This method will unzip the audit or udoc file from the zip file. Allow specify the outputFilename for
   * the file we extract
   * @param zipFileLocation
   * @param entryName
   * @param outputFilename
   * @return a file which is unzip from the zip file where located in zipFileLocation or Null
   *         if the zipFile represent by the zipFileLocation is not exist or the entryName is not
   *         existed in the zipFile.
   */
  private File unzipFromZipFile(String zipFileLocation, String entryName, String outputFilename)
          throws Exception
  {
  	try
  	{
  		Logger.log("unzipFromZipFile zipFileLocation "+zipFileLocation+" entryName "+entryName);
  		
  		File zipF = new File(zipFileLocation);
  		if(! zipF.exists())
  		{
  			ApplicationException ex = new ApplicationException("[SearchESDocumentManagerBean.unzipFromZipFile] Zip file "+zipFileLocation+" is not exist !!!");
  			Logger.warn("[SearchESDocumentManagerBean.unzipFromZipFile] Zip File not found ", ex);
  			return null;
  		}
  		
  		try
  		{
  			ZipFile zipFile = new ZipFile(zipFileLocation);
  			return extractZipEntry(zipFile, entryName, outputFilename);
  		}
  		catch(ZipException zipEx)
  		{
  			//28062006 The length of the given zipFileLocation is too long,
  			//Class ZipFile has problem in opening such a file, let's try using
  			//ZipInputStream
  			FileInputStream in = new FileInputStream(zipF);
  			ZipInputStream zipIn = new ZipInputStream(in);
  			return extractZipEntry(zipIn, entryName, outputFilename);
  		}
  	}
  	catch(Exception ex)
  	{
  		Logger.warn("[SearchESDocumentManagerBean.unzipFromZipFile] error is ",ex);
  		throw new SystemException(ex);
  	}
  }
  
  /**
   * 28062006 Extract the file within the zipFile given the entryName.
   * @param zipFile
   * @param entryName
   * @return the extracted file or Null of the given entryName can't found in the zipFile.
   * @throws Exception
   */
  private File extractZipEntry(ZipFile zipFile, String entryName, String outputFilename)
  	throws Exception
  {
  		InputStream input = null;
  		FileOutputStream output = null;
  		
  		try
  		{
  			ZipEntry zipEntry = zipFile.getEntry(entryName);
  			if(zipEntry == null)
  			{
  				ApplicationException ex1 = new ApplicationException("[SearchESDocumentManagerBean.extractZipEntry] Cannot find zip entry ["+entryName+"] in zip file "+zipFile.getName());
  				Logger.warn("[SearchESDocumentManagerBean.extractZipEntry] Cannot find zip entry", ex1);
  				return null;
  			}
  			
  			if(outputFilename == null || "".equals(outputFilename))
  			{
  				outputFilename = "temp_"+getTimestampFormat("yyyyMMddHHmmssSSS")+".tmp";
  			}
  			
  			File outputFile = null;
  			
  			if(! FileUtil.exist(IEStorePathConfig.ESTORE_TEMP, _TEMP_SUB_FOLDER+"/", outputFilename))
  			{
  				outputFile = FileUtil.createNewLocalFile(IEStorePathConfig.ESTORE_TEMP,_TEMP_SUB_FOLDER+"/",outputFilename);
  			}
  			else //replace the existing temp file.
  			{
  				outputFile = FileUtil.getFile(IEStorePathConfig.ESTORE_TEMP, _TEMP_SUB_FOLDER+"/", outputFilename);
  			}
  			output = new FileOutputStream(outputFile);
  			input = zipFile.getInputStream(zipEntry);
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
  			throw ex;
  		}
  		finally
  		{
  			if(output != null)
  			{
  				output.close();
  				output = null;
  			}
  			
  			if(input != null)
  			{
  				input.close();
  			}
  			
  			if(zipFile != null)
  			{
  				zipFile.close();
  			}
  		}
  }
  
  /**
   * 28062006 Extract the file from the ZipInputStream given the entryName.
   * (This method is used while we have problem extract the zip file(the filename is too long) 
   * through Class ZipFile. It is not recommended to use if the zip file contains
   * lot of zip entries)
   * @param zipIn
   * @param entryName
   * @param outputFilename
   * @return
   * @throws Exception
   */
  private File extractZipEntry(ZipInputStream zipIn, String entryName, String outputFilename)
  	throws Exception
  {
  	InputStream input = null;
  	FileOutputStream output = null;
  	ZipEntry entry = null;
  	
  	try
  	{
  		if(outputFilename == null || "".equals(outputFilename))
			{
				outputFilename = "temp_"+getTimestampFormat("yyyyMMddHHmmssSSS")+".tmp";
			}
			
			File outputFile = null;
			
			if(! FileUtil.exist(IEStorePathConfig.ESTORE_TEMP, _TEMP_SUB_FOLDER+"/", outputFilename))
			{
				outputFile = FileUtil.createNewLocalFile(IEStorePathConfig.ESTORE_TEMP,_TEMP_SUB_FOLDER+"/",outputFilename);
			}
			else //replace the existing temp file.
			{
				outputFile = FileUtil.getFile(IEStorePathConfig.ESTORE_TEMP, _TEMP_SUB_FOLDER+"/", outputFilename);
			}
			output = new FileOutputStream(outputFile);
			
			while( (entry = zipIn.getNextEntry()) != null )
			{
				if(entryName.equals(entry.getName()))
				{
					byte [] byteArray = new byte[512];
	  			int byteRead;
	  			while((byteRead = zipIn.read(byteArray)) > -1)
	  			{
	  				output.write(byteArray,0, byteRead);
	  			}
	  			break;
				}
			}
			return outputFile;
  	}
  	catch(Exception ex)
  	{
  		throw ex;
  	}
  	finally
  	{
  		if(output != null)
  		{
  			output.close();
  		}
  		if(input != null)
  		{
  			input.close();
  		}
  		if(zipIn != null)
  		{
  			zipIn.close();
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
	 * Locate a number of ProcessInstanceMetaInfo UID(PK) based on the given search criteria.
	 * @param The filter is created at action side. It is generated based on the criteria a 
	 * users key in (eg they wanna search doc meta info based on doc no they specified).
	 */
	public Collection findEsPiKeys(IDataFilter filter)
		throws FindEntityException, SystemException
	{
		Collection processInstanceInfoKey = null;
		
  	try
  	{
  		processInstanceInfoKey = this.getProcessInstanceMetaInfoEntityHandler().getKeyByFilterForReadOnly(filter);
  	}
  	catch (ApplicationException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsPiKeys] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsPiKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsPiKeys] Error ", ex);
      throw new SystemException(
        "[SearchESDocumentManagerBean.findEsPiKeys] Error ",ex);
    }
    
  	return processInstanceInfoKey;
	}
	
/**
	 * Locate a list of ProcessInstance MetaInfo that fulfil the filter condition
	 * @param filter filter it contain a domain filter which take in list of doc UID.
	 * @return a list of ProcessInstance MetaInfo or empty list if not fulfil the filter
	 * 				 condition.
	 */
	public Collection findEsPiEntityList(IDataFilter filter)
				 throws FindEntityException, SystemException
	{
		Collection processInstanceMetaInfo = new ArrayList();
		
    try
    {
    	processInstanceMetaInfo = this.getProcessInstanceMetaInfoEntityHandler().getEntityByFilterForReadOnly(filter);
    	//@@
    	Logger.log("[SearchESDocumentManagerBean.findEsPiEntityList] finished search process instance meta info");
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsPiEntityList] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsPiEntityList] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsPiEntityList] Error ", ex);
      throw new SystemException(
        "[SearchESDocumentManagerBean.findEsPiEntityList] Error ",ex);
    }
  	return processInstanceMetaInfo;
	}
	
	/**
	 * Locate a number of DocumentMetaInfo UID(PK) based on the given search criteria.
	 * The filter is created at action side. It is generated based on the criteria a 
	 * users key in (eg they wanna search doc meta info based on doc no they specified). 
	 * @param filter
	 * @return
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public Collection findEsDocKeys(IDataFilter filter) 
	    throws FindEntityException, SystemException
	{
		Collection documentKeyList = null;
    try
    {
    	documentKeyList = getDocumentMetaInfoEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsDocKeys] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsDocKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsDocKeys] Error ", ex);
      throw new SystemException(
        "SearchESDocumentManagerBean.findEsDocKeys(filter) Error ",ex);
    }

    return documentKeyList;
	}
	
	/**
	 * Locate a DocumentMetaInfo based on its UID
	 * @param uid DocumentMetaInfo's UID
	 * @return DocumentMetaInfo OBJ or null if not exist
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public DocumentMetaInfo findEsDoc(Long UID)
				 throws FindEntityException, SystemException
	{
		DocumentMetaInfo docInfo = null;
		try
		{
			docInfo = this.getDocumentMetaInfoEntityHandler().findByUID(UID);
		}
		catch(FindEntityException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findEsDoc] BL Exception", ex);
			throw new FindEntityException(ex);
		}
		catch(SystemException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findEsDoc] System Exception", ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findEsDoc] Error ", ex);
      throw new SystemException(
      		"SearchESDocumentManagerBean.findEsDoc(uid) Error ",ex);
		}
		return docInfo;
	}
	
	/**
	 * Locate a collection of DocumentMEtaInfo obj based on the given search criteria
	 * @param filter it contain a domain filter which take in list of doc UID.
	 * @return
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public Collection findEsDocEntityList(IDataFilter filter)
	 				throws FindEntityException, SystemException
	{
		
		Collection documentMetaInfoList = null;
    try
    {
    	documentMetaInfoList = getDocumentMetaInfoEntityHandler().getEntityByFilterForReadOnly(filter);
    	
    	//Do a post-process on the document meta info
    	postProcessDocumentMetaInfo(documentMetaInfoList);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsDocEntityList] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsDocEntityList] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SearchESDocumentManagerBean.findEsDocEntityList] Error ", ex);
      throw new SystemException(
        "[SearchESDocumentManagerBean.findEsDocEntityList] Error ",
        ex);
    }
		
		return documentMetaInfoList;
		
	}

	/**
	 * Locate a ProcessInstanceMetaInfo by its UID
	 * @param uid ProcessInstanceMetaInfo's UID
	 * @return ProcessInstanceMetaInfo OBJ or null if not exist
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public ProcessInstanceMetaInfo findEsPIByUID(Long uid)
				 throws FindEntityException, SystemException
	{
		ProcessInstanceMetaInfo metaInfo = null;
		try
		{
			metaInfo = this.getProcessInstanceMetaInfoEntityHandler().findByUID(uid);
		}
		catch(FindEntityException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findEsPIByUID] BL Exception", ex);
			throw new FindEntityException(ex);
		}
		catch(SystemException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findEsPIByUID] System Exception", ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findEsPIByUID] Error ", ex);
      throw new SystemException(
      		"SearchESDocumentManagerBean.findEsPIByUID(uid) Error ",ex);
		}
		return metaInfo;
	}

	/**
	 * Locate a list of Document MetaInfo UID which associate to a ProcessInstance meta info
	 * ProcessInstanceID and originatorID serve as a linkage between one processInstance meta info and its
	 * related document meta infos.
	 * @param processInstanceID 
	 * @param originatorID 
	 * @param processStartDate the start date time of the process instance
	 * @param IDataFilter indicate the doc meta info field we need to sort and the sorting order.
	 * @return a list of Document MetaInfo UID  or emptyList if we can't find the associate doc meta info
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	private Collection getAssociateDocMetaUID(String processInstanceID, String originatorID, Long processInstanceInfoUID,
			                                      IDataFilter filter)
				 throws FindEntityException, SystemException
	{
		ArrayList docUIDList = new ArrayList();
		try
		{
				Collection documentMetaList = this.findByProcessInstanceIDAndOriginatorID(processInstanceID,
																					originatorID, processInstanceInfoUID, filter);
			
				if(documentMetaList !=null)
				{
					Iterator ite = documentMetaList.iterator();
					while(ite.hasNext())
					{
						DocumentMetaInfo info = (DocumentMetaInfo)ite.next();
						docUIDList.add(info.getKey());
					}
				}
		}
		catch(FindEntityException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.getAssociateDocMetaUID] BL Exception", ex);
			throw new FindEntityException(ex);
		}
		catch(SystemException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.getAssociateDocMetaUID] System Exception", ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.getAssociateDocMetaUID] Error ", ex);
      throw new SystemException(
      		"SearchESDocumentManagerBean.getAssociateDocMetaUID(PIuid) Error ",ex);
		}
		return docUIDList;
	}

	/**
	 * This method will post-process the document meta info list.
	 * 1) It will infer the start time and end time based on the folder
	 * 2) It will generate the file that contain the audit file content (contains preamble header, 
	 *    delivery header(depend rnif version), service header, service content
	 *    and some useful info eg doc no, process instance id etc)
	 * Based on the document folder, we can infer the start time, end time.
	 * @param documentMetaInfo a collection of document meta info obj
	 */
	private void postProcessDocumentMetaInfo(Collection documentMetaInfo)
		throws ApplicationException
	{
		if(documentMetaInfo!=null)
		{
			Iterator i = documentMetaInfo.iterator();
			while(i.hasNext())
			{
				DocumentMetaInfo docInfo = (DocumentMetaInfo)i.next();
				
				//infer start time, end time
				inferStartTimeEndTime(docInfo);
				
				//generate the file that contain the audit file content
				//getDocumentFileContent(docInfo, "");

			}
		}
	}
	
	/**
	 * Based on the DocumentMEtaInfo folder, we can infer its start time and end time.
	 * Eg if its folder is in Inbound, then its start time and end time will be
	 *    DateTimeReceivedStart, DateTimeReceivedEnd
	 * Note: UI will always fetch the start time and end time from field 
	 *       DateTimeSendStart, DateTimeSendEnd
	 * @param docInfo the DocumentMetaInfo obj
	 */
	private void inferStartTimeEndTime(DocumentMetaInfo docInfo)
	{
		//	infer the start time and end time
		if(docInfo.getFolder().compareTo("Import")==0)
		{
			docInfo.setDateTimeSendStart(docInfo.getDateTimeImport());
			docInfo.setDateTimeSendEnd(null);
		}
		else if(docInfo.getFolder().compareTo("Inbound")==0)
		{
			Date startTime = docInfo.getDateTimeReceiveEnd();
			//Date endTime = docInfo.getDateTimeReceiveEnd();
			
			docInfo.setDateTimeSendStart(null);
			docInfo.setDateTimeSendEnd(startTime);
		}
		else if(docInfo.getFolder().compareTo("Outbound")==0)
		{
      Date sendEnd = docInfo.getDateTimeSendEnd();
      docInfo.setDateTimeSendStart(sendEnd);
			docInfo.setDateTimeSendEnd(null);
		}
		else if(docInfo.getFolder().compareTo("Export")==0)
		{
			docInfo.setDateTimeSendStart(docInfo.getDateTimeExport());
			docInfo.setDateTimeSendEnd(null);
		}
	}
	
/**
	 * Retrieve a list of Document meta info UID which tie to a process instance meta info.
	 * The retrieved result will also be sorted based on the given filter.
	 * @param filter It will contain the UID of a PI Meta Info and sort criteria.
	 * @return a list of document meta info's UIDs(PK) or empty list if no record
	 *         satisfy such query.
	 */
	public Collection findAssocEsDocKeys(IDataFilter filter)
				 throws FindEntityException, SystemException
	{
		Collection assocDocKey = null;
		
		try
		{
			
			//retrieve the Process Instance MetaInfo obj
			Collection c = this.getProcessInstanceMetaInfoEntityHandler().getEntityByFilter(filter);
			
			if(c!=null && c.size()>0)
			{
				Iterator ite = c.iterator();
				ProcessInstanceMetaInfo proInfo = (ProcessInstanceMetaInfo)ite.next();
				
				//The filter pass in include the sorting info that contains which docMetaInfo's field  we need to sort
				Object[] orderField = filter.getOrderFields();
				boolean [] sortOrder = filter.getSortOrders();
				
				//1 DEC 2005 We need to the sorting info when we fetch the docMetaInfo.
				DataFilterImpl sortFilter = new DataFilterImpl();
				sortFilter.setOrderFields(orderField, sortOrder);
				
				//retrieve the doc uid that tie to that process instance meta info
				assocDocKey = getAssociateDocMetaUID(proInfo.getProcessInstanceID(), proInfo.getOriginatorID(), (Long)proInfo.getKey(),
						                                 sortFilter);
			}
	
		}
		catch(FindEntityException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findAssocEsDocKeys] BL Exception", ex);
			throw new FindEntityException(ex.getMessage(),ex);
		}
		catch(SystemException ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findAssocEsDocKeys] System Exception", ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findAssocEsDocKeys] Error ", ex);
      throw new SystemException(
      		"SearchESDocumentManagerBean.findAssocEsDocKeys(filter,PIUid) Error ",ex);
		} 
		return assocDocKey;
	}
	
/**
	 * Return a list of code from code_value table. 
	 * 
	 * ProcessDef, docType, processState will be pre-stored into code_value table while estoring.
	 * This will be efficient if we fetch the above elements from code_value table instead of
	 * fetching from process_instance_meta_info, document_meta_info table directly.
	 * @param UID
	 * @return FieldValueCollection or null if no docType or process def found in Process_Instance_MetaInf
	 * 				 or document_meta_info table.
	 * @throws Exception
	 */
	public FieldValueCollection getProcessDef(Long UID) throws Exception
	{
		Logger.log("[SearchESDocumentManagerBean.getProcessDef] Fetching list of code value.");
		Collection codeValue = null;
		CodeValueHelper cvHelper = CodeValueHelper.getInstance();
		if(UID!=null && UID.longValue()== IFieldValueCollection.PROCESS_DEF.longValue()) //process def list
		{
			//docType = this.getProcessInstanceMetaInfoEntityHandler().getProcessDef();
			codeValue = cvHelper.retrieveByEntityTypeAndFieldID(ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_Def);
		}
		else if(UID!=null && UID.longValue() == IFieldValueCollection.DOC_TYPE.longValue()) //doc type list
		{
			//docType = this.getDocumentMetaInfoEntityHandler().getDocumentTypes();
			codeValue = cvHelper.retrieveByEntityTypeAndFieldID(DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Doc_Type);
		}
		else if(UID!=null && UID.longValue() == IFieldValueCollection.PROCESS_STATE.longValue() ) //process state list
		{
			codeValue = cvHelper.retrieveByEntityTypeAndFieldID(ProcessInstanceMetaInfo.class.getName(), (Integer)ProcessInstanceMetaInfo.Process_State);
		}
		else if(UID!=null && UID.longValue() == IFieldValueCollection.PARTNER_ID.longValue()) //partner ID list
		{
			codeValue = cvHelper.retrieveByEntityTypeAndFieldID(DocumentMetaInfo.class.getName(), (Integer)DocumentMetaInfo.Partner_ID);
		}
		else
		{
			throw new IllegalArgumentException("[SearchESDocumentManagerBean.getProcessDef] FieldValueCollection ID "+UID+" is not supported !!! ");
		}
		
		if(codeValue!=null)
		{
			Iterator i = codeValue.iterator();
			ArrayList a = new ArrayList();
			while(i.hasNext())
			{
				CodeValue cv = (CodeValue)i.next();
				a.add(cv.getCode());
			}
			return new FieldValueCollection(a);
		}
		
		return null;
	}
	
	/**
	 * To locate the receipt audit file info given the document meta info uid.
	 * The audit file info will be used by UI to render the audit file.
	 * @param UID
	 * @return
	 * @throws ApplicationException
	 */
	public AuditFileInfo findReceiptAuditInfoByUID(Long UID)throws ApplicationException
	{
		return getAuditFileInfo(UID, IEStoreConstants.AUDIT_TYPE_RECEIPT_AUDIT);
	}
	
	/**
	 * To locate the audit file info given the document meta info uid.
	 * The audit file info will be used by UI to render the audit file.
	 * @param UID
	 * @return
	 * @throws ApplicationException
	 */
	public AuditFileInfo findAuditFileInfoByUID(Long UID)throws ApplicationException
	{
		return getAuditFileInfo(UID, IEStoreConstants.AUDIT_TYPE_AUDIT);
	}
	
	/**
	 * To locate the audit file info given the document meta info uid.
	 * The audit file info will be used by UI to render the audit file.
	 * @param UID the document meta info's uid
	 * @return
	 */
	private AuditFileInfo getAuditFileInfo(Long UID, String auditType)
		throws ApplicationException
	{
		try
		{
			DocumentMetaInfo docInfo = this.findEsDoc(UID);
			AuditFileInfo auditInfo = null;
			if(docInfo != null)
			{
				RNFileContainer container = getDocumentFileContent(docInfo, auditType);
				
				String filename = docInfo.getFilename() == null ? "" : FileUtil.extractFilename(docInfo.getFilename());
				String docNo = docInfo.getDocNumber();
				String docType = docInfo.getDocType();
				String partnerID = docInfo.getPartnerID();
				String partnerDuns = docInfo.getPartnerDuns();
				String partnerName = docInfo.getPartnerName();
				Date dateCreated = docInfo.getDateTimeCreate();
				String preamble = container.getPreamble();
				String deliveryHeader = container.getDeliveryHeader();
				String serviceHeader = container.getServiceHeader();
				String serviceContent = container.getServiceContent();
			
				auditInfo = new AuditFileInfo(filename, docNo, docType, partnerID, partnerDuns, partnerName,
					                 dateCreated, preamble, deliveryHeader, serviceHeader, 
                           serviceContent, String.valueOf(UID));
			}
			return auditInfo;
		}
		catch(Exception ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.findAuditFileInfoByUID] Cannot search for audit info for UID "+ UID, ex);
			throw new ApplicationException("[SearchESDocumentManagerBean.findAuditFileInfoByUID] Unable to fetch audit file info given uid "+UID, ex);
		}
	}
	
	
	private void deleteTempFile(String pathKey, String subPath, String filename, File tempFile)
	{
		boolean isExist = true;
		try
		{
			/*
			boolean isExist = FileUtil.exist(pathKey, subPath+"/", filename);
			if(isExist)
			{
				FileUtil.delete(pathKey, subPath+"/"+ filename);
			}
			*/
			isExist = tempFile.delete(); 
			Logger.log("File "+tempFile.getAbsolutePath()+" Delete status is "+isExist);
		}
		catch(Exception ex)
		{
			Logger.warn("[SearchESDocumentManagerBean.deleteTempFile] Unable to delete temp file with path key "+ pathKey+", subPath "+subPath+", filename "+filename+" delete status "+isExist, ex);
			
		}
	}	
  
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
