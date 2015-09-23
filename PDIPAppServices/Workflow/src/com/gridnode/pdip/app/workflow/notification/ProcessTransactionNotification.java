/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceTransactionNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 13, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.workflow.notification;

import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.framework.notification.AbstractNotification;

/**
 * This class will be used to notify the creation or update on the ProcessInstance
 * by the WF module.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class ProcessTransactionNotification extends
                                                   AbstractNotification
{
  private GWFRtProcess _rtprocess;
  private GWFRtProcessDoc _rtprocessDoc;
  
  public ProcessTransactionNotification(GWFRtProcess rtprocess, GWFRtProcessDoc rtprocessDoc)
  {
    setRtprocess(rtprocess);
    setRtprocessDoc(rtprocessDoc);
  }

  public GWFRtProcess getRtprocess() 
  {
    return _rtprocess;
  }

  public void setRtprocess(GWFRtProcess rtprocess)
  {
    _rtprocess = rtprocess;
  }

  public GWFRtProcessDoc getRtprocessDoc()
  {
    return _rtprocessDoc;
  }

  public void setRtprocessDoc(GWFRtProcessDoc doc)
  {
    _rtprocessDoc = doc;
  }

  /* (non-Javadoc)
   * @see com.gridnode.pdip.framework.notification.INotification#getNotificationID()
   */
  public String getNotificationID()
  {
    // TODO Auto-generated method stub
    return "ProcessTransNotification";
  }
  
  public String toString()
  {
    return "RTProcess :"+(_rtprocess == null ? "" :_rtprocess.getKey())+" RtProcessDoc "+(_rtprocessDoc == null ? "" : _rtprocessDoc.getDocumentId());
  }
  
}
