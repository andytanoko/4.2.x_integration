/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessTransaction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Jan 11, 2007    Tam Wei Xiang       Added new field isProcessSuccess
 * Feb 01, 2007    Tam Wei Xiang       Added new field isInitiator
 * Feb 05, 2007    Tam Wei Xiang       Added new field userTrackingID
 * Feb 16, 2007    Tam Wei Xiang       To faciliate the Entity to XML conversion,
 *                                     the getter for Boolean type will start with
 *                                     prefix 'get' instead of the usual 'is'
 */
package com.gridnode.gtas.audit.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.gridnode.util.db.AbstractPersistable;

/**
 * The instance of this class has a direct mapping on one of the record in the ProcessTransaction
 * table. The instance not only contains the info in ProcessInfo, but also some info in DocInfo.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 * 
 * @hibernate.class table = "`isat_process_transaction`"
 * 
 * @hibernate.query name = "getProcessTransByProUID"
 *    query = "FROM ProcessTransaction AS pt WHERE pt.processInstanceUID = :processInstanceUID"    
 * @hibernate.query
 *   query = "SELECT pt.processInstanceUID FROM ProcessTransaction pt WHERE :fromStartTime <= pt.processStartTime AND :toStartTime >= pt.processStartTime AND ( pt.groupName IS NULL OR pt.groupName='')"
 *   name = "getProcessTransUIDByStartTimeWithoutGroup"   
 * @hibernate.query
 *   query = "SELECT pt.processInstanceUID FROM ProcessTransaction pt WHERE :fromStartTime <= pt.processStartTime AND :toStartTime >= pt.processStartTime AND pt.groupName IN (:groupNameList)"
 *   name = "getProcessTransUIDByStartTimeWithGroup"
 * @hibernate.query
 *   query = "SELECT pt.processInstanceUID FROM ProcessTransaction pt WHERE :fromStartTime <= pt.processStartTime AND :toStartTime >= pt.processStartTime AND pt.groupName IN (:groupNameList) ORDER BY pt.processStartTime ASC" 
 *   name = "getProcessTransUIDSortByStartTime"   
 * @hibernate.query
 *   query = "SELECT pt.processInstanceUID FROM ProcessTransaction pt WHERE pt.processInstanceUID < :processInstanceUID AND pt.processStartTime IS NULL AND pt.groupName IN (:groupNameList)"
 *   name = "getProcessTransUIDLesserThan"  
 * @hibernate.query
 *   query = "SELECT pt.processStartTime FROM ProcessTransaction pt ORDER BY pt.processStartTime ASC"
 *   name = "getSmallestProcessStartTime"    
 */
public class ProcessTransaction extends AbstractPersistable implements IAuditTrailEntity, Serializable
{
  private String _pipName;
  private String _pipVersion;
  private String _processID;
  private Date _processStartTime;
  private Date _processEndTime;
  private String _partnerName;
  private String _partnerDuns;
  private String _customerName;
  private String _customerDuns;
  private String _requestDocNo;
  private String _responseDocNo;
  private String _processStatus;
  private String _errorType;
  private String _errorReason;
  private Long _processInstanceUID;
  private Boolean _isProcessSuccess;
  private Boolean _isInitiator;
  private String _userTrackingID;
  private String _groupName;
  private String _test;
  
  public ProcessTransaction() 
  {
  }
  
  public ProcessTransaction( String groupName, String pipName, String pipVersion, String processID, 
                            Date processStartTime, Date processEndTime, String partnerName,
                            String partnerDuns, String customerName, String customerDuns,
                            String requestDocNo, String responseDocNo, String processStatus,
                            String errorType, String errorReason, Long processInstanceUID, Boolean isProcessSuccess,
                            Boolean isInitiator, String userTrackingID)
  {
    setPipName(pipName);
    setPipVersion(pipVersion);
    setProcessID(processID);
    setProcessStartTime(processStartTime);
    setProcessEndTime(processEndTime);
    setPartnerName(partnerName);
    setPartnerDuns(partnerDuns);
    setCustomerName(customerName);
    setCustomerDuns(customerDuns);
    setRequestDocNo(requestDocNo);
    setResponseDocNo(responseDocNo);
    setProcessStatus(processStatus);
    setErrorType(errorType);
    setErrorReason(errorReason);
    setProcessInstanceUID(processInstanceUID);
    setGroupName(groupName);
    setProcessSuccess(isProcessSuccess);
    setInitiator(isInitiator);
  }
  
  /**
   * @hibernate.property name="groupName" column = "`group_name`"
   */
  public String getGroupName()
  {
    return _groupName;
  }

  public void setGroupName(String name)
  {
    _groupName = name;
  }

  /**
   * note: Hibernate will auto map to the table column using the value in name property.
   *       it will also infer the appropriate Hibernate type
   * @hibernate.property name = "customerDuns" column = "`customer_duns`"
   * @return
   */
  public String getCustomerDuns()
  {
    return _customerDuns;
  }

  public void setCustomerDuns(String duns)
  {
    _customerDuns = duns;
  }
  
  /**
   * @hibernate.property name = "customerName" column = "`customer_name`"
   * @return
   */
  public String getCustomerName()
  {
    return _customerName;
  }

  public void setCustomerName(String name)
  {
    _customerName = name;
  }
  
