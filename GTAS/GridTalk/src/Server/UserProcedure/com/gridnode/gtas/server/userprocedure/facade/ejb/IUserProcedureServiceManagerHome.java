/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserProcedureServiceManagerObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh              Created
 */

package com.gridnode.gtas.server.userprocedure.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface IUserProcedureServiceManagerHome extends EJBHome
{
  public IUserProcedureServiceManagerObj create()
    throws CreateException,RemoteException;

}