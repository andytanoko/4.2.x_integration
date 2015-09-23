/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "partnerCat" stuff
 * 2002-12-04     Andrew Hill         Refactor to new CommInfo model
 */
package com.gridnode.gtas.client.web.channel;

import org.apache.struts.action.*;
import javax.servlet.http.*;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class CommInfoAForm extends GTActionFormBase
{
  private boolean _isNewEntity;

  private String _name;
  private String _description;
  private String _isPartner;
  private String _defaultTpt;
  private String _protocolType;
  private String _tptImplVersion;
  private String _refId;
  private String _partnerCategory;
  private String _url;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    if(_isNewEntity)
    {
      _isPartner = "false";
    }
  }

  void setIsNewEntity(boolean flag)
  { _isNewEntity = flag; }

  public boolean isNewEntity()
  { return _isNewEntity; }

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

  public void setDefaultTpt(String value)
  {
    _defaultTpt = value;
  }

  public String getDefaultTpt()
  {
    return _defaultTpt;
  }

  public void setProtocolType(String value)
  {
    _protocolType = value;
  }

  public String getProtocolType()
  {
    return _protocolType;
  }

  public void setTptImplVersion(String value)
  {
    _tptImplVersion = value;
  }

  public String getTptImplVersion()
  {
    return _tptImplVersion;
  }

  public void setRefId(String value)
  {
    _refId = value;
  }

  public String getRefId()
  {
    return _refId;
  }

  public void setIsPartner(String value)
  { _isPartner = value; }

  public String getIsPartner()
  { return _isPartner; }

  public void setPartnerCategory(String value)
  { _partnerCategory = value; }

  public String getPartnerCategory()
  { return _partnerCategory; }

  public void setUrl(String url)
  { _url = url; }

  public String getUrl()
  { return _url; }
}