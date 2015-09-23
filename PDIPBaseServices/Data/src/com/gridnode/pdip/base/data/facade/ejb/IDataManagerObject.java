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
package com.gridnode.pdip.base.data.facade.ejb;

import java.util.*;

import javax.ejb.*;
import java.rmi.*;
import com.gridnode.pdip.base.data.entities.model.*;
import com.gridnode.pdip.base.data.facade.exceptions.*;

public interface IDataManagerObject extends EJBObject {
    public Long createContextUId() throws DataException, RemoteException;
    public String createData(IData data) throws DataException, RemoteException;
    public String createDataDefinition(IDataDefinition dataDefinition) throws DataException, RemoteException;
    public DataItem getContextData(Long contextUId,String dataDefName,String dataDefType) throws DataException, RemoteException;
    public IDataDefinition getDataDefinition(String dataDefName,String dataDefType) throws DataException, RemoteException;


    public Collection getContextData(Long contextUId) throws DataException, RemoteException;
    public IDataDefinition getDataDefinition(String dataDefKey) throws DataException, RemoteException;
    public DataItem getDataItem(Long dataItemUId) throws DataException, RemoteException;
    public Map getFieldDataLocationMaps(Long dataItemUId) throws DataException, RemoteException;

    public void removeContextData(Long contextUId) throws RemoteException;
    public DataItem createDataItem(String dataDefKey,String dataContextType,Long contextUId,boolean isCopy) throws RemoteException;
    public DataItem createDataItem(Long dataItemUId, String dataContextType, Long contextUId) throws RemoteException;
    public void removeDataItem(Long dataItemUId) throws RemoteException;
    public void saveToMaster(Long dataItemUId) throws RemoteException;

    public Object getObjectData(String dataLocation) throws DataException, RemoteException;
    public void  setObjectData(String dataLocation,Object obj) throws DataException, RemoteException;
}
