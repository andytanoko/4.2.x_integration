/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGridTalkAction.java
 * (Copy from GridTalkEJBAction.java which is to be phased out).
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2002    Neo Sok Lay         Created
 * Jun 05 2002    Neo Sok Lay         Add methods to get/set role principals.
 * Jun 06 2002    Neo Sok Lay         Check for valid session/user
 * Jun 18 2002    Neo Sok Lay         Utility methods for constructing event
 *                                    responses.
 *                                    Control for perform(IEvent).
 * Jul 31 2002    Neo Sok Lay         Add method to get the Enterprise ID for this
 *                                    GTAS.
 * Aug 19 2002    Koh Han Sing        Modified to store username.
 * Jul 24 2003    Neo Sok Lay         Add method: getUserID()
 */
package com.gridnode.gtas.server.rdm.ejb.actions;

import com.gridnode.gtas.server.rdm.StateValidator;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rdm.helpers.*;
import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.rpf.ejb.action.EJBActionSupport;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Set;

/**
 * This is an abstract Action class for all GridTalk Action implementations
 * to extend. This abstract Action class provides helper methods for checking
 * attributes in the StateMachine, setting or clearing attributes, etc.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public abstract class AbstractGridTalkAction
  extends    EJBActionSupport
  implements IAttributeKeys, IEventLogger
{
  protected StateValidator _validator;
  protected String _sessionID;
  protected String _userID;
  protected String _application;


  // *********************** Methods from IEvent **************************

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

    if ((getExpectedEventClass() != null) &&
        !(getExpectedEventClass().isInstance(event)))
      throw new EventException(
                  "Unrecognized event for processing: " +
                  event.getClass().getName() +
                  ", expected: "+getExpectedEventClass().getName());

    try
    {
      doSemanticValidation(event);
    }
    catch (Exception ex)
    {
      logEventError(event, ex);
      throw new EventException(ex.getMessage());
    }
  }

  public IEventResponse perform(IEvent event) throws EventException
  {
    IEventResponse response = null;
    IEventLogger logger = null;


    try
    {
      logger = getEventLogger();

      if (logger != null)
        logger.logEventStart(event);

      response = doProcess(event);
    }
    catch (TypedException ex)
    {
      if (logger != null)
        logger.logEventError(event, ex);

      response = constructErrorResponse(event, ex);
    }
    catch (Throwable t)
    {
      if (logger != null)
        logger.logEventError(event, t);

      response = constructErrorResponse(event, new SystemException(t));
    }
    finally
    {
      if (logger != null)
        logger.logEventEnd(event);
    }
    return response;
  }

  // ******************* Own methods ********************************

  /**
   * Perform a semantic validation on the event. A semantic validation inspects
   * the event data to make sure that they are understandable in order to
   * continue processing the event. For example, validation in a Update type
   * of action may include checking the existence of the record to be updated.
   *
   * @param event The event to do semantic validation.
   * @exception Error encountered during validation.
   * @since 2.0
   */
  protected void doSemanticValidation(IEvent event) throws Exception
  {
  }

  /**
   * Construct an error event response object.
   *
   * @param event The event that was being performed but encountered error.
   * @param ex The exception that occurred.
   * @return The constructed event response object.
   *
   * @since 2.0
   */
  protected abstract IEventResponse constructErrorResponse(
    IEvent event, TypedException ex);

  /**
   * Starting actual processing of an event. This is only called when the
   * semantic validation of the event passes.
   *
   * @param event The event to be processed.
   * @return The event response upon successful processing of the event.
   * @exception Throwable Any error that may occur during processing.
   * @since 2.0
   */
  protected abstract IEventResponse doProcess(IEvent event) throws Throwable;

  /**
   * Get the short name of the concrete Action class.
   *
   * @return The name of the concrete Action class.
   *
   * @since 2.0
   */
  protected abstract String getActionName();

  /**
   * Get an event logger to log the start/end of an event or when error
   * occurs during the processing of an event. Default is a simple log statement
   * to the log file. Sub-classes can override this method to use a more
   * sophisticated logger.
   *
   * @return The event logger to use. May be null if no logging is needed.
   *
   * @since 2.0
   */
  protected IEventLogger getEventLogger()
  {
    return this;
  }

  /**
   * Get the Class of the expected event that the concrete action is
   * capable of processing.
   *
   * @return The Class of the expected event.
   *
   * @since 2.0
   */
  protected abstract Class getExpectedEventClass();

  // ********************* IEventLogger methods ***************************

  /**
   * Log the perform event error.
   */
  public void logEventError(IEvent event, Throwable t)
  {
    StringBuffer buff = new StringBuffer("[").append(getActionName()).append(
                              ".perform] Event Error ");

    Logger.warn(buff.toString(), t);
  }
  
  /**
   * Logs the start of the event perform.
   */
  public void logEventStart(IEvent event)
  {
    StringBuffer buff = new StringBuffer("[").append(getActionName()).append(
                              ".perform] Starts ");

    Logger.debug(buff.toString());
  }

  /**
   * Logs the end of the event perform.
   */
  public void logEventEnd(IEvent event)
  {
    StringBuffer buff = new StringBuffer("[").append(getActionName()).append(
                              ".perform] Ends ");

    Logger.debug(buff.toString());
  }

  /**
   * @see com.gridnode.gtas.server.rdm.helpers.IEventLogger#logEventStatus(IEvent, String)
   */
  public void logEventStatus(IEvent event, String message)
  {
    StringBuffer buff = new StringBuffer("[").append(getActionName()).append(
                              ".perform] ").append(message).append(" ");

    Logger.debug(buff.toString());
  }


  // ********************* State Checking methods ***************************

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
   * Sets the UserName attribute to the StateMacine with the specified value.
   * This value is validated before setting. If validation passes, <CODE>_userName</CODE>
   * is set equal to <CODE>userName</CODE>.
   *
   * @exception InvalidStateException <CODE>userName</CODE> is invalid.
   */
  protected void setUserName(String userName)
    throws InvalidStateException
  {
    sm.setAttribute(USER_NAME, userName);
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
   * Get the EnterpriseID for this GTAS.
   */
  protected String getEnterpriseID()
  {
    return (String)sm.getAttribute(ENTERPRISE_ID);
  }

  /**
   * Sets the ENTERPRISE_ID attribute into the state machine.
   *
   * @param principals The enterpriseID for this GTAS.
   */
  protected void setEnterpriseID(String enterpriseID)
  {
    sm.setAttribute(ENTERPRISE_ID, enterpriseID);
  }

  /**
   * Get the username of the user.
   */
  protected String getUserName()
  {
    return (String)sm.getAttribute(USER_NAME);
  }

  /**
   * Get the UserID of the current login user
   */
  protected String getUserID()
  {
    return _userID;
  }
  
  // *********************** Obtaining Managers ****************************

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

  // ******************** Construct Event Response ***************************

  /**
   * Constant for an empty (0-size) object array.
   */
  private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

  /**
   * Construct an event response object indicating an error.
   *
   * @param errorCode The code for the error
   * @param params The parameters for the error message (based on errorCode).
   * @param ex The exception that occurred. Must not be null.
   * @return The constructed event response object, specifically a
   * <CODE>BasicEventResponse</CODE>.
   *
   * @since 2.0
   */
  protected static IEventResponse constructEventResponse(
    short errorCode, Object[] params, TypedException ex)
  {
    return new BasicEventResponse(
                 errorCode,
                 params,
                 ex.getType(),
                 ex.getLocalizedMessage(),
                 ex.getStackTraceString());
  }

  /**
   * Construct an event response object indicating a message only.
   *
   * @param msgCode The code for the message.
   * @param params The parameters for the message (based on msgCode).
   * @return The constructed event response object, specifically a
   * <CODE>BasicEventResponse</CODE>.
   *
   * @since 2.0
   */
  protected static IEventResponse constructEventResponse(
    short msgCode, Object[] params)
  {
    return new BasicEventResponse(msgCode, params);
  }

  /**
   * Construct an event response object indicating a message along with a piece
   * of data to be returned to the action invoker.
   *
   * @param msgCode The code for the message
   * @param params The parameters for the message (based on msgCode).
   * @param returnData The piece of data to be returned.
   * @return The constructed event response object, specifically a
   * <CODE>BasicEventResponse</CODE>.
   *
   * @since 2.0
   */
  protected static IEventResponse constructEventResponse(
    short msgCode, Object[] params, Object returnData)
  {
    return new BasicEventResponse(msgCode, params, returnData);
  }

  /**
   * Construct an event response object to return a piece of data along with a
   * default message (with message code {@link IErrorCode#NO_ERROR}).
   *
   * @param returnData The data to be returned.
   * @return The constructed event response object, specifically a
   * <CODE>BasicEventResponse</CODE>.
   *
   * @since 2.0
   */
  protected static IEventResponse constructEventResponse(Object returnData)
  {
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             EMPTY_OBJ_ARRAY,
             returnData);
  }

  /**
   * Construct an event response object with a default message (with message
   * code {@link IErrorCode#NO_ERROR}).
   *
   * @return The constructed event response object, specifically a
   * <CODE>BasicEventResponse</CODE>.
   *
   * @since 2.0
   */
  protected static IEventResponse constructEventResponse()
  {
    return constructEventResponse(IErrorCode.NO_ERROR, EMPTY_OBJ_ARRAY);
  }

}