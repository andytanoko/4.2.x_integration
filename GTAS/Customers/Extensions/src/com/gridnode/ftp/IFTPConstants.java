/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFTPConstants.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 */


package com.gridnode.ftp;

/**
 * This interface defines constants to parameters passed as
 * command line arguments.
 *
 *
 */

public interface IFTPConstants
{

  public static final String HOST = "-host";

  public static final String PORT = "-port";

  public static final String USER_NAME = "-username";

  public static final String PASSWORD = "-password";

  public static final String FILENAME = "-filename";

  public static final String PROVIDER = "-provider";

  public static final String FILENAME_PREFIX = "-prefix";

  public static final String RENAME_PREFIX = "doa";

}