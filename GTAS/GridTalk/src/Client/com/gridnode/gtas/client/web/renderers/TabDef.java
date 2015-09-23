/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TabDef.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-16     Andrew Hill     Created
 */
package com.gridnode.gtas.client.web.renderers;

 
public class TabDef implements ITabDef
{
  private String _titleKey;
  private String _baseId;

  public TabDef(String titleKey,
                String baseId)
  {
    _titleKey = titleKey;
    _baseId = baseId;
  }

  public String toString()
  {
     StringBuffer buffer = new StringBuffer("tabDef[");
     buffer.append(_titleKey);
     buffer.append(',');
     buffer.append(_baseId);
     buffer.append("]");
     return buffer.toString();
  }

  public void setTitleKey(String titleKey)
  {
    _titleKey = titleKey;
  }

  public String getTitleKey()
  {
    return _titleKey;
  }

  public void setBaseId(String baseId)
  {
    _baseId = baseId;
  }

  public String getBaseId()
  {
    return _baseId;
  }
}