/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelServiceHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 30 2002    Jagadeesh               Created
 * Oct 20 2003    Neo Sok Lay             getCommInfo() using CommInfoFactory.
 * Nov 10 2005    Neo Sok Lay             Use ServiceLocator instead of ServiceLookup
 */

package com.gridnode.pdip.app.channel.handler;

import java.io.File;

import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.transport.comminfo.CommInfoFactory;
import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportControllerHome;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportControllerObj;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class ChannelServiceHandler
{

  private static final String CLASS_NAME = "ChannelServiceHandler";

  //private ITransportControllerHome _tptCntrlHome = null;
  private ITransportControllerObj _tptCntrlObject = null;

  public void connect(CommInfo commInfo, String[] header)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "connect", "Before Performing connect");
      if (commInfo != null)
        _tptCntrlObject.connect(getCommInfo(commInfo), header);
      else
        ChannelLogger.infoLog(CLASS_NAME, "connect", "CommInfo is Null ");
      ChannelLogger.infoLog(CLASS_NAME, "connect", "After Performing connect");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "connect()", "Cannot Connect", ex);
      throw new ChannelException("Cannot Connect ", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(CLASS_NAME, "connect()", "Cannot Connect", th);
      throw new SystemException("Cannot Connect ", th);
    }

  }

  public void connectAndListen(CommInfo commInfo, String[] header)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "connectAndListen()",
        "Before Performing connectAndListen");
      if (commInfo != null)
        _tptCntrlObject.connectAndListen(getCommInfo(commInfo), header);
      else
        ChannelLogger.infoLog(
          CLASS_NAME,
          "connectAndListen()",
          "CommInfo is Null");
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
      throw new ChannelException("Cannot Connect And Listen", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "connectAndListen()",
        "Cannot connect And Listen",
        th);
      throw new SystemException("Cannot Connect And Listen", th);
    }

  }

  public void send(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "send",
        "Before Obtaining SendMessageHandler");
      SendMessageHandler handler = SendMessageHandler.getSendMessageHandler();
      ChannelLogger.infoLog(
        CLASS_NAME,
        "send",
        "After Obtaining SendMessageHandler");
      handler.processSendMessage(info, dataToSend, file, header);
      ChannelLogger.infoLog(
        CLASS_NAME,
        "send",
        "ChannelInfo" + info.toString());
      ChannelLogger.infoLog(CLASS_NAME, "send", "Send via SendMessageHandler");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "Send()",
        "Cannot Send via this Channel",
        ex);
      throw new ChannelException("Cannot Connect ", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "Send()",
        "Cannot Send via this Channel",
        th);
      throw new SystemException("Cannot Connect ", th);
    }

  }

  public void disconnect(CommInfo commInfo)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "disconnect()",
        "Before Performing disconnect");
      if (commInfo != null)
        _tptCntrlObject.disconnect(getCommInfo(commInfo));
      else
        ChannelLogger.infoLog(CLASS_NAME, "disconnect()", "CommInfo is Null");
      ChannelLogger.infoLog(
        CLASS_NAME,
        "disconnect",
        "Before Performing disconnect");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "disconnect()",
        "Cannot disconnect via this Channel",
        ex);
      throw new ChannelException("Cannot DisConnect ", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "disconnect()",
        "Cannot disconnect via this Channel",
        th);
      throw new SystemException("Cannot DisConnect ", th);
    }
  }

  public void ping(CommInfo info) throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "ping", "Not Yet Supported .");
    }
    catch (Exception ex)
    {
      throw new ChannelException("Cannot Ping", ex);
    }
    catch (Throwable th)
    {
      throw new SystemException("Cannot Ping", th);
    }
  }

  /**
   * This Method Converts the generic CommInfo to Protocol Specific CommInfo.
   * @param commInfo
   * @return
   * @throws Exception
   */

  /*  private JMSCommInfo converToJMSCommInfo(CommInfo commInfo) throws Exception
    {
      ChannelLogger.infoLog(CLASS_NAME,"converToJMSCommInfo()",
      "Converting to JMSCommInfo.");
      return new JMSCommInfo(commInfo);
    }
  */

  public ChannelServiceHandler() throws Exception
  {
    initTransportController();
  }

  private void initTransportController() throws Exception
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "initTransportController()",
      "In init -" + "of Transport Controller ");
    //_tptCntrlHome =
    //  (ITransportControllerHome) ServiceLookup
    //    .getInstance(ServiceLookup.LOCAL_CONTEXT)
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

  //Will be changed with reflection.
  private ICommInfo getCommInfo(CommInfo commInfo) throws Exception
  {
    String protocolType = commInfo.getProtocolType();
    ChannelLogger.infoLog(
      "JMSSendMessageListenerMDBean",
      "getCommInfo()",
      "Protocol Type :" + protocolType);
    ChannelLogger.infoLog(
      "JMSSendMessageListenerMDBean",
      "getCommInfo()",
      "URL= : " + commInfo.getURL());

    ICommInfo tptCommInfo = CommInfoFactory.getTransportCommInfo(protocolType);
    tptCommInfo.setTptImplVersion(commInfo.getTptImplVersion());
    tptCommInfo.setURL(commInfo.getURL());

    return tptCommInfo;
    /*031020NSL refactored to obtain from CommInfoFactory 
     if(protocolType != null)
     {
       if(protocolType.equals(CommInfo.JMS))
       {
         ChannelLogger.infoLog("JMSSendMessageListenerMDBean","getCommInfo()",
         "SJMSCommInfo Created");
         JMSCommInfo jmsinfo = new JMSCommInfo(commInfo.getURL()); //Change..
         jmsinfo.setTptImplVersion(commInfo.getTptImplVersion());
         return jmsinfo;
       }
       else if(protocolType.equals(CommInfo.HTTP))
       {
         ChannelLogger.infoLog("JMSSendMessageListenerMDBean","getCommInfo()",
         "HttpCommInfo Created");
         HttpCommInfo httpinfo = new HttpCommInfo(commInfo.getURL());
         httpinfo.setTptImplVersion(commInfo.getTptImplVersion());
         return httpinfo;
       }
     }
     return null;
     */
  }

}
