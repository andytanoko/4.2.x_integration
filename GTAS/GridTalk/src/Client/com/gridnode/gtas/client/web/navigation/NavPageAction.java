/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavPageAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.renderers.BaseTagRenderer;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingPipelineImpl;
import com.gridnode.gtas.client.web.renderers.URLRewriterImpl;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.xml.IDocumentManager;

/**
 * Render one of the navigation pages (header or navbar).
 * For efficiency it calls rendering code inline instead of forwarding to rendering action.
 */
public class NavPageAction extends GTActionBase
{
  /**
   * request parameter containing id of nav mapping. Note that the id is not what a navMapping is
   * keyed by (thats the path), but rather the result the path maps to - the id of the reolver
   * so we dont look up the mapping based on this, we look up the resolver!
   */
  public static final String NAV_MAPPING_PARAMETER = "navMapping";
  public static final String NAV_GROUP_PARAMETER = "navgroup";

  public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping,form,request,response);
    try
    {
      IRenderingPipeline rPipe = getRenderingPipeline(actionContext);
      setRenderingPipeline(actionContext.getRequest(),rPipe);
      ActionForward view = mapping.findForward("view");
      return view;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unexpected error caught in NavPageAction",t);
    }
  }

  protected IRenderingPipeline getRenderingPipeline(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      NavigationConfig navConfig = getNavigationConfig(actionContext);
      HttpServletRequest request = actionContext.getRequest();
      String navgroupId = request.getParameter(NAV_GROUP_PARAMETER);
      if(navgroupId == null)
      { //obsolete - but leave in for now as may well revert
        String navMapping = request.getParameter(NAV_MAPPING_PARAMETER);
        navgroupId = getNavgroupId(actionContext, navMapping, navConfig);
      }
      if(navgroupId == null) throw new NullPointerException("null navgroup");
      Navgroup navgroup = navConfig.getNavgroup(navgroupId);
      if(navgroup == null) throw new NullPointerException("Couldn't find navgroup:" + navgroupId);
      String document = navgroup.getDocument();
      if(document == null) throw new NullPointerException("Navgroup '" + navgroupId + "' specifies no document");

      RenderingContext rContext = createRenderingContext(actionContext);
      BaseTagRenderer baseTagRenderer = new BaseTagRenderer(actionContext.getRequest(), "base_tag");
      RenderingPipelineImpl rPipe = new RenderingPipelineImpl(document,rContext.getDocumentManager());
      rPipe.addRenderer(baseTagRenderer);
      if(navgroupId != null)
      {
        NavPageRenderer npr = new NavPageRenderer(rContext, navgroup, actionContext.getMapping());
        rPipe.addRenderer(npr);
      }

      IDocumentRenderer ccr = getCommonContentRenderer(actionContext, rContext); //20030312AH
      rPipe.addRenderer(ccr);

      return rPipe;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error instantiating rendering pipeline in NavPageAction.",t);
    }
  }

  protected RenderingContext createRenderingContext(ActionContext actionContext)
    throws GTClientException
  {
    ISimpleResourceLookup rLookup = createResourceLookup(actionContext);
    ActionErrors actionErrors = getActionErrors(actionContext.getRequest());
    IDocumentManager docMgr = getDocumentManager();
    IURLRewriter urlRewriter = new URLRewriterImpl( actionContext.getResponse(),
                                                    actionContext.getRequest().getContextPath(),
                                                    null);
    RenderingContext rContext = new RenderingContext( null,
                                                      docMgr,
                                                      rLookup,
                                                      urlRewriter,
                                                      actionErrors,
                                                      actionContext.getRequest(),
                                                      getServlet().getServletContext());
    return rContext;
  }

  public NavigationConfig getNavigationConfig(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      ServletContext context = getServlet().getServletContext();
      NavigationConfig navConfig = (NavigationConfig)context.getAttribute(NavigationConfigManager.NAVIGATION_CONFIG_KEY);
      if(navConfig == null) throw new NullPointerException("null navConfig");
      return navConfig;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to retrieve navigation configuration information",t);
    }
  }

  public String getNavgroupId(ActionContext actionContext, String navMapping, NavigationConfig navConfig)
    throws GTClientException
  { //20030311AH - Now obsolete but leaving functional for time being
    try
    {
      if(navMapping == null) return null;
      if(navConfig == null) throw new NullPointerException("null navconfig");
      AbstractResolver resolver = navConfig.getResolver(navMapping);
      if(resolver == null) throw new NullPointerException("No resolver found with mapping id:" + navMapping);
      return resolver.getNavgroupId(actionContext.getRequest());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining navgroupId",t);
    }
  }
}

