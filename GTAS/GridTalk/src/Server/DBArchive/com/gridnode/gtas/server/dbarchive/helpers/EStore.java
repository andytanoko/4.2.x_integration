/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EStore.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 2, 2005        Tam Wei Xiang       Created
 * Oct 5, 2005        Lim Soon Hsiung     Handle trigger alert
 * Jan 18,2006        Tam Wei Xiang       Modified method triggerAlert(boolean).
 *                                        Added instance variable: _numRecordBeEStored
 *                                        Added method: triggerEStoreStartupAlert(),setNumRecordBeEStored
 *                                                      setArchiveByProcessInstance   
 * Mar 29,2006        Tam Wei Xiang       modified methid prepareAuditFile(list, list, hashtable)
 * Apr 24,2006        Tam Wei Xiang       modified method generateDocInfo(). The timestamp
 *                                        after parsing only capture till minute.
 * May 05,2006        Tam Wei Xiang       modified method generateDocInfo().
 *                                        The partner duns has been populated incorretly      
 * Sep 15,2006        Tam Wei Xiang       Added in flag to determine whether we need to perform
 *                                        the estore process.                                                                                                                                 
 */

package com.gridnode.gtas.server.dbarchive.helpers;

/**
 * This class serves as a delegator to the EStore related session bean.
 * 
 * @author Tam Wei Xiang
 *
 */

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.gtas.server.dbarchive.exception.EStoreException;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IProcessDocumentManagerHome;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IProcessDocumentManagerObj;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.document.folder.ExportFolder;
import com.gridnode.gtas.server.document.folder.ImportFolder;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;
import com.gridnode.gtas.server.dbarchive.model.DocInfo;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceInfo;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import static com.gridnode.gtas.server.dbarchive.helpers.IEStoreConstants.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class EStore
{
	public static final String ESTORE_ALERT_NAME = "ESTORE_ALERT";
	public static final String ESTORE_STARTUP_ALERT_NAME = "ESTORE_STARTUP_ALERT";
	
	private boolean _isEStoreEnabled;
	private ArrayList _docInfoList; 
	private EStoreHelper _esHelper;
  private DefaultProviderList _providerList = new DefaultProviderList();
  private boolean _sendAlert = true;
  
  private Date _estoreStartTime;
  private Date _estoreEndTime;
	
  //04 APR 2006
  private Hashtable _criteriaHTable;
  private int _numExpectedRecordBeEStored = 0; //indicate the expected record be estored.
  
  //01092006
  private boolean _isArchiveByProcess;
  private IProcessDocumentManagerObj _processDocMgr;
  
	public EStore(boolean isEnabled) throws Exception
	{
		_esHelper = EStoreHelper.getInstance();
		_isEStoreEnabled = isEnabled;
		_estoreStartTime = new Date();
		_processDocMgr = getProcessDocMgr();
		Logger.log("[EStore] EStore enableStatus "+ _isEStoreEnabled);
	}
	
	public void populateDoc(GridDocument gdoc, Hashtable<Long,RNProfile> rnprofileHTB, String[] gdocFilesArray) throws EStoreException
	{
		if(! _isEStoreEnabled) 
		{
			return;
		}
		
		try
		{
			if(_processDocMgr == null)
			{
				_processDocMgr = getProcessDocMgr();
			}
			DocInfo info = generateDocInfo(gdoc, rnprofileHTB, _isArchiveByProcess, gdocFilesArray);
			_processDocMgr.processDocInfo(info);
		}
		catch(Exception ex)
		{
			throw new EStoreException("[EStore.populateDoc] problem in populating doc into estore.", ex );
		}
	}
	
	public void populateProcessInstance(Map<Integer, Object> piMap) throws EStoreException
	{
		if(! _isEStoreEnabled) 
		{
			return;
		}
		
		SimpleDateFormat df = new SimpleDateFormat(IEStoreConstants.DATE_TIME_PATTERN);
		try
		{
			if(_processDocMgr == null)
			{
				_processDocMgr = getProcessDocMgr();
			}
			ProcessInstanceInfo proInfo = getProcessInstance(piMap, df);
			if(proInfo != null)
			{
					_processDocMgr.populateProcessInstance(proInfo);
			}
		}
		catch(Exception ex)
		{
			throw new EStoreException("[EStore.populateProcessInstance] problem in populating process instance. ", ex);
		}
	}
	
	public void setIsArchiveByProcess(boolean isArchivedByProcess)
	{
		_isArchiveByProcess = isArchivedByProcess;
	}
	
	private IProcessDocumentManagerObj getProcessDocMgr() throws ServiceLookupException
	{
		return (IProcessDocumentManagerObj)ServiceLocator.instance(
	         	ServiceLocator.CLIENT_CONTEXT).getObj(
	      		IProcessDocumentManagerHome.class.getName(),
	      		IProcessDocumentManagerHome.class,
	      		new Object[0]);
	}
	
	private ProcessInstanceInfo getProcessInstance(Map<Integer, Object> processMap, SimpleDateFormat df)
	{
		if(processMap == null)
		{
			return null;
		}
		
		String processInstanceID = (String)processMap.get(PROCESS_INSTANCE_ID);
		String processState = ((Integer)processMap.get(PROCESS_STATE)).toString();
		String processStartDate = (Date)processMap.get(START_TIME)==null?"":df.format((Date)processMap.get(START_TIME));
		String processEndDate = (Date)processMap.get(END_TIME)==null?"":df.format((Date)processMap.get(END_TIME));
		String processDef = (String)processMap.get(PROCESS_DEF);
		String roleType = (String)processMap.get(ROLE_TYPE);
		String originatorID = (String)processMap.get(ORIGINATOR_ID);
    
		Integer failedReason = (Integer)processMap.get(FAILED_REASON);
    String detailReason = (String)processMap.get(DETAIL_REASON);
    Integer retryNumber = (Integer)processMap.get(RETRY_NUMBER);
    
		return new ProcessInstanceInfo(processInstanceID,
		                                                          processState, processStartDate, processEndDate,
		                                                          processDef, roleType, originatorID,
                                                              failedReason, detailReason, retryNumber);
	}
	
	public boolean getIsEstoreEnabled()
	{
		return _isEStoreEnabled;
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
	
	//to generate the DocInfo object. It will be used to transform to XML format
	//and be used to keep track of the info for audit file. the linkage between
	//audit file and process instance is also captured in this object.
	private DocInfo generateDocInfo(GridDocument gdoc, 
			                            Hashtable<Long,RNProfile> rnprofileHTable, boolean isArchivedByPI,
			                            String[] gdocFilesArray) 
	        throws Exception
	{ 
		SimpleDateFormat DF = new SimpleDateFormat(IEStoreConstants.DATE_TIME_PATTERN);
		
		String senderTP = "";
		String receiverTP = "";
		String partnerName = "";
		String partnerID = "";
		String filename = "";
		String receiptAuditFilename = "";
		String udocFilename = "";
		String gdocFilename = "";
		String attachmentFilename = "";
		String rnifVersion = "";
		String senderCertUID = "", receiverCertUID = "";
		String docDateGenerated = gdoc.getDocDateGen()==null?"":gdoc.getDocDateGen();		
		String docNo = gdoc.getUdocNum()!=null?gdoc.getUdocNum():""; //compulsary field in GT
		String folder = gdoc.getFolder();
		//{originatorID, partnerDuns} uniqueness of processInstance = processInstanceID+originatorID
		//we use processInstanceID+originatorID to link between processInstance and its related document
		String originatorID = "",partnerDuns ="";
		if(rnprofileHTable!=null)
		{
			RNProfile profile = (RNProfile)rnprofileHTable.get(gdoc.getRnProfileUid());
			originatorID = profile!=null?profile.getProcessOriginatorId():"";
			rnifVersion = profile!=null?profile.getRNIFVersion():"";
			
			if(profile != null)
			{
				if("Outbound".equals(folder))
				{
					partnerDuns = profile.getReceiverGlobalBusIdentifier();
				}
				else if("Inbound".equals(folder) || ("Export".equals(folder) && "Inbound".equals(gdoc.getSrcFolder())))
				{
					partnerDuns = profile.getSenderGlobalBusIdentifier();
				}
			}
		}
		
		partnerID = getPartnerID(gdoc);
		partnerName = getPartnerName(gdoc);
    
		senderCertUID = gdoc.getSenderCert()!=null?String.valueOf(gdoc.getSenderCert().longValue()):null;
		receiverCertUID = gdoc.getReceiverCert()!=null?String.valueOf(gdoc.getReceiverCert().longValue()):null;
		
		String docType = gdoc.getUdocDocType(); //_udocDocType
		String dateTimeSendStart = gdoc.getDateTimeSendStart()==null?"":DF.format(gdoc.getDateTimeSendStart());
		String dateTimeSendEnd = gdoc.getDateTimeSendEnd()==null?"":DF.format(gdoc.getDateTimeSendEnd());
		String dateTimeReceiveStart = gdoc.getDateTimeReceiveStart()==null?"":DF.format(gdoc.getDateTimeReceiveStart());
		String dateTimeReceiveEnd = gdoc.getDateTimeReceiveEnd()==null?"":DF.format(gdoc.getDateTimeReceiveEnd());
		String dateTimeCreate = gdoc.getDateTimeCreate()==null?"":DF.format(gdoc.getDateTimeCreate()); //when we searchDocument, the document date is based on the created date of the doc
		String dateTimeImport = gdoc.getDateTimeImport()==null?"":DF.format(gdoc.getDateTimeImport()); //may require during archiveByDoc
		String dateTimeExport = gdoc.getDateTimeExport()==null?"":DF.format(gdoc.getDateTimeExport()); //may require during archiveByDoc
		String processDef = gdoc.getProcessDefId();
		String processInstanceID = gdoc.getProcessInstanceID();
		
    //gdocFilesArray  0 GDOC file, 1 UDOC file, 2 AUDIT file, 3 Attachment filename(0...*)
		gdocFilename = gdocFilesArray[0];
		udocFilename = gdocFilesArray[1];
		filename = gdocFilesArray[2];
		receiptAuditFilename = gdocFilesArray[3];
		attachmentFilename = gdocFilesArray[4];
		
		String gdocID = gdoc.getGdocId().toString();  //GDOC's gdocID (gdocID+ folder can uniquely idendified one gdoc).
		
		return new DocInfo(docType, docNo, partnerDuns, partnerID,
					partnerName, docDateGenerated,
					dateTimeSendStart, dateTimeSendEnd, dateTimeReceiveStart,
					dateTimeReceiveEnd, dateTimeCreate, dateTimeImport,
					dateTimeExport, processDef, processInstanceID,
					filename, folder,originatorID, gdocID,isArchivedByPI,udocFilename, rnifVersion,
					senderCertUID, receiverCertUID, gdocFilename, attachmentFilename, gdoc.isOriginalDoc(), gdoc.hasAttachment(),
					receiptAuditFilename, gdoc.getDocTransStatus(), gdoc.getUserTrackingID());
	}
  
	private String getPartnerID(GridDocument gdoc)
  {
    String folder = gdoc.getFolder();
    String srcFolder = gdoc.getSrcFolder();
    
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getSenderPartnerId();
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getRecipientPartnerId();
    }
    else if(ImportFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getRecipientPartnerId();
    }
    else if(ExportFolder.FOLDER_NAME.equals(folder))
    {
      if(ImportFolder.FOLDER_NAME.equals(srcFolder))
      {
        return gdoc.getRecipientPartnerId();
      }
      else if(InboundFolder.FOLDER_NAME.equals(srcFolder))
      {
        return gdoc.getSenderPartnerId();
      }
      else
      {
        return "";
      }
    }
    else 
    {
      return "";
    }
  }
  
	private String getPartnerName(GridDocument gdoc)
  {
    String folder = gdoc.getFolder();
    String srcFolder = gdoc.getSrcFolder();
    
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getSenderPartnerName();
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getRecipientPartnerName();
    }
    else if(ImportFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getRecipientPartnerName();
    }
    else if(ExportFolder.FOLDER_NAME.equals(folder))
    {
      if(ImportFolder.FOLDER_NAME.equals(srcFolder))
      {
        return gdoc.getRecipientPartnerName();
      }
      else if(InboundFolder.FOLDER_NAME.equals(srcFolder))
      {
        return gdoc.getSenderPartnerName();
      }
      else
      {
        return "";
      }
    }
    else 
    {
      return "";
    }
  }
}
