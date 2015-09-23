/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagaingInfoAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2003-12-22     Daniel D'Cotta      Moved Zip & ZipTreshold to FlowControlInfo
 */
package com.gridnode.gtas.client.web.channel;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class PackagingInfoAForm extends GTActionFormBase
{
  private boolean _isNewEntity;

  private String _name;
  private String _description;
  private String _envelope;
  // private String _zip;
  // private String _zipThreshold;
  private String _isPartner;
  private String _partnerCategory;
  private String _refId;

  // 20031124 DDJ: AS2 fields
  private String _isAckReq;
  private String _isAckSigned;
  private String _isNrrReq;
  private String _isAckSyn;
  private String _returnUrl;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    // _zip = "false";
    if(_isNewEntity)
    {
      _isPartner = "false";
    }


    //Following fields set as workaround for bug in GTAS action
    //that causes exception passing null to non-relevent fields
    _name = "";
    _description = "";
    _envelope = "None";
    // _zipThreshold = "128";
    
    // 20031124 DDJ: AS2 fields
    _isAckReq = "false";
    _isAckSigned = "false";
    _isNrrReq = "false";
    _isAckSyn = "false";
  }

  void setIsNewEntity(boolean flag)
  { _isNewEntity = flag; }

  public boolean isNewEntity()
  { return _isNewEntity; }

  public void setRefId(String value)
  { _refId = value; }

  public String getRefId()
  { return _refId; }

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getEnvelope()
  { return _envelope; }

  public void setEnvelope(String envelope)
  { _envelope=envelope; }

/*
  public String getZip()
  { return _zip; }

  public void setZip(String zip)
  { _zip=zip; }

  public String getZipThreshold()
  { return _zipThreshold; }

  public void setZipThreshold(String zipThreshold)
  { _zipThreshold=zipThreshold; }
*/  

  public void setIsPartner(String value)
  { _isPartner = value; }

  public String getIsPartner()
  { return _isPartner; }

  public void setPartnerCategory(String value)
  { _partnerCategory = value; }

  public String getPartnerCategory()
  { return _partnerCategory; }

  // 20031124 DDJ: AS2 fields
  public String getIsAckReq()
  { return _isAckReq; }

  public Boolean getIsAckReqAsBoolean()
  { return StaticUtils.booleanValue(_isAckReq); }

  public void setIsAckReq(String string)
  { _isAckReq = string; }

  public String getIsAckSigned()
  { return _isAckSigned; }

  public Boolean getIsAckSignedAsBoolean()
  { return StaticUtils.booleanValue(_isAckSigned); }

  public void setIsAckSigned(String string)
  { _isAckSigned = string; }

  public String getIsNrrReq()
  { return _isNrrReq; }

  public Boolean getIsNrrReqAsBoolean()
  { return StaticUtils.booleanValue(_isNrrReq); }

  public void setIsNrrReq(String string)
  { _isNrrReq = string; }

  public String getIsAckSyn()
  { return _isAckSyn; }

  public Boolean getIsAckSynAsBoolean()
  { return StaticUtils.booleanValue(_isAckSyn); }

  public void setIsAckSyn(String string)
  { _isAckSyn = string; }

  public String getReturnUrl()
  { return _returnUrl; }

  public void setReturnUrl(String string)
  { _returnUrl = string; }
}