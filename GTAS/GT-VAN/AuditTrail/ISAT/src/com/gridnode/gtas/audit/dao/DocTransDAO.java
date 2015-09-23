/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocTransDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 4,  2006    Tam Wei Xiang       Created
 * Jan 10, 2007    Tam Wei Xiang       Remove the rollback(). The client that
 *                                     use the DAO should rollback itself either
 *                                     through container rollback or we explicityly
 *                                     rollback.
 */
package com.gridnode.gtas.audit.dao;

import java.util.Collection;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;
import com.gridnode.gtas.audit.model.DocumentTransaction;

/**
 * This class handle the CRUD operation for the entity DocumentTransaction.
 * NOTE: the caller require to under the JTA transaction. EG call from SessionBean, MDBean
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class DocTransDAO extends AuditTrailEntityDAO
{
  private static final String CLASS_NAME = "DocTransDAO";
  
  public DocTransDAO()
  {
    super(false);
  }
  
  public DocTransDAO(boolean newSession)
  {
    super(newSession);
  }
  
  /**
   * Retrieve a collection of documentTransaction based on tracingID and direction.
   * @param tracingID
   * @param direction
   * @return
   * @throws AuditTrailDBServiceException
   */
  public Collection<DocumentTransaction> retrieveDocumentTrans(String tracingID, String direction) throws AuditTrailDBServiceException
  {
    String queryName = getPersistenceClass().getName()+".getDocTrans";
    String[] paramNames = new String[]{"tracingID", "direction"};
    String[] paramValues = new String[]{tracingID, direction};
    
    try
    {
      Collection<DocumentTransaction> docTrans = query(queryName,paramNames, paramValues);
      return docTrans;
    }
    catch(Exception ex)
    {
      throw new AuditTrailDBServiceException("["+CLASS_NAME+".retrieveDocumentTrans]Error in retrieving DocumentTransaction entity given criteria [tracingID :"+tracingID +" direction :"+direction+"]", ex);
    }
  }
  
  /**
   * Retrieve a collection of tracingID based on tracingID and msgID
   * @param tracingID
   * @param msgID
   * @return
   * @throws AuditTrailDBServiceException
   */
  public Collection<DocumentTransaction> retrieveDocTransTracingIDAndMsgID(String tracingID, String msgID) throws AuditTrailDBServiceException
  {
    String queryName = getPersistenceClass().getName()+".getTranDocsByTraceIDAndMsgID";
    String[] paramNames = new String[]{"tracingID", "messageID"};
    String[] paramValues = new String[]{tracingID, msgID};
    
    try
    {
      Collection<DocumentTransaction> docTrans = query(queryName,paramNames, paramValues);
      return docTrans;
    }
    catch(Exception ex)
    {
      throw new AuditTrailDBServiceException("["+CLASS_NAME+".retrieveDocumentTrans]Error in retrieving DocumentTransaction entity given criteria [tracingID :"+tracingID +" msgID :"+msgID+"]", ex);
    }
  }
  
  /**
   * Retrieve a single document transaction. Use only if we ensure it will only return one single doc trans
   * @param tracingID
   * @param direction
   * @return
   * @throws AuditTrailDBServiceException
   */
  public DocumentTransaction retrieveSingleDocumentTrans(String tracingID, String direction) throws AuditTrailDBServiceException
  {
    String queryName = getPersistenceClass().getName()+".getDocTrans";
    String[] paramNames = new String[]{"tracingID", "direction"};
    String[] paramValues = new String[]{tracingID, direction};
    
    try
    {
      DocumentTransaction docTrans = (DocumentTransaction)queryOne(queryName,paramNames, paramValues);
      return docTrans;
    }
    catch(Exception ex)
    {
      throw new AuditTrailDBServiceException("["+CLASS_NAME+".retrieveSingleDocumentTrans]Error in retrieving DocumentTransaction entity given criteria [tracingID :"+tracingID +" direction :"+direction+"]", ex);
    }
  }
  
  /**
   * Get the total number of document trans available given tracingID
   * @param tracingID
   * @return
   * @throws AuditTrailDBServiceException
   */
  public int getDocumentTransCount(String tracingID)
  {
    String queryName = getPersistenceClass().getName()+".getDocTransCount";
    String[] paramNames = new String[]{"tracingID"};
    String[] paramValues = new String[]{tracingID};
    Long docTransCount = (Long)queryOne(queryName, paramNames, paramValues);
    return (docTransCount == null ? 0 : docTransCount.intValue());
  }
  
  public int getActionDocTransCountByDirectionProcessUID(String direction, String tracingID, Long processUID) throws AuditTrailDBServiceException
  {
    String queryName = getPersistenceClass().getName()+".getActionDocTransCount";
    String[] paramNames = new String[]{"docTypeSigAck", "docTypeSigExp", "direction", "tracingID", "processInstanceUID"};
    Object[] paramValues = new Object[]{IAuditTrailConstant.SIGNAL_MESSAGE_ACK, IAuditTrailConstant.SIGNAL_MESSAGE_EXP, direction, tracingID, processUID};
    
    try
    {
      Long docTransCount = (Long)queryOne(queryName, paramNames, paramValues);
      return (docTransCount == null ? 0 : docTransCount.intValue());
    }
    catch(Exception ex)
    {
      throw new AuditTrailDBServiceException("["+CLASS_NAME+".getActionDocTransCountByDirection]Error in getting the total count for Action DocumentTransaction given criteria[tracingID: "+tracingID+", direction: "+direction+"]", ex);
    }
  }
  
  /**
   * Obtain a list of document transaction given tracingID, direction, and indicate whether is signal msg
   * @param direction
   * @param tracingID
   * @param isSignal indicate whether we fetch the signal msg or the action msg
   * @return
   */
  public Collection<DocumentTransaction> getDocTransByActionType(String direction, String tracingID, boolean isSignal)
  {
    String queryName = getPersistenceClass().getName()+".getDocTransByActionType";
    String[] paramNames = new String[]{"tracingID", "direction", "isSignal"};
    Object[] paramValues = new Object[]{tracingID, direction, isSignal};
    
    return query(queryName, paramNames, paramValues);
  }
  
  /**
   * Return a collection of DocumentTransaction entity given the process instance UID.
   * @param processInstanceUID The uid for identified a process in GT.
   * @return
   */
  public Collection<DocumentTransaction> getDocTransByProcessInstanceUID(Long processInstanceUID)
  {
    String queryName = getPersistenceClass().getName()+".getDocumentTransByProcessInstanceUID";
    String[] paramNames = new String[]{"processInstanceUID"};
    Object[] paramValues = new Object[]{processInstanceUID};
    return query(queryName, paramNames, paramValues);
  }
  
  /**
   * Get the DocumentTransaction which IS NOT associated to the ProcessTransaction as identified by the processInstanceUID
   * @param tracingID 
   * @param processInstanceUID
   * @return
   */
  public DocumentTransaction getDocTransByTracingID(String tracingID, Long processInstanceUID)
  {
    String queryName = getPersistenceClass().getName()+".getDocumentTransByTracingIDAndProcessInstanceUID";
    String[] paramNames = new String[]{"tracingID", "processInstanceUID"};
    Object[] paramValues = new Object[]{tracingID, processInstanceUID};
    return (DocumentTransaction)queryOne(queryName, paramNames, paramValues);
  }
  
  public void updateDocumentTransaction(DocumentTransaction docTrans)
  {
    update(docTrans);
  }
  
  /**
   * Return the Persistence Class that this DAO is handling.
   */
  public Class getPersistenceClass()
  {
    return DocumentTransaction.class;
  }
}
