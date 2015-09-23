/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReprocessManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 1, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.reprocess.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.gtas.audit.reprocess.exception.ReprocessActivityException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IReprocessManagerObj extends EJBObject
{
  /**
   * Determine whether we can perform reprocessing the tracingID correspond doc.
   * @param tracingID
   * @return
   * @throws ReprocessActivityException
   * @throws RemoteException
   */
  public String[] isAllowedReprocessDoc(String tracingID) throws ReprocessActivityException, RemoteException;
  
  /**
   * Get the business entity ID
   * @param tracingID
   * @return
   * @throws ReprocessActivityException
   */
  public String getBeID(String tracingID) throws ReprocessActivityException, RemoteException;
  
  /**
   * Retrieve the approprivate msgID given the tracingID. The msgID can be retrieved in following scenario
   * i) import doc correspond event list ii) IB doc iii) OB signal correspond event list 
   * @param tracingID
   * @return the msgID
   */
  public String getTracingIDCorrespondFirstInsertMsgID(String tracingID) throws ReprocessActivityException, RemoteException;
  
  /**
   * Get the response URL that we will forward the status of the reprocessing to the UI.
   * @return the URL where we sent the reprocessing status to.
   */
  public String getResponseURL() throws RemoteException;
}
