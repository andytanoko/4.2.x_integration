/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTActionBase.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-24     Andrew Hill         Created
 * 2002-10-29     Andrew Hill         getActionErrorForThrowable()
 * 2002-10-30     Andrew Hill         saveExceptions()
 * 2002-11-05     Andrew Hill         storeThrowableDetails()
 * 2002-12-11     Andrew Hill         Moved createRenderingContext() here
 * 2003-01-10     Andrew Hill         TIMEZONE_KEY
 * 2003-03-06     Andrew Hill         getProcessedPath()
 * 2003-04-25     Andrew Hill         dumpRequest() delegates to version in StaticWebUtils now
 * 2003-06-06     Andrew Hill         Version of saveExceptions() that also creates and saves the actionError
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRoleEntity;
import com.gridnode.gtas.client.ctrl.IGTRoleManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTUserEntity;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.CommonContentRenderer;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ISimpleResourceLookup;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingPipelineImpl;
import com.gridnode.gtas.client.web.renderers.SimpleResourceLookup;
import com.gridnode.gtas.client.web.renderers.URLRewriterImpl;
import com.gridnode.gtas.client.web.xml.IDocumentManager;

/**
 * Base class for Actions in presentation tier.
 */
public abstract class GTActionBase extends Action
{
  //Putting session key for timezone here instead of in ISessionKeys or wherever so that its in
  //the same places as the struts LOCALE_KEY
  //20030110AH
  public static final String TIMEZONE_KEY =
      "com.gridnode.gtas.client.web.strutsbase.GTActionBase.timeZone";

  /**
   * @deprecated - 20030306AH Specify navgroups using resolver mappings in navigation-config.xml
   */
  protected String getNavgroup(ActionContext actionContext)
    throws GTClientException
  { //20030306AH
    //@todo: remove this method & all subclass implementations of it
    throw new java.lang.UnsupportedOperationException("This method is obsolete");
  }

  /**
   * Look up and return the gridtalk session instance from the session context.
   * @param httpSession
   * @return gtSession
   */
  protected IGTSession getGridTalkSession(HttpSession httpSession)
  {
    return StaticWebUtils.getGridTalkSession(httpSession);
  }

  protected IGTSession getGridTalkSession(HttpServletRequest request)
  {
    return StaticWebUtils.getGridTalkSession(request);
  }

  protected IGTSession getGridTalkSession(ActionContext actionContext)
  {
    return StaticWebUtils.getGridTalkSession(actionContext);
  }

  protected IDocumentManager getDocumentManager()
  {
    ServletContext context = getServlet().getServletContext();
    return (IDocumentManager)context.getAttribute(IGlobals.DOCUMENT_MANAGER);
  }

  protected void setRenderingPipeline(HttpServletRequest request, IRenderingPipeline pipeline)
  {
    request.setAttribute(IRequestKeys.RENDERERS,pipeline);
  }

  protected ActionErrors getActionErrors(ServletRequest request)
  {
    return StaticWebUtils.getActionErrors(request);
  }

  protected Collection getAllRoles(IGTSession session) throws GTClientException
  {
    IGTRoleManager roleMgr = (IGTRoleManager)session.getManager(IGTManager.MANAGER_ROLE);
    return roleMgr.getAll();
  }

  protected Collection getUsersRoles(IGTSession session, IGTUserEntity user) throws GTClientException
  {
    IGTRoleManager roleMgr = (IGTRoleManager)session.getManager(IGTManager.MANAGER_ROLE);
    return roleMgr.getRolesForUser(user);
  }

  protected Collection getUsersRolesUids(IGTSession session, IGTUserEntity user) throws GTClientException
  {
    Collection userRoles = getUsersRoles(session, user);
    ArrayList roleUids = new ArrayList(userRoles.size());
    Iterator i = userRoles.iterator();
    while(i.hasNext())
    {
      IGTRoleEntity role = (IGTRoleEntity)i.next();
      roleUids.add(role.getFieldString(IGTRoleEntity.UID));
    }
    return roleUids;
  }

