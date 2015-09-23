/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsDocSearchPageAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 27, 2006    Tam Wei Xiang       Created
 * Dec 14  2006    Tam Wei Xiang       Added in Remark
 */
package com.gridnode.gtas.client.web.archive.searchpage;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class EsDocSearchPageAForm extends GTActionFormBase
{
	private String _partnerID;
	private String _partnerName;
	private String _folder;
	private String _docType;
	private String _fromCreateDate;
	private String _fromCreateDateHour;
	private String _toCreateDate;
	private String _toCreateDateHour;
	private String _fromSentDate;
	private String _fromSentDateHour;
	private String _toSentDate;
	private String _toSentDateHour;
	private String _fromReceivedDate;
	private String _fromReceivedDateHour;
	private String _toReceivedDate;
	private String _toReceivedDateHour;
	private String _docNo;
	private String _fromDocDate;
	private String _fromDocDateHour;
	private String _toDocDate;
	private String _toDocDateHour;
	private String _userTrackingID;
  private String _remark;
	private String _formMsg;
	
	//purely used in rendering the listview
	private String _lvFromCreateDate;
	private String _lvToCreateDate;
	private String _lvFromSentDate;
	private String _lvToSentDate;
	private String _lvFromReceivedDate;
	private String _lvToReceivedDate;
	private String _lvFromDocDate;
	private String _lvToDocDate;
	
	public EsDocSearchPageAForm() {}

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

	public String getFolder()
	{
		return _folder;
	}

	public void setFolder(String _folder)
	{
		this._folder = _folder;
	}

	public String getFromCreateDate()
	{
		return _fromCreateDate;
	}

	public void setFromCreateDate(String createDate)
	{
		_fromCreateDate = createDate;
	}

	public String getFromCreateDateHour()
	{
		return _fromCreateDateHour;
	}

	public void setFromCreateDateHour(String createDateHour)
	{
		_fromCreateDateHour = createDateHour;
	}

	public String getFromDocDate()
	{
		return _fromDocDate;
	}

	public void setFromDocDate(String docDate)
	{
		_fromDocDate = docDate;
	}

	public String getFromDocDateHour()
	{
		return _fromDocDateHour;
	}

	public void setFromDocDateHour(String docDateHour)
	{
		_fromDocDateHour = docDateHour;
	}

	public String getFromReceivedDate()
	{
		return _fromReceivedDate;
	}

	public void setFromReceivedDate(String receivedDate)
	{
		_fromReceivedDate = receivedDate;
	}

	public String getFromReceivedDateHour()
	{
		return _fromReceivedDateHour;
	}

	public void setFromReceivedDateHour(String receivedDateHour)
	{
		_fromReceivedDateHour = receivedDateHour;
	}

	public String getFromSentDate()
	{
		return _fromSentDate;
	}

	public void setFromSentDate(String sentDate)
	{
		_fromSentDate = sentDate;
	}

	public String getFromSentDateHour()
	{
		return _fromSentDateHour;
	}

	public void setFromSentDateHour(String sentDateHour)
	{
		_fromSentDateHour = sentDateHour;
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

	public String getToCreateDate()
	{
		return _toCreateDate;
	}

	public void setToCreateDate(String createDate)
	{
		_toCreateDate = createDate;
	}

	public String getToCreateDateHour()
	{
		return _toCreateDateHour;
	}

	public void setToCreateDateHour(String createDateHour)
	{
		_toCreateDateHour = createDateHour;
	}

	public String getToDocDate()
	{
		return _toDocDate;
	}

	public void setToDocDate(String docDate)
	{
		_toDocDate = docDate;
	}

	public String getToDocDateHour()
	{
		return _toDocDateHour;
	}

	public void setToDocDateHour(String docDateHour)
	{
		_toDocDateHour = docDateHour;
	}

	public String getToReceivedDate()
	{
		return _toReceivedDate;
	}

	public void setToReceivedDate(String receivedDate)
	{
		_toReceivedDate = receivedDate;
	}

	public String getToReceivedDateHour()
	{
		return _toReceivedDateHour;
	}

	public void setToReceivedDateHour(String receivedDateHour)
	{
		_toReceivedDateHour = receivedDateHour;
	}

	public String getToSentDate()
	{
		return _toSentDate;
	}

	public void setToSentDate(String sentDate)
	{
		_toSentDate = sentDate;
	}

	public String getToSentDateHour()
	{
		return _toSentDateHour;
	}

	public void setToSentDateHour(String sentDateHour)
	{
		_toSentDateHour = sentDateHour;
	}

	public String getUserTrackingID()
	{
		return _userTrackingID;
	}

	public void setUserTrackingID(String trackingID)
	{
		_userTrackingID = trackingID;
	}

	public String getRemark()
  {
    return _remark;
  }

  public void setRemark(String _remark)
  {
    this._remark = _remark;
  }

  public String getFormMsg()
	{
		return _formMsg;
	}

	public void setFormMsg(String msg)
	{
		_formMsg = msg;
	}

	public String getLvFromCreateDate()
	{
		return _lvFromCreateDate;
	}

	public void setLvFromCreateDate(String fromCreateDate)
	{
		_lvFromCreateDate = fromCreateDate;
	}

	public String getLvFromDocDate()
	{
		return _lvFromDocDate;
	}

	public void setLvFromDocDate(String fromDocDate)
	{
		_lvFromDocDate = fromDocDate;
	}

	public String getLvFromReceivedDate()
	{
		return _lvFromReceivedDate;
	}

	public void setLvFromReceivedDate(String fromReceivedDate)
	{
		_lvFromReceivedDate = fromReceivedDate;
	}

	public String getLvFromSentDate()
	{
		return _lvFromSentDate;
	}

	public void setLvFromSentDate(String fromSentDate)
	{
		_lvFromSentDate = fromSentDate;
	}

	public String getLvToCreateDate()
	{
		return _lvToCreateDate;
	}

	public void setLvToCreateDate(String toCreateDate)
	{
		_lvToCreateDate = toCreateDate;
	}

	public String getLvToDocDate()
	{
		return _lvToDocDate;
	}

	public void setLvToDocDate(String toDocDate)
	{
		_lvToDocDate = toDocDate;
	}

	public String getLvToReceivedDate()
	{
		return _lvToReceivedDate;
	}

	public void setLvToReceivedDate(String toReceivedDate)
	{
		_lvToReceivedDate = toReceivedDate;
	}

	public String getLvToSentDate()
	{
		return _lvToSentDate;
	}

	public void setLvToSentDate(String toSentDate)
	{
		_lvToSentDate = toSentDate;
	}
	
	
}
