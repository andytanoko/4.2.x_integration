/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityLocalObject.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Throw EntityModifiedException for setData().
 */
package com.gridnode.pdip.framework.db.ejb;

import java.rmi.RemoteException;

import javax.ejb.*;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;

public interface IEntityObject extends EJBObject {

	public IEntity getData() throws RemoteException;

	public void setData(IEntity data) throws EntityModifiedException, RemoteException;

}

