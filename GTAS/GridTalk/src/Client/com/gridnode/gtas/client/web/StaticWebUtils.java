/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StaticWebUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 * 2002-07-03     Andrew Hill         Added addParameterToURL() method
 * 2002-10-29     Andrew Hill         getRootException() & printStackTrace()
 * 2002-12-17     Andrew Hill         Basic browser detection utils
 * 2003-01-10     Andrew Hill         Locale & Timezone getters and setters
 * 2003-01-27     Andrew Hill         getDlhKey(), prepareFieldDlh()
 * 2003-04-25     Andrew Hill         logRequestDetails()
 * 2003-05-20     Andrew Hill         dumpActionErrors()
 * 2003-07-28     Andrew Hill         getLoggableRequestDetails()
 */
package com.gridnode.gtas.client.web;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.strutsbase.NoSessionException;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;

public class StaticWebUtils
{
  public static String addParameterToURL(String url, String parameter, String value)
  {
    if(url == null)
    {
      return parameter + "=" + value;
    }
    else
    {
      return url + ( (url.indexOf("?")==-1) ? "?" : "&" ) + parameter + "=" + value;
    }
  }

  public static IGTSession getGridTalkSession(HttpSession httpSession)
  {
    IGTSession gtasSession = (IGTSession)httpSession.getAttribute(ISessionKeys.GTAS_SESSION);
    if(gtasSession == null)
    {
      throw new NoSessionException("GTAS client session has not been created or has timed out");
    }
    return gtasSession;
  }

  public static IGTSession getGridTalkSession(HttpServletRequest request)
  {
    return getGridTalkSession(request.getSession());
  }

  public static IGTSession getGridTalkSession(ActionContext actionContext)
  {
    return getGridTalkSession(actionContext.getSession());
  }

  public static ActionErrors getActionErrors(ServletRequest request)
  {
    return (ActionErrors) request.getAttribute(Action.ERROR_KEY);
  }

  public static Throwable getRootException(Throwable t)
  {
    if(t instanceof GTClientException)
    {
      return ((GTClientException)t).getRootException();
    }
    else if(t instanceof ServletException)
    {
      Throwable t2 = ((ServletException)t).getRootCause();
      if( (t == t2) || (t2 == null) )
      {
        return t;
      }
      else
      {
        return getRootException(t2);
      }
    }
    else
    {
      return t;
    }
  }

  /**
   * Invoke the print stacktrace methods of an exception and its nested exceptions
   * if its a servlet exception (which doesnt normally print its root exceptions
   * trace)
   */
  public static void printStackTrace(Throwable t, PrintWriter writer)
  {
    if(t == null) writer.write("null");
    if(t instanceof ServletException)
    {
      t.printStackTrace(writer);
      Throwable t2 = ((ServletException)t).getRootCause();
      if( (t == t2) || (t2 == null) )
      {
      }
      else
      {
        printStackTrace(t2, writer);
      }
    }
    else
    {
      t.printStackTrace(writer);
    }
  }

  public static IRenderingPipeline getRenderingPipeline(HttpServletRequest request)
  {
    return (IRenderingPipeline)request.getAttribute(IRequestKeys.RENDERERS);
  }

  public static boolean isNetscape6(HttpServletRequest request)
  { //20021217AH
    //A note on browser detection. This is intended to remain basic and its use should be avoided
    //in favour of cross-browser compatible stuff wherever possible. This should only be used where
    //alternatives are very bad (such as the workaround for tabpanes in light of the NS6 display:none
    //submit bug)
    //Example:
    //Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; m18) Gecko/20001108 Netscape6/6.0
    String userAgent = request.getHeader("User-Agent");
    //return( ( userAgent.indexOf("Netscape") != -1) && (userAgent.indexOf("/6") != -1) );
    //todo: use above method but with something like lastIndexOf so as not to confuse with
    //the initial mozilla string
    return( ( userAgent.indexOf("Netscape6") != -1) );
  }

