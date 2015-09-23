/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditFileInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 12, 2005        Tam Wei Xiang       Created
 * 29 Nov 2005				 Sumedh							 change to implement IAuditFileMetaInfo
 */
package com.gridnode.gtas.server.dbarchive.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;
/**
 *
 *
 * @author Tam Wei Xiang
 * 
 * @version
 * @since GT 2.4.7
 */
public class AuditFileInfo
						 extends AbstractEntity
						 implements IAuditFileMetaInfo
{
	private String _filename;
	private String _docNo;
	private String _docType;
	private String _partnerID;
	private String _partnerDuns;
	private String _partnerName;
	private Date _dateCreated;
	private String _preamble;
	private String _deliveryHeader;
	private String _serviceHeader;
	private String _serviceContent;
	
	private String _docMetaInfoUID;
  
	public AuditFileInfo() {}
	
	public AuditFileInfo(String filename, String docNo, String docType, String partnerID,
									 String partnerDuns, String partnerName, Date dateCreated,
									 String preamble, String deliveryHeader,String serviceHeader,
									 String serviceContent, String docMetaInfoUID)
	{
		this._filename = filename;
		this._docNo = docNo;
		this._docType = docType;
		this._partnerID = partnerID;
		this._partnerDuns = partnerDuns;
		this._partnerName = partnerName;
		this._dateCreated = dateCreated;
		this._preamble = preamble;
		this._deliveryHeader = deliveryHeader;
		this._serviceHeader = serviceHeader;
		this._serviceContent = serviceContent;
		this._docMetaInfoUID = docMetaInfoUID;
	}
	
  // **************** Methods from AbstractEntity *********************
	public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return new StringBuffer("AuditFileInfo UID ").append(this.getKey()).append("/Filename ").append(this.getFilename()).toString();
  }

  public Number getKeyId()
  {
    return UID;
  }

  //	***************** Getters and Setters for attributes ***********************

	public Date getDateCreated()
	{
		return _dateCreated;
	}

	public void setDateCreated(Date created)
	{
		_dateCreated = created;
	}

	public String getDeliveryHeader()
	{
		return _deliveryHeader;
	}

	public void setDeliveryHeader(String header)
	{
		_deliveryHeader = header;
	}

	public String getDocNo()
	{
		return _docNo;
	}

	public void setDocNo(String no)
	{
		_docNo = no;
	}

	public String getDocType()
	{
		return _docType;
	}

	public void setDocType(String type)
	{
		_docType = type;
	}

	public String getFilename()
	{
		return _filename;
	}

	public void setFilename(String _filename)
	{
		this._filename = _filename;
	}

	public String getPartnerDuns()
	{
		return _partnerDuns;
	}

	public void setPartnerDuns(String duns)
	{
		_partnerDuns = duns;
	}

	public String getPartnerID()
	{
		return _partnerID;
	}

	public void setPartnerID(String _partnerid)
	{
		_partnerID = _partnerid;
	}

	public String getPartnerName()
	{
		return _partnerName;
	}

	public void setPartnerName(String name)
	{
		_partnerName = name;
	}

	public String getPreamble()
	{
		return _preamble;
	}

	public void setPreamble(String _preamble)
	{
		this._preamble = _preamble;
	}

	public String getServiceContent()
	{
		return _serviceContent;
	}

	public void setServiceContent(String content)
	{
		_serviceContent = content;
	}

	public String getServiceHeader()
	{
		return _serviceHeader;
	}

	public void setServiceHeader(String header)
	{
		_serviceHeader = header;
	}

	/**
	 * @return Returns the _docMetaInfoUID.
	 */
	public String getDocMetaInfoUID()
	{
		return _docMetaInfoUID;
	}

	/**
	 * @param metaInfoUID The _docMetaInfoUID to set.
	 */
	public void setDocMetaInfoUID(String metaInfoUID)
	{
		_docMetaInfoUID = metaInfoUID;
	}
}
