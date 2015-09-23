/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: TransportServiceDelegate.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Oct 29 2003    Ang Meng Hua      Created
 * Nov 05 2003    Jagadeesh         Added : Delegate Methods for Transport Services.
 * Dec 17 2003    Jagadeesh         Modified: To use new Implementation of
 *                                  CommInfoFactory.
 *
 */

package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.exceptions.TransportException;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.transport.comminfo.CommInfoFactory;
import com.gridnode.pdip.base.transport.exceptions.InvalidCommInfoException;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportServiceHome;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportServiceObj;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class TransportServiceDelegate
{
  private static final String CLASS_NAME = "TransportServiceDelegate";

  public TransportServiceDelegate()
  {
  }

  public static Message send(
    com.gridnode.pdip.app.channel.model.CommInfo commInfo,
    Message message)
    throws TransportException
  {
    try
    {
      return getTransportServiceFacade().send(
        getBaseCommInfo(commInfo),
        message);
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "send()", "Unable to send message");

      throw new TransportException("Unable to send message", ex);
    }
  }

  public static void connect(CommInfo commInfo, String[] header)
    throws TransportException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "connect()",
        "[Before Performing connect]");

      if (commInfo != null)
        getTransportServiceFacade().connect(getBaseCommInfo(commInfo), header);
      else
        throw new TransportException(
          CLASS_NAME + "[connect()][CommInfo is Null]");
      //ChannelLogger.infoLog(CLASS_NAME,"connect","After Performing connect");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "connect()", "Cannot Connect", ex);
      throw new TransportException(
        CLASS_NAME
          + "[connect()][Cannot Connect To URL]"
          + "["
          + commInfo.getURL()
          + "]",
        ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(CLASS_NAME, "connect()", "Cannot Connect", th);
      throw new SystemException(
        CLASS_NAME
          + "[connect()][Cannot Connect To URL]"
          + "["
          + commInfo.getURL()
          + "]",
        th);
    }

  }

  public static void connectAndListen(CommInfo commInfo, String[] header)
    throws TransportException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "connectAndListen()",
        "Before Performing connectAndListen");
      if (commInfo != null)
        getTransportServiceFacade().connectAndListen(
          getBaseCommInfo(commInfo),
          header);
      else
        throw new TransportException(
          CLASS_NAME + "[connectAndListen()][CommInfo is Null]");

      ChannelLogger.infoLog(
        CLASS_NAME,
        "connectAndListen()",
        "After Performing connectAndListen");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "connectAndListen()",
        "Cannot connect And Listen",
        ex);
      throw new TransportException(
        CLASS_NAME
          + "[connect()][Cannot ConnectAndListen To URL]"
          + "["
          + commInfo.getURL()
          + "]",
        ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "connectAndListen()",
        "Cannot connect And Listen",
        th);
      throw new SystemException(
        CLASS_NAME
          + "[connect()][Cannot ConnectAndListen To URL]"
          + "["
          + commInfo.getURL()
          + "]",
        th);
    }

  }

  public static void disconnect(CommInfo commInfo)
    throws TransportException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "disconnect()",
        "[Before Performing disconnect]");
      if (commInfo != null)
        getTransportServiceFacade().disconnect(getBaseCommInfo(commInfo));
      else
        throw new TransportException(
          CLASS_NAME + "[connectAndListen()][CommInfo is Null]");

      ChannelLogger.infoLog(
        CLASS_NAME,
        "disconnect",
        "After Performing disconnect");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "disconnect()",
        "[Cannot disconnect via ",
        ex);
      throw new TransportException(
        CLASS_NAME
          + "disconnect()"
          + "[Cannot DisConnect from URL]"
          + "["
          + commInfo.getURL()
          + "]",
        ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "disconnect()",
        "Cannot disconnect via this Channel",
        th);
      throw new SystemException(
        CLASS_NAME
          + "disconnect()"
          + "[Cannot DisConnect from URL]"
          + "["
          + commInfo.getURL()
          + "]",
        th);
    }
  }

  public static void ping(CommInfo info)
    throws TransportException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "ping()", "[Not Yet Supported.]");
    }
    catch (Exception ex)
    {
      throw new TransportException("[Cannot Ping]", ex);
    }
    catch (Throwable th)
    {
      throw new SystemException("[Cannot Ping]", th);
    }
  }

  private static com
    .gridnode
    .pdip
    .base
    .transport
    .comminfo
    .ICommInfo getBaseCommInfo(
      com.gridnode.pdip.app.channel.model.CommInfo commInfo)
    throws InvalidCommInfoException, ApplicationException
  {
    try
    {
      com.gridnode.pdip.base.transport.comminfo.ICommInfo tptCommInfo =
        CommInfoFactory.getTransportCommInfo(commInfo.getProtocolType());
      tptCommInfo.setTptImplVersion(commInfo.getTptImplVersion());
      tptCommInfo.setURL(commInfo.getURL());
      return tptCommInfo;
    }
    catch (java.net.MalformedURLException ex)
    {
      throw new ApplicationException(ex.getMessage(), ex);
    }

    /*return CommInfoFactory.getCommInfo(
      commInfo.getProtocolType(),
      commInfo.getURL(),
      commInfo.getTptImplVersion());
    */
  }

  private static ITransportServiceObj getTransportServiceFacade()
    throws ServiceLookupException
  {
    return (ITransportServiceObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        ITransportServiceHome.class.getName(),
        ITransportServiceHome.class,
        new Object[0]);
  }

}