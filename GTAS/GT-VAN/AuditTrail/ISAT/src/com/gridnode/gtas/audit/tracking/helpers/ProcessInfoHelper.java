/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInfoHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 24, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.helpers;

import java.sql.Types;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.audit.dao.AuditTrailEntityDAO;
import com.gridnode.gtas.audit.dao.ProcessTransactionDAO;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;
import com.gridnode.gtas.audit.model.ProcessTransaction;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class provides the necessary methods that to be used by the TrailInfoHandler to 
 * perform creation, update, retrieve, and persistence for the ProcessTransaction.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ProcessInfoHelper extends AbstractInfoHelper
{
  private static ProcessTransactionDAO _dao = new ProcessTransactionDAO();
  private static String CLASS_NAME = "ProcessInfoHelper";
  
  public ProcessInfoHelper()
  {
    //super(_dao);
    _dao = new ProcessTransactionDAO(false);
  }
   
  /*
  public static ProcessInfoHelper getInstance()
  {
    return _helper;
  }*/
  
  protected AuditTrailEntityDAO getDAO()
  {
    return _dao;
  }
  
  public ProcessTransaction createProcessTransaction(ProcessInfo processInfo)
  {
    ProcessTransaction proTrans = new ProcessTransaction();
    //proTrans.setPipName("");
    //proTrans.setPipVersion("");
    proTrans.setProcessID(processInfo.getProcessID());
    proTrans.setProcessStartTime(processInfo.getProcessStartTime());
    proTrans.setProcessEndTime(processInfo.getProcessEndTime());
    //proTrans.setPartnerName("");
    //proTrans.setPartnerDuns("");
    //proTrans.setCustomerName("");
    //proTrans.setCustomerDuns("");
    proTrans.setProcessStatus(processInfo.getProcessStatus());
    proTrans.setErrorType(processInfo.getErrorType());
    proTrans.setErrorReason(processInfo.getErrorReason());
    proTrans.setProcessInstanceUID(processInfo.getProcessInstanceUID());
    //proTrans.setGroupName("");
    proTrans.setProcessSuccess(isProcessSuccess(processInfo.getProcessStatus()));
    
    return proTrans;
  }
  
  public ProcessTransaction createProcessTransaction(ProcessSummary summary, DocInfo docInfo, String groupName)
  {
    ProcessTransaction proTrans = new ProcessTransaction();
    proTrans.setPipName(summary.getPipName());
    proTrans.setPipVersion(summary.getPipVersion());
    //proTrans.setProcessID("");
    proTrans.setPartnerDuns(summary.getTradingPartnerDuns());
    proTrans.setPartnerName(summary.getTradingPartnerName());
    proTrans.setCustomerDuns(summary.getCustomerDuns());
    proTrans.setCustomerName(summary.getCustomerName());
    //proTrans.setProcessStatus("");
    proTrans.setProcessInstanceUID(summary.getProcessInstanceUID());
    proTrans.setGroupName(groupName);
    proTrans.setUserTrackingID(docInfo.getUserTrackingID());
    
    proTrans.setInitiator(IAuditTrailConstant.SELF_INITIATOR.equals(summary.getProcessInitiatorID()));
    
    setResponseDocNo(docInfo.getDocumentDirection(), docInfo.getDocNo(), summary.getProcessInitiatorID(), proTrans);
    setRequestDocNo(docInfo.getDocumentDirection(), docInfo.getDocNo(), summary.getProcessInitiatorID(), proTrans);
    
    return proTrans;
  }
  
  public void updateProcessTransaction(ProcessSummary summary, DocInfo docInfo, ProcessTransaction proTrans, String groupName)
  {
      proTrans.setPipName(summary.getPipName());
      proTrans.setPipVersion(summary.getPipVersion());
      proTrans.setPartnerDuns(summary.getTradingPartnerDuns());
      proTrans.setPartnerName(summary.getTradingPartnerName());
      proTrans.setCustomerDuns(summary.getCustomerDuns());
      proTrans.setCustomerName(summary.getCustomerName());
      //proTrans.setProcessInstanceUID(summary.getProcessInstanceUID());
      proTrans.setGroupName(groupName);
      proTrans.setInitiator(IAuditTrailConstant.SELF_INITIATOR.equals(summary.getProcessInitiatorID()));
      proTrans.setUserTrackingID(docInfo.getUserTrackingID());
      
      String processInitiator = summary.getProcessInitiatorID();
      
      String docDirection = docInfo.getDocumentDirection();
      
      setResponseDocNo(docInfo.getDocumentDirection(), docInfo.getDocNo(), summary.getProcessInitiatorID(), proTrans);
      setRequestDocNo(docInfo.getDocumentDirection(), docInfo.getDocNo(), summary.getProcessInitiatorID(), proTrans);
      
      System.out.println("Using Process Summary "+summary+" updating process trans "+proTrans);
      updateAuditTrailEntity(proTrans);
      System.out.println("End update process trans "+proTrans);
  }
  
  public void updateProcessTransaction(ProcessInfo proInfo, ProcessTransaction proTrans)
  {
    proTrans.setProcessStartTime(proInfo.getProcessStartTime());
    proTrans.setProcessEndTime(proInfo.getProcessEndTime());
    proTrans.setProcessStatus(proInfo.getProcessStatus());
    proTrans.setErrorType(proInfo.getErrorType());
    proTrans.setErrorReason(proInfo.getErrorReason());
    proTrans.setProcessID(proInfo.getProcessID());
    proTrans.setProcessSuccess(isProcessSuccess(proInfo.getProcessStatus()));
    
    updateAuditTrailEntity(proTrans);
  }
  
  public ProcessTransaction getProcessTransaction(Long uid) throws AuditTrailTrackingException
  {
    try
    {
      return _dao.retrieveProTransactionByProcessInstanceUID(uid);
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Failed to get ProcessTransaction given for process UID "+uid, ex);
    }
  }
  
  public ProcessTransaction selectProcessTransForUpdate(Long uid) throws AuditTrailTrackingException
  {
    try
    {
      return _dao.selectForUpdateProcessTransaction(uid);
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Failed to get ProcessTransaction given for process UID "+uid, ex);
    }
  }
  
  
  public void insertProcessTransaction(ProcessTransaction processTrans) throws Exception
  {    
    _dao.insertProcessTrans(processTrans);
  }
  
  public boolean isProcessTransConstraintViolated(Exception ex)
  {
    return _dao.isConstraintViolation(ex);
  }
  
  private boolean isProcessSuccess(String processStatus)
  {
    return IAuditTrailConstant.PROCESS_STATE_COMPLETED.equals(processStatus);
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  private void setRequestDocNo(String docDirection, String docNo, String processInitiator, ProcessTransaction proTrans)
  {
    if(IAuditTrailConstant.DIRECTION_OB.equals(docDirection) && IAuditTrailConstant.SELF_INITIATOR.equals(processInitiator))
    {
      proTrans.setRequestDocNo(docNo);
    }
    
    else if(IAuditTrailConstant.DIRECTION_IB.equals(docDirection) && ! IAuditTrailConstant.SELF_INITIATOR.equals(processInitiator))
    {
      proTrans.setRequestDocNo(docNo);
    }
  }
  
  private void setResponseDocNo(String docDirection, String docNo, String processInitiator, ProcessTransaction proTrans)
  {
    if(IAuditTrailConstant.DIRECTION_IB.equals(docDirection) && IAuditTrailConstant.SELF_INITIATOR.equals(processInitiator))
    {
      proTrans.setResponseDocNo(docNo);
    }
    
    else if(IAuditTrailConstant.DIRECTION_OB.equals(docDirection) && ! IAuditTrailConstant.SELF_INITIATOR.equals(processInitiator))
    {
      proTrans.setResponseDocNo(docNo);
    }
  }
}
