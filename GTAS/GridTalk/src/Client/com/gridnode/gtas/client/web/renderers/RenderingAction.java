/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RenderingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-29     Andrew Hill         Created
 * 2002-11-05     Andrew Hill         Set Content-Language header
 * 2002-12-10     Andrew Hill         Throw GTClientException
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class RenderingAction extends Action
{
  public static final String CLASSNAME = "com.gridnode.gtas.client.web.renderers.RenderingAction";
  //Doing this in an action instead of a servlet allows us to
  //leverage the exception handler we set up and other code that actions get (like getlocale)
  public ActionForward execute(ActionMapping mapping,
				                        ActionForm form,
				                        HttpServletRequest request,
				                        HttpServletResponse response)
	                              throws Exception
  {
    try
    {
      ActionContext actionContext = new ActionContext(mapping,form,request,response);
      Locale locale = getLocale(request);
      Log log = actionContext.initLog(this.getClass());
      //response.setContentType("text/html; charset=UTF-8");
      response.setHeader("Content-Language", locale.getLanguage());
      RenderingUtils.renderPipeline(response.getWriter(), StaticWebUtils.getRenderingPipeline(request));
      return null;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error in RenderingAction",t);
    }
  }

}

