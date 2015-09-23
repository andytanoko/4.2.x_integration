/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultActivationRecordEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 * 2002-11-19     Andrew Hill         canEdit(), canDelete()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultActivationRecordEntity extends AbstractGTEntity
  implements IGTActivationRecordEntity
{
  public boolean canDelete() throws GTClientException
  {
    return false;
  }

  public boolean canEdit() throws GTClientException
  {
    Short currentType = (Short)getFieldValue(CURRENT_TYPE);
    if(CURRENT_TYPE_ACTIVATION.equals(currentType))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}