/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExceptionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-28     Andrew Hill         Created
 * 2002-11-01     Andrew Hill         Mimimise trace for all ResponseException (sys or app)
 * 2003-04-02     Andrew Hill         Render exception link label carefully
 * 2003-04-08     Andrew Hill         Use script url constant defined in IGlobals
 * 2003-07-09     Andrew Hill         Hardcode delegation to DeleteExceptionRenderer
 * 2003-07-11     Andrew Hill         Refactor to use SectionRenderer
 */
package com.gridnode.gtas.client.web.renderers;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.DeleteException;
import com.gridnode.gtas.client.ctrl.LoginException;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.ctrl.SessionCreationException;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.InvalidOperationContextException;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.strutsbase.NoSessionException;

public class ExceptionRenderer  extends AbstractRenderer
{
  static final int INIT_SW_SIZE = 4096;
  //private static final String SOURCE_NODE_ID = "exception_content";
  private static final String DEST_NODE_ID = "insert_error";

  //private boolean _showDetails = true;

  public ExceptionRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  public void render() throws RenderingException
  { //20030711AH - Refactored to use a section renderer
    try
    {
      RenderingContext rContext = getRenderingContext();
      HttpServletRequest request = rContext.getRequest();
      if(request == null)
      {
        throw new java.lang.NullPointerException("Reference to request in rendering context is null");
      }
      
      
      Throwable throwable = (Throwable)request.getAttribute(IRequestKeys.REQUEST_EXCEPTION);
      Throwable rootEx = (Throwable)request.getAttribute(IRequestKeys.REQUEST_ROOT_EXCEPTION);
      if(rootEx == null) rootEx = throwable;
      if(rootEx == null) return; //No exception to render!
      
      String headerText = getHeading(rContext, rootEx);
      String messageText = getMessage(rContext, throwable, rootEx);
  
      Element errorNode = getElementById(DEST_NODE_ID, false);
      if(errorNode == null) return;
      removeAllChildren(errorNode);
      Element header = _target.createElement("h4");
      header.setAttribute("class","errortext");
      replaceText(header, headerText);
      errorNode.appendChild(header);
      
      SectionRenderer sr = new SectionRenderer(rContext);
      sr.setAppend(true);
      sr.setLabelKey(null); //we will render this ourselves manually
      sr.setLocked(false);
      sr.setInsertionPoint(errorNode);
      sr.setContentElementName( rootEx instanceof DeleteException ? "div" : "pre");
      sr.render(_target);
      
      Element label = sr.getLabelNode();
      replaceText(label, messageText);
      label.setAttribute("class","errortext");
      
      Element contentNode = sr.getContentNode();
      if(rootEx instanceof DeleteException)
      { 
        DeleteExceptionRenderer der = new DeleteExceptionRenderer(rContext, (DeleteException)rootEx, contentNode);
        der.render(_target);
      }
      else
      {
        String trace = getTrace(rContext, throwable);
        contentNode = replaceNodeWithNewElement("pre", contentNode, false);
        contentNode.setAttribute("class","hiddenSection");
        replaceText(contentNode,trace);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering exception!",t);
    }
  }

  private String getMessage(RenderingContext rContext, Throwable throwable, Throwable rootEx)
    throws RenderingException
  {
    ISimpleResourceLookup rLookup = rContext.getResourceLookup();
    if(rLookup == null)
    {
      throw new java.lang.NullPointerException("Reference to resource lookup in rendering context is null");
    }
    String messageText = null;
    ActionErrors actionErrors = rContext.getActionErrors();
    
    if(actionErrors != null)
    {
      if(MessageUtils.getFirstError(actionErrors, IGlobals.EXCEPTION_ERROR_PROPERTY) != null)
      {
        ActionError error = MessageUtils.getFirstError(actionErrors, IGlobals.EXCEPTION_ERROR_PROPERTY);
        messageText = rLookup.getMessage(error.getKey(), error.getValues());
      }
    }
    if(messageText == null)
    { //20030710AH - Render exception message where no action error
      //String errorMessage = null;
      if(rootEx instanceof ResponseException)
      {
        ResponseException rex = (ResponseException)rootEx;
        String key = "gtas.error." + rex.getErrorCode();
        messageText = rContext.getResourceLookup().getMessage(key, rex.getErrorParams());
      }
      else
      {
        messageText = throwable.getMessage();
      }
    }
    return messageText;
  }

  private String getHeading(RenderingContext rContext, Throwable t) throws RenderingException
  { //20030710AH
    String labelKey = null;
    if(t == null)
    {
      labelKey = "gtas.error.unknown";
    }
    else if(t instanceof NoSessionException)
    {
      labelKey = "gtas.error.application"; //obsolete?
    }
    else if(t instanceof InvalidOperationContextException)
    {
      labelKey = "gtas.error.application";
    }
    else if(t instanceof ResponseException)
    {
      labelKey = ((ResponseException)t).isAppError() ? "gtas.error.application" : "gtas.error.system";
    }
    else if(t instanceof LoginException)
    {
      labelKey = "gtas.error.system";
    }
    else if(t instanceof SessionCreationException)
    {
      labelKey = "gtas.error.system";
    }
    else
    {
      labelKey = "gtas.error.error";
    }
    return rContext.getResourceLookup().getMessage(labelKey);
  }

  private String getTrace(RenderingContext rContext, Throwable throwable)
    throws RenderingException
  { //20030711AH
    try
    {
      StringWriter sWriter = new StringWriter(INIT_SW_SIZE);
      PrintWriter pWriter = new PrintWriter(sWriter,false);
      StaticWebUtils.printStackTrace(throwable, pWriter);
      pWriter.flush();
      return sWriter.toString();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error writing throwable stack trace to String object",t);
    }
  }
}