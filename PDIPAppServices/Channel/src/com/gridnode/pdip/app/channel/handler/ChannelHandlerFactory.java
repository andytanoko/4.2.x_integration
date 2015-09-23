/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelHandlerFactory.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 16 2002    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel.handler;

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.IChannelConfig;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

public class ChannelHandlerFactory
{
  public static final String CLASS_NAME = "ChannelHandlerFactory";

  public ChannelHandlerFactory()
  {
  }

  public static IChannelHandler getChannelHandler() throws Exception
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "In getChannelHandler");
      Configuration config =
        ConfigurationManager.getInstance().getConfig(
          IChannelConfig.CONFIG_NAME);
      String className =
        config.getString(IChannelConfig.DEFAULT_CHANNEL_HANDLER);
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "Class Name " + className);
      IChannelHandler handler =
        (IChannelHandler) Class.forName(className).newInstance();
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "Class Name OF Insti Class" + handler.getClass().getName());
      return handler;
    }
    catch (ClassNotFoundException cnex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "ClassNotFound" + cnex.getMessage());
      throw new Exception(cnex.getMessage());
    }
    catch (InstantiationException iex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "ClassNotFound" + iex.getMessage());
      throw new Exception(iex.getMessage());
    }
    catch (Exception ex)
    {
      throw new Exception(ex.getMessage());
    }

  }

  public static IChannelHandler getChannelHandler(int channelHandler)
    throws Exception
  {
    try
    {
      if (channelHandler == IChannelHandler.JMS_CHANNEL_HANDLER)
      {
        Configuration config =
          ConfigurationManager.getInstance().getConfig(
            IChannelConfig.CONFIG_NAME);
        String className =
          config.getString(IChannelConfig.DEFAULT_CHANNEL_HANDLER);
        return (IChannelHandler) Class.forName(className).newInstance();
      }
      else if (channelHandler == IChannelHandler.HTTP_CHANNEL_HANDLER)
      {
        Configuration config =
          ConfigurationManager.getInstance().getConfig(
            IChannelConfig.CONFIG_NAME);
        String className =
          config.getString(IChannelConfig.HTTP_CHANNEL_HANDLER);
        return (IChannelHandler) Class.forName(className).newInstance();
      }

    }
    catch (ClassNotFoundException cnex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "ClassNotFound" + cnex.getMessage());
      throw new Exception(cnex.getMessage());
    }
    catch (InstantiationException iex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelHandler",
        "ClassNotFound" + iex.getMessage());
      throw new Exception(iex.getMessage());
    }
    catch (Exception ex)
    {
      throw new Exception(ex.getMessage());
    }
    return null;
  }

  public static void main(String args[])
  {

  }

}