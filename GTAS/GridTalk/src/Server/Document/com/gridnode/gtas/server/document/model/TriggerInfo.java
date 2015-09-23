/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerProcessDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 26 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.gtas.server.partnerprocess.model.ITrigger;

public class TriggerInfo
{
  public static final Integer TRIGGER_IMPORT = ITrigger.TRIGGER_IMPORT;
  public static final Integer TRIGGER_MANUAL_EXPORT = ITrigger.TRIGGER_MANUAL_EXPORT;
  public static final Integer TRIGGER_MANUAL_SEND = ITrigger.TRIGGER_MANUAL_SEND;
  public static final Integer TRIGGER_RECEIVE = ITrigger.TRIGGER_RECEIVE;

  private String  docType = "";
  private String  partnerType = "";
  private String  partnerGroup = "";
  private String  partnerId = "";
  private Integer triggerType = new Integer(-1);

  public String getDocType()
  {
    return docType;
  }

  public String getPartnerType()
  {
    return partnerType;
  }

  public String getPartnerGroup()
  {
    return partnerGroup;
  }

  public String getPartnerId()
  {
    return partnerId;
  }

  public Integer getTriggerType()
  {
    return triggerType;
  }

  public void setDocType(String docType)
  {
    this.docType = docType;
  }

  public void setPartnerType(String partnerType)
  {
    this.partnerType = partnerType;
  }

  public void setPartnerGroup(String partnerGroup)
  {
    this.partnerGroup = partnerGroup;
  }

  public void setPartnerId(String partnerId)
  {
    this.partnerId = partnerId;
  }

  public void setTriggerType(Integer triggerType)
  {
    this.triggerType = triggerType;
  }
}