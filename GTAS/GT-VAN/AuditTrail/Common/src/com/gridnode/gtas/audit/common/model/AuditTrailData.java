/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Jan 09, 2007    Tam Wei Xiang       Added empty constructor
 * Feb 13, 2007    Tam Wei Xiang       To better support for the XMLBeanUtil by
 *                                     adding the setEventInfo, setDocInfo,
 *                                     setProcessInfo
 * Apr 05, 2007    Tam Wei Xiang       To category those events that is not depend
 *                                     on other AuditTrailData eg the DocInfo                                    
 */
package com.gridnode.gtas.audit.common.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/**
 * This class encapsulated two instances that are ITrailInfo and BusinessDocument. AuditTrailData
 * is the expected object type in the ISAT. Hence, all the data we send to the Remote-Event queue
 * will be encapsulated into and instance of AuditTrailData.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AuditTrailData implements Serializable
{
  
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 9179022816830619124L;
  private ITrailInfo _trailInfo;
  private BusinessDocument[] _bizDocuments;
  private boolean _isIndependent = true;
  
  public AuditTrailData(){};
  
  public AuditTrailData(ITrailInfo trailInfo, boolean isIndependent, BusinessDocument... bizDocument)
  {
    setBizDocuments(bizDocument);
    setTrailInfo(trailInfo);
    setIndependent(isIndependent);
  }

  public BusinessDocument[] getBizDocuments()
  {
    return _bizDocuments;
  }

  public void setBizDocuments(BusinessDocument... document)
  {
    _bizDocuments = document;
  }

  public ITrailInfo getTrailInfo()
  {
    return _trailInfo;
  }
  
  public void setTrailInfo(ITrailInfo _trailnfo)
  {
    this._trailInfo = _trailnfo;
  }
  
  //Require this setter method if we want to use XMLBeanUtil (JOXBeanReader) to deserialize from XML
  public void setEventInfo(EventInfo info)
  {
    this._trailInfo = info;
  }
  
//Require this setter method if we want to use XMLBeanUtil (JOXBeanReader) to deserialize from XML
  public void setProcessInfo(ProcessInfo info)
  {
    this._trailInfo = info;
  }

//Require this setter method if we want to use XMLBeanUtil (JOXBeanReader) to deserialize from XML
  public void setDocInfo(DocInfo info)
  {
    this._trailInfo = info;
  }

  public boolean isIndependent()
  {
    return _isIndependent;
  }

  public void setIndependent(boolean independent)
  {
    _isIndependent = independent;
  }
  
  
}
