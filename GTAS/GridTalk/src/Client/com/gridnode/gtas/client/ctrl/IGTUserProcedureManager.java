/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTUserProcedureManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-22     Daniel D'Cotta      Created
 * 2003-07-30     Andrew Hill         newSoapProcedure()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTUserProcedureManager extends IGTManager
{
  public IGTShellExecutableEntity newShellExecutable() throws GTClientException;
  public IGTJavaProcedureEntity   newJavaProcedure() throws GTClientException;
  public IGTSoapProcedureEntity   newSoapProcedure() throws GTClientException; //20030730AH
  public IGTParamDefEntity        newParamDef() throws GTClientException;
  public IGTReturnDefEntity       newReturnDef() throws GTClientException;
}