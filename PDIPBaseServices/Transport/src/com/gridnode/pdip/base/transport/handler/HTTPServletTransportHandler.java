/**
 * This software is the proprietary information of GridNode Pte Ltd.datcontext
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HTTPServletTransportHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 * Nov 11 2003    Guo Jianyu               added receiveAS2()
 * Jan 05 2003    Guo Jianyu               Bug fix: translateIncomingMimeMessage()
 *                                          used to unintentionally modify RN message
 *                                          in conversion between byte[] and String.
 *                                          It's now fixed by reading the contents through
 *                                          JavaMail.
 * Nov 14 2005    Neo Sok Lay              Perform Jndi lookups using ServiceLocator
 * Jan 24 2006    Tam Wei Xiang            Modified method: sendRN(GNTransportInfo),
 *                                                          receiveRN(Hashtable, byte[])
 *                                                          Remove the eventID from the header
 *                                                          if the msg is rn msg. 
 *                                                          Resolving the defect GNDB00026534.
 * Jan 29 2007    Neo Sok Lay              Use generic Destination instead of Topic/Queue
 * May 04 2007    Neo Sok Lay              GNDB00028300: Export cert to specific truststore instead of default.  
 * May 10 2010    Tam Wei Xiang            #1463 - Return HTTP response code 200 if it is an RNIF 1.1 request
 * May 18 2010    Tam Wei Xiang            #1470 - Enable http basic authentication                                                       
 */


package com.gridnode.pdip.base.transport.handler;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.base.transport.comminfo.HttpCommInfo;
import com.gridnode.pdip.base.transport.comminfo.IHttpCommInfo;
import com.gridnode.pdip.base.transport.exceptions.GNTransportException;
import com.gridnode.pdip.base.transport.helpers.*;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;

class HTTPTransportInfo
{
  public static   int STATUS_UNKNOWN = -1;
  public static   int STATUS_FAIL     = 0;
  public static   int STATUS_SUCCESS = 1;
  //-1 unknown, 0 fail, 1 success

  public Hashtable header = null;
  public byte[]    contend = null;
  public int       responseCode = 200;
  public int       receiveStatus  = STATUS_UNKNOWN;
}

public class HTTPServletTransportHandler
{
  public static   int EXCEPTION = 3000;

  private static final String CONTENT_TYPE = "text/html";
  private static final String MDN = "MDN";

  //static Properties jmsprops = null;
  //static InitialContext jmsctx = null;
  static ConnectionFactory jmscf = null;
  //static Connection connection = null;
  static Destination destination = null;
  //static Session session = null;
  //static MessageProducer producer = null;
  static Hashtable<String, String> sendProps = new Hashtable<String, String>();
  static Object lock = new Object();
  
  static 
  {

  }
  
  public HTTPServletTransportHandler()
  {
    //TWX
    HttpMessageContext context = HttpMessageContext.getInstance();
    
    if(sendProps.size() == 0)
    {
      synchronized(lock)
      {
        if(sendProps.size() == 0)
        {
          sendProps.put(JMSSender.CONN_FACTORY, context.getCONNECTION_FACTORY());
          sendProps.put(JMSSender.JMS_DEST_NAME, context.getDESTINATION_NAME());
          sendProps.put(JMSSender.JNDI_SERVICE, ServiceLocator.CLIENT_CONTEXT);
        }
      }
    }
  }

  static public byte[] getRequestContent(HttpServletRequest request) throws IOException
  {
    return HttpMessageContext.getMessage(request.getInputStream());
  }

  public void addResponseHeaders(HttpServletResponse response, Hashtable headers)
  {
    if(response == null || headers == null)
    return;
    HttpMessageContext context = HttpMessageContext.getInstance();
    Enumeration keys = headers.keys();
    while( keys.hasMoreElements() )
    {
      String keyValue = (String)keys.nextElement();
      String headerValue = (String)headers.get(keyValue);
      if(context.getLogheader())
        context.Debug("addResponseHeaders: header [" + keyValue +"] [" + headerValue +"]");
      try
      {
        response.addHeader(keyValue, headerValue);
      }
      catch (Exception ex)
      {
        context.Warn("addResponseHeaders: header [" + keyValue +"] [" + headerValue +"]", ex);
      }
    }
  }

