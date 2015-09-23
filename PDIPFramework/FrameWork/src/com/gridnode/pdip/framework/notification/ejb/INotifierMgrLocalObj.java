/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INotifierMgrLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.notification.ejb;

import javax.ejb.EJBLocalObject;

import com.gridnode.pdip.framework.notification.INotification;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface INotifierMgrLocalObj extends EJBLocalObject
{
  public void broadCastNotification(INotification notification) throws Exception;
}
