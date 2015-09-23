/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpMessgeContext.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 * Feb 26 2004	  Guo Jianyu		   changed default http sync timeout to 30 minutes
 * Nov 14 2005    Neo Sok Lay             Load config file relative to SystemUtil.workingDir.
 * Jan 12 2007    Neo Sok Lay             Use ProxySelector for proxy connection.
 * Feb 06 2007    Neo Sok Lay             Change topic to more generic Destination.
 * Mar 12 2007    Neo Sok Lay             Use UUID for unique filename.
 */

package com.gridnode.pdip.base.transport.helpers;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.*;

import com.gridnode.pdip.framework.net.GNProxyInitializer;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

public class HttpMessageContext
{

    // Constants for logging
  public static final int DEBUG = 0;
  public static final int INFO  = 1;
  public static final int ERROR = 2;
  public static final int WARN  = 3;

    //0 none
  //1 system.out
  //2 file
  //3 TPLog
  public static final int LOG_NONE = 0;
  public static final int LOG_SYSTEM = 1;
  public static final int LOG_FILE = 2;
  public static final int LOG_GTAS = 3;
  public static final int LOG_TEXTAREA = 4;

  protected ServletConfig config = null;
  protected GTConfigFile  configfile = null;
  protected Vector       SignalCookieList = new Vector();
  protected Vector       SignalEventList = new Vector();
  protected Vector       SignalObjectList = new Vector();

  Calendar logTimeStamp = new GregorianCalendar();
  protected String logFileName = "HttpReceiver";
  protected OutputStreamWriter logger = null;
  protected JTextArea          logger_textarea = null;
  protected StringBuffer       logger_buffer = new StringBuffer();
  protected boolean            enableLoggerBuffer = false;
  static HttpMessageContext    httpMessageContext = new HttpMessageContext();
  private int loglevel = DEBUG;
  private int synTimeout = 1800; //default: 30 minutes
  private boolean logheader = false;
  private String INITIAL_CONTEXT_FACTORY;
  private String PROVIDER_URL;
  private String URL_PKG_PREFIXES;
  private String CONNECTION_FACTORY;
  private String DESTINATION_NAME;
  private int   TIME_TO_LIVE = 0;
  private String email_alert_server = null;
  private String email_alert_sender = null;
  private String email_alert_subject = null;
  private String email_alert_content = null;
  private String email_alert_receiver = null;
  private boolean email_alert_enable = false;
  private int  email_alert_attachment = 1;
  private String email_alert_attachment_name = null;
  private int logType = LOG_SYSTEM;
  String logfilefullname = "";
  private String login_username;
  private String login_passwd;
  private String senderURL;
  private String keyStoreFileName;
  private String keyStorePass;

  private String trustStoreFileName;
  private String trustStorePass;

  private Properties _proxySetting = new Properties();
  
  protected HttpMessageContext()
  {
  }

  static public HttpMessageContext getInstance()
  {
    return httpMessageContext;
  }

  public int FindSignalIndex(String cookie)
  {
    int id = SignalCookieList.indexOf(cookie);
    return id;
  }

  public void RemoveSignal(String cookie)
  {
   int id = FindSignalIndex(cookie);
   if(id != -1)
    {
      SignalEventList.remove(id);
      SignalCookieList.remove(id);
      SignalObjectList.remove(id);
    }
  }

  public Object getSignalEvent(int id)
  {
   if(id != -1)
    return SignalCookieList.get(id);
   else
      return null;
  }

  public Object getSignalObject(int id)
  {
    if(id != -1)
      return SignalObjectList.get(id);
    else
      return null;
  }

  public void setSignalObject(int id, Object obj)
  {
    if(id != -1 && id < SignalObjectList.size())
      SignalObjectList.set(id,obj);
  }

  public String Exception2String(Throwable e)
  {
    ByteArrayOutputStream detailedError = new ByteArrayOutputStream();
    PrintStream outS = new PrintStream(detailedError);
    if(e != null)
      e.printStackTrace(outS);
    outS.flush();
    outS.close();
    return new String(detailedError.toByteArray());
  }

