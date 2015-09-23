/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IncorrectTriggerCriteriaException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.exceptions;

import java.util.Hashtable;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * This exception class is used for indicating the criteria fields in AlertTrigger
 * are incorrect, most likely missing fields.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class IncorrectTriggerCriteriaException
  extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5608849982383265116L;
	private static final String INCORRECT_CRITERIA   = "Incorrect AlertTrigger criteria specified:-";
  private static final String TRIGGER_LEVEL        = "\nFor Level   : ";
  private static final Hashtable _FIELD_DESC_MAP    = new Hashtable();
  static
  {
    _FIELD_DESC_MAP.put(AlertTrigger.ALERT_TYPE,    "\nAlert Type   : ");
    _FIELD_DESC_MAP.put(AlertTrigger.PARTNER_TYPE,  "\nPartner Type : ");
    _FIELD_DESC_MAP.put(AlertTrigger.PARTNER_GROUP, "\nPartner Group: ");
    _FIELD_DESC_MAP.put(AlertTrigger.DOC_TYPE,      "\nDoc. Type    : ");
    _FIELD_DESC_MAP.put(AlertTrigger.PARTNER_ID,    "\nPartner ID   : ");
  }

  private IncorrectTriggerCriteriaException(String msg)
  {
    super(msg);
  }

  public static IncorrectTriggerCriteriaException createEx(AlertTrigger trigger)
  {
    Number[] condFields = trigger.getLevelCondFields();

    StringBuffer buff = new StringBuffer(INCORRECT_CRITERIA);
    buff.append(TRIGGER_LEVEL).append(trigger.getLevel());
    for (int i=0; i<condFields.length; i++)
    {
      buff.append(_FIELD_DESC_MAP.get(condFields[i])).append(trigger.getFieldValue(condFields[i]));
    }
    return new IncorrectTriggerCriteriaException(buff.toString());
  }

}