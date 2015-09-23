/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTTransportControllerTest.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Nov 07 2002    Goh Kan Mun                 Updated test.
 * Jan 06 2003    Goh Kan Mun                 Update test.
 */

package com.gridnode.pdip.base.transport.test;

/**
 * <p>Title: PDIP : Peer Data Interchange Platform</p>
 * <p>Description: Transport Module - for PDIP GT(AS)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd - Singapore</p>
 * @author Jagadeesh
 * @version 1.0
 */


import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportControllerHome;
import com.gridnode.pdip.base.transport.facade.ejb.ITransportControllerObj;
import com.gridnode.pdip.base.transport.helpers.ITransportConstants;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GTTransportControllerTest extends TestCase
{
  public ITransportControllerObj _controller = null;

  private static String _hostName="localhost";
  private static String _userName="testUser";
  private static String _password="testPassword";
  private static String _topicName="test";
  private static final String CLASSNAME = "GTTransportControllerTest";
  private JMSCommInfo _valid_v2 = null;
  private JMSCommInfo _valid_v3 = null;
  private JMSCommInfo _invalid = null;

  public GTTransportControllerTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TptLogger.debugLog(CLASSNAME, "suite", "Creating test suite...");
    return new TestSuite(GTTransportControllerTest.class);
  }


  public void setUp()
  {
    TptLogger.debugLog(CLASSNAME, "setUp", "Setting up...");
    try
    {
      ServiceLocator locator = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT);
      _controller = (ITransportControllerObj) locator.getObj(
                              ITransportControllerHome.class.getName(),
                              ITransportControllerHome.class,
                              new Object[0]
                              );

      _valid_v3 = createJMSCommInfo(_hostName, 443, ICommInfo.JMS,
              "030000", _topicName, JMSCommInfo.TOPIC, _userName, _password);
      _valid_v2 = createJMSCommInfo(_hostName, 443, ICommInfo.JMS,
              "020002", _topicName, JMSCommInfo.TOPIC, _userName, _password);
      _invalid = createJMSCommInfo("192.3.2.1", 40001, ICommInfo.JMS,
              "020002", "test", JMSCommInfo.TOPIC, "testUser", "testPassword");
    }
    catch(Exception ex)
    {
      TptLogger.errorLog(CLASSNAME, "setUp", " Exception in SetUp  : ", ex);
    }
  }

  public void testConnectDisconnect()
  {
    try
    {
      connectFail(null);
      connectFail(_invalid);
      connectPass(_valid_v2);
      disconnectFail(null);
      disconnectFail(_invalid);
      disconnectPass(_valid_v2);
      disconnectFail(_valid_v3); // Attemp to disconnect an unexisting connection.

    }
    catch (Throwable t)
    {
      TptLogger.errorLog(CLASSNAME, "testConnectDisconnect", "Test Stopped. " +
                                    "Not able to continue other test", t);
      assertTrue(t.getMessage(), false);
    }
  }


//  public void testSend()
//  {
//    TptLogger.debugLog(CLASSNAME, "testSend", "Testing testSend()...");
//    try
//    {
//        String data[] = {"abc","cde","efg","hij"};
//        assertNotNull(controller);
//        CommInfoTest commInfo = new CommInfoTest(hostName, 443, "jms", "1.0.2", null,
//            topicName, IJMSCommInfo.TOPIC, userName, password);
//
//        String[] header = null;
//        byte[] fileData = null;
//        //jianyu -- "commInfo" is not used here since it's supposed to be passed
//        //by BL to TransportControllerBean in the same JVM. Instead "commInfo" is
//        //hardcoded for testing purpose for the time being.
//        controller.send(null, header, data, fileData);
//
//    }
//    catch(Exception ex)
//    {
//       TptLogger.errorLog(CLASSNAME, "testSend", "Cannot Send in GTTransportControllerTest ", ex);
//    }
//
//  }
//

