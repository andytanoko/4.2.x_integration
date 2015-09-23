/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Navgroup.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-21     Andrew Hill         Created
 * 2003-03-27     Andrew Hill         Highlighter support
 * 2003-06-17     Andrew Hill         Use sequenced HashMap from commons-collections (2.1) for children
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;

import com.gridnode.gtas.client.utils.*;

public class Navgroup extends IdentifiedBean
{
  private String  _headgroup;
  private String  _document;
  private SequencedHashMap _children; //20030617AH
  private String _highlighter; //20030327AH
  private String _headlight; //20030328AH

  public Navgroup()
  { 
    _children = new SequencedHashMap(); //20030617AH
  }

  public String toString()
  {
    return "Navgroup[" + getId() + "," + _headgroup + "," + _document + "]";
  }

  public void freeze()
  {
    super.freeze();
    Iterator i = getChildren();
    while(i.hasNext())
    {
      ((FreezeableConfigObject)i.next()).freeze();
    }
  }

  public void setHeadgroup(String headgroup)
  {
    assertNotFrozen();
    _headgroup = headgroup;
  }

  public String getHeadgroup()
  { return _headgroup; }

  public void setDocument(String document)
  {
    assertNotFrozen();
    _document = document;
  }

  public String getDocument()
  { return _document; }

  public void addChild(IdentifiedBean child)
  {
    assertNotFrozen();
    _children.put(child.getId(),child);
  }

  public IdentifiedBean getChild(String id)
  {
    return (IdentifiedBean)_children.get(id);
  }

  public Iterator getChildren()
  {
    return _children.values().iterator();
  }

  public void setHighlighter(String highlighter)
  { //20030327AH
    assertNotFrozen();
    _highlighter = highlighter;
  }

  public String getHighlighter()
  { //20030327AH
    return _highlighter;
  }

  public void setHeadlight(String headlight)
  { //20030328AH
    assertNotFrozen();
    _headlight = headlight;
  }

  public String getHeadlight()
  { //20030328AH
    return _headlight;
  }
}