/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingServiceObj
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 */

package com.gridnode.pdip.base.packaging.facade.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.messaging.Message;


public interface IPackagingServiceObj extends EJBObject
{
  public Message pack(PackagingInfo info, Message message) 
    throws PackagingException,SystemException,RemoteException;

  public Message unPack(PackagingInfo info, Message message) 
    throws PackagingException,SystemException,RemoteException;
}