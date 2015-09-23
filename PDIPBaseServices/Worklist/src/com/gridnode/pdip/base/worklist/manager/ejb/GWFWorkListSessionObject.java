package com.gridnode.pdip.base.worklist.manager.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;
import com.gridnode.pdip.base.worklist.exception.WLAssignmentException;
import com.gridnode.pdip.base.worklist.exception.WorklistException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;

public interface GWFWorkListSessionObject extends EJBObject{

    public void addWorkItem(GWFWorkListValueEntity entity) throws SystemException,RemoteException;

    public void dropWorkItem(Long workItemUId) throws WorklistException,SystemException,RemoteException;

    public void dropWorkItems(Collection workItemUIdColl) throws SystemException,RemoteException;

    public void performWorkItem(Long workItemUId) throws WorklistException,SystemException,RemoteException;

    public void assignWorkItem(Long workItemUId,String user) throws WorklistException,WLAssignmentException,SystemException,RemoteException;

    public void rejectWorkItem(Long workItemUId,String comments) throws WorklistException,WLAssignmentException,SystemException,RemoteException;

    public GWFWorkListValueEntity getWorkItem(Long workItemUId) throws WorklistException,SystemException,RemoteException;

    public Collection getWorkItemsByUser(String userName) throws SystemException,RemoteException;

    public Collection getWorkItemsByActivity(String processDefKey,String activityId) throws SystemException,RemoteException;

    public Collection getWorkItemsByPerformer(String performer) throws SystemException,RemoteException;

    public Collection getWorkItemsByRtActivity(Long rtActivityUId) throws SystemException,RemoteException;

    public Collection getWorkItemsByFilter(IDataFilter filter) throws SystemException,RemoteException;

/*
    public void placeWorkItem(GWFWorkListValueEntity entity) throws RemoteException;
    public Vector getWorkList(String user) throws RemoteException;
    public void clearWorkList(Vector entity) throws RemoteException;
    public Vector assignUnAssignedWorkItems(Vector users) throws RemoteException;
*/


}
