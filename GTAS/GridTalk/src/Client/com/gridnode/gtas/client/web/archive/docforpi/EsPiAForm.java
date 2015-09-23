package com.gridnode.gtas.client.web.archive.docforpi;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class EsPiAForm extends GTActionFormBase
{
	private String docDateGenerated;

	private String docNumber;

	private String originatorID;

	private String partnerDuns;

	private String partnerID;

	private String partnerName;

	private String processDef;

	private String processEndDate;

	private String processInstanceID;

	private String processStartDate;

	private String processState;

	private String roleType;
	
	private String processStartDateTime;
	
	private String processEndDateTime;
  
  //Regina Zeng 16 Oct 2006--Add user tracking ID and remark and process instance remark
  private String userTrackingID;
  
  private String remark;
  
  private String processInstanceRemark;
	
  private String failedReason;
  
  private String detailReason;
  
  private String retryNumber;
  
	public String getProcessEndDateTime()
	{
		return processEndDateTime;
	}

	public void setProcessEndDateTime(String processEndDateTime)
	{
		this.processEndDateTime = processEndDateTime;
	}

	public String getProcessStartDateTime()
	{
		return processStartDateTime;
	}

	public void setProcessStartDateTime(String processStartDateTime)
	{
		this.processStartDateTime = processStartDateTime;
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

	public String getOriginatorID()
	{
		return originatorID;
	}

	public void setOriginatorID(String originatorID)
	{
		this.originatorID = originatorID;
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

  public String getRemark()
  {
    return remark;
  }

  public void setRemark(String remark)
  {
    this.remark = remark;
  }

  public String getUserTrackingID()
  {
    return userTrackingID;
  }

  public void setUserTrackingID(String userTrackingID)
  {
    this.userTrackingID = userTrackingID;
  }

  public String getProcessInstanceRemark()
  {
    return processInstanceRemark;
  }

  public void setProcessInstanceRemark(String processInstanceRemark)
  {
    this.processInstanceRemark = processInstanceRemark;
  }

  public String getDetailReason()
  {
    return detailReason;
  }

  public void setDetailReason(String detailReason)
  {
    this.detailReason = detailReason;
  }

  public String getFailedReason()
  {
    return failedReason;
  }

  public void setFailedReason(String failedReason)
  {
    this.failedReason = failedReason;
  }

  public String getRetryNumber()
  {
    return retryNumber;
  }

  public void setRetryNumber(String retryNumber)
  {
    this.retryNumber = retryNumber;
  }
  
  public boolean isProcessInstanceFailed()
  {
    return failedReason != null && ! "".equals(failedReason) && Integer.parseInt(failedReason) > 0;
  }
  
}
