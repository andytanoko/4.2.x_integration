/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetProcedureDefFileListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh               Created
 */


package com.gridnode.gtas.events.userprocedure;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;


public class GetProcedureDefFileListEvent extends GetEntityListEvent
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7567419526106326582L;

	public GetProcedureDefFileListEvent()
  {
    super();
  }

  public GetProcedureDefFileListEvent(IDataFilter filter)
  {
    super(filter);
  }


  public GetProcedureDefFileListEvent(IDataFilter filter, int maxRows)
  {
    super(filter,maxRows);
  }

  public GetProcedureDefFileListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter,maxRows,startRow);
  }

  public GetProcedureDefFileListEvent(String listID, int maxRows, int startRow)
         throws EventException
  {
    super(listID,maxRows,startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcedureDefFileListEvent";
  }
}