  static public void returnNotSupprotResponse(HttpServletResponse response, String ID) throws IOException
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    context.Debug("returnNotSupprotResponse");
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head><title>" + ID + "</title></head>");
    out.println("<body>");
    out.println("<p>The servlet has received a GET. Please use Post.</p>");
    out.println("</body></html>");
  }

  static public void returnNotFoundResponse(HttpServletResponse response, String ID) throws IOException
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    context.Debug("returnNotFoundResponse");
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head><title>" + ID + "</title></head>");
    out.println("<body>");
    out.println("<p>The servlet has received a POST. But cannot find.</p>");
    out.println("</body></html>");
  }

  static public Hashtable getRequestHeader(HttpServletRequest request)
  {
      HttpMessageContext context = HttpMessageContext.getInstance();
      Enumeration headerNames = request.getHeaderNames();
      Hashtable   headers = new Hashtable();
      while(headerNames.hasMoreElements())
      {
        String headerName = (String)headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        if(context.getLogheader())
          context.Debug("getRequestHeader: header[" + headerName +"] [" + headerValue +"]");
        headers.put(headerName, headerValue);
      }
      return headers;
  }

  protected void initJMS() throws NamingException, JMSException, ServiceLookupException
  {
      HttpMessageContext context = HttpMessageContext.getInstance();
      context.Debug("initJMS start");
      /*
      jmsprops = new Properties();

      jmsprops.put(Context.INITIAL_CONTEXT_FACTORY, context.getINITIAL_CONTEXT_FACTORY());
      jmsprops.put(Context.PROVIDER_URL, context.getPROVIDER_URL());
      jmsprops.put(Context.URL_PKG_PREFIXES, context.getURL_PKG_PREFIXES());

      jmsctx  = new InitialContext(jmsprops);
      jmscf = (TopicConnectionFactory) jmsctx.lookup(context.getTOPIC_CONNECTION_FACTORY());
      connection = jmscf.createTopicConnection();
      Object obj = jmsctx.lookup(context.getTOPIC_NAME());
      topic = (Topic) PortableRemoteObject.narrow(obj, Topic.class);
      */
      //NSL20051114 Use ServiceLocator for lookup
      jmscf = (ConnectionFactory)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(context.getCONNECTION_FACTORY(), 
                                                                                                      ConnectionFactory.class);
      destination = (Destination)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(context.getDESTINATION_NAME(), Destination.class);
      /*
      connection = jmscf.createConnection();
      
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      producer = session.createProducer(null);
      producer.setTimeToLive(context.getTIME_TO_LIVE());
      */
  }

  public Object[] sendJMSMessage(Serializable msg, Hashtable header)
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    Connection connection = null;
    
    if(! JMSRetrySender.isSendViaDefMode())
    {
      Hashtable<String, String> msgProps = new Hashtable<String, String>();
      
      if(header != null)
      {
        Enumeration en = header.keys();
        while(en.hasMoreElements())
        {
          String key = (String)en.nextElement();
          String value = (String)header.get(key);
          msgProps.put(key, value);
        }
      }
      
      try
      {        
        JMSRetrySender.sendMessage(sendProps.get(JMSSender.JMS_DEST_NAME), msg, msgProps, sendProps, false);
        
        return new Object[]{true};
      }
      catch(Throwable th)
      {
        return new Object[]{false, th,false};
      }
    }
    
    try
    {
      connection = jmscf.createConnection();
      
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer producer = session.createProducer(destination);
      producer.setTimeToLive(context.getTIME_TO_LIVE());
      context.Info("sendJMSMessage start");
      ObjectMessage message = session.createObjectMessage(msg);
      if(header != null)
      {
        Enumeration en = header.keys();
        while(en.hasMoreElements())
        {
          String key = (String)en.nextElement();
          String value = (String)header.get(key);
          message.setStringProperty(key, value);
        }
      }
      //producer.send(destination, message);
      producer.send(message);
      context.Info("sendJMSMessage success"); 
      return new Object[]{true};
   //   topicSession.close();
    }
    catch (Throwable ex)
    {
      //context.Error("sendJMSMessage failed", ex);
      return new Object[]{false, ex};
    }
    finally
    {
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (Exception e) {}
      }
    }
  }

  public void sendGTSMessage(Serializable msg, Hashtable header) throws IOException
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    context.Info("sendGTSMessage start");
    
    Object[] sendStatus = sendJMSMessage(msg, header); //0: indicate send success or fail 1: the send failed exception if exist 2: whether to continue to the next retry
    if((Boolean)sendStatus[0])
      return;
    
    if(sendStatus.length == 3 && ! (Boolean)sendStatus[2])
    {
      Throwable th =(Throwable)sendStatus[1]; 
      context.Info("Delegate to channel level failed: "+th.getMessage());
      throw new IOException("sendGTSMessage:" + th.getMessage());
    }
    
    try
    {
      initJMS();
      sendStatus = sendJMSMessage(msg, header); //The second retry may still fail, so the client should resend the msg again.
      if(! (Boolean)sendStatus[0])
      {
        Throwable th =(Throwable)sendStatus[1]; 
        context.Info("Delegate to channel level failed: "+th.getMessage());
        throw th; 
      }
      
      context.Info("sendGTSMessage Success");
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      context.Warn("sendGTSMessage error", ex);
      throw new IOException("sendGTSMessage:" + ex.getMessage());
    }
  }
