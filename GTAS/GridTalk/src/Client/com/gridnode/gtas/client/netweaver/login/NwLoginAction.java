/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-20     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         Feed the session factory a global context
 * 2002-10-30     Andrew Hill         Use new error handling to display unexpected errors
 * 2002-11-06     Andrew Hill         Create LogoutListener
 * 2002-12-02     Andrew Hill         Forward to rego page if not registered
 * 2003-04-23     Andrew Hill         Check if bTier knows pcp (privateCertPassword)
 * 2003-04-25     Andrew Hill         Prompt for pcp if necessary
 * 2003-09-01     Daniel D'Cotta      Fix GNDB00014982 where uppercase UserId causes
 *                                    problems with the directory name of the temp folder
 * 2003-11-05     Andrew Hill         Init the noSecurity option based on value in action mapping
 */
package com.gridnode.gtas.client.netweaver.login;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.GTSessionFactory;
import com.gridnode.gtas.client.ctrl.GlobalContext;
import com.gridnode.gtas.client.ctrl.IGTHostInfo;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.LoginException;
import com.gridnode.gtas.client.ctrl.SessionCreationException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.ISessionKeys;
import com.gridnode.gtas.client.web.login.LoginAForm;
import com.gridnode.gtas.client.web.login.LoginActionMapping;
import com.gridnode.gtas.client.web.login.LogoutListener;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.pdip.framework.util.AssertUtil;

public class NwLoginAction extends GTActionBase
{

  public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping,form,request,response);
    try
    {
      Log log = actionContext.initLog(this.getClass());
      boolean failed = false;
      boolean checkPcpKnown = false; //20030425AH
      if(mapping instanceof LoginActionMapping)
      { //20030425AH - Check config as to whether to prompt or not
         checkPcpKnown = ((LoginActionMapping)mapping).isPromptPcp();
      }
      // Extract attributes we will need
      Locale locale = getLocale(request);
      MessageResources messages = getResources(request);

      // Validate the request parameters specified by the user
      ActionErrors errors = new ActionErrors();
      String username = ((LoginAForm)form).getUsername();
      String password = ((LoginAForm)form).getPassword();
      if ((username == null) || (username.length() < 1))
      {
        errors.add("username", new ActionError("error.username.required"));
        failed = true;
      }
      else
      {
        // 01092003 DDJ: This is to fix GNDB00014982 where uppercase causes
        //               problems with the directory name of the temp folder
        username = username.toLowerCase();
      }
      if ((password == null) || (password.length() < 1))
      {
        errors.add("password", new ActionError("error.password.required"));
        failed = true;
      }
      IGTSession gtasSession = null;
      if(!failed)
      {
        try
        {
          GlobalContext globalContext = GlobalContext.getInstance();
          gtasSession = GTSessionFactory.getSession(IGTSession.SESSION_DEFAULT, globalContext);
          
          if(mapping instanceof LoginActionMapping)
          { //20031105AH - HACK for GNDB00016109
             gtasSession.setNoSecurity( ((LoginActionMapping)mapping).isNoSecurity() );
          }
          
          IGTHostInfo host = null;
          if(log.isInfoEnabled())
          {
            log.info("Attempting to create IGTSession for user '" + username + "'");
          }
          gtasSession.login(host, username, password.toCharArray(), checkPcpKnown);
        }
        catch(SessionCreationException sessionCreationException)
        {
          sessionCreationException.printStackTrace();
          errors.add(IGlobals.EXCEPTION_ERROR_PROPERTY, new ActionError("error.login.sessionException"));
          saveExceptions(actionContext, sessionCreationException);
          if(log.isErrorEnabled())
          {
            log.error("Error attempting to login",sessionCreationException);
          }
          failed = true;
        }
        catch(LoginException loginException)
        {
          switch(loginException.getType())
          {
            case LoginException.INVALID_LOGIN:
              errors.add("login", new ActionError("error.login.invalid"));
              failed = true;
              break;
              
            case LoginException.NO_PRIVATE_CERT_PASSWORD: //20030423AH
              // This might occur if they submit to the action without going through the
              // PreLoginAction - for example leaving open the login screen while restarting the
              // server. By forwarding back to the input we insure that they will be forced to
              // enter the security password.
              errors.add("securityPassword", new ActionError("getCertPw.error.securityPassword.required"));
              failed = true;
              break;

            case LoginException.SERVER_ERROR:
              errors.add(IGlobals.EXCEPTION_ERROR_PROPERTY, new ActionError("error.login.server"));
              saveExceptions(actionContext, loginException);
              if(log.isErrorEnabled())
              {
                log.error("Error attempting to login",loginException);
              }
              failed = true;
              break;
          }
        }
      }
      // Report any errors we have discovered back to the original form
      if (failed)
      {
        saveErrors(request, errors);
        return (new ActionForward(mapping.getInput()));
      }
      else
      {
        // Save our gtasSession in the session
        HttpSession session = request.getSession();
        LogoutListener.setupListener(session,gtasSession); //20021106AH timeout listener
        session.setAttribute(ISessionKeys.GTAS_SESSION, gtasSession);
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null)
        {
          if ("request".equals(mapping.getScope()))
          {
            request.removeAttribute(mapping.getAttribute());
          }
        }

        //20030110AH - Save selected TimeZone in the session
        String timeZoneId = ((LoginAForm)form).getTimeZone();
        TimeZone timeZone = null;
        if( StaticUtils.stringNotEmpty(timeZoneId) )
        {
          timeZone = TimeZone.getTimeZone( timeZoneId );
        }
        else
        {
          timeZone = TimeZone.getDefault();
        }
        setTimeZone(actionContext,timeZone);
        //...


//        //20030317AH - No longer choose initial page here - this is done in InitialPageAction
//        //due to the use of frames
//        ActionForward forward = mapping.findForward("success");
//        if(forward == null) throw new NullPointerException("Couldnt find 'success' ActionForward");
////        String path = forward.getPath() + "&username=" + username + "&password=" + password;
////        log("path = " + path);
////        forward.setPath(path);
//        
//        /* set username and password for inbound page */
//        session.setAttribute("nw_username", username);
//        session.setAttribute("nw_password", password);
//        return forward;
        
        String nwForward = request.getParameter("nw_forward");
        ActionForward forward = mapping.findForward(nwForward);
        AssertUtil.assertTrue(forward != null);
        return forward;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unexpected error caught in Login action",t);
    }
  }
  
  private void log(String message)
  {
  	com.gridnode.gtas.client.netweaver.helper.Logger.debug("[NwLoginAction]" + message);
  }
}

