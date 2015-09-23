/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultTriggerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultTriggerEntity
  extends AbstractGTEntity
  implements IGTTriggerEntity
{

  public boolean canDelete() throws GTClientException
  {
    return canEdit();
  }

  public boolean canEdit() throws GTClientException
  {
    Integer triggerLevel = (Integer)this.getFieldValue(TRIGGER_LEVEL);

    return !TRIGGER_LEVEL_0.equals(triggerLevel);
  }


}