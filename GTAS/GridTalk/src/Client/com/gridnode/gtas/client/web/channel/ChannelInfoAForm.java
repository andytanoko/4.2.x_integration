/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-09-17     Andrew Hill         Add isPartner, packagingProfile, securityProfile
 * 2002-10-08     Andrew Hill         More changes for "partnerCat" stuff
 * 2003-12-22     Daniel D'Cotta      Added embeded FlowControlInfo entity
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

import org.apache.struts.action.*;
import javax.servlet.http.*;

public class ChannelInfoAForm extends GTActionFormBase
{
  private boolean _isNewEntity;

  private String _name;
  private String _description;
  private String _refId;
  private String _tptProtocolType;
  private String _tptCommInfoUid;
  private String _packagingProfile;
  private String _securityProfile;
  private String _isPartner;
  private String _partnerCategory;

  private String _isZip;
  private String _zipThreshold;
  private String _isSplit;
  private String _splitSize;
  private String _splitThreshold;

  public ChannelInfoAForm()
  {
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    // 20040531 DDJ: change checkbox to combobox (due to not populating after reset is called on diversion)        
    //_isZip = "false";
    //_isSplit = "false";
    if(_isNewEntity)
    {
      _isPartner = "false";
    }
  }

  void setIsNewEntity(boolean flag)
  { _isNewEntity = flag; }

  public boolean isNewEntity()
  { return _isNewEntity; }

  public void setIsPartner(String value)
  { _isPartner = value; }

  public String getIsPartner()
  { return _isPartner; }

  public void setPackagingProfile(String value)
  { _packagingProfile = value; }

  public String getPackagingProfile()
  { return _packagingProfile; }

  public void setSecurityProfile(String value)
  { _securityProfile = value; }

  public String getSecurityProfile()
  { return _securityProfile; }

  public void setName(String value)
  {
    _name = value;
  }

  public String getName()
  {
    return _name;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setRefId(String value)
  {
    _refId = value;
  }

  public String getRefId()
  {
    return _refId;
  }

  public void setTptProtocolType(String value)
  {
    _tptProtocolType = value;
  }

  public String getTptProtocolType()
  {
    return _tptProtocolType;
  }

  public void setTptCommInfoUid(String value)
  {
    _tptCommInfoUid = value;
  }

  public String getTptCommInfoUid()
  {
    return _tptCommInfoUid;
  }

  public void setPartnerCategory(String value)
  { _partnerCategory = value; }

  public String getPartnerCategory()
  { return _partnerCategory; }

  public String getIsZip()
  { return _isZip; }

  public Boolean getIsZipAsBoolean()
  { return StaticUtils.booleanValue(_isZip); }

   public void setIsZip(String isZip)
  { _isZip = isZip; }

  public String getZipThreshold()
  { return _zipThreshold; }

  public void setZipThreshold(String zipThreshold)
  { _zipThreshold = zipThreshold; }

  public String getIsSplit()
  { return _isSplit; }

  public Boolean getIsSplitAsBoolean()
  { return StaticUtils.booleanValue(_isSplit); }

   public void setIsSplit(String isSplit)
  { _isSplit = isSplit; }

  public String getSplitSize()
  { return _splitSize; }

  public void setSplitSize(String splitSize)
  { _splitSize = splitSize; }

  public String getSplitThreshold()
  { return _splitThreshold; }

  public void setSplitThreshold(String splitThreshold)
  { _splitThreshold = splitThreshold; }
}