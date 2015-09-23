/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessFailedJMSNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.notification;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class ProcessFailedJMSNotification extends AbstractNotification
{
  private int _numRecordFetched = 50;
  private int _maxRetry = 100;
  
  /**
   * 
   */
  private static final long serialVersionUID = 4410999127236829561L;
  
  public ProcessFailedJMSNotification(int numRecordFetched, int maxRetry)
  {
    setNumRecordFetched(numRecordFetched);
    setMaxRetry(maxRetry);
  }

  public int getMaxRetry()
  {
    return _maxRetry;
  }

  public void setMaxRetry(int retry)
  {
    _maxRetry = retry;
  }

  public int getNumRecordFetched()
  {
    return _numRecordFetched;
  }

  public void setNumRecordFetched(int recordFetched)
  {
    _numRecordFetched = recordFetched;
  }

  public String getNotificationID()
  {
    return "FailedJMS";
  }
  
  public String toString()
  {
    return "[ProcessFailedJMSNotification] NotificationID: "+getNotificationID()+" numRecordFetched: "+getNumRecordFetched()+" maxRetry: "+getMaxRetry();
  }
}
