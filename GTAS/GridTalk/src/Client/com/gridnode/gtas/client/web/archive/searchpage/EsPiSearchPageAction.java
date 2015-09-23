package com.gridnode.gtas.client.web.archive.searchpage;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gridnode.gtas.client.web.navigation.NavigationRenderer;
import com.gridnode.gtas.client.web.renderers.BaseTagRenderer;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingPipelineImpl;
import com.gridnode.gtas.client.web.renderers.SimpleResourceLookup;
import com.gridnode.gtas.client.web.renderers.URLRewriterImpl;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.*;

public class EsPiSearchPageAction extends GTActionBase
{
  public static final String TARGET = IDocumentKeys.ES_PI_SEARCH_PAGE;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping, form, request, response);
    Locale locale = getLocale(request);

    IDocumentManager docMgr = getDocumentManager();
    if (docMgr == null)
    {
      throw new java.lang.NullPointerException("No document manager found");
    }
    MessageResources messageResources = getResources(request);
    ActionErrors actionErrors = getActionErrors(request);
    //TEST
//    if (form == null)
//    {
//      form = new EsSearchPageAForm();
//    }
    ISimpleResourceLookup rLookup = new SimpleResourceLookup(locale,
        messageResources);
    IURLRewriter urlRewriter = new URLRewriterImpl(response, request.getContextPath());
    
//    //TEST
//    OperationContext operationContext = OperationContext.getOperationContext(request);
//    AssertUtil.assertTrue(operationContext != null);
//    RenderingContext rContext = new RenderingContext(operationContext, docMgr, rLookup,
//                                                     urlRewriter, actionErrors, request, getServlet().getServletContext());
    
    RenderingContext rContext = new RenderingContext(null, docMgr, rLookup,
        urlRewriter, actionErrors, request, getServlet().getServletContext());
    
    RenderingPipelineImpl pipeline = new RenderingPipelineImpl(TARGET, docMgr);
    BaseTagRenderer baseTagRenderer = new BaseTagRenderer(request, "base_tag");
    pipeline.addRenderer(baseTagRenderer);
    
    //22092006 testing
    //IDocumentRenderer pageRenderer = new EsPiSearchPageRenderer(rContext, mapping);
    IDocumentRenderer pageRenderer = new EsPiSearchPageRenderer(actionContext,rContext, false);
    
    
    pipeline.addRenderer(pageRenderer);

    NavigationRenderer navRenderer = new NavigationRenderer(rContext,
                                                            getProcessedPath(actionContext),
                                                            actionContext.getMapping());
    pipeline.addRenderer(navRenderer);

    addCommonContentRenderer(actionContext, rContext, pipeline);
    RenderUtil.renderError(actionContext, rContext, pipeline);
    setRenderingPipeline(request, pipeline);
    return mapping.findForward("view");
  }
}
