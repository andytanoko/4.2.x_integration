/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IACLManagerHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Oct 20 2005    Neo Sok Lay             method create found but does not throw javax.ejb.CreateException as required.
 */

package com.gridnode.pdip.base.acl.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * This interface defines the method of creating the EJBobject interface for the ACLManager.
 *
 * @author Goh Kan Mun
 *
 * @version 4.0
 * @since 2.0
 */

public interface IACLManagerHome extends EJBHome
{

  public IACLManagerObj create() throws CreateException, RemoteException;

}