/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionServiceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 * Dec 10 2002    Neo Sok Lay         Handle connection lost --> to reconnect.
 * Apr 02 2003    Neo Sok Lay         GNDB00013139: checkValidGmNodes()
 *                                    condition logic wrong.
 * Apr 26 2004    Neo Sok Lay         GNDB00021907: Handle connection lost if
 *                                    SendKeepAlive fail.
 */
package com.gridnode.gtas.server.connection.facade.ejb;

import com.gridnode.gtas.server.connection.connect.*;
import com.gridnode.gtas.server.connection.exceptions.*;
import com.gridnode.gtas.server.connection.helpers.*;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.gtas.server.connection.setup.ConnectionPostsetupDelegate;
import com.gridnode.gtas.server.connection.setup.ConnectionPresetupDelegate;
import com.gridnode.gtas.server.connection.setup.ConnectionSetupContext;
import com.gridnode.gtas.server.connection.setup.ConnectionSetupRequestDelegate;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.registration.exceptions.InvalidSecurityPasswordException;
import com.gridnode.gtas.server.registration.product.RegistrationLogic;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.util.PasswordMask;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This EJB handles the GridTalk Connection services.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class ConnectionServiceBean implements SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -99623811978853353L;
	private SessionContext    _ctx;

  public ConnectionServiceBean()
  {
  }

  public void setSessionContext(SessionContext ctx)
  {
    _ctx = ctx;
  }

  public void ejbCreate()
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

  /**
   * @see IConnectionServiceObj@getNetworkSetting
   */
  public NetworkSetting getNetworkSetting()
    throws NetworkSettingException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "getNetworkSetting";
    Object[] params     = new Object[] {
                          };

    NetworkSetting setting = null;
    try
    {
      logger.logEntry(methodName, params);

      setting = (NetworkSetting)NetworkSettingEntityHandler.getInstance().retrieve();
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new NetworkSettingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return setting;
  }

  /**
   * @see IConnectionServiceObj#saveNetworkSetting
   */
  public void saveNetworkSetting(NetworkSetting settings)
    throws NetworkSettingException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "saveNetworkSetting";
    Object[] params     = new Object[] {
                            settings,
                          };

    try
    {
      logger.logEntry(methodName, params);

      NetworkSettingEntityHandler.getInstance().save(settings);
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new NetworkSettingException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#getConnectionSetupResult
   */
  public ConnectionSetupResult getConnectionSetupResult()
    throws ConnectionSetupException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "getConnectionSetupResult";
    Object[] params     = new Object[] {
                          };

    ConnectionSetupResult setup = null;
    try
    {
      logger.logEntry(methodName, params);

      setup = (ConnectionSetupResult)ConnectionSetupEntityHandler.getInstance().retrieve();
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new ConnectionSetupException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return setup;
  }

  /**
   * @see IConnectionServiceObj#setupConnection
   */
  public void setupConnection(
    String currentLocation, String servicingRouter, String securityPassword)
    throws ConnectionSetupException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "setupConnection";
    Object[] params     = new Object[] {
                            currentLocation,
                            servicingRouter,
                            new PasswordMask(securityPassword).getMask(),
                          };

    try
    {
      logger.logEntry(methodName, params);

      // save the setup params
      ConnectionSetupEntityHandler.getInstance().updateSetupParam(
        currentLocation, servicingRouter);

      IRegistrationServiceObj regService = ServiceLookupHelper.getRegistrationService();
      regService.verifyAndSetSecurityPassword(securityPassword);

      ConnectionSetupResult setup = getConnectionSetupResult();
      NetworkSetting setting      = getNetworkSetting();

      ConnectionSetupContext.clearContext();
      ConnectionSetupContext ctx  = ConnectionSetupContext.getInstance();

      ctx.setProductKey(regService.getRegisteredProductKey());
      ctx.setGridNodeID(regService.getRegistrationInfo().getGridnodeID());
      ctx.setSignCert(regService.getMasterCert());
      ctx.setEncryptionCert(regService.getGridMasterCert());
      ctx.setCountryCode(currentLocation);
      ctx.setServicingRouter(servicingRouter);
      ctx.setResponseTimeout(setting.getResponseTimeout().intValue());
      ctx.setConnectionSetupResult(setup);
      ctx.setMasterCertName(RegistrationLogic.GT_CERT_NAME);

      // perform the connection setup
      if (setup.getStatus().shortValue() != ConnectionSetupResult.STATUS_SUCCESS)
      {
        ctx.setCertRequest(regService.getCertificateRequest());

        try
        {
          new ConnectionPresetupDelegate(ctx).execute();
          new ConnectionSetupRequestDelegate(ctx).execute();
          setup.setStatus(new Short(ConnectionSetupResult.STATUS_SUCCESS));
          setup.setFailureReason(null);
        }
        catch (ConnectionSetupException t)
        {
          setup.setStatus(new Short(ConnectionSetupResult.STATUS_FAILURE));
          setup.setFailureReason(t.getMessage());
        }
        finally // to allow for clean-up
        {
          ConnectionSetupEntityHandler.getInstance().save(setup);
          new ConnectionPostsetupDelegate(ctx).execute();
        }
      }
      else
      {
        throw new UnsupportedOperationException("Re-setup function not implemented yet");
        /**@todo for re-setup */
      }
    }
    catch (InvalidSecurityPasswordException ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
      throw ConnectionSetupException.invalidSecurityPassword(ex);
    }
    catch (ConnectionSetupException ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new ConnectionSetupException(t.getMessage());
    }
    finally
    {
      ConnectionSetupContext.clearContext();
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#reorderConnectionPriority
   */
  public void reorderConnectionPriority(
    Collection availableGridMasters, Collection availableRouters)
    throws ConnectionSetupException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "reorderConnectionPriority";
    Object[] params     = new Object[] {
                            availableGridMasters,
                            availableRouters,
                          };

    try
    {
      logger.logEntry(methodName, params);

      // check valid gm node uids
      checkValidGmNodes(availableGridMasters);

      // check valid router uids
      checkValidJmsRouters(availableRouters);

      // save the new order.
      ConnectionSetupEntityHandler.getInstance().reorderConnectionPriority(
        availableGridMasters, availableRouters);
    }
    catch (ConnectionSetupException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new ConnectionSetupException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Retrieve JmsRouter(s) using a filtering condition.
   *
   * @param filter The filtering condition
   * @returns Collection of JmsRouter(s) retrieved based on the specified
   * filter, or empty Collection if none that matches the filter condition.
   */
  public Collection getJmsRouters(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "getJmsRouters";
    Object[] params     = new Object[] {
                            filter,
                          };

    Collection results = null;
    try
    {
      logger.logEntry(methodName, params);

      results = JmsRouterEntityHandler.getInstance().getEntityByFilterForReadOnly(
                  filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Retrieve a JmsRouter using the specified uid.
   *
   * @param uid The UID of the JmsRouter to retrieve.
   * @return The JmsRouter retrieved.
   * @throws FindEntityException No JmsRouter found with the specified uid.
   */
  public JmsRouter getJmsRouter(Long uid)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "getJmsRouter";
    Object[] params     = new Object[] {
                            uid,
                          };

    JmsRouter router = null;
    try
    {
      logger.logEntry(methodName, params);

      router = (JmsRouter)JmsRouterEntityHandler.getInstance().getEntityByKeyForReadOnly(
                  uid);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return router;
  }

  /**
   * Delete a JmsRouter using the specified uid.
   *
   * @param uid The UID of the JmsRouter to delete.
   * @throws DeleteEntityException No such JmsRouter found with the specified
   * uid for deletion.
   */
  public void deleteJmsRouter(Long uid)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "deleteJmsRouter";
    Object[] params     = new Object[] {
                            uid,
                          };

    try
    {
      logger.logEntry(methodName, params);

      JmsRouterEntityHandler.getInstance().remove(uid);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  // ********************** Connection/Disconnection *************************

  /**
   * @see IConnectionServiceObj#connect
   */
  public void connect()
    throws ConnectionException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "connect";
    Object[] params     = new Object[] {

                          };

    try
    {
      logger.logEntry(methodName, params);

      connect(false);
    }
    catch (ConnectionException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new ConnectionException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * Connect to GridMaster.
   * 
   * @param auto <b>true</b> for auto-connect mode, <b>false</b> otherwise.
   * @throws Throwable Error during connection
   */
  private void connect(boolean auto) throws Throwable
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "connect";
    Object[] params     = new Object[] {
                            Boolean.valueOf(auto),
                          };

    try
    {
      logger.logEntry(methodName, params);

      ConnectionSetupResult setup = getConnectionSetupResult();
      if (setup.getStatus().shortValue() != ConnectionSetupResult.STATUS_SUCCESS)
      {
        if (auto)
        {
          logger.logMessage(methodName, params, "Connection Setup has not been completed. No Auto-connect required.");
          return;
        }
        else
          throw new ConnectionException("Connection Setup must be completed before connecting to GridMaster!");
      }

      NetworkSetting setting = getNetworkSetting();

      IRegistrationServiceObj regService = ServiceLookupHelper.getRegistrationService();
      String myNodeID = regService.getRegistrationInfo().getGridnodeID().toString();

      ConnectionContext ctx  = ConnectionContext.getInstance();

      ctx.setProductKey(regService.getRegisteredProductKey());
      ctx.setNetworkSetting(setting);
      ctx.setConnectionSetupResult(setup);
      ctx.setMyNodeID(myNodeID);

      if (auto)
        new AutoConnectDelegate(ctx).execute();
      else
        new ConnectDelegate(ctx).execute();
    }
    finally
    {
      logger.logExit(methodName, params);
    }
     
  }
  
  /**
   * @see IConnectionServiceObj#disconnect
   */
  public void disconnect()
    throws DisconnectionException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "disconnect";
    Object[] params     = new Object[] {

                          };

    try
    {
      logger.logEntry(methodName, params);

      boolean workInProgress = ServiceLookupHelper.getPostOffice().isPostmanWorkingNow();
      if (workInProgress)
        throw new DisconnectionException(
        "Delivery of messages to GridMaster is in progress currently. "+
        "Please wait for the delivery to finish before disconnecting.");

      ConnectionContext ctx = ConnectionContext.getInstance();

      if (ctx.getConnectedGmChannel() == null)
      {
        logger.logMessage(methodName, params,
          "No current connection with GridMaster to disconnect!");
      }
      else
      {
        new DisconnectDelegate(ctx).execute();
      }
      ConnectionContext.clearContext();
    }
    catch (DisconnectionException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new DisconnectionException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * @see IConnectionServiceObj@sendKeepAlive
   */
  public void sendKeepAlive()
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "sendKeepAlive";
    Object[] params     = new Object[] {

                          };

    try
    {
      logger.logEntry(methodName, params);

      ConnectionContext ctx = ConnectionContext.getInstance();

      if (ctx.getConnectedGmChannel() != null)
      {
        try
        {
          new SendKeepAliveDelegate(ctx).execute();
        }
        catch (ConnectionException ex)
        {
          onConnectionLost(
            new String[]{"GM.CONNECTION", ConnectionContext.CONN_LOST_NETWORK}, 
            ex.getMessage());
        }
      }
      else
      {
        logger.logMessage(methodName, params, "No connected GridMaster to send keep alive!");
        new KeepAliveTimerDelegate(ctx).cancelTimer();
      }
    }
    catch (Throwable t)
    {
      logger.logMessage(methodName, params, t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#receiveConnectionMessage
   */
  public void receiveConnectionMessage(
    String eventID, String eventSubID, String refTransID,
    String[] dataPayload, File[] filePayload)
    throws SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "receiveConnectionMessage";
    Object[] params     = new Object[] {

                          };

    try
    {
      logger.logEntry(methodName, params);

      ConnectionContext ctx = ConnectionContext.getInstance();

      EventReceiverDelegateMap.getReceiverDelegate(eventID).receive(
        eventSubID, refTransID, dataPayload, filePayload);
    }
    catch (Throwable t)
    {
      logger.logMessage(methodName, params, t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#onPartnerActivated
   */
  public void onPartnerActivated(String partnerNodeID)
    throws SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "onPartnerActivated";
    Object[] params     = new Object[] {
                            partnerNodeID,
                          };

    try
    {
      logger.logEntry(methodName, params);

      ConnectionContext ctx = ConnectionContext.getInstance();

      new InformPartnerOnlineDelegate(ctx, partnerNodeID).execute();
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#onPartnerDeactivated
   */
  public void onPartnerDeactivated(String partnerNodeID)
    throws SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "onPartnerDeactivated";
    Object[] params     = new Object[] {
                            partnerNodeID,
                          };

    try
    {
      logger.logEntry(methodName, params);

      ConnectionContext ctx = ConnectionContext.getInstance();

      new ReceiveOfflineNotificationDelegate(ctx, partnerNodeID).execute();
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#onConnectionLost
   */
  public void onConnectionLost(String[] header, String message)
    throws SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "onConnectionLost";
    Object[] params     = new Object[] {
                            header,
                            message,
                          };

    try
    {
      logger.logEntry(methodName, params);

       ConnectionContext ctx = ConnectionContext.getInstance();

       try
       {
         ctx.setConnectionLost(header[1]); //connection lost type: local/network
         // re-connect to GridMaster
         new ReconnectDelegate(ctx).execute();
         // reconnect should throw exception if final try still not successful.
       }
       catch (Throwable t)
       {
         logger.logMessage(methodName, params, t.getMessage());
         logger.logMessage(methodName, params, "Performing normal connection to GridMaster...");

         // do a normal connect for last try
         new ConnectDelegate(ctx).execute();
       }
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IConnectionServiceObj#onStartup
   */
  public void onStartup()
    throws SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "onStartup";
    Object[] params     = new Object[] {
                          };

    try
    {
      logger.logEntry(methodName, params);

      connect(true); //try to auto-connect to GridMaster
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  // ********************* Utility methods ********************************

  /**
   * Check that the specified UIDs of GridMaster nodes are all valid UIDs.
   *
   * @param gmUIDs Collection of UIDs.
   * @throws FieldValidationException Any of the specified UID does not exist
   * or the collection is empty.
   */
  private void checkValidGmNodes(Collection gmUIDs)
    throws Throwable
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "checkValidGmNodes";
    Object[] params     = new Object[] {
                            gmUIDs,
                          };

    try
    {
      logger.logEntry(methodName, params);

      if (gmUIDs.isEmpty())
        throw new FieldValidationException("Available GridMaster list should not be empty!");

      Collection invalid = new ArrayList(gmUIDs);
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(),
        new Short(GridNode.STATE_GM), false);
      filter.addDomainFilter(filter.getAndConnector(), GridNode.UID,
        gmUIDs, false);

      Collection valid = ServiceLookupHelper.getGridNodeManager().findGridNodesKeys(
                             filter);

      invalid.removeAll(valid);
      if (!invalid.isEmpty())
        throw new FieldValidationException("Invalid GridMasters specified: "+
                    invalid);
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new ConnectionSetupException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Check that the specified UIDs of JmsRouters are all valid UIDs.
   *
   * @param routerUIDs Collection of UIDs.
   * @throws FieldValidationException Any of the specified UID does not exist
   * or the collection is empty.
   */
  private void checkValidJmsRouters(Collection routerUIDs)
    throws Throwable
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "checkValidJmsRouters";
    Object[] params     = new Object[] {
                            routerUIDs,
                          };

    try
    {
      logger.logEntry(methodName, params);

      if (routerUIDs.isEmpty())
        throw new FieldValidationException("Available Routers list should not be empty!");

      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, JmsRouter.UID, routerUIDs, true);

      Collection invalid = JmsRouterEntityHandler.getInstance().getKeyByFilterForReadOnly(
                             filter);

      if (!invalid.isEmpty())
        throw new FieldValidationException("Invalid Jms Routers specified: "+invalid);
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new ConnectionSetupException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }


}