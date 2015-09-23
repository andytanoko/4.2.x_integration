/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReminderAlert.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for ReminderAlert entity. A ReminderAlert is a
 * classification of a type of document.<P>
 *
 * The Model:<BR><PRE>
 *   UId               - UID for a ReminderAlert entity instance.
 *   Version           - Version of the entity instance.
 *   TrackRecordUID    - UID of the ResponseTrackRecord this ReminderAlert is
 *                       configured for.
 *   DaysToReminder    - Number of days after tracking date before a reminder
 *                       will be sent out if the response document has not been
 *                       received.
 *   AlertToRaise      - Name of the Alert to raise if the reminder alert must
 *                       be raised.
 *   DocRecipientXpath - Xpath to extract the email code of the recipient of
 *                       the sent document.
 *   DocSenderXpath    - Xpath to extract the email code of the sender of the
 *                       sent document.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ReminderAlert
  extends    AbstractEntity
  implements IReminderAlert
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2703274672101932187L;
	protected Long  _trackRecordUID;
  protected int  _daysToReminder;
  protected String _alertToRaise;
  protected String _docRecipientXpath;
  protected String _docSenderXpath;

  public ReminderAlert()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return "TrackRecord: "+getTrackRecordUID() + "- " + getDaysToReminder() + " days";
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public Long getTrackRecordUID()
  {
    return _trackRecordUID;
  }

  public int getDaysToReminder()
  {
    return _daysToReminder;
  }

  public String getAlertToRaise()
  {
    return _alertToRaise;
  }

  public String getDocRecipientXpath()
  {
    return _docRecipientXpath;
  }

  public String getDocSenderXpath()
  {
    return _docSenderXpath;
  }

  // *************** Setters for attributes *************************

  public void setTrackRecordUID(Long trackRecordUID)
  {
    _trackRecordUID = trackRecordUID;
  }

  public void setDaysToReminder(int days)
  {
    _daysToReminder = days;
  }

  public void setAlertToRaise(String alertName)
  {
    _alertToRaise = alertName;
  }

  public void setDocRecipientXpath(String docRecipientXpath)
  {
    _docRecipientXpath = docRecipientXpath;
  }

  public void setDocSenderXpath(String docSenderXpath)
  {
    _docSenderXpath = docSenderXpath;
  }
}