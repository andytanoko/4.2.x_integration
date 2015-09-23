/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsPiEntity.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Oct 3 2005		Sumedh Chalermkanjana			Created
 */
package com.gridnode.gtas.server.dbarchive.model;

import com.gridnode.pdip.framework.db.entity.*;

public class EsPiEntity extends AbstractEntity
{
  private String docNo;

  private String docType;

  private String tradingPartner;

  private String fromDate;

  private String toDate;

  
  public EsPiEntity(String no, String type, String date, String date2, String partner)
  {
    docNo = no;
    docType = type;
    fromDate = date;
    toDate = date2;
    tradingPartner = partner;
  }

  public String getDocNo()
  {
    return docNo;
  }

  public void setDocNo(String docNo)
  {
    this.docNo = docNo;
  }

  public String getDocType()
  {
    return docType;
  }

  public void setDocType(String docType)
  {
    this.docType = docType;
  }

  public String getFromDate()
  {
    return fromDate;
  }

  public void setFromDate(String fromDate)
  {
    this.fromDate = fromDate;
  }

  public String getToDate()
  {
    return toDate;
  }

  public void setToDate(String toDate)
  {
    this.toDate = toDate;
  }

  public String getTradingPartner()
  {
    return tradingPartner;
  }

  public void setTradingPartner(String tradingPartner)
  {
    this.tradingPartner = tradingPartner;
  }

  public Number getKeyId()
  {
    return IProcessInstanceMetaInfo.UID;
  }

  public String getEntityName()
  {
    return IProcessInstanceMetaInfo.ENTITY_NAME;
  }

  /**
   * At the moment, return null. (No description)
   */
  public String getEntityDescr()
  {
    return null;
  }
  
  
}
