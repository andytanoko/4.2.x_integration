/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGridNodeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 * 2002-11-19     Andrew Hill         canEdit(), canDelete()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultGridNodeEntity extends AbstractGTEntity
  implements IGTGridNodeEntity
{
  public boolean canDelete() throws GTClientException
  {
    return false;
  }

  public boolean canEdit() throws GTClientException
  {
    return false;
  }
}