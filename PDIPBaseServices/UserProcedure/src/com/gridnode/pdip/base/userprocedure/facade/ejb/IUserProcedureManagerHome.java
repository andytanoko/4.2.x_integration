/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserProcedureManagerHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 */


package com.gridnode.pdip.base.userprocedure.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;


public interface IUserProcedureManagerHome extends EJBHome
{

  /**
   * Create the ProcedureDefinition Manager.
   *
   * @returns EJBLObject for the ProcedureDefinition Manager Bean.
   */

  public IUserProcedureManagerObj create()
         throws CreateException,RemoteException;
}


