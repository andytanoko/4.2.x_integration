/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-30     Daniel D'Cotta      Created
 * 2003-03-25     Andrew Hill         Made compatible with newUi
 * 2003-03-31     Andrew Hill         Include processDefName in listview heading
 * 2003-04-10     Andrew Hill         Pass isNotElvD value from abort
 */
package com.gridnode.gtas.client.web.bp;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;

public class ProcessInstanceListDecoratorRenderer  extends AbstractRenderer
{
  private static final String ABORT_IMAGE_SRC = "images/actions/abort.gif"; //20030325AH
  private String _abortUrl;

  public ProcessInstanceListDecoratorRenderer(RenderingContext rContext, String abortUrl)
  {
    super(rContext);
    _abortUrl = abortUrl;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();

      //20030331AH - Add name to heading
      String processDefName = rContext.getRequest().getParameter("processDefName");
      if( StaticUtils.stringNotEmpty(processDefName))
      {
        String heading = rContext.getResourceLookup().getMessage("processInstance.listview.heading1",
                         new String[] {processDefName});
        Element headingNode = getElementById("listview_heading",false);
        if(headingNode != null)
        {
          replaceTextCarefully(headingNode,heading);
        }
      }
      //...

      Node deleteLink = getElementById("delete_submit", true);

      //20030325AH - Mod to use new createButtonLink method
      String abortUrl = rContext.getUrlRewriter().rewriteURL(_abortUrl, false);
      abortUrl = StaticWebUtils.addParameterToURL(abortUrl,
                                                  EntityDispatchAction.IS_NOT_ELV_DIVERT,
                                                  "true"); //20030410AH
      String abortSubmitUrl = "submitMultipleEntities('abort','','" + abortUrl + "');";
      Element abortLink = createButtonLink( "abort",
                                            "processInstance.listview.abort",
                                            ABORT_IMAGE_SRC,
                                            abortSubmitUrl,
                                            true); //20030325AH
      deleteLink.getParentNode().insertBefore(abortLink, deleteLink); //20030325AH
      //...
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error decorating ProcessInstance listview", t);
    }
  }
}