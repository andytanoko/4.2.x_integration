/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.utils.StaticUtils;

public class ProcessMappingAForm extends GTActionFormBase
{
  private String _processDef;
  private String _isInitiatingRole;
  private String _docType;
  private String _sendChannelUid;
  private String _partnerId;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _isInitiatingRole = "false";
  }

  public String getProcessDef()
  { return _processDef; }

  public void setProcessDef(String processDef)
  { _processDef=processDef; }

  public String getIsInitiatingRole()
  { return _isInitiatingRole; }

  public boolean getIsInitiatingRolePrimitiveBoolean()
  { return StaticUtils.primitiveBooleanValue(_isInitiatingRole); }

  public void setIsInitiatingRole(String isInitiatingRole)
  { _isInitiatingRole=isInitiatingRole; }

  public String getDocType()
  { return _docType; }

  public void setDocType(String docType)
  { _docType=docType; }

  public String getSendChannelUid()
  { return _sendChannelUid; }

  public void setSendChannelUid(String sendChannelUid)
  { _sendChannelUid=sendChannelUid; }

  public String getPartnerId()
  { return _partnerId; }

  public void setPartnerId(String partnerId)
  { _partnerId=partnerId; }
}