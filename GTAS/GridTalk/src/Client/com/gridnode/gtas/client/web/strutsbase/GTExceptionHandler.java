/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTExceptionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-29     Andrew Hill         Created
 * 2002-10-30     Andrew Hill         Log the exceptions using commons logging
 * 2003-02-19     Andrew Hill         Return to login page for NoSessionException
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.ExceptionRenderingExceptionException;

public class GTExceptionHandler extends ExceptionHandler
{
  private static final Log _log = LogFactory.getLog(GTExceptionHandler.class); // 20031209 DDJ
  
  public ActionForward execute(Exception ex,
                               ExceptionConfig ae,
                               ActionMapping mapping,
                               ActionForm formInstance,
                               HttpServletRequest request,
                               HttpServletResponse response)
    throws ServletException
  {
    try
    {
      Throwable rootEx = StaticWebUtils.getRootException(ex);
      if(_log.isErrorEnabled())
      {
        _log.error("Error passed to ExceptionHandler", ex);
        if(ex instanceof ServletException)
        {
          _log.error("Root exception of passed ServletException", rootEx);
        }
      }

      if(ex instanceof ExceptionRenderingExceptionException)
      {
        throw new GTClientException("ExceptionDisplayAction failed to render an exception", ex);
      }

      request.setAttribute(IRequestKeys.REQUEST_EXCEPTION, ex );
      request.setAttribute(IRequestKeys.REQUEST_ROOT_EXCEPTION, rootEx );
      ActionForward forward = null; //20030219AH
      if(rootEx instanceof NoSessionException)
      { //20030219AH
        forward = mapping.findForward("reLogin");
        if(forward == null) throw new java.lang.NullPointerException("Couldn't find forward:reLogin");
        String fwdPath = StaticWebUtils.addParameterToURL(forward.getPath(),"isTimeout","true");
        forward = new ActionForward(fwdPath, forward.getRedirect());
      }
      else
      {
        forward = mapping.findForward("exception");
        if(forward == null) throw new java.lang.NullPointerException("Couldn't find forward:exception");
      }
      return forward;
    }
    catch(Throwable t)
    {
      t.printStackTrace();
      if(ex != null)
      {
        try
        {
          PrintWriter pWriter = response.getWriter();
          pWriter.write("Unable to forward to DisplayExceptionAction:<br/>");
          StaticWebUtils.printStackTrace(ex, pWriter);
          if(ex instanceof ExceptionRenderingExceptionException)
          {
            Throwable original = ((ExceptionRenderingExceptionException)ex).getOriginal();
            pWriter.write("Original error being displayed:<br/>");
            StaticWebUtils.printStackTrace(original, pWriter);
          }
        }
        catch(Throwable crikey)
        {
          //Give in & die already....
          crikey.printStackTrace();
        }
      }
      return null;
    }
  }
}



