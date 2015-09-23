/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BorderRenderingActionMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-10     Andrew Hill         Created
 * 2003-03-18     Andrew Hill         title property
 */
package com.gridnode.gtas.client.web.navigation;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionMapping;
 
public class BorderRenderingActionMapping extends GTActionMapping
{
  private String _document;
  private String[] _frames;
  private String _title; //20030318AH

  public void setDocument(String document)
  { _document = document; }

  public String getDocument()
  { return _document; }

  public void setFrames(String frames)
  {
    _frames = StaticUtils.explode(frames,",");
  }

  public String[] getFramesArray()
  { return _frames; }

  public void setTitle(String title)
  { _title = title; } //20030318AH

  public String getTitle()
  { return _title; } //20030318AH
}