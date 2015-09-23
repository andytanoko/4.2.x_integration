/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 9 9 2002	MAHESH              Created
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;

public interface IGWFRtProcessDocHome  extends EJBHome {

	public IGWFRtProcessDocObj create(IEntity data) throws CreateException,RemoteException;
	public IGWFRtProcessDocObj findByPrimaryKey(Long primaryKey) throws FinderException,RemoteException;
	public Collection findByFilter(IDataFilter filter) throws FinderException,RemoteException;
}