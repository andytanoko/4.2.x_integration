/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TaskDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-29     Andrew Hill         Created
 * 2002-10-21     Daniel D'Cotta      Added Partner Watch List
 * 2002-10-29     Andrew Hill         Modify entry methods to throw GTClientException
 * 2002-11-13     Andrew Hill         Added isReturnToUpdate() hook
 * 2003-03-12     Andrew Hill         Detail View (window) Support
 * 2003-03-19     Andrew Hill         getTemplateKey() hook
 * 2003-03-25     Andrew Hill         Dont add PWL to pipe
 * 2003-07-16     Andrew Hill         Support ISetOpConAttributeDivMsg objects
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.navigation.NavigationRenderer;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public abstract class TaskDispatchAction extends OperationDispatchAction
{
  protected static final String SUBMIT_MAPPING        = "submit";
  protected static final String RESUME_UPDATE_MAPPING = "resumeUpdate";
  protected static final String RESUME_VIEW_MAPPING   = "resumeView";
  public static final String FLAG_RMOC_CONFIRMATION = "rmocConfirmation"; //20030221AH
  public static final String IS_DETAIL_VIEW = "isDetailView"; //20030312AH
  public static final String CLOSE_WINDOW_FORWARD = "closeWindow"; //20030312AH

  /**
   * @returns true if failed
   */
  protected abstract boolean doComplete(ActionContext actionContext, ActionErrors errors)
    throws GTClientException;

  public ActionForward update(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    try
    {
      OperationContext opCon = prepareOperationContext(actionContext);
      flagRmocConfirmation(actionContext, opCon, true); //20030221AH
      checkDetailView(actionContext); //20030312AH
      performUpdateProcessing(actionContext);
      prepareView(actionContext, opCon, true);
      return mapping.findForward(IGlobals.VIEW_FORWARD);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error in dispatch action", t);
    }
  }

  /**
   * Tasks in update 'mode' may need user confirmation to remove their opCon if navigating to
   * a different link (while the rmoc param is set for the filter to detect). Setting the
   * rmocConfirmation flag (a Boolean) in the opCon will let the CommonContentRenderer know if it
   * should render javascript to perform this function. The default for tasks (and thus such things
   * as EDAs etc...) is for this to occur in update mode. Subclass may override this method to
   * prevent this if required. In addition to setting the flag, a forward divMsg will be created
   * so that views diverted to will also present the confirmation request to users.
   * 20030221AH
   * @param actionContext
   * @param opCon
   * @param isUpdateMode
   * @throws GTClientException
   */
  protected void flagRmocConfirmation(ActionContext actionContext,
                                      OperationContext opCon,
                                      boolean isUpdateMode)
    throws GTClientException
  { //20030221AH
    Boolean rmocConfirmation = (Boolean)opCon.getAttribute(FLAG_RMOC_CONFIRMATION);
    if(!Boolean.TRUE.equals(rmocConfirmation))
    {
      if(isUpdateMode)
      {
        rmocConfirmation = Boolean.TRUE;
        opCon.setAttribute(FLAG_RMOC_CONFIRMATION, rmocConfirmation);
        RmocConfirmationFlagDivMsg rcfdm = new RmocConfirmationFlagDivMsg(opCon);
        opCon.addForwardDivMsg(rcfdm);
      }
    }
  }

  public Object processForwardDivMsg(Object context, Object divMsg)
    throws GTClientException
  { //20030221AH, 20030716AH
    try
    {
      /*20030716AH - co: if(divMsg instanceof RmocConfirmationFlagDivMsg)
      {
        OperationContext opCon = OperationContext.getOperationContext( ((ActionContext)context).getRequest() );
        opCon.setAttribute(FLAG_RMOC_CONFIRMATION,  Boolean.TRUE );
        return divMsg; //Leave divMsg in place so it will propogate downwards for us
      }
      else if(divMsg instanceof DetailViewFlagDivMsg)
      { //20030312AH
        OperationContext opCon = OperationContext.getOperationContext( ((ActionContext)context).getRequest() );
        opCon.setAttribute(IS_DETAIL_VIEW,  Boolean.TRUE );
        return divMsg; //Leave divMsg in place so it will propogate downwards for us
      }*/
      if(divMsg instanceof ISetOpConAttributeDivMsg)
      { //20030716AH
        return processSetOpConAttributeDivMsg((ActionContext)context, (ISetOpConAttributeDivMsg)divMsg);
      }
      else
      {
        return super.processForwardDivMsg(context,divMsg);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing forwardDivMsg",t);
    }
  }
  
  public Object processBackDivMsg(Object context, Object divMsg)
    throws GTClientException
  { //20030716AH - Support ISetOpConAttributeDivMsg on the back queu as well
    try
    {
      if(divMsg instanceof ISetOpConAttributeDivMsg)
      { 
        return processSetOpConAttributeDivMsg((ActionContext)context, (ISetOpConAttributeDivMsg)divMsg);
      }
      else
      {
        return super.processBackDivMsg(context,divMsg);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing backDivMsg",t);
    }
  }
  
  /**
   * Perform the task of setting an attribute value in the opCon as specified by an instance
   * of ISetOpConAttributeDivMsg
   * @param actionContext
   * @param msg
   * @return msg if propogating or null
   */
  private ISetOpConAttributeDivMsg processSetOpConAttributeDivMsg(ActionContext actionContext, ISetOpConAttributeDivMsg msg)
    throws GTClientException
  { //20030716AH
    try
    {
      OperationContext opCon = OperationContext.getOperationContext( actionContext.getRequest() );
      if (opCon == null)
        throw new NullPointerException("opCon is null");
      Object attribute = msg.getAttribute();
      if (attribute == null)
        throw new NullPointerException("attribute is null");
      opCon.setAttribute( attribute,  msg.getValue() );
      return msg.isPropagating() ? msg : null;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing an ISetOpConAttributeDivMsg object",t);
    }
  }

  /**
   * Subclass may override to provide custom processing for an update operation before
   * preparing the view renderers occurs.
   */
  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  protected void performPreCompleteProcessing(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  /**
   * Subclass may override to return true and call back to update after completion of a task
   * to continue further updating in same operation. (Very similar to returning to an update
   * in the event of failed validation)
   * 20021113AH
   */
  protected boolean isReturnToUpdate(ActionContext actionContext) throws GTClientException
  {
    return false;
  }

  protected ActionForward performCompleteForwardProcessing(ActionContext actionContext,
                                                       OperationContext completedOpCon,
                                                       ActionForward forward)
    throws GTClientException
  {
    return forward;
  }

  protected IRenderingPipeline getRenderingPipeline(ActionContext actionContext,
                                                    OperationContext opContext,
                                                    boolean edit)
    throws GTClientException
  {
    RenderingContext rContext = createRenderingContext(actionContext, opContext);

    String pageKey = getFormDocumentKey(edit, actionContext);

    ArrayList insertions = new ArrayList(1);
    insertions.add( new InsertionDef("page_content", "insert_page", true, false, pageKey, false) );
    MultiNodeInsertionRenderer templateRenderer = new MultiNodeInsertionRenderer(rContext,insertions);
    BaseTagRenderer baseTagRenderer = new BaseTagRenderer(actionContext.getRequest(), "base_tag");

    IDocumentRenderer formRenderer = getFormRenderer(actionContext,rContext,edit);

    NavigationRenderer navRenderer = new NavigationRenderer(rContext,
                                                            getProcessedPath(actionContext),
                                                            actionContext.getMapping()); //20030310AH

    HelpLinkRenderer helpLinkRenderer = new HelpLinkRenderer(rContext, pageKey);

    String target = getTemplateKey(actionContext); //20030319AH

    RenderingPipelineImpl rPipe = new RenderingPipelineImpl(target,rContext.getDocumentManager());

    rPipe.addRenderer(templateRenderer);
    rPipe.addRenderer(baseTagRenderer);
    rPipe.addRenderer(formRenderer);
    rPipe.addRenderer(navRenderer);
    rPipe.addRenderer(helpLinkRenderer);
    addCommonContentRenderer(actionContext, rContext, rPipe);

    processPipeline( actionContext, rContext, rPipe); // Hook for subclass to decorate

    return rPipe;
  }

  protected String getTemplateKey(ActionContext actionContext) throws GTClientException
  { //20030319 - The insidious infection of the controller by the view progresses further :-(
    //In hidsight it would have been better if the first renderer in the pipeline obtained the
    //target document and returned it. (And the renderers should not be instantiated in the controller!)
    if( forceUseDvTemplate(actionContext) )
    {
      return IDocumentKeys.DETAIL_VIEW_TEMPLATE;
    }
    else
    {
      return checkDetailView(actionContext) ?
              IDocumentKeys.DETAIL_VIEW_TEMPLATE : IDocumentKeys.TEMPLATE; //20030312AH
    }
  }

  protected boolean forceUseDvTemplate(ActionContext actionContext) throws GTClientException
  { //20030319AH
    return false;
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    
  }

  public final ActionForward complete(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    ActionErrors actionErrors = new ActionErrors();
    try
    {
      ActionForward forward = getCompleteForward(actionContext);
      performPreCompleteProcessing(actionContext);

      boolean failed = doComplete(actionContext, actionErrors);
      if(failed)
      {
        saveErrors(actionContext.getRequest(), actionErrors);
        return update(mapping,actionForm,request,response);
      }
      else if(isReturnToUpdate(actionContext))
      { //20021113AH
        return update(mapping,actionForm,request,response);
      }
      else
      {
        OperationContext opCon = OperationContext.getOperationContext(request);
        forward = completeOperation(actionContext, forward);
        // Allow subclass to decorate the forward. We pass the completed op con too
        // as its been removed from session already so subclass cant otherwise get it anymore
        forward = performCompleteForwardProcessing(actionContext, opCon, forward);
        return forward;
      }
    }
    catch(Throwable t)
    {
      boolean returnToForward = false;
      try
      {
        returnToForward = handleCompletionException(t,actionContext,actionErrors);
      }
      catch(Throwable t2)
      {
        throw new ServletException("Error handling exception",t2);
      }
      if(!returnToForward)
      {
        throw new ServletException("Error completing task",t);
      }
      else
      {
        saveErrors(actionContext.getRequest(), actionErrors);
        return update(mapping,actionForm,request,response);
      }
    }
  }

  protected boolean handleCompletionException(Throwable exception,
                                              ActionContext actionContext,
                                              ActionErrors actionErrors)
    throws GTClientException
  {
    actionContext.getRequest().setAttribute(IRequestKeys.REQUEST_EXCEPTION, exception);
    if(exception instanceof GTClientException)
    {
      Throwable rootException = ((GTClientException)exception).getRootException();
      actionContext.getRequest().setAttribute(IRequestKeys.REQUEST_ROOT_EXCEPTION, rootException);
      if(rootException instanceof ResponseException)
      {
        ResponseException e = (ResponseException)rootException;
        ActionError error = getActionError(e);
        if(error != null)
        {
          actionErrors.add(getErrorField(e) , error );
        }
      }
      else
      {
        ActionError error = new ActionError("gtas.error.unknown");
        if(error != null)
        {
          actionErrors.add(getErrorField(exception) , error );
        }
      }
    }
    return true;
  }

  protected ActionError getActionError(ResponseException e)
  {
    return super.getActionErrorForThrowable(e);
  }

  protected String getErrorField(Throwable t)
  {
    return IGlobals.EXCEPTION_ERROR_PROPERTY;
  }

  /**
   * By default, when stacking an OperationContext to divert to another action,
   * an entity dispatch action will look for the "resumeUpdate" local forward mapping to obtain
   * the ActionForward to store for use when resuming the operation. By overriding this method
   * subclasses may alter this behaviour.
   * @param actionContext
   * @param actionForm
   * @throws GTClientException
   */
  protected ActionForward getResumeForward( ActionContext actionContext,
                                            ActionForm actionForm)
    throws GTClientException
  {
    //20030626AH - Note that EntityDispatchAction now overrides this method to provide support
    //for diverting from a viewmode screen (viewmode being an eda and not a tda concept) 
    ActionForward resume = actionContext.getMapping().findForward(RESUME_UPDATE_MAPPING);
    return resume;
  }

  protected ActionForward getSubmitForward( ActionContext actionContext )
    throws GTClientException
  { //20021203AH
    ActionForward forward = actionContext.getMapping().findForward(SUBMIT_MAPPING);
    return forward;
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    ActionForward submitForward = getSubmitForward(actionContext);
    if(submitForward == null) throw new NullPointerException("submitForward is null"); //20030416AH
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }

  protected boolean checkDetailView(ActionContext actionContext) throws GTClientException
  {
    return checkDetailView(actionContext.getRequest());
  }

  protected boolean checkDetailView(HttpServletRequest request) throws GTClientException
  { //20030312AH - Check if we are in a detail view window. If necessary set the flag in the opCon
    //attributes if this hasnt been done already and create a fwdDivMsg so diversions also
    //know about it.
    try
    {
      Boolean isDetailView = null;
      OperationContext opCon = OperationContext.getOperationContext(request);
      if(opCon != null)
      {
        isDetailView = (Boolean)opCon.getAttribute(IS_DETAIL_VIEW);
        if(isDetailView == null)
        {
          isDetailView = (Boolean)request.getAttribute(IS_DETAIL_VIEW);
        }
      }
      if(Boolean.TRUE.equals(isDetailView))
      {
        request.setAttribute(IS_DETAIL_VIEW,Boolean.TRUE);
        return true;
      }
      else
      {
        String isDetailViewStr = request.getParameter(IS_DETAIL_VIEW);
        if( StaticUtils.primitiveBooleanValue(isDetailViewStr) )
        {
          if(opCon != null)
          {
            opCon.setAttribute(IS_DETAIL_VIEW, Boolean.TRUE);
            DetailViewFlagDivMsg dvfdv = new DetailViewFlagDivMsg(opCon);
            opCon.addForwardDivMsg(dvfdv);
          }
          request.setAttribute(IS_DETAIL_VIEW,Boolean.TRUE);
          return true;
        }
        else
        {
          return false;
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error in checkDetailView method",t);
    }
  }

  protected ActionForward getCompleteForward(ActionContext actionContext)
    throws GTClientException
  { //20030312AH
    if( checkDetailView(actionContext) )
    {
      return actionContext.getMapping().findForward(CLOSE_WINDOW_FORWARD);
    }
    else
    {
      return super.getCompleteForward(actionContext);
    }
  }

  protected ActionForward getCancelForward(ActionContext actionContext)
    throws GTClientException
  {
    if( checkDetailView(actionContext) )
    {
      return actionContext.getMapping().findForward(CLOSE_WINDOW_FORWARD);
    }
    else
    {
      return super.getCompleteForward(actionContext);
    }
  }

  public ActionForward cancel(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  {
    try
    {
      boolean idv = checkDetailView(request);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error doing detailview check in cancel of task dispatch action",t);
    }
    return super.cancel(mapping, actionForm, request, response);
  }


}