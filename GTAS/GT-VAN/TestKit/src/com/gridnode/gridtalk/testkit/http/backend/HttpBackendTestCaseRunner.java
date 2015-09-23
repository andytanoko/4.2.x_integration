/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpBackendTestCaseRunner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107           Created
 */
 package com.gridnode.gridtalk.testkit.http.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gridnode.gridtalk.testkit.http.PartnerConfig;
import com.gridnode.gridtalk.testkit.http.Partner;
import com.gridnode.gridtalk.testkit.http.Util;

/**
 * Runs a single Backend test case.
 * @author i00107
 */
public class HttpBackendTestCaseRunner
{
  private Logger logger = Logger.getLogger("HttpBackendTestCaseRunner");

  private boolean _unitTest = Boolean.getBoolean("is.unit.test");
  private String _testCaseId;
  private boolean _valid = false;
  private HttpBackendHubConfig _hubConf;
  private PartnerConfig _tpConf;
  private StringBuffer _errors = new StringBuffer();
  private Timer _timer;
  private String _startTime;

  /**
   * @param testCaseId Test case id
   */
  public HttpBackendTestCaseRunner(String testCaseId)
  {
    init(testCaseId);
  }

  private void init(String testCaseId)
  {
    _testCaseId = testCaseId;
    File baseDir = new File("conf", testCaseId);
    logger.log(Level.INFO, "Expected testcasedir="+baseDir.getAbsolutePath());
    if (baseDir.exists())
    {
      File hubConf = new File(baseDir, "hub.conf");
      File tpConf = new File(baseDir, "tp.conf");
      if (hubConf.exists() && tpConf.exists())
      {
        _hubConf = new HttpBackendHubConfig(hubConf);
        _tpConf = new PartnerConfig(tpConf);
        if (!_hubConf.isValid())
        {
          _errors.append(_hubConf.getError());
        }
        if (!_tpConf.isValid())
        {
          _errors.append(_tpConf.getError());
        }
        if (_errors.length()==0)
        {
          _valid = true;
        }
      }
    }
    else
    {
      _errors.append("No such Test case id configured.");
    }
  }
  
  /**
   * @return <b>true</b> if the test case configurations are valid.
   */
  public boolean isTestCaseValid()
  {
    return _valid;
  }
  
  /**
   * @return Errors in the test case configurations.
   */
  public String getError()
  {
    return _errors.toString();
  }
  
  /**
   * @return Starting time of the test case.
   */
  public String getTestStartTime()
  {
    return _startTime;
  }
  
  /**
   * Runs the test case.
   */
  public void run()
  {
    final List<Partner> partners = _tpConf.getPartnerList();
    SendTask sendTask = new SendTask(partners, _hubConf.getSendNumTx());
    
    _timer = new Timer(_testCaseId, !_unitTest);
    _timer.schedule(sendTask, 1000, _hubConf.getSendInterval()*1000);
    _startTime = new Date().toString();
  }

  /**
   * Stops the test case.
   */
  public void stop()
  {
    if (_timer != null)
    {
      _timer.cancel();
     logger.log(Level.INFO, "Test case "+_testCaseId +" is stopped at "+new Date());
      HttpBackendServlet.removeTestCase(_testCaseId);
    }
  }
  

  /**
   * A timer task to send messages to HTTP backend connector
   * @author i00107
   */
  class SendTask extends TimerTask
  {
    private int _numTx;
    private List<Partner> _partners;
    private boolean _ended;
    private boolean[] _done;

    /**
     * @param partners List of partners to send msgs
     * @param numTx Number of transactions to send each time
     */
    SendTask(List<Partner> partners, int numTx)
    {
      _numTx = numTx;
      _partners = partners;
      _done = new boolean[partners.size()];
    }
    
    /**
     * @see java.util.TimerTask#cancel()
     */
    public boolean cancel()
    {
      if (_ended)
      {
        return false;
      }
      _ended = true;
      return true;
    }

