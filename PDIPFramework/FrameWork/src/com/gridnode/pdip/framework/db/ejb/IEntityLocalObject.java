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
 * Apr 17 2002    NSL/OHL             Created
 * Apr 29 2002    Neo Sok Lay         Throw EntityModifiedException for setData().
 */
package com.gridnode.pdip.framework.db.ejb;

import javax.ejb.EJBLocalObject;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;

public interface IEntityLocalObject extends EJBLocalObject
{
	public IEntity getData();

	public void setData(IEntity data) throws EntityModifiedException;
}