  static public void waitForaWhile(int ms)
  {
    if(ms == 0)
     return;
    Object event = new Object();
    synchronized(event)
    {
      try
      {
        event.wait(ms);
      }
      catch (Exception ex)
      {
      }
    }
  }
  public int AddSignal(String cookie, Object event, Object obj)
  {
    SignalCookieList.add(cookie);
    SignalEventList.add(event);
    SignalObjectList.add(obj);
    int id = SignalCookieList.indexOf(cookie);
    return id;
  }

  public void NotifySignal(int id)
  {
    Object event = getSignalEvent(id);
    synchronized(event)
        {
          event.notifyAll();
        }
  }

  public void waitSignal(int id)
  {
    Object event = getSignalEvent(id);
    log(DEBUG, "Context: Start wait sigal");
    synchronized(event)
    {
      try
      {
        Debug("Context: wait for[" + getSynTimeout() +"]");
        event.wait(getSynTimeout() * 1000);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    Debug("Context:Finish wait signal");
  }

  public void Debug(String msg)
  {
    log(DEBUG, msg);
  }

  public void Info(String msg)
  {
    log(INFO, msg);
  }
  
  public void Warn(String msg, Throwable e)
  {
    log(WARN, msg, e);
  }

  /**
   * @deprecated
   * @param msg
   * @param e
   */
  public void Error(String msg, Throwable e)
  {
    log(ERROR, msg, e);
  }
  
  /**
   * @param msg
   * @param e
   */
  public void Error(String errorCode, String msg, Throwable e)
  {
    log(errorCode, ERROR, msg, e);
  }
  
  static private String replaceSubString(String orig, String toreplace, String newSubString)
  {
    if(orig == null || orig.length() <= 0 || toreplace == null || toreplace.length() <= 0)
     return orig;
    if(newSubString == null)
     newSubString = "";
    int index = orig.indexOf(toreplace);
    while(index >= 0)
    {
      String first = "";
      first = orig.substring(0,index);
      String last = orig.substring(index + toreplace.length());
      orig = first + newSubString + last;
      index = orig.indexOf(toreplace, index + newSubString.length());
    }
    return orig;
  }

  public boolean sendEmailAlert(HttpServletRequest request, Throwable e, Hashtable headers, byte[] attachment)
  {
    GNTransportHeader header = new GNTransportHeader(headers);
    return sendEmailAlert(request, e, header.toString() , attachment);
  }

  public boolean sendEmailAlert(HttpServletRequest request, Throwable e, String headers, byte[] attachment)
  {
    boolean success =  false;
    try
    {
      if(email_alert_enable)
      {
        Date now = new Date();
        String subject = email_alert_subject;
        String content = email_alert_content;
        ByteArrayOutputStream detailedError = new ByteArrayOutputStream();
        String detailedErrorStr = null;
        if (e != null)
        {
          PrintStream outS = new PrintStream(detailedError);
          e.printStackTrace(outS);
          outS.flush();
          outS.close();
          detailedErrorStr = new String(detailedError.toByteArray());
        }
        else
          detailedErrorStr = "No detailed error message.";

        subject = replaceSubString(subject, "%n","\r\n");
        subject = replaceSubString(subject, "%t","\t");
        subject = replaceSubString(subject, "%d", now.toString());
        subject = replaceSubString(subject, "%h", headers);
        subject = replaceSubString(subject, "%c", request.getRemoteHost() + "(" + request.getRemoteAddr()+")");
        subject = replaceSubString(subject, "%s", request.getServerName() + ":" + request.getServerPort());

        subject = replaceSubString(subject, "%e", (e == null) ? "No error message" : e.toString());
        subject = replaceSubString(subject, "%E", detailedErrorStr);

        content = replaceSubString(content, "%n","\r\n");
        content = replaceSubString(content, "%t","\t");
        content = replaceSubString(content, "%d", now.toString());
        content = replaceSubString(content, "%h", headers);
        content = replaceSubString(content, "%e", (e == null) ? "No error message" : e.toString());
        content = replaceSubString(content, "%c", request.getRemoteHost() + "(" + request.getRemoteAddr()+")");
        content = replaceSubString(content, "%s", request.getServerName() + ":" + request.getServerPort());
        content = replaceSubString(content, "%E", detailedErrorStr);

        success =  SMTPAgent.sendMessage(email_alert_server, email_alert_sender,email_alert_receiver, subject , content, email_alert_attachment_name, attachment);
        if(success)
          Debug("send email alert success");
        else
          Debug("send email alert fail");
      }
      else
        Debug("send email alert disable");
    }
    catch (Exception ex)
    {
      Error("send email alert fails", ex);
    }
    return success;
  }

  public void log(int loggingConstant, String msg)
  {
    log(loggingConstant, msg, null);
  }

  protected void openNewLog()
  {
    Calendar cal = new GregorianCalendar();
    if(logger == null || logTimeStamp.get(Calendar.DAY_OF_MONTH) != cal.get(Calendar.DAY_OF_MONTH))
    {
      logTimeStamp = cal;
      if(logger != null)
      {
        try
        {
          logger.close();
        }
        catch (Exception ex)
        {
          System.out.println("Error: cannot close Log File");
        }
      }
      String logfilename1 = logFileName + "_" + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH) + 1) +"_" + cal.get(Calendar.DAY_OF_MONTH) + ".log";
      
      /* TWX 10032008
      String pathname = "../" + "log/";
      File pathfile = new File(pathname);
      if(!pathfile.exists())
      {
        pathname = "../" + "logs/";
        pathfile = new File(pathname);
        if(!pathfile.exists())
          pathname = "";
      }*/
      
      String pathname = SystemUtil.getLogDir() +"/httpreceiver/";
      
      File logfile = new File(pathname + logfilename1);
      logfilefullname = logfile.getAbsolutePath();
      if(getLogger_textarea() != null)
      {
        getLogger_textarea().append("HttpMessageContext Log File is:" + logfilefullname);
      }
      System.out.println("HttpMessageContext Log File is:" + logfilefullname);
      try
      {
        logger = new FileWriter(logfilefullname ,true);
      }
      catch (Exception ex)
      {
        System.out.println("cannot create new log");
      }
    }
  }
  
