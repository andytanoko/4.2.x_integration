/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGWFBaseManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 6, 2008   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.gwfbase.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IGWFBaseManagerObj extends EJBObject
{
  public BpssProcessSpecification getBpssProcessSpec(String uuid, String specVersion) throws SystemException, RemoteException;
}
