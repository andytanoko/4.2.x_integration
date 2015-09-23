/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaProcedureHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Feb 04 2003    Jagadeesh               Added: Delimiter Constant.
 */

package com.gridnode.pdip.base.userprocedure.handler;

import com.gridnode.pdip.base.userprocedure.model.ProcedureHandlerInfo;
import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;

import com.gridnode.pdip.framework.exceptions.SystemException;

public interface IProcedureHandler
{

  public static final String ARGUMENT_DELIMITER = "%";

  public Object execute(ProcedureHandlerInfo procedureHandlerInfo)
    throws UserProcedureExecutionException,SystemException;
}