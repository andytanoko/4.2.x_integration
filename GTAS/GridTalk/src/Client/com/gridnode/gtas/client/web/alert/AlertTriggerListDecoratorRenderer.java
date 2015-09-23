/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.alert;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class AlertTriggerListDecoratorRenderer  extends AbstractRenderer
{
  //private Integer _level;

  public AlertTriggerListDecoratorRenderer( RenderingContext rContext )
  {
    super(rContext);
  }

  protected void render() throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    Integer level = AlertTriggerListAction.getLevel(rContext.getRequest());
    if(level != null)
    {
      renderLabel("listview_heading","alertTrigger.listview.heading.level" + level,false);
      Element createLink = getElementById("create",false);
      if(createLink != null)
      {
        String href = createLink.getAttribute("href");
        if(href != null)
        {
          href = StaticWebUtils.addParameterToURL(href, AlertTriggerListAction.LEVEL_PARAM,level.toString());
          createLink.setAttribute("href",href);
        }
      }
    }
  }
}