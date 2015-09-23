/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-03     Andrew Hill         Created
 * 2002-10-28     Andrew Hill         Added method that shows params
 * 2002-01-07     Andrew Hill         getLocale() (Returns null)
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.*;

/**
 * An implementation of ISimpleResourceLookup that just returns the key as the resulting message.
 */
public class NoResourceLookup implements ISimpleResourceLookup
{
  public String getMessage(String key)
  {
    return key;
  }

  public String getMessage(String key, Object[] params)
  {
    if(params != null)
    {
      StringBuffer buffer = new StringBuffer(key);
      for(int i=0; i < params.length; i++)
      {
        buffer.append(" {");
        buffer.append(params[i].toString());
        buffer.append("}");
      }
      return buffer.toString();
    }
    else
    {
      return key;
    }
  }

  public Locale getLocale()
  { //20030107AH
    return null;
  }
}