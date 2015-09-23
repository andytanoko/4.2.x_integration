/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailEventQueueTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.test;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.jms.*;
import javax.naming.*;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.IReprocessConstant;
//import com.gridnode.gtas.audit.common.IReprocessConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.util.jms.JmsSender;


/**
 * A test utility to send the AuditTrailData to both the RemoteEventQueue and LocalEventQueue.
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailEventQueueTest
{
  private static final String REMOTE_EVENT_QUEUE = "queue/gtvan/isat/RemoteEventQueue";
  private static final String LOCAL_EVENT_QUEUE = "queue/gtvan/isat/LocalEventQueue";
  
  private static Queue remoteEventQueue = null;
  private static Queue localEventQueue = null; 
  private static QueueConnectionFactory connFac = null;
  
  public static void main(String[] args) throws Exception
  {
    /*
    Context ctx = getContext();
    connFac = lookupQueueConnectionFactory(ctx);
    remoteEventQueue = lookupQueue(REMOTE_EVENT_QUEUE, ctx);
    localEventQueue = lookupQueue(LOCAL_EVENT_QUEUE, ctx);
    
    
    for(int i = 0; i < 1; i++)
    {
      Thread t = new Thread(
                          new Runnable()
                          {
    
                            public void run()
                            {
                              try
                              {
                                sendMsgToQueue(connFac, remoteEventQueue, getAuditTrailData());
                                //sendMsgToQueue(connFac, localEventQueue, getAuditTrailData());
                              }
                              catch(Exception ex)
                              {
                                ex.printStackTrace();
                              }
                            }
    
                          });
      t.start();
      System.out.println("Started t is "+t.hashCode());
    }
    
    //jmsSend(getAuditTrailData()); */
    
    //sentToReprocessDocQueue("OB-561");
    //sentToReprocessDocQueue("OB-575");
    //sentToReprocessDocQueue("f2e4ecc3-4a0a-4403-af3a-b910b4118f64");
    
    //reprocessImportEvent("IP-287", "dad77606-d53f-4df8-94eb-19b44795d53a");
    
    //cancel PI, 4619
    //cancelProcessInstance("4633");
    //sendMsgToQueue(connFac, remoteEventQueue, getAuditTrailData());
    
    simulateEventBroadCast(100);
  }
  
  private static void sentToReprocessDocQueue(String tracingID) throws Exception
  {
    Queue q = lookupQueue("queue/gtvan/gtat/reprocessQueue", getContext());
    
    QueueConnection qConnect = connFac.createQueueConnection();
    QueueSession qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    QueueSender qSender = qSession.createSender(q);
    MapMessage mapMsg = qSession.createMapMessage();
    
    mapMsg.setString(IReprocessConstant.USER_NAME, "admin");
    //mapMsg.setString(IReprocessConstant.PASSWORD, "admin");
    
    mapMsg.setString(IReprocessConstant.ACTIVITY_TYPE, IReprocessConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS);
    mapMsg.setString(IReprocessConstant.TRACING_ID, tracingID);
    
    qSender.send(mapMsg);
    qConnect.close();
  }
  
  /*
  private static void reprocessImportEvent(String msgID, String tracingID) throws Exception
  {
    Queue q = lookupQueue("queue/gtvan/gtat/reprocessDocQueue", getContext());
    QueueConnection qConnect = connFac.createQueueConnection();
    QueueSession qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    QueueSender qSender = qSession.createSender(q);
    
    MapMessage mapMsg = qSession.createMapMessage();
    
    mapMsg.setString(IReprocessConstant.USER_NAME, "admin");
    mapMsg.setString(IReprocessConstant.PASSWORD, "admin");
    
    mapMsg.setString(IReprocessConstant.ACTIVITY_TYPE, IReprocessConstant.ACTIVITY_REPROCESS_ACTIVITY_TRACE_EVENTS);
    mapMsg.setString(IReprocessConstant.MESSAGE_ID, msgID);
    mapMsg.setString(IReprocessConstant.TRACING_ID, tracingID);
    
    qSender.send(mapMsg);
    qConnect.close();
  }
  
  private static void cancelProcessInstance(String processUID) throws Exception
  {
    Queue q = lookupQueue("queue/gtvan/gtat/reprocessDocQueue", getContext());
    QueueConnection qConnect = connFac.createQueueConnection();
    QueueSession qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    QueueSender qSender = qSession.createSender(q);
    
    MapMessage mapMsg = qSession.createMapMessage();
    
    mapMsg.setString(IReprocessConstant.USER_NAME, "admin");
    mapMsg.setString(IReprocessConstant.PASSWORD, "admin");
    
    mapMsg.setString(IReprocessConstant.ACTIVITY_TYPE, IReprocessConstant.ACTIVITY_CANCEL_PROCESS);
    mapMsg.setString(IReprocessConstant.PROCESS_UID, processUID);
    
    qSender.send(mapMsg);
    qConnect.close();
  }
  */
  
  //To simulate the broadcast of DocumentFlowNotification to TransactionFlowMDBean
  public static void simulateEventBroadCast(int numSend) throws Exception
  {
    System.out.println("Simulate event broadcast....");
    
    for(int i = 0 ; i < numSend; i++)
    {
      try
      {
        InitialContext context = new InitialContext(getProperties());
        ConnectionFactory factory = (ConnectionFactory)context.lookup("java:/ConnectionFactory");
        Connection conn = factory.createConnection();
        Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = (Destination)context.lookup("topic/notifierTopic");
        MessageProducer producer = sess.createProducer(dest);
        
        DocumentFlowNotification notification = getEventInfo(i);
        ObjectMessage msg = sess.createObjectMessage();
        msg.setObject(notification);
  
        // setup the message selector options
        msg.setStringProperty("id", notification.getNotificationID());
        msg.setStringProperty(SystemUtil.HOSTID_PROP_KEY, "obelix"); //NSL20070214
        
        producer.send(msg);
        System.out.println("Successfuly send out msgID: "+i);
      }
      catch(Exception ex)
      {
        System.out.println("send failed: "+ex.getMessage());
      }
      
      try
      {
          Thread.sleep(1000);
      }
        catch(Exception ex)
        {
          
        }
      
    }
  }
  
  public static Properties getProperties()
  {
    Properties p = new Properties();
    p.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" ) ;
    p.setProperty("java.naming.provider.url","192.168.213.241:1100") ;
    p.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" ) ; 
    return p;
  }
  
  public static QueueConnectionFactory lookupQueueConnectionFactory(Context ctx) throws Exception
  {
    System.out.println("Looking up connection factory....");
    return (QueueConnectionFactory)ctx.lookup("java:/ConnectionFactory");
  }
  
  public static Queue lookupQueue(String queueName, Context ctx) throws Exception
  {
    System.out.println("Looking up queue "+queueName);
    return (Queue)ctx.lookup(queueName);
  }
  
  public static void sendMsgToQueue(QueueConnectionFactory connectionFactory, Queue queue, AuditTrailData msg) throws Exception
  {
    QueueConnection queueConnection = connectionFactory.createQueueConnection();
    QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    QueueSender queueSender = queueSession.createSender(queue);
    ObjectMessage message = queueSession.createObjectMessage();
    message.setObject(msg);
    
    queueSender.send(message);
    System.out.println("After send msg to queue");
    
    //queueSender.send(queueSession.createMessage());
    queueConnection.close();
  }
  
  public static Context getContext() throws Exception
  {
    return new InitialContext(getProperties());
  }
  
  public static AuditTrailData getAuditTrailData() throws Exception
  {
    return new AuditTrailData(getEventInfo(), false, null);
    //return new AuditTrailData(getEventInfo(), null);
  }
  
  public static EventInfo getEventInfo()
  {
    Random rd = new Random();
    int i = rd.nextInt();
    EventInfo info = new EventInfo(IAuditTrailConstant.CHANNEL_CONNECTIVITY, new Date(), "OB-"+i, "FAILED", "test", "dad77606-d53f-4df8-94eb-19b44795d53a", IAuditTrailConstant.EVENT_TYPE_SIGNAL,"DEF", "");
    return info;
  }
  
  public static BusinessDocument getBusinessDocument() throws Exception
  {
    FileInputStream in = new FileInputStream(new File("c:/UnPackage61814.txt"));
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    
    byte[] buffer = new byte[512];
    int readSoFar = 0;
    while( (readSoFar = in.read(buffer)) > 0)
    {
      byteOut.write(buffer, 0 , readSoFar);
    }
    
    in.close();
    byteOut.close();
    
    return new BusinessDocument(byteOut.toByteArray(), "unPackage1234.txt",true);
  }
  
  //test broadcast the TXMR event to TransactionFlowMDBean
  public static DocumentFlowNotification getEventInfo(int msgID) throws Exception
  {
    DocumentFlowNotification flow = createDocFlowNotification(EDocumentFlowType.CHANNEL_CONNECTIVITY, new Date(), "OB-"+msgID, true, "", "tracingID", "msgType", null, "DEF", null, false, "");
    return flow;
  }
  
  private static DocumentFlowNotification createDocFlowNotification(EDocumentFlowType docFlowType, Date eventOccurTime,
                                                                    String messageID, boolean isSuccess, String errorReason,
                                                                    String tracingID, String msgType, String filePath,
                                                                    String beID, List attachmentUIDs, boolean isRequiredPack,
                                                                    String docFlowAddInfo)
     throws Exception
   {
     byte[] docInByte = null;
     
     return new DocumentFlowNotification(docFlowType, eventOccurTime, messageID, isSuccess,
                                         errorReason, tracingID, msgType, docInByte, beID, attachmentUIDs, filePath,
                                         isRequiredPack, docFlowAddInfo);
   }
}
