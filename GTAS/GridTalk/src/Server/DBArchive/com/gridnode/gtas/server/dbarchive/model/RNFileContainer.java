/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNFileContainer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 3, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.model;

import java.util.Hashtable;
import java.io.Serializable;
import java.util.Date;

/**
 * This class serve as a rnfile content container.
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public class RNFileContainer implements Serializable
{
	private final Integer _Preamble_Header = new Integer(0);
	private final Integer _Deliver_Header = new Integer(1);
	private final Integer _Service_Header = new Integer(2);
	private final Integer _Service_Content = new Integer(3);
	private final Integer _Filename = new Integer(4);
	private final Integer _DocNo = new Integer(5);
	private final Integer _Doc_Type = new Integer(6);
	private final Integer _Partner_ID = new Integer(7);
	private final Integer _Parner_Duns = new Integer(8);
	private final Integer _Partner_Name = new Integer(9);
	private final Integer _DATE_CREATED = new Integer(10);
	private Hashtable h;
	
	public RNFileContainer()
	{
		h = new Hashtable();
	}
	
	public String getPreamble()
	{
		return (String)h.get(_Preamble_Header);
	}
	
	public void setPreamble(String preamble)
	{
		h.put(_Preamble_Header, preamble);
	}
	
	public String getDeliveryHeader()
	{
		return (String)h.get(_Deliver_Header);
	}
	
	public void setDeliveryHeader(String deliver)
	{
		h.put(_Deliver_Header, deliver);
	}
	
	public String getServiceHeader()
	{
		return (String)h.get(_Service_Header);
	}
	
	public void setServiceHeader(String serviceHeader)
	{
		h.put(_Service_Header, serviceHeader);
	}
	
	public String getServiceContent()
	{
		return (String)h.get(_Service_Content);
	}
	
	public void setServiceContent(String content)
	{
		h.put(_Service_Content, content);
	}
	
	//Start File info
	public void setFilename(String filename)
	{
		h.put(_Filename, filename);
	}
	public String getFilename()
	{
		return (String)h.get(_Filename);
	}
	
	public void setDocNo(String docNo)
	{
		h.put(_DocNo, docNo);
	}
	public String getDocNo()
	{
		return (String)h.get(_DocNo);
	}
	
	public void setDocType(String docType)
	{
		h.put(_Doc_Type,docType);
	}
	public String getDocType()
	{
		return (String)h.get(_Doc_Type);
	}
	
	public void setPartnerID(String partnerID)
	{
		h.put(_Partner_ID,partnerID);
	}
	public String getPartnerID()
	{
		return (String)h.get(_Partner_ID);
	}
	
	public void setPartnerDuns(String partnerDuns)
	{
		h.put(_Parner_Duns,partnerDuns);
	}
	public String getPartnerDuns()
	{
		return (String)h.get(_Parner_Duns);
	}
	
	public void setPartnerName(String name)
	{
		h.put(_Partner_Name,name);
	}
	public String getPartnerName()
	{
		return (String)h.get(_Partner_Name);
	}
	public void setDate(Date d)
	{
		h.put(_DATE_CREATED, d);
	}
	public Date getDate()
	{
		return (Date)h.get(_DATE_CREATED);
	}
}
