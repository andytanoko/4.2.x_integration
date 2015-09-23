/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISimpleResourceLookup.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-03     Andrew Hill         Created
 * 2002-10-28     Andrew Hill         Method supporting params added
 * 2003-01-07     Andrew Hill         getLocale()
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.*;

public interface ISimpleResourceLookup
{
  public String getMessage(String key);
  public String getMessage(String key, Object[] params);
  public Locale getLocale(); //20030107AH
}