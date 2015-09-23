/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTRequestProcessor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-10     Andrew Hill         Created
 * 2002-11-05     Andrew Hill         Override processLocale()
 * 2003-01-24     Andrew Hill         Modified processActionForm() for opCon forms
 *                                    Support populate property in GTActionMapping
 * 2003-03-05     Andrew Hill         Store result of processPath in request attribute
 * 2003-04-10     Andrew Hill         Override doForward() with latest from struts cvs
 * 2003-04-25     Andrew Hill         Extra logging to aid diagnostics
 * 2003-05-02     Andrew Hill         Implement requiresGtSession check
 * 2003-07-01     Andrew Hill         Remove doForward() override as we have upgraded to struts 1.1 final release :-)
 * 2003-07-09     Andrew Hill         Ugly 'inline exception' code (very qad!)
 * 2005-03-17     Andrew Hill         Check for admin rights where specified in GTActionMapping
 * 2006-04-24     Neo Sok Lay         Check for P2P and UDDI requirement specified in GTActionMapping
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;

public class GTRequestProcessor extends RequestProcessor
{
  private static final Log _log = LogFactory.getLog(GTRequestProcessor.class); // 20031209 DDJ

  public static final String PROCESSED_PATH = GTRequestProcessor.class.getName() + ".processedPath"; //20030305AH, 20030709AH
  public static final String INLINE_EXCEPTION = "inlineException"; //20030709AH

  private static long _inlineExceptionNumber = 0; //20030709AH
  
  /**
   * Retrieve and return the <code>ActionForm</code> bean associated with
   * this mapping, creating and stashing one if necessary.  If there is no
   * form bean associated with this mapping, return <code>null</code>.
   * This subclass of RequestProcessor will also search for ActionForm in an
   * OperationContext object stored in session scope under an attribute identified
   * by OperationConstant.ID_PARAMETER.
   * Currently only one ActionForm is supported at a time by this method.
   * This search is done first and if it fails to find an action form, the normal
   * superclass behaviour will be followed.
   * 20030124: The behaviour has been modified slightly - it seems there was a problem with opCon
   * stored forms in that the first request, not having an opCon yet would cause the handling to
   * be delegated to struts, which if a name was specified in the actionMapping would create and
   * store an actionForm in the scope. We state scope=session for opCon forms - its only half
   * true (it is in the session just nested a bit ;->) so opCon forms ended up being given a
   * shared reference in the session. Future requests for this type of form would thus return the
   * same instance leading to concurrency issues.
   * A property has been added to the GTActionMapping named 'deferFormCreation'.
   * For forms in opCon it is therefore necessary to specify this property in the mapping as true
   * in order to avoid such problems.
   * If this mapping is specified for non-opCon forms, it results in the form never being
   * retrieved by this method thus leaving it to the action to manage that on its own.
   * For opCon forms, the form will be retrieved on subsequent requests related to that opCon.
   * Its creation is the responsibility of the Action however. A side effect of this to be aware of
   * is that if the form hasnt been supplied by the RequestProcessor then it can hardly be expected
   * to be populated either... This also is the responsibility of the action for opCon forms.
   * (Part of the price to be paid for the usefullness of opCons!)
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   * @param mapping The mapping we are using
   */
  protected ActionForm processActionForm(HttpServletRequest request,
                                         HttpServletResponse response,
                                         ActionMapping mapping)
  {
    String scope = mapping.getScope();
    if(scope != null)
    {
      try
      {
        if(scope.equals("session"))
        {
          OperationContext opCon = OperationContext.getOperationContext(request);
          if(opCon != null)
          {
            if(_log.isInfoEnabled())
            {
              _log.info("Looking for ActionForm in OperationContext[id=" + opCon.getOperationContextId() + "]"); //20030124AH
            }
            ActionForm form = opCon.getActionForm();
            if(form != null)
            {
              if(_log.isInfoEnabled())
              {
                _log.info("Found ActionForm in OperationContext"); //20030124AH
              }
              return form;
            }
            else
            {
              if(_log.isWarnEnabled())
              {
                _log.warn("No ActionForm found in OperationContext"); //20030124AH
              }
              return null; //20030124AH
            }
          }
        }
      }
      catch(OperationException e)
      {
         // OperationExceptions here are normal when we forward from a completed operation
          // to the next action, so we ignore them.
      }
      catch(Throwable t)
      {
        if(_log.isErrorEnabled())
        {
          _log.error("Error caught in processActionForm() performing customised ActionForm handling", t); //20030124AH
        }
      }
    }

    if(mapping instanceof GTActionMapping)
    { //20030124AH - Check the mapping to see if we should defer form creation or do it as normal
      if( ((GTActionMapping)mapping).isDeferFormCreation() )
      {
        if(_log.isInfoEnabled())
        {
          _log.info("Deferring ActionForm creation");
        }
        return null;
      }
    }
    //...
    if(_log.isInfoEnabled())
    {
      _log.info("Delegating to superclass to obtain ActionForm"); //20030124AH
    }
    ActionForm form = super.processActionForm(request, response, mapping);
    return form;
  }