    /**
     * @see java.util.TimerTask#run()
     */
    public void run()
    {
      if (!_ended)
      {
        synchronized (_done)
        {
          int _sendCount = 0;
          for (int i=0; i<_done.length && _sendCount<_numTx; i++)
          {
            if (!_done[i])
            {
              Partner p = _partners.get(i);
              
              while (!p.hasFinishedTx() && _sendCount<_numTx)
              {
                //spawn thread to send for this partner
                p.incrementCounter();
                SendThread t = new SendThread(p);
                _sendCount++;
                t.start();
              }
              if (p.hasFinishedTx())
              {
                _done[i] = true;
                logger.log(Level.INFO,"Finished sending all tx to partner "+p.getId());
              }
            }
          }
          if (_sendCount < _numTx)
          {
            logger.log(Level.INFO,"Finished sending all tx to all partners");
            stop();
            logger.log(Level.INFO,"Test case "+_testCaseId + " finished at "+new Date());
          }
        }
      }
      else
      {
        logger.log(Level.INFO,"Send timer has been cancelled.");
      }
    }
  }
  
  private final static String RECEIVER_ID_PLACEHOLDER = "###_RECEIVER_ID_###";
  private final static String SENDER_ID_PLACEHOLDER = "###_SENDER_ID_###";
  private final static String DOC_GEN_TS_PLACEHOLDER = "###_DOC_GEN_TS_###";
  private final static String DOC_ID_PLACEHOLDER = "###_DOC_ID_###";
  
  private final static String SENDER = "sender";
  private final static String RECIPIENT = "recipient";
  private final static String DOCTYPE = "doctype";
  private final static String FILENAME = "filename";
  private final static String CONTENTTYPE = "contenttype";
  
  private String getContent(File f) throws IOException
  {
    FileInputStream is = new FileInputStream(f);
    byte[] buff = new byte[1024];
    int readLen = -1;
    StringBuffer content = new StringBuffer();
    while ((readLen=is.read(buff))>0)
    {
      content.append(new String(buff, 0, readLen));
    }    
    return content.toString();
  }

  
  private String genTs()
  {
    //SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd'T'HHmmss'.'SSS'Z'");
    //Date   currentTime_1 = new Date();
    //return formatter.format(currentTime_1);
    return Util.genTS();
  }

  /**
   * A thread to send message for a partner.
   * @author i00107
   */
  class SendThread extends Thread
  {
    private Partner _partner;
    private Properties _props;
    private int _txCounter;

    /**
     * @param p The Partner to send msg to.
     */
    public SendThread(Partner p)
    {
      _partner = p;
      _txCounter = p.getCounter();
    }
    
    /**
     * @see java.lang.Thread#run()
     */
    public void run()
    {
      byte[] msg = generateDocMsg();
      

      if (msg != null)
      {
        if (_unitTest)
        {
          logger.log(Level.INFO,"Sending msg "+_txCounter+" for partner "+_partner.getId());
        }
        else
        {
          new HttpBackendClient(_hubConf.getURL(), msg, _props, _props.getProperty(CONTENTTYPE, "text/html"));
        }
      }
    }
    

    private byte[] generateDocMsg()
    {
      String receiverId = _partner.getId();
      String senderId = _hubConf.getBEid();
      String docGenTs = genTs();
      String docID = genTs();//UUID.randomUUID().toString();
      String fileName;
      try
      {
        String template = getContent(_hubConf.getDocTemplateFile());
        
        String content = template.replaceAll(SENDER_ID_PLACEHOLDER, senderId);
        content = content.replaceAll(RECEIVER_ID_PLACEHOLDER, receiverId);
        content = content.replaceAll(DOC_GEN_TS_PLACEHOLDER, docGenTs);
        content = content.replaceAll(DOC_ID_PLACEHOLDER, docID);
        
        _props = (Properties)_hubConf.getDocProperties().clone();
        _props.setProperty(SENDER, senderId);
        _props.setProperty(RECIPIENT, receiverId);
        
        String docType = _props.getProperty(DOCTYPE);
        String fileNamePattern = _props.getProperty(FILENAME);
        fileName = MessageFormat.format(fileNamePattern, docType+"_"+_txCounter);
        _props.setProperty("filename", fileName);
        if (_unitTest)
        {
          logger.log(Level.INFO,"payload is "+content);
        }
        return content.getBytes();
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE,"Error reading action template "+_hubConf.getDocTemplate(), ex);
      }
      return null;
    }
    

  }
  
  public static void main(String[] args)
  {
    HttpBackendTestCaseRunner runner = new HttpBackendTestCaseRunner("caseBackend4A5");
    if (runner.isTestCaseValid())
    {
      try
      {
        runner.run();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    else
    {
      System.out.println(runner.getError());
    }
    
  }
}
