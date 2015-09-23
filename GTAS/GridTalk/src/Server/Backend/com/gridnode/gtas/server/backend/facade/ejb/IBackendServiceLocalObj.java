/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBackendServiceLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 * May 28 2003    Jagadeesh           Added : getNextSeqRunningNo to return a
 *                                    sequential running no.
 * Oct 26 2005    Neo Sok Lay         Business methods throws Throwable is not
 *                                    acceptable for SAP J2EE deployment 
 */
package com.gridnode.gtas.server.backend.facade.ejb;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.backend.openapi.core.APIServiceInterface;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObj interface for BackendServiceBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I7
 * @since 2.0
 */
public interface IBackendServiceLocalObj
       extends   EJBLocalObject
{

  /**
   * Create a new Port.
   *
   * @param port The Port entity.
   */
  public Long createPort(Port port)
    throws CreateEntityException, SystemException, DuplicateEntityException;

  /**
   * Update a Port
   *
   * @param port The Port entity with changes.
   */
  public void updatePort(Port port)
    throws UpdateEntityException, SystemException;

  /**
   * Delete a Port.
   *
   * @param portUId The UID of the Port to delete.
   */
  public void deletePort(Long portUId)
    throws DeleteEntityException, SystemException;

  /**
   * Find a Port using the Port UID.
   *
   * @param portUId The UID of the Port to find.
   * @return The Port found, or <B>null</B> if none exists with that
   * UID.
   */
  public Port findPort(Long portUid)
    throws FindEntityException, SystemException;

  /**
   * Find a Port using the PortName
   *
   * @param portName The name of the Port to find.
   * @return The Port found, or <B>null</B> if none exists.
   */
  public Port findPort(String portName)
    throws FindEntityException, SystemException;

  /**
   * Find a number of Port that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Port found, or empty collection if none
   * exists.
   */
  public Collection findPorts(IDataFilter filter)
    throws FindEntityException, SystemException;

  /**
   * Find a number of Port that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of Port found, or empty collection if
   * none exists.
   */
  public Collection findPortsKeys(IDataFilter filter)
    throws FindEntityException, SystemException;


  // ********************* Methods for Rfc

  /**
   * Create a new Rfc.
   *
   * @param rfc The Rfc entity.
   */
  public Long createRfc(Rfc rfc)
    throws CreateEntityException, SystemException, DuplicateEntityException;

  /**
   * Update a Rfc
   *
   * @param rfc The Rfc entity with changes.
   */
  public void updateRfc(Rfc rfc)
    throws UpdateEntityException, SystemException;

  /**
   * Delete a Rfc.
   *
   * @param rfcUId The UID of the Rfc to delete.
   */
  public void deleteRfc(Long rfcUId)
    throws DeleteEntityException, SystemException;

  /**
   * Find a Rfc using the Rfc UID.
   *
   * @param rfcUId The UID of the Rfc to find.
   * @return The Rfc found, or <B>null</B> if none exists with that
   * UID.
   */
  public Rfc findRfc(Long rfcUid)
    throws FindEntityException, SystemException;

  /**
   * Find a Rfc using the RfcName
   *
   * @param rfcName The name of the Rfc to find.
   * @return The Rfc found, or <B>null</B> if none exists.
   */
  public Rfc findRfc(String rfcName)
    throws FindEntityException, SystemException;

  /**
   * Find a number of Rfc that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Rfc found, or empty collection if none
   * exists.
   */
  public Collection findRfcs(IDataFilter filter)
    throws FindEntityException, SystemException;

  /**
   * Find a number of Rfc that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of Rfc found, or empty collection if
   * none exists.
   */
  public Collection findRfcsKeys(IDataFilter filter)
    throws FindEntityException, SystemException;

  public boolean startListener()
    throws Exception;

  public boolean sendFile(Vector portInfo, File fileToSend)
    throws Exception;

  public boolean sendFile(Vector portInfo, File[] filesToSend)
    throws Exception;

  public APIServiceInterface testConnection(String ipAddress,
                                            int portNumber,
                                            boolean persist)
                                            throws Exception;

  /**
   * Returns a sequential running no, given the port uid.
   * @return Sequential Running no.
   * @throws SystemException thrown upon exception in getting next seq no.
   */
  public String getNextSeqRunningNo(Long portUID) throws SystemException;

}