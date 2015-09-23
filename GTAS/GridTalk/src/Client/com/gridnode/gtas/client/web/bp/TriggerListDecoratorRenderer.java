/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: triggerListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-15     Andrew Hill         Created
 * 2002-12-10     Andrew Hill         Fix level 0 heading bug
 */
package com.gridnode.gtas.client.web.bp;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTTriggerEntity;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
 
public class TriggerListDecoratorRenderer  extends AbstractRenderer
{
  private Integer _level;

  public TriggerListDecoratorRenderer( RenderingContext rContext, Integer level )
  {
    super(rContext);
    _level = level;
  }

  protected void render() throws RenderingException
  {
    if(_level != null)
    {
      renderLabel("listview_heading","trigger.listview.heading.level" + _level,false);//20021210
      if(IGTTriggerEntity.TRIGGER_LEVEL_0.equals(_level))
      {
        removeNode("create_details",false);
        removeNode("delete_details",false);
      }
      else
      {
        Element createLink = getElementById("create",false);
        if(createLink != null)
        {
          String href = createLink.getAttribute("href");
          if(href != null)
          {
            href = StaticWebUtils.addParameterToURL(href, "triggerLevel","" + _level);
            createLink.setAttribute("href",href);
          }
        }
      }
    }
  }
}