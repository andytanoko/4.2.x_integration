/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessTransManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 26, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IProcessTransManagerLocalHome extends EJBLocalHome
{
  public IProcessTransManagerLocalObj create() throws CreateException;
}
