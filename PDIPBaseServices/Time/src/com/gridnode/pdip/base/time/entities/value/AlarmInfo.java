// %1023788043887:com.gridnode.pdip.base.time%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 * Feb 11 2004    Koh Han Sing        Added taskId
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

import java.util.Date;

public class AlarmInfo
{
  private String _senderKey;
  private String _receiverKey;
  private Date _dueDate;
  private String _category;
  private long _alarmUid;
  private String _taskId;

  public AlarmInfo(long alarmUid, String sendKey, String receiverKey, String category,
            Date dueDate, String taskId)
  {
    this._alarmUid = alarmUid;
    this._senderKey = sendKey;
    this._receiverKey = receiverKey;
    this._category = category;
    this._dueDate = dueDate;
    this._taskId = taskId;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public long getAlarmUid()
  {
    return _alarmUid;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String getCategory()
  {
    return _category;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public Date getDueDate()
  {
    return _dueDate;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String getReceiverKey()
  {
    return _receiverKey;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String getSenderKey()
  {
    return _senderKey;
  }

  public String getTaskId()
  {
    return _taskId;
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String toString()
  {
    return "AlarmUId=" + _alarmUid + ";senderKey=" + _senderKey +
           ";receiverKey=" + getReceiverKey() + ";category=" + _category +
           ";taskId=" + _taskId + ";dueDate" + _dueDate;
  }
}
