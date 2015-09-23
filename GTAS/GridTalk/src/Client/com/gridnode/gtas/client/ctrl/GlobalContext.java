/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Context.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Andrew Hill         Created
 * 2002-10-25     Andrew Hill         Made into singleton
 * 2002-12-02     Andrew Hill         isRegistered property
 * 2003-03-31     Andrew Hill         Store default page size
 * 2004-04-24     Andrew Hill         isApplicationStarted property
 */
package com.gridnode.gtas.client.ctrl;

import java.io.Serializable;
import java.util.HashMap;

public final class GlobalContext implements Serializable
{
  private static GlobalContext _instance;
  private HashMap _attributes; //Can use hashmap as the accessors are already syncronized
  private boolean _isRegistered = false;
  private int _defaultPageSize = 6;
  private boolean _isPcpKnown; //20030424AH

  GlobalContext()
  {
  }

  public void setDefaultPageSize(int defaultPageSize)
  { //20030331AH
    //warning: this value is shared across the whole webapp
    //@todo: remove this attribute once we have a userPrefs entity to store it in
    _defaultPageSize = defaultPageSize;
  }

  public int getDefaultPageSize()
  { //20030331AH
    return _defaultPageSize;
  }

  /**
   * The GlobalContext is implemented as a singleton to avoid the cost of serialization
   * between clustered machines. This means each JVM will have its own copy and this copy
   * will likely be in a different state to others. Users of this class should account for this
   * probability.
   */
  public static final synchronized GlobalContext getInstance()
  {
    if(_instance == null)
    {
      _instance = new GlobalContext();
    }
    return _instance;
  }

  /**
   * Retrieve object from Context
   * @param key object under which object is keyed
   * @return Object
   */
  final synchronized Object getAttribute(Object key)
  {
    if(_attributes != null)
      return _attributes.get(key);
    else
      return null;
  }

  /**
   * Store an object in the Context
   * @param key used as key for object
   * @param object
   */
  final synchronized void setAttribute(Object key, Object value)
  {
    if(_attributes == null)
    {
      _attributes = new HashMap();
    }
    _attributes.put(key,value);
  }

  /**
   * Returns true if it is known for sure that the product is registered, else returns false indicating
   * that the product is probably not registered. (The lack of certainty being due to the possibility
   * of multiple clients one of which has registered since this reading was cached. If code
   * using this method needs to know for sure that product isnt registered it will have to doublecheck
   * false values itself. True on the other hand is a certain answer as the product cannot be UNregistered!)
   */
  boolean isRegistered()
  {
    return _isRegistered;
  }

  void setRegistered(boolean registered)
  {
    _isRegistered = registered;
  }
  
  boolean isPcpKnown()
  { //20030424AH
    return _isPcpKnown;
  }

  void setIsPcpKnown(boolean isApplicationStarted)
  { //20030424AH
    _isPcpKnown = isApplicationStarted;
  }

}