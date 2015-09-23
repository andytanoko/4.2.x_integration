/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelManagerHome.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Dec 03 2003    Jagadeesh               Modified : To throw CreateException,RemoteException
 */

package com.gridnode.pdip.app.channel.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

import java.rmi.RemoteException;

/**
 * This interface defines the method of creating the EJBobject interface for the ChannelManager.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IChannelManagerHome extends EJBHome
{
  public IChannelManagerObj create() throws CreateException, RemoteException;
}