  public static boolean isMozillaRv(HttpServletRequest request)
  { //20021217AH
    //Will also return true for NS7...
    //Example:
    //Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.0.0) Gecko/20020530
    String userAgent = request.getHeader("User-Agent");
    return( (userAgent.indexOf("Mozilla") != -1)
            && (userAgent.indexOf("MSIE") == -1)
            && (userAgent.indexOf("rv") != -1) );
  }

  public static boolean isExplorer(HttpServletRequest request)
  { //20021217AH
    //Example:
    //Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)
    String userAgent = request.getHeader("User-Agent");
    return( (userAgent.indexOf("MSIE") != -1) );
  }

  public static Locale getLocale(HttpServletRequest request)
  { //20030110AH
    Locale locale = (Locale)request.getSession().getAttribute(GTActionBase.LOCALE_KEY);
    if (locale == null)
    {
        locale = Locale.getDefault();
    }
    return locale;
  }

  public static void setLocale(HttpServletRequest request, Locale locale)
  { //20030110AH
    if (locale == null)
    {
      locale = Locale.getDefault();
    }
    request.getSession().setAttribute(Action.LOCALE_KEY, locale);
  }

  public static TimeZone getTimeZone(HttpServletRequest request)
  { //20030110AH
    TimeZone timezone = (TimeZone)request.getSession().getAttribute(GTActionBase.TIMEZONE_KEY);
    if(timezone == null)
    {
        timezone = TimeZone.getDefault();
    }
    return timezone;
  }

  public static void setTimeZone(HttpServletRequest request, TimeZone timeZone)
  { //20030110AH
    if(timeZone == null)
    {
      timeZone = TimeZone.getDefault();
    }
    request.getSession().setAttribute(GTActionBase.TIMEZONE_KEY, timeZone);
  }

  public static String getDlhKey(IGTEntity entity, OperationContext opCon, Number fieldId)
    throws GTClientException
  { //20030127AH
    //Nb: Havent got round to supporting dlhKey generations for new entity or no opCon situation
    //@todo: investigate if this method belongs better in another class (such as SimpleDownloadHelper)

    StringBuffer buffer = new StringBuffer();
    if(opCon != null)
    {
      buffer.append(opCon.getOperationContextId());
      buffer.append("_");
    }
    buffer.append(entity.getType());
    buffer.append("_"); //is this ok in a get query????????
    buffer.append( StaticUtils.stringValue(entity.getUidLong()) );
    buffer.append("_");
    buffer.append(entity.getFieldName(fieldId));
    return buffer.toString();
  }
  
  public static void logRequestDetails(HttpServletRequest request, Log log)
  { 
    if(log.isDebugEnabled())
    { //20030728AH - Use getLoggableRequestDetails method to build the string to log
      String requestInfo = getLoggableRequestDetails(request);
      log.debug(requestInfo);
    }
  }
  
  public static String getLoggableRequestDetails(HttpServletRequest request)
  { //20030728AH
    if(request == null)
    {
      return "request is null";
    }
    else
    {
      StringBuffer buffer = new StringBuffer(512);
      buffer.append("\nRequest URI=");
      buffer.append(request.getRequestURI());
      buffer.append("\nRequest Class=");
      buffer.append(request.getClass().getName());
      buffer.append("\nRequest Parameters:\n");
      java.util.Enumeration params = request.getParameterNames();
      while(params.hasMoreElements())
      {
        String param = (String)params.nextElement();
        String[] values = request.getParameterValues(param);
        if(values == null)
        {
          buffer.append(param);
          buffer.append("=null\n");
        }
        else if(values.length == 0)
        {
          buffer.append(param);
          buffer.append("={empty array}\n");
        }
        else
        {
          for(int i=0; i < values.length; i++)
          {
            buffer.append(param);
            buffer.append("[");
            buffer.append(i);
            buffer.append("]=\"");
            buffer.append(values[i]);
            buffer.append("\"\n");
          }
        }
      }
      return buffer.toString();
    }
  }
}