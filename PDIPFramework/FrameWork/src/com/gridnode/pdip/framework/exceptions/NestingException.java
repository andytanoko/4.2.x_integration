/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NestingException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 06 2003    Neo Sok Lay         Modified to enhance the printStackTrace()
 *                                    and getMessage().
 * Feb 27 2003    Neo Sok Lay         Refine to not print repetitive exceptions.
 * Oct 21 2003    Neo Sok Lay         Change getMessage() to return own message
 *                                    together with nested message, if any.
 */
package com.gridnode.pdip.framework.exceptions;

/**
 * <p>Title: PDIP</p>
 * <p>Description: Peer Data Interchange Platform</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.io.PrintStream;
import java.io.StringWriter;
import java.io.PrintWriter;

public class NestingException extends Exception
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6364865455893205482L;

	private Class _nestedExceptionClass;

  // the nested exception
  //private Throwable _nestedException;

  // String representation of stack trace - not transient!
  private String _nestedStackTrace;

  private String _nestedMsg;

  // convert a stack trace to a String so it can be serialized
  static public String generateStackTraceString(Throwable t)
  {
    String stackTrace = null;
    if (t != null)
    {
      StringWriter s = new StringWriter();
      t.printStackTrace(new PrintWriter(s));
      stackTrace = s.toString();
    }
    else
      stackTrace = "";

    return stackTrace;
  }

  // java.lang.Exception constructors
  public NestingException() {}
  public NestingException(String msg)
  {
    super(msg);
  }

  // additional c'tors - nest the exceptions, storing the stack trace
  public NestingException(Throwable nestedException)
  {
    setNestedException(nestedException);
  }

  public NestingException(String msg, Throwable nestedException)
  {
    this(msg);
    setNestedException(nestedException);
  }

  // methods
  private void setNestedException(Throwable nestedException)
  {
    if (nestedException != null)
    {
      _nestedExceptionClass = nestedException.getClass();
      _nestedMsg = nestedException.getMessage();
      _nestedStackTrace = generateStackTraceString(nestedException);
    }
  }

  public Class getNestedExceptionClass()
  {
    return _nestedExceptionClass;
  }

  // descend through linked-list of nesting exceptions, & output trace
  // note that this displays the 'deepest' trace first
  public String getStackTraceString()
  {
    StringBuffer stackTraceBuff = new StringBuffer();

    StringWriter s = new StringWriter();
    super.printStackTrace(new PrintWriter(s));
    stackTraceBuff.append(s.toString());

    if (_nestedStackTrace != null)
    {
      stackTraceBuff.append(_nestedStackTrace);
    }

    return stackTraceBuff.toString();
  }

  // overrides Exception.getMessage()
  public String getMessage()
  {
    // superMsg will contain whatever String was passed into the
    // constructor, and null otherwise.
    String superMsg = super.getMessage();

    StringBuffer msg = new StringBuffer();
    if (superMsg != null)
    {
      msg.append(superMsg);
      if (_nestedMsg != null)
        msg.append("\n<<Cause>> ").append(_nestedMsg);
    }
    else
    {
      msg.append(_nestedMsg == null? "(unknown error)" : _nestedMsg);
    }
    return msg.toString();
    /*031021NSL
    return superMsg;

    // if there's no nested exception, do like we would always do
    if (_nestedMsg == null)
      return "(unknown error)";

    // return the nested exception's message
    return _nestedMsg;
    */
  }

  // overrides Exception.toString()
/*
  public String toString()
  {
    StringBuffer theMsg = new StringBuffer(super.toString());
    if (getNestedException() != null)
        theMsg.append("; \n\t---> nested ").append(getNestedException());
    return theMsg.toString();
  }
*/

  public void printStackTrace()
  {
    synchronized (System.err)
    {
      System.err.println(getStackTraceString());
    }
  }

  public void printStackTrace(PrintStream ps)
  {
    synchronized (ps)
    {
      ps.println(getStackTraceString());
    }
  }

  public void printStackTrace(PrintWriter pw)
  {
    synchronized (pw)
    {
      pw.println(getStackTraceString());
    }
  }
/*
  public static void main(String[] args)
  {
    try
    {
      call1();
    }
    catch (Exception ex)
    {
      System.err.println("\ncall1 exception ============== ");
      ex.printStackTrace();
    }
  }

  static void call1() throws Exception
  {
    try
    {
      call2();
    }
    catch (Exception ex)
    {
//      System.err.println("\ncall2 exception ============== ");
//      ex.printStackTrace();
      throw ex;
    }
  }

  static void call2() throws Exception
  {
    try
    {
      call3();
    }
    catch (Exception ex)
    {
//      System.err.println("call3 exception ============== ");
//      ex.printStackTrace();
      throw new NestingException(ex);
    }
  }

  static void call3() throws Exception
  {
    try
    {
      call4();
    }
    catch (Exception ex)
    {
//      System.err.println("call4 exception ============== ");
//      ex.printStackTrace();
      throw new NestingException("call4 exception", ex);
    }
  }

  static void call4() throws Exception
  {
    try
    {
      call5();
    }
    catch (Exception ex)
    {
//      System.err.println("call5 exception ============== ");
//      ex.printStackTrace();
      throw new NestingException("call5 exception", ex);
    }
  }

  static void call5() throws Exception
  {
    throw new NestingException("Exception 5");
  }
*/
}

