/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultChannelHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 14 2002    Jagadeesh               Created
 * Nov 10 2005    Neo Sok Lay             Use ServiceLocator instead of ServiceLookup
 */

package com.gridnode.pdip.app.channel.handler;

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportControllerHome;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportControllerObj;
import com.gridnode.pdip.framework.util.ServiceLocator;

public abstract class DefaultChannelHandler implements IChannelHandler
{

  //protected ITransportControllerHome _tptCntrlHome;
  protected ITransportControllerObj _tptCntrlObject;
  //  protected MessageHandlerRegistry _msgHandlreReg   =null;
  private static final String CLASS_NAME = "DefaultChannelHandler";

  /**
   * Initilize
   */

  public DefaultChannelHandler() throws Exception
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "DefaultChannelHandler()",
      "In Constructor");
    ChannelLogger.infoLog(
      CLASS_NAME,
      "DefaultChannelHandler()",
      "Initilize TPT Facade");
    initTransportController();
    ChannelLogger.infoLog(
      CLASS_NAME,
      "DefaultChannelHandler()",
      "Initilize MessageHandler");
  }

  public void initTransportController() throws Exception
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "initTransportController()",
      "In init -" + "of Transport Controller ");
    //_tptCntrlHome =
    //  (ITransportControllerHome) ServiceLookup
    //    .getInstance(ServiceLookup.CLIENT_CONTEXT)
    //    .getHome(ITransportControllerHome.class);
    //_tptCntrlObject = _tptCntrlHome.create();
    _tptCntrlObject = (ITransportControllerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
                        ITransportControllerHome.class.getName(),
                        ITransportControllerHome.class,
                        new Object[0]);

    ChannelLogger.infoLog(
      CLASS_NAME,
      "initTransportController()",
      "Created Transport" + "Controller Object - Facade");

  }

}