  protected ISimpleResourceLookup createResourceLookup(ActionContext actionContext)
  {
    Locale locale = getLocale(actionContext.getRequest());
    MessageResources messageResources = getResources(actionContext.getRequest());
    return new SimpleResourceLookup(locale, messageResources);
  }

  /**
   * Get an instance of a CommonContentRenderer.
   * This is used for rendering content common to most pages, such as the GridTalk title, and
   * the info in the status bar. Actions should call this method and add the returned renderer to
   * their rendering pipeline to make use of this functionality.
   * The implementation here will return an instance of CommonContentRenderer however
   * subclass may override this to return any renderer they choose, or null, so
   * actions that call this method should be prepared to handle this (esp. null).
   * @param actionContext
   * @param renderingContext
   * @return IDocumentRenderer
   * @throws GTClientException
   */
  protected IDocumentRenderer getCommonContentRenderer( ActionContext actionContext,
                                                        RenderingContext rContext)
    throws GTClientException
  {
    IGTSession gtasSession = null;
    try
    {
      gtasSession = this.getGridTalkSession(actionContext);
    }
    catch(NoSessionException nse)
    {
      
    }
    CommonContentRenderer renderer = new CommonContentRenderer(rContext, gtasSession, actionContext.getMapping()); //20030312AH
    return renderer;
  }

  /**
   * Convienience call that wraps the call to getCommonContentRenderer and the call to add this to
   * the pipeline as well as the check for null. !Must be using the RenderingPipeLineImpl
   * or subclass of this).
   * @param actionContext
   * @param renderingPipeline
   * @throws GTClientException
   */
  protected void addCommonContentRenderer(ActionContext actionContext,
                                          RenderingContext rContext,
                                          RenderingPipelineImpl rPipe)
    throws GTClientException
  {
    IDocumentRenderer ccr = getCommonContentRenderer(actionContext, rContext);
    if(ccr != null) rPipe.addRenderer(ccr);
  }

  protected void dumpRequest(ActionContext actionContext)
  {
    Log log = LogFactory.getLog(this.getClass());
    StaticWebUtils.logRequestDetails(actionContext.getRequest(), log); //20030425AH
  }

  protected ActionError getActionErrorForThrowable( Throwable throwable )
  {
    Throwable rootException = null;
    if(throwable == null)
    {
      return new ActionError("gtas.error.null");
    }
    else
    {
      rootException = StaticWebUtils.getRootException(throwable);
    }
    if(rootException instanceof ResponseException)
    {
      ResponseException e = (ResponseException)rootException;
      return new ActionError("gtas.error." + e.getErrorCode(),e.getErrorParams());
    }
    else if(rootException instanceof NoSessionException)
    {
      return new ActionError("gtas.error.noSession");
    }
    else if(rootException instanceof InvalidOperationContextException)
    {
      return new ActionError("gtas.error.invalidOperationContext");
    }
    else
    {
      return new ActionError("gtas.error.unknown");
    }
  }

  /*
   * Convienience method to create an action error for the throwable and store it in the action errors
   * object (creating it if necessary) and storing the throwable in the request attributes, where the
   * common content renderer will find it once it notices the action error.
   * @param actionContext
   * @param throwable
   * @param storeActionError
   */
  protected void saveExceptions(ActionContext actionContext, Throwable throwable, boolean storeActionError)
  { //20030606AH
    if(storeActionError)
    {
      ActionErrors errors = getActionErrors(actionContext.getRequest());
      if(errors == null)
      {
        errors = new ActionErrors();
      }
      storeThrowableDetails(actionContext, errors, throwable); //implies call to saveExceptions(ac, t)
      saveErrors(actionContext.getRequest(), errors); 
    }
    else
    {
      saveExceptions(actionContext, throwable);
    }
  }

