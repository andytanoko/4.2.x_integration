/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 7, 2005        Tam Wei Xiang       Created
 * Jan 19,2006        Tam Wei Xiang       change the instance variable from GdocUID to GdocID.
 */

/**
 * This class will be used to map to XML file or vice versa.
 * The XML file will be transfered over to estore side for futher process
 *
 * Tam Wei Xiang
 * 
 * @version 1.0
 * @since 1.0
*/
package com.gridnode.gtas.server.dbarchive.model;

import java.io.Serializable;

public class DocInfo implements Serializable
{	
	private String docNumber;
	private String partnerDuns; //to faciliate searching
	private String partnerID; //""
	private String partnerName; //""
	private String docDateGenerated; //it is captured in the user document.
	private String dateTimeSendStart;
	private String dateTimeSendEnd;
	private String dateTimeReceiveStart;
	private String dateTimeReceiveEnd;
	private String dateTimeCreate;
	private String dateTimeImport; 
	private String dateTimeExport; 
	private String processDef;
	private String processInstanceID;
	private String filename; //It can be the actual audit or udoc filename depend on the archive type. **dun confused with fieldname udocFilename
	private String folder; //the direction
	private String originatorID; //the id of the initiator within a process instance
	private String gdocID;     //griddocument's gdocID
	//private String archiveType; //either archive by processInstance or document
	
	private boolean isArchivedByPI; //01092006 to indicate whether the doc is archived by doc or process instance
	
	private String docType; //_udocDocType: this field help us to identify the msg category
	                            //that the grid doc belongs.
	                            //Action msg: eg 3C3, RN_FailureNotification  Signal Msg: RN_ACK or RN_Exception
	
	private String udocFilename; //all the udoc will bring over. Used when archive by Process
	
	private String rnifVersion;
	
	private String senderCertUID; 
	private String receiverCertUID; 
	
	private String gdocFilename; //01092006 estore the gdoc physical file
	private String attachmentFilename; //01092006 indicate the gdoc has attachment.
	private boolean isOriginalDoc; 
	private boolean isContainAttachment;
	private String receiptAuditFilename; //Use for AS2;
	private String docTransStatus;
	private String userTrackingID;
	
	public DocInfo() {}
	
