/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcedureDefFilePathConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 20 2002    Jagadeesh               Created
 * Jul 30 2003    Koh Han Sing            Add path for WSDL.
 */


package com.gridnode.pdip.base.userprocedure.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;


public interface IProcedureDefFilePathConfig extends IPathConfig
{

  /**
   * Path At GTAS - for Shell Executable -*.exe & *.bat
   */
  public static final String PATH_EXEC = "userproc.path.exec";

  /**
   * Path at GTAS - for jar files  - *.jar.
   */

  public static final String PATH_JARS = "userproc.path.jars";

  /**
   * Path at GTAS - for class files - *.class
   */
  public static final String PATH_CLASSES = "userproc.path.classes";

  /**
   * Path at GTAS - for wsdl files - *.wsdl
   */
  public static final String PATH_WSDL = "userproc.path.wsdl";
}