/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExceptionDetails.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 26 Feb 2003    Neo Sok Lay         Created
 * 03 Mar 2006    Neo Sok Lay         Use generics
 */

package com.gridnode.pdip.app.alert.providers;

import java.util.ArrayList;
import java.util.List;

import com.gridnode.pdip.framework.exceptions.NestingException;

/**
 * This data provider provides the necessary information of an exception.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I8
 */
public class ExceptionDetails extends AbstractDetails
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2754871331671172705L;

	public static final String NAME = "Exception";

  public static final String FIELD_MSG         = "MESSAGE";
  public static final String FIELD_STACK_TRACE = "STACK_TRACE";

  public ExceptionDetails(Throwable t)
  {
    set(FIELD_MSG,         t.getMessage());
    set(FIELD_STACK_TRACE, getStackTrace(t));
  }

  public String getName()
  {
    return NAME;
  }

  public final static List<String> getFieldNameList()
  {
    List<String> list = new ArrayList<String>();
    list.add(FIELD_MSG);
    list.add(FIELD_STACK_TRACE);

    return list;
  }

  public static String getStackTrace(Throwable t)
  {
    return NestingException.generateStackTraceString(t);
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
      ExceptionDetails details = new ExceptionDetails(ex);
      System.out.println("getMessage *********************************");
      System.out.println(details.get(FIELD_MSG));
      System.out.println("Stack trace *********************************");
      System.out.println(details.get(FIELD_STACK_TRACE));
    }

  }

  static void call1()
  {
    call2();
  }
  static void call2()
  {
    call3();
  }
  static void call3()
  {
    call4();
  }
  static void call4()
  {
    String io = null;
    io.toCharArray();
  }
*/
}