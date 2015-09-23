/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocAlertEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay        Created
 * Apr 30 2003    Neo Sok Lay         Add IS_ATTACH_RESPONSE_DOC in ResponseTrackRecord.
 */
package com.gridnode.gtas.model.docalert;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the DocAlert module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0
 */
public class DocAlertEntityFieldID
{
  private Hashtable _table;
  private static DocAlertEntityFieldID _self = null;

  private DocAlertEntityFieldID()
  {
    _table = new Hashtable();

    //ReminderAlert
    _table.put(IReminderAlert.ENTITY_NAME,
      new Number[]
      {
        IReminderAlert.ALERT_TO_RAISE,
        IReminderAlert.DAYS_TO_REMINDER,
        IReminderAlert.DOC_RECPT_XPATH,
        IReminderAlert.DOC_SENDER_XPATH,
        IReminderAlert.TRACK_RECORD_UID,
        IReminderAlert.UID,
      });

      //ResponseTrackRecord
      _table.put(IResponseTrackRecord.ENTITY_NAME,
        new Number[]
        {
          IResponseTrackRecord.ALERT_RECPT_XPATH,
          IResponseTrackRecord.RECEIVE_RESPONSE_ALERT,
          IResponseTrackRecord.REMINDER_ALERTS,
          IResponseTrackRecord.RESPONSE_DOC_TYPE,
          IResponseTrackRecord.RESPONSE_DOCID_XPATH,
          IResponseTrackRecord.SENT_DOC_TYPE,
          IResponseTrackRecord.SENT_DOCID_XPATH,
          IResponseTrackRecord.START_TRACK_DATE_XPATH,
          IResponseTrackRecord.UID,
          IResponseTrackRecord.IS_ATTACH_RESPONSE_DOC,
        });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new DocAlertEntityFieldID();
    }
    return _self._table;
  }
}