/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 7, 2005        Tam Wei Xiang       Created
 * Dec 18 2006        Tam Wei Xiang       Added in failedReason, detailReason
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

public class ProcessInstanceInfo implements Serializable
{
	private String processInstanceID;
	private String processState;
	private String processStartDate;
	private String processEndDate;
	//private String partnerDuns;
	private String processDef; //the gtas define doc type
	private String roleType; //to derive myRole n partner role
	private String originatorID; //value is either 'SELF' or user define partnerID
	private String remark; //indicate the reason if the validation in estore failed (eg no docs associated to this PI).
	private Integer failedReason;
  private String detailReason;
  private Integer retryNumber;
  
	/**
	 * Empty constructor is required since the underlying layer dun
	 * know the signature of the constructor.
	 *
	 */
	public ProcessInstanceInfo() {}
	
	public ProcessInstanceInfo(String processInstanceID, String processState,
														String processStartDate, String processEndDate,
														String processDef,
														String roleType, String originatorID, Integer failedReason,
                            String detailReason, Integer retryNumber)
	{
		this.processInstanceID = processInstanceID;
		this.processState = processState;
		this.processStartDate = processStartDate;
		this.processEndDate = processEndDate;
		//this.partnerDuns = partnerDuns;
		this.processDef = processDef;
		this.roleType = roleType;
		this.originatorID = originatorID;
    this.failedReason = failedReason;
    this.detailReason = detailReason;
    this.retryNumber = retryNumber;
	}
	
	/*
	public String getPartnerDuns()
	{
		return partnerDuns;
	}

	public void setPartnerDuns(String partnerDuns)
	{
		this.partnerDuns = partnerDuns;
	} */

	public String getProcessDef()
	{
		return processDef;
	}

	public void setProcessDef(String processDef)
	{
		this.processDef = processDef;
	}

	public String getProcessEndDate()
	{
		return processEndDate;
	}

	public void setProcessEndDate(String processEndDate)
	{
		this.processEndDate = processEndDate;
	}

	public String getProcessInstanceID()
	{
		return processInstanceID;
	}

	public void setProcessInstanceID(String processInstanceID)
	{
		this.processInstanceID = processInstanceID;
	}

	public String getProcessStartDate()
	{
		return processStartDate;
	}

	public void setProcessStartDate(String processStartDate)
	{
		this.processStartDate = processStartDate;
	}

	public String getProcessState()
	{
		return processState;
	}

	public void setProcessState(String processState)
	{
		this.processState = processState;
	}

	public String getRoleType()
	{
		return roleType;
	}

	public void setRoleType(String roleType)
	{
		this.roleType = roleType;
	}

	public String getOriginatorID()
	{
		return originatorID;
	}

	public void setOriginatorID(String originatorID)
	{
		this.originatorID = originatorID;
	}
	
	public String getRemark()
	{
		return remark;
	}
	
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

  public String getDetailReason()
  {
    return detailReason;
  }

  public void setDetailReason(String detailReason)
  {
    this.detailReason = detailReason;
  }

  public Integer getFailedReason()
  {
    return failedReason;
  }

  public void setFailedReason(Integer failedReason)
  {
    this.failedReason = failedReason;
  }

  public Integer getRetryNumber()
  {
    return retryNumber;
  }

  public void setRetryNumber(Integer retryNumber)
  {
    this.retryNumber = retryNumber;
  }
  
  
}
