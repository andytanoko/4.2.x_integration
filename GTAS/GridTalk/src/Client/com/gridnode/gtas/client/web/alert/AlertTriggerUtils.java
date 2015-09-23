/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-13     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.ctrl.*;

public interface AlertTriggerUtils
{
  public static final Number[] _commonFields =
  {
    IGTAlertTriggerEntity.LEVEL,
    IGTAlertTriggerEntity.IS_ENABLED,
    IGTAlertTriggerEntity.IS_ATTACH_DOC,
  };
  
  public static final Number[][] _fields =
  {
    { //Level 0
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
    },
    { //Level 1
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
      //and RECIPIENTS
    },
    { //Level 2
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
      //and RECIPIENTS
    },
    { //Level 3
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.PARTNER_GROUP,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
      //and RECIPIENTS
    },
    { //Level 4
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_ID,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
      //and RECIPIENTS
    },    
  };
  
  public static final String[] _recipientTypeValues = new String[]
  {
    IGTAlertTriggerEntity.RECIPIENT_TYPE_USER,
    IGTAlertTriggerEntity.RECIPIENT_TYPE_ROLE,
    IGTAlertTriggerEntity.RECIPIENT_TYPE_EMAIL_ADDRESS,
    IGTAlertTriggerEntity.RECIPIENT_TYPE_EMAIL_CODE,
    IGTAlertTriggerEntity.RECIPIENT_TYPE_EMAIL_CODE_XPATH,
    IGTAlertTriggerEntity.RECIPIENT_TYPE_EMAIL_ADDRESS_XPATH,
  };
}
