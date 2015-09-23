/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavigationConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-21     Andrew Hill         Created
 * 2003-03-06     Andrew Hill         Support for Resolvers & Mappings
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.*;

import com.gridnode.gtas.client.utils.*;

public class NavigationConfig extends FreezeableConfigObject
{
  private HashMap _navgroups = new HashMap();
  private HashMap _resolvers = new HashMap(); //20030306AH
  private HashMap _mappings = new HashMap(); //20030306AH
  private HashMap _highlighters = new HashMap(); //20030327AH

  public Navgroup getNavgroup(String id)
  {
    if(id == null) //20030306AH
      throw new NullPointerException("id is null");
    return (Navgroup)_navgroups.get(id);
  }

  public Iterator getNavgroups()
  {
    return _navgroups.values().iterator();
  }

  public void addNavgroup(Navgroup navgroup)
  {
    assertNotFrozen();
    _navgroups.put(navgroup.getId(), navgroup);
  }

  public void addResolver(AbstractResolver resolver)
  { //20030306AH
    _resolvers.put(resolver.getId(), resolver);
  }

  public AbstractResolver getResolver(String id)
  { //20030306AH
    if(id == null)
      throw new NullPointerException("id is null");
    return (AbstractResolver)_resolvers.get(id);
  }

  public Iterator getResolvers()
  { //20030306AH
    return _resolvers.values().iterator();
  }

  public void addMapping(Mapping mapping)
  { //20030306AH
    _mappings.put(mapping.getPath(), mapping);
  }

  public Mapping getMapping(String path)
  { //20030306AH
    if(path == null)
      throw new NullPointerException("path is null");
    return (Mapping)_mappings.get(path);
  }

  public Iterator getMappings()
  { //20030306AH
    return _mappings.values().iterator();
  }

  public void addHighlighter(AbstractHighlighter highlighter)
  { //20030327AH
    _highlighters.put(highlighter.getId(), highlighter);
  }

  public AbstractHighlighter getHighlighter(String id)
  { //20030327AH
    if(id == null)
      throw new NullPointerException("id is null");
    return (AbstractHighlighter)_highlighters.get(id);
  }

  public Iterator getHighlighters()
  { //20030327AH
    return _highlighters.values().iterator();
  }

  public void freeze()
  {
    super.freeze();
    Iterator i = getNavgroups();
    while(i.hasNext())
    {
      ((FreezeableConfigObject)i.next()).freeze();
    }
    i = getResolvers(); //20030306AH
    while(i.hasNext())
    { //20030306AH
      ((FreezeableConfigObject)i.next()).freeze();
    }
    i = getResolvers(); //20030306AH
    while(i.hasNext())
    { //20030306AH
      ((FreezeableConfigObject)i.next()).freeze();
    }
    i = getHighlighters(); //20030327AH
    while(i.hasNext())
    { //20030327AH
      ((FreezeableConfigObject)i.next()).freeze();
    }
  }
}