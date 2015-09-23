/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageContext.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * AUG 17 2003    Jagadeesh             Created.
 */
package com.gridnode.pdip.app.channel;

import java.io.Serializable;
import java.util.*;

public class MessageContext implements IMessageContext, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3475714496152172003L;
	private Map _contextValuePair = new HashMap();
  private String _name = null;

  public MessageContext()
  {
  }

  public void setAttribute(String key, Object value)
  {
    _contextValuePair.put(key, value);
  }

  public Object getAttribute(String key)
  {
    return _contextValuePair.get(key);
  }

  public Enumeration getAttributeNames()
  {
    if (_contextValuePair.keySet() != null)
    {
      Set keySet = _contextValuePair.keySet();

      return Collections.enumeration(keySet);
    }
    else
    {
      return Collections.enumeration(Collections.EMPTY_SET);
    }
  }

  public void setName(String name)
  {
    _name = name;
  }

  public String getName()
  {
    return _name;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    Enumeration enm = this.getAttributeNames();
    while (enm.hasMoreElements())
    {
      String name = (String) enm.nextElement();
      Object value = this.getAttribute(name);
      sb.append(
        "[Context Key=" + name + "][Context Value" + value.toString() + "]");
    }
    return sb.toString();
  }
}
