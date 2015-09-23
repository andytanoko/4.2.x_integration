/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractLoginRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-06-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.login;

import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;


public abstract class AbstractLoginRenderer extends AbstractRenderer
{
  protected String _submitUrl;
  protected LoginAForm _form;

  public AbstractLoginRenderer( RenderingContext rContext,
                        LoginAForm form,
                        String submitUrl)
  { //20030107AH - Refactor to use RenderingContext
    //20030626AH - Refactor into this superclass
    super( rContext );
    _submitUrl = submitUrl;
    _form = form;
  }
  
  protected void renderShowInMaster()
    throws RenderingException
  {
    try
    {
      includeJavaScript(IGlobals.JS_NAVIGATION_METHODS);
      appendOnloadEventMethod("showInMaster();"); 
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering showInMaster functionality",t);
    }
  }
  
  protected void render() throws RenderingException
  {
    renderShowInMaster();
  }

}
