/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SimpleMheReference.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 29/05/2003     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gridnode.gtas.client.GTClientException;

/*
 * A simple implementation of IGTMheReference.
 * Note that this class is NOT threadsafe. If you need threadsafety then you must organise
 * the synchronisation yourself.
 */
public class SimpleMheReference implements IGTMheReference
{
  private HashMap _refListMap; 
  
  public SimpleMheReference()
  {
    _refListMap = new HashMap();
  }
  
  public SimpleMheReference(IGTEntityReference[] refs)
  {
    this();
    if (refs == null)
      throw new NullPointerException("refs is null");
    for(int i=0; i < refs.length; i++)
    {
      addReference(refs[i]);
    }
  }
  
  public void addReference(IGTEntityReference ref)
  {
    if (ref == null)
      throw new NullPointerException("ref is null");
    if (ref.getType() == null)
      throw new NullPointerException("ref.getType() is null");
    ArrayList refList = (ArrayList)_refListMap.get(ref.getType());
    if(refList == null)
    {
      refList = new ArrayList();
      _refListMap.put(ref.getType(), refList);
    }
    refList.add(ref);
  }
  
  public List get(String type) throws GTClientException
  {
    List refList = (List)_refListMap.get(type);
    return refList == null ? Collections.EMPTY_LIST : refList;
  }

  public List getAll() throws GTClientException
  {
    ArrayList retval = new ArrayList();
    Set entrySet = _refListMap.entrySet();
    Iterator iterator = entrySet.iterator();
    for(int i=0; iterator.hasNext(); i++)
    {
      Map.Entry entry = (Map.Entry)iterator.next();
      ArrayList refList = (ArrayList)entry.getValue();
      retval.addAll(refList);
    }
    return retval;
  }

  public int size() throws GTClientException
  {
    int size = 0;
    Set entrySet = _refListMap.entrySet();
    Iterator iterator = entrySet.iterator();
    for(int i=0; iterator.hasNext(); i++)
    {
      Map.Entry entry = (Map.Entry)iterator.next();
      ArrayList refList = (ArrayList)entry.getValue();
      size += refList.size();
    }
    return size;
  }

  public int size(String entityType) throws GTClientException
  {
    ArrayList refList = (ArrayList)_refListMap.get(entityType);
    return refList == null ? 0 : refList.size();
  }

  public String[] getReferencedTypes() throws GTClientException
  {
    Set entrySet = _refListMap.entrySet();
    String[] refTypes = new String[entrySet.size()];
    Iterator iterator = entrySet.iterator();
    for(int i=0; iterator.hasNext(); i++)
    {
      Map.Entry entry = (Map.Entry)iterator.next();
      refTypes[i] = (String)entry.getKey();
    }
    return refTypes;
  }

}
