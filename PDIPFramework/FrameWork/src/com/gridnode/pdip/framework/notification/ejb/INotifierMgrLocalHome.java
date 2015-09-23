/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INotifierMgrLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.notification.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface INotifierMgrLocalHome extends EJBLocalHome
{
  public INotifierMgrLocalObj create() throws CreateException;
}
