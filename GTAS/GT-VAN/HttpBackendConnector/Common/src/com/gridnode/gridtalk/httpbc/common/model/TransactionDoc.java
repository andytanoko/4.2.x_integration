/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransactionDoc.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 05 2007    i00107              Add MessageID
 */

package com.gridnode.gridtalk.httpbc.common.model;

import java.io.Serializable;

/**
 * @author i00107
 * This class serves as the data holder for a transaction document and its relevant
 * information.
 */
public class TransactionDoc implements Serializable
{
  public static final String DIRECTION_IN = "In";
  public static final String DIRECTION_OUT = "Out";
  
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 3366819064448978124L;
  private String _tracingId;
  private String _partnerId;
  private String _bizEntId;
  private String _docType;
  private FileContent _docContent;
  private FileContent[] _attContent;
  private String _direction;
  private long _timestamp;
  private String _messageID;
  
  public TransactionDoc()
  {
    
  }
  
  public TransactionDoc(String tracingID, String partnerID, String bizEntId, String docType,
                        FileContent doc, FileContent[] attachments, String direction)
  {
    _tracingId = tracingID;
    _partnerId = partnerID;
    _bizEntId = bizEntId;
    _docType = docType;
    _docContent = doc;
    _attContent = attachments;
    _direction = direction;
  }

  /**
   * @return Returns the attContent.
   */
  public FileContent[] getAttContent()
  {
    return _attContent;
  }

  /**
   * @return Returns the bizEntId.
   */
  public String getBizEntId()
  {
    return _bizEntId;
  }

  /**
   * @return Returns the docContent.
   */
  public FileContent getDocContent()
  {
    return _docContent;
  }

  /**
   * @return Returns the docType.
   */
  public String getDocType()
  {
    return _docType;
  }

  /**
   * @return Returns the partnerId.
   */
  public String getPartnerId()
  {
    return _partnerId;
  }

  /**
   * @return Returns the tracingId.
   */
  public String getTracingId()
  {
    return _tracingId;
  }
  
  /**
   * @return Returns the direction.
   */
  public String getDirection()
  {
    return _direction;
  }

  /**
   * @return Returns the timestamp.
   */
  public long getTimestamp()
  {
    return _timestamp;
  }

  /**
   * @param timestamp The timestamp to set.
   */
  public void setTimestamp(long timestamp)
  {
    _timestamp = timestamp;
  }
  
  /**
   * @param attContent The attContent to set.
   */
  public void setAttContent(FileContent[] attContent)
  {
    _attContent = attContent;
  }

  /**
   * @param bizEntId The bizEntId to set.
   */
  public void setBizEntId(String bizEntId)
  {
    _bizEntId = bizEntId;
  }

  /**
   * @param direction The direction to set.
   */
  public void setDirection(String direction)
  {
    _direction = direction;
  }

  /**
   * @param docContent The docContent to set.
   */
  public void setDocContent(FileContent docContent)
  {
    _docContent = docContent;
  }

  /**
   * @param docType The docType to set.
   */
  public void setDocType(String docType)
  {
    _docType = docType;
  }

  /**
   * @param partnerId The partnerId to set.
   */
  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

  /**
   * @param tracingId The tracingId to set.
   */
  public void setTracingId(String tracingId)
  {
    _tracingId = tracingId;
  }

  /**
   * @return the messageID
   */
  public String getMessageID()
  {
    return _messageID;
  }

  /**
   * @param messageID the messageID to set
   */
  public void setMessageID(String messageID)
  {
    _messageID = messageID;
  }

  public String toString()
  {
    return "TransactionDoc: TracingId="+_tracingId+", direction="+_direction+", messageID="+_messageID+", docType="+_docType+", partnerId="+_partnerId+", custBizEntId="+_bizEntId; 
  }


}
