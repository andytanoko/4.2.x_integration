/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BasicEventResponse.java
 *
 ****************************************************************************
 * Date           Author              Changes
************************************************************************
 * Apr 24 2002    Neo Sok Lay         Created
 * Jul 18 2003    Neo Sok Lay         Add constructor: 
 *                                    BasicEventResponse(messageCode,errorType,returnData)
 */
package com.gridnode.pdip.framework.rpf.event;

/**
 * This class provides a basic EventResponse implementation.<P>
 * It holds the information to be provided by a standard EventResonse object:
 * <PRE>
 * IsEventSuccessful - Whether the event has been performed successfully.
 * MessageCode       - The code for the message returned. This can be an error/
 *                     warning/information message code.
 * MessageParams     - Array of parameters for formatting the message (if message
 *                     code is provided).
 * ErrorReason       - If the event is not performed successfully, indicates a
 *                     short description of the reason for the failure.
 * ErrorTrace        - A more detailed trace of the reason for the failure of
 *                     the event.
 * ErrorType         - Type of error (System/application level etc).
 * ReturnData        - Piece of data to return to the requesting party (when
 *                     event is successful).
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class BasicEventResponse extends EventResponseSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2840066094180389844L;
	public static final String IS_SUCCESSFUL = "Is Successful";
  public static final String MESSAGE_CODE  = "Message Code";
  public static final String ERROR_REASON  = "Error Reason";
  public static final String ERROR_TRACE   = "Error Trace";
  public static final String ERROR_TYPE    = "Error Type";
  public static final String MESSAGE_PARAMS= "Message Parameters";
  public static final String RETURN_DATA   = "Return Data";

  /**
   * Construct a BasicEventResponse object which inidicates a successful
   * event status.
   *
   * @param messageCode The code for the message for the client handling
   * this EventResponse. <B>null</B> if none is required.
   * @param messageParams Array of parameters for the message. <B>null</B> if
   * no message code is specified, or no parameters.
   *
   * @since 2.0
   */
  public BasicEventResponse(short messageCode, Object[] messageParams)
  {
    super();
    setResponseData(IS_SUCCESSFUL, Boolean.TRUE);
    setResponseData(MESSAGE_CODE, new Short(messageCode));
    setResponseData(MESSAGE_PARAMS, messageParams);
  }

  /**
   * Construct a BasicEventResponse object which inidicates a successful
   * event status.
   *
   * @param messageCode The code for the message for the client handling
   * this EventResponse. <B>null</B> if none is required.
   * @param messageParams Array of parameters for the message. <B>null</B> if
   * no message code is specified, or no parameters.
   * @param returnData The data object to be returned.
   *
   * @since 2.0
   */
  public BasicEventResponse(
    short messageCode, Object[] messageParams, Object returnData)
  {
    this(messageCode, messageParams);
    setResponseData(RETURN_DATA, returnData);
  }

  /**
   * Constructs a BasicEventResponse object which indicates a failure event
   * status.
   *
   * @param messageCode The code for the message for the client handling this
   * EventResponse. <B>null</B> if none is required.
   * @param messageParams Array of parameters for the message. <B>null</B> if
   * no message code is specified, or no parameters.
   * @param errorReason Short description of the reason of the failure.
   * @param errorTrace A trace of the error to the root cause.
   *
   * @since 2.0
   */
  public BasicEventResponse(
    short messageCode, Object[] messageParams,
    short errorType, String errorReason, String errorTrace)
  {
    super();
    setResponseData(IS_SUCCESSFUL, Boolean.FALSE);
    setResponseData(MESSAGE_CODE, new Short(messageCode));
    setResponseData(MESSAGE_PARAMS, messageParams);
    setResponseData(ERROR_REASON, errorReason);
    setResponseData(ERROR_TRACE, errorTrace);
    setResponseData(ERROR_TYPE, new Short(errorType));
  }

  /**
   * Constructs a BasicEventResponse object which indicates a failure event
   * status.
   *
   * @param messageCode The code for the message for the client handling this
   * EventResponse. <B>null</B> if none is required.
   * @param errorType Type of error that occurred.
   * @param returnData Custom data to be returned to the requester. This may 
   * indicate further error details of the event.
   *
   * @since GT 2.2 I1
   */
  public BasicEventResponse(
    short messageCode, short errorType, Object returnData)
  {
    super();
    setResponseData(IS_SUCCESSFUL, Boolean.FALSE);
    setResponseData(MESSAGE_CODE, new Short(messageCode));
    setResponseData(RETURN_DATA, returnData);
    setResponseData(ERROR_TYPE, new Short(errorType));
  }

  public String getEventName()
  {
    return null;
  }

  /**
   * Get the message code of this event response.
   *
   * @return The message code, or <B>null</B> if none.
   *
   * @since 2.0
   */
  public short getMessageCode()
  {
    Short type = (Short)getResponseData(MESSAGE_CODE);

    return (type==null? -1 :type.shortValue());
  }

  /**
   * Get the error reason of this event response.
   *
   * @return The error reason, or <B>-1</B> if none.
   *
   * @since 2.0
   */
  public String getErrorReason()
  {
    return (String)getResponseData(ERROR_REASON);
  }

  /**
   * Get the error trace of this event response.
   *
   * @return The error trace, or <B>null</B> if none.
   *
   * @since 2.0
   */
  public String getErrorTrace()
  {
    return (String)getResponseData(ERROR_TRACE);
  }

  /**
   * Get the error type of this event response.
   *
   * @return The error type, or <B>-1</B> if none.
   *
   * @since 2.0
   */
  public short getErrorType()
  {
    Short type = (Short)getResponseData(ERROR_TYPE);

    return (type==null? -1 :type.shortValue());
  }


  /**
   * Check the status of the event.
   *
   * @return <B>true</B> if event is performed successfully, <B>false</B>
   * otherwise.
   *
   * @since 2.0
   */
  public boolean isEventSuccessful()
  {
    return ((Boolean)getResponseData(IS_SUCCESSFUL)).booleanValue();
  }

  /**
   * Get the message parameters of this event response.
   *
   * @return The message parameters, or <B>null</B> if none.
   *
   * @since 2.0
   */
  public Object[] getMessageParams()
  {
    return (Object[])getResponseData(MESSAGE_PARAMS);
  }

  /**
   * Get the data that is returned from the event if successful.
   *
   * @return The data returned from performing the event successfully,
   * <B>null</B> if event fails or no data is returned.
   *
   * @since 2.0
   */
  public Object getReturnData()
  {
    return getResponseData(RETURN_DATA);
  }

}