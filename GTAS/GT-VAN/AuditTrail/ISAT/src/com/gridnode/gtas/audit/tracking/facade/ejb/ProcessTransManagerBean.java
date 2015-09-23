/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessTransManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 26, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.facade.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.SessionContext;


import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.audit.dao.AuditTrailEntityDAO;
import com.gridnode.gtas.audit.dao.CommonResourceDAO;
import com.gridnode.gtas.audit.dao.ProcessTransactionDAO;
import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.model.ProcessTransaction;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.ProcessInfoHelper;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class handle the create/update of the entity Process Transaction. 
 * 
 * We seperate create/update portion out from the TrailInfoHandler
 * in order to determine unique key violation (the transaction will be commit after we finished executing the method within this class) in the TrailInfoHandler. 
 * Within the TrailInfoHandler can also perform necessary handling of that violation for example update.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ProcessTransManagerBean implements SessionBean
{
  private SessionContext _ctxt;
  private Logger _logger;
  private static final String CLASS_NAME = "ProcessTransManagerBean";
  
  /**
   * Perform creation of entity Process Transaction
   * @param processTrans
   */
  public void persistProcessTrans(ProcessTransaction processTrans)
  {
    ProcessTransactionDAO dao = new ProcessTransactionDAO();
    dao.insertAuditTrailEntity(processTrans);
  }
  
  /**
   * Perform update on Process Transaction given ProcessInfo
   * @param processInfo the process that emitted from OTC-Plug-in
   * @throws AuditTrailTrackingException
   */
  public boolean updateProcessTrans(ProcessInfo processInfo) throws AuditTrailTrackingException
  {
    String method = "updateProcessTrans";
    _logger.logMessage(method, null, "Updating process transaction with process uid "+processInfo.getProcessInstanceUID()+" using process info "+processInfo);
    
    ProcessTransactionDAO dao = new ProcessTransactionDAO();
    ProcessTransaction processTrans = dao.selectForUpdateProcessTransaction(processInfo.getProcessInstanceUID());
    
    if(processTrans != null)
    {
      handleProcessTransUpdate(processInfo, processTrans);
      dao.update(processTrans);
      _logger.logMessage(method, null, "End updated process transaction "+processTrans);
      return true;
    }
    else
    {
      _logger.logWarn(method, null, "Process transaction with process instance uid "+processInfo.getProcessInstanceUID()+" is not found", null);
      return false;
    }
    
  }
  
  /**
   * Perform update on Process Transaction given ProcessSummary, DocInfo
   * @param summary The ProcessSummary includes the info Pip name, Pip version, customer info, partner info etc
   * @param docInfo The document trans that emittend from OTC-plug-in
   * @param groupName the group that the docInfo correspond ProcessTransaction belong to.
   */
  public boolean updateProcessTrans(ProcessSummary summary, DocInfo docInfo, String groupName) throws AuditTrailTrackingException
  {
    String method = "updateProcessTrans";
    _logger.logMessage(method, null, "use docInfo: update process trans with docInfo "+docInfo);
    
    ProcessInfoHelper proHelper = new ProcessInfoHelper();
    
    ProcessTransaction processTrans = proHelper.selectProcessTransForUpdate(summary.getProcessInstanceUID());
    if(processTrans != null)
    {
      proHelper.updateProcessTransaction(summary, docInfo, processTrans, groupName);
      _logger.logMessage(method, null, "use docInfo: end updated Process Trans "+processTrans);
      return true;
    }
    else
    {
      _logger.logWarn(method, null, "Process transaction with process instance uid "+summary.getProcessInstanceUID()+" is not found", null);
      return false;
    }
  }
  
/**
   * Handling the update of the Process. Depending on the status of processInfo, we will perform update
   * on the existed process record in OTC. If the status of proTrans is running, we update the processState, processEndTime etc.
   * If the proTrans is not running, we will base on the process endTime of processInfo to perform the update. 
   * @param processInfo the process that emitted from OTC-Plug-in
   * @param proTrans The existing process in OTC
   */
  private void handleProcessTransUpdate(ProcessInfo processInfo, ProcessTransaction proTrans) throws AuditTrailTrackingException
  {
    String methodName = "handleProcessTransUpdate";
    ProcessInfoHelper proHelper = new ProcessInfoHelper();
    if(isProcessCompleted(proTrans))
    {
      _logger.logMessage(methodName, null, "ProcessInfo state is completed ... may update the ProcessTransaction .....");
      
      if(IAuditTrailConstant.PROCESS_STATE_RUNNING.equals(processInfo.getProcessStatus())) //ProcessInfo's status is running, not update of ProcessTrans is required.
      {
        _logger.logMessage(methodName, null, "ProcessTrans status is not running. Process Info state is running, not performing update.");
        return;
      }
      if(processInfo.getProcessEndTime() == null)
      {
        throw new AuditTrailTrackingException("Process ["+processInfo+"] status is "+processInfo.getProcessStatus()+"; Process end time is null");
      }
      Long processTransEndDate = proTrans.getProcessEndTime().getTime();
      
      Long processEndDate = processInfo.getProcessEndTime().getTime();
      if ((processTransEndDate < processEndDate) || "".equals(proTrans.getProcessStatus()))
      {
        _logger.logMessage(methodName, null, "Update to ProcessInfo "+ processInfo);
        proHelper.updateProcessTransaction(processInfo, proTrans);
      }
    }
    else
    {
      _logger.logMessage(methodName, null, "Process Transaction state is running or the state is not yet update, updating with ProcessInfo "+processInfo);
      
      proHelper.updateProcessTransaction(processInfo, proTrans);
    }
  }
  
  private boolean isProcessCompleted(ProcessTransaction proTrans)
  {
    String processStatus = proTrans.getProcessStatus();
    return processStatus != null && ! "".equals(proTrans.getProcessStatus()) && ! IAuditTrailConstant.PROCESS_STATE_RUNNING.equals(proTrans.getProcessStatus());
  }
  
  public void ejbCreate() throws CreateException
  {
    _logger = LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  
  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  
  public void ejbRemove() throws EJBException, RemoteException
  {
    
  }

  
  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }

}
