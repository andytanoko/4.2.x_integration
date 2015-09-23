/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionContext.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 * 2002-10-30     Andrew Hill         Add log feature. Change Hashtable to Hashmap
 */
package com.gridnode.gtas.client.web.strutsbase;

import org.apache.struts.action.*;
import org.apache.commons.logging.*;
import javax.servlet.http.*;
import java.util.*;

/**
 * Keep track of important parameters in an action without having to pass them all seperately.
 * nb: not designed to be threadsafe
 */
public class ActionContext
{
  private ActionMapping _mapping;
  private ActionForm _actionForm;
  private HttpServletRequest _request;
  private HttpServletResponse _response;
  private HashMap _attributes;
  private Log _log;

  public ActionContext(ActionMapping mapping,
                ActionForm actionForm,
                HttpServletRequest request,
                HttpServletResponse response)
  {
    _mapping = mapping;
    _actionForm = actionForm;
    _request = request;
    _response = response;
  }

  /**
   * Returns a log. If One was init or set earlier will create one, however the name used
   * for the created one will be that of this ActionContext class and not the action you want to
   * use the log in...
   */
  public Log getLog()
  {
    if(_log == null)
    {
      _log = LogFactory.getLog(ActionContext.class);
    }
    return _log;
  }

  public void setLog(Log log)
  {
    _log = log;
  }

  /**
   * From the action call using log = initLog(this.getClass()); and it will
   * both create and return the log, and store a ref to it for retrieval with getLog()
   */
  public Log initLog(Class actionClazz)
  {
    _log = LogFactory.getLog(actionClazz);
    return _log;
  }

  public Object getAttribute(Object key)
  {
    if(_attributes != null)
      return _attributes.get(key);
    else
      return null;
  }

  public void setAttribute(Object key, Object value)
  {
    if(_attributes == null)
    {
      _attributes = new HashMap();
    }
    _attributes.put(key,value);
  }

  public HttpSession getSession()
  {
    return _request.getSession();
  }

  public ActionMapping getMapping()
  {
    return _mapping;
  }

  public ActionForm getActionForm()
  {
    return _actionForm;
  }

  public void setActionForm(ActionForm actionForm)
  {
    _actionForm = actionForm;
  }

  public HttpServletRequest getRequest()
  {
    return _request;
  }

  public HttpServletResponse getResponse()
  {
    return _response;
  }
}