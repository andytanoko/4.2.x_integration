/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventTrail.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 5, 2007        i00107             Created
 * Feb 13,2007        Tam Wei Xiang      update the method name from getTrailInfo/setTrailInfo
 *                                       to getEventInfo/setEventInfo, so that XMLBeanUtil
 *                                       can correctly set the EventInfo into the right method
 *                                       in the target class (eg the AuditTrailData).       
 */

package com.gridnode.gridtalk.httpbc.common.model;

/**
 * @author i00107
 * This bean is used in event notification to hold the event information
 * with the related documents.
 */
public class EventTrail
{
  private EventInfo _trailInfo;
  private EventDoc[] _bizDocuments;
  
  public EventTrail()
  {
    
  }
  
  /**
   * @return the bizDocuments
   */
  public EventDoc[] getBizDocuments()
  {
    return _bizDocuments;
  }
  /**
   * @param bizDocuments the bizDocuments to set
   */
  public void setBizDocuments(EventDoc[] bizDocuments)
  {
    _bizDocuments = bizDocuments;
  }
  /**
   * @return the trailInfo
   */
  public EventInfo getEventInfo()
  {
    return _trailInfo;
  }
  /**
   * @param trailInfo the trailInfo to set
   */
  public void setEventInfo(EventInfo trailInfo)
  {
    _trailInfo = trailInfo;
  }
  
  
}
