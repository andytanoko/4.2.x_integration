/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;

public class GetAS2DocTypeMappingListEvent
extends    GetEntityListEvent
{
  private static final long serialVersionUID = 9153479341226853826L;
  
  public GetAS2DocTypeMappingListEvent()
  {
    super();
  }
  
  public GetAS2DocTypeMappingListEvent(IDataFilter filter)
  {
    super(filter);
  }
  
  public GetAS2DocTypeMappingListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }
  
  public GetAS2DocTypeMappingListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }
  
  public GetAS2DocTypeMappingListEvent(String listID, int maxRows, int startRow)
  throws EventException
  {
    super(listID, maxRows, startRow);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/GetAS2DocTypeMappingListEvent";
  }
}