	public DocInfo(String docType, String docNumber, String partnerDuns, String partnerID,
								String partnerName, String docDateGenerated,
								String dateTimeSendStart, String dateTimeSendEnd, String dateTimeReceiveStart,
								String dateTimeReceiveEnd, String dateTimeCreate, String dateTimeImport,
								String dateTimeExport, String processDef, String processInstanceID,
								String filename, String folder,String originatorID,
								String gdocID, boolean isArchivedByPI, String udocFilename, String rnifVersion,
								String senderCert, String receiverCert, String gdocFilename, String attachmentFilename,
								boolean isOriginalDoc, boolean hasAttachment, String receiptAuditFilename, String docTransStatus,
								String userTrackingID)
	{
		this.docType = docType;
		this.docNumber = docNumber;
		this.partnerDuns = partnerDuns;
		this.partnerID = partnerID;
		this.partnerName = partnerName;
		this.docDateGenerated = docDateGenerated;
		this.dateTimeSendStart = dateTimeSendStart;
		this.dateTimeSendEnd = dateTimeSendEnd;
		this.dateTimeReceiveStart = dateTimeReceiveStart;
		this.dateTimeReceiveEnd = dateTimeReceiveEnd;
		this.dateTimeCreate = dateTimeCreate;
		this.dateTimeImport = dateTimeImport;
		this.dateTimeExport = dateTimeExport;
		this.processDef = processDef;
		this.processInstanceID = processInstanceID;
		this.filename = filename;
		this.folder = folder;
		this.originatorID = originatorID;
		this.gdocID = gdocID;
		this.isArchivedByPI = isArchivedByPI;
		this.udocFilename = udocFilename;
		this.rnifVersion = rnifVersion;
		this.senderCertUID = senderCert;
		this.receiverCertUID = receiverCert;
		this.gdocFilename = gdocFilename;
		this.attachmentFilename = attachmentFilename;
		this.isOriginalDoc = isOriginalDoc;
		this.isContainAttachment = hasAttachment;
		this.receiptAuditFilename = receiptAuditFilename;
		this.docTransStatus = docTransStatus;
		this.userTrackingID = userTrackingID;
	}
	
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String auditFilename)
	{
		this.filename = auditFilename;
	}
	public String getDateTimeCreate()
	{
		return dateTimeCreate;
	}
	public void setDateTimeCreate(String dateTimeCreate)
	{
		this.dateTimeCreate = dateTimeCreate;
	}
	public String getDateTimeExport()
	{
		return dateTimeExport;
	}
	public void setDateTimeExport(String dateTimeExport)
	{
		this.dateTimeExport = dateTimeExport;
	}
	public String getDateTimeImport()
	{
		return dateTimeImport;
	}
	public void setDateTimeImport(String dateTimeImport)
	{
		this.dateTimeImport = dateTimeImport;
	}
	public String getDateTimeReceiveEnd()
	{
		return dateTimeReceiveEnd;
	}
	public void setDateTimeReceiveEnd(String dateTimeReceiveEnd)
	{
		this.dateTimeReceiveEnd = dateTimeReceiveEnd;
	}
	public String getDateTimeReceiveStart()
	{
		return dateTimeReceiveStart;
	}
	public void setDateTimeReceiveStart(String dateTimeReceiveStart)
	{
		this.dateTimeReceiveStart = dateTimeReceiveStart;
	}
	public String getDateTimeSendEnd()
	{
		return dateTimeSendEnd;
	}
	public void setDateTimeSendEnd(String dateTimeSendEnd)
	{
		this.dateTimeSendEnd = dateTimeSendEnd;
	}
	public String getDateTimeSendStart()
	{
		return dateTimeSendStart;
	}
	public void setDateTimeSendStart(String dateTimeSendStart)
	{
		this.dateTimeSendStart = dateTimeSendStart;
	}
	public String getDocDateGenerated()
	{
		return docDateGenerated;
	}
	public void setDocDateGenerated(String docDateGenerated)
	{
		this.docDateGenerated = docDateGenerated;
	}
	public String getDocNumber()
	{
		return docNumber;
	}
	public void setDocNumber(String docNumber)
	{
		this.docNumber = docNumber;
	}
	public String getDocType()
	{
		return docType;
	}
	public void setDocType(String docType)
	{
		this.docType = docType;
	}
	public String getFolder()
	{
		return folder;
	}
	public void setFolder(String folder)
	{
		this.folder = folder;
	}
	public String getPartnerDuns()
	{
		return partnerDuns;
	}
	public void setPartnerDuns(String partnerDuns)
	{
		this.partnerDuns = partnerDuns;
	}
	public String getPartnerID()
	{
		return partnerID;
	}
	public void setPartnerID(String partnerID)
	{
		this.partnerID = partnerID;
	}
	public String getPartnerName()
	{
		return partnerName;
	}
	public void setPartnerName(String partnerName)
	{
		this.partnerName = partnerName;
	}
	public String getProcessDef()
	{
		return processDef;
	}
	public void setProcessDef(String processDef)
	{
		this.processDef = processDef;
	}
	public String getProcessInstanceID()
	{
		return processInstanceID;
	}
	public void setProcessInstanceID(String processInstanceID)
	{
		this.processInstanceID = processInstanceID;
	}
	
	public String getOriginatorID()
	{
		return originatorID;
	}

	public void setOriginatorID(String originatorID)
	{
		this.originatorID = originatorID;
	}

	public String getGdocID()
	{
		return gdocID;
	}

	public void setGdocID(String gdocID)
	{
		this.gdocID = gdocID;
	}

	public boolean isArchivedByPI()
	{
		return isArchivedByPI;
	}

	public void setArchivedByPI(boolean isArchivedByPI)
	{
		this.isArchivedByPI = isArchivedByPI;
	}
	
	public String getUdocFilename()
	{
		return this.udocFilename;
	}
	
	public void setUdocFilename(String udocFilename)
	{
		this.udocFilename = udocFilename;
	}

	public String getRnifVersion()
	{
		return rnifVersion;
	}

	public void setRnifVersion(String rnifVersion)
	{
		this.rnifVersion = rnifVersion;
	}

	/**
	 * @return Returns the ownCertUID.
	 */
	public String getSenderCertUID()
	{
		return senderCertUID;
	}

	/**
	 * @param ownCertUID The ownCertUID to set.
	 */
	public void setSenderCertUID(String ownCertUID)
	{
		this.senderCertUID = ownCertUID;
	}

	/**
	 * @return Returns the tpCertUID.
	 */
	public String getReceiverCertUID()
	{
		return receiverCertUID;
	}

	/**
	 * @param tpCertUID The tpCertUID to set.
	 */
	public void setReceiverCertUID(String tpCertUID)
	{
		this.receiverCertUID = tpCertUID;
	}

	public String getGdocFilename()
	{
		return gdocFilename;
	}

	public void setGdocFilename(String gdocFilename)
	{
		this.gdocFilename = gdocFilename;
	}

	public String getAttachmentFilename()
	{
		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename)
	{
		this.attachmentFilename = attachmentFilename;
	}

	public boolean isOriginalDoc()
	{
		return isOriginalDoc;
	}

	public void setOriginalDoc(boolean isOriginalDoc)
	{
		this.isOriginalDoc = isOriginalDoc;
	}

	public boolean isContainAttachment()
	{
		return isContainAttachment;
	}

	public void setContainAttachment(boolean hasAttachment)
	{
		this.isContainAttachment = hasAttachment;
	}

	public String getDocTransStatus()
	{
		return docTransStatus;
	}

	public void setDocTransStatus(String docTransStatus)
	{
		this.docTransStatus = docTransStatus;
	}

	public String getReceiptAuditFilename()
	{
		return receiptAuditFilename;
	}

	public void setReceiptAuditFilename(String receiptAuditFilename)
	{
		this.receiptAuditFilename = receiptAuditFilename;
	}

	public String getUserTrackingID()
	{
		return userTrackingID;
	}

	public void setUserTrackingID(String userTrackingID)
	{
		this.userTrackingID = userTrackingID;
	}
	
	
}
