/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IncorrectRecipientFormatException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.util.Collection;
import java.util.Iterator;

/**
 * This exception class is used for indicating a Recipient format is incorrect.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class IncorrectRecipientFormatException
  extends ApplicationException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 925554614118806770L;
	private static final String INCORRECT_FORMAT     = "Incorrect Recipient format specified:-";
  private static final String RECIPIENT            = "\nRecipient   : ";

  private IncorrectRecipientFormatException(String msg)
  {
    super(msg);
  }

  public static IncorrectRecipientFormatException createEx(Collection invalidRecpts)
  {
    StringBuffer buff = new StringBuffer(INCORRECT_FORMAT);

    for (Iterator i=invalidRecpts.iterator(); i.hasNext(); )
    {
      buff.append(RECIPIENT).append(i.next());
    }
    return new IncorrectRecipientFormatException(buff.toString());
  }

}