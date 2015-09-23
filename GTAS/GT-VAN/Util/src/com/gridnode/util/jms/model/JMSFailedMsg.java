/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JMSFailedMsg.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 26, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.util.jms.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import com.gridnode.util.db.AbstractPersistable;

/**
 * This class is an exact replication from GT Framework's JMSFailedMsg.
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 * 
 * @hibernate.class table="`jms_failed_msg`"
 * @hibernate.query 
 *    name = "retrieveFailedJMS"
 *    query = "FROM JMSFailedMsg jmsg WHERE jmsg.retryCount < :retryCount AND jmsg.category = :category ORDER BY jmsg.createdDate ASC"
 * 
 */
public class JMSFailedMsg extends AbstractPersistable
{
  private String _destName;
  private Serializable _msgObj;
  private Hashtable _msgProps;
  private Properties _sendProps;
  private Date _createdDate;
  private Integer _retryCount = 0;
  private String _category;
  
  
  public JMSFailedMsg()
  {
    
  }
  
  public JMSFailedMsg(String destName, Serializable obj, 
                      Hashtable msgProps,String category, Properties sendProps)
  {
    _destName = destName;
    _msgObj = obj;
    _msgProps = msgProps;
    _category = category;
    _sendProps = sendProps;
    _createdDate = new Date();
  }
  
  /**
   * @hibernate.property name = "destName" column = "`dest_name`"
   * @return
   */
  public String getDestName()
  {
    return _destName;
  }

  public void setDestName(String name)
  {
    _destName = name;
  }

  /**
   * @hibernate.property name = "msgObj" column = "`msg_obj`"
   * @return
   */
  public Serializable getMsgObj()
  {
    return _msgObj;
  }

  public void setMsgObj(Serializable obj)
  {
    _msgObj = obj;
  }

  /**
   * @hibernate.property name = "msgProps" column = "`msg_props`"
   * @return
   */
  public Hashtable getMsgProps()
  {
    return _msgProps;
  }

  public void setMsgProps(Hashtable props)
  {
    _msgProps = props;
  }
  
  /**
   * @hibernate.property name = "createdDate" column = "`created_date`"
   * @return
   */
  public Date getCreatedDate()
  {
    return _createdDate;
  }

  public void setCreatedDate(Date date)
  {
    _createdDate = date;
  }

  /**
   * @hibernate.property name = "retryCount" column = "`retry_count`"
   * @return
   */
  public Integer getRetryCount()
  {
    return _retryCount;
  }

  public void setRetryCount(Integer count)
  {
    _retryCount = count;
  }
  
  /**
   * @hibernate.property name = "category" column = "`category`"
   * @return
   */
  public String getCategory()
  {
    return _category;
  }

  public void setCategory(String _category)
  {
    this._category = _category;
  }
  
  /**
   * @hibernate.property name = "sendProps" column = "`send_props`";
   * @return
   */
  public Properties getSendProps()
  {
    return _sendProps;
  }

  public void setSendProps(Properties props)
  {
    _sendProps = props;
  }

  public String toString()
  {
    return "[JMSFailedMsg] destName: "+getDestName()+" msgProps: "+getMsgProps()+" createdDate:"+getCreatedDate()+" retryCount:"+getRetryCount()+" category:"+getCategory()+" sendProps:"+getSendProps();
  }
}
