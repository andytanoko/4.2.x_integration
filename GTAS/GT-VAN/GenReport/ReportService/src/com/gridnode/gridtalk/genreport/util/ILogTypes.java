/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogTypes.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng          Created
 * Mar 07 2007			Alain Ah Ming				Changed log category to upper case
 */

package com.gridnode.gridtalk.genreport.util;

/**
 * @author Regina Zeng
 * This interface defines the Logger types for GenReport module.
 */
public interface ILogTypes
{
  public static final String TYPE_GENREPORT_GENERATE = "GN.GENREPORT.GENERATE";
  public static final String TYPE_GENREPORT_RECEIVE = "GN.GENREPORT.RECEIVE";
  public static final String TYPE_GENREPORT_UTIL = "GN.GENREPORT.UTIL";
  public static final String TYPE_GENREPORT_EJB = "GN.GENREPORT.EJB";
}
