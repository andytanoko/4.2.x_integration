/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsPiSearchPageAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 21, 2006    Tam Wei Xiang       Created
 * Oct 12, 2006    Regina Zeng         Add user tracking ID, remark
 */
package com.gridnode.gtas.client.web.archive.searchpage;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

/**
 * @author Tam Wei Xiang
 * @author Regina Zeng
 * 
 * @since GT 4.0
 */
public class EsPiSearchPageAForm extends GTActionFormBase
{
	private String _processDef;
	private String _processState;
	private String _partnerID;
	private String _partnerName;
	private String _processFromStartTime;
	private String _fromSTHour;
	private String _processToStartTime;
	private String _toSTHour;
	private String _docNo;
	private String _fromDocDate;
	private String _toDocDate;
	private String _formMsg;
  private String _userTrackingID; 
  private String _remark; //04122006 RZ:Added
	
	public EsPiSearchPageAForm() {}

	public String getDocNo()
	{
		return _docNo;
	}

	public void setDocNo(String no)
	{
		_docNo = no;
	}

	public String getFromDocDate()
	{
		return _fromDocDate;
	}

	public void setFromDocDate(String docDate)
	{
		_fromDocDate = docDate;
	}

	public String getFromSTHour()
	{
		return _fromSTHour;
	}

	public void setFromSTHour(String hour)
	{
		_fromSTHour = hour;
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

	public String getProcessDef()
	{
		return _processDef;
	}

	public void setProcessDef(String def)
	{
		_processDef = def;
	}

	public String getProcessFromStartTime()
	{
		return _processFromStartTime;
	}

	public void setProcessFromStartTime(String fromStartTime)
	{
		_processFromStartTime = fromStartTime;
	}

	public String getProcessState()
	{
		return _processState;
	}

	public void setProcessState(String state)
	{
		_processState = state;
	}

	public String getProcessToStartTime()
	{
		return _processToStartTime;
	}

	public void setProcessToStartTime(String toStartTime)
	{
		_processToStartTime = toStartTime;
	}

	public String getToDocDate()
	{
		return _toDocDate;
	}

	public void setToDocDate(String docDate)
	{
		_toDocDate = docDate;
	}

	public String getToSTHour()
	{
		return _toSTHour;
	}

	public void setToSTHour(String hour)
	{
		_toSTHour = hour;
	}

	public String getFormMsg()
	{
		return _formMsg;
	}

	public void setFormMsg(String msg)
	{
		_formMsg = msg;
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
}
