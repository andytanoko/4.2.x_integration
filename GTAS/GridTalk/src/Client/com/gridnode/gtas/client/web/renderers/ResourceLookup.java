/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceLookup.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-03     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.Locale;
import org.apache.struts.util.MessageResources;

public abstract class ResourceLookup
{
  protected Locale _locale;
  protected MessageResources _resources;

  public ResourceLookup(Locale locale, MessageResources resources)
  {
    _locale = locale;
    _resources = resources;
  }
}