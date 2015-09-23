/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PreLoginAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-28     Andrew Hill         Created
 * 2002-10-31     Andrew Hill         Change renderers used for new login look
 * 2003-01-13     Andrew Hill         Render getGmtOffset page if required
 * 2003-02-19     Andrew Hill         Track isTimeout flag
 * 2003-04-23     Andrew Hill         Check if bTier knows pcp (privateCertPassword)
 * 2003-04-25     Andrew Hill         Prompt for pcp if necessary
 */
package com.gridnode.gtas.client.netweaver.login;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.ISessionKeys;
import com.gridnode.gtas.client.web.login.GetCertPwAction;
import com.gridnode.gtas.client.web.login.GmtOffsetRenderer;
import com.gridnode.gtas.client.web.login.LoginAForm;
import com.gridnode.gtas.client.web.login.LoginActionMapping;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.StaticWebUtils;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Action class to assemble the renderers to render the login screen.
 * Will also hanlde the case of redisplaying the login screen with appropriate error messages
 * when the LoginAction or LoginAForm validation fails and forwards back here.
 */
public class NwPreLoginAction extends GTActionBase
{

  public ActionForward execute(ActionMapping mapping,
				                        ActionForm form,
				                        HttpServletRequest request,
				                        HttpServletResponse response)
	                              throws Exception
  {
    
    if(mapping instanceof LoginActionMapping)
    { //20030425AH - Check config as to whether to prompt or not
      if( ((LoginActionMapping)mapping).isPromptPcp() )
      {
        //20030423AH - We need to check if the GTAS has been given the private cert password yet.
        //If not we need to get it - which we will do by forwarding to a different screen to prompt
        //for it. We will (for now) use a throwaway gtasSession to check with
        GlobalContext globalContext = GlobalContext.getInstance();
        IGTSession gtasSession = GTSessionFactory.getSession(IGTSession.SESSION_DEFAULT, globalContext);
        if(!gtasSession.isPrivateCertificatePasswordKnown())
        {
          ActionForward getCertPwFwd = mapping.findForward(GetCertPwAction.FWD_MAPPING);
          if (getCertPwFwd == null)
    				throw new NullPointerException("getCertPwFwd is null");
          return getCertPwFwd;
        }
        else
        {
          gtasSession = null; //make available for gc immediately
        }
      }
     }
    
    //20030113AH - gmt stuff
    //We need to know what the clients current gmt offset is so we can narrow down the list
    //candidate timezones for the to choose on the login screen. We need to do this with javascript
    //The getGmtOffset.html contains the necessary script and submits it in the hidden
    //gmtOffset field. We will check our action form to see if we have this value already. If not
    //we will not render the login screen. We will instead render the getGmtOffset screen which
    //will submit this value back to this PreLoginAction. Once we have the value we can proceed
    //as normal.
    //(This could also have been implemented as a seperate action or by modifying the redirection
    //page to do it. btw: the getGmtOffset page will submit using a get so that the parameter
    //is tacked to the end of the login pages url afterwards - which the user can hit refresh for
    //(unlike many screens!)
    //...

    ActionContext actionContext = new ActionContext(mapping,form,request,response);
    Locale locale = getLocale(request);

    // Get document manager for use by renderers to find and parse xhtml documents into doms
    IDocumentManager docMgr = getDocumentManager();
    if(docMgr == null) throw new java.lang.NullPointerException("No document manager found");
    MessageResources messageResources = getResources(request);
    boolean isTimeout = isTimeout(request); //20030219AH
    ActionErrors actionErrors = getActionErrors(request);
    if(form == null) form = new LoginAForm(); //hmmmm

    String gmtOffset = ((LoginAForm)form).getGmtOffset();
    boolean haveGmtOffset = StaticUtils.stringNotEmpty(gmtOffset);
    String submitUrl = null;
    String target; //target dom
    if(haveGmtOffset)
    { //20030113AH
      // Lookup path to which login form should be submitted by browser
      submitUrl = mapping.findForward("submit").getPath();
      target = IDocumentKeys.LOGIN;
    }
    else
    {
      // Lookup path to which gmt form should be submitted by browser
      submitUrl = mapping.findForward("submitGmt").getPath();
      target = IDocumentKeys.GMT_OFFSET;
    }

    // Create a simple resource lookup object to resolve 118n keys into associated messages
    ISimpleResourceLookup rLookup = new SimpleResourceLookup(locale, messageResources);

    IURLRewriter urlRewriter = new URLRewriterImpl(response, request.getContextPath());
    RenderingContext rContext = new RenderingContext( null,
                                                      docMgr,
                                                      rLookup,
                                                      urlRewriter,
                                                      actionErrors,
                                                      request,
                                                      getServlet().getServletContext());
    BaseTagRenderer baseTagRenderer = new BaseTagRenderer(request, "base_tag");

    RenderingPipelineImpl pipeline = new RenderingPipelineImpl(target, docMgr);
    pipeline.addRenderer(baseTagRenderer);

    //20030113AH
    IDocumentRenderer pageRenderer = null;
    if(haveGmtOffset)
    {
      pageRenderer = new NwLoginRenderer(rContext,(LoginAForm)form,submitUrl);
    }
    else
    {
      pageRenderer = new GmtOffsetRenderer(rContext, (LoginAForm)form,submitUrl);
    }
    pipeline.addRenderer(pageRenderer);
    //....

    addCommonContentRenderer(actionContext, rContext, pipeline);
    setRenderingPipeline(request, pipeline);
    return mapping.findForward("view");
  }

  protected boolean isTimeout(HttpServletRequest request)
  { //20030219AH
    return StaticUtils.primitiveBooleanValue(request.getParameter("isTimeout"));
  }
}

