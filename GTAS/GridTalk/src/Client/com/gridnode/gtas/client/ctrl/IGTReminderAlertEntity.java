/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTReminderAlertEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.docalert.IReminderAlert;
 
public interface IGTReminderAlertEntity extends IGTEntity
{
  //Fields
  public static final Number UID                  = IReminderAlert.UID;
  public static final Number DAYS_TO_REMINDER     = IReminderAlert.DAYS_TO_REMINDER;
  public static final Number ALERT_TO_RAISE       = IReminderAlert.ALERT_TO_RAISE;
  public static final Number DOC_RECIPIENT_XPATH  = IReminderAlert.DOC_RECPT_XPATH;
  public static final Number DOC_SENDER_XPATH     = IReminderAlert.DOC_SENDER_XPATH;
}
