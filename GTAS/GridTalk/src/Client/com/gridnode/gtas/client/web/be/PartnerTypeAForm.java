/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-09     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class PartnerTypeAForm extends GTActionFormBase
{
  private String _uid;
  private String _partnerType;
  private String _description;

  public void setUid(String value)
  {
    _uid = value;
  }

  public String getUid()
  {
    return _uid;
  }

  /**
   * Set the partnerType uid
   * @param uid as string
   */
  public void setPartnerType(String value)
  {
    _partnerType = value;
  }

  /**
   * Get partnerType uid
   * @return partnerType uid as string
   */
  public String getPartnerType()
  {
    return _partnerType;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }


}