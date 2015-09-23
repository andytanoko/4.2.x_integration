/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMappingAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class BizCertMappingAForm extends GTActionFormBase
{
  private String _partnerId;
  private String _partnerCert;
  private String _ownCert;

  /*public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    ;
  }*/

  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

  public String getPartnerId()
  {
    return _partnerId;
  }

  public void setPartnerCert(String partnerCert)
  {
    _partnerCert = partnerCert;
  }

  public String getPartnerCert()
  {
    return _partnerCert;
  }

  public void setOwnCert(String ownCert)
  {
    _ownCert = ownCert;
  }

  public String getOwnCert()
  {
    return _ownCert;
  }
}