  /*
   * Returns true if this is a multipart form and the request is a POST
   * @param request
   * @return isPostedMultipartRequest
   *//*
  private boolean isMultipartRequest(HttpServletRequest request)
  {
    String contentType = request.getContentType();
    return ( (contentType != null)
          && (contentType.startsWith("multipart/form-data"))
          && (request.getMethod().equalsIgnoreCase("POST")));
  }*/

  protected void processLocale(HttpServletRequest request, HttpServletResponse response)
  {
    //20021105AH - Modified from original struts code to allow changing of locale on a per-request
    //basis. Nb that this adds a little overhead to each request, but not much.

    //20030124AH@todo - IE has problem with locale submission (accept-language header) when the
    //regional settings have been played with. Sometimes it sends the choice in ie, and sometimes
    //it sends the setting in regional settings... Need a workaround. May have to abandon locale
    //switching on a per-request basis. :-(

    // Are we configured to select the Locale automatically?
    if (!moduleConfig.getControllerConfig().getLocale()) //20030701AH - Use moduleConfig
    {
      return;
    }
    // Use the Locale returned by the servlet container (if any)
    HttpSession session = request.getSession();
    Locale locale = request.getLocale();
    Locale oldLocale = (Locale)session.getAttribute(Globals.LOCALE_KEY); //20030701AH - Use Globals.LOCALE_KEY
    if (locale != null)
    {
      if(locale.equals(oldLocale))
      {
        return;
      }
      else
      { // Only set locale in session if its changed
        if (_log.isDebugEnabled()) _log.debug("Setting user locale '" + locale + "'");
        //session.setAttribute(Action.LOCALE_KEY, locale);
        StaticWebUtils.setLocale(request,locale); //20030110AH
      }
    }

  }

  /**
   * This has been overridden to check the populate property if using a GTActionMapping
   * and if false to not populate the form. The prime reason for the existence of this property
   * is to provide support for the RenderingAction which is 'chained' from the Action.
   * Struts best practise discourages use of Action chaining, however the RenderingAction has
   * a good excuse - its the view component and the form shouldnt be repopulated when it is
   * invoked!
   * You shouldnt use action chaining as a methodology for action processing however as
   * its a bad idea. If you think you need to, think very hard about it first, as its perceived
   * need is most likely a flaw in your logic rather than a real need...
   * 20030124AH
   */
  protected void processPopulate(HttpServletRequest request,
                                   HttpServletResponse response,
                                   ActionForm form,
                                   ActionMapping mapping)
        throws ServletException
  {
    if(form != null)
    {
      if(mapping instanceof GTActionMapping)
      { //20030124AH
        if( ((GTActionMapping)mapping).isPopulate() == false )
        {
          return; //Do not pass go. Do not collect $200.
        }
      }
      super.processPopulate(request,response,form,mapping);
    }
    else
    {
      return; //No form to populate
    }
  }


  /*
   * Overridden to allow us to log some extra info on performance which is also helpful in diagnosing
   * problems. Delegates to superclass to do the actual work.
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */
  public void process(HttpServletRequest request,
                      HttpServletResponse response)
    throws IOException, ServletException
  {
    if(_log.isInfoEnabled())
    {
      _log.info("Started processing action request: " + request.getServletPath());
    }
    long startTime = System.currentTimeMillis();
    
    processInlineException(request);
    super.process(request, response);
    
    long endTime = System.currentTimeMillis();
    if(_log.isInfoEnabled())
    {
      _log.info("Completed processing action request (in approx " + (endTime - startTime) + " ms)");
      OperationContext.logActiveContexts(request, _log);
    }
  }
  
