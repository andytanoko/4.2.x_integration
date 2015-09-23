package com.gridnode.pdip.framework.db.jms;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.Message;

public class JMSFailedMsg implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1191068302197414352L;
  public static final String JMS_MSG_TYPE_OBJ = "1";  
  public static final String JMS_MSG_TYPE_MAP = "2";
  
  private Long _uid;
  private String _destinationType;
  private Hashtable<String, String> _configProps;
  private String _destName;
  private Serializable _msgObj;
  private Hashtable _msgProps;
  private Timestamp _createdDate;
  private Integer _retryCount = 0;
  
  public JMSFailedMsg(String destinationType, Hashtable<String, String> configProps, String destName, Serializable obj, Hashtable msgProps)
  {
    _destinationType = destinationType;
    _configProps = configProps;
    _destName = destName;
    _msgObj = obj;
    _msgProps = msgProps;
    _createdDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
  }

  public Hashtable<String, String> getConfigPrope()
  {
    return _configProps;
  }

  public void setConfigProps(Hashtable<String, String> name)
  {
    _configProps = name;
  }

  public String getDestinationType()
  {
    return _destinationType;
  }

  public void setDestinationType(String type)
  {
    _destinationType = type;
  }

  public String getDestName()
  {
    return _destName;
  }

  public void setDestName(String name)
  {
    _destName = name;
  }

  public Serializable getMsgObj()
  {
    return _msgObj;
  }

  public void setMsgObj(Serializable obj)
  {
    _msgObj = obj;
  }

  public Hashtable getMsgProps()
  {
    return _msgProps;
  }

  public void setMsgProps(Hashtable props)
  {
    _msgProps = props;
  }
  
  public Timestamp getCreatedDate()
  {
    return _createdDate;
  }

  public void setCreatedDate(Timestamp date)
  {
    _createdDate = date;
  }

  public Integer getRetryCount()
  {
    return _retryCount;
  }

  public void setRetryCount(Integer count)
  {
    _retryCount = count;
  }

  public Long getUid()
  {
    return _uid;
  }

  public void setUid(Long _uid)
  {
    this._uid = _uid;
  }

  public String toString()
  {
    return "[JMSFailedMsg] ConfigProps: "+getConfigPrope()+" destName: "+getDestName()+" msgProps: "+getMsgProps()+" createdDate:"+getCreatedDate()+" retryCount:"+getRetryCount()+" UID:"+getUid();
  }
}
