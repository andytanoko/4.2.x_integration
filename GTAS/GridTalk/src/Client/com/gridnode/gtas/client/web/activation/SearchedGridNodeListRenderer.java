/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchedGridNodeListRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-11     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.activation;

import com.gridnode.gtas.client.ctrl.IGTSearchedGridNodeEntity;
import com.gridnode.gtas.client.web.renderers.EmbeddedEntityListRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class SearchedGridNodeListRenderer extends EmbeddedEntityListRenderer
{
  public SearchedGridNodeListRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  protected String getEditUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    return null;
  }

  protected String getViewUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    if(gridNodeInactive(object))
    {
      //IGTEntity entity = (IGTEntity)object;
      String encodedViewUrl = "javascript:elvDivert('"
                              + _listViewOptions.getViewURL()
                              + "','" + getObjectReference(rowCount,object) + "');";
      return encodedViewUrl;
    }
    else
    {
      return null;
    }
  }

  private boolean gridNodeInactive(Object object) throws RenderingException
  {
    try
    {
      IGTSearchedGridNodeEntity searchedGn = (IGTSearchedGridNodeEntity)object;
      Short state = (Short)searchedGn.getFieldValue(IGTSearchedGridNodeEntity.STATE);
      return IGTSearchedGridNodeEntity.STATE_INACTIVE.equals(state);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error determining if SearchedGridNode is inactive: " + object,t);
    }
  }
}