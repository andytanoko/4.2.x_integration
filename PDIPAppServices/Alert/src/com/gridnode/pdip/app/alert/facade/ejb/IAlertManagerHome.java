/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertManagerHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 * Oct 17 2005    Neo Sok Lay             For J2EE compliance: create() method
 *                                        must throw RemoteException for remote
 *                                        interface.
 */

package com.gridnode.pdip.app.alert.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * This interface defines the method of creating the EJBobject interface for the AlertManager.
 *
 * @author Srinath
 *
 */

public interface IAlertManagerHome extends EJBHome
{

  public IAlertManagerObj create() throws CreateException, RemoteException;

}