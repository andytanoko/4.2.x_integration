/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityListRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-04     Andrew Hill         Created
 * 2002-09-19     Andrew Hill         Fixed incorrect rendering of uid value in select checkbox
 * 2003-03-25     Andrew Hill         Ensure magic links disabled for elvs
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.ctrl.IGTEntity;
 
public class EmbeddedEntityListRenderer extends EntityListRenderer
{
  public void setMagicLinksEnabled(boolean ignored)
  { //20030325AH
    if(ignored == true)
    { //20030326AH
      throw new UnsupportedOperationException("Magic links not supported by EmbeddedEntityListRenderer");
    }
  }

  public boolean isMagicLinksEnabled()
  { //20030325AH
    return false;
  }

  public EmbeddedEntityListRenderer(RenderingContext rContext)
  {
    super(rContext, null); //20030325AH
    _isMagicLinksEnabled = false; //20030326AH
  }

  protected String getEditUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    IGTEntity entity = (IGTEntity)object;
    String updateUrl = _listViewOptions.getUpdateURL();
    if(updateUrl != null)
    {
      return "javascript:elvDivert('" + updateUrl
                                      + "','" + getObjectReference(rowCount,object) + "');";
    }
    else
    {
      return null;
    }
  }

  protected String getViewUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    IGTEntity entity = (IGTEntity)object;
    String viewUrl = _listViewOptions.getViewURL();
    if(viewUrl != null)
    {
      return "javascript:elvDivert('" + viewUrl
                                      + "','" + getObjectReference(rowCount,object) + "');";
    }
    else
    {
      return viewUrl;
    }
  }
}