  private void processInlineException(HttpServletRequest request)
  { //20030709AH - @todo Refactor less ugly somehow
    HttpSession session = request.getSession(false);
    if(session != null)
    {
      String key = getInlineExceptionKey(request, false);
      if (key != null)
      {
        Throwable t = (Throwable)session.getAttribute(key);
        if(t != null)
        {
          //request.setAttribute(IGlobals.EXCEPTION_ERROR_PROPERTY, t);
          session.removeAttribute(INLINE_EXCEPTION);
          request.setAttribute(IRequestKeys.REQUEST_EXCEPTION, t);
          Throwable rootEx = StaticWebUtils.getRootException(t);
          request.setAttribute(IRequestKeys.REQUEST_ROOT_EXCEPTION, rootEx);
        }
      }
    }
  }
  
  public static String getInlineExceptionKey(HttpServletRequest request, boolean newKey)
  { //20030709AH
    if(newKey)
    {
      synchronized(GTRequestProcessor.class)
      {
        String key = INLINE_EXCEPTION + _inlineExceptionNumber++;
        return key;
      }
    }
    else
    {
      String param = request.getParameter(INLINE_EXCEPTION);
      return param;
    }
  }

    
  /*
   * Will delegate to superclass implementation to get the path, but before returning it
   * will for our convienience put the returned String into a request attribute under the
   * key PROCESSED_PATH so that we can easily make further use of it later while processing
   * the action.
   * @param request
   * @param response
   * @return path
   * @throws IOException
   */
  protected String processPath(HttpServletRequest request,
                                 HttpServletResponse response)
      throws IOException
  { //20030305AH - Store result of processPath() in request attribute so can use it later for
    //looking up stuff - such as in navigationRenderer...
    String path = super.processPath(request, response);
    request.setAttribute(PROCESSED_PATH, path);
    return path;
  }

  /*
   * Will delegate to the superclass to process the mapping, but before returning it to
   * the caller will check to see if it is of the class GTActionMapping, and if so will then
   * check if its logParameters property is set, and if so will call the logRequestDetails method
   * in StaticWebUtils.
   * @param request
   * @param response
   * @return actionMapping
   * @throws IOException
   */
  protected ActionMapping processMapping( HttpServletRequest request,
                                          HttpServletResponse response,
                                          String path)
    throws IOException
  { //20030425AH
    ActionMapping mapping = super.processMapping(request, response, path);
    if(mapping instanceof GTActionMapping)
    {
      if( ((GTActionMapping)mapping).isLogParameters() )
      {
        StaticWebUtils.logRequestDetails(request, _log);
      }
    }
    return mapping;
  }

  /**
   * Ask the specified <code>Action</code> instance to handle this
   * request.  Return the <code>ActionForward</code> instance (if any)
   * returned by the called <code>Action</code> for further processing.
   * Will also check to see if a GTAS Session is available if specified in the mapping.
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   * @param action The Action instance to be used
   * @param form The ActionForm instance to pass to this Action
   * @param mapping The ActionMapping instance to pass to this Action
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  protected ActionForward processActionPerform(HttpServletRequest request,
                                               HttpServletResponse response,
                                               Action action,
                                               ActionForm form,
                                               ActionMapping mapping)
    throws IOException, ServletException
  { //20030502AH - Copied from struts src, with extra code added 
    try
    {
      if(mapping instanceof GTActionMapping)
      {
        //20050317AH - mod to also check for asdmin rights where spec'd in mapping
        GTActionMapping gtMapping = (GTActionMapping)mapping;
        boolean needsSession = gtMapping.isRequiresGtSession() ||gtMapping.isRequiresAdmin() 
        												|| gtMapping.isRequiresP2P() || gtMapping.isRequiresUDDI();
        if( needsSession )
        {
          //We will try to obtain a reference to the IGTSession using the method provided
          //in StaticWebUtils. This will throw a NoSessionException if we have no IGTSession.
          //By putting this check here it will be captured as though it was thrown by the action.
          IGTSession gtasSession = StaticWebUtils.getGridTalkSession(request);
          //NSL20060424 Check for P2P and UDDI requirement
          if (gtMapping.isRequiresP2P() && gtasSession.isNoP2P())
          {
          	throw new IllegalStateException("P2P functionality access required for mapped path "+gtMapping.getPath());
          }
          if (gtMapping.isRequiresUDDI() && gtasSession.isNoUDDI())
          {
          	throw new IllegalStateException("UDDI functionality access required for mapped path "+gtMapping.getPath());
          }
          if(gtMapping.isRequiresAdmin() && !gtasSession.isAdmin())
          {
            throw new IllegalStateException("Admin access required for mapped path " + gtMapping.getPath());
          }
        }
      }
      return (action.execute(mapping, form, request, response));
    }
    catch (Exception e)
    {
      return (processException(request, response,e, form, mapping));
    }
  
  }
}



