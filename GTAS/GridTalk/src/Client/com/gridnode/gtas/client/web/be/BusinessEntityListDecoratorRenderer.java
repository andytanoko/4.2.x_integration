/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessEntityListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-30     Andrew Hill         Created
 * 2003-03-25     Andrew Hill         Modify rendering of send button
 * 2006-04-24     Neo Sok Lay         Check for noP2P and noUDDI
 */
package com.gridnode.gtas.client.web.be;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class BusinessEntityListDecoratorRenderer  extends AbstractRenderer
{
  private static final String SEND_IMAGE_SRC = "images/actions/send.gif"; //20030325AH
  private static final String PUBLISH_IMAGE_SRC = "images/actions/publish.gif";

  private String _sendUrl;
  private String _publishUrl;

  public BusinessEntityListDecoratorRenderer( RenderingContext rContext, String sendUrl, String publishUrl)
  {
    super(rContext);
    _sendUrl = sendUrl;
    _publishUrl = publishUrl;
  }

  protected void render() throws RenderingException
  {
    try
    {
      // Send Button
      RenderingContext rContext = getRenderingContext();
      
      //NSL20060424 Do not insert Send button if NoP2P
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      if (!gtasSession.isNoP2P())
      {
      	String sendUrl = rContext.getUrlRewriter().rewriteURL(_sendUrl, false);
      	String confirmMsg = rContext.getResourceLookup().getMessage("businessEntity.listview.confirmSend");
      	
      	Node deleteLink = getElementById("delete_submit", true);
      	
      	Element sendLink = createButtonLink("send",
      	                                    "businessEntity.listview.send",
      	                                    SEND_IMAGE_SRC,
      	                                    "submitMultipleEntitiesNewWindow('send','" + confirmMsg + "','" + sendUrl + "');",
      	                                    true); //20030325AH
      	
      	deleteLink.getParentNode().insertBefore(sendLink, deleteLink); //20030325AH
      }
      //NSL20060424 Do not insert Publish button if NoP2P
      if (!gtasSession.isNoUDDI())
      {
      	// Publish button
      	String confirmMsg = rContext.getResourceLookup().getMessage("businessEntity.listview.confirmPublish");
      	String publishUrl = rContext.getUrlRewriter().rewriteURL(_publishUrl, false);
      	Element publishLink = createButtonLink("publish",
      	                                       "businessEntity.listview.publish",
      	                                       PUBLISH_IMAGE_SRC,
      	                                       "submitMultipleEntitiesNewWindow('publish','" + confirmMsg + "','" + publishUrl + "');",
      	                                       true);
      	Node deleteLink = getElementById("delete_submit", true);
      	
      	deleteLink.getParentNode().insertBefore(publishLink, deleteLink);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error decorating BusinessEntity listview",t);
    }
  }
}