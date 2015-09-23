/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-14     Andrew Hill         Created
 * 2002-10-23     Andrew Hill         getServletContext(), getSession()
 * 2002-12-17     Andrew Hill         Browser Detection (basic)
 * 2003-08-26     Andrew Hill         Grab a ref to the ActionMapping when request is initialised
 */
package com.gridnode.gtas.client.web.renderers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentManager;

public class RenderingContext
{
  private IDocumentManager _docMgr;
  private OperationContext _opCon;
  private ISimpleResourceLookup _rLookup;
  private IURLRewriter _urlRewriter;
  private ActionErrors _actionErrors;
  private HttpServletRequest _request;
  private ServletContext _servletContext;
  private ActionMapping _mapping; //20030826AH

  /**
   * 'Internal' use only
   */
  RenderingContext()
  {
    setRequest(null);
    setOperationContext(null);
    setDocumentManager(null);
    setSimpleResourceLookup(null);
    setUrlRewriter(null);
    setActionErrors(null);
    setServletContext(null);
  }

  public RenderingContext(OperationContext opCon,
                          IDocumentManager docMgr,
                          ISimpleResourceLookup rLookup,
                          IURLRewriter urlRewriter,
                          ActionErrors actionErrors,
                          HttpServletRequest request,
                          ServletContext context)
  {
    setOperationContext(opCon);
    setDocumentManager(docMgr);
    setSimpleResourceLookup(rLookup);
    setUrlRewriter(urlRewriter);
    setActionErrors(actionErrors);
    setRequest(request);
    setServletContext(context);
  }

  public HttpServletRequest getRequest()
  {
    return _request;
  }

  public HttpSession getSession()
  {
    return _request.getSession();
  }

  public void setRequest(HttpServletRequest request)
  {
    if(request != null)
    { //20030826AH
      setMapping( (ActionMapping)request.getAttribute(Globals.MAPPING_KEY) );
    }
    _request = request;
  }

  public void setServletContext(ServletContext context)
  {
    _servletContext = context;
  }

  public ServletContext getServletContext()
  {
    return _servletContext;
  }

  public OperationContext getOperationContext()
  {
    return _opCon;
  }

  public void setOperationContext(OperationContext opCon)
  {
    _opCon = opCon;
  }

  public IDocumentManager getDocumentManager()
  {
    return _docMgr;
  }

  public void setDocumentManager(IDocumentManager docMgr)
  {
    _docMgr = docMgr;
  }

  public ISimpleResourceLookup getResourceLookup()
  {
    return _rLookup;
  }

  public void setSimpleResourceLookup(ISimpleResourceLookup rLookup)
  {
    if(rLookup != null)
    {
      _rLookup = rLookup;
    }
    else
    {
      _rLookup = new NoResourceLookup();
    }
  }

  public IURLRewriter getUrlRewriter()
  {
    return _urlRewriter;
  }

  public void setUrlRewriter(IURLRewriter urlRewriter)
  {
    if(urlRewriter != null)
    {
      _urlRewriter = urlRewriter;
    }
    else
    {
      _urlRewriter = new PassThruRewriter();
    }
  }

  public ActionErrors getActionErrors()
  {
    return _actionErrors;
  }

  public void setActionErrors(ActionErrors actionErrors)
  {
    _actionErrors = actionErrors;
  }

  public boolean isNetscape6()
  { //20021217AH
    return StaticWebUtils.isNetscape6(_request);
  }

  public boolean isMozillaRv()
  { //20021217AH
    return StaticWebUtils.isMozillaRv(_request);
  }

  public boolean isExplorer()
  { //20021217AH
    return StaticWebUtils.isExplorer(_request);
  } 
  
  public ActionMapping getMapping()
  { //20030826AH
    return _mapping;
  }

  public void setMapping(ActionMapping mapping)
  { //20030826AH
    _mapping = mapping;
  }

}