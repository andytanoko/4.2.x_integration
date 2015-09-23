/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BorderRenderingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-10     Andrew Hill         Created
 * 2003-03-18     Andrew Hill         Support for setting window title
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.xml.IDocumentManager;

/**
 * Render one of the frames or frameborder pages.
 * For efficiency it calls rendering code inline instead of forwarding to rendering action.
 */
public class BorderRenderingAction extends GTActionBase
{
  public ActionForward execute(ActionMapping actionMapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  {
    ActionContext actionContext = new ActionContext(actionMapping,form,request,response);
    try
    {
      BorderRenderingActionMapping mapping = (BorderRenderingActionMapping)actionMapping;
      String document = mapping.getDocument();
      IRenderingPipeline rPipe = getRenderingPipeline(actionContext, document);
      try
      {
        RenderingUtils.renderPipeline(response.getWriter(), rPipe);
        return null;
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error rendering border page or frameset page",t);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unexpected error caught in BorderRenderingAction",t);
    }
  }

  protected IRenderingPipeline getRenderingPipeline(ActionContext actionContext, String document)
    throws GTClientException
  {
    try
    {
      RenderingContext rContext = createRenderingContext(actionContext);
      BaseTagRenderer baseTagRenderer = new BaseTagRenderer(actionContext.getRequest(), "base_tag");
      RenderingPipelineImpl rPipe = new RenderingPipelineImpl(document,rContext.getDocumentManager());
      BorderRenderingActionMapping brMapping = ((BorderRenderingActionMapping)actionContext.getMapping());
      String[] frames = brMapping.getFramesArray();
      if(frames != null)
      {
        BorderRenderer borderRenderer = new BorderRenderer( rContext,
                                                            getSrcUrls(actionContext, frames),
                                                            brMapping.getTitle());
        rPipe.addRenderer(borderRenderer);
      }
      rPipe.addRenderer(baseTagRenderer);
      return rPipe;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error instantiating rendering pipeline in BorderRenderingAction."
                + " Document key=" + document,t);
    }
  }

  protected RenderingContext createRenderingContext(ActionContext actionContext)
    throws GTClientException
  {
    ISimpleResourceLookup rLookup = createResourceLookup(actionContext);
    //ActionErrors actionErrors = getActionErrors(actionContext.getRequest());
    ActionErrors actionErrors = null;
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

  protected String[] getSrcUrls(ActionContext actionContext, String[] srcForwards)
    throws GTClientException
  {
    try
    {
      if(srcForwards == null) return null;
      if(srcForwards.length == 0) return null;
      String[] urls = new String[srcForwards.length];
      ActionMapping mapping = actionContext.getMapping();
      for(int i=0; i < srcForwards.length; i++)
      {
        ActionForward forward = mapping.findForward(srcForwards[i].trim());
        if(forward == null)
        {
          throw new NullPointerException("Cannot find forward:" + srcForwards[i]);
        }
        urls[i] = forward.getPath();
      }
      return urls;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error looking up frame src forward urls",t);
    }
  }
}

