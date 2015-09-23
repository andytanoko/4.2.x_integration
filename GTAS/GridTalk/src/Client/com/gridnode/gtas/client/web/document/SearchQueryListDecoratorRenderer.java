/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SysFoldersListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-12-03     Daniel D'Cotta      Created
 * 2003-02-24     Daniel D'Cotta      Fixed nav tree highlight problem
 * 2005-03-22     Andrew Hill         Hide create and delete from users but not admins
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class SearchQueryListDecoratorRenderer  extends AbstractRenderer
{
  public SearchQueryListDecoratorRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  protected void render() throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    
    IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
    
    /*TWX 260402009
    if(!gtasSession.isAdmin())
    { //20050322AH
      removeNode("delete_details",false);
      removeNode("create_details",false);
    }*/
    
    
    // refresh 'gtas_nav_frame'
    // 20040224 DDJ: Fixed nav tree highlight problem
    //appendOnloadEventMethod("reloadNavFrame();");
    // 20030527 DDJ: Change contextPath from being hardcoded
    //appendOnloadEventMethod("loadNav('ngadocuments', '/gridtalk/navPageAction.do?navgroup=nga_documents', 'ntn_search');");
    String contextPath = rContext.getRequest().getContextPath();
    appendOnloadEventMethod("loadNav('ngadocuments', '" + contextPath +  "/navPageAction.do?navgroup=nga_documents', 'ntn_search');");
  }
}