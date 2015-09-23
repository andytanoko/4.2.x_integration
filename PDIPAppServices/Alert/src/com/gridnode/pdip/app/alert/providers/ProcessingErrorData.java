/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessingErrorData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 06 May 2003    Neo Sok Lay         Created
 * 03 Mar 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.providers;

import java.util.ArrayList;
import java.util.List;


/**
 * This data provider provides the necessary processing error information.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class ProcessingErrorData extends AbstractDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3454341635131052784L;

	public static final String NAME = "ProcessingError";

  public static final String FIELD_TYPE         = "TYPE";
  public static final String FIELD_REASON       = "REASON";

  private ExceptionDetails _exception;

  public ProcessingErrorData(String type, String reason, Throwable t)
  {
    set(FIELD_TYPE,   type);
    set(FIELD_REASON, reason);

    if (t != null)
      _exception = new ExceptionDetails(t);
  }

  public String getName()
  {
    return NAME;
  }

  public final static List<String> getFieldNameList()
  {
    List<String> list = new ArrayList<String>();
    list.add(FIELD_TYPE);
    list.add(FIELD_REASON);
    list.add(ExceptionDetails.FIELD_MSG);
    list.add(ExceptionDetails.FIELD_STACK_TRACE);

    return list;
  }

  protected Object getFieldValue(String fieldName)
  {
    Object val = null;
    if (ExceptionDetails.FIELD_MSG.equals(fieldName) || ExceptionDetails.FIELD_STACK_TRACE.equals(fieldName))
    {
      if (_exception != null)
        val = _exception.getFieldValue(fieldName);
    }
    else
    {
      val = super.getFieldValue(fieldName);
    }

    if (val == null)
      val = "[Not Available]";

    return val;
  }

}