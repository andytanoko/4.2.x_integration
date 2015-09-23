/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-07-30     Andrew Hill         Label for soapProcedure category
 */
package com.gridnode.gtas.client.web.bp;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTUserProcedureEntity;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class UserProcedureListDecoratorRenderer  extends AbstractRenderer
{
  private Integer _type;

  public UserProcedureListDecoratorRenderer(RenderingContext rContext, Integer type)
  {
    super(rContext);
    _type = type;
  }

  protected void render() throws RenderingException
  {
    if(_type != null)
    {
      //String type;
      if(IGTUserProcedureEntity.TYPE_EXECUTABLE.equals(_type))
      {
        renderLabel("listview_heading","userProcedure.listview.heading.executable",false);
      }
      else if(IGTUserProcedureEntity.TYPE_JAVA.equals(_type))
      {
        renderLabel("listview_heading","userProcedure.listview.heading.java",false);
      }
      else if(IGTUserProcedureEntity.TYPE_SOAP.equals(_type))
      { //20030730AH
        renderLabel("listview_heading","userProcedure.listview.heading.soap",false);
      }

      Element createLink = getElementById("create",false);
      if(createLink != null)
      {
        String href = createLink.getAttribute("href");
        if(href != null)
        {
          href = StaticWebUtils.addParameterToURL(href, "type","" + _type);
          createLink.setAttribute("href",href);
        }
      }
    }
  }
}