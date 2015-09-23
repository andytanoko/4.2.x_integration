/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.alert;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class AlertTriggerAForm extends GTActionFormBase
{
  private String _level;
  private String _alertUid;
  private String _alertType;
  private String _docType;
  private String _partnerType;
  private String _partnerGroup;
  private String _partnerId;
  private String _isEnabled;
  private String _isAttachDoc;
  private String[] _recipients;
  private String[] _deleteRecipients;
  
  private String _recipientType;
  private String _recipientValue;  

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    //Reset fields that wont be submitted in the request for certain values
    String level = request.getParameter("level");
    if( StaticUtils.primitiveIntValue(level) > 0)
    {
      setDeleteRecipients(null);
      setRecipientType(null);
      setRecipientValue(null);
    }
    setIsEnabled("false");
    setIsAttachDoc("false");    
  }
  
  public String getAlertType()
  {
    return _alertType;
  }

  public String getAlertUid()
  {
    return _alertUid;
  }

  public String getDocType()
  {
    return _docType;
  }

  public String getIsAttachDoc()
  {
    return _isAttachDoc;
  }

  public String getIsEnabled()
  {
    return _isEnabled;
  }

  public String getLevel()
  {
    return _level;
  }
  
  public Integer getLevelInteger()
  {
    return StaticUtils.integerValue( getLevel() );
  }

  public String getPartnerGroup()
  {
    return _partnerGroup;
  }

  public String getPartnerId()
  {
    return _partnerId;
  }

  public String getPartnerType()
  {
    return _partnerType;
  }

  public String[] getRecipients()
  {
    return _recipients;
  }

  public void setAlertType(String alertType)
  {
    _alertType = alertType;
  }

  public void setAlertUid(String alertUid)
  {
    _alertUid = alertUid;
  }

  public void setDocType(String docType)
  {
    _docType = docType;
  }

  public void setIsAttachDoc(String isAttachDoc)
  {
    _isAttachDoc = isAttachDoc;
  }

  public void setIsEnabled(String isEnabled)
  {
    _isEnabled = isEnabled;
  }

  public void setLevel(String level)
  {
    _level = level;
  }

  public void setPartnerGroup(String partnerGroup)
  {
    _partnerGroup = partnerGroup;
  }

  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

  public void setPartnerType(String partnerType)
  {
    _partnerType = partnerType;
  }

  public void setRecipients(String[] recipients)
  {
    _recipients = recipients;
  }

  public String getRecipientType()
  {
    return _recipientType;
  }

  public String getRecipientValue()
  {
    return _recipientValue;
  }

  public void setRecipientType(String recipientType)
  {
    _recipientType = recipientType;
  }

  public void setRecipientValue(String recipientValue)
  {
    _recipientValue = recipientValue;
  }

  public String[] getDeleteRecipients()
  {
    return _deleteRecipients;
  }

  public void setDeleteRecipients(String[] deleteRecipients)
  {
    _deleteRecipients = deleteRecipients;
  }

}