/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveTransactionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 27, 2006   i00107              Created
 * Jan 05 2007    i00107              Add MessageID for IN tx. 
 */

package com.gridnode.gridtalk.httpbc.ishb.listeners;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.UUID;

import com.gridnode.gridtalk.httpbc.common.model.FileContent;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;

import junit.framework.TestCase;

/**
 * @author i00107
 *
 */
public class ReceiveTransactionTest extends TestCase
{
  private JmsSender _inSender = new JmsSender();
  private JmsSender _outSender = new JmsSender();

  public ReceiveTransactionTest(String name)
  {
    super(name);
  }

  /**
   * @throws java.lang.Exception
   */
  protected void setUp() throws Exception
  {
    JndiFinder jndi = new JndiFinder(loadJndiProps("intx"));
    jndi.setJndiSuffix("_AT");
    
    _inSender.setSendProperties(loadJndiProps("intx"));
    
    jndi = new JndiFinder(loadJndiProps("outtx"));
    jndi.setJndiSuffix("_AT");
    
    _outSender.setSendProperties(loadJndiProps("outtx"));
  }
  
  private Properties loadJndiProps(String type) throws Exception
  {
    Properties props = new Properties();
    props.load(new FileInputStream("data/jms_"+type+".properties"));
    return props;
  }
  
  /**
   * @throws java.lang.Exception
   */
  protected void tearDown() throws Exception
  {
  }

  public synchronized void testMultipleSend()
  {
    class RunSend implements Runnable
    {
      public void run()
      {
        sendDirectionIn();
        //sendDirectionOut();
      }
    };
    
    Thread[] ts = new Thread[1];
    for (int i=0; i<ts.length; i++)
    {
      ts[i] = new Thread(new RunSend());
    }
    for (Thread t : ts)
    {
      t.start();
      
    }
    
    for (Thread t : ts)
    {
      try
      {
        t.join();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }
  
  public void sendDirectionIn()
  {
    TransactionDoc tDoc = new TransactionDoc();
    tDoc.setBizEntId("InBizEntId");
    tDoc.setDirection(TransactionDoc.DIRECTION_IN);
    tDoc.setDocType("InDocType");
    tDoc.setPartnerId("InPartnerId");
    tDoc.setTimestamp(System.currentTimeMillis());
    tDoc.setTracingId(UUID.randomUUID().toString());
    
    FileContent fc = new FileContent();
    fc.setFilename("InDocFilename.txt");
    fc.setContent(new String("This is just a simple test of sending an incoming transaction to HTTPBC for handling.").getBytes());
    tDoc.setDocContent(fc);
    tDoc.setMessageID("IB-134");

    try
    {
      _inSender.send(tDoc);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      assertTrue("Unable to send via JMS", false);
    }
  }
  
  public void sendDirectionOut()
  {
    TransactionDoc tDoc = new TransactionDoc();
    tDoc.setBizEntId("OutBizEntId");
    tDoc.setDirection(TransactionDoc.DIRECTION_OUT);
    tDoc.setDocType("OutDocType");
    tDoc.setPartnerId("OutPartnerId");
    tDoc.setTimestamp(System.currentTimeMillis());
    tDoc.setTracingId(UUID.randomUUID().toString());
    
    FileContent fc = new FileContent();
    fc.setFilename("OutDocFilename.txt");
    fc.setContent(new String("This is just a simple test of sending an outgoing transaction to HTTPBC for handling.").getBytes());
    tDoc.setDocContent(fc);

    try
    {
      _outSender.send(tDoc);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      assertTrue("Unable to send via JMS", false);
    }
    
  }
  
  public static void main(String[] args) throws Exception
  {
    ReceiveTransactionTest test = new ReceiveTransactionTest("");
    test.setUp();
    
    int i = 30;
    for(int j = 0; j < i ; j++)
    {
      System.out.println("Start sending....");
      test.testMultipleSend();
    }
  }
}
