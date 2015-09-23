/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifTestCaseRunner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 3, 2007    i00107           Created
 * sep 3, 2007    Vineeth		   Added a method loadFile()
 * sep 17, 2007   Vineeth          Enhanced the SendThread method
 */
 
package com.gridnode.gridtalk.testkit.http.rnif;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.testkit.http.PartnerConfig;
import com.gridnode.gridtalk.testkit.http.Partner;
import com.gridnode.gridtalk.testkit.http.Util;

/**
 * Runs a RNIF test case.
 * @author i00107
 *
 */
public class RnifTestCaseRunner
{
  private Logger logger = Logger.getLogger("RnifTestCaseRunner");

  private boolean _unitTest = Boolean.getBoolean("is.unit.test");
  private String _testCaseId;
  private boolean _valid = false;
  private RnifHubConfig _hubConf;
  private PartnerConfig _tpConf;
  private StringBuffer _errors = new StringBuffer();
  private Timer _timer;
  private String _startTime;
  private int _sendStart[] = new int[100];
  private int _sendCount = 0;
  private int _startingPoint = 0;
  private int _totalNumTx = 0;
  
  /**
   * 
   * @param testCaseId Test case id
   */
  public RnifTestCaseRunner(String testCaseId)
  {
    this(testCaseId, false);
  }
  
  /**
   * 
   * @param testCaseId Test case id
   * @param respondMode Start the test case in respond mode or not.
   */
  public RnifTestCaseRunner(String testCaseId, boolean respondMode)
  {
    init(testCaseId, respondMode);
  }

  private void init(String testCaseId, boolean respondMode)
  {
    _testCaseId = testCaseId;
    File baseDir = new File("conf", testCaseId);
    if (baseDir.exists())
    {
      File hubConf = new File(baseDir, "hub.conf");
      File tpConf = new File(baseDir, "tp.conf");
      
      if (hubConf.exists())
      {
        _hubConf = new RnifHubConfig(hubConf, respondMode);
        if (!_hubConf.isValid())
        {
          _errors.append(_hubConf.getError());
        }
      }
      else
      {
        _errors.append("'hub.conf' not configured.");
      }
      
      if (tpConf.exists())
      {
        _tpConf = new PartnerConfig(tpConf);
        if (!_tpConf.isValid())
        {
          _errors.append(_tpConf.getError());
        }
      }
      else if (!respondMode)
      {
        _errors.append("'tp.conf' not configured");
      }
      
      if (_errors.length()==0)
      {
        _valid = true;
      }
    }
    else
    {
      _errors.append("No such Test case id configured.");
    }
  }
  
  /**
   * 
   * @return <b>true</b> if the test case configurations are valid.
   */
  public boolean isTestCaseValid()
  {
    return _valid;
  }
  
  /**
   * 
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
      RnifServlet.removeTestCase(_testCaseId);
    }
  }

  /**
   * Sends an ACK for the received action msg.
   * @param actionContent The payload of received action msg.
   */
  public void sendAck(String actionContent)
  {
    //send ack asynchronously
    _timer = new Timer(_testCaseId, !_unitTest);
    _timer.schedule(new SendAckTask(actionContent), _hubConf.getAckSendDelay()*1000);
  }
  
  /**
   * Loads the properties file
   * @param fileName file name of the properties file.
   * @param testCaseId test case id for the test
   * @return returns the loaded file
   */
  public File loadFile(String fileName, String testCaseId)
  {	
	  
	  File baseDir = new File("conf", testCaseId);
	  File conFile = new File(baseDir, fileName);
	  return conFile;
	  
  }
  
