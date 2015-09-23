/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002   Mahesh              Created
 *
 */
package com.gridnode.pdip.base.contextdata.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.contextdata.facade.exceptions.DataException;
import com.gridnode.pdip.framework.exceptions.SystemException;
 
public interface IDataManagerObj extends EJBObject {

    public Long createContextUId() throws DataException, RemoteException;

    public Long createContextUId(HashMap contexDataMap) throws DataException, RemoteException;

    public void setContextData(Long contextUId,ContextKey key,Object objData) throws DataException,SystemException, RemoteException;

    public void setContextData(Long contextUId,HashMap contexDataMap) throws DataException,SystemException, RemoteException;

    public Object getContextData(Long contextUId,ContextKey key) throws DataException,SystemException, RemoteException;

    public HashMap getContextData(Long contextUId) throws DataException,SystemException, RemoteException;

    public Collection getContextKeys(Long contextUId) throws DataException,SystemException, RemoteException;

    public void removeContextUId(Long contextUId) throws DataException,SystemException, RemoteException;

    public void removeContextData(Long contextUId) throws DataException,SystemException, RemoteException;

    public void removeContextData(Long contextUId,Collection contextKeyColl) throws DataException,SystemException, RemoteException;

    public void removeContextData(Long contextUId,ContextKey key) throws DataException,SystemException, RemoteException;
}
