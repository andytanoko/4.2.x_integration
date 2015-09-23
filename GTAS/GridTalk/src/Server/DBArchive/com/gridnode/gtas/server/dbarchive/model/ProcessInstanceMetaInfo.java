/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 * Dec 18, 2006        Tam Wei Xiang       Added failedReason n DetailReason
 */
package com.gridnode.gtas.server.dbarchive.model;

import java.util.Date;
import java.util.Collection;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
/**
 * This is a value object for ProcessInstanceMetaInfoBean
 *
 * Tam Wei Xiang
 * 
 * @version 1.0
 * @since 1.0
 */
public class ProcessInstanceMetaInfo
						 extends AbstractEntity
						 implements IProcessInstanceMetaInfo
{
	private String _processInstanceID;
	private String _processState;
	private Date _processStartDate;
	private Date _processEndDate;
	private String _partnerDuns;
	private String _processDef; //the gtas define doc type
	private String _roleType; //to derive myRole n partner role
	private String _partnerID;
	private String _partnerName;
	private String _docNumber;
	private String _docDateGenerated;
	private String _originatorID;
	private Collection _assocDocs; //needed by UI
	private String _remark; //indicate the reason if the validation in estore failed (eg no docs associated to this PI).
	private String _userTrackingID;
	private Integer _failedReason;
  private String _detailReason;
  private Integer _retryNumber;
  
	public ProcessInstanceMetaInfo() {}
	
	public ProcessInstanceMetaInfo(String processInstanceID, String processState,
														Date processStartDate, Date processEndDate,
														String partnerDuns, String processDef,
														String roleType, String partnerID, String partnerName,
														String docNo, String docDateGenerated, String originatorID,
														Collection assocDocs, String remark,
														String userTrackingID, Integer failedReason,
                            String detailReason, Integer retryNumber)
	{
		this._processInstanceID = processInstanceID;
		this._processState = processState;
		this._processStartDate = processStartDate;
		this._processEndDate = processEndDate;
		this._partnerDuns = partnerDuns;
		this._processDef = processDef;
		this._roleType = roleType;
		this._partnerID=  partnerID;
		this._partnerName = partnerName;
		this._docNumber = docNo;
		this._docDateGenerated = docDateGenerated;
		this._originatorID = originatorID;
		this._assocDocs = assocDocs;
		this._remark = remark;
		this._userTrackingID = userTrackingID;
    this._failedReason = failedReason;
    this._detailReason = detailReason;
    this._retryNumber = retryNumber;
	}
	
  // **************** Methods from AbstractEntity *********************
	public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return new StringBuffer("Pro_InstanceMetaInfo: ProcessInstance ID ").append(this.getProcessInstanceID()).append("/Process Def ").append(this.getProcessDef()).toString();
  }

  public Number getKeyId()
  {
    return UID;
  }
  
  //***************** Getters and Setters for attributes ***********************
	public String getPartnerDuns()
	{
		return _partnerDuns;
	}

	public void setPartnerDuns(String partnerDuns)
	{
		this._partnerDuns = partnerDuns;
	}

	public String getProcessDef()
	{
		return _processDef;
	}

	public void setProcessDef(String processDef)
	{
		this._processDef = processDef;
	}

	public Date getProcessEndDate()
	{
		return _processEndDate;
	}

	public void setProcessEndDate(Date processEndDate)
	{
		this._processEndDate = processEndDate;
	}

	public String getProcessInstanceID()
	{
		return _processInstanceID;
	}

	public void setProcessInstanceID(String processInstanceID)
	{
		this._processInstanceID = processInstanceID;
	}

	public Date getProcessStartDate()
	{
		return _processStartDate;
	}

	public void setProcessStartDate(Date processStartDate)
	{
		this._processStartDate = processStartDate;
	}

	public String getProcessState()
	{
		return _processState;
	}

	public void setProcessState(String processState)
	{
		this._processState = processState;
	}

	public String getRoleType()
	{
		return _roleType;
	}

	public void setRoleType(String roleType)
	{
		this._roleType = roleType;
	}

	public String getPartnerID()
	{
		return _partnerID;
	}

	public void setPartnerID(String partnerID)
	{
		this._partnerID = partnerID;
	}

	public String getPartnerName()
	{
		return _partnerName;
	}

	public void setPartnerName(String partnerName)
	{
		this._partnerName = partnerName;
	}

	public String getDocNumber()
	{
		return _docNumber;
	}

	public void setDocNumber(String no)
	{
		_docNumber = no;
	}

	public String getDocDateGenerated()
	{
		return _docDateGenerated;
	}

	public void setDocDateGenerated(String dateGenerated)
	{
		_docDateGenerated = dateGenerated;
	}

	public String getOriginatorID()
	{
		return _originatorID;
	}

	public void setOriginatorID(String ID)
	{
		_originatorID = ID;
	}

	public Collection getAssocDocs()
	{
		return _assocDocs;
	}

	public void setAssocDocs(Collection docs)
	{
		_assocDocs = docs;
	}

	public String getRemark()
	{
		return _remark;
	}

	public void setRemark(String _remark)
	{
		this._remark = _remark;
	}

	public String getUserTrackingID()
	{
		return _userTrackingID;
	}

	public void setUserTrackingID(String trackingID)
	{
		_userTrackingID = trackingID;
	}

  public String getDetailReason()
  {
    return _detailReason;
  }

  public void setDetailReason(String reason)
  {
    _detailReason = reason;
  }

  public Integer getFailedReason()
  {
    return _failedReason;
  }

  public void setFailedReason(Integer reason)
  {
    _failedReason = reason;
  }

  public Integer getRetryNumber()
  {
    return _retryNumber;
  }

  public void setRetryNumber(Integer number)
  {
    _retryNumber = number;
  }
	
	
}
