/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMultiClassLoader.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 * Jul 11 2003    Koh Han Sing            Repackage into framework from base
 *                                        userprocedure
 * Jun 09 2005    Guo Jianyu              Defect GNDB00025802: changed DEFAULT_PROTOCOL_FILE
 *                                        from "file:/" to "file:";
 */
package com.gridnode.pdip.framework.util.classloader;

public interface IMultiClassLoader
{
  public static final String DEFAULT_PROTOCOL_TYPE = "file:";

  public void invokeClass(String classname,String args[]) throws Exception;

  public Object invokeClassMethod(
    String className,
    String methodName,
    Class [] cstParameterTypes,
    Object [] cstParameters,
    Class [] mtdParameterTypes,
    Object [] mtdParameters)
    throws Exception;

  public String getClassName() throws Exception;
}