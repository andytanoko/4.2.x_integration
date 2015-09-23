/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BorderRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-10     Andrew Hill         Created
 * 2003-03-18     Andrew Hill         Support for setting window title
 */
package com.gridnode.gtas.client.web.navigation;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
 

/**
 * For use in rendering the borders frameset et al...
 */
public class BorderRenderer extends AbstractRenderer
{
  String[] _srcUrls = null;
  String _title = null; //20030318AH

  public BorderRenderer(RenderingContext rContext, String[] srcUrls, String title)
  {
    super(rContext);
    _srcUrls = srcUrls;
    _title = title; //20030318AH
  }

  public void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();

      if( StaticUtils.stringNotEmpty(_title) )
      { //20030318AH
        Element titleNode = getElementById("title",false);
        if(titleNode != null)
        {
          String i18nTitle = rContext.getResourceLookup().getMessage(_title);
          replaceText(titleNode, i18nTitle);
        }
      }

      String[] srcUrls = _srcUrls;
      if(srcUrls != null)
      {
        try
        {
          Element frameset = getElementById("frameset",true);
          NodeList children = frameset.getChildNodes();
          int i=0;
          int j=0;
          int childLength = children.getLength();
          int srcLength = srcUrls.length;
          while( (i < childLength) && (j < srcLength) )
          {
            Node child = children.item(i);
            i++;
            if(nodeIsElement(child))
            {
              Element element = (Element)child;
              if("frame".equals( element.getNodeName() ) )
              {
                String url = rContext.getUrlRewriter().rewriteURL(srcUrls[j]);
                element.setAttribute("src",url);
                j++;
              }
            }
          }
        }
        catch(Throwable t)
        {
          throw new RenderingException("Error setting frame src values",t);
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error performing border or frame rendering",t);
    }
  }
}