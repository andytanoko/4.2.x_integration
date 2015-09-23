/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerFunctionManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-16     Andrew Hill         Created
 * 2002-09-23     Andrew Hill         cookWorkflowActivity()
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;

public interface IGTPartnerFunctionManager extends IGTManager
{
  public IGTWorkflowActivityEntity newWorkflowActivity() throws GTClientException;
  public List cookWorkflowActivity(IGTWorkflowActivityEntity workflow) throws GTClientException;
}