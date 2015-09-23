/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkEJBAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2002    Neo Sok Lay         Created
 * Jun 05 2002    Neo Sok Lay         Add methods to get/set role principals.
 * Jun 06 2002    Neo Sok Lay         Check for valid session/user
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import java.util.Set;

import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rdm.StateValidator;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.EJBActionSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This is an abstract Action class for all GridTalk Action implementations
 * to extend. This abstract Action class provides helper methods for checking
 * attributes in the StateMachine, setting or clearing attributes, etc.
 *
 * @author Neo Sok Lay
 * @deprecated Replaced by AbstractGridTalkAction
 * @version 2.0
 * @since 2.0
 */
public abstract class GridTalkEJBAction
  extends    EJBActionSupport
  implements IAttributeKeys
{
  protected StateValidator _validator;
  protected String _sessionID;
  protected String _userID;
  protected String _application;

  public void init(StateMachine sm)
  {
    super.init(sm);
    _validator = new StateValidator(sm);
  }

  public void doStart() throws EventException
  {
    validateCurrentState();
  }

  public void validateEvent(IEvent event) throws EventException
  {
    if (event == null)
      throw new EventException("Event is null");

    try
    {
      doSemanticValidation(event);
    }
    catch (Exception ex)
    {
      throw new EventException(ex.getMessage());
    }
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  }

  /**
   * Validate the SessionID attribute in the StateMachine.
   * If SessionID is ok, <CODE>_sessionID</CODE> is set equal to the SessionID
   * attribute.
   * @exception InvalidStateException SessionID attribute is invalid.
   */
  protected void checkSessionID(boolean checkAuth)
    throws InvalidStateException
  {
    _validator.checkStringAttr(SESSION_ID);
    _sessionID = (String)sm.getAttribute(SESSION_ID);

    try
    {
      boolean isAuth = getSessionManager().isAuthSession(_sessionID);
      if (checkAuth && !isAuth)
        throw new Exception("Session is not authenticated!");
    }
    catch (Exception ex)
    {
      throw new InvalidStateException(ex.getMessage());
    }

  }

  /**
   * Sets the SessionID attribute to the StateMacine with the specified value.
   * This value is validated before setting. If validation passes, <CODE>_session</CODE>
   * is set equal to <CODE>sessionID</CODE>.
   *
   * @exception InvalidStateException <CODE>sessionID</CODE> is invalid.
   */
  protected void setSessionID(String sessionID)
    throws InvalidStateException
  {
    _validator.checkStringAttrValue(SESSION_ID, sessionID);
    _sessionID = sessionID;
  }

  /**
   * Clears the SessionID attribute from the StateMachine.
   * <CODE>_sessionID</CODE> is also set to null.
   */
  protected void clearSessionID()
  {
    sm.setAttribute(SESSION_ID, null);
    _sessionID = null;
  }

  /**
   * Validate the UserID attribute in the StateMachine.
   * If UserID is ok, <CODE>_userID</CODE> is set equal to the UserID
   * attribute.
   * @exception InvalidStateException UserID attribute is invalid.
   */
  protected void checkUserID()
    throws InvalidStateException
  {
    _validator.checkStringAttr(USER_ID);
    _userID = (String)sm.getAttribute(USER_ID);

    try
    {
      String authSubject = getSessionManager().getSessionAuthSubject(_sessionID);
      if (!_userID.equals(authSubject))
        throw new Exception("User is not authenticated!");
    }
    catch (Exception ex)
    {
      throw new InvalidStateException(ex.getMessage());
    }

  }

  /**
   * Sets the UserID attribute to the StateMacine with the specified value.
   * This value is validated before setting. If validation passes, <CODE>_userID</CODE>
   * is set equal to <CODE>userID</CODE>.
   *
   * @exception InvalidStateException <CODE>userID</CODE> is invalid.
   */
  protected void setUserID(String userID)
    throws InvalidStateException
  {
    _validator.checkStringAttrValue(USER_ID, userID);
    _userID = userID;
  }

  /**
   * Clears the UserID attribute from the StateMachine.
   * <CODE>_userID</CODE> is also set to null.
   */
  protected void clearUserID()
  {
    sm.setAttribute(USER_ID, null);
    _userID = null;
  }

  /**
   * Validate the Application attribute in the StateMachine.
   * If Application is ok, <CODE>_application</CODE> is set equal to the Application
   * attribute.
   * @exception InvalidStateException Application attribute is invalid.
   */
  protected void checkApplication()
    throws InvalidStateException
  {
    _validator.checkStringAttr(APPLICATION);
    _application = (String)sm.getAttribute(APPLICATION);
  }

  /**
   * Sets the Application attribute to the StateMacine with the specified value.
   * This value is validated before setting. If validation passes, <CODE>_application</CODE>
   * is set equal to <CODE>application</CODE>.
   *
   * @exception InvalidStateException <CODE>application</CODE> is invalid.
   */
  protected void setApplication(String application)
    throws InvalidStateException
  {
    _validator.checkStringAttrValue(APPLICATION, application);
    _application = application;
  }

  /**
   * Get the EnterpriseID for this GTAS.
   */
  protected String getEnterpriseID()
  {
    return (String)sm.getAttribute(ENTERPRISE_ID);
  }

  /**
   * Helper method to validate the various attributes in the StateMachine.
   * For a start, this method checks the SessionID and UserID attributes.
   * Subclasses should override this method for different validation requirements.
   */
  protected void validateCurrentState()
    throws InvalidStateException
  {
    checkSessionID(true);
    checkUserID();
  }

  /**
   * Sets the PRINCIPALS attribute into the state machine.
   *
   * @param principals The set of Principals belonging to the authenticated
   * user of the session.
   */
  protected void setPrincipals(Set principals)
  {
    sm.setAttribute(PRINCIPALS, principals);
  }

  /**
   * Get the set of Principals of the authenticated user of the session,
   * or <B>null</B> if user is not authenticated.
   */
  protected Set getPrincipals()
  {
    return (Set)sm.getAttribute(PRINCIPALS);
  }

  /**
   * Clears the PRINCIPALS attribute from the StateMachine.
   */
  protected void clearPrincipals()
  {
    sm.setAttribute(PRINCIPALS, null);
  }

  /**
   * Get the SessionManager
   */
  protected ISessionManagerObj getSessionManager()
    throws ServiceLookupException
  {
    return (ISessionManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ISessionManagerHome.class.getName(),
      ISessionManagerHome.class,
      new Object[0]);
  }

}