/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveServiceManagerLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 4, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IArchiveServiceManagerLocalHome extends EJBLocalHome
{
  public IArchiveServiceManagerLocalObj create() throws CreateException;
}
