/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-20     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class GridTalkMappingRuleListDecoratorRenderer  extends AbstractRenderer
{
  private Boolean _headerTransformation;

  public GridTalkMappingRuleListDecoratorRenderer(RenderingContext rContext,
                                              Boolean headerTransformation)
  {
    super(rContext);
    _headerTransformation = headerTransformation;
  }

  protected void render() throws RenderingException
  {
    if(_headerTransformation != null)
    {
      // Modify heading to display which type of mapping rules we are showing.
      if(Boolean.TRUE.equals(_headerTransformation))
      {
        renderLabel("listview_heading","gridTalkMappingRule.listview.heading.header",false);
        // Modify the create link href to append headerTransformation so it is preset for us when we open
        // the create screen.
        Element createLink = getElementById("create",false);
        if(createLink != null)
        {
          String href = createLink.getAttribute("href");
          if(href != null)
          {
            href = StaticWebUtils.addParameterToURL(href, "headerTransformation","" + _headerTransformation);
            createLink.setAttribute("href",href);
          }
        }
      }
      else if(Boolean.FALSE.equals(_headerTransformation))
      {
        renderLabel("listview_heading","gridTalkMappingRule.listview.heading.content",false);
        //Dont need to alter create link for content rules.
      }


    }
  }
}