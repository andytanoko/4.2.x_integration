/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefChangeNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 3, 2008   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.gwfbase.notification;

import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.framework.notification.AbstractNotification;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 * @since GT 4.1.2
 */
public class ProcessDefChangeNotification extends AbstractNotification
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -141700466676174452L;
  private BpssProcessSpecification _processSpec = null;
  
  public ProcessDefChangeNotification(BpssProcessSpecification processSpec)
  {
    _processSpec = (BpssProcessSpecification)processSpec.clone();
  }
  
  public BpssProcessSpecification getProcessSpec()
  {
    return _processSpec;
  }

  public void setProcessSpec(BpssProcessSpecification spec)
  {
    _processSpec = spec;
  }

  public String getNotificationID()
  {
    return "ProcessDefChangeNotification";
  }

}
