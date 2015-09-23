/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SimpleResourceLookup.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-03     Andrew Hill         Created
 * 2002-10-28     Andrew Hill         Method supporting params added
 * 2003-01-07     Andrew Hill         get & set locale methods added
 */
package com.gridnode.gtas.client.web.renderers;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
 
public class SimpleResourceLookup extends ResourceLookup implements ISimpleResourceLookup
{
  public SimpleResourceLookup(Locale locale, MessageResources resources)
  {
    super(locale, resources);
  }

  public String getMessage(String key, Object[] params)
  {
    String msg = null;
    if( (key == null) || key.equals("") )
    {
      msg = "";
    }
    else
    {
      if(_resources != null)
      {
        if(params == null)
        {
          if(_locale != null)
          {
            msg = _resources.getMessage(_locale, key);
          }
          else
          {
            msg = _resources.getMessage(key);
          }
        }
        else
        {
          if(_locale != null)
          {
            msg = _resources.getMessage(_locale, key, params);
          }
          else
          {
            msg = _resources.getMessage(key, params);
          }
        }
      }
      else
      {
        msg = "!" + key + "!";
      }
      if(msg == null)
      {
        msg = "?" + key + "?";
      }
    }
    return msg;
  }

  public String getMessage(String key)
  {
    return getMessage(key, null);
  }

  public Locale getLocale()
  { //20030107AH
    return _locale;
  }

  public void setLocale(Locale locale)
  { //20030107AH
    _locale = locale;
  }
}