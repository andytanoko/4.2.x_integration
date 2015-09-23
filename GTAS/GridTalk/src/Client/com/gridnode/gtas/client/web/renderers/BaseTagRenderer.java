/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BaseTagRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created
 * 2002-05-30     Andrew Hill         Refactored to eliminate xmlc library use
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.*;
import javax.servlet.http.HttpServletRequest;

/**
 * A renderer for modifying HREF attribute of base tag
 */
public class BaseTagRenderer  extends AbstractRenderer
{
  private HttpServletRequest _request;
  private String _baseId;

  /**
   * Constructor that requires an HttpServletRequest (used for calculating base path) and the
   * id of the base tag in the target document. If the basetag id is not found in the target
   * document a RenderingException will be thrown.
   * @param request
   * @param baseId
   */
  public BaseTagRenderer(HttpServletRequest request, String baseId)
  {
    super(new RenderingContext());
    _request = request;
    _baseId = baseId;
  }

  /**
   * Modify the href attribute of the Base tag (identified by id)
   * @param target
   * @throws RenderingException
   */
  public void render() throws RenderingException
  {
    Document target = _target;
    try
    {
      Element baseTag = getElementById(_baseId);
      if(baseTag == null)
      {
        throw new RenderingException("ID " + _baseId + " not found in target document");
      }
      String href = getHRef(_request);
      baseTag.setAttribute("href",href);
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering base tag",e);
    }
  }

  /**
   * Logic to calculate base href attribute, code in method taken from struts base tag.
   * @param request The HttpServletRequest
   */
  private String getHRef(HttpServletRequest request)
  {
    // The following code has been borrowed from code in the struts taglibs...
    StringBuffer buf = new StringBuffer();
    buf.append(request.getScheme());
    buf.append("://");
    buf.append(request.getServerName());
    if("http".equals(request.getScheme()) && (80 == request.getServerPort()))
    {
        
    }
    else if("https".equals(request.getScheme()) && (443 == request.getServerPort()))
    {
        
    }
    else
    {
        buf.append(":");
        buf.append(request.getServerPort());
    }
    //buf.append(request.getRequestURI());
    //....
    buf.append(request.getContextPath());
    buf.append("/index.html");
    return buf.toString();
  }

}