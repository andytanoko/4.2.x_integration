/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DependencyCheckException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 16 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * Exception class for use with dependency checking.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DependencyCheckException extends SystemException
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1875903375611913235L;
	private static final String CREATE_DESCR_EX = "Error while creating entity descriptors";
  private static final String DEPENDENCY_CHECKER_EX = "Error while checking dependencies";
  
  /**
   * Constructor for DependencyCheckException.
   * @param msg Message of the exception.
   * @param ex The error that occurred
   */
  private DependencyCheckException(String msg, Throwable ex)
  {
    super(msg, ex);
  }

  public static DependencyCheckException createDescriptorEx(Throwable t)
  {
    return new DependencyCheckException(CREATE_DESCR_EX, t);
  }
  
  public static DependencyCheckException dependencyCheckerEx(Throwable t)
  {
    return new DependencyCheckException(DEPENDENCY_CHECKER_EX, t);
  }
}
