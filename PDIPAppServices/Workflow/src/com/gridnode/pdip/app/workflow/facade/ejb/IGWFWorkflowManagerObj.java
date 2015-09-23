/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    MAHESH              Created
 * Jul 11 2003    Neo Sok Lay         Add method: getRtProcessDocList(IDataFilter)
 * Jul 15 2003    Neo Sok Lay         Add method: getProcessInstanceKeys(IDataFilter)
 */
package com.gridnode.pdip.app.workflow.facade.ejb;


import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.app.workflow.exceptions.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.db.filter.*;

public interface IGWFWorkflowManagerObj extends EJBObject {

    public Long createRtProcess(String processDefKey, Long rtActivityUId,HashMap contextData) throws WorkflowException,SystemException,RemoteException;
    public Long createRtProcess(Long processUId, String processType, Long rtActivityUId,HashMap contextData,String engineType) throws WorkflowException,SystemException,RemoteException;
    public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId,String branchName,Long contextUId,String engineType) throws WorkflowException,SystemException,RemoteException;
    public void changeProcessState(Long rtProcessUId,int state,HashMap contextData) throws WorkflowException,SystemException,RemoteException;
    public void changeActivityState(Long rtActivityUId,int state,HashMap contextData) throws WorkflowException,SystemException,RemoteException;

    public void worklistCallback(Long rtActivityUId,int state,String engineType) throws WorkflowException,SystemException,RemoteException;
    
    //For BPSS  
    public void insertDocument(String documentId,String documentType,Object documentObject,String senderKey) throws WorkflowException,SystemException,RemoteException;
    public void insertDocument(String documentId,String documentType,Object documentObject,String senderKey,String initiatorPartnerKey,String responderPartnerKey) throws WorkflowException,SystemException,RemoteException;
    public void insertSignal(String documentId,String signalType,Object reason,String senderKey) throws WorkflowException,SystemException,RemoteException;

    public Collection getXpdlProcessInstanceList(String processId,String packageId,String pkgVersionId) throws WorkflowException,SystemException,RemoteException;
    public Collection getBpssProcessInstanceList(String processId,String processType,String processSpecName,String processSpecVersion,String processSpecUUid) throws WorkflowException,SystemException,RemoteException;
	  public Collection getBpssProcessInstanceKeys(String processId,String processType,String processSpecName,String processSpecVersion,String processSpecUUid) throws WorkflowException,SystemException,RemoteException;
    public GWFRtProcess getProcessInstance(Long rtProcessUId) throws WorkflowException,SystemException,RemoteException;
    public Collection getProcessInstanceList(IDataFilter filter) throws WorkflowException,SystemException,RemoteException;
	  public Collection getProcessInstanceKeys(IDataFilter filter) throws WorkflowException,SystemException,RemoteException;
    public void cancelProcessInstance(Long rtProcessUId,Object reason) throws WorkflowException,SystemException,RemoteException;
    public void removeProcessInstance(Long rtProcessUId) throws WorkflowException,SystemException,RemoteException;
    public Collection getRtProcessDocList(Long rtProcessUId) throws WorkflowException,SystemException,RemoteException;
    public Collection getRtProcessDocList(IDataFilter filter) throws WorkflowException,SystemException,RemoteException;

}
