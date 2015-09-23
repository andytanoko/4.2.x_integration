/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Andrew Hill         Created
 * 2004-08-03     Daniel D'Cotta      Added helpKey
 */
package com.gridnode.gtas.client.web.xml;

import com.gridnode.gtas.client.utils.FreezeableConfigObject;
import com.gridnode.gtas.client.utils.StaticUtils;

public class DocumentDescriptor extends FreezeableConfigObject
{
  public static final String CACHE_NONE   = "none";
  public static final String CACHE_FILE   = "file";
  public static final String CACHE_MEMORY = "memory";

  private static final String[] validCacheValues = { CACHE_NONE, CACHE_FILE, CACHE_MEMORY };

  private String _key;
  private String _uri;
  private String _cache = CACHE_NONE;
  private String _helpKey;
  private boolean _xml = true;
  private boolean _preload = false;
  private boolean _validate = false;

  public DocumentDescriptor()
  {
  }

  public String toString()
  {
    return  "DocumentDescriptor["
            + _key + ","
            + _uri + ","
            + _cache + ","
            + _helpKey + ","
            + _xml + ","
            + _preload + ","
            + _validate
            +"]";
  }

  public void setKey(String key)
  {
    assertNotFrozen();
    _key = key;
  }

  public String getKey()
  {
    return _key;
  }

  public void setUri(String uri)
  {
    assertNotFrozen();
    _uri = uri;
  }

  public String getUri()
  {
    return _uri;
  }

  public void setCache(String cache)
  {
    assertNotFrozen();
    if(cache == null) cache = CACHE_NONE;
    boolean valid = StaticUtils.arrayContains(validCacheValues, cache);
    if(!valid)
    {
      throw new java.lang.IllegalArgumentException(cache + " is not a valid value for cache");
    }
    _cache = cache;
  }

  public String getCache()
  {
    return _cache;
  }

  public void setHelpKey(String helpKey)
  {
    assertNotFrozen();
    _helpKey = helpKey;
  }

  public String getHelpKey()
  {
    return _helpKey;
  }

  public void setXml(boolean xml)
  {
    assertNotFrozen();
    _xml = xml;
  }

  public boolean isXml()
  {
    return _xml;
  }

  public void setPreload(boolean preload)
  {
    assertNotFrozen();
    _preload = preload;
  }

  public boolean isPreload()
  {
    return _preload;
  }

  public void setValidate(boolean validate)
  {
    assertNotFrozen();
    _validate = validate;
  }

  public boolean isValidate()
  {
    return _validate;
  }
}