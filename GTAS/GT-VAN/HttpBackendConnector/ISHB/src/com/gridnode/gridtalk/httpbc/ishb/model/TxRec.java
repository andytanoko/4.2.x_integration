/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TxRec.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 10, 2006   i00107              Created
 * Jan 05 2007    i00107              Extend from AbstractPersistable
 *                                    TxNo is now alias for UID
 *                                    Remove query for incrementAttemptCount
 *                                    Add incrementAttemptCount().
 * Feb 06 2007    i00107              Modify query "getNextTx" to retrieve based on attemptCount.                                   
 */

package com.gridnode.gridtalk.httpbc.ishb.model;

import java.util.Date;

import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.util.db.AbstractPersistable;

/**
 * @author i00107
 * @hibernate.class table = "\"httpbc_txrec\"" optimistic-lock = "version"
 * @hibernate.query name = "getNextTxWithAttemptCount" 
 *   query = "from TxRec as tx where tx.direction = :direction and (tx.attemptCount < :maxAttempts) order by tx.receiveTs asc"
 * @hibernate.query name = "getNextTx" 
 *   query = "from TxRec as tx where tx.direction = :direction order by tx.receiveTs asc"
 *//*   
 * @hibernate.mapping schema = "httpbc"
 * @hibernate.query name = "incrementAttemptCount" 
 *   query = "update TxRec set attemptCount = attemptCount + 1 where UID = :TxNo"  
 */
public class TxRec extends AbstractPersistable
{
//  private Long _txNo;
  private int _attemptCount;
  private String _direction;
  private String _tracingId;
  private TransactionDoc _txDoc;
  private Date _receiveTs = new Date();
  
  public TxRec()
  {
    
  }

  /**
   * @return Returns the attemptCount.
   * @hibernate.property column = "\"attempt_count\""
   */
  public int getAttemptCount()
  {
    return _attemptCount;
  }

  /**
   * @param attemptCount The attemptCount to set.
   */
  public void setAttemptCount(int attemptCount)
  {
    _attemptCount = attemptCount;
  }

  /**
   * Increment the attempt count by 1.
   */
  public void incrementAttemptCount()
  {
    _attemptCount++;
  }
  /**
   * @return Returns the direction.
   * @hibernate.property column = "\"direction\"" not-null = "true"
   */
  public String getDirection()
  {
    return _direction;
  }

  /**
   * @param direction The direction to set.
   */
  public void setDirection(String direction)
  {
    _direction = direction;
  }

  /**
   * @return Returns the tracingId.
   * @hibernate.property column = "\"tracing_id\"" not-null = "true"
   */
  public String getTracingId()
  {
    return _tracingId;
  }

  /**
   * @param tracingId The tracingId to set.
   */
  public void setTracingId(String tracingId)
  {
    _tracingId = tracingId;
  }

  /**
   * @return Returns the txDoc.
   * @hibernate.property column = "\"tx_doc\"" type = "serializable" not-null = "true"
   */
  public TransactionDoc getTxDoc()
  {
    return _txDoc;
  }

  /**
   * @param txDoc The txDoc to set.
   */
  public void setTxDoc(TransactionDoc txDoc)
  {
    _txDoc = txDoc;
  }

//  /**
//   * @return Returns the txNo.
//   * @hibernate.generator-param name = "sequence" value = "txno_seq"
//   * @hibernate.id generator-class = "sequence" column = "txno"
//   */
//  public Long getTxNo()
//  {
//    return _txNo;
//  }

  /**
   * The timestamp that the tx is received
   * @return the receiveTs
   * @hibernate.property column = "\"receive_ts\"" not-null = "true"
   */
  public Date getReceiveTs()
  {
    return _receiveTs;
  }

  /**
   * @param receiveTs the receiveTs to set
   */
  public void setReceiveTs(Date receiveTs)
  {
    _receiveTs = receiveTs;
  }

  /*
   * @hibernate.id generator-class = "assigned" column = "uid" name="uid"
   * @hibernate.version column = "version" unsaved-value = "null"
   */
  public String getTxNo()
  {
    return getUID();
  }

//  /**
//   * @param txNo The txNo to set.
//   */
//  public void setTxNo(Long txNo)
//  {
//    _txNo = txNo;
//  }
  
  public String toString()
  {
    return "TxRec:["+getTxNo()+"/"+getReceiveTs()+"/"+getTracingId()+"/"+getDirection()+"/"+getAttemptCount()+"/"+getTxDoc()+"]";
  }
  
}
