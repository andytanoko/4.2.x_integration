package com.gridnode.gtas.events.dbarchive;

public class EsDocSearchQuery
{
	private String docNo;
	private String docType;
	private String partnerId;
	private String partnerName;
	private String docDateFrom;
	private String docDateTo;
	private String docDateSentFrom;
	private String docDateSentTo;
	private String docDateReceivedFrom;
	private String docDateReceivedTo;
	
	//TWX 27 Mar 2006 new search criterias
	private String fromCreateDate;
	private String fromCreateDateTime;
	private String toCreateDate;
	private String toCreateDateTime;
	
	private String fromSentDateTime;
	private String toSentDateTime;
	
	private String fromReceivedDateTime;
	private String toReceivedDateTime;
	
	private String fromDocDateTime;
	private String toDocDateTime;
	
	private String folder;
	private String userTrackingID;
	
  private String remark;
  
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("docNo = ").append(docNo).append(" docType = ").append(docType).append(" partnerID = ").append(partnerId);
		str.append("partnerName = ").append(partnerName).append(" docDateFrom = ").append(docDateFrom).append(" docDateFromhr "+fromDocDateTime).append(" docDateTo = ").append(docDateTo).append("docDAteToTime "+toDocDateTime);
		str.append("docDateSentFrom = "+docDateSentFrom).append(" docDateSentFromTime "+fromSentDateTime).append(" docDateSentTo "+docDateSentTo).append(" docDateSentToTime "+toSentDateTime);
		str.append("fromReceived = "+docDateReceivedFrom).append(" fromReceivedTime "+fromReceivedDateTime).append(" toReceived "+docDateReceivedTo).append(" toReceiveTime "+toReceivedDateTime);
		str.append("fromCreateDate = "+fromCreateDate).append(" fromCreateDateTime = "+fromCreateDateTime).append(" toCreateDate "+toCreateDate).append(" toCreateDateTime "+toCreateDateTime);
		str.append("folder "+folder).append(" userTrackingID "+userTrackingID).append(" remark "+remark);
		return str.toString();
	}
	
	public EsDocSearchQuery(String docNo,
	                        String docType,
	                        String partnerId,
	                        String partnerName,
	                        String docDateFrom,
	                        String docDateTo,
	                        String docDateSentFrom,
	                        String docDateSentTo,
	                        String docDateReceivedFrom,
	                        String docDateReceivedTo,
	                        
	                        String fromCreateDate,
	                        String fromCreateDateTime,
	                        String toCreateDate,
	                        String toCreateDateTime,
	                        String fromSentDateTime,
	                        String toSentDateTime,
	                        String fromReceivedDateTime,
	                        String toReceivedDateTime,
	                        String fromDocDateTime,
	                        String toDocDateTime,
	                        String folder,
	                        String userTrackingID,
                          String remark)
	{
		setDocNo(docNo);
		setDocType(docType);
		setPartnerId(partnerId);
		setPartnerName(partnerName);
		setDocDateFrom(docDateFrom);
		setDocDateTo(docDateTo);
		setDocDateSentFrom(docDateSentFrom);
	  setDocDateSentTo(docDateSentTo);
	  setDocDateReceivedFrom(docDateReceivedFrom);
	  setDocDateReceivedTo(docDateReceivedTo);
	  
	  setFromCreateDate(fromCreateDate);
	  setFromCreateDateTime(fromCreateDateTime);
	  
	  setToCreateDate(toCreateDate);
	  setToCreateDateTime(toCreateDateTime);
	  
	  setFromSentDateTime(fromSentDateTime);
	  setToSentDateTime(toSentDateTime);
	  
	  setFromReceivedDateTime(fromReceivedDateTime);
	  setToReceivedDateTime(toReceivedDateTime);
	  
	  setFromDocDateTime(fromDocDateTime);
	  setToDocDateTime(toDocDateTime);
	  setFolder(folder);
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
	public String getDocDateReceivedFrom()
	{
		return docDateReceivedFrom;
	}
	public void setDocDateReceivedFrom(String docDateReceivedFrom)
	{
		this.docDateReceivedFrom = docDateReceivedFrom;
	}
	public String getDocDateReceivedTo()
	{
		return docDateReceivedTo;
	}
	public void setDocDateReceivedTo(String docDateReceivedTo)
	{
		this.docDateReceivedTo = docDateReceivedTo;
	}
	public String getDocDateSentFrom()
	{
		return docDateSentFrom;
	}
	public void setDocDateSentFrom(String docDateSentFrom)
	{
		this.docDateSentFrom = docDateSentFrom;
	}
	public String getDocDateSentTo()
	{
		return docDateSentTo;
	}
	public void setDocDateSentTo(String docDateSentTo)
	{
		this.docDateSentTo = docDateSentTo;
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
	public String getDocType()
	{
		return docType;
	}
	public void setDocType(String docType)
	{
		this.docType = docType;
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

	public String getFromCreateDateTime()
	{
		return fromCreateDateTime;
	}

	public void setFromCreateDateTime(String fromCreatDateTime)
	{
		this.fromCreateDateTime = fromCreatDateTime;
	}

	public String getFromCreateDate()
	{
		return fromCreateDate;
	}

	public void setFromCreateDate(String fromCreateDate)
	{
		this.fromCreateDate = fromCreateDate;
	}

	public String getFromDocDateTime()
	{
		return fromDocDateTime;
	}

	public void setFromDocDateTime(String fromDocDateTime)
	{
		this.fromDocDateTime = fromDocDateTime;
	}

	public String getFromReceivedDateTime()
	{
		return fromReceivedDateTime;
	}

	public void setFromReceivedDateTime(String fromReceivedDateTime)
	{
		this.fromReceivedDateTime = fromReceivedDateTime;
	}

	public String getFromSentDateTime()
	{
		return fromSentDateTime;
	}

	public void setFromSentDateTime(String fromSentDateTime)
	{
		this.fromSentDateTime = fromSentDateTime;
	}

	public String getToCreateDate()
	{
		return toCreateDate;
	}

	public void setToCreateDate(String toCreateDate)
	{
		this.toCreateDate = toCreateDate;
	}

	public String getToCreateDateTime()
	{
		return toCreateDateTime;
	}

	public void setToCreateDateTime(String toCreateDateTime)
	{
		this.toCreateDateTime = toCreateDateTime;
	}

	public String getToDocDateTime()
	{
		return toDocDateTime;
	}

	public void setToDocDateTime(String toDocDateTime)
	{
		this.toDocDateTime = toDocDateTime;
	}

	public String getToReceivedDateTime()
	{
		return toReceivedDateTime;
	}

	public void setToReceivedDateTime(String toReceivedDateTime)
	{
		this.toReceivedDateTime = toReceivedDateTime;
	}

	public String getToSentDateTime()
	{
		return toSentDateTime;
	}

	public void setToSentDateTime(String toSentDateTime)
	{
		this.toSentDateTime = toSentDateTime;
	}

	public String getFolder()
	{
		return folder;
	}

	public void setFolder(String folder)
	{
		this.folder = folder;
	}

	public String getUserTrackingID()
	{
		return userTrackingID;
	}

	public void setUserTrackingID(String userTrackingID)
	{
		this.userTrackingID = userTrackingID;
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
