/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) BusinessEntity Pte Ltd. All Rights Reserved.
 *
 * File: EmbeddedChannelInfoListRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-30     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.web.renderers.EmbeddedEntityListRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class EmbeddedChannelInfoListRenderer extends EmbeddedEntityListRenderer
{
  public EmbeddedChannelInfoListRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  protected String getEditUrl(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    return null;
  }
}