  private byte[] generateAckMsg(String actionContent)
  {
    String pipInstId = findPipInstId(actionContent);
    String receiverDuns = findReceiverDuns(actionContent);
    String srcMsgTrackingId = findMsgTrackingId(actionContent);
    String initiatorDuns = findInitiatorDuns(actionContent);
    String preambleContentID = genContentID();
    String deliveryContentID = genContentID();
    String serviceHeaderContentID = genContentID();
    String serviceContentContentID = genContentID();
    String deliverTs = genTs();
    String senderDuns = _hubConf.getDuns();
    String msgTrackingID = "reply_"+srcMsgTrackingId;
   
    try
    {
      String template = getContent(_hubConf.getAckTemplateFile());
      
      String content = template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
      content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
      content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
      content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
      content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
      content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, receiverDuns);
      content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, senderDuns);
      content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
      content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
      content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstId);
      content = content.replaceAll(SRC_MSG_TRACKING_ID_PLACEHOLDER, srcMsgTrackingId);
      
      Properties ackProp = _hubConf.getAckProperties();
      Enumeration keys = ackProp.propertyNames();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        String val = ackProp.getProperty(key);
        content = content.replaceAll(key, val);
      }
      if (_unitTest)
      {
        logger.log(Level.INFO, "payload is "+content);
      }
      logger.log(Level.INFO, "payload string is "+content.length());
      return content.getBytes();
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, "Error reading action template "+_hubConf.getAckTemplate(), ex);
    }
    return null;
  }
  
  /**
   * Find the PIP instance id from the specified content
   * @param content The content to look into
   * @return The PIP instance id extracted.
   */
  public static String findPipInstId(String content)
  {
    String[] searchTokens = { "<ServiceHeader>", "</ServiceHeader>", 
        "<pipInstanceId>", "</pipInstanceId>",
        "<InstanceIdentifier>", "</InstanceIdentifier>" };
    
    return search(content, searchTokens);
  }

  /**
   * Find the PIP code from the specified content
   * @param content The content to look into
   * @return The PIP code extracted.
   */
  public static String findPipCode(String content)
  {
    String[] searchTokens = { "<ServiceHeader>", "</ServiceHeader>", 
        "<pipCode>", "</pipCode>",
        "<GlobalProcessIndicatorCode>", "</GlobalProcessIndicatorCode>" };
    
    return search(content, searchTokens);
  }

  /**
   * Find the receiver duns from the specified content.
   * @param content The content to look into.
   * @return The receiver duns extracted.
   */
  public static String findReceiverDuns(String content)
  {
    String[] searchTokens = {
        "<DeliveryHeader>", "</DeliveryHeader>",
        "<messageReceiverIdentification>", "</messageReceiverIdentification>",
        "<GlobalBusinessIdentifier>", "</GlobalBusinessIdentifier>",
    };
    return search(content, searchTokens);
  }
  public RnifHubConfig getHubConfig()
  {
	  return _hubConf;
  }
  public PartnerConfig getPartnerConfig()
  {
	  return _tpConf;
  }
  
  /**
   * Find the sender duns from the specified content.
   * @param content The content to look into.
   * @return The sender duns extracted.
   */
  public static String findSenderDuns(String content)
  {
    String[] searchTokens = {
        "<DeliveryHeader>", "</DeliveryHeader>",
        "<messageSenderIdentification>", "</messageSenderIdentification>",
        "<GlobalBusinessIdentifier>", "</GlobalBusinessIdentifier>",
    };
    return search(content, searchTokens);
  }

  /**
   * Find the msg tracking id from the specified content.
   * @param content The content to look into
   * @return The msg tracking id extracted.
   */
  private static String findMsgTrackingId(String content)
  {
    String[] searchTokens = {
        "<DeliveryHeader>", "</DeliveryHeader>",
        "<messageTrackingID>", "</messageTrackingID>",
        "<InstanceIdentifier>", "</InstanceIdentifier>",
    };
    return search(content, searchTokens);
  }

  /**
   * Find the initiator duns from the specified content
   * @param content The content to look into
   * @return The initiator duns extracted.
   */
  public static String findInitiatorDuns(String content)
  {
    String[] searchTokens = {
        "<ServiceHeader>", "</ServiceHeader>",
        "<KnownInitiatingPartner>", "</KnownInitiatingPartner>",
        "<GlobalBusinessIdentifier>", "</GlobalBusinessIdentifier>",
    };
    return search(content, searchTokens);
  }

  private static String search(String content, String[] searchTokens)
  {
    int start = 0;
    int end = -1;
    for (int i=0; i<searchTokens.length; i++)
    {
      int idx = content.indexOf(searchTokens[i], start);
      if (idx < 0)
      {
        return null; //not found
      }
      if (i % 2 == 0) // start token 
      {
        start = idx + searchTokens[i].length();
      }
      else //end token
      {
        end = idx;
      }
    }
    return content.substring(start, end);
  }

  /**
   * A Timer task to send ACK msg
   * @author i00107
   */
  class SendAckTask extends TimerTask
  {
    private String _actionContent;

    /**
     * 
     * @param actionContent The action msg to respond to
     */
    SendAckTask(String actionContent)
    {
      _actionContent = actionContent;
    }
    
    /**
     * @see java.util.TimerTask#run()
     */
    public void run()
    {
      byte[] msg = generateAckMsg(_actionContent);
      if (msg != null)
      {
        if (_unitTest)
        {
          logger.log(Level.INFO, "Sending ack msg");
        }
        else
        {
          new RnifHttpClient(_hubConf.getURL(), msg,
                         "multipart/related",
                         "RosettaNet/V02.00", "async");
        }
      }
    }
  }

  /**
   * A timer task to send action msgs
   * @author i00107
   */
  class SendTask extends TimerTask
  {
    private int _numTx;
    private List<Partner> _partners;
    private boolean _ended;
    private boolean[] _done;
    
    /**
     * 
     * @param partners List of partners to send to
     * @param numTx Number of msgs to send each time
     */
    SendTask(List<Partner> partners, int numTx)
    {
      _numTx = numTx;
      _partners = partners;
      _done = new boolean[partners.size()];
      Partner ptr;
      for (int i = 0 ; i < _partners.size() ; i++)
      {
    	  ptr = _partners.get(i);
    	  _totalNumTx = _totalNumTx + ptr.getNumTx();
    	  if (ptr.hasFinishedTx())
		  {
			  _done[i] = true;
		  }
      }
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
          int counter = 0;
          int i = 0;
          SendThread t = null;
          Partner ptr;
          for (i = _startingPoint ; _sendCount < _totalNumTx ; i++)
          {
        	  if (!_done[i])
        	  {
        		  ptr = _partners.get(i);
        		  t = new SendThread(ptr);
        		  t.start();
        		  ptr.incrementCounter();
        		  _sendCount++;
        		  if (ptr.hasFinishedTx())
        		  {
        			  _done[i] = true;
        		  }
        		  counter++;
    		  }
    		  
    		  if (counter == _numTx)
    		  {
    			  if (i == (_partners.size() - 1))
    			  {
    				  _startingPoint = 0;
    			  }
    			  else
    			  {
    				  _startingPoint = i + 1;
    			  }
    			  break;
    		  }
    		  if(i == (_partners.size() - 1))
    		  {
    			  i = -1;
    		  }	  
    		  
          }
       }
          
          if (_sendCount == _totalNumTx)
          {
            logger.log(Level.INFO, "Finished sending all tx to all partners");
            stop();
            logger.log(Level.INFO, "Test case "+_testCaseId + " finished at "+new Date());
          }
        }
      
      else
      {
        logger.log(Level.INFO, "Send timer has been cancelled.");
      }
    }
  }
  
  private final static String CONTENT_ID_PATTERN = "<{0}.{1}.GridNode.{2}@{3}>";
  private final static String PREAMPLE_CID_PLACEHOLDER = "###_PREAMBLE_CID_###";
  private final static String DELIVERY_CID_PLACEHOLDER = "###_DELIVERY_CID_###";
  private final static String DELIVERY_TS_PLACEHOLDER = "###_DELIVERY_TS_###";
  private final static String RECEIVER_DUNS_PLACEHOLDER = "###_RECEIVER_DUNS_###";
  private final static String SENDER_DUNS_PLACEHOLDER = "###_SENDER_DUNS_###";
  private final static String MSG_TRACKING_ID_PLACEHOLDER = "###_MSG_TRACKING_ID_###";
  private final static String SERVICEH_CID_PLACEHOLDER = "###_SERVICEH_CID_###";
  private final static String PIP_INST_ID_PLACEHOLDER = "###_PIP_INST_ID_###";
  private final static String SERVICEC_CID_PLACEHOLDER = "###_SERVICEC_CID_###";
  private final static String DOC_GEN_TS_PLACEHOLDER = "###_DOC_GEN_TS_###";
  private final static String DOC_ID_PLACEHOLDER = "###_DOC_ID_###";
  private final static String INITIATOR_DUNS_PLACEHOLDER = "###_INITIATOR_DUNS_###";
  private final static String SRC_MSG_TRACKING_ID_PLACEHOLDER = "###_SRC_MSG_TRACKING_ID_###";
  
  private Random _random = new Random();

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

  private String genContentID()
  {
    String param0 = String.valueOf(_random.nextInt(Integer.MAX_VALUE));
    String param1 = String.valueOf(System.currentTimeMillis());
    String param2 = System.getProperty("user.name");
    String param3;
    try
    {
      param3 = InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException ex)
    {
      param3 = "unknown.host";
    }
    return MessageFormat.format(CONTENT_ID_PATTERN, param0, param1, param2, param3);
  }
  
  private String genTs()
  {
    //SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd'T'HHmmss'.'SSS'Z'");
    //Date   currentTime_1 = new Date();
    //return formatter.format(currentTime_1);
    
    return Util.genTS();
  }

  /**
   * A thread to send an action msg for a Partner
   * @author i00107
   *
   */
  class SendThread extends Thread
  {
    private Partner _partner;
    private int _txCounter;
    
    /**
     * 
     * @param p The Partner to send for
     */
    public SendThread(Partner p)
    {
      _partner = p;
      _txCounter = p.getCounter();
    }
    
    /**
     * 
     * @see java.lang.Thread#run()
     */
    public void run()
    {
      byte[] msg = generateActionMsg();
      if (msg != null)
      {
        if (_unitTest)
        {
          logger.log(Level.INFO, "Sending msg " + _txCounter + " for partner "+_partner.getId());
        }
        else
        {
          new RnifHttpClient(_hubConf.getURL(), msg,
                         "multipart/related",
                         "RosettaNet/V02.00", "async");
        }
      }
    }
    
    //public 
    private byte[] generateActionMsg()
    {
      String preambleContentID = genContentID();
      String deliveryContentID = genContentID();
      String serviceHeaderContentID = genContentID();
      String serviceContentContentID = genContentID();
      String deliverTs = genTs();
      String senderDuns = _partner.getId();
      String receiverDuns = _hubConf.getDuns();
      String initiatorDuns = senderDuns;
      String msgTrackingID = String.valueOf(_txCounter);
      String pipInstID = UUID.randomUUID().toString();//genTs();
      String docGenTs = genTs();
      String docID = UUID.randomUUID().toString();
      
      try
      {
        String template = getContent(_hubConf.getActionTemplateFile());
        
        String content = template.replaceAll(PREAMPLE_CID_PLACEHOLDER, preambleContentID);
        content = content.replaceAll(DELIVERY_CID_PLACEHOLDER, deliveryContentID);
        content = content.replaceAll(SERVICEH_CID_PLACEHOLDER, serviceHeaderContentID);
        content = content.replaceAll(SERVICEC_CID_PLACEHOLDER, serviceContentContentID);
        content = content.replaceAll(DELIVERY_TS_PLACEHOLDER, deliverTs);
        content = content.replaceAll(SENDER_DUNS_PLACEHOLDER, senderDuns);
        content = content.replaceAll(RECEIVER_DUNS_PLACEHOLDER, receiverDuns);
        content = content.replaceAll(INITIATOR_DUNS_PLACEHOLDER, initiatorDuns);
        content = content.replaceAll(MSG_TRACKING_ID_PLACEHOLDER, msgTrackingID);
        content = content.replaceAll(PIP_INST_ID_PLACEHOLDER, pipInstID);
        content = content.replaceAll(DOC_GEN_TS_PLACEHOLDER, docGenTs);
        content = content.replaceAll(DOC_ID_PLACEHOLDER, docID);
        
        Properties actionProp = _hubConf.getActionProperties();
        Enumeration keys = actionProp.propertyNames();
        while (keys.hasMoreElements())
        {
          String key = (String)keys.nextElement();
          String val = actionProp.getProperty(key);
          content = content.replaceAll(key, val);
        }
        if (_unitTest)
        {
          logger.log(Level.INFO, "payload is "+content);
        }
        return content.getBytes();
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, "Error reading action template "+_hubConf.getActionTemplate(), ex);
      }
      return null;
    }
    

  }
  
  public static void main(String[] args)
  {
    RnifTestCaseRunner runner = new RnifTestCaseRunner("case3c3", true);
    if (runner.isTestCaseValid())
    {/*
      try
      {
        runner.run();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }*/
      
      try
      {
        File actionMsg = new File("3C3_invoicenotfn.xml");
        FileInputStream is = new FileInputStream(actionMsg);
        byte[] buff = new byte[1024];
        int readLen = -1;
        StringBuffer content = new StringBuffer();
        while ((readLen=is.read(buff))>0)
        {
          content.append(new String(buff, 0, readLen));
        }    
        System.out.println("received action msg");
        runner.sendAck(content.toString());
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
  
  public String toString()
  {
	  return "Test Case:" + _testCaseId + " isValid:" + _valid; 
  }
}
