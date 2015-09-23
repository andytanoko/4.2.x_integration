/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-25     Andrew Hill         Created
 * 2003-01-21     Andrew Hill         Package protect constructor
 * 2003-04-17     Andrew Hill         Include response trace string in stack trace
 * 2003-07-09     Andrew Hill         Refactored to no longer require a BasicEventResponse to instantiate
 * 2003-07-24     Andrew Hill         Include indication of whether is app or sys error type in error message
 */
package com.gridnode.gtas.client.ctrl;

import java.io.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.gtas.exceptions.IErrorCode;

public class ResponseException extends GTClientException implements IErrorCode
{
  private static final String RESPONSE_TRACE_HEADING = "----Returned Error Trace:"; //20030417AH

  protected BasicEventResponse _response; //20030718AH - Made protected
  protected short _messageCode; //20030709AH, 20030718AH - Made protected
  protected short _errorType;   //20030709AH, 20030718AH - Made protected
  protected String _errorReason; //20030709AH, 20030718AH - Made protected
  protected String _stackTrace;  //20030709AH, 20030718AH - Made protected
  protected IEvent _event; //20030718AH

  ResponseException(BasicEventResponse response) //20030121AH - packageprotected
  { //20030708AH - Delegate to new constructor to store own ref to info
    this( response.getMessageCode(), response.getErrorType(), response.getErrorReason(), response.getErrorTrace() );
    _response = response;
  }
  
  ResponseException(short messageCode, short errorType, String reason, String stackTrace)
  { //20030708AH - Allow instantiating without a BasicEventResponse object
    super("GTAS "
            + (errorType == ApplicationException.APPLICATION ? "Application " : "System ") 
            + "Error[" + messageCode
            + "]: "
            + reason); //20030724AH - Indicate app or sys in message
    _messageCode = messageCode;
    _errorType = errorType;
    _errorReason = reason;
    _stackTrace = stackTrace;
  }

  public BasicEventResponse getResponse()
  {
    return _response;
  }

  public boolean isAppError()
  {
    return _errorType == ApplicationException.APPLICATION; //20030709AH
  }

  public int getErrorCode()
  {
    return (int)_messageCode; //20030709AH
  }

  public Object[] getErrorParams()
  {
    return _response == null ? null : _response.getMessageParams(); //20030709AH
  }

  public String getErrorTrace()
  {
    return _stackTrace; //20030709AH
  }

  public void printStackTrace()
  { //20030417AH
    super.printStackTrace();
    //super will call the stream version for us... :-)
  }

  public void printStackTrace(PrintStream stream)
  { //20030417AH
    super.printStackTrace(stream);
    String errorTrace = getErrorTrace();
    if(errorTrace != null)
    {
      stream.println(RESPONSE_TRACE_HEADING);
      stream.println(errorTrace);
    }
  }

  public void printStackTrace(PrintWriter writer)
  { //20030417AH
    super.printStackTrace(writer);
    String errorTrace = getErrorTrace();
    if(errorTrace != null)
    {
      writer.println(RESPONSE_TRACE_HEADING);
      writer.println(errorTrace);
    }
  }
  
  public short getErrorType()
  { //20030710AH
    return _errorType;
  }

  public String getErrorReason()
  { //20030710AH
    return _errorReason;
  }

  public IEvent getEvent()
  { //20030718AH
    return _event;
  }

  void setEvent(IEvent event)
  { //20030718AH
    _event = event;
  }

}
