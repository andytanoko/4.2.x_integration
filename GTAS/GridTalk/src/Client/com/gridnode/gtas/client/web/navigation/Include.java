/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Include.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-05     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.IdentifiedBean;

public class Include extends IdentifiedBean
{
  public String toString()
  {
    return "Include[" + getId() + "]";
  }

  public void freeze()
  {
    super.freeze();
  }
}