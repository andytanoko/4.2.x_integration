/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

/** 
 * @deprecated use BusinessEntityListDecoratorRenderer
 */
public class RegistryConnectInfoListDecoratorRenderer  extends AbstractRenderer
{
  private static final String PUBLISH_IMAGE_SRC = "images/actions/publish.gif";
  private String _publishUrl;

  public RegistryConnectInfoListDecoratorRenderer( RenderingContext rContext, String publishUrl)
  {
    super(rContext);
    _publishUrl = publishUrl;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      String publishUrl = rContext.getUrlRewriter().rewriteURL(_publishUrl, false);
      Element publishLink = createButtonLink("publish",
                                          "registryConnectInfo.listview.publishBusinessEntities",
                                          PUBLISH_IMAGE_SRC,
                                          "pergi('" + publishUrl +"');",
                                          true);
                                          
      Node createLink = getElementById("create_details",true);
      createLink.getParentNode().appendChild(publishLink);                                          
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error decorating BusinessEntity listview",t);
    }
  }
}