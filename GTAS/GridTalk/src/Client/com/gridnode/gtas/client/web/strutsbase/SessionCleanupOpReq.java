/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionCleanupOpReq.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Andrew Hill         Created
 * 2003-01-28     Andrew Hill         Implement IOperationContextRemovalListener
 */
package com.gridnode.gtas.client.web.strutsbase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gridnode.gtas.client.GTClientException;


/**
 * When its execute method is called, the object in the session stored in attribute name passed
 * to constructor is removed from the session. This is implemented as an IExecutableOpReq with the
 * intent that it be added to an OperationDispatchActions opReq queue for processing during
 * completion.
 */
public class SessionCleanupOpReq implements IExecutableOpReq, IOperationContextRemovalListener
{
  private static final Log _log = LogFactory.getLog(SessionCleanupOpReq.class); // 20031209 DDJ

  private String _attributeName;

  /**
   * Default noargs constructor. If you use this one be sure to call setAttributeName() before
   * you attempt to invoke execute() (unless you have a fetish for NullPointerExceptions).
   */
  public SessionCleanupOpReq()
  {    
  }

  /**
   * Convienience constructor that takes name of the attribute whose keyed object will be removed
   * from the session when execute is invoked.
   * @param attributeName
   */
  public SessionCleanupOpReq(String attributeName)
  {
    if(attributeName == null) throw new NullPointerException("attributeName is null"); //20030416AH
    setAttributeName(attributeName);
  }

  /**
   * Get name of attribute whose keyed object will be removed from session by the execute method.
   * @return attributeName
   */
  public String getAttributeName()
  { return _attributeName; }

  /**
   * Set name of attribute whose keyed object will be removed from session by the execute method.
   * @param attributeName
   */
  public void setAttributeName(String attributeName)
  { _attributeName = attributeName; }

  /**
   * Will remove the atrribute stored in the session under attributeName key. If there is no
   * object stored under that attribute will do nothing.
   * If the attributeName key is null will throw a NullPointerException.
   * Also Make sure that the actionContext passed is valid as the session is obtained from this.
   * If the session cannot be obtained a NullPointerException will be thrown.
   * (Actually oda will most likely be calling this method rather than your code!)
   * The other two parameters are not needed by this method and are ignored.
   * This ExecutableOpReq never requires further processing so this method always returns true.
   * @param actionContext
   * @param operationDispatchAction (ignored)
   * @param operationContext (ignored)
   * @returns processed always returns true value to indicate that processing of this opReq complete
   * @throws GTClientException
   */
  public boolean execute( ActionContext actionContext,
                          OperationDispatchAction oda,
                          OperationContext opCon)
    throws GTClientException
  {
    if(actionContext == null) throw new NullPointerException("actionContext is null"); //20030416AH
    doRemoveAttribute(actionContext.getRequest());
    return true; //No further processing required
  }

  public void  onRemoveOpCon(HttpServletRequest request, OperationContext opCon)
    throws GTClientException
  {
    doRemoveAttribute(request);
  }

  private void doRemoveAttribute(HttpServletRequest request) throws GTClientException
  {
    if(request == null) throw new NullPointerException("request is null"); //20030416AH
    if(_attributeName == null) throw new NullPointerException("_attributeName is null"); //20030416AH
    if(_log.isDebugEnabled())
    {
      _log.debug("Removing HttpSession attribute:" + _attributeName);
    }
    HttpSession session = request.getSession(false);
    if(session != null)
    {
      session.removeAttribute(_attributeName);
    }
  }

}