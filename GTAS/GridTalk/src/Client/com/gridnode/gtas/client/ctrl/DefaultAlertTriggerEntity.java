/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAlertTriggerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultAlertTriggerEntity extends AbstractGTEntity
  implements IGTAlertTriggerEntity
{

  /*
   * Returns true if this alertTrigger may be deleted. This is based on the value of the
   * canDelete field which is set by GTAS.
   * @returns canDelete
   */
  public boolean canDelete() throws GTClientException
  {
    Boolean canDelete = (Boolean)getFieldValue(CAN_DELETE);
    return(Boolean.TRUE.equals(canDelete));
  }
}