  public synchronized void log(int loggingConstant, String msg, Throwable e)
  {
  	log(null,loggingConstant,msg,e);
  }

  public synchronized void log(String errorCode, int loggingConstant, String msg, Throwable e)
  {
    Date now = new Date();
    String tmp = null;
    String detailedError = Exception2String(e);

    switch(loggingConstant)
    {
      case( DEBUG ) :
        tmp = "DEBUG";
        break;
      case( INFO )  :
        tmp = "info";
        break;
      case( WARN )  :
      	tmp = "WARN";
        break;
      case( ERROR ) :
        tmp = "ERORR";
        break;
      default       :
        tmp = "UNKNOWN (" + loggingConstant + ")";
        break;
    }

    if(getLogger_textarea() != null && getLogType() != LOG_TEXTAREA)
    {
      if(getLoglevel() <= loggingConstant)
        getLogger_textarea().append(msg + "\r\n" + detailedError);
    }
    if(isEnableLoggerBuffer())
    {
        logger_buffer.append(now + "  [" + tmp + "] " + Thread.currentThread() + ": " + msg + "\r\n" + detailedError);
    }
    if(getLogType() == LOG_NONE)
        return;
    else if(getLogType() == LOG_SYSTEM)
    {
      System.out.println(now + "  [" + tmp + "] " + Thread.currentThread() + ": " + msg + "\r\n" + detailedError);
      return;
    }
    else if(getLogType() == LOG_TEXTAREA)
    {
      if(getLogger_textarea() != null)
      {
        if(getLoglevel() <= loggingConstant)
          getLogger_textarea().append(msg + "\r\n" + detailedError);
      }
      return;
    }
    else if(getLogType() == LOG_GTAS)
    {
      if(loggingConstant == DEBUG)
        TptLogger.debugLog(Thread.currentThread().toString(), "HTTPChannel",  msg);
      else if(loggingConstant == INFO)
        TptLogger.infoLog(Thread.currentThread().toString(), "HTTPChannel",  msg);
      else if(loggingConstant == WARN)
        TptLogger.warnLog(Thread.currentThread().toString(), "HTTPChannel",  msg);
      else
        TptLogger.errorLog(errorCode,Thread.currentThread().toString(), "HTTPChannel",  msg, e);
      return;
    }
    if(getLoglevel() <= loggingConstant)
      System.out.println("[" + tmp + "] " + Thread.currentThread() + ": " + msg + "\r\n" + detailedError);
    try
    {
      openNewLog();
      PrintWriter pw = new PrintWriter(logger);
      pw.println( now + "  [" + tmp + "] " + Thread.currentThread() + ": " + msg );
      if (e!=null)
      {
        e.printStackTrace(pw);
      }
      logger.flush();
    }
    catch (Exception ex)
    {
      System.out.println("cannot write to log file");
    }
  }

