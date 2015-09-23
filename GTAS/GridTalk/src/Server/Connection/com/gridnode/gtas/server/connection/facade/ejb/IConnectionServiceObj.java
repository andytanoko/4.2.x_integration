/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionServiceObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 * Oct 28 2002    Neo Sok Lay         Provide Connect/Disconnect services.
 * Nov 29 2002    Neo Sok Lay         Handle connection lost.
 * Feb 06 2004    Neo Sok Lay         Handle Auto-connect on startup.
 */
package com.gridnode.gtas.server.connection.facade.ejb;

import com.gridnode.gtas.server.connection.exceptions.ConnectionException;
import com.gridnode.gtas.server.connection.exceptions.ConnectionSetupException;
import com.gridnode.gtas.server.connection.exceptions.DisconnectionException;
import com.gridnode.gtas.server.connection.exceptions.NetworkSettingException;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;

import javax.ejb.EJBObject;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * EJB proxy object for the ConnectionServiceBean.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 i1
 * @since 2.0 I6
 */
public interface IConnectionServiceObj extends EJBObject
{
  /**
   * Obtain the network settings for this GridTalk.
   *
   * @return The NetworkSetting.
   */
  public NetworkSetting getNetworkSetting()
    throws NetworkSettingException, SystemException, RemoteException;

  /**
   * Save the modified network settings.
   *
   * @param networkSetting The modified network settings to save.
   */
  public void saveNetworkSetting(NetworkSetting networkSetting)
    throws NetworkSettingException, SystemException, RemoteException;

  /**
   * Perform Connection Setup with the specified set of setup parameters.
   *
   * @param currentLocation The current country location of the GridTalk
   * @param servicingRouter IP of the Jms Router that serves this operation.
   * @param securityPassword Security Password to perform the operation.
   */
  public void setupConnection(
    String currentLocation, String servicingRouter, String securityPassword)
    throws ConnectionSetupException, SystemException, RemoteException;

  /**
   * Get the current Connection setup result.
   *
   * @return The current ConnectionSetupResult.
   */
  public ConnectionSetupResult getConnectionSetupResult()
    throws ConnectionSetupException, SystemException, RemoteException;

  /**
   * Re-order the AvailableGridMasters and AvailableRouters in the ConnectionSetupResult.
   *
   * @param availableGridMasters Collection of UIDs of the available GridMasters
   * (GridNode).
   * @param availableRouters Collection of UIDs of the available routers (JmsRouter).
   * @throws ConnectionSetupException
   */
  public void reorderConnectionPriority(
    Collection availableGridMasters, Collection availableRouters)
    throws ConnectionSetupException, SystemException, RemoteException;

  /**
   * Retrieve JmsRouter(s) using a filtering condition.
   *
   * @param filter The filtering condition
   * @returns Collection of JmsRouter(s) retrieved based on the specified
   * filter, or empty Collection if none that matches the filter condition.
   */
  public Collection getJmsRouters(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Retrieve a JmsRouter using the specified uid.
   *
   * @param uid The UID of the JmsRouter to retrieve.
   * @return The JmsRouter retrieved.
   * @throws FindEntityException No JmsRouter found with the specified uid.
   */
  public JmsRouter getJmsRouter(Long uid)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Delete a JmsRouter using the specified uid.
   *
   * @param uid The UID of the JmsRouter to delete.
   * @throws DeleteEntityException No such JmsRouter found with the specified
   * uid for deletion.
   */
  public void deleteJmsRouter(Long uid)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * Initiate the connection to a GridMaster.
   *
   * @throws ConnectionException Unable to connect to the GridMaster or Connection
   * setup has not been completed successfully.
   */
  public void connect()
    throws ConnectionException, SystemException, RemoteException;

  /**
   * Disconnect from the GridMaster currently connected.
   *
   * @throws DisconnectionException Unable to disconnect from the GridMaster,
   * or no connection with any GridMaster currently.
   */
  public void disconnect()
    throws DisconnectionException, SystemException, RemoteException;

  /**
   * Send Keep Alive signal to the currently connected GridMaster.
   */
  public void sendKeepAlive()
    throws RemoteException;

  /**
   * Invoked when a Connection process message is received.
   *
   * @param eventID The event ID of the message.
   * @param eventSubID The event sub id of the message.
   * @param refTransID the reference TransID from the sender.
   * @param dataPayload The data payload received.
   * @param filePayload the file payload received.
   */
  public void receiveConnectionMessage(
    String eventID, String eventSubID, String refTransID,
    String[] dataPayload, File[] filePayload)
    throws SystemException, RemoteException;

  /**
   * Invoked when a GridNode is being activated successfully as a partner.
   *
   * @param partnerNodeID GridNodeID of the partner.
   */
  public void onPartnerActivated(String partnerNodeID)
    throws SystemException, RemoteException;

  /**
   * Invoked when a partner Gridnode is being deactivated.
   *
   * @param partnerNodeID GridNodeID of the partner.
   */
  public void onPartnerDeactivated(String partnerNodeID)
    throws SystemException, RemoteException;

  /**
   * Invoked when the system detects a connection lost to local or network
   * Jms router.
   *
   * @param header Information header for connection identification.
   * @param message Exception message for connection lost.
   */
  public void onConnectionLost(String[] header, String message)
    throws SystemException, RemoteException;
  
  /**
   * Invoked when the system is started up. Possible handling would be
   * to auto-connect to GridMaster.
   */
  public void onStartup()
    throws SystemException, RemoteException;  
}