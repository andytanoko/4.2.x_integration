/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NoMatchingAlertTriggerException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This exception class is used for indicating there is no matching AlertTrigger
 * in progress search.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class NoMatchingAlertTriggerException
  extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3420412890486522402L;
	private static final String NO_MATCHING   = "No Matching AlertTrigger found for:-";
  private static final String ALERT_TYPE    = "\nAlert Type   : ";
  private static final String PARTNER_TYPE  = "\nPartner Type : ";
  private static final String PARTNER_GROUP = "\nPartner Group: ";
  private static final String DOC_TYPE      = "\nDoc. Type    : ";
  private static final String PARTNER_ID    = "\nPartner ID   : ";

  private NoMatchingAlertTriggerException(String msg)
  {
    super(msg);
  }

  public static NoMatchingAlertTriggerException createEx(String alertType,
                                                        String docType,
                                                        String partnerType,
                                                        String partnerGroup,
                                                        String partnerId)
  {
    StringBuffer buff = new StringBuffer(NO_MATCHING);
    buff.append(ALERT_TYPE).append(alertType);
    buff.append(DOC_TYPE).append(docType);
    buff.append(PARTNER_TYPE).append(partnerType);
    buff.append(PARTNER_GROUP).append(partnerGroup);
    buff.append(PARTNER_ID).append(partnerId);

    return new NoMatchingAlertTriggerException(buff.toString());
  }

}