  protected void saveExceptions(ActionContext actionContext, Throwable throwable)
  {
    HttpServletRequest request = actionContext.getRequest();
    Throwable rootEx = StaticWebUtils.getRootException(throwable);
    request.setAttribute(IRequestKeys.REQUEST_EXCEPTION, throwable);
    request.setAttribute(IRequestKeys.REQUEST_ROOT_EXCEPTION, rootEx );
  }

  protected void storeThrowableDetails(ActionContext actionContext, ActionErrors actionErrors, Throwable t)
  {
    ActionError error = this.getActionErrorForThrowable(t);
    actionErrors.add(IGlobals.EXCEPTION_ERROR_PROPERTY, error);
    saveExceptions(actionContext,t);
  }

  protected RenderingContext createRenderingContext(ActionContext actionContext, OperationContext opContext)
    throws GTClientException
  {
    ISimpleResourceLookup rLookup = createResourceLookup(actionContext);
    ActionErrors actionErrors = getActionErrors(actionContext.getRequest());
    IDocumentManager docMgr = getDocumentManager();

    IURLRewriter urlRewriter = new URLRewriterImpl( actionContext.getResponse(),
                                                    actionContext.getRequest().getContextPath(),
                                                    opContext);

    RenderingContext rContext = new RenderingContext( opContext,
                                                      docMgr,
                                                      rLookup,
                                                      urlRewriter,
                                                      actionErrors,
                                                      actionContext.getRequest(),
                                                      getServlet().getServletContext());
    return rContext;
  }

  protected Locale getLocale(HttpServletRequest request)
  { //20030110AH
    //Override struts method to ensure consistency with StaticWebUtils.
    //They SHOULD be doing the same thing. If not its a problem and this way we might
    //realise it sooner
    return StaticWebUtils.getLocale(request);
  }

  protected void setLocale(HttpServletRequest request, Locale locale)
  { //20030110AH
    //Override struts method to ensure consistency with StaticWebUtils.
    //They SHOULD be doing the same thing. If not its a problem and this way we might
    //realise it sooner
    StaticWebUtils.setLocale(request,locale);
  }

  protected Locale getLocale(ActionContext actionContext)
  { //20030110AH
    return getLocale(actionContext.getRequest());
  }

  protected void setLocale(ActionContext actionContext, Locale locale)
  { //20030110AH
    setLocale(actionContext.getRequest(),locale);
  }

  protected TimeZone getTimeZone(ActionContext actionContext)
  { //20030110AH
    return StaticWebUtils.getTimeZone(actionContext.getRequest());
  }

  protected void setTimeZone(ActionContext actionContext, TimeZone timeZone)
  { //20030110AH
    StaticWebUtils.setTimeZone(actionContext.getRequest(), timeZone);
  }

  protected String getProcessedPath(ActionContext actionContext)
  { //20030306AH
    return (String)actionContext.getRequest().getAttribute(GTRequestProcessor.PROCESSED_PATH);
  }
  
  /*
   * Convienience method to get path specified in an actionForward
   * If forwardName is null a NullPointerException is thrown.
   * @param actionContext
   * @param forwardName
   * @return forwardPath or null if forward not found
   */
  protected String getActionForwardPath(ActionContext actionContext, String forwardName)
  { //20030423AH
    if (actionContext == null)
			throw new NullPointerException("actionContext is null");
    return getActionForwardPath(actionContext.getMapping(), forwardName);
  }

  /*
   * Convienience method to get path specified in an actionForward
   * If forwardName is null a NullPointerException will be thrown
   * @param actionMapping
   * @param forwardName
   * @return forwardPath or null if forward not found
   */
	private String getActionForwardPath(ActionMapping actionMapping, String forwardName)
	{ //20030423AH
    if (actionMapping == null)
			throw new NullPointerException("actionMapping is null");
    if (forwardName == null)
			throw new NullPointerException("forwardName is null");
    ActionForward fwd = actionMapping.findForward(forwardName);
    return fwd == null ? null : fwd.getPath();
	}

}