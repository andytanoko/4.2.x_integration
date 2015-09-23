/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailSessionBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 4, 2007    Tam Wei Xiang       Created
 * Feb 5, 2007    Tam Wei Xiang       Associate the transaction to Hibernate transaction
 *                                    instead of using the UserTransaction
 * Mar 2, 2007    Tam Wei Xiang       Added in timestamp for AuditTrailData.  
 * Jul 29,2007    Tam Wei Xiang       #69: Enable the TXMR to handle the AuditTrailData
 *                                         that maybe processed before. This can happen
 *                                         when the JMS msg redelivered mechanism kick in.
 *                                         By allowing handling of the redelivered msg,
 *                                         the TXMR event can survive during the Clustering
 *                                         fail over kick in.                                 
 */
package com.gridnode.gtas.audit.tracking.facade.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.SessionContext;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;


import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.gtas.audit.common.model.ITrailInfo;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.gtas.audit.dao.AuditTrailDataRecordDAO;
import com.gridnode.gtas.audit.model.AuditTrailDataRecord;
import com.gridnode.gtas.audit.tracking.TrailInfoHandler;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.db.DAO;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to handle the AuditTrail Data emitted from GT.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailManagerBean implements SessionBean
{
  private static final String CLASS_NAME = "AuditTrailManagerBean";
  transient private SessionContext _sessionCtx = null;
  private Logger _logger = null;
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  }
  
  public void ejbPassivate() throws EJBException, RemoteException
  {
  }
  
  public void ejbCreate() throws CreateException
  {
    _logger = getLogger();
  }
  
  public void ejbRemove() throws EJBException, RemoteException
  {
  
  }

  public void setSessionContext(SessionContext sesionCtx) throws EJBException,
                                                    RemoteException
  {
    _sessionCtx = sesionCtx;
  }
  
  /**
   * Delegate the processing of AuditTrailData to TrailInfoHandler which in turn will process the AuditTrailData
   * and persist in DB.
   * @param trailData
   * @throws Exception
   */
  public void handleAuditTrailData(AuditTrailData trailData) throws AuditTrailTrackingException
  {
    String methodName = "handleAuditTrailData";
    
    DAO dao = new DAO();
    dao.associateTransactionContext(false);
    
    try
    { 
      System.out.println("GT trans start handling audit trail data....");
      _logger.logMessage(methodName, null, "GT trans start handling audit trail data....");
      dao.beginTransaction();
      
      TrailInfoHandler.getInstance().processTrailInfo(trailData);

      dao.commitTransaction();
      
      dao.closeTransactionContext();
      trailData = null;
    }
    catch(AuditTrailTrackingException ex)
    {
      ex.printStackTrace();
      _logger.logWarn(methodName, null, "GT trans Error in handling the AuditTrailData "+ex.getMessage(), ex);
      
      
      if(dao.hasTransaction())
      {
        System.out.println("GT trans: current hibernate contain active transaction, rolling back");
        _logger.logMessage(methodName, null, "current hibernate contain active transaction, rolling back ");
        dao.rollbackTransaction();
      }
      throw ex;
    }
  }
  
  /**
   * #69 Handle the potentially processed AuditTrailData. The internal handler will determine if a particular AuditTrailData
   *  has processed before.
   * @param trailData
   * @throws AuditTrailTrackingException
   */
  public void handleRedeliveredAuditTrailData(AuditTrailData trailData) throws AuditTrailTrackingException
  {
    String method = "handleRedeliveredAuditTrailData";
    //note: actually the DAO handling in method "handleAuditTrailData(AuditTrailData trailData)" is not necessary since we tie the Hibernate
    //      transaction directly to the container JTA.
    
    _logger.logMessage(method, null, "Start handling redelivered trail data");
    TrailInfoHandler.getInstance().processRedeliveredTrailInfo(trailData);
  }
  
  public void handleAuditTrailData(String trailDataUID) throws AuditTrailTrackingException
  {
    String methodName = "handleAuditTrailData";
    _logger.logMessage(methodName, null,CLASS_NAME+" START handling trail data ....");
    
    AuditTrailDataRecordDAO dao = new AuditTrailDataRecordDAO();
    dao.associateTransactionContext(false);
    dao.beginTransaction();
    
    AuditTrailDataRecord dataRecord = dao.retrieveAuditTrailDataRecordByUID(trailDataUID);
    
    if(dataRecord != null)
    {
      _logger.logMessage(methodName, null,CLASS_NAME+" data record retrieve is "+ dataRecord.toString());
      try
      { 
        TrailInfoHandler.getInstance().processTrailInfo(dataRecord.getObj());
        dao.delete(dataRecord);
        dao.commitTransaction();
        
        dao.closeTransactionContext();
        _logger.logMessage(methodName, null, CLASS_NAME+" end handling trail data.....");
      }
      catch(AuditTrailTrackingException ex)
      {
        ex.printStackTrace();
        _logger.logWarn(methodName, null, "Error in handling the AuditTrailData "+ex.getMessage(), ex);
        
        if(dao.hasTransaction())
        {
          System.out.println("GT trans handle Trail Data: current hibernate contain active transaction, rolling back");
          _logger.logMessage(methodName, null, "GT Trans handle Trail Data current hibernate contain active transaction, rolling back ");
          dao.rollbackTransaction();
        }
        else
        {
          System.out.println("handleAuditTrailData: GT trans no more transaction");
        }
        
        throw ex;
      }
    }
  }
  
  /**
   * In clustering environment, the JMS msg will be delivered to the available node in the clusther based on
   * load-balance mode. And we want to enforce the processing of the AuditTrailData should not be in paraller; thus
   * we delay the processing of the AuditTrail data by persisting it in second storage first, after that a scheduler will
   * invoke a MBean service to perform the actual handling of the AuditTrail data which ensure we process the msg by sequence.
   * @param auditTrailData
   */
  public void delayProcessAuditTrailData(AuditTrailData auditTrailData)
  {
    String method = "delayProcessAuditTrailData";
    _logger.logMessage(method, null, "Inserting AuditTrailData Record .....");
    trailDataToInsertIs(auditTrailData);
    
    AuditTrailDataRecord objContainer = new AuditTrailDataRecord(auditTrailData);
    objContainer.setLastModifiedDate(new Date());
    
    AuditTrailDataRecordDAO objDAO = new AuditTrailDataRecordDAO();
    
    objDAO.associateTransactionContext(false);
    
    try
    {
      objDAO.beginTransaction();
      
      objDAO.persistAuditTrailData(objContainer);
      
      objDAO.commitTransaction();
      
      objDAO.closeTransactionContext();
    }
    catch(Exception ex)
    {
      _logger.logMessage(method, null, "Error delaying the audit trail data "+ex.getMessage());
      ex.printStackTrace();
      
      if(objDAO.hasTransaction())
      {
        objDAO.rollbackTransaction();
      }
      else
      {
        System.out.println("delayProcessAuditTrailData: GT trans no more transaction");
      }
    }
  }
  
  private void trailDataToInsertIs(AuditTrailData trailData)
  {
    String method = "trail info type";
    
    if(trailData == null)
    {
      _logger.logMessage(method, null, CLASS_NAME+ " trail data is null !!!");
      return ;
    }
    
    ITrailInfo trailInfo = trailData.getTrailInfo();
    if(trailInfo != null)
    {
      if(trailInfo instanceof DocInfo)
      {
        _logger.logMessage(method, null,CLASS_NAME+ "Importing audit trail data is DOC INFO "+((DocInfo)trailInfo));
      }
      else if(trailInfo instanceof EventInfo)
      {
        _logger.logMessage(method, null,CLASS_NAME+ "Importing audit trail data is EventInfo "+ (EventInfo)trailInfo);
      }
      else if(trailInfo instanceof ProcessInfo)
      {
        _logger.logMessage(method, null,CLASS_NAME+ "Importing audit trail data is EventInfo "+ (ProcessInfo)trailInfo);
      }
      else
      {
        _logger.logMessage(method, null,CLASS_NAME+ "Unsupported audit trail data type detected ..");
      }
    }
    else
    {
      _logger.logMessage(method, null,CLASS_NAME+ "IAuditTrail Data is null !!");
    }
  }
  
  public void updateAuditTrailRecord(String trailDataRecordUID, String exceptionMsg) throws Exception
  {
    String methodName = "updateAuditTrailRecord";
    _logger.logMessage(methodName, null, "Start updating fail audit record for UID ....."+trailDataRecordUID);
    System.out.println("Start updating fail audit record for UID ....."+trailDataRecordUID);
    AuditTrailDataRecordDAO dao = new AuditTrailDataRecordDAO(false);
    
    dao.associateTransactionContext(false);
    
    try
    {
      dao.beginTransaction();
    
      AuditTrailDataRecord dataRecord = dao.retrieveAuditTrailDataRecordByUID(trailDataRecordUID);
      dataRecord.incrementAttemptCount();
      dataRecord.setFailedReason(exceptionMsg);
      dataRecord.setLastModifiedDate(new Date());
      dao.update(dataRecord);
    
      dao.commitTransaction();
      
      dao.closeTransactionContext();
      System.out.println("end updating fail audit record .....");
      _logger.logMessage(methodName, null, "end updating fail audit record .....for UID ....."+trailDataRecordUID);
    }
    catch(Exception ex)
    {
      _logger.logMessage(methodName,null, "Can't updateAuditTrailRecord "+ex.getMessage());
      ex.printStackTrace();
      
      if(dao.hasTransaction())
      {
        dao.commitTransaction();
      }
      else
      {
        System.out.println("updateAuditTrailRecord: GT trans no more transaction");
      }
    }
  }
  
  /**
   * Delete the AuditTrailData if we successfully process the trailDataRecord.
   * @param trailDataRecord
   * @param dao
   */
  public void deleteAuditTrailData(AuditTrailDataRecord trailDataRecord)
  {
    AuditTrailDataRecordDAO dao = new AuditTrailDataRecordDAO();
    dao.delete(trailDataRecord);
  }
  
  public List<String> retrieveAuditTrailDataRecordUID(int maxAttemptCount, int totalRecordToFetch) throws Exception
  {
    String methodName = "retrieveAuditTrailDataRecordUID";
    AuditTrailDataRecordDAO dao = new AuditTrailDataRecordDAO();
    
    dao.associateTransactionContext(false);
    
    try
    {
      dao.beginTransaction();
    
      List<String> trailDataRecord = dao.retrieveNAuditTrailDataByMaxAttemptCount(maxAttemptCount, totalRecordToFetch);
      
      dao.commitTransaction();
      
      dao.closeTransactionContext();
      return trailDataRecord;
    }
    catch(Exception ex)
    {
      _logger.logMessage(methodName,null, "Can't retrieve audit trail data record "+ex.getMessage());
      ex.printStackTrace();
      
      if(dao.hasTransaction())
      {
        dao.rollbackTransaction();
      }
      else
      {
        System.out.println("retrieveAuditTrailDataRecordUID: GT trans no more transaction");
      }
      
      throw ex;
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
}