//  public void testSendAndReceive() throws Exception
//  {
//    try
//    {
//    Hashtable header = getData("1", "2", "3", "4", null, null, "TestFile" + new Date(), null);
//    String[] data = {"1", "Test data 2", "Test data Test data 3"};
//    byte[] byteData = "12345".getBytes();
//    _controller.connectAndListen(_valid_v3, null);
//
//    _controller.send(_valid_v2, header, data, byteData);
//    assertEquals(header, rec._header);
//    assertEquals(data, rec._data);
//    assertEquals(byteData, rec._fileData);
//    assertEquals(_valid, rec._commInfo);
//    }
//    finally
//    {
//      _controller.disconnect(_valid_v3);
//    }
//  }

  public static void main(String args[])
  {
    if (args.length > 0)
      _hostName = args[0];
    if (args.length > 1)
      _topicName = args[1];
    if (args.length > 2)
      _userName = args[2];
    if (args.length > 3)
      _password = args[3];
    TptLogger.debugLog(CLASSNAME, "main", "GTTransportControllerTest: starting up...");
    TptLogger.debugLog(CLASSNAME, "main", "Host: " + _hostName);
    TptLogger.debugLog(CLASSNAME, "main", "Topic: " + _topicName);
    TptLogger.debugLog(CLASSNAME, "main", "User: " + _userName);
    TptLogger.debugLog(CLASSNAME, "main", "Password: " + _password);
    junit.textui.TestRunner.run (suite());
  }

  private void connectFail(ICommInfo commInfo)
  {
    try
    {
      _controller.connect(commInfo, null);
      assertTrue("Connect should fails.", false);
    }
    catch(Exception e)
    {
      TptLogger.infoLog(CLASSNAME, "connectFail", e.getMessage());
    }
  }

  private void connectPass(ICommInfo commInfo)
  {
    try
    {
      _controller.connect(commInfo, null);
    }
    catch(Exception e)
    {
      TptLogger.infoLog(CLASSNAME, "connectPass", "Error: ", e);
      assertTrue("Connect should pass.", false);
    }
  }

  private void disconnectFail(ICommInfo commInfo)
  {
    try
    {
      _controller.disconnect(commInfo);
      assertTrue("Disconnect should fails.", false);
    }
    catch(Exception ex)
    {
      TptLogger.errorLog(CLASSNAME, "disconnectFail", ex.getMessage());
    }
  }

  private void disconnectPass(ICommInfo commInfo)
  {
    try
    {
      _controller.disconnect(commInfo);
    }
    catch(Exception ex)
    {
      TptLogger.errorLog(CLASSNAME, "disconnectPass", "Cannot Disconnect in test ", ex);
      assertTrue("Disconnect should pass.", false);
    }
  }

  private void connectAndListenFail(ICommInfo commInfo)
  {
    try
    {
     _controller.connectAndListen(commInfo, null);
      assertTrue("connectAndListen should fails.", false);
    }
    catch(Exception ex)
    {
      TptLogger.errorLog(CLASSNAME, "connectAndListenFail", ex.getMessage());
    }
  }

  private void connectAndListenPass(ICommInfo commInfo)
  {
    try
    {
      _controller.connectAndListen(commInfo, null);
    }
    catch(Exception ex)
    {
      TptLogger.errorLog(CLASSNAME, "connectAndListenPass", "Cannot connectAndListen in test ", ex);
      assertTrue("connectAndListen should pass.", false);
    }
  }

  private JMSCommInfo createJMSCommInfo(String hostName, int port, String protocolType,
              String version, String topicName, int destinationType, String userName, String password)
  {
    JMSCommInfo commInfo = new JMSCommInfo();
    commInfo.setDestination(topicName);
    commInfo.setDestType(destinationType);
    commInfo.setHost(hostName);
    commInfo.setPassword(password);
    commInfo.setPort(port);
    commInfo.setProtocol(protocolType);
    commInfo.setTptImplVersion(version);
    commInfo.setUserName(userName);
    return commInfo;
  }

  private Hashtable getData(String eventId, String transactionId, String recipient,
          String sender, String eventSubId, String processId, String fileId,
          String channelName)
  {
    Hashtable envelopeheader = new Hashtable();
    envelopeheader.put(ITransportConstants.EVENT_ID,
                          eventId);
    envelopeheader.put(ITransportConstants.TRANSACTION_ID,
                          transactionId);
    envelopeheader.put(ITransportConstants.RECEIPENT_NODE_ID,
                          recipient == null ? new String("0") :
                          recipient);
    envelopeheader.put(ITransportConstants.SENDER_NODE_ID,
                          sender == null ? new String("0") :
                          sender);
    envelopeheader.put(ITransportConstants.EVENT_SUB_ID,
                          eventSubId == null ? new String("0") :
                          eventSubId);
    envelopeheader.put(ITransportConstants.PROCESS_ID,
                          processId == null ? new String("0") :
                          processId);
    envelopeheader.put(ITransportConstants.FILE_ID,
                          fileId == null ? ITransportConstants.UNDEFINED_FILE_ID :
                          fileId);
    envelopeheader.put("ChannelName",
                          channelName == null ? "Undefined Channel" :
                          channelName);
    return envelopeheader;
  }

}
