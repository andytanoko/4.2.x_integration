/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Message.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-22     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.IdentifiedBean;

public class Message extends IdentifiedBean
{
  private String _label;

  public String toString()
  {
    return "Message[" + getId() + "," + _label + "]";
  }

  public void setLabel(String label)
  {
    assertNotFrozen();
    _label = label;
  }

  public String getLabel()
  { return _label; }

}