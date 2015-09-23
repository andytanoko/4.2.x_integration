/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJMSMsgHandlerLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 30, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.framework.jms.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IJMSMsgHandlerLocalHome extends EJBLocalHome
{
  public IJMSMsgHandlerLocalObj create() throws CreateException;
}
