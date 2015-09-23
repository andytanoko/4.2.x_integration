/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June  27 2002	MAHESH              Created
 */
package com.gridnode.pdip.base.data.entities.ejb;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;

public interface IBusinessDocumentHome  extends EJBHome {

	public IBusinessDocumentObject create(IEntity data) throws CreateException,RemoteException;
	public IBusinessDocumentObject findByPrimaryKey(Long primaryKey) throws FinderException,RemoteException;
	public Collection findByFilter(IDataFilter filter) throws FinderException,RemoteException;
}