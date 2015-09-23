/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UidConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

class UidConstraint extends AbstractConstraint implements IUidConstraint
{
  UidConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_UID, detail);
  }

  protected void initialise(int type, Properties detail)
  {
  }
}