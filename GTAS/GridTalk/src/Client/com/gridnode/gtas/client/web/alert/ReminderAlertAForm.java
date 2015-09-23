/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlertAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ReminderAlertAForm extends GTActionFormBase
{
  private String _daysToReminder;
  private String _alertToRaise;
  private String _docRecipientXpath;
  private String _docSenderXpath;

  public String getDaysToReminder()
  { return _daysToReminder; }

  public Integer getDaysToReminderInteger()
  { return StaticUtils.integerValue(_daysToReminder); }

  public void setDaysToReminder(String daysToReminder)
  { _daysToReminder=daysToReminder; }

  public String getAlertToRaise()
  { return _alertToRaise; }

  public void setAlertToRaise(String alertToRaise)
  { _alertToRaise=alertToRaise; }

  public String getDocRecipientXpath()
  { return _docRecipientXpath; }

  public void setDocRecipientXpath(String docRecipientXpath)
  { _docRecipientXpath=docRecipientXpath; }

  public String getDocSenderXpath()
  { return _docSenderXpath; }

  public void setDocSenderXpath(String docSenderXpath)
  { _docSenderXpath=docSenderXpath; }
}