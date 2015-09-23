/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.connection.exceptions.ConnectionSetupException;
import com.gridnode.gtas.server.connection.model.AbstractXmlEntity;
import com.gridnode.gtas.server.connection.model.ConnectionSetupParam;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;

/** 
 * This EntityHandler handles the ConnectionSetupResult entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupEntityHandler
  extends ConnectionEntityHandler
{
  //private static final Object _lock = new Object();
  private static ConnectionSetupEntityHandler _self = null;

  private String _defServicingRouter;
  private String _defLocation;

  private ConnectionSetupEntityHandler() throws Throwable
  {
    super(CONNECTION_SETUP_ENTITY);
  }

  /**
   * Get an instance of the ConnectionSetupEntityHandler.
   */
  public static synchronized final ConnectionSetupEntityHandler getInstance() throws Throwable
  {
    if (_self == null)
    {
      _self = new ConnectionSetupEntityHandler();
    }
    return _self;
  }

  // *****************  Utility methods *******************************

  public void updateSetupParam(String currentLocation, String servicingRouter)
    throws Throwable
  {
    validateString(currentLocation, "Bad Current Location!");
    validateString(servicingRouter, "Bad Servicing Router!");

    ConnectionSetupResult setup = (ConnectionSetupResult)retrieve();
    setup.getSetupParams().setCurrentLocation(currentLocation);
    setup.getSetupParams().setServicingRouter(servicingRouter);

    save(setup);
  }

  public void reorderConnectionPriority(
    Collection availableGridMasters, Collection availableRouters)
    throws Throwable
  {
    ConnectionSetupResult setup = (ConnectionSetupResult)retrieve();
    setup.setAvailableGridMastersUIDs(availableGridMasters);
    setup.setAvailableRouterUIDs(availableRouters);

    save(setup);
  }

  /**
   * Validate the fields in the specified ConnectionSetup.
   *
   * @param setting The ConnectionSetup to be validated.
   */
  protected void validate(AbstractXmlEntity entity)
    throws Throwable
  {
  }

  protected void readConfig() throws Throwable
  {
    _defServicingRouter = getConfig().getString(SERVICING_ROUTER+getNodeUsage());
    validateString(_defServicingRouter, "No Servicing Router configured!");

    _defLocation = getCountryCode();
  }

  protected AbstractXmlEntity getDefaultEntity()
  {
    ConnectionSetupResult def = new ConnectionSetupResult();
    ConnectionSetupParam param = new ConnectionSetupParam();
    param.setCurrentLocation(_defLocation);
    param.setServicingRouter(_defServicingRouter);
    param.setOriginalLocation(_defLocation);
    param.setOriginalServicingRouter(_defServicingRouter);
    def.setSetupParams(param);

    return def;
  }

  private String getNodeUsage() throws Throwable
  {
    String nodeUsage = null;

    try
    {
      String category = ServiceLookupHelper.getGridNodeManager().findMyGridNode().getCategory();
      nodeUsage = ServiceLookupHelper.getGridNodeManager().findGnCategoryByCode(category).getNodeUsage();
    }
    catch (Throwable t)
    {
      Logger.err("[ConnectionSetupEntityHandler.getNodeUsage] Error ", t);
      throw new ConnectionSetupException("No GridNode has been setup!");
    }

    return nodeUsage;
  }

  private String getCountryCode() throws Throwable
  {
    String countryCode = null;
    try
    {
      countryCode = ServiceLookupHelper.getGridNodeManager().getMyCompanyProfile().getCountry();
    }
    catch (Throwable t)
    {
      Logger.err("[ConnectionSetupEntityHandler.getCountryCode] Error ", t);
      throw new ConnectionSetupException("No Company Profile has been setup!");
    }
    return countryCode;
  }
}