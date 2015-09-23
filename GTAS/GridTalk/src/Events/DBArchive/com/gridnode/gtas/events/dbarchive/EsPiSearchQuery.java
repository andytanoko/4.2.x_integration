package com.gridnode.gtas.events.dbarchive;

public class EsPiSearchQuery
{
	private String docNo;
	private String process;
	private String processState;
	private String partnerId;
	private String partnerName;
	private String docDateFrom;
	private String docDateTo;
	private String processStartFromDate;
	private String processStartFromTime;
	private String processStartToDate;
	private String processStartToTime;
	private String userTrackingID;
  private String remark; //04122006 RZ: Added field
  
	public EsPiSearchQuery(String docNo, String process, String processState, String partnerId, String partnerName, String docDateFrom, 
	                       String docDateTo,String processStartFromDate, String processStartFromTime, 
	                       String processStartToDate, String processStartToTime, String userTrackingID, String remark)
	{
		setDocNo(docNo);
		setProcess(process);
		setProcessState(processState);
		setPartnerId(partnerId);
		setPartnerName(partnerName);
		setDocDateFrom(docDateFrom);
		setDocDateTo(docDateTo);
		setProcessStartFromDate(processStartFromDate);
		setProcessStartFromTime(processStartFromTime);
		setProcessStartToDate(processStartToDate);
		setProcessStartToTime(processStartToTime);
		setUserTrackingID(userTrackingID);
    setRemark(remark);
	}
	
	public String getDocDateFrom()
	{
		return docDateFrom;
	}
	public void setDocDateFrom(String docDateFrom)
	{
		this.docDateFrom = docDateFrom;
	}
	public String getDocDateTo()
	{
		return docDateTo;
	}
	public void setDocDateTo(String docDateTo)
	{
		this.docDateTo = docDateTo;
	}
	public String getDocNo()
	{
		return docNo;
	}
	public void setDocNo(String docNo)
	{
		this.docNo = docNo;
	}
	public String getPartnerId()
	{
		return partnerId;
	}
	public void setPartnerId(String partnerId)
	{
		this.partnerId = partnerId;
	}
	public String getPartnerName()
	{
		return partnerName;
	}
	public void setPartnerName(String partnerName)
	{
		this.partnerName = partnerName;
	}
	public String getProcess()
	{
		return process;
	}
	public void setProcess(String process)
	{
		this.process = process;
	}
	public String getProcessState()
	{
		return processState;
	}
	public void setProcessState(String processState)
	{
		this.processState = processState;
	}
	
	public String getProcessStartFromDate()
	{
		return processStartFromDate;
	}



	public void setProcessStartFromDate(String processStartFromDate)
	{
		this.processStartFromDate = processStartFromDate;
	}



	public String getProcessStartFromTime()
	{
		return processStartFromTime;
	}



	public void setProcessStartFromTime(String processStartFromTime)
	{
		this.processStartFromTime = processStartFromTime;
	}



	public String getProcessStartToDate()
	{
		return processStartToDate;
	}



	public void setProcessStartToDate(String processStartToDate)
	{
		this.processStartToDate = processStartToDate;
	}



	public String getProcessStartToTime()
	{
		return processStartToTime;
	}



	public void setProcessStartToTime(String processStartToTime)
	{
		this.processStartToTime = processStartToTime;
	}
	
	public String getUserTrackingID()
	{
		return userTrackingID;
	}

	public void setUserTrackingID(String userTrackingID)
	{
		this.userTrackingID = userTrackingID;
	}

	public String toString()
	{
		return "docNo = " + docNo
			+ "\nprocess = " + process
			+ "\nprocessState = " + processState
			+ "\npartnerId = " + partnerId
			+ "\npartnerName = " + partnerName
			+ "\ndocDateFrom = " + docDateFrom
      + "\ndocDateTo = " + docDateTo
      +"\nProcess StartDate = "+ processStartFromDate
      +"\nProcess EndDate = "+ processStartToDate
      + "\ndocProcess FromTime = "+processStartFromTime
      + "\ndocProcess ToTime = "+processStartToTime
      + "\nuserTrackingID = "+userTrackingID
      + "\nremark = "+remark;
	}

  public String getRemark()
  {
    return remark;
  }

  public void setRemark(String remark)
  {
    this.remark = remark;
  }
}
