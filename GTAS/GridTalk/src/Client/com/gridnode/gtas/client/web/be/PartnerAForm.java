/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-17     Andrew Hill         Created
 * 2002-08-20     Andrew Hill         Added be and channel fields
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class PartnerAForm extends GTActionFormBase
{
  private String _partnerId;
  private String _name;
  private String _description;
  private String _partnerTypeUid;
  private String _partnerGroupUid;
  private String _state;
  private String _creator;
  private String _created;

  private String _be;
  private String _channel;

  public void setBe(String value)
  {
    _be = value;
  }

  public String getBe()
  {
    return _be;
  }

  public void setChannel(String value)
  {
    _channel = value;
  }

  public String getChannel()
  {
    return _channel;
  }

  public void setCreator(String value)
  {
    _creator = value;
  }

  public String getCreator()
  {
    return _creator;
  }

  public void setCreated(String value)
  {
    _created = value;
  }

  public String getCreated()
  {
    return _created;
  }

  public void setState(String value)
  {
    _state = value;
  }

  public String getState()
  {
    return _state;
  }

  public void setPartnerId(String value)
  {
    _partnerId = value;
  }

  public String getPartnerId()
  {
    return _partnerId;
  }

  public void setPartnerGroup(String value)
  {
    _partnerGroupUid = value;
  }

  public String getPartnerGroup()
  {
    return _partnerGroupUid;
  }



  /**
   * Set the partnerType uid
   * @param uid as string
   */
  public void setPartnerType(String value)
  {
    _partnerTypeUid = value;
  }

  /**
   * Get partnerType uid
   * @return partnerType uid as string
   */
  public String getPartnerType()
  {
    return _partnerTypeUid;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getName()
  {
    return _name;
  }

  public void setName(String value)
  {
    _name = value;
  }

}