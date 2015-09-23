/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: AuditFileAForm.java
 * 
 * ***************************************************************************
 * Date 				Author 			Changes
 * ***************************************************************************
 * Oct 12 2005 	  Sumedh C. 	   Created
 * Nov 23 2006    Regina Zeng    Added docMetaInfoUID
 */
package com.gridnode.gtas.client.web.archive.docforpi;

import com.gridnode.gtas.client.web.strutsbase.*;

public class AuditFileAForm extends GTActionFormBase
{
	private String filename;

	private String docNumber;

	private String docType;

	private String partnerID;

	private String partnerDuns;

	private String partnerName;

	private String dateTimeCreate;

	private String preamble;

	private String deliveryHeader;

	private String serviceHeader;

	private String serviceContent;
  
  private String docMetaInfoUID;

	public String getDeliveryHeader()
	{
		return deliveryHeader;
	}

	public void setDeliveryHeader(String deliveryHeader)
	{
		this.deliveryHeader = deliveryHeader;
	}

	public String getDocType()
	{
		return docType;
	}

	public void setDocType(String docType)
	{
		this.docType = docType;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
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

	public String getPreamble()
	{
		return preamble;
	}

	public void setPreamble(String preamble)
	{
		this.preamble = preamble;
	}

	public String getServiceContent()
	{
		return serviceContent;
	}

	public void setServiceContent(String serviceContent)
	{
		this.serviceContent = serviceContent;
	}

	public String getServiceHeader()
	{
		return serviceHeader;
	}

	public void setServiceHeader(String serviceHeader)
	{
		this.serviceHeader = serviceHeader;
	}

	public String getDateTimeCreate()
	{
		return dateTimeCreate;
	}

	public void setDateTimeCreate(String dateTimeCreate)
	{
		this.dateTimeCreate = dateTimeCreate;
	}

	public String getDocNumber()
	{
		return docNumber;
	}

	public void setDocNumber(String docNumber)
	{
		this.docNumber = docNumber;
	}

  public String getDocMetaInfoUID()
  {
    return docMetaInfoUID;
  }

  public void setDocMetaInfoUID(String docUID)
  {
    this.docMetaInfoUID = docUID;
  }
}
