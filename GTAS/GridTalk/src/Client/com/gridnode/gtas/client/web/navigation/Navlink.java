/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Navlink.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-22     Andrew Hill         Created
 * 2003-03-27     Andrew Hill         Assist highlight support by processing value query params
 * 2005-03-15     Andrew Hill         Added the requiresAdmin property
 * 2006-04-24     Neo Sok Lay         Added the requiresP2P and requiresUDDI property
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.*;

import com.gridnode.gtas.client.utils.*;

public class Navlink extends IdentifiedBean
{
  protected String _label;
  protected String _value;
  protected boolean _clear;

  protected String _valuePath; //20030327AH
  protected NameValuePair[] _parameters; //20030327AH
  private static final NameValuePair[] EMPTY_PARAMETERS = new NameValuePair[0]; //20030327AH
  private boolean _requiresAdmin; //20050315AH
  private boolean _requiresP2P; //NSL20060424
  private boolean _requiresUDDI; //NSL20060423
  public Navlink()
  {
    _clear = true;
  }

  public String toString()
  {
    return "Navlink[" + getId() + "," + _label + "," + _value + "," + _clear + "]";
  }

  public void freeze()
  { //20030327AH
    super.freeze();
    if(_parameters != null)
    {
      for(int i=0; i < _parameters.length; i++)
      {
        _parameters[i].freeze();
      }
    }
  }

  public void setLabel(String label)
  {
    assertNotFrozen();
    _label = label;
  }

  public String getLabel()
  { return _label; }

  public void setValue(String value)
  {
    assertNotFrozen();
    _value = value;
    processValueUrl(value); //20030327AH
  }

  public String getValue()
  { return _value; }

  public void setClear(boolean removeOpContext)
  {
    assertNotFrozen();
    _clear = removeOpContext;
  }

  public boolean isClear()
  { return _clear; }

  public String getValuePath()
  { return _valuePath; } //20030327AH

  public NameValuePair[] getParameters()
  { //20030327AH
    return _parameters == null ? EMPTY_PARAMETERS : (NameValuePair[])_parameters.clone();
  }

  public Iterator getChildren()
  { //20030327AH - Save us doing a whole bunch of instanceof
    return null;
  }

  //20030327AH - Methods to process value URL...

  protected void processValueUrl(String value)
  {
    String valuePath = extractValuePath(value);
    _valuePath = valuePath;
    if(valuePath != null)
    {
      String valueParams = extractValueParamString(value);
      if(valueParams != null)
      {
        StringTokenizer tokenizer = new StringTokenizer(valueParams,"&");
        int paramCount = tokenizer.countTokens();
        if(paramCount > 0)
        {
          NameValuePair[] params = new NameValuePair[paramCount];
          for(int i=0; i < paramCount; i++)
          {
            String parameter = tokenizer.nextToken();
            int equals = parameter.indexOf("=");
            if( (equals > 0) && (equals < (parameter.length() - 1)) )
            {
              String paramName = parameter.substring(0,equals);
              String paramValue = parameter.substring(equals + 1);
              NameValuePair nvp = new NameValuePair(paramName, paramValue);
              params[i] = nvp;
            }
            else
            {
              throw new IllegalArgumentException("Syntax error in value \""
                                                  + value
                                                  + "\" - Malformed parameter \""
                                                  + parameter
                                                  + "\"");
            }
          }
          _parameters = params;
        }
        else
        {
          _parameters = null;
        }
      }
    }
  }

  protected String extractValueParamString(String value)
  {
    if(value == null) return null;
    int query = value.indexOf("?");
    return (query != -1) ? value.substring(query+1) : null;
  }

  protected String extractValuePath(String value)
  {
    if(value == null) return null;
    int period = value.indexOf(".");
    return (period != -1) ? value.substring(0,period) : value;
  }

  //...

  public boolean isRequiresAdmin()
  { //20050315AH
    return _requiresAdmin;
  }

  public void setRequiresAdmin(boolean b)
  { //20050315AH
    _requiresAdmin = b;
  }

  public boolean isRequiresP2P()
  {
  	return _requiresP2P;
  }
  
  public void setRequiresP2P(boolean b)
  {
  	_requiresP2P = b;
  }
  
  public boolean isRequiresUDDI()
  {
  	return _requiresUDDI;
  }
  
  public void setRequiresUDDI(boolean b)
  {
  	_requiresUDDI = b;
  }

}