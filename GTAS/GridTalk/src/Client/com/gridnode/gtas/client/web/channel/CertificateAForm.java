/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-20     Andrew Hill         Created
 * 2003-01-22     Andrew Hill         Use nested forms for x500Name details
 * 2003-04-04     Andrew Hill         add x500Error flag
 * 2003-04-15     Andrew Hill         inKeyStore, inTrustStore
 * 2004-03-26     Daniel D'Cotta      Added RELATED_CERT_UID
 * 2006-07-26     Tam Wei Xiang       Added SerialNum, StartDate, EndDate
 * 2006-07-28     Tam Wei Xiang       Added isCA
 * 2006-08-28     Tam Wei Xiang       Added replacementUid
 * 2008-08-01	  Wong Yee Wah		  #38  Added swapDate, swapTime
 */
package com.gridnode.gtas.client.web.channel;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class CertificateAForm extends GTActionFormBase
{
  private String _id;
  private String _name = "";
  private String _isPartner = "false";
  private String _password = "";
  private FormFileElement[] _certFile = null;
  private boolean _isCertFileChanged = false;
  private boolean _isShowX500Name = false;
  private X500NameAForm _issuerDetails = new X500NameAForm(); //20030122AH
  private X500NameAForm _subjectDetails = new X500NameAForm(); //20030122AH
  private boolean _isX500Error = false; //20030304AH
  private String _inKeyStore; //20030415AH
  private String _inTrustStore; //20030415H
  private String _relatedCertUid; // 20040326 DDJ
  
  private Date _startDate;
  private Date _endDate;
  private String _serialNum;
  private boolean _isShowCertDetail = false; //detail consist of serialNum, startDate, endDate
  private String _isCA = "false";
  private String _replacementCertUid;
  private String _swapDate;
  private String _swapTime;
  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    //_isPartner = "false";
  }

  void clearCertificateDetails()
  { //20030122AH - Clean out details of cert (called if file removed)
    _issuerDetails = new X500NameAForm();
    _subjectDetails = new X500NameAForm();
  }

  public X500NameAForm getIssuerDetails()
  {
    return _issuerDetails;
  }

  public X500NameAForm getSubjectDetails()
  {
    return _subjectDetails;
  }

  public boolean isShowx500Name()
  {
    return _isShowX500Name;
  }

  /**
   * CertificateDispatchAction will determine if need to show the x500Name and
   * set it via this method. (Not calculated by the actionform itself).
   */
  public void setShowX500Name(boolean show)
  {
    _isShowX500Name = show;
  }

  public void setPassword(String password)
  {
    if(StaticUtils.stringNotEmpty(password))
    {
      if( !StaticUtils.objectsEqual(_password, password) )
      {
        setCertFileChanged(true);
      }
      _password = password;
    }
  }

  public String getPassword()
  {
    return _password;
  }

  public void setIsPartner(String isPartner)
  {
    if( !StaticUtils.objectsEqual(_isPartner, isPartner) )
    {
      setCertFileChanged(true);
    }
    _isPartner = isPartner;
  }

  public String getIsPartner()
  {
    return _isPartner;
  }

  public void setId(String id)
  {
    _id = id;
  }

  public String getId()
  {
    return _id;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public String getName()
  {
    return _name;
  }

  //CertFile fields:
  //With extra handling to track if file is changed

  /**
   * Returns true if any of the fields in the form changed in a way that might make the
   * imported certificate file need reevaluating to extract x500Name stuff.
   * (If the certFile was added/removed, or password, or isPartner change values)
   */
  public boolean isCertFileChanged()
  { //20030121AH
    return _isCertFileChanged;
  }

  void setCertFileChanged(boolean changed)
  { //nb: package protected so cant attack from browser ;-)
    _isCertFileChanged = changed;
  }

  public void setCertFileDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField("certFile",deletions[i]);
    }
    setCertFileChanged(true); //20030121AH
  }

  public void setCertFileUpload(FormFile file)
  {
    if(addFileToField("certFile",file))
    {
      setCertFileChanged(true); //20030121AH
    }
  }

  public FormFileElement[] getCertFile()
  { return _certFile; }

  public void setCertFile(FormFileElement[] certFileElements)
  {
    _certFile = certFileElements;
    setCertFileChanged(true); //20030121AH
  }
  //...............

  public void setX500Error(boolean isX500Error)
  { //20030404AH
    _isX500Error = isX500Error;
  }

  public boolean isX500Error()
  { //20030404
    return _isX500Error;
  }

  public void setInKeyStore(String inKeyStore)
  { //20030415AH
    _inKeyStore = inKeyStore;
  }

  public String getInKeyStore()
  { //20030415AH
    return _inKeyStore;
  }

  public void setInTrustStore(String inTrustStore)
  { //20030415AH
    _inTrustStore = inTrustStore;
  }

  public String getInTrustStore()
  { //20030415AH
    return _inTrustStore;
  }

  public void setRelatedCertUid(String relatedCertUid)
  {
    _relatedCertUid = relatedCertUid;
  }

  public String getRelatedCertUid()
  {
    return _relatedCertUid;
  }

	public Date getEndDate()
	{
		return _endDate;
	}

	public void setEndDate(Date date)
	{
		_endDate = date;
	}

	public Date getStartDate()
	{
		return _startDate;
	}

	public void setStartDate(Date date)
	{
		_startDate = date;
	}

	public String getSerialNum()
	{
		return _serialNum;
	}

	public void setSerialNum(String num)
	{
		_serialNum = num;
	}

	public boolean isShowCertDetail()
	{
		return _isShowCertDetail;
	}

	public void setShowCertDetail(boolean showCertDetail)
	{
		_isShowCertDetail = showCertDetail;
	}

	public String getIsCA()
	{
		return _isCA;
	}

	public void setIsCA(String _isca)
	{
		_isCA = _isca;
	}

	public String getReplacementCertUid()
	{
		return _replacementCertUid;
	}

	public void setReplacementCertUid(String certUid)
	{
		_replacementCertUid = certUid;
	}
	
	public void setSwapDate(String swapDate)
	{
		_swapDate = swapDate; 
	}
	
	public void setSwapTime(String swapTime)
	{
		_swapTime = swapTime; 
	}
	
	public String getSwapDate()
	{
		return _swapDate;
	}

	public String getSwapTime()
	{
		return _swapTime;
	}

	
}