  public String getConfigString(String header)
  {
    return getConfigString(header, null);
  }

  public String getConfigString(String header, String defaultvalue)
  {
    String value = "";
    if(configfile != null)
      value = configfile.getProperty(header);
    else
      value = config.getInitParameter(header);
    if(value == null || value.length() == 0)
      value = defaultvalue;
    if(value == null)
      value = "";
    return value;
  }

  public int getConfigInt(String header)
  {
    return getConfigInt(header, -1);
  }

  public int getConfigInt(String header, int defaultvalue)
  {
    String value = getConfigString(header);
    if("".equals(value))
      return defaultvalue;
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception ex)
    {
      return -1;
    }
  }

  public boolean getConfigBoolean(String header)
  {
    return getConfigBoolean(header, false);
  }

  public boolean getConfigBoolean(String header, boolean defaultvalue)
  {
    String value = getConfigString(header);
    if("".equals(value))
     return defaultvalue;
    try
    {
      return Boolean.valueOf(value).booleanValue();
    }
    catch(Exception e)
    {
      return false;
    }
  }

  public String getLogFileName()
  {
    return logFileName;
  }
  public void setLogFileName(String logFileName)
  {
    if(logFileName == null || logFileName.equals(this.logFileName))
     return;
    this.logFileName = logFileName;
    try
    {
      logger.close();
    }
    catch (Exception ex)
    {
    }
    logger = null;
  }

  public void setLoglevel(int loglevel)
  {
    if(loglevel > 2)
      loglevel = 2;

    if(loglevel < 0)
      loglevel = 0;

    this.loglevel = loglevel;
  }

  public int getLoglevel()
  {
    return loglevel;
  }

  public void setSynTimeout(String synTimeout)
  {
    try
    {
      int timeout = Integer.parseInt(synTimeout);
      if (timeout>0)
        setSynTimeout(timeout);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  static public String CreateNewMsgId()
  {
    Calendar cal = new GregorianCalendar();
    return "GNRNUID" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH)
            + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
  }

  static public File saveToTempFile(byte[] content)
  {
    try
    {
      File tfile = File.createTempFile("httpm"+UUIDUtil.getRandomUUIDInStr(),".tmp");
      tfile.deleteOnExit();
      FileOutputStream out = new FileOutputStream(tfile);
      out.write(content);
      out.flush();
      out.close();
      return tfile;
    }
    catch (Exception ex)
    {
      return null;
    }
  }


  static public Vector string2Vector(String str, char seperator)
  {
    Vector stringvector = new Vector();
    String temp = str;
    int index = -1;
    do
    {
      String aName = "";
      index = temp.indexOf(seperator);
      if(index >= 0)
      {
        aName = temp.substring(0, index);
        temp = temp.substring(index + 1);
        if(!emptyString(aName))
          stringvector.add(aName);
      }
    }
    while(index >= 0);
    if(temp != null && temp.length() > 0)
      stringvector.add(temp);
    return stringvector;
  }

  static public String[] stringVector2Array(Vector strVector)
  {
    if(strVector == null || strVector.size() == 0)
      return null;
    else
    {
      int size = strVector.size();
      String[] args = new String[size];
      for(int i = 0; i < size; i++)
        args[i] = (String)strVector.get(i);
      return args;
    }
  }



  static public byte[] getMessage(InputStream is) throws IOException
  {
    ByteArrayOutputStream   buffer = new ByteArrayOutputStream();
    int numBytesRead = 0;
    byte[] subbuffer = new byte[1000];
    while(numBytesRead!=-1)
    {
        numBytesRead = is.read(subbuffer,0,1000);
        if(numBytesRead > 0)
         buffer.write(subbuffer,0,numBytesRead);
    }
    return buffer.toByteArray();
  }

  static public String parseItem(String content, String head)
  {
    int idx = content.indexOf("<" + head + ">");
    if(idx < 0)
      return "";
    String result = content.substring(idx + head.length() + 2);
    idx = result.indexOf("</" + head + ">");
    if(idx < 0)
      return "";
    result = result.substring(0,idx);
    return result;
  }


  static public String readLine(InputStream input)
  {
    boolean bGetLine = false;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    try
    {
      while(!bGetLine)
      {
        byte[] by = new byte[1];
        input.read(by,0,1);
        if(by[0] == 0)
          break;
        if(by[0] != '\r')
        {
          if(by[0] == '\n')
            bGetLine = true;
          else
            buffer.write(by);
        }
      }
    }
    catch(Exception ex){}
    return buffer.toString();
  }


  public void setSynTimeout(int synTimeout)
  {
    this.synTimeout = synTimeout;
  }

  public int getSynTimeout()
  {
    return synTimeout;
  }

  public void setLogheader(boolean logheader)
  {
    this.logheader = logheader;
  }

  public boolean getLogheader()
  {
    return logheader;
  }
  
  public void setINITIAL_CONTEXT_FACTORY(String INITIAL_CONTEXT_FACTORY)
  {
    this.INITIAL_CONTEXT_FACTORY = INITIAL_CONTEXT_FACTORY;
  }
  public String getINITIAL_CONTEXT_FACTORY()
  {
    return INITIAL_CONTEXT_FACTORY;
  }
  public void setPROVIDER_URL(String PROVIDER_URL)
  {
    this.PROVIDER_URL = PROVIDER_URL;
  }
  public String getPROVIDER_URL()
  {
    return PROVIDER_URL;
  }
  public void setURL_PKG_PREFIXES(String URL_PKG_PREFIXES)
  {
    this.URL_PKG_PREFIXES = URL_PKG_PREFIXES;
  }
  public String getURL_PKG_PREFIXES()
  {
    return URL_PKG_PREFIXES;
  }
  public void setCONNECTION_FACTORY(String CONNECTION_FACTORY)
  {
    this.CONNECTION_FACTORY = CONNECTION_FACTORY;
  }
  public String getCONNECTION_FACTORY()
  {
    return CONNECTION_FACTORY;
  }
  public void setDESTINATION_NAME(String DEST_NAME)
  {
    this.DESTINATION_NAME = DEST_NAME;
  }
  public String getDESTINATION_NAME()
  {
    return DESTINATION_NAME;
  }
  public void setTIME_TO_LIVE(int TIME_TO_LIVE)
  {
    this.TIME_TO_LIVE = TIME_TO_LIVE;
  }
  public void setTIME_TO_LIVE(String TIME_TO_LIVE)
  {
    try
    {
      setTIME_TO_LIVE(Integer.parseInt(TIME_TO_LIVE));
    }
    catch (Exception ex)
    {
      setTIME_TO_LIVE(0);
    }
  }
  public int getTIME_TO_LIVE()
  {
    return TIME_TO_LIVE;
  }
  public void setEmail_alert_attachment(int email_alert_attachment)
  {
    this.email_alert_attachment = email_alert_attachment;
  }
  public void setEmail_alert_attachment_name(String email_alert_attachment_name)
  {
    this.email_alert_attachment_name = email_alert_attachment_name;
  }
  public void setEmail_alert_content(String email_alert_content)
  {
    this.email_alert_content = email_alert_content;
  }
  public void setEmail_alert_enable(boolean email_alert_enable)
  {
    this.email_alert_enable = email_alert_enable;
  }
  public void setEmail_alert_receiver(String email_alert_receiver)
  {
    this.email_alert_receiver = email_alert_receiver;
  }
  public void setEmail_alert_sender(String email_alert_sender)
  {
    this.email_alert_sender = email_alert_sender;
  }
  public void setEmail_alert_server(String email_alert_server)
  {
    this.email_alert_server = email_alert_server;
  }
  public void setEmail_alert_subject(String email_alert_subject)
  {
    this.email_alert_subject = email_alert_subject;
  }
  public int getEmail_alert_attachment()
  {
    return email_alert_attachment;
  }
  public String getEmail_alert_attachment_name()
  {
    return email_alert_attachment_name;
  }
  public String getEmail_alert_content()
  {
    return email_alert_content;
  }
  public boolean isEmail_alert_enable()
  {
    return email_alert_enable;
  }
  public String getEmail_alert_receiver()
  {
    return email_alert_receiver;
  }
  public String getEmail_alert_sender()
  {
    return email_alert_sender;
  }
  public String getEmail_alert_server()
  {
    return email_alert_server;
  }
  public String getEmail_alert_subject()
  {
    return email_alert_subject;
  }
  public ServletConfig getConfig()
  {
    return config;
  }

  public void initConfig(ServletConfig config)
  {
    //GNHttpConnection.clearProxySettings();
    _proxySetting.clear(); //NSL20070112
    setConfig(config);
    setLogType(LOG_FILE);
    setKeyStoreFileName(getConfigString(ITransportConfig.TRANSPORT_HTTPS_KEYSTORE_NAME));
    setKeyStorePass(getConfigString(ITransportConfig.TRANSPORT_HTTPS_KEYSTORE_PASSWORD));
    setTrustStoreFileName(getConfigString(ITransportConfig.TRANSPORT_HTTPS_TRUSTSTORE_NAME));
    setTrustStorePass(getConfigString(ITransportConfig.TRANSPORT_HTTPS_TRUSTSTORE_PASSWORD));

    Properties prop = System.getProperties();

    if(!emptyString(getTrustStoreFileName()))
      prop.put("javax.net.ssl.trustStore", getTrustStoreFileName());
    if(!emptyString(getTrustStorePass()))
      prop.put("javax.net.ssl.trustStorePassword", getTrustStorePass());
    if(!emptyString(getKeyStoreFileName()))
      prop.put("javax.net.ssl.keyStore", getKeyStoreFileName());
    if(!emptyString(getKeyStorePass()))
      prop.put("javax.net.ssl.keyStorePassword", getKeyStorePass());

    setSenderURL(getConfigString(ITransportConfig.TRANSPORT_HTTP_SENDER_URL));
    setLogin_username(getConfigString(ITransportConfig.TRANSPORT_HTTP_SERVLET_USERNAME, "gridnode"));
    setLogin_passwd(getConfigString(ITransportConfig.TRANSPORT_HTTP_SERVLET_PASSWORD,"gtas"));
    setLogFileName(getConfigString(ITransportConfig.TRANSPORT_HTTP_SERVLET_LOGFILE));
    setLogheader(getConfigBoolean(ITransportConfig.TRANSPORT_HTTP_SERVLET_LOGHEADER));
    setLoglevel(getConfigInt(ITransportConfig.TRANSPORT_HTTP_SERVLET_LOGLEVEL));
    setINITIAL_CONTEXT_FACTORY(getConfigString(ITransportConfig.APPSERVER_INITIAL_CONTEXT_FACTORY));
    setPROVIDER_URL(getConfigString(ITransportConfig.APPSERVER_PROVIDER_URL));
    setURL_PKG_PREFIXES(getConfigString(ITransportConfig.APPSERVER_URL_PKG_PREFIXES));
    setCONNECTION_FACTORY(getConfigString(ITransportConfig.APPSERVER_CONNECTION_FACTORY));
    setDESTINATION_NAME(getConfigString(ITransportConfig.APPSERVER_DESTINATION_BRIDGE_TO_APP));
    setTIME_TO_LIVE(0);
    int timeout = getConfigInt(ITransportConfig.TRANSPORT_HTTP_SYNC_TIMEOUT);
    System.out.println("Sync Timeout is specified to be " + timeout );
    if (timeout > 0)
      setSynTimeout(timeout);
    /*NSL20070112
    GNHttpConnection.setHttp_proxy(getConfigString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_PAC),
                  getConfigString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_URL),
                  getConfigString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_PORT),
                  getConfigString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_USERNAME),
                  getConfigString(ITransportConfig.TRANSPORT_HTTP_HTTP_PROXY_PASSWORD));
    */
    GNProxyInitializer.init(_proxySetting); //NSL20070112
    setEmail_alert_enable(getConfigBoolean(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_ISENABLE, false));
    if(isEmail_alert_enable())
    {
      setEmail_alert_server(getConfigString(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_SERVER, "smtp2.gridnode.com"));
      setEmail_alert_receiver(getConfigString(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_RECEIVER, "qing.song.zou@gridnode.com"));
      setEmail_alert_sender(getConfigString(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_SENDER, "HTTPChannel@gridnode.com"));
      setEmail_alert_subject(getConfigString(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_SUBJECT, "HTTPChannel fails processing message --- %d"));
      setEmail_alert_content(getConfigString(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_CONTENT, "HTTPChannel fails while processing a message from %c to %s.%nThe message is attached as HTTPChannel.txt%n%nMessage headers are:%n%h%n%nDetailed description of error is%n%n%E%n%n%n%n%n%t%t%t%t%t%tGridTalk HTTPChannel(Ver I8, Mar 5 2003)%n%t%t%t%t%t%t%d"));
      setEmail_alert_attachment(getConfigInt(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_ATTACHMENT, 0));
      setEmail_alert_attachment_name(getConfigString(ITransportConfig.TRANSPORT_HTTP_EMAIL_ALERT_ATTACHMENT_NAME, "HTTPChannel.txt"));
    }
  }

  public String getJava_home()
  {
    String javaHome = System.getProperty("java.home");
    return javaHome;
  }

  static public boolean emptyString(String str)
  {
    if(str == null || str.length() <= 0)
     return true;
    else
     return false;
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("\r\nHttpMessageContext setting\r\n");
    buffer.append("\r\n  Java_Home[" + getJava_home() + "]\r\n");
    buffer.append("\r\n  Config settings\r\n");
    if(configfile != null)
    {
      String value = config.getInitParameter(ITransportConfig.TRANSPORT_HTTP_SERVLET_CONFIGFILE);
      File aFile = new File(value);
      buffer.append("       File[" + aFile.getAbsolutePath() + "]\r\n");
    }
    else
      buffer.append("       File[web.xml]\r\n");

    buffer.append("\r\n  log settings\r\n");
    buffer.append("       FileName[" + getLogfilefullname() + "]\r\n");
    buffer.append("       Header[" + getLogheader() + "]\r\n");
    buffer.append("       level[" + getLoglevel() + "]\r\n");
    buffer.append("       BufferLog[" + isEnableLoggerBuffer() + "]\r\n");

    buffer.append("\r\n  Sender settings\r\n");
    buffer.append("       senderURL[" + getSenderURL() + "]\r\n");
    buffer.append("\r\n  Receiver GTAS settings\r\n");
    buffer.append("       initial_context_factory[" + getINITIAL_CONTEXT_FACTORY() + "]\r\n");
    buffer.append("       provider_url[" + getPROVIDER_URL() + "]\r\n");
    buffer.append("       url_pkg_prefixes[" + getURL_PKG_PREFIXES() + "]\r\n");
    buffer.append("       connection_factory[" + getCONNECTION_FACTORY() + "]\r\n");
    buffer.append("       destination_name[" + getDESTINATION_NAME() + "]\r\n");
    /*NSL20070112
    buffer.append("\r\n  HTTP Proxy settings\r\n");
    buffer.append("       proxy_pac[" + GNHttpConnection.getHttp_proxy_pac() + "]\r\n");
    buffer.append("       proxy_url[" + GNHttpConnection.getHttp_proxy_url() + "]\r\n");
    buffer.append("       proxy_port[" + GNHttpConnection.getHttp_proxy_port() + "]\r\n");
    buffer.append("       proxy_auth_username[" + GNHttpConnection.getAuth_username() + "]\r\n");
    buffer.append("       proxy_auth_password[" + GNHttpConnection.getAuth_password() + "]\r\n");
    buffer.append("       proxy list[");
    Vector list = GNHttpConnection.getPacProxyList();
    for(int i = 0; i < list.size() ; i++)
    {
      if(i != 0)
        buffer.append(";");
      buffer.append((String)list.get(i));
    }
    buffer.append("]\r\n");
    buffer.append("       proxy cached setting\r\n");
    Hashtable cachelist = GNHttpConnection.getProxysettingList();
    Enumeration en = cachelist.keys();
    while(en.hasMoreElements())
    {
      String key = (String)en.nextElement();
      Object cachesetting = cachelist.get(key);
      Object[] proxysetting = (Object[])cachesetting;
      if(((Boolean)proxysetting[0]).booleanValue())
      {
        buffer.append("           [" + key + "] proxy[" + proxysetting[1] + " " + proxysetting[2] + ":" + proxysetting[3] +  "]\r\n");
      }
    }
    */
    
    return buffer.toString();
  }

  public void setConfig(ServletConfig config)
  {
    this.config = config;
    String value = config.getInitParameter(ITransportConfig.TRANSPORT_HTTP_SERVLET_CONFIGFILE);
    if(value != null && value.length() > 0)
    {
      File aFile = new File(SystemUtil.getWorkingDirPath(), value); //NSL20051114 The config file is relative to working dir
      if(aFile.exists())
      {
        configfile = new GTConfigFile(aFile.getAbsolutePath());
        if(configfile.getBooleanProperty(ITransportConfig.TRANSPORT_HTTP_SERVLET_USE_CONFIGFILE))
        {
          System.out.println("HttpMessageContext: Config from " + aFile.getAbsolutePath());
          return;
        }
        else
        {
          configfile = null;
        }
      }
    }
    else
    System.out.println("HttpMessageContext: Config from web.xml");
    
    //NSL20070112 Load proxy setting
    value = config.getInitParameter(ITransportConfig.TRANSPORT_HTTP_SERVLET_PROXYFILE);
    if (value != null && value.length() > 0)
    {
      File f = new File(SystemUtil.getWorkingDirPath(), value);
      if (f.exists())
      {
        try
        {
          _proxySetting.load(new FileInputStream(f));
        }
        catch (Exception ex)
        {
          System.out.println("Unable to load proxy file: "+f.getAbsolutePath());
        }
      }
    }
  }

  public void setLogType(int logType)
  {
    this.logType = logType;
  }
  public int getLogType()
  {
    return logType;
  }
  public JTextArea getLogger_textarea()
  {
    return logger_textarea;
  }

  public void setLogger_textarea(JTextArea logger_textarea)
  {
    this.logger_textarea = logger_textarea;
  }

  public String getLogfilefullname()
  {
    if(logfilefullname.length() == 0)
      openNewLog();
    return logfilefullname;
  }
  public boolean isEnableLoggerBuffer()
  {
    return enableLoggerBuffer;
  }

  public void setEnableLoggerBuffer(boolean enableLoggerBuffer)
  {
    this.enableLoggerBuffer = enableLoggerBuffer;
  }

  public void resetLoggerBuffer()
  {
    logger_buffer = new StringBuffer();
  }

  public String getLoggerBufferMessage()
  {
    return logger_buffer.toString();
  }
  public void setLogin_username(String login_username)
  {
    this.login_username = login_username;
  }
  public String getLogin_username()
  {
    return login_username;
  }
  public void setLogin_passwd(String login_passwd)
  {
    this.login_passwd = login_passwd;
  }
  public String getLogin_passwd()
  {
    return login_passwd;
  }
  public void setSenderURL(String senderURL)
  {
    this.senderURL = senderURL;
  }
  public String getSenderURL()
  {
    return senderURL;
  }
  public void setKeyStoreFileName(String keyStoreFileName)
  {
    this.keyStoreFileName = keyStoreFileName;
  }
  public String getKeyStoreFileName()
  {
    return keyStoreFileName;
  }
  public void setKeyStorePass(String keyStorePass)
  {
    this.keyStorePass = keyStorePass;
  }
  public String getKeyStorePass()
  {
    return keyStorePass;
  }
  public String getTrustStoreFileName()
  {
    return trustStoreFileName;
  }
  public String getTrustStorePass()
  {
    return trustStorePass;
  }
  public void setTrustStoreFileName(String trustStoreFileName)
  {
    this.trustStoreFileName = trustStoreFileName;
  }
  public void setTrustStorePass(String trustStorePass)
  {
    this.trustStorePass = trustStorePass;
  }
}
