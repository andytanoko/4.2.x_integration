/**
i * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailDemo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.gtas.audit.common.model.ITrailInfo;
import com.gridnode.gtas.audit.common.model.ProcessInfo;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerHome;
import com.gridnode.gtas.audit.tracking.facade.ejb.IAuditTrailManagerObj;
import com.gridnode.util.jndi.JndiFinder;

/**
 * This class simulating the trans events, trans document, process for the 3A4 from GT to TXMR.
 *  
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailDemo
{
  //SETUP
  private static final String REMOTE_EVENT_QUEUE = "queue/gtvan/isat/RemoteEventQueue";
  private static final String LOCAL_EVENT_QUEUE = "queue/gtvan/isat/LocalEventQueue";
  
  private static Queue remoteEventQueue = null;
  private static Queue localEventQueue = null; 
  private static QueueConnectionFactory connFac = null;
  //END SETUP
  
  
  //GLOBAL
  private static Long _processUID;
  private static Date _processStartDate;
  
  
  private static int _messageID;
  private static String _uuid;
  private static IAuditTrailManagerObj _trailMgr;
  private static boolean _sendViaJms = true;
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    Context ctx = getContext();
    connFac = lookupQueueConnectionFactory(ctx);
    remoteEventQueue = lookupQueue(REMOTE_EVENT_QUEUE, ctx);
    localEventQueue = lookupQueue(LOCAL_EVENT_QUEUE, ctx);
    
    
    int count = 20;
    /*while(count > 0)
    {
      
    com.gridnode.gridtalk.httpbc.common.model.EventInfo info = new com.gridnode.gridtalk.httpbc.common.model.EventInfo();
    info.setEventName("test");
    info.setEventOccurredTime(new Date());
    info.setMessageID("IB-12345");
    info.setStatus("OK");
    info.setErrorReason(null);
    info.setTracingID("324324-asdfadf-34324");
    info.setEventType("Sig");
    info.setBeID("DEF");
    
    FileContent content = new FileContent("3a4c.xml", "haha".getBytes());
    EventTrail trail = new EventTrail();
    trail.setBizDocuments(new EventDoc[]{createEventDoc(content)}); 
    
    //if(count % 2 > 0)
    //{
      //trail.setEventInfo(info);
    //}
    String afterConvert = XMLBeanUtil.beanToXml(trail,"AuditTrailData");
    
    sendMsgToQueue(connFac, localEventQueue, afterConvert);
    
    System.out.println(afterConvert); 
    
    
    
    AuditTrailData trailData = new AuditTrailData();
    trailData = (AuditTrailData)XMLBeanUtil.xmlToBean(afterConvert, AuditTrailData.class);
    System.out.println(trailData.getTrailInfo()+" bizDocument is "+(trailData.getBizDocuments().length));
    --count;
    } */
    /**
     * 
     */
    //sendMsgToQueue(connFac, localEventQueue, afterConvert);
    
    int i = 300;
    
    System.out.println("Start send at "+new Date());
    while(i > 0)
    {
      simulate3A4R();
      simulate3A4CReceived();
      --i;
    }
    System.out.println("End sent at "+new Date());
    
    
    /*
    System.out.println("Please select the scenario:");
    System.out.println("1) 3A4R from GT to Partner");
    System.out.println("2) 3A4C from Partner to GT");
    System.out.println("3) "); */
    
    /* simulating the sending of the 3A4r to TXMR
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String s = "";
    while( ( s = reader.readLine()) != "")
    {
      int i = Integer.parseInt(s);
      if(i == 1)
      {
        simulate3A4R();
      }
      else if(i == 2)
      {
        simulate3A4CReceived();
      }
      else if(i == 0)
      {
        System.exit(0);
      }
    }*/
    
    int processInstanceUID = 712072807; //Math.abs(new Random().nextInt());
    
    //26Apr 07 Sending duplicate Process Trans
    i = 0;
    while(i > 0)
    {
      int randon = Math.abs((new Random()).nextInt());
      
      emitDocCreated("OB-"+randon, IAuditTrailConstant.DIRECTION_OB,"PO-1001", "testDoc/3A4RimportDoc.xml", new Long(randon), false, false, false, "3A4R", IAuditTrailConstant.SELF_INITIATOR, UUID.randomUUID().toString());
      emitDocCreated("OB-"+(randon+1), IAuditTrailConstant.DIRECTION_OB,"PO-1001", "testDoc/3A4RimportDoc.xml", new Long(randon), false, false, false, "3A4R", IAuditTrailConstant.SELF_INITIATOR, UUID.randomUUID().toString());
      //emitProcessInfo("gwf://"+processInstanceUID+"/Request Purchase Order/SELF", new Date(), null,IAuditTrailConstant.PROCESS_STATE_RUNNING,"", "", new Long(randon));
      //emitProcessInfo("gwf://"+processInstanceUID+"/Request Purchase Order/SELF", new Date(), null,IAuditTrailConstant.PROCESS_STATE_RUNNING,"", "", new Long(randon));
      //emitDocCreated("OB-"+(randon+2), IAuditTrailConstant.DIRECTION_OB,"PO-1001", "testDoc/3A4RimportDoc.xml", new Long(randon), false, false, false, "3A4R", IAuditTrailConstant.SELF_INITIATOR, UUID.randomUUID().toString());
      --i;
    }
    
    //emitProcessInfo("gwf://"+processInstanceUID+"/Request Purchase Order/SELF", new Date(), new Date(),IAuditTrailConstant.PROCESS_STATE_ABNORMALLY_ABORTED,"", "", new Long(processInstanceUID));
    
    
    //3 May i)  Receive IB doc and Doc Received Event together
    //      ii) Send IB doc and Doc Delivery Event
    
    i = 0;
    while(i > 0)
    {
      String uuid = UUID.randomUUID().toString();
      int msgID = Math.abs((new Random()).nextInt());
      int processID =  Math.abs((new Random()).nextInt());
      
      //IB doc
      /*
      sendDocReceivedEvent(uuid);
      sendUnpackEvent(uuid);
      sendProcessInjectEvent(uuid);
      sendIBDocTrans(msgID, uuid, new Long(processID)); */
      
      //OB doc
      sendOBDocTrans(msgID, uuid, new Long(processID));
      sendDocDeliveryEvent(msgID, uuid);
      
      --i;
    }
  }
  
  /*
  private static EventDoc createEventDoc(FileContent fc)
  {
    EventDoc eventDoc = new EventDoc();
    eventDoc.setDoc(fc.getContent());
    eventDoc.setFilename(fc.getFilename());
    eventDoc.setRequiredPack(true);
    return eventDoc;
  }*/
  
  public static void sendDocReceivedEvent(String uuid) throws Exception
  {
    EventInfo info = createEventInfo(IAuditTrailConstant.DOCUMENT_RECEIVED, "", IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    BusinessDocument doc = createBusinessDoc("testDoc/3a4cReceiveAudit.xml", true);
    if(_sendViaJms)
    {
      sendMsgToQueue(connFac, remoteEventQueue,createAuditTrailData(info, doc));
    }
    else
    {
      _trailMgr.handleAuditTrailData(createAuditTrailData(info, doc));
    }
  }
  
  public static void sendUnpackEvent(String uuid) throws Exception
  {
    EventInfo info = createEventInfo(IAuditTrailConstant.UNPACK_PAYLOAD, "", IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    BusinessDocument doc = null;
    if(_sendViaJms)
    {
      sendMsgToQueue(connFac, remoteEventQueue,createAuditTrailData(info, doc));
    }
    else
    {
      _trailMgr.handleAuditTrailData(createAuditTrailData(info, doc));
    }
  }
  
  public static void sendProcessInjectEvent(String uuid) throws Exception
  {
    EventInfo info = createEventInfo(IAuditTrailConstant.PROCESS_INJECTION, "", IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    BusinessDocument doc = null;
    if(_sendViaJms)
    {
      sendMsgToQueue(connFac, remoteEventQueue,createAuditTrailData(info, doc));
    }
    else
    {
      _trailMgr.handleAuditTrailData(createAuditTrailData(info, doc));
    }
  }
  
  public static void sendIBDocTrans(int ibMsgID, String uuid, Long processUID) throws Exception
  {
    emitDocCreated("IB-"+ibMsgID, IAuditTrailConstant.DIRECTION_IB, "PO-2104Confirmation", "testDoc/3A4CUDoc.xml",processUID, false, false,false,"3A4C", IAuditTrailConstant.SELF_INITIATOR,uuid);
  }
  
  public static void sendDocDeliveryEvent(int msgID, String uuid) throws Exception
  {
    boolean failedStatus = Math.abs( (new Random()).nextInt()) % 2 == 0;
    String eventStatus = failedStatus ? IAuditTrailConstant.STATUS_FAIL : IAuditTrailConstant.STATUS_SUCCESS;
    
    EventInfo info = createEventInfo(IAuditTrailConstant.DOCUMENT_DELIVERED, "OB-"+msgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    /*
    if(failedStatus)
    {
      info.setBeID("");
      info.setMessageID("");
    }*/
    
    BusinessDocument doc = createBusinessDoc("testDoc/AckSendAudit.xml", true);
    
    if(_sendViaJms)
    {
      sendMsgToQueue(connFac, remoteEventQueue,createAuditTrailData(info, doc));
    }
    else
    {
      _trailMgr.handleAuditTrailData( createAuditTrailData(info, doc));
    }
  }
  
  public static void sendOBDocTrans(int msgID, String uuid, Long processUID) throws Exception
  {
    emitDocCreated("OB-"+msgID, IAuditTrailConstant.DIRECTION_OB,"PO-1001", "testDoc/3A4RimportDoc.xml", processUID, false, false, false, "3A4R", IAuditTrailConstant.SELF_INITIATOR, uuid);
  }
  
  private static IAuditTrailManagerObj getAuditTrailMgr() throws Exception
  {
    InitialContext context = new InitialContext(getJndiProperties());
    Object obj = context.lookup(IAuditTrailManagerHome.class.getName());
    IAuditTrailManagerHome home = (IAuditTrailManagerHome)PortableRemoteObject.narrow(obj, IAuditTrailManagerHome.class);
    return home.create();
  }
  
  public static void simulate3A4R() throws Exception
  {
    //--------------------------- Doc Import --------------------------------
    //The import event
    
    String uuid = UUID.randomUUID().toString();
    int ipGdocID = getRandomInt();
    EventInfo info = createEventInfo(IAuditTrailConstant.DOCUMENT_IMPORT, "IP-"+ipGdocID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, null, uuid);
    BusinessDocument doc = createBusinessDoc("testDoc/3A4RimportDoc.xml", true);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    
    //OB Process start event
    info = createEventInfo(IAuditTrailConstant.OUTBOUND_PROCESSING_START, "IP-"+ipGdocID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null; 
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, null));
    
    
    int obGdocID = getRandomInt();
    String obMsgID = "OB-"+obGdocID;
    //OB Process End
    info = createEventInfo(IAuditTrailConstant.OUTBOUND_PROCESSING_END, obMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    
    //@@@@@@@@@@> create RT PROCESS
    int processUID = getRandomInt();
    _processUID = new Long(processUID);
    _processStartDate = new Date();
    emitProcessInfo("gwf://"+processUID+"/Request Purchase Order/SELF", _processStartDate, null,IAuditTrailConstant.PROCESS_STATE_RUNNING,"", "", _processUID);
    
    //@@@@@@@@@> create OB DOC
    emitDocCreated(obMsgID, IAuditTrailConstant.DIRECTION_OB,"PO-1001", "testDoc/3A4RimportDoc.xml", _processUID, false, false, false, "3A4R", IAuditTrailConstant.SELF_INITIATOR, uuid);
    
    //Channel Connect
    info = createEventInfo(IAuditTrailConstant.CHANNEL_CONNECTIVITY, obMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Pack Payload
    info = createEventInfo(IAuditTrailConstant.PACK_PAYLOAD, obMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Doc Delivery
    info = createEventInfo(IAuditTrailConstant.DOCUMENT_DELIVERED, obMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = createBusinessDoc("testDoc/3A4RDeliveredAudit.xml", false);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Doc ACK
    info = createEventInfo(IAuditTrailConstant.DOCUMENT_ACKNOWLEDGED, obMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = createBusinessDoc("testDoc/AckReceivedAudit.xml", false);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    
    //--------------------- ACK receive -----------------------
    uuid = UUID.randomUUID().toString();
    //Doc Received
    info = createEventInfo(IAuditTrailConstant.DOCUMENT_RECEIVED, "", IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = createBusinessDoc("testDoc/AckReceivedAudit.xml", true);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Doc Unpack
    info = createEventInfo(IAuditTrailConstant.UNPACK_PAYLOAD, "", IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    // @@@@@@@@ OB ACK sent
    int ibGdocID = getRandomInt();
    String ibMsgID = "IB-"+ibGdocID;
    emitDocCreated(ibMsgID, IAuditTrailConstant.DIRECTION_IB, null, "testDoc/AckSendUDoc.xml",_processUID, false, false,false,"RN_ACK", IAuditTrailConstant.SELF_INITIATOR,uuid);
    
    //Export process start
    
    info = createEventInfo(IAuditTrailConstant.EXPORT_PROCESSING_START, ibMsgID, IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Doc Export
    int epGdocID = getRandomInt();
    info = createEventInfo(IAuditTrailConstant.DOCUMENT_EXPORT, "EP-"+epGdocID, IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = createBusinessDoc("testDoc/ackExportedUDoc.xml", true);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Export End
    info = createEventInfo(IAuditTrailConstant.EXPORT_PROCESSING_END, "EP-"+epGdocID, IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "", uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc)); 
  }
  
  public static void simulate3A4CReceived() throws Exception
  {
    String uuid = UUID.randomUUID().toString();
    //------------------------ 3a4c received Event ---------------------------
    EventInfo info = createEventInfo(IAuditTrailConstant.DOCUMENT_RECEIVED, "", IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    BusinessDocument doc = createBusinessDoc("testDoc/3a4cReceiveAudit.xml", true);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Unpack Payload
    info = createEventInfo(IAuditTrailConstant.UNPACK_PAYLOAD, "", IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    
    info = createEventInfo(IAuditTrailConstant.PROCESS_INJECTION, "", IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    // @@@@@@@@@ IB doc created
    int ibGdocID = getRandomInt();
    String ibMsgID = "IB-"+ibGdocID;
    emitDocCreated(ibMsgID, IAuditTrailConstant.DIRECTION_IB, "PO-2104Confirmation", "testDoc/3A4CUDoc.xml",_processUID, false, false,false,"3A4C", IAuditTrailConstant.SELF_INITIATOR,uuid);
    
    //ExportProcessing start for 3A4C
    info = createEventInfo(IAuditTrailConstant.EXPORT_PROCESSING_START, ibMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Export to def port
    int exGdocID = getRandomInt();
    info = createEventInfo(IAuditTrailConstant.DOCUMENT_EXPORT, "EP-"+exGdocID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = createBusinessDoc("testDoc/3A4CUDoc.xml", true);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Export end
    info = createEventInfo(IAuditTrailConstant.EXPORT_PROCESSING_END, "EP-"+exGdocID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                                     IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    /**
     * String msgID, String direction, String docNo, String docFilePath, Long processInstanceUID,
                                     Boolean isRetry, Boolean isDuplicate, boolean isSignal, String docType, String processInitiator,
                                     String tracingID
     */
    
    //----------------------- send out ACK --------------
    uuid = UUID.randomUUID().toString();
    // @@@@@@@@ OB ACK sent
    String obMsgID = "OB-"+getRandomInt();
    emitDocCreated(obMsgID, IAuditTrailConstant.DIRECTION_OB, null, "testDoc/AckSendUDoc.xml",_processUID, false, false,false,"RN_ACK", IAuditTrailConstant.SELF_INITIATOR,uuid);
    
    //Doc send to channel
    int obGdocID = getRandomInt();
    info = createEventInfo(IAuditTrailConstant.CHANNEL_CONNECTIVITY, obMsgID, IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Doc Pack
    info = createEventInfo(IAuditTrailConstant.PACK_PAYLOAD, "", IAuditTrailConstant.EVENT_TYPE_SIGNAL,
                           IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = null;
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //Doc Delivery
    info = createEventInfo(IAuditTrailConstant.DOCUMENT_DELIVERED, obMsgID, IAuditTrailConstant.EVENT_TYPE_TRANS,
                           IAuditTrailConstant.STATUS_SUCCESS, "",uuid);
    doc = createBusinessDoc("testDoc/AckSendAudit.xml", true);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    
    //@@@@@@@ END PROCESS
    emitProcessInfo("gwf://"+_processUID+"/Request Purchase Order/SELF", _processStartDate, new Date(),IAuditTrailConstant.PROCESS_STATE_COMPLETED,"", "", _processUID);
    
  }
  
  //change to use audit trail mgr directly
  private static void emitDocCreated(String msgID, String direction, String docNo, String docFilePath, Long processInstanceUID,
                                     Boolean isRetry, Boolean isDuplicate, boolean isSignal, String docType, String processInitiator,
                                     String tracingID) throws Exception
  {
    /**
     * String docNo, String documentType, String documentDirection, String messageID, 
                 Date dateTimeSent, Date dateTimeReceived, String tracingID, Long documentSize,
                 Boolean isDuplicate, Boolean isRetry,String userTrackingID, String beID, ProcessSummary processSummary
     *
     */
    ProcessSummary summary = getProcessSummary(processInstanceUID, processInitiator);
    DocInfo info = new DocInfo(docNo, docType, direction, msgID, null, null, tracingID, new Long(5), isDuplicate, isRetry,"", "DEF", summary);
    BusinessDocument doc = createBusinessDoc(docFilePath, true);
    
    if(_sendViaJms)
    {
      sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, doc));
    }
    else
    {
      _trailMgr.handleAuditTrailData(createAuditTrailData(info, doc));
    }
  }
  
  private static void emitProcessInfo(String processID, Date processStartTime, Date processEndTime, String processStatus,
                                      String errorType, String errorReason, Long processInstanceUID) throws Exception
  {
    ProcessInfo info = new ProcessInfo(processID, processStartTime, processEndTime, processStatus, errorType, errorReason, processInstanceUID);
    sendMsgToQueue(connFac, remoteEventQueue, createAuditTrailData(info, null));
  }
  
  private static ProcessSummary getProcessSummary(Long processInstanceUID, String processInitiatorID)
  {
    String pipName = "3A4_GT4";
    String pipVersion = "V02.02";
    String tpPartnerDuns = "595165887";
    String tpPartnerName = "Max12";
    String customerDuns = "333444555";
    String customerName = "Inovis11";
    return new ProcessSummary(pipName, pipVersion, tpPartnerDuns, tpPartnerName, customerDuns, customerName, processInstanceUID, processInitiatorID);
  }
  
  private static int getRandomInt()
  {
    Random ran = new Random();
    return Math.abs(ran.nextInt());
  }
  
  public static Properties getJndiProperties()
  {
    Properties p = new Properties();
    p.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" ) ;
    p.setProperty("java.naming.provider.url","localhost:31099") ;
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
  
  public static void sendMsgToQueue(QueueConnectionFactory connectionFactory, Queue queue, Serializable msg) throws Exception
  {
    QueueConnection queueConnection = connectionFactory.createQueueConnection();
    QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    QueueSender queueSender = queueSession.createSender(queue);
    MessageProducer producer = queueSession.createProducer(queue);
    
    /*
    BytesMessage byteMsg = queueSession.createBytesMessage();
    byteMsg.writeBytes(((String)msg).getBytes());
    */
    StreamMessage streamMsg = queueSession.createStreamMessage();
    streamMsg.writeObject(msg);
    
    producer.send(streamMsg);
    queueConnection.close();
  }
  
  public static Context getContext() throws Exception
  {
    return new InitialContext(getJndiProperties());
  }
  
  public static AuditTrailData getAuditTrailData() throws Exception
  {
    return new AuditTrailData(getEventInfo(), false, null);
    //return new AuditTrailData(getEventInfo(), null);
  }
  
  public static EventInfo getEventInfo()
  {
    Random rd = new Random();
    int i = Math.abs(rd.nextInt());
    EventInfo info = new EventInfo(IAuditTrailConstant.CHANNEL_CONNECTIVITY, new Date(), "OB-"+i, "OK", null, UUID.randomUUID().toString(), IAuditTrailConstant.EVENT_TYPE_SIGNAL,"DEF", "");
    return info;
  }
  
  public static EventInfo createEventInfo(String eventName, String msgID, String eventType, String status, String failReason, String uuid)
  {
    EventInfo info = new EventInfo(eventName, new Date(), msgID, 
                                   status, failReason, uuid, eventType,"DEF","");
    return info;
  }
  
  public static BusinessDocument createBusinessDoc(String docPath, boolean isRequiredPack) throws Exception
  {
    return new BusinessDocument(readDocInByte(docPath), new File(docPath).getName(), isRequiredPack);
  }
  
  public static AuditTrailData createAuditTrailData(ITrailInfo info, BusinessDocument businessDoc)
  {
    if(businessDoc == null)
    {
      return new AuditTrailData(info, false, null);
    }
    else
    {
      return new AuditTrailData(info, false, businessDoc);
    }
  }
  
  private static byte[] readDocInByte(String docFilePath) throws Exception
  {
    FileInputStream in = new FileInputStream(new File(docFilePath));
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    
    byte[] buffer = new byte[512];
    int readSoFar = 0;
    while( (readSoFar = in.read(buffer)) > 0)
    {
      byteOut.write(buffer, 0 , readSoFar);
    }
    
    in.close();
    byteOut.close();
    return byteOut.toByteArray();
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
}
