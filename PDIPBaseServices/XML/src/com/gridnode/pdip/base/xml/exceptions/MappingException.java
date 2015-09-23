/**
 * This software is the proprietary information of Crimsonlogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2010 (C) Crimsonlogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: XMLException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2010    Wong Ming Qian      Create new class
 */
package com.gridnode.pdip.base.xml.exceptions;

/**
 * @author Wong Ming Qian
 * @version 4.2.2
 */

import com.gridnode.pdip.framework.exceptions.NestingException;

public class MappingException extends NestingException
{ 
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 8479322180376098098L;

  public MappingException()
  {
  }

  public MappingException(String message)
  {
    super(message);
  }

  public MappingException(String message,Exception nestedException)
  {
    super(message, nestedException);
  }
}