/*
  void TestJMS() throws IOException
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    System.out.println("B4 connection");
    context.setINITIAL_CONTEXT_FACTORY("org.jnp.interfaces.NamingContextFactory");
    context.setPROVIDER_URL("localhost:1099");
    context.setURL_PKG_PREFIXES("org.jboss.naming:org.jboss.naming");
    context.setTOPIC_CONNECTION_FACTORY("ConnectionFactory");
    context.setTOPIC_NAME("topic/fromRouter");
    context.setTIME_TO_LIVE("10000");
    System.out.println("After connection");
    String msg = "test";
    sendGTSMessage(msg, null);
  }

  public static void main(String args[])
      throws Exception
  {
    HTTPServletTransportHandler handler = new HTTPServletTransportHandler();
    handler.TestJMS();
  }*/


  protected HTTPTransportInfo receiveRN(Hashtable header, byte[] stuff) throws IOException
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    GNTransportHeader rnheader = new GNTransportHeader(header);

    HTTPTransportInfo transportInfo = new HTTPTransportInfo();
    //String   rnversion = rnheader.getRNVersion();
    if(!rnheader.isNativeRNMessage())
      {
          transportInfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
          context.Info("receiveRN: not RN message");
          return transportInfo;
      }
    //TWX: 24012006 remove the event ID from header since we know the rn msg is routed via http
    //not p2p.
    rnheader.removeProperty(ICommonHeaders.MSG_EVENT_ID);

    
    String  msgID = HttpMessageContext.CreateNewMsgId();
    rnheader.setRNMessage();
    rnheader.setRNSyncMessageID(msgID);
    if(context.getLogheader())
    {
      context.Debug("receiveRN: add messageID:" + msgID);
      context.Debug("receiveRN: set RN Package Type");
    }
    boolean isSync = rnheader.isRNSyncMessage();
    if(isSync)
      context.Info("receiveRN: is RN Sync message");
    else
      context.Info("receiveRN: is RN ASyn message");

    Hashtable jmsheader = new Hashtable();
    jmsheader.put("GNSelector", context.getDESTINATION_NAME()+"x");
    if(!rnheader.getRNVersion().equals(""))
      stuff = translateIncomingMimeMessage(rnheader, stuff);
    GNTransportPayload payload = new GNTransportPayload(rnheader.getProperties(), new String[0], stuff);
    sendGTSMessage(payload,jmsheader);
    if(isSync)
    {
          Object event = new Object();
          int id = context.AddSignal(msgID , event, null);
          context.waitSignal(id);
          Object   ob =  context.getSignalObject(context.FindSignalIndex(msgID));
          context.RemoveSignal(msgID);

          if(ob != null)
          {
            transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
            transportInfo.responseCode = HttpServletResponse.SC_OK;
            Object[] obj = (Object[])ob;
            transportInfo.header = (Hashtable)obj[0];
            transportInfo.contend = (byte[])obj[1];

            GNTransportHeader replyhttpheader = new GNTransportHeader(transportInfo.header);
            if(!replyhttpheader.getRNVersion().equals(""))
              transportInfo.contend = translateOutgoingMimeMessage(replyhttpheader,transportInfo.contend);
            replyhttpheader.setRNMessage();
            transportInfo.header = replyhttpheader.getProperties();
          }
          else
          {
            transportInfo.receiveStatus = HTTPTransportInfo.STATUS_FAIL;
            transportInfo.responseCode = HttpServletResponse.SC_NOT_ACCEPTABLE;
          }
    }
    else
    {
      //TWX 20100510 #1463
      if(rnheader.isRNIF11())
      {
        //RNIF1.1 spec: If the request
        //is accepted for processing by upper layers in the protocol, a 200 (OK) response will be
        //returned immediately as an acknowledgement of message receipt
        transportInfo.responseCode = HttpServletResponse.SC_OK;
      }
      else
      {
        transportInfo.responseCode = HttpServletResponse.SC_ACCEPTED;
      }
      
      
      transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
    }
    context.Debug("receiveRN: responseCode[" + transportInfo.responseCode + "]");
    return transportInfo;
  }

  private Hashtable standardizeHeader(Hashtable origHeader)
  {
    // convert all keys in header to upper case
    Hashtable header = new Hashtable();
    Enumeration allKeys = origHeader.keys();
    while (allKeys.hasMoreElements())
    {
      String key = (String)allKeys.nextElement();
      String value = (String)origHeader.get(key);
      header.put(key.toUpperCase(), value);
    }
    return header;
  }

  protected HTTPTransportInfo receiveAS2(Hashtable origHeader, byte[] stuff) throws IOException
  {
    Hashtable header = standardizeHeader(origHeader);

    HttpMessageContext context = HttpMessageContext.getInstance();

    HTTPTransportInfo transportInfo = new HTTPTransportInfo();

    if(!header.containsKey(IAS2Headers.AS2_FROM))
    {
      transportInfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
      context.Info("receiveAS2: not AS2 message");
      return transportInfo;
    }

    boolean isSync = (header.containsKey(IAS2Headers.DISPOSITION_NOTIFICATION_TO)) &&
      (!header.containsKey(IAS2Headers.RECEIPT_DELIVERY_OPTION));
    if(isSync)
      context.Info("receiveAS2: is AS2 Sync message");
    else
      context.Info("receiveAS2: is AS2 ASyn message");

    Hashtable jmsheader = new Hashtable();
    jmsheader.put("GNSelector", context.getDESTINATION_NAME()+"x");

    GNTransportPayload payload = new GNTransportPayload(header, new String[0], stuff);
    sendGTSMessage(payload,jmsheader);

    if(isSync)
    {
      Object event = new Object();
      String msgID = (String)header.get(IAS2Headers.MESSAGE_ID);
      int id = context.AddSignal(msgID , event, null);
      context.waitSignal(id);
      Object ob =  context.getSignalObject(context.FindSignalIndex(msgID));
      context.RemoveSignal(msgID);

      if(ob != null)
      {
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
        transportInfo.responseCode = HttpServletResponse.SC_OK;
        Object[] obj = (Object[])ob;
        transportInfo.header = (Hashtable)obj[0];
        transportInfo.contend = (byte[])obj[1];
//context.Debug("sync MDN: header is " + transportInfo.header);
//context.Debug("content is " + new String(transportInfo.contend));

        GNTransportHeader replyhttpheader = new GNTransportHeader(transportInfo.header);

        transportInfo.header = replyhttpheader.getProperties();
      }
      else
      {
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_FAIL;
        transportInfo.responseCode = HttpServletResponse.SC_NOT_ACCEPTABLE;
      }
    }
    else
     {
      transportInfo.responseCode = HttpServletResponse.SC_ACCEPTED;
      transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
     }
    context.Debug("receiveAS2: responseCode[" + transportInfo.responseCode + "]");
    return transportInfo;
  }

  protected HTTPTransportInfo receiveGTAS(Hashtable header, byte[] stuff) throws IOException
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    GNTransportHeader httpheader = new GNTransportHeader(header);
    context.Debug("receiveGTAS");
    HTTPTransportInfo transportInfo = new HTTPTransportInfo();
    if(httpheader.isGTASMessage())
    {
      Hashtable jmsheader = new Hashtable();
      jmsheader.put("GNSelector", context.getDESTINATION_NAME()+"x");
      sendGTSMessage(GNTransportInfo.load(stuff).getPayload(), jmsheader);
      transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
      context.Info("receiveGTAS: is GTAS message");
    }
    else
      {
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
        context.Info("receiveGTAS: not GTAS message");
      }
    return transportInfo;
  }

  public void sendResponse(HttpServletResponse response, HTTPTransportInfo httptransportinfo) throws IOException
  {
    boolean hasContentType = false;
    if (httptransportinfo.header != null)
    {
      Enumeration allKeys = httptransportinfo.header.keys();
      while (allKeys.hasMoreElements())
      {
        String aKey = (String)allKeys.nextElement();
        if (aKey.equalsIgnoreCase("content-type"))
        {
          hasContentType = true;
        }
        if (aKey.equalsIgnoreCase("TRANSFER-ENCODING"))
        {
          //remove "transfer-encoding" header because the chunked message content should
          //have been assembled by this handler
          httptransportinfo.header.remove(aKey);
        }
        else if(aKey.equalsIgnoreCase("date")) //to eliminate duplicate "date" header
        {
          if (response.containsHeader("Date"))
          {
            response.setHeader("Date", (String)httptransportinfo.header.get(aKey));
            httptransportinfo.header.remove(aKey);
          }
        }
      }
      addResponseHeaders(response,httptransportinfo.header);
    }

    if (hasContentType == false)
      response.setContentType(CONTENT_TYPE);
    response.setStatus(httptransportinfo.responseCode);

    if (!response.containsHeader("Content-Length") &&
      (httptransportinfo.contend != null))
      response.setContentLength(httptransportinfo.contend.length);

    ServletOutputStream out =  response.getOutputStream();
    if(httptransportinfo.contend != null)
      out.write(httptransportinfo.contend);
    out.close();
  }

  static public String getElement(String str, String tag)
  {
    String startstr = "<" + tag + ">";
    String endstr = "</" + tag + ">";
    int    startind = str.indexOf(startstr);
    int    endind = str.indexOf(endstr);
    if(startind == -1 || endind == -1 || startind >= endind)
      return "";
    startind += startstr.length();
    return str.substring(startind, endind);
  }

  static public void receive(HttpServletRequest request, HttpServletResponse response)
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    Hashtable header = null;
    byte[] contend = null;
    HTTPServletTransportHandler httpSevletTransporthandler = new HTTPServletTransportHandler();
    try
    {
      
      context.Debug("----receiver starts");
      header = getRequestHeader(request);
      contend   = getRequestContent(request);
      HTTPTransportInfo httptransportinfo = httpSevletTransporthandler.receive(header, contend);
      if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
        httpSevletTransporthandler.sendResponse(response, httptransportinfo);
      else
        HTTPServletTransportHandler.returnNotFoundResponse(response, "receiver");

      if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN && httptransportinfo.receiveStatus != HttpServletResponse.SC_NOT_FOUND)
          context.Debug("----receiver OK");
      else
          {
            context.sendEmailAlert(request, null, header, contend);
            context.Debug("----receiver fail");
          }
    }
    catch (Exception ex)
    {
        //TWX: In case the delegate of the msg to the Channel layer failed(eg JMS server error), we will return internal server error
        //     instead of return 202.
        HTTPTransportInfo transportInfo = new HTTPTransportInfo();
        transportInfo.responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        
        try
        {
          httpSevletTransporthandler.sendResponse(response, transportInfo);
        }
        catch(IOException e)
        {
          context.Warn("----receiver send response failed", e);
        }
        
        context.sendEmailAlert(request, ex, header, contend);
        context.Warn("----receiver fail", ex);
    }
  }

  static public void send(HttpServletRequest request, HttpServletResponse response)
  {
      HttpMessageContext context = HttpMessageContext.getInstance();
      Hashtable header = null;
      byte[] contend   = null;
      try
      {
        HTTPServletTransportHandler HTTPSevletTransporthandler = new HTTPServletTransportHandler();
        context.Debug("----sender starts");
        header = getRequestHeader(request);
        contend   = getRequestContent(request);
        HTTPTransportInfo httptransportinfo = HTTPSevletTransporthandler.send(header, contend);
        context.Debug("sender message sent , sent reply to GTAS");
        HTTPSevletTransporthandler.sendResponse(response, httptransportinfo);
        if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN && httptransportinfo.receiveStatus != HttpServletResponse.SC_NOT_FOUND)
            context.Debug("----sender OK");
        else
        {
            context.Debug("----sender fail");
            context.sendEmailAlert(request, null, header, contend);
        }
      }
      catch (Exception ex)
      {
            context.Warn("----sender fail", ex);
            context.sendEmailAlert(request, ex, header, contend);
            try
            {
              HTTPServletTransportHandler HTTPSevletTransporthandler = new HTTPServletTransportHandler();
              HTTPTransportInfo info = new HTTPTransportInfo();
              info.receiveStatus = HTTPTransportInfo.STATUS_FAIL;
              info.responseCode = EXCEPTION;
              info.contend = context.Exception2String(ex).getBytes();
              HTTPSevletTransporthandler.sendResponse(response, info);
            }
            catch(Throwable t)
            {
              context.Warn("Can't send back exception message", t);
            }
      }
  }

  public HTTPTransportInfo receive(Hashtable header, byte[] stuff) throws IOException
  {
   HttpMessageContext context = HttpMessageContext.getInstance();
   GNTransportHeader loghead = new GNTransportHeader(header);
   if(context.getLogheader())
   {
     context.Debug("receive: Header\r\n" + loghead);
   }

   context.Debug("header is: " + header);
   context.Debug("content is: " + stuff);
//TptLogger.debug("in receive", "stuff is " + new String(stuff));
   HTTPTransportInfo httptransportinfo = receiveGTAS(header, stuff);
   if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
    return httptransportinfo;

   httptransportinfo = receiveRN(header, stuff);
   if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
    return httptransportinfo;

   httptransportinfo = receiveAS2(header, stuff);
   if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
    return httptransportinfo;

   context.Info("receive:cannot accept this message");
   httptransportinfo.responseCode = HttpServletResponse.SC_NOT_FOUND;
   return httptransportinfo;
  }

  protected HTTPTransportInfo sendGTAS(GNTransportInfo stuff) throws Exception
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    context.Info("sendGTAS");
    HTTPTransportInfo transportInfo = new HTTPTransportInfo();
    GNTransportPayload payload = (GNTransportPayload)stuff.getPayload();
    GNTransportHeader httpheader = new GNTransportHeader(payload.getHeader());
    if(httpheader.isGTASMessage())
    {
      context.Info("sendGTAS: is GTAS message");
      HttpCommInfo httpcomminfo = (HttpCommInfo)stuff.getCommInfo();

      //TWX20100518  add http basic auth header
      addHttpBasicAuthHeader(httpheader, httpcomminfo);
      
      byte[] contend = stuff.save();
      GNHttpConnection httpconnection = new GNHttpConnection(httpcomminfo.getUrlWithoutUserInfo(),httpcomminfo.isAuthServer(),httpcomminfo.isAuthClient(),httpcomminfo.isVerifyHostname(),httpcomminfo.getKeystoreFile(),httpcomminfo.getKeystorePassword(),httpcomminfo.getTruststoreFile(),httpcomminfo.getTruststorePassword(),httpcomminfo.getTimeout());
      if(context.getLogheader())
      {
        context.Debug("sendGTAS:" + httpconnection.logMessageSetting());
      }
      if(IHttpCommInfo.HTTP_METHOD_POST.equals(httpcomminfo.getRequestMethod()))
        httpconnection.doPost(httpheader.getProperties(), contend);
      else if(IHttpCommInfo.HTTP_METHOD_GET.equals(httpcomminfo.getRequestMethod()))
        httpconnection.doGet(httpheader.getProperties());

      transportInfo.responseCode = httpconnection.getResponseCode();
      transportInfo.contend = httpconnection.getResponseMessage();
      transportInfo.header = httpconnection.getResponseHeaders();
      transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
    }
    else
      {
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
        context.Info("sendGTAS: not GTAS message");
      }
    return transportInfo;
  }

  public byte[] translateIncomingMimeMessage(GNTransportHeader head, byte[] contend)
  {
    try
    {
      String contentType = "Content-Type:" + head.getContentType() + "\r\n\r\n";
//      TptLogger.debug("translateIncomingMimeMessage", "content type is " + contentType);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bos.write(contentType.getBytes());
      bos.write(contend);

      javax.mail.Session session = javax.mail.Session.getDefaultInstance(System.getProperties(), null);
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      MimeMessage msg = new MimeMessage(session, bis);
      MimeMultipart multipart = (MimeMultipart)msg.getContent();
      MimeBodyPart rnMultipart = (MimeBodyPart)multipart.getBodyPart(0);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      rnMultipart.writeTo(out);
      return out.toByteArray();
    }
    catch(Exception e)
    {
      TptLogger.warn("translateIncomingMimeMessage", "Exception: ", e);
      return contend;
    }
/*
      HttpMessageContext context = HttpMessageContext.getInstance();
      String rnmsg = new String(contend);
      if(context.getLogheader())
      {
        context.Debug("translateIncomingMimeMessage: Header[B4 Translating]\r\n" + head);
        context.Debug("translateIncomingMimeMessage: Message[B4 Translating]\r\n" + rnmsg);
      }
      if(head.getRNVersion().length() > 0) //rnif2.0
      {
          int startindex = rnmsg.indexOf("\r\n") + 2;
          String boundary = rnmsg.substring(0,startindex -2);
          //rnmsg = rnmsg.substring(startindex);
          //int lastindex =  rnmsg.lastIndexOf("\r\n\r\n");
          int lastindex =  rnmsg.lastIndexOf(boundary);
          rnmsg = rnmsg.substring(startindex, lastindex) + "\r\n";
      }
      if(context.getLogheader())
      {
        context.Debug("translateIncomingMimeMessage: Header[After Translating]\r\n" + head);
        context.Debug("translateIncomingMimeMessage: Message[After Translating]\r\n" + rnmsg);
      }
      return rnmsg.getBytes();
*/
  }

  public byte[] translateOutgoingMimeMessage(GNTransportHeader head, byte[] contend)
  {
      HttpMessageContext context = HttpMessageContext.getInstance();
      String rnmsg = new String(contend);
      if(context.getLogheader())
      {
        context.Debug("translateOutgoingMimeMessage: Header[B4 Translating]\r\n" + head);
        context.Debug("translateOutgoingMimeMessage: Message[B4 Translating]\r\n" + rnmsg);
      }

      if(head.getRNVersion().length() > 0) //rnif2.0
      {
          String boundary = "----" + HttpMessageContext.CreateNewMsgId();
          String startboundary = "--" + boundary;
          String endboundary = startboundary + "--";
          contend = (startboundary + "\r\n" + new String(contend) + "\r\n" + endboundary + "\r\n").getBytes();
          String contentType = "multipart/related; type=\"" + head.getType() + "\"; boundary=\"" + boundary + "\"";

          head.setContentType(contentType);
          head.removeProperty(IRNHeaderConstants.TYPE_KEY);
          head.setProperty("mime-version", "1.0");
      }
      else
      {
          head.setContentType("application/x-rosettanet-agent; version=1.0");
      }
      if(context.getLogheader())
      {
        context.Debug("translateOutgoingMimeMessage: set ContentType:" + head.getContentType());
        context.Debug("translateOutgoingMimeMessage: remove IRNHeaderConstants.TYPE_KEY");
        context.Debug("translateOutgoingMimeMessage: set mime-version");
        context.Debug("translateOutgoingMimeMessage: Header[B4 Translating]\r\n" + head);
        context.Debug("translateOutgoingMimeMessage: Message[After Translating]\r\n"  + new String(contend));
      }
      return contend;
}

  protected HTTPTransportInfo sendRN(GNTransportInfo stuff) throws Exception
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    HTTPTransportInfo transportInfo = new HTTPTransportInfo();
    GNTransportPayload payload = (GNTransportPayload)stuff.getPayload();

    byte[] contend = payload.getFileContent();
    GNTransportHeader httpheader = new GNTransportHeader(payload.getHeader());
    context.Info("sendRN");
    if(httpheader.isNativeRNMessage())
    {
    	//TWX: 24012006 remove the event ID from header since we know the rn msg is routed via http
      //not p2p.
    	httpheader.removeProperty(ICommonHeaders.MSG_EVENT_ID);

      HttpCommInfo httpcomminfo = (HttpCommInfo)stuff.getCommInfo();
      context.Info("sendRN: is RN message");
      boolean isSync = httpheader.isRNSyncMessage();
      if(isSync)
        context.Info("sendRN: is RN Sync message");
      else
        context.Info("sendRN: is RN ASyn message");

      //TWX20100518 add http basic auth header
      addHttpBasicAuthHeader(httpheader, httpcomminfo);
      
      if(httpheader.isRNSyncMessage() && httpheader.isRNReplyMessage())
      {
        context.Info("sendRN: RN Sync Reply message");
        Object[] obj = new Object[2];
        obj[0] = httpheader.getProperties();
        obj[1] = contend;
        int id = context.FindSignalIndex(httpheader.getRNSyncMessageID());
        context.setSignalObject(id,obj);
        context.NotifySignal(id);
        transportInfo.responseCode = HttpServletResponse.SC_OK;
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
      }
      else
      {
        context.Info("sendRN: not RN Reply message");
        if(!httpheader.getRNVersion().equals(""))
          contend = translateOutgoingMimeMessage(httpheader, contend);
        GNHttpConnection httpconnection = new GNHttpConnection(httpcomminfo.getUrlWithoutUserInfo(),httpcomminfo.isAuthServer(),httpcomminfo.isAuthClient(),httpcomminfo.isVerifyHostname(),httpcomminfo.getKeystoreFile(),httpcomminfo.getKeystorePassword(),httpcomminfo.getTruststoreFile(),httpcomminfo.getTruststorePassword(),httpcomminfo.getTimeout());
        if(context.getLogheader())
        {
          context.Debug("sendRN:" + httpconnection.logMessageSetting());
        }
        if(IHttpCommInfo.HTTP_METHOD_POST.equals(httpcomminfo.getRequestMethod()))
          httpconnection.doPost(httpheader.getProperties(), contend);
        else if(IHttpCommInfo.HTTP_METHOD_GET.equals(httpcomminfo.getRequestMethod()))
          httpconnection.doGet(httpheader.getProperties());

        transportInfo.responseCode = httpconnection.getResponseCode();
        transportInfo.contend = httpconnection.getResponseMessage();
        transportInfo.header = httpconnection.getResponseHeaders();
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
        if(httpheader.isRNSyncMessage())
        {
            context.Info("sendRN: RN Sync Reply back");
            GNTransportHeader replyhttpheader = new GNTransportHeader(transportInfo.header);
            if(!replyhttpheader.getRNVersion().equals(""))
              transportInfo.contend = translateIncomingMimeMessage(replyhttpheader,transportInfo.contend);
            if(context.getLogheader())
              context.Debug("sendRN: RN Sync Reply Header:" + replyhttpheader);
            transportInfo.header = replyhttpheader.getProperties();
            Hashtable jmsheader = new Hashtable();
            jmsheader.put("GNSelector", context.getDESTINATION_NAME()+"x");

            GNTransportPayload rcpayload = new GNTransportPayload(transportInfo.header, new String[0], transportInfo.contend);
            sendGTSMessage(rcpayload, jmsheader);
        }
      }
    }
    else
      {
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
        context.Info("sendRN: not RN message");
      }
    return transportInfo;
  }

  /**
   * Add HTTP Basic auth header info if it is configured
   * @param httpheader
   * @param httpcomminfo
   */
  private void addHttpBasicAuthHeader(GNTransportHeader httpheader, HttpCommInfo httpcomminfo) throws GNTransportException
  {
    if(httpcomminfo.isConfiguredHttpBasicAuth())
    {
      HttpMessageContext context = HttpMessageContext.getInstance();
      context.Info("HTTP Basic Authentication is used");
      
      String authUsername = httpcomminfo.getHttpAuthUser();
      String authPassword = httpcomminfo.getHttpAuthPassword();
      
      String usrPw = authUsername+":"+authPassword;
      String base64UsrPw = base64Encode(usrPw);
      httpheader.setProperty(ITransportConstants.HTTP_AUTHORIZATION, ITransportConstants.HTTP_AUTHORIZATION_MODE_BASIC + " "+base64UsrPw);
    }
    else
    {
      return;
    }
  }

  private String base64Encode(String s) throws GNTransportException
  {
    if(s == null)
    {
      return s;
    }
    
    byte[] b = s.getBytes();
    ByteArrayOutputStream bout = new ByteArrayOutputStream(b.length);
    OutputStream out = bout;
    
    try
    {
      out = MimeUtility.encode(out, "base64");
      out.write(b);
      out.flush();
      
      return bout.toString();
      
    } catch (Exception ex)
    {
      throw new GNTransportException("Fail to perform base64 encoding", ex);
    } 
    finally
    {
      if(bout!= null)
      {
        try
        {
          bout.close();
        }
        catch (IOException e)
        {
          
        }
      }
    }
  }
  
  protected HTTPTransportInfo sendAS2(GNTransportInfo stuff) throws Exception
  {
    HttpMessageContext context = HttpMessageContext.getInstance();
    HTTPTransportInfo transportInfo = new HTTPTransportInfo();
    GNTransportPayload payload = (GNTransportPayload)stuff.getPayload();

    byte[] contend = payload.getFileContent();
    GNTransportHeader httpheader = new GNTransportHeader(payload.getHeader());
    context.Info("sendAS2");
    Hashtable headers = httpheader.getProperties();
    if(headers.containsKey(IAS2Headers.AS2_VERSION))
    {
//headers.put("Host", "merlin.seebeyond.com:30080"); //hack, to be removed
      HttpCommInfo httpcomminfo = (HttpCommInfo)stuff.getCommInfo();
      context.Info("sendAS2: is AS2 message");
      
      //TWX20100518 add http basic auth header
      addHttpBasicAuthHeader(httpheader, httpcomminfo);

      String MDNmode = (String)headers.get(MDN);
      if( MDNmode != null && IAS2Headers.SYNC.equalsIgnoreCase(MDNmode))
      { // synchronous MDN
        headers.remove(MDN);
        context.Info("sendAS2: AS2 Sync MDN");
        int id = context.FindSignalIndex((String)headers.get(IAS2Headers.ORIGINAL_MESSAGE_ID));
        headers.remove(IAS2Headers.ORIGINAL_MESSAGE_ID);

        Object[] obj = new Object[2];
        obj[0] = headers;
        obj[1] = contend;

        context.setSignalObject(id,obj);
        context.NotifySignal(id);
        transportInfo.responseCode = HttpServletResponse.SC_OK;
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
      }
      else
      {
        if (MDNmode == null)
          context.Debug("sendAS2: AS2 Request message");
        else
        {
          context.Debug("sendAS2: AS2 async MDN");
          headers.remove(MDN);
        }
        GNHttpConnection httpconnection = new GNHttpConnection(httpcomminfo.getUrlWithoutUserInfo(),httpcomminfo.isAuthServer(),httpcomminfo.isAuthClient(),httpcomminfo.isVerifyHostname(),httpcomminfo.getKeystoreFile(),httpcomminfo.getKeystorePassword(),httpcomminfo.getTruststoreFile(),httpcomminfo.getTruststorePassword(),httpcomminfo.getTimeout());
        if(context.getLogheader())
        {
          context.Debug("sendAS2:" + httpconnection.logMessageSetting());
        }

       //context.Debug("msg [" + headers + contend + "]");

        if(IHttpCommInfo.HTTP_METHOD_POST.equals(httpcomminfo.getRequestMethod()))
          httpconnection.doPost(headers, contend);
        else if(IHttpCommInfo.HTTP_METHOD_GET.equals(httpcomminfo.getRequestMethod()))
          httpconnection.doGet(headers);

        transportInfo.responseCode = httpconnection.getResponseCode();
        transportInfo.contend = httpconnection.getResponseMessage();
        transportInfo.header = httpconnection.getResponseHeaders();
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;

        if(headers.containsKey(IAS2Headers.DISPOSITION_NOTIFICATION_TO) &&
          !headers.containsKey(IAS2Headers.RECEIPT_DELIVERY_OPTION))
        { // synchronous AS2 request message
          context.Debug("sendAS2: AS2 request message expecting a sync MDN...");
          GNTransportHeader replyhttpheader = new GNTransportHeader(transportInfo.header);
          if(context.getLogheader())
            context.Debug("sendAS2: AS2 Sync MDN Header:" + replyhttpheader);
          transportInfo.header = standardizeHeader(replyhttpheader.getProperties());
          Hashtable jmsheader = new Hashtable();
          jmsheader.put("GNSelector", context.getDESTINATION_NAME()+"x");

          GNTransportPayload rcpayload = new GNTransportPayload(transportInfo.header, new String[0], transportInfo.contend);
          sendGTSMessage(rcpayload, jmsheader);
        }
      }
    }
    else
      {
        transportInfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
        context.Info("sendAS2: not AS2 message");
      }
    return transportInfo;
  }

  public HTTPTransportInfo sendGTASPackage(Hashtable header, byte[] transportinfo) throws Exception
  {
  //wont check package type
    GNTransportInfo stuff = GNTransportInfo.load(transportinfo);
    HttpMessageContext context = HttpMessageContext.getInstance();
    context.Info("sendGTASPackage: try sending GTAS Payload");
    GNTransportHeader loghead = new GNTransportHeader(stuff.getPayload().getHeader());
    if(context.getLogheader())
      context.Debug("send: Header\r\n" + loghead);

    HTTPTransportInfo httptransportinfo = sendGTAS(stuff);
    if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
      return httptransportinfo;

    httptransportinfo = sendRN(stuff);
    if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
      return httptransportinfo;

    return sendAS2(stuff);
  }

  public HTTPTransportInfo sendGTASSendCMDPackage(Hashtable header, byte[] keystorestuff) throws IOException
  {
      HTTPTransportInfo httptransportinfo = new HTTPTransportInfo();
      GNTransportHeader httpheader = new GNTransportHeader(header);
      HttpMessageContext context = HttpMessageContext.getInstance();
      if(httpheader.isSenderCMDMessage())
      {
        context.Info("sendGTASSendCMDPackage: get send CMD message");
        httptransportinfo.receiveStatus = HTTPTransportInfo.STATUS_SUCCESS;
        try
        {
          if(httpheader.isSenderCMD_SetTrustStore())//set trusted key store
          {
            //JavaKeyStoreHandler thandler = JavaKeyStoreHandler.getTrustStore();
            //NSL20070504 Open the specified truststore
            String truststoreFN = JavaKeyStoreHandler.getTrustStoreName(context.getTrustStoreFileName());
            String truststorePass = JavaKeyStoreHandler.getTrustStorePassword(context.getTrustStorePass());
            JavaKeyStoreHandler thandler = new JavaKeyStoreHandler();
            thandler.open(truststoreFN, truststorePass);
            
            JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
            handler.open(keystorestuff);
            
            thandler.append(handler);
            //thandler.write();
            //NSL20070504 write to specified location instead of default
            thandler.write(truststoreFN, truststorePass);
            
            context.Info("sendGTASSendCMDPackage: set truststore cmd");
            //context.Info("sendGTASSendCMDPackage: truststore "+ truststoreFN +" has been changed!\r\n     Tomcat needs to restart!!!");
            //NSL20070507 No long need restart for Truststore
            context.Info("sendGTASSendCMDPackage: truststore "+ truststoreFN +" has been changed.");
          }
          else if(httpheader.isSenderCMD_SetKeyStore())//set key store
          {
            JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
            handler.open(keystorestuff);
            handler.write(context.getKeyStoreFileName(), context.getKeyStorePass());
            context.Info("sendGTASSendCMDPackage: set keystore cmd");
            context.Info("sendGTASSendCMDPackage: keystore "+ context.getKeyStoreFileName() + " has been modified!\r\n     Tomcat needs to restart!!!");
          }
          else
            httptransportinfo.receiveStatus = HTTPTransportInfo.STATUS_FAIL;
        }
        catch (Exception ex)
        {
            httptransportinfo.receiveStatus = HTTPTransportInfo.STATUS_FAIL;
        }
      }
      else
        {
            context.Info("sendGTASSendCMDPackage: not Send CMD message");
            httptransportinfo.receiveStatus = HTTPTransportInfo.STATUS_UNKNOWN;
        }
      return httptransportinfo;
  }

  public HTTPTransportInfo send(Hashtable header, byte[] packstuff) throws Exception
  {
    HttpMessageContext context = HttpMessageContext.getInstance();

    HTTPTransportInfo httptransportinfo = sendGTASSendCMDPackage(header, packstuff);
    if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
      return httptransportinfo;

    httptransportinfo = sendGTASPackage(header, packstuff);
    if(httptransportinfo.receiveStatus != HTTPTransportInfo.STATUS_UNKNOWN)
      return httptransportinfo;

     context.Info("send:cannot send this message");
     httptransportinfo.responseCode = HttpServletResponse.SC_NOT_FOUND;
     return httptransportinfo;
  }
  
}