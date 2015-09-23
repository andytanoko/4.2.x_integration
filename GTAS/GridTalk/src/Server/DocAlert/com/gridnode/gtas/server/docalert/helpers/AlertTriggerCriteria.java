/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.document.model.GridDocument;
 
/**
 * An AlertTriggerCriteria is a data holder for the criteria fields to the
 * AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class AlertTriggerCriteria
{
  private String _docType;
  private String _partnerType;
  private String _partnerGroup;
  private String _partnerID;

  private AlertTriggerCriteria()
  {
  }

  public static AlertTriggerCriteria getCriteria(GridDocument gdoc)
  {
    AlertTriggerCriteria criteria = new AlertTriggerCriteria();
    String folder = gdoc.getFolder();

    criteria._docType = gdoc.getUdocDocType();
    if (GridDocument.FOLDER_INBOUND.equals(folder))
    {
      criteria._partnerType  = gdoc.getSenderPartnerType();
      criteria._partnerGroup = gdoc.getSenderPartnerGroup();
      criteria._partnerID    = gdoc.getSenderPartnerId();
    }
    else
    {
      criteria._partnerType  = gdoc.getRecipientPartnerType();
      criteria._partnerGroup = gdoc.getRecipientPartnerGroup();
      criteria._partnerID    = gdoc.getRecipientPartnerId();
    }

    return criteria;
  }

  public String getDocType()
  {
    return _docType;
  }

  public String getPartnerType()
  {
    return _partnerType;
  }

  public String getPartnerGroup()
  {
    return _partnerGroup;
  }

  public String getPartnerID()
  {
    return _partnerID;
  }

}