  /**
   * @hibernate.property name = "errorReason" column = "`error_reason`"
   * @return
   */
  public String getErrorReason()
  {
    return _errorReason;
  }

  public void setErrorReason(String reason)
  {
    _errorReason = reason;
  }
  
  /**
   * @hibernate.property name = "errorType" column = "`error_type`"
   * @return
   */
  public String getErrorType()
  {
    return _errorType;
  }

  public void setErrorType(String type)
  {
    _errorType = type;
  }
  
  /**
   * @hibernate.property name = "partnerDuns" column = "`partner_duns`"
   * @return
   */
  public String getPartnerDuns()
  {
    return _partnerDuns;
  }

  public void setPartnerDuns(String duns)
  {
    _partnerDuns = duns;
  }
  
  /**
   * @hibernate.property name = "partnerName" column = "`partner_name`"
   * @return
   */
  public String getPartnerName()
  {
    return _partnerName;
  }

  public void setPartnerName(String name)
  {
    _partnerName = name;
  }
  
  /**
   * @hibernate.property name = "pipName" column = "`pip_name`"
   * @return
   */
  public String getPipName()
  {
    return _pipName;
  }

  public void setPipName(String name)
  {
    _pipName = name;
  }
  
  /**
   * @hibernate.property name = "pipVersion" column = "`pip_version`"
   * @return
   */
  public String getPipVersion()
  {
    return _pipVersion;
  }

  public void setPipVersion(String version)
  {
    _pipVersion = version;
  }
  
  /**
   * @hibernate.property name = "processEndTime" type = "timestamp" column = "`process_end_time`"
   * @return
   */
  public Date getProcessEndTime()
  {
    return _processEndTime;
  }

  public void setProcessEndTime(Date endTime)
  {
    _processEndTime = endTime;
  }
  
  /**
   * @hibernate.property name = "processID" column = "`process_id`"
   * @return
   */
  public String getProcessID()
  {
    return _processID;
  }

  public void setProcessID(String _processid)
  {
    _processID = _processid;
  }
  
  /**
   * @hibernate.property name = "processStartTime" column = "`process_start_time`"
   * @return
   */
  public Date getProcessStartTime()
  {
    return _processStartTime;
  }

  public void setProcessStartTime(Date startTime)
  {
      _processStartTime = startTime;
  }
  
  /**
   * @hibernate.property name = "processStatus" column = "`process_status`"
   * @return
   */
  public String getProcessStatus()
  {
    return _processStatus;
  }

  public void setProcessStatus(String status)
  {
    _processStatus = status;
  }
  
  /**
   * @hibernate.property name = "requestDocNo" column = "`request_doc_no`"
   * @return
   */
  public String getRequestDocNo()
  {
    return _requestDocNo;
  }

  public void setRequestDocNo(String docNo)
  {
    _requestDocNo = docNo;
  }
  
  /**
   * @hibernate.property name = "responseDocNo" column = "`response_doc_no`"
   * @return
   */
  public String getResponseDocNo()
  {
    return _responseDocNo;
  }

  public void setResponseDocNo(String docNo)
  {
    _responseDocNo = docNo;
  }
  
  /**
   * @hibernate.property name = "processInstanceUID" column = "`process_instance_uid`"
   * @return
   */
  public Long getProcessInstanceUID()
  {
    return _processInstanceUID;
  }

  public void setProcessInstanceUID(Long instanceUID)
  {
    _processInstanceUID = instanceUID;
  }
  
  /**
   * @hibernate.property name = "ProcessSuccess" column = "`is_process_success`"
   * @return
   */
  public Boolean isProcessSuccess()
  {
    return _isProcessSuccess;
  }

  public void setProcessSuccess(Boolean processSuccess)
  {
    _isProcessSuccess = processSuccess;
  }
  
  /**
   * @hibernate.property name = "Initiator" column = "`is_initiator`"
   * @return
   */
  public Boolean isInitiator()
  {
    return _isInitiator;
  }

  public void setInitiator(Boolean initiator)
  {
    _isInitiator = initiator;
  }
  
  /**
   * @hibernate.property name = "userTrackingID" column = "`user_tracking_id`"
   * @return
   */
  public String getUserTrackingID()
  {
    return _userTrackingID;
  }

  public void setUserTrackingID(String trackingID)
  {
    _userTrackingID = trackingID;
  }

  public String toString()
  {
    return "ProcessTransaction[UID: "+(getUID()== null ? "" : getUID())+" pipName: "+getPipName()+" pipVersion: "+getPipVersion()+
           " processID: "+getProcessID()+" processStartTime: "+getProcessStartTime()+" processEndTime: "+getProcessEndTime()+
           " partnerName: "+getPartnerName()+" partnerDuns: "+getPartnerDuns()+" customerName: "+getCustomerName()+
           " customerDuns: "+getCustomerDuns()+" requestDocNo: "+getRequestDocNo()+" responseDocNo: "+getResponseDocNo()+" isProcessSuccess: "+isProcessSuccess()+
           " processStatus: "+getProcessStatus()+" errorType: "+getErrorType()+" errorReason: "+getErrorReason()+" processInstanceUID: "+getProcessInstanceUID()+" groupName: "+getGroupName()+
           " isInitiator: "+isInitiator()+" userTrackingID: "+getUserTrackingID()+" version: "+getVersion()+"]";
  }
}
