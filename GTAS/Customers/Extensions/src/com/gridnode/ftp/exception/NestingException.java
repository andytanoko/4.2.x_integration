/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NestingException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 */

package com.gridnode.ftp.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NestingException extends Exception
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2825868034142564408L;
	private Throwable superException;

  public NestingException()
  {
    super();
  }

  public NestingException(String message)
  {
    super(message);
  }

  public NestingException(String message,Throwable ex)
  {
    super(message);
    superException = ex;
  }

  public void printStackTrace()
  {
    printStackTrace(System.err);
  }
  public void printStackTrace(PrintStream ps)
  {
    if(superException != null)
    {
      String superString = super.getMessage();
      synchronized(ps)
      {
        ps.print(superString + (superString.endsWith(".") ? "" : ".") +
            "  Root exception is ");
        superException.printStackTrace(ps);
      }
    }
    else
    {
      super.printStackTrace(ps);
    }
  }

  public void printStackTrace(PrintWriter pw)
  {
    if(superException != null)
    {
      String superString = super.toString();
      synchronized(pw)
      {
        pw.print(superString + (superString.endsWith(".") ? "" : ".") +
            "  Root exception is ");
        superException.printStackTrace(pw);
      }
    }
    else
    {
      super.printStackTrace(pw);
    }
  }

  public String getMessage()
  {
    String answer = super.getMessage();
    if(superException != null && superException != this)
        answer = answer + " [Root error message: " + superException.getMessage() + "]";
    return answer;
  }

  public String toString()
  {
    String answer = super.toString();
    if(superException != null && superException != this)
        answer = answer + " [Root exception is " + superException.toString() + "]";
    return answer;
  }

}