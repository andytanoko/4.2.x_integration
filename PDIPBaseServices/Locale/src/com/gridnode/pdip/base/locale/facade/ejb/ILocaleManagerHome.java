/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILocaleManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 * Oct 20 2005    Neo Sok Lay         method create found but does not throw javax.ejb.CreateException as required
 */
package com.gridnode.pdip.base.locale.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

/**
 * EJBHome for LocaleManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I5
 */
public interface ILocaleManagerHome extends EJBHome
{
  public ILocaleManagerObj create() throws CreateException, RemoteException;
}