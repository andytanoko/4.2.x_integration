/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCertPwRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-23     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.login;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.*;

public class GetCertPwRenderer extends AbstractRenderer implements IDocumentRenderer
{
  private String _submitURL;
  private GetCertPwAForm _form;

  public GetCertPwRenderer( RenderingContext rContext,
                             GetCertPwAForm form,
                             String submitURL)
  {
    super( rContext );
    _submitURL = submitURL;
    _form = form;
  }

  protected void render() throws RenderingException
  { 
    try
    {
      RenderingContext rContext = getRenderingContext();
      IURLRewriter urlRewriter = rContext.getUrlRewriter();
      GetCertPwAForm form = _form;
      
      includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS);
      
      renderLabel("getCertPw_heading","getCertPw.heading",false);
      renderLabel("getCertPw_message","getCertPw.message",false);
      renderLabel("securityPassword_label","getCertPw.securityPassword",false);
      renderElementAttributeFromKey("getCertPw","value","getCertPw.submit");
      renderLabel("title","gridtalk.title",false);
      renderElementAttribute("getCertPw_form","action",urlRewriter.rewriteURL(_submitURL));
      renderElementAttribute("securityPassword","value","");
      renderLabel("title","gridtalk.title.window",false);

      ActionErrors actionErrors = rContext.getActionErrors();
      renderFieldError(actionErrors,"securityPassword");
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering getCertPw page",t);
    }
  }
  
}