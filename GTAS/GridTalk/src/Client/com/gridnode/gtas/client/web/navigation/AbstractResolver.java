/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractResolver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.IdentifiedBean;

public abstract class AbstractResolver extends IdentifiedBean
{ 
  public String toString()
  {
    return "Resolver[" + getId() + "]";
  }

  public abstract String getNavgroupId(HttpServletRequest request) throws GTClientException;
}