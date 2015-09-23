/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendServiceBean.java
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

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.backend.entities.ejb.IPortLocalObj;
import com.gridnode.gtas.server.backend.entities.ejb.IRfcLocalObj;
import com.gridnode.gtas.server.backend.helpers.BackendHelper;
import com.gridnode.gtas.server.backend.helpers.Logger;
import com.gridnode.gtas.server.backend.helpers.PortEntityHandler;
import com.gridnode.gtas.server.backend.helpers.RfcEntityHandler;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.backend.openapi.core.*;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

public class BackendServiceBean
       implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4565218311003905648L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ********************* Implementing methods in IBackendServiceLocalObj

  // ********************* Methods for Port

  /**
   * Create a new Port.
   *
   * @param port The Port entity.
   */
  public Long createPort(Port port)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[BackendServiceBean.createPort] Enter");

    try
    {
      IPortLocalObj obj =
      (IPortLocalObj)getPortEntityHandler().create(port);

      Logger.log("[BackendServiceBean.createPort] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[BackendServiceBean.createPort] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[BackendServiceBean.createPort] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.createPort] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.createPort(Port) Error ",
        ex);
    }
  }

  /**
   * Update a Port
   *
   * @param port The Port entity with changes.
   */
  public void updatePort(Port port)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.updatePort] Enter");

    try
    {
      getPortEntityHandler().update(port);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[BackendServiceBean.updatePort] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.updatePort] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.updatePort(Port) Error ",
        ex);
    }

    Logger.log("[BackendServiceBean.updatePort] Exit");
  }

  /**
   * Delete a Port.
   *
   * @param portUId The UID of the Port to delete.
   */
  public void deletePort(Long portUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.deletePort] Enter");

    try
    {
      getPortEntityHandler().remove(portUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[BackendServiceBean.deletePort] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[BackendServiceBean.deletePort] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.deletePort] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.deletePort(portUId) Error ",
        ex);
    }

    Logger.log("[BackendServiceBean.deletePort] Exit");
  }

  /**
   * Find a Port using the Port UID.
   *
   * @param portUId The UID of the Port to find.
   * @return The Port found, or <B>null</B> if none exists with that
   * UID.
   */
  public Port findPort(Long portUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.findPort] UID: "+
      portUid);

    Port port = null;

    try
    {
      port =
        (Port)getPortEntityHandler().
          getEntityByKeyForReadOnly(portUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findPort] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findPort] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findPort] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findPort(portUId) Error ",
        ex);
    }

    return port;
  }

  /**
   * Find a Port using the PortName
   *
   * @param portName The name of the Port to find.
   * @return The Port found, or <B>null</B> if none exists.
   */
  public Port findPort(String portName)
    throws FindEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.findPort] PortName: "+portName);

    Port port = null;

    try
    {
      port = (Port)getPortEntityHandler().findByPortName(portName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findPort] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findPort] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findPort] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findPort(portId) Error ",
        ex);
    }

    return port;
  }

  /**
   * Find a number of Port that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Port found, or empty collection if none
   * exists.
   */
  public Collection findPorts(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[BackendServiceBean.findPorts] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection ports = null;
    try
    {
      ports =
        getPortEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findPorts] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findPorts] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findPorts] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findPorts(filter) Error ",
        ex);
    }

    return ports;
  }

  /**
   * Find a number of Port that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of Port found, or empty collection if
   * none exists.
   */
  public Collection findPortsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[BackendServiceBean.findPortsKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection portsKeys = null;
    try
    {
      portsKeys =
        getPortEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findPortsKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findPortsKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findPortsKeys] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findPortsKeys(filter) Error ",
        ex);
    }

    return portsKeys;
  }


  // ********************* Methods for Rfc

  /**
   * Create a new Rfc.
   *
   * @param rfc The Rfc entity.
   */
  public Long createRfc(Rfc rfc)
    throws CreateEntityException, SystemException, DuplicateEntityException
  {
    Logger.log("[BackendServiceBean.createRfc] Enter");

    try
    {
      IRfcLocalObj obj =
      (IRfcLocalObj)getRfcEntityHandler().create(rfc);

      Logger.log("[BackendServiceBean.createRfc] Exit");
      return (Long)obj.getData().getKey();
    }
    catch (CreateException ex)
    {
      Logger.warn("[BackendServiceBean.createRfc] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[BackendServiceBean.createRfc] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.createRfc] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.createRfc(Rfc) Error ",
        ex);
    }
  }

  /**
   * Update a Rfc
   *
   * @param rfc The Rfc entity with changes.
   */
  public void updateRfc(Rfc rfc)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.updateRfc] Enter");

    try
    {
      getRfcEntityHandler().update(rfc);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[BackendServiceBean.updateRfc] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.updateRfc] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.updateRfc(Rfc) Error ",
        ex);
    }

    Logger.log("[BackendServiceBean.updateRfc] Exit");
  }

  /**
   * Delete a Rfc.
   *
   * @param rfcUId The UID of the Rfc to delete.
   */
  public void deleteRfc(Long rfcUId)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.deleteRfc] Enter");

    try
    {
      getRfcEntityHandler().remove(rfcUId);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[BackendServiceBean.deleteRfc] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[BackendServiceBean.deleteRfc] BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.deleteRfc] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.deleteRfc(rfcUId) Error ",
        ex);
    }

    Logger.log("[BackendServiceBean.deleteRfc] Exit");
  }

  /**
   * Find a Rfc using the Rfc UID.
   *
   * @param rfcUId The UID of the Rfc to find.
   * @return The Rfc found, or <B>null</B> if none exists with that
   * UID.
   */
  public Rfc findRfc(Long rfcUid)
    throws FindEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.findRfc] UID: "+
      rfcUid);

    Rfc rfc = null;

    try
    {
      rfc =
        (Rfc)getRfcEntityHandler().
          getEntityByKeyForReadOnly(rfcUid);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findRfc] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findRfc] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findRfc] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findRfc(rfcUId) Error ",
        ex);
    }

    return rfc;
  }

  /**
   * Find a Rfc using the RfcName
   *
   * @param rfcName The name of the Rfc to find.
   * @return The Rfc found, or <B>null</B> if none exists.
   */
  public Rfc findRfc(String rfcName)
    throws FindEntityException, SystemException
  {
    Logger.log("[BackendServiceBean.findRfc] RfcName: "+rfcName);

    Rfc rfc = null;

    try
    {
      rfc = (Rfc)getRfcEntityHandler().findByRfcName(rfcName);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findRfc] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findRfc] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findRfc] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findRfc(rfcId) Error ",
        ex);
    }

    return rfc;
  }

  /**
   * Find a number of Rfc that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Rfc found, or empty collection if none
   * exists.
   */
  public Collection findRfcs(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[BackendServiceBean.findRfcs] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection rfcs = null;
    try
    {
      rfcs =
        getRfcEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findRfcs] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findRfcs] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findRfcs] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findRfcs(filter) Error ",
        ex);
    }

    return rfcs;
  }

  /**
   * Find a number of Rfc that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of uids of Rfc found, or empty collection if
   * none exists.
   */
  public Collection findRfcsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[BackendServiceBean.findRfcsKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection rfcsKeys = null;
    try
    {
      rfcsKeys =
        getRfcEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[BackendServiceBean.findRfcsKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.findRfcsKeys] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[BackendServiceBean.findRfcsKeys] Error ", ex);
      throw new SystemException(
        "BackendServiceBean.findRfcsKeys(filter) Error ",
        ex);
    }

    return rfcsKeys;
  }

  // ********************* Methods for Backend services

  public boolean startListener()
    throws Exception
  {
    int port = BackendHelper.getBackendPort();
    GTAPIServer apiServer = new GTAPIServer(port);
    if (apiServer != null)
    {
      Logger.log("Backend Listener started at " + apiServer.getIPAddress());
      apiServer.start();
    }
    return true;
  }

  public boolean sendFile(Vector portInfo, File fileToSend)
    throws Exception
  {
    File[] filesToSend = new File[1];
    filesToSend[0] = fileToSend;
    return sendFile(portInfo, filesToSend);
  }

  public boolean sendFile(Vector portInfo, File[] filesToSend)
    throws Exception
  {
    String ipAddress = (String)portInfo.get(0);
    int port = ((Integer)portInfo.get(1)).intValue();

    APIServiceInterface serviceInterface = testConnection(ipAddress, port, true);
    if (serviceInterface != null)
    {
      APIParams parameters = new  APIParams(portInfo.toArray(), filesToSend);

      Logger.log("[BackendServiceBean.sendFile] Initiating import service");
      serviceInterface.performService(IAPIService.IMPORT, parameters);

      Logger.log("[BackendServiceBean.sendFile] Performing service disconnect");
      serviceInterface.serviceDisconnect();

      return true;
    }

    return false;
  }

  public APIServiceInterface testConnection(String ipAddress,
                                            int portNumber,
                                            boolean persist)
                                            throws Exception
  {
    try
    {
      APIConfig apiConfig = new APIConfig(ipAddress, portNumber, "", "");
      APIServiceInterface serviceInterface = new APIServiceInterface(apiConfig);

      Logger.log("[BackendServiceBean.sendFile] Performing service connect");
      serviceInterface.serviceConnect();

      if (!persist)
      {
        Logger.log("[BackendServiceBean.sendFile] Performing service disconnect");
        serviceInterface.serviceDisconnect();
      }

      return serviceInterface;
    }
    catch (Exception ex)
    {
      Logger.warn("[BackendServiceBean.sendFile] Fail to connect to remote listener!", ex);
      return null;
    }
  }

  /**
   * Returns a sequential running no, given the port uid.
   * @return Sequential Running no.
   * @throws Throwable thrown upon exception in getting next seq no.
   */

  public String getNextSeqRunningNo(Long portUID) throws SystemException
  {
  	try
  	{
      return getPortEntityHandler().getNextSequenceNo(portUID);
  		
  	}
    catch (SystemException ex)
    {
      Logger.warn("[BackendServiceBean.getNextSeqRunningNo] Unable to get next sequence no for port", ex);
      throw ex;
    }
  	catch (Throwable t)
  	{
      Logger.warn("[BackendServiceBean.getNextSeqRunningNo] Unexpected Error ", t);
      throw new SystemException("Fail to getNextSeqRunningNo for port", t);
  	}
  }


  // ********************* Methods for EntityHandler

  private PortEntityHandler getPortEntityHandler()
  {
     return PortEntityHandler.getInstance();
  }

  private RfcEntityHandler getRfcEntityHandler()
  {
     return RfcEntityHandler.getInstance();
  }

}